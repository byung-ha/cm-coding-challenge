package com.crewmeister.cmcodingchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.logging.Logger;

@SpringBootApplication
public class CmCodingChallengeApplication {
	private static final Logger LOGGER = Logger.getLogger( CmCodingChallengeApplication.class.getName() );

	public static void main(String[] args) {
		LOGGER.info("Start application");
		SpringApplication.run(CmCodingChallengeApplication.class, args);
		LOGGER.info("ran application");
	}

}
