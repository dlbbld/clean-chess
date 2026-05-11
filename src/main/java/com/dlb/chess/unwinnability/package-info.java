/**
 * The library's flagship feature — a Java port of Miguel Ambrona's
 * <a href="https://github.com/miguel-ambrona/D3-Chess">Chess Unwinnability Analyzer (CHA)</a> (GPL v3). Decides whether
 * a position is <em>unwinnable for a side</em> — the side has no theoretical mating sequence assuming worst-case play
 * by the opponent — and the symmetric notion of a <em>dead position</em> (unwinnable for both sides).
 *
 * <p>
 * Insufficient material covers the trivial cases (king-vs-king, king + minor vs king); positions like blocked pawn
 * walls, certain wrong-bishop endgames, and many forced-only-moves continuations are dead but <em>not</em> insufficient
 * — and most chess libraries get them wrong. CHA decides them correctly across the full range of positions.
 *
 * <h2>Two variants</h2>
 *
 * <ul>
 * <li><strong>Quick</strong> ({@link com.dlb.chess.unwinnability.quick.UnwinnableQuickAnalyzer}) — microsecond-scale,
 * structural, three-valued: {@code WINNABLE}, {@code UNWINNABLE}, {@code POSSIBLY_WINNABLE}. The third value is a
 * deliberate honesty signal — the quick algorithm is sound but not complete.</li>
 * <li><strong>Full</strong> ({@link com.dlb.chess.unwinnability.full.UnwinnableFullAnalyzer}) — deep search,
 * three-valued: {@code WINNABLE}, {@code UNWINNABLE}, {@code UNDETERMINED}. The undetermined case is bounded by a
 * 500&nbsp;000-position limit; most positions resolve well below it.</li>
 * </ul>
 *
 * <p>
 * Dead-position detection is the symmetric notion with analogous three-valued returns
 * ({@link com.dlb.chess.unwinnability.quick.enums.DeadPositionQuick},
 * {@link com.dlb.chess.unwinnability.full.enums.DeadPositionFull}).
 *
 * <h2>Opt-in by design</h2>
 *
 * <p>
 * Both variants are caller-invoked. CHA is <em>not</em> run automatically when a move is performed — the only deadness
 * check in the per-move game-status query is the structural insufficient-material test (see
 * {@link com.dlb.chess.board}). The motivating concern is bulk-PGN analysis, where a per-move CHA check would add
 * significant cumulative cost. The convenience entry points are on the
 * {@link com.dlb.chess.board.Board} interface ({@code isUnwinnableQuick}, {@code isUnwinnableFull},
 * {@code isDeadPositionQuick}, {@code isDeadPositionFull}) and inherited by {@link com.dlb.chess.board.Board}.
 *
 * <p>
 * See {@code specification.md} §3.2 for the full design rationale.
 */
@NonNullByDefault
package com.dlb.chess.unwinnability;

import org.eclipse.jdt.annotation.NonNullByDefault;
