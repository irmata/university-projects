<template>
    <div>
        <MenuSelectors />
        <p class="page-title">OUR LOCATION</p>
        <div>
          <!-- Map embed link from Google Maps Api -->
          <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2994.4936437210754!2d11.934255968622049!3d57.70720210582128!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x464ff34fe37e1fd1%3A0xcac103f8b3186f0f!2sChalmers%20Lindholmen%20University%20College!5e0!3m2!1sen!2sse!4v1696324992995!5m2!1sen!2sse" class="map-embed" style="border:0;" allowfullscreen="false" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>
        </div>
        <p class="page-content">{{ content }}</p>
    </div>
</template>

<script>
import MenuSelectors from '@/components/MenuSelectors.vue'
export default {
  components: {
    MenuSelectors
  },
  data() {
    return {
      content: 'We are located in the thriving technology environment of Chalmers University - Lindholmen Campus.'
    }
  }
}
</script>

<style>
.page-title {
  font-size: 52px;
  text-align: center;
  text-decoration: underline;
}
.map-embed {
  width: 1000px;
  height: 500px;
}
.page-content {
  font-size: 17px;
  text-align: center;
  margin: 10px 50px 10px 50px;
}
/* Media query for smaller screens */
@media (max-width: 1100px) {
  .map-embed {
    width: 500px;
    height: 225px;
  }
  .page-title {
    font-size: 32px;
  }
}
@media (max-width: 585px) {
  .map-embed {
    width: 400px;
  }
}
@media (max-width: 930px) {
  .page-content {
    margin: 10px 10px 10px 10px;
  }
}
@media (max-width: 500px) {
  .map-embed {
    width: 250px;
  }
}
@media (max-width: 340px) {
  .map-embed {
    width: 150px;
  }
}
</style>
