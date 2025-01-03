package com.dlb.chess.test.pgntest.enums;

import java.nio.file.Path;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.test.pgntest.PgnTestConstants;

public enum PgnTest {

  BASIC_MOVING_PIECE_WHITE(true, "basic/movingPiece/white"),
  BASIC_MOVING_PIECE_BLACK(true, "basic/movingPiece/black"),
  BASIC_CAPTURE_WHITE(true, "basic/capture/white"),
  BASIC_CAPTURE_BLACK(true, "basic/capture/black"),
  BASIC_EN_PASSANT_CAPTURE_WHITE(true, "basic/enPassantCapture/white"),
  BASIC_EN_PASSANT_CAPTURE_BLACK(true, "basic/enPassantCapture/black"),
  BASIC_PROMOTION_PIECE_WHITE(true, "basic/promotionPiece/white"),
  BASIC_PROMOTION_PIECE_BLACK(true, "basic/promotionPiece/black"),
  BASIC_PROMOTION_SQUARE_WHITE(true, "basic/promotionSquare/white"),
  BASIC_PROMOTION_SQUARE_BLACK(true, "basic/promotionSquare/black"),
  BASIC_CHECK_WHITE(true, "basic/check/white"),
  BASIC_CHECK_BLACK(true, "basic/check/black"),
  BASIC_DOUBLE_CHECK_WHITE(true, "basic/doubleCheck/white"),
  BASIC_DOUBLE_CHECK_BLACK(true, "basic/doubleCheck/black"),
  BASIC_CHECKMATE_WHITE(true, "basic/checkmate/white"),
  BASIC_CHECKMATE_BLACK(true, "basic/checkmate/black"),
  BASIC_CHECKMATE_VARIOUS_WHITE(false, "basic/checkmateVarious/white"),
  BASIC_CHECKMATE_VARIOUS_BLACK(false, "basic/checkmateVarious/black"),
  BASIC_CHECKMATE_DOUBLE_CHECK_WHITE(true, "basic/doubleCheckCheckmate/white"),
  BASIC_CHECKMATE_DOUBLE_CHECK_BLACK(true, "basic/doubleCheckCheckmate/black"),
  BASIC_STALEMATE(true, "basic/stalemate"),
  BASIC_INSUFFICIENT_MATERIAL(true, "basic/insufficientMaterial"),
  BASIC_THREEFOLD(true, "basic/threefold"),
  BASIC_THREEFOLD_INITIAL_EP(true, "basic/threefoldInitialEnPassantCapture"),
  BASIC_FIFTY(true, "basic/fifty"),
  BASIC_FIVEFOLD(true, "basic/fivefold"),
  BASIC_SEVENTY_FIVE(true, "basic/seventyFive"),
  BASIC_INTERVENING(true, "basic/intervening"),
  BASIC_DOUBLE_DRAW(true, "basic/doubleDraw"),
  BASIC_CASTLING_SPECIAL_WHITE(true, "basic/castlingSpecial/white"),
  BASIC_CASTLING_SPECIAL_BLACK(true, "basic/castlingSpecial/black"),
  BASIC_FORCED(true, "basic/forced"),
  BASIC_FROM_FEN(true, "basic/fromFen"),
  BASIC_FROM_FEN_YAWN_WHITE(true, "basic/fromFenYawn/white"),
  BASIC_FROM_FEN_YAWN_BLACK(true, "basic/fromFenYawn/black"),

  CAPTURE_AND_MAX_YAWN(false, "captureLastMove"),

  // skipped when testing against scalachess for cannot manage so long games in
  // testing approach
  LONG(false, "long"),
  LONGEST_MATE(false, "longestMate"),
  LONGEST_POSSIBLE(false, "longestPossible"),

  // skipped when testing against scalachess for cannot manage so long games in
  // testing approach
  RANDOM_NO_REPETITION(false, "random/noRepetition"),

  RANDOM_CHECKMATE(false, "random/checkmate"),
  RANDOM_STALEMATE(false, "random/stalemate"),
  RANDOM_INSUFFICIENT_MATERIAL(false, "random/insufficientMaterial"),
  RANDOM_FIFTY(false, "random/fifty"),
  RANDOM_SEVENTY_FIVE(false, "random/seventyFive"),
  RANDOM_THREEFOLD(false, "random/threefold"),
  RANDOM_FIVEFOLD(false, "random/fivefold"),

  VARIOUS(false, "various"),
  WCC2021(false, "wcc2021"),
  FIVEFOLD_CORRECT(false, "fivefold/correct"),
  FIVEFOLD_BEYOND(false, "fivefold/beyond"),
  FIFTY_GENERAL(false, "fifty/general"),
  FIFTY_PATTERN(false, "fifty/pattern"),

  SEVENTY_FIVE_CORRECT(false, "seventyFive/correct"),
  SEVENTY_FIVE_BEYOND(false, "seventyFive/beyond"),
  EARLY_DRAW(false, "earlyDraw"),
  WIKIPEDIA_THREEFOLD(false, "wikipedia/threefold"),
  WIKIPEDIA_FIFTY_MOVE(false, "wikipedia/fiftyMove"),
  SEQUENCE(false, "sequence"),
  SPECIAL(false, "special"),
  DGT_LIVE_CHESS(false, "dgt/liveChess"),
  DGT_CENTAUR(false, "dgt/centaur"),

  UNFAIR_LICHESS_EXAMPLES(false, "unfair/lichess/examples"),
  UNFAIR_LICHESS_HELPMATE(false, "unfair/lichess/helpmate"),
  UNFAIR_DEPTH_THREE(false, "unfair/depthThree"),
  UNFAIR_NOT_QUICK(false, "unfair/notQuick"),
  UNFAIR_AMBRONA(false, "unfair/ambrona"),

  PAWN_WALL(false, "pawnWall"),

  LAST_MOVE_ADDED_ACCIDENTALLY(false, "lastMoveAddedAccidentally"),

  MAX_SAME_PIECE_PROMOTION_WHITE(false, "maxPiece/white"),
  MAX_SAME_PIECE_PROMOTION_BLACK(false, "maxPiece/black"),
  MAX_SAME_PIECE_PROMOTION_COMBINED(false, "maxPiece/combined"),

  DOUBLE_CHECK_CHECKMATE_BIZARRE_CHECKMATE_WHITE(false, "doubleCheckCheckmateBizarre/white"),
  DOUBLE_CHECK_CHECKMATE_BIZARRE_CHECKMATE_BLACK(false, "doubleCheckCheckmateBizarre/black"),

  MONSTER_BLOG_INSTANT(false, "blog/instant"),
  MONSTER_BLOG_PREDRAW(false, "blog/predraw"),
  MONSTER_BLOG_TIMEOUT(false, "blog/timeout"),

  REPETITION_QUIZ_ONE(false, "repetitionQuiz/one"),
  REPETITION_QUIZ_TWO(false, "repetitionQuiz/two");

  private final boolean isBasicTest;
  private final String folderPart;

  PgnTest(boolean isBasicTest, String folderPart) {
    this.isBasicTest = isBasicTest;
    this.folderPart = folderPart;
  }

  public Path getFolderPath() {
    return NonNullWrapperCommon.resolve(PgnTestConstants.PGN_TEST_ROOT_FOLDER_PATH, folderPart);
  }

  public boolean getIsBasicTest() {
    return isBasicTest;
  }

  public static boolean existsFolderPath(Path folderPath) {
    for (final PgnTest pgnTest : PgnTest.values()) {
      if (pgnTest.getFolderPath().equals(folderPath)) {
        return true;
      }
    }
    return false;
  }

}
