package be.sint_andries.controller;

import be.sint_andries.Main;
import be.sint_andries.controller.CelFactories.GerechtListCel;
import be.sint_andries.model.Gerecht;
import be.sint_andries.model.Restaurantdag;
import be.sint_andries.model.Tijdstip;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

// TODO: 30.03.18 input controleren
public class AddRestaurantdagViewController extends Controller {


    @FXML
    private TextField txtNaam;
    @FXML
    private DatePicker dpDatum;
    @FXML
    private ListView<Gerecht> lvAlleHoofd;
    @FXML
    private ListView<Gerecht> lvGeselHoofd;
    @FXML
    private ListView<Gerecht> lvAlleDes;
    @FXML
    private ListView<Gerecht> lvGeselDes;
    private Restaurantdag initdata = null;
    private ObservableList<Gerecht> hoofdegerecht_origineel = FXCollections.observableArrayList();
    private ObservableList<Gerecht> dessert_origineel = FXCollections.observableArrayList();

    public void Back(Event event) throws IOException {
        HelperMethods.ChangeScene(event, "be/sint_andries/view/RestaurantdagOverViewView.fxml");
    }

    public void addHoofd() {
        ObservableList<Gerecht> ger = lvGeselHoofd.getItems();
        ger.add(lvAlleHoofd.getSelectionModel().getSelectedItem());
        lvGeselHoofd.setItems(ger);
    }

    public void delHoofd() {
        ObservableList<Gerecht> ger = lvGeselHoofd.getItems();
        ger.remove(lvGeselHoofd.getSelectionModel().getSelectedIndex());
    }

    public void addDes() {
        ObservableList<Gerecht> ger = lvGeselDes.getItems();
        ger.add(lvAlleDes.getSelectionModel().getSelectedItem());
        lvGeselDes.setItems(ger);
    }

    public void delDes() {
        ObservableList<Gerecht> ger = lvGeselDes.getItems();
        ger.remove(lvGeselDes.getSelectionModel().getSelectedIndex());
    }

    public void Opslaan(Event event) throws SQLException, IOException {
        if (txtNaam.getText().equals("")) {
            txtNaam.setStyle("-fx-border-color: red; -fx-border-width: 3px");
            txtNaam.requestFocus();
        } else if (dpDatum.getValue() == null) {
            dpDatum.setStyle("-fx-border-color: red; -fx-border-width: 3px");
            dpDatum.requestFocus();
        } else if (lvGeselHoofd.getItems().isEmpty()) {
            lvGeselHoofd.setStyle("-fx-border-color: red; -fx-border-width: 3px");
        } else if (lvGeselDes.getItems().isEmpty()) {
            lvGeselDes.setStyle("-fx-border-color: red; -fx-border-width: 3px");
        } else {

            PreparedStatement insertRestaurantdagGerecht = Main.connection.prepareStatement("INSERT INTO RestaurantdagGerechten VALUES (?, ?)");

            if (initdata == null) {

                PreparedStatement prepRestaurantdag = Main.connection.prepareStatement("INSERT INTO Restaurantdag VALUES (?, ?, ?)");


                String naam = txtNaam.getText();
                LocalDate date = dpDatum.getValue();
                prepRestaurantdag.setString(2, naam);
                prepRestaurantdag.setDate(3, java.sql.Date.valueOf(date));
                prepRestaurantdag.executeUpdate();
                PreparedStatement prs = Main.connection.prepareStatement("select * from Restaurantdag where Datum = ?");
                prs.setDate(1, java.sql.Date.valueOf(date));
                ResultSet rs = prs.executeQuery();
                Restaurantdag restaurantdag = null;
                while (rs.next()) {
                    restaurantdag = new Restaurantdag(rs.getDate(3).toLocalDate(), rs.getString(2), rs.getInt(1));
                    insertRestaurantdagGerecht.setInt(1, rs.getInt("Id"));
                }
                assert restaurantdag != null;
                for (Gerecht hoofdgerecht :
                        lvGeselHoofd.getItems()) {
                    insertRestaurantdagGerecht.setInt(2, hoofdgerecht.getId());
                    insertRestaurantdagGerecht.execute();
                }
                for (Gerecht dessert :
                        lvGeselDes.getItems()) {
                    insertRestaurantdagGerecht.setInt(2, dessert.getId());
                    insertRestaurantdagGerecht.execute();
                }


            } else {
                //klaarmaken van querrries
                PreparedStatement updateRestaurantdag = Main.connection.prepareStatement("UPDATE Restaurantdag SET Naam = ?, Datum = ? WHERE Id = ?");
                PreparedStatement deleteRestaurantdagGerecht = Main.connection.prepareStatement("DELETE FROM RestaurantdagGerechten WHERE RestaurantdagId = ? AND GerechtId = ?");

                //update restaurantdag
                updateRestaurantdag.setString(1, txtNaam.getText());
                updateRestaurantdag.setDate(2, java.sql.Date.valueOf(dpDatum.getValue()));
                updateRestaurantdag.setInt(3, initdata.getId());
                updateRestaurantdag.execute();

                ObservableList<Gerecht> hoofdgerechten = FXCollections.observableArrayList(lvGeselHoofd.getItems());
                ObservableList<Gerecht> desserten = FXCollections.observableArrayList(lvGeselDes.getItems());

                hoofdgerechten.removeAll(hoofdegerecht_origineel);
                for (Gerecht g :
                        hoofdgerechten) {
                    insertRestaurantdagGerecht.setInt(1, initdata.getId());
                    insertRestaurantdagGerecht.setInt(2, g.getId());
                    insertRestaurantdagGerecht.execute();
                }
                hoofdgerechten = FXCollections.observableArrayList(lvGeselHoofd.getItems());
                hoofdegerecht_origineel.removeAll(hoofdgerechten);
                for (Gerecht g :
                        hoofdegerecht_origineel) {
                    deleteRestaurantdagGerecht.setInt(1, initdata.getId());
                    deleteRestaurantdagGerecht.setInt(2, g.getId());
                    deleteRestaurantdagGerecht.execute();

                }

                desserten.removeAll(dessert_origineel);
                for (Gerecht g :
                        desserten) {
                    insertRestaurantdagGerecht.setInt(1, initdata.getId());
                    insertRestaurantdagGerecht.setInt(2, g.getId());
                    insertRestaurantdagGerecht.execute();
                }
                desserten = FXCollections.observableArrayList(lvGeselDes.getItems());
                dessert_origineel.removeAll(desserten);
                for (Gerecht g :
                        dessert_origineel) {
                    deleteRestaurantdagGerecht.setInt(1, initdata.getId());
                    deleteRestaurantdagGerecht.setInt(2, g.getId());
                    deleteRestaurantdagGerecht.execute();

                }
            }
            HelperMethods.ChangeScene(event, "be/sint_andries/view/RestaurantdagOverViewView.fxml");
        }
    }

    @Override
    public <T> void initdata(T dataToInit) {
        if (dataToInit instanceof Restaurantdag) {
            ObservableList<Gerecht> hoofdgerechten = FXCollections.observableArrayList();
            ObservableList<Gerecht> desserten = FXCollections.observableArrayList();
            ObservableList<Tijdstip> tijdstippen = FXCollections.observableArrayList();

            this.initdata = (Restaurantdag) dataToInit;

            txtNaam.setText(initdata.getNaam().getValue());
            dpDatum.setValue(initdata.getDatum());

            try {
                PreparedStatement prep = Main.connection.prepareStatement("SELECT * FROM RestaurantdagGerechten RG JOIN Gerecht G on RG.GerechtId = G.Id WHERE RestaurantdagId = ?");
                prep.setInt(1, initdata.getId());
                ResultSet rsgerechten = prep.executeQuery();

                while (rsgerechten.next()) {
                    String naam = rsgerechten.getString("Naam");
                    int id = rsgerechten.getInt("Id");

                    if (rsgerechten.getBoolean("IsDessert")) {
                        Gerecht ger = new Gerecht(naam, id);
                        desserten.add(ger);
                        dessert_origineel.add(ger);
                    } else {
                        Gerecht ger = new Gerecht(naam, rsgerechten.getDouble("Prijs"),rsgerechten.getBoolean(5) ,id);
                        hoofdgerechten.add(ger);
                        hoofdegerecht_origineel.add(ger);
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                PreparedStatement prepTijdstippen = Main.connection.prepareStatement("SELECT * FROM Tijdstip where RestaurantdagId = ?");
                prepTijdstippen.setInt(1, initdata.getId());
                ResultSet resultSet = prepTijdstippen.executeQuery();

                while (resultSet.next()) {
                    Tijdstip t = new Tijdstip(resultSet.getInt("Id"), resultSet.getShort(2), resultSet.getShort(3));
                    tijdstippen.add(t);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            lvGeselDes.setItems(desserten);
            lvGeselHoofd.setItems(hoofdgerechten);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lvAlleHoofd.setCellFactory(new GerechtListCel());
        lvAlleHoofd.setItems(getAlleGer(0));

        lvAlleDes.setCellFactory(lvAlleHoofd.getCellFactory());
        lvAlleDes.setItems(getAlleGer(1));

        lvGeselDes.setCellFactory(lvAlleHoofd.getCellFactory());
        lvGeselHoofd.setCellFactory(lvAlleHoofd.getCellFactory());
    }

    private ObservableList<Gerecht> getAlleGer(int dessert) {
        ObservableList<Gerecht> gerechts = FXCollections.observableArrayList();

        try {
            PreparedStatement statement = Main.connection.prepareStatement("SELECT * FROM Gerecht WHERE IsDessert = ?");
            statement.setInt(1, dessert);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Gerecht ger = new Gerecht(resultSet.getString("Naam"), resultSet.getDouble("Prijs"), resultSet.getBoolean(5), resultSet.getInt("Id"));
                gerechts.add(ger);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gerechts;
    }



}
