package be.sint_andries.controller;

import javafx.application.Platform;
import javafx.event.Event;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static be.sint_andries.controller.HelperMethods.ChangeScene;


/**
 * controller to HomeScreenView
 */
public class HomeScreenController extends Controller {

    /**
     * changes the scene to the GerechtOverzichtView, shows an error message when the fxml connot be loaded.
     * @param event the event that called this method.
     */
    public void ChangeSceneToGerechtOverZicht(Event event){
        try {
            ChangeScene(event, "be/sint_andries/view/GerechtOverzichtView.fxml");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not find GerechtOverzichtView.fxml", "IOException", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * changes the scene to RestaurantdahOverviewView, shows an error message when the fxml connot be loaded.
     * @param event the event that called this method.
     */
    public void ChangeSceneToRestaurantdagOverview(Event event) {
        try {
            ChangeScene(event, "be/sint_andries/view/RestaurantdagOverViewView.fxml");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not find RestaurantdagOverViewView.fxml", "IOException", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    /**
     * this method changes the scene to KlantoverviewView, shows an error message when the fxml connot be loaded.
     * @param event the event that called this function
     */
    public void ChangeSceneToKlantOverview(Event event){
        try {
            ChangeScene(event, "be/sint_andries/view/KlantOverviewView.fxml");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not find KlantOverviewView.fxml", "IOException", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    /**
     * this method changes the scene to SelectRestaurantdagView, shows an error message when the fxml connot be loaded.
     * @param event the event that called this function
     */
    public void ChangeSceneToArchief(Event event){
        try {
            ChangeScene(event, "be/sint_andries/view/SelectRestaurantdagView.fxml");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "SelectRestaurantdagView.fxml", "IOException", JOptionPane.ERROR_MESSAGE);
        }
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

    }


}
