<template>
  <div class="order-confirmation">
    <b-alert show variant="success" class="btn_message" v-if="dataLoaded">
      <div>Thank you for ordering: <br /></div>
      <div v-for="(item, index) in transactionItems" :key="index">
        {{ item.quantity }} x {{ item.name }}<br />
      </div>
      ---
    <div>Your order will be dispatched shortly.</div>
    </b-alert>

    <div v-else>
      Loading...
    </div>
  </div>
</template>

<script>
import { Api } from '@/Api'

export default {
  props: {
    transactionId: String
  },
  data() {
    return {
      transactionItems: [],
      error: null,
      dataLoaded: false
    }
  },
  created() {
    this.fetchTransactionItems()
  },
  methods: {
    async fetchTransactionItems() {
      if (this.transactionId) {
        try {
          const response = await Api.get(`/transactions/${this.transactionId}/products`)
          this.transactionItems = response.data
          this.error = null

          // Fetch product names for each item
          await Promise.all(this.transactionItems.map(async (item) => {
            try {
              const productResponse = await Api.get(`/products/${item.product}`)
              item.name = productResponse.data.name
            } catch (error) {
              item.name = 'Unknown Product'
              console.error('Error fetching product name:', error.message)
            }
          }))

          this.dataLoaded = true
        } catch (error) {
          this.transactionItems = []
          this.error = 'Error fetching transaction items: ' + error.message
          this.dataLoaded = true // Set dataLoaded to true even on error to avoid infinite loading.
        }
      }
    }
  }
}
</script>

<style scoped>
  .order-confirmation {
    margin: 20px;
  }

  .needs-space {
  margin-bottom: 20px; /* Add some bottom margin to create space */
}
</style>
