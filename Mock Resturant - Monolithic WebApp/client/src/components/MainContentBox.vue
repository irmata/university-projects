<template>
  <div class="main-content">
    <!-- Render the content of the current page view -->
    <router-view/>
  </div>
</template>

<script>
</script>

<style>
.main-content {
  box-shadow: 10px 10px 10px rgba(12, 167, 137, 1);
  border: 2px solid black;
  margin: 0px 20px 20px 60px;
  padding: 20px;
  max-width: 1800px;
  height: auto;
}
/* Media query for smaller screens */
@media (max-width: 1700px) {
  .main-content {
    margin: 0px 20px 20px 20px;
  }
}
</style>
