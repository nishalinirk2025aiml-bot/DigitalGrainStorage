package com.example.DigitalGrainStorage.repository;

import com.example.DigitalGrainStorage.model.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalEntryRepository
        extends JpaRepository<JournalEntry, Long> {

    List<JournalEntry> findByDebitAccount(String debitAccount);

    List<JournalEntry> findByCreditAccount(String creditAccount);
}