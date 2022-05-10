package com.dlb.chess.generate.san.strict;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.EmptyBoardMove;
import com.dlb.chess.squares.emptyboard.AbstractEmptyBoardSquares;

public class GenerateBishopSanValidateStrict extends AbstractGenerateSanValidateStrict {

  @Override
  PieceType getPieceType() {
    return BISHOP;
  }

  public static void main(String[] args) {
    new GenerateBishopSanValidateStrict().generateSan();
  }

  @Override
  Set<String> calculateEnumConstantHandwoven() {

    final Set<String> resultSet = new TreeSet<>();

    for (final Square toSquare : Square.BOARD_SQUARE_LIST) {

      appendOnlyMove(resultSet, toSquare);

      final Set<File> possibleFileList = new TreeSet<>();
      final Set<Rank> possibleRankList = new TreeSet<>();
      final Set<Square> possibleSquareList = new TreeSet<>();

      if (!FIRST_QUADRAT_EDGE_SQUARES.contains(toSquare)) {

        for (final EmptyBoardMove move : AbstractEmptyBoardSquares.calculateNonPawnEmptyBoardMoves(BISHOP, toSquare)) {
          final Square squareFromWhichBishopCanMoveToToSquare = move.toSquare();
          possibleFileList.add(squareFromWhichBishopCanMoveToToSquare.getFile());

          // if other bishop on same file we add the rank
          var isBishopOnSameFilePossible = false;
          var isBishopOnSameRankPossible = false;
          for (final EmptyBoardMove otherMove : AbstractEmptyBoardSquares.calculateNonPawnEmptyBoardMoves(BISHOP,
              toSquare)) {
            if (otherMove.equals(move)) {
              continue;
            }
            if (otherMove.toSquare().getFile() == move.toSquare().getFile()) {
              isBishopOnSameFilePossible = true;
              possibleRankList.add(squareFromWhichBishopCanMoveToToSquare.getRank());
            }
            if (otherMove.toSquare().getRank() == move.toSquare().getRank()) {
              isBishopOnSameRankPossible = true;
            }
          }
          if (isBishopOnSameFilePossible && isBishopOnSameRankPossible) {
            possibleSquareList.add(squareFromWhichBishopCanMoveToToSquare);
          }
        }
      }
      for (final File file : possibleFileList) {
        appendMoveWithFile(resultSet, toSquare, file);
      }
      for (final Rank rank : possibleRankList) {
        appendMoveWithRank(resultSet, toSquare, rank);
      }
      for (final Square square : possibleSquareList) {
        appendMoveWithFromSquare(resultSet, toSquare, square);
      }
    }

    return resultSet;

  }

  @Override
  Set<String> calculateEnumConstantFormal() {
    final Set<String> resultSet = new TreeSet<>();
    for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
      final Set<EmptyBoardMove> emptyBoardMoveSet = AbstractEmptyBoardSquares.calculateNonPawnEmptyBoardMovesTo(BISHOP,
          toSquare);
      final List<Square> fromSquareList = calculateFromSquareList(emptyBoardMoveSet);

      // checking each from square individually
      // file and rank
      for (final Square fromSquare : fromSquareList) {
        // there is a piece which can move to the to square
        appendOnlyMove(resultSet, toSquare);

        if (calculateIsFromFilePossibleDiagonal(fromSquare, toSquare, fromSquareList)) {
          appendMoveWithFile(resultSet, toSquare, fromSquare.getFile());
        }

        if (calculateIsFromRankPossibleBishop(fromSquare, fromSquareList)) {
          appendMoveWithRank(resultSet, toSquare, fromSquare.getRank());
        }
      }

      // checking each from square individually
      // square
      for (final Square fromSquare : fromSquareList) {
        // there is a piece which can move to the to square
        appendOnlyMove(resultSet, toSquare);

        if (calculateIsFromRankPossibleBishop(fromSquare, fromSquareList)
            && calculateHasOtherMovesFromSameRank(fromSquare, fromSquareList)) {
          appendMoveWithFromSquare(resultSet, toSquare, fromSquare);
        }
      }
    }

    return resultSet;
  }

  private static boolean calculateIsFromRankPossibleBishop(Square fromSquare, List<Square> fromSquareList) {
    for (final Square otherFromSquare : fromSquareList) {
      if (otherFromSquare.getFile() == fromSquare.getFile() && otherFromSquare.getRank() != fromSquare.getRank()) {
        return true;
      }
    }
    return false;
  }
}
