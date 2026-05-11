package com.dlb.chess.board;

import java.util.List;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.board.Board;
import com.dlb.chess.common.model.DynamicPosition;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.ucimove.utility.enums.AddSpace;
import com.dlb.chess.common.utility.RepetitionUtility;

public abstract class HalfMoveUtility {
  public static HalfMove calculateHalfMove(MoveSpecification moveSpecification, ChessBoard board) {
    final String san = board.getSan();
    return calculateHalfMoveInternal(moveSpecification, board, san);
  }

  private static HalfMove calculateHalfMoveInternal(MoveSpecification moveSpecification, ChessBoard board, String san) {

    final var halfMoveCount = board.getPerformedHalfMoveCount();
    final var index = halfMoveCount - 1;

    final var halfMoveClock = board.getHalfMoveClock();
    final var fullMoveNumber = board.getFullMoveNumber();
    final String fen = board.getFen();
    final var isCapture = board.isCapture();

    final var countRepetition = board.getRepetitionCount();

    final List<DynamicPosition> dynamicPositionList = board.getDynamicPositionList();
    final DynamicPosition dynamicPosition = board.getDynamicPosition();
    final var countRepetitionIgnoringEnPassantCapture = RepetitionUtility.calculateCountRepetition(
        board.getPerformedLegalMoveList(), dynamicPositionList, dynamicPosition,
        EnPassantCaptureRuleThreefold.DO_IGNORE);

    final Piece movingPiece = board.getMovingPiece();

    return new HalfMove(index, halfMoveCount, fullMoveNumber, halfMoveClock, isCapture, fen, dynamicPosition,
        countRepetition, countRepetitionIgnoringEnPassantCapture, san, movingPiece, moveSpecification);
  }

  public static String calculateMoveNumberAndSanWithSpace(int fullMoveNumber, Side havingMove, String san) {
    return calculateMoveNumberAndSan(fullMoveNumber, havingMove, san, AddSpace.YES);
  }

  public static String calculateMoveNumberAndSanWithoutSpace(HalfMove halfMove) {
    return calculateMoveNumberAndSan(halfMove, AddSpace.NO);
  }

  private static String calculateMoveNumberAndSan(HalfMove halfMove, AddSpace addSpace) {
    return calculateFullMoveNumberInitial(halfMove.fullMoveNumber(), halfMove.havingMove(), addSpace) + halfMove.san();
  }

  private static String calculateMoveNumberAndSan(int fullMoveNumber, Side havingMove, String san, AddSpace addSpace) {
    return calculateFullMoveNumberInitial(fullMoveNumber, havingMove, addSpace) + san;
  }

  public static String calculateFullMoveNumberInitialWithSpace(int initialFullMoveNumber, Side havingMove) {
    return calculateFullMoveNumberInitial(initialFullMoveNumber, havingMove, AddSpace.YES);
  }

  public static String calculateFullMoveNumberInitialWithoutSpace(int initialFullMoveNumber, Side havingMove) {
    return calculateFullMoveNumberInitial(initialFullMoveNumber, havingMove, AddSpace.NO);
  }

  private static String calculateFullMoveNumberInitial(int initialFullMoveNumber, Side havingMove, AddSpace addSpace) {

    return switch (havingMove) {
      case WHITE -> initialFullMoveNumber + "." + addSpace.getValue();
      case BLACK -> initialFullMoveNumber + "..." + addSpace.getValue();
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

}
