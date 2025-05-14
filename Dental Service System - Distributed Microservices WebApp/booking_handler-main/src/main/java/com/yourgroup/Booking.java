package com.yourgroup;

import org.bson.types.ObjectId;

public class Booking {
    private ObjectId id; // MongoDB auto-generated ID
    private String date;
    private ObjectId dentistId;
    private ObjectId patientId;
    private ObjectId dentalClinicId;
    private String time;

    // Constructors
    public Booking() {}

    public Booking(String date, ObjectId dentistId, ObjectId patientId, ObjectId dentalClinicId, String time) {
        this.date = date;
        this.dentistId = dentistId;
        this.patientId = patientId;
        this.dentalClinicId = dentalClinicId;
        this.time = time;
    }
    // Getters and setters
    public ObjectId getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public ObjectId getDentistId() {
        return dentistId;
    }

    public ObjectId getDentalClinicId() {
        return dentalClinicId;
    }

    public ObjectId getPatientId() {
        return patientId;
    }
    public void setId(ObjectId id) {this.id = id;}
}