package be.sint_andries.model;

public class Bestelling implements Comparable<Bestelling> {
    private Integer id = null;
    private int klantId;
    private Gerecht Gerecht;
    private int Aantal;

    /**
     * constructor to initialize the HashMap's
     */
    public Bestelling(Gerecht gerecht, int aantal) {
        this.Gerecht = gerecht;
        this.Aantal = aantal;
    }

    public Bestelling(Gerecht gerecht, int aantal, int klantId) {
        this(gerecht, aantal);
        this.klantId = klantId;
    }

    public Bestelling(Gerecht gerecht, int aantal, int klantId, int id) {
        this(gerecht, aantal, klantId);
        this.id = id;
    }

    public int getKlantId() {
        return klantId;
    }

    public be.sint_andries.model.Gerecht getGerecht() {
        return Gerecht;
    }

    public int getAantal() {
        return Aantal;
    }

    public void setAantal(int aantal) {
        Aantal = aantal;
    }

    @Override
    public int compareTo(Bestelling o) {
        if (this.getAantal() > o.getAantal() && this.Gerecht == o.getGerecht()) {
            return 1;
        } else {
            return Integer.compare(this.getGerecht().getId(), o.getGerecht().getId());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Bestelling) {
            return this.getGerecht() == ((Bestelling) o).getGerecht();
        } else {
            return false;
        }
    }

    public Integer getId() {
        return id;
    }
}
