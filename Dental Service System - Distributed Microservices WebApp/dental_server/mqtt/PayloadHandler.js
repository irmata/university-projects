class PayloadHandler {
  on (mqttClient, filterTopic, correlationID) {
    return new Promise((resolve, reject) => {
      const messageListener = (incomingTopic, message) => {
        const parsedMessage = JSON.parse(message.toString())
        console.log(`Received message on topic ${incomingTopic}: ${message.toString()}`)

        if (parsedMessage.correlationID === correlationID && filterTopic === incomingTopic) {
          mqttClient.removeListener('message', messageListener)
          resolve(message)
        }
      }

      mqttClient.on('message', messageListener)
    })
  }
}

module.exports = PayloadHandler
