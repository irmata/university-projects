<template>
  <div>
    <h1>Admin Page</h1>
    <div v-if="error">{{ error }}</div>

    <h5 v-for="restaurant in restaurants" :key="restaurant.id">
      {{ restaurant.name + ' - ' + restaurant.address }}
    </h5>

    <div class="container">
      <ul class="list-group">
        <li class="list-group-item transparent-list-item">
          <div class="row  larger-text">
            <div class="col-6 col-md-4">
              <strong>Transactions</strong><br>
              {{ this.transactions.length }}
            </div>
            <div class="col-6 col-md-4">
              <strong>Average</strong><br>
              {{ '$' + (this.totalTransactions() / this.transactions.length).toFixed(2) }}
            </div>
            <div class="col-6 col-md-4">
              <strong>Total</strong><br>
              {{ '$' + this.totalTransactions().toFixed(2) }}
            </div>
            <div class="col-6 col-md-4">
              <strong>Products</strong><br>
              {{ this.products.length }}
            </div>
            <div class="col-6 col-md-4">
              <strong>Customers</strong><br>
              {{ this.customerCount }}
            </div>
            <div class="col-6 col-md-4">
              <strong>Deliveries</strong><br>
              {{ this.deliveriesCount }}
            </div>
          </div>
        </li>
      </ul>
    </div>

    <div class="container">
      <h4 class="mt-4 mb-3 text-left">Promotions</h4>
      <ul class="list-group">
        <li class="list-group-item transparent-list-item" v-for="promotion in promotions" :key="promotion._id">
          <div class="d-flex justify-content-between">
            <div class="text-left">
              <strong>{{ promotion.name }}</strong><br>
              Discount: {{ promotion.discount }}% <br>
              Start Date: {{ promotion.promotionStartDate.substring(0, 10) }},
              End Date: {{ promotion.promotionEndDate.substring(0, 10) }} <br>
              <button type="button" @click="removePromotion(promotion._id)">Remove</button>
              <button type="button" @click="updatePromotion(promotion._id)">Update</button>
            </div>
          </div>
        </li>
        <button type="button" @click="removePromotions()">Remove All</button>
      </ul>
    </div>

    <div class="container container-move-up">
      <ul class="list-group">
        <h4 class="mt-4 mb-3 text-left">Products</h4>

        <div class="btn-group mb-4 flex-wrap" role="group" aria-label="Category Selection">
          <button type="button" @click="filterProducts('Mains')">Mains</button>
          <button type="button" @click="filterProducts('Deals')">Deals</button>
          <button type="button" @click="filterProducts('Sides')">Sides</button>
          <button type="button" @click="filterProducts('Desserts')">Desserts</button>
          <button type="button" @click="filterProducts('All')">All</button>
        </div>
        <div v-for="product in products" :key="product._id">
          <div v-if="category === product.category || category === 'All'">
            <li class="list-group-item d-flex align-items-center transparent-list-item">
              <img :src="getProductImagePath(product)" class="product-img mr-3" alt="Product Image" width="50"
                height="50">
              <div class="text-left">
                <strong>{{ product.name }}</strong><br>
                Price: ${{ product.price.toFixed(2) }} <br>
                <button type="button" @click="removeProduct(product._id)">Remove</button>
                <button type="button" @click="updateProduct(product._id)">Update</button>
              </div>
            </li>
          </div>
        </div>
        <button type="button" @click="removeProducts()">Remove All</button>
      </ul>
    </div>

    <div class="container">
      <h3>Latest Transactions</h3>
      <div class="scrollable-list">
        <b-table striped hover :items="transactions" :fields="transactionFields"></b-table>
      </div>
    </div>

  </div>
</template>

<script>
import { Api } from '@/Api'

export default {
  data() {
    return {
      category: 'Mains',
      error: null,
      restaurants: [],
      products: [],
      productFields: [
        { key: '_id', label: 'Product ID', sortable: false },
        { key: 'name', label: 'Name', sortable: true },
        { key: 'price', label: 'Cost', sortable: true },
        { key: 'description', label: 'Description', sortable: false },
        { key: 'category', label: 'Category', sortable: true }
        // Add more fields as needed
      ],
      customers: [],
      customerCount: null,
      promotions: [],
      promotionFields: [
        { key: '_id', label: 'Promotion ID', sortable: false },
        { key: 'name', label: 'Name', sortable: true },
        { key: 'discount', label: 'Discount', sortable: true },
        { key: 'promotionStartDate', label: 'Start Date', sortable: true },
        { key: 'promotionEndDate', label: 'End Date', sortable: true }
      ],
      transactions: [],
      transactionFields: [
        { key: '_id', label: 'Transaction ID', sortable: false },
        { key: 'totalAmount', label: 'Total Amount', sortable: true },
        { key: 'date', label: 'Date', sortable: true },
        { key: 'transactionStatus', label: 'Status', sortable: true },
        { key: 'customer.name', label: 'Customer', sortable: true }
        // Add more fields as needed
      ],
      deliveries: [],
      deliveriesCount: null
    }
  },
  computed: {
    formattedPromotions() {
      return this.promotions.map(promotion => {
        return {
          _id: promotion._id,
          name: promotion.name,
          discount: promotion.discount + '%',
          promotionStartDate: promotion.promotionStartDate.substring(0, 10),
          promotionEndDate: promotion.promotionEndDate.substring(0, 10)
        }
      })
    }
  },
  mounted() {
    // populate these immediately after component instantiated
    this.getRestaurants()
    this.getProducts()
    this.getPromotions()
    this.getCustomerCount()
    this.getTransactions()
    this.getDeliveriesCount()
  },
  methods: {
    filterProducts(category) {
      this.category = category
    },
    getProductImagePath(product) {
      const filename = product.name.replace(/ /g, '-').toLowerCase()
      return require(`@/assets/images/${filename}.jpg`)
    },
    totalTransactions() {
      // Use the reduce() method to sum the totalTransactionAmount property of products
      const totalAmount = this.transactions.reduce((accumulator, transaction) => {
        return accumulator + transaction.totalAmount
      }, 0) // Initialize accumulator to 0

      return totalAmount
    },
    // Similar to getRestaurants, getProducts method, define methods for other entities
    getRestaurants() {
      Api.get('/restaurants')
        .then((response) => {
          this.restaurants = response.data
          this.error = null
        })
        .catch((error) => {
          this.restaurants = []
          this.error = 'Error fetching products: ' + error.message
        })
    },
    getProducts() {
      Api.get('/products')
        .then((response) => {
          this.products = response.data
          this.error = null
        })
        .catch((error) => {
          this.products = []
          this.error = 'Error fetching products: ' + error.message
        })
    },
    removeProduct(id) {
      if (window.confirm('Are you sure you want to remove this product? This operation is irreversible.')) {
        Api.delete('/products/' + id)
          .then(() => {
            // Product deleted successfully, update the product list
            this.getProducts()
            this.error = null
          })
          .catch((error) => {
            // Handle errors when deleting the product
            this.error = 'Error deleting product: ' + error.message
          })
      }
    },
    updateProduct(productId) {
      // Prompt the user for the new product price
      const updatedPriceInput = window.prompt('Enter the new product price:')

      // Check if the input is valid (not null and is a number with up to two decimal places)
      if (updatedPriceInput !== null && !isNaN(updatedPriceInput)) {
        // Parse the input to a floating-point number and fix it to two decimal places
        const updatedPrice = parseFloat(updatedPriceInput).toFixed(2)

        Api.patch(`/products/${productId}`, { price: updatedPrice })
          .then(response => {
            console.log('Product price updated successfully:', response.data)
            // this.products.find(product => product._id === productId).price = updatedPrice;
            this.getProducts()
            this.error = null
          })
          .catch(error => {
            // Handle errors when updating the product price
            this.error = 'Error updating product price: ' + error.message
            console.error('Error updating product price:', error)
          })
      } else {
        // Handle invalid input (null or non-numeric value)
        this.error = 'Invalid input. Please enter a valid numeric price.'
      }
    },
    removeProducts() {
      if (window.confirm('Are you sure you want to remove all products? This operation is irreversible.')) {
        const userInput = window.prompt('To confirm, please type "REMOVE ALL"')
        if (userInput === 'REMOVE ALL') {
          // If the user confirms, proceed with the API call
          Api.delete('/products')
            .then((response) => {
              this.getProducts()
              this.error = null
            })
            .catch((error) => {
              this.products = []
              this.error = 'Error fetching products: ' + error.message
            })
        } else {
          // If the user cancels, do nothing
          // You can optionally show a message to the user indicating that the operation was canceled.
        }
      }
    },
    getCustomers() {
      Api.get('/customers')
        .then(response => {
          this.customers = response.data
          this.error = null
        })
        .catch(error => {
          this.customers = []
          this.error = 'Error fetching customers: ' + error.message
        })
    },
    getCustomerCount() {
      Api.get('/customers/count')
        .then(response => {
          this.customerCount = response.data.count
          this.error = null
        })
        .catch(error => {
          this.customerCount = null
          this.error = 'Error fetching customers: ' + error.message
        })
    },
    getPromotions() {
      Api.get('/promotions')
        .then(response => {
          this.promotions = response.data
          this.error = null
        })
        .catch(error => {
          this.promotions = []
          this.error = 'Error fetching customers: ' + error.message
        })
    },
    updatePromotion(promotionId) {
      // Prompt the user for the new discount value
      const updatedDiscountInput = window.prompt('Enter the new discount value:')

      // Check if the input is valid (not null and is a number with up to two decimal places)
      if (updatedDiscountInput !== null && !isNaN(updatedDiscountInput)) {
        // Parse the input to a floating-point number and fix it to two decimal places
        const updatedDiscount = parseFloat(updatedDiscountInput).toFixed(2)

        // Make a PATCH request to update the promotion discount
        Api.patch(`/promotions/${promotionId}`, { discount: updatedDiscount })
          .then(response => {
            // Handle the successful response, update your component state, etc.
            console.log('Promotion discount updated successfully:', response.data)
            this.getPromotions() // Optionally, update promotions in your local data if needed
            this.error = null
          })
          .catch(error => {
            // Handle errors when updating the promotion discount
            this.error = 'Error updating promotion discount: ' + error.message
            console.error('Error updating promotion discount:', error)
          })
      } else {
        // Handle invalid input (null or non-numeric value)
        this.error = 'Invalid input. Please enter a valid numeric discount value.'
      }
    },
    removePromotion(id) {
      if (window.confirm('Are you sure you want to remove this promotion? This operation is irreversible.')) {
        Api.delete('/promotions/' + id)
          .then(() => {
            // Product deleted successfully, update the product list
            this.getPromotions()
            this.error = null
          })
          .catch((error) => {
            // Handle errors when deleting the product
            this.error = 'Error deleting promotion: ' + error.message
          })
      }
    },
    removePromotions() {
      if (window.confirm('Are you sure you want to remove all promotions? This operation is irreversible.')) {
        const userInput = window.prompt('To confirm, please type "REMOVE ALL"')
        if (userInput === 'REMOVE ALL') {
          // If the user confirms, proceed with the API call
          Api.delete('/promotions')
            .then((response) => {
              this.getPromotions()
              this.error = null
            })
            .catch((error) => {
              this.promotions = []
              this.error = 'Error fetching promotions: ' + error.message
            })
        } else {
          // If the user cancels, do nothing
          // You can optionally show a message to the user indicating that the operation was canceled.
        }
      }
    },
    getTransactions() {
      Api.get('/transactions', { page: 1 })
        .then(response => {
          this.transactions = response.data
          this.error = null
          console.log(this.transactions)
        })
        .catch(error => {
          this.transactions = []
          this.error = 'Error fetching customers: ' + error.message
        })
    },
    getDeliveries() {
      Api.get('/deliveries')
        .then(response => {
          this.deliveries = response.data
          this.error = null
        })
        .catch(error => {
          this.deliveries = []
          this.error = 'Error fetching customers: ' + error.message
        })
    },
    getDeliveriesCount() {
      Api.get('/deliveries/count')
        .then(response => {
          this.deliveriesCount = response.data.count
          this.error = null
        })
        .catch(error => {
          this.deliveriesCount = null
          this.error = 'Error fetching customers: ' + error.message
        })
    }
    // Add more methods for other entities if needed
  }
}
</script>

<style scoped>
.scrollable-list {
  max-height: 250px;
  /* Set the maximum height for the scrollable list */
  overflow-y: auto;
  background-color: rgba(255, 255, 255, 30%);
  /* Enable vertical scrolling */
}

.transparent-list-item {
  background-color: rgba(255, 255, 255, 30%);
}

.product-img {
  width: 80px;
  height: 60px;
}

.container {
  margin-top: 30px;
  margin-bottom: 20px;
}

.container-move-up {
  margin-top: -10px;
}

.larger-text {
  font-size: 16px;
}
</style>
