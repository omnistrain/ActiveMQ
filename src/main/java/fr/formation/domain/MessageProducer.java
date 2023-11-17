package fr.formation.domain;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.command.ActiveMQTopic;

public class MessageProducer {

	  public static void main(String[] args) throws Exception {
	        thread(new HelloWorldProducer(), false);
	        thread(new HelloWorldProducer(), false);
	        thread(new HelloWorldConsumer(), false);
	        Thread.sleep(10000);
	        thread(new HelloWorldConsumer(), false);
	        thread(new HelloWorldProducer(), false);
	        thread(new HelloWorldConsumer(), false);
	        thread(new HelloWorldProducer(), false);
	        Thread.sleep(10000);
	        thread(new HelloWorldConsumer(), false);
	        thread(new HelloWorldProducer(), false);
	        thread(new HelloWorldConsumer(), false);
	        thread(new HelloWorldConsumer(), false);
	        thread(new HelloWorldProducer(), false);
	        thread(new HelloWorldProducer(), false);
	        Thread.sleep(10000);
	        thread(new HelloWorldProducer(), false);
	        thread(new HelloWorldConsumer(), false);
	        thread(new HelloWorldConsumer(), false);
	        thread(new HelloWorldProducer(), false);
	        thread(new HelloWorldConsumer(), false);
	        thread(new HelloWorldProducer(), false);
	        thread(new HelloWorldConsumer(), false);
	        thread(new HelloWorldProducer(), false);
	        thread(new HelloWorldConsumer(), false);
	        thread(new HelloWorldConsumer(), false);
	        thread(new HelloWorldProducer(), false);
	    }

	    public static void thread(Runnable runnable, boolean daemon) {
	        Thread brokerThread = new Thread(runnable);
	        brokerThread.setDaemon(daemon);
	        brokerThread.start();
	    }

	    public static class HelloWorldProducer implements Runnable {
	        public void run() {
	            try {
	                // Create a ConnectionFactory
	                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

	                // Create a Connection
	                ActiveMQConnection connection = (ActiveMQConnection) connectionFactory.createConnection();
	                connection.start();

	                // Create a Session
	                ActiveMQSession session = (ActiveMQSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

	                // Create the destination (Topic or Queue)
	                ActiveMQDestination destination = (ActiveMQDestination) session.createTopic("Exemple");

	                // Create a MessageProducer from the Session to the Topic or Queue
	                ActiveMQMessageProducer producer = (ActiveMQMessageProducer) session.createProducer(destination);
	                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

	                // Create a messages
	                String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
	                ActiveMQTextMessage message = (ActiveMQTextMessage) session.createTextMessage(text);

	                // Tell the producer to send the message
	                System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
	                producer.send(message);

	                // Clean up
	                session.close();
	                connection.close();
	            }
	            catch (Exception e) {
	                System.out.println("Caught: " + e);
	                e.printStackTrace();
	            }
	        }
	    }

	    public static class HelloWorldConsumer implements Runnable {
	        public void run() {
	            try {

	                // Create a ConnectionFactory
	                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

	                // Create a Connection
	                ActiveMQConnection connection = (ActiveMQConnection) connectionFactory.createConnection();
	                connection.start();

	                //connection.setExceptionListener(this);

	                // Create a Session
	                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

	                // Create the destination (Topic or Queue)
	                Destination destination = session.createTopic("Exemple");

	                // Create a MessageConsumer from the Session to the Topic or Queue
	                ActiveMQMessageConsumer consumer = (ActiveMQMessageConsumer) session.createConsumer(destination);

	                // Wait for a message
	                ActiveMQMessage message = (ActiveMQMessage) consumer.receive(1000);

	                if (message instanceof ActiveMQTextMessage && message != null) {
	                	ActiveMQTextMessage textMessage = (ActiveMQTextMessage) message;
	                    String text = textMessage.getText();
	                    System.out.println("Received: " + text);
	                } else {
	                    System.out.println("Received: aucun message");
	                }

	                consumer.close();
	                session.close();
	                connection.close();
	            } catch (Exception e) {
	                System.out.println("Caught: " + e);
	                e.printStackTrace();
	            }
	        }

	        public synchronized void onException(JMSException ex) {
	            System.out.println("JMS Exception occured.  Shutting down client.");
	        }
	    }
		
	


}
