package com.dlb.chess.san.exceptions;

import com.dlb.chess.common.exceptions.UsageException;
import com.dlb.chess.san.enums.SanValidationProblem;

public class SanValidationException extends UsageException {

  private final SanValidationProblem sanValidationProblem;

  public SanValidationException(SanValidationProblem sanValidationProblem, String message) {
    super(message);
    this.sanValidationProblem = sanValidationProblem;
  }

  public SanValidationProblem getSanValidationProblem() {
    return sanValidationProblem;
  }

}
