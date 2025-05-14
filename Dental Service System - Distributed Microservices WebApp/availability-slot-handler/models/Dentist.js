const mongoose = require('mongoose')

const dentistSchema = new mongoose.Schema({
  name: String,
  email: String,
  dental_clinic: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'DentalClinic'
  }
})

module.exports = mongoose.model('Dentist', dentistSchema)
