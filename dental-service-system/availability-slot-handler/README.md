# Availability Slot Handler for Distributed Systems Project

## Overview
This availability slot handler is a part of the [Dentist System](https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-17/dental_server), designed to work within a distributed system architecture. The component is responsible for handling booking requests to determine the availability of slots.

## Features
- **Event-Driven:** Listens to events/messages from an MQTT broker.
- **Database Interaction:** Interacts with MongoDB to query and update appointment-related data.
- **Booking Management:** Processes booking requests and checks the availability of dental appointment slots.

## Topics
- **/booking:** Receives booking requests and processes them to determine slot availability.
- **/get-appointments:** Fetches unavailable appointment slots based on the requested date and dentistID.
- **/appointments-response:** Publishes unavailable appointment slots.
- **/get-dental-clinics:** Receives request to fetch all dental clinics.
- **/dental-clinics-response:** Publishes all dental clinics.
- **/get-dentists:** Fetches all dentists based on the requested dentalClinicId.
- **/dentists-response:** Publishes all dentists associated with the dentalClinicId.

## Getting Started

### Prerequisites
- Command shell (or IDE with integrated CLI)
- Node.js
- Npm
- Git
- Access to our MongoDB system project DB
- Access to a MQTT broker

### Installing & Running
1. Clone the repository: `https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-17/availability-slot-handler`
2. Navigate to the project's directory
3. Provide necessary values to variables inside .env file (MongoDB REQUIRED)
4. Run `npm install` to install program dependencies
5. Start the application with command: `node index.js`

## Authors
- Kai Rowley
- Daniel Van Den Heuvel
