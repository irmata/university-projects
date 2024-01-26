var mongoose = require("mongoose");
var Schema = mongoose.Schema;

// Defining the schema for dentist unavailabilities
var DentistUnavailableSchema = new Schema({
    dentistId: {
        type: mongoose.Schema.Types.ObjectId,
        required: true
    },
    date: {
        type: String,
        required: [true, 'Date is required']
    }
});

// Creating the model from the schema
const DentistUnavailableModel = mongoose.model('DentistUnavailable', DentistUnavailableSchema, 'dentistunavailabilities');
module.exports = DentistUnavailableModel;