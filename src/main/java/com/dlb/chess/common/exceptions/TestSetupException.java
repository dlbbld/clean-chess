package com.dlb.chess.common.exceptions;

public class TestSetupException extends ChessApiRuntimeException {

  private static final String BASE_MESSAGE = "Invalid test";

  public TestSetupException() {
  }

  public TestSetupException(String message, Throwable cause) {
    super(calculateMessage(BASE_MESSAGE, message), cause);
  }

  public TestSetupException(String message) {
    super(calculateMessage(BASE_MESSAGE, message));
  }

}
