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

    public void generatePDF() {}

    public Object getSelected() {
        return agencyTable.getSelectionModel().getSelectedItem();
    }
    public int getSelectedInfoId() {
        return -1; //Has no info field
    }
    public void selectInfo(int selected) {
        agencyTable.getSelectionModel().select(selected -1); //selects the item index which is 0-based. TODO: select column with matching PK instead
    }
}
