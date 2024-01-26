<template>
  <div class="page-container">
    <div class="header">
      <h1>My Schedule</h1>
    </div>
    <div v-if="isLoading" class="loading-container">
      <div class="spinner"></div>
    </div>
    <div v-if="!isAppointmentConfirmed" class="calendar-container">
      <div class="calendar">
        <div class="box-header">
          <button @click="decrementMonth">‹</button>
          <span>{{ monthNames[currentMonth] }} {{ currentYear }}</span>
          <button @click="incrementMonth">›</button>
        </div>
        <div class="weekdays">
          <span v-for="day in weekDays" :key="day">{{ day }}</span>
        </div>
        <div class="days">
          <span v-for="day in previousMonthTrailingDays" :key="day" class="day other-month not-clickable">{{ day }}</span>
          <span v-for="day in numberOfDaysInMonth" :key="day" class="day" :class="{
            'is-today': isToday(day),
            'not-clickable': !isSelectable(day),
            'weekend': isWeekend(day),
            'selected': isSelected(day)
          }" @click="isSelectable(day) ? selectDate(day) : null">
            {{ day }}
          </span>
          <span v-for="day in nextMonthLeadingDays" :key="day" class="day other-month not-clickable">{{ day }}</span>
        </div>
      </div>
      <div class="time-slots">
        <div class="box-header">
          <span>Appointments</span>
        </div>
        <div v-for="appointment in appointments" :key="appointment.id" class="appointment-slot">
          {{ appointment.date }} - {{ appointment.time }} - {{ appointment.patientName }}
        </div>
      </div>
    </div>
    <!--  <div v-if="isAppointmentConfirmed" class="confirmation-container">
      <h2>Your Appointment is Scheduled</h2>
      <p><u>Visiting Details</u></p>
      <p>{{ selectedDate }}</p>
      <p>{{ selectedTime }}</p>
      <p>An email confirmation will be sent to you shortly.</p>
    </div> -->
    <transition name="fade">
      <div v-if="selectedDate && selectedTime && !isAppointmentConfirmed" class="confirm-button-container">
        <h2>An appointment on {{ selectedDate }} at {{ selectedTime }}</h2>
        <button @click="confirmAppointment" class="confirm-button">
          Confirm
        </button>
      </div>
    </transition>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data () {
    const today = new Date()
    return {
      today,
      selectedDate: null,
      selectedTime: null,
      currentMonth: today.getMonth(),
      currentYear: today.getFullYear(),
      isAppointmentConfirmed: false,
      appointments: [], // Array of appointments with fetched data
      openingTime: 9,
      closingTime: 17,
      isLoading: false,
      monthNames: [
        'January', 'February', 'March', 'April', 'May', 'June', 'July',
        'August', 'September', 'October', 'November', 'December'
      ],
      weekDays: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']
    }
  },
  computed: {
    maxYear () {
      return this.currentYear + 1
    },
    canIncrementMonth () {
      const today = new Date()
      const currentYear = today.getFullYear()
      const currentMonth = today.getMonth()

      if (this.currentYear === currentYear + 1 && this.currentMonth >= currentMonth) {
        return false
      }

      return this.currentYear <= currentYear + 1
    },
    canDecrementMonth () {
      return (
        this.currentYear > this.today.getFullYear() ||
        (this.currentYear === this.today.getFullYear() && this.currentMonth > this.today.getMonth())
      )
    },
    numberOfDaysInMonth () {
      return new Date(this.currentYear, this.currentMonth + 1, 0).getDate()
    },
    startDayOfMonth () {
      return new Date(this.currentYear, this.currentMonth, 1).getDay()
    },
    previousMonthTrailingDays () {
      const days = []
      for (let i = this.startDayOfMonth; i > 0; i--) {
        days.push(new Date(this.currentYear, this.currentMonth, -i + 1).getDate())
      }
      return days
    },
    nextMonthLeadingDays () {
      const days = []
      const daysInNextMonth = (7 - (this.numberOfDaysInMonth + this.startDayOfMonth) % 7) % 7
      for (let i = 1; i <= daysInNextMonth; i++) {
        days.push(i)
      }
      return days
    },
    timeSlots () {
      const slots = []
      for (let hour = this.openingTime; hour < this.closingTime; hour++) {
        slots.push(`${hour}:00 - ${hour + 1}:00`)
      }
      return slots
    }
  },
  methods: {
    isToday (day) {
      return day === this.today.getDate() && this.currentMonth === this.today.getMonth() && this.currentYear === this.today.getFullYear()
    },
    isSelectable (day) {
      // Return the negation of isWeekend(day)
      return !this.isWeekend(day)
    },
    isSelected (day) {
      if (!this.selectedDate) {
        return false
      }
      const month = String(this.currentMonth + 1).padStart(2, '0')
      const dayString = String(day).padStart(2, '0')
      const dateForComparison = `${this.currentYear}-${month}-${dayString}`

      return this.selectedDate === dateForComparison
    },
    isWeekend (day) {
      const date = new Date(this.currentYear, this.currentMonth, day)
      return date.getDay() === 0 || date.getDay() === 6
    },
    getNextMonday (date) {
      const nextMonday = new Date(date)
      nextMonday.setDate(nextMonday.getDate() + ((7 - nextMonday.getDay()) % 7 || 7))
      return nextMonday
    },
    incrementMonth () {
      if (this.canIncrementMonth) {
        if (this.currentMonth < 11) {
          this.currentMonth++
        } else if (this.currentYear < this.maxYear) {
          this.currentMonth = 0
          this.currentYear++
        }
      }
    },
    decrementMonth () {
      if (this.canDecrementMonth) {
        if (this.currentMonth > 0) {
          this.currentMonth--
        } else if (this.currentYear > this.today.getFullYear()) {
          this.currentMonth = 11
          this.currentYear--
        }
      }
    },
    selectDate (day) {
      const month = String(this.currentMonth + 1).padStart(2, '0')
      const dayString = String(day).padStart(2, '0')
      const formattedDate = `${this.currentYear}-${month}-${dayString}`
      this.selectedDate = formattedDate
    },
    selectTimeSlot (time) {
      this.selectedTime = time
    },
    confirmAppointment () {
      this.isLoading = true
      // Format: yyyy-mm-dd 00:00 - 00:00
      // Handle appointment creation
      console.log(this.selectedDate, this.selectedTime)

      // Loading animation
      setTimeout(() => {
        this.isLoading = false
        this.isAppointmentConfirmed = true
      }, 1300)
    },
    fetchAppointments () {
      axios.get('/dentists/:id/appointments')
        .then(response => {
          this.appointments = response.data // Assign response data to appointments array
        })
        .catch(error => {
          console.error('There was an error fetching the appointments:', error)
          // Handle error appropriately
        })
    }
  },
  mounted () {
    this.fetchAppointments()
  }
}
</script>

<style scoped>
.header {
  margin-left: 15px;
  color: #033058;
}

.page-container {
  padding: 20px;
  background: #FFFFFF;
  font-family: "Inter", sans-serif;
}

.calendar-container {
  display: flex;
  justify-content: space-between;
}

.box-header {
  font-size: 20px;
  color: #003366;
  display: flex;
  justify-content: space-between;
  padding: 1em;
}

.box-header button {
  cursor: pointer;
  font-size: 20px;
  color: #003366;
  border: none;
}

.calendar {
  background: #EEF1FA;
  padding: 20px;
  margin: 10px;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  flex-basis: calc(66% - 20px);
  border-left: 5px solid #2174c1;
}

.time-slots {
  width: 20%;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-left: 1px solid #ddd;
  padding-left: 20px;
  background: #EEF1FA;
  padding: 20px;
  margin: 10px;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  flex-basis: calc(33% - 20px);
  border-left: 5px solid #2174c1;
}

.time-slot {
  width: 60%;
  text-align: center;
  padding: 5px;
  border-radius: 5px;
  border: 2px solid #195b9f;
  color: #033058;
  margin: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.time-slot:hover {
  background-color: #d0eaff;
}

.time-slot.selected {
  background-color: #d0eaff;
}

.weekdays {
  margin-bottom: 5px;
}

.weekdays,
.days {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  text-align: center;
  color: #033058;
}

.day:not(.other-month):not(.not-clickable):not(.weekend):hover {
  cursor: pointer;
  background-color: #d0eaff;
  transition: background-color 0.3s;
}

.day {
  padding: 10px;
  border: 1px solid #b8c1d7;
}

.not-clickable {
  pointer-events: none;
  color: #b8c1d7;
}

.selected {
  background-color: #d0eaff;
}

.is-today {
  background-color: #478ac9;
  color: white;
}

.weekend {
  color: #b8c1d7;
}

.fade-enter-active {
  transition: opacity 0.3s;
}

.fade-enter-from {
  opacity: 0;
}

.confirm-button-container {
  text-align: center;
  color: #033058;
  margin-top: 20px;
}

.confirm-button {
  display: inline-block;
  font-family: "Inter", sans-serif;
  padding: 10px 20px;
  background-color: #195b9f;
  color: white;
  width: 300px;
  height: 40px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.confirmation-container {
  font-size: 20px;
  background: #EEF1FA;
  border-left: 5px solid #2174c1;
  color: #033058;
  padding: 20px;
  margin: 10px;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.loading-container {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
}

.spinner {
  border: 4px solid rgba(0, 0, 0, 0.1);
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border-left-color: #09f;
  animation: spin 1s ease infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>
