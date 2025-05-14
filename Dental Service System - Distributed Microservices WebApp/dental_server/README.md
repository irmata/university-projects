# Dental App Server for Distributed Systems Project

## Overview
This frontend/backend Vue/Express server setup is part of Group 17's Dental System (https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-17), designed to work within the projects distributed system architecture. The repo is responsible for both serving the initial frontend Vue application to end users (ie patients and similar interest groups alike) as well as serving further client requests that require bussiness logic and database operations - all via our Express server. Dental clinic related operations are also handled and facilitaited by the Express server. Incoming and outgoing network interactions from and to clients' is achieved via HTTP adoption, while interactions between the server and our Microservice-like handlers (responsible for our apps various database related use cases) is faciliated via MQTT. Our MQTT setup signifies an event driven configuration i.e. facilitating multicast, concurrent message-processing and transmission characteristics (both at the level of our connected MQTT clients and at broker (Hive) level which sits in-between connected clients), while our HTTP setup signifies a restful-like client/server configuration.

Our Vue application realizes e.g. login and booking interactions allowing patients to book and cancel appointments, hosts a interactive map view listing all available (mock) dental clincs in Gothenburg (with search and flter capabilities), as well as a patient dashboard. The Vue app effectively allows for smooth page transitions (due to Vue itself intercepting typical HTTP Page requests and manages routing itself) - thus making the user experience more appealing and resource friendly. The app is also well supported across many browser vendors and versions via our Babel transpilation config - supporting browsers whose usage is greater than 1% globally, and simultaneously compatible with the last 3 major versions of these browsers in question.

## Features
- **SPA (Single Page Application):** Client interface is served as a single HTML page.
- **RESTful API:** Adheres to numerous RESTful principles.
- **Non-blocking & Event-Driven Request Handling:** Able to achieve non-blocking code execution Via Node's "Event Loop" - thus granting faster response times for clients.

## Getting Started

### Pre-required Services & Tools to Run As Intended
- Command shell (or a IDE with a built-in CLI)
- Git
- Node (Recommended Version: LTS - no project minimum version has been identified, npm will instruct you if you require a higher node version)
- Npm
- Access and connection establishment to the project's MongoDB database
- Connection establishment to a MQTT Broker
- Google Maps API access

### Setup Steps to Run As Intended
1. Install Git
2. Install Node
3. Install Npm
4. Git clone repository at: https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-17/dental_server into a local folder
5. Open the cloned folder (dental_server) in a shell window or a code editor
6. Supply variables inside .env files (one inside / folder and one inside /client folder) with relevant values (either non, some or all may be filled-in)
7. Run the following command inside / folder (installs dependencies in / folder): npm i
8. Run the following command inside / folder (starts the Expres, backend server): npm start
9. Run the following command inside /client folder (installs dependencies in /client folder): npm i
10. Run the following command inside /client folder (starts the Vue frontend server via vue-cli-service): npm run dev
11. Open or enter the generated hosting link (from npm run dev) into a browser
12. You should now have the Vue app in a browser, as well as the backend server running ready to serve frontend->backend requests

##### Note: 
For the full-fledge system experiance and to work as intended in a hollistic manner, it is simultenously required to have our 4 bussiness logic handlers setup and running, i.e. our:
- Authentication-handler (https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-17/authentication-handler)
- Email-handler (https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-17/email-handler)
- Booking-handler (https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-17/booking_handler)
- Availability-slot-handler (https://git.chalmers.se/courses/dit355/2023/student-teams/dit356-2023-17/availability-slot-handler)

## Acknowledgments

We want to extend our appreciation to TA Nicole for the valuable insights and considerations in regards to: MQTT QoS requirements, simultaneous client-request-events, fault tolerance aspects as well as workflow and prioritization concerns.

## Authors
Louis Mercier
Daniel van den Heuvel
Kai Rowley
Sam Hardingham
Nishchya Arya
