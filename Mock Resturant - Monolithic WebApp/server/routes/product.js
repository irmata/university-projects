// routes/product.js
const express = require('express');
const router = express.Router();
const productController = require('../controllers/product');

router.post("/", productController.postProduct);
router.get("/", productController.getAllProducts);
router.get("/:id", productController.getProduct);
router.put("/:id", productController.updateProduct);
router.patch("/:id", productController.patchProduct);
router.delete("/:id", productController.deleteProduct);
router.delete("/", productController.deleteAllProducts);

module.exports = router;
