# Booking Handler for Distributed System Project

## Overview
This Booking Handler is a part of the [Dentist System](https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-17/dental_server), designed to work within a distributed system architecture. The component is responsible for handling booking and cancellation requests for the appointments.


## Features
* Event-Driven: Listens to events/messages from an MQTT broker.
* Database Integration: Stores and Deletes the appointments in the Mongo Database.
* Booking Processing: Processes the booking requests and sends out appropriate responses to different topics.

## Topics
* /booking-checked : Receives payload for starting the booking process.
* /booking-response : Sends payload to the frontend as a response to the booking request.
* /email-register : Sends payload to the email_handler in the event of a successful booking request.
* /cancellation : Receives payload for starting the cancellation process of a particular booked appointment.
* /email-delete : Sends payload to the email_handler in the event of a successful cancellation.
* /cancellation-complete : Sends payload to the frontend in response to the cancellation request.
* /get-appointments-patient : Receives payload to return the appointments for a patient.
* /get-appointments-dentist : Receives payload to return the appointments for a dentist.
* /get-appointments-patient-response : Sends payload as a response to the request for patient appointment.
* /get-appointments-dentist-response : Sends payload as a response to the request for dentist appointment.

## Getting Started
### Prerequisites
* Java
* Maven
* Access to the project's MongoDB database
* MQTT Broker details

### Installation
* Clone the repository: 'git clone git@git.chalmers.se:courses/dit355/2023/student-teams/dit356-2023-17/booking_handler.git'
* Open the project with IDEA IntelliJ (or your IDE).
* Add the correct Broker Credentials in the MqttConfig file which can be found under - booking_handler > src > main > resources 
* Run 'mvn clean install' to install dependencies.
* Run 'mvn exec:java -Dexec.mainClass="com.yourgroup.App"' to run the Booking Handler

## Authors and acknowledgment
* Nishchya Arya 
* Sam Hardingham
* Kai Rowley
