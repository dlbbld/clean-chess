package com.dlb.chess.san.validate;

import com.dlb.chess.common.interfaces.ChessBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.messages.Message;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.enums.SanTerminalMarker;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;

public abstract class SanValidateCheck extends AbstractSan {

  public static void validateSanTerminalMarker(ChessBoard board, SanTerminalMarker sanSanTerminalMarker,
      MoveSpecification moveSpecification) {
    final SanTerminalMarker boardSanTerminalMarker = calculateBoardSanTerminalMarker(board, moveSpecification);

    switch (sanSanTerminalMarker) {
      case CHECKMATE:
        switch (boardSanTerminalMarker) {
          case CHECKMATE:
            return;
          case CHECK:
            throw new SanValidationException(SanValidationProblem.CHECKMATE_SYMBOL_BUT_CHECK_ONLY,
                Message.getString("validation.san.checkmateSymbolButCheckOnly"));
          case NONE:
            throw new SanValidationException(SanValidationProblem.CHECKMATE_SYMBOL_BUT_NO_CHECK,
                Message.getString("validation.san.checkmateSymbolButNoCheck"));
          default:
            throw new IllegalArgumentException();
        }
      case CHECK:
        switch (boardSanTerminalMarker) {
          case CHECKMATE:
            throw new SanValidationException(SanValidationProblem.CHECK_SYMBOL_BUT_CHECKMATE,
                Message.getString("validation.san.checkSymbolButCheckmate"));
          case CHECK:
            break;
          case NONE:
            throw new SanValidationException(SanValidationProblem.CHECK_SYMBOL_BUT_NO_CHECK,
                Message.getString("validation.san.checkSymbolButNoCheck"));
          default:
            throw new IllegalArgumentException();
        }
        break;
      case NONE:
        switch (boardSanTerminalMarker) {
          case CHECKMATE:
            throw new SanValidationException(SanValidationProblem.NO_SYMBOL_BUT_CHECKMATE,
                Message.getString("validation.san.noSymbolButCheckmate"));
          case CHECK:
            throw new SanValidationException(SanValidationProblem.NO_SYMBOL_BUT_CHECK,
                Message.getString("validation.san.noSymbolButCheck"));
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

  private static SanTerminalMarker calculateBoardSanTerminalMarker(ChessBoard board,
      MoveSpecification moveSpecification) {
    board.performMove(moveSpecification);

    SanTerminalMarker sanTerminalMarker;
    if (board.isCheckmate()) {
      sanTerminalMarker = SanTerminalMarker.CHECKMATE;
    } else if (board.isCheck()) {
      sanTerminalMarker = SanTerminalMarker.CHECK;
    } else {
      sanTerminalMarker = SanTerminalMarker.NONE;
    }
    board.unperformMove();

    return sanTerminalMarker;
  }
}
