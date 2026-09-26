package com.example.DigitalGrainStorage.model;

import jakarta.persistence.*;

@Entity
@Table(name = "quality_tests")
public class QualityTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long grainLotId;

    private double moisture;
    private double foreignMatter;
    private double damagedGrain;

    private String grade;
    private String remarks;

    public QualityTest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGrainLotId() {
        return grainLotId;
    }

    public void setGrainLotId(Long grainLotId) {
        this.grainLotId = grainLotId;
    }

    public double getMoisture() {
        return moisture;
    }

    public void setMoisture(double moisture) {
        this.moisture = moisture;
    }

    public double getForeignMatter() {
        return foreignMatter;
    }

    public void setForeignMatter(double foreignMatter) {
        this.foreignMatter = foreignMatter;
    }

    public double getDamagedGrain() {
        return damagedGrain;
    }

    public void setDamagedGrain(double damagedGrain) {
        this.damagedGrain = damagedGrain;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}