const Product = require('../models/Product');

const postProduct = async (req, res) => {
  try {
    const { name, price, description, restaurant } = req.body;

    if (!name || !price || !description || !restaurant) {
      return res.status(400).send('Missing required fields for product creation.');
    }

    const product = new Product({
      name,
      price,
      description,
      restaurant,
    });

    const savedProduct = await product.save();
    res.status(201).json(savedProduct);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const getAllProducts = async (req, res) => {
  try {
    const products = await Product.find();
    console.log(products)
    res.status(200).json(products);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const getProduct = async (req, res) => {
  const productId = req.params.id;

  try {
    const product = await Product.findById(productId);
    if (!product) {
      return res.status(404).json({ message: 'Product not found.' });
    }

    res.status(200).json(product);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const updateProduct = async (req, res) => {
  const productId = req.params.id;
  const { name, price, description, restaurant } = req.body;

  try {
    const product = await Product.findByIdAndUpdate(
      productId,
      { name, price, description, restaurant },
      { new: true }
    );

    if (!product) {
      return res.status(404).json({ message: 'Product not found.' });
    }

    res.status(200).json(product);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const patchProduct = async (req, res) => {
  const productId = req.params.id;
  const { price, description } = req.body;

  try {
    const product = await Product.findByIdAndUpdate(
      productId,
      { $set: { price, description } },
      { new: true }
    );

    if (!product) {
      return res.status(404).json({ message: 'Product not found.' });
    }

    res.status(200).json(product);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const deleteProduct = async (req, res) => {
  const productId = req.params.id;

  try {
    const product = await Product.findByIdAndRemove(productId);

    if (!product) {
      return res.status(404).json({ message: 'Product not found.' });
    }

    res.status(200).json({ message: 'Product deleted successfully.' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const deleteAllProducts = async (req, res) => {
  try {
    const result = await Product.deleteMany();
    res.status(200).json({ message: 'All products deleted successfully.' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

module.exports = {
  postProduct,
  getAllProducts,
  getProduct,
  updateProduct,
  patchProduct,
  deleteProduct,
  deleteAllProducts,
};
