package be.sint_andries.controller;

import be.sint_andries.Main;
import be.sint_andries.model.Restaurantdag;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StartScreenViewController extends Controller {
    public Label lblTitel;
    private Restaurantdag initdata;
    @Override
    public <T> void initdata(T dataToInit) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ResultSet rs;
        try {
            rs = Main.connection.prepareStatement("SELECT * FROM Restaurantdag ORDER BY Datum desc").executeQuery();
            rs.next();
            initdata = new Restaurantdag(rs.getDate("Datum").toLocalDate(), rs.getString("Naam"), rs.getInt("Id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        lblTitel.setText(initdata.getNaam().getValue());

    }


    public void changeSceneToAddRestaurantdag(ActionEvent actionEvent) throws IOException {
        HelperMethods.ChangeSceneWithData(actionEvent, "be/sint_andries/view/AddRestaurantdagView.fxml", initdata);
    }

    public void changeSceneToKlantOverview(ActionEvent actionEvent) {
        try {
            HelperMethods.ChangeSceneWithData(actionEvent, "be/sint_andries/view/KlantOverviewView.fxml", initdata);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not find KlantOverview.fxml", "IOException", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void ChangeScenteToAddKlant(ActionEvent actionEvent) {
        try {
            HelperMethods.ChangeScene(actionEvent, "be/sint_andries/view/AddKlantView.fxml");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not find AddKlantView.fxml", "IOException", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
