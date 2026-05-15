/**
 * Board representation and the strict move-validation pipeline.
 *
 * <h2>The strict-game invariant</h2>
 *
 * <p>
 * A {@link com.dlb.chess.board.Board} represents a <em>game</em> — a position together with its move history — not
 * merely a position. Once any FIDE-automatic termination has been reached, the game has ended permanently and no
 * further moves are accepted by either pipeline:
 *
 * <ul>
 * <li>{@link com.dlb.chess.board.ValidateNewMove#validateNewMove} (MoveSpecification pipeline)</li>
 * <li>{@code com.dlb.chess.san.StrictSanParser#parseText} (SAN pipeline)</li>
 * </ul>
 *
 * <p>
 * The six FIDE-automatic terminations (queryable via
 * {@link com.dlb.chess.common.utility.BasicChessUtility#calculateGameStatus}) are:
 *
 * <ul>
 * <li>{@link com.dlb.chess.common.enums.GameStatus#CHECKMATE} (FIDE 5.1.1)</li>
 * <li>{@link com.dlb.chess.common.enums.GameStatus#STALEMATE} (FIDE 5.2.1)</li>
 * <li>{@link com.dlb.chess.common.enums.GameStatus#DEAD_POSITION_INSUFFICIENT_MATERIAL} — FIDE 5.2.2 dead position by
 * mutual insufficient material (the single-side {@code INSUFFICIENT_MATERIAL_*_ONLY} variants are diagnostic states,
 * NOT terminations)</li>
 * <li>{@link com.dlb.chess.common.enums.GameStatus#DEAD_POSITION_UNWINNABLE_QUICK} — FIDE 5.2.2 dead position by
 * Ambrona's quick unwinnability analyzer (both sides unwinnable). Detected only when the
 * {@code detectDeadPositionUnwinnable} board constructor flag is set; otherwise the predicate evaluates to
 * {@code false} and the game continues</li>
 * <li>{@link com.dlb.chess.common.enums.GameStatus#FIVE_FOLD_REPETITION_RULE} (FIDE 9.6.1)</li>
 * <li>{@link com.dlb.chess.common.enums.GameStatus#SEVENTY_FIVE_MOVE_RULE} (FIDE 9.6.2)</li>
 * </ul>
 *
 * <p>
 * An attempt to move on a terminal-state board surfaces as {@link com.dlb.chess.exceptions.InvalidMoveException} with
 * {@link com.dlb.chess.enums.MoveCheck#GAME_ALREADY_ENDED} and the originating
 * {@link com.dlb.chess.common.enums.GameStatus} as payload (or, on the SAN pipeline, the mirrored
 * {@code SanValidationException} with {@code SanValidationProblem.GAME_ALREADY_ENDED}).
 *
 * <p>
 * The claimable draws — {@link com.dlb.chess.common.enums.GameStatus FIDE 9.2 (3-fold) and 9.3 (50-move)} — are
 * intentionally NOT enforced here: a player may decline to claim and continue playing. They remain queryable on the
 * board.
 *
 * <p>
 * FEN imports are subject to a corresponding constraint: a halfmove clock greater than
 * {@link com.dlb.chess.common.constants.ChessConstants#SEVENTY_FIVE_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD} (150) cannot
 * be reached by legal play and is rejected at parse time with
 * {@code FenAdvancedValidationProblem.INVALID_HALF_MOVE_CLOCK_BEYOND_SEVENTY_FIVE_MOVE_RULE}.
 *
 * <p>
 * Detection of games whose recorded halfmove sequence continued past one of the path-dependent automatic terminations
 * (5-fold, 75-move) — useful for corpus mining over historical PGN databases — is not currently provided as part of the
 * public API; it would belong in a standalone utility outside the strict pipeline.
 */
@NonNullByDefault
package com.dlb.chess.board;

import org.eclipse.jdt.annotation.NonNullByDefault;
