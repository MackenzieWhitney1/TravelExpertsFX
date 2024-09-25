/**
 * Sample Skeleton for 'dialog-view.fxml' Controller Class
 */

package org.example.travelexpertsfx.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.example.travelexpertsfx.data.FeeDB;
import org.example.travelexpertsfx.models.Fee;
import org.example.travelexpertsfx.Mode;

import static org.example.travelexpertsfx.Validator.*;

public class FeeDialogController extends BaseDialogController<Fee, String> {

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

    @FXML // fx:id="tfFeeAmount"
    private TextField tfFeeAmount; // Value injected by FXMLLoader

    @FXML // fx:id="tfFeeDescription"
    private TextField tfFeeDescription; // Value injected by FXMLLoader

    @FXML // fx:id="tfFeeId"
    private TextField tfFeeId; // Value injected by FXMLLoader

    @FXML // fx:id="tfFeeName"
    private TextField tfFeeName; // Value injected by FXMLLoader
    private Mode mode; // either Add or Edit

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'dialog-view.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'dialog-view.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'dialog-view.fxml'.";
        assert lblMode != null : "fx:id=\"lblMode\" was not injected: check your FXML file 'dialog-view.fxml'.";
        assert tfFeeAmount != null : "fx:id=\"tfFeeAmount\" was not injected: check your FXML file 'dialog-view.fxml'.";
        assert tfFeeDescription != null : "fx:id=\"tfFeeDescription\" was not injected: check your FXML file 'dialog-view.fxml'.";
        assert tfFeeId != null : "fx:id=\"tfFeeId\" was not injected: check your FXML file 'dialog-view.fxml'.";
        assert tfFeeName != null : "fx:id=\"tfFeeName\" was not injected: check your FXML file 'dialog-view.fxml'.";

        btnSave.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                buttonSaveClicked(mouseEvent);
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

    }

    private void buttonSaveClicked(MouseEvent mouseEvent) {
        try {
            SaveEntity(
                    collectFeeInfo(),
                    FeeDB::insertFee,
                    FeeDB::updateFee,
                    Fee::getFeeId,
                    validateFeeInputs(),
                    mode);
        } catch (SQLException e){
            displayAlert(Alert.AlertType.ERROR, mode, "Database error: " + e.getMessage());
        }
    }

    private void buttonDeleteClicked() {
        String feeId = tfFeeId.getText();
        DeleteEntity(feeId, FeeDB::deleteFee);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        lblMode.setText(mode + " Fee");
        // adjust visibility of delete button
        btnDelete.setVisible(!mode.equals(Mode.ADD));
        // adjust if Fee ID is editable
        if(mode.equals(Mode.EDIT)){
            tfFeeId.setEditable(false);
            tfFeeId.setDisable(true);
        }
    }

    private boolean validateFeeInputs() throws SQLException {
        ArrayList<String> lstFeeId = null;
        try {
            lstFeeId = FeeDB.getExistingFeeIds();
        } catch (SQLException e) {
            displayAlert(Alert.AlertType.ERROR, mode, "Cannot retrieve fee ids.");
        }
        StringBuilder errorMsg = new StringBuilder();
        if(!validateNonEmptyEntry(tfFeeId)){
            errorMsg.append("Fee Id cannot be empty.\n");
        }
        assert lstFeeId != null;
        if (!validateEntryNotInList(tfFeeId, lstFeeId) && mode.equals(Mode.ADD)) {
            errorMsg.append("Fee Id cannot be one of the Fee Ids already used.\n");
        }
        if (!validateNonEmptyEntry(tfFeeName)) {
            errorMsg.append("Name cannot be empty.\n");
        }
        if (!validateNonEmptyPositiveDouble(tfFeeAmount)) {
            errorMsg.append("Fee Amount must be a positive double.");
        }
        if (!errorMsg.isEmpty()) {
            displayAlert(Alert.AlertType.ERROR, mode, String.valueOf(errorMsg));
            return false;
        } else {
            return true;
        }
    }

    private Fee collectFeeInfo() {
        String feeId = "";
        if(!tfFeeId.getText().isEmpty()){
            feeId = tfFeeId.getText();
        }
        return new Fee(feeId,
                tfFeeName.getText(),
                Double.parseDouble(tfFeeAmount.getText()),
                tfFeeDescription.getText()
        );
    }

    public void displayFee(Fee fee) {
        tfFeeId.setText(fee.getFeeId());
        tfFeeName.setText(fee.getFeeName());
        tfFeeAmount.setText(fee.getFeeAmt()+"");
        tfFeeDescription.setText(fee.getFeeDesc());
    } // public because it's called from dialog controller
}
