package org.example.travelexpertsfx.contexts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.travelexpertsfx.TravelExpertsApplication;
import org.example.travelexpertsfx.controllers.FeeDialogController;
import org.example.travelexpertsfx.data.FeeDB;
import org.example.travelexpertsfx.models.Fee;
import org.example.travelexpertsfx.models.Mode;

import java.io.IOException;
import java.sql.SQLException;

public class FeesContext implements ITableContext {
    private TableView<Fee> feeTable;
    private ObservableList<Fee> data = FXCollections.observableArrayList();

    public FeesContext(TableView<Fee> feeTable) {
        this.feeTable = feeTable;
    }

    @Override
    public void displayTableContent() {
        data.clear();
        try {
            data = FeeDB.getFees();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        feeTable.setItems(data);
    }

    @Override
    public void openDialog(Object obj, Mode mode) {
        FXMLLoader fxmlLoader = new FXMLLoader(TravelExpertsApplication.class.getResource("fee-dialog-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FeeDialogController dialogController = fxmlLoader.getController();
        dialogController.setMode(mode);
        if(mode.equals(Mode.EDIT)) { // fill the text fields in dialog
            dialogController.displayFee((Fee) obj);
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait(); // waits until user is done with second scene
        displayTableContent();
    }
}
