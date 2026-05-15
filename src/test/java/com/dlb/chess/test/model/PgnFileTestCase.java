package com.dlb.chess.test.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.report.CheckmateOrStalemate;
import com.dlb.chess.unwinnability.UnwinnabilityFullVerdict;
import com.dlb.chess.unwinnability.UnwinnabilityQuickVerdict;

public record PgnFileTestCase(String pgnFileName, String expectedRepetition, String expectedNoProgressMoveRule,
    int firstCapture, int maxNoProgressSequence, CheckmateOrStalemate checkmateOrStalemate,
    int repetitionCountFinalPosition, InsufficientMaterial insufficientMaterial, UnwinnabilityFullVerdict unwinnableFullWhite,
    UnwinnabilityFullVerdict unwinnableFullBlack, UnwinnabilityQuickVerdict unwinnableQuickWhite, UnwinnabilityQuickVerdict unwinnableQuickBlack,
    String fen) {

  @SuppressWarnings("null")
  @Override
  public @NonNull UnwinnabilityQuickVerdict unwinnableQuickWhite() {
    return unwinnableQuickWhite;
  }

  @SuppressWarnings("null")
  @Override
  public @NonNull UnwinnabilityQuickVerdict unwinnableQuickBlack() {
    return unwinnableQuickBlack;
  }

  @SuppressWarnings("null")
  @Override
  public @NonNull String fen() {
    return fen;
  }

}
