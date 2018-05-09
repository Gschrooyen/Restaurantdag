package be.sint_andries.controller;

import be.sint_andries.Main;
import be.sint_andries.model.Gerecht;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

// TODO: 29.03.18 functie voor toevoegen en volgende

public class AddNewGerechtViewController extends Controller {

    private ParentViewController parentViewController;
    @FXML
    public TextField txtPrijs;
    @FXML
    private TextField txtNaam;
    @FXML
    public CheckBox cbxDessert;
    @FXML
    private CheckBox cbxKind;
    private Gerecht initgerecht = null;
    private Gerecht laatsteGerecht;

    public void Back(Event event) throws IOException {
        HelperMethods.ChangeScene(event, "be/sint_andries/view/GerechtOverzichtView.fxml");
    }

    public void Overzicht(Event event) throws IOException {
        Opslaan();
        if (laatsteGerecht.isDessert().get()) {
            parentViewController.addDessert(laatsteGerecht);
        } else {
            parentViewController.addHoofdGerecht(laatsteGerecht);
        }

    }

    public void Volgende(Event event) throws IOException {
        Opslaan();
        HelperMethods.ChangeScene(event, "be/sint_andries/view/AddNewGerechtView.fxml");
    }

    private void Opslaan() {
        if (initgerecht == null) {
            try {
                PreparedStatement prep = Main.connection.prepareStatement("INSERT INTO Gerecht VALUES (?,?,?,?, ?)");
                prep.setString(2, txtNaam.getText());
                if (!cbxKind.isSelected()) {
                    prep.setInt(3, cbxDessert.isSelected() ? 1 : 0);
                } else {
                    prep.setInt(3, 0);
                }
                prep.setInt(4, cbxKind.isSelected() ? 1 : 0);
                if (!cbxDessert.isSelected()) {
                    prep.setDouble(5, Double.parseDouble(txtPrijs.getText()));
                }
                prep.execute();

                //get the newly added dish from the databse so we know the ID.
                prep = Main.connection.prepareStatement("SELECT * FROM Gerecht WHERE Naam = ? AND Prijs = ? AND IsKind = ?");
                prep.setString(1, txtNaam.getText());
                if (!cbxDessert.isSelected()){
                    prep.setDouble(2, Double.parseDouble(txtPrijs.getText()));
                }
                prep.setBoolean(3, cbxKind.isSelected());
                ResultSet rs = prep.executeQuery();

                if (rs.next()){
                    if (rs.getBoolean("IsDessert")){
                        laatsteGerecht = new Gerecht(rs.getString("Naam"), rs.getInt("Id"));
                    }else {
                        laatsteGerecht = new Gerecht(rs.getString("Naam"), rs.getDouble("Prijs"), rs.getBoolean("IsKind"), rs.getInt("Id"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            try {
                PreparedStatement prep = Main.connection.prepareStatement("UPDATE Gerecht SET Naam = ?, IsDessert = ?, Prijs = ?, IsKind = ? WHERE Id = ?");
                prep.setString(1, txtNaam.getText());
                prep.setInt(4, cbxKind.isSelected() ? 1 : 0);
                System.out.println(cbxKind.isSelected());
                if (!cbxKind.isSelected()) {
                    prep.setInt(2, cbxDessert.isSelected() ? 1 : 0);
                } else {
                    prep.setInt(2, 0);
                }

                if (!cbxDessert.isSelected()) {
                    prep.setDouble(3, Double.parseDouble(txtPrijs.getText()));
                }
                prep.setInt(5, initgerecht.getId());
                System.out.println(initgerecht.getId());
                System.out.println(prep);
                System.out.println(prep.executeUpdate());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void enablePrijs() {
        if (cbxDessert.isSelected()) {
            txtPrijs.setDisable(true);
        } else {
            txtPrijs.setDisable(false);
        }
    }


    @Override
    public <T> void initdata(T dataToInit) {
        if (dataToInit instanceof Gerecht) {
            initgerecht = (Gerecht) dataToInit;
            if (initgerecht.isDessert().getValue() | initgerecht.getPrijs() == null) {
                txtPrijs.setText("");
                txtPrijs.setDisable(true);
            } else {
                txtPrijs.setText(String.valueOf(initgerecht.getPrijs().getValue()));
            }
            cbxDessert.setSelected(initgerecht.isDessert().getValue());
            cbxKind.setSelected(initgerecht.isKind().getValue());
            txtNaam.setText(initgerecht.getNaam());
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    void init(ParentViewController parentViewController) {
        this.parentViewController = parentViewController;
    }
}
