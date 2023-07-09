package pl.edu.pw.elka.prm2t.projekt;

/**
 * Klasa służąca do uruchomienia aplikacji
 */
public class Nurikabe {
    /**
     * Tworzy nowy obiekt klasy BoardInfo plansza oraz GUI, do którego przekazuje utworzony obiekt plansza
     * @param args nieużywane
     */
    public static void main(String[] args) {
            BoardInfo plansza = new BoardInfo();
            new GUI(plansza);
    }
}
