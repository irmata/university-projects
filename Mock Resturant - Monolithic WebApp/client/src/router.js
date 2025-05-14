import Vue from 'vue'
import Router from 'vue-router'

import LoginPage from '@/views/LoginPage.vue'
import SignupPage from '@/views/SignupPage.vue'
import Home from '@/views/HomePage.vue' // Import HomePage.vue
import MenuPage from '@/views/MenuPage.vue' // Import MenuPage.vue
import AboutPage from '@/views/AboutPage.vue' // Import AboutPage.vue
import LocationPage from '@/views/LocationPage.vue' // Import LocationPage.vue
import CareerPage from '@/views/CareerPage.vue' // Import CareerPage.vue
import DataPage from '@/views/DataPage.vue' // Import DataPage.vue
import AdminPage from '@/views/AdminPage.vue' // Import AdminPage.vue
import UserInfoPage from '@/views/UserInfoPage.vue' // Import UserInfoPage.vue
import LoginInfoPage from '@/views/LoginInfoPage.vue' // Import LoginInfoPage.vue
import PaymentInfoPage from '@/views/PaymentInfoPage.vue' // Import PaymentInfoPage.vue

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'home',
      children: [
        {
          path: '/login',
          name: 'login',
          component: LoginPage
        },
        {
          path: '/signup',
          name: 'signup',
          component: SignupPage
        }
      ],
      component: Home
    },
    {
      path: '/menu',
      name: 'menu',
      component: MenuPage
    },
    {
      path: '/about',
      name: 'about',
      component: AboutPage
    },
    {
      path: '/location',
      name: 'location',
      component: LocationPage
    },
    {
      path: '/career',
      name: 'career',
      component: CareerPage
    },
    {
      path: '/data',
      name: 'data',
      component: DataPage
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminPage
    },
    {
      path: '/user-info',
      name: 'userInfo',
      component: UserInfoPage
    },
    {
      path: '/login-info',
      name: 'loginInfo',
      component: LoginInfoPage
    },
    {
      path: '/payment-info',
      name: 'paymentInfo',
      component: PaymentInfoPage
    },
    {
      path: '/:catchAll(.*)',
      redirect: '/'
    }
  ]
})
