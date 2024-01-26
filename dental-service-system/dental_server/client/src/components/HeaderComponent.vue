<template>
    <header class="header">
        <!-- First Row -->
        <div class="header-rows row-first d-flex justify-content-between align-items-center">
            <div class="logo-container">
              <router-link to="/">
                <img src="@/assets/header_logo.png" alt="SmileSlot Logo" class="logo" />
              </router-link>
            </div>
            <div class="user-actions d-flex justify-content-end align-items-center">
                <!-- User Actions -->
                <div class="action-item d-flex flex-column align-items-center">
                  <router-link to="/login" class="custom-link">
                    <img src="@/assets/user-icon.svg" alt="Log in" class="icon" />
                    <span>Log in</span>
                  </router-link>
                </div>
                <div class="action-item d-flex flex-column align-items-center">
                  <router-link to="/map" class="custom-link">
                    <img src="@/assets/clinic-icon.svg" alt="Find Clinic" class="icon" />
                    <span>Find clinic</span>
                  </router-link>
                </div>
            </div>
        </div>
       <!-- Second Row -->
       <div class="header-rows row-second d-flex justify-content-center align-items-center">
          <nav class="navigation">
             <ul class="nav-list d-flex gap-4 gap-lg-5">
                <li class="nav-item"><a href="/map" class="nav-link">Our clinics</a></li>
                <li class="nav-item"><a href="/patient-dashboard" class="nav-link">Booking details</a></li>
                <li class="nav-item"><a href="/about" class="nav-link">About us</a></li>
             </ul>
          </nav>
       </div>
    </header>
 </template>

<script>
export default {
  name: 'HeaderComponent',
  data () {
    return {
      isLoggedIn: false
    }
  }
}
</script>

<style scoped>
.header {
  width: 100%;
  margin: 0;
  font-family: 'Inter', sans-serif;
}

.header-rows {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
}

/* LOGO, LOG IN, FIND CLINC */
.row-first {
  background-color: #2174C1;
}

/* NAVIGATION LINKS */
.row-second {
  background-color: #FFFFFF;
  align-items: center;
  justify-content: center;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 1;
}

.logo-container .logo {
  height: 60px;
}

.navigation .nav-list {
  list-style: none;
  display: flex;
  gap: 2rem;
  margin: 0;
  padding: 0;
}

.navigation .nav-item .nav-link {
  font-size: 18px;
  font-weight: 500;
  color: #2174C1;
  text-decoration: none;
}

.navigation .nav-item .nav-link:hover {
  color: #195b9f;
}

.custom-link {
  text-decoration: none;
  color: inherit;
}

.user-actions {
  display: flex;
  flex-direction: row;
  gap: 2rem;
  color: #1A5B97;
  font-weight: 500;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem; /* 8px - the space between the icons and text */
}

.login-button,
.find-clinic-button {
  font-size: 18px;
  font-weight: 500;
  color: #FFFFFF;
  background-color: #2174C1;
  border: none;
  border-radius: 20px;
  padding: 0.5rem 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login-button:hover,
.find-clinic-button:hover {
  background-color: #195b9f;
}

</style>
