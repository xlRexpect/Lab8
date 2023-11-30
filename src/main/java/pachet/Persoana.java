package pachet;

public class Persoana
{
    private int id_persoana;

    private String nume;

    private int varsta;


    public Persoana(int id_persoana, String nume, int varsta)
    {
        this.id_persoana = id_persoana;
        this.nume = nume;
        this.varsta = varsta;
    }

    public int getId_persoana() {
        return id_persoana;
    }

    public void setId_persoana(int id_persoana) {
        this.id_persoana = id_persoana;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }


    @Override
    public String toString()
    {
        return
                 id_persoana +
                ", " +   nume + ", " +
                  varsta;
    }
}
