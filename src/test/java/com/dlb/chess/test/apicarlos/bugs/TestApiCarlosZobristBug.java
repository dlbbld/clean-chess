package com.dlb.chess.test.apicarlos.bugs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.test.apicarlos.NonNullWrapperApiCarlos;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveList;
import com.github.bhlangonijr.chesslib.pgn.PgnHolder;

class TestApiCarlosZobristBug {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestApiCarlosZobristBug.class);

  private static final boolean IS_RUN_ALL = true;

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {

    if (IS_RUN_ALL) {
      final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.LONGEST_MATE);
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        final String pgnFilePath = FileUtility.calculateFilePath(testCaseList.pgnTest().getFolderPath(),
            testCase.pgnFileName());
        logger.info(testCase.pgnFileName());
        testPrintingPosition(pgnFilePath);
        testWithoutPrintingPosition(pgnFilePath);
      }
      testPerformingBoardsMoves();
    }

  }

  private static void testPrintingPosition(String pgnFilePath) throws Exception {

    final PgnHolder pgn = new PgnHolder(pgnFilePath);

    pgn.loadPgn();
    final var game = NonNullWrapperCommon.getFirst(NonNullWrapperApiCarlos.getGames(pgn));
    game.loadMoveText();

    final var moves = game.getHalfMoves();
    final Board board = new Board();

    final Set<String> positionIdentifierSet = new TreeSet<>();
    final Set<Long> hashKeySet = new TreeSet<>();

    {
      positionIdentifierSet.add(calculatePositionIdentifier(board));
      final long hashKey = board.getHistory().getLast();
      hashKeySet.add(hashKey);
    }
    for (final Move move : moves) {
      board.doMove(move);
      {
        positionIdentifierSet.add(calculatePositionIdentifier(board));
        final long hashKey = board.getHistory().getLast();
        hashKeySet.add(hashKey);
      }
    }
    assertEquals(positionIdentifierSet.size(), hashKeySet.size());
    assertFalse(board.isRepetition());
  }

  private static void testWithoutPrintingPosition(String pgnFilePath) throws Exception {

    final PgnHolder pgn = new PgnHolder(pgnFilePath);
    pgn.loadPgn();
    final var game = NonNullWrapperCommon.getFirst(NonNullWrapperApiCarlos.getGames(pgn));
    game.loadMoveText();

    final Board board = new Board();
    for (final Move move : game.getHalfMoves()) {
      board.doMove(move);
    }
    assertFalse(board.isRepetition());
  }

  private static String calculatePositionIdentifier(Board board) {
    final StringBuilder identifier = new StringBuilder();

    identifier.append(board.getSideToMove()).append("_");

    var isEnPassantCapturePossible = false;
    for (final Move legalMove : MoveGenerator.generateLegalMoves(board)) {
      board.doMove(legalMove);
      final var moveBackup = board.getBackup().getLast();
      if (moveBackup.isEnPassantMove()) {
        isEnPassantCapturePossible = true;
        board.undoMove();
        break;
      }
      board.undoMove();
    }
    identifier.append(isEnPassantCapturePossible).append("_");

    identifier.append(board.getCastleRight().get(Side.WHITE)).append("/");
    identifier.append(board.getCastleRight().get(Side.BLACK)).append("_");

    final var fen = board.getFen();
    final var staticPosition = fen.substring(0, fen.indexOf(" "));

    identifier.append(staticPosition);

    return NonNullWrapperCommon.toString(identifier);
  }

  private static void testPerformingBoardsMoves() {
    final MoveList moveList = new MoveList(); // use MoveList to play moves on the board using SAN
    moveList.loadFromSan(
        "1. a4 b5 2. axb5 c6 3. bxc6 h5 4. g4 hxg4 5. f3 gxf3 6. cxd7+ Qxd7 7. Rxa7 fxe2 8. Qxe2 Qxd2+ 9. Bxd2 Rxh2 10. Rxa8 Rxh1 11. Rxb8 Rxg1 12. Rxc8+ Kd7 13. Rc7+ Kd8 14. Rxe7 Nf6 15. Nc3 Ne4 16. Bg5 Rxg5 17. Rxf7 Rg2 18. Qg4 Rxc2 19. Qxg7 Rxb2 20. Be2 Rxe2+ 21. Kd1 Ng5 22. Rf5 Rf2 23. Nd5 Rxf5 24. Nb4 Nf3 25. Nd3 Bb4 26. Qa1 Ke7 27. Kc2 Kf7 28. Kb3 Kg6 29. Ka4 Kg5 30. Nc5 Bc3 31. Kb5 Re5 32. Kb6 Rd5 33. Qa7 Rf5 34. Nd3 Re5 35. Qb7 Rf5 36. Qe7+ Kg6 37. Qe8+ Kg5 38. Kb7 Bd4 39. Ka6 Bc3 40. Kb7");

    final Board board = new Board();
    @SuppressWarnings("null") final Iterator<Move> moves = moveList.iterator();
    while (moves.hasNext()) {
      @SuppressWarnings("null") final Move move = moves.next();
      board.doMove(move);
    }
    assertFalse(board.isRepetition());
  }
}
