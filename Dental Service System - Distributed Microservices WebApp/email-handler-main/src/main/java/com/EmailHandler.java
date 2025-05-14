package com;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;

public class EmailHandler {

    private Mqtt5BlockingClient client;
    private DatabaseClient database;
    private final String emailUsername = "dentistsystem.noreply@gmail.com";
    private final String emailPassword = "kgig wxxa wyaw hlha";

    public void setupEmailHandler(String brokerUrl, String bookingRegisterTopic, String bookingCancelTopic, String mqttUsername, String mqttPassword, String databaseUri) {
        // Connect to MQTT Broker
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

        System.out.println("Successfully connected to MQTT broker.");

        client.subscribeWith()
                .topicFilter(bookingRegisterTopic)
                .send();

        client.subscribeWith()
                .topicFilter(bookingCancelTopic)
                .send();

        client.toAsync().publishes(ALL, publish -> {
            String topic = publish.getTopic().toString();
            System.out.println("Received message on topic: " + topic);
        
            Optional<ByteBuffer> payload = publish.getPayload();
            if (payload.isPresent()) {
                String messageData = StandardCharsets.UTF_8.decode(payload.get()).toString();
                try {
                    JsonElement parsedElement = JsonParser.parseString(messageData);
                    if (parsedElement.isJsonObject()) {
                        if (topic.equals(bookingRegisterTopic)) {
                            sendConfirmationEmail(messageData);
                        } else if (topic.equals(bookingCancelTopic)) {
                            sendCancellationEmail(messageData);
                        }
                    } else {
                        System.out.println("ERROR: Payload is not a valid JSON object");
                    }
                } catch (JsonSyntaxException e) {
                    System.out.println("ERROR: Payload is not in JSON format");
                }
            } else {
                System.out.println("ERROR: Received empty payload");
            }
        });

        // Connect to Database
        this.database = new DatabaseClient(databaseUri);
    }

    private void sendEmail(String patientEmail, String dentistEmail, String subject, String patientHtmlContent, String dentistHtmlContent) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailUsername, emailPassword);
                    }
                });
    
        try {
            // Send email to patient
            Message patientMessage = new MimeMessage(session);
            patientMessage.setFrom(new InternetAddress(emailUsername));
            patientMessage.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(patientEmail)
            );
            patientMessage.setSubject(subject);
            patientMessage.setContent(patientHtmlContent, "text/html");
    
            Transport.send(patientMessage);
            System.out.println("Email successfully sent to patient: " + patientEmail);
    
            // Send email to dentist
            Message dentistMessage = new MimeMessage(session);
            dentistMessage.setFrom(new InternetAddress(emailUsername));
            dentistMessage.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(dentistEmail)
            );
            dentistMessage.setSubject(subject);
            dentistMessage.setContent(dentistHtmlContent, "text/html");
    
            Transport.send(dentistMessage);
            System.out.println("Email successfully sent to dentist: " + dentistEmail);
        } catch (MessagingException e) {
            System.out.println("There was an error dispatching the email: " + e.getMessage());
        }
    }

    private void sendConfirmationEmail(String messageData) {
        JsonObject json = JsonParser.parseString(messageData).getAsJsonObject();
        String appointmentId = json.get("id").getAsString();
        String appointmentData = database.getAppointmentData(appointmentId);
        if(appointmentData == null){
            System.out.println("ERROR: Appointment ID invalid or not found");
            return;
        }
        
        String[] appointmentValues = appointmentData.split(",");
        String patientId = appointmentValues[0];
        String dentistId = appointmentValues[1];
        String timeSlot = appointmentValues[2];
        String date = appointmentValues[3];
        String clinicId = appointmentValues[4];
        
        String userData = database.getUserData(patientId, dentistId);
        if(userData == null){
            System.out.println("ERROR: User ID invalid or not found");
            return;
        }
        
        String[] userValues = userData.split(",");
        String patientEmail = userValues[0];
        String dentistEmail = userValues[1];
        String patientName = userValues[2];
        String dentistName = userValues[3];

        String clinicName = database.getClinicName(clinicId);
        if(clinicName == null){
            System.out.println("ERROR: Clinic name not found");
            return;
        }
        
        String subject = "Your Appointment Details";
        String patientHtmlContent = "<p>Dear <b>" + patientName + "</b>,</p>"
                + "<p>Your appointment for <b>" + timeSlot + "</b> on <b>" + date + "</b> has been confirmed.</p>"
                + "<p>Location: <b>" + clinicName + "</b></p>"
                + "<hr>"
                + "<p><i>Thank you for choosing us for your dental care.</i></p>";

        String dentistHtmlContent = "<p>Dear Dr. <b>" + dentistName + "</b>,</p>"
                + "<p>You have a new appointment with <b>" + patientName + "</b> for <b>" + timeSlot + "</b> on <b>" + date + "</b>.</p>"
                + "<hr>"
                + "<p><i>Thank you for providing dental care to our patients.</i></p>";

        sendEmail(patientEmail, dentistEmail, subject, patientHtmlContent, dentistHtmlContent);
    }

    private void sendCancellationEmail(String messageData) {
        JsonObject json = JsonParser.parseString(messageData).getAsJsonObject();
        
        String dentistId = json.get("dentistId").getAsString();
        String patientId = json.get("patientId").getAsString();
        String clinicId = json.get("dentalClinicId").getAsString();
        String timeSlot = json.get("time").getAsString();
        String date = json.get("date").getAsString();

        String userData = database.getUserData(patientId, dentistId);
        if(userData == null){
            System.out.println("ERROR: User ID invalid or not found");
            return;
        }
        
        String[] userValues = userData.split(",");
        String patientEmail = userValues[0];
        String dentistEmail = userValues[1];
        String patientName = userValues[2];
        String dentistName = userValues[3];

        String clinicName = database.getClinicName(clinicId);
        if(clinicName == null){
            System.out.println("ERROR: Clinic name not found");
            return;
        }
        
        String subject = "Cancellation Notice";
        String patientHtmlContent = "<p>Dear <b>" + patientName + "</b>,</p>"
                + "<p>Your appointment for <b>" + timeSlot + "</b> on <b>" + date + "</b> has been canceled.</p>"
                + "<p>Location: <b>" + clinicName + "</b></p>"
                + "<hr>"
                + "<p><i>We apologize for any inconvenience caused.</i></p>";

        String dentistHtmlContent = "<p>Dear Dr. <b>" + dentistName + "</b>,</p>"
                + "<p>The appointment with <b>" + patientName + "</b> for <b>" + timeSlot + "</b> on <b>" + date + "</b> has been canceled.</p>"
                + "<hr>"
                + "<p><i>Please update your schedule accordingly.</i></p>";

        sendEmail(patientEmail, dentistEmail, subject, patientHtmlContent, dentistHtmlContent);
    }

    public void disconnect() {
        try {
            if (client != null) {
                client.disconnect();
                System.out.println("Disconnected from MQTT broker.");
            }
            database.close();
        } catch (Exception e) {
            System.out.println("Error during disconnection: \n");
            e.printStackTrace();
        }
    }
}
