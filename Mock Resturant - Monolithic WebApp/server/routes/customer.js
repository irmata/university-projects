const express = require('express');
const router = express.Router();
const Customer = require('../models/Customer');
const customerController = require('../controllers/customer');

// Create a new customer
router.post('/', customerController.createCustomer);

// Get all customers
router.get('/', customerController.getAllCustomers);

// Count number of customers
router.get('/count', customerController.countCustomers);

// Get a specific customer by ID
router.get('/:id', customerController.getCustomerById);

// Update a customer by ID using PUT
router.put('/:id', customerController.updateCustomerByIdPut);

// Update a customer by ID using PATCH
router.patch('/:id', customerController.updateCustomerByIdPatch);

// Delete a customer by ID
router.delete('/:id', customerController.deleteCustomerById);

// Delete all customers
router.delete('/', customerController.deleteAllCustomers);

router.post('/login', customerController.loginCustomer);

module.exports = router;