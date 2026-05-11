package com.dlb.chess.pgn;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.san.ForgivenItem;
import com.dlb.chess.san.SanValidationProblem;
import com.google.common.collect.ImmutableList;

/**
 * Outcome of a lenient PGN parse-with-validation. On success, {@link #pgnFile} carries the parsed model and
 * {@link #sanForgivenItems} lists every SAN deviation the lenient layer forgave during movetext replay (empty when the
 * input was already canonical). On failure, {@link #pgnFile} is {@code null} and {@link #sanForgivenItems} contains
 * whatever was accumulated up to the failure point.
 */
@SuppressWarnings("null")
public record LenientPgnParserValidationResult(@NonNull LenientPgnParserValidationProblem problemParser,
    @NonNull SanValidationProblem problemSan, @NonNull String message, @Nullable PgnFile pgnFile,
    @NonNull ImmutableList<@NonNull ForgivenItem> sanForgivenItems) {

  public LenientPgnParserValidationResult {
    sanForgivenItems = NonNullWrapperCommon.copyOfList(sanForgivenItems);
  }

  public boolean isValid() {
    return problemParser == LenientPgnParserValidationProblem.OK;
  }
}