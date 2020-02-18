package com.yellowtaxipipeline;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

public class Producer implements Runnable {
	private String topic;

	public Producer(String topic) {
		this.topic = topic;
		try {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

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
	public void run() {

		try {

//			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.0.111:61616");
//
//			Connection connection = connectionFactory.createConnection();
//			connection.start();
//
//			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//
//			// Administrative object
//			Destination dest = session.createTopic(this.topic);
//
//			producer = session.createProducer(dest);
//			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
//
////			String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
//			message = session.createTextMessage("Consumer");
//
//			connection.close();

		} catch (Exception e) {
			System.out.println("caught " + e);
			e.printStackTrace();
		}
	}

	public void send(TripDatawithCrash tripDatawithCrash) {
		try {
			// print details of Bean object
//			while(message == null) {
////				message = session.createTextMessage("Consumer");
//				Thread.sleep(100);
//			}
			
			String tripDataMsg = gson.toJson(tripDatawithCrash);
//			System.out.println(tripDataMsg);
			message.setText(tripDataMsg);
//			System.out.println("sent message: " + message);
			
			producer.send(message);
//			Thread.sleep(100);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
