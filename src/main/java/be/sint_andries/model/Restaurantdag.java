package be.sint_andries.model;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Restaurantdag {
    private int id;
    private List<Tijdstip> tijdstippen;
    private SimpleStringProperty naam;
    private List<Klant> klanten;
    private HashSet<Gerecht> hoofdgerechten;
    private HashSet<Gerecht> desserts;
    private LocalDate datum;

    public Restaurantdag(LocalDate datum, String naam) {
        this.naam = new SimpleStringProperty(naam);
        this.datum = datum;
        this.hoofdgerechten = new HashSet<>();
        this.desserts = new HashSet<>();
        this.klanten = new ArrayList<>();
    }

    public Restaurantdag(LocalDate datum, String naam, int id) {
        this(datum, naam);
        this.id = id;
    }

    public Restaurantdag(LocalDate datum, String naam, int id, List<Tijdstip> tijdstippen){
        this(datum, naam, id);
        this.tijdstippen = tijdstippen;
    }

    public List<Klant> getKlanten() {
        return klanten;
    }

    public void addKlant(Klant klant) {
        this.klanten.add(klant);
    }

    public void addGerecht(Gerecht gerecht) {
        if (gerecht.isDessert().getValue()) {
            this.desserts.add(gerecht);
        } else {
            this.hoofdgerechten.add(gerecht);
        }

    }


    public HashSet<Gerecht> getHoofdgerechten() {
        return hoofdgerechten;
    }

    public HashSet<Gerecht> getDesserts() {
        return desserts;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public int getId() {
        return this.id;
    }

    public SimpleStringProperty getNaam() {
        return naam;
    }

    public SimpleStringProperty naamProperty() {
        return naam;
    }
}
