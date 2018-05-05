package be.sint_andries.controller;

import be.sint_andries.Main;
import be.sint_andries.model.Gerecht;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

// TODO: 30.03.18 splitsing dessert hoofdegecht 2 tabellen
public class GerechtOverzichtViewController extends Controller {
    @FXML
    private TableView<Gerecht> tblOverview;
    @FXML
    private TableColumn<Gerecht, String> colNaam;
    @FXML
    private TableColumn<Gerecht, SimpleDoubleProperty> colPrijs;
    @FXML
    private TableColumn<Gerecht, Boolean> colDessert;
    @FXML
    private Button btnBewerk;

    @FXML
    private void Back(Event event) throws IOException {
        HelperMethods.ChangeScene(event, "be/sint_andries/view/HomeScreenView.fxml");
    }

    @FXML
    private void Nieuw(Event event) throws IOException {
        HelperMethods.ChangeScene(event, "be/sint_andries/view/AddNewGerechtView.fxml");
    }

    @FXML
    private void BewerkActive(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            Bewerk(event);
        } else {
            if (tblOverview.getSelectionModel().getSelectedItem() != null) {
                btnBewerk.setDisable(false);
            } else {
                btnBewerk.setDisable(true);
            }
        }
    }

    @FXML
    private void Bewerk(Event event) throws IOException {
        HelperMethods.ChangeSceneWithData(event, "be/sint_andries/view/AddNewGerechtView.fxml", tblOverview.getSelectionModel().getSelectedItem());
    }

    @Override
    public <T> void initdata(T dataToInit) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colNaam.setCellValueFactory(new PropertyValueFactory<>("naam"));
        colPrijs.setCellValueFactory(new PropertyValueFactory<>("prijs"));

        //Display properties of the prijs column
        colPrijs.setCellFactory(column -> new TableCell<Gerecht, SimpleDoubleProperty>() {
            @Override
            protected void updateItem(SimpleDoubleProperty item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(item.get()));
                }
            }
        });
        colDessert.setCellValueFactory(celldata -> celldata.getValue().isDessert());


        //Display properties for the is Dessert column
        colDessert.setCellFactory(column -> new TableCell<Gerecht, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    if (item) {
                        setText("ja");
                    } else {
                        setText("nee");
                    }
                }
            }
        });

        tblOverview.setItems(initGerechten());

    }

    private ObservableList<Gerecht> initGerechten() {
        ObservableList<Gerecht> list = FXCollections.observableArrayList();
        try {
            PreparedStatement prep = Main.connection.prepareStatement("SELECT * FROM Gerecht");
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String naam = rs.getString("Naam");
                Double prijs = rs.getDouble("Prijs");
                Boolean isKind = rs.getBoolean(4);

                if (prijs == 0) {
                    Gerecht ger = new Gerecht(naam, id);
                    list.add(ger);
                } else {
                    Gerecht ger = new Gerecht(naam, prijs, isKind, id);
                    list.add(ger);
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
