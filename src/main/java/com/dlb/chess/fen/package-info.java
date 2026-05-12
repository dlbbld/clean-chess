/**
 * FEN (Forsyth–Edwards Notation) parsing, validation, and generation. Two parser entry points:
 *
 * <ul>
 * <li>{@link com.dlb.chess.fen.FenParser} — basic structural parsing. Validates the six FEN fields (placement, side to
 * move, castling rights, en-passant target, halfmove clock, fullmove number) for syntactic correctness only.</li>
 * <li>{@link com.dlb.chess.fen.FenParserAdvanced} — basic parsing plus advanced structural and rule-consistency
 * validation. Beyond field syntax it enforces:
 * <ul>
 * <li>exactly one king per side; pawn count &lt;= 8 per side; counts of non-pawn pieces beyond the starting set are
 * accounted for by missing pawns (promotion-consistency)</li>
 * <li>no pawns on rank 1 or rank 8</li>
 * <li>the side <em>not</em> to move is not in check (otherwise the last move would have been illegal)</li>
 * <li>castling rights consistent with king and rook static positions</li>
 * <li>en-passant target square consistent with the side to move and the adjacent pawn structure</li>
 * <li>halfmove clock not above the 75-move-rule threshold of 150 — a clock of exactly 150 is the legal terminal
 * moment and is accepted; only values strictly above 150 are rejected</li>
 * <li>fullmove number in the supported range</li>
 * </ul>
 * This is the variant {@link com.dlb.chess.board.Board#Board(String)} uses. It does not prove full game
 * reachability — a position passing these checks may still be unreachable from the initial position through any
 * legal sequence; structural and rule-consistency plausibility is the bar.</li>
 * </ul>
 *
 * <p>
 * The {@link com.dlb.chess.fen.model.Fen} record is the parsed result — a value object carrying the static position,
 * side to move, castling rights, en-passant capture target square, halfmove clock, and fullmove number. FEN string
 * generation is via {@link com.dlb.chess.fen.FenBoard} from a {@link com.dlb.chess.board.Board}.
 */
@NonNullByDefault
package com.dlb.chess.fen;

import org.eclipse.jdt.annotation.NonNullByDefault;
