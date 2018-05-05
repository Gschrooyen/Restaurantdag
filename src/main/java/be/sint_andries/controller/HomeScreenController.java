package be.sint_andries.controller;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static be.sint_andries.controller.HelperMethods.ChangeScene;



public class HomeScreenController extends Controller {
    @FXML
    private Button btnArchief;


    public void ChangeSceneToGerechtOverZicht(Event event) throws IOException {
        ChangeScene(event, "be/sint_andries/view/GerechtOverzichtView.fxml");
    }

    public void ChangeSceneToRestaurantdagOverview(Event event) throws IOException {
        ChangeScene(event, "be/sint_andries/view/RestaurantdagOverViewView.fxml");
    }


    /**
     * this method changes the be.sint_andries.view to KlantoverviewView
     *
     * @param event the event that called this function
     * @throws IOException exeption when loading file
     */
    public void ChangeSceneToKlantOverview(Event event) throws IOException {
        ChangeScene(event, "be/sint_andries/view/KlantOverviewView.fxml");

    }

    public void ChangeSceneToArchief(Event event) throws IOException {
        ChangeScene(event, "be/sint_andries/view/SelectRestaurantdagView.fxml");
    }

    /**
     * closes the program
     */
    public void close() {
        Platform.exit();
        System.exit(0);
    }

    @Override
    public <T> void initdata(T dataToInit) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnArchief.setMnemonicParsing(true);
    }


}
