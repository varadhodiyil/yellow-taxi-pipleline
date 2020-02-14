package com.assignment1;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Consumer implements Runnable {

	public void run() {
		
		
		try {
			
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.1.12:61616");
			
			Connection connection = connectionFactory.createConnection();			
			
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Destination dest = session.createTopic("dataset");
			
			MessageConsumer consumer = session.createConsumer(dest);
			
			MyListener myListener = new MyListener();
			
			consumer.setMessageListener(myListener);
			
			connection.start();
			String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
			// Synchronous message consumption, blocks until a message 
			// arrives or can time out if the message does not arrive 
			// within the specified time limit
			
//			consumer.close();
//			session.close();
//			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}

}
