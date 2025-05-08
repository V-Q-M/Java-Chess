//--------------------------------------------------------------------------
// This class holds the main method of the game
// It contains the input logic
// It contains move validation
// It contains the check detection
//--------------------------------------------------------------------------
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Chess {
    // STATE
    static boolean running = true;
    static boolean isSelecting = true;

    // PLAYER
    static boolean whiteTurn = true;
    static boolean blackCheck = false;
    static boolean whiteCheck = false;

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

    // Needed for move checking
    static int selectedPiece = -1;
    static int oldPosition = -1;

    // PIECES
    static int[] pieces = new int[64]; // stores the positions of the pieces
    static int[] squareColors = new int[64]; // stores the color each square should have
    static boolean[] allowedMoves = new boolean[64]; // stores the allowed moves of one piece
    static boolean[] allowedAttacks = new boolean[64]; //Same thing but for attacks
    static Scanner scan = new Scanner(System.in);

    // SETTINGS
    public static int visualStyle = 1; //0 = Double letter pieces. 1 = Unicode pieces.
    public static boolean singlePlayer = true;

    // SETUP
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

    // INPUT
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
            } else {
                System.out.println("Invalid move");
            }
        } else {
            System.out.println("Invalid input");
        }
    }

    public static int translateInput(String input){ // Translation and sanitization of input
        int moveConverted = -1;
        String moveString = input.toLowerCase();

        char letter = moveString.charAt(0);  // the letter part (a-h)
        char number    = moveString.charAt(1);  // the digit part (1-8)
        // Translate
        int col = letter - 'a';  // 'a' to 'h' → 0 to 7
        int row = number - '1';  // '1' to '8' → 0 to 7

        if (0 <= col && col < 8 && 0 <= row && row < 8){
            // Formula for index
            moveConverted  = 8 * row + col;
        }

        return moveConverted;
    }


    // LOGIC
    public static void gameLogic(int move){
        // squarecolor, white = 0, red = 1, blue = 2
        // selection part
        Arrays.fill(squareColors, 0);
        Visuals.kingHasRedSquare();
        if (isSelecting){
            if ((whiteTurn && Pieces.isInWhite(pieces[move])) || (!whiteTurn && Pieces.isInBlack(pieces[move]))){
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
                Visuals.printBoard();
            } else {
                System.out.println("Not your piece...");
            }
        } else {
            // Moving part
            if (allowedMoves[move] || allowedAttacks[move]){
                Arrays.fill(allowedMoves, false);
                Arrays.fill(allowedAttacks, false);
                int savePiece = pieces[move];
                pieces[move] = selectedPiece; // Put new pieces to new position
                pieces[oldPosition] = emptySquare; //clear old square
                if (!checkDetectionWithoutMarker(!whiteTurn)) { // Checks if you need to defend the check
                    // Commit to move
                    checkDetection(whiteTurn); // Checks if the enemy is checked
                    // Pawn promotion
                    if (selectedPiece == whitePawn && move > 56){
                        pieces[move] = whiteQueen;
                    } else if (selectedPiece == blackPawn && move < 8){
                        pieces[move] = blackQueen;
                    }
                    Arrays.fill(squareColors, 0);
                    Visuals.kingHasRedSquare();
                    Visuals.printBoard();
                    Arrays.fill(allowedMoves, false);
                    Arrays.fill(allowedAttacks, false);
                    whiteTurn = !whiteTurn; // other players turn
                    // next player
                } else {
                    pieces[oldPosition] = selectedPiece;
                    pieces[move] = savePiece;
                    if(whiteTurn){
                        whiteCheck = false;
                    } else if (!whiteTurn) {
                        blackCheck = false;
                    }
                    Visuals.printBoard();
                    System.out.println("Move prohibited. Protect your king!");
                }

            } else {
                Visuals.printBoard();
                System.out.println("Invalid move");
            }
            Arrays.fill(allowedMoves, false);
            Arrays.fill(allowedAttacks, false);
            isSelecting = true;
        }
    }

    // MOVEMENT

    public static void moveValidation(int move){
        switch (pieces[move]){
            case whitePawn   -> Pieces.whitePawnPattern(move);
            case whiteRook   -> Pieces.rookPattern(move, 0);
            case whiteKnight -> Pieces.knightPattern(move, 0);
            case whiteBishop -> Pieces.bishopPattern(move, 0);
            case whiteQueen  -> Pieces.queenPattern(move, 0);
            case whiteKing   -> Pieces.kingPattern(move, 0);
            case blackPawn   -> Pieces.blackPawnPattern(move);
            case blackRook   -> Pieces.rookPattern(move, 1);
            case blackKnight -> Pieces.knightPattern(move, 1);
            case blackBishop -> Pieces.bishopPattern(move, 1);
            case blackQueen  -> Pieces.queenPattern(move, 1);
            case blackKing   -> Pieces.kingPattern(move, 1);
            default -> System.out.println("ERROR");
        }
    }

    public static boolean checkDetection(boolean checkWhite) {
        // For white side
        int kingPosition;
        if (checkWhite) {
            kingPosition = -1;
            for (int i = 0; i < 64; i++) {
                if (pieces[i] == blackKing) {
                    kingPosition = i;
                }
                if (Pieces.isInWhite(pieces[i])) {
                    moveValidation(i);
                }
            }
            for (int i = 0; i < 64; i++) {
                if (allowedAttacks[kingPosition]) {
                    System.out.println("Black King is check!");
                    blackCheck = true;
                    return true;
                } else {
                    blackCheck = false;
                }
            }
        } else if (checkWhite == false) {
            kingPosition = -1;
            for (int i = 0; i < 64; i++) {
                if (pieces[i] == whiteKing) {
                    kingPosition = i;
                }
                if (Pieces.isInBlack(pieces[i])) {
                    moveValidation(i);
                }
            }
            for (int i = 0; i < 64; i++) {
                if (allowedAttacks[kingPosition]) {
                    System.out.println("White King is check!");
                    whiteCheck = true;
                    return true;
                } else {
                    whiteCheck = false;
                }
            }
        }
        //System.out.println(Arrays.toString(allowedAttacks));
        return false;
    }

    public static boolean checkDetectionWithoutMarker(boolean checkWhite){
        // For white side
        if (checkWhite){
            int kingPosition = -1;
            for (int i = 0; i < 64; i++){
                if (pieces[i] == blackKing){
                    kingPosition = i;
                }
                if (Pieces.isInWhite(pieces[i])){
                    moveValidation(i);
                }
            }
            for (int i = 0; i < 64; i++) {
                if (allowedAttacks[kingPosition]){
                    System.out.println("Black King is check!");
                    return true;
                } else {
                    blackCheck = false;
                }
            }
        } else if (checkWhite == false) {
            int kingPosition = -1;
            for (int i = 0; i < 64; i++){
                if (pieces[i] == whiteKing){
                    kingPosition = i;
                }
                if (Pieces.isInBlack(pieces[i])){
                    moveValidation(i);
                }
            }
            for (int i = 0; i < 64; i++) {
                if (allowedAttacks[kingPosition]){
                    System.out.println("White King is check!");
                    return true;
                }
                else {
                    whiteCheck = false;
                }
            }
        }
        //System.out.println(Arrays.toString(allowedAttacks));
        return false;
    }




    // MAIN LOOP
    public static void gameLoop(){
        startPosition();
        Visuals.printBoard();
        while(running){
            //System.out.println(Arrays.toString(pieces));
            System.out.println("White turn: " + whiteTurn);
            getInput();

        }

    }



    public static void main(String[] args) {
        System.out.println("Welcome to Chess!");
        gameLoop();

        System.out.println("You exited the game.");

    }
}
