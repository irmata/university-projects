const express = require('express');
const router = express.Router();
const Restaurant = require('../models/Restaurant');
const restaurantController = require('../controllers/restaurant');

// Create a new restaurant
router.post('/', restaurantController.createRestaurant);

// Get all restaurants
router.get('/', restaurantController.getAllRestaurants);

// Get a specific restaurant by ID
router.get('/:id', restaurantController.getRestaurantById);

// Update a restaurant by ID using PUT
router.put('/:id', restaurantController.updateRestaurantByIdPut);

// Update a restaurant by ID using PATCH
router.patch('/:id', restaurantController.updateRestaurantByIdPatch);

// Delete a restaurant by ID
router.delete('/:id', restaurantController.deleteRestaurantById);

// Delete the entire collection of restaurants
router.delete('/', restaurantController.deleteAllRestaurants);

module.exports = router;