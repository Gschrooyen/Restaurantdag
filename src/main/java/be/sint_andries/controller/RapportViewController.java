package be.sint_andries.controller;

import be.sint_andries.Main;
import be.sint_andries.model.*;
import javafx.application.Platform;
import javafx.scene.control.Label;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class RapportViewController extends Controller {
    public Label lblTitle;
    private Restaurantdag initdata;
    private List<Klant> klantList = new ArrayList<>();

    @Override
    public <T> void initdata(T dataToInit) {
        if (dataToInit instanceof Restaurantdag) {
            initdata = (Restaurantdag) dataToInit;

            try {
                PreparedStatement preparedStatement = Main.connection.prepareStatement("SELECT Klant.Id \"KlantId\", Klant.Naam \"KlantNaam\", GroepsNaam, BestellingsId, Aantal, Gerecht.Id \"GerechtId\", Gerecht.Naam \"GerechtNaam\", Prijs, IsDessert, IsKind, Tijdstip.Id \"TijdstipId\", Uur, Minuut FROM Klant LEFT OUTER JOIN GerechtBestelling ON Klant.Id = GerechtBestelling.KlantId LEFT JOIN Gerecht ON Gerecht.Id = GerechtBestelling.GerechtId LEFT OUTER JOIN Tijdstip ON Klant.TijdstipId = Tijdstip.Id WHERE Klant.RestaurantdagId = ? ORDER BY KlantId;");
                preparedStatement.setInt(1, initdata.getId());
                ResultSet rs = preparedStatement.executeQuery();
                Klant previousKlant = new Klant("", "", -1);
                while (rs.next()) {
                    Gerecht g;
                    if (rs.getBoolean("IsDessert")) {
                        g = new Gerecht(rs.getString("GerechtNaam"), rs.getInt("GerechtId"));
                    } else {
                        g = new Gerecht(rs.getString("GerechtNaam"), rs.getDouble("Prijs"), rs.getBoolean("IsKind"), rs.getInt("GerechtId"));
                    }
                    System.out.println("\t" + g.getNaam());
                    Bestelling b = new Bestelling(g, rs.getInt("Aantal"));
                    if (previousKlant.getId() != rs.getInt("KlantId")) {

                        if (!Objects.equals(previousKlant.getNaam(), "")) {
                            klantList.add(previousKlant);
                            System.out.println("NAAM: " + previousKlant.getNaam());
                        }
                        previousKlant = new Klant(  new Tijdstip(   rs.getInt("TijdStipId"),
                                rs.getShort("Uur"),
                                rs.getShort("Minuut")),
                                rs.getInt("KlantId"),
                                rs.getString("KlantNaam"),
                                rs.getString("GroepsNaam")
                        );
                    }
                    previousKlant.addBestelling(b);

                }
                klantList.add(previousKlant);
                klantList.forEach(System.out::println);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Fout bij het laden van de data", "SQLException", JOptionPane.ERROR_MESSAGE);
                try {
                    HelperMethods.ChangeScene(lblTitle.getScene(), "be/sint_andries/view/StartScreenView.fxml");
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "critical error program will shut down", "Error", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                    Platform.exit();
                    System.exit(-1);
                }
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
