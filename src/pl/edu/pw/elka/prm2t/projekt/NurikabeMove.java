package pl.edu.pw.elka.prm2t.projekt;

/**
 * Klasa reprezentująca zmianę koloru danego pola na planszy Nurikabe, którego
 * współrzędne przechowuje
 */
public class NurikabeMove{

    public int x;
    public int y;

    /**
     * Konstruktor ze współrzędnymi danego pola jako argumentami
     * @param n współrzędna pionowa pola na planszy
     * @param m współrzędna pozioma pola na planszy
     */
    NurikabeMove(int n, int m) {
        x = n;
        y = m;
    }

    /**
     * Jedna z dwóch metod służących do odzyskiwania współrzędnych posunięcia
     * @return współrzędna pionowa pola
     */
    public int getX() {
        return x;
    }

    /**
     * Druga z metod służących do odzyskiwania współrzędnych posunięcia
     * @return współrzędna pozioma pola
     */
    public int getY() {
        return y;
    }

    /**
     * Metoda niepotrzebna do działania gry, utworzona na potrzeby weryfikacji poprawności
     * działania mechanizmu cofania ruchów
     * @return zwięły opis obiektu klasy zawierający współrzędne
     */
    @Override
    public String toString() {
        return "NurikabeMove{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
