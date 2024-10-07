package org.example.travelexpertsfx.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.example.travelexpertsfx.contexts.*;
import org.example.travelexpertsfx.models.Agency;
import org.example.travelexpertsfx.models.Agent;
import org.example.travelexpertsfx.models.Fee;
import org.example.travelexpertsfx.Mode;
import org.example.travelexpertsfx.models.MyPackage;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnInfo;

    @FXML
    private Button btnExit;

    @FXML
    private TabPane fxTabs;

    @FXML
    private Tab fxTabAgent;

    @FXML
    private Tab fxTabFee;

    @FXML
    private Tab fxTabAgency;

    @FXML
    private Tab fxTabPackage;

    @FXML
    private TableView<Agent> tbAgent;

    @FXML
    private TableView<Fee> tbFee;

    @FXML
    private TableView<Agency> tbAgency;

    @FXML
    private TableView<MyPackage> tbPackage;


    private ITableContext _currentContext;
    private Mode mode;

    @FXML // fx:id="btnGeneratePDF"
    private Button btnGeneratePDF; // Value injected by FXMLLoader

    @FXML
    void initialize() {

        // Set up the initial context and display content for the Fees tab
        _currentContext = new FeesContext(tbFee);
        _currentContext.displayTableContent();
        _currentContext.setupTableColumns();

        // Add selection listeners for each table
//        addSelectionListener(tbFee);
//        addSelectionListener(tbAgent);
//        addSelectionListener(tbAgency);
//        addSelectionListener(tbPackage);

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

        fxTabAgency.setOnSelectionChanged(event -> {
            _currentContext = new AgenciesContext(tbAgency);
            _currentContext.displayTableContent();
            _currentContext.setupTableColumns();
        });

        fxTabPackage.setOnSelectionChanged(event -> {
            _currentContext = new PackagesContext(tbPackage);
            _currentContext.displayTableContent();
            _currentContext.setupTableColumns();
        });

        // Handle button click events
        btnAdd.setOnMouseClicked(event -> {
            mode = Mode.ADD;
            _currentContext.openDialog(null, mode);
        });

        btnEdit.setOnMouseClicked(event -> {
            Platform.runLater(() -> {
                mode = Mode.EDIT;
                Object selected;
                selected = _currentContext.getSelected();
                _currentContext.openDialog(selected, mode);
            });
        });

        btnInfo.setOnMouseClicked(event -> {
           int selected = _currentContext.getSelectedInfoId();
           if (selected == -1) return; //Early return if the current table has no info fields
           String currentTab = fxTabs.getSelectionModel().getSelectedItem().getId();
           switch (currentTab) {
               case "fxTabAgent": fxTabs.getSelectionModel().select(fxTabAgency); break; //select the tab of the table that contains additional info
               case "fxTabAgency": break;
               case "fxTabFee": break;
               case "fxTabPackage": break;
           }
           _currentContext.selectInfo(selected); //select the item with the id that corresponds to our info field, defined in the target context
        });

        btnExit.setOnMouseClicked(event -> System.exit(0));

        btnGeneratePDF.setOnMouseClicked(this::buttonGeneratePDFClicked);
    }

    @FXML
    private void buttonGeneratePDFClicked(MouseEvent mouseEvent) {
        // Call generatePDF method from FeesContext
        if (_currentContext != null) {
            try {
                _currentContext.generatePDF(); // Specify the correct file path
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // Add selection listener for any TableView (generic for both Agent and Fee tables)
//    private <T> void addSelectionListener(TableView<T> tableView) {
//        tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldItem, selectedItem) -> {
//            int index = tableView.getSelectionModel().getSelectedIndex();
//            if (tableView.getSelectionModel().isSelected(index)) {
//                Platform.runLater(() -> {
//                    mode = Mode.EDIT;
//                    _currentContext.openDialog(selectedItem, mode);
//                });
//            }
//        });
//    }
}
