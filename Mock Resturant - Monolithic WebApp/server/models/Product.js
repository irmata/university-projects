// models/Product.js
const mongoose = require('mongoose');

const productSchema = new mongoose.Schema({
  name: String,
  price: Number,
  description: String,
  restaurant: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Restaurant'
  },
  category: {
    type: String,
    enum: ['Mains', 'Deals', 'Sides', 'Desserts'],
    default: 'Mains',
}
});

module.exports = mongoose.model('Product', productSchema);