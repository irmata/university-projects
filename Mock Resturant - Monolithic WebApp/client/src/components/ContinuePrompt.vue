<template>
  <div class="continueOnDisplay" v-show="canContinue">Continue!</div>
</template>

<script>
export default {
  props: ['canContinue']
}
</script>

<style>
.continueOnDisplay {
  font-style: italic;
}
</style>
