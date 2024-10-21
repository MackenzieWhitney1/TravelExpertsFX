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
import org.example.travelexpertsfx.controllers.PackageDialogController;
import org.example.travelexpertsfx.data.AgencyDB;
import org.example.travelexpertsfx.data.PackageDB;
import org.example.travelexpertsfx.models.Agency;
import org.example.travelexpertsfx.models.MyPackage;

import java.io.IOException;
import java.sql.SQLException;

public class PackagesContext implements ITableContext {
    private final TableView<MyPackage> packageTable;
    private ObservableList<MyPackage> data = FXCollections.observableArrayList();

    public PackagesContext(TableView<MyPackage> packageTable) {
        this.packageTable = packageTable;
    }

    @Override
    public void displayTableContent() {
        data.clear();
        try {
            data = PackageDB.getPackages();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        packageTable.setItems(data);
    }

    @Override
    public void openDialog(Object obj, Mode mode) {
        FXMLLoader fxmlLoader = new FXMLLoader(TravelExpertsApplication.class.getResource("package-dialog-view.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PackageDialogController dialogController = fxmlLoader.getController();
        dialogController.setMode(mode);
        if(mode.equals(Mode.EDIT)) { // fill the text fields in dialog
            dialogController.displayPackage((MyPackage) obj);
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait(); // waits until user is done with second scene
        displayTableContent();
    }

    @Override
    public void setupTableColumns() {
        DatabaseHelper.setupTableColumns(packageTable, "packages", MyPackage.class);
    }

    @Override
    public void generatePDF() {

    }

    public Object getSelected() {
        return packageTable.getSelectionModel().getSelectedItem();
    }
    public int getSelectedInfoId() {
        return -1; //Has no info field
    }
    public void selectInfo(int selected) {
        packageTable.getSelectionModel().select(selected);
    }
    @Override
    public void audit(){

    }
}
