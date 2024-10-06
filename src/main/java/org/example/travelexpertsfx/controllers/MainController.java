package org.example.travelexpertsfx.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.example.travelexpertsfx.contexts.*;
import org.example.travelexpertsfx.models.*;
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
    private Button btnEdit;

    @FXML
    private Tab fxTabAgent;

    @FXML
    private Tab fxTabFee;

    @FXML
    private Tab fxTabAgency;

    @FXML
    private Tab fxTabPackage;

    @FXML
    private Tab fxTabBooking;

    @FXML
    private TableView<Agent> tbAgent;

    @FXML
    private TableView<Fee> tbFee;

    @FXML
    private TableView<Agency> tbAgency;

    @FXML
    private TableView<MyPackage> tbPackage;

    @FXML
    private TableView<Booking> tbBooking;


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
        addSelectionListenerAndBindButton(tbFee, btnEdit);
        // Set up the Edit button action
        setupEditButton(btnEdit, tbFee);
        addSelectionListener(tbAgent);
        addSelectionListener(tbAgency);
        addSelectionListener(tbPackage);
        addSelectionListener(tbBooking);

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

        fxTabBooking.setOnSelectionChanged(event -> {
            _currentContext = new BookingsContext(tbBooking);
            _currentContext.displayTableContent();
            _currentContext.setupTableColumns();
        });

        // Handle button click events
        btnAdd.setOnMouseClicked(event -> {
            mode = Mode.ADD;
            _currentContext.openDialog(null, mode);
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

    private <T> void addSelectionListenerAndBindButton(TableView<T> tableView, Button editButton) {
        // Bind the button's disable property to the table selection
        editButton.visibleProperty().bind(
                tableView.getSelectionModel().selectedItemProperty().isNull().not()
        );
    }

    private <T> void setupEditButton(Button editButton, TableView<T> tableView) {
        editButton.setOnAction(event -> {
            T selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                Platform.runLater(() -> {
                    mode = Mode.EDIT;
                    _currentContext.openDialog(selectedItem, mode);
                });
            } else {
                System.out.println("No item selected for editing");
            }
        });
    }
}
