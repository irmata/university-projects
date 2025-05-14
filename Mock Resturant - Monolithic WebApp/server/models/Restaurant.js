// models/Restaurant.js
const mongoose = require('mongoose');

const restaurantSchema = new mongoose.Schema({
  name: {
    type: String,
    required: true,
  },
  address: String,
  // Add other restaurant properties as needed
});

module.exports = mongoose.model('Restaurant', restaurantSchema);
