package com.dlb.chess.test.model;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.analysis.enums.CheckmateOrStalemate;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.test.pgntest.enums.UnwinnableFullResultTest;

public record PgnFileTestCase(String pgnFileName, String expectedRepetition, String expectedRepetitionInitialEnPassantCapture, String expectedYawnMoveRule, List<String> expectedSequenceRepetition, int firstCapture, int maxYawnSequence, CheckmateOrStalemate lastPositionEvaluation, InsufficientMaterial insufficientMaterial, UnwinnableFullResultTest unwinnableFullResultTest, String fen) {

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

  public List<String> expectedSequenceRepetition() {
    return expectedSequenceRepetition;
  }

  public int firstCapture() {
    return firstCapture;
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

  public UnwinnableFullResultTest unwinnableFullResultTest() {
    return unwinnableFullResultTest;
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
    return Objects.equals(expectedYawnMoveRule, other.expectedYawnMoveRule) && Objects.equals(expectedRepetition, other.expectedRepetition) && Objects.equals(expectedRepetitionInitialEnPassantCapture, other.expectedRepetitionInitialEnPassantCapture) && Objects.equals(expectedSequenceRepetition, other.expectedSequenceRepetition) && Objects.equals(fen, other.fen) && firstCapture == other.firstCapture && insufficientMaterial == other.insufficientMaterial
        && lastPositionEvaluation == other.lastPositionEvaluation && maxYawnSequence == other.maxYawnSequence && Objects.equals(pgnFileName, other.pgnFileName) && unwinnableFullResultTest == other.unwinnableFullResultTest;
  }

}
