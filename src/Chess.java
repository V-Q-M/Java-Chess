import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Chess {

    static boolean running = true;
    static boolean isSelecting = true;
    //static int player = 0;
    static boolean whiteTurn = true;
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

    final static int[] whitePieces = {whitePawn, whiteRook, whiteKnight, whiteBishop, whiteQueen, whiteKing};
    final static int[] blackPieces = {blackPawn, blackRook, blackKnight, blackBishop, blackQueen, blackKing};

    // SETTINGS
    public static int visualStyle = 1; //0 = Double letter pieces. 1 = Unicode pieces.

    static Scanner scan = new Scanner(System.in);

    static int[] pieces = new int[64]; // stores the positions of the pieces
    static int[] squareColors = new int[64];
    static boolean[] allowedMoves = new boolean[64]; // stores the allowed moves of one piece
    static boolean[] allowedAttacks = new boolean[64];


    public static void startPosition(){
        // White pieces
        pieces[0]  = whiteRook;
        pieces[1]  = whiteKnight;
        pieces[2]  = whiteBishop;
        pieces[3]  = whiteQueen;
        pieces[4]  = whiteKing;
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
        pieces[60] = blackKing;
        pieces[59] = blackQueen;
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
        final String BLACK = "\u001B[30m";

        if (visualStyle == 1){
            return switch(pieceId){
                case whitePawn   -> WHITE + "♟";
                case whiteRook   -> WHITE + "♜";
                case whiteKnight -> WHITE + "♞";
                case whiteBishop -> WHITE + "♝";
                case whiteQueen  -> WHITE + "♛";
                case whiteKing   -> WHITE + "♚";

                case blackPawn   -> BLACK + "♟";
                case blackRook   -> BLACK + "♜";
                case blackKnight -> BLACK + "♞";
                case blackBishop -> BLACK + "♝";
                case blackQueen  -> BLACK + "♛";
                case blackKing   -> BLACK + "♚";
                default -> " ";
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

    public static void printBoard() {
        final String RED_BACKGROUND = "\u001B[41m";  // Red background
        final String BLUE_BACKGROUND = "\u001B[44m";  // Blue background
        final String YELLOW_BACKGROUND = "\u001B[43m";  // Yellow background
        final String RESET = "\u001B[0m";  // Reset
        final String WHITE_BACKGROUND = "\u001B[47m";  // White background for light squares
        final String BLACK_BACKGROUND = "\u001B[100m";  // Black background for dark squares
        StringBuilder board = new StringBuilder();  // Use StringBuilder for better performance

        // Header with column labels ┌───┐
        board.append("   ┌───┬───┬───┬───┬───┬───┬───┬───┐\n");

        // Loop through the board from row 7 (top) to row 0 (bottom)
        for (int row = 7; row >= 0; row--) {
            board.append(" ").append(row + 1).append(" │");  // Print the rank number at the beginning

            // Loop through columns from 0 to 7
            for (int col = 0; col < 8; col++) {
                int i = row * 8 + col;  // Calculate the correct index for the 1D array representation
                int piece = pieces[i];
                String representation = pieceVisuals(piece);  // Get the piece's visual representation

                // Alternate background colors between light and dark squares
                if (squareColors[i] == 1){
                    representation = RED_BACKGROUND + " " + representation + " " + RESET;
                } else if (squareColors[i] == 2){
                    representation = BLUE_BACKGROUND + " " + representation + " " + RESET;
                } else if (squareColors[i] == 3){
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
                board.append("\n   ├───┼───┼───┼───┼───┼───┼───┼───┤\n");
            } else {
                board.append("\n   └───┴───┴───┴───┴───┴───┴───┴───┘\n");
            }
        }
        board.append("     A   B   C   D   E   F   G   H \n");

        // Print the final board
        System.out.println(board.toString());
    }


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
        System.out.print("Enter your target: ");
        String moveString = scan.next(); // Get input

        if (Objects.equals(moveString, "exit")){
            running = false;
        }
        else if (moveString != null && moveString.length() == 2){
            int move = translateInput(moveString); // Convert input to an index
            if (move != -1){
                gameLogic(move);
                System.out.println("You entered: " + moveString);
                System.out.println("Which means: " + move); // good logic
            } else {
                System.out.println("Invalid move");
            }
        } else {
            System.out.println("Invalid input");
        }
    }

    static int selectedPiece = -1;
    static int oldPosition = -1;
    public static void gameLogic(int move){
        // squarecolor, white = 0, red = 1, blue = 2
        // selection part
        Arrays.fill(squareColors, 0);
        if (isSelecting){
            if ((whiteTurn && isInWhite(pieces[move])) || (!whiteTurn && isInBlack(pieces[move]))){
                squareColors[move] = 3;
                moveValidation(move);

                for (int i = 0; i < 64; i++){
                    if (allowedAttacks[i]){
                        squareColors[i] = 1;
                    }
                    else if (allowedMoves[i]){ // It' a boolean
                        squareColors[i] = 2;
                    }

                }
                selectedPiece = pieces[move];
                oldPosition = move;
                isSelecting = false;
                printBoard();
            } else {
                System.out.println("Not your piece...");
            }
        } else {
            // Moving part
            //System.out.println("Mv: " + allowedMoves[move]);
            //System.out.println("Atk: " + allowedAttacks[move]);
            if (allowedMoves[move] || allowedAttacks[move]){
                Arrays.fill(allowedMoves, false);
                if (!checkDetection(!whiteTurn)) { // Checks if you need to defend the check
                    // Commit to move
                    pieces[move] = selectedPiece; // Put new pieces to new position
                    pieces[oldPosition] = emptySquare; //clear old square
                    printBoard();
                    Arrays.fill(allowedMoves, false);
                    checkDetection(whiteTurn); // Checks if the enemy is checked
                    whiteTurn = !whiteTurn;
                    // next player
                } else {
                    printBoard();
                    System.out.println("You are check. Protect your king!");
                }

            } else {
                printBoard();
                System.out.println("Invalid move");
            }
            Arrays.fill(allowedMoves, false);
            Arrays.fill(allowedAttacks, false);
            isSelecting = true;
        }

    }

    public static void moveValidation(int move){
        switch (pieces[move]){
            case whitePawn -> whitePawnPattern(move);
            case blackPawn -> blackPawnPattern(move);
            case whiteRook, blackRook -> rookPattern(move);
            case whiteKnight, blackKnight -> knightPattern(move);
            case whiteBishop, blackBishop -> bishopPattern(move);
            case whiteQueen, blackQueen -> queenPattern(move);
            case whiteKing, blackKing -> kingPattern(move);
            default -> System.out.println("ERROR");
        }
    }

    public static boolean checkDetection(boolean checkWhite){
        // For white side
        if (checkWhite){
            int kingPosition = -1;
            for (int i = 0; i < 64; i++){
                if (pieces[i] == blackKing){
                    kingPosition = i;
                }
                if (isInWhite(pieces[i])){
                    moveValidation(i);
                }
            }
            for (int i = 0; i < 64; i++) {
                if (allowedAttacks[kingPosition]){
                    System.out.println("Black King is check!");
                    return true;
                }
            }
        } else if (checkWhite == false) {
            int kingPosition = -1;
            for (int i = 0; i < 64; i++){
                if (pieces[i] == whiteKing){
                    kingPosition = i;
                }
                if (isInBlack(pieces[i])){
                    moveValidation(i);
                }
            }
            for (int i = 0; i < 64; i++) {
                if (allowedAttacks[kingPosition]){
                    System.out.println("White King is check!");
                    return true;
                }
            }
        }
        //System.out.println(Arrays.toString(allowedAttacks));
        return false;
    }



    public static boolean isInWhite(int piece){
        for (int i : whitePieces) {
            if (i == piece){
                return true;
            }
        }
        return false;
    }

    public static boolean isInBlack(int piece){
        for (int i : blackPieces) {
            if (i == piece){
                return true;
            }
        }
        return false;
    }



    public static void whitePawnPattern(int index){
        int target = index + 8;
        if (target < 64) {
            //System.out.println("target: " + target);
            if (pieces[target] == emptySquare){
                allowedMoves[target] = true;
            } else if (isInBlack(pieces[target])) {
                allowedAttacks[target] = true;
            }
        }
        target = index + 16;
        if (target < 64) {
            if (index < 16){
                if (pieces[target] == emptySquare) {
                    allowedMoves[target] = true;
                } else if (isInBlack(pieces[target])) {
                    allowedAttacks[target] = true;
                }
            }
        }
    }


    public static void blackPawnPattern(int index){
        int target = index - 8;
        if (target >= 0) {
            //System.out.println("target: " + target);
            if (pieces[target] == emptySquare){
                allowedMoves[target] = true;
            } else if (isInWhite(pieces[target])) {
                allowedAttacks[target] = true;
            }
        }
        target = index - 16;
        if (target >= 0) {
            if (index >= 48){
                if (pieces[target] == emptySquare) {
                    allowedMoves[target] = true;
                } else if (isInWhite(pieces[target])) {
                    allowedAttacks[target] = true;
                }
            }
        }
    }


    public static void rookPattern(int index){

    }
    public static void knightPattern(int index){

    }
    public static void bishopPattern(int index){

    }
    public static void kingPattern(int index){

    }
    public static void queenPattern(int index){

    }


    public static void gameLoop(){
        startPosition();
        printBoard();
        while(running){
            //System.out.println(Arrays.toString(pieces));
            System.out.println("White turn: " + whiteTurn);
            getInput();

        }

    }



    public static void main(String[] args) {
        gameLoop();

        System.out.println("You exited the game.");

        //System.out.println("Welcome to Chess Game!");
    }
}
