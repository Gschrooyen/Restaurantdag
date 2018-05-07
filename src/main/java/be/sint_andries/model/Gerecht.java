package be.sint_andries.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Gerecht implements Comparable {
    private int Id;
    private String naam = "generic_gerecht";
    private SimpleDoubleProperty prijs = new SimpleDoubleProperty(0.0);
    private SimpleBooleanProperty isDessert = new SimpleBooleanProperty(false), isKindGerecht = new SimpleBooleanProperty(false);

    public Gerecht(String naam) {
        this.naam = naam;
        this.isDessert = new SimpleBooleanProperty(true);
    }

    public Gerecht(String naam, int Id) {
        this(naam);
        this.Id = Id;
    }

    public Gerecht(String naam, Double prijs, Boolean isKind) {
        this.naam = naam;
        this.isKindGerecht = new SimpleBooleanProperty(isKind);
        this.isDessert = new SimpleBooleanProperty(false);
        this.prijs = new SimpleDoubleProperty(prijs);
    }

    public Gerecht(String naam, Double prijs, Boolean isKind, int Id) {
        this(naam, prijs, isKind);
        this.Id = Id;
        this.isDessert = new SimpleBooleanProperty(false);
        this.prijs = new SimpleDoubleProperty(prijs);
    }

    public Gerecht(Gerecht gerecht, Double newPrijs){
        this.isDessert = new SimpleBooleanProperty(false);
        this.isKindGerecht = gerecht.isKindGerecht;
        this.naam = gerecht.naam;
        this.prijs = new SimpleDoubleProperty(newPrijs);
    }
    public String getNaam() {
        return naam;
    }

    public SimpleDoubleProperty getPrijs() {
        return prijs;
    }

    public SimpleBooleanProperty isDessert() {
        return isDessert;
    }

    public int getId() {
        return Id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Gerecht) {

            return ((this.getId() == ((Gerecht) obj).getId()) && (this.naam.equals(((Gerecht) obj).naam)));
        }
        return false;
    }

    @Override
    public String toString() {
        return this.naam;
    }

    public SimpleBooleanProperty isKind() {
        return this.isKindGerecht;
    }


    public int compareTo(Object o) {
        if (o instanceof Gerecht){
            if (this.isDessert.get() && ((Gerecht) o).isDessert.get()){
                return this.naam.compareTo(((Gerecht) o).naam);
            }
            else if (!this.isDessert.get() && ((Gerecht) o).isDessert.get()){
                return -1;
            }else if (this.isDessert.get() && !((Gerecht) o).isDessert.get()){
                return 1;
            }else if (!this.isDessert.get() && !((Gerecht) o).isDessert.get()){
                if (this.isKind().get() && ((Gerecht) o).isKind().get()){
                    return this.naam.compareTo(((Gerecht) o).naam);
                }else if (!this.isKind().get() && ((Gerecht) o).isKind().get()){
                    return -1;
                }else if (this.isKind().get() && !((Gerecht) o).isKind().get()){
                    return 1;
                }else if (!this.isKind().get() && ((Gerecht) o).isKind().get()){
                    return this.naam.compareTo(((Gerecht) o).naam);
                }
            }
        }
        return 1;
    }
}
