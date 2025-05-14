// models/Delivery.js
const mongoose = require('mongoose');

const deliverySchema = new mongoose.Schema({
    type: {
        type: String,
        required: true,
    },
    location: {
        type: String,
        required: true,
    },
    phone: {
        type: String,
    },
    customer: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Customer',
        required: true,
    },
    timestamp: {
        type: Date,
        default: Date.now,
    },
    transaction: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Transaction',
        required: true,
    },
    deliveryStatus: {
        type: String,
        enum: ['Pending', 'In Progress', 'Delivered', 'Cancelled'],
        default: 'Pending',
    },
});

module.exports = mongoose.model('Delivery', deliverySchema);