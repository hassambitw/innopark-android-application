package com.autobots.innopark.data;

public class Fine
{
    int fineID;
    double fineAmount;
    //violation type, footage


    public Fine(int fineID, double fineAmount) {
        this.fineID = fineID;
        this.fineAmount = fineAmount;
    }

    public int getFineID() {
        return fineID;
    }

    public void setFineID(int fineID) {
        this.fineID = fineID;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }
}
