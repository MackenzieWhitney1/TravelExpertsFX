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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.travelexpertsfx.data.FeeDB;
import org.example.travelexpertsfx.models.Fee;

import static org.example.travelexpertsfx.Validator.*;

public class FeeDialogController {

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
    private String mode; // either Add or Edit

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

    private void buttonDeleteClicked() {
        int nrRows = 0;
        mode = "Delete";
        String feeId = tfFeeId.getText();
        try {
            nrRows = FeeDB.deleteFee(feeId);
        }
        catch(SQLIntegrityConstraintViolationException e){
            displayAlert(Alert.AlertType.ERROR, "Cannot delete fee used in booking details.");
            return;
        }
        catch (SQLException e){
            throw new RuntimeException();
        }
        if(nrRows == 0){
            displayAlert(Alert.AlertType.ERROR, "");
        } else { // successful
            displayAlert(Alert.AlertType.CONFIRMATION, "");
        }
    }

    public void setMode(String mode) {
        this.mode = mode;
        lblMode.setText(mode + " Fee");
        // adjust visibility of delete button
        btnDelete.setVisible(!mode.equals("Add"));
        // adjust if Fee ID is editable
        if(mode.equals("Edit")){
            tfFeeId.setEditable(false);
            tfFeeId.setDisable(true);
        }
    }

    private void buttonSaveClicked(MouseEvent mouseEvent) {
        int nrRows = 0;
        ArrayList<String> lstFeeId = null;
        try {
            lstFeeId = FeeDB.getExistingFeeIds();
        } catch (SQLException e) {
            displayAlert(Alert.AlertType.ERROR, "Cannot retrieve fee ids.");
            closeStage(mouseEvent);
        }
        boolean validatedInputs = validateFeeInputs(lstFeeId);
        if (validatedInputs) {
            Fee fee = collectFeeInfo();
            try {
                if (mode.equals("Add")) {
                    nrRows = FeeDB.insertFee(fee);
                } else // edit
                {
                    nrRows = FeeDB.updateFee(fee.getFeeId(), fee);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (nrRows == 0) {
                displayAlert(Alert.AlertType.ERROR, "");
            } else { // successful
                displayAlert(Alert.AlertType.CONFIRMATION, "");
                closeStage(mouseEvent);
            }
        }
    }

    private boolean validateFeeInputs(ArrayList<String> lstFeeId) {
        StringBuilder errorMsg = new StringBuilder();
        if(!validateNonEmptyEntry(tfFeeId)){
            errorMsg.append("Fee Id cannot be empty.\n");
        }
        if (!validateEntryNotInList(tfFeeId, lstFeeId) && mode.equals("Add")) {
            errorMsg.append("Fee Id cannot be one of the Fee Ids already used.\n");
        }
        if (!validateNonEmptyEntry(tfFeeName)) {
            errorMsg.append("Name cannot be empty.\n");
        }
        if (!validateNonEmptyPositiveDouble(tfFeeAmount)) {
            errorMsg.append("Fee Amount must be a positive double.");
        }
        if (!errorMsg.isEmpty()) {
            displayAlert(Alert.AlertType.ERROR, String.valueOf(errorMsg));
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

    // close the window
    private void closeStage(MouseEvent mouseEvent) {
        Node node = (Node) mouseEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void displayFee(Fee fee) {
        tfFeeId.setText(fee.getFeeId());
        tfFeeName.setText(fee.getFeeName());
        tfFeeAmount.setText(fee.getFeeAmt()+"");
        tfFeeDescription.setText(fee.getFeeDesc());
    } // public because it's called from dialog controller

    private void displayAlert(Alert.AlertType t, String msg){
        String content = "";
        Alert alert = new Alert(t);
        if(t.equals(Alert.AlertType.ERROR)) {
            alert.setHeaderText("Database Operation Error");
            content = mode + " failed";
        } else if (t.equals(Alert.AlertType.CONFIRMATION)){
            alert.setHeaderText("Database Operation Success");
            content = mode + " successful";
        }
        if(!msg.isEmpty()){
            content += "\n"+msg;
        }
        alert.setContentText(content);
        alert.showAndWait();
    }

}
