const express = require('express');
const router = express.Router();
const Delivery = require('../models/Delivery');
const deliveryController = require('../controllers/delivery');

// Create a new delivery
router.post('/', deliveryController.createDelivery);

// Get all deliveries
router.get('/', deliveryController.getAllDeliveries);

// Count number of deliveries
router.get('/count', deliveryController.countDeliveries);

// Get a specific delivery by ID
router.get('/:id', deliveryController.getDeliveryById);

// Update a delivery by ID using PUT
router.put('/:id', deliveryController.updateDeliveryByIdPut);

// Update a delivery by ID using PATCH
router.patch('/:id', deliveryController.updateDeliveryByIdPatch);

// Delete a delivery by ID
router.delete('/:id', deliveryController.deleteDeliveryById);

// Delete all deliveries
router.delete('/', deliveryController.deleteAllDeliveries);

module.exports = router;