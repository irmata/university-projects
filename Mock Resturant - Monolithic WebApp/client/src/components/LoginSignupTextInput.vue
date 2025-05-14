<template>
  <input :id="id ? id : null" required :type="type" :placeholder="placeholder" :class="['inputBox', isValidInputBox ? 'validInputBox' : null, margin ? margin : null, padding ? padding : null, width ? width : null]" @blur="validate" @keyup="checkHasContent" @click="clickEmit" v-model="currInput">
</template>

<script>

export default {
  data() {
    return {
      currInput: '',
      isValidInputBox: false,
      isInvalidated: null
    }
  },
  props: ['id', 'type', 'placeholder', 'margin', 'padding', 'width'],
  methods: {
    validate() {
      this.$emit('validateAfterLoseFocus', this)
    },
    checkHasContent() {
      this.$emit('inputChange', this)
    },
    clickEmit() {
      this.$emit('clickEmit', this)
    }
  }
}

</script>

<style>
.inputBox {
  font-family: 'Courier New', Courier, monospace;
  display: block;
  width: 100%;
  padding: 0.8em;
  background: rgba(0, 0, 0, 0.1);
  border: 0.125em solid rgba(167, 167, 167, 0.233);
}

.validInputBox {
  border: 0.125em solid rgba(221, 255, 221, 0.756);
  background: rgba(103, 255, 197, 0.689);
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

.extraExtraSmallAllAroundPadding {
  margin: 0.5em;
}

.extraSmallAllAroundPadding {
  margin: 1em;
}

.smallAllAroundPadding {
  margin: 1.5em;
}

.mediumAllAroundPadding {
  margin: 2em;
}

.largeAllAroundPadding {
  margin: 2.5em;
}

.extraLargeAllAroundPadding {
  margin: 3em;
}

.extraExtraLargeAllAroundPadding {
  margin: 3.5em;
}

.threeQuartersOfParent {
  width: 75%;
}

.halfOfParent {
 width: 50%;
}

.thirdOfParent {
  width: 30%;
}

.quarterOfParent {
  width: 25%;
}

.fifthOfParent {
  width: 20%;
}

.sixthOfParent {
  width: 16%;
}

</style>
