package com.dlb.chess.common.exceptions;

import com.dlb.chess.common.enums.FenAdvancedValidationProblem;

public class FenAdvancedValidationException extends UsageException {

  private final FenAdvancedValidationProblem fenAdvancedValidationProblem;

  public FenAdvancedValidationException(FenAdvancedValidationProblem fenAdvancedValidationProblem, String message) {
    super(message);
    this.fenAdvancedValidationProblem = fenAdvancedValidationProblem;
  }

  public FenAdvancedValidationProblem getFenAdvancedValidationProblem() {
    return fenAdvancedValidationProblem;
  }

}
