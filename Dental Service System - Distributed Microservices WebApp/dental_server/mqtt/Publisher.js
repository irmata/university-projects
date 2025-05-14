function Publisher (mqttClient, topic, payload, qos = 2, retain = false) {
  mqttClient.publish(topic, payload, { qos, retain }, (error) => {
    if (error) {
      console.error('Error publishing message: ', error)
    } else {
      console.log('Published message to topic: ', topic)
      console.log('Payload:', payload)
    }
  })
}

module.exports = Publisher
