/**
 * FEN (Forsyth–Edwards Notation) parsing, validation, and generation. Two parser entry points:
 *
 * <ul>
 * <li>{@link com.dlb.chess.fen.FenParser} — basic structural parsing. Validates the six FEN fields (placement, side to
 * move, castling rights, en-passant target, halfmove clock, fullmove number) for syntactic correctness only.</li>
 * <li>{@link com.dlb.chess.fen.FenParserAdvanced} — basic parsing plus position-legality checks. Rejects positions no
 * real game could reach: impossible double-checks, achievable pawn structure, castling rights consistent with rooks-
 * and-king positions, halfmove clock at or above the 75-move-rule threshold, etc. This is the variant
 * {@link com.dlb.chess.board.Board#Board(String)} uses, so a {@code Board} cannot be constructed from a position no
 * real game could reach.</li>
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
