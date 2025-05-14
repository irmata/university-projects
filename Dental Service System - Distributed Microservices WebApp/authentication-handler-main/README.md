# Authentication Handler for Distributed Systems Project

## Overview
This authentication handler is a part of the Dentist System (put link here), designed to work within a distributed system architecture. The component is responsible for authenticating messages received through an MQTT broker and querying a MongoDB database for authorization purposes.

## Features
- **Event-Driven Authentication:** Listens to events/messages from an MQTT broker.
- **Database Integration:** Queries MongoDB to validate user credentials.
- **Secure Communication:** Ensures encrypted data exchange between components.

### Prerequisites
- Java 
- Maven 
- Access to the project's MongoDB database
- MQTT Broker details

### Installing
1. Clone the repository: `git clone [repository URL]`
2. Open the project with IntelliJ IDEA.
3. Run `mvn clean install` to install dependencies.

### Deployment
1. Open a terminal in the root folder of the project and run 'mvn spring-boot:run' to start the application

## Authors
- Sam Hardingham - Sole contributor
