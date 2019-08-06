package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.studio.v1.flow.Execution;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.type.PhoneNumber;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@RestController
public class RESTController {

    private int count = 1;

    public static final String ACCOUNT_SID = System.getenv(ACCOUNT_SID);
    public static final String AUTH_TOKEN = System.getenv(AUTH_TOKEN);
    public static final String INCOMING_NUMBER = System.getenv(INCOMING_NUMBER);
    public static final String TWILIO_NUMBER = System.getenv(TWILIO_NUMBER);
    public static final String TWILIO_FLOW = System.getenv(TWILIO_FLOW);

    @Autowired
    private AppointmentDao appointmentDao;

    @RequestMapping(value = "/createFlow", method = RequestMethod.POST)
    public void createFlow() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Execution execution = Execution.creator(
                TWILIO_FLOW, // make sure this is the right Flow and has been set on Twilio phone
                new com.twilio.type.PhoneNumber(INCOMING_NUMBER), // incoming number
                new com.twilio.type.PhoneNumber(TWILIO_NUMBER)) // twilio number
                .create();

        System.out.println(execution.getSid());
    }

    @Logging
    @Timed
    @RequestMapping(value = "/addAppointment", method = RequestMethod.POST)
    public Appointment addAppointment(@RequestParam(value="details") String details) throws IOException {
        Appointment appt = new Appointment();
	appt.setDetails(details);
        appointmentDao.createAppointment(appt);
	System.out.println("added");
        return appt;
    }

    @RequestMapping("/appointByName")
    public Appointment appointmentByName(@RequestParam String details) throws IOException {
        Appointment appt = new Appointment();
        appt.setDetails(details);
        return appt;
    }

    @Logging
    @Timed
    @RequestMapping(value = "/getAppointment", method = RequestMethod.GET)
    public Appointment getAppointment(@RequestParam(value="id") String id) throws IOException {
        int intId = Integer.parseInt(id);
        Appointment appt = new Appointment();
        appt = appointmentDao.getAppointmentById(intId);
        String strBody = appt.getDetails();

	// create and send a message with the appt details string
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new PhoneNumber(INCOMING_NUMBER), // to phone number
                new PhoneNumber(TWILIO_NUMBER), // from twilio number
                strBody
        ).create();

	System.out.println(message.getSid());

        return appt;
    }

    @Logging
    @Timed
    @RequestMapping(value = "/deleteAppointment", method = RequestMethod.GET)
    public Appointment deleteAppointment(@RequestParam(value="id") String id) throws IOException {
        int intId = Integer.parseInt(id);
        Appointment appt = new Appointment();
        appt = appointmentDao.getAppointmentById(intId);
        appointmentDao.deleteAppointment(appt);
        System.out.println("deleted.");
        return appt;
    }

    @Logging
    @Timed
    @RequestMapping(value = "/updateAppointment", method = RequestMethod.GET)
    public Appointment updateAppointment(@RequestParam(value="id") String id,
				     @RequestParam(value="details") String details) throws IOException {
        int intId = Integer.parseInt(id);
        Appointment appt = new Appointment();
        appt = appointmentDao.getAppointmentById(intId);
	appt.setDetails(details);
	appointmentDao.updateAppointment(appt);
	System.out.println("updated.");
        return appt;
    }

    @Logging
    @Timed
    @RequestMapping(value = "/getLatestAppointment", method = RequestMethod.GET)
    public List<Appointment> getLatestAppointment() throws IOException {
        return appointmentDao.getLatestAppointment();
    }

}
