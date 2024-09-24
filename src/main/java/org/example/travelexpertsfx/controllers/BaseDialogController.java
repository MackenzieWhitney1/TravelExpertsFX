package org.example.travelexpertsfx.controllers;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.travelexpertsfx.models.Mode;

public abstract class BaseDialogController {
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
}
