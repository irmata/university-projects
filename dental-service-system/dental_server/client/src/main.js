import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import VueGoogleMaps from '@fawmi/vue-google-maps'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faAngleRight, faExclamationCircle } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

const app = createApp(App)

// Adds icons to the library for global use
library.add(faAngleRight)
library.add(faExclamationCircle)

app.component('font-awesome-icon', FontAwesomeIcon)

app.use(router)
app.use(VueGoogleMaps, {
  load: {
    key: process.env.VUE_APP_GOOGLE_MAPS_API_KEY,
    libraries: 'places'
  }
})

app.mount('#app')
