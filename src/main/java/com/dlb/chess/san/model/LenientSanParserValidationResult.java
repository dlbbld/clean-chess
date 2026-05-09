package com.dlb.chess.san.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.model.MoveSpecification;
import com.google.common.collect.ImmutableList;

/**
 * Outcome of a successful lenient SAN parse: the resolved move, plus the list of deviations the parser forgave to get
 * there. {@code forgivenItems} is empty when the input was already canonical SAN.
 */
@SuppressWarnings("null")
public record LenientSanParserValidationResult(@NonNull MoveSpecification moveSpecification,
    @NonNull ImmutableList<@NonNull ForgivenItem> forgivenItems) {

  public LenientSanParserValidationResult {
    forgivenItems = NonNullWrapperCommon.copyOfList(forgivenItems);
  }
}
