package com.dlb.chess.san.validate;

import com.dlb.chess.board.enums.CastlingRightLoss;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.enums.MoveCheck;
import com.dlb.chess.san.enums.SanValidationProblem;

/**
 * Maps the castling-refusal values of {@link MoveCheck} + {@link CastlingRightLoss} to their
 * counterparts in {@link SanValidationProblem}.
 *
 * <p>
 * The two input enums form orthogonal dimensions of the castling-failure space:
 * <ul>
 * <li>{@code MoveCheck} = which chess-rule check failed (5 values for castling);</li>
 * <li>{@code CastlingRightLoss} = why the castling right was lost (6 values + {@code NOT_LOST}).
 * </li>
 * </ul>
 * {@code SanValidationProblem} flattens these dimensions into a single user-facing enum: the
 * FINAL_NO_RIGHT case is expanded across the 6 provenance values, while the 4 TEMPORARY cases stay
 * flat (they have no provenance sub-dimension). Consumers switching on {@code SanValidationProblem}
 * get the whole story without drilling into side-fields.
 *
 * <p>
 * Both inner switches are exhaustive (no {@code default:}) so any new value added to
 * {@code MoveCheck} or {@code CastlingRightLoss} causes a compile error here.
 */
public abstract class CastlingMoveCheckMapper {

  public static SanValidationProblem map(MoveCheck castlingMoveCheck, CastlingRightLoss castlingRightLoss) {
    return switch (castlingMoveCheck) {
      case KING_CASTLING_FINAL_NO_RIGHT -> mapRightLoss(castlingRightLoss);
      case KING_CASTLING_TEMPORARY_SQUARES_NOT_EMPTY ->
          SanValidationProblem.KING_CASTLING_TEMPORARY_SQUARES_NOT_EMPTY;
      case KING_CASTLING_TEMPORARY_KING_IN_CHECK ->
          SanValidationProblem.KING_CASTLING_TEMPORARY_KING_IN_CHECK;
      case KING_CASTLING_TEMPORARY_KING_TRAVELS_THROUGH_CHECK ->
          SanValidationProblem.KING_CASTLING_TEMPORARY_KING_TRAVELS_THROUGH_CHECK;
      case KING_CASTLING_TEMPORARY_KING_ENDS_IN_CHECK ->
          SanValidationProblem.KING_CASTLING_TEMPORARY_KING_ENDS_IN_CHECK;
      case SUCCESS, BASIC_MOVING_PIECE_NONE, BASIC_MOVING_PIECE_OPPONENT,
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

  private static SanValidationProblem mapRightLoss(CastlingRightLoss castlingRightLoss) {
    return switch (castlingRightLoss) {
      case KING_MOVED -> SanValidationProblem.KING_CASTLING_FINAL_NO_RIGHT_KING_MOVED;
      case ROOK_MOVED -> SanValidationProblem.KING_CASTLING_FINAL_NO_RIGHT_ROOK_MOVED;
      case ROOK_CAPTURED -> SanValidationProblem.KING_CASTLING_FINAL_NO_RIGHT_ROOK_CAPTURED;
      case CASTLED -> SanValidationProblem.KING_CASTLING_FINAL_NO_RIGHT_CASTLED;
      case UNKNOWN_FEN_IMPORT -> SanValidationProblem.KING_CASTLING_FINAL_NO_RIGHT_UNKNOWN_FEN_IMPORT;
      case NOT_LOST -> throw new ProgrammingMistakeException(
          "NOT_LOST is not a valid provenance for a KING_CASTLING_FINAL_NO_RIGHT failure");
    };
  }

}
