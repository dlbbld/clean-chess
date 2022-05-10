package com.dlb.chess.common.exceptions;

import com.dlb.chess.common.enums.FenValidationProblem;

public class FenValidationException extends UsageException {

  private final FenValidationProblem fenValidationProblem;

  public FenValidationException(FenValidationProblem fenValidationProblem, String message) {
    super(message);
    this.fenValidationProblem = fenValidationProblem;
  }

  public FenValidationProblem getFenValidationProblem() {
    return fenValidationProblem;
  }

}
