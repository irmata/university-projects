package com.yourgroup;

import com.mongodb.client.MongoClients;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class DatabaseClient {

    private MongoClient mongoClient;
    private MongoDatabase database;

    public DatabaseClient(String mongoUri) {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(mongoUri))
                .serverApi(serverApi)
                .build();

        this.mongoClient = MongoClients.create(settings);
        this.database = mongoClient.getDatabase("Dentist-Service-System");

        try {
            Bson command = new BsonDocument("ping", new BsonInt64(1));
            Document commandResult = database.runCommand(command);
            System.out.println("Successfully connected to MongoDB Database.");
        } catch (MongoException e) {
            System.err.println("Error connecting to MongoDB Database: \n");
            e.printStackTrace();
        }
    }

    public ObjectId addBooking(Booking booking) {
        Document bookingDoc = new Document("date", booking.getDate())
                .append("dentistId", booking.getDentistId())
                .append("patientId", booking.getPatientId())
                .append("dentalClinicId", booking.getDentalClinicId())
                .append("time", booking.getTime());
        database.getCollection("appointments").insertOne(bookingDoc);

        // Return the ObjectId of the newly inserted booking
        return (ObjectId)bookingDoc.get("_id");
    }

    public Document deleteBooking(String appointmentId) {
        ObjectId objectId = new ObjectId(appointmentId);
        MongoCollection<Document> collection = database.getCollection("appointments");
        Document booking = collection.find(Filters.eq("_id", objectId)).first();

        if (booking != null) {
            collection.deleteOne(Filters.eq("_id", objectId));
            System.out.println("Booking with ID " + appointmentId + " has been deleted.");
        } else {
            System.out.println("No booking found with ID " + appointmentId);
        }
        return booking;
    }

    public List<Booking> getAppointmentsForPatient(ObjectId patientId) {
        List<Booking> appointments = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("appointments");

        // queries the database for appointments with that patient id
        for (Document doc : collection.find(Filters.eq("patientId", patientId))) {
            Booking booking = documentToBooking(doc);
            appointments.add(booking);
        }

        return appointments;
    }
    public List<Booking> getAppointmentsForDentist(ObjectId dentistId) {
        List<Booking> appointments = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("appointments");

        for (Document doc : collection.find(Filters.eq("dentistId", dentistId))) {
            Booking booking = documentToBooking(doc);
            appointments.add(booking);
        }

        return appointments;
    }

    // method to convert document to booking object
    private Booking documentToBooking(Document doc) {
        Booking booking = new Booking(
                doc.getString("date"),
                doc.getObjectId("dentistId"),
                doc.getObjectId("patientId"),
                doc.getObjectId("dentalClinicId"),
                doc.getString("time")
        );
        booking.setId(doc.getObjectId("_id")); // Set the id of the booking
        return booking;
    }
    public String getPatientName(ObjectId patientId) {
        Document patientDoc = database.getCollection("patients").find(Filters.eq("_id", patientId)).first();
        return patientDoc != null ? patientDoc.getString("name") : "Name not found";
    }

    public String getDentistName(ObjectId dentistId) {
        Document dentistDoc = database.getCollection("dentists").find(Filters.eq("_id", dentistId)).first();
        return dentistDoc != null ? dentistDoc.getString("name") : null;
    }

    public String getClinicName(ObjectId clinicId) {
        Document clinicDoc = database.getCollection("dentalclinics").find(Filters.eq("_id", clinicId)).first();
        return clinicDoc != null ? clinicDoc.getString("name") : null;
    }
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Disconnected from Database.");
        }
    }
}