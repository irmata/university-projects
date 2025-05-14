# Email Handler for Distributed Systems Project

## Overview
This email handler is a part of the [Dentist System](https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-17/dental_server), designed to work within a distributed system architecture. The component is responsible for sending customers their booked appointment slot to their email address.

## Features
- **Event-Driven:** Listens to events/messages from an MQTT broker.
- **Database Integration:** Queries MongoDB to get user email.
- **Secure Communication:** Ensures encrypted data exchange between components.

## Topics
- **/email-register:** Receives payload to send email as confirmation.
- **/email-delete:** Receives payload to send email as cancellation.

## Getting Started

### Prerequisites
- Java 8 or above
- Maven 3.x.x

### Installing
1. Clone the repository: `git clone [repository URL]`
2. Open the project's directory
3. Run `mvn install`
4. Navigate into the generated `target` subfolder
5. Run `java -jar emailHandler-1.0-SNAPSHOT-jar-with-dependencies`

## Authors
- Daniel Van Den Heuvel
