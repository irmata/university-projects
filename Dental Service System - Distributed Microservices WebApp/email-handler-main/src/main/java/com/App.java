package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public class App {
    public static void main(String[] args) throws IOException {
        // Disable logging
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb").setLevel(Level.OFF);
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("io.netty").setLevel(Level.OFF);

        EmailHandler emailHandler = new EmailHandler();
        String brokerUrl = "ca4ef47f00e54a4c84672a25e453461e.s2.eu.hivemq.cloud";
        String bookingRegisterTopic = "/email-register";
        String bookingCancelTopic = "/email-delete";
        String mqttUsername = "dentistservicesystem";
        String mqttPassword = "group17Project";
        String databaseUri = "mongodb+srv://danielvdh24:mongodb@cluster0.kcicm76.mongodb.net/";
        emailHandler.setupEmailHandler(brokerUrl, bookingRegisterTopic, bookingCancelTopic, mqttUsername, mqttPassword, databaseUri);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Email Handler is running. Type 'exit' to stop.");
        System.out.println("-----------------------------------------------");

        while (true) {
            String input = reader.readLine();
            if ("exit".equalsIgnoreCase(input)) {
                break;
            }
        }

        System.out.println("-----------------------------------------------");
        emailHandler.disconnect();
        System.out.println("Email Handler stopped.");
    }
}
