package com.dlb.chess.unwinnability.findhelpmate;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.board.enums.SquareType;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.utility.MaterialUtility;
import com.dlb.chess.distance.KingDistance;
import com.dlb.chess.distance.KnightDistance;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.unwinnability.findhelpmate.enums.Goal;

//Figure 13 Going-to-corner routine used in Figure 12.
public class GoingToCorner implements EnumConstants {

  // Inputs: position, legal move in the position, objective (Win or Lose)
  // Output: bool (indicating whether or not m is leading to a corner mating position)
  public static boolean goingToCorner(Side color, StaticPosition staticPosition, LegalMove m, Goal goal) {

    // 1: let P be moved piece in m and let s be the square P is moving to

    final Piece p;
    final Square s;
    final Square fromSquare;
    if (CastlingUtility.calculateIsCastlingMove(m.moveSpecification())) {
      // the API does not treat the king as the moving piece for the castling move
      // that castling is a king move does not mean the king is the moving piece in the move
      p = Piece.calculateKingPiece(m.moveSpecification().havingMove());
      s = CastlingUtility.calculateKingCastlingTo(m.moveSpecification());
      fromSquare = CastlingUtility.calculateKingCastlingFrom(m.moveSpecification());
    } else {
      // let P be moved piece in m
      p = m.movingPiece();

      // let s be the square P is moving to
      s = m.moveSpecification().toSquare();

      fromSquare = m.moveSpecification().fromSquare();
    }

    // 2: if P.type not in {K,N} then return false ( -> We focus on “slow” (non-sliding) pieces
    // that could take several turns to reach the desired square)
    if (p.getPieceType() != PieceType.KING && p.getPieceType() != PieceType.KNIGHT) {
      return false;
    }

    // 3: if the intended winner has dark-squared bishops or the intended loser has lightsquared
    // bishops (and the intended winner does not) then ( -> The target corner is set
    // to be h8)
    Square target;
    if (MaterialUtility.calculateHasNoBishops(color, staticPosition, SquareType.DARK_SQUARE)
        && (MaterialUtility.calculateNumberOfPieces(color, staticPosition, PieceType.BISHOP) != 0 || MaterialUtility
            .calculateHasNoBishops(color.getOppositeSide(), staticPosition, SquareType.LIGHT_SQUARE))) {
      // 4: set target := if goal = Win then (P.type=K)?h6 : h8 else (P.type =K)?h8 : g8
      target = goal == Goal.WIN ? p.getPieceType() == KING ? H6 : H8 : p.getPieceType() == KING ? H8 : G8;

      // 5: else ( -> The target corner is set to be a8)
    } else {
      // 6: set target := if goal = Win then (P.type=K)?a6 : a8 else (P.type =K)?a8 : b8
      target = goal == Goal.WIN ? p.getPieceType() == KING ? A6 : A8 : p.getPieceType() == KING ? A8 : B8;
    }
    // 7: if the intended winner has the black pieces then
    if (color == BLACK) {
      // 8: set target := (flip-rank  flip-file)(target) . Flip the target with respect to the
      // center of the board (a8 becomes h1, and h8 becomes a1)
      //
      target = Square.flip(target);
    }

    // 9: if P.type =K then return king-distance(s, target) < king-distance(P.sq, target)
    if (p.getPieceType() == KING) {
      return KingDistance.distance(s, target) < KingDistance.distance(fromSquare, target);
    }
    // 10: else return knight-distance(s, target) < knight-distance(P.sq, target) ( -> P.type =N)
    return KnightDistance.distance(s, target) < KnightDistance.distance(fromSquare, target);

  }
}
