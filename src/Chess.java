import java.util.Arrays;
import java.util.Scanner;

public class Chess {

    // IDs
    final static int emptySquare = 0;

    final static int whitePawn = 1;
    final static int whiteRook = 2;
    final static int whiteKnight = 3;
    final static int whiteBishop = 4;
    final static int whiteQueen = 5;
    final static int whiteKing = 6;

    final static int blackPawn = 11;
    final static int blackRook = 12;
    final static int blackKnight = 13;
    final static int blackBishop = 14;
    final static int blackQueen = 15;
    final static int blackKing = 16;

    // SETTINGS
    public static int visualStyle = 0; //0 = Double letter pieces. 1 = Unicode pieces.

    static Scanner scan = new Scanner(System.in);

    static int[] pieces = new int[64]; // stores the positions of the pieces
    boolean[] allowedMoves = new boolean[64]; // stores the allowed moves of one piece

    // Need input
    // Need to add translation

    public static int translateInput(String input){ // Translation and sanitazion of input
        int moveConverted = -1;
        String moveString = input.toLowerCase();

        char letter = moveString.charAt(0);  // the letter part (a-h)
        char number    = moveString.charAt(1);  // the digit part (1-8)
        //System.out.println(letter);
        //System.out.println(number);
        // Translate
        int col = letter - 'a';  // 'a' to 'h' → 0 to 7
        int row = number - '1';  // '1' to '8' → 0 to 7

        if (0 <= col && col < 8 && 0 <= row && row < 8){
            // Formula for index
            moveConverted  = 8 * row + col;
        }

        return moveConverted;
    }

    public static void getInput(){
        System.out.print("Enter your move: ");
        String moveString = scan.next(); // Get input
        System.out.println("You entered: " + moveString);

        int move = translateInput(moveString); // Convert input to an index

        if (move != -1){
            System.out.println("Which means: " + move); // good logic
        } else {
            System.out.println("Invalid move");
        }
    }


    public static void startPosition(){
        // White pieces
        pieces[0]  = whiteRook;
        pieces[1]  = whiteKnight;
        pieces[2]  = whiteBishop;
        pieces[3]  = whiteKing;
        pieces[4]  = whiteQueen;
        pieces[5]  = whiteBishop;
        pieces[6]  = whiteKnight;
        pieces[7]  = whiteRook;
        pieces[8]  = whitePawn;
        pieces[9]  = whitePawn;
        pieces[10] = whitePawn;
        pieces[11] = whitePawn;
        pieces[12] = whitePawn;
        pieces[13] = whitePawn;
        pieces[14] = whitePawn;
        pieces[15] = whitePawn;

        // Black pieces
        pieces[63] = blackRook;
        pieces[62] = blackKnight;
        pieces[61] = blackBishop;
        pieces[60] = blackQueen;
        pieces[59] = blackKing;
        pieces[58] = blackBishop;
        pieces[57] = blackKnight;
        pieces[56] = blackRook;
        pieces[55] = blackPawn;
        pieces[54] = blackPawn;
        pieces[53] = blackPawn;
        pieces[52] = blackPawn;
        pieces[51] = blackPawn;
        pieces[50] = blackPawn;
        pieces[49] = blackPawn;
        pieces[48] = blackPawn;
    }

    public static String pieceVisuals(int pieceId){
        final String RESET = "\u001B[0m";
        final String WHITE = "\u001B[97m";
        final String BLACK = "\u001B[96m";

        if (visualStyle == 1){
            return switch(pieceId){
                case whitePawn   -> "♟ ";
                case whiteRook   -> "♜ ";
                case whiteKnight -> "♞ ";
                case whiteBishop -> "♝ ";
                case whiteQueen  -> "♛ ";
                case whiteKing   -> "♚ ";

                case blackPawn   -> "♙ ";
                case blackRook   -> "♖ ";
                case blackKnight -> "♘ ";
                case blackBishop -> "♗ ";
                case blackQueen  -> "♕ ";
                case blackKing   -> "♔ ";
                default -> "  ";
            };
        }
        // Default returns double letters
        return switch(pieceId){
            case whitePawn   -> WHITE + "PA" + RESET;
            case whiteRook   -> WHITE + "RK" + RESET;
            case whiteKnight -> WHITE + "KN" + RESET;
            case whiteBishop -> WHITE + "BP" + RESET;
            case whiteQueen  -> WHITE + "QN" + RESET;
            case whiteKing   -> WHITE + "KG" + RESET;
            case blackPawn   -> BLACK + "PA" + RESET;
            case blackRook   -> BLACK + "RK" + RESET;
            case blackKnight -> BLACK + "KN" + RESET;
            case blackBishop -> BLACK + "BP" + RESET;
            case blackQueen  -> BLACK + "QN" + RESET;
            case blackKing   -> BLACK + "KG" + RESET;
            default -> "  ";
            };
    }

    public static void printBoard(){
        // loop through all pieces translate the id into a visual representation

        for (int i = 63; i >= 0; i--){
            int piece = pieces[i];
            String representation = pieceVisuals(piece); // pieceVisual decides how to it should look
            if ((i+1) % 8 == 0) {
                System.out.print((i / 8) + 1 + " |" + representation + "|");
            }
            else if (i % 8 == 0){
                System.out.println(representation + "|");
                System.out.println("   -- -- -- -- -- -- -- --");
            } else {
                System.out.print(representation + "|");
            }
        }
        System.out.println("   A  B  C  D  E  F  G  H ");
        System.out.println();
    }

    public static void main(String[] args) {
        startPosition();
        System.out.println(Arrays.toString(pieces));
        printBoard();
        getInput();

        //System.out.println("Welcome to Chess Game!");
    }
}
