package com.dlb.chess.test.generate.san.strict;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.EmptyBoardMove;
import com.dlb.chess.squares.emptyboard.AbstractEmptyBoardSquares;
import com.dlb.chess.squares.pawn.diagonal.PawnDiagonalSquares;

//TODO make the count for the possible SAN
public abstract class AbstractPawnSanValidateStrict extends AbstractGenerateSanValidateStrict {

  @Override
  PieceType getPieceType() {
    return PAWN;
  }

  abstract Side getSide();

  @Override
  Set<String> calculateEnumConstantHandwoven() {

    final Set<String> resultSet = new TreeSet<>();

    // one square advance (includes two square advance)
    for (final Square toSquare : Square.REAL) {
      if (toSquare.getRank() != Rank.calculateGroundRank(getSide())
          && toSquare.getRank() != Rank.calculatePawnInitialRank(getSide())) {
        appendOnlyMove(resultSet, toSquare);
      }
    }

    // diagonal moves
    for (final Square fromSquare : Square.REAL) {
      if (fromSquare.getRank() != Rank.calculateGroundRank(getSide())
          && !Rank.calculateIsPromotionRank(getSide(), fromSquare.getRank())) {

        // diagonal left
        if (Square.calculateHasLeftDiagonalSquare(getSide(), fromSquare)) {
          final Square leftDiagonalSquare = Square.calculateLeftDiagonalSquare(getSide(), fromSquare);
          appendMoveWithFile(resultSet, leftDiagonalSquare, fromSquare.getFile());
        }
        // diagonal right
        if (Square.calculateHasRightDiagonalSquare(getSide(), fromSquare)) {
          final Square rightDiagonalSquare = Square.calculateRightDiagonalSquare(getSide(), fromSquare);
          appendMoveWithFile(resultSet, rightDiagonalSquare, fromSquare.getFile());
        }

      }
    }

    return resultSet;
  }

  @Override
  Set<String> calculateEnumConstantFormal() {
    final Set<String> resultSet = new TreeSet<>();

    for (final EmptyBoardMove move : AbstractEmptyBoardSquares.calculatePawnEmptyBoardMoves(getSide())) {
      appendOnlyMove(resultSet, move.toSquare());
    }

    for (final Square fromSquare : Square.REAL) {
      for (final Square diagonalSquare : PawnDiagonalSquares.getPawnDiagonalSquares(getSide(), fromSquare)) {
        appendMoveWithFile(resultSet, diagonalSquare, fromSquare.getFile());
      }
    }

    return resultSet;
  }

}
