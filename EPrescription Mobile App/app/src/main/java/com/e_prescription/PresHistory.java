package com.e_prescription;

public class PresHistory {
    private String doctor;
    private String medicines;
    private String symptoms;
    private String date;

    public PresHistory(String doctor, String medicines, String symptoms,String date) {
        this.doctor = doctor;
        this.medicines = medicines;
        this.symptoms = symptoms;
        this.date=date;
    }

    public PresHistory() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getMedicines() {
        return medicines;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
}
