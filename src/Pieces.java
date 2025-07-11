public class Pieces {

    // Simple check, more for readability purposes
    public static boolean isInWhite(int piece) {
        return piece >= 1 && piece <= 6;
    }

    public static boolean isInBlack(int piece){
        return piece >= 11 && piece <= 16;
    }

    // Movement patters
    // Black pawn is whitePawn reversed
    // Knight and King are unique
    // Queen uses rook + bishop moves

    // Helper function
    public static boolean decideMove(int i, int color, int[] allowedAttacks, int[] allowedMoves){
        if (Chess.pieces[i] == Chess.emptySquare) {
            allowedMoves[i] = 1;
        }
        else if ((isInBlack(Chess.pieces[i]) && color == 0) || (isInWhite(Chess.pieces[i]) && color == 1)) {
            allowedAttacks[i] = 1;
            return true;
        }
        else if ((isInBlack(Chess.pieces[i]) && color == 1) || (isInWhite(Chess.pieces[i]) && color == 0)) {
            return true;
        }
        return false;
    }

    public static void simplifiedDecideMove(int target, int color, int[] allowedAttacks, int[] allowedMoves){
        if (Chess.pieces[target] == Chess.emptySquare){
            allowedMoves[target] = 1;
        } else if ((isInBlack(Chess.pieces[target]) && color == 0) || (isInWhite(Chess.pieces[target]) && color == 1)) {
            allowedAttacks[target] = 1;
        }
    }

    public static void undoMove(int move, int oldPosition, int capturedPiece, int selectedPiece){
        Chess.pieces[oldPosition] = selectedPiece;
        Chess.pieces[move] = capturedPiece;
    }

    public static void whitePawnPattern(int index, int[] allowedAttacks, int[] allowedMoves){
        int target = index + 8;
        if (target < 64) {
            if (Chess.pieces[target] == Chess.emptySquare){
                allowedMoves[target] = 1;
            }
        }
        target = index + 16;
        if ((target < 64) && index < 16) {
            if (Chess.pieces[target] == Chess.emptySquare) {
                allowedMoves[target] = 1;
            }
        }

        target = index + 9;
        if ((target < 64) && ((target % 8) != 0)) {
            if (isInBlack(Chess.pieces[target])) {
                allowedAttacks[target] = 1;
            }
        }
        target = index + 7;
        if ((target < 64) && ((target % 8) != 7)) {
            if (isInBlack(Chess.pieces[target])) {
                allowedAttacks[target] = 1;
            }
        }

    }

    public static void blackPawnPattern(int index, int[] allowedAttacks, int[] allowedMoves){
        int target = index - 8;
        if (target >= 0) {
            if (Chess.pieces[target] == Chess.emptySquare) {
                allowedMoves[target] = 1;
            }

        }
        target = index - 16;
        if ((target >= 0) && (index >= 48)) {
            if (Chess.pieces[target] == Chess.emptySquare) {
                allowedMoves[target] = 1;
            }
        }
        target = index - 9;
        if ((target >= 0) && ((target % 8) != 7)) {
            if (isInWhite(Chess.pieces[target])) {
                allowedAttacks[target] = 1;
            }
        }
        target = index - 7;
        if ((target >= 0) && ((target % 8) != 0)) {
            if (isInWhite(Chess.pieces[target])) {
                allowedAttacks[target] = 1;
            }
        }

    }


    public static void rookPattern(int index, int color, int[] allowedAttacks, int[] allowedMoves){
        // up
        for (int i = index + 8; i < 64; i += 8) {
            if (decideMove(i, color, allowedAttacks, allowedMoves)) { // If returns true, there is collision and it should be stopped
                break;
            }
        }
        // down
        for (int i = index - 8; i >= 0; i -= 8) {
            if (decideMove(i, color, allowedAttacks, allowedMoves)) { // If returns true, there is collision and it should be stopped
                break;
            }
        }
        // right
        for (int i = index + 1; i % 8 != 0; i++) {
            if (decideMove(i, color, allowedAttacks, allowedMoves)) { // If returns true, there is collision and it should be stopped
                break;
            }
        }
        // left
        for (int i = index - 1; i % 8 != 7 && i >= 0; i--) {
            if (decideMove(i, color, allowedAttacks, allowedMoves)) { // If returns true, there is collision and it should be stopped
                break;
            }
        }
    }

    public static void knightPattern(int index, int color, int[] allowedAttacks, int[] allowedMoves){
        int col = index % 8;

        int target = index + 16; // up right
        if (target < 63) {
            if(col > 0) simplifiedDecideMove(target - 1, color, allowedAttacks, allowedMoves);
            if(col < 7) simplifiedDecideMove(target + 1, color, allowedAttacks, allowedMoves);
        }
        target -= 8;        //target = index + 8;
        if (target < 62) {
            if(col > 1) simplifiedDecideMove(target - 2, color, allowedAttacks, allowedMoves);
            if(col < 6) simplifiedDecideMove(target + 2, color, allowedAttacks, allowedMoves);
        }
        target -= 16;        //target = index - 8;
        if (target > 1) {
            if(col > 1) simplifiedDecideMove(target - 2, color, allowedAttacks, allowedMoves);
            if(col < 6) simplifiedDecideMove(target + 2, color, allowedAttacks, allowedMoves);
        }
        target -= 8;         //target = index - 16;
        if (target > 0) {
            if(col > 0) simplifiedDecideMove(target - 1, color, allowedAttacks, allowedMoves);
            if(col < 7) simplifiedDecideMove(target + 1, color, allowedAttacks, allowedMoves);
        }


    }

    public static void bishopPattern(int index, int color, int[] allowedAttacks, int[] allowedMoves){
        // up
        for (int i = index + 9; i < 64; i += 9) {
            if (i % 8 == 0) {
                break;
            }
            if (decideMove(i, color, allowedAttacks, allowedMoves)) { // If returns true, there is collision and it should be stopped
                break;
            }
        }
        // down
        for (int i = index - 9; i >= 0; i -= 9) {
            if (i % 8 == 7) {
                break;
            }
            if (decideMove(i, color, allowedAttacks, allowedMoves)) { // If returns true, there is collision and it should be stopped
                break;
            }
        }
        // up
        for (int i = index + 7; i < 64; i += 7) {
            if (i % 8 == 7) {
                break;
            }
            if (decideMove(i, color, allowedAttacks, allowedMoves)) { // If returns true, there is collision and it should be stopped
                break;
            }
        }
        // down
        for (int i = index - 7; i >= 0; i -= 7) {
            if (i % 8 == 0) {
                break;
            }
            if (decideMove(i, color, allowedAttacks, allowedMoves)) { // If returns true, there is collision and it should be stopped
                break;
            }
        }
    }

    public static void kingPattern(int index, int color, int[] allowedAttacks, int[] allowedMoves){
        int target = index + 8;
        if (target < 64) {
            simplifiedDecideMove(target, color, allowedAttacks, allowedMoves);
        }
        target = index - 8;
        if (0 <= target) {
            simplifiedDecideMove(target, color, allowedAttacks, allowedMoves);
        }
        target = index + 1;
        if (target  < 64){
            simplifiedDecideMove(target, color, allowedAttacks, allowedMoves);
        }
        target = index - 1;
        if (0 <= target) {
            simplifiedDecideMove(target, color, allowedAttacks, allowedMoves);
        }
        target = index + 9;
        if (target  < 64 && target % 8 != 0){
            simplifiedDecideMove(target, color, allowedAttacks, allowedMoves);
        }
        target = index + 7;
        if (target  < 64 && target % 8 != 7){
            simplifiedDecideMove(target, color, allowedAttacks, allowedMoves);
        }
        target = index - 9;
        if (target >= 0 && target % 8 != 7) {
            simplifiedDecideMove(target, color, allowedAttacks, allowedMoves);
        }
        target = index - 7;
        if (target >= 0 && target % 8 != 0) {
            simplifiedDecideMove(target, color, allowedAttacks, allowedMoves);
        }

        // Castling Logic


            // Kingside castle (e1 to g1 -> index 4 to 6)
            if (Chess.pieces[5] == Chess.emptySquare && Chess.pieces[6] == Chess.emptySquare) {
                allowedMoves[6] = 1;
            }
            // Queenside castle (e1 to c1 -> index 4 to 2)
            if (
                    Chess.pieces[3] == Chess.emptySquare &&
                            Chess.pieces[2] == Chess.emptySquare &&
                            Chess.pieces[1] == Chess.emptySquare
            ) {
                allowedMoves[2] = 1;
            }
    }

    public static void queenPattern(int index, int color, int[] allowedAttacks, int[] allowedMoves) {
        rookPattern(index, color, allowedAttacks, allowedMoves);
        bishopPattern(index, color, allowedAttacks, allowedMoves);
    }

}
