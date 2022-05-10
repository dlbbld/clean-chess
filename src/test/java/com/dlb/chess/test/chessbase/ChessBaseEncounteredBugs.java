package com.dlb.chess.test.chessbase;

public class ChessBaseEncounteredBugs {

  public static void main(String[] args) {
    System.out.println("chessbase overspecifies SAN");
    // chessbase overspecifies SAN if two same pieces could move to a square but one is pinned. Example:
    // seventyFive_03_1_last_move_checkmate_by_white_with_capture.pgn.
    // Chessbase generates 56... Nef5 but 56... Nf5 is correct.

    System.out.println("half-move clock in FEN is incorrect");
    // 1. e4 e5 2. Bc4 Bc5 3. Qf3 Nc6 4. Bb3 Bb4 5. Qc3 Bc5 6. Qc4 f6 7. Qd5 Na5 8. Qc4 Nc6 9. Qf7#
    // half-move clock after example 8... Nc6 is 0. like always 0. for the non capturing mating move it is correct as
    // the
    // correct result is zero, but as it seems just because it is always zero

  }

}
