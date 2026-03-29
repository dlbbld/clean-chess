package com.dlb.chess.pgn.parser.model;

import com.dlb.chess.pgn.parser.enums.LenientPgnParserValidationProblem;
import com.dlb.chess.san.enums.SanValidationProblem;

public record LenientPgnParserValidationResult(LenientPgnParserValidationProblem problemParser,
    SanValidationProblem problemSan, String message) {
  public boolean isValid() {
    return problemParser == LenientPgnParserValidationProblem.OK;
  }
}