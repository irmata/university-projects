<template>
  <div class="page-container">
    <div class="header">
      <h1>Welcome!</h1>
      <div class="sub-header">
        <span class="tools">All your toothy needs in one place.</span>
        <span class="vertical-line"></span>
        <span class="clinic-info">
          <span class="clinic-label">Your clinic: </span>
          <span class="clinic-name">Hisingen Tandvård</span>
        </span>
      </div>
    </div>
    <div class="dashboard">
      <div class="appointments">
        <h2>Current appointments</h2>
        <div class="appointment-list">
          <!-- One div per appointment with v-for -->
          <div class="appointment" v-for="appointment in appointments" :key="appointment._id">
            <div class="date">{{ appointment.date }} | {{ appointment.time }}</div>
            <button @click="cancelAppointment(appointment._id)">
              <font-awesome-icon icon="angle-right" /> Cancel appointment
            </button>
          </div>
        </div>
      </div>
      <div class="previous-appointments">
        <h2>Previous appointments</h2>
        <div v-if="previousAppointments.length > 0">
          <div class="appointment" v-for="appointment in previousAppointments" :key="appointment._id">
            <div class="date">{{ appointment.date }} | {{ appointment.time }}</div>
            <div class="details">{{ appointment.details }}</div>
          </div>
        </div>
        <div v-else>
          <p>No prior appointments yet.</p>
        </div>
      </div>
      <div class="actions">
        <h2>Manage Appointments</h2>
        <button @click="goToCalendar"><font-awesome-icon icon="angle-right" /> Book appointment</button>
        <h2>Clinics</h2>
        <button @click="goToMap">
          <font-awesome-icon icon="angle-right" /> View clinics</button>
      </div>
    </div>
  </div>
</template>

<script>

import axios from 'axios'
export default {

  name: 'PatientDashboardView',
  data () {
    return {
      appointments: [], // Array of appointments with fetched data
      previousAppointments: [], // Previous appointments
      myIPaddress: 'localhost'
    }
  },
  async mounted () {
    const isLoggedIn = await this.checkLoggedIn()
    if (!isLoggedIn) {
      this.$router.push({ path: '/login', query: { redirected: 'true' } })
    } else {
      this.fetchAppointments()
    }
  },
  created () {
    this.fetchAppointments()
  },
  methods: {
    bookAppointment () {
      // should send the user to the calendar view
    },
    async checkLoggedIn () {
      try {
        const response = await axios.get(`http://${this.myIPaddress}:3000/check-session`, { withCredentials: true })
        return response.data.isLoggedIn
      } catch (error) {
        console.error('Error checking login status:', error)
        return false
      }
    },
    fetchAppointments () {
      axios.get(`http://${this.myIPaddress}:3000/patient/appointments`, { withCredentials: true })
        .then(response => {
          if (response.data && response.data.appointments) {
            const currentDate = new Date()
            this.appointments = response.data.appointments.filter(appointment => new Date(appointment.date) >= currentDate)
            this.previousAppointments = response.data.appointments.filter(appointment => new Date(appointment.date) < currentDate)
          } else {
            console.error('No appointments data in response')
          }
        })
        .catch(error => {
          console.error('Error fetching appointments:', error)
        })
    },
    cancelAppointment (appointmentId) {
      console.log('Cancelling appointment with ID:', appointmentId)
      axios.delete(`http://${this.myIPaddress}:3000/patient/appointments/${appointmentId}`)
        .then(() => {
          console.log(`Appointment ${appointmentId} successfully canceled`)
          this.fetchAppointments() // Refresh the appointments list
          window.location.reload()
        })
        .catch(error => console.error('Failed cancelling appointment', error))
    },
    goToCalendar () {
      this.$router.push('/patient-calendar')
    },
    goToMap () {
      this.$router.push('/map')
    },
    goToChangeClinic () {
      // This should navigate to the route for changing the clinic
      // this.$router.push('/change-clinic');
    }
  }
}
</script>

  <style scoped>
.page-container {
  padding: 20px;
  background: #FFFFFF;
  font-family: "Inter", sans-serif;
}

h1 {
  color: #033058;
  font-size: 2rem;
  margin-bottom: -5px;
}

.sub-header {
  display: flex;
  align-items: center;
}

.header {
  margin-left: 15px;
}

.tools,
.clinic-label {
  color: #326492;
  font-size: 1rem;
}

.clinic-name {
  color: #2174c1;
  font-size: 1rem;
}

.vertical-line {
  background-color: #033058;
  width: 3px;
  height: 50px;
  margin: -15px 15px 0px 25px;
}

.dashboard {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  background: #EEF1FA;
  padding: 20px;
  margin: 10px;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  flex-basis: calc(33% - 20px);
  border-left: 5px solid #2174c1;
}

.appointments,
.previous-appointments,
.actions {
  padding: 20px;
  margin: 10px;
  flex-basis: calc(33% - 20px);
}

.appointment-list {
  margin-top: 10px;
}

.appointment {
  padding: 10px;
  border-bottom: 2px solid #aaa9af;
}

.date {
  font-weight: bold;
  color: #4a4b4c;
}

.details {
  margin-top: 5px;
}

button {
  padding: 10px 20px;
  margin-top: 10px;
  margin-right: 20px;
  background-color: transparent;
  border: none;
  border-radius: 5px;
  color: #2174c1;
  cursor: pointer;
  font-weight: 600;
}

button:hover {
  background-color: #195b9f;
  color: #fff;
}

h2 {
  color: #185a96;
  font-size: 1.5rem;
}

</style>
