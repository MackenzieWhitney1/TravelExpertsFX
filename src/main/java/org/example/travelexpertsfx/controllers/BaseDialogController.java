package org.example.travelexpertsfx.controllers;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.travelexpertsfx.Mode;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Objects;

public abstract class BaseDialogController<T, IDType> {

    protected void SaveEntity(
            T entity,
            SQLFunction<T, Integer> insertFunc,
            SQLBiFunction<IDType, T, Integer> updateFunc,
            SQLFunction<T, IDType> getIdFunc,
            Mode mode) throws SQLException
    {
        Integer nrRows;
        if(!Objects.isNull(entity)) { // check entity isn't null.
            try {
                if (mode == Mode.ADD) {
                    nrRows = insertFunc.apply(entity);  // Call the insert function
                } else {
                    IDType entityId = getIdFunc.apply(entity);  // Get the entity's ID (could be Integer or String)
                    nrRows = updateFunc.apply(entityId, entity);  // Call the update function
                }
            } catch (SQLException e) {
                displayAlert(Alert.AlertType.ERROR, mode, "Database error: " + e.getMessage());
                return;
            }

            if (nrRows == 0) {
                displayAlert(Alert.AlertType.ERROR, mode, "Error occurred while saving.");
            } else {
                displayAlert(Alert.AlertType.CONFIRMATION, mode, "Save successful.");
            }
        }
    }

    protected void DeleteEntity(
            IDType idValue,
            SQLFunction<IDType, Integer> deleteFunc
    )
    {
        int nrRows;
        Mode mode = Mode.DELETE;
        try {
            nrRows = deleteFunc.apply(idValue);
        }
        catch(SQLIntegrityConstraintViolationException e){
            displayAlert(Alert.AlertType.ERROR, mode, "Cannot delete entity which has dependencies.");
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
    protected void closeStage(MouseEvent mouseEvent) {
        Node node = (Node) mouseEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
    protected void displayAlert(Alert.AlertType t, Mode mode, String msg){
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

    @FunctionalInterface
    public interface SQLFunction<T, R> {
        R apply(T t) throws SQLException;
    }

    @FunctionalInterface
    public interface SQLBiFunction<T, U, R> {
        R apply(T t, U u) throws SQLException;
    }
}
