package com.dlb.chess.san.validate;

import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.enums.CheckmateOrCheck;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;

public abstract class SanValidateCheck extends AbstractSan {

  public static void validateCheckmateOrCheck(ApiBoard board, CheckmateOrCheck sanCheckmateOrCheck,
      MoveSpecification moveSpecification) {
    final CheckmateOrCheck boardCheckmateOrCheck = calculateBoardCheckmateOrCheck(board, moveSpecification);

    switch (sanCheckmateOrCheck) {
      case CHECKMATE:
        switch (boardCheckmateOrCheck) {
          case CHECKMATE:
            return;
          case CHECK:
            throw new SanValidationException(SanValidationProblem.CHECKMATE_BUT_CHECK_ONLY,
                "It's check only but checkmate is specified");
          case NONE:
            throw new SanValidationException(SanValidationProblem.CHECKMATE_BUT_NONE,
                "It's not checkmate but checkmate is specified");
          default:
            throw new IllegalArgumentException();
        }
      case CHECK:
        switch (boardCheckmateOrCheck) {
          case CHECKMATE:
            throw new SanValidationException(SanValidationProblem.CHECK_BUT_CHECKMATE,
                "It's checkmate but check is specified");
          case CHECK:
            break;
          case NONE:
            throw new SanValidationException(SanValidationProblem.CHECK_BUT_NONE,
                "It's not check but check is specified");
          default:
            throw new IllegalArgumentException();
        }
        break;
      case NONE:
        switch (boardCheckmateOrCheck) {
          case CHECKMATE:
            throw new SanValidationException(SanValidationProblem.NONE_BUT_CHECKMATE,
                "It's checkmate but that is not specified");
          case CHECK:
            throw new SanValidationException(SanValidationProblem.NONE_BUT_CHECK,
                "It's check but that is not specified");
          case NONE:
            break;
          default:
            throw new IllegalArgumentException();
        }
        break;
      default:
        throw new IllegalArgumentException();
    }
  }

  private static CheckmateOrCheck calculateBoardCheckmateOrCheck(ApiBoard board, MoveSpecification moveSpecification) {
    board.performMove(moveSpecification);

    CheckmateOrCheck checkmateOrCheck;
    if (board.isCheckmate()) {
      checkmateOrCheck = CheckmateOrCheck.CHECKMATE;
    } else if (board.isCheck()) {
      checkmateOrCheck = CheckmateOrCheck.CHECK;
    } else {
      checkmateOrCheck = CheckmateOrCheck.NONE;
    }
    board.unperformMove();

    return checkmateOrCheck;
  }
}
