package org.example;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "patients")
@TypeAlias("patient")
public class patient {
    @Id
    private String id;
    @Indexed(unique = true)
    private String email;
    private String name;
    private String password;
    @Field("appointment_id")
    private List<String> appointment_id; // List to store appointment IDs

    public patient() {
        // Initialize the appointmentIds list
        this.appointment_id = new ArrayList<>();
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public List<String> getAppointmentIds() {return appointment_id;}
}