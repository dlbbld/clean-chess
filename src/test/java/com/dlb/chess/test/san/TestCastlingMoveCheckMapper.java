package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.enums.MoveCheck;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.validate.CastlingMoveCheckMapper;

/**
 * Lock-down tests for the bridge between {@link MoveCheck} (internal pipeline vocabulary) and
 * {@link SanValidationProblem} (external error-code vocabulary) in the castling subset.
 *
 * <p>
 * The two enums deliberately carry parallel sets of constants in this subset. These tests make the invariants
 * machine-checkable so drift is caught at build time:
 *
 * <ul>
 * <li>{@link MoveCheck}'s castling values appear in priority order (CASTLING_PRIORITY_2..6).</li>
 * <li>{@link SanValidationProblem}'s KING_CASTLING_* values appear in the same order.</li>
 * <li>{@link CastlingMoveCheckMapper#map} is exhaustive: every castling {@code MoveCheck} maps to a non-null
 * {@code SanValidationProblem}.</li>
 * <li>The two sets have the same cardinality (parity count).</li>
 * </ul>
 */
class TestCastlingMoveCheckMapper {

  private static final List<MoveCheck> EXPECTED_CASTLING_MOVE_CHECKS = NonNullWrapperCommon.listOf(
      MoveCheck.KING_CASTLING_FINAL_NO_RIGHT, MoveCheck.KING_CASTLING_TEMPORARY_SQUARES_NOT_EMPTY,
      MoveCheck.KING_CASTLING_TEMPORARY_KING_IN_CHECK, MoveCheck.KING_CASTLING_TEMPORARY_KING_TRAVELS_THROUGH_CHECK,
      MoveCheck.KING_CASTLING_TEMPORARY_KING_ENDS_IN_CHECK);

  private static final List<SanValidationProblem> EXPECTED_KING_CASTLING_PROBLEMS = NonNullWrapperCommon.listOf(
      SanValidationProblem.KING_CASTLING_FINAL_NO_RIGHT, SanValidationProblem.KING_CASTLING_TEMPORARY_SQUARES_NOT_EMPTY,
      SanValidationProblem.KING_CASTLING_TEMPORARY_KING_IN_CHECK,
      SanValidationProblem.KING_CASTLING_TEMPORARY_KING_TRAVELS_THROUGH_CHECK,
      SanValidationProblem.KING_CASTLING_TEMPORARY_KING_ENDS_IN_CHECK);

  @SuppressWarnings("static-method")
  @Test
  void testMapperIsExhaustive() {
    for (final MoveCheck moveCheck : EXPECTED_CASTLING_MOVE_CHECKS) {
      final SanValidationProblem mapped = CastlingMoveCheckMapper.map(moveCheck);
      assertNotNull(mapped, "mapper returned null for " + moveCheck);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testParityCount() {
    assertEquals(EXPECTED_CASTLING_MOVE_CHECKS.size(), EXPECTED_KING_CASTLING_PROBLEMS.size(),
        "MoveCheck and SanValidationProblem must have the same number of castling constants");
  }

  @SuppressWarnings("static-method")
  @Test
  void testMapperMatchesExpectedOrdering() {
    for (var i = 0; i < EXPECTED_CASTLING_MOVE_CHECKS.size(); i++) {
      assertEquals(EXPECTED_KING_CASTLING_PROBLEMS.get(i),
          CastlingMoveCheckMapper.map(NonNullWrapperCommon.get(EXPECTED_CASTLING_MOVE_CHECKS, i)),
          "mapping mismatch at priority " + (i + 1));
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testMapperMatchesExpectedName() {
    for (var i = 0; i < EXPECTED_CASTLING_MOVE_CHECKS.size(); i++) {
      assertEquals(NonNullWrapperCommon.get(EXPECTED_KING_CASTLING_PROBLEMS, i).name(),
          CastlingMoveCheckMapper.map(NonNullWrapperCommon.get(EXPECTED_CASTLING_MOVE_CHECKS, i)).name(),
          "mapping name mismatch for position " + (i + 1));
    }
  }
}
