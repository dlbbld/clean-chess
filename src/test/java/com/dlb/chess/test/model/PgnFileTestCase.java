package com.dlb.chess.test.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.analysis.enums.CheckmateOrStalemate;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public record PgnFileTestCase(String pgnFileName, String expectedRepetition,
    String expectedRepetitionInitialEnPassantCapture, String expectedYawnMoveRule, int firstCapture,
    int maxYawnSequence, CheckmateOrStalemate checkmateOrStalemate, int repetitionCountFinalPosition,
    InsufficientMaterial insufficientMaterial, UnwinnableFull unwinnableFullHavingMove,
    UnwinnableFull unwinnableFullNotHavingMove, UnwinnableQuick unwinnableQuickHavingMove,
    UnwinnableQuick unwinnableQuickNotHavingMove, String fen) {

  public String pgnFileName() {
    return pgnFileName;
  }

  public String expectedRepetition() {
    return expectedRepetition;
  }

  public String expectedRepetitionInitialEnPassantCapture() {
    return expectedRepetitionInitialEnPassantCapture;
  }

  public String expectedYawnMoveRule() {
    return expectedYawnMoveRule;
  }

  public int firstCapture() {
    return firstCapture;
  }

  public int maxYawnSequence() {
    return maxYawnSequence;
  }

  public CheckmateOrStalemate checkmateOrStalemate() {
    return checkmateOrStalemate;
  }

  public InsufficientMaterial insufficientMaterial() {
    return insufficientMaterial;
  }

  public UnwinnableFull unwinnableFullHavingMove() {
    return unwinnableFullHavingMove;
  }

  public UnwinnableFull unwinnableFullNotHavingMove() {
    return unwinnableFullNotHavingMove;
  }

  public String fen() {
    return fen;
  }

  public int repetitionCountFinalPosition() {
    return repetitionCountFinalPosition;
  }

  public UnwinnableQuick unwinnableQuickHavingMove() {
    return unwinnableQuickHavingMove;
  }

  public UnwinnableQuick unwinnableQuickNotHavingMove() {
    return unwinnableQuickNotHavingMove;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (PgnFileTestCase) obj;
    return Objects.equals(expectedYawnMoveRule, other.expectedYawnMoveRule)
        && Objects.equals(expectedRepetition, other.expectedRepetition)
        && Objects.equals(expectedRepetitionInitialEnPassantCapture, other.expectedRepetitionInitialEnPassantCapture)
        && Objects.equals(fen, other.fen) && firstCapture == other.firstCapture
        && insufficientMaterial == other.insufficientMaterial && checkmateOrStalemate == other.checkmateOrStalemate
        && maxYawnSequence == other.maxYawnSequence && Objects.equals(pgnFileName, other.pgnFileName)
        && unwinnableFullHavingMove == other.unwinnableFullHavingMove
        && unwinnableFullNotHavingMove == other.unwinnableFullNotHavingMove
        && unwinnableQuickHavingMove == other.unwinnableQuickHavingMove
        && unwinnableQuickNotHavingMove == other.unwinnableQuickNotHavingMove;
  }

}
