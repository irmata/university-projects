<template>
    <div>
        <MenuSelectors />
        <p class="page-title">WORK WITH US</p>
        <div class="work-banner">
            <img :src="workBanner" alt="Work Banner">
        </div>
        <p class="page-sub-title">{{ subTitle }}</p>
        <div v-for="(section, index) in sections" :key="index" class="section-container">
            <p class="section-title">{{ section.sectionTitle }}</p>
            <p class="section-content">{{ section.sectionContent }}</p>
        </div>
    </div>
</template>

<script>
import MenuSelectors from '@/components/MenuSelectors.vue'
import workBanner from '@/assets/images/banners/workBanner.png'
export default {
  components: {
    MenuSelectors
  },
  data() {
    return {
      subTitle: 'Why Lindholmen Eats?',
      sections: [
        { sectionTitle: 'Warm and Inclusive Atmosphere', sectionContent: "Our workplace is more than just a place to clock in; it's a space where camaraderie and support thrive. We believe in inclusivity and diversity, welcoming team members from all backgrounds to contribute their unique perspectives." },
        { sectionTitle: 'Passion for Food', sectionContent: "If you have a passion for exceptional cuisine and want to be a part of a team that shares your love for food, you've come to the right place. Lindholmen Eats is where culinary enthusiasts come to flourish." },
        { sectionTitle: 'Growth and Development', sectionContent: "We believe in nurturing talent from within. Whether you're just starting your career or looking to advance, we provide opportunities for learning and growth, helping you reach your full potential." },
        { sectionTitle: 'Employee Benefits', sectionContent: 'Your well-being matters to us. We offer competitive compensation packages, flexible work arrangements, and benefits to ensure you thrive both personally and professionally.' }
      ],
      workBanner
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
.work-banner {
  display: block;
  margin-left: auto;
  margin-right: auto;
}
.work-banner img {
  max-width: 100%;
  max-height: 100%;
  width: auto;
  height: auto;
}
.section-container {
  margin: 0px 40px;
}
.section-title {
  font-size: 18px;
  text-align: left;
  font-weight: bold;
}
.page-sub-title {
  font-size: 18px;
  font-weight: bold;
  text-align: center;
}
.section-content {
  font-size: 17px;
  text-align: left;
}
.page-sub-title {
  margin: 20px 40px;
}
/* Media query for smaller screens */
@media (max-width: 1100px) {
  .work-banner {
    width: 350px;
    height: 209px;
  }
  .page-title {
    font-size: 32px;
  }
}
</style>
