package be.sint_andries.controller;

import be.sint_andries.Main;
import be.sint_andries.model.Restaurantdag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.ResourceBundle;

public class SelectRestaurantdagViewController extends Controller {


    public static Restaurantdag restaurantdag;
    public TableView<Restaurantdag> tblRestaurantdag;
    public TableColumn<Restaurantdag, Integer> colId;
    public TableColumn<Restaurantdag, String> colNaam;
    public TableColumn<Restaurantdag, Date> colDatum;
    public Button btnBack;
    public Button btnSelect;

    public void setActive() {
        if (tblRestaurantdag.getSelectionModel().getSelectedItem() != null) {
            btnSelect.setDisable(false);
        } else {
            btnSelect.setDisable(true);
        }
    }

    public void selectRestaurantdag(Event event) throws IOException {
        SelectRestaurantdagViewController.restaurantdag = tblRestaurantdag.getSelectionModel().getSelectedItem();
        HelperMethods.ChangeSceneWithData(event, "be/sint_andries/view/KlantOverviewView.fxml", restaurantdag);
    }

    @Override
    public <T> void initdata(T dataToInit) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDatum.setCellValueFactory(new PropertyValueFactory<>("datum"));
        colNaam.setCellValueFactory(new PropertyValueFactory<>("naam"));

        tblRestaurantdag.setItems(getRestaurantdagen());
    }

    private ObservableList<Restaurantdag> getRestaurantdagen() {
        ObservableList<Restaurantdag> restaurantdagen = FXCollections.observableArrayList();
        Statement state;
        try {
            state = Main.connection.createStatement();
            ResultSet rs = state.executeQuery("SELECT * FROM Restaurantdag");
            RestaurantdagOverviewViewController.fillList(restaurantdagen, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurantdagen;
    }
}
