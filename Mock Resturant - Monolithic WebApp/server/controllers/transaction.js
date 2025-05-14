const Transaction = require('../models/Transaction');

const createTransaction = async (req, res) => {
  try {
    const transaction = new Transaction(req.body);
    const savedTransaction = await transaction.save();
    res.status(201).json(savedTransaction);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const getAllTransactions = async (req, res) => {
  const perPage = 100;
  const page = parseInt(req.query.page) || 1;

  try {
    const transactions = await Transaction.find({ totalAmount: { $gt: 0 }})
      .sort({ date: -1 })
      .skip((perPage * page) - perPage)
      .limit(perPage)
      .populate('customer'); // Populate the 'customer' field in the Transaction documents with the corresponding Customer object

    res.json(transactions);
  } catch (err) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

const getTransactionById = async (req, res) => {
  try {
    const transaction = await Transaction.findById(req.params.id);
    if (!transaction) {
      return res.status(404).json({ error: 'Transaction not found' });
    }
    res.json(transaction);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const updateTransactionByIdPut = async (req, res) => {
  try {
    const transaction = await Transaction.findByIdAndUpdate(
      req.params.id,
      req.body,
      { new: true }
    );
    if (!transaction) {
      return res.status(404).json({ error: 'Transaction not found' });
    }
    res.json(transaction);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const updateTransactionByIdPatch = async (req, res) => {
  try {
    const transaction = await Transaction.findByIdAndUpdate(
      req.params.id,
      { $set: req.body },
      { new: true }
    );
    if (!transaction) {
      return res.status(404).json({ error: 'Transaction not found' });
    }
    res.json(transaction);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const deleteTransactionById = async (req, res) => {
  try {
    const transaction = await Transaction.findByIdAndRemove(req.params.id);
    if (!transaction) {
      return res.status(404).json({ error: 'Transaction not found' });
    }
    res.json({ message: 'Transaction deleted successfully' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};



const addProductToTransaction = async (req, res) => {
  try {
    const { product, quantity } = req.body;

    if (!product) {
      return res.status(400).json({ error: 'Product ID is required' });
    }

    const transaction = await Transaction.findById(req.params.id);
    if (!transaction) {
      return res.status(404).json({ error: 'Transaction not found' });
    }

    const existingProductIndex = transaction.products.findIndex(p => p.product.toString() === product);

    if (existingProductIndex !== -1) {
      transaction.products[existingProductIndex].quantity += 1;
    } else {
      const newProduct = {
        product,
        quantity: 1,
      };

      transaction.products.push(newProduct);
    }

    await transaction.save();

    res.json(transaction);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const getProductsInTransaction = async (req, res) => {
  try {
    const transaction = await Transaction.findById(req.params.id);
    if (!transaction) {
      return res.status(404).json({ error: 'Transaction not found' });
    }

    res.json(transaction.products);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const getProductInTransaction = async (req, res) => {
  try {
    const transaction = await Transaction.findById(req.params.id);
    if (!transaction) {
      return res.status(404).json({ error: 'Transaction not found' });
    }

    const product = transaction.products.find(p => p.product.toString() === req.params.product_id);

    if (!product) {
      return res.status(404).json({ error: 'Product not found in transaction' });
    }

    res.json(product);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const removeProductFromTransaction = async (req, res) => {
  try {
    const { product_id } = req.params;

    if (!product_id) {
      return res.status(400).json({ error: 'Product ID is required' });
    }

    const transaction = await Transaction.findById(req.params.id);
    if (!transaction) {
      return res.status(404).json({ error: 'Transaction not found' });
    }

    const productIndex = transaction.products.findIndex(p => p.product.toString() === product_id);
    if (productIndex === -1) {
      return res.status(404).json({ error: 'Product not found in transaction' });
    }

    const productItem = transaction.products[productIndex];

    if (productItem.quantity > 0) {
      productItem.quantity -= 1;

      if (productItem.quantity === 0) {
        transaction.products.splice(productIndex, 1);
      }
    } else {
      return res.status(400).json({ error: 'Product quantity is already zero' });
    }

    await transaction.save();

    res.json({ message: 'Product removed from transaction successfully' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// post a product to a transaction
const postProductToTransaction = async (req, res) => {
  try {
    const { product, quantity } = req.body; // Modified to use req.body.product

    if (!product) {
      return res.status(400).json({ error: 'Product ID is required' });
    }

    const transaction = await Transaction.findById(req.params.id);
    if (!transaction) {
      return res.status(404).json({ error: 'Transaction not found' });
    }

    // Check if the product already exists in the transaction
    const existingProduct = transaction.products.find(p => p.product.toString() === product);

    if (existingProduct) {
      // Increment the quantity for the existing product by 1
      existingProduct.quantity += 1;
    } else {
      // Product doesn't exist, create a new product entry with quantity 1
      const newProduct = {
        product: product,
        quantity: 1, // Increment quantity by 1
      };

      transaction.products.push(newProduct);
    }

    await transaction.save();

    res.json(transaction);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};



module.exports = {
  createTransaction,
  getAllTransactions,
  getTransactionById,
  updateTransactionByIdPut,
  updateTransactionByIdPatch,
  deleteTransactionById,
  addProductToTransaction,
  getProductsInTransaction,
  getProductInTransaction,
  removeProductFromTransaction,
  postProductToTransaction,
};
