const Delivery = require('../models/Delivery');

// Create a new delivery
exports.createDelivery = async (req, res) => {
  try {
    const delivery = new Delivery(req.body);
    const savedDelivery = await delivery.save();
    res.status(201).json(savedDelivery);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

// Get all deliveries
exports.getAllDeliveries = async (req, res) => {
  try {
    const deliveries = await Delivery.find();
    res.json(deliveries);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

// Count number of deliveries
exports.countDeliveries = async (req, res) => {
  try {
    const deliveriesCount = await Delivery.countDocuments();
    res.json({ count: deliveriesCount });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

// Get a specific delivery by ID
exports.getDeliveryById = async (req, res) => {
  try {
    const delivery = await Delivery.findById(req.params.id);
    if (!delivery) {
      return res.status(404).json({ error: 'Delivery not found' });
    }
    res.json(delivery);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

// Update a delivery by ID using PUT
exports.updateDeliveryByIdPut = async (req, res) => {
  try {
    const delivery = await Delivery.findByIdAndUpdate(
      req.params.id,
      req.body,
      { new: true }
    );
    if (!delivery) {
      return res.status(404).json({ error: 'Delivery not found' });
    }
    res.json(delivery);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

// Update a delivery by ID using PATCH
exports.updateDeliveryByIdPatch = async (req, res) => {
  try {
    const delivery = await Delivery.findByIdAndUpdate(
      req.params.id,
      { $set: req.body }, // Use $set to update only the specified fields
      { new: true }
    );
    if (!delivery) {
      return res.status(404).json({ error: 'Delivery not found' });
    }
    res.json(delivery);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

// Delete a delivery by ID
exports.deleteDeliveryById = async (req, res) => {
  try {
    const delivery = await Delivery.findByIdAndRemove(req.params.id);
    if (!delivery) {
      return res.status(404).json({ error: 'Delivery not found' });
    }
    res.json({ message: 'Delivery deleted successfully' });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

// Delete all deliveries
exports.deleteAllDeliveries = async (req, res) => {
  try {
    await Delivery.deleteMany({});
    res.json({ message: 'All deliveries deleted successfully' });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};