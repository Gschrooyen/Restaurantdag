package be.sint_andries.model;

public class Tijdstip implements Comparable {
    private int id;
    private short uur;
    private short minuut;


    public Tijdstip(int id, short uur, short minuut) {
        this.id = id;
        this.uur = uur;
        this.minuut = minuut;
    }

    public Tijdstip(short uur, short minuut) {
        this.uur = uur;
        this.minuut = minuut;
    }

    @Override
    public String toString() {
        if (this.minuut == 0) {
            return uur + ":" + "00";
        }
        return uur + ":" + minuut;
    }

    public int getId() {
        return id;
    }

    public short getUur() {
        return uur;
    }

    public short getMinuut() {
        return minuut;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Tijdstip){
            if (this.uur > ((Tijdstip) o).uur){
                return 1;
            }else if (((Tijdstip) o).uur > this.uur){
                return -1;
            }else if (this.uur == ((Tijdstip) o).uur){
                if (this.minuut > ((Tijdstip) o).minuut){
                    return 1;
                }else if (((Tijdstip) o).minuut > this.minuut){
                    return -1;
                }else if (((Tijdstip) o).minuut == this.minuut){
                    return Integer.compare(((Tijdstip) o).minuut, this.getMinuut());
                }
            }
        }
        return 1;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(Integer.hashCode(uur) + Integer.hashCode(minuut));
    }
}
