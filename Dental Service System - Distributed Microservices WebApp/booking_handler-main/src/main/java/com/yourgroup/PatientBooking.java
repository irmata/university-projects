package com.yourgroup;

public class PatientBooking extends Booking{
    private String dentistName;
    private String clinicName;

    public PatientBooking(Booking booking, String dentistName, String clinicName) {
        super(booking.getDate(), booking.getDentistId(), booking.getPatientId(), booking.getDentalClinicId(), booking.getTime());
        this.setId(booking.getId());
        this.dentistName = dentistName;
        this.clinicName = clinicName;
    }

    public String getDentistName() {
        return dentistName;
    }

    public void setDentistName(String dentistName) {
        this.dentistName = dentistName;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }
}
