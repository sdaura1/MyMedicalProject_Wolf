package com.sani.shaheed.mymedicalproject;

public class Medicine {

    int interval, dosage;
    String medName, description, entrydate, id;

    public Medicine(){

    }

    public Medicine(String medName, String description, int interval, String entrydate, int dosage){
        this.medName = medName;
        this.description = description;
        this.interval = interval;
        this.entrydate = entrydate;
        this.dosage = dosage;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }
}
