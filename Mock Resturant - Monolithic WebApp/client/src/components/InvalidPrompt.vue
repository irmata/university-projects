<template>
  <div :class="invalidPromptWrapperWidth ? invalidPromptWrapperWidth : null">
    <div class="invalidText" v-show="showError">
      {{ invalidText }}
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showError: false
    }
  },
  props: ['invalidText', 'invalidPromptWrapperWidth']
}
</script>

<style>

.invalidText {
  text-align: center;
  font-size: 0.9em;
  color: rgb(255, 96, 64);
  animation: fadeIn 1s;
  box-sizing: border-box;
}

@keyframes fadeIn {
  0% {
    opacity: 0;
  }

  100% {
    opacity: 1;
  }
}

.smallWidth, .mediumWidth, .largeWidth {
  height: 3vh;
}

.smallWidth {
  width: 20vw;
}

.mediumWidth {
  width: 30vw;
}

.largeWidth {
  width: 40vw;
}

</style>
