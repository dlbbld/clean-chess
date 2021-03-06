package com.dlb.chess.common.enums;

public enum GameStatusAnalysis {

  WHITE_DELIVERS_CHECKMATE,
  BLACK_DELIVERS_CHECKMATE,
  STALEMATE,
  INSUFFICIENT_MATERIAL_BOTH,
  FIVE_FOLD_REPETITION_RULE,
  SEVENTY_FIVE_MOVE_RULE,
  INSUFFICIENT_MATERIAL_WHITE_ONLY,
  INSUFFICIENT_MATERIAL_BLACK_ONLY,
  OTHER
}
