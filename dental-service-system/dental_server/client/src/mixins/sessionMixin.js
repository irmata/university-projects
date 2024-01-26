import axios from 'axios'

export const sessionMixin = {
  methods: {
    async checkSession () {
      axios.defaults.withCredentials = true
      try {
        const response = await axios.get('http://localhost:3000/check-session')
        if (response.data.isLoggedIn) {
          return response.data.user
        }
        return null
      } catch (error) {
        console.error('Error checking session:', error)
        return null
      }
    }
  }
}
