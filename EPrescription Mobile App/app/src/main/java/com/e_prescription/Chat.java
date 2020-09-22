package com.e_prescription;

public class Chat {

    private String doctor,medicines,symptoms;

    public Chat(String doctor, String medicines, String symptoms) {
        this.doctor = doctor;
        this.medicines = medicines;
        this.symptoms = symptoms;
    }

    public String getDoctor() {
        return doctor;
    }

    public String getMedicines() {
        return medicines;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
}


