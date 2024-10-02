package org.example.travelexpertsfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.travelexpertsfx.Mode;
import org.example.travelexpertsfx.data.AgencyDB;
import org.example.travelexpertsfx.data.AgentDB;
import org.example.travelexpertsfx.models.Agency;

import java.sql.SQLException;

public class AgencyDialogController extends BaseDialogController<Agency, Integer> {


    @FXML
    private Button btnCancel;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Label lblMode;

    @FXML
    private TextField tfAgencyId;

    @FXML
    private TextField tfAgncyAddress;

    @FXML
    private TextField tfAgncyCity;

    @FXML
    private TextField tfAgncyCountry;

    @FXML
    private TextField tfAgncyFax;

    @FXML
    private TextField tfAgncyPhone;

    @FXML
    private TextField tfAgncyPostal;

    @FXML
    private TextField tfAgncyProv;
    private Mode mode;

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'agency-dialog-view.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'agency-dialog-view.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'agency-dialog-view.fxml'.";
        assert lblMode != null : "fx:id=\"lblMode\" was not injected: check your FXML file 'agency-dialog-view.fxml'.";
        assert tfAgencyId != null : "fx:id=\"tfAgencyId\" was not injected: check your FXML file 'agency-dialog-view.fxml'.";
        assert tfAgncyAddress != null : "fx:id=\"tfAgncyAddress\" was not injected: check your FXML file 'agency-dialog-view.fxml'.";
        assert tfAgncyCity != null : "fx:id=\"tfAgncyCity\" was not injected: check your FXML file 'agency-dialog-view.fxml'.";
        assert tfAgncyCountry != null : "fx:id=\"tfAgncyCountry\" was not injected: check your FXML file 'agency-dialog-view.fxml'.";
        assert tfAgncyFax != null : "fx:id=\"tfAgncyFax\" was not injected: check your FXML file 'agency-dialog-view.fxml'.";
        assert tfAgncyPhone != null : "fx:id=\"tfAgncyPhone\" was not injected: check your FXML file 'agency-dialog-view.fxml'.";
        assert tfAgncyPostal != null : "fx:id=\"tfAgncyPostal\" was not injected: check your FXML file 'agency-dialog-view.fxml'.";
        assert tfAgncyProv != null : "fx:id=\"tfAgncyProv\" was not injected: check your FXML file 'agency-dialog-view.fxml'.";

        btnSave.setOnMouseClicked(_ -> buttonSaveClicked());

        btnCancel.setOnMouseClicked(_ -> closeStage(btnCancel));

        btnDelete.setOnMouseClicked(_ -> {
            buttonDeleteClicked();
            closeStage(btnDelete);
        });

    } // ends initialize

    private void buttonSaveClicked() {
        try {
            SaveEntity(
                    collectAgency(),
                    AgencyDB::insertAgency,
                    AgencyDB::updateAgency,
                    Agency::getAgencyId,
                    mode,
                    btnSave);
        } catch (SQLException e){
            displayAlert(Alert.AlertType.ERROR, mode, "Database error: " + e.getMessage());
        }
    }

    private void buttonDeleteClicked() {
        int agentId = Integer.parseInt(tfAgencyId.getText());
        DeleteEntity(agentId, AgentDB::deleteAgent);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        lblMode.setText(mode + " Agency");
        // adjust visibility of delete button
        btnDelete.setVisible(!mode.equals(Mode.ADD));
    }

    private Agency collectAgency() {
        int agencyId = 0;
        if(!tfAgencyId.getText().isEmpty()){
            agencyId = Integer.parseInt(tfAgencyId.getText());
        }
        return new Agency(
                agencyId,
                tfAgncyAddress.getText(),
                tfAgncyCity.getText(),
                tfAgncyProv.getText(),
                tfAgncyPostal.getText(),
                tfAgncyCountry.getText(),
                tfAgncyPhone.getText(),
                tfAgncyFax.getText()
        );
    }

    public void displayAgency(Agency agency) {
        tfAgencyId.setText(agency.getAgencyId()+"");
        tfAgncyAddress.setText(agency.getAgncyAddress());
        tfAgncyCity.setText(agency.getAgncyCity());
        tfAgncyProv.setText(agency.getAgncyProv());
        tfAgncyPostal.setText(agency.getAgncyPostal());
        tfAgncyCountry.setText(agency.getAgncyCountry());
        tfAgncyPhone.setText(agency.getAgncyPhone());
        tfAgncyFax.setText(agency.getAgncyFax());

    } // public because it's called from dialog controller

}
