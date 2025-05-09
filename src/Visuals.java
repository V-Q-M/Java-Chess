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
        else guiBoard();
    }

    private static void cliStyleBoard(){
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

    private static void guiBoard(){
        JFrame frame = new JFrame("Chess Board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < 8; col++) {
                int index = row * 8 + col;
                int pieceId = Chess.pieces[index];

                // Convert to chess coordinate
                char file = (char) ('a' + col);     // a-h
                int rank = row + 1;                 // 1-8
                String coordinate = "" + file + rank;

                JButton square = new JButton(pieceVisuals(pieceId));
                square.setOpaque(true);
                square.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 70));
                square.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                square.setFocusPainted(false);

                // Set foreground color (text color) based on piece
                if (pieceId <  7) {
                    square.setForeground(Color.WHITE); // white pieces
                } else if (pieceId > 8) {
                    square.setForeground(new Color(39, 39, 39)); // black pieces
                }

                // Background color logic (your original style)
                switch (Chess.squareColors[index]) {
                    case 1 -> square.setBackground(new Color(235, 60, 60, 128));   // red
                    case 2 -> square.setBackground(new Color(53, 125, 202, 128));  // blue
                    case 3 -> square.setBackground(new Color(255, 187, 27, 128));  // yellow
                    default -> {
                        if ((row + col) % 2 == 0) {
                            square.setBackground(new Color(240, 229, 225)); // light
                        } else {
                            square.setBackground(new Color(70, 68, 68));    // dark
                        }
                    }
                }

                // Print square coordinate on click
                square.addActionListener(e -> Chess.pieceSelection(Chess.translateInput(coordinate)));

                boardPanel.add(square);
            }
        }

        frame.add(boardPanel);
        frame.setVisible(true);
    }



}
