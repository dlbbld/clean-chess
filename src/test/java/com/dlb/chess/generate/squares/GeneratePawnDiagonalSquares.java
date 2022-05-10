package com.dlb.chess.generate.squares;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;

public class GeneratePawnDiagonalSquares extends AbstractGenerateSquares {

  public static void main(String[] args) {
    generatePawnCode();

  }

  private static void generatePawnCode() {
    generateKnightKingPawnCode(WHITE, PAWN, GeneratePawnMoveType.DIAGONAL, 84);
    generateKnightKingPawnCode(BLACK, PAWN, GeneratePawnMoveType.DIAGONAL, 84);

  }

  static Set<Square> calculatePawnDiagonalBoardSquares(Side havingMove, Square fromSquare) {
    if (fromSquare == Square.NONE) {
      throw new IllegalArgumentException("The empty square is not supported");
    }

    final Set<Square> resultSet = new TreeSet<>();

    final Rank rank = fromSquare.getRank();
    if (Rank.calculateIsGroundRank(havingMove, rank) || Rank.calculateIsPromotionRank(havingMove, rank)) {
      // no moves possibles which we represent as empty list
      // needed later in legal move generation to find illegal moves
      return resultSet;
    }

    // left
    if (File.calculateHasRightFile(havingMove, fromSquare.getFile())) {
      final File rightFile = File.calculateRightFile(havingMove, fromSquare.getFile());
      final Rank nextRank = Rank.calculateNextRank(havingMove, fromSquare.getRank());
      processNonEmpty(resultSet, rightFile, nextRank);
    }

    // right
    if (File.calculateHasLeftFile(havingMove, fromSquare.getFile())) {
      final File leftFile = File.calculateLeftFile(havingMove, fromSquare.getFile());
      final Rank nextRank = Rank.calculateNextRank(havingMove, fromSquare.getRank());
      processNonEmpty(resultSet, leftFile, nextRank);
    }

    return resultSet;
  }

}
