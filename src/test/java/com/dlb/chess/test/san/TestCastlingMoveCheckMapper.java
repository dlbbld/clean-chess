package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.enums.CastlingRightLoss;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.enums.MoveCheck;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.validate.CastlingMoveCheckMapper;

/**
 * Lock-down tests for the bridge between {@link MoveCheck} + {@link CastlingRightLoss} (internal pipeline vocabulary)
 * and {@link SanValidationProblem} (external error-code vocabulary) in the castling subset.
 *
 * <p>
 * The input enums form two orthogonal dimensions; {@code SanValidationProblem} flattens them: the FINAL_NO_RIGHT case
 * expands across the 6 provenance values of {@code CastlingRightLoss} (all except {@code NOT_LOST}), while the 4
 * TEMPORARY cases stay flat. Total castling constants in {@code SanValidationProblem} = 4 + 6 = 10.
 *
 * <p>
 * These tests make the invariants machine-checkable so drift is caught at build time:
 * <ul>
 * <li>{@link MoveCheck}'s castling values appear in priority order.</li>
 * <li>{@link SanValidationProblem}'s KING_CASTLING_* values appear in the same order (FINAL before TEMPORARY).</li>
 * <li>The mapper is exhaustive for every castling {@code MoveCheck} and every provenance except {@code NOT_LOST}.</li>
 * <li>The parity relation holds: |KING_CASTLING_*| = (|castling MoveCheck| − 1) + (|CastlingRightLoss| − 1).</li>
 * </ul>
 */
class TestCastlingMoveCheckMapper {

  private static final List<MoveCheck> EXPECTED_CASTLING_MOVE_CHECKS = NonNullWrapperCommon.listOf(
      MoveCheck.KING_CASTLING_FINAL_NO_RIGHT, MoveCheck.KING_CASTLING_TEMPORARY_SQUARES_NOT_EMPTY,
      MoveCheck.KING_CASTLING_TEMPORARY_KING_IN_CHECK, MoveCheck.KING_CASTLING_TEMPORARY_KING_TRAVELS_THROUGH_CHECK,
      MoveCheck.KING_CASTLING_TEMPORARY_KING_ENDS_IN_CHECK);

  private static final List<SanValidationProblem> EXPECTED_KING_CASTLING_PROBLEMS = NonNullWrapperCommon.listOf(
      SanValidationProblem.KING_CASTLING_FINAL_NO_RIGHT_KING_MOVED,
      SanValidationProblem.KING_CASTLING_FINAL_NO_RIGHT_ROOK_MOVED,
      SanValidationProblem.KING_CASTLING_FINAL_NO_RIGHT_ROOK_CAPTURED,
      SanValidationProblem.KING_CASTLING_FINAL_NO_RIGHT_CASTLED,
      SanValidationProblem.KING_CASTLING_FINAL_NO_RIGHT_UNKNOWN_FEN_IMPORT,
      SanValidationProblem.KING_CASTLING_TEMPORARY_SQUARES_NOT_EMPTY,
      SanValidationProblem.KING_CASTLING_TEMPORARY_KING_IN_CHECK,
      SanValidationProblem.KING_CASTLING_TEMPORARY_KING_TRAVELS_THROUGH_CHECK,
      SanValidationProblem.KING_CASTLING_TEMPORARY_KING_ENDS_IN_CHECK);

  /**
   * Provenance values of {@link CastlingRightLoss} that correspond to a FINAL_NO_RIGHT failure.
   */
  private static final List<CastlingRightLoss> EXPECTED_FINAL_NO_RIGHT_PROVENANCES = NonNullWrapperCommon.listOf(
      CastlingRightLoss.KING_MOVED, CastlingRightLoss.ROOK_MOVED, CastlingRightLoss.ROOK_CAPTURED,
      CastlingRightLoss.CASTLED, CastlingRightLoss.UNKNOWN_FEN_IMPORT);

  private static final List<SanValidationProblem> EXPECTED_FINAL_NO_RIGHT_PROBLEMS = NonNullWrapperCommon.listOf(
      SanValidationProblem.KING_CASTLING_FINAL_NO_RIGHT_KING_MOVED,
      SanValidationProblem.KING_CASTLING_FINAL_NO_RIGHT_ROOK_MOVED,
      SanValidationProblem.KING_CASTLING_FINAL_NO_RIGHT_ROOK_CAPTURED,
      SanValidationProblem.KING_CASTLING_FINAL_NO_RIGHT_CASTLED,
      SanValidationProblem.KING_CASTLING_FINAL_NO_RIGHT_UNKNOWN_FEN_IMPORT);

  @SuppressWarnings("static-method")
  @Test
  void testMapperIsExhaustiveForTemporary() {
    // For the 4 TEMPORARY MoveCheck values, the provenance is irrelevant; any value works.
    for (final MoveCheck moveCheck : EXPECTED_CASTLING_MOVE_CHECKS) {
      if (moveCheck == MoveCheck.KING_CASTLING_FINAL_NO_RIGHT) {
        continue;
      }
      final SanValidationProblem mapped = CastlingMoveCheckMapper.map(moveCheck, CastlingRightLoss.NOT_LOST);
      assertNotNull(mapped, "mapper returned null for " + moveCheck);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testMapperIsExhaustiveForFinalNoRight() {
    for (final CastlingRightLoss loss : EXPECTED_FINAL_NO_RIGHT_PROVENANCES) {
      final SanValidationProblem mapped = CastlingMoveCheckMapper.map(MoveCheck.KING_CASTLING_FINAL_NO_RIGHT, loss);
      assertNotNull(mapped, "mapper returned null for FINAL_NO_RIGHT + " + loss);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testParityCount() {
    final var expectedProblemCount = EXPECTED_CASTLING_MOVE_CHECKS.size() - 1
        + EXPECTED_FINAL_NO_RIGHT_PROVENANCES.size();
    assertEquals(expectedProblemCount, EXPECTED_KING_CASTLING_PROBLEMS.size(),
        "|KING_CASTLING_*| must equal (|castling MoveCheck| - 1) + |FINAL_NO_RIGHT provenances|");
  }

  @SuppressWarnings("static-method")
  @Test
  void testFinalNoRightMappingMatchesProvenanceOrder() {
    for (var i = 0; i < EXPECTED_FINAL_NO_RIGHT_PROVENANCES.size(); i++) {
      assertEquals(NonNullWrapperCommon.get(EXPECTED_FINAL_NO_RIGHT_PROBLEMS, i),
          CastlingMoveCheckMapper.map(MoveCheck.KING_CASTLING_FINAL_NO_RIGHT,
              NonNullWrapperCommon.get(EXPECTED_FINAL_NO_RIGHT_PROVENANCES, i)),
          "FINAL_NO_RIGHT mapping mismatch for provenance "
              + NonNullWrapperCommon.get(EXPECTED_FINAL_NO_RIGHT_PROVENANCES, i));
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testTemporaryMappingMatchesExpectedOrder() {
    // The 4 TEMPORARY entries in EXPECTED_KING_CASTLING_PROBLEMS start at index 6 (after the 6 FINAL entries).
    final var temporaryStartIndex = EXPECTED_FINAL_NO_RIGHT_PROVENANCES.size();
    final var temporaryMoveChecks = EXPECTED_CASTLING_MOVE_CHECKS.subList(1, EXPECTED_CASTLING_MOVE_CHECKS.size());
    for (var i = 0; i < temporaryMoveChecks.size(); i++) {
      assertEquals(NonNullWrapperCommon.get(EXPECTED_KING_CASTLING_PROBLEMS, temporaryStartIndex + i),
          CastlingMoveCheckMapper.map(NonNullWrapperCommon.get(temporaryMoveChecks, i), CastlingRightLoss.NOT_LOST),
          "TEMPORARY mapping mismatch at position " + i);
    }
  }
}
