package com.dlb.chess.test.apicarlos.utility;

import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.test.apicarlos.NonNullWrapperApiCarlos;
import com.dlb.chess.test.apicomparison.utility.EnumConversionUtility;
import com.github.bhlangonijr.chesslib.move.Move;

public abstract class MoveConversionUtility {

  // the SAN is only set when the game is loaded from the PGN
  // this method is only for this case
  public static MoveSpecification convertMove(Move move, String san) {
    switch (san) {
      case "O-O":
      case "O-O+":
      case "O-O#":
        return new MoveSpecification(CastlingMove.KING_SIDE);
      case "O-O-O":
      case "O-O-O+":
      case "O-O-O#":
        return new MoveSpecification(CastlingMove.QUEEN_SIDE);
      default:
        break;
    }
    return convertNonCastlingMove(move);
  }

  // when having created the board and the piece can be determined
  public static MoveSpecification convertMove(Move move, com.github.bhlangonijr.chesslib.Piece movingPiece) {
    if (movingPiece == com.github.bhlangonijr.chesslib.Piece.WHITE_KING) {
      if (move.getFrom() == com.github.bhlangonijr.chesslib.Square.E1
          && move.getTo() == com.github.bhlangonijr.chesslib.Square.G1) {
        return new MoveSpecification(CastlingMove.KING_SIDE);
      }
      if (move.getFrom() == com.github.bhlangonijr.chesslib.Square.E1
          && move.getTo() == com.github.bhlangonijr.chesslib.Square.C1) {
        return new MoveSpecification(CastlingMove.QUEEN_SIDE);
      }
    }
    if (movingPiece == com.github.bhlangonijr.chesslib.Piece.BLACK_KING) {
      if (move.getFrom() == com.github.bhlangonijr.chesslib.Square.E8
          && move.getTo() == com.github.bhlangonijr.chesslib.Square.G8) {
        return new MoveSpecification(CastlingMove.KING_SIDE);
      }
      if (move.getFrom() == com.github.bhlangonijr.chesslib.Square.E8
          && move.getTo() == com.github.bhlangonijr.chesslib.Square.C8) {
        return new MoveSpecification(CastlingMove.QUEEN_SIDE);
      }
    }
    return convertNonCastlingMove(move);
  }

  private static MoveSpecification convertNonCastlingMove(Move move) {
    final com.github.bhlangonijr.chesslib.Square from = NonNullWrapperApiCarlos.getFrom(move);
    final com.github.bhlangonijr.chesslib.Square to = NonNullWrapperApiCarlos.getTo(move);

    final Square fromSquare = EnumConversionUtility.convertToMySquare(from);
    final Square toSquare = EnumConversionUtility.convertToMySquare(to);

    final com.github.bhlangonijr.chesslib.Piece promotion = NonNullWrapperApiCarlos.getPromotion(move);

    if (promotion != com.github.bhlangonijr.chesslib.Piece.NONE) {
      final PromotionPieceType promotionPieceType = EnumConversionUtility.convertToMyPromotionPieceType(promotion);
      return new MoveSpecification(fromSquare, toSquare, promotionPieceType);
    }

    return new MoveSpecification(fromSquare, toSquare);
  }

  public static Move convertMoveSpecification(Side havingMove, MoveSpecification moveSpecification) {
    if (CastlingUtility.calculateIsCastlingMove(moveSpecification)) {
      return switch (havingMove) {
        case BLACK -> switch (moveSpecification.castlingMove()) {
          case KING_SIDE -> new Move(com.github.bhlangonijr.chesslib.Square.E8,
              com.github.bhlangonijr.chesslib.Square.G8);
          case QUEEN_SIDE -> new Move(com.github.bhlangonijr.chesslib.Square.E8,
              com.github.bhlangonijr.chesslib.Square.C8);
          case NONE -> throw new IllegalArgumentException();
          default -> throw new IllegalArgumentException();
        };
        case WHITE -> switch (moveSpecification.castlingMove()) {
          case KING_SIDE -> new Move(com.github.bhlangonijr.chesslib.Square.E1,
              com.github.bhlangonijr.chesslib.Square.G1);
          case QUEEN_SIDE -> new Move(com.github.bhlangonijr.chesslib.Square.E1,
              com.github.bhlangonijr.chesslib.Square.C1);
          case NONE -> throw new IllegalArgumentException();
          default -> throw new IllegalArgumentException();
        };
        case NONE -> throw new IllegalArgumentException();
        default -> throw new IllegalArgumentException();
      };
    }

    final com.github.bhlangonijr.chesslib.Square from = EnumConversionUtility
        .convertToSquare(moveSpecification.fromSquare());
    final com.github.bhlangonijr.chesslib.Square to = EnumConversionUtility
        .convertToSquare(moveSpecification.toSquare());
    final com.github.bhlangonijr.chesslib.Piece promotion = EnumConversionUtility.convertToPiece(havingMove,
        moveSpecification.promotionPieceType());
    return new Move(from, to, promotion);
  }
}
