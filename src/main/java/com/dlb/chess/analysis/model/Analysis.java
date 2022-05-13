package com.dlb.chess.analysis.model;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.analysis.enums.CheckmateOrStalemate;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public record Analysis(Side havingMove, List<HalfMove> halfMoveList, List<List<HalfMove>> repetitionListList,
    List<List<HalfMove>> repetitionListListInitialEnPassantCapture, List<RepeatingSequence> sequenceRepetitionList,
    List<List<YawnHalfMove>> yawnMoveListList, boolean hasThreefoldRepetition,
    boolean hasThreefoldRepetitionInitialEnPassantCapture, boolean hasFivefoldRepetition, boolean hasFiftyMoveRule,
    boolean hasSeventyFiveMoveRule, boolean hasThreeSequenceRepetition, boolean isGameContinuedOverFivefoldRepetition,
    boolean isGameContinuedOverSeventyFiveMove, int firstCapture, boolean hasCapture, int maxYawnSequence,
    CheckmateOrStalemate lastPositionEvaluation, InsufficientMaterial insufficientMaterial,
    UnwinnableQuick unwinnableQuickResultNotHavingMove, String fen, ApiBoard board) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (Analysis) obj;
    return Objects.equals(board, other.board) && Objects.equals(fen, other.fen)
        && Objects.equals(yawnMoveListList, other.yawnMoveListList) && firstCapture == other.firstCapture
        && Objects.equals(halfMoveList, other.halfMoveList) && hasCapture == other.hasCapture
        && hasFiftyMoveRule == other.hasFiftyMoveRule && hasFivefoldRepetition == other.hasFivefoldRepetition
        && hasSeventyFiveMoveRule == other.hasSeventyFiveMoveRule
        && hasThreeSequenceRepetition == other.hasThreeSequenceRepetition
        && hasThreefoldRepetition == other.hasThreefoldRepetition
        && hasThreefoldRepetitionInitialEnPassantCapture == other.hasThreefoldRepetitionInitialEnPassantCapture
        && havingMove == other.havingMove && insufficientMaterial == other.insufficientMaterial
        && isGameContinuedOverFivefoldRepetition == other.isGameContinuedOverFivefoldRepetition
        && isGameContinuedOverSeventyFiveMove == other.isGameContinuedOverSeventyFiveMove
        && lastPositionEvaluation == other.lastPositionEvaluation && maxYawnSequence == other.maxYawnSequence
        && Objects.equals(repetitionListList, other.repetitionListList)
        && Objects.equals(repetitionListListInitialEnPassantCapture, other.repetitionListListInitialEnPassantCapture)
        && Objects.equals(sequenceRepetitionList, other.sequenceRepetitionList)
        && unwinnableQuickResultNotHavingMove == other.unwinnableQuickResultNotHavingMove;
  }

  public Side havingMove() {
    return havingMove;
  }

  public List<HalfMove> halfMoveList() {
    return halfMoveList;
  }

  public List<List<HalfMove>> repetitionListList() {
    return repetitionListList;
  }

  public List<List<HalfMove>> repetitionListListInitialEnPassantCapture() {
    return repetitionListListInitialEnPassantCapture;
  }

  public List<RepeatingSequence> sequenceRepetitionList() {
    return sequenceRepetitionList;
  }

  public List<List<YawnHalfMove>> yawnMoveListList() {
    return yawnMoveListList;
  }

  public boolean hasThreefoldRepetition() {
    return hasThreefoldRepetition;
  }

  public boolean hasThreefoldRepetitionInitialEnPassantCapture() {
    return hasThreefoldRepetitionInitialEnPassantCapture;
  }

  public boolean hasFivefoldRepetition() {
    return hasFivefoldRepetition;
  }

  public boolean hasFiftyMoveRule() {
    return hasFiftyMoveRule;
  }

  public boolean hasSeventyFiveMoveRule() {
    return hasSeventyFiveMoveRule;
  }

  public boolean hasThreeSequenceRepetition() {
    return hasThreeSequenceRepetition;
  }

  public boolean isGameContinuedOverFivefoldRepetition() {
    return isGameContinuedOverFivefoldRepetition;
  }

  public boolean isGameContinuedOverSeventyFiveMove() {
    return isGameContinuedOverSeventyFiveMove;
  }

  public int firstCapture() {
    return firstCapture;
  }

  public boolean hasCapture() {
    return hasCapture;
  }

  public int maxYawnSequence() {
    return maxYawnSequence;
  }

  public CheckmateOrStalemate lastPositionEvaluation() {
    return lastPositionEvaluation;
  }

  public InsufficientMaterial insufficientMaterial() {
    return insufficientMaterial;
  }

  public UnwinnableQuick unwinnableQuickResultNotHavingMove() {
    return unwinnableQuickResultNotHavingMove;
  }

  public String fen() {
    return fen;
  }

  public ApiBoard board() {
    return board;
  }

}
