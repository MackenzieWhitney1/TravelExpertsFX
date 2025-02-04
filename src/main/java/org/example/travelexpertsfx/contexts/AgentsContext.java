package org.example.travelexpertsfx.contexts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.travelexpertsfx.DatabaseHelper;
import org.example.travelexpertsfx.TravelExpertsApplication;
import org.example.travelexpertsfx.controllers.AgentDialogController;
import org.example.travelexpertsfx.data.AgentDB;
import org.example.travelexpertsfx.models.Agency;
import org.example.travelexpertsfx.models.Agent;
import org.example.travelexpertsfx.Mode;

import java.io.IOException;
import java.sql.*;

public class AgentsContext implements ITableContext {
    private final TableView<Agent> agentTable;
    private ObservableList<Agent> data = FXCollections.observableArrayList();

    public AgentsContext(TableView<Agent> agentTable) {
        this.agentTable = agentTable;
    }

    @Override
    public void displayTableContent() {
        data.clear();
        try {
            data = AgentDB.getAgents();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        agentTable.setItems(data);
    }

    @Override
    public void openDialog(Object obj, Mode mode) {
        FXMLLoader fxmlLoader = new FXMLLoader(TravelExpertsApplication.class.getResource("agent-dialog-view.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AgentDialogController dialogController = fxmlLoader.getController();
        dialogController.setMode(mode);
        if(mode.equals(Mode.EDIT)) { // fill the text fields in dialog
            dialogController.displayAgent((Agent) obj);
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait(); // waits until user is done with second scene
        displayTableContent();
    }

    public void setupTableColumns() {
        DatabaseHelper.setupTableColumns(agentTable, "agents", Agent.class);
    }

    public void generatePDF() {}

    public Object getSelected() {
        return agentTable.getSelectionModel().getSelectedItem();
    }
    public int getSelectedInfoId() {
        int index = agentTable.getSelectionModel().getSelectedIndex(); //Gets index of selected row
        return data.get(index).getAgencyId(); //Gets the info field of the row, in this case Agency Id
    }
    public void selectInfo(int selected) {
        agentTable.getSelectionModel().select(selected);
    }
    @Override
    public void audit(){

    }
}
