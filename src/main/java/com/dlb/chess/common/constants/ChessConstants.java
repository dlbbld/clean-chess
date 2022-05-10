package com.dlb.chess.common.constants;

import com.dlb.chess.common.NonNullWrapperCommon;

public class ChessConstants {
  public static final int FIFTY_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD = 100;
  public static final int SEVENTY_FIVE_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD = 150;

  public static final int THREEFOLD_REPETITION_RULE_THRESHOLD = 3;
  public static final int FIVEFOLD_REPETITION_RULE_THRESHOLD = 5;
  public static final String PGN_EXTENSION = "pgn";
  public static final String PGN_FILE_EXTENSION_INCLUDING_DOT = "." + PGN_EXTENSION;

  public static final int INITIAL_NUMBER_OF_ROOKS = 2;
  public static final int INITIAL_NUMBER_OF_KNIGHTS = 2;
  public static final int INITIAL_NUMBER_OF_LIGHT_SQUARE_BISHOPS = 1;
  public static final int INITIAL_NUMBER_OF_DARK_SQUARE_BISHOPS = 1;
  public static final int INITIAL_NUMBER_OF_QUEENS = 1;
  public static final int NUMBER_OF_KINGS = 1;
  public static final int INITIAL_NUMBER_OF_PAWNS = 8;

  public static final String ROOK_LETTER = "R";
  public static final String KNIGHT_LETTER = "N";
  public static final String BISHOP_LETTER = "B";
  public static final String QUEEN_LETTER = "Q";
  public static final String KING_LETTER = "K";
  public static final String PAWN_LETTER = "P";

  public static final String ROOK_LETTER_LOWER_CASE = NonNullWrapperCommon.toLowerCase(ROOK_LETTER);
  public static final String KNIGHT_LETTER_LOWER_CASE = NonNullWrapperCommon.toLowerCase(KNIGHT_LETTER);
  public static final String BISHOP_LETTER_LOWER_CASE = NonNullWrapperCommon.toLowerCase(BISHOP_LETTER);
  public static final String QUEEN_LETTER_LOWER_CASE = NonNullWrapperCommon.toLowerCase(QUEEN_LETTER);
  public static final String KING_LETTER_LOWER_CASE = NonNullWrapperCommon.toLowerCase(KING_LETTER);
  public static final String PAWN_LETTER_LOWER_CASE = NonNullWrapperCommon.toLowerCase(PAWN_LETTER);

  public static final String FILE_A_LETTER = "a";
  public static final String FILE_B_LETTER = "b";
  public static final String FILE_C_LETTER = "c";
  public static final String FILE_D_LETTER = "d";
  public static final String FILE_E_LETTER = "e";
  public static final String FILE_F_LETTER = "f";
  public static final String FILE_G_LETTER = "g";
  public static final String FILE_H_LETTER = "h";

  public static final int RANK_1_NUMBER = 1;
  public static final int RANK_2_NUMBER = 2;
  public static final int RANK_3_NUMBER = 3;
  public static final int RANK_4_NUMBER = 4;
  public static final int RANK_5_NUMBER = 5;
  public static final int RANK_6_NUMBER = 6;
  public static final int RANK_7_NUMBER = 7;
  public static final int RANK_8_NUMBER = 8;

}
