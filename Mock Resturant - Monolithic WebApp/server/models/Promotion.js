// models/Promotion.js
const mongoose = require('mongoose');

const promotionSchema = new mongoose.Schema({
    name: String,
    description: String,
    discount: Number,
    promotionStartDate: Date,
    promotionEndDate: Date,
    startTime: String,
    endTime: String,
    activeDays: [String],
    products: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Product'
    }]
});

module.exports = mongoose.model('Promotion', promotionSchema);