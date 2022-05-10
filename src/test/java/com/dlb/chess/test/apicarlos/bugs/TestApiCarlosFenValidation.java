package com.dlb.chess.test.apicarlos.bugs;

import org.junit.jupiter.api.Test;

import com.dlb.chess.fen.constants.FenConstants;
import com.github.bhlangonijr.chesslib.Board;

class TestApiCarlosFenValidation {

  private static final boolean IS_WORKING = false;

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    if (IS_WORKING) {
      testFen();
      testFenHavingMove();
    }
  }

  private static void testFenHavingMove() throws Exception {
    final Board board = new Board();
    // invalid side field
    board.loadFromFen("r1bqk1nr/pppp1ppp/2n5/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQK2R x KQkq - 0 4");
    // board assumes black has the move
    System.out.println(board.getSideToMove()); // BLACK
  }

  private static void testFen() throws Exception {
    final Board board = new Board();
    // initial position - accepted ok
    board.loadFromFen(FenConstants.FEN_INITIAL_STR);

    // invalid chars
    // position field contains invalid letter - exception - ok
    // board.loadFromFen("xnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    // side field contains invalid letter - accepted - not ok
    board.loadFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR x KQkq - 0 1");
    // castling field contains invalid letter - accepted - not ok
    board.loadFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w xQkq - 0 1");
    // en passant field contains invalid letter - exception - ok
    // board.loadFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq x 0 1");
    // halfmove clock contains invalid letter - exception - ok
    // board.loadFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - x 1");
    // move counter contains invalid letter - exception - ok
    // board.loadFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 x");

    // non possible values
    // position field contains non complete rank - accepted - not ok
    board.loadFromFen("rnbqkbnr/pppppppp/7/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

    // en passant field contains non possible value for position - accepted - not ok
    board.loadFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq e3 0 1");

    // position field contains non possible value (opponent king in check) - accepted - not ok
    board.loadFromFen("4k3/8/8/8/8/8/4R3/4K3 w - - 0 1");

    // castling field contains non possible value for position - accepted - not ok
    board.loadFromFen("4k3/8/8/8/8/8/8/4KR2 w K - 0 1");

    // halfmove counter contains non possible value (too big in regard to fullMoveNumber) - accepted - not ok
    board.loadFromFen("4k3/8/8/8/8/8/8/4KR2 w K - 500 50");

    System.out.println(board.getSideToMove());

  }

}
