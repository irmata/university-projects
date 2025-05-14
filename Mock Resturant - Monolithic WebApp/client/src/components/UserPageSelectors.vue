<template>
  <div>
    <p class="page-title">User Profile</p>
    <div class="user-links-container">
      <router-link
        v-for="link in links"
        :key="link.pageLabel"
        :to="{ name: link.name }"
        class="user-link"
      >
        <div @click="activateLink(link)">{{ link.pageLabel }}</div>
      </router-link>
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      links: [
        { pageLabel: 'User Info', name: 'userInfo', isActive: true },
        { pageLabel: 'Login Info', name: 'loginInfo', isActive: false },
        { pageLabel: 'Payment Info', name: 'paymentInfo', isActive: false }
      ]
    }
  },
  methods: {
    activateLink(clickedLink) {
      this.links.forEach((link) => {
        link.isActive = false
      })
      clickedLink.isActive = true
    }
  }
}
</script>

<style>
.page-title {
  text-decoration: none;
  text-align: center;
  color: black;
  font-size: 45px;
  transition: text-decoration 0.3s, font-size 0.3s;
}
.user-links-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  margin-left: 50px;
  gap: 60px; /* Default gap for larger screens */
}
.user-link {
  text-decoration: none;
  color: black;
  font-size: 29px;
  transition: text-decoration 0.3s, font-size 0.3s;
}
.user-link:hover {
  text-decoration: underline;
  font-size: 31px;
  color: black;
}
.user-link.active-link {
  text-decoration: underline;
  font-size: 26px;
}
/* Media queries for responsive design */
@media (max-width: 1000px) {
  .user-link {
    font-size: 24px;
  }
  .user-link:hover {
    font-size: 25px;
  }
  .page-title {
    font-size: 40px;
  }
}
@media (max-width: 768px) {
  .user-link {
    font-size: 25px;
  }
  .user-link:hover {
    font-size: 26px;
  }
  .user-links-container {
    align-items: center;
    gap: 22px; /* Reduced gap for smaller screens */
  }
  .page-title {
    font-size: 41px;
  }
}
@media (max-width: 768px) {
  .user-link {
    font-size: 21px;
  }
  .user-link:hover {
    font-size: 22px;
  }
  .user-links-container {
    align-items: center;
    gap: 20px; /* Reduced gap for smaller screens */
  }
  .page-title {
    font-size: 37px;
  }
}
@media (max-width: 550px) {
  .user-links-container {
    margin-left: 0px;
  }
  .user-link {
    font-size: 18px;
  }
  .user-link:hover {
    font-size: 18px;
  }
  .page-title {
    font-size: 34px;
  }
}
</style>
