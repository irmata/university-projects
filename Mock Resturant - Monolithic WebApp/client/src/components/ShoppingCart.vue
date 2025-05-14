<template>
    <div>
      <b-sidebar id="sidebar-right" title="Shopping Cart" right shadow @hidden="closeCart">
        <div class="cart-content px-3 py-2">
          <div style="text-align: left;">
            <div v-for="(item, index) in cart" :key="index" class="cart-item">
              <div @click="removeItem(index, item._id)">
              {{ item.name }} - ${{ item.price.toFixed(2) }}
              <span class="remove-text" v-if="!isDisabled">Remove</span>
            </div>
            </div>
          </div>
          <div v-if="cart.length > 0">
            <p><strong>Total Price: ${{ getTotalPrice().toFixed(2) }}</strong></p>

            <div class="delivery-options">
      <input type="radio" id="pickup" value="pickup" :disabled="isDisabled" v-model="deliveryOption">
      <label for="pickup">Pick-up  </label>
      <div></div>
      <input type="radio" id="delivery" value="delivery" :disabled="isDisabled" v-model="deliveryOption">
      <label for="delivery">Delivery</label>
      <!-- Add delivery-related form fields here based on selected option -->
      <input v-if="deliveryOption === 'delivery'" :disabled="isDisabled" type="text" v-model="deliveryAddress" placeholder="Enter Address" class="delivery-address">
      <input v-if="deliveryOption === 'delivery'" :disabled="isDisabled" type="text" v-model="phoneNumber" placeholder="Enter Phone" class="delivery-address">
      <!-- Additional delivery options can be added as needed -->
    </div>
            <!-- Proceed to Checkout Button -->
            <div class = "add-space"></div>
            <b-button @click="proceedToCheckout" :disabled="isDisabled">Submit Order</b-button>
            <div v-if="message">
                <b-alert show variant="success">{{ 'Order Confirmed.' }}</b-alert>
            </div>
            <div v-if="errorMessage" class="error-message">
      <b-alert show variant="danger">{{ errorMessage }}</b-alert>
    </div>
          </div>
        </div>
            <!-- Add this line to include the OrderConfirmation component -->
        <order-confirmation v-if="isDisabled" :transactionId="transactionId" />
      </b-sidebar>
    </div>
  </template>

<script>
import { Api } from '@/Api'
import OrderConfirmation from '@/components/OrderConfirmation.vue'

export default {
  components: {
    OrderConfirmation
  },
  props: {
    cart: Array,
    transactionId: String,
    isDisabled: Boolean
  },
  data() {
    return {
      deliveryOption: 'pickup', // Default to pick-up
      deliveryAddress: '', // Store delivery address in local data
      phoneNumber: '', // Store delivery address in local data
      message: '',
      errorMessage: ''
    }
  },
  methods: {
    removeItem(index, productId) {
      if (!this.isDisabled) {
        this.$emit('remove', index)

        // Make a DELETE request to remove the product to the transaction
        Api.delete(`/transactions/${this.transactionId}/products/${productId}`, {
        })
          .then((response) => {
          // Handle success
            this.products = response.data
            this.error = null
          })
          .catch((error) => {
          // Handle error
            this.products = []
            this.error = 'Error adding product to cart: ' + error.message
          })
      }
    },
    getTotalPrice() {
      return this.cart.reduce((total, item) => total + item.price, 0)
    },
    closeCart() {
      this.$emit('close')
    },
    proceedToCheckout() {
      // Check if address is less than 10 characters and phone number is less than 8 characters
      if (this.deliveryOption === 'delivery' && (this.deliveryAddress.length < 10 || this.phoneNumber.length < 8)) {
        // Display an error message to the user
        this.errorMessage = 'Please enter a valid address and phone number.'
        return // Do not proceed if the validation fails
      }

      // Reset the error message (in case it was previously set due to a validation error)
      this.errorMessage = ''

      // Emit an event with cart items, delivery option, and address (if applicable) when proceeding to checkout
      this.$emit('checkout', { cart: this.cart, deliveryOption: this.deliveryOption, deliveryAddress: this.deliveryAddress })
      this.postTransaction()
      if (this.deliveryOption === 'delivery') {
        this.postDelivery()
      }
    },
    postTransaction() {
      Api.patch(`/transactions/${this.transactionId}`, { transactionStatus: 'Completed' })
        .then(response => {
          this.message = response.data.message
          this.$emit('update:isDisabled', true) // Emit an event to update the prop
        })
        .catch(error => {
          this.message = error
        })
    },
    postDelivery() {
      const deliveryData = {
        type: 'Online',
        location: this.deliveryAddress,
        phone: this.phoneNumber,
        customer: '650d7cff19b27a88f50509e9', // You should replace this placeholder with the actual customer ID
        timestamp: new Date().toISOString(), // Get the current timestamp in ISO format
        transaction: this.transactionId,
        deliveryStatus: 'Pending'
      }

      Api.post('/deliveries', deliveryData)
        .then(response => {
          this.message = response.data.message
          this.$emit('update:isDisabled', true) // Emit an event to update the prop
        })
        .catch(error => {
          this.message = 'Error creating delivery: ' + error.message
          console.error(error)
        })
    }
  }
}
</script>

  <style scoped>
  /* Add additional styles as needed */

  /* Style for the shopping cart list */
  .cart-content .cart-item {
    font-family: Arial, sans-serif;
    margin-bottom: 10px;
    position: relative;
  }

  .delivery-address {
    font-family: Arial, sans-serif;
  }

  /* Style for the "Remove from Cart" text */
  .cart-content .remove-text {
    display: none;
    position: relative;
    right: 0;
    cursor: pointer;
    color: red;
  }

  /* Show the "Remove from Cart" text on hover */
  .cart-content .cart-item:hover .remove-text {
    display: block;
  }

  /* Style for delivery options section */
  .cart-content .delivery-options {
    margin-top: 20px;
  }

  .add-space {
  margin-bottom: 20px; /* Add some bottom margin to create space */
}

  /* Additional styles for delivery-related form fields can be added here */
  </style>
