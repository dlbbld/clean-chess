package com.dlb.chess.san.enums;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ChessConstants;

public enum SanLetter {
  CAPTURE("x"),
  PROMOTION("="),
  CHECK("+"),
  CHECKMATE("#"),
  CASTLING_O("O"),
  CASTLING_HYPHEN("-"),
  ROOK(ChessConstants.ROOK_LETTER),
  KNIGHT(ChessConstants.KNIGHT_LETTER),
  BISHOP(ChessConstants.BISHOP_LETTER),
  QUEEN(ChessConstants.QUEEN_LETTER),
  KING(ChessConstants.KING_LETTER),
  FILE_A(ChessConstants.FILE_A_LETTER),
  FILE_B(ChessConstants.FILE_B_LETTER),
  FILE_C(ChessConstants.FILE_C_LETTER),
  FILE_D(ChessConstants.FILE_D_LETTER),
  FILE_E(ChessConstants.FILE_E_LETTER),
  FILE_F(ChessConstants.FILE_F_LETTER),
  FILE_G(ChessConstants.FILE_G_LETTER),
  FILE_H(ChessConstants.FILE_H_LETTER),
  RANK_1_NUMBER(NonNullWrapperCommon.valueOf(ChessConstants.RANK_1_NUMBER)),
  RANK_2_NUMBER(NonNullWrapperCommon.valueOf(ChessConstants.RANK_2_NUMBER)),
  RANK_3_NUMBER(NonNullWrapperCommon.valueOf(ChessConstants.RANK_3_NUMBER)),
  RANK_4_NUMBER(NonNullWrapperCommon.valueOf(ChessConstants.RANK_4_NUMBER)),
  RANK_5_NUMBER(NonNullWrapperCommon.valueOf(ChessConstants.RANK_5_NUMBER)),
  RANK_6_NUMBER(NonNullWrapperCommon.valueOf(ChessConstants.RANK_6_NUMBER)),
  RANK_7_NUMBER(NonNullWrapperCommon.valueOf(ChessConstants.RANK_7_NUMBER)),
  RANK_8_NUMBER(NonNullWrapperCommon.valueOf(ChessConstants.RANK_8_NUMBER));

  private final String letter;

  SanLetter(String letter) {
    this.letter = letter;
  }

  public String getLetter() {
    return letter;
  }

  public static boolean exists(String letter) {
    for (final SanLetter sanLetter : SanLetter.values()) {
      if (sanLetter.getLetter().equals(letter)) {
        return true;
      }
    }
    return false;
  }

}
