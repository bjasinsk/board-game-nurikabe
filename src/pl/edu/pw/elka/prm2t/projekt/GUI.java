package pl.edu.pw.elka.prm2t.projekt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Klasa przechowująca wszystkie elementy interfejsu użytkownika, dziedzicząca po klasie JFrame z pakietu Swing.
 */
public class GUI extends JFrame implements ActionListener {
    /**
     * Przechowuje informacje o planszy, na podstawie których aktualizuje układ przycisków.
     */
    BoardInfo board;
    /**
     * Panel planszy.
     */
    JPanel boardPanel;
    /**
     * Przycisk wyjścia z gry.
     */
    JMenuItem exitItem;
    /**
     * Przycisk pokazujący okno z listą autorów.
     */
    JMenuItem authorsItem;
    /**
     * Przycisk otwierający w domyślnej przeglądarce stronę z zasadami gry.
     */
    JMenuItem rulesItem;
    /**
     * Przycisk otwierający w domyślnej przeglądarce stronę z repozytorium.
     */
    JMenuItem repoItem;
    /**
     * Przycisk sprawdzający poprawność rozwiązania.
     */
    JButton checkButton;
    /**
     * Menu wyboru poziomów
     */
    JMenu levelItem;
    /**
     * Przycisk ładowania planszy z pliku.
     */
    JMenuItem loadBoardItem;
    /**
     * Przycisk zapisywania planszy do pliku png.
     */
    JMenuItem printItem;
    /**
     * Informacja o statusie gry.
     */
    JLabel status;
    /**
     * Przycisk zapisywania stanu gry.
     */
    JMenuItem saveGameItem;
    /**
     * Przycisk wczytywania stanu gry.
     */
    JMenuItem loadGameItem;
    /**
     * Przycisk cofania ruchu.
     */
    JButton undoMove;
    /**
     * Przyciski wchodzące w skład planszy.
     */
    JButton[][] buttons;

    /**
     * Inicjuje interfejs użytkownika, ustawiając wszystkie parametry. Następnie ładuje domyślną, pustą planszę.
     * @param board Obiekt z informacjami o planszy.
     */
    public GUI(BoardInfo board) {
        // Ustawienia okna
        super("Nurikabe");
        setSize(350,450);
        setLayout(new FlowLayout());
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.board = board;


        // Pasek menu
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);


        // Menu - Gra
        JMenu gameMenu = new JMenu("Gra");
        menuBar.add(gameMenu);

        saveGameItem = new JMenuItem("Zapisz stan");
        gameMenu.add(saveGameItem);
        saveGameItem.addActionListener(this);

        loadGameItem = new JMenuItem("Wczytaj stan");
        gameMenu.add(loadGameItem);
        loadGameItem.addActionListener(this);

        exitItem = new JMenuItem("Wyjdź");
        gameMenu.add(exitItem);
        exitItem.addActionListener(this);


        // Menu - Plansza
        JMenu boardMenu = new JMenu("Plansza");
        menuBar.add(boardMenu);

        levelItem = new JMenu("Wybierz poziom");
        boardMenu.add(levelItem);

        for (int i = 1; i < 16; i++) {
            JMenuItem item = new JMenuItem();
            item.setText("Poziom " + i);

            int finalI = i;
            item.addActionListener(e -> {
                board.loadLevelbyNumber(finalI);
                reloadButtons();
            });

            levelItem.add(item);
        }

        loadBoardItem = new JMenuItem("Wczytaj schemat");
        loadBoardItem.addActionListener(this);
        boardMenu.add(loadBoardItem);

        printItem = new JMenuItem("Drukuj");
        boardMenu.add(printItem);
        printItem.addActionListener(this);


        // Menu - Inne
        JMenu otherMenu = new JMenu("Inne");
        menuBar.add(otherMenu);

        authorsItem = new JMenuItem("Autorzy");
        otherMenu.add(authorsItem);
        authorsItem.addActionListener(this);

        repoItem = new JMenuItem("Repozytorium");
        otherMenu.add(repoItem);
        repoItem.addActionListener(this);

        rulesItem = new JMenuItem("Zasady");
        otherMenu.add(rulesItem);
        rulesItem.addActionListener(this);


        // Napis statusu
        status = new JLabel("Nurikabe");
        add(status);


        // Plansza z przyciskami
        boardPanel = new JPanel(new GridLayout(board.vertical, board.horizontal));
        boardPanel.setPreferredSize(new Dimension(300,300));
        add(boardPanel);
        reloadButtons();


        // Przycisk potwierdzenia
        checkButton = new JButton("Sprawdź rozwiązanie");
        checkButton.setPreferredSize(new Dimension(175,50));
        checkButton.setContentAreaFilled(false);
        checkButton.setFocusable(false);
        add(checkButton);
        checkButton.addActionListener(this);


        //Przycisk cofania ruchu
        undoMove = new JButton("Cofnij ruch");
        undoMove.setPreferredSize(new Dimension(100,50));
        undoMove.setContentAreaFilled(false);
        undoMove.setFocusable(false);
        add(undoMove);
        undoMove.addActionListener(this);


        // Wyświetlenie okna
        setVisible(true);
    }

    /**
     * Inicjuje tablicę przycisków tworzących planszę do gry. Po kliknięciu któregoś z tych przycisków przez gracza,
     * aktualizuje schemat przycisków wchodzących w skład planszy, pobierając aktualne dane z klasy BoardInfo, zmienia
     * kolor klikniętego przycisku (pod warunkiem, że nie jest on polem z wartością liczbową, wynika to z zasad gry)
     * oraz zapisuje ostatnie posunięcie jako obiekt klasy NurikabeMove do przeznaczonego do przechowywania
     * tej informacji obiektu klasy SavedMoves w obiekcie klasy BoardInfo będącego polem this
     */
    public void reloadButtons() {
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(board.vertical, board.horizontal));

        buttons = new JButton[board.vertical][board.horizontal];
        for(int i = 0; i< board.vertical; i++) {
            for(int j = 0; j< board.horizontal; j++) {
                JButton b = new JButton();
                if (board.numbers[i][j] != 0) {
                    b.setText(String.valueOf(board.numbers[i][j]));
                    b.setMargin(new Insets(0,0,0,0));
                }
                b.setFocusable(false);
                if (board.colours[i][j]) {
                    b.setBackground(Color.WHITE);
                } else {
                    b.setBackground(Color.BLACK);
                }

                buttons[i][j] = b;
                boardPanel.add(buttons[i][j]);

                int finalI = i;
                int finalJ = j;
                buttons[i][j].addActionListener(e -> {
                    if(buttons[finalI][finalJ].getBackground() == Color.WHITE && board.getNumber(finalI, finalJ) == 0){
                        buttons[finalI][finalJ].setBackground(Color.BLACK);
                    }else{
                        buttons[finalI][finalJ].setBackground(Color.WHITE);
                    }
                    board.changeColour(finalI, finalJ);
                    board.savedMoves.nextMove(new NurikabeMove(finalI, finalJ));
                });
            }
        }

        repaint();
        revalidate();
        doLayout();
    }

    /**
     * Metoda przechwytująca i obsługująca wszystkie istotne z punktu widzenia działania aplikacji wydarzenia
     * Skutki jej działania to zmiana stanu obiektu BoardInfo, zamknięcie aplikacji, otwarcie linków w przeglądarce
     * do zasad gry lub repozytorium projektu, wyświetlenie nowego okna z listą autorów, wyświetlenie komunikatu
     * o poprawności bądź niepoprawności rozwiązania planszy, cofnięcie osatniego ruchu, zapis aktualnej gry
     * lub odczyt tej zapisanej, lub załadowanie nowej planszy, a także zapisanie aktualnego wyglądu aplikacji do
     * pliku PNG
     * @param ae dowolne wydarzenie zaistniałe w aplikacji, przede wszystkim kliknięcia różnych przycisków
     *           w interfejsie, oprócz przycisków tworzących plansze
     */
    public void actionPerformed(ActionEvent ae){

        if(ae.getSource() == exitItem){
            dispose();
        }

        else if(ae.getSource() == authorsItem) {
            JOptionPane.showMessageDialog(null, "Karol Babik, Bartosz Jasiński, Jakub Skorupa, Aleksandra Rostkowska");
        }

        else if(ae.getSource() == repoItem){
            String link = "https://gitlab-stud.elka.pw.edu.pl/kbabik/prm2t22l_pro_babik_nurikabe";
            try {
                Desktop.getDesktop().browse(new URL(link).toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }

        else if(ae.getSource() == rulesItem) {
            String link = "http://www.cross-plus-a.com/pl/html/cros7nur.htm";
            try {
                Desktop.getDesktop().browse(new URL(link).toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }

        else if (ae.getSource() == checkButton) {
            Solver solver = new Solver(board);
            if (solver.sprawdzObszary() && solver.sprawdzGroble() && solver.sprawdz2x2()) {
                status.setText("Poprawne rozwiązanie!");
            } else {
                status.setText("Rozwiązanie niepoprawne!");
            }
        }

        else if(ae.getSource() == printItem) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                BufferedImage img = new BufferedImage(boardPanel.getWidth(), boardPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
                boardPanel.paint(img.getGraphics());
                File outputfile = new File(fileChooser.getSelectedFile().getAbsolutePath() + ".png");
                try {
                    ImageIO.write(img, "png", outputfile);
                    JOptionPane.showMessageDialog(null, "Zapisano plik.");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Nie udało się zapisać pliku.");
                }
            }
        }

        else if (ae.getSource() == saveGameItem) {
            board.saveBoard();
            board.saveColours();
        }

        else if (ae.getSource() == loadGameItem) {
            board.loadLevel(new File("resource/boardSave.txt"));
            board.loadColours();
            board.loadLevel(new File("resource/boardSave.txt"));
            board.loadColours();
            reloadButtons();
        }

        else if (ae.getSource() == undoMove) {
            if (board.savedMoves.size() != 0) {
                NurikabeMove move = board.savedMoves.undoMove();
                int n = move.getX();
                int m = move.getY();
                if (buttons[n][m].getBackground() == Color.WHITE && board.getNumber(n, m) == 0) {
                    buttons[n][m].setBackground(Color.BLACK);
                } else {
                    buttons[n][m].setBackground(Color.WHITE);
                }
                board.changeColour(n, m);
            }
        }

        else if (ae.getSource() == loadBoardItem) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                board.loadLevel(file);
                reloadButtons();
            }
        }
    }
}
