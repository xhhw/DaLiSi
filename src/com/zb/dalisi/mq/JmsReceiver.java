package com.zb.dalisi.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsReceiver {
	private ConnectionFactory connectionFactory = null;
	private Connection connection = null;
	private Session session = null;
	private MessageConsumer consumer = null;
	private Destination destination = null;

	public JmsReceiver() {

	}

	public void init() {
		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(Boolean.TRUE.booleanValue(),
					Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue("xkey");
			consumer = session.createConsumer(destination);
			consumer.setMessageListener(new MessageListener() {

				@Override
				public void onMessage(Message msg) {
					// TODO Auto-generated method stub
					TextMessage message = (TextMessage) msg;
					try {
						System.out.println("Receiver " + message.getText());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			});
			
			connection.start();
			
//			while (true) {
//				TextMessage message = (TextMessage) consumer.receive(1000);
//				if (null != message) {
//					System.out.println("Receiver " + message.getText());
//				} else {
//					break;
//				}
//			}
			Thread.sleep(1000);
			session.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JmsReceiver jms = new JmsReceiver();
		jms.init();
	}

}
