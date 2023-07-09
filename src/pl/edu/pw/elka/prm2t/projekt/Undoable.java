package pl.edu.pw.elka.prm2t.projekt;

/**
 * Interfejs służący do implementowania możliwości zapisu i cofania ruchów w grze Nurikabe
 * W założeniu miał być bardziej ogólny i używać abstrakcyjnej klasy Move, po której miała dziedziczyć
 * klasa NurikabeMove, ale wystąpiły pewne problemy, na rozwiązanie których zabrakło czasu
 */
public interface Undoable {

    /**
     * Służy do zapisywania i procesowania posunięcia
     * @param m obiekt klasy dziedziczącej po NurikabeMove reperezentujący posunięcie
     * @param <M> klasa dziedzicząca po NurikabeMove
     */
    <M extends NurikabeMove> void nextMove(M m);

    /**
     * Służy do zwrócenia ruchów w kolejności od najmłodszego
     * @return obiekt reprezentujący wcześniej zapisane posunięcie
     */
    NurikabeMove undoMove();
}
