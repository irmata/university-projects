<template>
  <nav class="navbar">
    <p class="logo">{{ logoText }}</p>
    <p class="logo-text">{{ tagText }}</p>
    <div class="icon-list">
      <ul class="social-icons">
        <li v-for="(item, index) in socialItems" :key="index">
          <a :href="item.link" class="social-link">
            <i :class="item.icon"></i>
          </a>
        </li>
      </ul>
      <ul class="utility-icons">
        <li>
          <b-button v-b-toggle.sidebar-right class="btn btn-dark">
            <i class="fas fa-shopping-cart"></i> Cart
          </b-button>
        </li>
        <li>
            <router-link :to="{name: 'login'}">
            <i class="fas fa-user"></i>
            Login
            </router-link>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
export default {
  data() {
    return {
      logoText: 'Lindholmen Eats',
      tagText: 'ALL RIGHTS RESERVED 2023©',
      socialItems: [
        { link: 'https://www.facebook.com', icon: 'fab fa-facebook' },
        { link: 'https://www.instagram.com', icon: 'fab fa-instagram' },
        { link: 'https://www.twitter.com', icon: 'fab fa-twitter' },
        { link: 'https://www.youtube.com', icon: 'fab fa-youtube' }
      ],
      utilityItems: [
        { link: '#', icon: 'fas fa-shopping-cart' },
        { link: '#', icon: 'fas fa-user' }
      ]
    }
  }
}
</script>

<style>
.icon-list {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.navbar {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  text-align: center;
  position: relative;
}

.navbar::before,
.navbar::after {
  content: '';
  position: absolute;
  top: 25%;
  width: 37%;
  height: 12px;
  background-color: #0ca789;
  border-radius: 5px;
  transition: width 0.3s, height 0.3s;
}

.navbar::before {
  left: 0;
  transform: translateX(20px);
}

.navbar::after {
  right: 0;
  transform: translateX(-20px);
}

/* Styles for larger screens */
@media (min-width: 769px) {
  .navbar::before {
    left: 0;
    width: 25%;
  }

  .navbar::after {
    right: 0;
    width: 25%;
  }
}

/* Styles for smaller screens */
@media (max-width: 768px) {
  .navbar::before,
  .navbar::after {
    width: 15%;
  }
}

/* Styles for smaller screens */
@media (max-width: 500px) {
  .navbar::before,
  .navbar::after {
    width: 8%;
  }

  .icon-list {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 100%;
}
}

/* Styles for smaller screens */
@media (max-width: 400px) {
  .navbar::before,
  .navbar::after {
    width: 0%;
  }
}

.logo {
  font-family: 'Graduate';
  font-size: 30px;
  font-weight: bold;
  text-align: center;
  margin-top: 20px;
  margin-bottom: 0px;
}

.logo-text {
  font-family: 'Graduate';
  font-size: 16px;
  text-align: center;
  margin-bottom: 0;
}
.utility-icons,
.social-icons {
  list-style-type: none;
  display: flex;
  justify-content: center; /* Center the icons horizontally */
}

.utility-icons li {
  margin-right: 10px;
}

/* Media query for small screens */
@media (max-width: 768px) {
  .utility-icons,
  .social-icons {
    justify-content: center; /* Center the icons horizontally for small screens */
  }
}

.social-link,
.app-function {
  display: flex;
  font-size: 30px;
  margin: 10px 8px;
  transition: font-size 0.3s;
  color: #000000 !important;
  text-decoration: none !important;
}

/* Media query for smaller screens */
</style>
