package Domain;

import java.util.Objects;

public class Inchiriere extends Entity {
    private final Masina car;
    private String dateIn;
    private String dateOut;
    public Inchiriere(int id, Masina car, String dateIn, String dateOut){
        super(id);
        this.car = car;
        this.dateIn = dateIn;
        this.dateOut = dateOut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inchiriere that = (Inchiriere) o;
        return Objects.equals(car, that.car) && Objects.equals(dateIn, that.dateIn) && Objects.equals(dateOut, that.dateOut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(car, dateIn, dateOut);
    }

    public Masina getCar() {
        return car;
    }

    public String getDateIn() {
        return dateIn;
    }

    public void setDateIn(String dateIn) {
        this.dateIn = dateIn;
    }

    public void setDateOut(String dateOut) {
        this.dateOut = dateOut;
    }

    public String getDateOut(){
        return this.dateOut;
    }

    public String toString(){
        return "ID inchiriere:"+getId()+", Masina cu id-ul:"+getCar().getId()+
                ", date in:"+getDateIn()+", date out:"+getDateOut();
    }
}
