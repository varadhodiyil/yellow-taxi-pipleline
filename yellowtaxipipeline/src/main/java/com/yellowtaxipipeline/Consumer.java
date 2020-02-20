package com.yellowtaxipipeline;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;


/*
 * Class for Consumer
 */
public class Consumer implements Runnable {
	private String topic;

	public Consumer(String topic) {
		this.topic = topic;
	}

	/*
	 * run method of Thread initialized with AticveMQ connection
	 */
	@Override
	public void run() {
		try {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Constants.MQ_HOST);

			Connection connection = connectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Destination dest = session.createTopic(this.topic);

			MessageConsumer consumer = session.createConsumer(dest);

			MyListener myListener = new MyListener(this.topic);

			consumer.setMessageListener(myListener);

			

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
