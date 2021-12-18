package com.autobots.innopark.data.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Fine
{

    String avenue_name;
    @ServerTimestamp
    Date created_datetime;
    @ServerTimestamp
    Date due_datetime;
    double fine_amount;
    String fine_description;
    String fine_type;
    String footage;
    boolean is_accepted;
    boolean is_disputed;
    boolean is_paid;
    boolean is_reviewed;
    String session_id;
    String vehicle;
    String fineID;
    String parentDocumentId;
    String payment_link;

    public Fine() {}


    public Fine(String avenue_name, Date created_datetime, Date due_datetime, double fine_amount, String fine_description, String fine_type, String footage, boolean is_accepted, boolean is_disputed, boolean is_paid, boolean is_reviewed, String session_id, String vehicle, String fineID, String parentDocumentId, String payment_link) {
        this.avenue_name = avenue_name;
        this.created_datetime = created_datetime;
        this.due_datetime = due_datetime;
        this.fine_amount = fine_amount;
        this.fine_description = fine_description;
        this.fine_type = fine_type;
        this.footage = footage;
        this.is_accepted = is_accepted;
        this.is_disputed = is_disputed;
        this.is_paid = is_paid;
        this.is_reviewed = is_reviewed;
        this.session_id = session_id;
        this.vehicle = vehicle;
        this.fineID = fineID;
        this.parentDocumentId = parentDocumentId;
    }

    public String getPayment_link() {
        return payment_link;
    }

    public void setPayment_link(String payment_link) {
        this.payment_link = payment_link;
    }

    public String getParentDocumentId() {
        return parentDocumentId;
    }

    public void setParentDocumentId(String parentDocumentId) {
        this.parentDocumentId = parentDocumentId;
    }

    public String getFineID() {
        return fineID;
    }

    public void setFineID(String fineID) {
        this.fineID = fineID;
    }

    public String getAvenue_name() {
        return avenue_name;
    }

    public void setAvenue_name(String avenue_name) {
        this.avenue_name = avenue_name;
    }

    public Date getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(Date created_datetime) {
        this.created_datetime = created_datetime;
    }

    public Date getDue_datetime() {
        return due_datetime;
    }

    public void setDue_datetime(Date due_datetime) {
        this.due_datetime = due_datetime;
    }

    public double getFine_amount() {
        return fine_amount;
    }

    public void setFine_amount(double fine_amount) {
        this.fine_amount = fine_amount;
    }

    public String getFine_description() {
        return fine_description;
    }

    public void setFine_description(String fine_description) {
        this.fine_description = fine_description;
    }

    public String getFine_type() {
        return fine_type;
    }

    public void setFine_type(String fine_type) {
        this.fine_type = fine_type;
    }

    public String getFootage() {
        return footage;
    }

    public void setFootage(String footage) {
        this.footage = footage;
    }

    public boolean isIs_accepted() {
        return is_accepted;
    }

    public void setIs_accepted(boolean is_accepted) {
        this.is_accepted = is_accepted;
    }

    public boolean isIs_disputed() {
        return is_disputed;
    }

    public void setIs_disputed(boolean is_disputed) {
        this.is_disputed = is_disputed;
    }

    public boolean isIs_paid() {
        return is_paid;
    }

    public void setIs_paid(boolean is_paid) {
        this.is_paid = is_paid;
    }

    public boolean isIs_reviewed() {
        return is_reviewed;
    }

    public void setIs_reviewed(boolean is_reviewed) {
        this.is_reviewed = is_reviewed;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }
}
