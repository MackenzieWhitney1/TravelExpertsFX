package org.example.travelexpertsfx.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.example.travelexpertsfx.DatabaseHelper;
import org.example.travelexpertsfx.data.BookingDB;
import org.example.travelexpertsfx.models.Booking;
import org.example.travelexpertsfx.models.BookingAudit;

import java.sql.SQLException;

public class BookingAuditController {

    @FXML
    TableView<BookingAudit> tbBookingAudit;

    @FXML
    Button btnClose;

    @FXML
    void initialize() {
        btnClose.setOnMouseClicked(event ->{
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
        DatabaseHelper.setupTableColumns(tbBookingAudit,"bookings_audits", BookingAudit.class);
        ObservableList<BookingAudit> data = FXCollections.observableArrayList();
        try{
            data = BookingDB.getBookingAudits();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        tbBookingAudit.setItems(data);
    }
}
