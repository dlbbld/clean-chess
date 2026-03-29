package com.dlb.chess.pgn.parser.exceptions;

import com.dlb.chess.common.exceptions.UsageException;
import com.dlb.chess.pgn.parser.enums.StrictPgnParserValidationProblem;
import com.dlb.chess.san.enums.SanValidationProblem;

public class StrictPgnParserValidationException extends UsageException {

  private final StrictPgnParserValidationProblem strictPgnParserValidationProblem;

  private final SanValidationProblem sanValidationProblem;

  public StrictPgnParserValidationException(StrictPgnParserValidationProblem strictPgnParserValidationProblem,
      SanValidationProblem sanValidationProblem, String message) {
    super(message);
    this.strictPgnParserValidationProblem = strictPgnParserValidationProblem;
    this.sanValidationProblem = sanValidationProblem;
  }

  public StrictPgnParserValidationProblem getStrictPgnParserValidationProblem() {
    return strictPgnParserValidationProblem;
  }

  public SanValidationProblem getSanValidationProblem() {
    return sanValidationProblem;
  }

}
