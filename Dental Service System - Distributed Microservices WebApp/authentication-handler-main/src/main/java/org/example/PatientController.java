package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerPatient(@RequestBody patient patient) {
        Optional<org.example.patient> existingPatient = patientRepository.findByEmail(patient.getEmail());

        // prevents patients with duplicate email addresses from registering
        if (existingPatient.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use.");
        }
        // prevents invalid email formats
        if (patient.getEmail() == null || !isValidEmail(patient.getEmail())) {
            return ResponseEntity.badRequest().body("Invalid email format.");
        }
        // prevents empty names
        if (patient.getName() == null || patient.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Name cannot be empty.");
        }
        // prevents blank passwords
        if (patient.getPassword() == null || patient.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Password cannot be blank.");
        }

        // encrypts password
        String encodedPassword = passwordEncoder.encode(patient.getPassword());
        patient.setPassword(encodedPassword);

        // finally saves patient to database
        org.example.patient savedPatient = patientRepository.save(patient);
        return ResponseEntity.ok(savedPatient);
    }

    // GET all patients
    @GetMapping("/")
    public List<patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // GET patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable String id) {
        Optional<patient> patient = patientRepository.findById(id);
        if (!patient.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient with that ID not found.");
        }
        return ResponseEntity.ok(patient.get());
    }

    // GET patient by Email (must be specified as a parameter, e.g. api/patients/emails?email=test1@test.com)
    @GetMapping("/emails")
    public ResponseEntity<?> getPatientByEmail(@RequestParam String email) {
        Optional<patient> patient = patientRepository.findByEmail(email);
        if (!patient.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient with that email not found.");
        }
        return ResponseEntity.ok(patient.get());
    }

    // DELETE patient by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatientById(@PathVariable String id) {
        Optional<patient> patient = patientRepository.findById(id);
        if (!patient.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient with that ID not found.");
        }
        patientRepository.deleteById(id);
        return ResponseEntity.ok("Patient successfully deleted from the database");
    }

    // PATCH patient email, name or password
    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable String id, @RequestBody PatientUpdate update) {
        Optional<patient> existingPatient = patientRepository.findById(id);

        if (!existingPatient.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient with that ID not found.");
        }

        patient patient = existingPatient.get();

        // checks if email is provided and valid
        if (update.getEmail() != null) {
            if (!isValidEmail(update.getEmail())) {
                return ResponseEntity.badRequest().body("Invalid email format");
            }
            patient.setEmail(update.getEmail());
        }

        // checks if name is provided and not empty
        if (update.getName() != null) {
            if (update.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Name cannot be empty");
            }
            patient.setName(update.getName());
        }

        // checks if password is provided and not empty
        if (update.getPassword() != null) {
            if (update.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Password cannot be empty");
            }
            String encodedPassword = passwordEncoder.encode(update.getPassword());
            patient.setPassword(encodedPassword);
        }

        // saves updated patient
        patientRepository.save(patient);
        return ResponseEntity.ok(patient);
    }

    // regex function for checking if the email is in a valid format
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return email != null && pat.matcher(email).matches();
    }


}
