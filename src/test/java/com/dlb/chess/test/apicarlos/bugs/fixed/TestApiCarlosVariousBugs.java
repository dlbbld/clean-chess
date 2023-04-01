package com.dlb.chess.test.apicarlos.bugs.fixed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveConversionException;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveList;

class TestApiCarlosVariousBugs {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestApiCarlosVariousBugs.class);

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {

    testPositionIdStableForUndoMove();

    testPositionId();
    testBoardEquals();
    testThreefoldSampled();
    testSanParseExceptionOnOverspecification();
    testInsufficientMaterialKbvKb();
    testInsufficientMaterialKbvKbMultiple();
    testSanGenerationCastlingCausingCheckOrCheckmate();

  }

  private static void testPositionIdStableForUndoMove() throws Exception {
    {
      final Board board = new Board();
      final Move e2e4 = new Move(Square.E2, Square.E4);
      final Move e7e5 = new Move(Square.E7, Square.E5);
      board.doMove(e2e4);
      board.doMove(e7e5);
      final var hashKeyAsIs = board.getIncrementalHashKey();

      board.undoMove();
      board.doMove(e7e5);
      final var hashKeyAsAfterRedo = board.getIncrementalHashKey();
      assertEquals(hashKeyAsIs, hashKeyAsAfterRedo);
    }
    {
      final MoveList moveList = new MoveList();
      moveList.loadFromSan("1. e4 e5 2. Nf3 Nc6 3. Bc4 Bc5 4. d4 a5");

      final Board board = new Board();
      for (final Move element : moveList) {
        board.doMove(element);
      }

      for (final Move move : MoveGenerator.generateLegalMoves(board)) {
        board.doMove(move);
        final var hashKeyAsIs = board.getIncrementalHashKey();

        board.undoMove();
        board.doMove(move);
        final var hashKeyAsAfterRedo = board.getIncrementalHashKey();

        assertEquals(hashKeyAsIs, hashKeyAsAfterRedo);

        board.undoMove();
      }
    }

  }

  private static void testPositionId() throws Exception {
    final Board board = new Board();
    board.loadFromFen("6k1/8/8/8/6p1/8/5PR1/6K1 w - - 4 32");
    board.doMove(new Move(Square.F2, Square.F4));

    assertEquals("6k1/8/8/8/5Pp1/8/6R1/6K1 b - f3 0 32", board.getFen());
    assertEquals("6k1/8/8/8/5Pp1/8/6R1/6K1 b --", board.getPositionId());

  }

  private static void testBoardEquals() throws Exception {
    final Board board1 = new Board();
    board1.doMove(new Move("b1c3", Side.WHITE));
    board1.doMove(new Move("b8c6", Side.BLACK));
    board1.doMove(new Move("c3b1", Side.WHITE));
    board1.doMove(new Move("c6b8", Side.BLACK));
    board1.doMove(new Move("b1c3", Side.WHITE));
    board1.doMove(new Move("b8c6", Side.BLACK));
    board1.doMove(new Move("c3b1", Side.WHITE));

    final Board board2 = new Board();
    board2.loadFromFen(board1.getFen());

    assertFalse(board1.strictEquals(board2));
  }

  private static void testThreefoldSampled() throws Exception {
    {
      // 01_threefold_fails_castle_right;
      final MoveList moveList = new MoveList();
      moveList.loadFromSan("1. h3 h6 2. Rh2 Na6 3. Rh1 Nb8 4. Nc3 Nc6 5. Nb1 Nb8 6. Nf3 Nf6 7. Ng1 Ng8");

      final Board board = new Board();
      for (final Move element : moveList) {
        board.doMove(element);
      }
      assertTrue(board.isRepetition());
    }

    {
      logger.info("4... Ng8");
      final MoveList moveList = new MoveList(); // use MoveList to play moves on the board using SAN
      moveList.loadFromSan("1. Nf3 Nf6 2. Ng1 Ng8 3. Nf3 Nf6 4. Ng1 Ng8");

      final Board board = new Board();
      for (final Move element : moveList) {
        board.doMove(element);
      }
      assertTrue(board.isRepetition());
    }
    {
      logger.info("5... Bf8");
      final MoveList moveList = new MoveList(); // use MoveList to play moves on the board using SAN
      moveList.loadFromSan("1. e4 e5 2. Be2 Be7 3. Bf1 Bf8 4. Bd3 Bd6 5. Bf1 Bf8");

      final Board board = new Board();
      for (final Move element : moveList) {
        board.doMove(element);
      }
      assertTrue(board.isRepetition());
    }
    {
      logger.info("After loosing castling right");
      final MoveList moveList = new MoveList();
      moveList.loadFromSan("1. e4 e5 2. Nf3 Nf6 3. Ng1 Ng8 4. Ke2 Ke7 5. Ke1 Ke8 6. Na3 Na6 7. Nb1 Nb8");

      final Board board = new Board();
      for (final Move element : moveList) {
        board.doMove(element);
      }
      assertFalse(board.isRepetition());
    }
    {
      logger.info("After two square advance");
      final MoveList moveList = new MoveList();
      moveList.loadFromSan("1. Nf3 Nf6 2. Nc3 c5 3. e3 d5 4. Be2 Ne4 5. Bf1 Nf6 6. Be2 Ne4 7. Bf1 Nf6");

      final Board board = new Board();
      for (final Move element : moveList) {
        board.doMove(element);
      }
      assertTrue(board.isRepetition());
    }
    {
      logger.info("Capablanca - Lasker 1921");
      final MoveList moveList = new MoveList();
      moveList.loadFromSan(
          "1. d4 d5 2. Nf3 Nf6 3. c4 e6 4. Bg5 Nbd7 5. e3 Be7 6. Nc3 O-O 7. Rc1 b6 8. cxd5 exd5 9. Qa4 c5 10. Qc6 Rb8 11. Nxd5 Bb7 12. Nxe7+ Qxe7 13. Qa4 Rbc8 14. Qa3 Qe6 15. Bxf6 Qxf6 16. Ba6 Bxf3 17. Bxc8 Rxc8 18. gxf3 Qxf3 19. Rg1 Re8 20. Qd3 g6 21. Kf1 Re4 22. Qd1 Qh3+ 23. Rg2 Nf6 24. Kg1 cxd4 25. Rc4 dxe3 26. Rxe4 Nxe4 27. Qd8+ Kg7 28. Qd4+ Nf6 29. fxe3 Qe6 30. Rf2 g5 31. h4 gxh4 32. Qxh4 Ng4 33. Qg5+ Kf8 34. Rf5 h5 35. Qd8+ Kg7 36. Qg5+ Kf8 37. Qd8+ Kg7 38. Qg5+ Kf8");

      final Board board = new Board();
      for (final Move element : moveList) {
        board.doMove(element);
      }
      assertTrue(board.isRepetition());
    }
    {
      logger.info("En passant capture not possible for own king in check");
      final Board board = new Board();
      board.loadFromFen("8/8/8/8/4p3/8/R2P3k/K7 w - - 0 37");

      board.doMove(new Move(Square.D2, Square.D4)); // initial position - two square pawn advance
      board.doMove(new Move(Square.H2, Square.H3)); // en passant capture not possible - own king in check

      board.doMove(new Move(Square.A2, Square.A3));
      board.doMove(new Move(Square.H3, Square.H2));

      board.doMove(new Move(Square.A3, Square.A2)); // twofold repetition
      board.doMove(new Move(Square.H2, Square.H1));

      board.doMove(new Move(Square.A2, Square.A3));
      board.doMove(new Move(Square.H1, Square.H2));

      board.doMove(new Move(Square.A3, Square.A2)); // threefold repetiton

      assertTrue(board.isRepetition());
    }
    {
      logger.info("En passant capture not possible for would expose own king to check");
      final Board board = new Board();
      board.loadFromFen("6k1/8/8/8/6p1/8/5PR1/6K1 w - - 0 32");

      board.doMove(new Move(Square.F2, Square.F4)); // initial position - two square pawn advance
      board.doMove(new Move(Square.G8, Square.F7)); // en passant capture not possible - would expose own king to check

      board.doMove(new Move(Square.G1, Square.F2));
      board.doMove(new Move(Square.F7, Square.G8));

      board.doMove(new Move(Square.F2, Square.G1)); // twofold repetition
      board.doMove(new Move(Square.G8, Square.H7));

      board.doMove(new Move(Square.G1, Square.H2));
      board.doMove(new Move(Square.H7, Square.G8));

      board.doMove(new Move(Square.H2, Square.G1)); // threefold repetiton

      assertTrue(board.isRepetition());
    }

    {
      logger.info("En passant capture possible");
      final MoveList moveList = new MoveList();
      moveList.loadFromSan("1. e4 Nf6 2. e5 d5 3. Bc4 Nc6 4. Bf1 Nb8 5. Bc4 Nc6 6. Bf1 Nb8");

      final Board board = new Board();
      for (final Move element : moveList) {
        board.doMove(element);
      }
      assertFalse(board.isRepetition());
    }

    {
      logger.info("Blatny - Holzke");
      final MoveList moveList = new MoveList();
      moveList.loadFromSan(
          "1. Nf3 Nf6 2. c4 c5 3. b3 d6 4. d4 cxd4 5. Nxd4 e5 6. Nb5 Be6 7. g3 a6 8. N5c3 d5 9. cxd5 Nxd5 10. Bg2 Bb4 11. Bd2 Nc6 12. O-O O-O 13. Na4 Rc8 14. a3 Be7 15. e3 b5 16. Nb2 Qb6 17. Nd3 Rfd8 18. Qe2 Nf6 19. Nc1 e4 20. Bc3 Nd5 21. Bxe4 Nxc3 22. Nxc3 Na5 23. N1a2 Nxb3 24. Rad1 Bc4 25. Qf3 Qf6 26. Qg4 Be6 27. Qe2 Rxc3 28. Nxc3 Qxc3 29. Rxd8+ Bxd8 30. Rd1 Be7 31. Bb7 Nc5 32. Qf3 g6 33. Bd5 Bxd5 34. Qxd5 Qxa3 35. Qe5 Ne6 36. Ra1 Qd6 37. Qxd6 Bxd6 38. Rxa6 Bc5 39. Kf1 Kf8 40. Ke2 Ke7 41. Kd3 Kd7 42. g4 Kc7 43. Ra8 Kc6 44. f4 Be7 45. Rc8+ Kd5 46. Re8 Kd6 47. g5 f5 48. Rb8 Kc6 49. Re8 Kd6 50. Rb8 Kc6 51. Re8 Kd6");

      final Board board = new Board();
      for (final Move element : moveList) {
        board.doMove(element);
      }
      assertFalse(board.isRepetition());
    }
  }

  private static void testSanParseExceptionOnOverspecification() throws Exception {
    {
      final MoveList moveList = new MoveList("4k3/8/8/8/1b6/2N5/8/4K1N1 w - - 0 1");
      var isException = false;
      try {
        moveList.addSanMove("Nge2", false, true);
      } catch (@SuppressWarnings("unused") final MoveConversionException e) {
        isException = true;
      }
      assertTrue(isException);
    }

  }

  /**
   * KBvKB with bishop on different color field is winnable.
   */
  private static void testInsufficientMaterialKbvKb() throws Exception {
    final Board board = new Board();

    logger.info("First example");
    final var bishopOnSameColorSquares = "8/8/8/4k3/5b2/3K4/8/2B5 w - - 0 1";
    board.loadFromFen(bishopOnSameColorSquares);
    assertTrue(board.isInsufficientMaterial());

    logger.info("Second example");
    final var bishopOnDifferentColorSquares = "8/8/8/4k3/5b2/3K4/2B5/8 w - - 0 1";
    board.loadFromFen(bishopOnDifferentColorSquares);
    assertFalse(board.isInsufficientMaterial());
  }

  private static void testInsufficientMaterialKbvKbMultiple() throws Exception {
    logger.info("First example");
    final Board board = new Board();
    board.loadFromFen("B3k3/8/8/8/8/8/8/4KB2 w - - 0 1");
    assertTrue(board.isInsufficientMaterial());

    logger.info("Second example");
    board.loadFromFen("B1b1k3/3b4/4b3/8/8/8/8/4KB2 w - - 0 1");
    assertTrue(board.isInsufficientMaterial());
  }

  private static void testSanGenerationCastlingCausingCheckOrCheckmate() throws Exception {

    {
      final MoveList moveList = new MoveList();
      final var san = "1. f4 f5 2. Nh3 Nf6 3. e4 fxe4 4. Bb5 Nh5 5. a3 Nxf4 6. a4 Nh5 7. a5 Kf7 8. O-O+";
      moveList.loadFromSan(san);
      final var moveListSan = moveList.toSanArray();
      assertNotNull(moveListSan);
      final var sanGeneratedLastMove = NonNullWrapperCommon.getLast(moveListSan);
      assertEquals("O-O+", sanGeneratedLastMove);
    }

    {
      final MoveList moveList = new MoveList();
      final var san = """
          1. d4 e6 2. Nf3 f5 3. Nc3 Nf6 4. Bg5 Be7 5. Bxf6 Bxf6 6. e4 fxe4 7. Nxe4 b6 \
          8. Ne5 O-O 9. Bd3 Bb7 10. Qh5 Qe7 11. Qxh7+ Kxh7 12. Nxf6+ Kh6 13. Neg4+ Kg5 14. h4+ Kf4 \
          15. g3+ Kf3 16. Be2+ Kg2 17. Rh2+ Kg1 18. O-O-O#""";
      moveList.loadFromSan(san);
      final var moveListSan = moveList.toSanArray();
      assertNotNull(moveListSan);
      final var sanGeneratedLastMove = NonNullWrapperCommon.getLast(moveListSan);
      assertEquals("O-O-O#", sanGeneratedLastMove);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testSanGenerationNoOverspecificationForPinnedPieceOne() throws Exception {
    final MoveList moveList = new MoveList();
    // 1. d4 e6 2. Nc3 Bb4 3. Nf3 Nc6 4. Ng5 h6 5. Ne4

    moveList.add(new Move(Square.D2, Square.D4));
    moveList.add(new Move(Square.E7, Square.E6));
    moveList.add(new Move(Square.B1, Square.C3));
    moveList.add(new Move(Square.F8, Square.B4));
    moveList.add(new Move(Square.G1, Square.F3));
    moveList.add(new Move(Square.B8, Square.C6));
    moveList.add(new Move(Square.F3, Square.G5));
    moveList.add(new Move(Square.H7, Square.H6));
    moveList.add(new Move(Square.G5, Square.E4));

    // correct, last move is not Nge4 because the knight on c3 cannot move to e4
    assertEquals("d4 e6 Nc3 Bb4 Nf3 Nc6 Ng5 h6 Ne4", moveList.toSan().trim());
  }

  @SuppressWarnings("static-method")
  @Test
  void testSanGenerationNoOverspecificationForPinnedPieceTwo() throws Exception {
    final MoveList moveList = new MoveList("4k3/8/8/8/1b6/2N5/8/4K1N1 w - - 0 1");

    // correct, first move is not Nge2 because the knight on c3 cannot move to e2
    moveList.add(new Move(Square.G1, Square.E2)); // Ne2
    assertEquals("Ne2", moveList.toSan().trim());

    // correct, first move is not Nge2 because the knight on c3 cannot move to e2
    moveList.add(new Move(Square.E8, Square.E7)); // Ne2
    assertEquals("Ne2 Ke7", moveList.toSan().trim());
  }
}
