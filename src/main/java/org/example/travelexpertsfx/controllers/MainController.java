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

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAdd"
    private Button btnAdd; // Value injected by FXMLLoader

    @FXML // fx:id="btnExit"
    private Button btnExit; // Value injected by FXMLLoader

    @FXML
    private TableColumn<Agent, Integer> colAgencyID;

    @FXML
    private TableColumn<Agent, String> colAgtEmail;

    @FXML
    private TableColumn<Agent, String> colAgtFirstName;

    @FXML
    private TableColumn<Agent, Integer> colAgtId;

    @FXML
    private TableColumn<Agent, String> colAgtInitial;

    @FXML
    private TableColumn<Agent, String> colAgtLastName;

    @FXML
    private TableColumn<Agent, String> colAgtPhone;

    @FXML
    private TableColumn<Agent, String> colAgtPosition;

    @FXML // fx:id="colFeeAmount"
    private TableColumn<Fee, Double> colFeeAmount; // Value injected by FXMLLoader

    @FXML // fx:id="colFeeDescription"
    private TableColumn<Fee, String> colFeeDescription; // Value injected by FXMLLoader

    @FXML // fx:id="colFeeId"
    private TableColumn<Fee, String> colFeeId; // Value injected by FXMLLoader

    @FXML // fx:id="colFeeName"
    private TableColumn<Fee, String> colFeeName; // Value injected by FXMLLoader

    @FXML // fx:id="fxTabAgent"
    private Tab fxTabAgent; // Value injected by FXMLLoader

    @FXML // fx:id="fxTabFee"
    private Tab fxTabFee; // Value injected by FXMLLoader

    @FXML // fx:id="tbAgent"
    private TableView<Agent> tbAgent; // Value injected by FXMLLoader

    @FXML // fx:id="tbFee"
    private TableView<Fee> tbFee; // Value injected by FXMLLoader

    private ITableContext _currentContext;
    private Mode mode;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdd != null : "fx:id=\"btnAdd\" was not injected: check your FXML file 'main-view.fxml'.";
        assert btnExit != null : "fx:id=\"btnExit\" was not injected: check your FXML file 'main-view.fxml'.";
        assert colAgencyID != null : "fx:id=\"colAgencyID\" was not injected: check your FXML file 'main-view.fxml'.";
        assert colAgtEmail != null : "fx:id=\"colAgtEmail\" was not injected: check your FXML file 'main-view.fxml'.";
        assert colAgtFirstName != null : "fx:id=\"colAgtFirstName\" was not injected: check your FXML file 'main-view.fxml'.";
        assert colAgtId != null : "fx:id=\"colAgtId\" was not injected: check your FXML file 'main-view.fxml'.";
        assert colAgtInitial != null : "fx:id=\"colAgtInitial\" was not injected: check your FXML file 'main-view.fxml'.";
        assert colAgtLastName != null : "fx:id=\"colAgtLastName\" was not injected: check your FXML file 'main-view.fxml'.";
        assert colAgtPhone != null : "fx:id=\"colAgtPhone\" was not injected: check your FXML file 'main-view.fxml'.";
        assert colAgtPosition != null : "fx:id=\"colAgtPosition\" was not injected: check your FXML file 'main-view.fxml'.";
        assert colFeeAmount != null : "fx:id=\"colFeeAmount\" was not injected: check your FXML file 'main-view.fxml'.";
        assert colFeeDescription != null : "fx:id=\"colFeeDescription\" was not injected: check your FXML file 'main-view.fxml'.";
        assert colFeeId != null : "fx:id=\"colFeeId\" was not injected: check your FXML file 'main-view.fxml'.";
        assert colFeeName != null : "fx:id=\"colFeeName\" was not injected: check your FXML file 'main-view.fxml'.";
        assert fxTabAgent != null : "fx:id=\"fxTabAgent\" was not injected: check your FXML file 'main-view.fxml'.";
        assert fxTabFee != null : "fx:id=\"fxTabFee\" was not injected: check your FXML file 'main-view.fxml'.";
        assert tbAgent != null : "fx:id=\"tbAgent\" was not injected: check your FXML file 'main-view.fxml'.";
        assert tbFee != null : "fx:id=\"tbFee\" was not injected: check your FXML file 'main-view.fxml'.";
// set up table columns - this isn't DRY
        colFeeId.setCellValueFactory(new PropertyValueFactory<Fee, String>("feeId"));
        colFeeName.setCellValueFactory(new PropertyValueFactory<Fee, String>("feeName"));
        colFeeAmount.setCellValueFactory(new PropertyValueFactory<Fee, Double>("feeAmt"));
        colFeeDescription.setCellValueFactory(new PropertyValueFactory<Fee, String>("feeDesc"));

        colAgtId.setCellValueFactory(new PropertyValueFactory<Agent, Integer>("agentId"));
        colAgtFirstName.setCellValueFactory(new PropertyValueFactory<Agent, String>("agtFirstName"));
        colAgtInitial.setCellValueFactory(new PropertyValueFactory<Agent, String>("agtMiddleInitial"));
        colAgtLastName.setCellValueFactory(new PropertyValueFactory<Agent, String>("agtLastName"));
        colAgtPhone.setCellValueFactory(new PropertyValueFactory<Agent, String>("agtBusPhone"));
        colAgtEmail.setCellValueFactory(new PropertyValueFactory<Agent, String>("agtEmail"));
        colAgtPosition.setCellValueFactory(new PropertyValueFactory<Agent, String>("agtPosition"));
        colAgencyID.setCellValueFactory(new PropertyValueFactory<Agent, Integer>("agencyId"));

        _currentContext = new FeesContext(tbFee);
        _currentContext.displayTableContent();

        // make this part of an abstraction
        fxTabFee.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                _currentContext = new FeesContext(tbFee);
                _currentContext.displayTableContent();
            }
        });
        fxTabAgent.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                _currentContext = new AgentsContext(tbAgent);
                _currentContext.displayTableContent();
            }
        });

        // refactor inside methods to use wrapper class
        tbFee.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Fee>() {
            @Override
            public void changed(ObservableValue<? extends Fee> observableValue, Fee fee, Fee t1) {
                int index = tbFee.getSelectionModel().getSelectedIndex();
                // check when it loses or gains selection
                if(tbFee.getSelectionModel().isSelected(index)){
                    // open the dialog in a separate thread - to avoid error when dialog closes
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            mode = Mode.EDIT;
                            _currentContext.openDialog(t1, mode);
                        }
                    }); //whenever possible to run the task, run it
                }
            }
        });
        tbAgent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Agent>() {
            @Override
            public void changed(ObservableValue<? extends Agent> observableValue, Agent agent, Agent t1) {
                int index = tbAgent.getSelectionModel().getSelectedIndex();
                // check when it loses or gains selection
                if(tbAgent.getSelectionModel().isSelected(index)){
                    // open the dialog in a separate thread - to avoid error when dialog closes
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            mode = Mode.EDIT;
                            _currentContext.openDialog(t1, mode);
                        }
                    }); //whenever possible to run the task, run it
                }
            }
        });

        btnAdd.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mode = Mode.ADD;
                _currentContext.openDialog(null, mode); // no item at beginning of add.
            }
        });
        btnExit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.exit(0);
            }
        });
    } // end initialize
} // end class
