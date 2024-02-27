package Domain;

import java.io.Serializable;
import java.util.Objects;

public class Masina extends Entity implements Serializable {
    private String marca;
    private String model;
    public Masina(int id, String brand, String model){
        super(id);
        this.marca = brand;
        this.model = model;
    }
    public String getMarca(){
        return this.marca;
    }

    public String getModel(){
        return this.model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Masina masina = (Masina) o;
        return Objects.equals(marca, masina.marca) && Objects.equals(model, masina.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(marca, model);
    }

    public void setMarca(String m){
        this.marca = m;
    }

    public void setModel(String model){
        this.model = model;
    }

    public String toString(){
        return "ID:"+getId()+" ,marca:"+getMarca()+" ,model:"+getModel();
    }
}

