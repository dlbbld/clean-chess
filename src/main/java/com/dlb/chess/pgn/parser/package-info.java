/**
 * PGN parsers — the strict pipeline ({@link com.dlb.chess.pgn.parser.StrictPgnParser}) which enforces export-format
 * invariants and is the round-trip-canonical reference, and the lenient pipeline
 * ({@link com.dlb.chess.pgn.parser.LenientPgnParser}) which tolerates real-world PGN: spaced move-number indicators,
 * missing seven-tag-roster entries, optional termination markers, extra whitespace, UTF-8 BOM on input.
 *
 * <p>
 * Both produce the same {@link com.dlb.chess.pgn.parser.model.PgnFile} model. The two-parser split is deliberate — a
 * single parser with a "strictness flag" inevitably grows conditional branches that obscure both rule sets; splitting
 * keeps each parser readable and lets them evolve independently. See {@code specification.md} §3.3 and §5 for the
 * intentional deviations from the PGN spec (newlines/tabs in commentary content, pre-game commentary slot).
 *
 * <p>
 * Diagnostic quality is a project goal: each problem has a typed code (see
 * {@link com.dlb.chess.pgn.parser.enums.StrictPgnParserValidationProblem} and
 * {@link com.dlb.chess.pgn.parser.enums.LenientPgnParserValidationProblem}) plus a human-readable message naming the
 * offending construct. The shared tokenizer lives in {@link com.dlb.chess.pgn.parser.sequential}.
 *
 * <p>
 * For PGN export, see {@link com.dlb.chess.pgn.create}; for file-system writing, see {@link com.dlb.chess.pgn.writer};
 * for diagnostics outside the strict pipeline, see {@link com.dlb.chess.pgn.diagnostic}.
 */
@NonNullByDefault
package com.dlb.chess.pgn.parser;

import org.eclipse.jdt.annotation.NonNullByDefault;
