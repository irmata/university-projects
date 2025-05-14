package com.yourgroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

public class App {
    public static void main(String[] args) throws IOException {
        // Disable logging
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb").setLevel(Level.OFF);
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("io.netty").setLevel(Level.OFF);

        Properties mqttProps = new Properties();
        try (InputStream input = App.class.getClassLoader().getResourceAsStream("mqttConfig.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find mqttConfig.properties");
                return;
            }
            mqttProps.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        BookingHandler bookingHandler = new BookingHandler();
        String brokerUrl = mqttProps.getProperty("mqtt.url");
        String createTopic = "/booking-checked"; // Topic for creating bookings
        String deleteTopic = "/cancellation"; // Topic for deleting bookings
        String mqttUsername = mqttProps.getProperty("mqtt.username");
        String mqttPassword = mqttProps.getProperty("mqtt.password");
        String databaseUri = "mongodb+srv://nish:qwerty1234@cluster0.kcicm76.mongodb.net/Dentist-Service-System?retryWrites=true&w=majority";
        bookingHandler.setupBookingHandler(brokerUrl, createTopic, deleteTopic, mqttUsername, mqttPassword, databaseUri);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Booking Handler is now running. Type 'exit' to stop.");
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");

        while (true) {
            String input = reader.readLine();
            if ("exit".equalsIgnoreCase(input)) {
                break;
            }
        }

        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
        bookingHandler.disconnect();
        System.out.println("Booking Handler stopped.");
    }
}
