package com.dlb.chess.test.board;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.PgnFile;
import com.dlb.chess.san.StrictSanParser;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.parser.PgnCacheForStrictPgnParserTestCases;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;

/**
 * Verifies the SAN ↔ MoveSpecification consistency that {@link com.dlb.chess.board.Board#performMove(String)
 * Board.moveStrict(String)} relies on: once {@link SanValidation#validateSan SanValidation.validateSan} has produced a
 * MoveSpecification from a SAN, that MoveSpec is the canonical representation of the move and round-trips both ways.
 * The board therefore performs the move with no further re-validation of the spec.
 *
 * <h2>Forward (played moves)</h2>
 *
 * <p>
 * For each halfmove of each PGN: derive the MoveSpec via {@code validateSan(san, board)} <em>before</em> performing,
 * then perform via {@code board.moveStrict(san)} and assert:
 *
 * <ol>
 * <li>the derived MoveSpec equals the legal move that was actually played
 * ({@code board.getLastMove().moveSpecification()});</li>
 * <li>the SAN reconstructed from the legal move ({@code board.getSan()}) equals the original PGN SAN.</li>
 * </ol>
 *
 * <h2>Reverse (every legal move at every position)</h2>
 *
 * <p>
 * At each position reached during PGN playthrough, for <em>every</em> legal move from {@link Board#getLegalMoveSet
 * getLegalMoveSet}: perform the move so the board can compute its SAN, capture the SAN, unperform back to the original
 * position, then derive a fresh MoveSpec from that SAN via {@code validateSan} and assert it equals the LegalMove's
 * stored MoveSpec. This checks the round-trip on every legal move, not just the chosen line.
 *
 * <h2>Scope and runtime</h2>
 *
 * <p>
 * Iterates the parser-integration smoke list (~45 PGNs spanning every major parser code path — standard moves,
 * captures, en passant, promotion, castling, check, checkmate, custom starting positions). The reverse test is the
 * slower of the two but completes in seconds.
 */
class TestPerformMoveSanContract {

  private static final Logger logger = Nulls.getLogger(TestPerformMoveSanContract.class);

  @SuppressWarnings("static-method")
  @Test
  void testPlayedMoveSanMoveSpecRoundtrip() {
    for (final PgnFileTestCaseList testCaseList : CreatePgnTestCases.getParserIntegrationSmokeList()) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        logger.info(testCase.pgnFileName());
        verifyProvidedSanToCalculatedSan(testCaseList, testCase);
      }
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testAllLegalMovesSanMoveSpecRoundtrip() {
    for (final PgnFileTestCaseList testCaseList : CreatePgnTestCases.getParserIntegrationSmokeList()) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        logger.info(testCase.pgnFileName());
        verifyCalculatedSanToCalculatedMoveSpecification(testCaseList, testCase);
      }
    }
  }

  /**
   * Forward direction: for each played halfmove, derive MoveSpec from SAN, perform via SAN, then assert the played
   * LegalMove and the reconstructed SAN both match.
   */
  private static void verifyProvidedSanToCalculatedSan(PgnFileTestCaseList testCaseList, PgnFileTestCase testCase) {
    final PgnFile pgnFile = PgnCacheForStrictPgnParserTestCases.getPgn(testCaseList.pgnTest().getFolderPath(),
        testCase.pgnFileName());
    final Board board = new Board(pgnFile.startFen(), false);

    var halfMoveIndex = 0;
    for (final PgnHalfMove halfMove : pgnFile.halfMoveList()) {
      halfMoveIndex++;
      final var hmi = halfMoveIndex;
      final String expectedProvidedSan = halfMove.san();

      final MoveSpecification expectedCalculatedMoveSpecification = StrictSanParser
          .parseText(expectedProvidedSan, board).moveSpecification();

      board.moveStrict(expectedProvidedSan);

      final MoveSpecification actualStoredMoveSpecification = board.getLastMove().moveSpecification();
      assertEquals(expectedCalculatedMoveSpecification, actualStoredMoveSpecification,
          () -> testCase.pgnFileName() + ": halfmove " + hmi + " (" + expectedProvidedSan
              + ") — MoveSpec derived from SAN does not match the LegalMove's MoveSpec after perform");

      final String actualCalculatedSan = board.getSan();
      assertEquals(expectedProvidedSan, actualCalculatedSan, () -> testCase.pgnFileName() + ": halfmove " + hmi + " ("
          + expectedProvidedSan + ") — SAN reconstructed from LegalMove does not match the original PGN SAN");
    }
  }

  /**
   * Reverse direction: at each position, for every legal move (not just the played one), perform → capture SAN →
   * unperform → derive MoveSpec from SAN at the original position → assert it equals the LegalMove's stored MoveSpec.
   */
  private static void verifyCalculatedSanToCalculatedMoveSpecification(PgnFileTestCaseList testCaseList,
      PgnFileTestCase testCase) {
    final PgnFile pgnFile = PgnCacheForStrictPgnParserTestCases.getPgn(testCaseList.pgnTest().getFolderPath(),
        testCase.pgnFileName());
    final Board board = new Board(pgnFile.startFen(), false);

    for (final PgnHalfMove halfMove : pgnFile.halfMoveList()) {
      board.moveStrict(halfMove.san());
      final MoveSpecification expectedStoredMoveSpecification = board.getLastMove().moveSpecification();
      final String calculatedSan = board.getSan();
      board.unmove();
      final MoveSpecification actualCalculatedMoveSpecification = StrictSanParser.parseText(calculatedSan, board)
          .moveSpecification();
      assertEquals(expectedStoredMoveSpecification, actualCalculatedMoveSpecification);
      board.moveStrict(halfMove.san());
    }
  }

}
