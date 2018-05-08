package be.sint_andries.controller;


import be.sint_andries.Main;
import be.sint_andries.model.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.util.converter.IntegerStringConverter;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * controller to AddKlantView
 */

public class AddKlantController extends Controller {

    public TableView<Bestelling> tblDessert;
    public TableColumn<Bestelling, Gerecht> colDessert;
    public TableColumn<Bestelling, Integer> colDessertAantal;
    public ComboBox<Tijdstip> cbxTijdstip;
    public TableColumn<Bestelling, Double> colTotaal;
    public Label lblTotaalPers;
    public Label lblTotaalPrijs;
    @FXML
    private TableView<Bestelling> tblHoofd;
    @FXML
    private TableColumn<Bestelling, Gerecht> colHoofdGerecht;
    @FXML
    private TableColumn<Bestelling, Integer> colHoofdgerechtAantal;
    @FXML
    private TextField txtGroepsnaam;
    @FXML
    private TextField txtNaam;
    private Klant initData;


    public void Back(Event event) {
        try {
            HelperMethods.ChangeSceneWithData(event, "be/sint_andries/view/KlantOverviewView.fxml", getResto());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not find KlantOverview.fxml", "IOException", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "IOException", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    public void edit(TableColumn.CellEditEvent e) {
        Bestelling b = (Bestelling) e.getTableView().getSelectionModel().getSelectedItem();
        b.setAantal((Integer) e.getNewValue());


        txtNaam.focusTraversableProperty().setValue(false);
        txtGroepsnaam.focusTraversableProperty().setValue(false);
        cbxTijdstip.focusTraversableProperty().setValue(false);
        tblHoofd.focusTraversableProperty().setValue(false);
        if (e.getTableView() == tblDessert) {
            if (e.getTableView().getSelectionModel().getSelectedIndex() != e.getTableView().getItems().size() - 1) {
                e.getTableView().getSelectionModel().selectBelowCell();
            } else {
                e.getTableView().getSelectionModel().clearSelection();
            }

        } else {
            if (e.getTableView().getSelectionModel().getSelectedIndex() != e.getTableView().getItems().size() - 1) {
                tblHoofd.focusTraversableProperty().setValue(true);
                e.getTableView().getSelectionModel().selectBelowCell();
            } else {

                tblDessert.getSelectionModel().selectFirst();
                tblHoofd.getSelectionModel().clearSelection();
                tblDessert.edit(0, colDessertAantal);
            }
            tblHoofd.refresh();
            int totaalPers = 0;
            Double totaalPrijs = 0.0;
            for (Bestelling bestelling : tblHoofd.getItems()) {
                totaalPers += bestelling.getAantal();
                totaalPrijs += bestelling.getGerecht().getPrijs().doubleValue() * bestelling.getAantal();
            }
            lblTotaalPers.setText(totaalPers + "");
            lblTotaalPrijs.setText("\u20ac" + totaalPrijs);
        }

    }

    @Override
    public <T> void initdata(T dataToInit) {
        if (dataToInit instanceof Klant) {
            initData = (Klant) dataToInit;
            String naam = ((Klant) dataToInit).getNaam();
            String groep = ((Klant) dataToInit).getGroepsNaam();
            txtNaam.setText(naam);
            txtGroepsnaam.setText(groep);
        }
        try {
            tblHoofd.setItems(getBestellingen(true));
            tblDessert.setItems(getBestellingen(false));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "SQLException", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        cbxTijdstip.getSelectionModel().select(initData.getTijdstip());
        int totPers = 0;
        Double totPrijs = 0.0;
        for (Bestelling best :
                tblHoofd.getItems()) {
            totPers += best.getAantal();
            totPrijs += best.getGerecht().getPrijs().doubleValue() * best.getAantal();
        }
        lblTotaalPrijs.setText("€" + totPrijs);
        lblTotaalPers.setText("" + totPers);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colHoofdGerecht.setCellValueFactory(new PropertyValueFactory<>("Gerecht"));
        colHoofdgerechtAantal.setCellValueFactory(new PropertyValueFactory<>("Aantal"));
        colTotaal.setCellValueFactory(new PropertyValueFactory<Bestelling, Double>("Prijs") {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Bestelling, Double> param) {
                return new SimpleDoubleProperty(param.getValue().getGerecht().getPrijs().get() * param.getValue().getAantal()).asObject();
            }
        });

        colHoofdGerecht.setCellFactory(column -> new TableCell<Bestelling, Gerecht>() {
            @Override
            protected void updateItem(Gerecht item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.getNaam() + " X€" + item.getPrijs().get());
                }
            }
        });
        colDessert.setCellValueFactory(colHoofdGerecht.getCellValueFactory());
        colDessert.setCellFactory(column -> new TableCell<Bestelling, Gerecht>() {
            @Override
            protected void updateItem(Gerecht item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.getNaam());
                }
            }
        });
        colDessertAantal.setCellValueFactory(colHoofdgerechtAantal.getCellValueFactory());

        tblDessert.setEditable(true);
        tblHoofd.setEditable(true);
        colDessertAantal.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colHoofdgerechtAantal.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        try {
            tblHoofd.setItems(getBestellingen(true));
            tblDessert.setItems(getBestellingen(false));
            cbxTijdstip.setItems(tijdstippen());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (initData == null) {
            cbxTijdstip.getSelectionModel().select(0);
        }
        tblHoofd.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                tblHoofd.edit(tblHoofd.getSelectionModel().getSelectedIndex(), colHoofdgerechtAantal);
            }
            txtNaam.focusTraversableProperty().setValue(true);
            txtGroepsnaam.focusTraversableProperty().setValue(true);
            cbxTijdstip.focusTraversableProperty().setValue(true);
            tblHoofd.focusTraversableProperty().setValue(true);
        });
        tblDessert.setOnKeyReleased(event -> {
            System.out.println("fire");
            if (event.getCode() == KeyCode.ENTER) {
                tblDessert.edit(tblDessert.getSelectionModel().getSelectedIndex(), colDessertAantal);
            }
            txtNaam.focusTraversableProperty().setValue(true);
            txtGroepsnaam.focusTraversableProperty().setValue(true);
            cbxTijdstip.focusTraversableProperty().setValue(true);
            tblHoofd.focusTraversableProperty().setValue(true);
        });

    }

    public void Overzicht(Event event) {
        try {
            if (addKlant()) {
                try {
                    HelperMethods.ChangeScene(event, "be/sint_andries/view/KlantOverviewView.fxml");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Could not find KlantOverviewView.fxml", "IOException", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "SQLException", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    public void Volgende(Event e) throws IOException, SQLException {
        if (addKlant()) {
            HelperMethods.ChangeScene(e, "be/sint_andries/view/AddKlantView.fxml");
        }
    }

    private ObservableList<Tijdstip> tijdstippen() throws SQLException {
        ObservableList<Tijdstip> tijden = FXCollections.observableArrayList();
        PreparedStatement preptijden = Main.connection.prepareStatement("SELECT Uur, Minuut, Id FROM Tijdstip WHERE RestaurantdagId = 1");
        //preptijden.setInt(1, getRestoId());
        ResultSet rsTijd = preptijden.executeQuery();
        while (rsTijd.next()) {
            tijden.add(new Tijdstip(rsTijd.getInt(3), rsTijd.getShort(1), rsTijd.getShort(2)));
        }
        return tijden;
    }

    private boolean addKlant() throws SQLException {
        if (check_everything()) {
            if (initData == null) {
                PreparedStatement prep = Main.connection.prepareStatement("INSERT INTO Klant VALUES (?, ?, ?, ?, ?)");
                String naam = txtNaam.getText();
                int tid = cbxTijdstip.getSelectionModel().getSelectedItem().getId();
                prep.setInt(5, tid);
                prep.setString(2, naam);
                if (txtGroepsnaam.getText() != null || !Objects.equals(txtGroepsnaam.getText(), "")) {
                    prep.setString(3, txtGroepsnaam.getText());
                }
                prep.setInt(4, getRestoId());
                //insert new Klant
                int insert = prep.executeUpdate();
                if (insert != 1) {
                    throw new SQLException("not inserted");
                }
                ResultSet rsKlant = Main.connection.prepareStatement("SELECT Id FROM Klant ORDER BY Id DESC ").executeQuery();
                rsKlant.next();
                int klantId = rsKlant.getInt("Id");
                insertBestellingen(tblHoofd.getItems(), klantId);
                insertBestellingen(tblDessert.getItems(), klantId);
            } else {
                PreparedStatement prepUpdateKlant = Main.connection.prepareStatement("UPDATE Klant SET naam = ?, GroepsNaam = ?, TijdstipId = ? WHERE Id = ?");
                prepUpdateKlant.setString(1, txtNaam.getText());
                if (txtGroepsnaam.getText() != null || !Objects.equals(txtGroepsnaam.getText(), "")) {
                    prepUpdateKlant.setString(2, txtGroepsnaam.getText());
                }
                prepUpdateKlant.setInt(3, cbxTijdstip.getSelectionModel().getSelectedItem().getId());
                prepUpdateKlant.setInt(4, initData.getId());
                System.out.println(prepUpdateKlant.executeUpdate());
                updateBestelling(tblHoofd);
                updateBestelling(tblDessert);
            }
            return true;
        }
        return false;
    }

    private void updateBestelling(TableView<Bestelling> tableView) {
        PreparedStatement prepUpdateBestelling = null;
        try {
            prepUpdateBestelling = Main.connection.prepareStatement("UPDATE GerechtBestelling SET Aantal = ? WHERE BestellingsId = ?");
            for (Bestelling b :
                    tableView.getItems()) {
                if (b.getId() != null) {
                    prepUpdateBestelling.setInt(1, b.getAantal());
                    prepUpdateBestelling.setInt(2, b.getId());
                    prepUpdateBestelling.executeUpdate();
                } else if (b.getAantal() != 0) {
                    ObservableList<Bestelling> best = FXCollections.observableArrayList();
                    best.add(b);
                    insertBestellingen(best, initData.getId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void insertBestellingen(ObservableList<Bestelling> bestellings, int klantId) {
        PreparedStatement prepBestelling = null;
        try {
            prepBestelling = Main.connection.prepareStatement("INSERT INTO GerechtBestelling VALUES (?,NULL, ?, ?)");
            for (Bestelling g :
                    bestellings) {
                if (g.getAantal() != 0) {
                    prepBestelling.setInt(1, g.getGerecht().getId());
                    prepBestelling.setInt(2, klantId);
                    prepBestelling.setInt(3, g.getAantal());
                    prepBestelling.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getRestoId() throws SQLException {
        return getResto().getId();
    }

    private Restaurantdag getResto() throws SQLException {

        if (initData != null) {
            PreparedStatement prepInint = Main.connection.prepareStatement("SELECT * FROM Restaurantdag WHERE Id = ?");
            prepInint.setInt(1, initData.getId());
            ResultSet rsInit = prepInint.executeQuery();
            if (rsInit.next()) {
                int id = rsInit.getInt(1);
                String naam = rsInit.getString(2);
                LocalDate Datum = rsInit.getDate(3).toLocalDate();
                return new Restaurantdag(Datum, naam, id);
            }
        }
        ResultSet rs = Main.connection.prepareStatement("SELECT * FROM Restaurantdag ORDER BY Datum DESC").executeQuery();
        rs.next();
        int id = rs.getInt(1);
        String naam = rs.getString(2);
        LocalDate datum = rs.getDate(3).toLocalDate();
        return new Restaurantdag(datum, naam, id);
    }

    private ObservableList<Bestelling> getBestellingen(boolean hoofdegercht) throws SQLException {
        ObservableList<Bestelling> bestellingen = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = Main.connection.prepareStatement("SELECT GerechtId FROM RestaurantdagGerechten WHERE RestaurantdagId = ?");
        preparedStatement.setInt(1, getRestoId());
        ResultSet rs = preparedStatement.executeQuery();
        PreparedStatement preparedStatement1 = Main.connection.prepareStatement("SELECT * FROM Gerecht WHERE Id = ? AND IsDessert = ?");
        preparedStatement1.setBoolean(2, !hoofdegercht);
        while (rs.next()) {
            //System.out.println(rs.getString("Naam"));
            preparedStatement1.setInt(1, rs.getInt(1));
            ResultSet rsGerecht = preparedStatement1.executeQuery();
            while (rsGerecht.next()) {
                String naam = rsGerecht.getString("Naam");
                int id = rsGerecht.getInt("Id");
                if (!rsGerecht.getBoolean("IsDessert")) {
                    Double prijs = rsGerecht.getDouble("Prijs");
                    System.out.println(prijs);
                    Gerecht ger = new Gerecht(naam, prijs, rsGerecht.getBoolean("IsKind"), id);
                    if (initData != null) {
                        bestellingen.add(new Bestelling(ger, 0, initData.getId()));
                    } else {
                        bestellingen.add(new Bestelling(ger, 0));
                    }
                } else {
                    Gerecht ger = new Gerecht(naam, id);
                    if (initData != null) {
                        bestellingen.add(new Bestelling(ger, 0, initData.getId()));
                    } else {
                        bestellingen.add(new Bestelling(ger, 0));
                    }
                }
            }

        }
        System.out.println(initData);
        if (initData != null) {

            PreparedStatement prepbestellingpersoonlijk = Main.connection.prepareStatement("SELECT GerechtId, Aantal, BestellingsId FROM GerechtBestelling WHERE KlantId = ?");
            prepbestellingpersoonlijk.setInt(1, initData.getId());
            preparedStatement1.setBoolean(2, !hoofdegercht);
            ResultSet rsBestellingPersoonlijk = prepbestellingpersoonlijk.executeQuery();

            while (rsBestellingPersoonlijk.next()) {
                Bestelling b;
                int gerechtId = rsBestellingPersoonlijk.getInt(1);
                int aantal = rsBestellingPersoonlijk.getInt(2);
                int bestellingsId = rsBestellingPersoonlijk.getInt(3);
                preparedStatement1.setInt(1, gerechtId);
                ResultSet rsGerechtPersoonlijk = preparedStatement1.executeQuery();
                while (rsGerechtPersoonlijk.next()) {
                    int id = rsGerechtPersoonlijk.getInt(1);
                    String naam = rsGerechtPersoonlijk.getString(2);
                    if (rsGerechtPersoonlijk.getBoolean(3)) {
                        Gerecht ger = new Gerecht(naam, id);
                        b = new Bestelling(ger, aantal, initData.getId(), bestellingsId);
                        bestellingen.removeIf(best -> best.getGerecht().equals(ger));
                        bestellingen.add(b);
                    } else {
                        Gerecht ger = new Gerecht(naam, rsGerechtPersoonlijk.getDouble(5), rsGerechtPersoonlijk.getBoolean(4), id);
                        b = new Bestelling(ger, aantal, initData.getId(), bestellingsId);
                        bestellingen.removeIf(best -> best.getGerecht().equals(ger));
                        bestellingen.add(b);
                        for (Bestelling best :
                                bestellingen) {
                            System.out.println(best.getGerecht().getNaam() + " " + best.getAantal());
                        }
                    }
                }
            }
        }
        return bestellingen;
    }

    private String fetchGroep(String naam) throws SQLException {
        PreparedStatement preparedStatement = Main.connection.prepareStatement("SELECT Groepsnaam FROM Klant WHERE Naam LIKE '" + naam + "%' ORDER BY Id DESC ");
        //preparedStatement.setString(1, naam);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            if (Objects.equals(txtNaam.getText(), "") || txtNaam.getText() == null) {
                return "";
            } else {
                return resultSet.getString(1);
            }
        } else {

            System.out.println(txtNaam.getText() + "bla");
            return txtNaam.getText();

        }

    }

    public void fillgroepsnaam() {
        new Thread(() -> {

            try {
                long current = System.currentTimeMillis();
                txtGroepsnaam.setText(fetchGroep(txtNaam.getText()));
                System.out.println("fetching from db took " + (System.currentTimeMillis() - current) + " ms");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "SQLException", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        }).start();

    }

    private boolean check_everything() {
        txtNaam.setStyle("");
        txtGroepsnaam.setStyle("");
        tblDessert.setStyle("");
        tblHoofd.setStyle("");
        System.out.println("anyone there???");
        int fouten = 0;
        if (txtNaam.getText().equals("")) {
            System.out.println("in here");
            txtNaam.setStyle("-fx-border-color: red; -fx-border-width: 3px;");
            fouten++;
            new Thread(() -> {
                JOptionPane.showMessageDialog(null, "er is geen naam ingegeven", "Fout", JOptionPane.WARNING_MESSAGE);
            });
        }

        if (txtGroepsnaam.getText().equals("")) {
            txtGroepsnaam.setStyle("-fx-border-color: red;-fx-border-width: 3px");
            fouten++;
            new Thread(() -> {
                JOptionPane.showMessageDialog(null, "er is geen groepsnaam ingegeven", "Fout", JOptionPane.WARNING_MESSAGE);
            });
        }

        int aantalHoofd = 0;
        for (Bestelling bestelling : tblHoofd.getItems()) {
            aantalHoofd += bestelling.getAantal();
        }
        int aantaldes = 0;
        for (Bestelling bestelling : tblDessert.getItems()) {
            aantaldes += bestelling.getAantal();
        }
        if (aantalHoofd == 0) {
            tblHoofd.setStyle("-fx-border-color: red; -fx-border-width: 3px;");
            fouten++;
            new Thread(() -> {
                JOptionPane.showMessageDialog(null, "er zijn geen hoofdgerechten ingegeven", "Fout", JOptionPane.WARNING_MESSAGE);
            });
        }
        if (aantaldes == 0) {
            tblDessert.setStyle("-fx-border-color: red; -fx-border-width: 3px;");
            fouten++;
            new Thread(() -> {
                JOptionPane.showMessageDialog(null, "er zijn geen desserten ingegeven", "Fout", JOptionPane.WARNING_MESSAGE);
            });
        }
        if (aantaldes != aantalHoofd) {
            tblHoofd.setStyle("-fx-border-color: red; -fx-border-width: 3px;");
            tblDessert.setStyle("-fx-border-color: red; -fx-border-width: 3px;");
            fouten++;
            new Thread(() -> {
                JOptionPane.showMessageDialog(null, "het aantal desserten is niet gelijk aan het aantal hoofdgerechten", "Fout", JOptionPane.WARNING_MESSAGE);
            });
        }
        return !(fouten > 0);
    }
}