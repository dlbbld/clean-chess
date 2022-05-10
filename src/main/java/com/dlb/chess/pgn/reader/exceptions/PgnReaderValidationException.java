package com.dlb.chess.pgn.reader.exceptions;

import com.dlb.chess.common.exceptions.UsageException;
import com.dlb.chess.pgn.reader.enums.PgnReaderValidationProblem;
import com.dlb.chess.san.enums.SanValidationProblem;

public class PgnReaderValidationException extends UsageException {

  private final PgnReaderValidationProblem pgnReaderValidationProblem;

  private final SanValidationProblem sanValidationProblem;

  public PgnReaderValidationException(PgnReaderValidationProblem pgnReaderValidationProblem,
      SanValidationProblem sanValidationProblem, String message) {
    super(message);
    this.pgnReaderValidationProblem = pgnReaderValidationProblem;
    this.sanValidationProblem = sanValidationProblem;
  }

  public PgnReaderValidationProblem getPgnReaderValidationProblem() {
    return pgnReaderValidationProblem;
  }

  public SanValidationProblem getSanValidationProblem() {
    return sanValidationProblem;
  }

}