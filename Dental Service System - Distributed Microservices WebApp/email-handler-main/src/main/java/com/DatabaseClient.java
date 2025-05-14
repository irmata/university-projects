package com;

import com.mongodb.client.MongoClients;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

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

    /*
     * Note: getAppointmentData and getUserData does not perform checks for 
     * the existence of values in the retrieved documents. It assumes that 
     * these fields are always present and correctly formatted in MongoDB.
     */
    public String getAppointmentData(String appointmentId) {
        try {
            ObjectId appointmentObject = new ObjectId(appointmentId);
    
            MongoCollection<Document> appointments = database.getCollection("appointments");
            Document appointment = appointments.find(new Document("_id", appointmentObject)).first();
            if (appointment == null) {
                return null;
            }
    
            ObjectId patientObjectId = appointment.getObjectId("patientId");
            ObjectId dentistObjectId = appointment.getObjectId("dentistId");
            ObjectId clinicObjectId = appointment.getObjectId("dentalClinicId");

            String patientId = patientObjectId.toHexString();
            String dentistId = dentistObjectId.toHexString();
            String clinicId = clinicObjectId.toHexString();
            String timeSlot = appointment.getString("time");
            String date = appointment.getString("date");
    
            return String.join(",", patientId, dentistId, timeSlot, date, clinicId);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public String getUserData(String patientId, String dentistId) {
        try {    
            ObjectId patientObject = new ObjectId(patientId);
            ObjectId dentistObject = new ObjectId(dentistId);
            
            MongoCollection<Document> patients = database.getCollection("patients");
            Document patient = patients.find(new Document("_id", patientObject)).first();
            if (patient == null) {
                return null;
            }
            String patientEmail = patient.getString("email");
            String patientName = patient.getString("name");
    
            MongoCollection<Document> dentists = database.getCollection("dentists");
            Document dentist = dentists.find(new Document("_id", dentistObject)).first();
            if (dentist == null) {
                return null;
            }
            String dentistEmail = dentist.getString("email");
            String dentistName = dentist.getString("name");
    
            return String.join(",", patientEmail, dentistEmail, patientName, dentistName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public String getClinicName(String clinicId){
        try {    
            ObjectId clinicObject = new ObjectId(clinicId);
            
            MongoCollection<Document> clinics = database.getCollection("dentalclinics");
            Document clinic = clinics.find(new Document("_id", clinicObject)).first();
            if (clinic == null) {
                return null;
            }
            String clinicName = clinic.getString("name");
    
            return clinicName;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Disconnected from Database.");
        }
    }
}
