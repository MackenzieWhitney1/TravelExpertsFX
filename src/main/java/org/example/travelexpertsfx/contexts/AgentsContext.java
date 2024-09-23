package org.example.travelexpertsfx.contexts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.travelexpertsfx.TravelExpertsApplication;
import org.example.travelexpertsfx.controllers.AgentDialogController;
import org.example.travelexpertsfx.data.AgentDB;
import org.example.travelexpertsfx.models.Agent;

import java.io.IOException;
import java.sql.SQLException;

public class AgentsContext implements ITableContext {
    private TableView<Agent> agentTable;
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
    public void openDialog(Object obj, String mode) {
        FXMLLoader fxmlLoader = new FXMLLoader(TravelExpertsApplication.class.getResource("agent-dialog-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AgentDialogController dialogController = fxmlLoader.getController();
        dialogController.setMode(mode);
        if(mode.equals("Edit")) { // fill the text fields in dialog
            dialogController.displayAgent((Agent) obj);
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait(); // waits until user is done with second scene
        displayTableContent();
    }
}
