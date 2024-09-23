package org.example.travelexpertsfx.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.example.travelexpertsfx.data.AgentDB;
import org.example.travelexpertsfx.models.Agent;
import org.example.travelexpertsfx.models.Mode;

public class AgentDialogController extends BaseDialogController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

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
        btnSave.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                buttonSaveClicked();
                closeStage(mouseEvent);
            }
        });

        btnCancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                closeStage(mouseEvent);
            }
        });

        btnDelete.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                buttonDeleteClicked();
                closeStage(mouseEvent);
            }
        });

    } // ends initialize

    private void loadComboBox() {
        ArrayList<Integer> agencyIds = new ArrayList<>();
        try {
            agencyIds = AgentDB.getAgencyIds();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Integer> agencyIdList = FXCollections.observableList(agencyIds);
        cbAgencyId.setItems(agencyIdList);
    }

    private void buttonDeleteClicked() {
        int nrRows = 0;
        mode = Mode.DELETE;
        int agentId = Integer.parseInt(tfAgentId.getText());
        try {
            nrRows = AgentDB.deleteAgent(agentId);
        }
        catch(SQLIntegrityConstraintViolationException e){
            displayAlert(Alert.AlertType.ERROR, mode, "Cannot delete agent who has customers.");
            return;
        }
        catch (SQLException e){
            throw new RuntimeException();
        }
        if(nrRows == 0){
            displayAlert(Alert.AlertType.ERROR, mode, "");
        } else { // successful
            displayAlert(Alert.AlertType.CONFIRMATION, mode, "");
        }
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        lblMode.setText(mode + " Agent");
        // adjust visibility of delete button
        btnDelete.setVisible(!mode.equals(Mode.ADD));
        if(mode.equals(Mode.ADD)) {
            cbAgencyId.getSelectionModel().select(0);
        }
    }

    private void buttonSaveClicked() {
        int nrRows = 0;
        Agent agent = collectAgent();

        try {
            if (mode.equals(Mode.ADD)) {
                nrRows = AgentDB.insertAgent(agent);
            } else // edit
            {
                nrRows = AgentDB.updateAgent(agent.getAgentId(), agent);
            }
        } catch (SQLIntegrityConstraintViolationException e){
            displayAlert(Alert.AlertType.ERROR, mode,"This agency doesn't exist.");
            return;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(nrRows == 0){
            displayAlert(Alert.AlertType.ERROR, mode, "");
        } else { // successful
            displayAlert(Alert.AlertType.CONFIRMATION, mode, "");
        }
    }

    private Agent collectAgent() {
        int agentId = 0;
        if(!tfAgentId.getText().isEmpty()){
            agentId = Integer.parseInt(tfAgentId.getText());
        }
        int agencyId = cbAgencyId.getSelectionModel().getSelectedItem();
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

}
