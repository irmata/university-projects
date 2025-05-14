<template>
  <footer class="footer">
    <ul class="footer-container">
      <router-link
        v-for="link in links"
        :key="link.pageLabel"
        :to="{ name: link.name }"
        class="footer-option"
        >{{ link.pageLabel }}
      </router-link>
    </ul>
  </footer>
</template>

<script>
export default {
  data() {
    return {
      links: [
        { pageLabel: 'About', name: 'about' },
        { pageLabel: 'Location', name: 'location' },
        { pageLabel: 'Career', name: 'career' },
        { pageLabel: 'Data', name: 'data' }
      ]
    }
  }
}
</script>

<style>
.footer {
  padding: 10px 0;
  margin-top: 20px;
  background-color: #0ca789;
}
.footer-container {
  text-align: center;
}
.footer-option {
  display: inline-block;
  font-size: 20px;
  padding: 0 40px;
  color: #000000 !important;
  text-decoration: none !important;
}
@media (max-width: 500px) {
  .footer-option {
    font-size: 15px;
  }
  .footer-option {
    padding: 0 15px;
  }
}
</style>
