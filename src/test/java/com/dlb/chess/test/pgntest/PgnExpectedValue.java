package com.dlb.chess.test.pgntest;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.analysis.enums.CheckmateOrStalemate;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public class PgnExpectedValue {

  private static final Logger logger = NonNullWrapperCommon.getLogger(PgnExpectedValue.class);

  private static PgnFileTestCaseList calculateTestCaseList(PgnTest pgnTest) {

    switch (pgnTest) {
      case BASIC_CASTLING_SPECIAL_WHITE:
        return createTestCasesBasicCastlingSpecialWhite();
      case BASIC_CASTLING_SPECIAL_BLACK:
        return createTestCasesBasicCastlingSpecialBlack();
      case BASIC_MOVING_PIECE_WHITE:
        return createTestCasesBasicMovingPieceWhite();
      case BASIC_MOVING_PIECE_BLACK:
        return createTestCasesBasicMovingPieceBlack();
      case BASIC_CAPTURE_WHITE:
        return createTestCasesBasicCaptureWhite();
      case BASIC_CAPTURE_BLACK:
        return createTestCasesBasicCaptureBlack();
      case BASIC_EN_PASSANT_CAPTURE_WHITE:
        return createTestCasesBasicEnPassantCaptureWhite();
      case BASIC_EN_PASSANT_CAPTURE_BLACK:
        return createTestCasesBasicEnPassantCaptureBlack();
      case BASIC_PROMOTION_PIECE_WHITE:
        return createTestCasesBasicPromotionPieceWhite();
      case BASIC_PROMOTION_PIECE_BLACK:
        return createTestCasesBasicPromotionPieceBlack();
      case BASIC_PROMOTION_SQUARE_WHITE:
        return createTestCasesBasicPromotionSquareWhite();
      case BASIC_PROMOTION_SQUARE_BLACK:
        return createTestCasesBasicPromotionSquareBlack();
      case BASIC_CHECK_WHITE:
        return createTestCasesBasicCheckWhite();
      case BASIC_CHECK_BLACK:
        return createTestCasesBasicCheckBlack();
      case BASIC_DOUBLE_CHECK_WHITE:
        return createTestCasesBasicDoubleCheckWhite();
      case BASIC_DOUBLE_CHECK_BLACK:
        return createTestCasesBasicDoubleCheckBlack();
      case BASIC_CHECKMATE_WHITE:
        return createTestCasesBasicCheckmateWhite();
      case BASIC_CHECKMATE_BLACK:
        return createTestCasesBasicCheckmateBlack();
      case BASIC_CHECKMATE_VARIOUS_WHITE:
        return createTestCasesBasicCheckmateVariousWhite();
      case BASIC_CHECKMATE_VARIOUS_BLACK:
        return createTestCasesBasicCheckmateVariousBlack();
      case BASIC_CHECKMATE_DOUBLE_CHECK_WHITE:
        return createTestCasesBasicCheckmateDoubleCheckWhite();
      case BASIC_CHECKMATE_DOUBLE_CHECK_BLACK:
        return createTestCasesBasicCheckmateDoubleCheckBlack();
      case BASIC_DOUBLE_DRAW:
        return createTestCasesBasicDoubleDraw();
      case BASIC_FIFTY:
        return createTestCasesBasicFifty();
      case BASIC_FIVEFOLD:
        return createTestCasesBasicFivefold();
      case BASIC_FORCED:
        return createTestCasesBasicForced();
      case BASIC_FROM_FEN:
        return createTestCasesBasicFromFen();
      case BASIC_FROM_FEN_YAWN_WHITE:
        return createTestCasesBasicFromFenYawnWhite();
      case BASIC_FROM_FEN_YAWN_BLACK:
        return createTestCasesBasicFromFenYawnBlack();
      case BASIC_INSUFFICIENT_MATERIAL:
        return createTestCasesBasicInsufficientMaterial();
      case BASIC_INTERVENING:
        return createTestCasesBasicIntervening();
      case BASIC_SEVENTY_FIVE:
        return createTestCasesBasicSeventyFive();
      case BASIC_STALEMATE:
        return createTestCasesBasicStalemate();
      case BASIC_THREEFOLD:
        return createTestCasesBasicThreefold();
      case BASIC_THREEFOLD_INITIAL_EP:
        return createTestCasesBasicThreefoldInitialEnPassantCapture();
      case CAPTURE_AND_MAX_YAWN:
        return createTestCasesCapture();
      case DGT_CENTAUR:
        return createTestCasesDgtCentaur();
      case DGT_LIVE_CHESS:
        return createTestCasesDgtLiveChess();
      case EARLY_DRAW:
        return createTestCasesEarlyDraw();
      case FIFTY_GENERAL:
        return createTestCasesFiftyGeneral();
      case FIFTY_PATTERN:
        return createTestCasesFiftyPattern();
      case FIVEFOLD_BEYOND:
        return createTestCasesFivefoldBeyond();
      case FIVEFOLD_CORRECT:
        return createTestCasesFivefoldCorrect();
      case GAMES_VARIOUS:
        return createTestCasesGamesVarious();
      case GAMES_WCC2021:
        return createTestCasesGamesWcc201();
      case LAST_MOVE_ADDED_ACCIDENTALLY:
        return createTestCasesLastMoveAddedAccidentally();
      case UNFAIR_LICHESS_ANALYSIS_GAMES:
        return createTestCasesUnfairLichessAnalysisGames();
      case UNFAIR_LICHESS_ANALYSIS_HELPMATE:
        return createTestCasesUnfairLichessAnalysisHelpmate();
      case UNFAIR_HALF_MOVE_DEPTH_THREE:
        return createTestCasesUnfairHalfMoveDepthThree();
      case UNFAIR_NOT_QUICK:
        return createTestCasesUnfairNotQuick();
      case UNFAIR_AMBRONA_EXAMPLES:
        return createTestCasesUnfairAmbronaExamples();
      case LONG:
        return createTestCasesLong();
      case LONGEST_MATE:
        return createTestCasesLongestMate();
      case LONGEST_POSSIBLE:
        return createTestCasesLongestPossible();
      case MAX_SAME_PIECE_PROMOTION_WHITE:
        return createTestCasesMaxSamePiecePromotionWhite();
      case MAX_SAME_PIECE_PROMOTION_BLACK:
        return createTestCasesMaxSamePiecePromotionBlack();
      case MAX_SAME_PIECE_PROMOTION_COMBINED:
        return createTestCasesMaxSamePiecePromotionByCombined();
      case PAWN_WALL:
        return createTestCasesPawnWall();
      case RANDOM_CHECKMATE:
        return createTestCasesRandomCheckmate();
      case RANDOM_FIFTY:
        return createTestCasesRandomFifty();
      case RANDOM_FIVEFOLD:
        return createTestCasesRandomFivefold();
      case RANDOM_INSUFFICIENT_MATERIAL:
        return createTestCasesRandomInsufficientMaterial();
      case RANDOM_NO_REPETITION:
        return createTestCasesRandomNoRepetition();
      case RANDOM_SEVENTY_FIVE:
        return createTestCasesRandomSeventyFive();
      case RANDOM_STALEMATE:
        return createTestCasesRandomStalemate();
      case RANDOM_THREEFOLD:
        return createTestCasesRandomThreefold();
      case SEQUENCE:
        return createTestCasesSequence();
      case SEVENTY_FIVE_BEYOND:
        return createTestCasesSeventyFiveBeyond();
      case SEVENTY_FIVE_CORRECT:
        return createTestCasesSeventyFiveCorrect();
      case SPECIAL:
        return createTestCasesSpecial();
      case WIKIPEDIA_FIFTY_MOVE:
        return createTestCasesWikipediaFiftyMove();
      case WIKIPEDIA_THREEFOLD:
        return createTestCasesWikipediaThreefold();
      case BIZARRE_CHECKMATE:
        return createTestCasesBizarreCheckmate();
      case MONSTER_BLOG_INSUFFICIENT_MATERIAL:
        return createTestCasesMonsterBlogInsufficientMaterial();
      case MONSTER_BLOG_INSUFFICIENT_MATERIAL_PREDRAW:
        return createTestCasesMonsterBlogInsufficientMaterialPredraw();
      case MONSTER_BLOG_INSUFFICIENT_MATERIAL_TIMEOUT:
        return createTestCasesMonsterBlogInsufficientMaterialTimeout();
      default:
        throw new IllegalArgumentException();
    }
  }

  private static final EnumMap<PgnTest, PgnFileTestCaseList> allTestCaseListMap = NonNullWrapperCommon
      .newEnumMap(PgnTest.class);

  private static final List<PgnFileTestCaseList> allTestCaseListList = new ArrayList<>();

  private static final List<PgnFileTestCaseList> restricedTestCaseListList = new ArrayList<>();

  static {
    logger.info(PgnTestConstants.PGN_TEST_INCLUSION.getMessage());

    for (final PgnTest pgnTest : PgnTest.values()) {
      final PgnFileTestCaseList testCaseList = calculateTestCaseList(pgnTest);
      allTestCaseListMap.put(pgnTest, testCaseList);
      allTestCaseListList.add(testCaseList);

      if (pgnTest == PgnTest.LONGEST_POSSIBLE) {
        switch (PgnTestConstants.PGN_TEST_INCLUSION) {
          case ALL:
          case ONLY_LONGEST_POSSIBLE:
            restricedTestCaseListList.add(testCaseList);
            break;
          case ALL_EXCEPT_LONGEST_POSSIBLE:
            break;
          default:
            throw new IllegalArgumentException();
        }
      } else {
        switch (PgnTestConstants.PGN_TEST_INCLUSION) {
          case ALL:
          case ALL_EXCEPT_LONGEST_POSSIBLE:
            restricedTestCaseListList.add(testCaseList);
            break;
          case ONLY_LONGEST_POSSIBLE:
            break;
          default:
            throw new IllegalArgumentException();
        }
      }
    }

    checkUniqueFileNames(allTestCaseListList);
  }

  public static PgnFileTestCaseList getTestList(PgnTest pgnTest) {
    if (!allTestCaseListMap.containsKey(pgnTest)) {
      throw new ProgrammingMistakeException("The test list was not constructed correctly");
    }

    @SuppressWarnings("null") final PgnFileTestCaseList testCaseList = allTestCaseListMap.get(pgnTest);
    return testCaseList;
  }

  public static List<PgnFileTestCaseList> getTestList(PgnTest... pgnTestArray) {
    final List<PgnFileTestCaseList> resultListList = new ArrayList<>();
    for (final PgnTest pgnTest : pgnTestArray) {
      if (!allTestCaseListMap.containsKey(pgnTest)) {
        throw new ProgrammingMistakeException("The test list was not constructed correctly");
      }
      resultListList.add(allTestCaseListMap.get(pgnTest));
    }
    return resultListList;
  }

  public static PgnTest findPgnFileBelongingPgnTestHavingTestValuesAlready(String testPgnFileName) {
    for (final PgnFileTestCaseList testCaseList : allTestCaseListList) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        if (testCase.pgnFileName().equals(testPgnFileName)) {
          return testCaseList.pgnTest();
        }
      }
    }
    throw new IllegalArgumentException("No such file exists");
  }

  public static PgnTest findPgnFileBelongingPgnTestNotHavingTestValuesAlready(String testPgnFileName) {
    for (final PgnFileTestCaseList testCaseList : allTestCaseListList) {
      if (FileUtility.exists(testCaseList.pgnTest().getFolderPath(), testPgnFileName)) {
        return testCaseList.pgnTest();
      }
    }
    throw new IllegalArgumentException("No such file exists");
  }

  public static PgnFileTestCase findPgnFileBelongingPgnTestCase(String testPgnFileName) {
    for (final PgnFileTestCaseList testCaseList : allTestCaseListList) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        if (testCase.pgnFileName().equals(testPgnFileName)) {
          return testCase;
        }
      }
    }
    throw new IllegalArgumentException("The file " + testPgnFileName + " does not exist");
  }

  public static List<PgnFileTestCaseList> getRestrictedTestListList() {
    return restricedTestCaseListList;
  }

  // We want to have unique file names for the test cases. For the convenience
  // that o can run a test case for testing by
  // only specifying it's name.
  private static void checkUniqueFileNames(List<PgnFileTestCaseList> testCaseListList) {
    final Set<String> fileNames = new TreeSet<>();
    for (final PgnFileTestCaseList testCaseList : testCaseListList) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        final String pgnFileName = testCase.pgnFileName();
        final var isNotContained = !fileNames.add(pgnFileName);
        if (isNotContained) {
          throw new ProgrammingMistakeException("The PGN test cases files names are not unique");
        }
      }
    }
  }

  private static PgnFileTestCaseList createTestCasesBasicMovingPieceWhite() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_white_moving_rook.pgn", "", "", "", -1, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkbnr/1ppppppp/8/p7/8/P7/RPPPPPPP/1NBQKBNR b Kkq - 1 2"));
    list.add(new PgnFileTestCase("02_white_moving_knight.pgn", "", "", "", -1, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkbnr/pppppppp/8/8/8/2N5/PPPPPPPP/R1BQKBNR b KQkq - 1 1"));
    list.add(new PgnFileTestCase("03_white_moving_bishop.pgn", "", "", "", -1, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkbnr/ppp1pppp/8/3p4/8/1P6/PBPPPPPP/RN1QKBNR b KQkq - 1 2"));
    list.add(new PgnFileTestCase("04_white_moving_queen.pgn", "", "", "", -1, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkbnr/ppp1pppp/8/3p4/3P4/3Q4/PPP1PPPP/RNB1KBNR b KQkq - 1 2"));
    list.add(new PgnFileTestCase("05_white_moving_king.pgn", "", "", "", -1, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPPKPPP/RNBQ1BNR b kq - 1 2"));
    list.add(new PgnFileTestCase("06_white_moving_pawn.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"));

    return new PgnFileTestCaseList(PgnTest.BASIC_MOVING_PIECE_WHITE, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicMovingPieceBlack() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_black_moving_rook.pgn", "", "", "", -1, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkbn1/pppppppr/7p/8/3PP3/8/PPP2PPP/RNBQKBNR w KQq - 1 3"));
    list.add(new PgnFileTestCase("02_black_moving_knight.pgn", "", "", "", -1, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkb1r/pppppppp/5n2/8/8/2N5/PPPPPPPP/R1BQKBNR w KQkq - 2 2"));
    list.add(new PgnFileTestCase("03_black_moving_bishop.pgn", "", "", "", -1, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqk1nr/ppppppbp/6p1/8/3PP3/8/PPP2PPP/RNBQKBNR w KQkq - 1 3"));
    list.add(new PgnFileTestCase("04_black_moving_queen.pgn", "", "", "", -1, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnb1kbnr/pppp1ppp/8/4p3/3PP2q/8/PPP2PPP/RNBQKBNR w KQkq - 1 3"));
    list.add(new PgnFileTestCase("05_black_moving_king.pgn", "", "", "", -1, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbq1bnr/ppppkppp/8/4p3/3PP3/8/PPP2PPP/RNBQKBNR w KQ - 1 3"));
    list.add(new PgnFileTestCase("06_black_moving_pawn.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2"));

    return new PgnFileTestCaseList(PgnTest.BASIC_MOVING_PIECE_BLACK, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicCaptureWhite() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_white_rook_rook.pgn", "", "", "", 7, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1nbqkbnr/1ppppppp/1R6/p7/P7/8/1PPPPPPP/1NBQKBNR b Kk - 0 4"));
    list.add(new PgnFileTestCase("02_white_rook_knight.pgn", "", "", "", 7, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bqkb1r/pppppppp/5n2/4R3/P7/8/1PPPPPPP/1NBQKBNR b Kkq - 0 4"));
    list.add(new PgnFileTestCase("03_white_rook_bishop.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqk1nr/pppp1ppp/8/4p3/7P/R7/PPPPPPP1/RNBQKBN1 b Qkq - 0 3"));
    list.add(new PgnFileTestCase("04_white_rook_queen.pgn", "", "", "", 5, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnb1kbnr/pppp1ppp/8/4p2P/7R/8/PPPPPPP1/RNBQKBN1 b Qkq - 0 3"));
    list.add(new PgnFileTestCase("05_white_rook_pawn.pgn", "", "", "", 7, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bqkbnr/2pppppp/2n5/pR6/P7/8/1PPPPPPP/1NBQKBNR b Kkq - 0 4"));
    list.add(new PgnFileTestCase("06_white_knight_rook.pgn", "", "", "", 7, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "N1bqkbnr/p1pppppp/8/1p6/3n4/8/PPPPPPPP/R1BQKBNR b KQk - 0 4"));
    list.add(new PgnFileTestCase("07_white_knight_knight.pgn", "", "", "", 5, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bqkb1r/pppppppp/2n2N2/8/8/8/PPPPPPPP/R1BQKBNR b KQkq - 0 3"));
    list.add(new PgnFileTestCase("08_white_knight_bishop.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqk1nr/pppp1ppp/3Np3/8/8/8/PPPPPPPP/R1BQKBNR b KQkq - 0 3"));
    list.add(new PgnFileTestCase("09_white_knight_queen.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnb1kbnr/pppp1ppp/4pN2/8/8/8/PPPPPPPP/R1BQKBNR b KQkq - 0 3"));
    list.add(new PgnFileTestCase("10_white_knight_pawn.pgn", "", "", "", 3, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkbnr/ppp1pppp/8/3N4/8/8/PPPPPPPP/R1BQKBNR b KQkq - 0 2"));
    list.add(new PgnFileTestCase("11_white_bishop_rook.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bqkbnB/pppppp1p/2n3p1/8/8/1P6/P1PPPPPP/RN1QKBNR b KQq - 0 3"));
    list.add(new PgnFileTestCase("12_white_bishop_knight.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bqkb1r/pppppppp/2n2B2/8/3P4/8/PPP1PPPP/RN1QKBNR b KQkq - 0 3"));
    list.add(new PgnFileTestCase("13_white_bishop_bishop.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqk1nr/ppppppBp/6p1/8/8/1P6/P1PPPPPP/RN1QKBNR b KQkq - 0 3"));
    list.add(new PgnFileTestCase("14_white_bishop_queen.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnb1kbnr/pppp1ppp/4pB2/8/8/1P6/P1PPPPPP/RN1QKBNR b KQkq - 0 3"));
    list.add(new PgnFileTestCase("15_white_bishop_pawn.pgn", "", "", "", 5, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkbnr/pp2Bppp/3p4/2p5/8/3P4/PPP1PPPP/RN1QKBNR b KQkq - 0 3"));
    list.add(new PgnFileTestCase("16_white_queen_rook.pgn", "", "", "", 5, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "Qnbqkbnr/2pppppp/1p6/p7/8/4P3/PPPP1PPP/RNB1KBNR b KQk - 0 3"));
    list.add(new PgnFileTestCase("17_white_queen_knight.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bqkbnr/1ppppppp/2Q5/p7/8/2P5/PP1PPPPP/RNB1KBNR b KQkq - 0 3"));
    list.add(new PgnFileTestCase("18_white_queen_bishop.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rn1qkbnr/ppp1pppp/3p4/8/6Q1/4P3/PPPP1PPP/RNB1KBNR b KQkq - 0 3"));
    list.add(new PgnFileTestCase("19_white_queen_queen.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnb1kbnr/pppp1ppp/4p3/8/7Q/4P3/PPPP1PPP/RNB1KBNR b KQkq - 0 3"));
    list.add(new PgnFileTestCase("20_white_queen_pawn.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bqkbnr/pppp1ppQ/2n5/4p3/8/4P3/PPPP1PPP/RNB1KBNR b KQkq - 0 3"));
    list.add(new PgnFileTestCase("21_white_king_rook.pgn", "", "", "", 9, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1nbqkbnr/1ppppppp/8/p7/4P3/3K1N2/PPPP1PPP/RNBQ1B1R b k - 0 5"));
    list.add(new PgnFileTestCase("22_white_king_knight.pgn", "", "", "", 7, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bqkb1r/pppppppp/5n2/8/3KP3/8/PPPP1PPP/RNBQ1BNR b kq - 0 4"));
    list.add(new PgnFileTestCase("23_white_king_bishop.pgn", "", "", "", 9, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqk2r/pppp1pp1/5n1p/4p3/1K2P3/8/PPPP1PPP/RNBQ1BNR b kq - 0 5"));
    list.add(new PgnFileTestCase("24_white_king_queen.pgn", "", "", "", 7, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnb1kbnr/pppp1ppp/8/4p3/4P3/P3K3/1PPP1PPP/RNBQ1BNR b kq - 0 4"));
    list.add(new PgnFileTestCase("25_white_king_pawn.pgn", "", "", "", 7, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bqkbnr/pppnpppp/8/8/3KP3/8/PPPP1PPP/RNBQ1BNR b kq - 0 4"));

    return new PgnFileTestCaseList(PgnTest.BASIC_CAPTURE_WHITE, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicCaptureBlack() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_black_rook_rook.pgn", "", "", "", 8, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1nbqkbnr/1ppppppp/8/p2r4/P7/8/1PPPPPPP/1NBQKBNR w Kk - 0 5"));
    list.add(new PgnFileTestCase("02_black_rook_knight.pgn", "", "", "", 6, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1nbqkbnr/rppppppp/8/p7/8/8/PPPPPPPP/R1BQKBNR w KQk - 0 4"));
    list.add(new PgnFileTestCase("03_black_rook_bishop.pgn", "", "", "", 6, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1nbqkbnr/1ppppppp/7r/p7/3P2P1/8/PPP1PP1P/RN1QKBNR w KQk - 0 4"));
    list.add(new PgnFileTestCase("04_black_rook_queen.pgn", "", "", "", 6, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkbn1/ppppppp1/8/7r/4P2p/8/PPPP1PPP/RNB1KBNR w KQq - 0 4"));
    list.add(new PgnFileTestCase("05_black_rook_pawn.pgn", "", "", "", 6, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkbn1/ppppppp1/3r4/7p/8/8/PPP1PPPP/RNBQKBNR w KQq - 0 4"));
    list.add(new PgnFileTestCase("06_black_knight_rook.pgn", "", "", "", 8, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bqkbnr/pppppppp/8/8/2PPP3/8/PP1K1PPP/nNBQ1BNR w kq - 0 5"));
    list.add(new PgnFileTestCase("07_black_knight_knight.pgn", "", "", "", 8, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bqkbnr/pppppppp/8/8/3PP3/2N5/PPP1KPPP/R1BQ1BnR w kq - 0 5"));
    list.add(new PgnFileTestCase("08_black_knight_bishop.pgn", "", "", "", 8, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkb1r/pppppppp/8/8/PPPP4/8/4PPPP/RNBQKnNR w KQkq - 0 5"));
    list.add(new PgnFileTestCase("09_black_knight_queen.pgn", "", "", "", 8, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkb1r/pppppppp/8/2P5/1P1P4/8/P3PPPP/RNBnKBNR w KQkq - 0 5"));
    list.add(new PgnFileTestCase("10_black_knight_pawn.pgn", "", "", "", 4, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bqkbnr/pppppppp/8/8/3nP3/8/PPP2PPP/RNBQKBNR w KQkq - 0 3"));
    list.add(new PgnFileTestCase("11_black_bishop_rook.pgn", "", "", "", 6, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rn1qkbnr/p1pppppp/1p6/4N3/8/6P1/PPPPPP1P/RNBQKB1b w Qkq - 0 4"));
    list.add(new PgnFileTestCase("12_black_bishop_knight.pgn", "", "", "", 6, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rn1qkbnr/ppp1pppp/3p4/6N1/8/2P5/PP1PPPPP/RbBQKB1R w KQkq - 0 4"));
    list.add(new PgnFileTestCase("13_black_bishop_bishop.pgn", "", "", "", 6, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rn1qkbnr/p1pppppp/1p6/4N3/8/4P3/PPPP1PPP/RNBQKb1R w KQkq - 0 4"));
    list.add(new PgnFileTestCase("14_black_bishop_queen.pgn", "", "", "", 6, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rn1qkbnr/ppp1pppp/8/3p4/4P3/2N4P/PPPP1PP1/R1BbKBNR w KQkq - 0 4"));
    list.add(new PgnFileTestCase("15_black_bishop_pawn.pgn", "", "", "", 6, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rn1qkbnr/ppp1pppp/8/3pP3/8/5P2/PPPP2bP/RNBQKBNR w KQkq - 0 4"));
    list.add(new PgnFileTestCase("16_black_queen_rook.pgn", "", "", "", 6, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnb1kbnr/pppp1ppp/4p3/8/8/BP6/P1PPPPPP/qN1QKBNR w Kkq - 0 4"));
    list.add(new PgnFileTestCase("17_black_queen_knight.pgn", "", "", "", 8, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnb1kbnr/ppp1pppp/8/3p2P1/7P/2P5/PP1PPP2/RqBQKBNR w KQkq - 0 5"));
    list.add(new PgnFileTestCase("18_black_queen_bishop.pgn", "", "", "", 4, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnb1kbnr/pppp1ppp/8/4p1q1/3P4/8/PPP1PPPP/RN1QKBNR w KQkq - 0 3"));
    list.add(new PgnFileTestCase("19_black_queen_queen.pgn", "", "", "", 6, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnb1kbnr/pppp1ppp/8/4p3/3P1q2/8/PPP1PPPP/RNB1KBNR w KQkq - 0 4"));
    list.add(new PgnFileTestCase("20_black_queen_pawn.pgn", "", "", "", 6, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnb1kbnr/pppp1ppp/8/3Np3/4P3/8/PPPP1qPP/R1BQKBNR w KQkq - 0 4"));
    list.add(new PgnFileTestCase("21_black_king_rook.pgn", "", "", "", 16, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbq1bnr/ppp1pppp/7B/3p4/3P2N1/1P1Q4/P1P1PPPP/k3KBNR w K - 0 9"));
    list.add(new PgnFileTestCase("22_black_king_knight.pgn", "", "", "", 22, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "2bq1bnr/rpppp1pp/p1n2p2/3B1R1P/4P3/5P2/PPPP2P1/RNBQK1k1 w Q - 0 12"));
    list.add(new PgnFileTestCase("23_black_king_bishop.pgn", "", "", "", 16, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbq1bnr/ppp1pppp/8/3p4/3P4/P1P2NPQ/1P2PPBP/RNk1K2R w KQ - 0 9"));
    list.add(new PgnFileTestCase("24_black_king_queen.pgn", "", "", "", 6, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bq1bnr/pppppkpp/2n2p2/8/8/2P5/PP1PPPPP/RNB1KBNR w KQ - 0 4"));
    list.add(new PgnFileTestCase("25_black_king_pawn.pgn", "", "", "", 18, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbq1b1r/ppp1pppp/1P3n2/3p4/3P4/5N2/k1PNPPPP/1RBQKB1R w K - 0 10"));

    return new PgnFileTestCaseList(PgnTest.BASIC_CAPTURE_BLACK, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicEnPassantCaptureWhite() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_white_en_passant_capture_right_a6.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "4k3/8/P7/8/8/8/8/4K3 b - - 0 101"));
    list.add(new PgnFileTestCase("02_white_en_passant_capture_right_b6.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "4k3/p7/1P6/8/8/8/8/4K3 b - - 0 101"));
    list.add(new PgnFileTestCase("03_white_en_passant_capture_right_c6.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "4k3/pp6/2P5/8/8/8/8/4K3 b - - 0 101"));
    list.add(new PgnFileTestCase("04_white_en_passant_capture_right_d6.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "4k3/ppp5/3P4/8/8/8/8/4K3 b - - 0 101"));
    list.add(new PgnFileTestCase("05_white_en_passant_capture_right_e6.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "4k3/pppp4/4P3/8/8/8/8/4K3 b - - 0 101"));
    list.add(new PgnFileTestCase("06_white_en_passant_capture_right_f6.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4k3/ppppp3/5P2/8/8/8/8/4K3 b - - 0 101"));
    list.add(new PgnFileTestCase("07_white_en_passant_capture_right_g6.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4k3/pppppp2/6P1/8/8/8/8/4K3 b - - 0 101"));
    list.add(new PgnFileTestCase("08_white_en_passant_capture_left_b6.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4k3/2pppppp/1P6/8/8/8/8/4K3 b - - 0 101"));
    list.add(new PgnFileTestCase("09_white_en_passant_capture_left_c6.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4k3/1p1ppppp/2P5/8/8/8/8/4K3 b - - 0 101"));
    list.add(new PgnFileTestCase("10_white_en_passant_capture_left_d6.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4k3/1pp1pppp/3P4/8/8/8/8/4K3 b - - 0 101"));
    list.add(new PgnFileTestCase("11_white_en_passant_capture_left_e6.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4k3/1ppp1ppp/4P3/8/8/8/8/4K3 b - - 0 101"));
    list.add(new PgnFileTestCase("12_white_en_passant_capture_left_f6.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4k3/1pppp1pp/5P2/8/8/8/8/4K3 b - - 0 101"));
    list.add(new PgnFileTestCase("13_white_en_passant_capture_left_g6.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4k3/1ppppp1p/6P1/8/8/8/8/4K3 b - - 0 101"));
    list.add(new PgnFileTestCase("14_white_en_passant_capture_left_h6.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4k3/1pppppp1/7P/8/8/8/8/4K3 b - - 0 101"));

    return new PgnFileTestCaseList(PgnTest.BASIC_EN_PASSANT_CAPTURE_WHITE, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicEnPassantCaptureBlack() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_black_en_passant_capture_left_a3.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "4k3/8/8/8/2pppppp/p7/8/4K3 w - - 0 101"));
    list.add(new PgnFileTestCase("02_black_en_passant_capture_left_b3.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "4k3/8/8/8/3ppppp/1p6/8/4K3 w - - 0 101"));
    list.add(new PgnFileTestCase("03_black_en_passant_capture_left_c3.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "4k3/8/8/8/4pppp/2p5/8/4K3 w - - 0 101"));
    list.add(new PgnFileTestCase("04_black_en_passant_capture_left_d3.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "4k3/8/8/8/5ppp/3p4/8/4K3 w - - 0 101"));
    list.add(new PgnFileTestCase("05_black_en_passant_capture_left_e3.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "4k3/8/8/8/6pp/4p3/8/4K3 w - - 0 101"));
    list.add(new PgnFileTestCase("06_black_en_passant_capture_left_f3.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "4k3/8/8/8/7p/5p2/8/4K3 w - - 0 101"));
    list.add(new PgnFileTestCase("07_black_en_passant_capture_left_g3.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "4k3/8/8/8/8/6p1/8/4K3 w - - 0 101"));
    list.add(new PgnFileTestCase("08_black_en_passant_capture_right_b3.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.WINNABLE, "4k3/8/8/8/8/1p6/2PPPPPP/4K3 w - - 0 101"));
    list.add(new PgnFileTestCase("09_black_en_passant_capture_right_c3.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.WINNABLE, "4k3/8/8/8/8/2p5/1P1PPPPP/4K3 w - - 0 101"));
    list.add(new PgnFileTestCase("10_black_en_passant_capture_right_d3.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4k3/8/8/8/8/3p4/1PP1PPPP/4K3 w - - 0 101"));
    list.add(new PgnFileTestCase("11_black_en_passant_capture_right_e3.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4k3/8/8/8/8/4p3/1PPP1PPP/4K3 w - - 0 101"));
    list.add(new PgnFileTestCase("12_black_en_passant_capture_right_f3.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.WINNABLE, "4k3/8/8/8/8/5p2/1PPPP1PP/4K3 w - - 0 101"));
    list.add(new PgnFileTestCase("13_black_en_passant_capture_right_g3.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.WINNABLE, "4k3/8/8/8/8/6p1/1PPPPP1P/4K3 w - - 0 101"));
    list.add(new PgnFileTestCase("14_black_en_passant_capture_right_h3.pgn", "", "", "", 2, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.WINNABLE, "4k3/8/8/8/8/7p/1PPPPPP1/4K3 w - - 0 101"));

    return new PgnFileTestCaseList(PgnTest.BASIC_EN_PASSANT_CAPTURE_BLACK, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicPromotionPieceWhite() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_white_promotion_capture_no_rook.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rRbqkbnr/p1pppppp/n7/7Q/2P5/8/P2PPPPP/RNB1KBNR b KQkq - 0 9"));
    list.add(new PgnFileTestCase("02_white_promotion_capture_no_knight.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rNbqkbnr/p1pppppp/n7/7Q/2P5/8/P2PPPPP/RNB1KBNR b KQkq - 0 9"));
    list.add(new PgnFileTestCase("03_white_promotion_capture_no_bishop.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rBbqkbnr/p1pppppp/n7/7Q/2P5/8/P2PPPPP/RNB1KBNR b KQkq - 0 9"));
    list.add(new PgnFileTestCase("04_white_promotion_capture_no_queen.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rQbqkbnr/p1pppppp/n7/7Q/2P5/8/P2PPPPP/RNB1KBNR b KQkq - 0 9"));
    list.add(new PgnFileTestCase("05_white_promotion_capture_yes_rook.pgn", "", "", "", 3, 1, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "R2qkbnr/2pppppp/n7/8/8/8/1PPPPPPP/RNBQKBNR b KQk - 0 5"));
    list.add(new PgnFileTestCase("06_white_promotion_capture_yes_knight.pgn", "", "", "", 3, 1, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "N2qkbnr/2pppppp/n7/8/8/8/1PPPPPPP/RNBQKBNR b KQk - 0 5"));
    list.add(new PgnFileTestCase("07_white_promotion_capture_yes_bishop.pgn", "", "", "", 3, 1, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "B2qkbnr/2pppppp/n7/8/8/8/1PPPPPPP/RNBQKBNR b KQk - 0 5"));
    list.add(new PgnFileTestCase("08_white_promotion_capture_yes_queen.pgn", "", "", "", 3, 1, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "Q2qkbnr/2pppppp/n7/8/8/8/1PPPPPPP/RNBQKBNR b KQk - 0 5"));

    return new PgnFileTestCaseList(PgnTest.BASIC_PROMOTION_PIECE_WHITE, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicPromotionPieceBlack() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_black_promotion_capture_no_rook.pgn", "", "", "", 4, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnb1kbnr/pppp1pp1/8/4p3/q7/7R/PPPPPPP1/RNBQKBNr w Qkq - 0 9"));
    list.add(new PgnFileTestCase("02_black_promotion_capture_no_knight.pgn", "", "", "", 4, 3, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnb1kbnr/pppp1pp1/8/4p3/q7/7R/PPPPPPP1/RNBQKBNn w Qkq - 0 9"));
    list.add(new PgnFileTestCase("03_black_promotion_capture_no_bishop.pgn", "", "", "", 4, 3, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnb1kbnr/pppp1pp1/8/4p3/q7/7R/PPPPPPP1/RNBQKBNb w Qkq - 0 9"));
    list.add(new PgnFileTestCase("04_black_promotion_capture_no_queen.pgn", "", "", "", 4, 3, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnb1kbnr/pppp1pp1/8/4p3/q7/7R/PPPPPPP1/RNBQKBNq w Qkq - 0 9"));
    list.add(new PgnFileTestCase("05_black_promotion_capture_yes_rook.pgn", "", "", "", 4, 1, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/pppp1ppp/8/8/8/5NP1/P3PPBP/RNrQK2R w KQkq - 0 6"));
    list.add(new PgnFileTestCase("06_black_promotion_capture_yes_knight.pgn", "", "", "", 4, 1, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/pppp1ppp/8/8/8/5NP1/P3PPBP/RNnQK2R w KQkq - 0 6"));
    list.add(new PgnFileTestCase("07_black_promotion_capture_yes_bishop.pgn", "", "", "", 4, 1, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/pppp1ppp/8/8/8/5NP1/P3PPBP/RNbQK2R w KQkq - 0 6"));
    list.add(new PgnFileTestCase("08_black_promotion_capture_yes_queen.pgn", "", "", "", 4, 1, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/pppp1ppp/8/8/8/5NP1/P3PPBP/RNqQK2R w KQkq - 0 6"));

    return new PgnFileTestCaseList(PgnTest.BASIC_PROMOTION_PIECE_BLACK, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicPromotionSquareWhite() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_white_promotion_square_straight_a8.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "Q7/1PPPPPPP/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("02_white_promotion_square_straight_b8.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "1Q6/P1PPPPPP/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("03_white_promotion_square_straight_c8.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "2Q5/PP1PPPPP/2B5/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("04_white_promotion_square_straight_d8.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "3Q4/PPP1PPPP/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("05_white_promotion_square_straight_e8.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "4Q3/PPPP1PPP/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("06_white_promotion_square_straight_f8.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "5Q2/PPPPP1PP/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("07_white_promotion_square_straight_g8.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "6Q1/PPPPPP1P/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("08_white_promotion_square_straight_h8.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "7Q/PPPPPPP1/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("09_white_promotion_square_right_a8.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "Qrrrbrrr/P1PPPPPP/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("10_white_promotion_square_right_b8.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rQrrbrrr/PP1PPPPP/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("11_white_promotion_square_right_c8.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rrQrbrrr/PPP1PPPP/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("12_white_promotion_square_right_d8.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rrrQbrrr/PPPP1PPP/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("13_white_promotion_square_right_e8.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rrrrQrrr/PPPPP1PP/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("14_white_promotion_square_right_f8.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rrrrbQrr/PPPPPP1P/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("15_white_promotion_square_right_g8.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rrrrbrQr/PPPPPPP1/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("16_white_promotion_square_left_b8.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "nQnnnnnn/1PPPPPPP/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("17_white_promotion_square_left_c8.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "nnQnnnnn/P1PPPPPP/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("18_white_promotion_square_left_d8.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "nnnQnnnn/PP1PPPPP/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("19_white_promotion_square_left_e8.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "nnnnQnnn/PPP1PPPP/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("20_white_promotion_square_left_f8.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "nnnnnQnn/PPPP1PPP/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("21_white_promotion_square_left_g8.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "nnnnnnQn/PPPPP1PP/8/8/8/8/8/2k1K3 b - - 0 100"));
    list.add(new PgnFileTestCase("22_white_promotion_square_left_h8.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "nnnnnnnQ/PPPPPP1P/8/8/8/8/8/2k1K3 b - - 0 100"));

    return new PgnFileTestCaseList(PgnTest.BASIC_PROMOTION_SQUARE_WHITE, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicPromotionSquareBlack() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_black_promotion_square_straight_a1.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/1ppppppp/q7 w - - 0 101"));
    list.add(new PgnFileTestCase("02_black_promotion_square_straight_b1.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/p1pppppp/1q6 w - - 0 101"));
    list.add(new PgnFileTestCase("03_black_promotion_square_straight_c1.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/pp1ppppp/2q5 w - - 0 101"));
    list.add(new PgnFileTestCase("04_black_promotion_square_straight_d1.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/ppp1pppp/3q4 w - - 0 101"));
    list.add(new PgnFileTestCase("05_black_promotion_square_straight_e1.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/pppp1ppp/4q3 w - - 0 101"));
    list.add(new PgnFileTestCase("06_black_promotion_square_straight_f1.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/5b2/ppppp1pp/5q2 w - - 0 101"));
    list.add(new PgnFileTestCase("07_black_promotion_square_straight_g1.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/pppppp1p/6q1 w - - 0 101"));
    list.add(new PgnFileTestCase("08_black_promotion_square_straight_h1.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/ppppppp1/7q w - - 0 101"));
    list.add(new PgnFileTestCase("09_black_promotion_square_left_a1.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/p1pppppp/qQQNQQQQ w - - 0 101"));
    list.add(new PgnFileTestCase("10_black_promotion_square_left_b1.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/pp1ppppp/QqQNQQQQ w - - 0 101"));
    list.add(new PgnFileTestCase("11_black_promotion_square_left_c1.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/ppp1pppp/QQqNQQQQ w - - 0 101"));
    list.add(new PgnFileTestCase("12_black_promotion_square_left_d1.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/pppp1ppp/QQQqQQQQ w - - 0 101"));
    list.add(new PgnFileTestCase("13_black_promotion_square_left_e1.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/ppppp1pp/QQQNqQQQ w - - 0 101"));
    list.add(new PgnFileTestCase("14_black_promotion_square_left_f1.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/pppppp1p/QQQNQqQQ w - - 0 101"));
    list.add(new PgnFileTestCase("15_black_promotion_square_left_g1.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/ppppppp1/QQQNQQqQ w - - 0 101"));
    list.add(new PgnFileTestCase("16_black_promotion_square_right_b1.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/1ppppppp/BqBNBBBB w - - 0 101"));
    list.add(new PgnFileTestCase("17_black_promotion_square_right_c1.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/p1pppppp/BBqNBBBB w - - 0 101"));
    list.add(new PgnFileTestCase("18_black_promotion_square_right_d1.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/pp1ppppp/BBBqBBBB w - - 0 101"));
    list.add(new PgnFileTestCase("19_black_promotion_square_right_e1.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/ppp1pppp/BBBNqBBB w - - 0 101"));
    list.add(new PgnFileTestCase("20_black_promotion_square_right_f1.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/pppp1ppp/BBBNBqBB w - - 0 101"));
    list.add(new PgnFileTestCase("21_black_promotion_square_right_g1.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/ppppp1pp/BBBNBBqB w - - 0 101"));
    list.add(new PgnFileTestCase("22_black_promotion_square_right_h1.pgn", "", "", "", 1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3k1K2/8/8/8/8/8/pppppp1p/BBBNBBBq w - - 0 101"));

    return new PgnFileTestCaseList(PgnTest.BASIC_PROMOTION_SQUARE_BLACK, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicCheckWhite() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_white_rook_check_direct_adjacent.pgn", "", "", "", -1, 5, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbq1bnr/pppp1ppp/3Rk3/4p3/P7/8/1PPPPPPP/1NBQKBNR b K - 5 4"));
    list.add(new PgnFileTestCase("02_white_rook_check_direct_range.pgn", "", "", "", -1, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbq1bnr/pppp1ppp/1R1k4/4p3/P7/8/1PPPPPPP/1NBQKBNR b K - 5 4"));
    list.add(new PgnFileTestCase("03_white_rook_check_discover.pgn", "", "", "", -1, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkb1r/ppp1pppp/3pn3/P2R4/Q7/2P5/1P1PPPPP/1NB1KBNR b Kkq - 10 8"));
    list.add(new PgnFileTestCase("04_white_knight_check_direct.pgn", "", "", "", -1, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkb1r/pppppppp/5N2/8/4n3/8/PPPPPPPP/R1BQKBNR b KQkq - 5 3"));
    list.add(new PgnFileTestCase("05_white_knight_check_discover_orthogonal.pgn", "", "", "", -1, 5,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbq1bnr/pppp2pp/PN6/R4k2/4pp2/5N2/1PPPPPPP/2BQKB1R b K - 5 8"));
    list.add(new PgnFileTestCase("06_white_knight_check_discover_diagonal.pgn", "", "", "", -1, 7,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bqkb1r/ppp1pppp/3p1n2/8/Q2n4/N1P5/PP1PPPPP/R1B1KBNR b KQkq - 7 5"));
    list.add(new PgnFileTestCase("07_white_bishop_check_direct_adjacent.pgn", "", "", "", -1, 3,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkb1r/pppppBpp/5p1n/8/8/4P3/PPPP1PPP/RNBQK1NR b KQkq - 3 3"));
    list.add(new PgnFileTestCase("08_white_bishop_check_direct_range.pgn", "", "", "", -1, 1, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/ppp1pppp/3p4/1B6/8/4P3/PPPP1PPP/RNBQK1NR b KQkq - 1 2"));
    list.add(new PgnFileTestCase("09_white_bishop_check_discover.pgn", "", "", "", -1, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bq1bnr/pppp1ppp/P1n5/R3k3/2B1p3/4P3/1PPP1PPP/1NBQK1NR b K - 4 7"));
    list.add(new PgnFileTestCase("10_white_queen_check_direct_orthogonal_adjacent.pgn", "", "", "", 3, 3,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r2qkb1r/pbppQppp/1pn2P2/8/8/8/PPP1PPPP/RNB1KBNR b KQkq - 3 6"));
    list.add(new PgnFileTestCase("11_white_queen_check_direct_orthogonal_range.pgn", "", "", "", 3, 2,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bqkb1r/p1pp1ppp/1pn2P2/8/8/4Q3/PPP1PPPP/RNB1KBNR b KQkq - 1 5"));
    list.add(new PgnFileTestCase("12_white_queen_check_direct_diagonal_adjacent.pgn", "", "", "", -1, 3,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bqkbnr/pppppQpp/2n2p2/8/8/2P5/PP1PPPPP/RNB1KBNR b KQkq - 3 3"));
    list.add(new PgnFileTestCase("13_white_queen_check_direct_diagonal_range.pgn", "", "", "", -1, 1,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/ppppp1pp/5p2/7Q/8/4P3/PPPP1PPP/RNB1KBNR b KQkq - 1 2"));
    list.add(new PgnFileTestCase("14_white_king_check_discover_orthogonal.pgn", "", "", "", 3, 3,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkb1r/p1pp2pp/1p3Pn1/8/8/5K2/PPP2PPP/RNB1QBNR b kq - 3 8"));
    list.add(new PgnFileTestCase("15_white_king_check_discover_diagonal.pgn", "", "", "", -1, 13,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bqkb1r/ppp1pppp/3p4/4n2n/QK6/2P5/PP1PPPPP/RNB2BNR b kq - 13 8"));

    return new PgnFileTestCaseList(PgnTest.BASIC_CHECK_WHITE, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicCheckBlack() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_black_rook_check_direct_adjacent.pgn", "", "", "", -1, 6, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "1nbqkbnr/1ppppppp/8/p7/2rKP3/8/PPPP1PPP/RNBQ1BNR w k - 6 5"));
    list.add(new PgnFileTestCase("02_black_rook_check_direct_range.pgn", "", "", "", 7, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1nbqkbnr/1ppppppp/8/8/2N2P2/r4K2/PPPPP1PP/R1BQ1BNR w k - 3 6"));
    list.add(new PgnFileTestCase("03_black_rook_check_discover.pgn", "", "", "", -1, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "2b1kbnr/pr1ppppp/2n5/q1p3B1/4N3/1p1P1NPP/PPP1PPB1/1R1QK2R w Kk - 1 10"));
    list.add(new PgnFileTestCase("04_black_knight_check_direct.pgn", "", "", "", -1, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bqkbnr/pppppppp/1N6/8/8/5n2/PPPPPPPP/R1BQKBNR w KQkq - 6 4"));
    list.add(new PgnFileTestCase("05_black_knight_check_discover_orthogonal.pgn", "", "", "", 3, 7,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1b1kbnr/ppp1pppp/4q1n1/3P4/8/1PN3N1/P1PP1PPP/R1BQKB1R w KQkq - 1 7"));
    list.add(new PgnFileTestCase("06_black_knight_check_discover_diagonal.pgn", "", "", "", -1, 6,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnb1kb1r/pp1ppppp/1qp1n3/8/8/2N2P1N/PPPPPKPP/R1BQ1B1R w kq - 4 7"));
    list.add(new PgnFileTestCase("07_black_bishop_check_direct_adjacent.pgn", "", "", "", -1, 2,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqk1nr/pppp1ppp/4p3/8/8/1PN2P2/P1PPPbPP/R1BQKBNR w KQkq - 1 4"));
    list.add(new PgnFileTestCase("08_black_bishop_check_direct_range.pgn", "", "", "", -1, 6, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rn1qkbnr/pbpppppp/1p6/8/8/6PB/PPPPPPKP/RNBQ2NR w kq - 6 5"));
    list.add(new PgnFileTestCase("09_black_bishop_check_discover.pgn", "", "", "", 3, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rn2kbnr/ppp1qppp/8/8/6b1/2N2N2/PPPP1PPP/R1BQKB1R w KQkq - 4 6"));
    list.add(new PgnFileTestCase("10_black_queen_check_direct_orthogonal_adjacent.pgn", "", "", "", -1, 6,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnb1kbnr/ppp1pppp/3p4/8/3N4/2N1P3/PPPPqPPP/R1BQKB1R w KQkq - 6 5"));
    list.add(new PgnFileTestCase("11_black_queen_check_direct_orthogonal_range.pgn", "", "", "", 3, 3,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnb1kbnr/ppp1pppp/8/3Pq3/8/2N5/PPPP1PPP/R1BQKBNR w KQkq - 3 4"));
    list.add(new PgnFileTestCase("12_black_queen_check_direct_diagonal_adjacent.pgn", "", "", "", -1, 4,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnb1kbnr/pppp1ppp/4p3/8/8/2NP1N2/PPPqPPPP/R1BQKB1R w KQkq - 4 4"));
    list.add(new PgnFileTestCase("13_black_queen_check_direct_diagonal_range.pgn", "", "", "", -1, 2,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnb1kbnr/pp1ppppp/1qp5/8/8/5P2/PPPPPKPP/RNBQ1BNR w kq - 2 3"));
    list.add(new PgnFileTestCase("14_black_king_check_discover_orthogonal.pgn", "", "", "", 3, 8,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rn2qbnr/pppb1ppp/3k4/8/8/2NB1N2/PPPP1PPP/R1BQK2R w KQ - 8 8"));
    list.add(new PgnFileTestCase("15_black_king_check_discover_diagonal.pgn", "", "", "", -1, 7,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbq2nr/pppp1p1p/4p1pb/8/3N2k1/1PNP4/PBPKPPPP/R2Q1B1R w - - 7 8"));

    return new PgnFileTestCaseList(PgnTest.BASIC_CHECK_BLACK, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicDoubleCheckWhite() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_white_double_check_rook.pgn", "", "", "", -1, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "5k2/1n6/8/8/5R2/B7/8/4K3 b - - 3 101"));
    list.add(new PgnFileTestCase("02_white_double_check_knight_orthogonal.pgn", "", "", "", -1, 3,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "3n1k2/3N4/8/8/8/8/5R2/4K3 b - - 3 101"));
    list.add(new PgnFileTestCase("03_white_double_check_knight_diagonal.pgn", "", "", "", -1, 3,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "2n2k2/8/4N3/8/8/B7/8/4K3 b - - 3 101"));
    list.add(new PgnFileTestCase("04_white_double_check_bishop.pgn", "", "", "", -1, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "5k2/8/3B4/3n4/8/8/5R2/4K3 b - - 3 101"));

    return new PgnFileTestCaseList(PgnTest.BASIC_DOUBLE_CHECK_WHITE, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicDoubleCheckBlack() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_black_double_check_rook.pgn", "", "", "", -1, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "4k3/6R1/8/7b/8/8/4Kr2/8 w - - 3 102"));
    list.add(new PgnFileTestCase("02_black_double_check_knight_orthogonal.pgn", "", "", "", -1, 3,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "6k1/3r4/8/8/5n2/3K4/5B2/8 w - - 3 102"));
    list.add(new PgnFileTestCase("03_black_double_check_knight_diagonal.pgn", "", "", "", -1, 3,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "6k1/7q/2B5/8/8/4n3/2K5/8 w - - 3 102"));
    list.add(new PgnFileTestCase("04_black_double_check_bishop.pgn", "", "", "", -1, 1, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3Q4/1K5r/8/3b4/6k1/8/8/8 w - - 1 102"));

    return new PgnFileTestCaseList(PgnTest.BASIC_DOUBLE_CHECK_BLACK, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicCheckmateWhite() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_white_rook_checkmate_direct_adjacent.pgn", "", "", "", -1, 3,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "2kR4/R7/8/8/8/8/8/3RK3 b - - 3 101"));
    list.add(new PgnFileTestCase("02_white_rook_checkmate_direct_range.pgn", "", "", "", -1, 4,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "1k5R/6R1/8/8/8/8/8/3RK3 b - - 4 101"));
    list.add(
        new PgnFileTestCase("03_white_rook_checkmate_discover.pgn", "", "", "", -1, 3, CheckmateOrStalemate.CHECKMATE,
            1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
            UnwinnableQuick.UNWINNABLE, "2k5/R6R/4B3/7p/8/8/8/1R1RRK2 b - - 1 102"));
    list.add(
        new PgnFileTestCase("04_white_knight_checkmate_direct.pgn", "", "", "", -1, 1, CheckmateOrStalemate.CHECKMATE,
            1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
            UnwinnableQuick.UNWINNABLE, "3k4/1Q6/2N5/7p/8/8/8/2RKR3 b - - 1 101"));
    list.add(new PgnFileTestCase("05_white_knight_checkmate_discover_orthogonal.pgn", "", "", "", -1, 5,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "3k4/8/5N2/4R3/8/6b1/3R4/2RK4 b - - 5 102"));
    list.add(new PgnFileTestCase("06_white_knight_checkmate_discover_diagonal.pgn", "", "", "", -1, 5,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "3k2N1/1R6/8/4R1B1/3b4/8/8/2RK4 b - - 5 102"));
    list.add(new PgnFileTestCase("07_white_bishop_checkmate_direct_adjacent.pgn", "", "", "", -1, 5,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "5B2/8/R4Q2/1k6/2B5/2K5/8/8 b - - 5 102"));
    list.add(new PgnFileTestCase("08_white_bishop_checkmate_direct_range.pgn", "", "", "", -1, 5,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "5B2/8/R4Q2/3k4/8/2K2B2/8/8 b - - 5 102"));
    list.add(
        new PgnFileTestCase("09_white_bishop_checkmate_discover.pgn", "", "", "", -1, 1, CheckmateOrStalemate.CHECKMATE,
            1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
            UnwinnableQuick.UNWINNABLE, "8/2B5/1R6/2k2Q2/7p/2K5/8/8 b - - 1 102"));
    list.add(new PgnFileTestCase("10_white_queen_checkmate_direct_orthogonal_adjacent.pgn", "", "", "", -1, 1,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/8/1RQ5/2k4p/5B2/2K5/8/8 b - - 1 101"));
    list.add(new PgnFileTestCase("11_white_queen_checkmate_direct_orthogonal_range.pgn", "", "", "", -1, 3,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/k7/8/Q1K5/1R6/8/8/8 b - - 3 101"));
    list.add(new PgnFileTestCase("12_white_queen_checkmate_direct_diagonal_adjacent.pgn", "", "", "", -1, 5,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "k7/1QK5/8/7R/1R6/8/8/8 b - - 5 102"));
    list.add(new PgnFileTestCase("13_white_queen_checkmate_direct_diagonal_range.pgn", "", "", "", -1, 1,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/2Q5/4k1K1/1R6/8/1Q6/8/4q3 b - - 1 101"));
    list.add(new PgnFileTestCase("14_white_king_checkmate_discover_orthogonal.pgn", "", "", "", -1, 3,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "Q7/4k2R/R6K/8/8/8/8/8 b - - 3 101"));
    list.add(new PgnFileTestCase("15_white_king_checkmate_discover_diagonal.pgn", "", "", "", -1, 1,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "3k4/1K6/8/BN4p1/8/7B/8/4R3 b - - 1 102"));

    return new PgnFileTestCaseList(PgnTest.BASIC_CHECKMATE_WHITE, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicCheckmateBlack() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_black_rook_checkmate_direct_adjacent.pgn", "", "", "", -1, 3,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "2Kr4/7q/8/8/8/8/8/2kr4 w - - 3 102"));
    list.add(new PgnFileTestCase("02_black_rook_checkmate_direct_range.pgn", "", "", "", -1, 5,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "4kr2/3q4/8/8/7b/8/6rN/4K3 w - - 5 103"));
    list.add(
        new PgnFileTestCase("03_black_rook_checkmate_discover.pgn", "", "", "", -1, 5, CheckmateOrStalemate.CHECKMATE,
            1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
            UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE, "3K4/1r6/3k4/b3r3/5N2/8/8/2r5 w - - 5 103"));
    list.add(
        new PgnFileTestCase("04_black_knight_checkmate_direct.pgn", "", "", "", -1, 1, CheckmateOrStalemate.CHECKMATE,
            1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
            UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE, "Q1r5/8/7r/3K4/1r3n2/8/8/3kr3 w - - 1 102"));
    list.add(new PgnFileTestCase("05_black_knight_checkmate_discover_orthogonal.pgn", "", "", "", -1, 5,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "4k3/8/5n1r/8/8/3N4/5q2/7K w - - 5 103"));
    list.add(new PgnFileTestCase("06_black_knight_checkmate_discover_diagonal.pgn", "", "", "", -1, 1,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "Q4q2/3rk3/n7/b7/8/8/7r/4K3 w - - 1 103"));
    list.add(new PgnFileTestCase("07_black_bishop_checkmate_direct_adjacent.pgn", "", "", "", -1, 1,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "4k3/P7/8/5r1r/8/8/q6b/6KN w - - 1 102"));
    list.add(new PgnFileTestCase("08_black_bishop_checkmate_direct_range.pgn", "", "", "", -1, 1,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "Q7/8/8/2b1kr1r/8/8/q7/6K1 w - - 1 102"));
    list.add(
        new PgnFileTestCase("09_black_bishop_checkmate_discover.pgn", "", "", "", -1, 5, CheckmateOrStalemate.CHECKMATE,
            1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
            UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE, "6q1/P7/4k3/1B2br1r/8/8/8/6K1 w - - 5 103"));
    list.add(new PgnFileTestCase("10_black_queen_checkmate_direct_orthogonal_adjacent.pgn", "", "", "", -1, 1,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "1Q6/8/4k3/7r/8/8/r5q1/6K1 w - - 1 102"));
    list.add(new PgnFileTestCase("11_black_queen_checkmate_direct_orthogonal_range.pgn", "", "", "", -1, 1,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "1Q6/8/4k3/4nrqr/8/8/8/6K1 w - - 1 102"));
    list.add(new PgnFileTestCase("12_black_queen_checkmate_direct_diagonal_adjacent.pgn", "", "", "", -1, 1,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "Q7/8/4k3/4nr1r/8/8/5q2/6K1 w - - 1 102"));
    list.add(new PgnFileTestCase("13_black_queen_checkmate_direct_diagonal_range.pgn", "", "", "", -1, 1,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "Q7/8/1q2k3/3bnr1r/8/8/8/6K1 w - - 1 102"));
    list.add(new PgnFileTestCase("14_black_king_checkmate_discover_orthogonal.pgn", "", "", "", -1, 5,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "5q2/8/8/8/3kr3/3q4/N7/4K3 w - - 5 103"));
    list.add(new PgnFileTestCase("15_black_king_checkmate_discover_diagonal.pgn", "", "", "", -1, 5,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "2K5/8/6N1/bnnn2k1/6b1/8/7b/8 w - - 5 103"));

    return new PgnFileTestCaseList(PgnTest.BASIC_CHECKMATE_BLACK, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicCheckmateVariousWhite() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(
        new PgnFileTestCase("01_checkmate_by_white_capture_yes.pgn", "", "", "", 7, 5, CheckmateOrStalemate.CHECKMATE,
            1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
            UnwinnableQuick.UNWINNABLE, "1r1qkbnr/pbp2Qp1/2pp3p/4p1N1/4P3/8/PPPP1PPP/RNB1K2R b KQk - 0 8"));
    list.add(
        new PgnFileTestCase("02_checkmate_by_white_capture_no.pgn", "", "", "", -1, 5, CheckmateOrStalemate.CHECKMATE,
            1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
            UnwinnableQuick.UNWINNABLE, "r2qkbnr/pbpppQ1p/1pn3p1/4Np2/4P3/8/PPPP1PPP/RNB1KB1R b KQkq - 1 6"));
    list.add(new PgnFileTestCase("03_checkmate_by_white_discover_check.pgn", "", "", "", 4, 19,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "r1b2bnr/1ppp1ppp/p2Q4/4p3/4P2k/3P4/PPPBBPP1/RN2K1NR b Q - 19 18"));
    list.add(
        new PgnFileTestCase("04_checkmate_fools_mate_by_white.pgn", "", "", "", -1, 1, CheckmateOrStalemate.CHECKMATE,
            1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
            UnwinnableQuick.UNWINNABLE, "rnbqkbnr/ppppp2p/5p2/6pQ/4P3/2N5/PPPP1PPP/R1B1KBNR b KQkq - 1 3"));
    list.add(
        new PgnFileTestCase("05_checkmate_scholars_mate_by_white.pgn", "", "", "", 7, 4, CheckmateOrStalemate.CHECKMATE,
            1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
            UnwinnableQuick.UNWINNABLE, "r1bqk1nr/pppp1Qpp/2n5/2b1p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 4"));
    return new PgnFileTestCaseList(PgnTest.BASIC_CHECKMATE_VARIOUS_WHITE, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicCheckmateVariousBlack() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("01_checkmate_by_black_capture_yes.pgn", "", "", "", 3, 2,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "rnb1kbnr/1p1p1pp1/p1B4p/2P5/8/P1N1q1PR/1PP1P3/R1BK2q1 w kq - 0 14"));
    list.add(new PgnFileTestCase("02_checkmate_by_black_capture_no.pgn", "", "", "", 5, 10,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "r7/ppp3pp/2N1k3/1B6/3np3/1b3n2/1P1b1PPP/RN1K3R w - - 7 21"));
    list.add(new PgnFileTestCase("03_checkmate_by_black_discover_check.pgn", "", "", "", 6, 3,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "1nbq4/1p1p2pk/7n/1N2pp1P/1p2Pb2/7N/r7/r2KQ2R w - - 0 21"));
    list.add(new PgnFileTestCase("04_checkmate_fools_mate_by_black.pgn", "", "", "", -1, 1,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "rnb1kbnr/pppp1ppp/8/4p3/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 1 3"));
    list.add(new PgnFileTestCase("05_checkmate_scholars_mate_by_black.pgn", "", "", "", 8, 4,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "rnb1k1nr/pppp1ppp/8/2b1p3/2B1P3/2NP4/PPP2qPP/R1BQK1NR w KQkq - 0 5"));
    return new PgnFileTestCaseList(PgnTest.BASIC_CHECKMATE_VARIOUS_BLACK, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicCheckmateDoubleCheckWhite() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_white_double_check_checkmate_rook.pgn", "", "", "", -1, 3,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/2rpp3/1R2k3/7R/8/8/B7/B3K3 b - - 3 101"));
    list.add(new PgnFileTestCase("02_white_double_check_checkmate_knight_orthogonal.pgn", "", "", "", -1, 3,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/4p3/3pk1n1/2N4R/8/8/4R3/4KQ2 b - - 3 101"));
    list.add(new PgnFileTestCase("03_white_double_check_checkmate_knight_diagonal.pgn", "", "", "", -1, 3,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/2pp1p2/2n1pk2/3N3R/8/8/1B4R1/4K3 b - - 3 101"));
    list.add(new PgnFileTestCase("04_white_double_check_checkmate_bishop.pgn", "", "", "", -1, 3,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/3p1pp1/4pkn1/R7/7B/8/8/1B2KR2 b - - 3 101"));

    return new PgnFileTestCaseList(PgnTest.BASIC_CHECKMATE_DOUBLE_CHECK_WHITE, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicCheckmateDoubleCheckBlack() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_black_double_check_checkmate_rook.pgn", "", "", "", -1, 3,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "4k3/8/8/b7/8/8/R6r/1r2K3 w - - 3 102"));
    list.add(new PgnFileTestCase("02_black_double_check_checkmate_knight_orthogonal.pgn", "", "", "", -1, 3,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "3k4/8/4r3/8/8/5n2/R7/3NKBb1 w - - 3 102"));
    list.add(new PgnFileTestCase("03_black_double_check_checkmate_knight_diagonal.pgn", "", "", "", -1, 3,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "b4k2/6b1/3n4/8/4KP2/7r/Q7/8 w - - 3 102"));
    list.add(new PgnFileTestCase("04_black_double_check_checkmate_bishop.pgn", "", "", "", -1, 3,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "1r2k3/8/8/2r5/4n3/8/3b4/2KRN3 w - - 3 102"));

    return new PgnFileTestCaseList(PgnTest.BASIC_CHECKMATE_DOUBLE_CHECK_BLACK, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicStalemate() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("01_white_is_stalemated.pgn", "", "", "", 4, 12, CheckmateOrStalemate.STALEMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "r4knr/p2b1p2/3p4/p1pPp2p/P1PnP1pP/8/2q5/K7 w - - 0 26"));
    list.add(new PgnFileTestCase("02_black_is_stalemated.pgn", "", "", "", 33, 10, CheckmateOrStalemate.STALEMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/5Q2/1p3p1k/pP1p1Pp1/P1pPp1Pp/2P1P2P/8/RNB1KBNR b KQ - 0 24"));
    return new PgnFileTestCaseList(PgnTest.BASIC_STALEMATE, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicInsufficientMaterial() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("insufficient_material_KBwBb_K.pgn", "", "", "", 3, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "6k1/8/3K4/8/8/8/3BB3/8 b - - 0 33"));

    list.add(new PgnFileTestCase("insufficient_material_KBwN_K.pgn", "", "", "", 3, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/6k1/3K4/4N3/8/7B/8/8 w - - 0 36"));
    list.add(new PgnFileTestCase("insufficient_material_KBbN_K.pgn", "", "", "", 3, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/6k1/3K4/8/8/5N2/8/2B5 b - - 0 33"));

    list.add(new PgnFileTestCase("insufficient_material_KBw_K.pgn", "", "", "", 3, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/3K2k1/8/8/8/8/5B2 b - - 0 33"));
    list.add(new PgnFileTestCase("insufficient_material_KBb_K.pgn", "", "", "", 3, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/4k3/8/4K3/8/2B5 w - - 0 35"));

    list.add(new PgnFileTestCase("insufficient_material_KBw_KBb.pgn", "", "", "", 3, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/3b2k1/8/3K2B1/8/8/8 b - - 0 35"));
    list.add(new PgnFileTestCase("insufficient_material_KBb_KBw.pgn", "", "", "", 3, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/k3b3/8/5K2/8/8/2B5 b - - 0 35"));

    list.add(new PgnFileTestCase("insufficient_material_KBw_KBw.pgn", "", "", "", 3, 17, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "5K2/8/8/1B6/8/k7/6b1/8 w - - 0 39"));
    list.add(new PgnFileTestCase("insufficient_material_KBb_KBb.pgn", "", "", "", 3, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/4k3/5b2/3K4/8/2B5 w - - 0 33"));

    list.add(new PgnFileTestCase("insufficient_material_KN_KN.pgn", "", "", "", 3, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/4K3/8/1n6/8/5k1N/8 w - - 0 37"));

    list.add(new PgnFileTestCase("insufficient_material_KNN_K.pgn", "", "", "", 3, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "5k2/8/5N2/2K5/8/5N2/8/8 w - - 0 34"));

    list.add(new PgnFileTestCase("insufficient_material_KN_K.pgn", "", "", "", 3, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "4k3/8/8/2K5/8/5N2/8/8 w - - 0 35"));

    list.add(new PgnFileTestCase("insufficient_material_K_K.pgn", "", "", "", 3, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/5k2/8/2K5/8/8/8/8 w - - 0 37"));

    list.add(new PgnFileTestCase("insufficient_material_K_KBw.pgn", "", "", "", 3, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "2b5/8/2k5/8/4K3/8/8/8 b - - 0 34"));
    list.add(new PgnFileTestCase("insufficient_material_K_KBb.pgn", "", "", "", 3, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/2b5/2K5/8/4k3/8 w - - 0 34"));

    list.add(new PgnFileTestCase("insufficient_material_K_KBwBb.pgn", "", "", "", 3, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "4k3/8/8/4K3/8/b7/4b3/8 b - - 0 34"));

    list.add(new PgnFileTestCase("insufficient_material_K_KBwN.pgn", "", "", "", 3, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "4k3/8/8/2K1n3/8/8/4b3/8 b - - 0 35"));
    list.add(new PgnFileTestCase("insufficient_material_K_KBbN.pgn", "", "", "", 3, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1n2k3/8/8/8/8/b4K2/8/8 b - - 0 33"));

    list.add(new PgnFileTestCase("insufficient_material_K_KN.pgn", "", "", "", 3, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "4k3/8/8/1K2n3/8/8/8/8 b - - 0 36"));

    list.add(new PgnFileTestCase("insufficient_material_K_KNN.pgn", "", "", "", 3, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1n2k3/8/3n4/8/8/8/3K4/8 b - - 0 35"));

    // more than two bishops one side same square and only kings - is insufficient
    // material
    list.add(new PgnFileTestCase("insufficient_material_KBwBw_K.pgn", "", "", "", 7, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "2B5/3B4/8/6K1/8/1k6/8/8 w - - 0 32"));
    list.add(new PgnFileTestCase("insufficient_material_KBbBb_K.pgn", "", "", "", 3, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "5B2/8/2k5/8/8/1K6/8/2B5 b - - 0 36"));
    list.add(new PgnFileTestCase("insufficient_material_K_KBwBw.pgn", "", "", "", 4, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "3k4/1b6/2b5/8/8/8/3K4/8 w - - 0 27"));
    list.add(new PgnFileTestCase("insufficient_material_K_KBbBb.pgn", "", "", "", 4, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "5b2/3k4/8/8/5K2/2b5/8/8 b - - 0 25"));

    // KN against king and single piece
    list.add(new PgnFileTestCase("insufficient_material_KN_KR.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "7r/3k4/8/8/8/8/3K4/1N6 w - - 0 21"));
    list.add(new PgnFileTestCase("insufficient_material_KN_KBw.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/k2b4/8/8/2K5/8/8/1N6 w - - 0 23"));
    list.add(new PgnFileTestCase("insufficient_material_KN_KBb.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "5b2/8/3k4/8/8/4K3/8/1N6 w - - 0 24"));
    list.add(new PgnFileTestCase("insufficient_material_KN_KQ.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "4k3/8/8/8/8/5K2/3q4/1N6 w - - 0 24"));
    list.add(new PgnFileTestCase("insufficient_material_KN_KP.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3k4/8/3p4/8/8/4K3/8/1N6 w - - 0 23"));

    // KBw against king and single piece
    list.add(new PgnFileTestCase("insufficient_material_KBw_KR.pgn", "", "", "", 5, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r7/8/3K4/8/8/2k5/8/5B2 w - - 0 28"));
    list.add(new PgnFileTestCase("insufficient_material_KBw_KN.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1n6/8/8/2k1K3/8/8/8/5B2 w - - 0 26"));
    list.add(new PgnFileTestCase("insufficient_material_KBw_KQ.pgn", "", "", "", 5, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3K4/8/8/4q3/k7/8/8/5B2 b - - 0 30"));
    list.add(new PgnFileTestCase("insufficient_material_KBw_KP.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/2p1K3/8/1k6/8/5B2 w - - 0 27"));

    // KBb against king and single piece
    list.add(new PgnFileTestCase("insufficient_material_KBb_KR.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/r7/k7/8/1K6/2B5 b - - 0 25"));
    list.add(new PgnFileTestCase("insufficient_material_KBb_KN.pgn", "", "", "", 5, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1n6/8/8/8/4K3/1k6/1B6/8 b - - 3 27"));
    list.add(new PgnFileTestCase("insufficient_material_KBb_KQ.pgn", "", "", "", 5, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/2q5/8/2BK4/k7/8 b - - 0 28"));
    list.add(new PgnFileTestCase("insufficient_material_KBb_KP.pgn", "", "", "", 5, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/6B1/8/2p5/8/1k6/8/1K6 b - - 0 26"));

    // King and single piece against KN
    list.add(new PgnFileTestCase("insufficient_material_KR_KN.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1n6/8/8/2k5/4K3/8/7R/8 b - - 0 22"));
    // we have insufficientMaterial_KBw_KN already tested above
    // we have insufficientMaterial_KBb_KN already tested above
    list.add(new PgnFileTestCase("insufficient_material_KQ_KN.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "1n6/8/8/4Q3/k3K3/8/8/8 b - - 0 24"));
    list.add(new PgnFileTestCase("insufficient_material_KQQ_KN.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "1n6/7Q/8/2Q5/8/k2K4/8/8 b - - 0 28"));
    list.add(new PgnFileTestCase("insufficient_material_KP_KN.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1n6/8/8/2k5/8/8/6KP/8 b - - 0 24"));

    // King and single piece against KBw
    list.add(new PgnFileTestCase("insufficient_material_KR_KBw.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "2b5/8/8/8/2k5/8/8/2K4R w - - 0 24"));
    list.add(new PgnFileTestCase("insufficient_material_KR_KBwBw.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "2b5/8/8/1k6/3R4/8/4K3/1b6 b - - 0 26"));
    // we have insufficientMaterial_KN_KBw already tested above
    list.add(new PgnFileTestCase("insufficient_material_KQ_KBw.pgn", "", "", "", 5, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "2b5/2Q5/8/8/8/k7/8/2K5 w - - 0 25"));
    list.add(new PgnFileTestCase("insufficient_material_KQBw_KBw.pgn", "", "", "", 5, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "4B3/8/1k6/8/4b3/2K5/7Q/8 w - - 0 30"));
    list.add(new PgnFileTestCase("insufficient_material_KQQ_KBw.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "3Q4/2Q5/b7/8/k7/8/8/2K5 b - - 0 26"));
    list.add(new PgnFileTestCase("insufficient_material_KQ_KBwBw.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "2b5/8/8/8/k7/8/K7/3b3Q b - - 0 27"));
    list.add(new PgnFileTestCase("insufficient_material_KQQ_KBwBw.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "1Qb5/7Q/8/8/k7/2K5/8/3b4 b - - 0 30"));
    list.add(new PgnFileTestCase("insufficient_material_KP_KBw.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/3P4/k5b1/8/8/2K5 w - - 0 27"));

    // King and single piece against KBb
    list.add(new PgnFileTestCase("insufficient_material_KR_KBb.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/5kb1/8/8/8/3K4/8/7R w - - 0 24"));
    list.add(new PgnFileTestCase("insufficient_material_KR_KBbBb.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "5b2/8/6k1/8/8/R1K5/8/4b3 w - - 0 29"));

    // we have insufficientMaterial_KN_KBb already tested above
    list.add(new PgnFileTestCase("insufficient_material_KQ_KBb.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/7k/4Q3/8/b2K4/8/8 w - - 0 24"));
    list.add(new PgnFileTestCase("insufficient_material_KQBb_KBb.pgn", "", "", "", 5, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "5B2/3Q4/7b/3K3k/8/8/8/8 b - - 0 29"));
    list.add(new PgnFileTestCase("insufficient_material_KQQ_KBb.pgn", "", "", "", 5, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "4Qb2/6k1/8/8/Q7/3K4/8/8 w - - 0 29"));
    list.add(new PgnFileTestCase("insufficient_material_KQ_KBbBb.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "5b2/7k/8/8/K7/1Q6/8/4b3 w - - 0 31"));
    list.add(new PgnFileTestCase("insufficient_material_KQQ_KBbBb.pgn", "", "", "", 5, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "5b2/Q7/8/8/7k/1K6/3Q4/4b3 w - - 0 34"));

    list.add(new PgnFileTestCase("insufficient_material_KP_KBb.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "5b2/8/4P3/3K2k1/8/8/8/8 b - - 0 25"));

    list.add(new PgnFileTestCase("insufficient_material_KBw_KBwQ.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/1b2q3/8/k4K2/8/8/8/5B2 w - - 0 25"));
    list.add(new PgnFileTestCase("insufficient_material_KBw_KBwR.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1b5/8/8/8/1k6/8/3K4/5B2 b - - 0 24"));
    list.add(new PgnFileTestCase("insufficient_material_KBw_KQR.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r7/3B4/8/8/1k3q2/8/8/1K6 b - - 0 24"));
    list.add(new PgnFileTestCase("insufficient_material_KBw_KBwQR.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1b5/8/8/8/1k3q2/8/8/1K3B2 w - - 0 23"));

    list.add(new PgnFileTestCase("insufficient_material_KBb_KBbQ.pgn", "", "", "", 3, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "4kb2/6q1/8/8/8/8/3K4/2B5 w - - 0 28"));
    list.add(new PgnFileTestCase("insufficient_material_KBb_KBbR.pgn", "", "", "", 3, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "4kb2/8/8/8/4r3/8/3K4/2B5 w - - 0 28"));
    list.add(new PgnFileTestCase("insufficient_material_KBb_KQR.pgn", "", "", "", 3, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/3k3r/8/8/5q2/8/3K4/2B5 w - - 0 28"));
    list.add(new PgnFileTestCase("insufficient_material_KBb_KBbQR.pgn", "", "", "", 3, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "4kb2/7r/8/3q4/8/8/3K4/2B5 w - - 0 27"));

    list.add(new PgnFileTestCase("insufficient_material_KBb_KQBb.pgn", "", "", "", 3, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "5b2/5k2/8/4q3/2K5/8/8/2B5 b - - 1 31"));
    list.add(new PgnFileTestCase("insufficient_material_KBb_KQQ.pgn", "", "", "", 3, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/4k3/7q/8/K7/3B1q2/8 b - - 0 32"));
    list.add(new PgnFileTestCase("insufficient_material_KBbBb_KQ.pgn", "", "", "", 3, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "7B/4k3/8/8/8/1q6/3BK3/8 b - - 0 34"));
    list.add(new PgnFileTestCase("insufficient_material_KBbBb_KQQ.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/3q4/8/8/k2B4/B5K1/3q4/8 b - - 0 29"));
    list.add(new PgnFileTestCase("insufficient_material_KBbBb_KR.pgn", "", "", "", 5, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/7B/k7/8/8/1B4r1/4K3 b - - 1 32"));
    list.add(new PgnFileTestCase("insufficient_material_KBbQ_KBb.pgn", "", "", "", 5, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "Q7/8/8/1k6/8/8/3K4/b1B5 w - - 0 28"));
    list.add(new PgnFileTestCase("insufficient_material_KBbQR_KBb.pgn", "", "", "", 5, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "3R3Q/8/7b/8/k7/8/4K3/2B5 b - - 0 31"));
    list.add(new PgnFileTestCase("insufficient_material_KBbR_KBb.pgn", "", "", "", 5, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "3R1b2/8/8/8/k7/8/4K3/2B5 w - - 0 33"));
    list.add(new PgnFileTestCase("insufficient_material_KBw_KQBw.pgn", "", "", "", 5, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/8/5q2/1k1B4/8/1b1K4 w - - 0 31"));
    list.add(new PgnFileTestCase("insufficient_material_KBw_KQQ.pgn", "", "", "", 5, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1q6/8/8/8/5q2/1k6/6K1/5B2 w - - 0 29"));
    list.add(new PgnFileTestCase("insufficient_material_KBwBw_KQ.pgn", "", "", "", 5, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "q3B3/8/8/8/8/1k4K1/8/5B2 b - - 0 34"));
    list.add(new PgnFileTestCase("insufficient_material_KBwBw_KQQ.pgn", "", "", "", 5, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "q1B5/8/4B1q1/8/1k6/8/8/5K2 w - - 0 32"));
    list.add(new PgnFileTestCase("insufficient_material_KBwBw_KR.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1B5/8/8/8/8/2k5/8/3K1B2 b - - 0 32"));
    list.add(new PgnFileTestCase("insufficient_material_KBwQ_KBw.pgn", "", "", "", 5, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/k7/8/6B1/5b2/3Q4/2K5 w - - 0 35"));
    list.add(new PgnFileTestCase("insufficient_material_KBwQR_KBw.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "5R1Q/1b1B4/8/8/2k5/8/8/2K5 b - - 0 38"));
    list.add(new PgnFileTestCase("insufficient_material_KBwR_KBw.pgn", "", "", "", 3, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "1R6/8/3k4/8/5K2/7B/4b3/8 w - - 0 30"));
    list.add(new PgnFileTestCase("insufficient_material_KN_KQQ.pgn", "", "", "", 5, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/3k4/6q1/8/5K2/8/1Nq5 w - - 0 27"));
    list.add(new PgnFileTestCase("insufficient_material_KQR_KBb.pgn", "", "", "", 3, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "1R6/3k4/8/b7/8/8/5K2/1Q6 w - - 0 30"));
    list.add(new PgnFileTestCase("insufficient_material_KQR_KBw.pgn", "", "", "", 3, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "1R6/8/4k3/8/8/3b4/3Q4/4K3 w - - 0 27"));
    return new PgnFileTestCaseList(PgnTest.BASIC_INSUFFICIENT_MATERIAL, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicThreefold() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("threefold_01_1_moves_very_low_one_before_threefold.pgn", "", "", "", -1, 7,
        CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkb1r/pppppppp/5n2/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 7 4"));
    list.add(new PgnFileTestCase("threefold_01_2_moves_very_low_end_with_threefold.pgn", "repPos=3: 2...Ng8 4...Ng8",
        "", "", -1, 8, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 8 5"));
    list.add(new PgnFileTestCase("threefold_02_1_moves_low_one_before_threefold.pgn", "", "", "", -1, 9,
        CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bqk2r/pppp1ppp/2n2n2/2b1p3/4P3/2N2N2/PPPP1PPP/R1BQKB1R b KQkq - 9 7"));
    list.add(new PgnFileTestCase("threefold_02_2_moves_low_end_with_threefold.pgn", "repPos=3: 3...Nc6 5...Bf8 7...Bf8",
        "", "", -1, 10, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bqkb1r/pppp1ppp/2n2n2/4p3/4P3/2N2N2/PPPP1PPP/R1BQKB1R w KQkq - 10 8"));
    list.add(new PgnFileTestCase("threefold_03_1_moves_medium_one_before_threefold.pgn", "", "", "", -1, 14,
        CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bq1rk1/pp1nbppp/2n1p3/1BppP3/3P1P2/2N1BN2/PPP3PP/R2QR1K1 w - - 14 13"));
    list.add(new PgnFileTestCase("threefold_03_2_moves_medium_end_with_threefold.pgn", "repPos=3: 9.O-O 11.Rf1 13.Rf1",
        "", "", -1, 15, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bq1rk1/pp1nbppp/2n1p3/1BppP3/3P1P2/2N1BN2/PPP3PP/R2Q1RK1 b - - 15 13"));
    list.add(new PgnFileTestCase("threefold_04_1_moves_high_one_before_threefold.pgn", "", "", "", -1, 10,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r4rk1/pp1b1pbp/q1n1p1p1/2ppPn2/P2P4/1P3NP1/2P1NPBP/R1BQ1RK1 b - - 10 17"));
    list.add(new PgnFileTestCase("threefold_04_2_moves_high_end_with_threefold.pgn",
        "repPos=3: 13...Bg7 15...Bg7 17...Nfe7", "", "", -1, 11, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r4rk1/pp1bnpbp/q1n1p1p1/2ppP3/P2P4/1P3NP1/2P1NPBP/R1BQ1RK1 w - - 11 18"));
    list.add(new PgnFileTestCase("threefold_05_1_moves_very_high_one_before_threefold.pgn", "", "", "", 23, 11,
        CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "1R4R1/5p1k/4p2p/3NP1pP/3r4/8/5PPK/8 w - - 11 49"));
    list.add(new PgnFileTestCase("threefold_05_2_moves_very_high_end_with_threefold.pgn",
        "repPos=3: 45.Rh8+ 47.Rh8+ 49.Rh8+", "", "", 23, 12, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1R5R/5p1k/4p2p/3NP1pP/3r4/8/5PPK/8 b - - 12 49"));
    list.add(new PgnFileTestCase("threefold_06_1_castling_one_before_threefold.pgn", "", "", "", -1, 9,
        CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkb1r/1ppppppp/p7/8/3Nn3/8/PPPPPPPP/RNBQKB1R b KQk - 9 10"));
    list.add(new PgnFileTestCase("threefold_06_2_castling_end_with_threefold.pgn", "repPos=3: 6...Ra7 8...Ra7 10...Ra7",
        "", "", -1, 10, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "1nbqkb1r/rppppppp/p7/8/3Nn3/8/PPPPPPPP/RNBQKB1R w KQk - 10 11"));
    list.add(new PgnFileTestCase("threefold_07_1_en_passant_capture_one_before_threefold.pgn", "",
        "repPos=3: 2...d5 4...Nb8 6...Nb8", "", -1, 8, CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkb1r/ppp1pppp/5n2/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq - 8 7"));
    list.add(new PgnFileTestCase("threefold_07_2_en_passant_capture_end_with_threefold.pgn",
        "repPos=3: 3.Bc4 5.Bc4 7.Bc4", "repPos=3: 2...d5 4...Nb8 6...Nb8", "", -1, 9, CheckmateOrStalemate.NA, 3,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkb1r/ppp1pppp/5n2/3pP3/2B5/8/PPPP1PPP/RNBQK1NR b KQkq - 9 7"));
    list.add(new PgnFileTestCase("threefold_08_1_en_passant_capture_castling_one_before_threefold.pgn", "",
        "repPos=3: 2...d5 4...Nb8 6...Nb8", "", -1, 16, CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkb1r/ppp1pppp/5n2/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQ - 16 11"));
    list.add(new PgnFileTestCase("threefold_08_2_en_passant_capture_castling_end_with_threefold.pgn",
        "repPos=3: 8...Ke8 10...Ke8 12...Nb8", "repPos=3: 2...d5 4...Nb8 6...Nb8", "", -1, 20, CheckmateOrStalemate.NA,
        3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkb1r/ppp1pppp/5n2/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQ - 20 13"));
    list.add(new PgnFileTestCase("threefold_09_1_two_threefolds_end_with_second.pgn",
        "repPos=3: 2...d5 4...Bc8 6...Bc8; repPos=3: 8.g3 10.Bc1 12.Bc1", "", "", -1, 8, CheckmateOrStalemate.NA, 3,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkbnr/ppp2pp1/8/3pp2p/3PP2P/6P1/PPP2P2/RNBQKBNR b KQkq - 8 12"));
    list.add(new PgnFileTestCase("threefold_09_2_two_threefolds_continue_beyond_second.pgn",
        "repPos=3: 2...d5 4...Bc8 6...Bc8; repPos=3: 8.g3 10.Bc1 12.Bc1", "", "", -1, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bqk1nr/2p3p1/ppnb1p2/3pp1Bp/1P1PP1QP/N1P3P1/P4P2/R3KBNR b KQkq - 1 17"));
    final var threefoldRepetitionMultiple = "repPos=4: 2...d5 4...Bc8 6...Bc8 8...Bc8; repPos=3: 3.Bb5+ 5.Bb5+ 7.Bb5+; repPos=3: 3...Bd7 5...Bd7 7...Bd7; repPos=3: 4.Bf1 6.Bf1 8.Bf1; repPos=4: 10.g3 12.Bc1 14.Bc1 16.Bc1; repPos=3: 10...Bb4+ 12...Bb4+ 14...Bb4+; repPos=3: 11.Bd2 13.Bd2 15.Bd2; repPos=3: 11...Bf8 13...Bf8 15...Bf8";

    // it is intended that both test cases have the same outcome
    list.add(new PgnFileTestCase("threefold_10_1_multiple_threefolds_end_with_last.pgn", threefoldRepetitionMultiple,
        "", "", -1, 12, CheckmateOrStalemate.NA, 4, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/ppp2pp1/8/3pp2p/3PP2P/6P1/PPP2P2/RNBQKBNR b KQkq - 12 16"));
    list.add(new PgnFileTestCase("threefold_10_2_multiple_threefolds_continue_beyond_last.pgn",
        threefoldRepetitionMultiple, "", "", 37, 13, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "2b1kbnr/Q4p2/2pq4/3ppP2/1n1PP3/5B2/1PP1N3/RNBQK1r1 w Qk - 0 30"));
    list.add(new PgnFileTestCase("threefold_11_1_max_position_repetitions_for_threefold_castling.pgn",
        "repPos=5: 19...Rh8 21...Rh8 23...Nb8 25...Nb8 27...Ng8", "", "", -1, 48, CheckmateOrStalemate.NA, 5,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "rnbqkbnr/1ppp1pp1/8/p3p2p/P3P2P/8/1PPP1PP1/RNBQKBNR w - - 48 28"));
    list.add(new PgnFileTestCase("threefold_11_2_max_position_repetitions_for_threefold_both.pgn",
        "repPos=3: 22...Rh8 24...Bc8 26...Nb8", "repPos=3: 4...d5 6...Ng8 8...Bc8", "", -1, 44, CheckmateOrStalemate.NA,
        3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/2p1ppp1/p6p/1p1pP3/8/P6P/1PPP1PP1/RNBQKBNR w - - 44 27"));
    list.add(new PgnFileTestCase("threefold_11_3_max_position_repetitions_for_threefold_both_up_to_fivefold.pgn",
        "repPos=5: 22...Rh8 24...Bc8 26...Nb8 28...Ke8 30...Qd8", "repPos=3: 4...d5 6...Ng8 8...Bc8", "", -1, 52,
        CheckmateOrStalemate.NA, 5, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "rnbqkbnr/2p1ppp1/p6p/1p1pP3/8/P6P/1PPP1PP1/RNBQKBNR w - - 52 31"));

    list.add(new PgnFileTestCase("threefold_max_visual_position_repetition_before_threefold_not_varying.pgn",
        "repPos=3: 40...Bc8 42...Bc8 49...Bc8", "repPos=3: 4...d5 6...Bc8 8...Bc8", "", -1, 90, CheckmateOrStalemate.NA,
        3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/1pp1ppp1/8/p2pP2p/P6P/8/1PPP1PP1/RNBQKBNR w - - 90 50"));
    list.add(new PgnFileTestCase("threefold_max_visual_position_repetition_before_threefold_varying.pgn",
        "repPos=3: 40...Bc8 42...Bc8 49...Bc8", "repPos=3: 4...d5 6...Bc8 8...Bc8", "", -1, 90, CheckmateOrStalemate.NA,
        3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/1pp1ppp1/8/p2pP2p/P6P/8/1PPP1PP1/RNBQKBNR w - - 90 50"));

    list.add(new PgnFileTestCase("threefold_max_visual_position_repetition_before_threefold_not_varying_busted.pgn",
        "repPos=3: 40.Rh1 42.Bf1 49.Bf1; repPos=3: 40...Bc8 42...Bc8 49...Bc8", "repPos=3: 4...d5 6...Bc8 8...Bc8", "",
        -1, 90, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/1pp1ppp1/8/p2pP2p/P6P/8/1PPP1PP1/RNBQKBNR w - - 90 50"));
    list.add(new PgnFileTestCase("threefold_max_visual_position_repetition_before_threefold_varying_busted.pgn",
        "repPos=3: 40.Rh1 42.Qd1 49.Qd1; repPos=3: 40...Bc8 42...Bc8 49...Bc8", "repPos=3: 4...d5 6...Bc8 8...Bc8", "",
        -1, 90, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/1pp1ppp1/8/p2pP2p/P6P/8/1PPP1PP1/RNBQKBNR w - - 90 50"));

    list.add(new PgnFileTestCase("threefold_castling_right_lost_position_allows_castling.pgn",
        "repPos=3: 5...Nb8 7...Nb8 9...Bc5", "", "", -1, 16, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqk2r/pppp1ppp/5n2/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w kq - 16 10"));
    list.add(new PgnFileTestCase("threefold_castling_right_lost_position_disallows_castling.pgn",
        "repPos=3: 3...Nb8 5...Nb8 7...Ng8", "", "", -1, 12, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w kq - 12 8"));
    list.add(new PgnFileTestCase("threefold_castling_white_both_sides_lost.pgn", "repPos=3: 5...Ke8 7...Nb8 9...Nb8",
        "", "", -1, 16, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w - - 16 10"));
    list.add(new PgnFileTestCase("threefold_castling_white_king_side_lost.pgn", "repPos=3: 5...Ng8 7...Ng8 9...Ng8", "",
        "", -1, 16, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/ppppppp1/8/7p/7P/8/PPPPPPP1/RNBQKBNR w Qkq - 16 10"));
    list.add(new PgnFileTestCase("threefold_castling_white_queen_side_lost.pgn", "repPos=3: 5...Nb8 7...Nb8 9...Nb8",
        "", "", -1, 16, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/1ppppppp/8/p7/P7/8/1PPPPPPP/RNBQKBNR w Kkq - 16 10"));
    list.add(new PgnFileTestCase("threefold_two_square_advance_pawn_same_rank_no_1_one_before_threefold.pgn", "", "",
        "", -1, 7, CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkb1r/pp2pppp/8/2pp4/4n3/2N1PN2/PPPP1PPP/R1BQKB1R b KQkq - 7 7"));
    list.add(new PgnFileTestCase("threefold_two_square_advance_pawn_same_rank_no_2_end_with_threefold.pgn",
        "repPos=3: 3...d5 5...Nf6 7...Nf6", "", "", -1, 8, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkb1r/pp2pppp/5n2/2pp4/8/2N1PN2/PPPP1PPP/R1BQKB1R w KQkq - 8 8"));
    list.add(new PgnFileTestCase("threefold_two_square_advance_pawn_same_rank_no_3_beyond_threefold.pgn",
        "repPos=3: 3...d5 5...Nf6 7...Nf6", "", "", -1, 8, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rn1qkb1r/pp1bppp1/5n2/1Bpp3p/P7/2N1PN2/1PPP1PPP/R1BQK2R w KQkq - 2 10"));

    list.add(new PgnFileTestCase("threefold_two_square_advance_pawn_same_rank_yes_en_passant_capture_possible_yes.pgn",
        "repPos=3: 4...Ng8 6...Ng8 8...Bc8", "repPos=4: 2...d5 4...Ng8 6...Ng8 8...Bc8", "", -1, 12,
        CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bqkbnr/ppp1pppp/2n5/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq - 12 9"));

    list.add(new PgnFileTestCase("threefold_en_passant_capture_exposing_own_king_to_check_constructed.pgn",
        "repPos=6: 5...d5 7...Nc6 9...Nf6 11...Nf6 13...Bc8 15...Qe7", "", "", 5, 20, CheckmateOrStalemate.NA, 6,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "r1b1kb1r/ppp1qppp/2n2n2/3pP3/8/5N2/PPPP1PPP/RNBQKB1R w KQkq - 20 16"));
    list.add(new PgnFileTestCase("threefold_en_passant_capture_leaving_own_king_in_check_constructed.pgn",
        "repPos=3: 6...d5+ 8...Qe8+ 10...Qe8+", "", "", -1, 8, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnb1qbnr/ppp1kppp/4p3/1K1pP3/8/8/PPPP1PPP/RNBQ1BNR w - - 8 11"));

    list.add(new PgnFileTestCase("threefold_en_passant_capture_exposing_own_king_to_check_chess.com.pgn",
        "repPos=4: 32.f4 34.Kg1 36.Kg1 38.Kg1", "", "", 12, 12, CheckmateOrStalemate.NA, 4, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "6k1/8/8/8/5Pp1/8/6R1/6K1 b - - 12 38"));
    list.add(new PgnFileTestCase("threefold_en_passant_capture_leaving_own_king_in_check_chess.com.pgn",
        "repPos=4: 37.d4+ 39.Ra2+ 41.Ra2+ 43.Ra2+", "", "", 4, 12, CheckmateOrStalemate.NA, 4,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/8/3Pp3/8/R6k/K7 b - - 12 43"));

    list.add(new PgnFileTestCase("threefold_two_square_advance_all_cases_both_sides_one_half_move_before_threefold.pgn",
        "", "repPos=3: 13...a5 15...Nc6 17...Nc6", "", 47, 14, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1b4r/1ppqnkpn/8/pPbpP2p/1BP2pP1/3P3N/P1N2Q1P/R3KB1R b KQ - 10 43"));

    return new PgnFileTestCaseList(PgnTest.BASIC_THREEFOLD, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicThreefoldInitialEnPassantCapture() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("initial_ep_two_repetitions_end_with_yes.pgn", "", "", "", -1, 4,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkb1r/ppp1pppp/5n2/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq - 4 5"));
    list.add(new PgnFileTestCase("initial_ep_two_repetitions_end_with_no.pgn", "", "", "", -1, 6,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bqkb1r/ppp1pppp/2n2n2/3pP3/8/2N5/PPPP1PPP/R1BQKBNR w KQkq - 6 6"));

    list.add(new PgnFileTestCase("initial_ep_three_repetitions_end_with_yes.pgn", "",
        "repPos=3: 2...d5 4...Bc8 8...Nb8", "", -1, 12, CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkb1r/ppp1pppp/5n2/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq - 12 9"));
    list.add(new PgnFileTestCase("initial_ep_three_repetitions_end_with_no.pgn", "", "repPos=3: 2...d5 4...Bc8 8...Nb8",
        "", -1, 14, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnb1kb1r/pppqpppp/5n2/3pP3/8/5Q2/PPPP1PPP/RNB1KBNR w KQkq - 14 10"));

    list.add(new PgnFileTestCase("initial_ep_two_three_repetitions_end_with_yes.pgn", "",
        "repPos=3: 2...d5 4...Bc8 8...Nb8; repPos=3: 16.b4 18.Bf1 20.Nb1", "", -1, 22, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkbnr/1pp1pppp/8/3pP3/pP4P1/7P/P1PP1P2/RNBQKBNR b kq - 8 20"));

    list.add(new PgnFileTestCase("initial_ep_two_three_repetitions_end_with_no.pgn", "",
        "repPos=3: 2...d5 4...Bc8 8...Nb8; repPos=3: 16.b4 18.Bf1 20.Nb1", "", 40, 22, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rn1qkbnr/1pp1pppp/8/3pP3/pP4P1/8/P1PP1P2/RNBQKBNR b kq - 0 21"));

    return new PgnFileTestCaseList(PgnTest.BASIC_THREEFOLD_INITIAL_EP, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicFifty() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("fifty_01_1_one_before_fifty.pgn", "", "", "", -1, 99, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1r1nk2r/2pb1pp1/1p5Q/p2ppnbp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 b - - 99 56"));
    list.add(new PgnFileTestCase("fifty_01_2_end_with_fifty.pgn", "", "", "7.Ba6 (1) 56...Nd6 (100)", -1, 100,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1nk2r/2pb1pp1/1p1n3Q/p2pp1bp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 w - - 100 57"));
    list.add(new PgnFileTestCase("fifty_02_1_last_move_checkmate_by_white_with_capture.pgn", "", "", "", 6, 99,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "1rbqkb1r/pp1p1Qpp/2p5/3Bp3/PR1NP3/Bn2N3/2PP1PPP/5RK1 b k - 0 57"));
    list.add(new PgnFileTestCase("fifty_02_2_last_move_checkmate_by_black_with_capture.pgn", "", "", "", 3, 99,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "4rrk1/pppbnppp/8/8/1n1N4/4bN2/PP1PPqPP/R1BQKB1R w - - 0 54"));
    list.add(new PgnFileTestCase("fifty_03_1_last_move_checkmate_by_white_without_capture.pgn", "", "",
        "7...Rb8 (1) 57.Qe7# (100)", 6, 100, CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "1rbnk2r/pp1pQppp/1bp5/q2Bp3/PBRNP3/4N3/2PP1PPP/2R3K1 b k - 100 57"));
    list.add(new PgnFileTestCase("fifty_03_2_last_move_checkmate_by_black_without_capture.pgn", "", "",
        "4.Qc2 (1) 53...Qd1# (100)", 3, 100, CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "rk6/ppp1Nppp/3r1n2/8/bn1Q4/4bN2/PP1PPPPP/R1BqKB1R w KQ - 100 54"));
    list.add(new PgnFileTestCase("fifty_04_1_last_move_white_is_stalemated.pgn", "", "", "24.Ke3 (1) 73...Qc2 (100)", 5,
        100, CheckmateOrStalemate.STALEMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/8/1p1p4/p1pPp1p1/P1P1P3/8/2q4r/K2k4 w - - 100 74"));
    list.add(new PgnFileTestCase("fifty_04_2_last_move_black_is_stalemated.pgn", "", "", "32...Kb8 (1) 82.Qb6 (100)", 3,
        100, CheckmateOrStalemate.STALEMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "k7/2R5/1Q3N2/5pP1/5P2/8/PPP1P3/K7 b - - 100 82"));
    list.add(new PgnFileTestCase("fifty_05_bishop_walk.pgn", "", "", "3.Na3 (1) 52...Nb3 (100) 57...Bc8 (110)", -1, 110,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bqkbnr/ppp2ppp/1N6/3pp3/3PP3/1n6/PPP2PPP/R1BQKBNR w KQkq - 110 58"));
    list.add(new PgnFileTestCase("fifty_06_01_such_49_moves_can_claim.pgn", "", "", "", -1, 99, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r2qkbnr/pppb1ppp/1N6/n2pp1Q1/3PPB2/8/PPP2PPP/R3KBNR b KQkq - 99 52"));
    list.add(new PgnFileTestCase("fifty_06_02_such_49_moves_cannot_claim_forced_capture.pgn", "", "", "", -1, 99,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r2qkbnr/pppbQppp/1N6/n2pp3/3PP3/4B3/PPP2PPP/R3KBNR b KQkq - 99 52"));
    list.add(new PgnFileTestCase("fifty_06_03_such_49_moves_cannot_claim_forced_pawn_move.pgn", "", "", "", -1, 99,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "1rb3n1/pppq1pp1/2n1r3/2bpp2p/3PP1Qk/4K2N/PPP2PPP/RNBB3R b - - 99 53"));
    return new PgnFileTestCaseList(PgnTest.BASIC_FIFTY, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicFivefold() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("fivefold_01_1_one_before_fivefold.pgn", "repPos=4: 5...a6 7...Qd8 9...Qd8 11...Qd8",
        "", "", -1, 12, CheckmateOrStalemate.NA, 4, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bqk1nr/1pp2ppp/p1np4/2b1p3/2B1P3/3P1N1P/PPP2PP1/RNBQK2R w KQkq - 12 12"));
    list.add(new PgnFileTestCase("fivefold_01_2_end_with_fivefold.pgn",
        "repPos=5: 5...a6 7...Qd8 9...Qd8 11...Qd8 13...Nc6", "", "", -1, 16, CheckmateOrStalemate.NA, 5,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "r1bqk1nr/1pp2ppp/p1np4/2b1p3/2B1P3/3P1N1P/PPP2PP1/RNBQK2R w KQkq - 16 14"));
    list.add(new PgnFileTestCase("fivefold_02_1_one_before_fivefold.pgn",
        "repPos=4: 5...a6 7...Qd8 9...Qd8 11...Qd8; repPos=4: 6.Bg5 8.Bg5 10.Bg5 12.Bg5; repPos=4: 6...Qd7 8...Qd7 10...Qd7 12...Qd7; repPos=4: 7.Bc1 9.Bc1 11.Bc1 13.Bc1",
        "", "", -1, 15, CheckmateOrStalemate.NA, 4, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1b1k1nr/1ppq1ppp/p1np4/2b1p3/2B1P3/3P1N1P/PPP2PP1/RNBQK2R b KQkq - 15 13"));
    final var dyamicPositionRepetition2And3 = "repPos=5: 5...a6 7...Qd8 9...Qd8 11...Qd8 13...Qd8; repPos=4: 6.Bg5 8.Bg5 10.Bg5 12.Bg5; repPos=4: 6...Qd7 8...Qd7 10...Qd7 12...Qd7; repPos=4: 7.Bc1 9.Bc1 11.Bc1 13.Bc1";
    list.add(new PgnFileTestCase("fivefold_02_2_end_with_fivefold.pgn", dyamicPositionRepetition2And3, "", "", -1, 16,
        CheckmateOrStalemate.NA, 5, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "r1bqk1nr/1pp2ppp/p1np4/2b1p3/2B1P3/3P1N1P/PPP2PP1/RNBQK2R w KQkq - 16 14"));
    // here we make a few more moves away from fold repetition and expect the same
    // result
    list.add(new PgnFileTestCase("fivefold_03_1_continue_previous_without_further_repetitions.pgn",
        dyamicPositionRepetition2And3, "", "", -1, 16, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bqk1nr/1pp2p1p/pbnp2p1/4p3/1PB1P3/P2P1N1P/2P2PP1/RNBQK2R w KQkq - 1 16"));
    // here we go to sixfold
    list.add(new PgnFileTestCase("fivefold_03_2_continue_previous_and_end_with_sixfold.pgn",
        "repPos=6: 5...a6 7...Qd8 9...Qd8 11...Qd8 13...Qd8 15...Qd8; repPos=5: 6.Bg5 8.Bg5 10.Bg5 12.Bg5 14.Bg5; repPos=5: 6...Qd7 8...Qd7 10...Qd7 12...Qd7 14...Qd7; repPos=5: 7.Bc1 9.Bc1 11.Bc1 13.Bc1 15.Bc1",
        "", "", -1, 20, CheckmateOrStalemate.NA, 6, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "r1bqk1nr/1pp2ppp/p1np4/2b1p3/2B1P3/3P1N1P/PPP2PP1/RNBQK2R w KQkq - 20 16"));
    return new PgnFileTestCaseList(PgnTest.BASIC_FIVEFOLD, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicSeventyFive() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("seventy_five_01_1_one_before_seventy_five.pgn", "", "",
        "7.Ba6 (1) 56...Nd6 (100) 81.Rb3 (149)", -1, 149, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.WINNABLE,
        "1r1n2r1/2pk1ppR/1pbn4/p2pp2p/P1B1P2P/1RKP1Q1N/1PP1NPP1/1qB3b1 b - - 149 81"));
    list.add(new PgnFileTestCase("seventy_five_01_2_end_with_seventy_five.pgn", "", "",
        "7.Ba6 (1) 56...Nd6 (100) 81...Kc8 (150)", -1, 150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "1rkn2r1/2p2ppR/1pbn4/p2pp2p/P1B1P2P/1RKP1Q1N/1PP1NPP1/1qB3b1 w - - 150 82"));
    list.add(new PgnFileTestCase("seventy_five_01_3_beyond_seventy_five.pgn", "", "",
        "7.Ba6 (1) 56...Nd6 (100) 81...Kc8 (150) 83...Ka7 (154)", -1, 154, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1r1n2r1/k1p2ppR/1pbn4/pR1pp2p/P1B1P2P/1K1P1Q1N/1PP1NPP1/1qB3b1 w - - 154 84"));
    list.add(new PgnFileTestCase("seventy_five_02_1_beyond_seventy_five.pgn", "", "",
        "7.Ba6 (1) 56...Nd6 (100) 81...Kc8 (150) 83...Ka7 (154)", 167, 154, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3nrr2/kqp2pp1/1pb1BRn1/pQ2p1Np/P2RPB1P/3P1K2/1PP1NPPb/8 w - - 99 134"));
    list.add(new PgnFileTestCase("seventy_five_02_2_beyond_seventy_five_end_with_new_fifty.pgn", "", "",
        "7.Ba6 (1) 56...Nd6 (100) 81...Kc8 (150) 83...Ka7 (154); 84...Rf8 (1) 134.Qd5 (100)", 167, 154,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "3nrr2/kqp2pp1/1pb1BRn1/p2Qp1Np/P2RPB1P/3P1K2/1PP1NPPb/8 b - - 100 134"));
    list.add(new PgnFileTestCase("seventy_five_02_3_beyond_seventy_five_beyond_new_fifty.pgn", "", "",
        "7.Ba6 (1) 56...Nd6 (100) 81...Kc8 (150) 83...Ka7 (154); 84...Rf8 (1) 134.Qd5 (100) 142...Qd2 (117)", 167, 154,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "2Qnrr2/k1pB1ppN/1p2R1n1/p2bp2p/P2RPB1P/3P1K2/1PPqNPPb/8 w - - 117 143"));
    list.add(new PgnFileTestCase("seventy_five_02_4_beyond_seventy_five_one_before_new_seventy_five.pgn", "", "",
        "7.Ba6 (1) 56...Nd6 (100) 81...Kc8 (150) 83...Ka7 (154); 84...Rf8 (1) 134.Qd5 (100) 158...Ne8 (149)", 167, 154,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r3n2Q/k1p1rpp1/1p2RB2/p2bp1Np/P2RPn1P/3P1K2/1PPN1PPb/1B4q1 w - - 149 159"));
    list.add(new PgnFileTestCase("seventy_five_02_5_beyond_seventy_five_end_with_new_seventy_five.pgn", "", "",
        "7.Ba6 (1) 56...Nd6 (100) 81...Kc8 (150) 83...Ka7 (154); 84...Rf8 (1) 134.Qd5 (100) 159.Qh6 (150)", 167, 154,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "r3n3/k1p1rpp1/1p2RB1Q/p2bp1Np/P2RPn1P/3P1K2/1PPN1PPb/1B4q1 b - - 150 159"));
    list.add(new PgnFileTestCase(
        "seventy_five_02_6_beyond_seventy_five_end_beyond_new_seventy_five_with_yawn_moves.pgn", "", "",
        "7.Ba6 (1) 56...Nd6 (100) 81...Kc8 (150) 83...Ka7 (154); 84...Rf8 (1) 134.Qd5 (100) 159.Qh6 (150) 173.Re7 (178)",
        167, 178, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "r2r1B1Q/1kp1Rpp1/1p3n2/p3p1Np/P2RPn1P/1b1P1K2/1PPN1PPb/1B2q3 b - - 178 173"));
    list.add(new PgnFileTestCase(
        "seventy_five_02_7_beyond_seventy_five_end_beyond_new_seventy_five_with_yawn_moves_than_non_yawn_moves.pgn", "",
        "",
        "7.Ba6 (1) 56...Nd6 (100) 81...Kc8 (150) 83...Ka7 (154); 84...Rf8 (1) 134.Qd5 (100) 159.Qh6 (150) 173.Re7 (178)",
        167, 178, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rn2RB2/2p2ppQ/kp1N2n1/p3p2p/P1r1P2P/1b1P1K1N/1PP2PPb/1B2q3 w - - 10 179"));
    list.add(new PgnFileTestCase("seventy_five_03_1_last_move_checkmate_by_white_with_capture.pgn", "", "",
        "4...Be7 (1) 54.Nf6 (100) 78...Qc4 (149)", 5, 149, CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "5rrk/ppp2ppQ/3n1N2/2bbp3/n1qPB3/B1P5/PPN2PPP/1KR4R b - - 0 79"));
    list.add(new PgnFileTestCase("seventy_five_03_2_last_move_checkmate_by_black_with_capture.pgn", "", "",
        "4.Kd2 (1) 53...Qe6 (100) 78.Nb6 (149)", 156, 149, CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "2b4r/ppp1ppb1/kN1r3p/3p2p1/P2P2n1/3n1QP1/1PPBPP1q/R3NBRK w - - 0 79"));
    list.add(new PgnFileTestCase("seventy_five_04_1_last_move_checkmate_by_white_without_capture.pgn", "", "",
        "4...Be7 (1) 54.Nf6 (100) 79.Qe8# (150)", 5, 150, CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "4Q1k1/ppp2ppp/1b4rr/1n1bp3/1BqPB1N1/2P3n1/PPN2PPP/1KR4R b - - 150 79"));
    list.add(new PgnFileTestCase("seventy_five_04_2_last_move_checkmate_by_black_without_capture.pgn", "", "",
        "4.Kd2 (1) 53...Qe6 (100) 78...Qg4# (150)", -1, 150, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "3r1n1b/pppbpp2/k1r4p/3pQ1pK/Pn1P2q1/RNN3P1/1PPBPPBP/6R1 w - - 150 79"));
    list.add(new PgnFileTestCase("seventy_five_05_1_last_move_white_is_stalemated.pgn", "", "",
        "22.Kg2 (1) 71...Qd3 (100) 96...Qf2 (150)", 6, 150, CheckmateOrStalemate.STALEMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1r3n1r/kbp1n2p/1p2p2b/pP1pP1p1/P2P1pP1/5P2/5q2/7K w - - 150 97"));
    list.add(new PgnFileTestCase("seventy_five_05_2_last_move_black_is_stalemated.pgn", "", "",
        "27...Kf6 (1) 77.Kc6 (100) 102.Qh5 (150)", 5, 150, CheckmateOrStalemate.STALEMATE, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/4K3/1NB4P/p1pp3Q/PpP1k1p1/1P3pP1/1B3P2/3R2NR b - - 150 102"));
    return new PgnFileTestCaseList(PgnTest.BASIC_SEVENTY_FIVE, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicIntervening() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("intervening_threefold_encapsulating_threefold.pgn",
        "repPos=3: 1...e5 3...Qd8 9...Ng8; repPos=3: 4...Nh6 6...Nh6 8...Nh6", "", "", -1, 16, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/1ppp1ppp/p7/4p3/4P3/P7/1PPP1PPP/RNBQKBNR w KQkq - 0 11"));

    list.add(new PgnFileTestCase("intervening_threefold_encapsulating_fivefold.pgn",
        "repPos=3: 2...c5 4...Qd8 16...Bc8; repPos=5: 6...Be4 8...Be4 10...Be4 12...Be4 14...Be4", "", "", -1, 30,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bqkbnr/p2ppppp/2n5/1pp5/1PP5/2N5/P2PPPPP/R1BQKBNR w KQkq - 30 18"));

    list.add(new PgnFileTestCase("intervening_fivefold_encapsulating_threefold.pgn",
        "repPos=5: 2...g5 4...Ng8 6...Ng8 8...Bf8 16...Nb8; repPos=3: 10...Ne5 12...Ne5 14...Ne5", "", "", -1, 30,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqk1nr/ppppp2p/7b/5pp1/5PP1/7B/PPPPP2P/RNBQK1NR w KQkq - 30 18"));

    list.add(new PgnFileTestCase("intervening_fivefold_encapsulating_fivefold.pgn",
        "repPos=5: 2...e6 4...Bf8 6...Bf8 8...Bf8 18...Qd8; repPos=5: 9...Qb6 11...Qb6 13...Qb6 15...Qb6 17...Qb6", "",
        "", -1, 34, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkb1r/pp1p1ppp/2p1pn2/8/8/2P1PN2/PP1P1PPP/RNBQKB1R w KQkq - 34 20"));

    list.add(new PgnFileTestCase("intervening_threefold_interlocked_threefold.pgn",
        "repPos=3: 2...g6 4...Bf8 8...Bf8; repPos=3: 5...Bh6 7...Bh6 9...Bh6", "", "", -1, 16, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bqk1nr/p1pppp1p/1pn3pb/8/8/1PN3PB/P1PPPP1P/R1BQK1NR w KQkq - 16 11"));

    list.add(new PgnFileTestCase("intervening_threefold_interlocked_fivefold.pgn",
        "repPos=3: 2...e5 4...Bc8 12...Bf8; repPos=5: 5...Bc5 7...Bc5 9...Bc5 11...Bc5 14...Bc5", "", "", -1, 26,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqk2r/p1pp1ppp/5n2/1pb1p3/1PB1P3/5N2/P1PP1PPP/RNBQK2R w KQkq - 26 16"));

    list.add(new PgnFileTestCase("intervening_fivefold_interlocked_threefold.pgn",
        "repPos=5: 2...e5 4...Qd8 6...Qd8 8...Qd8 16...Bc8; repPos=3: 10...Ng4 12...Ng4 20...Bc8", "", "", -1, 38,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bqkb1r/ppp2ppp/2n5/3pp1N1/3PP1n1/2N5/PPP2PPP/R1BQKB1R w KQkq - 38 22"));

    list.add(new PgnFileTestCase("intervening_fivefold_interlocked_fivefold.pgn",
        "repPos=5: 3...d5 5...Qd8 7...Qd8 9...Bc8 17...Bc8; repPos=5: 10...Ba6 12...Ba6 14...Ba6 16...Ba6 22...Nb8", "",
        "", -1, 40, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnq1kbnr/p1p1pp1p/bp4p1/3p4/3P4/BP4P1/P1P1PP1P/RNQ1KBNR w KQkq - 40 24"));

    return new PgnFileTestCaseList(PgnTest.BASIC_INTERVENING, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicDoubleDraw() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("threefold_and_fifty_move.pgn", "repPos=3: 4...g6 6...Bc8 54...Qd8", "",
        "5.Bb2 (1) 54...Qd8 (100)", -1, 100, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkbnr/p1p2p1p/1p4p1/3pp3/3PP3/1P4P1/P1P2P1P/RNBQKBNR w KQkq - 100 55"));
    list.add(new PgnFileTestCase("threefold_and_seventy_five_move.pgn", "repPos=3: 4...g6 6...Bc8 79...Qd8", "",
        "5.Bb2 (1) 54...Nf6 (100) 79...Qd8 (150)", -1, 150, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "rnbqkbnr/p1p2p1p/1p4p1/3pp3/3PP3/1P4P1/P1P2P1P/RNBQKBNR w KQkq - 150 80"));
    list.add(new PgnFileTestCase("fivefold_and_fifty_move.pgn", "repPos=5: 4...g6 6...Bc8 49...Qd8 51...Qd8 54...Qd8",
        "", "5.Bb2 (1) 54...Qd8 (100)", -1, 100, CheckmateOrStalemate.NA, 5, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "rnbqkbnr/p1p2p1p/1p4p1/3pp3/3PP3/1P4P1/P1P2P1P/RNBQKBNR w KQkq - 100 55"));
    list.add(
        new PgnFileTestCase("fivefold_and_seventy_five_move.pgn", "repPos=5: 4...g6 6...Bc8 71...Qd8 73...Nb8 79...Bf8",
            "", "5.Bb2 (1) 54...Nf6 (100) 79...Bf8 (150)", -1, 150, CheckmateOrStalemate.NA, 5,
            InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
            UnwinnableQuick.UNWINNABLE, "rnbqkbnr/p1p2p1p/1p4p1/3pp3/3PP3/1P4P1/P1P2P1P/RNBQKBNR w KQkq - 150 80"));

    return new PgnFileTestCaseList(PgnTest.BASIC_DOUBLE_DRAW, list);

  }

  private static PgnFileTestCaseList createTestCasesBasicCastlingSpecialWhite() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_white_kingside_check.pgn", "", "", "", 6, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbq1b1r/pppppkpp/8/PB5n/4p3/7N/1PPP2PP/RNBQ1RK1 b - - 2 8"));
    list.add(new PgnFileTestCase("02_white_kingside_checkmate.pgn", "", "", "", 30, 17, CheckmateOrStalemate.CHECKMATE,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "rnbq1bnr/pppp1ppp/8/Q7/3PP3/1PBB1N1N/P1P2PPP/1k3RK1 b - - 6 21"));
    list.add(new PgnFileTestCase("03_white_kingside_fifty_move.pgn", "", "", "18...Nec6 (1) 68.O-O (100)", 3, 100,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "2Qqb3/5k1r/2B3p1/2NPBp2/P3n1p1/N7/1n5r/R4RK1 b - - 100 68"));
    list.add(new PgnFileTestCase("04_white_kingside_seventy_five_move.pgn", "", "",
        "18...Nec6 (1) 68.Nc4 (100) 93.O-O (150)", 3, 150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "B7/1bn1k1r1/4N1p1/3PBp2/P1N3p1/2q5/1n1Q3r/R4RK1 b - - 150 93"));
    list.add(new PgnFileTestCase("05_white_kingside_stalemate.pgn", "", "", "", 3, 7, CheckmateOrStalemate.STALEMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/p5N1/P1PP4/R2Q2P1/1k3PBP/5RK1 b - - 6 36"));
    list.add(new PgnFileTestCase("06_white_queenside_check.pgn", "", "", "", 8, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbq3r/pppk1pp1/4pn1p/7Q/5B2/5N2/PPP1PPPP/2KR1BNR b - - 3 8"));
    list.add(new PgnFileTestCase("07_white_queenside_checkmate.pgn", "", "", "", 16, 6, CheckmateOrStalemate.CHECKMATE,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "rnb1qbnr/ppppp1pp/5p2/1B4N1/3PPBPP/2N2P2/PPPQ4/2KR3k b - - 2 14"));
    list.add(new PgnFileTestCase("08_white_queenside_fifty_move.pgn", "", "", "16...Kf7 (1) 66.O-O-O (100)", 7, 100,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "2b4Q/2qnk3/1Q3r2/p1p3b1/PpP2Pp1/1N2PNPp/1B2B2P/2KR3R b - - 100 66"));
    list.add(new PgnFileTestCase("09_white_queenside_seventy_five_move.pgn", "", "",
        "16...Kf7 (1) 66.Qh7+ (100) 91.O-O-O (150)", 7, 150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "6Q1/3bk1r1/1q3n2/p1p1b1Q1/PpP2Pp1/1N2PNPp/1B2B2P/2KR3R b - - 150 91"));
    list.add(new PgnFileTestCase("10_white_queenside_stalemate.pgn", "", "", "", 18, 7, CheckmateOrStalemate.STALEMATE,
        1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/4B1P1/pP6/P1p1P1PP/2N2Q2/1BPP3k/2KR4 b - - 2 35"));

    return new PgnFileTestCaseList(PgnTest.BASIC_CASTLING_SPECIAL_WHITE, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicCastlingSpecialBlack() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_black_kingside_check.pgn", "", "", "", 3, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnbq1rk1/ppppb2p/7n/8/8/8/PPPPPKPP/RNBQ1BNR w - - 2 6"));
    list.add(new PgnFileTestCase("02_black_kingside_checkmate.pgn", "", "", "", 15, 6, CheckmateOrStalemate.CHECKMATE,
        1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "K4rk1/p1pppp1p/6p1/np1n4/3b4/8/PPP1PPPP/RNBQ1BNR w - - 2 15"));
    list.add(new PgnFileTestCase("03_black_kingside_fifty_move.pgn", "", "", "17.Rg5 (1) 66...O-O (100)", 5, 100,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "5rk1/1Q2n3/R6p/8/1b1B1p1K/1P6/P1P5/RN1Q1BNb w - - 100 67"));
    list.add(new PgnFileTestCase("04_black_kingside_seventy_five_move.pgn", "", "",
        "17.Rg5 (1) 66...Bc3 (100) 91...O-O (150)", 5, 150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "5rk1/1Q2n3/R6p/1N6/3B1p1K/1Pb2b2/P1P5/R2Q1BN1 w - - 150 92"));
    list.add(new PgnFileTestCase("05_black_kingside_stalemate.pgn", "", "", "", 6, 4, CheckmateOrStalemate.STALEMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "5rk1/K2n1pb1/2qp3p/pp2p1pP/6P1/8/8/8 w - - 2 31"));
    list.add(new PgnFileTestCase("06_black_queenside_check.pgn", "", "", "", 3, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "2kr1bnr/ppq2pp1/2n4p/5b2/5B2/2N5/PPPKPPPP/R2Q1BNR w - - 2 8"));
    list.add(new PgnFileTestCase("07_black_queenside_checkmate.pgn", "", "", "", 15, 3, CheckmateOrStalemate.CHECKMATE,
        1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "2kr3K/pbppq2p/1pn2p2/4p3/5Pn1/8/PPPPP1PP/RNBQ1BNR w - - 1 12"));
    list.add(new PgnFileTestCase("08_black_queenside_fifty_move.pgn", "", "", "10.Nh3 (1) 59...O-O-O (100)", 3, 100,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "2kr3r/q2b1p1p/2n2npB/p7/4P3/bQN3PN/1P3P1P/R3KB1R w K - 100 60"));
    list.add(new PgnFileTestCase("09_black_queenside_seventy_five_move.pgn", "", "",
        "10.Nh3 (1) 59...Rg8 (100) 84...O-O-O (150)", 3, 150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "2kr4/3b1prp/1qn2npB/p7/4P3/b1N3PN/QP3PBP/R3K1R1 w - - 150 85"));
    list.add(new PgnFileTestCase("10_black_queenside_stalemate.pgn", "", "", "", 4, 15, CheckmateOrStalemate.STALEMATE,
        1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "2kr4/1bpp3K/1p3q2/pP2pP2/P5n1/3n4/8/8 w - - 15 42"));

    return new PgnFileTestCaseList(PgnTest.BASIC_CASTLING_SPECIAL_BLACK, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicForced() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("forced_checkmate.pgn", "", "", "", 9, 16, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "7k/q5Q1/p4PPK/6PP/8/5P2/8/8 b - - 16 40"));
    list.add(new PgnFileTestCase("forced_checkmate_played.pgn", "", "", "", 9, 16, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "6k1/5PP1/6PK/6PP/8/p7/8/8 b - - 0 45"));

    list.add(new PgnFileTestCase("forced_insufficient_material.pgn", "", "", "", 5, 23, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "r7/K7/2k5/8/8/8/8/8 w - - 11 43"));
    list.add(new PgnFileTestCase("forced_insufficient_material_played.pgn", "", "", "", 5, 23, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "K7/8/2k5/8/8/8/8/8 b - - 0 43"));

    list.add(new PgnFileTestCase("forced_stalemate_01.pgn", "", "", "", 5, 23, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1r4k1/6Q1/8/8/8/q7/P7/K7 b - - 4 36"));
    list.add(new PgnFileTestCase("forced_stalemate_01_played.pgn", "", "", "", 5, 23, CheckmateOrStalemate.STALEMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1r6/6k1/8/8/8/q7/P7/K7 w - - 0 37"));

    list.add(new PgnFileTestCase("forced_stalemate_02.pgn", "", "", "", 8, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "6kr/1p1N1p1p/1P3P1P/P6P/3q1RQr/7p/4pN1P/4B1KR b - - 2 41"));
    list.add(new PgnFileTestCase("forced_stalemate_02_played.pgn", "", "", "", 8, 8, CheckmateOrStalemate.STALEMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "6kr/1p1N1p1p/1P3P1P/P6P/6N1/7p/4p2P/4B1KR b - - 0 43"));

    return new PgnFileTestCaseList(PgnTest.BASIC_FORCED, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicFromFen() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("capture_first_move_half_move_clock_100_black_to_move.pgn", "", "",
        "50...NA (1) 100.NA (100)", 1, 100, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "k7/2R1Q3/5N2/5pP1/5P2/8/PPP1P3/K7 w - - 0 101"));
    list.add(new PgnFileTestCase("capture_first_move_half_move_clock_100_white_to_move.pgn", "", "",
        "50.NA (1) 99...NA (100)", 1, 100, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1r1nk2r/2pb1pp1/1p1n3Q/p2pp1Np/P3P2P/1R1PR3/1PP2PP1/qBB1K1N1 b - - 0 100"));
    list.add(new PgnFileTestCase("capture_first_move_half_move_clock_101_black_to_move.pgn", "", "",
        "50.NA (1) 99...NA (100) 100.NA (101)", 1, 101, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1r1nk2r/2p2pp1/1p1n3Q/p2pp1bp/P3P2P/1R1PR2b/1PP2PP1/qBB1K1N1 w - - 0 101"));
    list.add(new PgnFileTestCase("capture_first_move_half_move_clock_101_white_to_move.pgn", "", "",
        "49...NA (1) 99.NA (100) 99...NA (101)", 1, 101, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1r1nk2r/2pb1pp1/1R1n3Q/p2pp1bp/P3P2P/3PR2N/1PP2PP1/qBB1K1N1 b - - 0 100"));
    list.add(new PgnFileTestCase("capture_first_move_half_move_clock_149_black_to_move.pgn", "", "",
        "26.NA (1) 75...NA (100) 100.NA (149)", 1, 149, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1r1nk2r/2pb1pp1/1p5Q/p2ppnbp/q3P2P/1R1PR2N/1PP2PP1/1BB1K1N1 w - - 0 101"));
    list.add(new PgnFileTestCase("capture_first_move_half_move_clock_149_white_to_move.pgn", "", "",
        "25...NA (1) 75.NA (100) 99...NA (149)", 1, 149, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1rbqkb1r/pp1p1ppp/2p5/3Bp3/PR2P3/BN2NQ2/2PP1PPP/5RK1 b k - 0 100"));
    list.add(new PgnFileTestCase("capture_first_move_half_move_clock_150_black_to_move.pgn", "", "",
        "25...NA (1) 75.NA (100) 100.NA (150)", 1, 150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "k7/2q1Q3/5N2/5pP1/5P2/8/PPP1P3/K7 w - - 0 101"));
    list.add(new PgnFileTestCase("capture_first_move_half_move_clock_150_white_to_move.pgn", "", "",
        "25.NA (1) 74...NA (100) 99...NA (150)", 1, 150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1r1nk2r/2pb1pp1/1p1Q4/p2pp1bp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 b - - 0 100"));
    list.add(new PgnFileTestCase("capture_first_move_half_move_clock_151_black_to_move.pgn", "", "",
        "25.NA (1) 74...NA (100) 99...NA (150) 100.NA (151)", 1, 151, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1r1nk2r/2pb1pp1/1p5Q/p2pp1bp/P3n2P/1R1PR2N/1PP2PP1/qBB1K1N1 w - - 0 101"));
    list.add(new PgnFileTestCase("capture_first_move_half_move_clock_151_white_to_move.pgn", "", "",
        "24...NA (1) 74.NA (100) 99.NA (150) 99...NA (151)", 1, 151, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1r1nk2r/2pb1pp1/1p1n4/p2pp1bQ/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 b - - 0 100"));
    list.add(new PgnFileTestCase("capture_first_move_half_move_clock_99_black_to_move.pgn", "", "", "", 1, 99,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1nk2r/2pb1pp1/1p5Q/p2ppnbp/P3P2P/1R1PR2N/1qP2PP1/1BB1K1N1 w - - 0 101"));
    list.add(new PgnFileTestCase("capture_first_move_half_move_clock_99_white_to_move.pgn", "", "", "", 1, 99,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "1rbqkb1r/pR1p1ppp/2p5/3Bp3/P2NP3/Bn2NQ2/2PP1PPP/5RK1 b k - 0 100"));
    list.add(new PgnFileTestCase("capture_second_move_half_move_clock_100_black_to_move.pgn", "", "",
        "50...NA (1) 100.NA (100) 100...Kb8 (101)", 2, 101, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1k6/2R5/5N2/5pP1/5P2/Q7/PPP1P3/K7 b - - 0 101"));
    list.add(new PgnFileTestCase("capture_second_move_half_move_clock_100_white_to_move.pgn", "", "",
        "50.NA (1) 99...NA (100) 100.Qe6+ (101)", 2, 101, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1r1nk2r/2p2pp1/1p1nb3/p2pp1bp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 w - - 0 101"));
    list.add(new PgnFileTestCase("capture_second_move_half_move_clock_101_black_to_move.pgn", "", "",
        "50.NA (1) 99...NA (100) 100...Qa3 (102)", 2, 102, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1r1nk2r/2pb1pQ1/1p1n4/p2pp1bp/P3P2P/qR1PR2N/1PP2PP1/1BB1K1N1 b - - 0 101"));
    list.add(new PgnFileTestCase("capture_second_move_half_move_clock_101_white_to_move.pgn", "", "",
        "49...NA (1) 99.NA (100) 100.Kf1 (102)", 2, 102, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1r1nk2r/2pb1pp1/1p1n3Q/p2pp2p/P3P2P/1R1Pb2N/1PP2PP1/qBB2KN1 w - - 0 101"));
    list.add(new PgnFileTestCase("capture_second_move_half_move_clock_149_black_to_move.pgn", "", "",
        "26.NA (1) 75...NA (100) 100...Rb7 (150)", 2, 150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3nk2r/1rpb1pp1/1Q6/p2ppnbp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 b - - 0 101"));
    list.add(new PgnFileTestCase("capture_second_move_half_move_clock_149_white_to_move.pgn", "", "",
        "25...NA (1) 75.NA (100) 100.Kh1 (150)", 2, 150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1rbqkb1r/pp1p1ppp/8/3pp3/PR1NP3/Bn2NQ2/2PP1PPP/5R1K w k - 0 101"));
    list.add(new PgnFileTestCase("capture_second_move_half_move_clock_150_black_to_move.pgn", "", "",
        "25...NA (1) 75.NA (100) 100.NA (150) 100...Kb8 (151)", 2, 151, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1k6/2R5/5N2/5pP1/5P2/Qr6/PPP1P3/K7 b - - 0 101"));
    list.add(new PgnFileTestCase("capture_second_move_half_move_clock_150_white_to_move.pgn", "", "",
        "25.NA (1) 74...NA (100) 99...NA (150) 100.Nf4 (151)", 2, 151, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1r1nk2r/2pb1p2/1p1n3p/p2pp1bp/P3PN1P/1R1PR3/1PP2PP1/qBB1K1N1 w - - 0 101"));
    list.add(new PgnFileTestCase("capture_second_move_half_move_clock_151_black_to_move.pgn", "", "",
        "25.NA (1) 74...NA (100) 99...NA (150) 100...Nb5 (152)", 2, 152, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1r1nk2r/2pb1pp1/1p5Q/pR1pp1bp/P3P2P/3PR2N/1PP2PP1/qBB1K1N1 b - - 0 101"));
    list.add(new PgnFileTestCase("capture_second_move_half_move_clock_151_white_to_move.pgn", "", "",
        "24...NA (1) 74.NA (100) 99.NA (150) 100.Nf3 (152)", 2, 152, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1r1nk2r/2pb1pp1/1p1n3Q/p2pp2p/P3P2P/1R1PbN1N/1PP2PP1/qBB1K3 w - - 0 101"));
    list.add(new PgnFileTestCase("capture_second_move_half_move_clock_99_black_to_move.pgn", "", "",
        "51.NA (1) 100...Qa3 (100)", 2, 100, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1r1nk2r/2pb1pp1/1p5Q/p2ppnbp/P3P2P/R2PR2N/1PP2PP1/1BB1K1N1 b - - 0 101"));
    list.add(new PgnFileTestCase("capture_second_move_half_move_clock_99_white_to_move.pgn", "", "",
        "50...NA (1) 100.Qf6 (100)", 2, 100, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1rbqkb1r/pp1p1p1p/2p2p2/3Bp3/PR1NP3/Bn2N3/2PP1PPP/5RK1 w k - 0 101"));
    list.add(new PgnFileTestCase("no_move_half_move_clock_100_black_to_move.pgn", "", "", "50...NA (1) 100.NA (100)",
        -1, 100, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "k7/2R1Q3/5N2/5pP1/5P2/8/PPP1P3/K7 b - - 100 100"));
    list.add(new PgnFileTestCase("no_move_half_move_clock_100_white_to_move.pgn", "", "", "50.NA (1) 99...NA (100)", -1,
        100, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1nk2r/2pb1pp1/1p1n3Q/p2pp1bp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 w - - 100 100"));
    list.add(new PgnFileTestCase("no_move_half_move_clock_101_black_to_move.pgn", "", "",
        "50.NA (1) 99...NA (100) 100.NA (101)", -1, 101, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1nk2r/2pb1pp1/1p1n3Q/p2pp1bp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 b - - 101 100"));
    list.add(new PgnFileTestCase("no_move_half_move_clock_101_white_to_move.pgn", "", "",
        "49...NA (1) 99.NA (100) 99...NA (101)", -1, 101, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1nk2r/2pb1pp1/1p1n3Q/p2pp1bp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 w - - 101 100"));
    list.add(new PgnFileTestCase("no_move_half_move_clock_149_black_to_move.pgn", "", "",
        "26.NA (1) 75...NA (100) 100.NA (149)", -1, 149, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1nk2r/2pb1pp1/1p5Q/p2ppnbp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 b - - 149 100"));
    list.add(new PgnFileTestCase("no_move_half_move_clock_149_white_to_move.pgn", "", "",
        "25...NA (1) 75.NA (100) 99...NA (149)", -1, 149, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1rbqkb1r/pp1p1ppp/2p5/3Bp3/PR1NP3/Bn2NQ2/2PP1PPP/5RK1 w k - 149 100"));
    list.add(new PgnFileTestCase("no_move_half_move_clock_150_black_to_move.pgn", "", "",
        "25...NA (1) 75.NA (100) 100.NA (150)", -1, 150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "k7/2R1Q3/5N2/5pP1/5P2/8/PPP1P3/K7 b - - 150 100"));
    list.add(new PgnFileTestCase("no_move_half_move_clock_150_white_to_move.pgn", "", "",
        "25.NA (1) 74...NA (100) 99...NA (150)", -1, 150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "1r1nk2r/2pb1pp1/1p1n3Q/p2pp1bp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 w - - 150 100"));
    list.add(new PgnFileTestCase("no_move_half_move_clock_151_black_to_move.pgn", "", "",
        "25.NA (1) 74...NA (100) 99...NA (150) 100.NA (151)", -1, 151, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1r1nk2r/2pb1pp1/1p1n3Q/p2pp1bp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 b - - 151 100"));
    list.add(new PgnFileTestCase("no_move_half_move_clock_151_white_to_move.pgn", "", "",
        "24...NA (1) 74.NA (100) 99.NA (150) 99...NA (151)", -1, 151, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1r1nk2r/2pb1pp1/1p1n3Q/p2pp1bp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 w - - 151 100"));
    list.add(new PgnFileTestCase("no_move_half_move_clock_99_black_to_move.pgn", "", "", "", -1, 99,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1nk2r/2pb1pp1/1p5Q/p2ppnbp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 b - - 99 100"));
    list.add(new PgnFileTestCase("no_move_half_move_clock_99_white_to_move.pgn", "", "", "", -1, 99,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "1rbqkb1r/pp1p1ppp/2p5/3Bp3/PR1NP3/Bn2NQ2/2PP1PPP/5RK1 w k - 99 100"));
    list.add(new PgnFileTestCase("one_move_half_move_clock_100_black_to_move.pgn", "", "",
        "50...NA (1) 100.NA (100) 100...Kb8 (101)", -1, 101, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1k6/2R1Q3/5N2/5pP1/5P2/8/PPP1P3/K7 w - - 101 101"));
    list.add(new PgnFileTestCase("one_move_half_move_clock_100_white_to_move.pgn", "", "",
        "50.NA (1) 99...NA (100) 100.Qe6+ (101)", -1, 101, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1nk2r/2pb1pp1/1p1nQ3/p2pp1bp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 b - - 101 100"));
    list.add(new PgnFileTestCase("one_move_half_move_clock_101_black_to_move.pgn", "", "",
        "50.NA (1) 99...NA (100) 100...Qa3 (102)", -1, 102, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1nk2r/2pb1pp1/1p1n3Q/p2pp1bp/P3P2P/qR1PR2N/1PP2PP1/1BB1K1N1 w - - 102 101"));
    list.add(new PgnFileTestCase("one_move_half_move_clock_101_white_to_move.pgn", "", "",
        "49...NA (1) 99.NA (100) 100.Kf1 (102)", -1, 102, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1nk2r/2pb1pp1/1p1n3Q/p2pp1bp/P3P2P/1R1PR2N/1PP2PP1/qBB2KN1 b - - 102 100"));
    list.add(new PgnFileTestCase("one_move_half_move_clock_149_black_to_move.pgn", "", "",
        "26.NA (1) 75...NA (100) 100...Rb7 (150)", -1, 150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "3nk2r/1rpb1pp1/1p5Q/p2ppnbp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 w - - 150 101"));
    list.add(new PgnFileTestCase("one_move_half_move_clock_149_white_to_move.pgn", "", "",
        "25...NA (1) 75.NA (100) 100.Kh1 (150)", -1, 150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "1rbqkb1r/pp1p1ppp/2p5/3Bp3/PR1NP3/Bn2NQ2/2PP1PPP/5R1K b k - 150 100"));
    list.add(new PgnFileTestCase("one_move_half_move_clock_150_black_to_move.pgn", "", "",
        "25...NA (1) 75.NA (100) 100.NA (150) 100...Kb8 (151)", -1, 151, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1k6/2R1Q3/5N2/5pP1/5P2/8/PPP1P3/K7 w - - 151 101"));
    list.add(new PgnFileTestCase("one_move_half_move_clock_150_white_to_move.pgn", "", "",
        "25.NA (1) 74...NA (100) 99...NA (150) 100.Nf4 (151)", -1, 151, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1r1nk2r/2pb1pp1/1p1n3Q/p2pp1bp/P3PN1P/1R1PR3/1PP2PP1/qBB1K1N1 b - - 151 100"));
    list.add(new PgnFileTestCase("one_move_half_move_clock_151_black_to_move.pgn", "", "",
        "25.NA (1) 74...NA (100) 99...NA (150) 100...Nb5 (152)", -1, 152, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1r1nk2r/2pb1pp1/1p5Q/pn1pp1bp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 w - - 152 101"));
    list.add(new PgnFileTestCase("one_move_half_move_clock_151_white_to_move.pgn", "", "",
        "24...NA (1) 74.NA (100) 99.NA (150) 100.Nf3 (152)", -1, 152, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1r1nk2r/2pb1pp1/1p1n3Q/p2pp1bp/P3P2P/1R1PRN1N/1PP2PP1/qBB1K3 b - - 152 100"));
    list.add(new PgnFileTestCase("one_move_half_move_clock_99_black_to_move.pgn", "", "", "51.NA (1) 100...Qa3 (100)",
        -1, 100, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1nk2r/2pb1pp1/1p5Q/p2ppnbp/P3P2P/qR1PR2N/1PP2PP1/1BB1K1N1 w - - 100 101"));
    list.add(new PgnFileTestCase("one_move_half_move_clock_99_white_to_move.pgn", "", "", "50...NA (1) 100.Qf6 (100)",
        -1, 100, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "1rbqkb1r/pp1p1ppp/2p2Q2/3Bp3/PR1NP3/Bn2N3/2PP1PPP/5RK1 b k - 100 100"));
    list.add(new PgnFileTestCase("pawn_first_move_half_move_clock_100_black_to_move.pgn", "", "",
        "50...NA (1) 100.NA (100)", -1, 100, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "k7/2R1Q3/5N2/6P1/5p2/5P2/PPP1P3/K7 w - - 0 101"));
    list.add(
        new PgnFileTestCase("pawn_first_move_half_move_clock_100_white_to_move.pgn", "", "", "50.NA (1) 99...NA (100)",
            -1, 100, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
            UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
            "1r1nk2r/2pb1pp1/1p1n3Q/p2pp1bp/P3P2P/1R1PR1PN/1PP2P2/qBB1K1N1 b - - 0 100"));
    list.add(new PgnFileTestCase("pawn_first_move_half_move_clock_101_black_to_move.pgn", "", "",
        "50.NA (1) 99...NA (100) 100.NA (101)", -1, 101, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1nk2r/2pb2p1/1p1n3Q/p2pppbp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 w - f6 0 101"));
    list.add(new PgnFileTestCase("pawn_first_move_half_move_clock_101_white_to_move.pgn", "", "",
        "49...NA (1) 99.NA (100) 99...NA (101)", -1, 101, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1nk2r/2pb1pp1/1p1n3Q/p2pp1bp/P2PP2P/1R2R2N/1PP2PP1/qBB1K1N1 b - - 0 100"));
    list.add(new PgnFileTestCase("pawn_first_move_half_move_clock_149_black_to_move.pgn", "", "",
        "26.NA (1) 75...NA (100) 100.NA (149)", -1, 149, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1r1nk2r/2pb1pp1/7Q/pp1ppnbp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 w - - 0 101"));
    list.add(new PgnFileTestCase("pawn_first_move_half_move_clock_149_white_to_move.pgn", "", "",
        "25...NA (1) 75.NA (100) 99...NA (149)", -1, 149, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1rbqkb1r/pp1p1ppp/2p5/P2Bp3/1R1NP3/Bn2NQ2/2PP1PPP/5RK1 b k - 0 100"));
    list.add(new PgnFileTestCase("pawn_first_move_half_move_clock_150_black_to_move.pgn", "", "",
        "25...NA (1) 75.NA (100) 100.NA (150)", -1, 150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "k7/2R1Q3/5N2/6P1/5p2/5P2/PPP1P3/K7 w - - 0 101"));
    list.add(new PgnFileTestCase("pawn_first_move_half_move_clock_150_white_to_move.pgn", "", "",
        "25.NA (1) 74...NA (100) 99...NA (150)", -1, 150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1nk2r/2pb1pp1/1p1n3Q/p2pp1bp/P3P2P/1R1PR1PN/1PP2P2/qBB1K1N1 b - - 0 100"));
    list.add(new PgnFileTestCase("pawn_first_move_half_move_clock_151_black_to_move.pgn", "", "",
        "25.NA (1) 74...NA (100) 99...NA (150) 100.NA (151)", -1, 151, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1nk2r/2pb1pp1/1p1n3Q/p3p1bp/P2pP2P/1R1PR2N/1PP2PP1/qBB1K1N1 w - - 0 101"));
    list.add(new PgnFileTestCase("pawn_first_move_half_move_clock_151_white_to_move.pgn", "", "",
        "24...NA (1) 74.NA (100) 99.NA (150) 99...NA (151)", -1, 151, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1nk2r/2pb1pp1/1p1n3Q/p2pp1bp/P3P1PP/1R1PR2N/1PP2P2/qBB1K1N1 b - g3 0 100"));
    list.add(new PgnFileTestCase("pawn_first_move_half_move_clock_99_black_to_move.pgn", "", "", "", -1, 99,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1nk2r/2pb1pp1/7Q/pp1ppnbp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 w - - 0 101"));
    list.add(new PgnFileTestCase("pawn_first_move_half_move_clock_99_white_to_move.pgn", "", "", "", -1, 99,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "1rbqkb1r/pp1p1ppp/2p5/3Bp3/PR1NP2P/Bn2NQ2/2PP1PP1/5RK1 b k h3 0 100"));
    list.add(new PgnFileTestCase("repetition_from_one_move_black_to_move_fivefold.pgn",
        "repPos=5: 17...Qd7 19...Nb8 21...Bf8 23...Qd7 25...Qd7", "", "", -1, 17, CheckmateOrStalemate.NA, 5,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1n2kb1r/p2q1ppp/8/4p1B1/4P3/8/PPP2PPP/2KR4 w k - 17 26"));
    list.add(new PgnFileTestCase("repetition_from_one_move_black_to_move_fourfold.pgn",
        "repPos=4: 17...Qd7 19...Nb8 21...Bf8 23...Qd7", "", "", -1, 13, CheckmateOrStalemate.NA, 4,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1n2kb1r/p2q1ppp/8/4p1B1/4P3/8/PPP2PPP/2KR4 w k - 13 24"));
    list.add(new PgnFileTestCase("repetition_from_one_move_black_to_move_sixfold.pgn",
        "repPos=6: 17...Qd7 19...Nb8 21...Bf8 23...Qd7 25...Qd7 27...Bf8", "", "", -1, 21, CheckmateOrStalemate.NA, 6,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1n2kb1r/p2q1ppp/8/4p1B1/4P3/8/PPP2PPP/2KR4 w k - 21 28"));
    list.add(new PgnFileTestCase("repetition_from_one_move_black_to_move_threefold.pgn",
        "repPos=3: 17...Qd7 19...Nb8 21...Bf8", "", "", -1, 9, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1n2kb1r/p2q1ppp/8/4p1B1/4P3/8/PPP2PPP/2KR4 w k - 9 22"));
    list.add(new PgnFileTestCase("repetition_from_one_move_white_to_move_fivefold.pgn",
        "repPos=5: 17.Qa4 19.Qa4 21.Qa4 23.Qa4 25.Rf1", "", "", -1, 17, CheckmateOrStalemate.NA, 5,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "r1b2r2/pp1pk1pp/8/7q/Q2pP1n1/5N1P/PP3PP1/3R1RK1 b - - 17 25"));
    list.add(new PgnFileTestCase("repetition_from_one_move_white_to_move_fourfold.pgn",
        "repPos=4: 17.Qa4 19.Qa4 21.Qa4 23.Qa4", "", "", -1, 13, CheckmateOrStalemate.NA, 4, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1b2r2/pp1pk1pp/8/7q/Q2pP1n1/5N1P/PP3PP1/3R1RK1 b - - 13 23"));
    list.add(new PgnFileTestCase("repetition_from_one_move_white_to_move_sixfold.pgn",
        "repPos=6: 17.Qa4 19.Qa4 21.Qa4 23.Qa4 25.Rf1 27.Kg1", "", "", -1, 21, CheckmateOrStalemate.NA, 6,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "r1b2r2/pp1pk1pp/8/7q/Q2pP1n1/5N1P/PP3PP1/3R1RK1 b - - 21 27"));
    list.add(new PgnFileTestCase("repetition_from_one_move_white_to_move_threefold.pgn",
        "repPos=3: 17.Qa4 19.Qa4 21.Qa4", "", "", -1, 9, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1b2r2/pp1pk1pp/8/7q/Q2pP1n1/5N1P/PP3PP1/3R1RK1 b - - 9 21"));
    list.add(new PgnFileTestCase("repetition_from_three_moves_black_to_move_fivefold.pgn",
        "repPos=5: 6...g3 8...Nb8 10...Bf8 12...Qh4 14...Qh4", "", "", -1, 16, CheckmateOrStalemate.NA, 5,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "rnb1kbnr/pppp1p1p/8/4N3/2B1Pp1q/6p1/PPPP2PP/RNBQ1K1R w kq - 16 15"));
    list.add(new PgnFileTestCase("repetition_from_three_moves_black_to_move_fourfold.pgn",
        "repPos=4: 6...g3 8...Nb8 10...Bf8 12...Qh4", "", "", -1, 12, CheckmateOrStalemate.NA, 4,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnb1kbnr/pppp1p1p/8/4N3/2B1Pp1q/6p1/PPPP2PP/RNBQ1K1R w kq - 12 13"));
    list.add(new PgnFileTestCase("repetition_from_three_moves_black_to_move_sixfold.pgn",
        "repPos=6: 6...g3 8...Nb8 10...Bf8 12...Qh4 14...Qh4 16...Ng8", "", "", -1, 20, CheckmateOrStalemate.NA, 6,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "rnb1kbnr/pppp1p1p/8/4N3/2B1Pp1q/6p1/PPPP2PP/RNBQ1K1R w kq - 20 17"));
    list.add(new PgnFileTestCase("repetition_from_three_moves_black_to_move_threefold.pgn",
        "repPos=3: 6...g3 8...Nb8 10...Bf8", "", "", -1, 8, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnb1kbnr/pppp1p1p/8/4N3/2B1Pp1q/6p1/PPPP2PP/RNBQ1K1R w kq - 8 11"));
    list.add(new PgnFileTestCase("repetition_from_three_moves_white_to_move_fivefold.pgn",
        "repPos=5: 14.f4 16.Qc3 18.Nd2 20.Ra1 22.Kg1", "", "", -1, 16, CheckmateOrStalemate.NA, 5,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "r4rk1/p1pq1ppp/1pn1p3/1b2P3/1Pp2Pn1/2Q3P1/PB1NP1BP/R4RK1 b - - 16 22"));
    list.add(new PgnFileTestCase("repetition_from_three_moves_white_to_move_fourfold.pgn",
        "repPos=4: 14.f4 16.Qc3 18.Nd2 20.Ra1", "", "", -1, 12, CheckmateOrStalemate.NA, 4, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r4rk1/p1pq1ppp/1pn1p3/1b2P3/1Pp2Pn1/2Q3P1/PB1NP1BP/R4RK1 b - - 12 20"));
    list.add(new PgnFileTestCase("repetition_from_three_moves_white_to_move_sixfold.pgn",
        "repPos=6: 14.f4 16.Qc3 18.Nd2 20.Ra1 22.Kg1 24.Bg2", "", "", -1, 20, CheckmateOrStalemate.NA, 6,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "r4rk1/p1pq1ppp/1pn1p3/1b2P3/1Pp2Pn1/2Q3P1/PB1NP1BP/R4RK1 b - - 20 24"));
    list.add(new PgnFileTestCase("repetition_from_three_moves_white_to_move_threefold.pgn",
        "repPos=3: 14.f4 16.Qc3 18.Nd2", "", "", -1, 8, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r4rk1/p1pq1ppp/1pn1p3/1b2P3/1Pp2Pn1/2Q3P1/PB1NP1BP/R4RK1 b - - 8 18"));
    list.add(new PgnFileTestCase("repetition_from_two_moves_black_to_move_fivefold.pgn",
        "repPos=5: 13.a4 15.Qd1 17.Nc3 19.Bc4 21.Ng5", "", "", -1, 16, CheckmateOrStalemate.NA, 5,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "r2qk2r/pppbnpp1/1bn4p/4p1N1/P1BP4/2N5/5PPP/R1BQR1K1 b kq - 16 21"));
    list.add(new PgnFileTestCase("repetition_from_two_moves_black_to_move_fourfold.pgn",
        "repPos=4: 13.a4 15.Qd1 17.Nc3 19.Bc4", "", "", -1, 12, CheckmateOrStalemate.NA, 4, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r2qk2r/pppbnpp1/1bn4p/4p1N1/P1BP4/2N5/5PPP/R1BQR1K1 b kq - 12 19"));
    list.add(new PgnFileTestCase("repetition_from_two_moves_black_to_move_sixfold.pgn",
        "repPos=6: 13.a4 15.Qd1 17.Nc3 19.Bc4 21.Ng5 23.Kg1", "", "", -1, 20, CheckmateOrStalemate.NA, 6,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "r2qk2r/pppbnpp1/1bn4p/4p1N1/P1BP4/2N5/5PPP/R1BQR1K1 b kq - 20 23"));
    list.add(new PgnFileTestCase("repetition_from_two_moves_black_to_move_threefold.pgn",
        "repPos=3: 13.a4 15.Qd1 17.Nc3", "", "", -1, 8, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r2qk2r/pppbnpp1/1bn4p/4p1N1/P1BP4/2N5/5PPP/R1BQR1K1 b kq - 8 17"));
    list.add(new PgnFileTestCase("repetition_from_two_moves_white_to_move_fivefold.pgn",
        "repPos=5: 23...Qc8 25...Rf8 27...Nh5 29...Kg8 31...Bd7", "", "", -1, 18, CheckmateOrStalemate.NA, 5,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "r1q2rk1/3b1ppp/nb1p4/1p1Pp2n/1P2P3/4BN1P/1Q2RPP1/RB3NK1 w - - 18 32"));
    list.add(new PgnFileTestCase("repetition_from_two_moves_white_to_move_fourfold.pgn",
        "repPos=4: 23...Qc8 25...Rf8 27...Nh5 29...Kg8", "", "", -1, 14, CheckmateOrStalemate.NA, 4,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1q2rk1/3b1ppp/nb1p4/1p1Pp2n/1P2P3/4BN1P/1Q2RPP1/RB3NK1 w - - 14 30"));
    list.add(new PgnFileTestCase("repetition_from_two_moves_white_to_move_sixfold.pgn",
        "repPos=6: 23...Qc8 25...Rf8 27...Nh5 29...Kg8 31...Bd7 33...Bb6", "", "", -1, 22, CheckmateOrStalemate.NA, 6,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "r1q2rk1/3b1ppp/nb1p4/1p1Pp2n/1P2P3/4BN1P/1Q2RPP1/RB3NK1 w - - 22 34"));
    list.add(new PgnFileTestCase("repetition_from_two_moves_white_to_move_threefold.pgn",
        "repPos=3: 23...Qc8 25...Rf8 27...Nh5", "", "", -1, 10, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1q2rk1/3b1ppp/nb1p4/1p1Pp2n/1P2P3/4BN1P/1Q2RPP1/RB3NK1 w - - 10 28"));
    list.add(new PgnFileTestCase("repetition_from_zero_moves_black_to_move_fivefold.pgn",
        "repPos=5: 57.Qe3 59.Qe3 61.Nd2 63.Kg1", "", "", -1, 16, CheckmateOrStalemate.NA, 5, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "r2q2n1/1bpk1pp1/p2b3p/1p2pN2/P2PP3/R1P1Q3/1P1N1PPP/2B2RK1 b - - 16 63"));
    list.add(new PgnFileTestCase("repetition_from_zero_moves_black_to_move_fourfold.pgn",
        "repPos=4: 57.Qe3 59.Qe3 61.Nd2", "", "", -1, 12, CheckmateOrStalemate.NA, 4, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r2q2n1/1bpk1pp1/p2b3p/1p2pN2/P2PP3/R1P1Q3/1P1N1PPP/2B2RK1 b - - 12 61"));
    list.add(new PgnFileTestCase("repetition_from_zero_moves_black_to_move_sixfold.pgn",
        "repPos=6: 57.Qe3 59.Qe3 61.Nd2 63.Kg1 65.Nf5", "", "", -1, 20, CheckmateOrStalemate.NA, 6,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "r2q2n1/1bpk1pp1/p2b3p/1p2pN2/P2PP3/R1P1Q3/1P1N1PPP/2B2RK1 b - - 20 65"));
    list.add(new PgnFileTestCase("repetition_from_zero_moves_black_to_move_threefold.pgn", "repPos=3: 57.Qe3 59.Qe3",
        "", "", -1, 8, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r2q2n1/1bpk1pp1/p2b3p/1p2pN2/P2PP3/R1P1Q3/1P1N1PPP/2B2RK1 b - - 8 59"));
    list.add(new PgnFileTestCase("repetition_from_zero_moves_white_to_move_fivefold.pgn",
        "repPos=5: 51...Nf6 53...Nf6 55...Nf6 57...Nf6", "", "", -1, 26, CheckmateOrStalemate.NA, 5,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "r1bqkb1r/1pp2ppp/p1p2n2/4p2Q/P3P3/8/1PPP1PPP/RNB1K1NR w KQkq - 26 58"));
    list.add(new PgnFileTestCase("repetition_from_zero_moves_white_to_move_fourfold.pgn",
        "repPos=4: 51...Nf6 53...Nf6 55...Nf6", "", "", -1, 22, CheckmateOrStalemate.NA, 4, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bqkb1r/1pp2ppp/p1p2n2/4p2Q/P3P3/8/1PPP1PPP/RNB1K1NR w KQkq - 22 56"));
    list.add(new PgnFileTestCase("repetition_from_zero_moves_white_to_move_sixfold.pgn",
        "repPos=6: 51...Nf6 53...Nf6 55...Nf6 57...Nf6 59...Nf6", "", "", -1, 30, CheckmateOrStalemate.NA, 6,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "r1bqkb1r/1pp2ppp/p1p2n2/4p2Q/P3P3/8/1PPP1PPP/RNB1K1NR w KQkq - 30 60"));
    list.add(new PgnFileTestCase("repetition_from_zero_moves_white_to_move_threefold.pgn",
        "repPos=3: 51...Nf6 53...Nf6", "", "", -1, 18, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bqkb1r/1pp2ppp/p1p2n2/4p2Q/P3P3/8/1PPP1PPP/RNB1K1NR w KQkq - 18 54"));
    list.add(new PgnFileTestCase("three_moves_half_move_clock_100_black_to_move.pgn", "", "",
        "50...NA (1) 100.NA (100) 101...Kc8 (103)", -1, 103, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "2k5/3RQ3/5N2/5pP1/5P2/8/PPP1P3/K7 w - - 103 102"));
    list.add(new PgnFileTestCase("three_moves_half_move_clock_100_white_to_move.pgn", "", "",
        "50.NA (1) 99...NA (100) 101.Rb5 (103)", -1, 103, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1n1k1r/2pb1pp1/1p1nQ3/pR1pp1bp/P3P2P/3PR2N/1PP2PP1/qBB1K1N1 b - - 103 101"));
    list.add(new PgnFileTestCase("three_moves_half_move_clock_101_black_to_move.pgn", "", "",
        "50.NA (1) 99...NA (100) 101...Nc6 (104)", -1, 104, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r2k2r/2pb1pp1/1pnn3Q/p2pp1bp/P3P2P/qR1P3N/1PP1RPP1/1BB1K1N1 w - - 104 102"));
    list.add(new PgnFileTestCase("three_moves_half_move_clock_101_white_to_move.pgn", "", "",
        "49...NA (1) 99.NA (100) 101.Rf3 (104)", -1, 104, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1n1k1r/2pb1pp1/1p1n3Q/p2pp1bp/P3P2P/1R1P1R1N/1PP2PP1/qBB2KN1 b - - 104 101"));
    list.add(new PgnFileTestCase("three_moves_half_move_clock_149_black_to_move.pgn", "", "",
        "26.NA (1) 75...NA (100) 100...Rb7 (150) 101...Ra7 (152)", -1, 152, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "3nk2r/r1pb1pp1/1p5Q/p2ppnbp/P3P2P/1R1PR2N/1PP2PP1/qBBK2N1 w - - 152 102"));
    list.add(new PgnFileTestCase("three_moves_half_move_clock_149_white_to_move.pgn", "", "",
        "25...NA (1) 75.NA (100) 100.Kh1 (150) 101.Qd1 (152)", -1, 152, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1rb1kb1r/pp1p1ppp/2p5/3Bp3/PR1NP2q/Bn2N3/2PP1PPP/3Q1R1K b k - 152 101"));
    list.add(new PgnFileTestCase("three_moves_half_move_clock_150_black_to_move.pgn", "", "",
        "25...NA (1) 75.NA (100) 100.NA (150) 101...Ka8 (153)", -1, 153, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "k7/1R2Q3/5N2/5pP1/5P2/8/PPP1P3/K7 w - - 153 102"));
    list.add(new PgnFileTestCase("three_moves_half_move_clock_150_white_to_move.pgn", "", "",
        "25.NA (1) 74...NA (100) 99...NA (150) 101.Nfe2 (153)", -1, 153, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1r1nk2r/1npb1pp1/1p5Q/p2pp1bp/P3P2P/1R1PR3/1PP1NPP1/qBB1K1N1 b - - 153 101"));
    list.add(new PgnFileTestCase("three_moves_half_move_clock_151_black_to_move.pgn", "", "",
        "25.NA (1) 74...NA (100) 99...NA (150) 101...Nd4 (154)", -1, 154, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1r1nk2r/2pb1pp1/1pQ5/p2pp1bp/P2nP2P/1R1PR2N/1PP2PP1/qBB1K1N1 w - - 154 102"));
    list.add(new PgnFileTestCase("three_moves_half_move_clock_151_white_to_move.pgn", "", "",
        "24...NA (1) 74.NA (100) 99.NA (150) 101.Nfg1 (154)", -1, 154, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1r2k2r/1npb1pp1/1p1n3Q/p2pp1bp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 b - - 154 101"));
    list.add(new PgnFileTestCase("three_moves_half_move_clock_99_black_to_move.pgn", "", "",
        "51.NA (1) 100...Qa3 (100) 101...Ne6 (102)", -1, 102, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r2k2r/2pb1pp1/1p2n2Q/p2ppnbp/P3P2P/qR1PR2N/1PP1NPP1/1BB1K3 w - - 102 102"));
    list.add(new PgnFileTestCase("three_moves_half_move_clock_99_white_to_move.pgn", "", "",
        "50...NA (1) 100.Qf6 (100) 101.Qf3 (102)", -1, 102, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1rb1kb1r/pp1pqppp/2p5/3Bp3/PR1NP3/Bn2NQ2/2PP1PPP/5RK1 b k - 102 101"));
    list.add(new PgnFileTestCase("two_moves_half_move_clock_100_black_to_move.pgn", "", "",
        "50...NA (1) 100.NA (100) 101.Rd7 (102)", -1, 102, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1k6/3RQ3/5N2/5pP1/5P2/8/PPP1P3/K7 b - - 102 101"));
    list.add(new PgnFileTestCase("two_moves_half_move_clock_100_white_to_move.pgn", "", "",
        "50.NA (1) 99...NA (100) 100...Kf8 (102)", -1, 102, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1n1k1r/2pb1pp1/1p1nQ3/p2pp1bp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 w - - 102 101"));
    list.add(new PgnFileTestCase("two_moves_half_move_clock_101_black_to_move.pgn", "", "",
        "50.NA (1) 99...NA (100) 101.Re2 (103)", -1, 103, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1nk2r/2pb1pp1/1p1n3Q/p2pp1bp/P3P2P/qR1P3N/1PP1RPP1/1BB1K1N1 b - - 103 101"));
    list.add(new PgnFileTestCase("two_moves_half_move_clock_101_white_to_move.pgn", "", "",
        "49...NA (1) 99.NA (100) 100...Kf8 (103)", -1, 103, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r1n1k1r/2pb1pp1/1p1n3Q/p2pp1bp/P3P2P/1R1PR2N/1PP2PP1/qBB2KN1 w - - 103 101"));
    list.add(new PgnFileTestCase("two_moves_half_move_clock_149_black_to_move.pgn", "", "",
        "26.NA (1) 75...NA (100) 100...Rb7 (150) 101.Kd1 (151)", -1, 151, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "3nk2r/1rpb1pp1/1p5Q/p2ppnbp/P3P2P/1R1PR2N/1PP2PP1/qBBK2N1 b - - 151 101"));
    list.add(new PgnFileTestCase("two_moves_half_move_clock_149_white_to_move.pgn", "", "",
        "25...NA (1) 75.NA (100) 100.Kh1 (150) 100...Qh4 (151)", -1, 151, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1rb1kb1r/pp1p1ppp/2p5/3Bp3/PR1NP2q/Bn2NQ2/2PP1PPP/5R1K w k - 151 101"));
    list.add(new PgnFileTestCase("two_moves_half_move_clock_150_black_to_move.pgn", "", "",
        "25...NA (1) 75.NA (100) 100.NA (150) 101.Rb7+ (152)", -1, 152, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1k6/1R2Q3/5N2/5pP1/5P2/8/PPP1P3/K7 b - - 152 101"));
    list.add(new PgnFileTestCase("two_moves_half_move_clock_150_white_to_move.pgn", "", "",
        "25.NA (1) 74...NA (100) 99...NA (150) 100...N6b7 (152)", -1, 152, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1r1nk2r/1npb1pp1/1p5Q/p2pp1bp/P3PN1P/1R1PR3/1PP2PP1/qBB1K1N1 w - - 152 101"));
    list.add(new PgnFileTestCase("two_moves_half_move_clock_151_black_to_move.pgn", "", "",
        "25.NA (1) 74...NA (100) 99...NA (150) 101.Qc6 (153)", -1, 153, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1r1nk2r/2pb1pp1/1pQ5/pn1pp1bp/P3P2P/1R1PR2N/1PP2PP1/qBB1K1N1 b - - 153 101"));
    list.add(new PgnFileTestCase("two_moves_half_move_clock_151_white_to_move.pgn", "", "",
        "24...NA (1) 74.NA (100) 99.NA (150) 100...N8b7 (153)", -1, 153, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1r2k2r/1npb1pp1/1p1n3Q/p2pp1bp/P3P2P/1R1PRN1N/1PP2PP1/qBB1K3 w - - 153 101"));
    list.add(new PgnFileTestCase("two_moves_half_move_clock_99_black_to_move.pgn", "", "",
        "51.NA (1) 100...Qa3 (100) 101.Ne2 (101)", -1, 101, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1r1nk2r/2pb1pp1/1p5Q/p2ppnbp/P3P2P/qR1PR2N/1PP1NPP1/1BB1K3 b - - 101 101"));
    list.add(new PgnFileTestCase("two_moves_half_move_clock_99_white_to_move.pgn", "", "",
        "50...NA (1) 100.Qf6 (100) 100...Qe7 (101)", -1, 101, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1rb1kb1r/pp1pqppp/2p2Q2/3Bp3/PR1NP3/Bn2N3/2PP1PPP/5RK1 w k - 101 101"));

    return new PgnFileTestCaseList(PgnTest.BASIC_FROM_FEN, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicFromFenYawnWhite() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_white_initial_fifty_reoccuring_fifty.pgn", "", "",
        "50.NA (1) 99...NA (100); 100...Qb6 (1) 150.Qd4+ (100)", -1, 100, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/3p4/1kp1p3/8/2qQ3P/5PPK/8/8 b - - 100 150"));
    list.add(new PgnFileTestCase("02_white_initial_fifty_reoccuring_seventy_five.pgn", "", "",
        "50.NA (1) 99...NA (100); 100...Qb6 (1) 150.Qd4+ (100) 175.Qc8 (150)", -1, 150, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "2Q5/3p1q2/2pkp3/8/7P/5PPK/8/8 b - - 150 175"));
    list.add(new PgnFileTestCase("03_white_initial_fifty_reoccuring_above_fifty.pgn", "", "",
        "50.NA (1) 99...NA (100); 100...Qb6 (1) 150.Qd4+ (100) 174...Qf7 (149)", -1, 149, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "Q7/3p1q2/2pkp3/8/7P/5PPK/8/8 w - - 149 175"));
    list.add(new PgnFileTestCase("04_white_initial_fifty_reoccuring_above_seventy_five.pgn", "", "",
        "50.NA (1) 99...NA (100); 100...Qb6 (1) 150.Qd4+ (100) 175.Qc8 (150) 189.Qb3 (178)", -1, 178,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/3pk3/2p1p3/8/7P/qQ3PP1/6K1/8 b - - 178 189"));
    list.add(new PgnFileTestCase("05_white_initial_seventy_five_reoccuring_fifty.pgn", "", "",
        "25.NA (1) 74...NA (100) 99...NA (150); 100...Qb6 (1) 150.Qd4+ (100)", -1, 150, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/3p4/1kp1p3/8/2qQ3P/5PPK/8/8 b - - 100 150"));
    list.add(new PgnFileTestCase("06_white_initial_seventy_five_reoccuring_seventy_five.pgn", "", "",
        "25.NA (1) 74...NA (100) 99...NA (150); 100...Qb6 (1) 150.Qd4+ (100) 175.Qc8 (150)", -1, 150,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "2Q5/3p1q2/2pkp3/8/7P/5PPK/8/8 b - - 150 175"));
    list.add(new PgnFileTestCase("07_white_initial_seventy_five_reoccuring_above_fifty.pgn", "", "",
        "25.NA (1) 74...NA (100) 99...NA (150); 100...Qb6 (1) 150.Qd4+ (100) 174...Qf7 (149)", -1, 150,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "Q7/3p1q2/2pkp3/8/7P/5PPK/8/8 w - - 149 175"));
    list.add(new PgnFileTestCase("08_white_initial_seventy_five_reoccuring_above_seventy_five.pgn", "", "",
        "25.NA (1) 74...NA (100) 99...NA (150); 100...Qb6 (1) 150.Qd4+ (100) 175.Qc8 (150) 189.Qb3 (178)", -1, 178,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/3pk3/2p1p3/8/7P/qQ3PP1/6K1/8 b - - 178 189"));
    list.add(new PgnFileTestCase("09_white_initial_above_fifty_reoccuring_fifty.pgn", "", "",
        "45.NA (1) 94...NA (100) 99...NA (110); 100...Qb6 (1) 150.Qd4+ (100)", -1, 110, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/3p4/1kp1p3/8/2qQ3P/5PPK/8/8 b - - 100 150"));
    list.add(new PgnFileTestCase("10_white_initial_above_fifty_reoccuring_seventy_five.pgn", "", "",
        "44...NA (1) 94.NA (100) 99...NA (111); 100...Qb6 (1) 150.Qd4+ (100) 175.Qc8 (150)", -1, 150,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "2Q5/3p1q2/2pkp3/8/7P/5PPK/8/8 b - - 150 175"));
    list.add(new PgnFileTestCase("11_white_initial_above_fifty_reoccuring_above_fifty.pgn", "", "",
        "44.NA (1) 93...NA (100) 99...NA (112); 100...Qb6 (1) 150.Qd4+ (100) 174...Qf7 (149)", -1, 149,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "Q7/3p1q2/2pkp3/8/7P/5PPK/8/8 w - - 149 175"));
    list.add(new PgnFileTestCase("12_white_initial_above_fifty_reoccuring_above_seventy_five.pgn", "", "",
        "43...NA (1) 93.NA (100) 99...NA (113); 100...Qb6 (1) 150.Qd4+ (100) 175.Qc8 (150) 189.Qb3 (178)", -1, 178,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/3pk3/2p1p3/8/7P/qQ3PP1/6K1/8 b - - 178 189"));
    list.add(new PgnFileTestCase("13_white_initial_above_seventy_five_reoccuring_fifty.pgn", "", "",
        "20.NA (1) 69...NA (100) 94...NA (150) 99...NA (160); 100...Qb6 (1) 150.Qd4+ (100)", -1, 160,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/3p4/1kp1p3/8/2qQ3P/5PPK/8/8 b - - 100 150"));
    list.add(new PgnFileTestCase("14_white_initial_above_seventy_five_reoccuring_seventy_five.pgn", "", "",
        "19...NA (1) 69.NA (100) 94.NA (150) 99...NA (161); 100...Qb6 (1) 150.Qd4+ (100) 175.Qc8 (150)", -1, 161,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "2Q5/3p1q2/2pkp3/8/7P/5PPK/8/8 b - - 150 175"));
    list.add(new PgnFileTestCase("15_white_initial_above_seventy_five_reoccuring_above_fifty.pgn", "", "",
        "19.NA (1) 68...NA (100) 93...NA (150) 99...NA (162); 100...Qb6 (1) 150.Qd4+ (100) 174...Qf7 (149)", -1, 162,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "Q7/3p1q2/2pkp3/8/7P/5PPK/8/8 w - - 149 175"));
    list.add(new PgnFileTestCase("16_white_initial_above_seventy_five_reoccuring_above_seventy_five.pgn", "", "",
        "18...NA (1) 68.NA (100) 93.NA (150) 99...NA (163); 100...Qb6 (1) 150.Qd4+ (100) 175.Qc8 (150) 189.Qb3 (178)",
        -1, 178, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/3pk3/2p1p3/8/7P/qQ3PP1/6K1/8 b - - 178 189"));

    return new PgnFileTestCaseList(PgnTest.BASIC_FROM_FEN_YAWN_WHITE, list);
  }

  private static PgnFileTestCaseList createTestCasesBasicFromFenYawnBlack() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_black_initial_fifty_reoccuring_fifty.pgn", "", "",
        "50...NA (1) 100.NA (100); 101.Qd3 (1) 150...Qe3 (100)", 1, 100, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/6p1/7k/6n1/2NQ4/4q3/2P1P3/1K6 w - - 100 151"));
    list.add(new PgnFileTestCase("02_black_initial_fifty_reoccuring_seventy_five.pgn", "", "",
        "50...NA (1) 100.NA (100); 101.Qd3 (1) 150...Qe3 (100) 175...Qe3 (150)", 1, 150, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/6pk/8/6n1/2NQ4/4q3/1KP1P3/8 w - - 150 176"));
    list.add(new PgnFileTestCase("03_black_initial_fifty_reoccuring_above_fifty.pgn", "", "",
        "50...NA (1) 100.NA (100); 101.Qd3 (1) 150...Qe3 (100) 175.Qd4 (149)", 1, 149, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/6pk/8/4q1n1/2NQ4/8/1KP1P3/8 b - - 149 175"));
    list.add(new PgnFileTestCase("04_black_initial_fifty_reoccuring_above_seventy_five.pgn", "", "",
        "50...NA (1) 100.NA (100); 101.Qd3 (1) 150...Qe3 (100) 175...Qe3 (150) 179...Qg3 (158)", 1, 158,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "1Q6/6pk/8/6n1/2N5/6q1/1KP1P3/8 w - - 158 180"));
    list.add(new PgnFileTestCase("05_black_initial_seventy_five_reoccuring_fifty.pgn", "", "",
        "13.NA (1) 62...NA (100) 87...NA (150) 100.NA (175); 101.Qd3 (1) 150...Qe3 (100)", 1, 175,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/6p1/7k/6n1/2NQ4/4q3/2P1P3/1K6 w - - 100 151"));
    list.add(new PgnFileTestCase("06_black_initial_seventy_five_reoccuring_seventy_five.pgn", "", "",
        "25...NA (1) 75.NA (100) 100.NA (150); 101.Qd3 (1) 150...Qe3 (100) 175...Qe3 (150)", 1, 150,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/6pk/8/6n1/2NQ4/4q3/1KP1P3/8 w - - 150 176"));
    list.add(new PgnFileTestCase("07_black_initial_seventy_five_reoccuring_above_fifty.pgn", "", "",
        "25...NA (1) 75.NA (100) 100.NA (150); 101.Qd3 (1) 150...Qe3 (100) 175.Qd4 (149)", 1, 150,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/6pk/8/4q1n1/2NQ4/8/1KP1P3/8 b - - 149 175"));
    list.add(new PgnFileTestCase("08_black_initial_seventy_five_reoccuring_above_seventy_five.pgn", "", "",
        "25...NA (1) 75.NA (100) 100.NA (150); 101.Qd3 (1) 150...Qe3 (100) 175...Qe3 (150) 179...Qg3 (158)", 1, 158,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "1Q6/6pk/8/6n1/2N5/6q1/1KP1P3/8 w - - 158 180"));
    list.add(new PgnFileTestCase("09_black_initial_above_fifty_reoccuring_fifty.pgn", "", "",
        "45...NA (1) 95.NA (100) 100.NA (110); 101.Qd3 (1) 150...Qe3 (100)", 1, 110, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/6p1/7k/6n1/2NQ4/4q3/2P1P3/1K6 w - - 100 151"));
    list.add(new PgnFileTestCase("10_black_initial_above_fifty_reoccuring_seventy_five.pgn", "", "",
        "45.NA (1) 94...NA (100) 100.NA (111); 101.Qd3 (1) 150...Qe3 (100) 175...Qe3 (150)", 1, 150,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/6pk/8/6n1/2NQ4/4q3/1KP1P3/8 w - - 150 176"));
    list.add(new PgnFileTestCase("11_black_initial_above_fifty_reoccuring_above_fifty.pgn", "", "",
        "44...NA (1) 94.NA (100) 100.NA (112); 101.Qd3 (1) 150...Qe3 (100) 175.Qd4 (149)", 1, 149,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/6pk/8/4q1n1/2NQ4/8/1KP1P3/8 b - - 149 175"));
    list.add(new PgnFileTestCase("12_black_initial_above_fifty_reoccuring_above_seventy_five.pgn", "", "",
        "44.NA (1) 93...NA (100) 100.NA (113); 101.Qd3 (1) 150...Qe3 (100) 175...Qe3 (150) 179...Qg3 (158)", 1, 158,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "1Q6/6pk/8/6n1/2N5/6q1/1KP1P3/8 w - - 158 180"));
    list.add(new PgnFileTestCase("13_black_initial_above_seventy_five_reoccuring_fifty.pgn", "", "",
        "20...NA (1) 70.NA (100) 95.NA (150) 100.NA (160); 101.Qd3 (1) 150...Qe3 (100)", 1, 160,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/6p1/7k/6n1/2NQ4/4q3/2P1P3/1K6 w - - 100 151"));
    list.add(new PgnFileTestCase("14_black_initial_above_seventy_five_reoccuring_seventy_five.pgn", "", "",
        "20.NA (1) 69...NA (100) 94...NA (150) 100.NA (161); 101.Qd3 (1) 150...Qe3 (100) 175...Qe3 (150)", 1, 161,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/6pk/8/6n1/2NQ4/4q3/1KP1P3/8 w - - 150 176"));
    list.add(new PgnFileTestCase("15_black_initial_above_seventy_five_reoccuring_above_fifty.pgn", "", "",
        "19...NA (1) 69.NA (100) 94.NA (150) 100.NA (162); 101.Qd3 (1) 150...Qe3 (100) 175.Qd4 (149)", 1, 162,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/6pk/8/4q1n1/2NQ4/8/1KP1P3/8 b - - 149 175"));
    list.add(new PgnFileTestCase("16_black_initial_above_seventy_five_reoccuring_above_seventy_five.pgn", "", "",
        "19.NA (1) 68...NA (100) 93...NA (150) 100.NA (163); 101.Qd3 (1) 150...Qe3 (100) 175...Qe3 (150) 179...Qg3 (158)",
        1, 163, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "1Q6/6pk/8/6n1/2N5/6q1/1KP1P3/8 w - - 158 180"));

    return new PgnFileTestCaseList(PgnTest.BASIC_FROM_FEN_YAWN_BLACK, list);
  }

  private static PgnFileTestCaseList createTestCasesLong() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("nikolic_arsovic_1989.pgn",
        "repPos=3: 96.Rf5 98.Kd1 102.Kd1; repPos=3: 96...Rh1+ 98...Rh1+ 102...Rh1+; repPos=3: 145...Ra8 147...Ra8 151...Ra8; repPos=3: 203...Rf6+ 205...Rf6+ 207...Rf6+",
        "", "112.Rh8 (1) 161...Rc8 (100) 166...Rb5 (110); 167...Rb4+ (1) 217.Rh7 (100) 242.Kc3 (150) 269...Rg7 (205)",
        33, 205, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/6r1/8/8/3K4/3B4/5R2/3k4 w - - 205 270"));

    return new PgnFileTestCaseList(PgnTest.LONG, list);
  }

  private static PgnFileTestCaseList createTestCasesLongestMate() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("longest_mate_seven_pieces_rank_1.pgn", "", "",
        "43...Ke6 (1) 93.Kh3 (100) 118.Qe8 (150) 545...Kc5 (1005)", 6, 1005, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/6Qk/5Kb1/8/8/8/8 b - - 20 586"));
    list.add(new PgnFileTestCase("longest_mate_seven_pieces_rank_1_amend_repeat_position.pgn", "", "",
        "43...Ke6 (1) 93.Kh3 (100) 118.Qe8 (150) 341.Kb7 (596)", 6, 596, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "4Q3/1K6/8/5rk1/8/2bN1n2/8/8 b - - 596 341"));
    list.add(new PgnFileTestCase("longest_mate_seven_pieces_rank_1_amend_reproduce_and_repeat_position.pgn", "", "", "",
        3, 33, CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4Q3/1K6/8/5rk1/8/2bN1n2/8/8 b - - 33 40"));
    list.add(new PgnFileTestCase("longest_mate_seven_pieces_rank_2.pgn", "", "",
        "41...Kd6 (1) 91.Ka3 (100) 116.Qd8 (150) 545.Nc5+ (1008)", 5, 1008, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/2K5/k7/8/Q7/8/8 b - - 8 584"));
    list.add(new PgnFileTestCase("longest_mate_seven_pieces_rank_3.pgn", "", "",
        "45...Rg4+ (1) 95.Qd8+ (100) 120.Qb6 (150) 551.Nc5+ (1012)", 5, 1012, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/2K5/k7/8/Q7/8/8 b - - 8 590"));
    list.add(new PgnFileTestCase("longest_mate_seven_pieces_rank_4.pgn", "", "",
        "24...Kf8 (1) 74.Kf1 (100) 99.Qh8 (150) 550...Bb5 (1053)", 3, 1053, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/5K2/6Qk/8/8/8/8 b - - 4 585"));
    list.add(new PgnFileTestCase("longest_mate_seven_pieces_rank_5.pgn", "", "",
        "38.Kb4 (1) 87...Rf7 (100) 112...Bc4 (150) 535.Nc5+ (995)", 5, 995, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/2K5/k7/8/Q7/8/8 b - - 8 574"));
    list.add(new PgnFileTestCase("longest_mate_seven_pieces_rank_6.pgn", "", "",
        "53.Kc2 (1) 102...Kd2 (100) 127...Kf5 (150) 541...Bd2 (978)", 14, 978, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/2K5/kQ6/8/8/8 b - - 6 576"));

    return new PgnFileTestCaseList(PgnTest.LONGEST_MATE, list);
  }

  private static PgnFileTestCaseList createTestCasesLongestPossible() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    // longest possible game seventy-five-move rule
    list.add(new PgnFileTestCase("longest_game_seventy_five_move_rule_murphy.pgn", "", "",
        "1.Na3 (1) 50...Ng6 (100) 75.Nf3 (149); 76.Ne5 (1) 125...Qd8 (100) 150.Nc3 (149); 151.Ng5 (1) 200...Ne5 (100) 225.Ne5 (149); 226.Nb5 (1) 275...Bf5 (100) 300.Nd5 (149); 301.Ng6 (1) 350...Nf4 (100) 375.Nc4 (149); 376.Nb4 (1) 425...Nb4 (100) 450.Nf4 (149); 451.Rg1 (1) 500...Ne4 (100) 525.Rg1 (149); 526.Rh1 (1) 575...Ng3 (100) 600.Rh1 (149); 601.Rg1 (1) 650...Be4 (100) 675.Rg1 (149); 676.Rh1 (1) 725...Bd3 (100) 750.Rh1 (149); 751.Rg1 (1) 800...Qd8 (100) 824...Qd8 (148); 825...Nh6 (1) 875.Be5 (100) 899...Nc6 (149); 900...Ba6 (1) 950.Bf6 (100) 974...Nd4 (149); 975...Qb6 (1) 1025.Qb2 (100) 1049...Nb3 (149); 1050...Bg7 (1) 1100.Qb1 (100) 1124...Bb7 (149); 1125...Qb8 (1) 1175.Qc2 (100) 1199...Rc8 (149); 1200...Bc6 (1) 1250.Qd3 (100) 1274...Rc6 (149); 1275...Rf6 (1) 1325.Qb2 (100) 1349...Rd6 (149); 1350...Nh6 (1) 1400.Rh1 (100) 1424...Re6 (149); 1425...Nh6 (1) 1475.Qb2 (100) 1499...Rd6 (149); 1500...Qc7 (1) 1550.Qg3 (100) 1574...Re6 (149); 1575...Bh6 (1) 1625.Qc1 (100) 1649...Nh6 (149); 1650...Rg6 (1) 1700.Qe5 (100) 1724...Bh6 (149); 1725...Re3 (1) 1775.Bb2 (100) 1799...Kf8 (149); 1800...Ke8 (1) 1850.Qd5 (100) 1874...Ke8 (149); 1875...Re5 (1) 1925.Na5 (100) 1949...Ke8 (149); 1950...Rg8 (1) 2000.Qc2 (100) 2024...Kf8 (149); 2025...Nf6 (1) 2075.Bc1 (100) 2099...Ke8 (149); 2100...Rg8 (1) 2150.Qg3 (100) 2174...Bg5 (149); 2175...Kf7 (1) 2225.Qe5 (100) 2249...Bh4 (149); 2250...Bg3 (1) 2300.Qf8+ (100) 2324...Bg3 (149); 2325...Rg6 (1) 2375.Nd8 (100) 2399...Qc8 (149); 2400...Rh6 (1) 2450.Qb3 (100) 2474...Kc7 (149); 2475...Kd8 (1) 2525.Qg7 (100) 2549...Kb6 (149); 2550...Qa5 (1) 2600.Nb3 (100) 2624...Rf6 (149); 2625...Qa5 (1) 2675.Qge5 (100) 2699...Re6 (149); 2700...Kb5 (1) 2750.Rh6 (100) 2774...Rf6 (149); 2775...Rh6 (1) 2825.Qgg6 (100) 2849...Re6 (149); 2850...Re4 (1) 2900.Qcc3 (100) 2924...Rf6 (149); 2925...Qa4 (1) 2975.Qf6 (100) 2999...Rf6 (149); 3000...Rc6 (1) 3050.Qe5 (100) 3074...Rg8 (149); 3075...Rgg6 (1) 3125.Nc6 (100) 3149...Rgg6 (149); 3150...Rde6 (1) 3200.Ng7 (100) 3224...Rge6 (149); 3225...Rd3 (1) 3275.Nf6 (100) 3299...Rf6 (149); 3300...Rd3 (1) 3350.Nb4 (100) 3374...Rc6 (149); 3375...Rc8 (1) 3425.Qh8 (100) 3449...Qa6 (149); 3450...Qa5 (1) 3500.Rg1 (100) 3524...Qa5 (149); 3525...Qa2 (1) 3575.Ng4 (100) 3599...Qa6 (149); 3600...Kc7 (1) 3650.Qf7 (100) 3674...Qa5 (149); 3675...Rh6 (1) 3725.Qe4 (100) 3749...Qa6 (149); 3750...Kc7 (1) 3800.Nb4 (100) 3824...Qa5 (149); 3825...Rd6 (1) 3875.Qc2 (100) 3899...Qa6 (149); 3900...Qa5 (1) 3950.Rg1 (100) 3974...Qa6 (149); 3975...Qa3 (1) 4025.Qc3 (100) 4049...Qa5 (149); 4050...Rg6 (1) 4100.Ng7 (100) 4124...Qa6 (149); 4125...Rf7 (1) 4175.Qh3 (100) 4199...Qa5 (149); 4200...Qa6 (1) 4250.Qd1 (100) 4274...Qa5 (149); 4275...Qa3 (1) 4325.Nb8 (100) 4349...Qa5 (149); 4350...Kb7 (1) 4400.Qh3 (100) 4424...Qa3 (149); 4425...Kc7 (1) 4475.Qf3+ (100) 4499.Ra3 (148); 4500.Qg5 (1) 4549...Kc6 (100) 4574.Kh2 (149); 4575.Ne3 (1) 4624...Kb6 (100) 4649.Bd3 (149); 4650.Rc3 (1) 4699...Kb5 (100) 4724.Qf4 (149); 4725.Ne4 (1) 4774...Rd4 (100) 4799.Ng7 (149); 4800.Nb1 (1) 4849...Kb5 (100) 4874.Be4 (149); 4875.Qe3 (1) 4924...Rg5 (100) 4949.Ne6 (149); 4950.Rc3 (1) 4999...Re2 (100) 5024.Nf6 (149); 5025.Ra6+ (1) 5074...Ra6 (100) 5099.Ng8 (149); 5100.Ra1 (1) 5149...Ra4 (100) 5174.Nf6 (149); 5175.Re3 (1) 5224...Rh4 (100) 5249.Rd3 (149); 5250.Re1 (1) 5299...Bd1 (100) 5324.Bb2 (149); 5325.Nf1 (1) 5374...Bf5 (100) 5399.Bc3 (149); 5400.Qh4 (1) 5449...Bg2 (100) 5474.Bb2 (149); 5475.Qf1 (1) 5524...Kb5 (100) 5549.Bc3 (149); 5550.Qf1 (1) 5599...Re1+ (100) 5624.Bb2 (149); 5625.Nd8 (1) 5674...Be4 (100) 5699.Bc3 (149); 5700.Nb2 (1) 5749...Kc7 (100) 5774.Ba5+ (149); 5775.Nb2 (1) 5824...Rf1 (100) 5849.Nf4 (149); 5850.Qf5 (1) 5899...Rb4 (100) 5924.Ne2 (149); 5925.Qh4 (1) 5974...Kb6 (100) 5999.Nf4 (149); 6000.Qg7 (1) 6049...Re2 (100) 6074.Ne2 (149); 6075.Rd8 (1) 6124...Ra5 (100) 6149.Ng4 (149); 6150.Nb1 (1) 6199...Ra1 (100) 6224.Nge3 (149); 6225.Rg1 (1) 6274...Rg4 (100) 6299.Nf5 (149); 6300.Qd4+ (1) 6349...Kd6 (100) 6374.Nfe3 (149); 6375.Qh5 (1) 6424...Be4 (100) 6449.Re1 (149); 6450.Nb1 (1) 6499...Bf5 (100) 6524.Qf3 (149); 6525.Qd4+ (1) 6574...Rac3 (100) 6599.Kg2 (149); 6600.Kf2 (1) 6649...Bd3 (100) 6674.Re1 (149); 6675.Rd8 (1) 6724...Bd3 (100) 6749.Qfh5 (149); 6750.Nc2 (1) 6799...Bd3 (100) 6824.Ng4 (149); 6825.Rd8 (1) 6874...Rg2+ (100) 6899.Nc4+ (149); 6900.Q8h7 (1) 6949...Ka4 (100) 6974.Nb2 (149); 6975.Ra8 (1) 7024...Rg8 (100) 7049.Rg1 (149); 7050.Q8e5 (1) 7099...Re6 (100) 7124.Rg2 (149); 7125.Qc5+ (1) 7174...Ne2 (100) 7199.Kf3 (149); 7200.Kf2 (1) 7249...Rd4 (100) 7274.Kf2 (149); 7275.Qf7 (1) 7324...Rhh4 (100) 7349.Qh2 (149); 7350.Rg3 (1) 7399...Rc1 (100) 7424.Kf3 (149); 7425.Q8h6 (1) 7474...Rf1+ (100) 7499.Kf4 (149); 7500.Kf5 (1) 7549...Rhh1 (100) 7574.Qh2 (149); 7575.Q8e5 (1) 7624...Na2 (100) 7649.Kf6 (149); 7650.Q8h5 (1) 7699...Nc3 (100) 7724.Rg3 (149); 7725.Qg2 (1) 7774...Rcc1 (100) 7799.Rg2 (149); 7800.Q2h5 (1) 7849...Bc4 (100) 7874.Nd3 (149); 7875.Rf2 (1) 7924...Kb5 (100) 7949.Rg1 (149); 7950.Re4 (1) 7999...Rgg1 (100) 8024.Q8h3 (149); 8025.Rf7 (1) 8074...Rgd1 (100) 8099.Ra8 (149); 8100.Qd6+ (1) 8149...Rh8 (100) 8174.Rb8+ (149); 8175.Qf2+ (1) 8224...Bc4 (100) 8248...Qe1 (148); 8249...Kc6 (1) 8299.Qf6 (100) 8323...Bf7 (149); 8324...Kb6 (1) 8374.Kf5 (100) 8398...Rg5 (149); 8399...Rf3+ (1) 8449.Qc1 (100) 8473...Ka6 (149); 8474...Bd5 (1) 8524.Qa7+ (100) 8548...Kb6 (149); 8549...Ka7 (1) 8599.Ke5 (100) 8623...Kc5 (149); 8624...Rgg1 (1) 8674.Qa5 (100) 8698...Kc6 (149); 8699...Kb6 (1) 8749.Qe5 (100) 8773...Rg7 (149); 8774...Kd8 (1) 8824.Qd2+ (100) 8849.Qd7# (150)",
        750, 150, CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "3k4/3Q4/4K3/8/8/8/8/8 b - - 150 8849"));
    // longest possible game fifty-five-move rule
    list.add(new PgnFileTestCase("longest_game_fifty_move_rule_labelle.pgn", "", "",
        "1.Nf3 (1) 50...Nf4 (100) 75.Rh1 (149); 76.Nc4 (1) 125...Qg4 (100) 150.Rg1 (149); 151.Nh3 (1) 200...Rf8 (100) 225.Nf6 (149); 226.Rg1 (1) 275...Rdb8 (100) 300.Nc4 (149); 301.Nf4 (1) 350...Rh8 (100) 375.Rh1 (149); 376.Ne6 (1) 425...Rd7 (100) 450.Ng3 (149); 451.Nf5 (1) 500...Ra8 (100) 525.Ne8 (149); 526.Nec7 (1) 575...Ne6 (100) 600.Nf5 (149); 601.Ng3 (1) 650...Qe5 (100) 675.Ra1 (149); 676.Nf5 (1) 725...Rg4 (100) 750.Rg1 (149); 751.Ng3 (1) 800...Re8 (100) 825.Nf3 (149); 826.Nf4 (1) 875...Ka4 (100) 900.N8c7 (149); 901.Rb1 (1) 950...Rh4 (100) 975.Na3 (149); 976.Nc4 (1) 1025...Bg7 (100) 1049...Rh8 (148); 1050...Re8 (1) 1100.Nge4 (100) 1124...Ne5 (149); 1125...Bd8 (1) 1175.Kg2 (100) 1199...Nd5 (149); 1200...Bb5 (1) 1250.Bb2 (100) 1274...Bg7 (149); 1275...Rg8 (1) 1325.Rf1 (100) 1349...Bg5 (149); 1350...Re4 (1) 1400.Bd5 (100) 1424...Re4 (149); 1425...Rg8 (1) 1475.Bd5 (100) 1499...Na3 (149); 1500...Bb1 (1) 1550.Qd8 (100) 1574...Ka6 (149); 1575...Bf7 (1) 1625.Rh1 (100) 1649...Nc4 (149); 1650...Na3 (1) 1700.Rg2 (100) 1724...Rd8 (149); 1725...Rg4 (1) 1775.Qg7 (100) 1799...Bc6 (149); 1800...Rh1 (1) 1850.Ne1 (100) 1874...Qc6 (149); 1875...Bd5 (1) 1925.Rab1 (100) 1949...Qg8 (149); 1950...Bd6 (1) 2000.Kb2 (100) 2024...Kb8 (149); 2025...Qc6 (1) 2075.Rh2 (100) 2099...Rh8 (149); 2100...Re8 (1) 2150.Ra4 (100) 2174...Bc6 (149); 2175...Bf4 (1) 2225.Qf3 (100) 2249...Be4 (149); 2250...Bg3 (1) 2300.Rg1 (100) 2324...Re1 (149); 2325...Qg3 (1) 2375.Rc1 (100) 2399...Be7 (149); 2400...Rf3 (1) 2450.Nh6 (100) 2474...Bb7 (149); 2475...Bd5 (1) 2525.Rc1 (100) 2549...Rh2 (149); 2550...Be5 (1) 2600.Nd6 (100) 2624...Bf6 (149); 2625...Qe7 (1) 2675.Nh6 (100) 2699...Re8 (149); 2700...Rd8 (1) 2750.Nf6 (100) 2774...Rc8 (149); 2775...Ng8 (1) 2825.Bb2 (100) 2849...Rc8 (149); 2850...Ne4 (1) 2900.Bb4 (100) 2924...Rc8 (149); 2925...Kf8 (1) 2975.Bb4 (100) 2999...Rh2 (149); 3000...Kf8 (1) 3050.Ne4 (100) 3074...Rh1 (149); 3075...Rh3 (1) 3125.Ncd2 (100) 3149...Qc6 (149); 3150...Rg3 (1) 3200.Qba1 (100) 3224...Kg7 (149); 3225...Qg2 (1) 3275.Qb8 (100) 3299...Rf2 (149); 3300...Qe1 (1) 3350.Qdc6 (100) 3374...Rd8 (149); 3375...Rc1 (1) 3425.Qbb4 (100) 3449...Qd6 (149); 3450...Re8 (1) 3500.Qab2 (100) 3524...Rb8 (149); 3525...Kf8 (1) 3575.Ka2 (100) 3599...Qd6 (149); 3600...Re8 (1) 3650.Bf2 (100) 3674...Qb7 (149); 3675...Rg8 (1) 3725.Q4g3 (100) 3749...Qd6 (149); 3750...Qe5 (1) 3800.Qab2 (100) 3824...Qb8 (149); 3825...Ke8 (1) 3875.Rg5 (100) 3899...Qg8 (149); 3900...Qd8 (1) 3950.Bb4 (100) 3974...Qc6 (149); 3975...Ke7 (1) 4025.Bc2 (100) 4049...Kf6 (149); 4050...Kf7 (1) 4100.Qfg2 (100) 4124...Kg7 (149); 4125...Kf6 (1) 4175.Ra2 (100) 4199.Qge1 (148); 4200.Qeg1 (1) 4249...Kf8 (100) 4274.Qf4 (149); 4275.Qc2 (1) 4324...Kg7 (100) 4349.Rf2 (149); 4350.Bg3 (1) 4399...Kg7 (100) 4424.Qgg2 (149); 4425.Qbb1 (1) 4474...Kg7 (100) 4499.Qge4 (149); 4500.Q4d3 (1) 4549...Qe8 (100) 4574.Nf2 (149); 4575.Q2a1 (1) 4624...Qd5 (100) 4649.Q2e3 (149); 4650.Qd6 (1) 4699...Kf7 (100) 4724.Qed1 (149); 4725.Qgb3 (1) 4774...Kg8 (100) 4799.Q4b2 (149); 4800.Qb1c1 (1) 4849...Qf6 (100) 4874.Qhh1 (149); 4875.Bf6 (1) 4924...Qd6 (100) 4949.Qgf3 (149); 4950.Qfd6 (1) 4999...Kg7 (100) 5024.Rb4 (149); 5025.Qcc5 (1) 5074...Qf7 (100) 5099.Qec7 (149); 5100.Qdd6 (1) 5149...Qe5 (100) 5174.Qbc5 (149); 5175.Qa8 (1) 5224...Qc4 (100) 5249.Q2h1 (149); 5250.Q5a2 (1) 5299...Qb1 (100) 5324.Qca3 (149); 5325.Qhd6 (1) 5374...Qh4 (100) 5399.Bd6 (149); 5400.Qac1 (1) 5449...Qef7 (100) 5474.Qc8 (149); 5475.Qbd4 (1) 5524...Qfe7 (100) 5549.Qbb1 (149); 5550.Q8e5 (1) 5599...Qh4 (100) 5624.Qeg7 (149); 5625.Qgc7 (1) 5674...Qae3 (100) 5699.Q3d2 (149); 5700.Nf7 (1) 5749...Qc5 (100) 5774.Qad4 (149); 5775.Qdd5 (1) 5824...Qh8 (100) 5849.Qed8 (149); 5850.Q7e7 (1) 5899...Qaf3 (100) 5924.Qcc1 (149); 5925.Q3c2 (1) 5974...Qee8 (100) 5999.Qbb5 (149); 6000.Qgd3 (1) 6049...Qc2 (100) 6074.Be3 (149); 6075.Bd2 (1) 6124...Qd3 (100) 6149.Qdf8 (149); 6150.Rc1 (1) 6199...Q4b4 (100) 6224.Ne3 (149); 6225.Qbd1 (1) 6274...Q6e6 (100) 6299.Q6f5 (149); 6300.Qff8 (1) 6349...Qfh4 (100) 6374.Qdd1 (149); 6375.Qed6 (1) 6424...Qc1 (100) 6449.Qda3 (149); 6450.Bh2 (1) 6499...Qfd5 (100) 6524.Qdb3 (149); 6525.Qff2 (1) 6574...Qcc4 (100) 6599.Qgd8 (149); 6600.Qbb8 (1) 6649...Qdc5 (100) 6674.Qhh6 (149); 6675.Q2d2 (1) 6724...Qhe1 (100) 6749.Qd2 (149); 6750.Qfe8 (1) 6799...Q4d4 (100) 6824.Qde5 (149); 6825.Qff6 (1) 6874...Qfg1 (100) 6899.Qed8 (149); 6900.Qbe8 (1) 6949...Qff6 (100) 6974.Qd7 (149); 6975.Qff6 (1) 7024...Qgh6 (100) 7049.Qce6 (149); 7050.Qg4 (1) 7099...Q8h5 (100) 7124.Qb6b7 (149); 7125.Qf8 (1) 7174...Qhf7 (100) 7199.Qbc8 (149); 7200.Qf2 (1) 7249...Qcd2 (100) 7274.Qdd1 (149); 7275.Qd2 (1) 7324...Qef4 (100) 7349.Qdb7 (149); 7350.Qbc2 (1) 7399...Qgf2 (100) 7424.Qg5 (149); 7425.Qb1 (1) 7474...Qda1 (100) 7499.Qaa5 (149); 7500.Qf6 (1) 7549...Qg6 (100) 7574.Qc4 (149); 7575.Qc7 (1) 7624...Qdd1 (100) 7649.Kb8 (149); 7650.Qb5 (1) 7699...Kh4 (100) 7724.Qf6+ (149); 7725.Kc7 (1) 7774...Qee5+ (100) 7798...Qff5 (148); 7799...Qfc2 (1) 7849.Kb8 (100) 7873...Kd6 (149); 7874...Qfc1 (1) 7924.Kc8 (100) 7948...Qhd2 (149); 7949...Qgg1 (1) 7999.Ka7 (100) 8023...Qdf2 (149); 8024...Q2e2 (1) 8074.Kf7 (100) 8098...Qaa1 (149); 8099...Qe1e4 (1) 8149.Kg8 (100) 8173...Q3b1 (149); 8174...Qf2 (1) 8224.Qe4 (100) 8248...Qab8 (149); 8249...Q1d2 (1) 8299.Qf6 (100) 8323...Qh7+ (149); 8324...Qba2 (1) 8374.Qd8 (100) 8398...Qee1 (149); 8399...Qgc5 (1) 8449.Qd6 (100) 8473...Qb2 (149); 8474...Qba1 (1) 8524.Qb5 (100) 8548...Qa7 (149); 8549...Qf6 (1) 8599.Qc7 (100) 8623...Qga8 (149); 8624...Qc4 (1) 8674.Qf5 (100) 8698...Qf5 (149); 8699...Kh4 (1) 8749.Kc5 (100) 8773...Qe1 (149); 8774...Kg7 (1) 8824.Qf3+ (100) 8849.Qg4# (150)",
        4199, 150, CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/8/8/5K2/6Qk/8/8/8 b - - 150 8849"));

    return new PgnFileTestCaseList(PgnTest.LONGEST_POSSIBLE, list);
  }

  private static PgnFileTestCaseList createTestCasesRandomNoRepetition() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("no_repetition_1.pgn", "", "",
        "162.Rd3 (1) 211...Kh8 (100) 236...Ka8 (150) 1145...Ka1 (1968)", 10, 1968, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "7K/8/8/8/8/1R6/8/k7 w - - 1968 1146"));
    list.add(new PgnFileTestCase("no_repetition_2.pgn", "", "",
        "180...Ke8 (1) 230.Ka8 (100) 255.Kg6 (150) 880.Kh1 (1400)", 10, 1400, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/1k6/8/4r3/7K b - - 1400 880"));
    list.add(new PgnFileTestCase("no_repetition_3.pgn", "", "",
        "106.Rg5 (1) 155...Kg1 (100) 180...Kd7 (150) 457...Kh8 (704)", 14, 704, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "7k/3K4/8/8/8/6R1/8/8 w - - 704 458"));
    list.add(new PgnFileTestCase("no_repetition_4.pgn", "", "",
        "122...Kc7 (1) 172.Rd1 (100) 189.Re2 (134); 190.Re6+ (1) 239...Kf4 (100) 246.Rd8 (113); 247.Kb3 (1) 296...Ka5 (100) 321...Kg1 (150) 324...Kh1 (156)",
        27, 156, CheckmateOrStalemate.NA, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/8/8/8/4K3/8/4R3/7k w - - 156 325"));
    list.add(new PgnFileTestCase("no_repetition_5.pgn", "", "", "", 18, 79, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "4k3/8/3B4/8/8/8/4q3/K7 b - - 79 182"));

    return new PgnFileTestCaseList(PgnTest.RANDOM_NO_REPETITION, list);
  }

  private static PgnFileTestCaseList createTestCasesRandomCheckmate() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("random_checkmate_1.pgn", "", "", "", 5, 3, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "3qkbnr/pb1pp2p/p1r2p2/2p1P1pQ/7P/2P4N/PPNP1PP1/1RB1K2R b k - 3 12"));
    list.add(new PgnFileTestCase("random_checkmate_2.pgn", "", "", "", 9, 15, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "5k1Q/R4P2/8/p7/7P/4p2N/7K/3r4 b - - 2 82"));
    list.add(new PgnFileTestCase("random_checkmate_3.pgn", "", "", "", 19, 64, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "2b5/8/K7/2q4p/5k2/8/8/8 w - - 13 180"));
    list.add(new PgnFileTestCase("random_checkmate_4.pgn", "", "", "", 4, 17, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "5k2/8/1b6/7K/4p3/3R2q1/p6r/8 w - - 7 119"));
    list.add(new PgnFileTestCase("random_checkmate_5.pgn", "", "", "", 15, 89, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "4K3/4q3/3k4/8/8/7N/8/8 w - - 8 215"));

    return new PgnFileTestCaseList(PgnTest.RANDOM_CHECKMATE, list);
  }

  private static PgnFileTestCaseList createTestCasesRandomStalemate() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("random_stalemate_1.pgn", "", "", "", 13, 93, CheckmateOrStalemate.STALEMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "K7/8/1q4k1/8/8/8/8/8 w - - 93 233"));
    list.add(new PgnFileTestCase("random_stalemate_2.pgn", "", "", "", 21, 92, CheckmateOrStalemate.STALEMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "k7/8/1Q6/3K4/p7/P1B5/8/8 b - - 92 191"));
    list.add(new PgnFileTestCase("random_stalemate_3.pgn", "", "", "", 14, 40, CheckmateOrStalemate.STALEMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "5K2/3k1P2/p7/6r1/8/8/8/8 w - - 1 122"));
    list.add(new PgnFileTestCase("random_stalemate_4.pgn", "", "", "", 13, 9, CheckmateOrStalemate.STALEMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "6k1/6P1/p3N2P/P1p5/2P1B3/N2K4/7P/R4R2 b - - 2 46"));
    list.add(new PgnFileTestCase("random_stalemate_5.pgn", "", "", "", 16, 93, CheckmateOrStalemate.STALEMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/6p1/3b2P1/8/8/2k5/K7 w - - 72 250"));

    return new PgnFileTestCaseList(PgnTest.RANDOM_STALEMATE, list);
  }

  private static PgnFileTestCaseList createTestCasesRandomInsufficientMaterial() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("random_insufficient_material_1.pgn", "", "", "", 16, 38, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/5k2/8/8/8/8/8/2K5 b - - 0 153"));
    list.add(new PgnFileTestCase("random_insufficient_material_2.pgn", "", "", "", 19, 66, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/7k/8/8/8/8/6K1/8 b - - 0 191"));
    list.add(new PgnFileTestCase("random_insufficient_material_3.pgn", "", "", "", 10, 95, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/8/8/8/1K6/5k2 w - - 0 177"));
    list.add(new PgnFileTestCase("random_insufficient_material_4.pgn", "", "", "", 16, 82, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/4K3/8/5k2/8/8/8 w - - 0 232"));
    list.add(new PgnFileTestCase("random_insufficient_material_5.pgn", "", "", "", 10, 95, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/6k1/8/4N3/8/8/2K5/8 b - - 0 204"));

    return new PgnFileTestCaseList(PgnTest.RANDOM_INSUFFICIENT_MATERIAL, list);
  }

  private static PgnFileTestCaseList createTestCasesRandomFifty() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("random_fifty_1.pgn", "", "", "146...Ke8 (1) 196.Kg1 (100)", 9, 100,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "8/1k6/8/2B5/8/8/8/1b4K1 b - - 100 196"));
    list.add(new PgnFileTestCase("random_fifty_2.pgn", "", "", "164.Rc3+ (1) 213...Kf2 (100)", 11, 100,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/n7/3K4/3R4/8/5k2/8 w - - 100 214"));
    list.add(new PgnFileTestCase("random_fifty_3.pgn", "", "", "126.Kh6 (1) 175...Qb1 (100)", 26, 100,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "8/2K5/6k1/8/8/8/8/1q6 w - - 100 176"));
    list.add(new PgnFileTestCase("random_fifty_4.pgn", "", "", "179.Kh4 (1) 228...Ka7 (100)", 26, 100,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/k7/8/5N2/8/7K/6N1/8 w - - 100 229"));
    list.add(new PgnFileTestCase("random_fifty_5.pgn", "", "", "137.Kb5 (1) 186...Nd2 (100)", 8, 100,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "8/5k2/8/K7/8/8/1B1n4/8 w - - 100 187"));

    return new PgnFileTestCaseList(PgnTest.RANDOM_FIFTY, list);
  }

  private static PgnFileTestCaseList createTestCasesRandomSeventyFive() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(
        new PgnFileTestCase("random_seventy_five_1.pgn", "", "", "176.Bf7 (1) 225...Kd6 (100) 250...Kd6 (150)", 8, 150,
            CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
            UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/5B2/K2k4/7p/7P/8/8/8 w - - 150 251"));
    list.add(new PgnFileTestCase("random_seventy_five_2.pgn", "", "", "198.Re8 (1) 247...Kg2 (100) 272...Ka1 (150)", 15,
        150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/8/K7/8/8/8/5R2/k7 w - - 150 273"));
    list.add(new PgnFileTestCase("random_seventy_five_3.pgn", "", "", "162.Kb2 (1) 211...Ke4 (100) 236...Kh6 (150)", 20,
        150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/8/3q3k/8/8/8/8/2K5 w - - 150 237"));
    list.add(
        new PgnFileTestCase("random_seventy_five_4.pgn", "", "", "132.Kg8 (1) 181...Ng2 (100) 206...Kc2 (150)", 22, 150,
            CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
            UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "2K5/2N5/8/8/8/8/2kn4/8 w - - 150 207"));

    return new PgnFileTestCaseList(PgnTest.RANDOM_SEVENTY_FIVE, list);
  }

  private static PgnFileTestCaseList createTestCasesRandomThreefold() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("random_threefold_1.pgn", "repPos=3: 110...Bf2 114...Bf2 119...Bf2", "", "", 16, 27,
        CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/7K/k7/p6p/P6P/5p2/5b2/8 w - - 27 120"));
    list.add(new PgnFileTestCase("random_threefold_2.pgn", "repPos=3: 197.Kf7 199.Kf7 201.Kf7", "", "", 13, 99,
        CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "8/5K2/2k5/1p6/pP6/P7/8/8 b - - 99 201"));
    list.add(new PgnFileTestCase("random_threefold_3.pgn", "repPos=3: 158.Ke3 162.Ke3 171.Ke3", "", "", 36, 40,
        CheckmateOrStalemate.NA, 3, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "k7/8/8/P7/8/4K3/8/8 b - - 40 171"));
    list.add(new PgnFileTestCase("random_threefold_4.pgn", "repPos=3: 188.Kg4 190.Rf5 193.Rf5", "", "", 32, 96,
        CheckmateOrStalemate.NA, 3, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/6k1/8/5R2/6K1/8/8/8 b - - 96 193"));
    list.add(new PgnFileTestCase("random_threefold_5.pgn", "repPos=3: 198...Ke3 200...Ke3 202...Ke3", "", "", 11, 38,
        CheckmateOrStalemate.NA, 3, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/1K6/8/4k3/7p/8 w - - 26 203"));

    return new PgnFileTestCaseList(PgnTest.RANDOM_THREEFOLD, list);
  }

  private static PgnFileTestCaseList createTestCasesRandomFivefold() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("random_fivefold_1.pgn",
        "repPos=3: 149.Kxd3 165.Kd3 171.Kd3; repPos=4: 156.Kb2 173.Kb2 183.Kb2 193.Kb2; repPos=4: 156...Kc8 158...Kc8 160...Kc8 183...Kc8; repPos=3: 172...Kb7 189...Kb7 192...Kb7; repPos=5: 173...Ka6 175...Ka6 186...Ka6 188...Ka6 193...Ka6; repPos=3: 175.Kb2 186.Kb2 188.Kb2",
        "", "", 22, 89, CheckmateOrStalemate.NA, 5, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/8/k7/p7/P7/8/1K6/8 w - - 89 194"));
    list.add(new PgnFileTestCase("random_fivefold_2.pgn",
        "repPos=3: 136.Nd7 138.Kb4 140.Kb4; repPos=3: 212...Rd3 214...Rd3 216...Rd3; repPos=5: 251...Kb7 255...Rc8 257...Rc8 259...Rc8 261...Rc8; repPos=3: 254...Rc5 258...Rc5 260...Rc5; repPos=4: 255.Kb2 257.Kb2 259.Kb2 261.Kb2",
        "", "", 13, 92, CheckmateOrStalemate.NA, 5, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "2r5/1k6/8/8/8/8/1K6/8 w - - 71 262"));

    return new PgnFileTestCaseList(PgnTest.RANDOM_FIVEFOLD, list);
  }

  private static PgnFileTestCaseList createTestCasesGamesVarious() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("almtwali_vs_danielbaechli_2020.pgn", "", "", "", 14, 91, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "6k1/4P2p/5K1P/8/8/8/8/8 b - - 0 114"));
    list.add(new PgnFileTestCase("krush_zatonski_2008.pgn", "", "", "", 13, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "6rk/1p1R1pqp/p1p4p/6rP/P7/5QP1/1P3P2/4R1K1 b - - 3 33"));
    list.add(new PgnFileTestCase("krush_zatonski_2008_reconstructed.pgn", "", "", "", 13, 11, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r5qk/1R2Rprp/5Q1p/p6P/P7/2P3P1/5P2/6K1 w - - 11 47"));
    list.add(new PgnFileTestCase("rikikits_vs_demchenko_2016_amended.pgn",
        "repPos=3: 48.Kc7 50.Kc7 52.Kc7; repPos=3: 48...Bd5 50...Bd5 52...Bd5; repPos=3: 49.Kd6 51.Kd6 53.Kd6; repPos=3: 49...Be6 51...Be6 53...Be6; repPos=4: 73.Kf4 75.Kf4 81.Kf4 85.Kf4; repPos=3: 76...Kb5 80...Be6 86...Kb5",
        "", "112.Kd2 (1) 161...Bb5 (100) 163.Bh8 (103)", 7, 103, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "7B/5p2/2k5/K3P3/1P6/3b4/8/8 b - - 103 163"));
    list.add(new PgnFileTestCase("demchenko_vs_verdenotte_2018_amended.pgn", "", "",
        "88...Rb3 (1) 138.Ra1 (100) 141...Rb6 (107)", 6, 107, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/1r6/3B4/1k1K4/8/R7/8 w - - 107 142"));
    list.add(new PgnFileTestCase("demchenko_vs_chesspanda123_2017_amended.pgn", "", "",
        "61...Re1+ (1) 111.Kc5 (100) 136.Rg8+ (150)", 39, 150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "6R1/8/8/8/4B3/4K3/5r2/6k1 b - - 150 136"));
    list.add(new PgnFileTestCase("savic_vs_bueble_2020.pgn",
        "repPos=3: 76.Rb6 80.Kh6 84.Rb6; repPos=3: 76...Ra8 80...Ra8 84...Ra8", "", "", 12, 31, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "5k2/8/6K1/8/8/8/8/8 w - - 0 92"));
    list.add(new PgnFileTestCase("blatny_holzke_1997.pgn", "", "repPos=3: 47...f5 49...Kd6 51...Kd6", "", 8, 24,
        CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/3R2bp/4n1p1/1p1k1pP1/5P1P/1K2P3/8/8 b - - 24 64"));
    list.add(new PgnFileTestCase("gvetadze_milliet_2014.pgn", "repPos=3: 25.Qg6+ 27.Qg6+ 29.Qg6+",
        "repPos=3: 24...f5 26...Kg8 28...Kg8", "", 10, 9, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "2q2rk1/2r1b3/1p2p1Q1/4Pp2/8/3B4/1P3PPP/3R2K1 b - - 9 29"));
    // O-O-O+ was played in this game!!!
    list.add(new PgnFileTestCase("hikaru_vs_penguingm1_2014.pgn", "", "", "", 11, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "2k5/1pp1r3/p5pp/2P5/1P6/P2pP1PP/4PK2/8 w - - 0 36"));

    // "threefold repetition" with initial en passant
    list.add(new PgnFileTestCase("gmjoey1_vs_bugsbunny444_2013.pgn", "repPos=3: 18.Qg6+ 20.Qg6+ 22.Qg6+",
        "repPos=3: 17...f5 19...Kg8 21...Kg8", "", 18, 9, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rn1q1rk1/pp1b4/4p1Q1/bN1pPp2/3P4/PpPB4/5PPP/R3K2R b KQ - 9 22"));
    list.add(new PgnFileTestCase("gmjoey1_vs_tiohoracio_2015.pgn",
        "repPos=3: 23.Qh7+ 25.Qh7+ 27.Qh7+; repPos=3: 23...Kf7 25...Kf7 27...Kf7",
        "repPos=3: 22...f5 24...Kg8 26...Kg8", "", 6, 10, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.WINNABLE,
        "r1b2r2/1p3kpQ/4p3/4Pp2/p6R/2q5/P5PP/1R5K w - - 10 28"));
    list.add(new PgnFileTestCase("gmjoey1_vs_dulerile_2018.pgn",
        "repPos=3: 45.Rc8+ 47.Rc8+ 49.Rc8+; repPos=3: 45...Kb7 47...Kb7 49...Kb7",
        "repPos=3: 44...a5 46...Ka8 48...Ka8", "", 6, 10, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "2R5/1k3p2/1p4p1/pP1b3p/P4B1P/6P1/1r6/5K2 w - - 10 50"));

    // checkmating with castling
    list.add(new PgnFileTestCase("lasker_vs_alan_thomas_1912.pgn", "", "", "", 9, 6, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "rn3r2/pbppq1p1/1p2pN2/8/3P2NP/6P1/PPP1BP1R/2KR2k1 b - - 6 18"));

    // fooling around as long as I could
    list.add(new PgnFileTestCase("mickeymousetest_donaldducktest_2021.pgn", "", "", "339.Qd6 (1) 388...Ne3 (100)", 49,
        100, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbq1b1r/4k3/8/7N/4P2p/4n3/8/R1BQKBNR w - - 100 389"));

    list.add(new PgnFileTestCase("jobava_so_2017.pgn", "repPos=3: 37.Rxa7+ 39.Ra7+ 41.Ra7+", "", "", 5, 8,
        CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/R3k3/8/6N1/5p1P/P1Pr4/1P2r3/2K5 b - - 8 41"));

    // fifty-move claim but only 67 half-moves
    list.add(new PgnFileTestCase("gunina_harika_2019.pgn", "", "", "", 15, 67, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "R7/1n6/2K5/8/8/8/2k5/8 b - - 67 101"));

    // knight-bishop checkmate in blitz unsuccessful
    list.add(new PgnFileTestCase("drozdova_tan_2018.pgn", "", "",
        "72.Kf4 (1) 121...Nd5 (100) 146...Ne4 (150) 155...Bh3 (168)", 8, 168, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/8/3n1k1b/8/6K1 w - - 168 156"));

    // 75-move-rule exceeded by 116.5 full moves - no arbiter call
    list.add(new PgnFileTestCase("anikonov_zhigalko_2018.pgn", "", "", "104.Ra8 (1) 153...Ke5 (100) 166...Kb5 (126)", 8,
        126, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "2R5/4r3/8/1k1n4/8/5K2/8/8 w - - 126 167"));

    // KRvKRB played over fifty moves, under seventy-five moves
    list.add(new PgnFileTestCase("kevlishvili_zhigalko_2018.pgn",
        "repPos=4: 95.Kd3 101.Nf5 144.Nf5 166.Kd3; repPos=3: 95...Nd4 99...Nd4 132...Nd4; repPos=3: 96.Ne7 100.Ne7 133.Ne7; repPos=3: 96...Nc6 131...Nc6 133...Nc6; repPos=3: 97.Nf5 132.Nf5 134.Nf5; repPos=3: 101...Kc6 144...Kc6 166...Kc6; repPos=3: 102.Ne7+ 145.Ne7+ 167.Ne7+; repPos=3: 102...Kd7 145...Kd7 167...Kd7; repPos=3: 103.Nf5 146.Nf5 168.Nf5; repPos=3: 103...Nc5+ 146...Nc5+ 168...Nc5+; repPos=3: 104.Ke3 147.Ke3 169.Ke3; repPos=3: 104...Ke6 147...Ke6 169...Ke6; repPos=3: 105.Ng3 148.Ng3 170.Ng3; repPos=3: 106.Ne2 108.Ne2 171.Ne2",
        "", "64.Nd5 (1) 113...Kc6 (100) 138...Nf6 (150) 180.Kc4 (233)", 6, 233, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/8/5N1n/4K3/8/3k4 b - - 0 192"));

    // KRvKRN - white about being mated lost on time
    list.add(new PgnFileTestCase("harikrishna_hovhannisyan_2018.pgn", "", "", "", 32, 91, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.WINNABLE, "8/K1k5/3n1R2/8/7r/8/8/8 w - - 91 128"));

    // KvKNN - unclear why game was stopped
    list.add(new PgnFileTestCase("bazeev_riazantsev_2018.pgn", "", "", "59.Ke3 (1) 108...Ng6+ (100) 109...Nf8+ (102)",
        8, 102, CheckmateOrStalemate.NA, 1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "5n2/5k1K/8/8/6n1/8/8/8 w - - 102 110"));

    // Blitz, was it fivefold? No, only threefold
    list.add(new PgnFileTestCase("khademalsharieh_bodnaruk_2018.pgn", "repPos=3: 77...Re1 79...Kg7 83...Kg7", "", "",
        21, 49, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "4Q3/3K4/6k1/8/5R2/8/8/4r3 b - - 0 109"));

    // we need more skillful arbiters
    list.add(new PgnFileTestCase("grischuk_mamedyarov_2017.pgn", "repPos=4: 61.Rb2 63.Kg2 65.Kg2 67.Rb2", "", "", 7, 25,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/8/4p3/4Bk1K/5Pr1/8 w - - 2 88"));

    // youtube video - why savic so angry?
    list.add(new PgnFileTestCase("pranav_savic_2021_incomplete_speculative.pgn", "", "", "", 7, 73,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/5k2/2R5/5p2/5P2/6P1/6K1/r7 w - - 73 101"));

    // repetition after promotion
    list.add(new PgnFileTestCase("keres_fischer_1962.pgn", "", "", "", 20, 17, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/p4Q1k/1p6/1P6/8/7K/8/6q1 b - - 5 77"));
    list.add(new PgnFileTestCase("keres_fischer_1962_changed.pgn", "repPos=3: 76.Qf8+ 78.Qf8+ 80.Qf8+", "", "", 20, 17,
        CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "5Q1k/p7/1p6/1P6/8/7K/8/6q1 b - - 11 80"));

    // Lichess issue - game import
    list.add(new PgnFileTestCase("swiercz_karjakin_2015.pgn", "repPos=3: 42.Rf8+ 44.Rf8+ 46.Rf8+", "", "", 24, 11,
        CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "5R2/p3r3/P1R1pkp1/5p1p/5P1P/r5P1/4PK2/8 b - - 9 46"));

    // the threefold I did not see
    list.add(new PgnFileTestCase("gwendolus_vs_danielbaechli_2022.pgn", "repPos=3: 50.Bd4 52.Rh6 54.Rh6", "", "", 12,
        15, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "2k1r3/1p6/7R/3p4/1P1Bn3/2P2K2/P7/8 b - - 10 54"));

    // import on Lichess did not work
    list.add(new PgnFileTestCase("tal_bronstein_1961.pgn", "", "", "", 3, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/5p2/1p2p2p/6pk/r6q/4Q3/5P2/2R3K1 w - - 6 41"));

    // Ambrona 83
    list.add(new PgnFileTestCase("Ob5ozxgG.pgn", "", "", "", 5, 83, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1k6/5p1p/1p2pP1P/1P2P3/8/1K6/8/8 b - - 83 95"));

    return new PgnFileTestCaseList(PgnTest.GAMES_VARIOUS, list);
  }

  private static PgnFileTestCaseList createTestCasesGamesWcc201() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("round_01_nepomniachtchi_carlsen_2021_wcc.pgn", "", "", "", 17, 14,
        CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/4r2p/p2rnkp1/2p2p2/2N4P/2PP1P2/2K2P2/R3R3 b - - 14 45"));
    list.add(new PgnFileTestCase("round_01_nepomniachtchi_carlsen_2021_wcc_amended.pgn",
        "repPos=3: 41...Re7 43...Rc6 45...Rc6", "", "", 17, 15, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/4r2p/p1r1nkp1/2p2p2/2N4P/2PP1P2/2K2P2/R3R3 w - - 15 46"));
    list.add(new PgnFileTestCase("round_02_carlsen_nepomniachtchi_2021_wcc.pgn", "", "", "", 12, 9,
        CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "8/6kp/7R/6Pp/r4P2/8/5K2/8 w - - 9 59"));
    list.add(new PgnFileTestCase("round_03_nepomniachtchi_carlsen_2021_wcc.pgn", "", "", "", 28, 6,
        CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/3b4/7p/p1p1k3/P1p2p1P/2P2P2/2B2KP1/8 b - - 6 41"));
    list.add(new PgnFileTestCase("round_04_carlsen_nepomniachtchi_2021_wcc.pgn", "", "", "", 5, 7,
        CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r4nk1/4Rp1p/1p3Np1/1P1P2P1/8/p2r4/5P1P/R5K1 b - - 7 33"));
    list.add(new PgnFileTestCase("round_04_carlsen_nepomniachtchi_2021_wcc_amended.pgn",
        "repPos=3: 29...a3 31...Kg7 33...Kg7", "", "", 5, 8, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r4n2/4Rpkp/1p3Np1/1P1P2P1/8/p2r4/5P1P/R5K1 w - - 8 34"));
    list.add(new PgnFileTestCase("round_05_nepomniachtchi_carlsen_2021_wcc.pgn", "", "", "", 17, 14,
        CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4kb2/2p3p1/4np1p/4pN1P/4P1P1/2P1BP2/4K3/r2R4 b - - 14 43"));
    list.add(new PgnFileTestCase("round_05_nepomniachtchi_carlsen_2021_wcc_amended.pgn",
        "repPos=3: 39...Ra2+ 41...Ra2+ 43...Ra2+", "", "", 17, 15, CheckmateOrStalemate.NA, 3,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "4kb2/2p3p1/4np1p/4pN1P/4P1P1/2P1BP2/r3K3/3R4 w - - 15 44"));
    list.add(new PgnFileTestCase("round_06_carlsen_nepomniachtchi_2021_wcc.pgn", "", "", "", 13, 55,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "3k4/5RN1/4P3/5P2/7K/8/8/6q1 b - - 2 136"));
    list.add(new PgnFileTestCase("round_07_nepomniachtchi_carlsen_2021_wcc.pgn",
        "repPos=3: 34...Kg7 36...Kg7 38...Kg7; repPos=3: 35.Ra5 37.Ra5 39.Ra5; repPos=3: 35...Kf6 37...Kf6 39...Kf6",
        "", "", 17, 15, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/R4pk1/6p1/7p/7P/2r3P1/5PK1/8 b - - 15 41"));
    list.add(new PgnFileTestCase("round_08_carlsen_nepomniachtchi_2021_wcc.pgn", "", "", "", 6, 12,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/7k/8/3P4/6P1/1P2qQ1p/6P1/7K b - - 1 46"));
    list.add(new PgnFileTestCase("round_09_nepomniachtchi_carlsen_2021_wcc.pgn", "", "", "", 18, 6,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r3b1k1/P4pp1/4p3/2N1P2p/7P/5Pn1/8/R5K1 w - - 1 40"));
    list.add(new PgnFileTestCase("round_10_carlsen_nepomniachtchi_2021_wcc.pgn", "", "", "", 5, 12,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/pp6/2pk1n2/3pN3/3P4/2P1K3/PP6/8 b - - 1 41"));
    list.add(new PgnFileTestCase("round_11_nepomniachtchi_carlsen_2021_wcc.pgn", "", "", "", 20, 7,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "K7/5pk1/1R4p1/P1q5/5P2/4P3/8/8 w - - 7 50"));

    return new PgnFileTestCaseList(PgnTest.GAMES_WCC2021, list);
  }

  private static PgnFileTestCaseList createTestCasesFivefoldCorrect() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("akshat_li_2018.pgn", "repPos=5: 60...Rc1 62...Rc1 68...Rc1 73...Rc1 75...Rc1", "", "",
        19, 32, CheckmateOrStalemate.NA, 5, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/Pk6/5P2/1K1p3p/3Bp2P/4P3/8/2r5 w - - 32 76"));

    list.add(new PgnFileTestCase("miton_yakubboev_2018.pgn",
        "repPos=5: 38...Rd5+ 40...Rd5+ 42...Rd5+ 44...Rd5+ 46...Rd5+; repPos=4: 39.Ke4 41.Ke4 43.Ke4 45.Ke4; repPos=3: 39...Re5+ 43...Re5+ 45...Re5+; repPos=3: 40.Kd4 44.Kd4 46.Kd4",
        "", "", 25, 23, CheckmateOrStalemate.NA, 5, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/p7/1p2k2p/1P1r4/P1pK4/4P2P/8/2R5 w - - 23 47"));
    list.add(new PgnFileTestCase("potapov_adly_2018.pgn", "repPos=5: 43.Qf5+ 45.Qf5+ 47.Qf5+ 49.Qf5+ 51.Qf5+", "", "",
        20, 24, CheckmateOrStalemate.NA, 5, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/6rk/7p/p4Q2/8/Pr5P/3n1P2/3N3K b - - 24 51"));
    list.add(new PgnFileTestCase("sethuraman_guseinov_2017.pgn",
        "repPos=5: 51.Rxf7+ 53.Rf7+ 55.Rf7+ 57.Rf7+ 59.Rf7+; repPos=4: 51...Kg6 53...Kg6 55...Kg6 57...Kg6; repPos=4: 52.Rf6+ 54.Rf6+ 56.Rf6+ 58.Rf6+; repPos=4: 52...Kg7 54...Kg7 56...Kg7 58...Kg7",
        "", "", 14, 19, CheckmateOrStalemate.NA, 5, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "1r6/1P3Rkp/3Np3/4P3/1r4p1/8/7P/7K b - - 16 59"));
    list.add(new PgnFileTestCase("paichadze_ter-sahakyan_2019.pgn",
        "repPos=5: 58...Re4+ 60...Re4+ 62...Re4+ 64...Re4+ 66...Re4+; repPos=4: 59.Kf2 61.Kf2 63.Kf2 65.Kf2; repPos=4: 59...Rf4 61...Rf4 63...Rf4 65...Rf4; repPos=4: 60.Ke3 62.Ke3 64.Ke3 66.Ke3",
        "", "", 5, 18, CheckmateOrStalemate.NA, 5, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/8/8/4k3/4r3/4KR2/8/8 w - - 18 67"));
    list.add(new PgnFileTestCase("robson_moranda_2019.pgn",
        "repPos=5: 34...Kf8 36...Kf8 38...Kf8 40...Kf8 42...Kf8; repPos=4: 35.Raf7+ 37.Raf7+ 39.Raf7+ 41.Raf7+; repPos=4: 35...Ke8 37...Ke8 39...Ke8 41...Ke8; repPos=4: 36.Ra7 38.Ra7 40.Ra7 42.Ra7",
        "", "", 6, 17, CheckmateOrStalemate.NA, 5, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "5k2/R5R1/3pp2p/4p3/4P2P/1r1r2PK/8/8 w - - 17 43"));
    return new PgnFileTestCaseList(PgnTest.FIVEFOLD_CORRECT, list);
  }

  private static PgnFileTestCaseList createTestCasesFivefoldBeyond() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("savchenko_yu_y2017.pgn",
        "repPos=3: 68.Rf6 70.Kf3 72.Kf3; repPos=5: 75.Kf3 77.Kf3 79.Rf6 83.Rf6 85.Rf6; repPos=3: 78.Rh6 80.Rh6 82.Kf3",
        "", "", 20, 43, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/8/1k2K3/1P2p3/2n1P3/8/8/3n4 w - - 0 96"));
    list.add(new PgnFileTestCase("wang_yu_2017.pgn",
        "repPos=4: 40.f4 42.Kd5 46.Kd5 56.Kd5; repPos=5: 40...Kc7 42...Kc7 44...Kc7 46...Kc7 48...Kc7; repPos=3: 41.Kc5 45.Kc5 49.Kc5; repPos=4: 41...Kd7 45...Kd7 49...Kd7 57...Bg7",
        "", "", 17, 36, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/5p2/8/4N1P1/8/b2P1pk1/3K4/8 w - - 0 69"));
    list.add(new PgnFileTestCase("yu_alekseenko_2018.pgn",
        "repPos=4: 68...Qe3+ 72...Qe3+ 76...Qe3+ 78...Qe3+; repPos=4: 69.Kg2 73.Kg2 77.Kg2 79.Kg2; repPos=3: 69...Qe2+ 73...Qe2+ 75...Qe2+; repPos=3: 70.Kg3 74.Kg3 76.Kg3; repPos=7: 83.Qa8+ 85.Qa8+ 87.Qa8+ 89.Qa8+ 91.Qa8+ 93.Qa8+ 95.Qa8+; repPos=4: 83...Kb6 85...Kb6 87...Kb6 91...Kb6; repPos=4: 84.Qb8+ 86.Qb8+ 88.Qb8+ 92.Qb8+; repPos=4: 84...Kc6 86...Kc6 88...Kc6 92...Kc6",
        "", "", 4, 32, CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/2p5/p3r3/1p1Q2R1/2k2P2/P3q3/1P4K1/8 b - - 32 97"));
    return new PgnFileTestCaseList(PgnTest.FIVEFOLD_BEYOND, list);
  }

  private static PgnFileTestCaseList createTestCasesFiftyGeneral() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("adly_flores_2016.pgn",
        "repPos=3: 112.Rh5 114.Kc6 116.Rh5; repPos=3: 112...Nc3 114...Nc3 116...Nc3", "",
        "92...Nb3+ (1) 142.Ke6 (100) 143...Nb8 (103)", 26, 103, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "1R2k3/8/4K3/8/8/8/8/8 b - - 0 144"));
    list.add(new PgnFileTestCase("cheparinov_anand_2018.pgn", "", "", "61...Kb5 (1) 111.Ra7 (100) 130.Rb1 (138)", 11,
        138, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/2KB4/k7/r7/8/1R6 b - - 138 130"));
    list.add(new PgnFileTestCase("giri_grischuk_2014.pgn",
        "repPos=3: 86...Kc5 114...Kc5 116...Kc5; repPos=3: 87.Ka4 115.Ka4 117.Ka4; repPos=4: 87...Kb6 89...Nc6 115...Kb6 117...Kb6",
        "", "85.Rc8 (1) 134...Kc5 (100) 146...Nc6 (124)", 6, 124, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/3P1k2/3K4/8/8/8/8 b - - 1 156"));
    list.add(new PgnFileTestCase("carlsen_liem_2014.pgn", "", "", "52...Kc7 (1) 102.Bb7 (100) 103.Ba6 (102)", 11, 102,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/5p2/Bp2pP1k/1p2P3/2b2K2/P1P5/8/8 b - - 102 103"));
    list.add(new PgnFileTestCase("pruijssers_inarkiev_2015.pgn", "", "", "59.Rb8 (1) 108...Ra2 (100)", 37, 100,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "8/8/8/7P/8/6Pk/6r1/2R2K1r w - - 11 128"));
    list.add(new PgnFileTestCase("riazantsev_levin_2016.pgn",
        "repPos=3: 61...Kg7 64...Qa2 81...Qa2; repPos=3: 77...Qd5 87...Qd5 96...Kg7; repPos=3: 152...Qg1+ 154...Qg1+ 156...Qg1+",
        "", "46...Qe4 (1) 96.Rf4 (100) 113...Qd5 (135)", 10, 135, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "5Rqk/8/6PP/8/5P2/5NK1/8/8 b - - 4 181"));
    list.add(new PgnFileTestCase("radjabov_deac_2016.pgn", "", "", "69...Kf5 (1) 119.Rh2 (100) 126...Ke5 (115)", 17,
        115, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/8/8/4k3/4pP2/4P1K1/r6R/8 b - f3 0 127"));
    list.add(new PgnFileTestCase("alekseenko_grachev_2017.pgn", "repPos=3: 64.Rf2 66.Bh5 70.Rf2", "",
        "55...Rg8 (1) 105.Kf4 (100) 117...Rh3 (125)", 18, 125, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/2kB1K2/4P3/4P3/4p3/2P5/1P3q2/8 w - - 2 126"));
    list.add(new PgnFileTestCase("vaisser_karpov_2017.pgn", "", "", "82...Kd7 (1) 132.Ke4 (100) 152.Bc3+ (140)", 11,
        140, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "6r1/8/7R/k7/2K5/2B5/8/8 b - - 140 152"));
    list.add(new PgnFileTestCase("yu_korobov_2017.pgn", "", "", "60...Bc7 (1) 110.Ra4+ (100) 123.Rb2 (126)", 17, 126,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/k1K5/8/1R3b2/8 b - - 126 123"));
    list.add(new PgnFileTestCase("yu_shimanov_2018.pgn", "repPos=3: 128.Ra1 130.Ra1 132.Ra1", "",
        "88.Ra2+ (1) 137...Kg7 (100) 155...Rc1 (136)", 9, 136, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/8/8/5k2/6p1/6K1 w - - 0 163"));
    list.add(new PgnFileTestCase("ju_sebenik_2018.pgn", "", "", "64.Rg3+ (1) 113...Kb7 (100) 128.Rf2 (129)", 19, 129,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/8/k7/6Kp/5R1P/5b2 b - - 129 128"));
    list.add(new PgnFileTestCase("wang_wen_2018.pgn",
        "repPos=3: 131.Ra2 137.Ke3 139.Ra2; repPos=4: 131...Kg1 137...Kg1 139...Kg1 141...Rf8; repPos=3: 133...Kf1 135...Kf1 143...Kf1",
        "", "95...Kb5 (1) 145.Be4 (100) 169.Rc1 (148)", 12, 148, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/8/8/8/4BK2/1r6/7k/2R5 b - - 148 169"));
    list.add(new PgnFileTestCase("meier_dominguez_2018.pgn",
        "repPos=4: 89...Rd3 91...Rd3 93...Kg8 95...Rd3; repPos=3: 98...Rd1 100...Rd1 104...Rd1", "",
        "46...Kf7 (1) 96.Ra7 (100) 106...Rd1 (121)", 12, 121, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "5R2/5P1k/3B3p/3K3P/8/8/8/8 b - - 0 115"));
    list.add(new PgnFileTestCase("bindrich_kovalev_2018.pgn", "", "", "57...Ra3+ (1) 107.Kg2 (100) 118.Kf3 (122)", 15,
        122, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/6k1/1B5p/5K1P/7r/8 b - - 3 121"));
    list.add(new PgnFileTestCase("jobava_salem_2018.pgn", "", "", "36.Ne1 (1) 85...Nb4 (100) 92...Nb4 (114)", 13, 114,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4k3/1q1b4/2p1p3/2p1Pp2/1BPp1Pp1/1PbP2Pp/2NN3P/2KQ4 b - - 0 93"));
    list.add(new PgnFileTestCase("sarana_chirila_2019.pgn",
        "repPos=3: 68.Ne3 76.Ne3 104.Ne3; repPos=3: 68...Bd3 76...Bd3 104...Bd3; repPos=3: 69.Nd1 77.Nd1 105.Nd1; repPos=3: 69...Bc2 77...Bc2 105...Bc2",
        "", "63...Bd3 (1) 113.Ke3 (100) 116...Kf7 (107)", 10, 107, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/7p/4k3/3NPb1P/3K4/8/8/8 b - - 66 150"));
    list.add(new PgnFileTestCase("topalov_dominguez_2019.pgn", "", "", "38...Qc3 (1) 88.Kg3 (100) 90.Kg3 (104)", 22,
        104, CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "8/4p3/3p1k2/3P4/2P2pQ1/5P1K/8/7q w - - 15 109"));
    return new PgnFileTestCaseList(PgnTest.FIFTY_GENERAL, list);
  }

  private static PgnFileTestCaseList createTestCasesFiftyPattern() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    // because below 100 not displayed
    // 55.Nc4 (1) 103...Kh5 (98)
    list.add(new PgnFileTestCase("98_start_white_demchenko_martinez_2019.pgn", "repPos=3: 70...Ng5 83...Kh5 103...Kh5",
        "", "", 29, 98, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/8/4p3/6nk/6p1/4N1P1/5PK1/8 w - - 98 104"));

    // because below 100 not displayed
    // 68...Rh2 (1) 117.Kd3 (98)
    list.add(new PgnFileTestCase("98_start_black_sjugirov_inarkiev_2019.pgn", "", "", "", 4, 98,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/8/r7/3K4/1R3N2/6k1 b - - 98 117"));

    // because below 100 not displayed
    // 115.Kg2 (1) 163...Kc5 (98); 164.Kh1 (99)
    list.add(new PgnFileTestCase("99_start_white_so_nakamura_2018.pgn",
        "repPos=3: 145...Kb2 153...Kb2 157...Kb2; repPos=3: 146.Kh1 154.Kh1 158.Kh1", "", "", 15, 99,
        CheckmateOrStalemate.NA, 2, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/2k1b3/8/8/7p/7K b - - 99 164"));

    // because below 100 not displayed
    // 59...Bg6 (1) 108.Nb5 (98); 108...Bh5 (99)
    list.add(new PgnFileTestCase("99_start_black_naroditsky_boruchovsky_2018.pgn",
        "repPos=3: 72...Ke6 90...Ke6 92...Be8; repPos=3: 75.Nd4+ 97.Nd4+ 107.Nd4+; repPos=3: 75...Ke5 97...Ke5 107...Ke5; repPos=3: 76.Nb5 98.Nb5 108.Nb5; repPos=4: 76...Bh5 98...Bh5 102...Bh5 108...Bh5; repPos=3: 84...Ke6 96...Ke6 106...Ke6",
        "", "", 27, 99, CheckmateOrStalemate.NA, 4, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/6K1/5P2/1N2k2b/8/8/8/8 w - - 99 109"));

    list.add(new PgnFileTestCase("100_start_white_baryshpolets_steingrimsson_2019.pgn",
        "repPos=3: 77...Qg7+ 83...Qg7+ 95...Qg7+; repPos=3: 78.Kb6 84.Kb6 96.Kb6; repPos=3: 78...Qd4+ 84...Qd4+ 96...Qd4+; repPos=3: 79.Ka6 85.Ka6 97.Ka6",
        "", "77.Kc7 (1) 126...Nf4+ (100)", 8, 100, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1k6/4K3/6Q1/4q3/5n2/8/8/8 w - - 100 127"));

    list.add(new PgnFileTestCase("100_start_black_nakamura_radjabov_2014.pgn", "repPos=3: 68...Ref8 70...Be6 72...Be6",
        "", "27...Rh5 (1) 77.Rd3 (100)", 8, 100, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1kbb1r1r/2p2pp1/1p6/p1pNPn2/P1P2P1p/1P1R1N1P/5BPK/3R4 b - - 100 77"));

    list.add(new PgnFileTestCase("101_start_white_nakamura_vachier-lagrave_2018.pgn",
        "repPos=3: 111.Kf6 119.Kf6 121.Kf6; repPos=3: 111...Kf1 119...Kf1 121...Kf1", "",
        "72.Bf4 (1) 121...Kf1 (100) 122.Kf7 (101)", 5, 101, CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/5K2/8/6Bp/4p2P/4Pb2/8/5k2 b - - 101 122"));

    list.add(new PgnFileTestCase("101_start_black_sevian_andriasian_2019.pgn", "", "",
        "74...Kd5 (1) 124.Kc6 (100) 124...Rc1+ (101)", 10, 101, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "2k5/7R/2K5/4N3/8/8/8/2r5 w - - 101 125"));

    list.add(new PgnFileTestCase("101_start_black_grischuk_vachier-lagrave_2017.pgn",
        "repPos=3: 67...Kd6 75...Kd6 86...Kd6; repPos=4: 68.Rh8 74.Rh8 76.Rh8 87.Rh8; repPos=3: 68...Kc6 76...Kc6 87...Kc6; repPos=4: 69.Rc8+ 73.Rc8+ 77.Rc8+ 88.Rc8+",
        "", "39...Kf6 (1) 89.Ra8 (100) 89...Kd6 (101)", 3, 101, CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "R7/8/3kp3/3p4/3Pr3/3KP3/8/8 w - - 101 90"));

    list.add(new PgnFileTestCase("102_start_white_harikrishna_yu_2017.pgn", "", "",
        "52.Rf3+ (1) 101...Rg1 (100) 102...Ke5 (102)", 17, 102, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/1R1b3K/4k3/8/8/8/6r1 w - - 102 103"));

    list.add(new PgnFileTestCase("102_start_black_riazantsev_deac_2017.pgn", "", "",
        "91...Ne3 (1) 141.Ke3 (100) 142.Kf3 (102)", 7, 102, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/3nk3/R7/5K2/8/8 b - - 102 142"));

    return new PgnFileTestCaseList(PgnTest.FIFTY_PATTERN, list);
  }

  private static PgnFileTestCaseList createTestCasesSeventyFiveCorrect() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("firouzja_demchenko_2019.pgn", "repPos=3: 100...Kg8 102...Ra3 104...Kg8", "",
        "52.Kf2 (1) 101...Ra5+ (100) 126...Kg7 (150)", 15, 150, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/6k1/4R2p/4K1pP/5rP1/5P2/8/8 w - - 150 127"));
    list.add(
        new PgnFileTestCase("topalov_nakamura_2016.pgn", "", "", "58.Kc8 (1) 107...Nc4 (100) 132...Rg8 (150)", 15, 150,
            CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
            UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "6r1/8/8/8/5k2/R6K/3n4/8 w - - 150 133"));
    list.add(
        new PgnFileTestCase("yudasin_erenburg_2017.pgn", "", "", "123.Kf2 (1) 172...Ke1 (100) 197...Ne7 (150)", 5, 150,
            CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
            UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "4B3/4n3/8/2k2p2/6p1/4K1P1/8/8 w - - 150 198"));
    return new PgnFileTestCaseList(PgnTest.SEVENTY_FIVE_CORRECT, list);
  }

  private static PgnFileTestCaseList createTestCasesSeventyFiveBeyond() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("anton_guijarro_antipov_2015.pgn",
        "repPos=3: 122.Kh3 126.Kh3 128.Kh3; repPos=3: 124.Kh3 130.Kh3 132.Kh3", "",
        "62...Nd7 (1) 112.Kg2 (100) 137.Kg2 (150) 147...Kg4 (171)", 34, 171, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/3n4/3P4/8/6k1/4n3/7K/8 w - - 171 148"));
    list.add(new PgnFileTestCase("aronian_navara_2017.pgn", "repPos=3: 46...Bb3 48...Kf7 50...Bb3", "",
        "64...Kf6 (1) 114.Re6+ (100) 139.Kf6 (150) 147...Kh6 (167)", 8, 167, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/3R4/5K1k/7p/7P/5b2/8/8 w - - 167 148"));
    list.add(new PgnFileTestCase("moiseenko_radjabov_2016.pgn", "repPos=3: 135...Kf8 137...Rd1 139...Kf8", "",
        "87...Ke6 (1) 137.Rb7 (100) 162.Rc1 (150) 182...Rb1+ (191)", 16, 191, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1k6/3R4/8/1K1N4/8/8/8/1r6 w - - 191 183"));
    list.add(new PgnFileTestCase("onischuk_guseinov_2014.pgn", "", "",
        "62.Be3+ (1) 111...Nc6 (100) 136...Kg5 (150) 138.Bd6 (153)", 6, 153, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/3Bn3/8/4B1k1/4K3/8/8 b - - 153 138"));
    list.add(new PgnFileTestCase("cheparinov_jones_2019.pgn", "", "",
        "69.Ke4 (1) 118...Ra6 (100) 143...Kc4 (150) 144...Rg6 (152)", 6, 152, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/6r1/K6R/2k5/8/2n5/8 w - - 152 145"));
    return new PgnFileTestCaseList(PgnTest.SEVENTY_FIVE_BEYOND, list);
  }

  private static PgnFileTestCaseList createTestCasesEarlyDraw() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("grischuk_giri_2019.pgn", "repPos=3: 18.Kf1 20.Kf1 22.Kf1", "", "", 11, 10,
        CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r4rk1/p4pp1/np2p3/q5N1/3PP3/P7/5P1P/R2Q1KR1 b - - 10 22"));
    list.add(new PgnFileTestCase("karjakin_nepomniachtchi_2019.pgn", "repPos=3: 17.Nb5 19.Nb5 21.Nb5", "", "", 18, 13,
        CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rn1r2k1/1pqbppbp/2p2np1/pN6/8/4BNP1/PPQ1PPBP/R2R2K1 b - - 13 21"));
    list.add(new PgnFileTestCase("vachier-lagrave_aronian_2019.pgn", "repPos=3: 21.Ra1 23.Nc4 25.Nc4", "", "", 21, 13,
        CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "5rk1/1np1q1pp/p1n1p3/Pr2p3/1pNpP3/1N1P3P/1PP2PP1/R2QR1K1 b - - 13 25"));
    list.add(new PgnFileTestCase("vachier-lagrave_ding_liren_2019.pgn", "repPos=3: 16...Qg6 18...Qg6 20...Qg6", "", "",
        19, 19, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r4rk1/bpp1n1pp/p2pp1q1/4p3/PP2Pn2/N1PPBN1P/R4PPK/3QR3 w - - 19 21"));
    return new PgnFileTestCaseList(PgnTest.EARLY_DRAW, list);
  }

  private static PgnFileTestCaseList createTestCasesWikipediaThreefold() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("2_0_1_spassky_fischer_1972_seventeenth.pgn", "", "", "", 11, 20,
        CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/1p2ppk1/p1np4/6p1/2R1P3/1P4KP/P1R1r1P1/8 b - - 7 45"));
    list.add(new PgnFileTestCase("2_0_1_spassky_fischer_1972_seventeenth_changed.pgn",
        "repPos=3: 41...g5 43...Re1 45...Re1", "", "", 11, 20, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/1p2ppk1/p1np4/6p1/2R1P3/1P4KP/P1R3P1/4r3 w - - 8 46"));
    list.add(new PgnFileTestCase("2_0_2_fischer_spassky_1972_eighteenth.pgn", "", "", "", 8, 21,
        CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "2r5/5R1Q/1kqr1p2/4p3/pP6/Pp4P1/1P5P/KR6 w - - 21 48"));
    list.add(new PgnFileTestCase("2_0_2_fischer_spassky_1972_eighteenth_changed.pgn", "repPos=3: 44.Qh6 46.Qh6 48.Qh6",
        "", "", 8, 22, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "2r5/5R2/1kqr1p1Q/4p3/pP6/Pp4P1/1P5P/KR6 b - - 22 48"));
    list.add(new PgnFileTestCase("2_1_fischer_petrosian_1971.pgn", "repPos=3: 30.Qe2 32.Qe2 34.Qe2", "", "", 8, 11,
        CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/pp3p1k/2p2q1p/3r1P2/5R2/7P/P1P1QP2/7K b - - 10 34"));
    list.add(new PgnFileTestCase("2_2_1_ponomariov_adams_2005_wijk_aan_zee.pgn", "", "", "", 5, 8,
        CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "7k/6p1/1p1p4/nP1p3p/3P4/R1P3rP/4K3/5B2 w - - 8 42"));
    list.add(new PgnFileTestCase("2_2_1_ponomariov_adams_2005_wijk_aan_zee_changed.pgn",
        "repPos=3: 38.Kd2 40.Kd2 42.Kd2", "", "", 5, 9, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "7k/6p1/1p1p4/nP1p3p/3P4/R1P3rP/3K4/5B2 b - - 9 42"));
    list.add(new PgnFileTestCase("2_2_2_adams_ponomariov_2005_sofia.pgn", "", "", "", 5, 11, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.WINNABLE, "r7/p1r2ppp/6k1/3p4/6R1/4RP2/PP3P1P/1K6 b - - 10 27"));
    list.add(new PgnFileTestCase("2_2_2_adams_ponomariov_2005_sofia_changed.pgn",
        "repPos=3: 23...Rc7 25...Kf6 27...Kf6", "", "", 5, 11, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.WINNABLE,
        "r7/p1r2ppp/5k2/3p4/6R1/4RP2/PP3P1P/1K6 w - - 11 28"));
    list.add(new PgnFileTestCase("2_2_3_ponomariov_adams_2005_sofia.pgn", "", "", "", 17, 12, CheckmateOrStalemate.NA,
        2, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "3Q4/5pk1/7R/1p3Pp1/1P6/7P/6P1/4q2K w - - 7 53"));
    list.add(new PgnFileTestCase("2_2_3_ponomariov_adams_2005_sofia_changed.pgn",
        "repPos=3: 49...Qe5+ 51...Qe5+ 53...Qe5+", "", "", 17, 12, CheckmateOrStalemate.NA, 3,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3Q4/5pk1/7R/1p2qPp1/1P6/7P/6PK/8 w - - 9 54"));
    list.add(new PgnFileTestCase("2_3_capablanca_lasker_1921.pgn", "repPos=3: 34...h5 36...Kf8 38...Kf8", "", "", 15, 8,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "1Q3k2/p4p2/1p6/7R/3q4/1P2n3/P7/6K1 b - - 7 46"));
    list.add(new PgnFileTestCase("2_4_1_alekhine_lasker_1914.pgn", "", "", "", 6, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r2q1r1k/p1p1b3/4pnQp/3p4/8/2NB4/PPP2PPP/R5K1 b - - 3 16"));
    list.add(new PgnFileTestCase("2_4_1_alekhine_lasker_1914_changed.pgn", "repPos=3: 17.Qxh6+ 19.Qh6+ 21.Qh6+", "", "",
        6, 8, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r3qr1k/p1p1b3/4pn1Q/3p4/8/2NB4/PPP2PPP/R5K1 b - - 8 21"));
    list.add(new PgnFileTestCase("2_4_2_lasker_alekhine_1914.pgn", "repPos=3: 21.Qd4 23.Kg1 25.Kg1", "", "", 3, 11,
        CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "7r/2p1bppp/2p1k3/2P1n3/3QN3/5q2/PP3P1P/R1BR2K1 b - - 11 25"));

    list.add(new PgnFileTestCase("2_5_korchnoi_portisch_1970_game_1.pgn", "repPos=3: 64.Kh5 66.Kh5 68.Rb8", "", "", 35,
        19, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "1R6/5nk1/3r4/4p1pK/4P2p/7N/8/8 b - - 19 68"));
    list.add(new PgnFileTestCase("2_5_korchnoi_portisch_1970_game_2.pgn", "", "", "", 14, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "2rr1k2/1b3pp1/pp2pq1p/2bP4/P7/2N5/BP2QPPP/3RR1K1 w - - 9 32"));
    list.add(new PgnFileTestCase("2_5_korchnoi_portisch_1970_game_3.pgn", "", "", "", 26, 14, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "3N2k1/8/8/pPn4p/1p5P/8/6P1/6K1 w - - 1 64"));
    list.add(new PgnFileTestCase("2_5_korchnoi_portisch_1970_game_4.pgn", "", "", "", 8, 10, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "2b1nrk1/p2p1npp/2q1p3/2N5/5P2/P5P1/1P3QBP/R3K2R b KQ - 10 25"));
    list.add(new PgnFileTestCase("2_5_korchnoi_portisch_1970_game_4_changed.pgn",
        "repPos=3: 21...Qb5 23...Qb5 25...Qb5", "", "", 8, 11, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "2b1nrk1/p2p1npp/4p3/1qN5/5P2/P5P1/1P3QBP/R3K2R w KQ - 11 26"));

    list.add(new PgnFileTestCase("2_6_kasparov_deep_blue_1997.pgn", "", "", "", 8, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/pp4P1/8/8/1kp2N2/1n2R1P1/3r4/1K6 w - - 1 50"));
    list.add(new PgnFileTestCase("2_6_kasparov_deep_blue_1997_changed.pgn", "repPos=3: 50.g8=Q 52.Kb1 54.Kb1", "", "",
        8, 8, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "6Q1/pp6/8/8/1kp2N2/1n2R1P1/3r4/1K6 b - - 8 54"));
    list.add(new PgnFileTestCase("2_7_1_giuoco_piano.pgn", "", "", "", 8, 10, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bqrknQ/ppp3pB/3p1p2/3P4/6PR/8/PP3P1P/R5K1 b - - 10 22"));
    list.add(new PgnFileTestCase("2_7_1_giuoco_piano_changed.pgn", "repPos=3: 20.Bh7 22.Bh7 24.Bh7", "", "", 8, 14,
        CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bqrknQ/ppp3pB/3p1p2/3P4/6PR/8/PP3P1P/R5K1 b - - 14 24"));
    list.add(new PgnFileTestCase("2_7_2_pirc_defense.pgn", "", "", "", 16, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rn1Nk2r/pp2p2p/3p2p1/1bp5/5Pn1/2N5/PPP2bPP/R1BQK2R w kq - 5 14"));
    list.add(new PgnFileTestCase("2_7_2_pirc_defense_changed.pgn", "repPos=3: 12.Kd2 14.Kd2 16.Kd2", "", "", 16, 10,
        CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rn1Nk2r/pp2p2p/3p2p1/1bp5/5Pn1/2N5/PPPK1bPP/R1BQ3R b kq - 10 16"));
    list.add(new PgnFileTestCase("3_1_karpov_miles_1986.pgn", "", "", "", 7, 9, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r3kb1r/5ppp/4p3/1N6/4P3/8/Pn1BK1PP/R6R b k - 9 26"));
    list.add(new PgnFileTestCase("3_1_karpov_miles_1986_changed.pgn", "repPos=3: 22...Ra4 24...Ra4 26...Ra4", "", "", 7,
        10, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4kb1r/5ppp/4p3/1N6/r3P3/8/Pn1BK1PP/R6R w k - 10 27"));
    list.add(new PgnFileTestCase("3_2_fischer_spassky_1972_twentieth.pgn", "", "", "", 6, 30, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/8/3k2b1/1p2p2p/p2n2p1/P1K1N1P1/1PP4P/4N3 w - - 30 55"));
    list.add(new PgnFileTestCase("3_2_fischer_spassky_1972_twentieth_changed.pgn", "repPos=3: 48.Kc3 50.Ne1 58.Kc3", "",
        "", 6, 37, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/8/3k2b1/1p2p2p/p2n2p1/P1K1N1P1/1PP4P/4N3 b - - 37 58"));
    list.add(new PgnFileTestCase("4_0_1_pest_paris.pgn",
        "repPos=5: 18...Nb6 20...Bc7 22...Bc7 24...Bc7 26...Bc7; repPos=5: 19.Nc5 21.Nc5 23.Nc5 25.Nc5 27.Nc5; repPos=4: 19...Bd6 21...Bd6 23...Bd6 25...Bd6; repPos=4: 20.N5e4 22.N5e4 24.N5e4 26.N5e4",
        "", "", 5, 23, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "1r3rk1/p1b3pp/1np5/2N5/2pP4/2Nb4/PP2RPPP/R1B3K1 w - - 23 28"));
    list.add(new PgnFileTestCase("4_0_1_pest_paris_six_fold.pgn",
        "repPos=6: 18...Nb6 20...Bc7 22...Bc7 24...Bc7 26...Bc7 28...Bc7; repPos=5: 19.Nc5 21.Nc5 23.Nc5 25.Nc5 27.Nc5; repPos=5: 19...Bd6 21...Bd6 23...Bd6 25...Bd6 27...Bd6; repPos=5: 20.N5e4 22.N5e4 24.N5e4 26.N5e4 28.N5e4",
        "", "", 5, 25, CheckmateOrStalemate.NA, 6, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "1r3rk1/p1b3pp/1np5/5b2/2pPN3/2N5/PP2RPPP/R1B3K1 w - - 25 29"));
    list.add(new PgnFileTestCase("4_1_pillsbury_burn_1898.pgn",
        "repPos=3: 42...Qe3 46...Kg7 50...Kg7; repPos=3: 43.Qb2 47.Qb2 51.Qb2", "", "", 8, 42, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/5Q2/8/P6p/7P/1q2p1P1/kp5K/8 w - - 2 91"));
    return new PgnFileTestCaseList(PgnTest.WIKIPEDIA_THREEFOLD, list);
  }

  private static PgnFileTestCaseList createTestCasesWikipediaFiftyMove() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("1_filipowicz_smederevac_1966.pgn", "", "", "21.Qe2 (1) 70...Rd7 (100)", -1, 100,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4q3/r1br1nk1/1pb1p1p1/p1pnPp1p/P2p1P1P/NP1P1BPN/2PB3K/R3R2Q w - - 100 71"));
    list.add(new PgnFileTestCase("2_1_timman_lutz_1995.pgn", "", "", "69...Rh1 (1) 119.Rg5 (100) 121...Rb5+ (105)", 16,
        105, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "8/7k/8/1r3KR1/5B2/8/8/8 w - - 105 122"));
    list.add(new PgnFileTestCase("2_2_karpov_kasparov_1991.pgn", "", "", "63...Rg8 (1) 113.Ng5 (100) 114...Rf6+ (103)",
        30, 103, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "7k/4NK2/5r2/5BN1/8/8/8/8 w - - 103 115"));
    list.add(new PgnFileTestCase("2_3_lputian_harutjunyan_2001.pgn", "", "",
        "86...Qe2+ (1) 136.Qd4+ (100) 142.Qf6+ (112)", 19, 112, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "7k/5q2/5Q1P/6P1/8/6K1/8/8 b - - 112 142"));
    list.add(new PgnFileTestCase("2_4_nguyen_vachier-lagrave_2008.pgn", "", "", "71...Ra5+ (1) 121.Bc5+ (100)", 6, 100,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "4R3/kr6/2K5/2B5/8/8/8/8 b - - 100 121"));
    return new PgnFileTestCaseList(PgnTest.WIKIPEDIA_FIFTY_MOVE, list);
  }

  private static PgnFileTestCaseList createTestCasesSequence() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("rep_1.pgn",
        "repPos=3: 1.Nf3 3.Nf3 5.Nf3; repPos=3: 1...Nf6 3...Nf6 5...Nf6; repPos=3: 2.Ng1 4.Ng1 6.Ng1; repPos=3: 2...Ng8 4...Ng8",
        "", "", -1, 11, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkb1r/pppppppp/5n2/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 11 6"));
    list.add(new PgnFileTestCase("rep_2.pgn",
        "repPos=3: 1.Nf3 3.Nf3 5.Nf3; repPos=3: 1...Nf6 3...Nf6 5...Nf6; repPos=3: 2.Ng1 4.Ng1 6.Ng1; repPos=4: 2...Ng8 4...Ng8 6...Ng8",
        "", "", -1, 12, CheckmateOrStalemate.NA, 4, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 12 7"));
    list.add(new PgnFileTestCase("rep_3.pgn",
        "repPos=4: 1.Nf3 3.Nf3 5.Nf3 7.Nf3; repPos=4: 1...Nf6 3...Nf6 5...Nf6 7...Nf6; repPos=4: 2.Ng1 4.Ng1 6.Ng1 8.Ng1; repPos=5: 2...Ng8 4...Ng8 6...Ng8 8...Ng8",
        "", "", -1, 16, CheckmateOrStalemate.NA, 5, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 16 9"));
    return new PgnFileTestCaseList(PgnTest.SEQUENCE, list);
  }

  private static PgnFileTestCaseList createTestCasesCapture() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("01_no_capture.pgn", "", "", "", -1, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bq1rk1/1pp2p2/pbnp1n1p/3Np1p1/1PB1P1P1/P2P1N1P/2P2P2/R1BQK2R w KQ - 2 11"));
    list.add(new PgnFileTestCase("02_capture_last_move_piece.pgn", "", "", "", 21, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bq1rk1/1pp2p2/pbnp1N1p/4p1p1/1PB1P1P1/P2P1N1P/2P2P2/R1BQK2R b KQ - 0 11"));
    list.add(new PgnFileTestCase("03_capture_last_move_en_passant.pgn", "", "", "", 25, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "r1bq1rk1/1p3p2/pbPp1n1p/n2Np1p1/4P1P1/PB1P1N1P/2P2P2/R1BQK2R b KQ - 0 13"));
    list.add(new PgnFileTestCase("04_capture_last_move_mate.pgn", "", "", "", 37, 16, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "r1b1n2k/1ppqNp1Q/pbnp3p/4pBp1/PP2P1P1/3P1N1P/2P2P2/R1B1K2R b KQ - 0 19"));
    list.add(new PgnFileTestCase("05_capture_last_move_piece_more_moves_max_yawn_unchanged.pgn", "", "", "", 21, 4,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1b1r2k/1ppq1p2/pbnp1N1p/4p1p1/1PB1P1PP/P2P1N2/2P2P2/R1BQK2R b KQ - 4 14"));
    list.add(new PgnFileTestCase("06_capture_last_move_en_passant_capture_more_moves_max_yawn_unchanged.pgn", "", "",
        "", 25, 4, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "Q1b2r2/2b2pk1/p2p1nNp/n3p1p1/4P1P1/PB1P1N1P/2Q2P2/R1B1K2R b KQ - 4 18"));
    list.add(new PgnFileTestCase("07_capture_last_move_piece_more_moves_max_yawn_changed.pgn",
        "repPos=3: 11...Kg7 13...Kg7 15...Kg7", "", "", 21, 9, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bq1r2/1pp2pk1/pbnp1N1p/4p1p1/1PB1P1P1/P2P1N1P/2P2P2/R1BQK2R w KQ - 9 16"));
    list.add(new PgnFileTestCase("08_capture_last_move_en_passant_capture_more_moves_max_yawn_changed.pgn", "", "", "",
        25, 9, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "r1bq1rk1/1p3p2/1bPp3p/p2Np1pn/B2NP1P1/P2Pn2P/2PQKP2/R1B3R1 b - - 9 19"));
    return new PgnFileTestCaseList(PgnTest.CAPTURE_AND_MAX_YAWN, list);
  }

  private static PgnFileTestCaseList createTestCasesSpecial() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    // nigle short tweet - white to play and draw (on white timeout chess.com draws,
    // lichess.org grants black the win
    list.add(new PgnFileTestCase("nigel_short_tweet_white_to_move.pgn", "", "", "", 5, 13, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "K1n5/2k5/P7/8/8/8/8/8 w - - 1 37"));
    list.add(new PgnFileTestCase("nigel_short_tweet_black_to_move.pgn", "", "", "", 3, 12, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "K1n5/2k5/P7/8/8/8/8/8 b - - 0 44"));

    // king against all
    list.add(new PgnFileTestCase("king_against_all_white_only_king_white_to_move.pgn", "", "", "", 6, 8,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkbnr/pppppppp/8/8/8/4K3/8/8 w kq - 8 33"));
    list.add(new PgnFileTestCase("king_against_all_white_only_king_black_to_move.pgn", "", "", "", 6, 9,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "rnbqkbnr/pppppppp/8/8/8/4K3/8/8 b kq - 9 33"));
    list.add(new PgnFileTestCase("king_against_all_black_only_king_black_to_move.pgn", "", "", "", 5, 10,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/3k4/8/8/8/PPPPPPPP/RNBQKBNR b KQ - 10 36"));
    list.add(new PgnFileTestCase("king_against_all_black_only_king_white_to_move.pgn", "", "", "", 5, 11,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/3k4/8/8/PPPPPPPP/RNBQKBNR w KQ - 11 37"));

    // norgaard buchanan
    list.add(new PgnFileTestCase("norgaard_buchanan_strategems_2002_alive.pgn", "", "", "", 10, 9,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "Bb2kb2/bKp1p1p1/1pP1P1P1/pP6/6P1/P7/8/8 b - - 2 46"));
    list.add(new PgnFileTestCase("norgaard_buchanan_strategems_2002_alive_played_out.pgn", "", "", "", 10, 19,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "1bK1kb2/b1p1pBp1/1pP1P1P1/1P4P1/p7/P7/8/8 b - - 1 57"));

    list.add(new PgnFileTestCase("norgaard_buchanan_strategems_2002_dead.pgn", "repPos=3: 74.Bc8 84.Bc8 86.Bc8", "", "",
        14, 49, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "KbB1kb2/b1p1p1p1/1pP1P1P1/1P4P1/p7/P7/8/8 b - - 26 86"));

    list.add(new PgnFileTestCase("forced_checkmate_white_only.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "k1r5/2P5/KP6/P7/8/n7/6R1/7B w - - 0 50"));

    list.add(new PgnFileTestCase("both_kings_blocked_on_the_side_with_pawns.pgn", "", "", "", 11, 8,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/6p1/1p3pP1/1P3Ppk/1Pp3p1/KpP3P1/1P6/8 w - - 0 44"));

    list.add(new PgnFileTestCase("both_kings_blocked_in_a_corner_with_pawns_and_bishop_pawn_moves_not_exhausted.pgn",
        "", "", "", 24, 19, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "k1b5/1p1p4/1PpP4/8/8/1pPp4/1P1P4/K1B5 w - - 1 36"));

    list.add(new PgnFileTestCase("both_kings_blocked_in_a_corner_with_pawns_and_bishop_pawn_moves_exhausted.pgn", "",
        "", "", 24, 19, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "k1b5/1p1p4/1P1P4/2p5/2P5/1p1p4/1P1P4/K1B5 w - - 0 37"));

    list.add(new PgnFileTestCase("white_king_blocked_in_a_corner_with_pawns_and_bishop_no_piece_capturable.pgn", "", "",
        "", 24, 19, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "k7/8/8/8/2p5/1pPp4/1P1P4/K1B5 w - - 1 43"));

    list.add(new PgnFileTestCase("white_king_trapped_on_side_with_pawns_capturing_only_piece_stalemates.pgn", "", "",
        "", 11, 8, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "k7/8/1p6/1P6/1Pp5/KpP5/1P6/8 w - - 0 37"));

    // unwinnable due to fivefold or seventy-five-move rule
    list.add(new PgnFileTestCase("unwinnable_fivefold_inevitable.pgn",
        "repPos=4: 3...Kxg8 5...Kg8 7...Kg8 9...Kg8; repPos=4: 4.Kh6 6.Kh6 8.Kh6 10.Kh6; repPos=4: 4...Kh8 6...Kh8 8...Kh8 10...Kh8; repPos=4: 5.Kg6 7.Kg6 9.Kg6 11.Kg6",
        "", "", 1, 15, CheckmateOrStalemate.NA, 4, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "7k/8/2R3K1/8/8/8/8/8 b - - 15 11"));

    list.add(new PgnFileTestCase("unwinnable_seventy_five_move_rule_inevitable.pgn", "", "",
        "26.NA (1) 75...NA (100) 100.NA (149)", -1, 149, CheckmateOrStalemate.NA, 1, InsufficientMaterial.BLACK_ONLY,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "7k/8/2R3K1/8/8/8/8/8 b - - 149 100"));

    return new PgnFileTestCaseList(PgnTest.SPECIAL, list);
  }

  private static PgnFileTestCaseList createTestCasesDgtLiveChess() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("01_threefold_fails_castle_right.pgn", "repPos=3: 3...Nb8 5...Nb8 7...Ng8", "", "", -1,
        12, CheckmateOrStalemate.NA, 3, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "rnbqkbnr/ppppppp1/7p/8/8/7P/PPPPPPP1/RNBQKBNR w Qkq - 12 8"));
    list.add(new PgnFileTestCase("02_fivefold_fails_too_early.pgn",
        "repPos=6: 1...e5 3...Nb8 5...Nb8 7...Bf8 9...Ng8 11...Ng8", "", "", -1, 20, CheckmateOrStalemate.NA, 6,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 20 12"));
    return new PgnFileTestCaseList(PgnTest.DGT_LIVE_CHESS, list);
  }

  private static PgnFileTestCaseList createTestCasesDgtCentaur() {
    final List<PgnFileTestCase> list = new ArrayList<>();
    list.add(new PgnFileTestCase("black_insufficient_material_KQ_KN.pgn", "", "", "", 3, 20, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/8/8/QK3n2/6k1 w - - 1 43"));
    list.add(new PgnFileTestCase("black_insufficient_material_KQ_KB.pgn", "", "", "", 6, 13, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/8/5b2/QK6/6k1 w - - 1 35"));
    list.add(new PgnFileTestCase("black_insufficient_material_KR_KB.pgn", "", "", "", 5, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/8/8/QK4kb/8 w - - 1 44"));

    list.add(new PgnFileTestCase("centaur_blunders.pgn", "", "", "45.Kc2 (1) 94...Nc2 (100)", 5, 100,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/2k1K3/8/8/2n5/2R5 w - - 100 95"));

    list.add(new PgnFileTestCase("fifty_move_rule_reached_by_human_move.pgn", "", "", "42...Kf4 (1) 92.Kd3 (100)", 5,
        100, CheckmateOrStalemate.NA, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/8/3k4/8/2B5/3K4/5B2/8 b - - 100 92"));
    list.add(new PgnFileTestCase("fifty_move_rule_reached_by_computer_move.pgn", "", "", "66.Ke4 (1) 115...Kh6 (100)",
        5, 100, CheckmateOrStalemate.NA, 1, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/5B2/7k/5K2/3B4/8/8/8 w - - 100 116"));

    list.add(new PgnFileTestCase("threefold_repetition_reached_by_human_move.pgn", "repPos=3: 42.Kxe3 44.Ke3 46.Ke3",
        "", "", 6, 18, CheckmateOrStalemate.NA, 3, InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/8/8/8/8/4K3/5R2/7k b - - 8 46"));
    list.add(new PgnFileTestCase("threefold_repetition_reached_by_computer_move.pgn",
        "repPos=3: 35...Kxh1 37...Kh1 39...Kh1", "", "", 5, 15, CheckmateOrStalemate.NA, 3,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/8/4K3/5R2/7k w - - 8 40"));

    return new PgnFileTestCaseList(PgnTest.DGT_CENTAUR, list);
  }

  private static PgnFileTestCaseList createTestCasesUnfairLichessAnalysisGames() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("02dMeCVV.pgn", "", "", "", 19, 32, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/4k3/2p4p/p1Pp1p1P/P2P1P2/8/6K1 w - - 32 58"));
    list.add(new PgnFileTestCase("03VIxJUJ.pgn", "", "", "", 6, 13, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/1pp5/k1b5/Qr6/8/8/p1K5/8 b - - 1 55"));
    list.add(new PgnFileTestCase("0dKUIfwq.pgn", "", "", "", 6, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "7K/6qP/8/8/8/4k3/8/8 w - - 1 76"));
    list.add(new PgnFileTestCase("0ehvDno9.pgn", "", "", "", 6, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/4RP2/8/P6k/PB6/4q1K1 w - - 0 47"));
    list.add(new PgnFileTestCase("0pZSCKZV.pgn", "", "", "", 11, 12, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/5k2/1p2p3/1P2Pp1p/3K1P1P/8/8/8 w - - 1 56"));
    list.add(new PgnFileTestCase("0u6eh8ZP.pgn", "", "", "", 9, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/1k6/8/Kp6/1P6/8/8/8 w - - 0 51"));
    list.add(new PgnFileTestCase("0xMzcUDT.pgn", "", "", "", 21, 14, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "2K5/2q5/4P3/8/5P2/3P4/k7/8 w - - 0 68"));
    list.add(new PgnFileTestCase("0yJiwUDU.pgn", "", "", "", 25, 46, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/2p1k2p/p1Pp2pP/P2P2P1/8/3K4/8 b - - 46 71"));
    list.add(new PgnFileTestCase("14SOSdb3.pgn", "", "", "", 22, 14, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/1k1p3p/2pPp2p/p1P1P2P/P3K3/8/8 b - - 5 59"));
    list.add(new PgnFileTestCase("16myWOwv.pgn", "", "", "", 15, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/8/2p5/8/2n2K2/3kQ3 b - - 2 61"));
    list.add(new PgnFileTestCase("1rcB0Tru.pgn", "", "", "", 10, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "6Rk/8/2b1p2K/pp2r3/2p5/8/8/8 b - - 1 42"));
    list.add(new PgnFileTestCase("1TWhVJGR.pgn", "", "", "", 12, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "7k/5K1P/8/8/8/8/8/8 b - - 2 61"));
    list.add(new PgnFileTestCase("29moHRC8.pgn", "", "", "", 10, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/6p1/7p/6p1/4K1Qk/8/8/8 b - - 5 52"));
    list.add(new PgnFileTestCase("2gCvxApk.pgn", "", "", "", 16, 21, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/4Q3/2k5/P7/6q1/6K1 w - - 0 61"));
    list.add(new PgnFileTestCase("2k5piUl6.pgn", "", "", "", 14, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "R7/6k1/8/8/8/1P6/KPP5/r7 w - - 1 50"));
    list.add(new PgnFileTestCase("2Lb1nhb5.pgn", "", "", "", 4, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "k7/5R2/1Q6/8/2P5/6Pp/P4PKP/8 w - - 0 50"));
    list.add(new PgnFileTestCase("2mgjVvcC.pgn", "", "", "", 10, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "3Q4/2p4p/p6k/5pp1/3b3K/6P1/PP5P/5q2 w - - 0 32"));
    list.add(new PgnFileTestCase("2nK9yweT.pgn", "", "", "", 14, 16, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/2p1k3/p1p1p1p1/P1PpP1P1/2bP1K2/8/8 b - - 16 60"));
    list.add(new PgnFileTestCase("2pwjX9B1.pgn", "", "", "", 5, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "6Q1/7k/p7/1p4K1/8/5r2/8/4qr2 b - - 0 47"));
    list.add(new PgnFileTestCase("2sFHWmg3.pgn", "", "", "", 3, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "7b/5k1K/7P/8/8/8/8/8 w - - 0 57"));
    list.add(new PgnFileTestCase("2wVTK5vV.pgn", "", "", "", 11, 14, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "5qK1/7P/8/k7/8/8/8/8 w - - 3 62"));
    list.add(new PgnFileTestCase("33Tpyihf.pgn", "", "", "", 7, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/2p5/8/2PK4/3Q4/7Q/2k5 b - - 0 53"));
    list.add(new PgnFileTestCase("3bKyjTHM.pgn", "", "", "", 18, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/p7/5pp1/8/Qp3K2/k7 b - - 3 54"));
    list.add(new PgnFileTestCase("3cYOGNS6.pgn", "", "", "", 11, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "Q7/8/4Q3/7k/5Pp1/5KP1/7P/8 w - - 0 43"));
    list.add(new PgnFileTestCase("3jesiit8.pgn", "", "", "", 11, 22, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/8/3Q4/P1P2PP1/1k4Kq w - - 22 54"));
    list.add(new PgnFileTestCase("3sU6yQo5.pgn", "", "", "", 35, 64, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/1k6/6p1/3p1pP1/p1pP1Pp1/P1P3P1/8/5K2 b - - 24 111"));
    list.add(new PgnFileTestCase("3uoeIjQi.pgn", "", "", "", 17, 49, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/1k6/p1p1p1p1/P1P1P1Pb/7K/4B3/8 w - - 49 80"));
    list.add(new PgnFileTestCase("3xFc21eB.pgn", "", "", "", 15, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "6Kr/4k1P1/8/8/8/5R2/8/8 w - - 4 53"));
    list.add(new PgnFileTestCase("43ghpltO.pgn", "", "", "", 12, 22, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/7k/8/p2p1p1p/P2P1P1P/2K5/8/8 w - - 22 71"));
    list.add(new PgnFileTestCase("47GuK7dy.pgn", "", "", "", 16, 19, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "7k/7P/5K2/8/8/8/8/8 b - - 2 116"));
    list.add(new PgnFileTestCase("49QwULSR.pgn", "", "", "", 10, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/6K1/8/6p1/7p/7k/6Q1/8 b - - 3 64"));
    list.add(new PgnFileTestCase("4KKvXluk.pgn", "", "", "", 5, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/5k2/1p1p4/1P1Pp1p1/4P1P1/3K2P1/8 w - - 2 51"));
    list.add(new PgnFileTestCase("55wBEu8Z.pgn", "", "", "", 11, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/p1r5/5R1p/Pp1p2qk/3PpK1P/4Pp1Q/5P2/8 w - - 9 43"));
    list.add(new PgnFileTestCase("5inggIqz.pgn", "", "", "", 3, 18, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/2k3p1/p1p1p1P1/P1P1P1P1/3K4/8 w - - 11 60"));
    list.add(new PgnFileTestCase("5YEU1wHP.pgn", "", "", "", 10, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/2b5/4pkp1/8/5pP1/4q3/3r4/5K2 w - - 2 41"));
    list.add(new PgnFileTestCase("6HdGxhSR.pgn", "", "", "", 13, 15, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/p1p2p1k/P1P2P1p/3K3P/8/8 w - - 15 53"));
    list.add(new PgnFileTestCase("6kfJPvsA.pgn", "", "", "", 6, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/7p/8/1K6/8/6p1/7k/7Q b - - 1 57"));
    list.add(new PgnFileTestCase("6u1akjTw.pgn", "", "", "", 31, 38, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/4k3/p1p2p1p/PpP2P1P/1P2K3/8 b - - 38 90"));
    list.add(new PgnFileTestCase("6XQZaBGI.pgn", "", "", "", 14, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/4bkp1/8/K2nP2p/3r4/8/8/5q2 w - - 0 50"));
    list.add(new PgnFileTestCase("78XhRaIr.pgn", "", "", "", 32, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/6qK/7P/8/8/2k5/8/8 w - - 0 73"));
    list.add(new PgnFileTestCase("7alMRe6y.pgn", "", "", "", 3, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "2kR4/2p5/1pK2p2/r3p3/8/8/8/8 b - - 0 47"));
    list.add(new PgnFileTestCase("7CxnEuV4.pgn", "", "", "", 15, 42, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/p5k1/Pp1p1p1p/1P1P1P1P/1K6/8/8 w - - 42 68"));
    list.add(new PgnFileTestCase("7fWsFh0J.pgn", "", "", "", 6, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/8/5K2/6pk/7R/7q b - - 1 60"));
    list.add(new PgnFileTestCase("7uopV5nZ.pgn", "", "", "", 18, 33, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "6k1/8/8/1p1p2p1/1P1P2P1/5K2/8/8 w - - 33 59"));
    list.add(new PgnFileTestCase("83SCI1Fg.pgn", "", "", "", 9, 48, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "6k1/8/8/1p2p1p1/pPp1P1P1/P1P5/8/K7 w - - 48 78"));
    list.add(new PgnFileTestCase("85kfBN8B.pgn", "", "", "", 7, 46, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/2k5/1p6/1Pp1p3/2P1Pp1p/5P1P/8/5K2 b - - 46 75"));
    list.add(new PgnFileTestCase("87JajHeg.pgn", "", "", "", 6, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/3k4/1p2p2p/1P2P2P/8/6K1/8 b - - 0 50"));
    list.add(new PgnFileTestCase("8B3XZBsv.pgn", "", "", "", 14, 73, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/2k5/4p3/p1p1P1p1/P1P3P1/8/1K6/8 b - - 73 83"));
    list.add(new PgnFileTestCase("8cTl7SrQ.pgn", "", "", "", 15, 15, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/7R/8/5bp1/6rk/5n2/5K2/8 b - - 0 48"));
    list.add(new PgnFileTestCase("8FUSHxUV.pgn", "", "", "", 6, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "k7/P1K5/8/8/8/8/8/8 b - - 2 58"));
    list.add(new PgnFileTestCase("8sompKsV.pgn", "", "", "", 8, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/4R3/5p1k/5Pp1/6Pp/8/6KP/7B b - - 3 60"));
    list.add(new PgnFileTestCase("8TigzhbJ.pgn", "repPos=3: 22...Bd7 24...Rac8 26...Rac8", "", "", 22, 90,
        CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/2k4p/1p2p1pP/1P1pPpP1/3P1P2/8/8/K7 b - - 90 94"));
    list.add(new PgnFileTestCase("8uFm7Zvw.pgn", "", "", "", 9, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "3R4/5p1k/1N3Q2/7q/5P1K/1P4P1/7P/8 w - - 5 45"));
    list.add(new PgnFileTestCase("992nwupc.pgn", "", "", "", 18, 23, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/1k6/4p3/3pP1p1/1p1P2P1/1P6/8/1K6 b - - 23 56"));
    list.add(new PgnFileTestCase("9AqVp2F8.pgn", "", "", "", 16, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/5p2/8/p4pK1/k4N2/1R6/P7/8 b - - 0 55"));
    list.add(new PgnFileTestCase("9FpfMBVq.pgn", "", "", "", 7, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/p7/Pp1p1p1k/1P1P1Pp1/3b2P1/3K1B2/8 b - - 4 54"));
    list.add(new PgnFileTestCase("9jxoYRyn.pgn", "", "", "", 9, 37, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/6k1/5p2/1p2pP1p/1P2P2P/7K/8/8 b - - 37 72"));
    list.add(new PgnFileTestCase("9T0wrl30.pgn", "", "", "", 20, 26, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/p7/Pp1k2p1/1P1p1pPp/3P1P1P/6K1/8/8 b - - 26 52"));
    list.add(new PgnFileTestCase("9vMq768Z.pgn", "", "", "", 5, 17, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1rK1k3/2P2R2/4Pp2/5P2/8/8/8/8 w - - 17 66"));

    list.add(new PgnFileTestCase("a2l4gphm.pgn", "", "", "", 37, 20, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "5k2/8/3p4/3p1p1p/p1pP1P1P/PpP2K2/1P5B/8 w - - 20 67"));

    list.add(new PgnFileTestCase("A4aWYoPF.pgn", "", "", "", 10, 30, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "4k3/8/5p2/p2p1Pp1/P2P2Pp/1K5P/8/8 w - - 30 62"));
    list.add(new PgnFileTestCase("aefWMnIP.pgn", "", "", "", 3, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/5pk1/5Pp1/7p/7P/2r3PK/1q6/8 b - - 0 51"));
    list.add(new PgnFileTestCase("AHPAU56z.pgn", "", "", "", 5, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/p6p/5kp1/5pP1/5P1K/1r5P/8/8 b - - 0 47"));
    list.add(new PgnFileTestCase("AigeGQsz.pgn", "", "", "", 12, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/5kp1/2r2pP1/5K2/1q6/2q5/8 b - - 0 54"));
    list.add(new PgnFileTestCase("Altbmg1n.pgn", "", "", "", 6, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/4k2p/1p2p1pP/pP2P1P1/P3K3/8/8 b - - 2 42"));
    list.add(new PgnFileTestCase("aoXPcDW6.pgn", "", "", "", 6, 34, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/5k2/1p2p1p1/pP2P1P1/P7/4K3/8 b - - 34 55"));
    list.add(new PgnFileTestCase("APEww4mD.pgn", "", "", "", 5, 31, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/8/8/5K2/2r4p/6Rk b - - 31 84"));
    list.add(new PgnFileTestCase("aWI97MHZ.pgn", "", "", "", 18, 16, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/1k1p2p1/2pPp1P1/1pP1P1K1/1P6/8 w - - 16 51"));
    list.add(new PgnFileTestCase("aXUqSEe1.pgn", "", "", "", 5, 20, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/6k1/1p1p1p1p/1P1P1P1P/8/8/7K w - - 0 70"));
    list.add(new PgnFileTestCase("B3mIj9Su.pgn", "", "", "", 18, 21, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/5p2/8/4p3/3r2p1/4K1kR b - - 21 75"));
    list.add(
        new PgnFileTestCase("B8bpMZ71.pgn", "", "", "", 11, 13, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
            UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
            UnwinnableQuick.UNWINNABLE, "2Q5/8/5Q2/6B1/8/1P2P1P1/P3kPq1/5RK1 w - - 1 55"));
    list.add(new PgnFileTestCase("bDxnc7TH.pgn", "", "", "", 14, 12, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/1p1k2p1/pP1p1pP1/P2P1P2/3K4/8/8 b - - 2 58"));
    list.add(new PgnFileTestCase("BFfZguIQ.pgn", "", "", "", 10, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/p4pk1/3b4/5Pp1/6q1/8/8/7K w - - 2 43"));
    list.add(new PgnFileTestCase("bfJbt6GP.pgn", "", "", "", 7, 12, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/7p/4npp1/1pk5/1P6/2K5/1r1r4/8 b - - 0 52"));
    list.add(new PgnFileTestCase("bQ0EXLal.pgn", "", "", "", 10, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1b3Rk1/1p4pp/p7/8/8/7r/2K5/8 b - - 1 47"));
    list.add(new PgnFileTestCase("bXkWOZ49.pgn", "", "", "", 3, 20, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/3k4/6p1/3p1pP1/p1pP1P1K/P1P5/8/8 w - - 20 64"));
    list.add(new PgnFileTestCase("c3ew66ZV.pgn", "", "", "", 9, 32, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/3k4/1p2p1p1/pP1pP1P1/P2P4/1K6/8 b - - 32 62"));
    list.add(new PgnFileTestCase("CezFfltE.pgn", "", "", "", 11, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/p1p1k1p1/P1P1p1P1/4Pp1p/2K2P1P/8 b - - 4 48"));
    list.add(new PgnFileTestCase("cLnhkfGh.pgn", "", "", "", 16, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/4k3/1p1p4/1p1P1p2/1P3P1p/4K2P/8/8 w - - 2 50"));
    list.add(new PgnFileTestCase("CQI03gYb.pgn", "", "", "", 5, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "4k3/8/1p6/1p1p2p1/1P1Pp1P1/1P2P3/8/4K3 w - - 10 46"));
    list.add(new PgnFileTestCase("cZ8QxYP8.pgn", "", "", "", 7, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/k7/7P/8/1PPPPbP1/1NBQKBNR w K - 0 23"));
    list.add(new PgnFileTestCase("D342JjRk.pgn", "", "", "", 14, 70, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/6k1/1p1p4/1PpPp1p1/2P1P1p1/6P1/8/5K2 w - - 40 100"));
    list.add(new PgnFileTestCase("D4vIOthl.pgn", "", "", "", 6, 13, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/6Q1/7k/7p/3K4/8/6p1/8 b - - 0 65"));
    list.add(new PgnFileTestCase("dBergEkt.pgn", "", "", "", 17, 12, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1K4Qk/8/8/8/6p1/5p2/8/8 b - - 0 55"));
    list.add(new PgnFileTestCase("DiKq9jcy.pgn", "", "", "", 9, 14, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/6kP/3Q2P1/3qP3/1BK5/2P5/8/8 w - - 3 65"));
    list.add(new PgnFileTestCase("DQxKRa2g.pgn", "", "", "", 11, 36, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/6p1/k2p1pP1/1p1P1P2/pP6/P7/2K5 b - - 15 68"));
    list.add(new PgnFileTestCase("dZhy51vS.pgn", "", "", "", 12, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "2R5/8/1p6/kP6/p6p/P7/6KP/1R6 b - - 1 48"));
    list.add(new PgnFileTestCase("E0TisOA3.pgn", "", "", "", 4, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/5Q2/4R3/3p2k1/3P3p/P4PKP/8 w - - 0 55"));
    list.add(new PgnFileTestCase("E8I7AMGj.pgn", "", "", "", 10, 18, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "3k4/2p5/1pPp2p1/1P1P1pPp/5P1P/2K5/8/8 w - - 18 58"));
    list.add(new PgnFileTestCase("E9P918aj.pgn", "", "", "", 18, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/3k2qK/7P/8/8 w - - 0 51"));
    list.add(new PgnFileTestCase("eC0QpBxu.pgn", "", "", "", 9, 47, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/3k4/p2p2p1/P2P2P1/8/8/1K6/8 b - - 47 77"));
    list.add(new PgnFileTestCase("eExHpNDf.pgn", "", "", "", 11, 34, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "3NR3/4P3/8/8/4k3/6P1/6q1/6K1 w - - 17 84"));
    list.add(new PgnFileTestCase("efAanuaT.pgn", "", "", "", 10, 54, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/4k3/3p2p1/p1pP2P1/P1P5/4K3/8/8 b - - 35 89"));
    list.add(new PgnFileTestCase("EFd9NOSF.pgn", "", "", "", 15, 49, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/p2k4/Pp1p2p1/1P1Pp1P1/4P3/8/6K1 b - - 49 89"));
    list.add(new PgnFileTestCase("eKwKl6Y9.pgn", "", "", "", 17, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/7P/8/6qK/8/2k5 w - - 0 70"));
    list.add(new PgnFileTestCase("EPTBN53W.pgn", "", "", "", 3, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/8/8/5k1p/7K w - - 2 58"));
    list.add(new PgnFileTestCase("EQCMW0jB.pgn", "", "", "", 6, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/7r/p4K1k/8/7R/6q1 b - - 0 58"));
    list.add(new PgnFileTestCase("EsFX1Urt.pgn", "", "", "", 7, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/7p/4K2k/6R1/8/8/8 b - - 0 54"));
    list.add(new PgnFileTestCase("EstAIWqd.pgn", "", "", "", 15, 13, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/8/8/1K4p1/6rk/7R b - - 0 65"));
    list.add(new PgnFileTestCase("Ew7uTqu0.pgn", "", "", "", 13, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "6kR/p4pp1/6b1/8/5K2/r7/8/8 b - - 3 48"));
    list.add(new PgnFileTestCase("f7x3bSzh.pgn", "", "", "", 12, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "k6r/p4p1p/2b3p1/1B6/8/N3b1P1/P4p1P/1R5K w - - 0 34"));
    list.add(new PgnFileTestCase("fAjvL1hG.pgn", "", "", "", 15, 52, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/7k/1p2p1p1/pP2P1Pp/P6P/8/2K5/8 w - - 52 75"));
    list.add(new PgnFileTestCase("FBqHYgEu.pgn", "repPos=3: 40.Ke2 68.Ke2 70.Ke2; repPos=3: 40...Kd6 68...Kd6 70...Kd6",
        "", "", 23, 88, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/8/4k3/p1p2p1p/PpP2P1P/1P6/1K6/8 b - - 22 96"));
    list.add(new PgnFileTestCase("fbzlzlUK.pgn", "", "", "", 4, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "5b2/5pk1/3r1P2/2n4K/4b3/6q1/8/8 b - - 0 55"));
    list.add(new PgnFileTestCase("FMDN8gJ7.pgn", "", "", "", 5, 12, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/2p5/2kb4/1Q6/8/4K3 b - - 12 68"));
    list.add(new PgnFileTestCase("fN4vRqp0.pgn", "", "", "", 5, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/6Q1/8/7k/5p2/5RK1 w - - 0 73"));
    list.add(new PgnFileTestCase("fPnoYQtW.pgn", "", "", "", 11, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/1K5p/8/8/6pQ/7k b - - 4 52"));
    list.add(new PgnFileTestCase("FSZeGcrT.pgn", "", "", "", 5, 60, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/k7/2p1p3/1pP1P1p1/pP4P1/P7/2K5 w - - 60 84"));
    list.add(new PgnFileTestCase("fuNe9BZw.pgn", "", "", "", 3, 97, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/4k3/p1p2p1p/P1P2P1P/8/3K4/8 w - - 19 95"));
    list.add(new PgnFileTestCase("FYTeO80q.pgn", "", "", "", 7, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/6Qp/p3B1p1/7k/P4Pq1/7K/1P3R1P/8 w - - 0 38"));
    list.add(new PgnFileTestCase("G0xDBYwE.pgn", "", "", "", 6, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "7k/8/8/KqN5/2P5/8/8/8 w - - 0 59"));
    list.add(new PgnFileTestCase("G8i5Bn7F.pgn", "", "", "", 11, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "7Q/7k/5K2/8/8/8/6p1/8 b - - 2 57"));
    list.add(new PgnFileTestCase("gcJZWCKq.pgn", "", "", "", 6, 19, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "7k/7P/5K2/8/8/8/8/8 b - - 0 79"));
    list.add(new PgnFileTestCase("GCuwJ5bP.pgn", "", "", "", 6, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/Kq6/P5k1/8/8/8 w - - 0 52"));
    list.add(new PgnFileTestCase("GgiPdCBU.pgn", "", "", "", 19, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/1p1k4/1Pp1p1p1/p1P1PpP1/P4P2/8/5K2 b - - 10 48"));
    list.add(new PgnFileTestCase("GH6YBJIu.pgn", "", "", "", 12, 14, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/2p2k2/p1Pp2p1/P2P2P1/1K6/8/8 b - - 9 52"));
    list.add(new PgnFileTestCase("GibMYT4i.pgn", "", "", "", 26, 46, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/4k3/4b3/1p1p1p1p/1P1P1P1P/8/8/7K w - - 46 78"));
    list.add(new PgnFileTestCase("GIwBNz5S.pgn", "", "", "", 14, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/1p6/p7/8/3K4/5b2/5kR1 b - - 0 56"));
    list.add(new PgnFileTestCase("GpCPpsRz.pgn", "", "", "", 28, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "7k/5Q2/8/p1p1NBP1/P1P2P2/7p/6KP/8 w - - 0 47"));
    list.add(new PgnFileTestCase("gPxJdo7u.pgn", "", "", "", 17, 45, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/1k6/1p2p1p1/1P2P1P1/8/8/6K1/8 b - - 40 92"));
    list.add(new PgnFileTestCase("gtzPUYWo.pgn", "", "", "", 13, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/8/4pp2/3Rkp2/4r3/5K2 b - - 0 66"));
    list.add(new PgnFileTestCase("Gw5OAmSo.pgn", "", "", "", 16, 13, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/3k4/3p4/p1pPp2p/P1P1P2P/8/2K5/8 w - - 13 64"));
    list.add(new PgnFileTestCase("GysBobWK.pgn", "", "", "", 11, 31, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/5k2/1p1p2p1/1P1P2P1/8/4K3/8 w - - 31 73"));
    list.add(new PgnFileTestCase("GzoCTk46.pgn", "", "", "", 9, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/7p/5p2/5kp1/4Q3/8/4K3 b - - 5 58"));
    list.add(new PgnFileTestCase("h2INNt6T.pgn", "", "", "", 3, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "7R/6pk/6rp/p6K/P6P/8/8/6r1 b - - 3 40"));
    list.add(new PgnFileTestCase("H4MIp43q.pgn", "", "", "", 5, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/1k6/p1p3p1/P1Pp1pP1/K2P1P2/8/8/8 b - - 2 53"));
    list.add(new PgnFileTestCase("H968wWnH.pgn", "", "", "", 3, 15, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/5k2/8/2p2p2/p1P2P1p/P3K2P/8/8 w - - 15 51"));
    list.add(new PgnFileTestCase("hdott1S4.pgn", "", "", "", 25, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/5R2/6PN/5B1K/5k2/7r/8 w - - 1 59"));
    list.add(new PgnFileTestCase("hHLqO2Sb.pgn", "", "", "", 5, 49, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/2k4p/1p1p2pP/1P1P2P1/8/5K2/8 b - - 49 67"));
    list.add(new PgnFileTestCase("hkiG96je.pgn", "", "", "", 10, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "6qK/8/7P/8/8/k7/8/8 w - - 0 59"));
    list.add(new PgnFileTestCase("hRedYjtT.pgn", "", "", "", 7, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "7R/6pk/p5p1/8/1b2p3/4P3/b1r5/K7 b - - 5 39"));
    list.add(new PgnFileTestCase("HX7UOzXi.pgn", "", "", "", 20, 25, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/8/8/5k1p/7K w - - 2 85"));
    list.add(new PgnFileTestCase("I0zJt0qZ.pgn", "", "", "", 13, 22, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/2k4p/1p2p1pP/1P2P1P1/8/3K4 b - - 15 79"));
    list.add(new PgnFileTestCase("I9MLIaiu.pgn", "", "", "", 20, 29, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/4k3/4p2p/p1p1P2P/P1P2K2/8/8/8 w - - 29 68"));
    list.add(new PgnFileTestCase("IcjtzT9i.pgn", "", "", "", 25, 26, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/2k3qK/5P1P/8/8 w - - 0 77"));
    list.add(new PgnFileTestCase("IdZxNXzF.pgn", "", "", "", 23, 19, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/8/5k2/7p/7K w - - 0 79"));
    list.add(new PgnFileTestCase("if6AKvpb.pgn", "", "", "", 5, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/3k4/1P2R3/4KP2/P2q4/8 w - - 7 51"));
    list.add(new PgnFileTestCase("ig7fudY7.pgn", "", "", "", 8, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/8/5K1R/1r5k/6p1/8 b - - 0 53"));
    list.add(new PgnFileTestCase("iNx1gFMc.pgn", "", "", "", 26, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/1p2k1p1/pP1p1pPp/P2P1P1P/5K2/8/8 b - - 0 49"));
    list.add(new PgnFileTestCase("Ir1xVUbe.pgn", "", "", "", 12, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/3k4/p2p2p1/P2P2P1/1K6/8/8 b - - 7 55"));
    list.add(new PgnFileTestCase("IT4IGHvQ.pgn", "", "", "", 17, 21, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/p1k3p1/P1p2pPp/2P2P1P/4K3/8 b - - 21 48"));
    list.add(new PgnFileTestCase("Ity8OMJy.pgn", "", "", "", 9, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/4k2p/p1p2p1P/P1P2Pp1/4K1P1/8/8 w - - 10 53"));
    list.add(new PgnFileTestCase("Iy1VEUMC.pgn", "", "", "", 9, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "3Kq3/2P3k1/8/8/P7/8/8/8 w - - 4 59"));
    list.add(new PgnFileTestCase("j17qnxd0.pgn", "", "", "", 16, 38, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "4K2k/6Q1/7p/8/8/8/8/8 b - - 38 67"));
    list.add(new PgnFileTestCase("j4phmaNj.pgn", "", "", "", 11, 18, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "5Kq1/8/4k3/8/7P/8/8/8 w - - 1 67"));
    list.add(new PgnFileTestCase("jehHS0Jj.pgn", "", "", "", 10, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/6k1/8/6qK/8/7P/8/8 w - - 0 65"));
    list.add(new PgnFileTestCase("JfHZLRvD.pgn", "", "", "", 6, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "r7/pp6/3p1p2/2p1k3/2q1Q2P/5KP1/5P2/3R3R b - - 0 33"));
    list.add(new PgnFileTestCase("JIyquz2Q.pgn", "", "", "", 8, 25, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/2k5/6p1/1p1p1pPp/pP1P1P1P/P7/5K2/8 b - - 25 55"));
    list.add(new PgnFileTestCase("jK46bYGo.pgn", "", "", "", 12, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "6kQ/p4rp1/4r3/3K4/8/8/8/8 b - - 6 48"));
    list.add(new PgnFileTestCase("jnox9kxK.pgn", "", "", "", 36, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/5K2/8/p7/1Q6/k1q5 b - - 0 86"));
    list.add(new PgnFileTestCase("jnsJhG8T.pgn", "", "", "", 27, 48, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/1k6/1p1pB3/pPpP4/P1Pp1p1p/3PbP1P/6K1/8 w - - 48 85"));
    list.add(new PgnFileTestCase("jnvJUlDw.pgn", "", "", "", 11, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/2p4Q/3rn1pk/6p1/2K5/8/8/8 b - - 2 48"));
    list.add(new PgnFileTestCase("JQpTTEF7.pgn", "", "", "", 5, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/2k5/p1p5/P1P1p1p1/P3P1Pp/4K2P/8/8 w - - 1 40"));
    list.add(new PgnFileTestCase("K5d1o8ea.pgn", "", "", "", 11, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/8/8/5k1p/7K w - - 2 61"));
    list.add(new PgnFileTestCase("KbEG3yK3.pgn", "", "", "", 20, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/7p/8/8/8/b4K2/kQ6 b - - 0 61"));
    list.add(new PgnFileTestCase("kbutR5IJ.pgn", "", "", "", 8, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "2k1Q3/2p5/2K5/2N5/2q5/4q3/8/8 b - - 0 53"));
    list.add(new PgnFileTestCase("Kdf0f1OE.pgn", "", "", "", 21, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/8/5K2/7p/7k/6R1 b - - 0 69"));
    list.add(new PgnFileTestCase("KE47c49r.pgn", "", "", "", 6, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "7R/5ppk/6bp/8/4r3/2K5/8/8 b - - 11 46"));
    list.add(new PgnFileTestCase("KeK8Chul.pgn", "", "", "", 17, 14, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/2k5/1p1p2p1/1P1P2Pp/7P/8/6K1 b - - 14 52"));
    list.add(new PgnFileTestCase("kj6Dk7Fb.pgn", "", "", "", 5, 39, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/6k1/p2p1p1p/P2P1P1P/8/8/4K3 b - - 39 64"));
    list.add(new PgnFileTestCase("knQsF7R8.pgn", "", "", "", 7, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "5qK1/3k3P/8/8/6P1/8/8/8 w - - 5 49"));
    list.add(new PgnFileTestCase("KP23RUWa.pgn", "", "", "", 11, 17, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/5k2/7K/8/1Q5q/P7/8 w - - 17 61"));
    list.add(new PgnFileTestCase("KPvcgsfS.pgn", "", "", "", 11, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "1rK5/2R5/4k3/8/8/6P1/P7/8 w - - 0 48"));
    list.add(new PgnFileTestCase("KQKPJzpe.pgn", "", "", "", 5, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/k4p2/p1p2p1p/P1P2P1P/5K2/8 b - - 11 48"));
    list.add(new PgnFileTestCase("KQvOkgie.pgn", "", "", "", 12, 58, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/1k6/3p2p1/2pP1pP1/p1P2P2/P3K3/8/8 b - - 58 75"));
    list.add(new PgnFileTestCase("Ks0yR98N.pgn", "", "", "", 4, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "7Q/6P1/8/8/7r/5k1K/8/8 w - - 0 61"));
    list.add(new PgnFileTestCase("l8Wwo7if.pgn", "", "", "", 6, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/7p/3q1kp1/6P1/4K3/3r4/8/8 b - - 0 56"));
    list.add(new PgnFileTestCase("LbP5CIDy.pgn", "", "", "", 21, 39, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/2k1p1p1/1p1pPpPp/1P1P1P1P/1K6/8/8 b - - 38 105"));
    list.add(new PgnFileTestCase("LC76ur18.pgn", "", "", "", 3, 15, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/2p5/3b4/8/2K5/R7/kq6 b - - 1 65"));
    list.add(new PgnFileTestCase("Lf3eQe81.pgn", "", "", "", 3, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/4k3/1p1p2p1/1P1P2P1/1P1K4/8/8 w - - 6 57"));
    list.add(new PgnFileTestCase("lHtqM0fz.pgn", "", "", "", 22, 19, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/8/6K1/8/7p/6Rk b - - 7 74"));
    list.add(new PgnFileTestCase("lk42iihk.pgn", "", "", "", 5, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/R7/8/8/6k1/6P1/5Q1K/7q w - - 2 42"));
    list.add(new PgnFileTestCase("LqTNOgyT.pgn", "", "", "", 23, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/3Q4/8/P6P/5k2/8/3q1K2 w - - 0 53"));
    list.add(new PgnFileTestCase("LTFQx2YH.pgn", "", "", "", 7, 17, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/4k3/p1p2p1p/P1P2P1P/K7/8/8 b - - 17 66"));
    list.add(new PgnFileTestCase("Lv0hWAvD.pgn", "", "", "", 21, 34, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/p4k1p/Pp1p2pP/1P1P2P1/4K3/8/8 b - - 4 67"));
    list.add(new PgnFileTestCase("lYf34Uiq.pgn", "", "", "", 14, 13, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/2K5/8/8/7p/5Qk1 b - - 5 69"));
    list.add(new PgnFileTestCase("m5WDCW16.pgn", "", "", "", 17, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/6k1/8/7R/8/6PP/5PBK/7r w - - 5 42"));
    list.add(new PgnFileTestCase("M9Xic74G.pgn", "", "", "", 15, 31, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "6k1/8/6p1/1p2p1Pp/1P2P2P/6K1/8/8 w - - 31 67"));
    list.add(new PgnFileTestCase("mBrCKInG.pgn", "", "", "", 9, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "3r4/5pk1/p3pPp1/1p2P3/4K3/r7/3q4/8 b - - 0 45"));
    list.add(new PgnFileTestCase("McLow5Hz.pgn", "", "", "", 5, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/7P/8/4k3/4B3/5PR1/6PK/7r w - - 1 43"));
    list.add(new PgnFileTestCase("MgqytYeQ.pgn", "", "", "", 8, 15, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/6k1/5p2/1p2pP1p/1P2P2P/5K2/8/8 b - - 15 52"));
    list.add(new PgnFileTestCase("Mm4HIjDh.pgn", "", "", "", 18, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/2p1k1p1/1pP1p1P1/1P2P3/7K/8/8 b - - 5 43"));
    list.add(new PgnFileTestCase("MOyPDylu.pgn", "repPos=3: 46.Bc2 56.Bc2 60.Ke2", "", "", 14, 56,
        CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/k1p1p1p1/1pPpP1Pp/1P1PbK1P/2B5/8 b - - 12 80"));
    list.add(new PgnFileTestCase("MvEif0NV.pgn", "", "", "", 19, 27, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/2k3qK/6PP/8/8/8 w - - 0 67"));
    list.add(new PgnFileTestCase("MXeVCHSn.pgn", "", "", "", 3, 46, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "1k6/8/8/p1p2p2/P1P2P1p/2P2P1P/3K4/8 w - - 37 75"));
    list.add(new PgnFileTestCase("mXxGD2IV.pgn", "", "", "", 12, 90, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/6k1/8/2p2p1p/1pP1pP1P/1P2P3/4K3/8 w - - 90 100"));
    list.add(new PgnFileTestCase("N6AYM22R.pgn", "", "", "", 4, 16, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/5pp1/6kp/7P/5n2/p7/6r1/7K b - - 0 58"));
    list.add(new PgnFileTestCase("ncuz3jy9.pgn", "", "", "", 6, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/7p/6pk/4p3/6P1/5q2/3K1P2/1q6 w - - 0 47"));
    list.add(new PgnFileTestCase("nECJszX7.pgn", "", "", "", 18, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/p1p5/Ppkp4/1P6/K7/2q5/7p/8 b - - 0 46"));
    list.add(new PgnFileTestCase("NmQfkdhK.pgn", "", "", "", 16, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "4Rk2/5p2/5Pp1/6P1/8/6p1/5r1r/6K1 b - - 6 52"));
    list.add(new PgnFileTestCase("nNXmBTlz.pgn", "", "", "", 5, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/p1k5/Pp1p2p1/1P1P2Pp/3K3P/8/8 b - - 0 46"));
    list.add(new PgnFileTestCase("nvxFBquo.pgn", "", "", "", 10, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "7K/2P2k1r/3P4/8/8/6R1/8/8 w - - 0 64"));
    list.add(new PgnFileTestCase("o6TGucU0.pgn", "", "", "", 5, 20, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/p5kp/5n1P/8/7K/5q2/8/6r1 b - - 0 57"));
    list.add(new PgnFileTestCase("OCeJcw04.pgn", "", "", "", 24, 27, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/5k2/1p1p4/1P1P1p2/1K3Pp1/1P4P1/8/8 b - - 4 74"));
    list.add(new PgnFileTestCase("odke8ePj.pgn", "", "", "", 12, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/5pkp/8/7P/6p1/5pP1/5P1K/5q2 w - - 1 57"));
    list.add(new PgnFileTestCase("Oejr5ZFX.pgn", "", "", "", 19, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/5Q2/8/7R/6k1/P5p1/7K w - - 0 47"));
    list.add(new PgnFileTestCase("ogehooFw.pgn", "", "", "", 21, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/p1k5/P1p2p1p/2P2P1P/3K4/8/8 b - - 3 48"));
    list.add(new PgnFileTestCase("om0bCR5w.pgn", "", "", "", 15, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "R4k2/8/5K1p/8/8/6r1/8/7q b - - 1 48"));
    list.add(new PgnFileTestCase("Omq6Fdhm.pgn", "", "", "", 12, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/8/pp6/k7/1Q2K3/8 b - - 2 69"));
    list.add(new PgnFileTestCase("ooSZuhhE.pgn", "", "", "", 5, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1Q6/2k1K3/2p5/8/8/8/8/8 b - - 0 40"));
    list.add(new PgnFileTestCase("oT7sOSw5.pgn", "", "", "", 11, 21, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/2p1k3/p1Pp2p1/Pp1P1pPp/1P3P1P/5P2/3K4 b - - 21 58"));
    list.add(new PgnFileTestCase("p2ujQM8o.pgn", "", "", "", 19, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/6pk/5rP1/7K/5q1P/5P2/8 b - - 0 39"));
    list.add(new PgnFileTestCase("p4CeCK3J.pgn", "", "", "", 6, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "4Q3/8/8/2Q5/8/1P6/1KP5/q6k w - - 1 54"));
    list.add(new PgnFileTestCase("PAjZeTkN.pgn", "", "", "", 6, 18, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/Nk6/8/R7/Kr6/P7/8 w - - 0 64"));
    list.add(new PgnFileTestCase("pBFolKHd.pgn", "", "", "", 5, 33, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/1k6/3p3p/1p1P1p1P/1P2pP1K/4P3/8/8 w - - 33 67"));
    list.add(new PgnFileTestCase("pFsUTatm.pgn", "", "", "", 22, 27, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/5q2/2k1K3/2P1Q3/3P4/4P3/8/8 w - - 1 57"));
    list.add(new PgnFileTestCase("PHCKkpCX.pgn", "", "", "", 6, 51, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/1p2k3/1P1p2p1/3P2Pp/7P/1K6/8 b - - 51 70"));
    list.add(new PgnFileTestCase("pIgctI4e.pgn", "", "", "", 20, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "6k1/R7/4NB2/6p1/8/6PK/5P1P/8 b - - 0 40"));
    list.add(new PgnFileTestCase("PIL4PUtT.pgn", "", "", "", 19, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/3Q4/2P5/1PB3k1/P7/5P1K/R4Rq1 w - - 0 45"));
    list.add(new PgnFileTestCase("PkZ6qiA6.pgn", "", "", "", 7, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/7p/3p4/2pk4/4Q3/p7/Kb6/7q b - - 5 50"));
    list.add(new PgnFileTestCase("pqiyPXvP.pgn", "", "", "", 10, 23, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/2k5/p2p2p1/Pp1Pp1P1/1P2P3/5K2/8 w - - 23 70"));
    list.add(new PgnFileTestCase("pqsImnAT.pgn", "", "", "", 6, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/4k3/1p1p2p1/pP1P2P1/P7/4K3/8/8 b - - 6 52"));
    list.add(new PgnFileTestCase("PUA2eCrY.pgn", "", "", "", 14, 25, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/3k4/1p2p1p1/1P1pP1Pp/1P1P3P/8/8/6K1 w - - 21 63"));
    list.add(new PgnFileTestCase("qDcEiBsg.pgn", "", "", "", 8, 81, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/2k5/4p1p1/p2pPpPp/P2P1P1P/3K4/8/8 b - - 81 91"));
    list.add(new PgnFileTestCase("QirOKYTP.pgn", "", "", "", 11, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "r1b5/ppppn2p/5Qp1/8/1P2q3/N1Pkb3/P4P1P/3RKR2 b - - 1 20"));
    list.add(new PgnFileTestCase("qle7LoyU.pgn", "", "", "", 10, 79, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/1k1p4/p1pP1p1p/P1P2P1P/8/3K4/8 b - - 79 83"));
    list.add(new PgnFileTestCase("QnFE4Znl.pgn", "", "", "", 3, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/8/6QP/6PK/2k3q1 w - - 6 51"));
    list.add(new PgnFileTestCase("QPdYSKDq.pgn", "", "", "", 18, 16, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "6r1/8/4kn1K/8/2q2P2/8/8/8 w - - 16 72"));
    list.add(new PgnFileTestCase("QpptleSj.pgn", "", "", "", 4, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/1p6/pPp5/P1P5/8/4nkp1/6p1/6K1 b - - 1 71"));
    list.add(new PgnFileTestCase("qpXuukKR.pgn", "", "", "", 18, 35, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/3p1k1p/p1pPp1pP/P1P1P1P1/8/3K4 w - - 14 67"));
    list.add(new PgnFileTestCase("QSTM2G7V.pgn", "", "", "", 22, 33, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/3k1p2/4pPp1/2p1P1Pp/1pP4P/1P6/8/6K1 b - - 33 56"));
    list.add(new PgnFileTestCase("quMnfy5i.pgn", "", "", "", 6, 19, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/5k2/5p1p/4pP1P/2p1P3/1pP3K1/1P6/8 b - - 3 52"));
    list.add(new PgnFileTestCase("QwKzanAX.pgn", "", "", "", 6, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/2p2pkp/7P/8/8/7K/5qr1/8 b - - 0 38"));
    list.add(new PgnFileTestCase("r1SzzM60.pgn", "", "", "", 17, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "2k5/2P5/8/1KN5/8/8/8/8 b - - 0 66"));
    list.add(new PgnFileTestCase("rbPFjPRm.pgn", "", "", "", 14, 39, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/1k6/3p2p1/1p1P1pPp/pP3P1P/P7/2K5/8 b - - 39 69"));
    list.add(new PgnFileTestCase("RF0MOp86.pgn", "", "", "", 7, 13, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "Q7/k1p5/1p1p4/p7/2r5/r7/8/3K4 b - - 13 57"));
    list.add(new PgnFileTestCase("RJ6gw7gD.pgn", "", "", "", 13, 14, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/6k1/5p2/2p1pPp1/1pP1P1P1/1P3K2/8/8 b - - 4 69"));
    list.add(new PgnFileTestCase("rSC90mAs.pgn", "", "", "", 17, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "k7/P1K5/8/8/8/8/8/8 b - - 2 62"));
    list.add(new PgnFileTestCase("RSqLhejA.pgn", "", "", "", 12, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/4n3/4p3/8/2p5/5K1k/7R/2r3q1 b - - 3 62"));
    list.add(new PgnFileTestCase("RvN1zpCB.pgn", "", "", "", 3, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "6nr/6pp/4pp2/8/1PP5/P4P1k/3q3Q/3B1RK1 b - - 2 25"));
    list.add(new PgnFileTestCase("rVN4T4Tz.pgn", "", "", "", 7, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "K7/1q6/8/P7/8/8/8/6k1 w - - 0 69"));
    list.add(new PgnFileTestCase("RXlEuUhF.pgn", "", "", "", 8, 39, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "7k/8/7p/2p2p1P/1pP2P2/pP2K3/P7/8 w - - 39 64"));
    list.add(new PgnFileTestCase("s2ZKBPfv.pgn", "", "", "", 12, 22, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/1p1k4/pPp5/P1Pp2p1/3P2P1/5K2/8/8 w - - 22 54"));
    list.add(new PgnFileTestCase("sBwTHJz0.pgn", "", "", "", 15, 60, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/7k/p2p2p1/Pp1P2Pp/1P1K3P/8/8 w - - 60 67"));
    list.add(new PgnFileTestCase("SdlLyZvq.pgn", "", "", "", 5, 27, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/4k3/1p2p1p1/1P2P1Pp/7P/7K/8/8 b - - 27 76"));
    list.add(new PgnFileTestCase("SDy9Y3D0.pgn", "", "", "", 20, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/4p3/3Qk1K1/3r4/8/8 b - - 0 54"));
    list.add(new PgnFileTestCase("Sdz1yTzS.pgn", "", "", "", 6, 15, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/6qK/8/8/6P1/P7/4k3 w - - 15 59"));
    list.add(new PgnFileTestCase("sMv8Hh43.pgn", "", "", "", 11, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/6p1/5kP1/7K/2r2q2/8/8/8 b - - 1 52"));
    list.add(new PgnFileTestCase("sp0oSyY2.pgn", "", "", "", 4, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/pp6/8/kQ5K/8/8/b7/2q5 b - - 0 40"));
    list.add(new PgnFileTestCase("SrLwzxpB.pgn", "", "", "", 5, 14, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/3k4/p2p2p1/Pp1P2P1/1P6/8/1K6 b - - 8 54"));
    list.add(new PgnFileTestCase("ST89KN4m.pgn", "", "", "", 5, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/6k1/5n2/6pK/8 w - - 2 67"));
    list.add(new PgnFileTestCase("SwCSdv8K.pgn", "", "", "", 17, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/6p1/8/8/3K4/8/3kQ3 b - - 0 58"));
    list.add(new PgnFileTestCase("SX3iSehH.pgn", "", "", "", 16, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "5KQ1/5q2/4k3/P7/8/8/8/8 w - - 0 55"));
    list.add(new PgnFileTestCase("Sx7EyNdc.pgn", "", "", "", 17, 47, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "3k4/8/2p5/1pP1p1p1/1P2P1P1/3K4/8/8 w - - 47 67"));
    list.add(new PgnFileTestCase("Szu1FAr2.pgn", "", "", "", 3, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "7R/pp4pk/2p3p1/8/3q4/8/6K1/8 b - - 7 46"));
    list.add(new PgnFileTestCase("tapdr97m.pgn", "", "", "", 14, 15, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "7k/6pP/6P1/5K2/8/8/8/8 w - - 1 67"));
    list.add(new PgnFileTestCase("tDwQdtb5.pgn", "", "", "", 18, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/8/5k2/7p/7K w - - 0 73"));
    list.add(new PgnFileTestCase("tEtllzqk.pgn", "", "", "", 17, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/7R/6pk/5p2/5bpK/8/r7/8 b - - 1 53"));
    list.add(new PgnFileTestCase("TjtEtQJ4.pgn", "", "", "", 5, 29, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "k7/2p5/p1Pp4/P2P1p2/5Pp1/3K2P1/8/8 w - - 29 66"));
    list.add(new PgnFileTestCase("tn1wmL3G.pgn", "", "", "", 23, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/1p4pk/p1b5/6Pp/7P/r7/7K w - - 0 46"));
    list.add(new PgnFileTestCase("TTb9iG4x.pgn", "", "", "", 15, 45, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/7k/5p1p/2p1pP1P/1pP1P3/1P1K4/8/8 w - - 45 61"));
    list.add(new PgnFileTestCase("TV4G8kFN.pgn", "", "", "", 20, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/6pk/6Pp/7K/3q3P/8/4p3/8 b - - 0 60"));
    list.add(new PgnFileTestCase("u2fQpBN9.pgn", "", "", "", 11, 13, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "3Q4/5R2/4p3/2p1P3/2N1k2p/8/6KP/8 b - - 13 46"));
    list.add(new PgnFileTestCase("U5uv4wPs.pgn", "", "", "", 10, 18, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/7R/6pk/4n1r1/8/7K/8 b - - 3 78"));
    list.add(new PgnFileTestCase("U8Rj7Y9F.pgn", "", "", "", 4, 18, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/4k3/6p1/1p2p1Pp/1P2P2P/8/8/4K3 b - - 18 63"));
    list.add(new PgnFileTestCase("uBSFb2kx.pgn", "", "", "", 16, 41, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/3k4/8/8/8/6BR/6PK/7r w - - 41 88"));
    list.add(new PgnFileTestCase("UDdCTvWG.pgn", "", "", "", 20, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "7k/5K1P/8/8/8/8/8/8 b - - 2 62"));
    list.add(new PgnFileTestCase("UdUE6AvZ.pgn", "", "", "", 16, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/Kq6/P7/8/6k1/8/8/8 w - - 1 50"));
    list.add(new PgnFileTestCase("UsN4NLQ4.pgn", "", "", "", 6, 13, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/6p1/5pkp/8/7P/3r2r1/7K w - - 0 45"));
    list.add(new PgnFileTestCase("UuNO3dGf.pgn", "", "", "", 5, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/1P6/8/1R6/P4k2/8/1r3K2 w - - 4 72"));
    list.add(new PgnFileTestCase("UVt2ZNXy.pgn", "", "", "", 21, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/6pk/8/6Pp/6nP/1r6/6K1 w - - 3 59"));
    list.add(new PgnFileTestCase("uwwUPCFw.pgn", "", "", "", 5, 12, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/7p/7k/6R1/4K3/8/8 b - - 0 58"));
    list.add(new PgnFileTestCase("ux9sCCZ8.pgn", "", "", "", 5, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "4rR1k/p5p1/6P1/2pp1K2/1p1b2P1/4q3/P1P5/8 b - - 3 34"));
    list.add(new PgnFileTestCase("V08kX4kz.pgn", "", "", "", 20, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/6b1/1p3k2/1Pp1p1p1/2P1PpP1/5P2/8/5K2 b - - 11 61"));
    list.add(new PgnFileTestCase("v4xJHVln.pgn", "", "", "", 3, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/8/7p/6p1/3K2pk/7Q b - - 3 59"));
    list.add(new PgnFileTestCase("vaTHx2Ow.pgn", "", "", "", 6, 22, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "7k/7P/5K2/8/8/8/8/8 b - - 0 74"));
    list.add(new PgnFileTestCase("VCzr8ukR.pgn", "", "", "", 29, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/3k4/p3p2p/Pp1pP2P/1P1P4/5K2/8 w - - 0 44"));
    list.add(new PgnFileTestCase("VdYuP1l4.pgn", "", "", "", 20, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/p7/8/8/1Q5K/k7 b - - 0 62"));
    list.add(new PgnFileTestCase("VeCbG5uw.pgn", "", "", "", 12, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/5kP1/8/5q2/5K2 w - - 0 59"));
    list.add(new PgnFileTestCase("VIdrelSz.pgn", "", "", "", 4, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "7r/2PR4/6pk/6q1/5P1K/r7/8/8 w - - 0 40"));
    list.add(new PgnFileTestCase("Vnqfiwd6.pgn", "", "", "", 5, 17, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "6kR/5pp1/1K2p1p1/3r4/8/8/8/8 b - - 3 48"));
    list.add(new PgnFileTestCase("VpZzyQaL.pgn", "", "", "", 20, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/3k4/3p4/2pP1p1p/p1P2P1P/P2K4/8/8 b - - 6 46"));
    list.add(new PgnFileTestCase("vS3VdvtP.pgn", "", "", "", 3, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3Q2k1/6pp/4K3/4p3/3q4/2b5/8/8 b - - 0 44"));
    list.add(new PgnFileTestCase("vWvNhvif.pgn", "", "", "", 6, 22, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/5k2/3p2p1/1p1Pp1P1/pP2Pp2/P4P2/8/2K5 b - - 16 65"));
    list.add(new PgnFileTestCase("vytUGbcM.pgn", "", "", "", 19, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "7K/6Pq/8/3k4/8/8/8/8 w - - 0 74"));
    list.add(new PgnFileTestCase("vZUevQUB.pgn", "", "", "", 3, 56, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/4k2p/1p1p2pP/1P1P2P1/8/2K5/8 b - - 56 73"));
    list.add(new PgnFileTestCase("W4wkO3cX.pgn", "", "", "", 11, 36, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "5k2/6p1/1p2p1P1/pP1pP2K/P2P4/8/8/8 b - - 26 81"));
    list.add(new PgnFileTestCase("W5huoA6x.pgn", "", "", "", 6, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "2Q5/pk6/8/2K5/8/8/8/8 b - - 1 69"));
    list.add(new PgnFileTestCase("w5N015UJ.pgn", "", "", "", 23, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "k7/P7/2K5/8/8/8/8/8 b - - 2 59"));
    list.add(new PgnFileTestCase("wCL9HARO.pgn", "", "", "", 6, 15, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "6K1/6q1/8/8/8/7k/7P/8 w - - 0 56"));
    list.add(new PgnFileTestCase("WdedP6nc.pgn", "", "", "", 6, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/6K1/8/1p6/k7/Q7 b - - 0 59"));
    list.add(new PgnFileTestCase("WeqThgc0.pgn", "", "", "", 14, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/p1k5/P1p5/2Pp3p/3P1p1P/5P1K/8/8 b - - 0 47"));
    list.add(new PgnFileTestCase("WEYtzOxf.pgn", "", "", "", 10, 43, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/5k2/1p3p1p/1Pp1pP1P/2P1P3/3K4/8/8 b - - 7 69"));
    list.add(new PgnFileTestCase("WIQLYQ5T.pgn", "", "", "", 12, 21, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/1p6/pPp1p2k/P1PpP1p1/3P2P1/8/8/6K1 b - - 1 58"));
    list.add(new PgnFileTestCase("wJymwD30.pgn", "", "", "", 7, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/8/7R/6pk/r6p/7K b - - 3 59"));
    list.add(new PgnFileTestCase("wOF3za6s.pgn", "", "", "", 21, 75, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/2k5/1p2p1p1/1P1pP1Pp/3P3P/8/3K4/8 w - - 75 90"));
    list.add(new PgnFileTestCase("wUCLya3K.pgn", "", "", "", 12, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/5p2/1K4p1/8/8/8/3Q4/3k4 b - - 0 67"));
    list.add(new PgnFileTestCase("wuHnMP2q.pgn", "", "", "", 4, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "7R/7p/p5p1/8/2p4P/5P1k/P2q3Q/5RK1 b - - 3 33"));
    list.add(new PgnFileTestCase("WxdB1E0N.pgn", "", "", "", 4, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/8/8/p1k5/K7 w - - 2 62"));
    list.add(new PgnFileTestCase("xD85FRxa.pgn", "", "", "", 9, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "6qK/8/4k3/6B1/8/8/5P2/8 w - - 0 55"));
    list.add(new PgnFileTestCase("xfvaW7PK.pgn", "", "", "", 12, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "6qK/5k1P/8/8/8/8/8/8 w - - 1 51"));
    list.add(new PgnFileTestCase("xgHJ9FM2.pgn", "", "", "", 4, 40, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "k7/8/6p1/1p1p1pP1/pP1PpP2/P3P3/3K4/8 w - - 40 62"));
    list.add(new PgnFileTestCase("XIfR5l9e.pgn", "", "", "", 31, 34, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/4b3/5k2/p1p1pBp1/PpP1P1Pp/1P3K1P/8 w - - 34 76"));
    list.add(new PgnFileTestCase("xmpqlKFo.pgn", "", "", "", 9, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/Kq6/8/8/8/8/P7/5k2 w - - 0 54"));
    list.add(new PgnFileTestCase("XooYe68z.pgn", "", "", "", 7, 26, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/6k1/6p1/4p1P1/p1p1P1K1/P1P5/2P5/8 b - - 26 54"));
    list.add(new PgnFileTestCase("Xp7OUyAx.pgn", "", "", "", 4, 19, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/3k4/1p2p1p1/1P2P1Pp/3K3P/8/8/8 w - - 19 55"));
    list.add(new PgnFileTestCase("xqzt1PdW.pgn", "", "", "", 6, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "7k/7P/5K2/8/8/8/8/8 b - - 0 58"));
    list.add(new PgnFileTestCase("XtJJQxwF.pgn", "", "", "", 10, 18, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/8/8/5K1k/6p1/7R b - - 0 63"));
    list.add(new PgnFileTestCase("XtvgUXHD.pgn", "", "", "", 16, 15, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/p6p/6Qk/6q1/1K6/8/8/8 b - - 5 72"));
    list.add(new PgnFileTestCase("Y6dvuR4W.pgn", "", "", "", 6, 9, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/1k4p1/1p2p1P1/1P2P3/2K5/8 w - - 9 48"));
    list.add(new PgnFileTestCase("yEtZYyMQ.pgn", "", "", "", 8, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/4k3/4p1p1/2p1P1Pp/1pP2K1P/1P6/8/8 w - - 3 42"));
    list.add(new PgnFileTestCase("YGj1C2WB.pgn", "", "", "", 13, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/3K4/8/5kQ1/4p3/8 b - - 5 69"));
    list.add(new PgnFileTestCase("YI1t9qvC.pgn", "", "", "", 10, 13, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "7k/5K1P/8/8/8/8/8/8 b - - 2 74"));
    list.add(new PgnFileTestCase("YictG6WZ.pgn", "", "", "", 10, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/6k1/8/5RPK/7r/8/8/8 w - - 6 51"));
    list.add(new PgnFileTestCase("yjfLthhQ.pgn", "", "", "", 6, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1K6/8/8/8/3p4/4kQ2/3p4/8 b - - 6 58"));
    list.add(new PgnFileTestCase("YoQvXlEO.pgn", "", "", "", 12, 14, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/6Q1/5Q1p/7k/3P4/1PP2RBK/7P/5q2 w - - 5 54"));
    list.add(new PgnFileTestCase("yQOHgRpo.pgn", "", "", "", 14, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "7k/6pp/7P/8/8/4q3/2r5/7K w - - 6 57"));
    list.add(new PgnFileTestCase("yrg4ColU.pgn", "", "", "", 9, 18, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/2k5/2p3p1/p1P1p1P1/P3P3/6K1/8 b - - 18 53"));
    list.add(new PgnFileTestCase("YRvWOIpy.pgn", "", "", "", 3, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/R7/Kr6/P6k/8/8/8/8 w - - 1 57"));
    list.add(new PgnFileTestCase("YUaykh8t.pgn", "", "", "", 12, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/8/1p4P1/1QK5/p4P2/k7 w - - 0 49"));
    list.add(new PgnFileTestCase("yVPekOlc.pgn", "", "", "", 3, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/8/8/5k1p/7K w - - 0 67"));
    list.add(new PgnFileTestCase("yxiNrimN.pgn", "", "", "", 3, 28, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "k7/8/p1p2p1p/P1P2P1P/4K3/8/8/8 w - - 28 69"));
    list.add(new PgnFileTestCase("yxUT9b2S.pgn", "", "", "", 12, 49, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/2k5/2p1p1p1/1pP1P1Pp/1P5P/7K/8/8 b - - 49 78"));
    list.add(new PgnFileTestCase("z3xkCyBY.pgn", "", "", "", 14, 11, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "7k/2Q4q/7K/2p3P1/2P5/8/8/8 w - - 6 59"));
    list.add(new PgnFileTestCase("Z5BCc8QN.pgn", "", "", "", 4, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/8/8/8/8/p1k5/K7 w - - 2 59"));
    list.add(new PgnFileTestCase("z9uuT4Ei.pgn", "", "", "", 6, 30, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/4k3/p1p2p2/P1P2P1p/2P2P1P/2K5/8/8 b - - 30 66"));
    list.add(new PgnFileTestCase("zDsWuY13.pgn", "", "", "", 16, 15, CheckmateOrStalemate.NA, 2,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/3k2p1/1p2p1Pp/1P2P2P/3K4/8/8 b - - 15 54"));
    list.add(new PgnFileTestCase("zIC5NqLC.pgn", "", "", "", 5, 13, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "k7/8/KP6/P7/8/1r6/8/8 w - - 1 58"));
    list.add(new PgnFileTestCase("zmelXKvA.pgn", "", "", "", 23, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/6p1/8/8/6p1/6rk/3K3R/8 b - - 10 58"));
    list.add(new PgnFileTestCase("ZNs9RrhS.pgn", "", "", "", 19, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "7k/5R2/6RK/7r/6PP/8/8/8 w - - 2 54"));
    list.add(new PgnFileTestCase("Zqc7ug8u.pgn", "", "", "", 11, 14, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "7R/6pk/8/6K1/2n1r3/8/8/8 b - - 9 61"));

    return new PgnFileTestCaseList(PgnTest.UNFAIR_LICHESS_ANALYSIS_GAMES, list);
  }

  private static PgnFileTestCaseList createTestCasesUnfairLichessAnalysisHelpmate() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("03VIxJUJ_helpmate.pgn", "", "", "", 6, 13, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/1p6/2p5/8/6k1/8/1r4K1 w - - 8 70"));
    list.add(new PgnFileTestCase("0dKUIfwq_helpmate.pgn", "", "", "", 6, 24, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/8/k1K5/8/8/Q7 b - - 24 89"));
    list.add(new PgnFileTestCase("0ehvDno9_helpmate.pgn", "", "", "", 6, 8, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "5Q2/P6k/8/8/4Q3/8/1B6/4R1K1 b - - 2 61"));
    list.add(new PgnFileTestCase("0u6eh8ZP_helpmate.pgn", "", "", "", 9, 10, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "1k1Q4/8/K7/8/8/8/8/8 b - - 10 61"));
    list.add(new PgnFileTestCase("0xMzcUDT_helpmate.pgn", "", "", "", 21, 18, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "k7/8/1K6/8/5Q2/8/8/Q3Q3 b - - 18 88"));
    list.add(new PgnFileTestCase("16myWOwv_helpmate.pgn", "", "", "", 15, 20, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "7q/8/8/8/8/8/8/5k1K w - - 5 78"));
    list.add(new PgnFileTestCase("1rcB0Tru_helpmate.pgn", "", "", "", 10, 6, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/4K1k1/2b2q2/1p6/8/8/8/2q5 w - - 3 52"));
    list.add(new PgnFileTestCase("29moHRC8_helpmate.pgn", "", "", "", 10, 6, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/6p1/8/8/8/7k/5K2/4q2q w - - 4 65"));
    list.add(new PgnFileTestCase("2gCvxApk_helpmate.pgn", "", "", "", 16, 21, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "Q7/8/1k6/Q7/8/8/8/5K2 b - - 4 69"));
    list.add(new PgnFileTestCase("2k5piUl6_helpmate.pgn", "", "", "", 14, 8, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "k1Q5/8/K7/8/8/8/8/8 b - - 0 72"));
    list.add(new PgnFileTestCase("2mgjVvcC_helpmate.pgn", "", "", "", 10, 5, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/2p4p/p6k/5pQ1/3b3K/6P1/PP5P/5q2 b - - 0 32"));
    list.add(new PgnFileTestCase("2pwjX9B1_helpmate.pgn", "", "", "", 5, 10, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/p7/6k1/8/5r1K/1q2q3 w - - 9 60"));
    list.add(new PgnFileTestCase("2sFHWmg3_helpmate.pgn", "", "", "", 3, 20, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/8/k1K5/8/8/Q7 b - - 20 70"));
    list.add(new PgnFileTestCase("2wVTK5vV_helpmate.pgn", "", "", "", 11, 18, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/k1K5/8/8/8/Q7 b - - 18 72"));
    list.add(new PgnFileTestCase("3bKyjTHM_helpmate.pgn", "", "", "", 18, 10, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/7q/8/7K/8/p7/k7/5qq1 w - - 2 65"));
    list.add(new PgnFileTestCase("3cYOGNS6_helpmate.pgn", "", "", "", 11, 8, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "Q4Q2/8/8/8/8/1k1K3Q/7P/1Q6 b - - 8 57"));
    list.add(new PgnFileTestCase("3jesiit8_helpmate.pgn", "", "", "", 11, 22, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "Q7/8/1k1Q4/8/2P2P2/6P1/4K3/8 b - - 2 69"));
    list.add(new PgnFileTestCase("3xFc21eB_helpmate.pgn", "", "", "", 15, 48, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "k1R5/8/K7/8/8/8/8/8 b - - 9 82"));
    list.add(new PgnFileTestCase("49QwULSR_helpmate.pgn", "", "", "", 10, 11, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/7k/8/3q3K w - - 3 76"));
    list.add(new PgnFileTestCase("55wBEu8Z_helpmate.pgn", "", "", "", 11, 9, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/p1r5/5R1p/Pp1p2Pk/3PpK2/4Pp1Q/5P2/8 b - - 0 43"));
    list.add(new PgnFileTestCase("6kfJPvsA_helpmate.pgn", "", "", "", 6, 12, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/7k/5K2/4q2q w - - 12 73"));
    list.add(new PgnFileTestCase("78XhRaIr_helpmate.pgn", "", "", "", 32, 26, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/8/2K5/k7/8/Q7 b - - 26 87"));
    list.add(new PgnFileTestCase("7alMRe6y_helpmate.pgn", "", "", "", 3, 7, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/2p5/r3qk2/8/8/7K/1b3q2 w - - 2 68"));
    list.add(new PgnFileTestCase("7fWsFh0J_helpmate.pgn", "", "", "", 6, 9, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/3q1K1k/7q/8 w - - 6 66"));
    list.add(new PgnFileTestCase("8cTl7SrQ_helpmate.pgn", "", "", "", 15, 32, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/k1b5/b7/K7 w - - 32 70"));
    list.add(new PgnFileTestCase("APEww4mD_helpmate.pgn", "", "", "", 5, 31, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/2r5/6K1/6q1/6k1 w - - 6 89"));
    list.add(new PgnFileTestCase("B8bpMZ71_helpmate.pgn", "", "", "", 11, 13, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "2Q5/8/8/6B1/3QkPK1/PP2P1P1/8/5R2 b - - 2 61"));
    list.add(new PgnFileTestCase("bfJbt6GP_helpmate.pgn", "", "", "", 7, 12, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/7p/8/6p1/8/2Kq3k/8/1q5n w - - 2 73"));
    list.add(new PgnFileTestCase("bQ0EXLal_helpmate.pgn", "", "", "", 10, 11, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "1b6/1p4pp/8/8/8/k7/8/K6r w - - 11 59"));
    list.add(new PgnFileTestCase("cZ8QxYP8_helpmate.pgn", "", "", "", 7, 12, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "7k/7Q/7K/8/2PP3P/4P1P1/8/2BQ1B1R b - - 10 52"));
    list.add(new PgnFileTestCase("D4vIOthl_helpmate.pgn", "", "", "", 6, 13, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/6k1/8/8/8/8/5K2/4q2q w - - 10 76"));
    list.add(new PgnFileTestCase("dBergEkt_helpmate.pgn", "", "", "", 17, 26, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/6k1/8/q5K1/1q6 w - - 26 74"));
    list.add(new PgnFileTestCase("DiKq9jcy_helpmate.pgn", "", "", "", 9, 14, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "4Q2Q/8/5k2/3Q4/1BK5/2P5/8/8 b - - 0 70"));
    list.add(new PgnFileTestCase("E9P918aj_helpmate.pgn", "", "", "", 18, 30, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "1k1Q4/8/K7/8/8/8/8/8 b - - 30 70"));
    list.add(new PgnFileTestCase("eExHpNDf_helpmate.pgn", "", "", "", 11, 34, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "6Q1/k7/8/8/Q7/3K4/8/1R6 b - - 8 99"));
    list.add(new PgnFileTestCase("eKwKl6Y9_helpmate.pgn", "", "", "", 17, 30, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "k7/1Q6/K7/8/8/8/8/8 b - - 30 88"));
    list.add(new PgnFileTestCase("EQCMW0jB_helpmate.pgn", "", "", "", 6, 8, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/r7/q4K1k/7q/8 w - - 8 66"));
    list.add(new PgnFileTestCase("EsFX1Urt_helpmate.pgn", "", "", "", 7, 14, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/7k/6q1/6K1 w - - 14 67"));
    list.add(new PgnFileTestCase("EstAIWqd_helpmate.pgn", "", "", "", 15, 13, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/5K1k/8/4rq2 w - - 10 76"));
    list.add(new PgnFileTestCase("Ew7uTqu0_helpmate.pgn", "", "", "", 13, 12, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/5pp1/8/8/p7/3b3k/8/r6K w - - 12 59"));
    list.add(new PgnFileTestCase("f7x3bSzh_helpmate.pgn", "", "", "", 12, 9, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "k6r/p4p1p/2B3p1/8/8/N3b1P1/P4p1P/1R5K b - - 0 34"));
    list.add(new PgnFileTestCase("FMDN8gJ7_helpmate.pgn", "", "", "", 5, 12, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/1k2b3/2p5/2K5 w - - 2 73"));
    list.add(new PgnFileTestCase("fN4vRqp0_helpmate.pgn", "", "", "", 5, 9, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/8/8/8/5KQk/5R2 b - - 2 74"));
    list.add(new PgnFileTestCase("fPnoYQtW_helpmate.pgn", "", "", "", 11, 9, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/7k/5K2/4q2q w - - 8 66"));
    list.add(new PgnFileTestCase("FYTeO80q_helpmate.pgn", "", "", "", 7, 9, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/6Qp/p5p1/7k/P4PB1/7K/1P3R1P/8 b - - 0 38"));
    list.add(new PgnFileTestCase("G0xDBYwE_helpmate.pgn", "", "", "", 6, 20, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/k1Q5/1N6/K7/8/8/8/8 b - - 20 72"));
    list.add(new PgnFileTestCase("G8i5Bn7F_helpmate.pgn", "", "", "", 11, 28, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/7k/8/q6K w - - 28 73"));
    list.add(new PgnFileTestCase("GCuwJ5bP_helpmate.pgn", "", "", "", 6, 18, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "1k1Q4/8/K7/8/8/8/8/8 b - - 18 64"));
    list.add(new PgnFileTestCase("GIwBNz5S_helpmate.pgn", "", "", "", 14, 12, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/7q/8/8/8/8/8/5k1K w - - 12 80"));
    list.add(new PgnFileTestCase("gtzPUYWo_helpmate.pgn", "", "", "", 13, 10, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/4kp2/4rp2/3q1K2 w - - 0 71"));
    list.add(new PgnFileTestCase("GzoCTk46_helpmate.pgn", "", "", "", 9, 9, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/7p/4qK1k/8/6q1 w - - 6 74"));
    list.add(new PgnFileTestCase("hdott1S4_helpmate.pgn", "", "", "", 25, 30, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "7N/5Q2/5k1K/8/8/8/7B/8 b - - 1 78"));
    list.add(new PgnFileTestCase("hkiG96je_helpmate.pgn", "", "", "", 10, 24, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/8/k1K5/8/8/Q7 b - - 24 73"));
    list.add(new PgnFileTestCase("IcjtzT9i_helpmate.pgn", "", "", "", 25, 28, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "k7/8/1K6/8/8/6Q1/8/Q7 b - - 28 100"));
    list.add(new PgnFileTestCase("if6AKvpb_helpmate.pgn", "", "", "", 5, 11, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "k3R3/8/K7/P7/1P3P2/8/8/8 b - - 2 60"));
    list.add(new PgnFileTestCase("ig7fudY7_helpmate.pgn", "", "", "", 8, 8, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/5K1k/8/4rq2 w - - 8 59"));
    list.add(new PgnFileTestCase("Iy1VEUMC_helpmate.pgn", "", "", "", 9, 8, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "4K3/8/Q7/8/8/k7/8/1Q6 b - - 6 67"));
    list.add(new PgnFileTestCase("j17qnxd0_helpmate.pgn", "", "", "", 16, 38, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/7k/8/q6K w - - 24 85"));
    list.add(new PgnFileTestCase("j4phmaNj_helpmate.pgn", "", "", "", 11, 20, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/8/k1K5/8/8/Q7 b - - 20 81"));
    list.add(new PgnFileTestCase("jehHS0Jj_helpmate.pgn", "", "", "", 10, 11, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/8/2K5/k7/8/Q7 b - - 10 75"));
    list.add(new PgnFileTestCase("JfHZLRvD_helpmate.pgn", "", "", "", 6, 8, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "r7/pp6/3p1p2/2p1k3/4q2P/5KP1/5P2/3R3R w - - 0 34"));
    list.add(new PgnFileTestCase("jK46bYGo_helpmate.pgn", "", "", "", 12, 11, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/6pk/8/q5K1 w - - 2 68"));
    list.add(new PgnFileTestCase("jnox9kxK_helpmate.pgn", "", "", "", 36, 12, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/7q/8/8/8/8/8/5k1K w - - 12 100"));
    list.add(new PgnFileTestCase("jnvJUlDw_helpmate.pgn", "", "", "", 11, 8, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/2p5/8/8/8/7k/4K3/3q2qn w - - 2 69"));
    list.add(new PgnFileTestCase("KbEG3yK3_helpmate.pgn", "", "", "", 20, 18, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/7k/b7/3q3K w - - 18 76"));
    list.add(new PgnFileTestCase("kbutR5IJ_helpmate.pgn", "", "", "", 8, 7, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "2k1q3/2p5/2K5/2N5/2q5/8/8/8 w - - 0 54"));
    list.add(new PgnFileTestCase("Kdf0f1OE_helpmate.pgn", "", "", "", 21, 16, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/7k/8/q6K w - - 16 80"));
    list.add(new PgnFileTestCase("KE47c49r_helpmate.pgn", "", "", "", 6, 13, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/6p1/6b1/8/8/7k/8/3q3K w - - 13 66"));
    list.add(new PgnFileTestCase("knQsF7R8_helpmate.pgn", "", "", "", 7, 12, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "k7/4K3/8/8/8/8/1Q6/Q7 b - - 12 60"));
    list.add(new PgnFileTestCase("KP23RUWa_helpmate.pgn", "", "", "", 11, 17, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "k7/8/8/8/2K5/Q7/8/1Q6 b - - 6 75"));
    list.add(new PgnFileTestCase("KPvcgsfS_helpmate.pgn", "", "", "", 11, 6, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "k5Q1/8/K7/8/8/8/P7/8 b - - 0 58"));
    list.add(new PgnFileTestCase("Ks0yR98N_helpmate.pgn", "", "", "", 4, 32, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "k7/8/1K6/8/8/6Q1/8/Q7 b - - 32 78"));
    list.add(new PgnFileTestCase("LC76ur18_helpmate.pgn", "", "", "", 3, 15, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/3b4/3Kq3/8/8/2qk4 w - - 2 75"));
    list.add(new PgnFileTestCase("lHtqM0fz_helpmate.pgn", "", "", "", 22, 19, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/7k/8/q6K w - - 12 82"));
    list.add(new PgnFileTestCase("lk42iihk_helpmate.pgn", "", "", "", 5, 29, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "k6Q/8/K7/8/8/8/R7/8 b - - 29 58"));
    list.add(new PgnFileTestCase("LqTNOgyT_helpmate.pgn", "", "", "", 23, 11, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "4Q2Q/5k2/8/8/8/3K4/8/3Q4 b - - 2 64"));
    list.add(new PgnFileTestCase("lYf34Uiq_helpmate.pgn", "", "", "", 14, 22, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/7k/6q1/6K1 w - - 22 82"));
    list.add(new PgnFileTestCase("m5WDCW16_helpmate.pgn", "", "", "", 17, 14, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "5Q2/2k4R/K7/8/6PP/8/8/7B b - - 14 58"));
    list.add(new PgnFileTestCase("McLow5Hz_helpmate.pgn", "", "", "", 5, 22, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "5Q1Q/2k3R1/8/1K6/8/8/B5P1/8 b - - 22 60"));
    list.add(new PgnFileTestCase("MvEif0NV_helpmate.pgn", "", "", "", 19, 27, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "k7/8/1K6/8/8/8/7Q/Q7 b - - 24 88"));
    list.add(new PgnFileTestCase("N6AYM22R_helpmate.pgn", "", "", "", 4, 16, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/6p1/7p/8/8/7k/q7/5q1K w - - 0 73"));
    list.add(new PgnFileTestCase("nvxFBquo_helpmate.pgn", "", "", "", 10, 10, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "2QQ4/5k1K/8/8/8/5R2/8/8 b - - 2 68"));
    list.add(new PgnFileTestCase("om0bCR5w_helpmate.pgn", "", "", "", 15, 8, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "q7/8/8/5k2/8/8/5K2/1r2q3 w - - 8 61"));
    list.add(new PgnFileTestCase("Omq6Fdhm_helpmate.pgn", "", "", "", 12, 9, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "7q/8/8/8/8/8/8/1q3k1K w - - 2 83"));
    list.add(new PgnFileTestCase("ooSZuhhE_helpmate.pgn", "", "", "", 5, 26, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/7k/8/q6K w - - 26 59"));
    list.add(new PgnFileTestCase("p4CeCK3J_helpmate.pgn", "", "", "", 6, 13, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "k7/1Q6/K7/8/8/8/8/6Q1 b - - 13 76"));
    list.add(new PgnFileTestCase("PAjZeTkN_helpmate.pgn", "", "", "", 6, 18, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "1Q6/k7/N7/8/R7/K7/8/8 b - - 0 69"));
    list.add(new PgnFileTestCase("pFsUTatm_helpmate.pgn", "", "", "", 22, 27, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "2QQ4/k4K2/8/8/4P3/8/8/Q7 b - - 2 66"));
    list.add(new PgnFileTestCase("PIL4PUtT_helpmate.pgn", "", "", "", 19, 10, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "Q1Q5/8/7Q/8/1PB4k/8/5P2/R4RK1 b - - 2 54"));
    list.add(new PgnFileTestCase("PkZ6qiA6_helpmate.pgn", "", "", "", 7, 8, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/3p4/4k3/8/6K1/8/6qq w - - 2 65"));
    list.add(new PgnFileTestCase("QirOKYTP_helpmate.pgn", "", "", "", 11, 10, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "r1b5/ppppn2p/5Qp1/8/1P2q3/N1Pk4/P2b1P1P/3RKR2 w - - 2 21"));
    list.add(new PgnFileTestCase("QnFE4Znl_helpmate.pgn", "", "", "", 3, 12, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "6QQ/2k1Q3/8/1K6/8/8/8/8 b - - 12 69"));
    list.add(new PgnFileTestCase("RF0MOp86_helpmate.pgn", "", "", "", 7, 13, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/1kp5/3p4/8/p7/r7/2rK4/1q6 w - - 2 66"));
    list.add(new PgnFileTestCase("RSqLhejA_helpmate.pgn", "", "", "", 12, 14, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/4p3/8/5Knk/2p1q3/2r5 w - - 14 73"));
    list.add(new PgnFileTestCase("RvN1zpCB_helpmate.pgn", "", "", "", 3, 3, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "6nr/6pp/4pp2/8/1PP5/P4P1k/7q/3B1RK1 w - - 0 26"));
    list.add(new PgnFileTestCase("rVN4T4Tz_helpmate.pgn", "", "", "", 7, 18, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "1k1Q4/8/K7/8/8/8/8/8 b - - 18 81"));
    list.add(new PgnFileTestCase("SDy9Y3D0_helpmate.pgn", "", "", "", 20, 11, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/7k/8/4q2K w - - 0 66"));
    list.add(new PgnFileTestCase("Sdz1yTzS_helpmate.pgn", "", "", "", 6, 26, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "1k1Q4/8/K7/8/8/8/8/8 b - - 26 80"));
    list.add(new PgnFileTestCase("sMv8Hh43_helpmate.pgn", "", "", "", 11, 8, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/6p1/5kP1/2r4K/5q2/8/8/8 w - - 2 53"));
    list.add(new PgnFileTestCase("sp0oSyY2_helpmate.pgn", "", "", "", 4, 8, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/1p6/8/8/7k/b7/q6K w - - 2 57"));
    list.add(new PgnFileTestCase("SwCSdv8K_helpmate.pgn", "", "", "", 17, 22, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/7k/6q1/6K1 w - - 22 75"));
    list.add(new PgnFileTestCase("SX3iSehH_helpmate.pgn", "", "", "", 16, 10, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "Q4K2/8/1k6/Q7/8/8/8/8 b - - 10 63"));
    list.add(new PgnFileTestCase("Szu1FAr2_helpmate.pgn", "", "", "", 3, 13, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/pp4p1/6p1/8/8/7k/8/q6K w - - 13 60"));
    list.add(new PgnFileTestCase("U5uv4wPs_helpmate.pgn", "", "", "", 10, 18, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/6p1/8/7k/8/r6K w - - 5 90"));
    list.add(new PgnFileTestCase("uBSFb2kx_helpmate.pgn", "", "", "", 16, 41, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "5B1k/8/8/8/8/7R/Q5K1/8 b - - 10 100"));
    list.add(new PgnFileTestCase("UdUE6AvZ_helpmate.pgn", "", "", "", 16, 16, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "1k1Q4/8/K7/8/8/8/8/8 b - - 16 59"));
    list.add(new PgnFileTestCase("UuNO3dGf_helpmate.pgn", "", "", "", 5, 12, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "kQ6/8/8/8/8/8/7Q/5K2 b - - 12 85"));
    list.add(new PgnFileTestCase("uwwUPCFw_helpmate.pgn", "", "", "", 5, 14, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/7k/6q1/6K1 w - - 14 71"));
    list.add(new PgnFileTestCase("ux9sCCZ8_helpmate.pgn", "", "", "", 5, 5, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "5r1k/p5p1/6P1/2pp1K2/1p1b2P1/4q3/P1P5/8 w - - 0 35"));
    list.add(new PgnFileTestCase("v4xJHVln_helpmate.pgn", "", "", "", 3, 15, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/7k/4Kn2/4q2q w - - 4 76"));
    list.add(new PgnFileTestCase("VdYuP1l4_helpmate.pgn", "", "", "", 20, 14, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "7q/8/8/8/8/8/8/5k1K w - - 14 74"));
    list.add(new PgnFileTestCase("VeCbG5uw_helpmate.pgn", "", "", "", 12, 26, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "1k1Q4/8/K7/8/8/8/8/8 b - - 26 77"));
    list.add(new PgnFileTestCase("VIdrelSz_helpmate.pgn", "", "", "", 4, 5, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "7r/2PR4/6pk/6P1/7K/r7/8/8 b - - 0 40"));
    list.add(new PgnFileTestCase("Vnqfiwd6_helpmate.pgn", "", "", "", 5, 17, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/6p1/5p2/8/6k1/8/q5K1/1q6 w - - 8 67"));
    list.add(new PgnFileTestCase("vS3VdvtP_helpmate.pgn", "", "", "", 3, 12, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/6k1/8/5K2/4q2q w - - 12 68"));
    list.add(new PgnFileTestCase("vytUGbcM_helpmate.pgn", "", "", "", 19, 22, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/8/2K5/k7/8/Q7 b - - 22 86"));
    list.add(new PgnFileTestCase("W5huoA6x_helpmate.pgn", "", "", "", 6, 26, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/7k/8/q6K w - - 26 89"));
    list.add(new PgnFileTestCase("wCL9HARO_helpmate.pgn", "", "", "", 6, 15, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "1k1Q4/8/K7/8/8/8/8/8 b - - 14 75"));
    list.add(new PgnFileTestCase("WdedP6nc_helpmate.pgn", "", "", "", 6, 22, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "7q/8/8/8/8/8/8/5k1K w - - 22 73"));
    list.add(new PgnFileTestCase("wUCLya3K_helpmate.pgn", "", "", "", 12, 7, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/4qK1k/8/6q1 w - - 6 86"));
    list.add(new PgnFileTestCase("wuHnMP2q_helpmate.pgn", "", "", "", 4, 7, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "7R/7p/p5p1/8/2p4P/5P1k/P6q/5RK1 w - - 0 34"));
    list.add(new PgnFileTestCase("xD85FRxa_helpmate.pgn", "", "", "", 9, 30, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "1k1Q4/8/K7/8/8/8/8/8 b - - 30 81"));
    list.add(new PgnFileTestCase("xfvaW7PK_helpmate.pgn", "", "", "", 12, 22, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/8/2K5/k7/8/Q7 b - - 22 62"));
    list.add(new PgnFileTestCase("xmpqlKFo_helpmate.pgn", "", "", "", 9, 18, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "1k1Q4/8/K7/8/8/8/8/8 b - - 18 69"));
    list.add(new PgnFileTestCase("XtJJQxwF_helpmate.pgn", "", "", "", 10, 18, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/7k/6q1/6K1 w - - 10 69"));
    list.add(new PgnFileTestCase("XtvgUXHD_helpmate.pgn", "", "", "", 16, 15, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/6k1/8/q7/1q5K w - - 14 93"));
    list.add(new PgnFileTestCase("YGj1C2WB_helpmate.pgn", "", "", "", 13, 16, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/7k/6q1/6K1 w - - 16 79"));
    list.add(new PgnFileTestCase("YictG6WZ_helpmate.pgn", "", "", "", 10, 37, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "k1K5/8/8/8/8/8/8/R7 b - - 37 73"));
    list.add(new PgnFileTestCase("yjfLthhQ_helpmate.pgn", "", "", "", 6, 12, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/8/8/8/8/5K1k/6q1/2q5 w - - 12 70"));
    list.add(new PgnFileTestCase("YRvWOIpy_helpmate.pgn", "", "", "", 3, 10, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/KQk5/8/8/8/8/3R4 b - - 10 64"));
    list.add(new PgnFileTestCase("z3xkCyBY_helpmate.pgn", "", "", "", 14, 11, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "7k/7Q/7K/2p3P1/2P5/8/8/8 b - - 0 59"));
    list.add(new PgnFileTestCase("zmelXKvA_helpmate.pgn", "", "", "", 23, 14, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/q7/8/8/8/8/K1k5/4b3 w - - 14 80"));

    return new PgnFileTestCaseList(PgnTest.UNFAIR_LICHESS_ANALYSIS_HELPMATE, list);
  }

  private static PgnFileTestCaseList createTestCasesUnfairHalfMoveDepthThree() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    // four half-moves depth!!!
    list.add(new PgnFileTestCase("mf4MFw9v.pgn", "", "", "", 23, 12, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/8/pp6/k1p5/1qQ5/K7 w - - 2 72"));

    list.add(new PgnFileTestCase("pUEeHLfu.pgn", "", "", "", 20, 18, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "7k/5Q2/6Q1/6P1/5P1p/6K1/7P/8 w - - 0 78"));
    list.add(new PgnFileTestCase("UNX9jAKK.pgn", "", "", "", 9, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/p4kp1/6Pp/7K/2rq4/7P/8/8 b - - 0 51"));
    list.add(new PgnFileTestCase("V7eJ1RR9.pgn", "", "", "", 14, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1k6/2P5/K7/2q5/8/8/8/8 b - - 0 56"));

    return new PgnFileTestCaseList(PgnTest.UNFAIR_HALF_MOVE_DEPTH_THREE, list);
  }

  private static PgnFileTestCaseList createTestCasesUnfairNotQuick() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(
        new PgnFileTestCase("f6c1lu7R.pgn", "", "", "", 13, 70, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
            UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
            UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/7p/5p1P/5p1K/5Pp1/6P1/1k6 w - - 70 83"));

    return new PgnFileTestCaseList(PgnTest.UNFAIR_NOT_QUICK, list);
  }

  private static PgnFileTestCaseList createTestCasesUnfairAmbronaExamples() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("ae_01.pgn", "", "", "", -1, 10, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "5brk/4p1p1/3pP1P1/1B1P2p1/3p2p1/3P4/4K1P1/8 w - - 10 100"));
    list.add(new PgnFileTestCase("ae_02.pgn", "", "", "", -1, 10, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/1p4p1/1Pp3p1/k1P3p1/1pP3Pb/1P4p1/6P1/7K w - - 10 100"));
    list.add(new PgnFileTestCase("ae_03.pgn", "", "", "", -1, 10, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/1k5B/7b/8/1p1p1p1p/1PpP1P1P/2P3K1/N3b3 b - - 10 100"));
    list.add(new PgnFileTestCase("ae_04.pgn", "", "", "", -1, 10, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "7b/1k5B/7b/8/1p1p1p1p/1PpP1P1P/2P3K1/N7 b - - 10 100"));
    list.add(new PgnFileTestCase("ae_04_proof_game.pgn", "", "", "", 4, 10, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "7b/1k5B/7b/8/1p1p1p1p/1PpP1P1P/2P1P1K1/N7 b - - 6 56"));
    list.add(new PgnFileTestCase("ae_05.pgn", "", "", "", -1, 10, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "4K3/8/8/8/8/p1p2p1p/P1pppp1P/bnrqkrnb b - - 10 100"));
    list.add(new PgnFileTestCase("ae_06.pgn", "", "", "", -1, 10, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "k1bK4/1p1p4/1PpPp3/2P1Pp2/2p1pP2/2p1P3/2P5/8 w - - 10 100"));
    list.add(new PgnFileTestCase("ae_07.pgn", "", "", "", -1, 10, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "Bb2kb2/bKp1p1p1/1pP1P1P1/pP6/6P1/P7/8/8 b - - 10 100"));
    list.add(new PgnFileTestCase("ae_08.pgn", "", "", "", -1, 10, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "Bb2kb2/bKp1p1p1/1pP1P1P1/1P6/p5P1/P7/8/8 b - - 10 100"));

    // cannot use as position illegal (too many white light-squared bishops)
    // nineth example from Ambrona, file name would become as below, but no such file as not used
    // ae_09.pgn

    list.add(new PgnFileTestCase("ae_10.pgn", "", "", "", -1, 10, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "7k/8/1p6/1Pp5/2Pp4/pB1Pp1p1/P1B1P1P1/3B2K1 b - - 10 100"));

    list.add(new PgnFileTestCase("ae_11_FKr42ZRT.pgn", "", "", "", 17, 16, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/8/7p/5p1P/5p1K/5Pp1/6P1/5kb1 b - - 13 63"));
    list.add(new PgnFileTestCase("ae_12_bKHPqNEw.pgn", "", "", "", 5, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "1k6/1P5p/BP3p2/1P6/8/8/5PKP/8 b - - 0 41"));
    list.add(new PgnFileTestCase("ae_13_OawUhnkq.pgn", "", "", "", 6, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "5r1k/6P1/7K/5q2/8/8/8/8 b - - 0 51"));
    list.add(new PgnFileTestCase("ae_14.pgn", "", "", "", -1, 10, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "k7/Q6r/2b5/1pBp1p1p/1P1P1P1P/KP6/1P6/8 b - - 10 100"));
    list.add(new PgnFileTestCase("ae_15_QRvIMh3z.pgn", "", "", "", 16, 46, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNDETERMINED, UnwinnableFull.UNDETERMINED,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "2b5/1p6/pPp3k1/2Pp3p/P2PpBpP/4P1P1/5K2/8 b - - 46 59"));
    list.add(new PgnFileTestCase("ae_16.pgn", "", "", "", -1, 10, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNDETERMINED, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1k6/p1p1p1p1/P1P1P1P1/p1p1p1p1/8/8/P1P1P1P1/4K3 w - - 10 100"));
    list.add(new PgnFileTestCase("ae_17.pgn", "", "", "", -1, 10, CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE,
        UnwinnableFull.WINNABLE, UnwinnableFull.UNDETERMINED, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "rnb1b3/pk1p4/p1pPp1p1/P1P1P1P1/RBP5/P7/5B2/7K w - - 10 100"));

    return new PgnFileTestCaseList(PgnTest.UNFAIR_AMBRONA_EXAMPLES, list);
  }

  private static PgnFileTestCaseList createTestCasesPawnWall() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    // pawn walls without bishops
    list.add(new PgnFileTestCase("pawn_wall_horizontal_1.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "4k3/8/8/p1p1p1p1/P1P1P1P1/8/8/4K3 w - - 0 50"));

    list.add(new PgnFileTestCase("pawn_wall_horizontal_2.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "4k3/8/8/p1p2p1p/P1P2P1P/8/8/4K3 w - - 0 50"));

    list.add(new PgnFileTestCase("pawn_wall_zig_zag_no_holes.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "4k3/5p1p/4pPpP/1p1pP1P1/pPpP4/P1P5/8/4K3 w - - 0 50"));

    list.add(new PgnFileTestCase("pawn_wall_zig_zag_holes_1.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "4k3/5p1p/4pP1P/3pP3/p1pP4/P1P5/8/4K3 w - - 0 50"));

    list.add(new PgnFileTestCase("pawn_wall_zig_zag_holes_2.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "4k3/5p1p/1p2pP1P/pPp1P3/P1P5/8/8/4K3 w - - 0 50"));

    // pawn walls with bishops
    list.add(new PgnFileTestCase("pawn_wall_bishop_1.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/6k1/p1p1p3/P1P1Pp1p/5P1P/2B5/6K1/8 b - - 0 50"));

    list.add(new PgnFileTestCase("pawn_wall_bishop_2.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "2b5/6k1/p1p1p3/P1P1Pp1p/5P1P/8/6K1/8 b - - 0 50"));

    list.add(new PgnFileTestCase("pawn_wall_bishop_3.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "2b5/6k1/p1p1p3/P1P1Pp1p/5P1P/2B5/6K1/8 b - - 0 50"));

    list.add(new PgnFileTestCase("pawn_wall_bishop_4.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/6k1/p1p1p3/P1P1Pp1p/5P1P/1bB5/6K1/8 b - - 0 50"));

    list.add(new PgnFileTestCase("pawn_wall_bishop_5.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "3B4/1b4k1/p1p1p3/P1P1Pp1p/5P1P/8/6K1/8 b - - 0 50"));

    // pawn walls but king on wrong side
    list.add(new PgnFileTestCase("pawn_wall_both_kings_on_white_side.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/8/8/p1p1p1p1/P1P1P1P1/8/1k6/4K3 w - - 0 50"));

    list.add(new PgnFileTestCase("pawn_wall_both_kings_on_black_side.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "3k2K1/1p6/pPp2p1p/P1P2P1P/8/8/8/8 w - - 0 50"));

    // en passant without bishops
    list.add(new PgnFileTestCase("pawn_wall_without_en_passant_capture_1.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "4k3/8/8/p1p1p1p1/PpPpPpPp/1P1P1P1P/8/4K3 b - - 0 50"));

    list.add(new PgnFileTestCase("pawn_wall_with_en_passant_capture_1.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4k3/8/8/p1p1p1p1/PpPpPpPp/1P1P1P1P/8/4K3 b - a3 0 50"));

    list.add(new PgnFileTestCase("pawn_wall_without_en_passant_capture_2.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "4k3/8/8/p1p1p1p1/PpPpPpPp/1P1P1P1P/8/4K3 b - - 0 50"));

    list.add(new PgnFileTestCase("pawn_wall_with_en_passant_capture_2.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4k3/8/8/p1p1p1p1/PpPpPpPp/1P1P1P1P/8/4K3 b - a3 0 50"));

    list.add(new PgnFileTestCase("pawn_wall_without_en_passant_capture_3.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "4k3/8/8/p1p1p1p1/P1PpPpPp/3P1P1P/8/4K3 b - - 0 50"));

    list.add(new PgnFileTestCase("pawn_wall_with_en_passant_capture_3.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4k3/8/8/p1p1p1p1/P1PpPpPp/3P1P1P/8/4K3 b - e3 0 50"));

    list.add(new PgnFileTestCase("pawn_wall_without_en_passant_capture_4.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE, "4k3/8/7p/p1p2p1P/P1P1pP2/4P3/8/4K3 b - - 0 50"));

    list.add(new PgnFileTestCase("pawn_wall_with_en_passant_capture_4.pgn", "", "", "", -1, 0, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4k3/8/7p/p1p2p1P/P1P1pP2/4P3/8/4K3 b - f3 0 50"));

    // en passant with bishops
    list.add(new PgnFileTestCase("pawn_wall_bishop_en_passant_capture_legal.pgn", "", "", "", -1, 0,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "4k3/8/8/p1p1p3/P1P1Pp1p/1B3P1P/8/4K3 b - e3 0 50"));

    // norgaard
    // dead position
    list.add(new PgnFileTestCase("norgaard_pawn_wall_example_1.pgn", "", "", "", 5, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "6k1/1p1p1p1p/1P1P1P1P/8/8/4K3/1P1P1P1P/8 w - - 0 39"));

    // TODO today c code quick sees unwinnable
    list.add(new PgnFileTestCase("norgaard_pawn_wall_example_2.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNDETERMINED, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1k6/p1p1p1p1/P1P1P1P1/p1p1p1p1/8/8/P1P1P1P1/4K3 w - - 0 34"));

    // beyond dead position
    list.add(new PgnFileTestCase("norgaard_beyond_pawn_wall_example_1.pgn", "", "", "638...Kg8 (1) 688.Ka3 (100)", 5,
        100, CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "7k/1p1p1p1p/1P1P1P1P/1P1P1P1P/8/K7/8/8 b - - 100 688"));

    list.add(new PgnFileTestCase("norgaard_beyond_pawn_wall_example_2.pgn", "", "", "833...Kb8 (1) 883.Kg2 (100)", 5,
        100, CheckmateOrStalemate.NA, 2, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "k7/p1p1p1p1/P1P1P1P1/P1P1P1P1/8/8/6K1/8 b - - 100 883"));

    return new PgnFileTestCaseList(PgnTest.PAWN_WALL, list);
  }

  private static PgnFileTestCaseList createTestCasesLastMoveAddedAccidentally() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_result_white_win_last_move_white_draws.pgn", "", "", "", 27, 21,
        CheckmateOrStalemate.NA, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "8/6Pk/2b5/2P1K3/8/8/8/8 b - - 2 87"));
    list.add(new PgnFileTestCase("02_result_draw_one_move_in_KvK.pgn", "", "", "", 22, 13, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BOTH, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/8/4K3/8/k7/8 b - - 1 69"));
    list.add(new PgnFileTestCase("03_result_draw_black_last_move_loses.pgn", "", "", "", 7, 18, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/4k1pP/6K1/8/8/8 w - - 2 69"));
    list.add(new PgnFileTestCase("04_result_draw_black_last_move_loses.pgn", "", "", "", 8, 13, CheckmateOrStalemate.NA,
        1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        "8/P1R5/7p/4kppP/6P1/8/1K6/r7 w - - 10 66"));

    return new PgnFileTestCaseList(PgnTest.LAST_MOVE_ADDED_ACCIDENTALLY, list);
  }

  private static PgnFileTestCaseList createTestCasesMaxSamePiecePromotionWhite() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_white_ten_rooks.pgn", "", "", "", 47, 48, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "1R6/1R6/2R1kbR1/8/R7/2NB1N2/3BKR2/3Q4 b - - 0 85"));
    list.add(new PgnFileTestCase("02_white_ten_knights.pgn", "", "", "", 47, 34, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/kN6/NNN2b1p/2N5/N1N4R/2N5/n1QB1KB1/1R6 b - - 0 75"));
    list.add(new PgnFileTestCase("03_white_ten_bishops_nine_light_squared.pgn", "", "", "", 47, 11,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "6Br/2k4p/4B3/3B2N1/5b2/1B1B1B2/4B1B1/2qK1B1R w - - 0 67"));
    list.add(new PgnFileTestCase("04_white_ten_bishops_nine_darks_squared.pgn", "", "", "", 47, 11,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "6Br/2k4p/4B3/3B2N1/5b2/1B1B1B2/4B1B1/2qK1B1R w - - 0 67"));
    list.add(new PgnFileTestCase("05_white_nine_queens.pgn", "", "", "", 47, 16, CheckmateOrStalemate.STALEMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.UNWINNABLE, "2Q2Q2/k7/bn5B/1Q6/3QQQ1K/Q7/8/5BNR b - - 4 72"));

    return new PgnFileTestCaseList(PgnTest.MAX_SAME_PIECE_PROMOTION_WHITE, list);
  }

  private static PgnFileTestCaseList createTestCasesMaxSamePiecePromotionBlack() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_black_ten_rooks.pgn", "", "", "", 56, 63, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "2rk3r/2rbQ1b1/6r1/1n3r2/KB3r1r/nN6/3r3r/8 b - - 0 118"));
    list.add(new PgnFileTestCase("02_black_ten_knights.pgn", "", "", "", 42, 42, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "r1bB1b1K/1BR1n1r1/Q3nnn1/1q3Nkn/1N1n2nn/nn6/8/2R5 w - - 42 130"));
    list.add(new PgnFileTestCase("03_black_ten_bishops_nine_light_squared.pgn", "", "", "", 56, 27,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "1nb2bn1/5b2/5k2/3b1b2/1Rb3b1/2Pb4/r3b1b1/2K4r w - - 2 102"));
    list.add(new PgnFileTestCase("04_black_ten_bishops_nine_dark_squared.pgn", "", "", "", 56, 32,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "4kb2/8/2n2n2/3b2b1/3b1b2/P1b1b2r/1b3bR1/7K w - - 0 96"));
    list.add(new PgnFileTestCase("05_black_nine_queens.pgn", "", "", "", 42, 35, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "r1b1qb1K/1n4r1/8/2n2k2/2Bq4/8/8/7q w - - 0 139"));

    return new PgnFileTestCaseList(PgnTest.MAX_SAME_PIECE_PROMOTION_BLACK, list);
  }

  private static PgnFileTestCaseList createTestCasesMaxSamePiecePromotionByCombined() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_twenty_rooks.pgn", "", "", "", 5, 28, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "8/5k2/1r6/8/8/3r4/2r5/1r3K2 w - - 1 114"));
    list.add(new PgnFileTestCase("02_twenty_knights.pgn", "", "", "", 7, 34, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.WINNABLE, "7k/8/3n4/4n3/3n4/nn1n4/8/1KnN4 w - - 6 126"));
    list.add(new PgnFileTestCase("03_twenty_bishops.pgn", "", "", "", 7, 22, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "2B1B3/3B2kR/4BrB1/2B5/B7/6K1/2B3B1/8 b - - 4 98"));
    list.add(new PgnFileTestCase("04_eighteen_queens.pgn", "", "", "", 7, 35, CheckmateOrStalemate.CHECKMATE, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE,
        UnwinnableQuick.UNWINNABLE, "8/8/8/8/8/8/5KQk/8 b - - 14 110"));

    return new PgnFileTestCaseList(PgnTest.MAX_SAME_PIECE_PROMOTION_COMBINED, list);
  }

  private static PgnFileTestCaseList createTestCasesBizarreCheckmate() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_white_checkmates_double_check_one_empty_square.pgn", "", "", "", -1, 1,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/2B5/4NN2/2bNkN2/2NNNN2/8/8/4K3 b - - 1 100"));
    list.add(new PgnFileTestCase("02_white_checkmates_double_check_all_empty_squares.pgn", "", "", "", -1, 1,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.WINNABLE,
        UnwinnableFull.UNWINNABLE, UnwinnableQuick.WINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/2B2N2/7R/2b1k3/1R6/8/3R1R2/4K3 b - - 1 100"));
    list.add(new PgnFileTestCase("03_black_checkmates_double_check_one_empty_square.pgn", "", "", "", -1, 1,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "1k6/3b4/nn2B3/nKn5/nnnn4/8/8/8 w - - 1 101"));
    list.add(new PgnFileTestCase("04_black_checkmates_double_check_all_empty_squares.pgn", "", "", "", -1, 1,
        CheckmateOrStalemate.CHECKMATE, 1, InsufficientMaterial.NONE, UnwinnableFull.UNWINNABLE,
        UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.WINNABLE,
        "2rkr3/r7/3K4/r7/2n5/4B3/7b/8 w - - 1 101"));

    return new PgnFileTestCaseList(PgnTest.BIZARRE_CHECKMATE, list);
  }

  private static PgnFileTestCaseList createTestCasesMonsterBlogInsufficientMaterial() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_K_K.pgn", "", "", "", 5, 4, CheckmateOrStalemate.NA, 1, InsufficientMaterial.BOTH,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/8/6k1/8/5K2/8/8/8 b - - 0 26"));
    list.add(new PgnFileTestCase("02_KN_K.pgn", "", "", "", 5, 4, CheckmateOrStalemate.NA, 1, InsufficientMaterial.BOTH,
        UnwinnableFull.UNWINNABLE, UnwinnableFull.UNWINNABLE, UnwinnableQuick.UNWINNABLE, UnwinnableQuick.UNWINNABLE,
        "8/5k2/8/8/8/8/8/1NK5 b - - 0 26"));

    return new PgnFileTestCaseList(PgnTest.MONSTER_BLOG_INSUFFICIENT_MATERIAL, list);
  }

  private static PgnFileTestCaseList createTestCasesMonsterBlogInsufficientMaterialPredraw() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_KBw_KBb.pgn", "", "", "", 17, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/3B1k2/7b/8/8/8/5K2/8 b - - 0 29"));
    list.add(new PgnFileTestCase("02_KN_KB.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "2b5/3k4/8/6K1/8/8/3N4/8 b - - 0 27"));
    list.add(new PgnFileTestCase("03_KB_KN.pgn", "", "", "", 7, 8, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/6k1/7n/8/8/7B/5K2/8 b - - 0 30"));
    list.add(new PgnFileTestCase("04_KNN_K.pgn", "", "", "", 8, 13, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.BLACK_ONLY, UnwinnableFull.WINNABLE, UnwinnableFull.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableQuick.UNWINNABLE, "8/8/7k/8/4N3/3N4/6K1/8 b - - 0 37"));
    list.add(new PgnFileTestCase("05_KN_KN.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1n6/8/1k6/8/3K4/8/8/1N6 b - - 0 24"));

    return new PgnFileTestCaseList(PgnTest.MONSTER_BLOG_INSUFFICIENT_MATERIAL_PREDRAW, list);
  }

  private static PgnFileTestCaseList createTestCasesMonsterBlogInsufficientMaterialTimeout() {
    final List<PgnFileTestCase> list = new ArrayList<>();

    list.add(new PgnFileTestCase("01_K_more.pgn", "", "", "", 5, 3, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "1nbk1b2/1qpp1p2/8/8/3K4/8/8/8 b - - 3 20"));
    list.add(new PgnFileTestCase("02_KN_KQ.pgn", "", "", "", 5, 2, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/1q6/3k4/8/2N5/8/5K2 b - - 1 26"));
    list.add(new PgnFileTestCase("03_KN_KNP_forced_mate.pgn", "", "", "", 5, 14, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3N2nk/4p3/5pKp/6p1/8/8/8/8 b - - 14 36"));
    list.add(new PgnFileTestCase("04_KN_KP.pgn", "", "", "", 23, 9, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "2k5/8/p7/8/8/2N1K3/8/8 b - - 2 37"));
    list.add(new PgnFileTestCase("05_KN_KR.pgn", "", "", "", 10, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/2k4N/8/8/8/3r4/4K3/8 b - - 1 29"));
    list.add(new PgnFileTestCase("06_KB_KR.pgn", "", "", "", 20, 7, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "7r/2k5/8/8/5B2/8/4K3/8 b - - 1 37"));
    list.add(new PgnFileTestCase("07_KBw_KRBw.pgn", "", "", "", 17, 15, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.WHITE_ONLY, UnwinnableFull.UNWINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.UNWINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/2r2k2/3b4/8/8/2BK4/8 b - - 15 40"));
    list.add(new PgnFileTestCase("08_KB_KNP_forced_mate.pgn", "", "", "", 22, 27, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "knB5/3p4/pKp5/p7/1p6/8/8/8 b - - 12 60"));
    list.add(new PgnFileTestCase("09_KB_KP.pgn", "", "", "", 5, 6, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/5p2/8/4k3/1K6/8/8/5B2 b - - 0 28"));
    list.add(new PgnFileTestCase("10_KBb_KRBw.pgn", "", "", "", 9, 4, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "4r3/3b4/4k3/8/5K2/8/3B4/8 b - - 4 37"));
    list.add(new PgnFileTestCase("11_KNN_KP_forced_mate.pgn", "", "", "", 10, 15, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "7k/8/5N1K/8/5N1p/8/8/8 b - - 1 49"));
    list.add(new PgnFileTestCase("12_KNN_KP.pgn", "", "", "", 15, 5, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "8/8/8/2kp4/8/8/2NKN3/8 b - - 5 41"));
    list.add(new PgnFileTestCase("13_KNN_KB.pgn", "", "", "", 25, 17, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "k7/1bKN4/8/1N6/8/8/8/8 b - - 17 51"));
    list.add(new PgnFileTestCase("14_KNN_KQQQQQQQQQ.pgn", "", "", "", 13, 26, CheckmateOrStalemate.NA, 1,
        InsufficientMaterial.NONE, UnwinnableFull.WINNABLE, UnwinnableFull.WINNABLE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuick.POSSIBLY_WINNABLE, "3q4/3N3K/8/5N2/8/7k/8/qqqqqqqq b - - 1 88"));

    return new PgnFileTestCaseList(PgnTest.MONSTER_BLOG_INSUFFICIENT_MATERIAL_TIMEOUT, list);
  }
}
