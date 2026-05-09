package com.dlb.chess.san.model;

import com.dlb.chess.san.enums.LenientSanValidationProblem;

/**
 * One forgiven deviation surfaced by the lenient SAN parser.
 *
 * @param code the typed deviation classifier
 * @param originalToken the original SAN token as the user wrote it (the entire move, not just the deviating fragment)
 * @param canonicalSan the canonical SAN equivalent the parser resolved the input to
 */
public record ForgivenItem(LenientSanValidationProblem code, String originalToken, String canonicalSan) {

  public ForgivenItem {
    if (originalToken.isBlank()) {
      throw new IllegalArgumentException("originalToken must not be blank");
    }
    if (canonicalSan.isBlank()) {
      throw new IllegalArgumentException("canonicalSan must not be blank");
    }
  }
}
