package be.sint_andries.controller;

import javafx.event.ActionEvent;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static be.sint_andries.controller.HelperMethods.ChangeScene;


/**
 * controller to HomeScreenView
 */
public class HomeScreenController extends Controller {

    @Override
    public <T> void initdata(T dataToInit) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void changeSceneToStart(ActionEvent actionEvent) {
        try {
            ChangeScene(actionEvent, "be/sint_andries/view/StartScreenView.fxml");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "SelectRestaurantdagView.fxml", "IOException", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void changeSceneToNieuw(ActionEvent actionEvent) {
        Integer choise = JOptionPane.showConfirmDialog(null, "ben je zeker dat je een nieuwe restaurantdag wilt aanmaken", "bevestigen", JOptionPane.YES_NO_OPTION);
        if (choise == 0) {
            try {
                ChangeScene(actionEvent, "be/sint_andries/view/ParentView.fxml");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "SelectRestaurantdagView.fxml", "IOException", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * this method changes the scene to SelectRestaurantdagView, shows an error message when the fxml connot be loaded.
     * @param actionEvent the event that called this function
     */
    public void changeSceneToArchief(ActionEvent actionEvent){
        try {
            ChangeScene(actionEvent, "be/sint_andries/view/SelectRestaurantdagView.fxml");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "SelectRestaurantdagView.fxml", "IOException", JOptionPane.ERROR_MESSAGE);
        }
    }

}
