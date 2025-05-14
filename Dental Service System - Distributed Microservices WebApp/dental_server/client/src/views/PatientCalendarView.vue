<template>
  <!-- Page container -->
  <div class="page-container">

    <!-- Header section -->
    <div class="header">
      <h1>Book an Appointment</h1>
    </div>

    <!-- Clinic dropdown -->
    <span class="clinic-dropdown">
      <label for="clinicSelect">Clinic: </label>
      <select id="clinicSelect" v-model="selectedClinicId">
        <option disabled value="">Select a clinic</option>
        <option v-for="clinic in clinics" :key="clinic.id" :value="clinic.id">
          {{ clinic.name }}
        </option>
      </select>
    </span>

    <!-- Loading spinner -->
    <div v-if="isLoading" class="loading-container">
      <div class="spinner"></div>
    </div>

    <!-- Calendar container -->
    <div v-if="!isAppointmentConfirmed" class="calendar-container">
      <!-- Calendar view -->
      <div class="calendar">
        <!-- Calendar navigation header -->
        <div class="box-header">
          <button @click="decrementMonth">‹</button>
          <span>{{ monthNames[currentMonth] }} {{ currentYear }}</span>
          <button @click="incrementMonth">›</button>
        </div>
        <!-- Weekday labels -->
        <div class="weekdays">
          <span v-for="day in weekDays" :key="day">{{ day }}</span>
        </div>
        <!-- Calendar days -->
        <div class="days">
          <!-- Previous month's trailing days -->
          <span v-for="day in previousMonthTrailingDays" :key="day" class="day other-month not-clickable">{{ day }}</span>
          <!-- Current month's days -->
          <span v-for="day in numberOfDaysInMonth" :key="day" class="day" :class="{
            'is-today': isToday(day),
            'not-clickable': !isSelectable(day) || isDayFullyBooked(day),
            'weekend': isWeekend(day),
            'selected': isSelected(day),
            'available': availableDays.includes(day) // highlight available days
            }"
            @click="isSelectable(day) && !isDayFullyBooked(day) ? selectDate(day) : null"
          >
            {{ day }}
          </span>
          <!-- Next month's leading days -->
          <span v-for="day in nextMonthLeadingDays" :key="day" class="day other-month not-clickable">{{ day }}</span>
        </div>
      </div>

      <!-- Time slots & clinics section -->
      <div class="time-slots">
        <!-- Time slots header -->
        <div class="box-header" style="justify-content: center">
          <span>Available time slots for {{ selectedDate }}</span>
        </div>
        <!-- Conditional List of time slots based on selected day -->
        <div class="scrollable-time-slots" v-if="selectedDate">
          <div v-for="slot in availableSlotsForSelectedDate" :key="slot.time" class="time-slot"
              :class="{ 'selected': selectedTime === slot.time }"
              @click="selectTimeSlot(slot.time)">
            {{ slot.time }}
          </div>
        </div>
      </div>
    </div>

    <!-- Appointment confirmation section -->
    <div v-if="isAppointmentConfirmed" class="confirmation-container">

      <!-- Return to calendar button -->
      <div class="box-header">
        <button @click="ReturnToCalendar">‹</button>
      </div>

      <h2>Your Appointment is Scheduled</h2>
      <p><u>Visiting Details</u></p>
      <p>{{ selectedDate }}</p>
      <p>{{ selectedTime }}</p>
      <p>An email confirmation will be sent to you shortly.</p>
      <button @click="goToDashboard" class="dashboard-button">
        Go to bookings
      </button>
    </div>

    <!-- Confirmation button section -->
    <transition name="fade">
      <div v-if="selectedDate && selectedTime && !isAppointmentConfirmed" class="confirm-button-container" ref="confirmButtonContainer">
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
import { sessionMixin } from '@/mixins/sessionMixin'

export default {
  mixins: [sessionMixin],
  data () {
    const today = new Date()
    return {
      today,
      selectedDate: null,
      selectedTime: null,
      selectedClinic: 'Azure Center',
      currentMonth: today.getMonth(),
      dentists: [],
      clinics: [],
      localMockTimeSlots: [], // Local copy of mockTimeSlots
      currentYear: today.getFullYear(),
      isAppointmentConfirmed: false,
      bookedSlotsByDate: {}, // Object to track booked slots for each day
      openingTime: null, // Will be set dynamically
      closingTime: null, // Will be set dynamically
      isLoading: false,
      myIPaddress: 'localhost', // ENTER_IP_ADDRESS
      monthNames: [
        'January', 'February', 'March', 'April', 'May', 'June', 'July',
        'August', 'September', 'October', 'November', 'December'
      ],
      weekDays: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
      selectedClinicId: null, // to keep track of the selected clinic
      timeSlotsForClinic: [], // holds the time slots for the selected clinic
      patientId: null,
      unavailableDates: []
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
      if (this.openingTime !== null && this.closingTime !== null) {
        for (let hour = this.openingTime; hour < this.closingTime; hour++) {
          slots.push(`${hour}:00 - ${hour + 1}:00`)
        }
      }
      return slots
    },
    // Computed property to get available days for the selected clinic
    availableDays () {
      const daysInMonth = this.numberOfDaysInMonth
      return Array.from({ length: daysInMonth }, (_, i) => i + 1)
        .filter(day => !this.unavailableDates.includes(day))
    },
    availableSlotsForSelectedDate () {
      const bookedSlots = this.bookedSlotsByDate[this.selectedDate] || []
      return this.timeSlotsForClinic.filter(slot => !bookedSlots.includes(slot.time))
    }
  },
  methods: {
    async fetchUnavailableDatesForClinic () {
      try {
        const response = await axios.post(`http://${this.myIPaddress}:3000/calendar/timeslots/`, {
          dentalClinicId: this.selectedClinicId
        })
        this.unavailableDates = response.data.unavailableDates.map(date => new Date(date).getDate())
      } catch (error) {
        console.error('Error fetching unavailable dates', error)
      }
    },
    isToday (day) {
      return day === this.today.getDate() && this.currentMonth === this.today.getMonth() && this.currentYear === this.today.getFullYear()
    },
    isSelectable (day) {
      // Return false if no clinic is selected
      if (!this.selectedClinicId) {
        return false
      }

      const dateToCheck = new Date(this.currentYear, this.currentMonth, day)
      const today = new Date()
      today.setHours(0, 0, 0, 0) // Reset time to 00:00:00 for accurate comparison

      // Check if the date is in the past
      if (dateToCheck < today) {
        return false
      }

      const formattedDate = `${this.currentYear}-${String(this.currentMonth + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`
      // Check if the day is fully booked
      const daySlots = this.bookedSlotsByDate[formattedDate]
      const isFullyBooked = daySlots && daySlots.length >= this.timeSlots.length
      return !isFullyBooked && this.availableDays.includes(day)
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
    ReturnToCalendar () {
      this.isAppointmentConfirmed = false
      this.refreshSlots() // Call the refresh method
    },
    updateBookedSlots (date, slots) {
    // Assuming slots is an array of time slots that were just booked
      if (!this.bookedSlotsByDate[date]) {
        this.bookedSlotsByDate[date] = []
      }
      this.bookedSlotsByDate[date].push(...slots)
    },
    isDayFullyBooked (day) {
      const date = `${this.currentYear}-${String(this.currentMonth + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`
      const bookedSlots = this.bookedSlotsByDate[date] || []
      return bookedSlots.length >= this.timeSlots.length
    },
    // Method to fetch dentists based on the selected clinic
    async fetchDentistsForSelectedDate () {
      try {
        const response = await axios.post(`http://${this.myIPaddress}:3000/calendar/dentists/`, {
          dentalClinicId: this.selectedClinicId
        })
        this.dentists = response.data
      } catch (error) {
        console.error('Error fetching dentists:', error)
      }
    },
    // Method to fetch unavailable slots for a specific clinic
    async fetchSlotsForClinic (clinicId, selectedDate) {
      try {
        // Fetch unavailable slots for the clinic
        const response = await axios.post(`http://${this.myIPaddress}:3000/calendar/timeslots/`, {
          dentalClinicId: clinicId,
          date: this.selectedDate
        })

        const unavailableSlots = response.data.unavailableSlots
        // Mark these slots as unbookable
        this.updateBookedSlots(selectedDate, unavailableSlots)

        // Find the selected clinic to get its opening and closing times
        const selectedClinic = this.clinics.find(clinic => clinic.id === clinicId)
        if (selectedClinic) {
          this.openingTime = parseInt(selectedClinic.opening_time.split(':')[0], 10)
          this.closingTime = parseInt(selectedClinic.closing_time.split(':')[0], 10)
          const appointmentDuration = selectedClinic.default_appointment_duration

          // Calculate available slots based on the selected date
          this.timeSlotsForClinic = this.calculateAvailableSlotsForDate(selectedDate, this.openingTime, this.closingTime, unavailableSlots, appointmentDuration)
        }
      } catch (error) {
        console.error('Error fetching slots for clinic:', error)
      }
    },

    calculateAvailableSlotsForDate (selectedDate, openingTime, closingTime, unavailableSlots, appointmentDuration) {
      const availableSlots = []
      const durationInMinutes = appointmentDuration // Duration in minutes
      let currentTime = openingTime * 60 // Convert opening time to minutes
      const closingTimeInMinutes = closingTime * 60 // Convert closing time to minutes

      while (currentTime + durationInMinutes <= closingTimeInMinutes) {
        const startHour = Math.floor(currentTime / 60)
        const startMinutes = currentTime % 60
        const endHour = Math.floor((currentTime + durationInMinutes) / 60)
        const endMinutes = (currentTime + durationInMinutes) % 60

        const formattedStartHour = startHour < 10 ? `0${startHour}` : `${startHour}`
        const formattedStartMinutes = startMinutes < 10 ? `0${startMinutes}` : `${startMinutes}`
        const formattedEndHour = endHour < 10 ? `0${endHour}` : `${endHour}`
        const formattedEndMinutes = endMinutes < 10 ? `0${endMinutes}` : `${endMinutes}`

        // Construct the time slot string
        const slot = `${formattedStartHour}:${formattedStartMinutes} - ${formattedEndHour}:${formattedEndMinutes}`

        // Check if the time slot is not in the list of unavailable slots
        if (!unavailableSlots.includes(slot)) {
          availableSlots.push({ time: slot, isAvailable: true })
        }
        currentTime += durationInMinutes // Move to the next slot
      }
      return availableSlots
    },
    selectDate (day) {
      const month = String(this.currentMonth + 1).padStart(2, '0')
      const dayString = String(day).padStart(2, '0')
      const formattedDate = `${this.currentYear}-${month}-${dayString}`
      this.selectedDate = formattedDate
      // Fetch available slots for the selected date
      if (this.selectedClinicId) {
        this.fetchSlotsForClinic(this.selectedClinicId, formattedDate)
      }
    },
    selectTimeSlot (time) {
      this.selectedTime = time
      // Wait for the DOM to update
      this.$nextTick(() => {
        // Check if the confirmation button container is rendered
        if (this.$refs.confirmButtonContainer) {
          // Scroll the confirm button container into view
          this.$refs.confirmButtonContainer.scrollIntoView({ behavior: 'smooth' })
        }
      })
    },
    async confirmAppointment () {
      this.isLoading = true
      // Mock appointment details, should actually come from the user's session instead
      const appointmentDetails = {
        dentalClinicId: this.selectedClinicId,
        date: this.selectedDate,
        time: this.selectedTime,
        dentistId: '65687ca72cbbbc27aa512bf2', // Placeholder dentist ID]
        patientId: this.patientId
      }

      // Log the appointment details to the console
      console.log('Sending appointment details:', appointmentDetails)

      try {
        const response = await axios.post(`http://${this.myIPaddress}:3000/dashboard/booking/${this.selectedClinicId}/slots/`, appointmentDetails)
        console.log('Appointment booked successfully:', response.data)
        this.isAppointmentConfirmed = true

        // Add the booked time slot to the bookedSlotsByDate for the selected date
        if (!this.bookedSlotsByDate[this.selectedDate]) {
          this.bookedSlotsByDate[this.selectedDate] = []
        }
        this.bookedSlotsByDate[this.selectedDate].push(this.selectedTime)
        this.bookedSlotsByDate[this.selectedDate] = [...new Set(this.bookedSlotsByDate[this.selectedDate])]

        // Update the slots to mark the booked slot as unavailable
        this.markSlotAsBooked(this.selectedDate, this.selectedTime)
        // Call the refresh method
        await this.refreshSlots()
      } catch (error) {
        console.error('Error booking appointment:', error)
      } finally {
        this.isLoading = false
      }

      // Loading animation
      setTimeout(() => {
        this.isLoading = false
        this.isAppointmentConfirmed = true
      }, 1300)
    },
    markSlotAsBooked (date, bookedTime) {
      this.timeSlotsForClinic = this.timeSlotsForClinic.map(slot => {
        if (slot.time === bookedTime) {
          return { ...slot, isAvailable: false }
        }
        return slot
      })
    },
    // Method to remove booked slot from calendar
    removeBookedSlot (date, time) {
    // Find the index of the slot that matches the booked time
      const slotIndex = this.timeSlotsForClinic.findIndex(slot => slot.time === time)
      if (slotIndex !== -1) {
        // Update the isAvailable property of the slot to false
        this.timeSlotsForClinic[slotIndex].isAvailable = false
      }
    },
    async refreshSlots () {
      if (this.selectedDate && this.selectedClinicId) {
        await this.fetchSlotsForClinic(this.selectedClinicId, this.selectedDate)
      }
    },
    // Mark unavailable slots based on mock data
    processUnavailableDates () {
      // Clear previously fetched time slots
      this.timeSlotsForClinic = []

      // Update localMockTimeSlots to reflect the unavailable dates for the selected clinic
      const daysInMonth = this.numberOfDaysInMonth
      const unavailableDates = this.getUnavailableDatesForClinic(this.selectedClinicId, daysInMonth)

      // Filtering localMockTimeSlots based on available dates
      this.localMockTimeSlots = this.localMockTimeSlots.filter(slot => {
        const slotDate = new Date(slot.date).getDate()
        return !unavailableDates.includes(slotDate) || slot.dentalClinicId !== this.selectedClinicId
      })
    },

    getUnavailableDatesForClinic (clinicId, daysInMonth) {
      return this.localMockTimeSlots
        .filter(slot => slot.dentalClinicId === clinicId)
        .map(slot => new Date(slot.date).getDate())
        .filter(date => date <= daysInMonth)
    },
    // Method to fetch the list of dental clinics
    async fetchDentalClinics () {
      try {
        const response = await axios.get(`http://${this.myIPaddress}:3000/calendar/dentalclinics/`)
        console.log('Fetching clinics...', response.data)
        this.clinics = response.data.map(clinic => ({
          id: clinic._id,
          name: clinic.name,
          opening_time: clinic.opening_time,
          closing_time: clinic.closing_time,
          default_appointment_duration: clinic.default_appointment_duration
        }))
      } catch (error) {
        console.error('Error fetching clinics:', error)
      }
    },
    fetchDentists () {
      if (!this.selectedClinicId) return

      const requestData = {
        dentalClinicId: this.selectedClinicId
      }

      axios.post(`http://${this.myIPaddress}:3000/calendar/dentists/`, requestData)
        .then(response => {
          // Assuming you have a dentists data property - to fix
          this.dentists = response.data
          console.log('Dentists:', this.dentists)
        })
        .catch(error => {
          console.error('Error fetching dentists:', error)
        })
    },
    goToDashboard () {
      this.$router.push('/patient-dashboard')
    }
  },
  created () {
    this.fetchDentalClinics() // Fetch clinics when component is created
  },
  async mounted () {
    this.refreshSlots()
    this.patientId = await this.checkSession()
  },
  watch: {
    selectedClinicId (newVal) {
      if (newVal) {
        this.fetchUnavailableDatesForClinic()
        this.fetchSlotsForClinic(newVal)
        this.fetchDentistsForSelectedDate() // Fetch dentists when a clinic is selected
      }
    },
    selectedDate (newVal, oldVal) {
      if (newVal !== oldVal && this.selectedClinicId) {
        this.fetchSlotsForClinic(this.selectedClinicId) // Fetch slots when a date is selected
      }
    }
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

.clinic-dropdown {
  margin-left: 15px;
  color: #033058;
}

.box-header {
  font-size: 20px;
  color: #003366;
  font-weight: 700;
  display: flex;
  justify-content: space-between;
  padding: 1em;
}

.box-header button {
  cursor: pointer;
  font-size: 27px;
  color: white;
  background-color: #2174c1;
  border: none;
  border-radius: 5px;
  height: 30px;
  width: 30px;
  text-align: center;
  padding-bottom: 35px;
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

.dashboard-button {
  display: inline-block;
  font-family: "Inter", sans-serif;
  font-weight: 600;
  padding: 10px 20px;
  background-color: #195b9f;
  color: white;
  margin-top: 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

select {
  padding: 5px;
  border-radius: 5px;
  border: 1px solid #ccc;
  font-size: 1rem;
  width: 250px;
  font-family: "Inter", sans-serif;
}

.available {
  background-color: #3fb5a3;
}

.scrollable-time-slots {
  max-height: 345px; /* Set a maximum height */
  overflow-y: auto; /* Enable vertical scrolling */
  width: 100%; /* Full width */
}

.time-slots {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  justify-content: center;
  background: #EEF1FA;
  padding: 20px;
  margin: 10px;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  flex-basis: calc(33% - 20px);
  border-left: 5px solid #2174c1;
}

.time-slot {
  width: 93%;
  height: 20px;
  text-align: center;
  padding: 10px; /* Adjust padding to give more space inside the slot */
  margin: 10px 0;
  border-radius: 5px;
  border: 2px solid #195b9f;
  color: #033058;
  cursor: pointer;
  transition: background-color 0.3s;
  display: flex; /* Use flexbox to align text */
  align-items: center; /* Center text vertically */
  justify-content: center; /* Center text horizontally */
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
  color: #144674;
  font-weight: 600;
}

.day:not(.other-month):not(.not-clickable):not(.weekend):hover {
  cursor: pointer;
  background-color: #b2e1da;
  transition: background-color 0.3s;
}

.day {
  padding: 10px;
  border: 0.25px solid #2174c1;

}

.not-clickable {
  pointer-events: none;
  color: #649ed4;
}

.selected {
  background-color: #b2e1da;
}

.is-today {
  background-color: #478ac9;
  color: white;
}

.weekend {
  color: #9fdad1;
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
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}
</style>
