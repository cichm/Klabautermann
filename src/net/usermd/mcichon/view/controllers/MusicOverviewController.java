package net.usermd.mcichon.view.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import net.usermd.mcichon.model.ItemModel;
import net.usermd.mcichon.MainApp;

public class MusicOverviewController {
    @FXML
    private TableView< ItemModel > songTable;
    @FXML
    private TableColumn< ItemModel , String > nameColumn;
    @FXML
    private TableColumn< ItemModel , String > timeColumn;

    @FXML
    private Label nameLabel;
    @FXML
    private Label timeLabel;

    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public MusicOverviewController() {
    }

    /**
     * Definitions the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory( cellData -> cellData.getValue().nameProperty() );
        timeColumn.setCellValueFactory( cellData -> cellData.getValue().timeProperty() );

        showItemDetails(null);

        // Listen for selection changes and show the item details when changed.
        songTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showItemDetails( newValue ));
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp( MainApp mainApp ) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        songTable.setItems( mainApp.getItemData() );
    }

    /**
     * Fills all text fields to show details about the item.
     * If the specified item is null, all text fields are cleared.
     *
     * @param item the item or null
     */
    private void showItemDetails( ItemModel item ) {
        if (item != null) {
            // Fill the labels with info from the item object.
            nameLabel   .setText( item.getName() );
            timeLabel   .setText( item.getTime() );
        } else {
            // Item is null, remove all the text.
            nameLabel   .setText("");
            timeLabel   .setText("");
        }
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeleteItem() {
        int selectedIndex = songTable.getSelectionModel().getSelectedIndex();
        if ( selectedIndex >= 0 ) {
            songTable.getItems().remove( selectedIndex );
        } else {
            // Nothing selected.
            Alert alert = new Alert( AlertType.WARNING );
            alert.initOwner( mainApp.getPrimaryStage() );
            alert.setTitle(         "No Selection" );
            alert.setHeaderText(    "No item Selected" );
            alert.setContentText(   "Please select a item in the table." );

            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new item.
     */
    @FXML
    private void handleNewItem() {
        ItemModel tempItem = new ItemModel();
        boolean okClicked = mainApp.showItemEditDialog( tempItem );
        if ( okClicked ) {
            mainApp.getItemData().add(tempItem);
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected item.
     */
    @FXML
    private void handleEditItem() {
        ItemModel selectedItem = songTable.getSelectionModel().getSelectedItem();
        if ( selectedItem != null ) {
            boolean okClicked = mainApp.showItemEditDialog(selectedItem);
            if ( okClicked ) {
                showItemDetails( selectedItem );
            }

        } else {
            Alert alert = new Alert( AlertType.WARNING );
            alert.initOwner( mainApp.getPrimaryStage() );
            alert.setTitle(         "No Selection" );
            alert.setHeaderText(    "No item Selected" );
            alert.setContentText(   "Please select a item in the table." );

            alert.showAndWait();
        }
    }
}