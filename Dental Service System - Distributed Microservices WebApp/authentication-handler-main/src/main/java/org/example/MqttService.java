package org.example;

import com.google.gson.Gson;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttTopic;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Optional;

@Service
public class MqttService {

    // mqtt credentials, will change to main cluster for testing before deployment
    final String host = "ca4ef47f00e54a4c84672a25e453461e.s2.eu.hivemq.cloud";
    final String username = "dentistservicesystem";
    final String password = "group17Project";
    private Mqtt5BlockingClient client;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        client = MqttClient.builder()
                .useMqttVersion5()
                .serverHost(host)
                .serverPort(8883)
                .sslWithDefaultConfig()
                .buildBlocking();

        client.connectWith()
                .simpleAuth()
                .username(username)
                .password(UTF_8.encode(password))
                .applySimpleAuth()
                .send();

        System.out.println("Connected successfully");

        client.subscribeWith()
                        .topicFilter("/patient/check-authentication")
                                .send();

        client.subscribeWith()
                        .topicFilter("/patient/registration-request")
                                .send();

        client.toAsync().publishes(ALL, publish -> {
            MqttTopic receivedTopic = publish.getTopic();
            String message = UTF_8.decode(publish.getPayload().get()).toString();

            if ("/patient/check-authentication".equals(receivedTopic.toString())) {
                try {
                    // parses incoming Json object
                    JsonObject json = JsonParser.parseString(message).getAsJsonObject();
                    String email = json.get("email").getAsString();
                    String password = json.get("password").getAsString();
                    String patientId = getPatientIdByEmail(email);

                    // extracts the correlation ID from the incoming message
                    String correlationID = json.has("correlationID") ? json.get("correlationID").getAsString() : "";

                    // compiles response Json object
                    JsonObject response = responseGenerator(email, password);

                    // Add the correlation ID to the response
                    response.addProperty("correlationID", correlationID);
                    response.addProperty("patientId", patientId);

                    Gson gson = new Gson();
                    String responseMessage = gson.toJson(response);

                    System.out.println("Sending response to /patient/authentication-response -> " + responseMessage);

                    client.publishWith()
                            .topic("/patient/authentication-response")
                            .payload(UTF_8.encode(responseMessage))
                            .send();
                } catch (Exception e) {
                    System.err.println("Error processing message: " + e.getMessage());
                    e.printStackTrace();
                }
            } else if ("/patient/registration-request".equals(receivedTopic.toString())) {
                try {
                    JsonObject json = JsonParser.parseString(message).getAsJsonObject();
                    patient patient = new patient();
                    patient.setEmail(json.get("email").getAsString());
                    patient.setName(json.get("name").getAsString());
                    patient.setPassword(json.get("password").getAsString());

                    // extracts the correlation ID from the incoming message
                    String correlationID = json.has("correlationID") ? json.get("correlationID").getAsString() : "";

                    PatientController patientController = context.getBean(PatientController.class);
                    ResponseEntity<?> response = patientController.registerPatient(patient);

                    // constructs the response object
                    JsonObject responseJson = new JsonObject();
                    responseJson.addProperty("correlationID", correlationID);
                    responseJson.addProperty("statusCode", response.getStatusCodeValue());

                    if (response.getStatusCode() == HttpStatus.OK && response.hasBody()) {
                        patient registeredPatient = (patient) response.getBody();
                        JsonObject patientData = new JsonObject();
                        patientData.addProperty("email", registeredPatient.getEmail());
                        patientData.addProperty("name", registeredPatient.getName());
                        // excludes password for security reasons, no reason to send the plain text password back to the client
                        responseJson.add("data", patientData);
                    } else if (response.hasBody()) {
                        responseJson.addProperty("message", response.getBody().toString());
                    } else {
                        responseJson.addProperty("message", "Unknown error occurred");
                    }

                    // sends the response
                    String responseMessage = responseJson.toString();
                    client.publishWith()
                            .topic("/patient/registration-response")
                            .payload(UTF_8.encode(responseMessage))
                            .send();
                } catch (Exception e) {
                    System.err.println("Error processing message: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
    public String getPatientIdByEmail(String email) {
        Optional<patient> patient = patientRepository.findByEmail(email);
        if(!patient.isPresent()) {
            return "not found";
        } else {
            return patient.get().getId();
        }
    }
    private JsonObject responseGenerator(String email, String password) {
        JsonObject responseJson = new JsonObject();

        // if the email does not exist in the database, return false
        Optional<patient> patientOpt = patientRepository.findByEmail(email);
        if (!patientOpt.isPresent()) {
            responseJson.addProperty("message", "email does not exist");
            responseJson.addProperty("authenticated", false);
            return responseJson;
        }

        // if password is incorrect, return false
        patient patient = patientOpt.get();
        if (!passwordEncoder.matches(password, patient.getPassword())) {
            responseJson.addProperty("message", "password is incorrect");
            responseJson.addProperty("authenticated", false);
            return responseJson;
        }

        // user successfully authenticated
        responseJson.addProperty("message", "authenticated successfully");
        responseJson.addProperty("authenticated", true);
        return responseJson;
    }
}
