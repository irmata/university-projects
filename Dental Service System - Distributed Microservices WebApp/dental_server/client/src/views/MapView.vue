<template>
  <div class="container">
    <!-- Column 1: Map Area -->
    <div class="map-area">
      <h1>Find nearby clinics</h1>
      <p>Find details & explore clinics near you</p>
      <!-- GoogleMap component with markers -->
      <GoogleMap
        v-if="userLocation"
        :center="userLocation"
        :zoom="14"
        map-type-id="roadmap"
        style="width: 90%; height: 500px; border-radius: 30px; align-self: start;"
      >
        <!-- Iterate over clinics data and create a marker for each clinic -->
        <GMapMarker
          v-for="clinic in filteredClinics"
          :key="clinic._id.$oid"
          :position="{ lat: clinic.latitude, lng: clinic.longitude }"
          @click="markerClicked(clinic)"
        />
        <GMapInfoWindow
          v-if="selectedClinic"
          :position="{ lat: selectedClinic.latitude, lng: selectedClinic.longitude }"
          @closeclick="selectedClinic = null"
        >
          <div>
            <h1>{{ selectedClinic.name }}</h1>
            <p>{{ selectedClinic.street_name }} {{ selectedClinic.street_number }}, {{ selectedClinic.post_number }}, {{ selectedClinic.city }}</p>
            <p>{{ calculatedDistance }} km from your location</p>
            <p>{{ selectedClinic.opening_time }} - {{ selectedClinic.cloising_time }}</p>
            <p>{{ selectedClinic.ownership_type }}</p>
          </div>
        </GMapInfoWindow>
      </GoogleMap>
      <!-- Optionally, show a loading message or element when userLocation is null -->
      <div v-else class="spinner-container">
        <div class="spinner"></div>
      </div>
      <div id="google-map"></div>
    </div>

    <!-- Column 2: Filter Area -->
    <div class="filter-area">
      <h2>Filter clinics</h2>
      <div class="filter-group">
        <h3>Search clinic by type</h3>
        <div class="dropdown" ref="dropdown">
          <button @click="toggleDropdown" class="dropdown-button">{{ selectedFilterType || 'Filter by Type' }}</button>
          <div v-if="showDropdown" class="dropdown-content">
            <a href="#" @click.prevent="selectFilter('Private')">Find private clinics</a>
            <a href="#" @click.prevent="selectFilter('Public')">Find public clinics</a>
          </div>
        </div>
      </div>

      <div class="filter-group">
        <h3>Search clinic by name</h3>
        <input type="text" placeholder="Enter clinic name..." v-model="filterName">
      </div>
      <div class="filter-group">
        <h3>Search clinic by location</h3>
        <input type="text" placeholder="Enter street name..." v-model="filterLocation">
        <p class="location-note">
          <font-awesome-icon :icon="['fas', 'exclamation-circle']" style="height: 15px; width: 15px;"/>
          Make sure location is enabled for precise distance to nearby clinics
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { Map as GoogleMap, Marker as GMapMarker } from '@fawmi/vue-google-maps'
import clinicsData from '@/assets/markerData.json' // Placeholder data until endpoints is sorted out

export default {
  name: 'MapView',
  components: {
    GoogleMap,
    GMapMarker
  },
  data () {
    return {
      showDropdown: false,
      selectedFilterType: null,
      userLocation: null, // No default location
      selectedClinic: null, // Track the selected clinic
      calculatedDistance: null, // Holds the distance from user's location to the selected clinic
      filterName: '',
      filterLocation: '',
      filteredClinics: [],
      clinics: clinicsData.map(clinic => ({ // Placeholder data until endpoint is sorted out
        ...clinic,
        latitude: parseFloat(clinic.latitude),
        longitude: parseFloat(clinic.longitude)
      }))
    }
  },
  methods: {
    getUserLocation () {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition((position) => {
          this.userLocation = {
            lat: position.coords.latitude,
            lng: position.coords.longitude
          }
          this.filterClinics() // Call filterClinics here after setting the location
        }, (error) => {
          console.error('Error fetching user location:', error)
          // handle errors or set a fallback location
        })
      } else {
        console.log('Geolocation is not supported by this browser.')
        // handle the case where Geolocation isn't supported
      }
    },
    selectFilter (type) {
      this.selectedFilterType = type
      this.filterClinics()
      this.showDropdown = false
    },
    filterClinics () {
      this.filteredClinics = this.clinics
        .filter(clinic => {
          return (!this.selectedFilterType || clinic.ownership_type === this.selectedFilterType) &&
                (!this.filterName || clinic.name.toLowerCase().includes(this.filterName.toLowerCase().trim())) &&
                (!this.filterLocation || clinic.street_name.toLowerCase().includes(this.filterLocation.toLowerCase().trim()))
        })
        .map(clinic => ({
          ...clinic,
          distance: this.calculateDistance({ lat: clinic.latitude, lng: clinic.longitude }).toFixed(2)
        }))
    },
    toggleDropdown () {
      this.showDropdown = !this.showDropdown
    },
    handleClickOutside (event) {
      if (this.$refs.dropdown && !this.$refs.dropdown.contains(event.target)) {
        this.showDropdown = false
      }
    },
    markerClicked (clinic) {
      // Clone the clinic object to ensure reactivity
      this.selectedClinic = { ...clinic }
      // Calculate distance
      this.calculatedDistance = this.calculateDistance({ lat: clinic.latitude, lng: clinic.longitude }).toFixed(2)
      console.log('Selected Clinic:', this.selectedClinic)
    },
    // When a marker is clicked, this method is called to generate the content for the InfoWindow
    generateContent (clinic) {
      if (!clinic) return '' // Guard clause to handle null or undefined clinic
      const distance = this.calculateDistance({ lat: parseFloat(clinic.latitude), lng: parseFloat(clinic.longitude) }).toFixed(2)
      // Return the HTML content for the InfoWindow
      return `
        <div class="info-window-content">
          <h1>${clinic.name}</h1>
          <p>${clinic.street_name} ${clinic.street_number}, ${clinic.post_number}, ${clinic.city}</p>
          <p>${distance} km from your location</p>
          <p>${clinic.opening_time} - ${clinic.cloising_time}</p>
          <p>${clinic.ownership_type}</p>
        </div>
      `
    },
    calculateDistance (clinicLocation) {
      /* Using Haversine formula to calculate the distance
      between two points given their latitudes and longitudes.
      It accounts for the curvature of the Earth */
      if (!this.userLocation) { // Guard clause to handle null or undefined userLocation
        return 0
      }

      const toRadian = angle => (Math.PI / 180) * angle
      const earthRadius = 6371 // Earth radius in kilometers

      const deltaLatitude = toRadian(clinicLocation.lat - this.userLocation.lat)
      const deltaLongitude = toRadian(clinicLocation.lng - this.userLocation.lng)
      const a = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2) +
                Math.cos(toRadian(this.userLocation.lat)) * Math.cos(toRadian(clinicLocation.lat)) *
                Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2)
      const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
      const distance = earthRadius * c // Distance in kilometers

      return distance
    },
    async fetchClinics () {
      try {
        const response = await axios.get('/clinics')
        this.clinics = response.data.map(clinic => ({
          ...clinic,
          latitude: parseFloat(clinic.latitude), // convert to number from string
          longitude: parseFloat(clinic.longitude) // convert to number from string
        }))
      } catch (error) {
        console.error('Error fetching clinics:', error)
      }
    }
  },
  mounted () {
    document.addEventListener('click', this.handleClickOutside)
    this.getUserLocation()
    // this.filterClinics()
    this.filteredClinics = this.clinics
    // this.fetchClinics() uncomment this once endpoint is ready
  },
  beforeUnmount () {
    document.removeEventListener('click', this.handleClickOutside)
  },
  watch: {
    selectedFilterType () {
      this.filterClinics()
    },
    filterName () {
      this.filterClinics()
    },
    filterLocation () {
      this.filterClinics()
    }
  }
}
</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  padding: 20px;
  gap: 30px; /*space between columns */
  margin-top: 1rem;
}

.map-area {
  flex-basis: 60%;
}

.map-area h1 {
  color: #0C375E;
  font-family: 'Inter', sans-serif;
  font-weight: 700;
  font-size: 24pt;
  margin-top: 0;
}

.map-area p {
  color: #1A5B97;
  font-weight: 600;
  font-family: 'Open Sans', sans-serif;
  font-size: 14pt;
  margin-top: -20px;
}

.filter-area {
  flex-basis: 30%;
  position: relative;
  background-color: #EEF1FA;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-top: 85px;
}

.filter-area h2 {
  color: #1A5B97;
  font-weight: 600;
  font-family: 'Inter', sans-serif;
  font-size: 24pt;
}

.filter-area::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 10px;
  border-radius: 10px 0 0 10px;
  background-color: #2174C1;
}

.filter-group {
  width: 80%;
}

.filter-group h3 {
  margin-bottom: 10px;
  color: #1A5B97;
  font-weight: 600;
  font-family: 'Inter', sans-serif;
  font-size: 14pt;
}

.filter-group input,
.filter-area .dropdown-button {
  width: 100%;
  padding: 0.5rem;
  margin-bottom: 1rem;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: white;
  cursor: pointer;
  box-sizing: border-box;
}

.filter-group input,
.filter-area .dropdown-button {
  color: #5F5F5F;
  font-family: 'Inter', sans-serif;
  font-size: 11pt;
}

.filter-group button {
  padding: 10px;
  margin-bottom: 10px;
}

.location-note {
  font-size: 0.8em;
  color: #666;
  font-family: 'Inter';
  font-style: italic;
}

/* Styles for the InfoWindow */
.info-window-content {
  width: 150px;
  font-size: 0.8rem;
}

.filter-area .dropdown {
  position: relative;
  display: inline-block;
  width: 100%;
}

.filter-area .dropdown button {
  color: #5F5F5F;
  font-family: 'Inter', sans-serif;
  font-size: 11pt;
}

.filter-area .dropdown-content {
  display: block;
  position: absolute;
  background-color: #f9f9f9;
  width: 100%;
  box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
  border-radius: 5px;
  z-index: 1;
  box-sizing: border-box;
}

.filter-area .dropdown-content a {
  color: #5F5F5F;
  font-family: 'Inter', sans-serif;
  font-size: 12pt;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
}

.filter-area .dropdown-content a:hover {
  background-color: #f1f1f1;
}
/* Styles for the loading message */
.spinner {
  border: 4px solid rgba(0, 0, 0, 0.1);
  width: 36px;
  height: 36px;
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
