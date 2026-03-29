package com.dlb.chess.pgn.parser.model;

import com.dlb.chess.pgn.parser.enums.StrictPgnParserValidationProblem;
import com.dlb.chess.san.enums.SanValidationProblem;

public record StrictPgnParserValidationResult(StrictPgnParserValidationProblem problemParser,
    SanValidationProblem problemSan, String message) {
  public boolean isValid() {
    return problemParser == StrictPgnParserValidationProblem.OK;
  }
}