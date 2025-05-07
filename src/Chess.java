public class Chess {

    int emptySquare = 0;

    int whitePawn = 1;
    int whiteRook = 2;
    int whiteKnight = 3;
    int whiteBishop = 4;
    int whiteQueen = 5;
    int whiteKing = 6;

    int blackPawn = 11;
    int blackRook = 12;
    int blackKnight = 13;
    int blackBishop = 14;
    int blackQueen = 15;
    int blackKing = 16;


    int[] pieces = new int[64]; // stores the positions of the pieces
    boolean[] allowedMoves = new boolean[64]; // stores the allowed moves of one piece


    public static void startPosition(){

    }


    public static void main(String[] args) {
        startPosition();


        //System.out.println("Welcome to Chess Game!");
    }
}
