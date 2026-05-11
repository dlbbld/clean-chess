/**
 * Game-level reports about a {@link com.dlb.chess.common.interfaces.ChessBoard}: threefold-repetition listings
 * (including the missed-claim-ahead opportunities other libraries don't surface), no-progress (50/75-move-rule)
 * sequences, and a printable summary. {@link com.dlb.chess.report.Reporter} is the entry point.
 *
 * <p>
 * Two surfaces:
 *
 * <ul>
 * <li>{@code Reporter.calculateReport(...)} returns a {@link com.dlb.chess.report.Report} record — the
 * programmatic shape carrying repetition-list-of-lists, threefold-claim-ahead slots, no-progress sequences.</li>
 * <li>{@code Reporter.printReport(...)} emits a human-readable summary to {@code stdout} via the
 * {@link com.dlb.chess.messages.Message} bundle. Used by the README examples.</li>
 * </ul>
 *
 * <p>
 * Distinguishes the on-board predicates ("threefold has occurred") from the with-move predicates ("some legal move
 * would create a threefold position the side could claim before playing it") — the latter is the missed-claim feature.
 *
 * <p>
 * Output formatting helpers live in {@link com.dlb.chess.report.print}; the data records in
 * {@link com.dlb.chess.report.model}.
 */
@NonNullByDefault
package com.dlb.chess.report;

import org.eclipse.jdt.annotation.NonNullByDefault;
