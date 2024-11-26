package com.onlineshop;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

// start with gradle runPublisher
public class MQTTPublisherExample {

    private static final String BROKER_URL = "tcp://ds-exercise-01.netd.cs.tu-dresden.de:1883";
    private static final String CLIENT_ID = "MQTTPublisherExample";
    private static final String TOPIC = "simple_topic";
 
    public static void main(String[] args) {

        try {
            // Create an MQTT client
            MqttClient publisher = new MqttClient(BROKER_URL, CLIENT_ID);

            // MQTT connection options
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // Connect to the broker
            System.out.println("Connecting to broker: " + BROKER_URL);
            publisher.connect(connOpts);
            System.out.println("Connected");

            Integer counter = 0;
 
            // Periodically publish messages
            while (true) {
                counter++;
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = now.format(formatter);
                
                // Create the JSON payload
                String payload = "{\"channel\": "+TOPIC+",  \"timestamp\": "+formattedDateTime+",\"value\": "+counter+"}";

                // Create a new MQTT message
                MqttMessage message = new MqttMessage(payload.getBytes());

                // Publish the message to the topic
                publisher.publish(TOPIC, message);
                System.out.println("Message published:\n\tTopic: " + TOPIC + "\n\tMessage: " + payload);
                // Wait for 1 second before sending the next message

                Thread.sleep(1000);
            }

        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
