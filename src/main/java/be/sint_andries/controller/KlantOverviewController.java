package be.sint_andries.controller;

import be.sint_andries.Main;
import be.sint_andries.controller.PDF_makers.PDFMaker;
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

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
// TODO: 6/05/2018 knop om klant te verwijderen.
/**
 * the controler to KlantOverviewView
 */
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

    /**
     * changes the scene to the HomeScreenView, shows an error message when the fxml cannot be loaded.
     * @param event the event that called this method.
     */
    public void Back(Event event){
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                System.out.println("getCurrentDirectory(): "
                        +  chooser.getCurrentDirectory());
                System.out.println("getSelectedFile() : "
                        +  chooser.getSelectedFile());
                PDFMaker.makeTafelverdelingPDF(getKlanten(), chooser.getSelectedFile() + "\\test.pdf");
            }
            else {
                System.out.println("No Selection ");
            }

            System.out.println("made pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            HelperMethods.ChangeScene(event, "be/sint_andries/view/StartScreenView.fxml");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not find HomeScreenView.fxml", "IOException", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    /**
     * changes the scene to the AddKlantView to add a new person, shows an error message when the fxml cannot be loaded.
     * @param event the event that called this method.
     */
    public void Nieuw(Event event) {
        try {
            HelperMethods.ChangeScene(event, "be/sint_andries/view/AddKlantView.fxml");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not find AddKlantView.fxml", "IOException", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    /**
     * changes the scene to the GerechtOverzichtView to edit a person, shows an error message when the fxml cannot be loaded.
     * @param event the event that called this method.
     */
    public void Bewerk(Event event) {
        try {
            HelperMethods.ChangeSceneWithData(event, "be/sint_andries/view/AddKlantView.fxml", tblKlant.getSelectionModel().getSelectedItem());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not find AddKlantView.fxml", "IOException", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * checks if something is selected, activates the "bewerk" button if true
     * @param event the event that called this method
     */
    public void checkBewerk(MouseEvent event)  {
        try {
            HelperMethods.setbtnActive(tblKlant, btnBewerk, event, this.getClass().getMethod("Bewerk", Event.class), this);
        } catch (IllegalAccessException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "IllegalAccesException", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "InvocationTargetException", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "NoSuchMethodExcetion", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();

        }
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
                JOptionPane.showMessageDialog(null, e1.getMessage(), "SQLException", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

    }

    /**
     * get a list of all customers from the database
     * @return ObservableList of all customers
     * @throws Exception thrown when initdata == null
     */
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
            JOptionPane.showMessageDialog(null, e.getMessage(), "SQLException", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return klanten;
    }
}
