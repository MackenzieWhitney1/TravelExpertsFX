package org.example.travelexpertsfx.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.example.travelexpertsfx.contexts.AgentsContext;
import org.example.travelexpertsfx.contexts.FeesContext;
import org.example.travelexpertsfx.contexts.ITableContext;
import org.example.travelexpertsfx.models.Agent;
import org.example.travelexpertsfx.models.Fee;
import org.example.travelexpertsfx.Mode;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnExit;

    @FXML
    private Tab fxTabAgent;

    @FXML
    private Tab fxTabFee;

    @FXML
    private TableView<Agent> tbAgent;

    @FXML
    private TableView<Fee> tbFee;

    private ITableContext _currentContext;
    private Mode mode;

    @FXML
    void initialize() {
        // Set up the initial context and display content for the Fees tab
        _currentContext = new FeesContext(tbFee);
        _currentContext.displayTableContent();
        _currentContext.setupTableColumns();

        // Add selection listeners for each table
        addSelectionListener(tbFee);
        addSelectionListener(tbAgent);

        // Handle tab change events
        fxTabFee.setOnSelectionChanged(event -> {
            _currentContext = new FeesContext(tbFee);
            _currentContext.displayTableContent();
            _currentContext.setupTableColumns();
        });

        fxTabAgent.setOnSelectionChanged(event -> {
            _currentContext = new AgentsContext(tbAgent);
            _currentContext.displayTableContent();
            _currentContext.setupTableColumns();
        });

        // Handle button click events
        btnAdd.setOnMouseClicked(event -> {
            mode = Mode.ADD;
            _currentContext.openDialog(null, mode);
        });

        btnExit.setOnMouseClicked(event -> System.exit(0));
    }

    // Add selection listener for any TableView (generic for both Agent and Fee tables)
    private <T> void addSelectionListener(TableView<T> tableView) {
        tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldItem, selectedItem) -> {
            int index = tableView.getSelectionModel().getSelectedIndex();
            if (tableView.getSelectionModel().isSelected(index)) {
                Platform.runLater(() -> {
                    mode = Mode.EDIT;
                    _currentContext.openDialog(selectedItem, mode);
                });
            }
        });
    }
}
