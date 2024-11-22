package org.example.travelexpertsfx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.example.travelexpertsfx.Mode;
import org.example.travelexpertsfx.data.*;
import org.example.travelexpertsfx.models.Booking;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static org.example.travelexpertsfx.Validator.*;

public class BookingDialogController extends BaseDialogController<Booking,Integer> {
    @FXML
    private Label lblMode;

    @FXML
    private TextField tfBookingId;

    @FXML
    private ComboBox<Integer> cbCustomer;

    @FXML
    private ComboBox<Integer> cbPackage;

    @FXML
    private ComboBox<String> cbTripType;

    @FXML
    private DatePicker dpBookingDate;

    @FXML
    private TextField tfBookingNo;

    @FXML
    private TextField tfTravelerCount;

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
        assert cbCustomer != null : "fx:id=\"cbCustomer\" was not injected: check your FXML file 'booking-dialog-view.fxml'.";
        assert cbPackage != null : "fx:id=\"cbPackage\" was not injected: check your FXML file 'booking-dialog-view.fxml'.";
        assert cbTripType != null : "fx:id=\"cbTripType\" was not injected: check your FXML file 'booking-dialog-view.fxml'.";
        assert btnSave!= null :"\"fx:id=\"btnSave\" was not injected: check your FXML file 'booking-dialog-view.fxml'.";
        assert btnCancel != null :"\"fx:id=\"btnCancel\" was not injected: check your FXML file 'booking-dialog-view.fxml'.";
        assert btnDelete!= null :"\"fx:id=\"btnDelete\" was not injected: check your FXML file 'booking-dialog-view.fxml'.";

        btnSave.setOnMouseClicked(_ -> buttonSaveClicked());

        btnCancel.setOnMouseClicked(_ -> closeStage(btnCancel));

        btnDelete.setOnMouseClicked(_ -> {
            buttonDeleteClicked();
            closeStage(btnDelete);
        });
        loadCustomerComboBox();
        loadTripTypeComboBox();
        loadPackageComboBox();

    }

    private void loadCustomerComboBox() {
        Map<Integer, String> customers;
        try {
            customers = CustomerDB.getCustomerComboBoxMap();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Integer> customerIds = FXCollections.observableList(customers.keySet().stream().toList());
        cbCustomer.setItems(customerIds);
        // Custom StringConverter to display names instead of IDs
        cbCustomer.setConverter(new StringConverter<>() {
            @Override
            public String toString(Integer id) {
                return customers.get(id); // Display the customer's name
            }

            @Override
            public Integer fromString(String s) {
                return 0;
            }
        });
    }
    private void loadTripTypeComboBox() {
        Map<String, String> tripTypes;
        try {
            tripTypes = TripTypeDB.getTripTypeComboBoxMap();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<String> tripTypeIds = FXCollections.observableList(tripTypes.keySet().stream().toList());
        cbTripType.setItems(tripTypeIds);
        // Custom StringConverter to display names instead of IDs
        cbTripType.setConverter(new StringConverter<>() {
            @Override
            public String toString(String id) {
                return tripTypes.get(id); // Display the trip type's name
            }

            @Override
            public String fromString(String s) {
                return "";
            }
        });
    }

    private void loadPackageComboBox() {
        Map<Integer, String> packages;
        try {
            packages = PackageDB.getPackageComboBoxMap();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Integer> packageIds = FXCollections.observableList(packages.keySet().stream().toList());
        cbPackage.setItems(packageIds);
        // Custom StringConverter to display names instead of IDs
        cbPackage.setConverter(new StringConverter<>() {
            @Override
            public String toString(Integer id) {
                return packages.get(id); // Display the package's name
            }

            @Override
            public Integer fromString(String s) {
                return 0;
            }
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
        cbCustomer.setValue(booking.getCustomerId());
        cbTripType.setValue(booking.getTripTypeId());
        cbPackage.setValue(booking.getPackageId());
    }
    public void setMode(Mode mode){
        this.mode = mode;
        lblMode.setText(mode+" Booking");
        btnDelete.setVisible(!mode.equals(Mode.ADD));
        if(mode.equals(Mode.ADD)) {
            cbCustomer.getSelectionModel().select(0);

            // if package should be optional, change this and handle add/edits to null.
            cbPackage.getSelectionModel().select(0);
            cbTripType.getSelectionModel().select(0);
        }
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
        int customerId = cbCustomer.getSelectionModel().getSelectedItem();
        String tripTypeId = cbTripType.getSelectionModel().getSelectedItem();
        int packageId = cbPackage.getSelectionModel().getSelectedItem();

        if(validateBookingInputs()) {
            return new Booking(
                    Integer.parseInt(tfBookingId.getText()),
                    Date.from(dpBookingDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    tfBookingNo.getText(),
                    Double.parseDouble(tfTravelerCount.getText()),
                    customerId,
                    tripTypeId,
                    packageId
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

        if (errorMsg.isEmpty()) {
            return true;
        } else {
            displayAlert(Alert.AlertType.ERROR, mode, String.valueOf(errorMsg));
            return false;
        }
    }
}
