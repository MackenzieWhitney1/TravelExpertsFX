package org.example.travelexpertsfx.controllers;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.example.travelexpertsfx.PDFGenerator;
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
    private Button btnEdit;

    @FXML
    private Button btnInfo;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnAudit;

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
        btnAudit.setVisible(false);
      
        // Handle tab change events
        fxTabFee.setOnSelectionChanged(event -> {
            _currentContext = new FeesContext(tbFee);
            _currentContext.displayTableContent();
            _currentContext.setupTableColumns();
            btnGeneratePDF.setVisible(true);
            btnAudit.setVisible(false);
        });

        fxTabAgent.setOnSelectionChanged(event -> {
            _currentContext = new AgentsContext(tbAgent);
            _currentContext.displayTableContent();
            _currentContext.setupTableColumns();
            btnGeneratePDF.setVisible(false);
            btnAudit.setVisible(false);
        });

        fxTabAgency.setOnSelectionChanged(event -> {
            _currentContext = new AgenciesContext(tbAgency);
            _currentContext.displayTableContent();
            _currentContext.setupTableColumns();
            btnGeneratePDF.setVisible(false);
            btnAudit.setVisible(false);
        });

        fxTabPackage.setOnSelectionChanged(event -> {
            _currentContext = new PackagesContext(tbPackage);
            _currentContext.displayTableContent();
            _currentContext.setupTableColumns();
            btnGeneratePDF.setVisible(false);
            btnAudit.setVisible(false);
        });

        fxTabBooking.setOnSelectionChanged(event -> {
            _currentContext = new BookingsContext(tbBooking);
            _currentContext.displayTableContent();
            _currentContext.setupTableColumns();
            btnGeneratePDF.setVisible(false);
            btnAudit.setVisible(true);
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
               case "fxTabBooking":break;
           }
           _currentContext.selectInfo(selected); //select the item with the id that corresponds to our info field, defined in the target context
        });

        btnExit.setOnMouseClicked(event -> System.exit(0));

        btnGeneratePDF.setOnMouseClicked(this::buttonGeneratePDFClicked);

        btnAudit.setOnMouseClicked(event ->{
            _currentContext.audit();
        });
    }

    @FXML
    private void buttonGeneratePDFClicked(MouseEvent mouseEvent) {
        final String userHome = System.getProperty("user.home");
        final String pdfPath = userHome + "\\Documents\\fees.pdf"; // Saves to the Documents folder
        try {
            PDFGenerator.generateInvoice(pdfPath);
        } catch (FileNotFoundException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
        // Call generatePDF method from FeesContext
        /*if (_currentContext != null) {
            try {
                _currentContext.generatePDF(); // Specify the correct file path
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }*/
    }
}
