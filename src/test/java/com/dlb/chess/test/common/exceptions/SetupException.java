package com.dlb.chess.test.common.exceptions;

import com.dlb.chess.common.exceptions.ChessApiRuntimeException;

public class SetupException extends ChessApiRuntimeException {

  private static final String BASE_MESSAGE = "Invalid test";

  public SetupException() {
  }

  public SetupException(String message, Throwable cause) {
    super(BASE_MESSAGE + " - " + message, cause);
  }

  public SetupException(String message) {
    super(BASE_MESSAGE + " - " + message);
  }

}
