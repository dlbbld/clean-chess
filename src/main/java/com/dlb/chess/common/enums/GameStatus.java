package com.dlb.chess.common.enums;

/**
 * Status of a game (board with history). The five FIDE-automatic terminations ({@link #CHECKMATE}, {@link #STALEMATE},
 * {@link #DEAD_POSITION_INSUFFICIENT_MATERIAL}, {@link #FIVE_FOLD_REPETITION_RULE}, {@link #SEVENTY_FIVE_MOVE_RULE}) end the
 * game permanently — no further moves are accepted by the validation pipeline (see {@code ValidateNewMove} and
 * {@code StrictSanParser}).
 *
 * <p>
 * The two single-side insufficient-material variants ({@link #INSUFFICIENT_MATERIAL_WHITE_ONLY},
 * {@link #INSUFFICIENT_MATERIAL_BLACK_ONLY}) are diagnostic states, not terminations: under FIDE 5.2.2 a dead position
 * requires that <em>neither</em> side can deliver checkmate. If one side still has mating material the game continues,
 * so these statuses do <em>not</em> block further moves — see {@link #isAutomaticTermination()}.
 *
 * <p>
 * Claimable draws (3-fold repetition, 50-move rule) are NOT represented here — they remain queryable on the board but
 * never end the game automatically, since a player may decline to claim and continue playing under FIDE rules.
 */
public enum GameStatus {

  CHECKMATE,
  STALEMATE,
  DEAD_POSITION_INSUFFICIENT_MATERIAL,
  INSUFFICIENT_MATERIAL_WHITE_ONLY,
  INSUFFICIENT_MATERIAL_BLACK_ONLY,
  FIVE_FOLD_REPETITION_RULE,
  SEVENTY_FIVE_MOVE_RULE,
  ONGOING;

  /**
   * Returns {@code true} iff this status is one of the five FIDE-automatic terminations that end the game permanently.
   * The single-side insufficient-material variants and {@link #ONGOING} return {@code false}.
   */
  public boolean isAutomaticTermination() {
    return switch (this) {
      case CHECKMATE, STALEMATE, DEAD_POSITION_INSUFFICIENT_MATERIAL, FIVE_FOLD_REPETITION_RULE, SEVENTY_FIVE_MOVE_RULE -> true;
      case INSUFFICIENT_MATERIAL_WHITE_ONLY, INSUFFICIENT_MATERIAL_BLACK_ONLY, ONGOING -> false;
    };
  }
}
