<template>
    <font-awesome-icon :class="['nextButton', 'nextButtonError', canContinue ? 'nextButtonContinue' : null, margin ? margin : null]"  @mouseover="dragOverEmit" @click="clickEmit" icon="fa-solid fa-right-long"/>
</template>

<script>
export default {
  props: ['canContinue', 'margin'],
  methods: {
    dragOverEmit() {
      this.$emit('dragOverEmit')
    },
    clickEmit() {
      this.$emit('clickEmit')
    }
  }
}
</script>

<style>

.nextButton, .nextButtonError, .nextButtonContinue {
  font-size: 3.5em;
  border: 0.01em rgba(114, 114, 114, 0.153) solid;
  padding: 0.2em 2.2em 0.2em 2.2em;
  background: rgba(183, 183, 183, 0.101);
}

.nextButtonError:hover {
  transition-duration: 320ms;
  border-style: solid;
  border-color: rgb(255, 97, 97);
}

.nextButtonContinue:hover {
  transition-duration: 320ms;
  background: rgb(187, 255, 205);
  border-style: solid;
  border-color: rgb(187, 255, 205);
}

.extraExtraSmallAllAroundMargin {
  margin: 0.5em;
}

.extraSmallAllAroundMargin {
  margin: 1em;
}

.smallAllAroundMargin {
  margin: 1.5em;
}

.mediumAllAroundMargin {
  margin: 2em;
}

.largeAllAroundMargin {
  margin: 2.5em;
}

.extraLargeAllAroundMargin {
  margin: 3em;
}

.extraExtraLargeAllAroundMargin {
  margin: 3.5em;
}

</style>
