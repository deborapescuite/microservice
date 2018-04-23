package com.dpgb.microservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MicroserviceApplication {
	private static final Logger logger = LogManager.getLogger(MicroserviceApplication.class);
	public static void main(String[] args) {
		logger.info("Applications is getting started");
		SpringApplication.run(MicroserviceApplication.class, args);
	}

}
