public class ChangeEntry {
    public int id;
    public String name;
    public String beschreibung;
    public String kuerzel;
    public String datum;

    public  ChangeEntry(Integer id,String name, String beschreibung, String kuerzel, String datum){
        this.id = id;
        this.name = name;
        this.beschreibung = beschreibung;
        this.kuerzel = kuerzel;
        this.datum = datum;
    }
}
