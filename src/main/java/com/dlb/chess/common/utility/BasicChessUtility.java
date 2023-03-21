package com.dlb.chess.common.utility;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.DynamicPosition;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.fen.FenParserRaw;
import com.dlb.chess.model.CastlingRightBoth;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.utility.CastlingUtility;

public class BasicChessUtility {

  public static String calculateSanList(List<HalfMove> halfMoveList) {
    final List<String> sanList = new ArrayList<>();
    for (final HalfMove halfMove : halfMoveList) {
      sanList.add(halfMove.san());
    }
    return BasicUtility.calculateCommaSeparatedList(sanList);
  }

  public static String calculatePiecePlacementList(List<HalfMove> halfMoveList) {
    final List<String> piecePlacementList = new ArrayList<>();
    for (final HalfMove halfMove : halfMoveList) {
      final String fen = halfMove.fen();
      final String piecePlacement = FenParserRaw.parsePiecePlacement(fen);
      piecePlacementList.add(piecePlacement);
    }
    return BasicUtility.calculateCommaSeparatedList(piecePlacementList);
  }

  public static String calculateDynamicPositionIdentifierList(List<HalfMove> halfMoveList) {
    final List<String> piecePlacementList = new ArrayList<>();
    for (final HalfMove halfMove : halfMoveList) {
      final String dynamicPositionIdentifier = calculateDynamicPositionIdentifier(halfMove);
      piecePlacementList.add(dynamicPositionIdentifier);
    }
    return BasicUtility.calculateCommaSeparatedList(piecePlacementList);
  }

  public static String calculateDynamicPositionIdentifier(HalfMove halfMove) {
    final StringBuilder result = new StringBuilder();

    final DynamicPosition dynamicPosition = halfMove.dynamicPosition();
    result.append(dynamicPosition.havingMove()).append("-");

    final String fen = halfMove.fen();
    final String piecePlacement = FenParserRaw.parsePiecePlacement(fen);
    result.append(piecePlacement).append("-");

    result.append(dynamicPosition.isEnPassantCapturePossible()).append("-");

    final CastlingRightBoth castlingRightBoth = dynamicPosition.castlingRightBoth();

    final CastlingRight whiteCastlingRight = CastlingUtility.getCastlingRight(castlingRightBoth, Side.WHITE);
    result.append(whiteCastlingRight.getHasKingSide()).append("-");
    result.append(whiteCastlingRight.getHasQueenSide()).append("-");

    final CastlingRight blackCastlingRight = CastlingUtility.getCastlingRight(castlingRightBoth, Side.BLACK);
    result.append(blackCastlingRight.getHasKingSide()).append("-");
    result.append(blackCastlingRight.getHasQueenSide());

    return NonNullWrapperCommon.toString(result);
  }

  public static Side calculateSideHavingMoveForFen(String fen) {
    return calculateSideHavingMoveForSide(FenParserRaw.parseHavingMove(fen));
  }

  public static Side calculateSideHavingMoveForSide(String side) {
    return switch (side) {
      case "b" -> Side.BLACK;
      case "w" -> Side.WHITE;
      default -> throw new IllegalArgumentException();
    };
  }

  public static Side calculateSideMoved(Side havingMoveInitial, int halfMoveCount) {
    switch (havingMoveInitial) {
      case BLACK:
        if (halfMoveCount % 2 == 0) {
          return Side.WHITE;
        }
        return Side.BLACK;
      case WHITE:
        if (halfMoveCount % 2 == 0) {
          return Side.BLACK;
        }
        return Side.WHITE;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }

  }

  public static int calculateFullMoveNumber(Side havingMoveInitial, int fullMoveNumberInitial,
      int performedHalfMoveCount) {
    return switch (havingMoveInitial) {
      case BLACK -> fullMoveNumberInitial + (int) StrictMath.floor(performedHalfMoveCount / 2.0);
      case WHITE -> fullMoveNumberInitial + (int) StrictMath.floor((performedHalfMoveCount - 1) / 2.0);
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static GameStatus calculateGameStatus(ApiBoard board) {

    if (board.isCheckmate()) {
      return GameStatus.CHECKMATE;
    }
    if (board.isStalemate()) {
      return GameStatus.STALEMATE;
    }
    if (board.isFivefoldRepetition()) {
      return GameStatus.FIVE_FOLD_REPETITION_RULE;
    }
    if (board.isSeventyFiftyMove()) {
      return GameStatus.SEVENTY_FIVE_MOVE_RULE;
    }
    if (board.isInsufficientMaterial()) {
      return GameStatus.INSUFFICIENT_MATERIAL_BOTH;
    }
    if (board.isInsufficientMaterial(board.getHavingMove().getOppositeSide())) {
      return GameStatus.INSUFFICIENT_MATERIAL_MADE_THE_MOVE_ONLY;
    }
    if (board.isInsufficientMaterial(board.getHavingMove())) {
      return GameStatus.INSUFFICIENT_MATERIAL_NOT_MADE_THE_MOVE_ONLY;
    }
    return GameStatus.OTHER;
  }

  public static boolean calculateIsResetHalfMoveClock(LegalMove legalMove) {
    return legalMove.movingPiece() != Piece.NONE && legalMove.movingPiece().getPieceType() == PieceType.PAWN
        || legalMove.pieceCaptured() != Piece.NONE;
  }
}
