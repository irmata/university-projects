const subscribers = {
  dentistsResponse: {
    subscribe: (mqttClient, qos = 2) => {
      mqttClient.subscribe('/dentists-response', { qos }, (error, subInfo) => {
        if (error) {
          console.error('Error subscribing to /dentists-response topic:', error)
        } else {
          console.log('Subscribing To:', subInfo)
        }
      })
    }
  },
  unavailableAppointmentsResponse: {
    subscribe: (mqttClient, qos = 2) => {
      mqttClient.subscribe('/appointments-response', { qos }, (error, subInfo) => {
        if (error) {
          console.error('Error subscribing to /appointments-response topic:', error)
        } else {
          console.log('Subscribing To:', subInfo)
        }
      })
    }
  },
  dentalClinicsResponse: {
    subscribe: (mqttClient, qos = 2) => {
      mqttClient.subscribe('/dental-clinics-response', { qos }, (error, subInfo) => {
        if (error) {
          console.error('Error subscribing to /dental-clinics-response topic:', error)
        } else {
          console.log('Subscribing To:', subInfo)
        }
      })
    }
  },
  getPatientAppointmentsResponse: {
    subscribe: (mqttClient, qos = 2) => {
      mqttClient.subscribe('/get-appointments-patient-response', { qos }, (error, subInfo) => {
        if (error) {
          console.error('Error subscribing to /get-appointments-patient-response topic:', error)
        } else {
          console.log('Subscribing To:', subInfo)
        }
      })
    }
  },
  appointmentCancellation: {
    subscribe: (mqttClient, qos = 2) => {
      mqttClient.subscribe('/cancellation-complete', { qos }, (error, subInfo) => {
        if (error) {
          console.error('Error subscribing to /dental-clinics-response topic:', error)
        } else {
          console.log('Subscribing To:', subInfo)
        }
      })
    }
  },
  patientLogin: {
    subscribe: (mqttClient, qos = 2) => {
      mqttClient.subscribe('/patient/authentication-response', { qos }, (error, subInfo) => {
        if (error) {
          console.error('Error subscribing to /patient/authentication-response topic:', error)
        } else {
          console.log('Subscribing To:', subInfo)
        }
      })
    }
  },
  patientRegister: {
    subscribe: (mqttClient, qos = 2) => {
      mqttClient.subscribe('/patient/registration-response', { qos }, (error, subInfo) => {
        if (error) {
          console.error('Error subscribing to /patient/registration-response topic:', error)
        } else {
          console.log('Subscribing To:', subInfo)
        }
      })
    }
  },
  bookingResponse: {
    subscribe: (mqttClient, qos = 2) => {
      mqttClient.subscribe('/booking-response', { qos }, (error, subInfo) => {
        if (error) {
          console.error('Error subscribing to /booking-response topic:', error)
        } else {
          console.log('Subscribing To:', subInfo)
        }
      })
    }
  }
}

module.exports = subscribers
