package com.dlb.chess.common.exceptions;

public abstract class UsageException extends ChessApiRuntimeException {

  public UsageException() {
  }

  public UsageException(String message, Throwable cause) {
    super(message, cause);
  }

  public UsageException(String message) {
    super(message);
  }

}
