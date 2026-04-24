package com.dlb.chess.enums;

import com.dlb.chess.common.exceptions.ProgrammingMistakeException;

// Outcomes of the castling-specific check performed in CastlingUtility.
// FINAL means the castling right is permanently lost; TEMPORARY means the
// board state blocks castling now but castling may become possible again.
// The cases are overlapping so they are checked in the order listed.
public enum CastlingCheck {
  SUCCESS,
  FINAL_NO_RIGHT,
  TEMPORARY_SQUARES_NOT_EMPTY,
  TEMPORARY_KING_IN_CHECK,
  TEMPORARY_KING_TRAVELS_THROUGH_CHECK,
  TEMPORARY_KING_ENDS_IN_CHECK;

  // Translator to the broader MoveCheck enum. Used when a CastlingCheck result is surfaced through
  // an InvalidMoveException or SanValidationException, both of which carry a MoveCheck for public
  // API stability.
  public MoveCheck toMoveCheck() {
    return switch (this) {
      case FINAL_NO_RIGHT -> MoveCheck.KING_CASTLING_FINAL_NO_RIGHT;
      case TEMPORARY_SQUARES_NOT_EMPTY -> MoveCheck.KING_CASTLING_TEMPORARY_SQUARES_NOT_EMPTY;
      case TEMPORARY_KING_IN_CHECK -> MoveCheck.KING_CASTLING_TEMPORARY_KING_IN_CHECK;
      case TEMPORARY_KING_TRAVELS_THROUGH_CHECK -> MoveCheck.KING_CASTLING_TEMPORARY_KING_TRAVELS_THROUGH_CHECK;
      case TEMPORARY_KING_ENDS_IN_CHECK -> MoveCheck.KING_CASTLING_TEMPORARY_KING_ENDS_IN_CHECK;
      case SUCCESS -> throw new ProgrammingMistakeException("SUCCESS is not a castling-refusal reason");
    };
  }
}
