package com.jumia.brunocardoso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BrunocardosoApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(BrunocardosoApplication.class);

	/**
	 * @SuppressWarnings("squid:S4823")
	 * Ignore SonarQube's suggestion for Make sure that command line arguments are used safely here.
	 * This is the Spring Boot default method to start the application.
	 */
	@SuppressWarnings("squid:S4823") //Ignore SonarQube's suggestion for Make sure that command line arguments are used safely here.
	public static void main(String[] args) {

		LOGGER.info("Initializing Bruno Cardoso application ........");

		SpringApplication.run(BrunocardosoApplication.class, args);
	}

}
