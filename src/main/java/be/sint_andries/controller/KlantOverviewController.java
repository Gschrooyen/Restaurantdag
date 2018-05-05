package be.sint_andries.controller;

import be.sint_andries.Main;
import be.sint_andries.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class KlantOverviewController extends Controller {
    @FXML
    private Button btnBewerk;
    @FXML
    private TableColumn<Klant, Integer> colPers;
    @FXML
    private TableView<Klant> tblKlant;
    @FXML
    private TableColumn<Klant, SimpleStringProperty> colNaam;
    @FXML
    private TableColumn<Klant, SimpleStringProperty> colGroep;
    private Restaurantdag initdata = null;


    public void Back(Event event) throws IOException {
        HelperMethods.ChangeScene(event, "be/sint_andries/view/HomeScreenView.fxml");
    }

    public void Nieuw(Event event) throws IOException {
        HelperMethods.ChangeScene(event, "be/sint_andries/view/AddKlantView.fxml");
    }

    public void Bewerk(Event event) throws IOException {
        HelperMethods.ChangeSceneWithData(event, "be/sint_andries/view/AddKlantView.fxml", tblKlant.getSelectionModel().getSelectedItem());
    }

    public void checkBewerk(MouseEvent event) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        HelperMethods.setbtnActive(tblKlant, btnBewerk, event, this.getClass().getMethod("Bewerk", Event.class), this);
    }

    @Override
    public <T> void initdata(T dataToInit) {
        System.out.println(dataToInit);
        if (dataToInit instanceof Restaurantdag) {
            initdata = (Restaurantdag) dataToInit;
            System.out.println(initdata);
        }
        try {
            tblKlant.setItems(getKlanten());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(initdata);
        //set up the columns
        colNaam.setCellValueFactory(new PropertyValueFactory<>("naam"));
        colGroep.setCellValueFactory(new PropertyValueFactory<>("groepsNaam"));
        colPers.setCellValueFactory(new PropertyValueFactory<>("aantalPers"));

        try {
            tblKlant.setItems(getKlanten());
        } catch (Exception e) {
            try {
                ResultSet rs = Main.connection.prepareStatement("SELECT * FROM Restaurantdag ORDER BY Datum desc").executeQuery();
                rs.next();
                initdata = new Restaurantdag(rs.getDate("Datum").toLocalDate(), rs.getString("Naam"), rs.getInt("Id"));
                initialize(url, rb);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

    }

    private ObservableList<Klant> getKlanten() throws Exception {
        if (initdata == null) {
            throw new Exception("Restaurantdag is niet ingesteld");
        }
        ObservableList<Klant> klanten = FXCollections.observableArrayList();
        try {
            PreparedStatement prep = Main.connection.prepareStatement("SELECT K.Id kid, Naam, GroepsNaam, K.RestaurantdagId, T.Id tid, Uur, Minuut FROM Klant K JOIN Tijdstip T on K.TijdstipId = T.Id where K.RestaurantdagId = ?");
            prep.setInt(1, initdata.getId());
            ResultSet rsKlanten = prep.executeQuery();
            while (rsKlanten.next()) {
                Klant k;
                short uur = rsKlanten.getShort("Uur");
                short minuut = rsKlanten.getShort("Minuut");
                int tijdId = rsKlanten.getInt("tid");
                Tijdstip tijd = new Tijdstip(tijdId, uur, minuut);
                int id = rsKlanten.getInt("kid");
                String naam = rsKlanten.getString("Naam");
                if (rsKlanten.getString("GroepsNaam") != null) {
                    String groep = rsKlanten.getString("Groepsnaam");
                    k = new Klant(tijd, id, naam, groep);
                } else {
                    k = new Klant(tijd, id, naam);
                }
                PreparedStatement prepBestellingen = Main.connection.prepareStatement("SELECT G.Naam gerechtnaam, GB.Aantal aantal, G.Id id, G.IsDessert dessert, G.Prijs prijs, GB.BestellingsId bestid, G.IsKind FROM GerechtBestelling GB JOIN Gerecht G on GB.GerechtId = G.Id where KlantId = ?");
                prepBestellingen.setInt(1, k.getId());
                ResultSet rsbestellingen = prepBestellingen.executeQuery();
                while (rsbestellingen.next()) {
                    String gerechtnaam = rsbestellingen.getString("gerechtnaam");
                    int aantal = rsbestellingen.getInt("aantal");
                    int gerechtid = rsbestellingen.getInt("id");
                    Gerecht g;
                    if (rsbestellingen.getBoolean("dessert")) {
                        g = new Gerecht(gerechtnaam, gerechtid);
                    } else {
                        Double prijs = rsbestellingen.getDouble("prijs");
                        g = new Gerecht(gerechtnaam, prijs,rsbestellingen.getBoolean(7) ,gerechtid);
                    }
                    int bestid = rsbestellingen.getInt("bestid");
                    Bestelling b = new Bestelling(g, aantal, bestid);
                    k.addBestelling(b);
                }
                klanten.add(k);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return klanten;
    }
}
