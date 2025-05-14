<template>
  <div class="card mb-4" @click="addToCart">
    <div class="image-container">
      <img :src="getProductImagePath(product)" class="card-img-top" alt="Product Image">
      <div class="overlay">
        <span v-if="!isDisabled" class="message">{{ cartMessage }}</span>
      </div>
    </div>
    <div class="card-body">
      <h5 class="card-title">{{ product.name }}</h5>
      <p class="card-text">${{ product.price }} - {{ product.description }}</p>
    </div>
  </div>
</template>

<script>
import { Api } from '@/Api'

export default {
  data() {
    return {
      cartMessage: 'Add to Order'
    }
  },
  props: {
    product: Object,
    transactionId: String,
    isDisabled: Boolean
  },
  methods: {
    addToCart() {
      if (!this.isDisabled) {
        // Change the message to +1
        this.cartMessage = 'Item Added'
        setTimeout(() => {
          this.cartMessage = 'Add to Order'
        }, 400) // Reset the message after 1 second (1000 milliseconds)

        // Emit the 'add-to-cart' event with the product data
        this.$emit('add-to-cart', this.product)

        // First, make a POST request to add the product to the transaction
        Api.post(`/transactions/${this.transactionId}/products`, { product: this.product._id, quantity: 1 })
          .then(() => {
            // Next, make a GET request to obtain the current amount
            return Api.get(`/transactions/${this.transactionId}`)
          })
          .then((response) => {
            // Handle success
            const currentAmount = response.data.totalAmount

            // Calculate the new total amount rounded to two decimal places
            const newTotalAmount = (currentAmount + this.product.price).toFixed(2)

            // Finally, make a PATCH request to update the total cost
            return Api.patch(`/transactions/${this.transactionId}`, {
              totalAmount: newTotalAmount
            })
          })
          .then((response) => {
            // Handle success if needed
            // Do something with the response if necessary
          })
          .catch((error) => {
            // Handle error
            this.error = 'Error adding product to cart: ' + error.message
          })
      }
    },
    getProductImagePath(product) {
      const filename = product.name.replace(/ /g, '-').toLowerCase()
      return require(`@/assets/images/${filename}.jpg`)
    }
  }
}
</script>

<style>
.card-img-top {
  transition: opacity 0.1s ease;
  /* Add a smooth transition effect */
  width: 100%;
  height: auto;
}
.image-container {
  position: relative;
}
.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}
.overlay:hover {
  opacity: 1;
}
.message {
  color: white;
  font-size: 18px;
}
.card-body {
  overflow: hidden;
}
</style>
