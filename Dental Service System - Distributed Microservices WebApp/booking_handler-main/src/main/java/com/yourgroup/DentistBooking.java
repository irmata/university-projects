package com.yourgroup;

public class DentistBooking extends Booking{
    private String patientName;
    private String clinicName;

    public DentistBooking(Booking booking, String patientName, String clinicName) {
        super(booking.getDate(), booking.getDentistId(), booking.getPatientId(), booking.getDentalClinicId(), booking.getTime());
        this.setId(booking.getId());
        this.patientName = patientName;
        this.clinicName = clinicName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }
}
