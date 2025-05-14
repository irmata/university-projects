var mongoose = require("mongoose");
var Schema = mongoose.Schema;

// Defining the schema for appointments
var AppointmentSchema = new Schema({
    date: {
        type: String,
        required: true
    },
    dentistId: {
        type: mongoose.Schema.Types.ObjectId,
        required: true
    },
    patientId: {
        type: mongoose.Schema.Types.ObjectId,
        required: true
    },
    dentalClinicId: {
        type: mongoose.Schema.Types.ObjectId,
        required: true
    },
    time: {
        type: String,
        required: true
    }
});

// Creating the model from the schema
const Appointment = mongoose.model('Appointment', AppointmentSchema, 'appointments');
module.exports = Appointment;
