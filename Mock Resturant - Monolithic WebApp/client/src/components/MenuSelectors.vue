<template>
  <div class="menu-links-container">
    <router-link
  v-for="link in links"
  :key="link.pageLabel"
  :to="{ name: link.name }"
  class="menu-link"
>
<div  @click="activateLink(link)">{{ link.pageLabel }}</div>
</router-link>

    <router-view />
  </div>
</template>

<script>
export default {
  props: {
    category: String
  },
  data() {
    return {
      links: [
        { pageLabel: 'Mains', name: 'menu', isActive: false },
        { pageLabel: 'Deals', name: 'menu', isActive: false },
        { pageLabel: 'Home', name: 'home', isActive: true },
        { pageLabel: 'Sides', name: 'menu', isActive: false },
        { pageLabel: 'Desserts', name: 'menu', isActive: false }
      ]
    }
  },
  methods: {
    activateLink(clickedLink) {
      this.links.forEach((link) => {
        link.isActive = false
      })
      clickedLink.isActive = true
      console.log(clickedLink.pageLabel)
      if (clickedLink.pageLabel !== 'Home') {
        this.$emit('update:category', clickedLink.pageLabel)
      }
      this.$emit('link-clicked', clickedLink.pageLabel) // Emit the clicked link's label
    }
  }
}
</script>

<style scoped>
.menu-links-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 60px; /* Default gap for larger screens */
}
.menu-link {
  text-decoration: none;
  color: black;
  font-size: 29px;
  transition: text-decoration 0.3s, font-size 0.3s;
}
.menu-link:hover {
  text-decoration: underline;
  font-size: 31px;
  color: black;
}
.menu-link.active-link {
  text-decoration: underline;
  font-size: 26px;
}
/* Media queries for responsive design */
@media (max-width: 1000px) {
  .menu-link {
    font-size: 24px;
  }

  .menu-link:hover {
    font-size: 25px;
  }
}
@media (max-width: 768px) {
  .menu-link {
    font-size: 25px;
  }
  .menu-link:hover {
    font-size: 26px;
  }
  .menu-links-container {
    align-items: center;
    gap: 22px; /* Reduced gap for smaller screens */
  }
}
@media (max-width: 768px) {
  .menu-link {
    font-size: 21px;
  }
  .menu-link:hover {
    font-size: 22px;
  }
  .menu-links-container {
    align-items: center;
    gap: 20px; /* Reduced gap for smaller screens */
  }
}
@media (max-width: 550px) {
  .menu-link {
    font-size: 18px;
  }
  .menu-link:hover {
    font-size: 18px;
  }
}
</style>
