package com.example.demo;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.studio.v1.flow.Execution;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.type.PhoneNumber;
import com.twilio.converter.Promoter;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.net.URI;

@Component
public class ScheduledTasks {

    public static final String ACCOUNT_SID = System.getenv(ACCOUNT_SID);
    public static final String AUTH_TOKEN = System.getenv(AUTH_TOKEN);
    public static final String INCOMING_NUMBER = System.getenv(INCOMING_NUMBER);
    public static final String TWILIO_NUMBER = System.getenv(TWILIO_NUMBER);

    RestTemplate restTemplate = new RestTemplate();

    @Scheduled(cron = "1 * * * * *") // execute every minute
    public void getLatestAppointment()
    {
        String getURL = "http://localhost:8080/getLatestAppointment";
        ArrayList<Appointment> list = restTemplate.getForObject(getURL, ArrayList.class);
        System.out.println("LATEST APPOINTMENT:");
        System.out.println(list.get(0));

	Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                new PhoneNumber(INCOMING_NUMBER), // to phone number
                new PhoneNumber(TWILIO_NUMBER), // from twilio number
                "Reminder! Latest appointment is : " + list.get(0)
        ).create();

        System.out.println(message.getSid());
    }

    @Scheduled(cron = "0 */1 * * * *") // execute at the top of every hour 
    public void getChristmasCountdown()
    {
        LocalDate today = LocalDate.now();
        LocalDate xmas = LocalDate.of(2018, Month.DECEMBER, 25);
        LocalDate nextXmasDay = xmas.withYear(today.getYear());

        //If Christmas has occurred this year already, add 1 to the year.
        if (nextXmasDay.isBefore(today) || nextXmasDay.isEqual(today)) {
            nextXmasDay = nextXmasDay.plusYears(1);
        }

        Period p = Period.between(today, nextXmasDay);
        long p2 = ChronoUnit.DAYS.between(today, nextXmasDay);
        System.out.println("There are " + p2 + " days until Christmas."); 

	Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                new PhoneNumber(INCOMING_NUMBER), // to phone number
                new PhoneNumber(TWILIO_NUMBER), // from twilio number
                "Don't stress out! Only " + p2 + " more days until Christmas!")
            .setMediaUrl(Promoter.listOfOne(URI.create("https://media.giphy.com/media/hWa6QijPQGdLq/giphy-downsized-large.gif")))
            .create();

        System.out.println(message.getSid());

    }


}
