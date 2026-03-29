package com.dlb.chess.pgn.parser.exceptions;

import com.dlb.chess.common.exceptions.UsageException;
import com.dlb.chess.pgn.parser.enums.LenientPgnParserValidationProblem;
import com.dlb.chess.san.enums.SanValidationProblem;

public class LenientPgnParserValidationException extends UsageException {

  private final LenientPgnParserValidationProblem lenientPgnParserValidationProblem;

  private final SanValidationProblem sanValidationProblem;

  public LenientPgnParserValidationException(LenientPgnParserValidationProblem lenientPgnParserValidationProblem,
      SanValidationProblem sanValidationProblem, String message) {
    super(message);
    this.lenientPgnParserValidationProblem = lenientPgnParserValidationProblem;
    this.sanValidationProblem = sanValidationProblem;
  }

  public LenientPgnParserValidationProblem getLenientPgnParserValidationProblem() {
    return lenientPgnParserValidationProblem;
  }

  public SanValidationProblem getSanValidationProblem() {
    return sanValidationProblem;
  }

}