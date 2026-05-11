package com.dlb.chess.pgn;

import com.dlb.chess.san.SanValidationProblem;

public record StrictPgnParserValidationResult(StrictPgnParserValidationProblem problemParser,
    SanValidationProblem problemSan, String message) {
  public boolean isValid() {
    return problemParser == StrictPgnParserValidationProblem.OK;
  }
}