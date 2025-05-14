<template>
  <div>
    <UserPageSelectors />
    <p class="prompt" :class="{ 'success-prompt': successMessage, 'error-prompt': errorMessage }">
      {{ successMessage || errorMessage }}
    </p>
    <div class="user-details" v-if="!isResetting">
      <label for="email" class="input-label">EMAIL</label>
      <input type="text" v-model="email" readonly class="input-field-style" id="email">
      <label for="password" class="input-label">PASSWORD</label>
      <input type="text" v-model="password" :readonly="isReadonly" class="input-field-style" id="password">
    </div>
    <div class="password-reset" v-if="isResetting">
      <label for="oldPassword" class="input-label">ENTER OLD PASSWORD</label>
      <input type="text" v-model="oldPassword" class="input-field-style" id="oldPassword">
      <label for="newPassword" class="input-label">ENTER NEW PASSWORD</label>
      <input type="text" v-model="newPassword" class="input-field-style" id="newPassword">
    </div>
    <button @click="toggleResetMode" class="reset-button">{{ buttonText }}</button>
  </div>
</template>

<script>
import UserPageSelectors from '@/components/UserPageSelectors.vue'
import { Api } from '@/Api'
export default {
  components: {
    UserPageSelectors
  },
  data() {
    return {
      customerId: '651f129cc1748be12d41412a', // testing - set to actual customer id after login
      email: null,
      password: null,
      errorMessage: null,
      successMessage: null,
      oldPassword: null,
      newPassword: null,
      buttonText: 'RESET PASSWORD',
      isReadonly: true,
      isResetting: false
    }
  },
  methods: {
    getUserInfo() {
      Api.get('/customers/' + this.customerId)
        .then((response) => {
          this.errorMessage = null
          this.email = response.data.email
          this.maskPassword(response.data.password)
        })
        .catch((error) => {
          this.errorMessage = 'Error fetching user data: ' + error.message
        })
    },
    getPassword() {
      Api.get('/customers/' + this.customerId)
        .then((response) => {
          this.maskPassword(response.data.password)
        })
        .catch((error) => {
          this.errorMessage = 'Error fetching user data: ' + error.message
        })
    },
    maskPassword(password) {
      this.password = password ? '●'.repeat(password.length) : null
    },
    resetPassword() {
      Api.get('/customers/' + this.customerId)
        .then((response) => {
          this.errorMessage = null
          if (this.oldPassword === null || this.newPassword === null) {
            this.errorMessage = 'Password reset cancelled'
          } else if (this.oldPassword !== response.data.password) {
            this.errorMessage = 'Old password does not match'
            this.oldPassword = null
            this.newPassword = null
          } else {
            const editedPassword = {}
            editedPassword.password = this.newPassword
            Api.put('/customers/' + this.customerId, editedPassword)
              .then((response) => {
                this.successMessage = 'Password successfully updated'
                this.oldPassword = null
                this.newPassword = null
                this.getPassword()
              })
              .catch((error) => {
                this.successMessage = null
                this.errorMessage = 'Error updating user data: ' + error.message
              })
          }
        })
        .catch((error) => {
          this.errorMessage = 'Error fetching user data: ' + error.message
        })
    },
    toggleResetMode() {
      this.isResetting = !this.isResetting
      if (this.isResetting) {
        this.successMessage = null
        this.errorMessage = null
        this.buttonText = 'SAVE'
        this.isReadonly = false
      } else {
        this.buttonText = 'RESET PASSWORD'
        this.isReadonly = true
        this.resetPassword()
      }
    }
  },
  created() {
    this.getUserInfo()
  }
}
</script>

<style>
.error-prompt,
.success-prompt {
  margin-top: 40px;
  text-decoration: none;
  text-align: center;
  color: red;
  font-size: 18px;
  transition: text-decoration 0.3s, font-size 0.3s, margin-top 0.3s;
}
.success-prompt {
  color: green;
}
.user-details,
.password-reset {
  margin-top: 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
}
.password-reset {
  white-space: nowrap;
}
.input-label {
  text-align: center;
  font-size: 20px;
  width: 230px;
}
.reset-button {
  background-color: #faf9eb;
  border: 2px solid #0ca789;
  margin-top: 40px;
  margin-bottom: 40px;
  border-radius: 5px;
  padding: 5px;
  text-align: center;
  font-size: 18px;
  width: 225px;
  height: 45px;
  transition: width 0.3s;
}
/* Media queries for responsive design */
@media (max-width: 550px) {
  .user-details {
    margin-top: 20px;
  }
  .input-field-style {
    width: 250px;
    font-size: 14px;
  }
  .error-prompt,
  .success-prompt {
    font-size: 12px;
  }
  .reset-button {
    width: 175px;
    font-size: 14px;
  }
}
</style>
