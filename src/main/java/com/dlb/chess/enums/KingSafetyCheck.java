package com.dlb.chess.enums;

import com.dlb.chess.common.exceptions.ProgrammingMistakeException;

// Outcomes of the king-safety check performed by ChessRuleAnalyzer.
//
// "King safety" here means: after the move is made, is the own king attacked?
// The analyzer answers this independently of whether the move is movement-pseudo-legal —
// callers should typically run movement analysis first.
//
// The vocabulary distinguishes:
// - king move vs non-king move (the type of move that produced the king-attack);
// - was-in-check vs not-in-check (the pre-move state);
// - for king moves that were in check before, whether other king moves exist (player feedback
//   to suggest moving a different piece if the king has no escape).
public enum KingSafetyCheck {
  SUCCESS,

  NON_KING_LEFT_IN_CHECK,
  NON_KING_EXPOSED_TO_CHECK,

  KING_EXPOSED_TO_CHECK,
  KING_LEFT_IN_CHECK_LEGAL_MOVES,
  KING_LEFT_IN_CHECK_NO_LEGAL_MOVES;

  // Translator to the broader MoveCheck enum, used when a KingSafetyCheck failure is surfaced
  // through InvalidMoveException for MoveCheck public-API stability.
  public MoveCheck toMoveCheck() {
    return switch (this) {
      case NON_KING_LEFT_IN_CHECK -> MoveCheck.ALL_BUT_KING_KING_LEFT_IN_CHECK;
      case NON_KING_EXPOSED_TO_CHECK -> MoveCheck.ALL_BUT_KING_KING_EXPOSED_TO_CHECK;
      case KING_EXPOSED_TO_CHECK -> MoveCheck.KING_KING_EXPOSED_TO_CHECK;
      case KING_LEFT_IN_CHECK_LEGAL_MOVES -> MoveCheck.KING_KING_LEFT_IN_CHECK_LEGAL_MOVES;
      case KING_LEFT_IN_CHECK_NO_LEGAL_MOVES -> MoveCheck.KING_KING_LEFT_IN_CHECK_NO_LEGAL_MOVES;
      case SUCCESS -> throw new ProgrammingMistakeException("SUCCESS is not a king-safety failure");
    };
  }
}
