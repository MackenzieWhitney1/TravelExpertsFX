package org.example.travelexpertsfx.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Fee {
    private SimpleStringProperty feeId;
    private SimpleStringProperty feeName;
    private SimpleDoubleProperty feeAmt;
    private SimpleStringProperty feeDesc;

    public Fee(String feeId, String feeName, Double feeAmt, String feeDesc) {
        this.feeId = new SimpleStringProperty(feeId);
        this.feeName = new SimpleStringProperty(feeName);
        this.feeAmt = new SimpleDoubleProperty(feeAmt);
        this.feeDesc = new SimpleStringProperty(feeDesc);
    }

    public String getFeeId() {
        return feeId.get();
    }

    public SimpleStringProperty feeIdProperty() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId.set(feeId);
    }

    public String getFeeName() {
        return feeName.get();
    }

    public SimpleStringProperty feeNameProperty() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName.set(feeName);
    }

    public double getFeeAmt() {
        return feeAmt.get();
    }

    public SimpleDoubleProperty feeAmtProperty() {
        return feeAmt;
    }

    public void setFeeAmt(double feeAmt) {
        this.feeAmt.set(feeAmt);
    }

    public String getFeeDesc() {
        return feeDesc.get();
    }

    public SimpleStringProperty feeDescProperty() {
        return feeDesc;
    }

    public void setFeeDesc(String feeDesc) {
        this.feeDesc.set(feeDesc);
    }
}