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
import org.example.travelexpertsfx.controllers.AgencyDialogController;
import org.example.travelexpertsfx.controllers.AgentDialogController;
import org.example.travelexpertsfx.data.AgencyDB;
import org.example.travelexpertsfx.data.AgentDB;
import org.example.travelexpertsfx.models.Agency;
import org.example.travelexpertsfx.models.Agent;

import java.io.IOException;
import java.sql.SQLException;

public class AgenciesContext implements ITableContext {
    private final TableView<Agency> agencyTable;
    private ObservableList<Agency> data = FXCollections.observableArrayList();

    public AgenciesContext(TableView<Agency> agencyTable) {
        this.agencyTable = agencyTable;
    }

    @Override
    public void displayTableContent() {
        data.clear();
        try {
            data = AgencyDB.getAgencies();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        agencyTable.setItems(data);
    }

    @Override
    public void openDialog(Object obj, Mode mode) {
        FXMLLoader fxmlLoader = new FXMLLoader(TravelExpertsApplication.class.getResource("agency-dialog-view.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AgencyDialogController dialogController = fxmlLoader.getController();
        dialogController.setMode(mode);
        if(mode.equals(Mode.EDIT)) { // fill the text fields in dialog
            dialogController.displayAgency((Agency) obj);
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait(); // waits until user is done with second scene
        displayTableContent();
    }

    public void setupTableColumns() {
        DatabaseHelper.setupTableColumns(agencyTable, "agencies", Agency.class);
    }

    public void generatePDF() {
        /*// Create a PDF writer
            PdfWriter writer = new PdfWriter(new File(pdfPath));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add a title
            document.add(new Paragraph("Fees Report"));

            // Create a table with columns
            Table table = new Table(4); // Adjust the number of columns based on your data

            // Add table headers
            table.addCell("Fee ID");
            table.addCell("Fee Name");
            table.addCell("Fee Amount");
            table.addCell("Fee Description");

            // Populate the table with data from feeTable
            ObservableList<Fee> fees = feeTable.getItems();
            for (Fee fee : fees) {
                table.addCell(fee.getFeeId());
                table.addCell(fee.getFeeName());
                table.addCell(String.valueOf(fee.getFeeAmt()));
                table.addCell(fee.getFeeDesc() != null ? fee.getFeeDesc() : "N/A");
            }

            // Add the table to the document
            document.add(table);
            document.close();

            System.out.println("PDF created successfully at: " + pdfPath);*/
    }

    public Object getSelected() {
        return agencyTable.getSelectionModel().getSelectedItem();
    }
    public int getSelectedInfoId() {
        return -1; //Has no info field
    }
    public void selectInfo(int selected) {
        agencyTable.getSelectionModel().select(selected -1); //selects the item index which is 0-based. TODO: select column with matching PK instead
    }
    @Override
    public void audit(){

    }
}
