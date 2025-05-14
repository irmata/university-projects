const mongoose = require('mongoose')

const DentalClinicSchema = mongoose.Schema({
  name: String,
  street_name: String,
  street_number: String,
  post_number: String,
  city: String,
  latitude: String,
  longitude: String,
  opening_time: String,
  closing_time: String,
  default_appointment_duration: {
    type: Number,
    enum: [15, 20, 30, 60]
  },
  ownership_type: String,
  dentists: [
    {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Dentist'
    }
  ]
})

module.exports = mongoose.model('DentalClinic', DentalClinicSchema)
