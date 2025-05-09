import javax.swing.*;
import java.awt.*;

public class Visuals {
    final static String RED_BACKGROUND = "\u001B[41m";  // Red background
    final static String BLUE_BACKGROUND = "\u001B[44m";  // Blue background
    final static String YELLOW_BACKGROUND = "\u001B[43m";  // Yellow background
    final static String RESET = "\u001B[0m";  // Reset
    final static String WHITE_BACKGROUND = "\u001B[47m";  // White background for light squares
    final static String BLACK_BACKGROUND = "\u001B[100m";  // Black background for dark squares
    static StringBuilder board = new StringBuilder();  // Use StringBuilder for better performance

    public static void mainMenu(){
        System.out.print("""
\u001B[32m┌────────────────────────────────────────────────┐
│  ___  __    __     ___  _  _  ____  ____  ____ │
│ / __)(  )  (  )   / __)/ )( \\(  __)/ ___)/ ___)│
│( (__ / (_/\\ )(   ( (__ ) __ ( ) _) \\___ \\\\___ \\│
│ \\___)\\____/(__)   \\___)\\_)(_/(____)(____/(____/│
└────────────────────────────────────────────────┘\u001B[0m
""");
        System.out.println("                 \u001B[32m1. Singleplayer\n                 2. Multiplayer\n                 3.    Quit\u001B[0m");
        System.out.print("                \u001B[40mEnter your choice: \u001B[0m");
        String prompt = Chess.scan.next();
        switch (prompt){
            case "1" -> Chess.singlePlayer = true;
            case "2" -> Chess.singlePlayer = false;
            case "3" -> System.exit(0);
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("Do you want CLI or GUI visuals?");
        System.out.println("1. CLI\n2. GUI");
        System.out.println("Enter choice: ");
        String visualMode = Chess.scan.next();
        if (visualMode.equals("1")) Chess.cliStyle = true;
        else if (visualMode.equals("2")) Chess.cliStyle = false;
    }



    public static void kingHasRedSquare(){
        if (Chess.blackCheck){
            for (int i = 0; i < 63; i++) {
                if (Chess.pieces[i] == Chess.blackKing){
                    Chess.squareColors[i] = 1;
                    break;
                }
            }
        }
        if (Chess.whiteCheck){
            for (int i = 0; i < 63; i++) {
                if (Chess.pieces[i] == Chess.whiteKing){
                    Chess.squareColors[i] = 1;
                    break;
                }
            }
        }
    }


    public static String pieceVisuals(int pieceId){
        final String RESET = "\u001B[0m";
        final String WHITE = "\u001B[97m";
        final String BLACK = "\u001B[30m";

        if (Chess.visualStyle == 1 && Chess.cliStyle){
            return switch(pieceId){
                case Chess.whitePawn   -> WHITE + "♟";
                case Chess.whiteRook   -> WHITE + "♜";
                case Chess.whiteKnight -> WHITE + "♞";
                case Chess.whiteBishop -> WHITE + "♝";
                case Chess.whiteQueen  -> WHITE + "♛";
                case Chess.whiteKing   -> WHITE + "♚";

                case Chess.blackPawn   -> BLACK + "♟";
                case Chess.blackRook   -> BLACK + "♜";
                case Chess.blackKnight -> BLACK + "♞";
                case Chess.blackBishop -> BLACK + "♝";
                case Chess.blackQueen  -> BLACK + "♛";
                case Chess.blackKing   -> BLACK + "♚";
                default -> " ";
            };
        } else if (!Chess.cliStyle){
            return switch(pieceId){
                case Chess.whitePawn   -> "♟";
                case Chess.whiteRook   -> "♜";
                case Chess.whiteKnight -> "♞";
                case Chess.whiteBishop -> "♝";
                case Chess.whiteQueen  -> "♛";
                case Chess.whiteKing   -> "♚";

                case Chess.blackPawn   -> "♟";
                case Chess.blackRook   -> "♜";
                case Chess.blackKnight -> "♞";
                case Chess.blackBishop -> "♝";
                case Chess.blackQueen  -> "♛";
                case Chess.blackKing   -> "♚";
                default -> " ";
            };
        }
        // Default returns double letters
        return switch(pieceId){
            case Chess.whitePawn   -> WHITE + "PA" + RESET;
            case Chess.whiteRook   -> WHITE + "RK" + RESET;
            case Chess.whiteKnight -> WHITE + "KN" + RESET;
            case Chess.whiteBishop -> WHITE + "BP" + RESET;
            case Chess.whiteQueen  -> WHITE + "QN" + RESET;
            case Chess.whiteKing   -> WHITE + "KG" + RESET;
            case Chess.blackPawn   -> BLACK + "PA" + RESET;
            case Chess.blackRook   -> BLACK + "RK" + RESET;
            case Chess.blackKnight -> BLACK + "KN" + RESET;
            case Chess.blackBishop -> BLACK + "BP" + RESET;
            case Chess.blackQueen  -> BLACK + "QN" + RESET;
            case Chess.blackKing   -> BLACK + "KG" + RESET;
            default -> "  ";
        };
    }


    public static void printBoard() {
        if(Chess.cliStyle) cliStyleBoard();
        else updateGuiBoard();
    }

    private static void cliStyleBoard(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        // Header with column labels ┌───┐
        board.append("         ┌───┬───┬───┬───┬───┬───┬───┬───┐\n");

        // Loop through the board from row 7 (top) to row 0 (bottom)
        for (int row = 7; row >= 0; row--) {
            board.append("       ").append(row + 1).append(" │");  // Print the rank number at the beginning

            // Loop through columns from 0 to 7
            for (int col = 0; col < 8; col++) {
                int i = row * 8 + col;  // Calculate the correct index for the 1D array representation
                int piece = Chess.pieces[i];
                String representation = pieceVisuals(piece);  // Get the piece's visual representation

                // Alternate background colors between light and dark squares
                if (Chess.squareColors[i] == 1){
                    representation = RED_BACKGROUND + " " + representation + " " + RESET;
                } else if (Chess.squareColors[i] == 2){
                    representation = BLUE_BACKGROUND + " " + representation + " " + RESET;
                } else if (Chess.squareColors[i] == 3){
                    representation = YELLOW_BACKGROUND + " " + representation + " " + RESET;
                } else {
                    if ((row + col) % 2 == 0) {
                        representation = BLACK_BACKGROUND + " " + representation + " " + RESET;
                    } else {
                        representation = WHITE_BACKGROUND + " " + representation + " " + RESET;
                    }
                }
                // Add the piece representation with borders
                board.append(representation).append("│");
            }

            // Add row separator and move to the next line
            if (row > 0) {
                board.append("\n         ├───┼───┼───┼───┼───┼───┼───┼───┤\n");
            } else {
                board.append("\n         └───┴───┴───┴───┴───┴───┴───┴───┘\n");
            }
        }
        board.append("           A   B   C   D   E   F   G   H \n");

        // Print the final board
        System.out.println(board.toString());
    }


    private static final JButton[] squares = new JButton[64];

    public static void initGuiBoard() {
        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 1200);

        JPanel boardPanel = new JPanel(new GridLayout(10, 10));
        frame.add(boardPanel);

        Font labelFont = new Font("Noto Serif", Font.PLAIN, 55);
        Font pieceFont = new Font("Segoe UI Symbol", Font.PLAIN, 95);
        Color labelColor = new Color(240, 229, 225, 255); // Dark magenta or your choice
        Color labelBackground = new Color(73, 71, 71);
        for (int row = 9; row >= 0; row--) {
            for (int col = 0; col <= 9; col++) {
                // Top-left and bottom-right corners: just empty space
                if ((row == 9 && col == 0) || (row == 0 && col == 9) || (row == 9 && col == 9) || (row == 0 && col == 0)) {
                    JLabel empty = new JLabel();
                    empty.setOpaque(true);
                    empty.setBackground(labelBackground);
                    boardPanel.add(empty);
                }

                // Top row: column labels
                else if (row == 9) {
                    JLabel label = new JLabel(Character.toString((char) ('A' + col - 1)), SwingConstants.CENTER);
                    label.setFont(labelFont);
                    label.setForeground(labelColor);
                    label.setOpaque(true);
                    label.setBackground(labelBackground);
                    boardPanel.add(label);
                }

                // Bottom row: column labels
                else if (row == 0) {
                    JLabel label = new JLabel(Character.toString((char) ('A' + col - 1)), SwingConstants.CENTER);
                    label.setFont(labelFont);
                    label.setForeground(labelColor);
                    label.setOpaque(true);
                    label.setBackground(labelBackground);
                    boardPanel.add(label);
                }

                // Left column: rank labels
                else if (col == 0) {
                    JLabel label = new JLabel(Integer.toString(row), SwingConstants.CENTER);
                    label.setFont(labelFont);
                    label.setForeground(labelColor);
                    label.setOpaque(true);
                    label.setBackground(labelBackground);
                    boardPanel.add(label);
                }

                // Right column: rank labels
                else if (col == 9) {
                    JLabel label = new JLabel(Integer.toString(row), SwingConstants.CENTER);
                    label.setFont(labelFont);
                    label.setForeground(labelColor);
                    label.setOpaque(true);
                    label.setBackground(labelBackground);
                    boardPanel.add(label);
                }
                else {
                    int actualRow = row - 1;
                    int actualCol = col - 1;
                    int index = actualRow * 8 + actualCol;

                    JButton square = new JButton();
                    square.setFont(pieceFont);
                    square.setBorder(BorderFactory.createLineBorder(new Color(28, 28, 28), 2));
                    square.setFocusPainted(false);

                    char file = (char) ('A' + actualCol);
                    int rank = actualRow + 1;
                    String coordinate = "" + file + rank;

                    int finalIndex = index;
                    square.addActionListener(e -> {
                        if (Chess.isSelecting) {
                            Chess.pieceSelection(Chess.translateInput(coordinate)); // Do as if you had typed your coordinate
                        } else {
                            Chess.gameLogic(Chess.translateInput(coordinate), Chess.selectedPiece);
                        }
                        updateGuiBoard(); // Just update visuals
                    });

                    squares[index] = square;
                    boardPanel.add(square);
                }
            }
        }
        boardPanel.setBorder(BorderFactory.createLineBorder(new Color(28, 28, 28), 2));

        frame.setVisible(true);
    }

    private static void updateGuiBoard() {
        for (int i = 0; i < 64; i++) {
            JButton square = squares[i];
            int pieceId = Chess.pieces[i];
            square.setText(pieceVisuals(pieceId));
            square.setOpaque(true);

            // Example color logic...
            square.setForeground(pieceId < 7 ? Color.WHITE : new Color(39, 39, 39));

            // Set background color
            int row = i / 8;
            int col = i % 8;
            switch (Chess.squareColors[i]) {
                case 1 -> square.setBackground(new Color(210, 96, 96, 255));
                case 2 -> square.setBackground(new Color(149, 186, 224, 255));
                case 3 -> square.setBackground(new Color(227, 179, 92, 255));
                default -> {
                    if ((row + col) % 2 == 0)
                        square.setBackground(new Color(70, 68, 68));
                    else
                        square.setBackground(new Color(240, 229, 225));
                }
            }
        }
    }


}
