const express = require('express')
const morgan = require('morgan')
const mqtt = require('mqtt')
const { clearInterval } = require('timers')
const routes = require('./http/route-handler.js')
const subscribers = require('./mqtt/subscribers.js')
const { v4: uuidv4 } = require('uuid')
const session = require('express-session')
require('dotenv').config()
const mongoose = require('mongoose')

const app = express()
const port = 3000

const mongoURI = process.env.MONGODB_URI // <-- PROVIDE credentials in .env file -->

app.use(express.json())
app.use(express.static('client/dist'))
app.use(morgan('short')) // Incoming request logging

// Middleware for sessions
app.use(session({
  secret: 'teeheesecretkey',
  resave: false,
  saveUninitialized: false,
  cookie: { secure: false, httpOnly: true, maxAge: 24 * 60 * 60 * 1000 }
}))

// Middleware to allow CORS
const cors = require('cors')
app.use(cors({
  origin: 'http://localhost:8080',
  credentials: true
}))

mongoose.connect(mongoURI, { useNewUrlParser: true, useUnifiedTopology: true })
  .then(() => console.log('\nSuccessfully connected to MongoDB!'))
  .catch(err => console.error('\nMongoDB connection error: ', err))

const mqttOptions = {
  clientId: 'dental-app-frontend-server',
  username: process.env.MQTT_USERNAME, // <-- PROVIDE credentials in .env file -->
  password: process.env.MQTT_PASSWORD, // <-- PROVIDE credentials in .env file -->
  clean: true, // Disregard previous connection state between this client and broker: true
  rejectUnauthorized: false // Reject connnection if broker SSL can't be verified: false
}

// Client connect to MQTT broker
const mqttClient = mqtt.connect(process.env.MQTT_URL, mqttOptions)

// Logger for pending MQTT broker connectivity establishment
const connectingToMqttBrokerLogger = setInterval(() => {
  console.log('\nServer attempting to connect to MQTT broker ...\nRepetitively Failing?\nReasons: 1. Incorrect/Unprovided MQTT Credentials 2. Connectivity Issues')
}, 5000)

mqttClient.on('connect', () => {
  clearInterval(connectingToMqttBrokerLogger)
  console.log('\nServer connected to a MQTT broker!')

  for (const topicKey in subscribers) {
    const subscriber = subscribers[topicKey]
    subscriber.subscribe(mqttClient)
  }
})

// Middleware to pass the MQTT client and a correlated request ID (for MQTT listening management) to route handlers
app.use((req, res, next) => {
  req.mqttClient = mqttClient
  req.body.correlationID = uuidv4()
  next()
})

// Middleware for route handlers
app.use('/', routes)

app.listen(port, 'localhost', () => {
  console.log(`\nServer listening for incoming HTTP requests on localhost:${port} ...`)
})

process.on('SIGINT', () => {
  console.log('\nReceived SIGINT (Ctrl+c). Exiting process.')
  process.exit(0)
})

module.exports = {
  mqttClient
}
