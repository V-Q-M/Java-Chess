import java.util.Arrays;

public class ChessBot {
    static int searchRange = 63;
    // KEYS
    final static int pawnValue = 10;
    final static int knightValue = 30;
    final static int bishopValue = 30;
    final static int rookValue = 50;
    final static int queenValue = 90;

    static int[] rookMap = {5,0,0,0,0,0,0,5,
                            0,0,0,0,0,0,0,0,
                            0,0,0,0,0,0,0,0,
                            0,0,0,0,0,0,0,0,
                            0,0,0,0,0,0,0,0,
                            0,0,0,0,0,0,0,0,
                            0,0,0,0,0,0,0,0,
                            5,0,0,0,0,0,0,5};

    static int[] pawnMap = {0,0,0,0,0,0,0,0,
                            0,0,0,0,0,0,0,0,
                            0,0,0,0,0,0,0,0,
                            0,0,0,0,0,0,0,0,
                            5,5,5,7,7,5,5,5,
                            1,1,1,1,1,1,1,1,
                            0,0,0,0,0,0,0,0,
                            5,5,5,5,5,5,5,5};

    static int[] knightMap = {-10,-10,-10,-10,-10,-10,-10,-10,
                            10,12,15,15,15,15,12,10,
                            15,15,20,20,20,20,15,15,
                            15,15,20,20,20,20,15,15,
                            15,15,20,20,20,20,15,15,
                            15,15,20,20,20,20,15,15,
                            10,12,15,15,15,15,12,10,
                            -10,-10,-10,-10,-10,-10,-10,-10};

    static int[] bishopMap = {5,0,0,0,0,0,0,5,
                            0,6,0,0,0,0,6,0,
                            0,0,0,0,0,0,0,0,
                            0,0,0,15,15,0,0,0,
                            0,0,0,15,15,0,0,0,
                            0,0,0,0,0,0,0,0,
                            0,10,0,0,0,0,10,0,
                            5,-5,-5,-5,-5,-5,-5,5};

    static int[] queenMap ={5,5,5,0,0,5,5,5,
                            0,0,0,0,0,0,0,0,
                            0,0,0,0,0,0,0,0,
                            0,0,0,0,0,0,0,0,
                            5,5,5,5,5,5,5,5,
                            2,5,7,10,10,7,5,2,
                            -5,5,5,5,5,5,5,-5,
                            5,-5,-5,-5,-5,-5,-5,5};

    static int[] kingMap = {-10,-10,-10,-10,-10,-10,-10,-10,
                            -10,-10,-10,-10,-10,-10,-10,-10,
                            -10,-10,-10,-10,-10,-10,-10,-10,
                            -10,-10,-10,-10,-10,-10,-10,-10,
                            -10,-10,-10,-10,-10,-10,-10,-10,
                            -2,-3,-5,-5,-5,-5,-3,-2,
                            2,2,2,1,1,2,2,2,
                            10,5,5,5,5,5,5,10};

    static int[] squareOffsets = {};

    // Very simple AI. Takes the first piece it sees and moves it to the first location it sees
    // Optimizations:
    // - Make it prioritize moving higher value pieces
    // - Make it prioritize squares with high scores. Need some values for that
    // - To do that we add the piece Values to the allowedMoves/ allowedAttack arrays
    // - Need to add a layer, so every move also checks enemy response

    private static void offsetMap(int i, boolean blackTurn){
            if(blackTurn){
                switch (Chess.pieces[i]) {
                    case Chess.blackPawn:
                        for (int j = 0; j < 64; j++) {
                            if (Chess.allowedMoves[j] != 0) {
                                Chess.allowedMoves[j] += pawnMap[j];
                            }
                        }
                        break;
                    case Chess.blackKnight:
                        for (int j = 0; j < 64; j++) {
                            if (Chess.allowedMoves[j] != 0) {
                                Chess.allowedMoves[j] += knightMap[j];
                            }
                        }
                        break;
                    case Chess.blackRook:
                        for (int j = 0; j < 64; j++) {
                            if (Chess.allowedMoves[j] != 0) {
                                Chess.allowedMoves[j] += rookMap[j];
                            }
                        }
                        break;
                    case Chess.blackBishop:
                        for (int j = 0; j < 64; j++) {
                            if (Chess.allowedMoves[j] != 0) {
                                Chess.allowedMoves[j] += bishopMap[j];
                            }
                        }
                        break;
                    case Chess.blackQueen:
                        for (int j = 0; j < 64; j++) {
                            if (Chess.allowedMoves[j] != 0) {
                                Chess.allowedMoves[j] += queenMap[j];
                            }
                        }
                        break;
                    case Chess.blackKing:
                        for (int j = 0; j < 64; j++) {
                            if (Chess.allowedMoves[j] != 0) {
                                Chess.allowedMoves[j] += kingMap[j];
                            }
                        }
                }
            }

    }


    private static void evaluateSquareWorth(boolean blackTurn, int[] allowedAttacksCopy, int[] allowedMovesCopy){
        for (int i = 0; i < 64; i++) {
            if (blackTurn) {
                if (Chess.allowedAttacks[i] != 0  && Pieces.isInWhite(Chess.pieces[i])) { // If its a white square
                    switch (Chess.pieces[i]) {
                        case Chess.whitePawn:
                            Chess.allowedAttacks[i] += pawnValue;
                            Chess.allowedMoves[i] += pawnValue;
                            break;
                        case Chess.whiteKnight:
                            Chess.allowedAttacks[i] += knightValue;
                            Chess.allowedMoves[i] += knightValue;
                            break;
                        case Chess.whiteBishop:
                            Chess.allowedAttacks[i] += bishopValue;
                            Chess.allowedMoves[i] += bishopValue;
                            break;
                        case Chess.whiteRook:
                            Chess.allowedAttacks[i] += rookValue;
                            Chess.allowedMoves[i] += rookValue;
                            break;
                        case Chess.whiteQueen:
                            Chess.allowedAttacks[i] += queenValue;
                            Chess.allowedMoves[i] += queenValue;
                            break;
                    }
                }
            } else {
                if (allowedAttacksCopy[i] != 0 && Pieces.isInWhite(Chess.pieces[i])) {
                    switch (Chess.pieces[i]) {
                        case Chess.blackPawn:
                            allowedAttacksCopy[i] -= pawnValue;
                            allowedMovesCopy[i] -= pawnValue;
                            break;
                        case Chess.blackKnight:
                            allowedAttacksCopy[i] -= knightValue;
                            allowedMovesCopy[i] -= knightValue;
                            break;
                        case Chess.blackBishop:
                            allowedAttacksCopy[i] -= bishopValue;
                            allowedMovesCopy[i] -= bishopValue;
                            break;
                        case Chess.blackRook:
                            allowedAttacksCopy[i] -= rookValue;
                            allowedMovesCopy[i] -= rookValue;
                            break;
                        case Chess.blackQueen:
                            allowedAttacksCopy[i] -= queenValue;
                            allowedMovesCopy[i] -= queenValue;
                            break;

                    }
                }
            }
        }
    }

    public static void aiFirstSteps(){
       // System.out.println("Hi, I am ChessBot.");
        if (Chess.isSelecting) {
            aiMoveSelection(true);
        } else {
            aiMoveExecution(true);
        }
    }

    public static int[] findTheMaximumAlgorithm(){
        int maximum = 0;
        int indexOfMax;
        int[] result = new int[]{-1,- 1};
     //   System.out.println(Arrays.toString(Chess.allowedAttacks));
        for (int i = 0; i < 64; i++) {
            if (Chess.allowedMoves[i] > maximum){
                maximum = Chess.allowedMoves[i];
                indexOfMax = i;
                result[0] = maximum;
                result[1] = indexOfMax;
            }
        }
        return result;
    }

    static int highestValueYet = -1;
    static int highestValueTargetSquare = -1;
    static int highestValueStartingSquarePosition = -1;

    private static void aiMoveSelection(boolean blackTurn){
        highestValueYet = -1;
        highestValueTargetSquare = -1;
        highestValueStartingSquarePosition = -1;

        if (searchRange == 63) {
           for (int i = 63; i >= 0; i--) {
               Arrays.fill(Chess.allowedAttacks, 0);
               Arrays.fill(Chess.allowedMoves, 0);

               if (Pieces.isInBlack(Chess.pieces[i])) {  // Is a black piece
                   if (Chess.pieceSelection(i, true, Chess.allowedAttacks, Chess.allowedMoves)) { // No bugs happend and filled allowed Attacks
                       evaluateSquareWorth(blackTurn, Chess.allowedAttacks, Chess.allowedMoves);

                       offsetMap(i, blackTurn);

                       int[] currentMaximum = findTheMaximumAlgorithm();
                       System.out.println("highest value" + highestValueYet);
                       System.out.println("currentMax" + currentMaximum[0]);
                       if (currentMaximum[0] > highestValueYet) {
                           highestValueYet = currentMaximum[0];
                           highestValueStartingSquarePosition = i;
                           highestValueTargetSquare = currentMaximum[1];
                       }
                   }
               }
           }
           // Best attack move
           if (highestValueStartingSquarePosition != -1) {
               Chess.pieceSelection(highestValueStartingSquarePosition, false, Chess.allowedAttacks, Chess.allowedMoves);
               System.out.println("Computer: Found a valuable move from "+ ((char)('a' + (highestValueStartingSquarePosition % 8))) + (highestValueStartingSquarePosition / 8 + 1) +
                       " to " + ((char)('a' + (highestValueTargetSquare % 8))) + (highestValueTargetSquare / 8 + 1));
           }
       } else {
           highestValueYet = -1;
           highestValueTargetSquare = -1;
           highestValueStartingSquarePosition = -1;


            Arrays.fill(Chess.allowedAttacks, 0);
            Arrays.fill(Chess.allowedMoves, 0);

            // If nothing of value found, select sequentially
            for (int i = searchRange; i >= 0; i--) {
              //  System.out.println("Loop in Question is working...");
                if (Pieces.isInBlack(Chess.pieces[i])) {
                    Arrays.fill(Chess.allowedAttacks, 0);
                    Arrays.fill(Chess.allowedMoves, 0);
                   // Chess.moveValidation(i);
                    Chess.pieceSelection(i, true,Chess.allowedAttacks, Chess.allowedMoves);
                        Chess.isSelecting = false;
                        System.out.println("Computer: Chose fallback move at " + i);
                        System.out.println("Computer: At least I found something...");
                        System.out.println("isSelecting = " + Chess.isSelecting);
                        break; // optimally has unselected itself
                }
            }
       }
       Chess.isSelecting = false;

    }


    private static void aiMoveExecution(boolean blackTurn){

        if (highestValueTargetSquare >= 0) {
            System.out.println("SelectedPiece = " + Chess.selectedPiece);
            if (!Chess.gameLogic(highestValueTargetSquare, Chess.selectedPiece)) {
                aiMoveSelection(blackTurn);
            }
            //Chess.gameLogic(highestValueTargetSquare, Chess.selectedPiece);
            System.out.println("Computer: I chose to attack " + ((char)('a' + (highestValueTargetSquare % 8))) + (highestValueTargetSquare / 8 + 1));
            Chess.isSelecting = true;
            highestValueYet = -1;
            highestValueTargetSquare = -1;
            highestValueStartingSquarePosition = -1;

            //Visuals.updateGuiBoard();
        } else {
            for (int i = 63; i >= 0; i--) {
                if (Chess.allowedAttacks[i] != 0 || Chess.allowedMoves[i] != 0) {
                    Chess.gameLogic(i, Chess.selectedPiece);
                    System.out.println("Computer: I chose " + ((char)('a' + (i % 8))) + (i / 8 + 1) );
                    highestValueYet = -1;
                    highestValueTargetSquare = -1;
                    highestValueStartingSquarePosition = -1;

                    //Visuals.updateGuiBoard();
                    break;
                }
            }
            if (!Chess.isSelecting) {
                //System.out.println("Ai: Damn. Found no target");
                Chess.isSelecting = true;
                searchRange--;
                System.out.println("Search Range: " + searchRange);
            }
            if (searchRange == 0) {
                Arrays.fill(Chess.allowedAttacks, 0);
                Arrays.fill(Chess.squareColors, 0);
                Chess.whiteTurn = true;
                Visuals.printBoard();

                System.out.println("Computer: Found absolutely nothing");
                System.out.println("Computer: I'm probably mate. GG!");

                //Visuals.updateGuiBoard();
            }
        }
    }
}