package com.dlb.chess.test.model;

import com.dlb.chess.analysis.enums.CheckmateOrStalemate;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public record PgnFileTestCase(String pgnFileName, String expectedRepetition,
    String expectedRepetitionInitialEnPassantCapture, String expectedYawnMoveRule, int firstCapture,
    int maxYawnSequence, CheckmateOrStalemate checkmateOrStalemate, int repetitionCountFinalPosition,
    InsufficientMaterial insufficientMaterial, UnwinnableFull unwinnableFullWhite, UnwinnableFull unwinnableFullBlack,
    UnwinnableQuick unwinnableQuickWhite, UnwinnableQuick unwinnableQuickBlack, String fen) {

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

  public UnwinnableFull unwinnableFullWhite() {
    return unwinnableFullWhite;
  }

  public UnwinnableFull unwinnableFullBlack() {
    return unwinnableFullBlack;
  }

  public String fen() {
    return fen;
  }

  public int repetitionCountFinalPosition() {
    return repetitionCountFinalPosition;
  }

  public UnwinnableQuick unwinnableQuickWhite() {
    return unwinnableQuickWhite;
  }

  public UnwinnableQuick unwinnableQuickBlack() {
    return unwinnableQuickBlack;
  }

}
