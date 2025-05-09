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
        System.out.println("Hi, I am ChessBot.");
        if (Chess.isSelecting) {
            aiMoveSelection();
        } else {
            aiMoveExecution();
        }
    }

    public static int[] findTheMaximumAlgorithm(){
        int maximum = -1;
        int indexOfMax;
        int[] result = new int[]{-1,- 1};

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
            System.out.println("AI: Found a valuable move from " + highestValueStartingSquarePosition +
                    " to " + highestValueTargetSquare);
        }

        // If nothing of value found, select sequentially
        for (int i = searchRange; i >= 0; i--) {
            if (Pieces.isInBlack(Chess.pieces[i])) {
                Arrays.fill(Chess.allowedAttacks, 0);
                Arrays.fill(Chess.allowedMoves, 0);
                Chess.moveValidation(i);
                if (Chess.allowedMoves[i] != 0) {
                    Arrays.fill(Chess.allowedAttacks, 0);
                    Arrays.fill(Chess.allowedMoves, 0);
                    Chess.pieceSelection(i, false);
                    System.out.println("AI: Chose fallback move at " + i);
                    return;
                }
            }
        }

        // Nothing found
        if (Chess.isSelecting) {
            Arrays.fill(Chess.allowedAttacks, 0);
            Arrays.fill(Chess.allowedMoves, 0);
            highestValueYet = 0;
            highestValueTargetSquare = 0;
            highestValueStartingSquarePosition = -1;
            System.out.println("AI: No move found");
            Chess.isSelecting = false;
        }
    }


    private static void aiMoveExecution(){

        if (highestValueTargetSquare >= 0) {
            Chess.gameLogic(highestValueTargetSquare, Chess.selectedPiece);
            System.out.println("Ai: I chose to attack" + highestValueTargetSquare);
        } else {
            for (int i = 63; i >= 0; i--) {
                if (Chess.allowedAttacks[i] != 0) {
                    Chess.gameLogic(i, Chess.selectedPiece);
                    System.out.println("Ai: I chose" + i);
                    break;
                }
            }
            if (!Chess.isSelecting) {
                System.out.println("Ai: Damn. Found no move");
                Chess.isSelecting = true;
                searchRange--;
                System.out.println("Search Range: " + searchRange);
            }
            if (searchRange == 0) {
                Arrays.fill(Chess.allowedAttacks, 0);
                Arrays.fill(Chess.squareColors, 0);
                Chess.whiteTurn = true;
                Visuals.printBoard();
                System.out.println("Ai: Found absolutely nothing");
            }
        }
    }
}