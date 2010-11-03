package net.raescott.logging;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A simple project to explore Logging with Aspects.
 */
public class Log {

	public static void main(String[] args) {
		System.out.println("Simple program to illustrate AOP logging.");
		System.out.println("First we'll get a simple regular Logger:");

		Logger logger = Logger.getLogger("myLogger");
		System.out.println("The log level is currently: " + logger.getLevel());
		logger.setLevel(Level.FINEST);  // Doesn't work!
		System.out.println("The log level is currently: " + logger.getLevel());
		logger.fine("This is s regular fine log message.");
		logger.finer("This is s regular finer log message.");
		logger.finest("This is s regular finest log message.");
		logger.warning("This is s regular warning log message.");
		logger.severe("This is s regular severe log message.");

		// Let's initialize a Spring Bean container now.
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		BeanFactory bf = ctx;

		// Make sure Spring is working okay.
		MyClassToLog myClassToLog = (MyClassToLog) ctx.getBean("myClassToLog");
		System.out.println("myClassToLog: " + myClassToLog);

		// Add in AOP here.
		System.out.println("This is AOP Logging: ");
		myClassToLog.getAttribute1();
	}
}
