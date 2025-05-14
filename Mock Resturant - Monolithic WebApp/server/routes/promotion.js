// routes/promotion.js
const express = require('express');
const router = express.Router();
const promotionController = require('../controllers/promotion');

router.post("/", promotionController.postPromotion);
router.get("/", promotionController.getAllPromotions);
router.get("/:id", promotionController.getPromotion);
router.put("/:id", promotionController.updatePromotion);
router.patch("/:id", promotionController.patchPromotion);
router.delete("/:id", promotionController.deletePromotion);
router.delete("/", promotionController.deleteAllPromotions);

module.exports = router;