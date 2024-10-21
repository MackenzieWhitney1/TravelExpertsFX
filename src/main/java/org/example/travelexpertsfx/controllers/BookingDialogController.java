package org.example.travelexpertsfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.travelexpertsfx.Mode;
import org.example.travelexpertsfx.data.BookingDB;
import org.example.travelexpertsfx.models.Booking;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;

import static org.example.travelexpertsfx.Validator.*;

public class BookingDialogController extends BaseDialogController<Booking,Integer> {
    @FXML
    private Label lblMode;

    @FXML
    private TextField tfBookingId;

    @FXML
    private DatePicker dpBookingDate;

    @FXML
    private TextField tfBookingNo;

    @FXML
    private TextField tfTravelerCount;

    @FXML
    private TextField tfCustomerId;

    @FXML
    private TextField tfTripTypeId;

    @FXML
    private TextField tfPackageId;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnDelete;

    private Mode mode;
    @FXML
    void initialize() {
        assert tfBookingId != null :"\"fx:id=\"tfBookingId\" was not injected: check your FXML file 'booking-dialog-view.fxml'.";
        assert dpBookingDate != null :"\"fx:id=\"dpBookingDate\" was not injected: check your FXML file 'booking-dialog-view.fxml'.";
        assert tfBookingNo != null :"\"fx:id=\"tfBookingNo\" was not injected: check your FXML file 'booking-dialog-view.fxml'.";
        assert tfTravelerCount != null :"\"fx:id=\"tfTravelerCount\" was not injected: check your FXML file 'booking-dialog-view.fxml'.";
        assert tfCustomerId != null :"\"fx:id=\"tfCustomerId\" was not injected: check your FXML file 'booking-dialog-view.fxml'.";
        assert tfTripTypeId != null :"\"fx:id=\"tfTripTypeId\" was not injected: check your FXML file 'booking-dialog-view.fxml'.";
        assert tfPackageId != null :"\"fx:id=\"tfPackageId\" was not injected: check your FXML file 'booking-dialog-view.fxml'.";
        assert btnSave!= null :"\"fx:id=\"btnSave\" was not injected: check your FXML file 'booking-dialog-view.fxml'.";
        assert btnCancel != null :"\"fx:id=\"btnCancel\" was not injected: check your FXML file 'booking-dialog-view.fxml'.";
        assert btnDelete!= null :"\"fx:id=\"btnDelete\" was not injected: check your FXML file 'booking-dialog-view.fxml'.";

        btnSave.setOnMouseClicked(_ -> buttonSaveClicked());

        btnCancel.setOnMouseClicked(_ -> closeStage(btnCancel));

        btnDelete.setOnMouseClicked(_ -> {
            buttonDeleteClicked();
            closeStage(btnDelete);
        });

    }

    private void buttonDeleteClicked(){
        int bookingId = Integer.parseInt(tfBookingId.getText());
        DeleteEntity(bookingId,BookingDB::deleteBooking);
    }

    public void  displayBooking(Booking booking){
        tfBookingId.setText(booking.getBookingId()+"");
        java.sql.Date sqlBookingDate = (java.sql.Date)booking.getBookingDate();
        dpBookingDate.setValue(sqlBookingDate.toLocalDate());
        tfBookingNo.setText(booking.getBookingNo());
        tfTravelerCount.setText(booking.getTravelerCount()+"");
        tfCustomerId.setText(booking.getCustomerId()+"");
        tfTripTypeId.setText(booking.getTripTypeId());
        tfPackageId.setText(booking.getPackageId()+"");
    }
    public void setMode(Mode mode){
        this.mode = mode;
        lblMode.setText(mode+" Booking");
        btnDelete.setVisible(!mode.equals(Mode.ADD));
    }

    private void buttonSaveClicked(){
        try {
            SaveEntity(
                    collectBooking(),
                    BookingDB::insertBooking,
                    BookingDB::updateBooking,
                    Booking::getBookingId,
                    mode,
                    btnSave);
        }catch (SQLException e){
            displayAlert(Alert.AlertType.ERROR, mode, "Database error: " + e.getMessage());
        }
    }

    private Booking collectBooking(){
        if(validateBookingInputs()) {
            return new Booking(
                    Integer.parseInt(tfBookingId.getText()),
                    Date.from(dpBookingDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    tfBookingNo.getText(),
                    Double.parseDouble(tfTravelerCount.getText()),
                    Integer.parseInt(tfCustomerId.getText()),
                    tfTripTypeId.getText(),
                    Integer.parseInt(tfPackageId.getText())
                    );
        }
        return null;
    }

    private boolean validateBookingInputs(){
        StringBuilder errorMsg = new StringBuilder();

        if(!validateNonEmptyPositiveInteger(tfBookingId)){
            errorMsg.append("Booking ID must be a valid positive number.\n");
        }
        if(!validateDateSelected(dpBookingDate)){
            errorMsg.append("Booking Date must be selected.\n");
        }
        if(!validateNonEmptyEntry(tfBookingNo)){
            errorMsg.append("Booking Number cannot be empty.\n");
        }
        if(!validateNonEmptyPositiveDouble(tfTravelerCount)){
            errorMsg.append("Traveler Count must be a valid positive number.\n");
        }
        if(!validateNonEmptyPositiveInteger(tfCustomerId)){
            errorMsg.append("Customer ID must be a valid positive integer.\n");
        }
        if(!validateNonEmptyWithinLength(tfTripTypeId,1)){
            errorMsg.append("Trip Type ID must be exactly 1 character.\n");
        }
        if(!validateNonEmptyPositiveInteger(tfPackageId)){
            errorMsg.append("Package ID must be a valid positive integer.\n");
        }
        if (errorMsg.isEmpty()) {
            return true;
        } else {
            displayAlert(Alert.AlertType.ERROR, mode, String.valueOf(errorMsg));
            return false;
        }
    }
}
