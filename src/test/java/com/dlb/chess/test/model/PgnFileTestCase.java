package com.dlb.chess.test.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.analysis.enums.CheckmateOrStalemate;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.test.pgntest.enums.UnwinnableFullResultTest;

public record PgnFileTestCase(String pgnFileName, String expectedRepetition, String expectedRepetitionInitialEnPassantCapture, String expectedYawnMoveRule, int firstCapture, int maxYawnSequence, CheckmateOrStalemate checkmateOrStalemate, int repetitionCountFinalPosition, InsufficientMaterial insufficientMaterial, UnwinnableFullResultTest unwinnableHavingMove, UnwinnableFullResultTest unwinnableNotHavingMove, String fen) {

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

  public UnwinnableFullResultTest unwinnableHavingMove() {
    return unwinnableHavingMove;
  }

  public UnwinnableFullResultTest unwinnableNotHavingMove() {
    return unwinnableNotHavingMove;
  }

  public String fen() {
    return fen;
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
    return Objects.equals(expectedYawnMoveRule, other.expectedYawnMoveRule) && Objects.equals(expectedRepetition, other.expectedRepetition) && Objects.equals(expectedRepetitionInitialEnPassantCapture, other.expectedRepetitionInitialEnPassantCapture) && Objects.equals(fen, other.fen) && firstCapture == other.firstCapture && insufficientMaterial == other.insufficientMaterial && checkmateOrStalemate == other.checkmateOrStalemate && maxYawnSequence == other.maxYawnSequence && Objects.equals(pgnFileName, other.pgnFileName) && unwinnableHavingMove == other.unwinnableHavingMove && unwinnableNotHavingMove == other.unwinnableNotHavingMove;
  }

}
