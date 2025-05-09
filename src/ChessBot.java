public class ChessBot {
    static int searchRange = 63;

    // Very simple AI. Takes the first piece it sees and moves it to the first location it sees
    // Optimizations:
    // - Make it prioritize moving higher value pieces
    // - Make it prioritize squares with high scores. Need some values for that
    //

    public static void aiFirstSteps(){
        System.out.println("Hi, I am ChessBot.");
        if (Chess.isSelecting) {
            for (int i = searchRange; i >=0; i--) {
                if (Chess.pieceSelection(i)){
                    Chess.isSelecting = false;
                    System.out.println("Ai: I chose" + i);
                    break;
                }
            }
            if (Chess.isSelecting) {
                System.out.println("Ai: Damn. Found no target");
                Chess.isSelecting = false;
            }
        } else {
            for (int i = 63; i >= 0; i--) {
                if (Chess.allowedMoves[i] || Chess.allowedAttacks[i]) {
                    Chess.gameLogic(i, Chess.selectedPiece);
                    System.out.println("Ai: I chose" + i);
                    break;
                }
            }
            if (!Chess.isSelecting) {
                System.out.println("Ai: Damn. Found no move");
                Chess.isSelecting = true;
                //Chess.whiteTurn = true;
                searchRange--;
            }
        }
    }
}
