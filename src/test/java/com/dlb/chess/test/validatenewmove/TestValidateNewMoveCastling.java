package com.dlb.chess.test.validatenewmove;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.enums.MoveCheck;

class TestValidateNewMoveCastling extends AbstractTestValidateNewMove {

  @Test
  @SuppressWarnings("static-method")
  void testKingOrRookNotOnRequiredSquare() {
    // white
    // king-side
    // king only
    {
      final var fen = "rnbqkbnr/ppp2ppp/3p4/4p3/8/5P2/PPPPPKPP/RNBQ1BNR w kq - 0 3";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.KING_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE);
    }
    // rook only
    {
      final var fen = "rnbqkbr1/pppppppp/5n2/8/8/5N2/PPPPPPPP/RNBQKBR1 w Qq - 4 3";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.KING_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE);
    }
    // king and rook
    {
      final var fen = "r1bqk2r/ppppbppp/2n2n2/4p3/4P3/5N2/PPPPKPPP/RNBQ1BR1 w kq - 4 5";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.KING_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE);
    }
    // queen-side
    // king only
    {
      final var fen = "r1bqkbnr/pppp1ppp/2n5/4p3/4P3/8/PPPPKPPP/RNBQ1BNR w kq - 2 3";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE);
    }
    // rook only
    {
      final var fen = "rnbqkbnr/ppp2ppp/4p3/3p4/P7/8/RPPPPPPP/1NBQKBNR w Kkq - 0 3";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE);
    }
    // king and rook
    {
      final var fen = "r1bqk2r/ppppbppp/2n2n2/4p3/8/2N1P3/PPPPKPPP/1RBQ1BNR w kq - 3 5";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE);
    }

    // black
    // king-side
    // king only
    {
      final var fen = "rnbk1bnr/ppqppppp/2p5/8/8/P1P2N2/1PQPPPPP/RNB1KB1R b KQ - 0 4";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.KING_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE);
    }
    // rook only
    {
      final var fen = "rnbqkbn1/ppppp1pr/5p2/7p/4P2P/2N2N2/PPPP1PP1/R1BQKB1R b KQq - 2 4";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.KING_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE);
    }
    // king and rook
    {
      final var fen = "rnbq1bn1/ppppkppr/8/4p2p/4P3/P1N2N2/1PPPBPPP/R1BQK2R b KQ - 0 5";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.KING_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE);
    }
    // queen-side
    // king only
    {
      final var fen = "rnbq1bnr/ppppkppp/8/1B2p3/4P3/N7/PPPP1PPP/R1BQK1NR b KQ - 3 3";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE);
    }
    // rook only
    {
      final var fen = "1nbqkbnr/rppppppp/B7/p7/4P1Q1/8/PPPP1PPP/RNB1K1NR b KQk - 3 3";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE);
    }
    // king and rook
    {
      final var fen = "1rbq1k1r/ppppppbp/n4np1/8/4P3/1PNB1N2/PBPP1PPP/R2Q1RK1 b - - 4 7";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE);
    }
  }

  @Test
  @SuppressWarnings("static-method")
  void testNoCastlingRight() {
    // white
    // king-side - lost king-side
    {
      final var fen = "rnbqk2r/pppp1ppp/5n2/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w Qkq - 0 25";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.KING_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE);
    }
    // king-side - lost both
    {
      final var fen = "rnbqk2r/pppp1ppp/5n2/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w kq - 0 25";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.KING_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE);
    }

    // queen-side - lost queen-side
    {
      final var fen = "rnb1kbnr/pp2pppp/2pq4/3p4/2P5/2NP4/PPQBPPPP/R3KBNR w Kkq - 0 25";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE);
    }
    // queen-side - lost both
    {
      final var fen = "rnb1kbnr/pp2pppp/2pq4/3p4/2P5/2NP4/PPQBPPPP/R3KBNR w kq - 0 25";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE);
    }

    // black
    // king-side - lost king-side
    {
      final var fen = "rnbqk2r/pppp1ppp/4pn2/2b5/8/1PN5/PBPPPPPP/R2QKBNR b KQq - 0 25";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.KING_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE);
    }
    // king-side - lost both
    {
      final var fen = "rnbqk2r/pppp1ppp/4pn2/2b5/8/1PN5/PBPPPPPP/R2QKBNR b KQ - 0 25";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.KING_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE);
    }

    // queen-side - lost queen-side
    {
      final var fen = "r3k2r/pbqpppbp/n1p2np1/1p6/3PP3/2N2N1P/PPPBBPP1/R2Q1RK1 b k - 0 25";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE);
    }
    // queen-side - lost both
    {
      final var fen = "r3k2r/pbqpppbp/n1p2np1/1p6/3PP3/2N2N1P/PPPBBPP1/R2Q1RK1 b - - 0 25";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE);
    }
  }

  @Test
  @SuppressWarnings("static-method")
  void testSquaresBetweenKingAndRookNotEmpty() {
    // white
    // king-side
    {
      final var fen = "r1bqkbnr/pppppppp/2n5/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 0 25";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.KING_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY);
    }
    // queen-side
    // destination squares empty
    {
      final var fen = "rnbqkbnr/pp2pppp/8/2pp4/8/8/PP2PPPP/RN2KBNR w KQkq - 0 25";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY);
    }
    // queen-side
    // destination squares not empty
    {
      final var fen = "rnbqkbnr/pp2pppp/8/2pp4/8/8/PP2PPPP/R1B1KBNR w KQkq - 0 25";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY);
    }

    // black
    // king-side
    {
      final var fen = "rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 0 25";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.KING_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY);
    }
    // queen-side
    // destination squares empty
    {
      final var fen = "rn2kbnr/pbppqppp/1p2p3/8/4P3/2N2N2/PPPPBPPP/R1BQK2R b KQkq - 0 25";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY);
    }
    // queen-side
    // destination squares not empty
    {
      final var fen = "rn1qkbnr/pbpp1ppp/1p2p3/8/4P3/2N2N2/PPPPBPPP/R1BQK2R b KQkq - 0 25";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY);
    }
  }

  @Test
  @SuppressWarnings("static-method")
  void testKingInCheck() {
    // white
    // king-side
    {
      final var fen = "rnbqk2r/pppp1ppp/5n2/4p3/2B1P3/2N2N2/PPPP1bPP/R1BQK2R w KQkq - 0 5";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.KING_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_4_KING_IN_CHECK);
    }
    // queen-side
    {
      final var fen = "rnb1kbnr/ppp2ppp/4p3/3p4/3P1B2/2NQ4/PPP1PqPP/R3KBNR w KQkq - 0 5";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_4_KING_IN_CHECK);
    }

    // black
    // king-side
    {
      final var fen = "r1bqk2r/1ppp1pNp/p1n2n2/2b1p3/2B1P3/8/PPPP1PPP/RNBQK2R b KQkq - 0 6";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.KING_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_4_KING_IN_CHECK);
    }
    // queen-side
    {
      final var fen = "r3kb1r/pbpp1ppp/1pn1Rq1n/7P/3P4/5NP1/PPP1PPB1/RNBQK3 b Qkq - 0 9";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_4_KING_IN_CHECK);
    }
  }

  @Test
  @SuppressWarnings("static-method")
  void testKingWouldTravelThroughCheck() {
    // white
    // king-side
    {
      final var fen = "rnb1kbnr/pppp2pp/5q2/8/2B5/7N/PPPP2PP/RNBQK2R w KQkq - 0 25";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.KING_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_5_KING_WOULD_TRAVEL_THROUGH_CHECK);
    }
    // queen-side
    {
      final var fen = "rnbqkbnr/pp2pppp/8/8/5B2/2N2Q2/PPP1PPPP/R3KBNR w KQkq - 0 25";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_5_KING_WOULD_TRAVEL_THROUGH_CHECK);
    }

    // black
    // king-side
    {
      final var fen = "rnbqk2r/ppppppbp/4Nnp1/8/8/8/PPPPPPPP/R1BQKBNR b KQkq - 0 25";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.KING_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_5_KING_WOULD_TRAVEL_THROUGH_CHECK);
    }
    // queen-side
    {
      final var fen = "r3kbnr/pbqp1ppp/1pn1p3/2p3B1/1P1P4/2P5/P3PPPP/RN1QKBNR b KQkq - 0 25";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_5_KING_WOULD_TRAVEL_THROUGH_CHECK);
    }
  }

  @Test
  @SuppressWarnings("static-method")
  void testKingEndInCheck() {
    // white
    // king-side
    {
      final var fen = "rnbqk1nr/pppp1ppp/4p3/2b5/2B1P3/5P1N/PPPP2PP/RNBQK2R w KQkq - 0 25";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.KING_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_6_KING_WOULD_END_IN_CHECK);
    }
    // queen-side
    {
      final var fen = "r1bqkbnr/ppp2ppp/3p4/4p3/1P6/NnP5/PBQPPPPP/R3KBNR w KQkq - 0 25";
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_6_KING_WOULD_END_IN_CHECK);
    }

    // black
    // king-side
    {
      final var fen = "rnbqk2r/ppppppbp/6pN/8/6n1/4P3/PPPP1PPP/RNBQKB1R b KQkq - 0 25";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.KING_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_6_KING_WOULD_END_IN_CHECK);
    }
    // queen-side
    {
      final var fen = "r3kbnr/pbp1pppp/2np1q2/1p6/8/1P4PB/P1PPPP1P/RNBQK1NR b KQkq - 0 25";
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE);
      check(fen, move, MoveCheck.CASTLING_PRIORITY_6_KING_WOULD_END_IN_CHECK);
    }
  }
}
