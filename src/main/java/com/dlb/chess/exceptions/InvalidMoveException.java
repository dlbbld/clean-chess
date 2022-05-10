package com.dlb.chess.exceptions;

import com.dlb.chess.enums.MoveCheck;

public class InvalidMoveException extends RuntimeException {

  private final MoveCheck moveCheck;

  // TODO add an example of a legal to square to move validation, add it here (so it can be tested) and test it
  public InvalidMoveException(String message, MoveCheck moveCheck) {
    super(message);
    this.moveCheck = moveCheck;
  }

  public MoveCheck getMoveCheck() {
    return moveCheck;
  }

}
