package com.dlb.chess.fen.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.CastlingRightBoth;

public record Fen(String fen, StaticPosition staticPosition, Side havingMove, CastlingRightBoth castlingRightBoth,
    Square enPassantCaptureTargetSquare, int halfMoveClock, int fullMoveNumber) {

  public String fen() {
    return fen;
  }

  public StaticPosition staticPosition() {
    return staticPosition;
  }

  public Side havingMove() {
    return havingMove;
  }

  public CastlingRightBoth castlingRightBoth() {
    return castlingRightBoth;
  }

  public Square enPassantCaptureTargetSquare() {
    return enPassantCaptureTargetSquare;
  }

  public int halfMoveClock() {
    return halfMoveClock;
  }

  public int fullMoveNumber() {
    return fullMoveNumber;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (Fen) obj;
    return Objects.equals(castlingRightBoth, other.castlingRightBoth)
        && enPassantCaptureTargetSquare == other.enPassantCaptureTargetSquare && Objects.equals(fen, other.fen)
        && fullMoveNumber == other.fullMoveNumber && halfMoveClock == other.halfMoveClock
        && havingMove == other.havingMove && Objects.equals(staticPosition, other.staticPosition);
  }
}
