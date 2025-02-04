package org.example.travelexpertsfx.contexts;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.travelexpertsfx.DatabaseHelper;
import org.example.travelexpertsfx.PDFGenerator;
import org.example.travelexpertsfx.TravelExpertsApplication;
import org.example.travelexpertsfx.controllers.FeeDialogController;
import org.example.travelexpertsfx.data.FeeDB;
import org.example.travelexpertsfx.models.Agency;
import org.example.travelexpertsfx.models.Fee;
import org.example.travelexpertsfx.Mode;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class FeesContext implements ITableContext {
    private TableView<Fee> feeTable;
    private ObservableList<Fee> data = FXCollections.observableArrayList();

    private final String userHome = System.getProperty("user.home");
    private final String pdfPath = userHome + "\\Documents\\fees.pdf"; // Saves to the Documents folder

    public FeesContext(TableView<Fee> feeTable) {
        this.feeTable = feeTable;
    }

    @Override
    public void displayTableContent() {
        data.clear();
        try {
            data = FeeDB.getFees();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        feeTable.setItems(data);
    }

    @Override
    public void openDialog(Object obj, Mode mode) {
        FXMLLoader fxmlLoader = new FXMLLoader(TravelExpertsApplication.class.getResource("fee-dialog-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FeeDialogController dialogController = fxmlLoader.getController();
        dialogController.setMode(mode);

        if(mode.equals(Mode.EDIT)) { // fill the text fields in dialog
            dialogController.displayFee((Fee) obj);
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait(); // waits until user is done with second scene
        displayTableContent();
    }

    public void setupTableColumns() {
        DatabaseHelper.setupTableColumns(feeTable, "fees", Fee.class);
    }

    // PDF Generation method
    public void generatePDF() {
        try {
            PDFGenerator.generateInvoice(pdfPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getSelected() {
        return feeTable.getSelectionModel().getSelectedItem();
    }
    public int getSelectedInfoId() {
        return -1; //Has no info field
    }
    public void selectInfo(int selected) {
        feeTable.getSelectionModel().select(selected);
    }
    @Override
    public void audit(){

    }
}
