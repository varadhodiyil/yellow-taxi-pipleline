package com.yellowtaxipipeline;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.google.gson.Gson;
import com.yellowtaxipipeline.model.YellowTripData;

public class ProducerListener implements MessageListener {

	@Override
	public void onMessage(Message message) {

		try {
			System.out.println("Enters ProducerListener");

			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				String text = textMessage.getText();
				Gson gson = new Gson();
				YellowTripData[] YellowTripDataArray = gson.fromJson(text, YellowTripData[].class);
				for (YellowTripData yellowTripData : YellowTripDataArray) {
					System.out.println("Received: " + Thread.currentThread().getName() + yellowTripData.getVendorID());

//							taxiZoneLookup.getLocationID() + ":" + taxiZoneLookup.getBorough()
//					+ ":" + taxiZoneLookup.getZone() + ":" + taxiZoneLookup.getServiceZone());
				}

			} else {
				System.out.println("Received: " + message);
			}
		} catch (JMSException e) {
			e.getStackTrace();
		}

	}

}
