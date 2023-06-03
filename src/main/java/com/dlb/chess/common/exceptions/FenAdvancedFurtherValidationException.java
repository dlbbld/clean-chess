package com.dlb.chess.common.exceptions;

import com.dlb.chess.common.enums.FenAdvancedFurtherValidationProblem;

public class FenAdvancedFurtherValidationException extends UsageException {

  private final FenAdvancedFurtherValidationProblem fenAdvancedFurtherValidationProblem;

  public FenAdvancedFurtherValidationException(FenAdvancedFurtherValidationProblem fenAdvancedFurtherValidationProblem,
      String message) {
    super(message);
    this.fenAdvancedFurtherValidationProblem = fenAdvancedFurtherValidationProblem;
  }

  public FenAdvancedFurtherValidationProblem getFenAdvancedFurtherValidationProblem() {
    return fenAdvancedFurtherValidationProblem;
  }

}
