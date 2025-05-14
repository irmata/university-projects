<template>
    <div>
        <UserPageSelectors />
        <p class="prompt" :class="{ 'success-prompt': successMessage, 'error-prompt': errorMessage }">
            {{ successMessage || errorMessage }}
        </p>
        <div class="card-banner">
            <img :src="cardBanner" alt="Payment Card">
        </div>
        <div class="user-details">
            <label for="bankCardNumber" class="input-label">CARD NUMBER</label>
            <input type="text" v-model="bankCardNumber" :readonly="isReadonly" class="input-field-style" id="bankCardNumber">
            <label for="expiry" class="input-label">EXPIRY</label>
            <input type="text" v-model="expiry" :readonly="isReadonly" class="input-field-style" id="expiry">
            <label for="cvc" class="input-label">CVC</label>
            <input type="text" v-model="cvc" :readonly="isReadonly" class="input-field-style" id="cvc">
        </div>
        <button @click="toggleEditMode" class="edit-button">{{ buttonText }}</button>
    </div>
</template>

<script>
import UserPageSelectors from '@/components/UserPageSelectors.vue'
import cardBanner from '@/assets/images/banners/cardBanner.png'
import { Api } from '@/Api'

export default {
  components: {
    UserPageSelectors
  },
  data() {
    return {
      cardBanner,
      customerId: '651f129cc1748be12d41412a', // testing - set to actual customer id after login
      errorMessage: null,
      successMessage: null,
      isReadonly: true,
      isEditing: false,
      buttonText: 'EDIT',
      type: null,
      bankCardNumber: null,
      expiry: null,
      cvc: null
    }
  },
  methods: {
    getUserInfo() {
      Api.get('/customers/' + this.customerId)
        .then((response) => {
          this.errorMessage = null
          const firstPaymentMethod = response.data.paymentMethods[0]
          this.type = firstPaymentMethod.type
          this.bankCardNumber = firstPaymentMethod.bankCardNumber
          this.expiry = firstPaymentMethod.expiry
          this.cvc = firstPaymentMethod.cvc
          this.originalUserData = {
            type: this.type,
            bankCardNumber: this.bankCardNumber,
            expiry: this.expiry,
            cvc: this.cvc
          }
        })
        .catch((error) => {
          this.errorMessage = 'Error fetching user data: ' + error.message
        })
    },
    postUserChange() {
      const editedData = {}
      if (this.bankCardNumber !== this.originalUserData.bankCardNumber) {
        editedData.bankCardNumber = this.bankCardNumber
      }
      if (this.expiry !== this.originalUserData.expiry) {
        editedData.expiry = this.expiry
      }
      if (this.cvc !== this.originalUserData.cvc) {
        editedData.cvc = this.cvc
      }

      if (Object.keys(editedData).length === 0) {
        // No user data was changed, return without making request
        return
      }

      Api.put('/customers/' + this.customerId, editedData)
        .then((response) => {
          this.errorMessage = null
          this.successMessage = 'Payment info successfully updated'
          this.originalUserData = { ...this.originalUserData, ...editedData }
        })
        .catch((error) => {
          this.successMessage = null
          this.errorMessage = 'Error updating payment info: ' + error.message
        })
    },
    maskCardNumber(bankCardNumber) {
      const maskedPart = '●'.repeat(bankCardNumber.length - 4)
      const visiblePart = bankCardNumber.slice(-4)
      const maskedWithSpaces = maskedPart.match(/.{1,4}/g).join(' ')
      this.bankCardNumber = maskedWithSpaces + ' ' + visiblePart
    },
    toggleEditMode() {
      this.isEditing = !this.isEditing
      if (this.isEditing) {
        this.successMessage = null
        this.errorMessage = null
        this.buttonText = 'SAVE'
        this.isReadonly = false
      } else {
        this.buttonText = 'EDIT'
        this.isReadonly = true
        this.postUserChange()
        this.getUserInfo()
      }
    }
  },
  created() {
    this.getUserInfo()
  }
}
</script>

<style>
.card-banner {
  display: block;
  margin: 40px auto;
}
.card-banner img {
  max-width: 100%;
  max-height: 100%;
  width: auto;
  height: auto;
}
.edit-button {
  background-color: #faf9eb;
  border: 2px solid #0ca789;
  margin-top: 40px;
  margin-bottom: 40px;
  border-radius: 5px;
  padding: 5px;
  text-align: center;
  font-size: 18px;
  width: 200px;
  height: 45px;
  transition: width 0.3s;
}
/* Media query for smaller screens */
@media (max-width: 1100px) {
  .card-banner {
    width: 350px;
    height: 209px;
  }
}
@media (max-width: 550px) {
  .card-banner {
    width: 175px;
    height: 175px;
  }
  .edit-button {
    width: 150px;
  }
}
</style>
