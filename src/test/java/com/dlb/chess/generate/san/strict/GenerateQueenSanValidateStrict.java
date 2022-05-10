package com.dlb.chess.generate.san.strict;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.EmptyBoardMove;
import com.dlb.chess.squares.emptyboard.AbstractEmptyBoardSquares;

public class GenerateQueenSanValidateStrict extends AbstractGenerateSanValidateStrict {

  @Override
  PieceType getPieceType() {
    return QUEEN;
  }

  public static void main(String[] args) {
    new GenerateQueenSanValidateStrict().generateSan();
  }

  @Override
  Set<String> calculateEnumConstantHandwoven() {

    final Set<String> resultSet = new TreeSet<>();

    for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
      // possible for all squares
      appendOnlyMove(resultSet, toSquare);

      // file always possible
      appendMoveWithAllFiles(resultSet, toSquare);

      final Set<EmptyBoardMove> emptyBoardMoveSet = AbstractEmptyBoardSquares.calculateNonPawnEmptyBoardMovesTo(QUEEN,
          toSquare);
      final List<Square> fromSquareList = calculateFromSquareList(emptyBoardMoveSet);

      // checking each from square individually
      for (final Square fromSquare : fromSquareList) {

        // rank
        if (calculateIsFromRankPossibleQueen(fromSquare, toSquare, fromSquareList)) {
          appendMoveWithRank(resultSet, toSquare, fromSquare.getRank());
        }

        // from square
        if (calculateIsFromRankPossibleQueen(fromSquare, toSquare, fromSquareList)
            && calculateHasOtherMovesFromSameRank(fromSquare, fromSquareList)) {
          appendMoveWithFromSquare(resultSet, toSquare, fromSquare);
        }
      }
    }
    return resultSet;
  }

  @Override
  Set<String> calculateEnumConstantFormal() {
    final Set<String> resultSet = new TreeSet<>();
    for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
      final Set<EmptyBoardMove> emptyBoardMoveSet = AbstractEmptyBoardSquares.calculateNonPawnEmptyBoardMovesTo(QUEEN,
          toSquare);
      final List<Square> fromSquareList = calculateFromSquareList(emptyBoardMoveSet);

      // checking each from square individually
      // file and rank
      for (final Square fromSquare : fromSquareList) {
        // there is a piece which can move to the to square
        appendOnlyMove(resultSet, toSquare);

        if (calculateIsFromFilePossibleOrthogonal(fromSquare, toSquare, fromSquareList)
            && calculateIsFromFilePossibleDiagonal(fromSquare, toSquare, fromSquareList)) {
          appendMoveWithFile(resultSet, toSquare, fromSquare.getFile());
        }

        if (calculateIsFromRankPossibleQueen(fromSquare, toSquare, fromSquareList)) {
          appendMoveWithRank(resultSet, toSquare, fromSquare.getRank());
        }
      }

      // checking each from square individually
      // square
      for (final Square fromSquare : fromSquareList) {
        // there is a piece which can move to the to square
        appendOnlyMove(resultSet, toSquare);

        if (calculateIsFromRankPossibleQueen(fromSquare, toSquare, fromSquareList)
            && calculateHasOtherMovesFromSameRank(fromSquare, fromSquareList)) {
          appendMoveWithFromSquare(resultSet, toSquare, fromSquare);
        }
      }
    }

    return resultSet;
  }

  private static boolean calculateIsFromRankPossibleQueen(Square fromSquare, Square toSquare,
      List<Square> fromSquareList) {
    for (final Square otherFromSquare : fromSquareList) {

      if (otherFromSquare.getFile() == fromSquare.getFile() && otherFromSquare.getRank() != fromSquare.getRank()
          && (!calculateIsToSquareOnSameFile(fromSquare, toSquare)
              || calculateIsOppositeVertical(fromSquare, toSquare, otherFromSquare))) {
        return true;
      }
    }
    return false;
  }

  private static boolean calculateIsToSquareOnSameFile(Square fromSquare, Square toSquare) {
    return fromSquare.getFile() == toSquare.getFile();
  }
}
