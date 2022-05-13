package com.dlb.chess.test.pgntest.enums;

import com.dlb.chess.test.pgntest.PgnTestConstants;

public enum PgnTest {

  BASIC_MOVING_PIECE_WHITE(true, "basic\\movingPiece\\white"),
  BASIC_MOVING_PIECE_BLACK(true, "basic\\movingPiece\\black"),
  BASIC_CAPTURE_WHITE(true, "basic\\capture\\white"),
  BASIC_CAPTURE_BLACK(true, "basic\\capture\\black"),
  BASIC_EN_PASSANT_CAPTURE_WHITE(true, "basic\\enPassantCapture\\white"),
  BASIC_EN_PASSANT_CAPTURE_BLACK(true, "basic\\enPassantCapture\\black"),
  BASIC_PROMOTION_PIECE_WHITE(true, "basic\\promotionPiece\\white"),
  BASIC_PROMOTION_PIECE_BLACK(true, "basic\\promotionPiece\\black"),
  BASIC_PROMOTION_SQUARE_WHITE(true, "basic\\promotionSquare\\white"),
  BASIC_PROMOTION_SQUARE_BLACK(true, "basic\\promotionSquare\\black"),
  BASIC_CHECK_WHITE(true, "basic\\check\\white"),
  BASIC_CHECK_BLACK(true, "basic\\check\\black"),
  BASIC_DOUBLE_CHECK_WHITE(true, "basic\\doubleCheck\\white"),
  BASIC_DOUBLE_CHECK_BLACK(true, "basic\\doubleCheck\\black"),
  BASIC_CHECKMATE_WHITE(true, "basic\\checkmate\\white"),
  BASIC_CHECKMATE_BLACK(true, "basic\\checkmate\\black"),
  BASIC_CHECKMATE_VARIOUS_WHITE(false, "basic\\checkmateVarious\\white"),
  BASIC_CHECKMATE_VARIOUS_BLACK(false, "basic\\checkmateVarious\\black"),
  BASIC_CHECKMATE_DOUBLE_CHECK_WHITE(true, "basic\\checkmateDoubleCheck\\white"),
  BASIC_CHECKMATE_DOUBLE_CHECK_BLACK(true, "basic\\checkmateDoubleCheck\\black"),
  BASIC_STALEMATE(true, "basic\\stalemate"),
  BASIC_INSUFFICIENT_MATERIAL(true, "basic\\insufficientMaterial"),
  BASIC_THREEFOLD(true, "basic\\threefold"),
  BASIC_THREEFOLD_INITIAL_EP(true, "basic\\threefoldInitialEnPassantCapture"),
  BASIC_FIFTY(true, "basic\\fifty"),
  BASIC_FIVEFOLD(true, "basic\\fivefold"),
  BASIC_SEVENTY_FIVE(true, "basic\\seventyFive"),
  BASIC_INTERVENING(true, "basic\\intervening"),
  BASIC_DOUBLE_DRAW(true, "basic\\doubleDraw"),
  BASIC_CASTLING_SPECIAL_WHITE(true, "basic\\castlingSpecial\\white"),
  BASIC_CASTLING_SPECIAL_BLACK(true, "basic\\castlingSpecial\\black"),
  BASIC_FORCED(true, "basic\\forced"),
  BASIC_FROM_FEN(true, "basic\\fromFen"),
  BASIC_FROM_FEN_YAWN_WHITE(true, "basic\\fromFenYawn\\white"),
  BASIC_FROM_FEN_YAWN_BLACK(true, "basic\\fromFenYawn\\black"),

  CAPTURE_AND_MAX_YAWN(false, "captureAndMaxYawn"),

  // skipped when testing against scalachess for cannot manage so long games in
  // testing approach
  LONG(false, "long"),
  LONGEST_MATE(false, "longestMate"),
  LONGEST_POSSIBLE(false, "longestPossible"),

  // skipped when testing against scalachess for cannot manage so long games in
  // testing approach
  RANDOM_NO_REPETITION(false, "random\\noRepetition"),

  RANDOM_CHECKMATE(false, "random\\checkmate"),
  RANDOM_STALEMATE(false, "random\\stalemate"),
  RANDOM_INSUFFICIENT_MATERIAL(false, "random\\insufficientMaterial"),
  RANDOM_FIFTY(false, "random\\fifty"),
  RANDOM_SEVENTY_FIVE(false, "random\\seventyFive"),
  RANDOM_THREEFOLD(false, "random\\threefold"),
  RANDOM_FIVEFOLD(false, "random\\fivefold"),

  GAMES_VARIOUS(false, "games\\various"),
  GAMES_WCC2021(false, "games\\wcc2021"),
  FIVEFOLD_CORRECT(false, "fivefold\\correct"),
  FIVEFOLD_BEYOND(false, "fivefold\\beyond"),
  FIFTY_GENERAL(false, "fifty\\general"),
  FIFTY_PATTERN(false, "fifty\\pattern"),

  SEVENTY_FIVE_CORRECT(false, "seventyFive\\correct"),
  SEVENTY_FIVE_BEYOND(false, "seventyFive\\beyond"),
  EARLY_DRAW(false, "earlyDraw"),
  WIKIPEDIA_THREEFOLD(false, "wikipedia\\threefold"),
  WIKIPEDIA_FIFTY_MOVE(false, "wikipedia\\fiftyMove"),
  SEQUENCE(false, "sequence"),
  SPECIAL(false, "special"),
  DGT_LIVE_CHESS(false, "dgt\\liveChess"),
  DGT_CENTAUR(false, "dgt\\centaur"),

  UNFAIR_LICHESS_ANALYSIS_GAMES(false, "unfair\\lichessAnalysis\\games"),
  UNFAIR_LICHESS_ANALYSIS_HELPMATE(false, "unfair\\\\lichessAnalysis\\helpmate"),
  UNFAIR_HALF_MOVE_DEPTH_THREE(false, "unfair\\halfMoveDepthThree"),
  UNFAIR_NOT_QUICK(false, "unfair\\notQuick"),
  UNFAIR_AMBRONA_EXAMPLES(false, "unfair\\ambronaExamples"),

  PAWN_WALL(false, "pawnWall"),

  LAST_MOVE_ADDED_ACCIDENTALLY(false, "lastMoveAddedAccidentally"),

  MAX_SAME_PIECE_PROMOTION_WHITE(false, "maxSamePiecePromotion\\white"),
  MAX_SAME_PIECE_PROMOTION_BLACK(false, "maxSamePiecePromotion\\black"),
  MAX_SAME_PIECE_PROMOTION_COMBINED(false, "maxSamePiecePromotion\\combined"),

  BIZARRE_CHECKMATE(false, "bizarreCheckmate");

  private final boolean isBasicTest;
  private final String folderPart;

  PgnTest(boolean isBasicTest, String folderPart) {
    this.isBasicTest = isBasicTest;
    this.folderPart = folderPart;
  }

  public String getFolderPath() {
    return PgnTestConstants.PGN_TEST_ROOT_FOLDER_PATH + "\\" + folderPart;
  }

  public boolean getIsBasicTest() {
    return isBasicTest;
  }

  public static boolean existsFolderPath(String folderPath) {
    for (final PgnTest pgnTest : PgnTest.values()) {
      if (pgnTest.getFolderPath().equals(folderPath)) {
        return true;
      }
    }
    return false;
  }

}
