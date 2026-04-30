package com.dlb.chess.test.board;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.san.validate.SanValidation;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.parser.PgnCacheForStrictPgnParserTestCases;
import com.dlb.chess.test.pgntest.PgnExpectedValue;

/**
 * Verifies the SAN ↔ MoveSpecification consistency that
 * {@link com.dlb.chess.board.Board#performMove(String) Board.performMove(String)} relies on:
 * once {@link SanValidation#validateSan SanValidation.validateSan} has produced a
 * MoveSpecification from a SAN, that MoveSpec is the canonical representation of the move and
 * round-trips both ways. The board therefore performs the move with no further re-validation
 * of the spec.
 *
 * <h2>Forward (played moves)</h2>
 *
 * For each halfmove of each PGN: derive the MoveSpec via {@code validateSan(san, board)}
 * <em>before</em> performing, then perform via {@code board.performMove(san)} and assert:
 *
 * <ol>
 * <li>the derived MoveSpec equals the legal move that was actually played
 *     ({@code board.getLastMove().moveSpecification()});</li>
 * <li>the SAN reconstructed from the legal move ({@code board.getSan()}) equals the original
 *     PGN SAN.</li>
 * </ol>
 *
 * <h2>Reverse (every legal move at every position)</h2>
 *
 * At each position reached during PGN playthrough, for <em>every</em> legal move from
 * {@link ApiBoard#getLegalMoveSet getLegalMoveSet}: perform the move so the board can compute
 * its SAN, capture the SAN, unperform back to the original position, then derive a fresh
 * MoveSpec from that SAN via {@code validateSan} and assert it equals the LegalMove's stored
 * MoveSpec. This checks the round-trip on every legal move, not just the chosen line.
 *
 * <h2>Scope and runtime</h2>
 *
 * Iterates the parser-integration smoke list (~45 PGNs spanning every major parser code path
 * — standard moves, captures, en passant, promotion, castling, check, checkmate, custom
 * starting positions). The reverse test is the slower of the two but completes in seconds.
 */
class TestPerformMoveSanContract {

  @SuppressWarnings("static-method")
  @Test
  void testPlayedMoveSanMoveSpecRoundtrip() {
    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getParserIntegrationSmokeList()) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        verifyPlayedMoveRoundtrip(testCaseList, testCase);
      }
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testAllLegalMovesSanMoveSpecRoundtrip() {
    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getParserIntegrationSmokeList()) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        verifyAllLegalMovesRoundtrip(testCaseList, testCase);
      }
    }
  }

  /**
   * Forward direction: for each played halfmove, derive MoveSpec from SAN, perform via SAN,
   * then assert the played LegalMove and the reconstructed SAN both match.
   */
  private static void verifyPlayedMoveRoundtrip(PgnFileTestCaseList testCaseList, PgnFileTestCase testCase) {
    final PgnFile pgnFile = PgnCacheForStrictPgnParserTestCases.getPgn(testCaseList.pgnTest().getFolderPath(),
        testCase.pgnFileName());
    final ApiBoard board = new Board(pgnFile.startFen());

    var halfMoveIndex = 0;
    for (final PgnHalfMove halfMove : pgnFile.halfMoveList()) {
      halfMoveIndex++;
      final int hmi = halfMoveIndex;
      final String san = halfMove.san();

      final MoveSpecification derivedSpec = SanValidation.validateSan(san, board);

      board.performMove(san);

      assertEquals(derivedSpec, board.getLastMove().moveSpecification(),
          () -> testCase.pgnFileName() + ": halfmove " + hmi + " (" + san
              + ") — MoveSpec derived from SAN does not match the LegalMove's MoveSpec after perform");
      assertEquals(san, board.getSan(),
          () -> testCase.pgnFileName() + ": halfmove " + hmi + " (" + san
              + ") — SAN reconstructed from LegalMove does not match the original PGN SAN");
    }
  }

  /**
   * Reverse direction: at each position, for every legal move (not just the played one),
   * perform → capture SAN → unperform → derive MoveSpec from SAN at the original position →
   * assert it equals the LegalMove's stored MoveSpec.
   */
  private static void verifyAllLegalMovesRoundtrip(PgnFileTestCaseList testCaseList, PgnFileTestCase testCase) {
    final PgnFile pgnFile = PgnCacheForStrictPgnParserTestCases.getPgn(testCaseList.pgnTest().getFolderPath(),
        testCase.pgnFileName());
    final ApiBoard board = new Board(pgnFile.startFen());

    var halfMoveIndex = 0;
    for (final PgnHalfMove halfMove : pgnFile.halfMoveList()) {
      halfMoveIndex++;
      // Test all legal moves at this position before playing the actual halfmove.
      verifyAllLegalMovesAtCurrentPosition(board, testCase.pgnFileName(), halfMoveIndex);
      board.performMove(halfMove.san());
    }
  }

  private static void verifyAllLegalMovesAtCurrentPosition(ApiBoard board, String pgnFileName, int positionIndex) {
    // Snapshot the legal-move set into a list before iteration; perform/unperform mutate the
    // board's per-halfmove lists internally, and we want to iterate the position's set as a
    // stable view.
    final List<LegalMove> legalMoves = new ArrayList<>(board.getLegalMoveSet());
    for (final LegalMove legalMove : legalMoves) {
      final MoveSpecification originalSpec = legalMove.moveSpecification();

      // Perform the legal move so the board produces its SAN, capture it, then undo.
      board.performMove(originalSpec);
      final String san = board.getSan();
      board.unperformMove();

      // Re-derive the MoveSpec from the SAN at the original position; must equal the original.
      final MoveSpecification derivedSpec = SanValidation.validateSan(san, board);
      assertEquals(originalSpec, derivedSpec,
          () -> pgnFileName + ": position before halfmove " + positionIndex + " — SAN→MoveSpec roundtrip of legal move "
              + "with SAN \"" + san + "\" does not match the LegalMove's MoveSpec");
    }
  }
}
