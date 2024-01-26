<template>
  <div class="page-container">
    <div class="header">
      <h1>Welcome!</h1>
      <div class="sub-header">
        <span class="tools">All your tools in one place</span>
        <span class="vertical-line"></span>
        <span class="clinic-info">
          <span class="clinic-label">Your clinic: </span>
          <span class="clinic-name"> Hisingen Tandvård</span>
        </span>
        <span class="vertical-line"></span>
        <span class="dentist-dropdown">
          <label for="dentistSelect">Dentist:</label>
          <select id="dentistSelect" v-model="selectedDentist">
            <option v-for="dentist in dentists" :key="dentist.id" :value="dentist.name">
              {{ dentist.name }}
            </option>
          </select>
        </span>
      </div>
    </div>
    <div class="dashboard">
      <div class="appointments">
        <h2>Incoming appointments</h2>
        <div class="appointment-list">
          <!-- One div per appointment with v-for -->
          <div class="appointment">
            <div class="date">2023-11-25 | 12:30-13:20</div>
            <div class="details">Consultation - Nils Olstakka - Age 58</div>
          </div>
        </div>
      </div>
      <div class="previous-appointments">
        <h2>Previous appointments</h2>
        <!-- One div per appointment with v-for -->
        <div class="appointment">
          <div class="date">2023-11-24 | 12:00-13:00</div>
          <div class="details">Checkup - Lisa Lagom - Age 33</div>
        </div>
      </div>
      <div class="actions">
        <h2>Availability</h2>
        <button>Set availability</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AdminDashboardView',
  data () {
    return {
      selectedDentist: null,
      dentists: [
        { id: 1, name: 'Dr. Kai' },
        { id: 2, name: 'Dr. Sam' }
        // rest of dentists
      ]
    }
  },
  methods: {

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

label {
  margin-right: 5px;
  font-size: 1rem;
  color: #326492;
}

select {
  padding: 5px;
  border-radius: 5px;
  border: 1px solid #ccc;
  font-size: 1rem;
  width: 150px;
  font-family: "Inter", sans-serif;
}

.dashboard {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.appointments,
.previous-appointments,
.actions {
  background: #EEF1FA;
  padding: 20px;
  margin: 10px;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  flex-basis: calc(33% - 20px);
  border-left: 5px solid #2174c1;
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
  background-color: #2174c1;
  border: none;
  border-radius: 5px;
  color: white;
  cursor: pointer;
  font-family: "Inter", sans-serif;
}

button:hover {
  background-color: #195b9f;
}

h2 {
  color: #185a96;
  font-size: 1.5rem;
}
</style>
