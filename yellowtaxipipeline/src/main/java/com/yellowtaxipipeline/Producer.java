package com.yellowtaxipipeline;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.gson.Gson;

/*
 * Generic producer class for Pulishing message
 */
public class Producer<T> implements Runnable {
	private String topic;

	public Producer(String topic) {
		this.topic = topic;
		try {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Constants.MQ_HOST);

			Connection connection = connectionFactory.createConnection();
			connection.start();
//connection.
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// Administrative object
			Destination dest = session.createTopic(this.topic);

			producer = session.createProducer(dest);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

//		String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
			message = session.createTextMessage("Consumer");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	Session session;
	MessageProducer producer;
	TextMessage message;
	Gson gson = new Gson();

	@Override
	public void run() {

		try {

		} catch (Exception e) {
			System.out.println("caught " + e);
			e.printStackTrace();
		}
	}

	/*
	 * Method to Publish the message retrieve from Type T
	 */
	public void send(T t) {
		try {

			String messageJson = gson.toJson(t);
			message.setText(messageJson);
			producer.send(message);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
