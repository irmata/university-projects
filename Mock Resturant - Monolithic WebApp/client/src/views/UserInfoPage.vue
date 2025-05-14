<template>
  <div>
    <UserPageSelectors />
    <p class="prompt" :class="{ 'success-prompt': successMessage, 'error-prompt': errorMessage }">
      {{ successMessage || errorMessage }}
    </p>
    <div class="user-details">
      <label for="name" class="input-label">NAME</label>
      <input type="text" v-model="name" :readonly="isReadonly" class="input-field-style" id="name">
      <label for="birthday" class="input-label">BIRTHDAY</label>
      <input type="text" v-model="birthday" :readonly="isReadonly" class="input-field-style" id="birthday">
      <label for="phone" class="input-label">PHONE</label>
      <input type="text" v-model="phone" :readonly="isReadonly" class="input-field-style" id="phone">
      <label for="address" class="input-label">ADDRESS</label>
      <input type="text" v-model="address" :readonly="isReadonly" class="input-field-style" id="address">
    </div>
    <button @click="toggleEditMode" class="edit-button">{{ buttonText }}</button>
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
      name: null,
      birthday: null,
      phone: null,
      address: null,
      originalUserData: {},
      errorMessage: null,
      successMessage: null,
      buttonText: 'EDIT',
      isReadonly: true,
      isEditing: false
    }
  },
  methods: {
    getUserInfo() {
      Api.get('/customers/' + this.customerId)
        .then((response) => {
          this.errorMessage = null
          this.name = response.data.name
          this.birthday = response.data.birthday
          this.formatBirthday()
          this.phone = response.data.phone
          this.address = response.data.address
          this.originalUserData = {
            name: this.name,
            birthday: this.birthday,
            phone: this.phone,
            address: this.address
          }
        })
        .catch((error) => {
          this.errorMessage = 'Error fetching user data: ' + error.message
        })
    },
    postUserInfo() {
      const editedData = {}
      if (this.name !== this.originalUserData.name) {
        editedData.name = this.name
      }
      if (this.birthday !== this.originalUserData.birthday) {
        editedData.birthday = this.birthday
        this.formatBirthday()
      }
      if (this.phone !== this.originalUserData.phone) {
        editedData.phone = this.phone
      }
      if (this.address !== this.originalUserData.address) {
        editedData.address = this.address
      }

      if (Object.keys(editedData).length === 0) {
        // No user data was changed, return without making request
        return
      }

      Api.put('/customers/' + this.customerId, editedData)
        .then((response) => {
          this.errorMessage = null
          this.successMessage = 'User data successfully updated'
          this.originalUserData = { ...this.originalUserData, ...editedData }
        })
        .catch((error) => {
          this.successMessage = null
          this.errorMessage = 'Error updating user data: ' + error.message
        })
    },
    formatBirthday() {
      if (this.birthday) {
        const dateObject = new Date(this.birthday)
        const options = { day: '2-digit', month: '2-digit', year: 'numeric' }
        this.birthday = dateObject.toLocaleDateString(undefined, options)
      }
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
        this.postUserInfo()
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
.user-details {
  margin-top: 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
}
.input-label {
  text-align: center;
  font-size: 20px;
  width: 100px;
}
.input-field-style {
  background-color:  #faf9eb;
  border: 2px solid #0ca789;
  border-radius: 5px;
  padding: 5px;
  text-align: center;
  font-size: 18px;
  width: 400px;
  height: 45px;
  transition: width 0.3s;
}
.input-field-style:focus,
.input-field-style:active {
  outline: none;
}
.input-field-style:read-only {
  background-color: #dbdbd0;
  opacity: 0.8;
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
  .edit-button {
    width: 150px;
  }
}
</style>
