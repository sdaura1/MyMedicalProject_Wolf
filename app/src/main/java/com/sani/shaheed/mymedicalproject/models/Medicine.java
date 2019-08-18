package com.sani.shaheed.mymedicalproject.models;

import java.sql.Date;

public class Medicine {

    int interval, id, dosage;
    String medName, description, start_date, finish_date, entrydate;

    public Medicine(){

    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getEntrydate() {
        return entrydate;
    }

    public void setEntrydate(String entrydate) {
        this.entrydate = entrydate;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(String finish_date) {
        this.finish_date = finish_date;
    }

    public Medicine(String medName, String description, int interval, String entrydate, int dosage,
                    String start_date, String finish_date){
        this.medName = medName;
        this.description = description;
        this.interval = interval;
        this.entrydate = entrydate;
        this.dosage = dosage;
        this.start_date = start_date;
        this.finish_date = finish_date;

    }

    public String getName() {
        return medName;
    }

    public void setName(String name) {
        this.medName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getDate(){
        return entrydate;
    }

    public void setDate(String entrydate){
        this.entrydate = entrydate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }
}
