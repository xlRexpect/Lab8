package pachet;

public class Excursie
{
    private int id_excursie;

    private int id_perosana;

    private String destinatie;

    private int an;

    public Excursie(int id_excursie, int id_perosana, String destinatie, int an) {
        this.id_excursie = id_excursie;
        this.id_perosana = id_perosana;
        this.destinatie = destinatie;
        this.an = an;
    }

    public int getId_excursie() {
        return id_excursie;
    }

    public void setId_excursie(int id_excursie) {
        this.id_excursie = id_excursie;
    }

    public int getId_perosana() {
        return id_perosana;
    }

    public void setId_perosana(int id_perosana) {
        this.id_perosana = id_perosana;
    }

    public String getDestinatie() {
        return destinatie;
    }

    public void setDestinatie(String destinatie) {
        this.destinatie = destinatie;
    }

    public int getAn() {
        return an;
    }

    public void setAn(int an) {
        this.an = an;
    }

    @Override
    public String toString() {
        return
                  id_excursie  + ", "
                 + id_perosana + ", "
                 + destinatie  + ", "
                 + an;

    }
}


