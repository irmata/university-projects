<template>
  <div class="signupLoginContainer">
    <router-link id="signupLink" :to="{name: 'signup'}"><div ref="signupHeader" class="signupActive">signup</div></router-link>
    <router-link id="loginLink" :to="{name: 'login'}"><div ref="loginHeader" class="loginActive">login</div></router-link>
  </div>
</template>

<script>

export default {
  props: ['selected'],
  methods: {},
  mounted() {
    if (this.$props.selected === 'signup') {
      this.$refs.loginHeader.classList.add('deactive')
    } else {
      this.$refs.signupHeader.classList.add('deactive')
    }
  }
}

</script>

<style>

.signupLoginContainer {
  text-align: center;
}

#signupLink, #loginLink {
  color: rgb(255, 243, 198);
}

.signupActive, .loginActive  {
  font-size: 3.5em;
}

.signupActive {
  color: rgb(255, 84, 57);
}

.loginActive {
  color: rgb(42, 134, 255);
}

.deactive {
  color: grey;
  font-size: 1.2em;
}

</style>
