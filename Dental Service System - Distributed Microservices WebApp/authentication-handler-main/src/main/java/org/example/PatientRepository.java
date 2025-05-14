package org.example;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

@Document(collection = "patients")

public interface PatientRepository extends MongoRepository<patient, String> {
    Optional<patient> findById(String id);
    Optional<patient> findByEmail(String email);
    void deleteById(String id);
}
