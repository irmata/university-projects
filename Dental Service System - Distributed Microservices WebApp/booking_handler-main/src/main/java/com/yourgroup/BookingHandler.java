package com.yourgroup;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;

public class BookingHandler {

    private Mqtt5BlockingClient client;
    private DatabaseClient database;
    private Gson gson;

    public BookingHandler() {
        JsonDeserializer<ObjectId> deserializer = (json, typeOfT, context) -> new ObjectId(json.getAsString());
        JsonSerializer<ObjectId> serializer = (src, typeOfSrc, context) -> new JsonPrimitive(src.toHexString());
        gson = new GsonBuilder()
                .registerTypeAdapter(ObjectId.class, serializer)
                .registerTypeAdapter(ObjectId.class, deserializer)
                .create();
    }

    public void setupBookingHandler(String brokerUrl, String createTopic, String deleteTopic, String mqttUsername, String mqttPassword, String databaseUri) {
        client = MqttClient.builder()
                .useMqttVersion5()
                .serverHost(brokerUrl)
                .serverPort(8883)
                .sslWithDefaultConfig()
                .buildBlocking();

        client.connectWith()
                .simpleAuth()
                .username(mqttUsername)
                .password(StandardCharsets.UTF_8.encode(mqttPassword))
                .applySimpleAuth()
                .send();

        System.out.println("Successfully connected to MQTT broker");

        // Subscribe to the topic for creating bookings
        client.subscribeWith()
                .topicFilter(createTopic)
                .send();

        // Subscribe to the topic for deleting bookings
        client.subscribeWith()
                .topicFilter(deleteTopic)
                .send();

        // subscribes to the topic for getting patient bookings
        client.subscribeWith()
                .topicFilter("/get-appointments-patient")
                .send();

        client.subscribeWith()
                .topicFilter("/get-appointments-dentist")
                .send();

        client.toAsync().publishes(ALL, publish -> {
            String topic = publish.getTopic().toString();
            String messageData = StandardCharsets.UTF_8.decode(publish.getPayload().get()).toString();

            if (topic.equals("/booking-checked")) {
                System.out.println("Received create booking message on topic: " + topic);
                JsonObject responseJson = handleBookingCheck(messageData);
                if (responseJson != null && responseJson.get("statusCode").getAsInt() == 200) {
                    try {
                        ObjectId bookingId = addBooking(messageData);
                        System.out.println("Created booking with ID: " + bookingId.toHexString());
                        publishBookingId(bookingId, "/email-register");
                        // Publish response after successful booking
                        publishResponse(responseJson, "/booking-response");
                    } catch (Exception e) {
                        // Handle and publish error response if booking saving fails
                        JsonObject errorResponse = new JsonObject();
                        errorResponse.addProperty("statusCode", 500);
                        errorResponse.addProperty("message", "Error saving booking: " + e.getMessage());
                        publishResponse(errorResponse, "/booking-response");
                    }
                }
            } else if (topic.equals(deleteTopic)) {
                System.out.println("Received delete booking message on topic: " + topic);
                deleteBooking(messageData);

            } else if (topic.equals("/get-appointments-patient")) {
                System.out.println("Received get appointments request for patient on topic: " + topic);
                handleGetAppointmentsForPatient(messageData);
            } else if (topic.equals("/get-appointments-dentist")) {
                System.out.println("Received get appointments request for dentist on topic: " + topic);
                handleGetAppointmentsForDentist(messageData);
            }

            System.out.println("-----------------------------------------------");
        });

        this.database = new DatabaseClient(databaseUri);
    }

    private JsonObject handleBookingCheck(String messageData) {
        JsonObject receivedJson = gson.fromJson(messageData, JsonObject.class);
        int statusCode = receivedJson.has("statusCode") ? receivedJson.get("statusCode").getAsInt() : 500;
        JsonObject responseJson = new JsonObject();

        if (statusCode == 200) {
            try {
                Booking booking = gson.fromJson(messageData, Booking.class);
                String dentistName = database.getDentistName(booking.getDentistId());
                String patientName = database.getPatientName(booking.getPatientId());
                String clinicName = database.getClinicName(booking.getDentalClinicId());

                responseJson.addProperty("date", booking.getDate());
                responseJson.addProperty("dentistName", dentistName);
                responseJson.addProperty("patientName", patientName);
                responseJson.addProperty("dentalClinicName", clinicName);
                responseJson.addProperty("time", booking.getTime());
                responseJson.addProperty("statusCode", 200);
                responseJson.addProperty("message", receivedJson.get("message").getAsString());
                responseJson.addProperty("correlationID", receivedJson.get("correlationID").getAsString());
            } catch (Exception e) {
                responseJson.addProperty("statusCode", 500);
                responseJson.addProperty("message", "Error: " + e.getMessage());
            }
        } else {
            responseJson = receivedJson; // Use the received JSON as is for non-200 status codes
            publishResponse(responseJson, "/booking-response");
            System.out.println("Status code 500 request sent through the /booking-response");
        }
        return responseJson;
    }

    private ObjectId addBooking(String messageData) {
        Booking booking = gson.fromJson(messageData, Booking.class);
        return database.addBooking(booking);
    }

    private void publishBookingId(ObjectId bookingId, String topic) {
        String jsonMessage = gson.toJson(new BookingId(bookingId.toHexString()));

        Mqtt5Publish publishMessage = Mqtt5Publish.builder()
                .topic(topic)
                .payload(StandardCharsets.UTF_8.encode(jsonMessage))
                .build();

        client.publish(publishMessage);
        System.out.println("Published booking ID: " + bookingId.toHexString() + " to topic: " + topic);
    }
    private void publishResponse(JsonObject responseJson, String topic) {
        String jsonMessage = gson.toJson(responseJson);

        Mqtt5Publish publishMessage = Mqtt5Publish.builder()
                .topic(topic)
                .payload(StandardCharsets.UTF_8.encode(jsonMessage))
                .build();

        client.publish(publishMessage);
        System.out.println("Published response to topic: " + topic);
    }

    private void deleteBooking(String messageData) {
        try {
            DeleteRequest deleteRequest = gson.fromJson(messageData, DeleteRequest.class);
            Document booking = database.deleteBooking(deleteRequest.getDeleteId());
            JsonObject responseJson = new JsonObject();

            if (booking != null) {
                // If the booking was successfully deleted
                responseJson.addProperty("statusCode", 200);
                responseJson.addProperty("message", "Deletion Completed");

                // Correctly retrieve the message and status code from the responseJson object
                String message = responseJson.get("message").getAsString();
                int statusCode = responseJson.get("statusCode").getAsInt();

                publishCancellationResponse(booking, message, statusCode, "/cancellation-complete");

                // Convert the Document into a JSON and publish it to the /email-delete topic
                publishDeletedBooking(booking, "/email-delete");

            } else {
                // If the booking was not successfully deleted
                responseJson.addProperty("statusCode", 500);
                responseJson.addProperty("message", "Deletion failed at the booking Handler");

                // Correctly retrieve the message and status code from the responseJson object
                String message = responseJson.get("message").getAsString();
                int statusCode = responseJson.get("statusCode").getAsInt();

                publishCancellationResponse(booking, message, statusCode, "/cancellation-complete");
            }

        } catch (Exception e) {
            // System.out.println("Error processing delete request: " + e.getMessage());
            e.printStackTrace();
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("statusCode", 500);
            errorResponse.addProperty("message", "Deletion failed at the booking Handler");
            // Convert the JsonObject to a string before publishing
            publishRawCancellationResponse(gson.toJson(errorResponse), "/cancellation-completed");
        }
    }

    private void publishDeletedBooking(Document booking, String topic) {
        // Convert the Document into a JSON object
        JsonObject bookingJson = new JsonObject();
        for (String key : booking.keySet()) {
            bookingJson.addProperty(key, booking.get(key).toString());
        }

        // Publish the detailed booking JSON to the /email-delete topic
        String jsonMessage = gson.toJson(bookingJson);
        Mqtt5Publish publishMessage = Mqtt5Publish.builder()
                .topic(topic)
                .payload(StandardCharsets.UTF_8.encode(jsonMessage))
                .build();

        client.publish(publishMessage);
        System.out.println("Published deleted booking to topic: " + topic);
    }
    private void publishCancellationResponse(Document booking, String message, int statusCode, String topic) {
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("patientName", database.getPatientName(booking.getObjectId("patientId")));
        responseJson.addProperty("dentistName", database.getDentistName(booking.getObjectId("dentistId")));
        responseJson.addProperty("dentalClinicName", database.getClinicName(booking.getObjectId("dentalClinicId")));
        responseJson.addProperty("date", booking.getString("date"));
        responseJson.addProperty("time", booking.getString("time"));
        responseJson.addProperty("statusCode", statusCode);
        responseJson.addProperty("message", message);

        String jsonMessage = gson.toJson(responseJson);
        Mqtt5Publish publishMessage = Mqtt5Publish.builder()
                .topic("/cancellation-complete")
                .payload(StandardCharsets.UTF_8.encode(jsonMessage))
                .build();

        client.publish(publishMessage);
        System.out.println("Published cancellation response to topic: " + topic);
    }

    private void publishRawCancellationResponse(String messageData, String topic) {
        Mqtt5Publish publishMessage = Mqtt5Publish.builder()
                .topic(topic)
                .payload(StandardCharsets.UTF_8.encode(messageData))
                .build();

        client.publish(publishMessage);
        System.out.println("Published raw cancellation response to topic: " + topic);
    }

    private void handleGetAppointmentsForPatient(String messageData) {
        try {
            PatientIdRequest request = gson.fromJson(messageData, PatientIdRequest.class);
            List<Booking> appointments = database.getAppointmentsForPatient(new ObjectId(request.getPatientId()));
            List<PatientBooking> patientBookings = appointments.stream()
                    .map(booking -> {
                        String dentistName = database.getDentistName(booking.getDentistId());
                        String clinicName = database.getClinicName(booking.getDentalClinicId());
                        return new PatientBooking(booking, dentistName, clinicName);
                    })
                    .collect(Collectors.toList());
            publishAppointments(patientBookings, "/get-appointments-patient-response");
        } catch (Exception e) {
            System.out.println("Error processing get appointments request: " + e.getMessage());
        }
    }

    private void handleGetAppointmentsForDentist(String messageData) {
        try {
            DentistIdRequest request = gson.fromJson(messageData, DentistIdRequest.class);
            List<Booking> appointments = database.getAppointmentsForDentist(new ObjectId(request.getDentistId()));
            List<DentistBooking> dentistBookings = appointments.stream()
                    .map(booking -> {
                        String patientName = database.getPatientName(booking.getPatientId());
                        String clinicName = database.getClinicName(booking.getDentalClinicId());
                        return new DentistBooking(booking, patientName, clinicName);
                    })
                    .collect(Collectors.toList());
            publishAppointments(dentistBookings, "/get-appointments-dentist-response");
        } catch (Exception e) {
            System.out.println("Error processing get appointments request for dentist: " + e.getMessage());
        }
    }

    private void publishAppointments(List<? extends Booking> appointments, String topic) {
        String jsonMessage = gson.toJson(appointments);

        Mqtt5Publish publishMessage = Mqtt5Publish.builder()
                .topic(topic)
                .payload(StandardCharsets.UTF_8.encode(jsonMessage))
                .build();

        client.publish(publishMessage);
        System.out.println("Published appointments for patient to topic: " + topic);
    }


    public void disconnect() {
        try {
            if (client != null) {
                client.disconnect();
                System.out.println("Disconnected from MQTT broker.");
            }
            if (database != null) {
                database.close();
            }
        } catch (Exception e) {
            System.out.println("Error during disconnection: \n");
            e.printStackTrace();
        }
    }
    private static class PatientIdRequest {
        private String patientId;

        public String getPatientId() {
            return patientId;
        }
    }

    private static class DentistIdRequest {
        private String dentistId;

        public String getDentistId() {
            return dentistId;
        }
    }

    private static class BookingId {
        private final String id;

        public BookingId(String id) {
            this.id = id;
        }
    }
    private static class DeleteRequest {
        private String id;
        private int statusCode;
        private String message;
        public String getDeleteId() {
            return id;
        }
        public int getStatusCode() {
            return statusCode;
        }
        public String getMessage() {
            return message;
        }
    }
}