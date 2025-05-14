<template>
    <footer class="footer-bar"></footer>
</template>

<script>
export default {
  name: 'FooterComponent'
}
</script>

<style scoped>
  .footer-bar {
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 50px; /* Adjust the height as per your design */
  background-color: #2174C1;
  z-index: 10; /* Ensure it stays above other content */
  }
</style>
