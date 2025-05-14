<template>
    <div>
        <MenuSelectors />
        <p class="page-title">PRIVACY POLICY</p>
        <div v-for="(section, index) in sections" :key="index" class="section-container">
            <p class="section-title">{{ section.sectionTitle }}</p>
            <p class="section-content">{{ section.sectionContent }}</p>
        </div>
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
      sections: [
        { sectionTitle: 'Information We Collect', sectionContent: 'We may collect personal information, such as your name, email address, phone number, and location when you interact with our website or services. This data helps us provide you with a tailored experience and improve our offerings.' },
        { sectionTitle: 'How We Use Your Information', sectionContent: 'We use the information we collect to: Process orders and reservations, Personalize your experience, and Improve our products and services.' },
        { sectionTitle: 'Data Security', sectionContent: 'We take data security seriously. We implement industry-standard security measures to protect your information from unauthorized access, disclosure, alteration, or destruction.' },
        { sectionTitle: 'Cookies and Tracking', sectionContent: 'Our website may use cookies and similar tracking technologies to enhance your browsing experience. You can control cookies through your browser settings.' }
      ]
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
.section-container {
  margin: 0px 40px;
}
.section-title {
  font-size: 18px;
  font-weight: bold;
  text-align: left;
}
.section-content {
  font-size: 17px;
  text-align: left;
}
/* Media query for smaller screens */
@media (max-width: 1100px) {
  .page-title {
    font-size: 32px;
  }
}
</style>
