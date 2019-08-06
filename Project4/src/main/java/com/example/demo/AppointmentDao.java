package com.example.demo;

import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Query;
import java.util.List;

/**
 * Data Access Object - provide some specific data operations without exposing details of the database
 * Access data for the Greeting entity.
 * Repository annotation allows Spring to find and configure the DAO.
 * Transactional annotation will cause Spring to call begin() and commit()
 * at the start/end of the method. If exception occurs it will also call rollback().
 */
@Repository
@Transactional
public class AppointmentDao {

    //PersistenceContext annotation used to specify there is a database source.
    //EntityManager is used to create and remove persistent entity instances,
    //to find entities by their primary key, and to query over entities.
    @PersistenceContext
    private EntityManager entityManager;

    public void createAppointment(Appointment appointment) {
        entityManager.persist(appointment);
        return;
    }

    public Appointment getAppointmentById(int id) {
        return entityManager.find(Appointment.class, id);
    }

    public void deleteAppointment(Appointment appointment) {
        if (entityManager.contains(appointment))
            entityManager.remove(appointment);
        else {
            System.out.println("appointment to be deleted not found.");
        }
        return;
    }

    public void updateAppointment(Appointment appointment) {
	entityManager.merge(appointment);
        return;
    }

    public List<Appointment> getLatestAppointment() {
        Query q = entityManager.createNativeQuery("SELECT * FROM appointments ORDER BY id DESC LIMIT 1");
        return q.getResultList();
    }

}
