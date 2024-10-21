package org.example.travelexpertsfx.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

public class Booking {


    private final SimpleIntegerProperty bookingId;
    private final SimpleObjectProperty<Date> bookingDate;
    private final SimpleStringProperty bookingNo;
    private final SimpleDoubleProperty travelerCount;
    private final SimpleIntegerProperty customerId;
    private final SimpleStringProperty tripTypeId;
    private final SimpleIntegerProperty packageId;
    public Booking(int bookingId,
                   Date bookingDate,
                   String bookingNo,
                   double travelerCount,
                   int customerId,
                   String tripTypeId,
                   int packageId){
        this.bookingId= new SimpleIntegerProperty(bookingId);
        this.bookingDate = new SimpleObjectProperty<Date>(bookingDate);
        this.bookingNo = new SimpleStringProperty(bookingNo);
        this.travelerCount = new SimpleDoubleProperty(travelerCount);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.tripTypeId = new SimpleStringProperty(tripTypeId);
        this.packageId = new SimpleIntegerProperty(packageId);
    }
    public int getBookingId() {
        return bookingId.get();
    }

    public SimpleIntegerProperty bookingIdProperty() {
        return bookingId;
    }

    public Date getBookingDate() {
        return bookingDate.get();
    }

    public SimpleObjectProperty<Date> bookingDateProperty() {
        return bookingDate;
    }

    public String getBookingNo() {
        return bookingNo.get();
    }

    public SimpleStringProperty bookingNoProperty() {
        return bookingNo;
    }

    public double getTravelerCount() {
        return travelerCount.get();
    }

    public SimpleDoubleProperty travelerCountProperty() {
        return travelerCount;
    }

    public int getCustomerId() {
        return customerId.get();
    }

    public SimpleIntegerProperty customerIdProperty() {
        return customerId;
    }

    public String getTripTypeId() {
        return tripTypeId.get();
    }

    public SimpleStringProperty tripTypeIdProperty() {
        return tripTypeId;
    }

    public int getPackageId() {
        return packageId.get();
    }

    public SimpleIntegerProperty packageIdProperty() {
        return packageId;
    }

}
