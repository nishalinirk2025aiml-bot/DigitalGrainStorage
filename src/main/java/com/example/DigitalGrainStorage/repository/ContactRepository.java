package com.example.DigitalGrainStorage.repository;

import com.example.DigitalGrainStorage.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}