package com.dlb.chess.moves.legal.king;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.constants.CastlingConstants;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.enums.MoveCheck;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.utility.CastlingUtility;

public class KingCastlingLegalMoves extends KingLegalMoves {
  public static Set<LegalMove> calculateKingCastlingLegalMoves(StaticPosition staticPosition, Side havingMove,
      CastlingRight castlingRight) {

    final Set<LegalMove> legalMoveSet = new TreeSet<>();

    switch (havingMove) {
      case BLACK:
        if (CastlingUtility.calculateQueenSideCastlingCheck(staticPosition, havingMove,
            castlingRight) == MoveCheck.SUCCESS) {
          legalMoveSet.add(CastlingConstants.BLACK_QUEEN_SIDE_CASTLING_MOVE);
        }

        if (CastlingUtility.calculateKingSideCastlingCheck(staticPosition, havingMove,
            castlingRight) == MoveCheck.SUCCESS) {
          legalMoveSet.add(CastlingConstants.BLACK_KING_SIDE_CASTLING_MOVE);
        }
        break;
      case WHITE:
        if (CastlingUtility.calculateQueenSideCastlingCheck(staticPosition, havingMove,
            castlingRight) == MoveCheck.SUCCESS) {
          legalMoveSet.add(CastlingConstants.WHITE_QUEEN_SIDE_CASTLING_MOVE);
        }

        if (CastlingUtility.calculateKingSideCastlingCheck(staticPosition, havingMove,
            castlingRight) == MoveCheck.SUCCESS) {
          legalMoveSet.add(CastlingConstants.WHITE_KING_SIDE_CASTLING_MOVE);
        }
        break;
      case NONE:
      default:
        throw new IllegalArgumentException();

    }
    if (CastlingUtility.calculateQueenSideCastlingCheck(staticPosition, havingMove,
        castlingRight) == MoveCheck.SUCCESS) {
      legalMoveSet.add(new LegalMove(new MoveSpecification(havingMove, CastlingMove.QUEEN_SIDE)));
    }

    if (CastlingUtility.calculateKingSideCastlingCheck(staticPosition, havingMove,
        castlingRight) == MoveCheck.SUCCESS) {
      legalMoveSet.add(new LegalMove(new MoveSpecification(havingMove, CastlingMove.KING_SIDE)));
    }

    return legalMoveSet;
  }

}
