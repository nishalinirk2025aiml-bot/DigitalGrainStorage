package com.example.DigitalGrainStorage.repository;

import com.example.DigitalGrainStorage.model.QualityTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QualityTestRepository extends JpaRepository<QualityTest, Long> {

    List<QualityTest> findByGrainLotId(Long grainLotId);
}