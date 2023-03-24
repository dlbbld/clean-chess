package com.dlb.chess.generate.san.strict;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.EmptyBoardMove;
import com.dlb.chess.model.PawnDiagonalBoardMove;
import com.dlb.chess.moves.utility.PawnDiagonalMoveUtility;
import com.dlb.chess.squares.emptyboard.AbstractEmptyBoardSquares;

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
    for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
      if (!Rank.calculateIsGroundRank(getSide(), toSquare.getRank())
          && !Rank.calculateIsPawnInititalRank(getSide(), toSquare.getRank())) {
        appendOnlyMove(resultSet, toSquare);
      }
    }

    // diagonal moves
    for (final Square fromSquare : Square.BOARD_SQUARE_LIST) {
      if (!Rank.calculateIsGroundRank(getSide(), fromSquare.getRank())
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

    final Set<PawnDiagonalBoardMove> diagonalMoveSet = PawnDiagonalMoveUtility.calculatePawnDiagonalMoves(getSide());
    for (final PawnDiagonalBoardMove diagonalMove : diagonalMoveSet) {
      appendMoveWithFile(resultSet, diagonalMove.toSquare(), diagonalMove.fromSquare().getFile());
    }

    return resultSet;
  }

}
