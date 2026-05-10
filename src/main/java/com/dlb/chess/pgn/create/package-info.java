/**
 * PGN export. {@link com.dlb.chess.pgn.create.PgnCreate} produces a {@link com.dlb.chess.pgn.parser.model.PgnFile}
 * record from a {@link com.dlb.chess.board.Board} (or its serialized {@code String} form), in the unique <em>export
 * format</em> defined by the PGN specification.
 *
 * <p>
 * The export format is byte-stable round-trip with the strict parser ({@link com.dlb.chess.pgn.parser.StrictPgnParser})
 * — anything {@code PgnCreate} writes is parseable by both the strict and the lenient pipelines. Standard tag-roster
 * tags are emitted in canonical order; missing tags from imported PGN are filled with {@code "?"} placeholders or
 * today's date for the {@code Date} tag.
 *
 * <p>
 * For writing the export string to a file, see {@link com.dlb.chess.pgn.writer.PgnWriter}.
 */
@NonNullByDefault
package com.dlb.chess.pgn.create;

import org.eclipse.jdt.annotation.NonNullByDefault;
