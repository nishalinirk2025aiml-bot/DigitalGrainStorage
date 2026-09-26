package com.example.DigitalGrainStorage.repository;

import com.example.DigitalGrainStorage.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByPaymentType(String paymentType);

    List<Payment> findByReferenceId(Long referenceId);
}