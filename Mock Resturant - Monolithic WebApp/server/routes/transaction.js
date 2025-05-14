const express = require('express');
const router = express.Router();
const transactionController = require('../controllers/transaction');

// Create a new transaction
router.post('/', transactionController.createTransaction);

// Get all transactions
router.get('/', transactionController.getAllTransactions);

// Get a specific transaction by ID
router.get('/:id', transactionController.getTransactionById);

// Update a transaction by ID using PUT
router.put('/:id', transactionController.updateTransactionByIdPut);

// Update a transaction by ID using PATCH
router.patch('/:id', transactionController.updateTransactionByIdPatch);

// Delete a transaction by ID
router.delete('/:id', transactionController.deleteTransactionById);

// Add a product to a transaction
router.put('/:id/products/:product_id', transactionController.addProductToTransaction);

// Get all products in a transaction
router.get('/:id/products', transactionController.getProductsInTransaction);

// Get a specific product in a transaction by product_id
router.get('/:id/products/:product_id', transactionController.getProductInTransaction);

// Remove a product from a transaction by product_id
router.delete('/:id/products/:product_id', transactionController.removeProductFromTransaction);

// Add a product to a transaction
router.post('/:id/products', transactionController.addProductToTransaction);


module.exports = router;
