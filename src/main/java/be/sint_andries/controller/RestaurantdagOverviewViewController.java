package be.sint_andries.controller;

import be.sint_andries.Main;
import be.sint_andries.model.Restaurantdag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RestaurantdagOverviewViewController extends Controller implements Initializable {
    @FXML
    private TableColumn<Restaurantdag, Integer> colId;
    @FXML
    private TableColumn<Restaurantdag, String> ColNaam;
    @FXML
    private TableColumn<Restaurantdag, LocalDate> ColDatum;
    @FXML
    private Button BtnEdit;
    @FXML
    private TableView<Restaurantdag> tblOverview;


    public void Back(Event event) throws IOException {
        HelperMethods.ChangeScene(event, "be/sint_andries/view/HomeScreenView.fxml");
    }

    public void Nieuw(Event event) throws IOException {
        HelperMethods.ChangeScene(event, "be/sint_andries/view/AddRestaurantdagView.fxml");
    }

    public void Bewerk(Event event) throws IOException {
        HelperMethods.ChangeSceneWithData(event, "be/sint_andries/view/AddRestaurantdagView.fxml", tblOverview.getSelectionModel().getSelectedItem());
    }

    public void checkBewerk() {
        if (tblOverview.getSelectionModel().getSelectedItem() != null) {
            BtnEdit.setDisable(false);
        } else {
            BtnEdit.setDisable(true);
        }
    }

    @Override
    public <T> void initdata(T dataToInit) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColNaam.setCellValueFactory(new PropertyValueFactory<>("naam"));
        ColDatum.setCellValueFactory(new PropertyValueFactory<>("datum"));

        tblOverview.setItems(getRestaurantdagen());
    }

    private ObservableList<Restaurantdag> getRestaurantdagen() {
        ObservableList<Restaurantdag> list = FXCollections.observableArrayList();
        try {
            ResultSet rs = Main.connection.createStatement().executeQuery("select * from Restaurantdag ORDER BY Datum DESC");
            fillList(list, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    static void fillList(ObservableList<Restaurantdag> list, ResultSet rs) throws SQLException {
        while (rs.next()) {
            int id = rs.getInt("Id");
            String naam = rs.getString("Naam");
            LocalDate datum = rs.getDate("Datum").toLocalDate();

            Restaurantdag restaurantdag = new Restaurantdag(datum, naam, id);
            list.add(restaurantdag);
        }
    }

}
