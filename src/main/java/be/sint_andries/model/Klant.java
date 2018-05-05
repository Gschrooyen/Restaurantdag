package be.sint_andries.model;

import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

public class Klant {
    private Tijdstip tijdstip = null;
    private Integer id = null;
    private SimpleStringProperty naam = null, groepsNaam = null;
    private ArrayList<Bestelling> bestelling = null;
    private Integer aantalPers;


    /**
     * constructor without group
     *
     * @param naam person name
     */
    public Klant(String naam) {
        this.naam = new SimpleStringProperty(naam);
        this.bestelling = new ArrayList<>();

    }
    public Klant(String naam, Tijdstip tijdstip){
        this.naam = new SimpleStringProperty(naam);
        this.tijdstip = tijdstip;
    }

    public Klant(String naam, String groepsnaam, Tijdstip tijdstip){
        bestelling = new ArrayList<>();
        this.naam = new SimpleStringProperty(naam);
        this.groepsNaam = new SimpleStringProperty(groepsnaam);
        this.tijdstip = tijdstip;
    }
    /**
     * @param naam person name
     * @param id   person klantId in db
     */
    public Klant(String naam, int id) {
        this(naam);
        this.id = id;
    }

    /**
     * constructort with group
     *
     * @param naam       person name
     * @param groepsnaam group name
     */
    public Klant(String naam, String groepsnaam) {
        this(naam);
        this.groepsNaam = new SimpleStringProperty(groepsnaam);
    }

    /**
     * @param naam       person name
     * @param groepsnaam group name
     * @param id         person klantId in db
     */
    public Klant(String naam, String groepsnaam, int id) {
        this(naam, groepsnaam);
        this.id = id;
    }

    public Klant(String naam, String groepsnaam, int id, ArrayList<Bestelling> bestelling) {
        this(naam, groepsnaam, id);
        this.bestelling = bestelling;
    }

    public Klant(Tijdstip tijdstip, Integer id, String naam, String groepsNaam, ArrayList<Bestelling> bestelling) {
        this(naam, groepsNaam, id, bestelling);
        this.tijdstip = tijdstip;
    }

    public Klant(Tijdstip tijdstip, Integer id, String naam, String groepsNaam) {
        this(naam, groepsNaam, id);
        this.tijdstip = tijdstip;
    }

    public Klant(Tijdstip tijdstip, Integer id, String naam) {
        this(naam, id);
        this.tijdstip = tijdstip;
    }

    public Klant(Tijdstip tijdstip, String naam, String groepsNaam) {
        this(naam, groepsNaam);
        this.tijdstip = tijdstip;
    }

    public Klant(Tijdstip tijdstip, String naam) {
        this(naam);
        this.tijdstip = tijdstip;
    }

    /**
     * adds order to person
     *
     * @param bestelling the order to add
     */
    public void addBestelling(Bestelling bestelling) {
        this.bestelling.add(bestelling);
    }

    public String getNaam() {
        return naam.get();
    }

    public void setNaam(String naam) {
        this.naam.set(naam);
    }

    public Integer getAantalPers() {
        return telAantal();
    }

    private Integer telAantal() {
        int totaal = 0;
        if (bestelling != null) {
            for (Bestelling b :
                    this.bestelling) {
                if (!b.getGerecht().isDessert().getValue()) {
                    totaal += b.getAantal();
                }
            }
        }
        return totaal;
    }

    public SimpleStringProperty naamProperty() {
        return naam;
    }

    public String getGroepsNaam() {
        if (groepsNaam != null){
            return groepsNaam.get();
        }else {
            return null;
        }
    }

    public void setGroepsNaam(String groepsNaam) {
        this.groepsNaam.set(groepsNaam);
    }

    public SimpleStringProperty groepsNaamProperty() {
        return groepsNaam;
    }

    public Integer getId() {
        return id;
    }

    public ArrayList<Bestelling> getBestelling() {
        return bestelling;
    }


    @Override
    public String toString() {
        return super.toString();
    }

    public Tijdstip getTijdstip() {
        return tijdstip;
    }

    public Double getPrijs(){
        Double prijs = 0.0;
        if (bestelling != null) {
            for (Bestelling b :
                    bestelling) {
                prijs += b.getGerecht().getPrijs().doubleValue();
            }
        }
        return prijs;
    }

    public int getKinderen(){
        int totaal = 0;
        if (bestelling != null) {
            for (Bestelling b :
                    bestelling) {
                if (b.getGerecht().isKind().getValue()) {
                    totaal += b.getAantal();
                }
            }
        }
        return totaal;
    }

    public int getVolwassenen(){
        return getAantalPers() - getKinderen();
    }
}
