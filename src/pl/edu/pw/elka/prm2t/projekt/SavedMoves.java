package pl.edu.pw.elka.prm2t.projekt;

import java.util.ArrayList;

/**
 * Klasa implementująca interfejs Undoable służąca do zapisania i późniejszego zwrócenia posunięć
 * reprezentowanych za pomocą obiektów klasy NurikabeMove
 */
public class SavedMoves implements Undoable{

    public ArrayList<NurikabeMove> moves;

    /**
     * Konstruktor inicjuje tablicę, w której zapisane będą kolejne obiekty klasy Move
     */
    SavedMoves() {
        moves = new ArrayList<>();
    }

    /**
     * Zapisuje w tablicy obiekt NurikabeMove na indeksie 0 oraz jeśli tablica jest rozmiaru 100
     * usuwa najstarszy zapisany ruch
     * @param move obiekt klasy NurikabeMove zapisywany do tablicy
     */
    @Override
    public void nextMove(NurikabeMove move) {
        int n = move.getX();
        int m = move.getY();
        if (moves.size() >= 100) {
            moves.remove(99);
            moves.add(0, new NurikabeMove(n, m));
        } else {
            moves.add(0, new NurikabeMove(n, m));
        }
    }

    /**
     * Zwraca najmłodszy ruch, czyli obiekt NurikabeMove z tablicy moves o indeksie 0.
     * Aby metoda była poprawnie napisana wymagane jest zwracanie domyślnego obiektu
     * (o ustawionych współrzędnych 0,0) w miejscu użycia tej metody nie należy jednak
     * dopuścić do możliwości jej wywołania, gdy rozmiar tablicy moves jest równy 0
     * Do tego służy metoda size()
     * @return obiekt NurikabeMove z tablicy o indeksie 0 lub domyślny o współrzędnych (0,0)
     */
    @Override
    public NurikabeMove undoMove() {
        if (moves.size() != 0) {
            return moves.remove(0);
        } else {
            return new NurikabeMove(0, 0);
        }
    }

    /**
     * Zwraca aktualny rozmiar tablicy, w której przechowywane są ruchy, potrzebne do zabezpieczenia się w GUI
     * przed zwróceniem przez metodę undoMove() domyślnego NurikabeMove(0,0)
     * @return rozmiar tablicy moves
     */
    public int size() {
        return moves.size();
    }
}
