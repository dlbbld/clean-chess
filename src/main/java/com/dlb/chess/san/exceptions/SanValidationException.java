package com.dlb.chess.san.exceptions;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.CastlingRightLoss;
import com.dlb.chess.common.exceptions.UsageException;
import com.dlb.chess.enums.MoveCheck;
import com.dlb.chess.san.enums.SanValidationProblem;

public class SanValidationException extends UsageException {

  private final SanValidationProblem sanValidationProblem;
  private final @Nullable MoveCheck moveCheck;
  private final @Nullable CastlingRightLoss castlingRightLoss;

  public SanValidationException(SanValidationProblem sanValidationProblem, String message) {
    super(message);
    this.sanValidationProblem = sanValidationProblem;
    this.moveCheck = null;
    this.castlingRightLoss = null;
  }

  public SanValidationException(SanValidationProblem sanValidationProblem, String message, MoveCheck moveCheck,
      CastlingRightLoss castlingRightLoss) {
    super(message);
    this.sanValidationProblem = sanValidationProblem;
    this.moveCheck = moveCheck;
    this.castlingRightLoss = castlingRightLoss;
  }

  public SanValidationProblem getSanValidationProblem() {
    return sanValidationProblem;
  }

  public @Nullable MoveCheck getMoveCheck() {
    return moveCheck;
  }

  public @Nullable CastlingRightLoss getCastlingRightLoss() {
    return castlingRightLoss;
  }

}
