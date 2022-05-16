package com.dlb.chess.unwinnability.findhelpmate.comparator;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.squares.to.threaten.AbstractThreatenSquares;

public abstract class AbstractLegalMovesComparator implements Comparator<LegalMove> {

  static final int NO_ORDER = 111;

  final Side color;
  final Side havingMove;
  final StaticPosition staticPosition;
  final Set<Square> squaresAttackedByNotHavingMove;
  final Set<Square> emptySquaresAttackedByNotHavingMove;

  abstract int compareHavingMove(LegalMove firstLegalMove, LegalMove secondLegalMove);

  abstract int compareNotHavingMove(LegalMove firstLegalMove, LegalMove secondLegalMove);

  public AbstractLegalMovesComparator(Side color, Side havingMove, StaticPosition staticPosition) {
    this.color = color;
    this.havingMove = havingMove;
    this.staticPosition = staticPosition;
    this.squaresAttackedByNotHavingMove = AbstractThreatenSquares.calculateThreatenedSquares(staticPosition,
        havingMove.getOppositeSide());
    this.emptySquaresAttackedByNotHavingMove = new TreeSet<>();
    for (final Square square : squaresAttackedByNotHavingMove) {
      if (staticPosition.isEmpty(square)) {
        emptySquaresAttackedByNotHavingMove.add(square);
      }
    }

  }

  @Override
  public int compare(LegalMove firstLegalMove, LegalMove secondLegalMove) {

    // castling moves have least precedence
    if (!CastlingUtility.calculateIsCastlingMove(firstLegalMove.moveSpecification())
        && CastlingUtility.calculateIsCastlingMove(secondLegalMove.moveSpecification())) {
      return -1;
    }
    if (CastlingUtility.calculateIsCastlingMove(firstLegalMove.moveSpecification())
        && !CastlingUtility.calculateIsCastlingMove(secondLegalMove.moveSpecification())) {
      return 1;
    }
    if (CastlingUtility.calculateIsCastlingMove(firstLegalMove.moveSpecification())
        && CastlingUtility.calculateIsCastlingMove(secondLegalMove.moveSpecification())) {
      return 0;
    }

    // in the following we have no castling move which is essential because the moving piece type is not set for the
    // castling move by design
    // using it would cause exceptions

    // intended winner is moving
    if (color == havingMove) {
      return compareHavingMove(firstLegalMove, secondLegalMove);
    }
    // intended loser is moving
    return compareNotHavingMove(firstLegalMove, secondLegalMove);
  }
}
