const mqtt = require('mqtt')
const mongoose = require('mongoose')
const Appointment = require('./models/Appointment.js')
const Dentist = require('./models/Dentist.js')
const DentistUnavailability = require('./models/DentistUnavailability.js')
const DentalClinic = require('./models/DentalClinic.js')
const { clearInterval } = require('timers')
require('dotenv').config()

async function run () {
  const isMongoDBConnected = await mongoose.connect(process.env.MONGODB_URI) // <-- PROVIDE credentials in .env file -->
  if (!isMongoDBConnected) {
    console.error(isMongoDBConnected)
    console.error('Retrying ...')
    run()
  } else {
    console.log('Connected to MongoDB')

    const mqttOptions = {
      clientId: 'availability-slot-handler',
      username: process.env.MQTT_USERNAME, // <-- PROVIDE credentials in .env file -->
      password: process.env.MQTT_PASSWORD, // <-- PROVIDE credentials in .env file -->
      clean: true, // Disregard previous connection state between this client and broker: true
      rejectUnauthorized: false // Reject conn if broker SSL can't be verified: false
    }

    // Client connect to MQTT broker
    const mqttClient = mqtt.connect(process.env.MQTT_URL, mqttOptions) // <-- PROVIDE credentials in .env file -->

    // Logs every 2s mqtt broker connection is not established
    const connectingToMqttBrokerLogger = setInterval(() => {
      console.log('Server attempting to connect to MQTT broker ...')
    }, 2000)

    mqttClient.on('connect', () => {
      clearInterval(connectingToMqttBrokerLogger)
      console.log('Connected to MQTT broker')
      console.log('Availability Slot Handler is running. Input CTRL+C to stop.')
      console.log('-----------------------------------------------------------')

      mqttClient.subscribe('/booking', { qos: 2 })
      mqttClient.subscribe('/get-appointments', { qos: 2 })
      mqttClient.subscribe('/get-appointments-patient', { qos: 2 })
      mqttClient.subscribe('/get-dental-clinics', { qos: 2 })
      mqttClient.subscribe('/get-dentists', { qos: 2 })
    })

    mqttClient.on('message', async (incomingTopic, message) => {
      if (incomingTopic === '/booking') {
        console.log('Receiving request on topic: /booking')
        await handleBooking(mqttClient, message)
      } else if (incomingTopic === '/get-appointments') {
        console.log('Receiving request on topic: /get-appointments')
        await getUnavailableAppointments(mqttClient, message)
      } else if (incomingTopic === '/get-dental-clinics') {
        console.log('Receiving request on topic: /get-dental-clinics')
        await getDentalClinics(mqttClient)
      } else if (incomingTopic === '/get-dentists') {
        console.log('Receiving request on topic: /get-dentists')
        await getDentists(mqttClient, message)
      } else if (incomingTopic === '/get-appointments-patient') {
        console.log('Receiving request on topic: /get-appointments-patient')
        await getUserAppointments(mqttClient, message)
      }
    })

    process.on('SIGINT', async () => {
      if (mqttClient.connected) {
        await new Promise(resolve => {
          mqttClient.end(false, () => {
            console.log('Disconnected from MQTT broker')
            resolve()
          })
        })
      }

      try {
        await mongoose.connection.close()
        console.log('Disconnected from MongoDB')
      } catch (error) {
        console.error('Error closing MongoDB connection:', error)
      }

      console.log('-----------------------------------------------------------')
      console.log('Availability Slot Handler stopped.')
      process.exit(0)
    })
  }
}

async function handleBooking (mqttClient, message) {
  const parsedMessageObj = JSON.parse(message.toString('utf8'))

  const { dentalClinicId, date, time } = parsedMessageObj

  const dentalClinic = await DentalClinic.findOne({ _id: dentalClinicId })

  const dentalClinicsDentistIds = dentalClinic.dentists

  const dentistUnavilabilities = await DentistUnavailability.find({ date, dentist: { $in: dentalClinicsDentistIds } })

  const nonMarkedDayOffDentists = dentalClinicsDentistIds.filter((dentistId) => {
    for (const dentistUnavObj of dentistUnavilabilities) {
      if (dentistUnavObj.dentist.equals(dentistId)) {
        return false
      }
    }

    return dentistId
  })

  const appointments = await Appointment.find({ date, time, dentalClinicId })

  const bookableDentists = nonMarkedDayOffDentists.filter((dentistId) => {
    for (const appointment of appointments) {
      if (appointment.dentistId.equals(dentistId)) {
        return false
      }
    }

    return dentistId
  })

  if (bookableDentists.length === 0) {
    parsedMessageObj.statusCode = 500
    parsedMessageObj.message = 'Slot unavailable'

    mqttClient.publish('/booking-checked', JSON.stringify(parsedMessageObj), { qos: 2, retain: false }, (error) => {
      if (error) {
        console.error('Error publishing message:', error)
      } else {
        console.log('Published message')
      }
    })
  } else if (bookableDentists.length === 1) {
    parsedMessageObj.dentistId = bookableDentists[0]
    parsedMessageObj.statusCode = 200
    parsedMessageObj.message = 'Slot available'

    mqttClient.publish('/booking-checked', JSON.stringify(parsedMessageObj), { qos: 2, retain: false }, (error) => {
      if (error) {
        console.error('Error publishing message:', error)
      } else {
        console.log('Published message')
      }
    })
  } else {
    parsedMessageObj.dentistId = bookableDentists.at(Math.floor(Math.random() * bookableDentists.length))
    parsedMessageObj.statusCode = 200
    parsedMessageObj.message = 'Slot available'

    mqttClient.publish('/booking-checked', JSON.stringify(parsedMessageObj), { qos: 2, retain: false }, (error) => {
      if (error) {
        console.error('Error publishing message:', error)
      } else {
        console.log('Published message')
      }
    })
  }
}

async function getUnavailableAppointments (mqttClient, message) {
  try {
    const parsedMessage = JSON.parse(message.toString())
    const { date, dentalClinicId } = parsedMessage

    const appointments = await Appointment.find({
      date,
      dentalClinicId: new mongoose.Types.ObjectId(dentalClinicId)
    })

    const unavailableSlots = appointments.map(appointment => appointment.time)

    // Fetch the dental clinic to get the dentists array
    const dentalClinic = await DentalClinic.findOne({ _id: dentalClinicId })
    const dentists = dentalClinic.dentists

    let unavailableDates = []

    // Iterate through each dentist to get their unavailable dates
    for (const dentistId of dentists) {
      console.log(`Checking unavailability for dentist: ${dentistId}`)
      console.log(typeof dentistId, dentistId)

      const dentistUnavailabilities = await DentistUnavailability.find({
        dentistId: new mongoose.Types.ObjectId(dentistId)
      })

      console.log(`Unavailabilities found: ${dentistUnavailabilities.length}`)

      // Extract the dates and add them to the unavailableDates array
      for (const unavailability of dentistUnavailabilities) {
        console.log(`Adding unavailable date: ${unavailability.date}`)
        unavailableDates.push(unavailability.date)
      }
    }
    console.log(`Final unavailable dates: ${unavailableDates}`)

    // Remove duplicates from unavailableDates
    unavailableDates = [...new Set(unavailableDates)]

    const responsePayload = {
      date,
      dentalClinicId,
      unavailableSlots,
      unavailableDates
    }

    mqttClient.publish('/appointments-response', JSON.stringify(responsePayload), { qos: 2, retain: false })
    console.log('Published to: /appointments-response')
  } catch (error) {
    console.error('Error fetching appointments:', error)
  }
}

async function getUserAppointments (mqttClient, message) {
  try {
    const parsedMessage = JSON.parse(message.toString())
    const { patientId } = parsedMessage

    const objectId = new mongoose.Types.ObjectId(patientId)

    const appointments = await Appointment.find({ patientId: objectId })

    const responsePayload = {
      appointments
    }
    mqttClient.publish('/get-appointments-patient-response', JSON.stringify(responsePayload), { qos: 2, retain: false })
    console.log('Published to: /get-appointments-patient-response')
  } catch (error) {
    console.error('Error fetching user appointments:', error)
  }
}

async function getDentalClinics (mqttClient) {
  try {
    const dentalClinics = await DentalClinic.find({})
    mqttClient.publish('/dental-clinics-response', JSON.stringify(dentalClinics), { qos: 2, retain: false })
    console.log('Published to: /dental-clinics-response')
  } catch (error) {
    console.error('Error fetching dental clinics:', error)
  }
}

async function getDentists (mqttClient, message) {
  try {
    const parsedMessage = JSON.parse(message.toString())
    const { dentalClinicId } = parsedMessage

    const dentists = await Dentist.find({
      dental_clinic: new mongoose.Types.ObjectId(dentalClinicId)
    })

    mqttClient.publish('/dentists-response', JSON.stringify(dentists), { qos: 2, retain: false })
    console.log('Published to: /dentists-response')
  } catch (error) {
    console.error('Error fetching dentists:', error)
  }
}

run()
