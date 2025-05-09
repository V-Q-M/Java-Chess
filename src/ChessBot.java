import java.util.Arrays;

public class ChessBot {
    static int searchRange = 63;
    // KEYS
    final static int pawnValue = 1;
    final static int knightValue = 3;
    final static int bishopValue = 3;
    final static int rookValue = 5;
    final static int queenValue = 9;


    // Very simple AI. Takes the first piece it sees and moves it to the first location it sees
    // Optimizations:
    // - Make it prioritize moving higher value pieces
    // - Make it prioritize squares with high scores. Need some values for that
    // - To do that we add the piece Values to the allowedMoves/ allowedAttack arrays

    private static void evaluateSquareWorth(){
        for (int i = 0; i < 64; i++) {
            if (Chess.allowedAttacks[i] != 0  && Pieces.isInWhite(Chess.pieces[i])) { // If its a white square
                switch (Chess.pieces[i]) {
                    case Chess.whitePawn -> Chess.allowedAttacks[i] += pawnValue;
                    case Chess.whiteKnight-> Chess.allowedAttacks[i] += knightValue;
                    case Chess.whiteBishop-> Chess.allowedAttacks[i] += bishopValue;
                    case Chess.whiteRook-> Chess.allowedAttacks[i] += rookValue;
                    case Chess.whiteQueen-> Chess.allowedAttacks[i] += queenValue;
                }
            }
        }
    }


    public static void aiFirstSteps(){
       // System.out.println("Hi, I am ChessBot.");
        if (Chess.isSelecting) {
            aiMoveSelection();
        } else {
            aiMoveExecution();
        }
    }

    public static int[] findTheMaximumAlgorithm(){
        int maximum = 0;
        int indexOfMax;
        int[] result = new int[]{-1,- 1};
     //   System.out.println(Arrays.toString(Chess.allowedAttacks));
        for (int i = 0; i < 64; i++) {
            if (Chess.allowedAttacks[i] > maximum){
                maximum = Chess.allowedAttacks[i];
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

    private static void aiMoveSelection(){
       if (searchRange == 63) {
           for (int i = 63; i >= 0; i--) {
               Arrays.fill(Chess.allowedAttacks, 0);
               Arrays.fill(Chess.allowedMoves, 0);

               if (Pieces.isInBlack(Chess.pieces[i])) {  // Is a black piece
                   if (Chess.pieceSelection(i, true)) { // No bugs happend and filled allowed Attacks
                       evaluateSquareWorth();
                       int[] currentMaximum = findTheMaximumAlgorithm();

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
               Chess.pieceSelection(highestValueStartingSquarePosition, false);
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
                    Chess.pieceSelection(i, true);
                        Chess.isSelecting = false;
                        System.out.println("Computer: Chose fallback move at " + i);
                        System.out.println("Computer: At least I found something...");
                        System.out.println("isSelecting = " + Chess.isSelecting);
                        break; // optimally has unselected itself
                }
            }
       }
       Chess.isSelecting = false;

/*
        // Shouldnt reach this. If it does somethings wrong
        if (Chess.isSelecting) {
            Arrays.fill(Chess.allowedAttacks, 0);
            Arrays.fill(Chess.allowedMoves, 0);
            highestValueYet = 0;
            highestValueTargetSquare = 0;
            highestValueStartingSquarePosition = -1;
            System.out.println("AI: No move found ERRRRRROR");
            Chess.isSelecting = false;
        }*/
    }


    private static void aiMoveExecution(){

        if (highestValueTargetSquare >= 0) {
            System.out.println("SelectedPiece = " + Chess.selectedPiece);
            if (!Chess.gameLogic(highestValueTargetSquare, Chess.selectedPiece)) {
                aiMoveSelection();
            }
            //Chess.gameLogic(highestValueTargetSquare, Chess.selectedPiece);
            System.out.println("Computer: I chose to attack " + ((char)('a' + (highestValueTargetSquare % 8))) + (highestValueTargetSquare / 8 + 1));
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