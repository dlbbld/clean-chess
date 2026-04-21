package com.dlb.chess.san.validate;

import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.enums.MoveCheck;
import com.dlb.chess.san.enums.SanValidationProblem;

/**
 * Maps the castling-refusal values of {@link MoveCheck} to their counterparts in
 * {@link SanValidationProblem}.
 *
 * <p>
 * The two enums deliberately carry overlapping concepts in the castling subset: {@code MoveCheck}
 * is the internal pipeline result type, {@code SanValidationProblem} is the external error code
 * surfaced via {@code SanValidationException}. They live in separate layers and are renamed
 * independently; this class is the compile-time-enforced bridge between them. The exhaustive
 * switch (no {@code default:}) guarantees that any new castling value added to {@code MoveCheck}
 * without a corresponding entry here causes a compile error.
 */
public abstract class CastlingMoveCheckMapper {

  public static SanValidationProblem map(MoveCheck castlingMoveCheck) {
    return switch (castlingMoveCheck) {
      case KING_CASTLING_FINAL_NO_RIGHT ->
          SanValidationProblem.KING_CASTLING_FINAL_NO_RIGHT;
      case KING_CASTLING_TEMPORARY_SQUARES_NOT_EMPTY ->
          SanValidationProblem.KING_CASTLING_TEMPORARY_SQUARES_NOT_EMPTY;
      case KING_CASTLING_TEMPORARY_KING_IN_CHECK ->
          SanValidationProblem.KING_CASTLING_TEMPORARY_KING_IN_CHECK;
      case KING_CASTLING_TEMPORARY_KING_TRAVELS_THROUGH_CHECK ->
          SanValidationProblem.KING_CASTLING_TEMPORARY_KING_TRAVELS_THROUGH_CHECK;
      case KING_CASTLING_TEMPORARY_KING_ENDS_IN_CHECK ->
          SanValidationProblem.KING_CASTLING_TEMPORARY_KING_ENDS_IN_CHECK;
      case SUCCESS, BASIC_NOT_HAVING_MOVE, BASIC_MOVING_PIECE_NONE, BASIC_MOVING_PIECE_OPPONENT,
          BASIC_NON_PAWN_PROMOTION_PIECE_SET, ALL_MOVEMENT_NOT_POSSIBLE,
          ALL_TO_SQUARE_OCCUPIED_BY_OWN_PIECE, LONG_RANGE_PIECES_CANNOT_JUMP_OVER_PIECES,
          ALL_BUT_KING_KING_LEFT_IN_CHECK, ALL_BUT_KING_KING_EXPOSED_TO_CHECK,
          PAWN_PROMOTION_MOVE_NO_PROMOTION_PIECE, PAWN_NON_PROMOTION_MOVE_PROMOTION_PIECE,
          PAWN_FORWARD_TWO_SQUARE_JUMP_OVER_SQUARE_ONLY_NOT_EMPTY,
          PAWN_FORWARD_TWO_SQUARE_TO_SQUARE_ONLY_NOT_EMPTY,
          PAWN_FORWARD_TWO_SQUARE_BOTH_SQUARE_NOT_EMPTY,
          PAWN_FORWARD_ONE_SQUARE_TO_SQUARE_NOT_EMPTY_OWN_PIECE,
          PAWN_FORWARD_ONE_SQUARE_TO_SQUARE_NOT_EMPTY_OPPONENT_PIECE,
          PAWN_DIAGONAL_OWN_PIECE, PAWN_EN_PASSANT_CAPTURE_WRONG_RANK,
          PAWN_EN_PASSANT_CAPTURE_NO_IMMEDIATE_BEFORE_TWO_SQUARE_ADVANCE,
          KING_CAPTURES_GUARDED_PIECE, KING_MOVES_NEXT_TO_OPPONENT_KING,
          KING_IN_CHECK_TO_EMPTY_ATTACKED_SQUARE_NO_LEGAL_MOVES,
          KING_IN_CHECK_TO_EMPTY_ATTACKED_SQUARE_LEGAL_MOVES, KING_MOVES_INTO_CHECK ->
          throw new ProgrammingMistakeException(
              "MoveCheck value is not a castling-refusal reason: " + castlingMoveCheck);
    };
  }

}
