package com.example.hospitalsystem;

public class Appointment {

    private String patientName, email, phone, desc, status,sick,time,date;
    public Appointment(){

    }
    public Appointment(String patientName, String email, String phone, String desc, String status
        ,String date,String sick,String time){
        this.patientName = patientName;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.desc = desc;
        this.time = time;
        this.date = date;
        this.sick = sick;
    }

    public String getSick() {
        return sick;
    }

    public void setSick(String sick) {
        this.sick = sick;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
