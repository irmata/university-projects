<template>
  <div class="auth-container">
    <div class="login-container column" v-if="!isLoggedIn">
      <h2>Log in</h2>
      <p>Log in to your account</p>
      <form class="auth-form" @submit.prevent="onLogin">
        <input type="email" v-model="loginEmail" placeholder="E-mail address..." />
        <input type="password" v-model="loginPassword" placeholder="Password..." />
        <button type="submit">Login</button>
      </form>
    </div>
    <div v-else class="logout-container column">
      <h2>Logged in as {{ loggedInUser }}</h2>
      <button @click="onLogout">Logout</button>
    </div>
    <div class="register-container column">
      <h2>Register</h2>
      <p>Create a new account</p>
      <form class="auth-form" @submit.prevent="onRegister">
        <input type="text" v-model="registerName" placeholder="Name..." />
        <input type="email" v-model="registerEmail" placeholder="E-mail address..." />
        <input type="password" v-model="registerPassword" placeholder="Password..." />
        <button type="submit">Register</button>
      </form>
    </div>
    <div v-if="showToast" class="toast" :class="toastType">{{ toastMessage }}</div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'LoginView',
  data () {
    return {
      loginEmail: '',
      loginPassword: '',
      myIPaddress: 'localhost', // ENTER_IP_ADDRESS
      showToast: false,
      toastMessage: '',
      toastType: '', // 'success' or 'error'
      isLoggedIn: false,
      loggedInUser: '',
      registerName: '',
      registerEmail: '',
      registerPassword: ''
    }
  },
  mounted () {
    this.checkIfRedirected()
    axios.defaults.withCredentials = true
    axios.get(`http://${this.myIPaddress}:3000/check-session`)
      .then(response => {
        if (response.data.isLoggedIn) {
          this.isLoggedIn = true
          this.loggedInUser = response.data.email
          console.log('is logged in as ', this.loggedInUser)
        } else {
          console.log('is not logged in')
        }
      })
      .catch(error => {
        console.error('Error checking session:', error)
      })
  },
  methods: {
    async onLogin () {
      const loginData = {
        email: this.loginEmail,
        password: this.loginPassword
      }
      try {
        const response = await axios.post(`http://${this.myIPaddress}:3000/patient/login/`, loginData)
        if (response.status === 200) {
          console.log('Login successful:', response.data)
          this.isLoggedIn = true
          this.loggedInUser = response.data.email
          this.showToastMessage('Login successful!', 'success')
          this.$router.push('/patient-dashboard')
        } else {
          console.error('Login failed:', response.data)
          this.showToastMessage('Login failed', 'error')
        }
      } catch (error) {
        console.error('Error during login:', error)
        this.showToastMessage('An error occurred during login', error)
      }
    },
    checkIfRedirected () {
      if (this.$route.query.redirected) {
        this.showToastMessage('Please log in to access Bookings', 'info')
      }
    },
    showToastMessage (message, type) {
      this.toastMessage = message
      this.toastType = type
      this.showToast = true
      setTimeout(() => {
        this.showToast = false
      }, 3000) // toast will disappear after 3 seconds
    },
    async onRegister () {
      const registrationData = {
        name: this.registerName,
        email: this.registerEmail,
        password: this.registerPassword
      }
      try {
        const response = await axios.post(`http://${this.myIPaddress}:3000/patient/registration`, registrationData)
        if (response.status === 200) {
          this.showToastMessage('Registration successful!', 'success')
          // could login automatically here, but leaving for now
        } else {
          this.showToastMessage(response.message, 'error')
        }
      } catch (error) {
        console.error('Error during registration:', error)
        if (error.response && error.response.data && error.response.data.message) {
          this.showToastMessage(error.response.data.message, 'error')
        } else {
          this.showToastMessage('An error occurred during registration', 'error')
        }
      }
    },
    async onLogout () {
      try {
        await axios.post(`http://${this.myIPaddress}:3000/patient/logout`)
        this.isLoggedIn = false
        this.loggedInUser = ''
      } catch (error) {
        console.error('Error during logout:', error)
        this.showToastMessage('Logout failed', 'error')
      }
    }
  }
}
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 20px;
  gap: 50px; /* space between columns */
  margin-top: 3rem;
}

.column {
  flex: 1;
  max-width: 500px;
}

.login-container,
.register-container {
  position: relative;
  background-color: #EEF1FA;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.login-container::before,
.register-container::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 10px; /* Width of the colored bar */
  border-radius: 10px 0 0 10px;
  background-color: #2174C1;
}

h2 {
  color: #0C375E;
  font-family: 'Inter', sans-serif;
  font-weight: 700;
  font-size: 24pt;
  padding-left: 20px;
  margin-bottom:-0.75rem;
}

p {
  color: #1A5B97;
  font-family: 'Open Sans', sans-serif;
  font-weight: 600;
  font-size: 18pt;
  padding-left: 20px;
  margin-bottom: 2.25rem;
}

.auth-form {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.auth-form input {
  width: 90%;
  padding: 0.5rem;
  margin-bottom: 1rem;
  margin: 0 auto 1rem;
  border: 1px solid transparent;
  border-radius: 5px;
}

.auth-form input:focus {
  outline: none;
  border: 1px solid #2174C1;
  box-shadow: 0 0 0 2px rgba(33, 116, 193, 1);
}

button {
  width: 50%;
  padding: 0.7rem;
  border: none;
  border-radius: 10px;
  background-color: #2174C1;
  color: white;
  cursor: pointer;
  font-size: 1rem;
  font-family: 'Inter', sans-serif;
  font-weight: 500;
  margin-top: 3rem;
}

button:hover {
  background-color: #175187;
}
.toast {
  position: fixed;
  left: 50%;
  bottom: 20px;
  transform: translateX(-50%);
  padding: 10px;
  border-radius: 5px;
  color: white;
  background-color: black;
  text-align: center;
  z-index: 1000;
}

.toast.success {
  background-color: green;
}

.toast.error {
  background-color: red;
}

</style>
