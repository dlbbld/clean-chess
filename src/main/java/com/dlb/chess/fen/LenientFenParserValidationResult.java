package com.dlb.chess.fen;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.enums.FenAdvancedValidationProblem;
import com.dlb.chess.fen.model.Fen;
import com.google.common.collect.ImmutableList;

/**
 * Outcome of a lenient FEN parse-with-validation. On success, {@link #fen} carries the parsed model and
 * {@link #forgivenItems} lists every syntactic-tolerance transformation the lenient layer applied (empty when the
 * input was already canonical FEN). On failure, {@link #fen} is {@code null} and {@link #forgivenItems} contains
 * whatever was accumulated up to the failure point.
 *
 * <p>The lenient layer only forgives syntactic deviations (whitespace, casing, missing trailing counters, etc.); it
 * does not weaken {@link FenParserAdvanced}'s structural/rule-consistency checks. When the underlying advanced
 * parser rejects the normalised FEN, {@link #fenAdvancedValidationProblem} carries the specific cause so callers can
 * react without parsing the message.
 */
@SuppressWarnings("null")
public record LenientFenParserValidationResult(@NonNull LenientFenParserValidationProblem problem,
    @NonNull FenAdvancedValidationProblem fenAdvancedValidationProblem, @NonNull String message, @Nullable Fen fen,
    @NonNull ImmutableList<@NonNull ForgivenFenItem> forgivenItems) {

  public LenientFenParserValidationResult {
    forgivenItems = Nulls.copyOfList(forgivenItems);
  }

  public boolean isValid() {
    return problem == LenientFenParserValidationProblem.OK;
  }
}
