package com.onlineshop;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

// start with gradle runSubscriber
public class MQTTSubscriberExample {

private static final String BROKER_URL = "tcp://ds-exercise-01.netd.cs.tu-dresden.de:1883";
private static final String CLIENT_ID = "MQTTSubscriberExample";
private static final String TOPIC = "simple_topic";

    public static void main(String[] args) {
    
        try {
            // Create an MQTT client
            MqttClient subscriber = new MqttClient(BROKER_URL, CLIENT_ID);

            // MQTT connection options
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // Callback to handle incoming messages and connection events
            subscriber.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost! " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("Message received:\n\tTopic: " + topic + "\n\tMessage: " + new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not needed for this case, as we are just receiving messages
                }
            });

            // Connect to the broker
            System.out.println("Connecting to broker: " + BROKER_URL);
            subscriber.connect(connOpts);
            System.out.println("Connected");

            // Subscribe to a topic
            subscriber.subscribe(TOPIC,0);
            System.out.println("Subscribed to topic: " + TOPIC);

            // Keep the client running to listen for messages
            while (true) {
                // The client will keep running and receiving messages.
                // We can add some delay to avoid busy-waiting.
                Thread.sleep(1000);
            }

        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
