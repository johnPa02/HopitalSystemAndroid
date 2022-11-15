package com.example.hospitalsystem;

public class Appointment {

    private String patientName, email, phone, desc, status;
    public Appointment(){

    }
    public Appointment(String patientName, String email, String phone, String desc, String status){
        this.patientName = patientName;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.desc = desc;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
