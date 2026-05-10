package com.dlb.chess.test.fen.exception;

import com.dlb.chess.common.enums.FenAdvancedFurtherValidationProblem;
import com.dlb.chess.common.exceptions.UsageException;

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
