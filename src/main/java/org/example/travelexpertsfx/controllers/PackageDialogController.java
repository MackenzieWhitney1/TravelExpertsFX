package org.example.travelexpertsfx.controllers;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.example.travelexpertsfx.Mode;
import org.example.travelexpertsfx.data.PackageDB;
import org.example.travelexpertsfx.models.MyPackage;


public class PackageDialogController extends BaseDialogController<MyPackage, Integer> {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private DatePicker dpPkgEndDate;

    @FXML
    private DatePicker dpPkgStartDate;

    @FXML
    private Label lblMode;

    @FXML
    private TextField tfPackageId;

    @FXML
    private TextField tfPkgAgencyCommission;

    @FXML
    private TextField tfPkgBasePrice;

    @FXML
    private TextField tfPkgDesc;

    @FXML
    private TextField tfPkgName;

    private Mode mode;

    @FXML
    void initialize() {
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'package-dialog-view.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'package-dialog-view.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'package-dialog-view.fxml'.";
        assert dpPkgEndDate != null : "fx:id=\"dpPkgEndDate\" was not injected: check your FXML file 'package-dialog-view.fxml'.";
        assert dpPkgStartDate != null : "fx:id=\"dpPkgStartDate\" was not injected: check your FXML file 'package-dialog-view.fxml'.";
        assert lblMode != null : "fx:id=\"lblMode\" was not injected: check your FXML file 'package-dialog-view.fxml'.";
        assert tfPackageId != null : "fx:id=\"tfPackageId\" was not injected: check your FXML file 'package-dialog-view.fxml'.";
        assert tfPkgAgencyCommission != null : "fx:id=\"tfPkgAgencyCommission\" was not injected: check your FXML file 'package-dialog-view.fxml'.";
        assert tfPkgBasePrice != null : "fx:id=\"tfPkgBasePrice\" was not injected: check your FXML file 'package-dialog-view.fxml'.";
        assert tfPkgDesc != null : "fx:id=\"tfPkgDesc\" was not injected: check your FXML file 'package-dialog-view.fxml'.";
        assert tfPkgName != null : "fx:id=\"tfPkgName\" was not injected: check your FXML file 'package-dialog-view.fxml'.";

        btnSave.setOnMouseClicked(_ -> buttonSaveClicked());

        btnCancel.setOnMouseClicked(_ -> closeStage(btnCancel));

        btnDelete.setOnMouseClicked(_ -> {
            buttonDeleteClicked();
            closeStage(btnDelete);
        });
    }

    private void buttonSaveClicked() {
        try {
            SaveEntity(
                    collectPackage(),
                    PackageDB::insertPackage,
                    PackageDB::updatePackage,
                    MyPackage::getPackageId,
                    mode,
                    btnSave);
        } catch (SQLException e){
            displayAlert(Alert.AlertType.ERROR, mode, "Database error: " + e.getMessage());
        }
    }

    private void buttonDeleteClicked() {
        int packageId = Integer.parseInt(tfPackageId.getText());
        DeleteEntity(packageId, PackageDB::deletePackage);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        lblMode.setText(mode + " Package");
        // adjust visibility of delete button
        btnDelete.setVisible(!mode.equals(Mode.ADD));
    }

    private MyPackage collectPackage() {
        int packageId = 0;
        if(!tfPackageId.getText().isEmpty()){
            packageId = Integer.parseInt(tfPackageId.getText());
        }

        return new MyPackage(
                packageId,
                tfPkgName.getText(),
                Date.from(dpPkgStartDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(dpPkgEndDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                tfPkgDesc.getText(),
                Double.parseDouble(tfPkgBasePrice.getText()),
                Double.parseDouble(tfPkgAgencyCommission.getText())
        );
    }

    public void displayPackage(MyPackage myPackage) {
        tfPackageId.setText(myPackage.getPackageId()+"");
        tfPkgName.setText(myPackage.getPkgName());

        /* dates need to be cast to java.sql.Date even if the
        * method signature says it returns java.util.date.
        * There is an issue with UnsupportedOperationException otherwise
        */
        java.sql.Date sqlStartDate = (java.sql.Date) myPackage.getPkgStartDate();
        java.sql.Date sqlEndDate = (java.sql.Date) myPackage.getPkgEndDate();
        dpPkgStartDate.setValue(sqlStartDate.toLocalDate());
        dpPkgEndDate.setValue(sqlEndDate.toLocalDate());
        tfPkgDesc.setText(myPackage.getPkgDesc());
        tfPkgBasePrice.setText(myPackage.getPkgBasePrice()+"");
        tfPkgAgencyCommission.setText(myPackage.getPkgAgencyCommission()+"");
    }
}
