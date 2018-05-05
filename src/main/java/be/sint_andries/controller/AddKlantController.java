package be.sint_andries.controller;


import be.sint_andries.Main;
import be.sint_andries.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;


public class AddKlantController extends Controller {

    public TableView<Bestelling> tblDessert;
    public TableColumn<Bestelling, Gerecht> colDessert;
    public TableColumn<Bestelling, Integer> colDessertAantal;
    public ComboBox<Tijdstip> cbxTijdstip;
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

    public void Back(Event event) throws IOException, SQLException {
        HelperMethods.ChangeSceneWithData(event, "be/sint_andries/view/KlantOverviewView.fxml", getResto());

    }

    public void edit(TableColumn.CellEditEvent e) {
        Bestelling b = (Bestelling) e.getTableView().getSelectionModel().getSelectedItem();
        System.out.println(b);
        System.out.println(e.getNewValue());
        b.setAantal((Integer) e.getNewValue());
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
            e.printStackTrace();
        }
        cbxTijdstip.getSelectionModel().select(initData.getTijdstip());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colHoofdGerecht.setCellValueFactory(new PropertyValueFactory<>("Gerecht"));
        colHoofdgerechtAantal.setCellValueFactory(new PropertyValueFactory<>("Aantal"));
        colHoofdGerecht.setCellFactory(column -> new TableCell<Bestelling, Gerecht>() {
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
        colDessert.setCellValueFactory(colHoofdGerecht.getCellValueFactory());
        colDessert.setCellFactory(colHoofdGerecht.getCellFactory());
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

    }

    public void Overzicht(Event event) throws IOException, SQLException {
        addKlant();
        HelperMethods.ChangeScene(event, "be/sint_andries/view/KlantOverviewView.fxml");
    }

    public void Volgende(Event e) throws IOException, SQLException {
        addKlant();
        HelperMethods.ChangeScene(e, "be/sint_andries/view/AddKlantView.fxml");
    }

    private ObservableList<Tijdstip> tijdstippen() throws SQLException {
        ObservableList<Tijdstip> tijden = FXCollections.observableArrayList();
        PreparedStatement preptijden = Main.connection.prepareStatement("SELECT Uur, Minuut, Id FROM Tijdstip where RestaurantdagId = ?");
        preptijden.setInt(1, getRestoId());
        ResultSet rsTijd = preptijden.executeQuery();
        while (rsTijd.next()) {
            tijden.add(new Tijdstip(rsTijd.getInt(3), rsTijd.getShort(1), rsTijd.getShort(2)));
        }
        return tijden;
    }

    private void addKlant() throws SQLException {

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

    }

    private void updateBestelling(TableView<Bestelling> tableView) throws SQLException {
        PreparedStatement prepUpdateBestelling = Main.connection.prepareStatement("UPDATE GerechtBestelling SET Aantal = ? where BestellingsId = ?");
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
    }

    private void insertBestellingen(ObservableList<Bestelling> bestellings, int klantId) throws SQLException {
        PreparedStatement prepBestelling = Main.connection.prepareStatement("INSERT INTO GerechtBestelling VALUES (?,null, ?, ?)");
        for (Bestelling g :
                bestellings) {
            if (g.getAantal() != 0) {
                prepBestelling.setInt(1, g.getGerecht().getId());
                prepBestelling.setInt(2, klantId);
                prepBestelling.setInt(3, g.getAantal());
                prepBestelling.executeUpdate();
            }
        }
    }

    private int getRestoId() throws SQLException {
        return getResto().getId();
    }

    private Restaurantdag getResto() throws SQLException {
        if (initData != null) {
            PreparedStatement prepInint = Main.connection.prepareStatement("SELECT * FROM Restaurantdag where Id = ?");
            prepInint.setInt(1, initData.getId());
            ResultSet rsInit = prepInint.executeQuery();
            rsInit.next();
                int id = rsInit.getInt(1);
                String naam = rsInit.getString(2);
                LocalDate Datum = rsInit.getDate(3).toLocalDate();
                return new Restaurantdag(Datum, naam, id);

        }
        ResultSet rs = Main.connection.prepareStatement("SELECT * FROM Restaurantdag order by Datum desc").executeQuery();
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
                if (rsGerecht.getBoolean("IsDessert")) {
                    Double prijs = rsGerecht.getDouble("Prijs");
                    Gerecht ger = new Gerecht(naam, prijs, rsGerecht.getBoolean("IsKind") ,id);
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
                        Gerecht ger = new Gerecht(naam, rsGerechtPersoonlijk.getDouble(4), rsGerechtPersoonlijk.getBoolean(5),id);
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
}