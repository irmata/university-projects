const mongoose = require('mongoose')

const appointmentSchema = mongoose.Schema({
  date: String,
  dentistId: {
    type: mongoose.Schema.Types.ObjectId,
    rel: 'Dentist'

  },
  patientId: {
    type: mongoose.Schema.Types.ObjectId,
    rel: 'Patient'
  },
  dentalClinicId: {
    type: mongoose.Schema.Types.ObjectId,
    rel: 'DentcalClinic'
  },
  time: String
})

module.exports = mongoose.model('appointments', appointmentSchema)
