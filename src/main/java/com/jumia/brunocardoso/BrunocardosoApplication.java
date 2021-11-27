package com.jumia.brunocardoso;

import com.jumia.brunocardoso.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@SpringBootApplication
public class BrunocardosoApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(BrunocardosoApplication.class);

	public static void main(String[] args) {

		LOGGER.info("Initializing Bruno Cardoso application ........");

		SpringApplication.run(BrunocardosoApplication.class, args);
	}

}
