package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.studio.v1.flow.Execution;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.type.PhoneNumber;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {

	public static final String ACCOUNT_SID = System.getenv(ACCOUNT_SID);
	public static final String AUTH_TOKEN = System.getenv(AUTH_TOKEN);
	public static final String INCOMING_NUMBER = System.getenv(INCOMING_NUMBER);
	public static final String TWILIO_NUMBER = System.getenv(TWILIO_NUMBER);
	public static final String TWILIO_FLOW = System.getenv(TWILIO_FLOW);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        	Execution execution = Execution.creator(
					TWILIO_FLOW, // make sure this is the right Flow and has been set on Twilio$
                	new com.twilio.type.PhoneNumber(INCOMING_NUMBER), // incoming number
                	new com.twilio.type.PhoneNumber(TWILIO_NUMBER)) // twilio number
                	.create();

        	System.out.println(execution.getSid());

	}
}
