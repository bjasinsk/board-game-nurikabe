package pl.edu.pw.elka.prm2t.projekt;

import java.io.*;

/**
 * Klasa, której obiekty służą do przechowywania wszystkich informacji o stanie planszy, modyfikowania stanu
 * jej pól, czyli wartości, jak i zaznaczeń lub odznaczeń pól na planszy. Umożliwia ona również zapisywanie stanu gry
 * ostatniej gry oraz wczytywanie jej, również po zamknięciu i ponownym uruchomieniu aplikacji. Pozwala również
 * wczytywać na początku gry różne poziomy, zamieszczone w zasobach gry lub własne plansze użytkownika.
 * Plansze są reprezentowane przez pliki tekstowe z wartościami poszczególnych pól planszy (liczby podane w formacie
 * dziesiętnym, bez ograniczenia wielkości liczby), gdzie nowa linia w pliku reprezentuje kolejny wiersz
 * planszy a spacje oddzielają kolejne komórki. Mechanizm wczytywania planszy nie sprawdza, czy wczytywana plansza
 * jest rozwiązywalna.
 * Ponieważ metody zapisujące planszę korzystają cały czas z dwóch utworzonych na stałe plików nie należy tworzyć
 * jednocześnie więcej niż jednego obiektu tej klasy
 */
public class BoardInfo {

    /**
     * Tablica przechowująca wartości pól na planszy, domyślna wartość to 0, pola z taką wartością są pomijane w
     * metodach działających na tej tablicy
     */
    public Integer[][] numbers;
    /**
     * Tablica przechowująca zakolorowania pól na planszy z konwencją: true - pole białe, niezakolorowane oraz
     * false - pole czarne, zakolorowane
     */
    public Boolean[][] colours;
    /**
     * Dwa pola przechowujące wymiary planszy: poziomy i pionowy
     */
    public int horizontal;
    public int vertical;
    /**
     * Pole przechowujące obiekt klasy SavedMoves służący do zapisu posunięć gracza
     */
    public SavedMoves savedMoves;

    /**
     * Tworzy planszę, wczytując domyślną planszę zapisaną w pliku "resource/plansza0.txt" oraz inicjuje wszystkie
     * pola obiektu (by metody w innych klasach z nich korzystające działały poprawnie) za pomocą metody
     * loadValues(File file)
     * Jest to plansza pusta o wymiarach 6 na 6
     */
    BoardInfo() {
        try {
            loadValues(new File("resource/plansza0.txt"));
            resetColours();
            savedMoves = new SavedMoves();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zwraca rozmiar obszaru przypisany do danego pola, wzięty z pliku tekstowego opisującego planszę
     * Wykorzystywana w klasie Solver
     * @param n współrzędna pozioma pola na planszy
     * @param m współrzędna pionowa pola na planszy
     * @return wartość przypisaną do danego pola w tablicy numbers
     */
    public int getNumber(int n, int m) {
        return numbers[n][m];
    }

    /**
     * Zmienia informację o zakolorowaniu pola w tablicy colours na przeciwną to jest z białego na czarny
     * lub z czarnego na biały
     * @param n współrzędna pozioma pola na planszy
     * @param m współrzędna pionowa pola na planszy
     */
    public void changeColour(int n, int m) {
        colours[n][m] = !colours[n][m];
    }

    /**
     * Zwraca informację o zakolorowaniu danego pola, czyli czy jest białe czy czarne
     * @param n współrzędna pozioma pola na planszy
     * @param m współrzędna pionowa pola na planszy
     * @return zwraca true, jeśli dane pole jest zapisane w tablicy colours jako białe, zwraca false w przypadku
     * przeciwnym
     */
    public boolean getColour(int n, int m) {
        return colours[n][m];
    }

    /**
     * Odczytuje z pliku tekstowego pod warunkiem, że przechowuje on dane w odpowiednim formacie (patrz: dokumentacja
     * klasy), wymiary planszy potrzebne do poprawnej inicjacji tablic, inicjuje te tablice oraz zapisuje
     * wartości pól opisujące rozmiary obszarów do tablicy numbers
     * na planszy
     * Wymaga wywołania z plikiem "boardSave.txt" przed wywołaniem metody loadColours() przy odczycie zapisanego stanu
     * gry
     * @param file plik tekstowy, z którego ma zostać odczytana plansza
     * @throws IOException jeśli wystąpił błąd w odczycie pliku rzucany przez strumienie utworzone w metodzie
     */
    public void loadValues(File file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        String[] length = line.split(" ");
        this.vertical = 1;
        while (br.readLine() != null) vertical++;
        br.close();
        fr.close();
        this.horizontal = length.length;
        numbers = new Integer[vertical][horizontal];
        colours = new Boolean[vertical][horizontal];
        int n = 0;
        int m;

        FileReader fRead = new FileReader(file);
        BufferedReader bRead = new BufferedReader(fRead);
        String thisLine = bRead.readLine();
        while (thisLine != null) {
            m = 0;
            String[] tokens = thisLine.split(" ");
            for (String token : tokens) {
                int v = Integer.parseInt(token);
                numbers[n][m] = v;
                m++;
            }
            n++;
            thisLine = bRead.readLine();
        }
        bRead.close();
        fRead.close();
    }

    /**
     * Służy do odczytania planszy z pliku tekstowego podanego przez użytkownika, zapisanego w systemie operacyjnym
     * oraz zresetowania wypełnienienia pól
     * Używa metod loadValues() i resetColours()
     * @param file plik, z którego ma zostać odczytana plansza
     */
    public void loadLevel(File file) {
        try {
            loadValues(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        resetColours();
    }

    /**
     * Służy do odczytania jednej z domyślnych plansz gry oraz zresetowania wypełnienia pól
     * Używa metod loadValues() i resetColours()
     * @param n numer ewidencyjny planszy spośród domyślnych plansz zapisanych w zasobach gry
     */
    public void loadLevelbyNumber(int n) {
        try {
            this.loadValues(new File("resource/plansza"+ n +".txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        resetColours();
    }

    /**
     * Służy do resetowania wypełnienia pól na planszy, czyli odkolorowania wszystkich na białe
     * poprzez ustawienie wszystkich wartości w tablicy colours na true
     */
    public void resetColours() {
        for (int i = 0; i < vertical; i++) {
            for(int j = 0; j < horizontal; j++) {
                colours[i][j] = true;
            }
        }
    }

    /**
     * Zapisuje aktualną tablicę numbers do pliku tekstowego boardSave.txt w katalogu resource zgodnie w formacie
     * umożliwiającym następnie odczytanie tego pliku przez metodę loadValues()
     */
    public void saveBoard() {
        try {
            FileWriter fw = new FileWriter("resource/boardSave.txt");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < vertical; i++) {
                for (int j = 0; j < horizontal; j++) {
                    sb.append(numbers[i][j]).append(" ");
                }
                sb.append("\n");
            }
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Zapisuje aktualną tablicę colours do pliku tekstowego coloursSave.txt w katalogu resource w formacie
     * umożliwiającym następnie odczytanie tego pliku przez metodę loadColours(), czyli oddzielenie wierszy
     * i poszczególnych pól zgodnie z opisem w ogólnej dokumentacji klasy, natomiast wartości pól są zapisywane jako
     * ciąg znaków "true" lub "false"
     * Modyfikacje tego pliku powinny być wykonywane tylko i wyłącznie za pomocą tej metody, by zapewnić zapisanie
     * informacji o zakolorowaniu pól w poprawny sposób
     */
    public void saveColours() {
        try {
            FileWriter fw = new FileWriter("resource/coloursSave.txt");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < vertical; i++) {
                for (int j = 0; j < horizontal; j++) {
                    sb.append(colours[i][j]).append(" ");
                }
                sb.append("\n");
            }
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Odczytuje z pliku tekstowego coloursSave.txt w katalogu resource zapisane informacje o zakolorowaniu pól planszy
     * Wymaga poprawnego formatowania do poprawnego odczytu (patrz: dokumentacja metody saveColours()) oraz  uprzedniego
     * wywołania metody loadValues() z plikiem boardSave.txt, ponieważ inaczej tablica colours nie będzie odpowiednich
     * rozmiarów
     */
    public void loadColours() {
        try {
            int n = 0;
            int m;

            FileReader fRead = new FileReader("resource/coloursSave.txt");
            BufferedReader bRead = new BufferedReader(fRead);
            String thisLine = bRead.readLine();
            while (thisLine != null) {
                m = 0;
                String[] tokens = thisLine.split(" ");
                for (String token : tokens) {
                    boolean v = Boolean.parseBoolean(token);
                    colours[n][m] = v;
                    m++;
                }
                n++;
                thisLine = bRead.readLine();
            }
            bRead.close();
            fRead.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
