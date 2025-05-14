<template>

  <div id="passwordStrengthCheckerContainer" :class="[margin ? margin : null]">
    <div :class="[{weakCircle: state === 'Weak'}, {normalCircle: state === 'Normal'}, {strongCircle: state === 'Strong'}]">{{ state }} :</div>
    <div id="passwordStrengthCircles">
      <font-awesome-icon :class="[{weakCircle: state === 'Weak'}, {normalCircle: state === 'Normal'}, {strongCircle: state === 'Strong'}]" icon="fa-solid fa-circle"/>
      <font-awesome-icon :class="[{inactiveCircle: state === 'Weak'}, {normalCircle: state === 'Normal'}, {strongCircle: state === 'Strong'}]" icon="fa-solid fa-circle"/>
      <font-awesome-icon :class="state === 'Strong' ? 'strongCircle' : 'inactiveCircle'" icon="fa-solid fa-circle"/>
    </div>
  </div>

</template>

<script>
export default {
  data() {
    return {
      state: 'Weak'
    }
  },
  props: ['margin']
}
</script>

<style>

#passwordStrengthCheckerContainer {
  display: flex;
  font-family:'Courier New', Courier, monospace;
  font-weight: 600;
}

#passwordStrengthCircles > * {
  margin: 0 0.8em 0 0.8em;
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

.inactiveCircle{
  color: grey;
}

.weakCircle{
  color: rgb(255, 78, 78);
}

.normalCircle {
  color: rgb(93, 93, 255);
}

.strongCircle {
  color: rgb(128, 174, 105);
}

</style>
