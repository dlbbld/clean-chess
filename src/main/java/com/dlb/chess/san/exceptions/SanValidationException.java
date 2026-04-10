package com.dlb.chess.san.exceptions;

import com.dlb.chess.board.enums.CastlingRightLoss;
import com.dlb.chess.common.exceptions.UsageException;
import com.dlb.chess.enums.MoveCheck;
import com.dlb.chess.san.enums.SanValidationProblem;

public class SanValidationException extends UsageException {

  private final SanValidationProblem sanValidationProblem;
  private final MoveCheck moveCheck;
  private final CastlingRightLoss castlingRightLoss;

  public SanValidationException(SanValidationProblem sanValidationProblem, String message) {
    this(sanValidationProblem, message, MoveCheck.SUCCESS, CastlingRightLoss.NONE);
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

  public MoveCheck getMoveCheck() {
    return moveCheck;
  }

  public CastlingRightLoss getCastlingRightLoss() {
    return castlingRightLoss;
  }

}
