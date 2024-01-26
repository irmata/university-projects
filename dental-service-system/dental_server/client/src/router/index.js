import { createRouter, createWebHistory } from 'vue-router'

import BookingView from '@/views/BookingView.vue'
import PatientCalendarView from '@/views/PatientCalendarView.vue'
import PatientDashboardView from '@/views/PatientDashboardView.vue'
import AdminDashboardView from '@/views/AdminDashboardView.vue'
import AdminCalendarView from '@/views/AdminCalendarView.vue'
import HomeView from '@/views/HomeView.vue'
import LoginView from '@/views/LoginView.vue'
import MapView from '@/views/MapView.vue'
import AboutUsView from '@/views/AboutUsView.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomeView
  },
  {
    path: '/booking',
    name: 'Booking',
    component: BookingView
  },
  {
    path: '/patient-calendar',
    name: 'PatientCalendar',
    component: PatientCalendarView
  },
  {
    path: '/admin-calendar',
    name: 'AdminCalendar',
    component: AdminCalendarView
  },
  {
    path: '/patient-dashboard',
    name: 'PatientDashboard',
    component: PatientDashboardView
  },
  {
    path: '/admin-dashboard',
    name: 'AdminDashboard',
    component: AdminDashboardView
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView
  },
  {
    path: '/map',
    name: 'Map',
    component: MapView
  },
  {
    path: '/about',
    name: 'About',
    component: AboutUsView
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
