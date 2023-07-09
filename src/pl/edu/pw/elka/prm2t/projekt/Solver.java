package pl.edu.pw.elka.prm2t.projekt;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa służąca do sprawdzania spełnienia zasad Nurikabe dla danej rozgrywki:
 * 1. Obszary, których częścią są pola z wartościami liczbowymi muszą mieć taki rozmiar, na jaki
 * wskazuje ta wartość liczbowa
 * 2. Grobla, czyli zaznaczone na czarno przez gracza pola muszą tworzyć jedną ciągłą figurę (to jest każde
 * zaznaczone pole musi stykać się co najmniej jednym bokiem z innym zaznaczonym polem)
 * 3. Zaznaczone pola nie mogą tworzyć nigdzie na planszy kwadratu o boku długości dwa
 */
public class Solver {
    BoardInfo plansza;

    /**
     * Aby dalsze metody działały poprawnie, plansza przekazana do konstruktora musi być w całości zainicjalizowana
     * to jest żadne z pól obiektu BoardInfo (oprócz SavedMoves) nie może być null
     * @param board plansza, której poprawność rozwiązania jest sprawdzana
     */
    Solver(BoardInfo board) {
        plansza = board;
    }

    /**
     * Sprawdza spełnienie pierwszej zasady, wywołując metodę obszar() dla każdego pola w tablicy plansza.numbers,
     * które nie jest równe 0, a następnie sprawdza czy obliczony rozmiar pola zgadza się z wartością pobraną
     * z plansza.numbers odpowiadającą temu polu
     * @return false, jeśli porównanie nie będzie prawdziwe
     */
    public boolean sprawdzObszary() {
        for (int i = 0; i < plansza.vertical; i++) {
            for (int j = 0; j < plansza.horizontal; j++) {
                if (plansza.numbers[i][j] != 0) {
                    if (obszar(i, j) != plansza.numbers[i][j]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sprawdza spełnienie drugiej zasady, wywołując metodę sasiad() na rzecz każdego czarnego pola
     * @return false, jeśli dla co najmniej jednego pola metoda sasiad() zwróci false
     */
    public boolean sprawdzGroble() {
        for (int i = 0; i < plansza.vertical; i++) {
            for (int j = 0; j < plansza.horizontal; j++) {
                if (!plansza.colours[i][j]) {
                    if (!sasiad(i, j)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sprawdza spełnienie trzeciej zasady, sprawdzając czy żadne cztery pola na planszy tworzące kwadrat 2 na 2
     * nie są wszystkie czarne
     * @return false, jeśli metoda czarny2x2() wywołana na rzecz tego pola zwróci true
     */
    public boolean sprawdz2x2() {
        for (int i = 0; i < plansza.vertical - 1; i++) {
            for (int j = 0; j < plansza.horizontal - 1; j++) {
                if (!plansza.colours[i][j]) {
                    if (czarny2x2(i, j)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Tworzy listę pól obszaru, reprezentowanych jako obiekty klasy Point o współrzędnych takich, jak współrzędne
     * pola na planszy, poczynając od tego, na rzecz którego została wywołana metoda
     * Następnie dodaje wszystkich białych sąsiadów pola z listy do tej listy, po czym usuwa to pole i zwiększa licznik
     * rozmiaru obszaru o jeden, dzieje się to rekurencyjnie, aż lista zostanie opróżniona
     * @param n wsp. pion. pola
     * @param m wsp. poz. pola
     * @return rozmiar obszaru
     */
    private int obszar(int n, int m) {
        int rozmiar = 0;
        List<Point> points = new ArrayList<>();
        points.add(new Point(n, m));
        Boolean[][] data = new Boolean[plansza.colours.length][];
        for (int i = 0; i < plansza.colours.length; i++) {
            data[i] = plansza.colours[i].clone();
        }
        while (points.size() != 0) {
            Point p = points.get(0);
            int x = p.x;
            int y = p.y;
            if (y - 1 >= 0) {
                if (data[x][y - 1]) {
                    if (!points.contains(new Point(x, y - 1))) {
                        points.add(new Point(x, y - 1));
                    }
                }
            }
            if (x - 1 >= 0) {
                if (data[x - 1][y]) {
                    if (!points.contains(new Point(x - 1, y))) {
                        points.add(new Point(x - 1, y));
                    }
                }
            }

            if (x + 1 <= plansza.vertical - 1) {
                if (data[x + 1][y]) {
                    if (!points.contains(new Point(x + 1, y))) {
                        points.add(new Point(x + 1, y));
                    }
                }
            }
            if (y + 1 <= plansza.horizontal - 1) {
                if (data[x][y + 1]) {
                    if (!points.contains(new Point(x, y + 1))) {
                        points.add(new Point(x, y + 1));
                    }
                }
            }
            data[x][y] = false;
            points.remove(p);
            rozmiar += 1;
        }
        return rozmiar;
    }

    /**
     * Sprawdza po kolei wszystkich sąsiadów pola, na którego rzecz została wywołana (góra, dół, prawo, lewo)
     * i po kolei sprawdza, czy któryś z nich jest czarny
     * @param n wsp. pion. pola
     * @param m wsp. poz. pola
     * @return true, jeśli co najmniej jeden z sąsiadów jest czarny
     */
    private boolean sasiad(int n, int m) {
        boolean gora = false, dol = false, prawo = false, lewo = false;
        if (m - 1 >= 0) {
            if (!plansza.getColour(n, m - 1)) {
                lewo = true;
            }
        }
        if (n - 1 >= 0) {
            if (!plansza.getColour(n - 1, m)) {
                gora = true;
            }
        }

        if (n + 1 <= plansza.vertical - 1) {
            if (!plansza.getColour(n + 1, m)) {
                dol = true;
            }
        }
        if (m + 1 <= plansza.horizontal - 1) {
            if (!plansza.getColour(n, m + 1)) {
                prawo = true;
            }
        }
        return gora || dol || prawo || lewo;
    }

    /**
     * Sprawdza, czy gdzieś na planszy jest zaczerniony kwadrat o boku 2 na 2
     * Bierze po kolei cztery pola w kształcie kwadratu z tablicy, dodane warunki, żeby nie spowodowało to
     * wybrania indeksu wykraczającego poza tablicę
     * Każde czarne pole zwiększa zmienną int size o 1
     * @param n wsp. pion. pola w lewym górnym rogu tego kwadratu
     * @param m wsp. poz. pola w lewym górnym rogu tego kwadratu
     * @return true, jeśli po sprawdzeniu wszystkich czterech pól size będzie równe 4
     */
    private boolean czarny2x2(int n, int m) {
        int size = 0;
        if (!plansza.colours[n][m]) {
            size++;
        }
        if (!plansza.colours[n + 1][m]) {
            size++;
        }
        if (!plansza.colours[n][m + 1]) {
            size++;
        }
        if (!plansza.colours[n + 1][m + 1]) {
            size++;
        }
        return size == 4;
    }
}
