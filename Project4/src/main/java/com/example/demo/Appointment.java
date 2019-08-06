package com.example.demo;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "appointments")
public class Appointment implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String details;

    public Appointment() {}

    public Appointment(int id, String details) {
        this.details = details;
    }

    public String toString()
    {
        return this.getId() + ", " + this.getDetails();
    }

    public int getId() {return id;}

    public String getDetails() {return details;}

    public void setDetails(String details) {this.details = details;}
}
