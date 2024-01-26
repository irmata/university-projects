const mongoose = require('mongoose')

const dentistUnavailabilitySchema = mongoose.Schema({
  dentist: {
    type: mongoose.Schema.Types.ObjectId,
    rel: 'Dentist'
  },
  date: String
})

module.exports = mongoose.model('dentistunavailabilities', dentistUnavailabilitySchema)
