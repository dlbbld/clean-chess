package com.dlb.chess.pgn.reader.exceptions;

import com.dlb.chess.common.exceptions.UsageException;
import com.dlb.chess.pgn.reader.enums.PgnReaderStrictValidationProblem;
import com.dlb.chess.san.enums.SanValidationProblem;

public class PgnReaderStrictValidationException extends UsageException {

  private final PgnReaderStrictValidationProblem pgnReaderStrictValidationProblem;

  private final SanValidationProblem sanValidationProblem;

  public PgnReaderStrictValidationException(PgnReaderStrictValidationProblem pgnReaderStrictValidationProblem,
      SanValidationProblem sanValidationProblem, String message) {
    super(message);
    this.pgnReaderStrictValidationProblem = pgnReaderStrictValidationProblem;
    this.sanValidationProblem = sanValidationProblem;
  }

  public PgnReaderStrictValidationProblem getPgnReaderStrictValidationProblem() {
    return pgnReaderStrictValidationProblem;
  }

  public SanValidationProblem getSanValidationProblem() {
    return sanValidationProblem;
  }

}
