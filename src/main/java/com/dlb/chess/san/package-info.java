/**
 * SAN (Standard Algebraic Notation) parsing, validation, and generation. Validates a candidate SAN string against the
 * current legal-move set, with diagnostic-quality error messages classifying every failure mode (wrong file, wrong
 * rank, missing disambiguation, illegal capture target, exposed king, missing/wrong check-or-checkmate suffix, and so
 * on — see {@link com.dlb.chess.san.enums.SanValidationProblem}).
 *
 * <p>
 * The strict pipeline is the main entry point: {@code SanValidation.validateSan(san, board)} converts a SAN string
 * into a {@link com.dlb.chess.common.model.MoveSpecification}, throwing {@code SanValidationException} on any failure.
 * The same pipeline is reached from both the programmatic {@link com.dlb.chess.board.Board#performMove(String)} and
 * the lenient PGN parser's movetext path — one validation surface, used twice.
 *
 * <p>
 * Generation goes the other direction: {@link com.dlb.chess.san.MoveToSan} produces canonical SAN for a played move
 * (with minimal disambiguation and the correct check/checkmate suffix); {@link com.dlb.chess.san.MoveToLan} produces
 * long algebraic notation.
 *
 * <p>
 * Format-level checks live in {@link com.dlb.chess.san.validate.format}; movement-level (legal-move, king-safety)
 * checks live in {@link com.dlb.chess.san.validate.movement}. A future lenient-SAN release (see {@code tasks.md})
 * will add tolerance for things the strict pipeline rejects today: long algebraic input, zero-instead-of-O castling,
 * over-specified disambiguation, missing/wrong check suffixes, etc.
 */
@NonNullByDefault
package com.dlb.chess.san;

import org.eclipse.jdt.annotation.NonNullByDefault;
