<template>
  <div>
    <LoginSignupModal id="loginModal"/>
    <div id="loginContainer">
      <LoginSignupInputForm>
        <LoginSignupFormHeader v-show="!isSuccessfulLogin" selected="login"/>
        <div id="inputContainer" v-show="!isSuccessfulLogin">
          <div id="emailPasswordForgotContainer">
            <div id="emailPasswordInputContainer">
              <div>
                <LoginSignupTextInput type="email" placeholder="Email" @validateAfterLoseFocus="validate($event, 'email')"/>
                <InvalidPrompt ref="notValidEmailFormatPrompt" invalidText="Not a Valid Email Format" invalidPromptWrapperWidth="smallWidth"/>
              </div>
              <div>
                <LoginSignupTextInput type="password" placeholder="Password" @validateAfterLoseFocus="validate($event, 'password')" @inputChange="restartLoginPrep"/>
                <InvalidPrompt ref="passwordFailMeetReqPrompt" invalidText="Password Must Contain (min): 4 Chars, 1 Digit. Max Pass Length: 30" invalidPromptWrapperWidth="smallWidth"/>
              </div>
            </div>
            <div id="forgotPassword">Forgot Password</div>
          </div>
          <div id="checkRememberMeContainer">
            <input type="checkbox">
            <div id="rememberMe">Remember Me</div>
          </div>
          <LoginSignupNextButton v-show="!loginIssued" id="loginNextButton" @dragOverEmit="evaluateValidationState" @clickEmit="login" :canContinue="canContinue"/>
        </div>
        <InvalidPrompt ref="existsInvalidInput" :invalidText="inabilityToProceedReason" invalidPromptWrapperWidth="mediumWidth"/>
        <ContinuePrompt ref="continuePrompt" id="continuePrompt" :canContinue="canContinue"/>
        <div id="loginResultContainer" v-show="loginIssued || loginPostCancelled">
          <div id="loginConStatusContainer" :class="[!loginPostCancelled ? 'loadingPrompt' : 'failEstablishConPrompt']" v-show="!loginPostResponse">{{ !loginPostCancelled ? 'Logging In...': 'Could Not Establish Connection To Servers, Try Again Later.' }}
          </div>
          <div v-show="loginPostResponse">
            <div v-show="!isSuccessfulLogin">
              <p>User Does Not Exist.</p>
            </div>
            <div>
              <p>Welcome {{ customerName }}</p>
            </div>
          </div>
          <div v-show="(!loginPostResponse && loginPostCancelled)" id="retryLogin" @click="login">Retry</div>
        </div>
      </LoginSignupInputForm>
    </div>
  </div>
</template>

<script>
import LoginSignupModal from './../components/LoginSignupModal'

import LoginSignupInputForm from './../components/LoginSignupInputForm'

import LoginSignupFormHeader from './../components/LoginSignupFormHeader'

import LoginSignupTextInput from './../components/LoginSignupTextInput'

import LoginSignupNextButton from './../components/LoginSignupNextButton'

import InvalidPrompt from './../components/InvalidPrompt'

import ContinuePrompt from '../components/ContinuePrompt.vue'

import { Api } from '@/Api'

export default {
  data() {
    return {
      formInputData: {
        email: '',
        password: ''
      },
      canContinue: false,
      inabilityToProceedReason: '',
      nrOfExistingInvalidInputs: [],
      setTimeoutTimerScheduleID: '',
      loginIssued: false,
      loginPostResponse: false,
      isSuccessfulLogin: false,
      loginPostCancelled: false,
      customerName: ''
    }
  },
  methods: {
    validate(inputField, caseType) {
      const inputValue = inputField.currInput
      let isValidResult = true
      const emailRegex = /^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/
      const normalPassReq = /^(?=(?:[^A-Za-z]*[A-Za-z]){4})(?=.*\d){1}.{0,30}$/

      switch (caseType) {
        case 'email':
          isValidResult = emailRegex.test(inputValue)
          if (isValidResult) {
            this.$refs.notValidEmailFormatPrompt.showError = false
          } else {
            this.$refs.notValidEmailFormatPrompt.showError = true
          }
          break
        case 'password':
          isValidResult = normalPassReq.test(inputValue)
          if (isValidResult) {
            this.$refs.passwordFailMeetReqPrompt.showError = false
          } else {
            this.$refs.passwordFailMeetReqPrompt.showError = true
          }
          break
      }
      if (!isValidResult) {
        if (!inputField.isInvalidated) { // green to red
          this.nrOfExistingInvalidInputs.push(0)
          inputField.isValidInputBox = false
          inputField.isInvalidated = true
        }
      } else {
        if (inputField.isInvalidated) { // red to green
          this.nrOfExistingInvalidInputs.pop()
          inputField.isValidInputBox = true
          inputField.isInvalidated = false
        } else if (inputField.isInvalidated === null) {
          inputField.isValidInputBox = true
          inputField.isInvalidated = false
        }
        this.formInputData[caseType] = inputValue
        console.log(this.formInputData)
      }
    },
    restartLoginPrep() {
      this.loginPostCancelled = false
      this.loginIssued = false
    },
    evaluateValidationState() {
      if (this.nrOfExistingInvalidInputs.length > 0) {
        this.canContinue = false
        this.$refs.existsInvalidInput.showError = true
        this.inabilityToProceedReason = 'Must Resolve Issues Before Continuing'
        this.setErrorDisplayTimeout()
      } else {
        const arrOfcurrentSceneInputData = Object.values(this.formInputData)
        for (const val of arrOfcurrentSceneInputData) {
          if (!val) {
            this.canContinue = false
            this.$refs.existsInvalidInput.showError = true
            this.inabilityToProceedReason = 'Please Fill in Remaining Input Fields'
            this.setErrorDisplayTimeout()
            return
          }
        }
        if (!this.canContinue) {
          this.canContinue = true
          this.$refs.existsInvalidInput.showError = false
          if (this.setTimeoutTimerScheduleID) {
            clearTimeout(this.setTimeoutTimerScheduleID)
          }
        }
      }
    },
    setErrorDisplayTimeout() {
      this.setTimeoutTimerScheduleID = setTimeout(() => {
        this.$refs.existsInvalidInput.showError = false
      }, 3500)
    },
    async login() {
      if (this.canContinue || this.loginPostCancelled) {
        const { email, password } = this.formInputData
        const body = {
          email,
          password
        }
        try {
          this.loginIssued = true
          this.canContinue = this.loginPostCancelled = false
          const response = await Api.post('/customers/login', body, {
            timeout: 8000
          })
          this.loginPostResponse = true
          if (response) {
            console.log(response)
            this.isSuccessfulLogin = true
            this.customerName = response.data.message
          } else {
            this.isSuccessfulLogin = false
          }
        } catch {
          this.loginPostCancelled = true
          console.log('im hit')
        }
      }
    }
  },
  beforeDestroy() {
    if (this.setTimeoutTimerScheduleID) {
      clearTimeout(this.setTimeoutTimerScheduleID)
    }
  },
  components: { LoginSignupModal, LoginSignupInputForm, LoginSignupFormHeader, LoginSignupTextInput, LoginSignupNextButton, InvalidPrompt, ContinuePrompt }
}

</script>

<style>

#loginModal {
  z-index: 2;
}

#loginContainer {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 2;
}

#inputContainer {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

#emailPasswordInputContainer > * {
  margin-top: 0.4em;
}

#checkRememberMeContainer {
  display: flex;
  margin-top: 1.5em;
}

#forgotPassword {
  margin-top: 1.2em;
}

#rememberMe {
  margin-left: 1em;
}

#loginNextButton {
  margin-top: 0.4em;
}

#continuePrompt {
  position: absolute;
  top: 92%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.loadingPrompt {
  color: rgb(76, 127, 255);
}

.failEstablishConPrompt {
  color: rgba(207, 135, 0, 0.931);
}

#retryLogin {
  text-align: center;
  color: rgb(252, 195, 161);
  font-size: 1.4em;
}

#retryLogin:hover {
  font-size: 1.6em;
  color: rgba(124, 120, 65, 0.57);
  transition-duration: 500ms;
  text-decoration: underline;
  cursor: pointer;
}

</style>
