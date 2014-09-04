package com.zb.dalisi.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;  
import org.apache.activemq.ActiveMQConnectionFactory; 

public class JmsSender {
	private ConnectionFactory connectionFactory = null;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private MessageProducer producer = null;

	JmsSender() {

	}

	public void init() {
		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");

		try {

			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(Boolean.TRUE.booleanValue(),
					Session.AUTO_ACKNOWLEDGE);
			// Queue
			destination = session.createQueue("xkey");
			producer = session.createProducer(destination);
			// Topic
			/**
			 * Topic topic = session.createTopic("xkey.Topic"); producer =
			 * session.createProducer(topic);
			 */
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			sendMessage(session, producer);
			session.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void sendMessage(Session session, MessageProducer producer)
			throws JMSException {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 1; i <= 5; i++) {
			TextMessage message = session
					.createTextMessage("First ActiveMQ Test:::: " + i);
			// ·¢ËÍÏûÏ¢
			System.out.println("Sender£º" + "First ActiveMQ Test::: " + i);
			producer.send(message);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JmsSender jms = new JmsSender();
		jms.init();
	}

}
