const express = require('express')
const router = express.Router()
const Publisher = require('../mqtt/Publisher.js')
const PayloadHandler = require('../mqtt/PayloadHandler.js')
const mongoose = require('mongoose')
const DentistUnavailable = require('./models/DentistUnavailable.js')
const Appointment = require('./models/Appointment.js')

const payloadHandler = new PayloadHandler()

router.post('/patient/registration', (req, res) => {
  const mqttClient = req.mqttClient

  Publisher(mqttClient, '/patient/registration-request', JSON.stringify(req.body))

  payloadHandler.on(mqttClient, '/patient/registration-response', req.body.correlationID).then(payload => {
    const response = JSON.parse(payload)

    if (response.statusCode === 200) {
      res.status(response.statusCode).json(response.data)
    } else {
      res.status(response.statusCode).send({ message: response.message })
    }
  }).catch(error => {
    console.error('Error in registration: ', error)
    res.status(500).send({ message: 'Internal server error' })
  })
})

router.post('/patient/login', async (req, res) => {
  const mqttClient = req.mqttClient

  Publisher(mqttClient, '/patient/check-authentication', JSON.stringify(req.body))

  payloadHandler.on(mqttClient, '/patient/authentication-response', req.body.correlationID).then(payload => {
    const authResponse = JSON.parse(payload)

    // Checks the status of authentication (success/fail)
    if (authResponse.authenticated) {
      // If authentication is successful
      const patientId = authResponse.patientId
      const email = req.body.email

      console.log('Session set for:', patientId, ' email: ', email)

      req.session.patientId = patientId
      req.session.email = email

      res.status(200).send({ message: 'Login successful' })
    } else {
      // If authentication fails
      console.log('Authentication failed:', authResponse.message)

      res.status(401).send({ message: authResponse.message })
    }
  }).catch(error => {
    console.error('Error in authentication:', error)
    res.status(500).send({ message: 'Authentication process error' })
  })
})

router.post('/patient/logout', (req, res) => {
  // Destroys the session
  req.session.destroy(err => {
    if (err) {
      console.error('Session destruction error:', err)
      return res.status(500).send({ message: 'Error during logout' })
    }

    res.status(200).send({ message: 'Logout successful' })
  })
})

router.get('/check-session', (req, res) => {
  if (req.session && req.session.patientId) {
    res.status(200).json({ isLoggedIn: true, user: req.session.patientId, email: req.session.email })
  } else {
    res.status(200).json({ isLoggedIn: false })
  }
})

router.post('/dashboard/booking/:clinicId/slots/', (req, res) => {
  const mqttClient = req.mqttClient

  payloadHandler.on(mqttClient, '/booking-response', req.body.correlationID).then(payload => {
    res.send(payload)
  })

  Publisher(mqttClient, '/booking', JSON.stringify(req.body))
})

router.post('/calendar/timeslots/', (req, res) => {
  const mqttClient = req.mqttClient

  payloadHandler.on(mqttClient, '/appointments-response').then(payload => {
    res.send(payload)
  })

  Publisher(mqttClient, '/get-appointments', JSON.stringify(req.body))
})

router.get('/calendar/dentalclinics/', (req, res) => {
  const mqttClient = req.mqttClient

  payloadHandler.on(mqttClient, '/dental-clinics-response').then(payload => {
    const parsedPayload = JSON.parse(payload)
    res.send(parsedPayload)
  })

  Publisher(mqttClient, '/get-dental-clinics', JSON.stringify(req.body))
})

router.post('/calendar/dentists/', (req, res) => {
  const mqttClient = req.mqttClient

  payloadHandler.on(mqttClient, '/dentists-response').then(payload => {
    res.send(payload)
  })

  Publisher(mqttClient, '/get-dentists', JSON.stringify(req.body))
})

router.delete('/patient/appointments/:id', async (req, res) => {
  try {
    const appointmentId = req.params.id

    // Validate the appointment ID
    if (!mongoose.Types.ObjectId.isValid(appointmentId)) {
      return res.status(400).send({ message: 'Invalid appointment ID.' })
    }

    // Check if the appointment exists
    const appointment = await Appointment.findById(appointmentId)
    if (!appointment) {
      return res.status(404).send({ message: 'Appointment not found.' })
    }

    // Send cancellation message to MQTT broker
    const mqttClient = req.mqttClient
    Publisher(mqttClient, '/cancellation', JSON.stringify({ id: appointmentId }))

    res.status(200).send({ message: 'Appointment cancelled successfully.' })
  } catch (error) {
    console.error('Error in deleting appointment:', error)
    res.status(500).send({ message: 'Internal server error' })
  }
})

router.get('/patient/appointments', (req, res) => {
  if (req.session && req.session.patientId) {
    const mqttClient = req.mqttClient
    const requestPayload = { patientId: req.session.patientId }

    payloadHandler.on(mqttClient, '/get-appointments-patient-response')
      .then(payload => {
        const response = JSON.parse(payload)
        res.status(200).json(response)
      })
      .catch(error => {
        console.error('Error in MQTT response:', error)
        res.status(500).send({ message: 'Error fetching appointments' })
      })

    Publisher(mqttClient, '/get-appointments-patient', JSON.stringify(requestPayload))
  } else {
    res.status(401).send({ message: 'Unauthorized' })
  }
})

router.get('/dentists/:id/appointments', async (req, res) => {
  try {
    const dentistId = req.params.id

    // Ensuring the dentistId is a valid ObjectId
    if (!mongoose.Types.ObjectId.isValid(dentistId)) {
      return res.status(400).send({ message: 'Invalid dentist ID format.' })
    }

    // Accessing the MongoDB collection to verify the dentist id
    const dentistsCollection = mongoose.connection.collection('dentists')
    const dentistExists = await dentistsCollection.findOne({ _id: new mongoose.Types.ObjectId(dentistId) })

    if (!dentistExists) {
      return res.status(404).send({ message: 'Dentist ID not found. This Dentist does not exist.' })
    }

    // Querying the database for appointments with the matching dentistId
    const appointments = await Appointment.find({ dentistId })

    // If no appointments are found, return no appointment found
    if (appointments.length === 0) {
      return res.status(200).send({ message: 'The Dentist has no booked appointments at this time or does not exist. Please check the dentist ID and try again.' })
    }

    res.status(200).json(appointments)
  } catch (error) {
    console.error(error)
    res.status(500).send({ message: 'Internal Server Error', error })
  }
})

router.post('/dentists/:id/unavailability', async (req, res) => {
  try {
    const { dentist, date } = req.body
    const dentistIdFromUrl = req.params.id

    // Validating the input.
    if (!dentist || !date) {
      return res.status(400).send({ message: 'Dentist and date are required.' })
    }
    // Ensuring the dentistId is a valid ObjectId
    if (!mongoose.Types.ObjectId.isValid(dentistIdFromUrl)) {
      return res.status(400).send({ message: 'Invalid dentist ID format.' })
    }

    // Accessing the MongoDB collection to verify the dentist id.
    const dentistsCollection = mongoose.connection.collection('dentists')
    const dentistExists = await dentistsCollection.findOne({ _id: new mongoose.Types.ObjectId(dentistIdFromUrl) })

    if (!dentistExists) {
      return res.status(404).send({ message: 'Dentist ID not found. This Dentist does not exist.' })
    }

    // Checking if the dentist ID in the URL matches the dentist ID in the body.
    if (dentistIdFromUrl !== dentist) {
      return res.status(400).send({ message: 'Dentist ID in the URL does not match the ID in the body. Please use the same ID and try again' })
    }
    // Create a new unavailability record.
    const unavailable = new DentistUnavailable({
      dentistId: dentist,
      date
    })

    // Save the record to MongoDB.
    await unavailable.save()

    res.status(201).send(unavailable)
  } catch (error) {
    console.error(error)
    res.status(500).send({ message: 'Internal Server Error', error })
  }
})

// Catch-all route for undefined backend path
router.get('*', (req, res) => {
  res.status(404).send('Path does not exist')
})

module.exports = router
