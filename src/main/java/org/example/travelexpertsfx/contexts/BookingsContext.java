package org.example.travelexpertsfx.contexts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.travelexpertsfx.DatabaseHelper;
import org.example.travelexpertsfx.Mode;
import org.example.travelexpertsfx.TravelExpertsApplication;
import org.example.travelexpertsfx.controllers.BookingDialogController;
import org.example.travelexpertsfx.data.BookingDB;
import org.example.travelexpertsfx.models.Booking;

import java.io.IOException;
import java.sql.SQLException;


public class BookingsContext implements ITableContext {
    private TableView<Booking> bookingTable;
    private ObservableList<Booking> data = FXCollections.observableArrayList();

    private final String userHome = System.getProperty("user.home");
    private final String pdfPath = userHome + "\\Documents\\bookings.pdf";

    public BookingsContext(TableView<Booking> bookingTable){this.bookingTable=bookingTable;}

    @Override
    public void displayTableContent(){
        data.clear();
        try{
            data = BookingDB.getBookings();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        bookingTable.setItems(data);
    }

    @Override
    public void openDialog(Object obj, Mode mode){
        FXMLLoader fxmlLoader = new FXMLLoader(TravelExpertsApplication.class.getResource("booking-dialog-view.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load());
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        BookingDialogController dialogController = fxmlLoader.getController();
        dialogController.setMode(mode);
        if(mode.equals(Mode.EDIT)){
            dialogController.displayBooking((Booking) obj);
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        displayTableContent();
    }

    public void setupTableColumns(){
        DatabaseHelper.setupTableColumns(bookingTable,"bookings",Booking.class);
    }

    public void generatePDF(){

    }
}
