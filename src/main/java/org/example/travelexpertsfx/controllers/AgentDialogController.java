package org.example.travelexpertsfx.controllers;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.travelexpertsfx.data.AgentDB;
import org.example.travelexpertsfx.models.Agent;
import org.example.travelexpertsfx.Mode;

import static org.example.travelexpertsfx.Validator.validateNonEmptyEntry;

public class AgentDialogController extends BaseDialogController<Agent, Integer> {

    @FXML // fx:id="btnCancel"
    private Button btnCancel; // Value injected by FXMLLoader

    @FXML // fx:id="btnDelete"
    private Button btnDelete; // Value injected by FXMLLoader

    @FXML // fx:id="btnSave"
    private Button btnSave; // Value injected by FXMLLoader

    @FXML // fx:id="lblMode"
    private Label lblMode; // Value injected by FXMLLoader

    @FXML // fx:id="tfAgentId"
    private TextField tfAgentId; // Value injected by FXMLLoader

    @FXML // fx:id="tfAgtBusPhone"
    private TextField tfAgtBusPhone; // Value injected by FXMLLoader

    @FXML // fx:id="tfAgtEmail"
    private TextField tfAgtEmail; // Value injected by FXMLLoader

    @FXML // fx:id="tfAgtFirstName"
    private TextField tfAgtFirstName; // Value injected by FXMLLoader

    @FXML // fx:id="tfAgtLastName"
    private TextField tfAgtLastName; // Value injected by FXMLLoader

    @FXML // fx:id="tfAgtMiddleInitial"
    private TextField tfAgtMiddleInitial; // Value injected by FXMLLoader

    @FXML // fx:id="tfAgtPosition"
    private TextField tfAgtPosition; // Value injected by FXMLLoader

    @FXML // fx:id="cbAgencyId"
    private ComboBox<Integer> cbAgencyId; // Value injected by FXMLLoader

    private Mode mode;

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'dialog-view.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'dialog-view.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'dialog-view.fxml'.";
        assert lblMode != null : "fx:id=\"lblMode\" was not injected: check your FXML file 'dialog-view.fxml'.";
        assert tfAgentId != null : "fx:id=\"tfAgentId\" was not injected: check your FXML file 'dialog-view.fxml'.";
        assert tfAgtBusPhone != null : "fx:id=\"tfAgtBusPhone\" was not injected: check your FXML file 'dialog-view.fxml'.";
        assert tfAgtEmail != null : "fx:id=\"tfAgtEmail\" was not injected: check your FXML file 'dialog-view.fxml'.";
        assert tfAgtFirstName != null : "fx:id=\"tfAgtFirstName\" was not injected: check your FXML file 'dialog-view.fxml'.";
        assert tfAgtLastName != null : "fx:id=\"tfAgtLastName\" was not injected: check your FXML file 'dialog-view.fxml'.";
        assert tfAgtMiddleInitial != null : "fx:id=\"tfAgtMiddleInitial\" was not injected: check your FXML file 'dialog-view.fxml'.";
        assert tfAgtPosition != null : "fx:id=\"tfAgtPosition\" was not injected: check your FXML file 'dialog-view.fxml'.";
        assert cbAgencyId != null : "fx:id=\"cbAgencyId\" was not injected: check your FXML file 'dialog-view.fxml'.";

        loadComboBox();
        btnSave.setOnMouseClicked(_ -> buttonSaveClicked());

        btnCancel.setOnMouseClicked(_ -> closeStage(btnCancel));

        btnDelete.setOnMouseClicked(_ -> {
            buttonDeleteClicked();
            closeStage(btnDelete);
        });

    } // ends initialize

    private void loadComboBox() {
        ArrayList<Integer> agencyIds;
        try {
            agencyIds = AgentDB.getAgencyIds();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Integer> agencyIdList = FXCollections.observableList(agencyIds);
        cbAgencyId.setItems(agencyIdList);
    }

    private void buttonSaveClicked() {
        try {
            SaveEntity(
                    collectAgent(),
                    AgentDB::insertAgent,
                    AgentDB::updateAgent,
                    Agent::getAgentId,
                    mode,
                    btnSave);
        } catch (SQLException e){
            displayAlert(Alert.AlertType.ERROR, mode, "Database error: " + e.getMessage());
        }
    }

    private void buttonDeleteClicked() {
        int agentId = Integer.parseInt(tfAgentId.getText());
        DeleteEntity(agentId, AgentDB::deleteAgent);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        lblMode.setText(mode + " Agent");
        // adjust visibility of delete button
        btnDelete.setVisible(!mode.equals(Mode.ADD));
        if(mode.equals(Mode.ADD)) {
            cbAgencyId.getSelectionModel().select(0);
        }
        if(mode.equals(Mode.EDIT)){
            tfAgentId.setEditable(false);
            tfAgentId.setDisable(true);
        }
    }

    private Agent collectAgent() {
        int agentId = 0;
        if (!tfAgentId.getText().isEmpty()) {
            agentId = Integer.parseInt(tfAgentId.getText());
        }
        int agencyId = cbAgencyId.getSelectionModel().getSelectedItem();
        if (validateAgentInputs()) {
            return new Agent(
                    agentId,
                    tfAgtFirstName.getText(),
                    tfAgtMiddleInitial.getText(),
                    tfAgtLastName.getText(),
                    tfAgtBusPhone.getText(),
                    tfAgtEmail.getText(),
                    tfAgtPosition.getText(),
                    agencyId
            );
        }
        return null;
    }

    public void displayAgent(Agent agent) {
        tfAgentId.setText(agent.getAgentId()+"");
        tfAgtFirstName.setText(agent.getAgtFirstName());
        tfAgtMiddleInitial.setText(agent.getAgtMiddleInitial());
        tfAgtLastName.setText(agent.getAgtLastName());
        tfAgtBusPhone.setText(agent.getAgtBusPhone());
        tfAgtEmail.setText(agent.getAgtEmail());
        tfAgtPosition.setText(agent.getAgtPosition());
        cbAgencyId.getSelectionModel().select((Integer) agent.getAgencyId());

    } // public because it's called from dialog controller

    private boolean validateAgentInputs() {
        StringBuilder errorMsg = new StringBuilder();

        if(!validateNonEmptyEntry(tfAgtFirstName)){
            errorMsg.append("First name cannot be empty.\n");
        }

        // middle initial is allowed to be null

        if(!validateNonEmptyEntry(tfAgtLastName)){
            errorMsg.append("Last name cannot be empty.\n");
        }

        if(!validateNonEmptyEntry(tfAgtBusPhone)){
            errorMsg.append("Business phone cannot be empty.\n");
        }

        if(!validateNonEmptyEntry(tfAgtEmail)){
            errorMsg.append("Email cannot be empty.\n");
        }

        if(!validateNonEmptyEntry(tfAgtPosition)){
            errorMsg.append("Position cannot be empty.\n");
        }

        // cbAgencyId not validated against.

        if (!errorMsg.isEmpty()) {
            displayAlert(Alert.AlertType.ERROR, mode, String.valueOf(errorMsg));
            return false;
        } else {
            return true;
        }
    }
}
