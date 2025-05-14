const Promotion = require('../models/Promotion');

const postPromotion = async (req, res) => {
    const reqData = req.body;

    try {
        const promotion = new Promotion(reqData);
        const savedPromotion = await promotion.save();
        res.status(201).json(savedPromotion);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

const getAllPromotions = async (req, res) => {
    try {
        const promotions = await Promotion.find();
        res.json(promotions);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

const getPromotion = async (req, res) => {
    try {
        const promotion = await Promotion.findById(req.params.id);
        if (!promotion) {
            return res.status(404).json({ error: 'Promotion not found' });
        }
        res.json(promotion);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

const updatePromotion = async (req, res) => {
    const reqData = req.body;

    try {
        const promotion = await Promotion.findByIdAndUpdate(
            req.params.id,
            { $set: reqData },
            { new: true }
        );
        if (!promotion) {
            return res.status(404).json({ error: 'Promotion not found' });
        }
        res.json(promotion);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

const patchPromotion = async (req, res) => {
    const reqData = req.body;

    try {
        const promotion = await Promotion.findByIdAndUpdate(
            req.params.id,
            { $set: reqData },
            { new: true }
        );
        if (!promotion) {
            return res.status(404).json({ error: 'Promotion not found' });
        }
        res.json(promotion);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

const deletePromotion = async (req, res) => {
    try {
        const promotion = await Promotion.findByIdAndRemove(req.params.id);
        if (!promotion) {
            return res.status(404).json({ error: 'Promotion not found' });
        }
        res.json({ message: 'Promotion deleted successfully' });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

const deleteAllPromotions = async (req, res) => {
    try {
        const result = await Promotion.deleteMany();
        res.json(result);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

module.exports = {
    postPromotion,
    getAllPromotions,
    getPromotion,
    updatePromotion,
    patchPromotion,
    deletePromotion,
    deleteAllPromotions
};
