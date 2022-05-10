package com.dlb.chess.common.exceptions;

public class FileSystemAccessException extends ChessApiRuntimeException {

  private static final String BASE_MESSAGE = "File system access problem";

  public FileSystemAccessException() {
    super(BASE_MESSAGE);
  }

  public FileSystemAccessException(String message, Throwable cause) {
    super(calculateMessage(BASE_MESSAGE, message), cause);
  }

  public FileSystemAccessException(String message) {
    super(calculateMessage(BASE_MESSAGE, message));
  }

}
