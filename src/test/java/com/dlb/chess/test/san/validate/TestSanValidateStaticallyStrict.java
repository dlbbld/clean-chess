package com.dlb.chess.test.san.validate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.CastlingConstants;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.san.SanValidation;
import com.dlb.chess.san.enums.SanLetter;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.san.validate.statically.strict.calculate.SanValidateStaticallyStrict;

class TestSanValidateStaticallyStrict implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void testInvalidFormat() {
    checkNotExistsBoth("");
    checkNotExistsBoth("x");
    checkNotExistsBoth("x+");
    checkNotExistsBoth("R");
    checkNotExistsBoth("Ra");
    checkNotExistsBoth("Rxa");
    checkNotExistsBoth("axb");
    checkNotExistsBoth("Q1");
    checkNotExistsBoth("Q22");
    checkNotExistsBoth("Qx2");
  }

  @SuppressWarnings("static-method")
  @Test
  void testCastling() {
    checkExistsBoth("O-O");
    checkExistsBoth("O-O+");
    checkExistsBoth("O-O#");

    checkExistsBoth("O-O-O");
    checkExistsBoth("O-O-O+");
    checkExistsBoth("O-O-O#");

    checkNotExistsBoth("O-Ox");
    checkNotExistsBoth("O-O-Ox");
  }

  @SuppressWarnings("static-method")
  @Test
  void testStandard() {
    // rook
    checkExistsBoth("Rb2");
    checkExistsBoth("Rab2");
    checkExistsBoth("R5b2");
    checkExistsBoth("Rxb2");
    checkExistsBoth("Raxb2");
    checkExistsBoth("R5xb2");
    checkExistsBoth("Rb2+");
    checkExistsBoth("Rab2+");
    checkExistsBoth("R5b2+");
    checkExistsBoth("Rb2#");
    checkExistsBoth("Rab2#");
    checkExistsBoth("R5b2#");
    checkExistsBoth("Rxb2+");
    checkExistsBoth("Raxb2+");
    checkExistsBoth("R5xb2+");
    checkExistsBoth("Rxb2#");
    checkExistsBoth("Raxb2#");
    checkExistsBoth("R5xb2#");

    // knight
    checkExistsBoth("Nc3");
    checkExistsBoth("Nac3");
    checkExistsBoth("N4c3");
    checkExistsBoth("Na2c3");
    checkExistsBoth("Nxc3");
    checkExistsBoth("Naxc3");
    checkExistsBoth("N4xc3");
    checkExistsBoth("Na2xc3");
    checkExistsBoth("Nc3+");
    checkExistsBoth("Nac3+");
    checkExistsBoth("N4c3+");
    checkExistsBoth("Na2c3+");
    checkExistsBoth("Nc3#");
    checkExistsBoth("Nac3#");
    checkExistsBoth("N4c3#");
    checkExistsBoth("Na2c3#");
    checkExistsBoth("Nxc3+");
    checkExistsBoth("Naxc3+");
    checkExistsBoth("N4xc3+");
    checkExistsBoth("Na2xc3+");
    checkExistsBoth("Nxc3#");
    checkExistsBoth("Naxc3#");
    checkExistsBoth("N4xc3#");
    checkExistsBoth("Na2xc3#");

    // bishop
    checkExistsBoth("Bd4");
    checkExistsBoth("Bcd4");
    checkExistsBoth("B5d4");
    checkExistsBoth("Be5d4");
    checkExistsBoth("Bxd4");
    checkExistsBoth("Bcxd4");
    checkExistsBoth("B5xd4");
    checkExistsBoth("Be5xd4");
    checkExistsBoth("Bd4+");
    checkExistsBoth("Bcd4+");
    checkExistsBoth("B5d4+");
    checkExistsBoth("Be5d4+");
    checkExistsBoth("Bd4#");
    checkExistsBoth("Bcd4#");
    checkExistsBoth("B5d4#");
    checkExistsBoth("Be5d4#");
    checkExistsBoth("Bxd4+");
    checkExistsBoth("Bcxd4+");
    checkExistsBoth("B5xd4+");
    checkExistsBoth("Be5xd4+");
    checkExistsBoth("Bxd4#");
    checkExistsBoth("Bcxd4#");
    checkExistsBoth("B5xd4#");
    checkExistsBoth("Be5xd4#");

    // queen
    checkExistsBoth("Qf7");
    checkExistsBoth("Qaf7");
    checkExistsBoth("Q8f7");
    checkExistsBoth("Qg8f7");
    checkExistsBoth("Qxf7");
    checkExistsBoth("Qaxf7");
    checkExistsBoth("Q8xf7");
    checkExistsBoth("Qg8xf7");
    checkExistsBoth("Qf7+");
    checkExistsBoth("Qaf7+");
    checkExistsBoth("Q8f7+");
    checkExistsBoth("Qg8f7+");
    checkExistsBoth("Qf7#");
    checkExistsBoth("Qaf7#");
    checkExistsBoth("Q8f7#");
    checkExistsBoth("Qg8f7#");
    checkExistsBoth("Qxf7+");
    checkExistsBoth("Qaxf7+");
    checkExistsBoth("Q8xf7+");
    checkExistsBoth("Qg8xf7+");
    checkExistsBoth("Qxf7#");
    checkExistsBoth("Qaxf7#");
    checkExistsBoth("Q8xf7#");
    checkExistsBoth("Qg8xf7#");

    // king
    checkExistsBoth("Kd7");
    checkExistsBoth("Kxd7");
    checkExistsBoth("Kd7+");
    checkExistsBoth("Kd7#");
    checkExistsBoth("Kxd7+");
    checkExistsBoth("Kxd7#");

    // white pawn
    checkExistsWhite("e4");
    checkExistsWhite("dxe4");
    checkExistsWhite("fxe4");
    checkExistsWhite("e8=Q");
    checkExistsWhite("e8=N");
    checkExistsWhite("dxe8=Q");
    checkExistsWhite("fxe8=N");

    checkExistsWhite("e4+");
    checkExistsWhite("dxe4+");
    checkExistsWhite("fxe4+");
    checkExistsWhite("e8=Q+");
    checkExistsWhite("e8=N+");
    checkExistsWhite("dxe8=Q+");
    checkExistsWhite("fxe8=N+");

    checkExistsWhite("e4#");
    checkExistsWhite("dxe4#");
    checkExistsWhite("fxe4#");
    checkExistsWhite("e8=Q#");
    checkExistsWhite("e8=N#");
    checkExistsWhite("dxe8=Q#");
    checkExistsWhite("fxe8=N#");

    checkNotExistsWhite("f1=Q");
    checkNotExistsWhite("f1=B");
    checkNotExistsWhite("exf1=Q");
    checkNotExistsWhite("gxf1=B");

    checkNotExistsWhite("f1=Q+");
    checkNotExistsWhite("f1=B+");
    checkNotExistsWhite("exf1=Q+");
    checkNotExistsWhite("gxf1=B+");

    checkNotExistsWhite("f1=Q#");
    checkNotExistsWhite("f1=B#");
    checkNotExistsWhite("exf1=Q#");
    checkNotExistsWhite("gxf1=B#");

    // black pawn
    checkExistsBlack("f5");
    checkExistsBlack("exf5");
    checkExistsBlack("gxf5");
    checkExistsBlack("f1=Q");
    checkExistsBlack("f1=B");
    checkExistsBlack("exf1=Q");
    checkExistsBlack("gxf1=B");

    checkExistsBlack("f5+");
    checkExistsBlack("exf5+");
    checkExistsBlack("gxf5+");
    checkExistsBlack("f1=Q+");
    checkExistsBlack("f1=B+");
    checkExistsBlack("exf1=Q+");
    checkExistsBlack("gxf1=B+");

    checkExistsBlack("f5#");
    checkExistsBlack("exf5#");
    checkExistsBlack("gxf5#");
    checkExistsBlack("f1=Q#");
    checkExistsBlack("f1=B#");
    checkExistsBlack("exf1=Q#");
    checkExistsBlack("gxf1=B#");

    checkNotExistsBlack("e8=Q");
    checkNotExistsBlack("e8=N");
    checkNotExistsBlack("dxe8=Q");
    checkNotExistsBlack("fxe8=N");

    checkNotExistsBlack("e8=Q+");
    checkNotExistsBlack("e8=N+");
    checkNotExistsBlack("dxe8=Q+");
    checkNotExistsBlack("fxe8=N+");

    checkNotExistsBlack("e8=Q#");
    checkNotExistsBlack("e8=N#");
    checkNotExistsBlack("dxe8=Q#");
    checkNotExistsBlack("fxe8=N#");

  }

  @SuppressWarnings("static-method")
  @Test
  void testInvalidMovement() {
    // rook
    checkNotExistsBoth("R2a1");
    checkNotExistsBoth("Ra2a1");
    checkNotExistsBoth("Rc2b2");

    // knight
    checkNotExistsBoth("Nb3a1");
    checkNotExistsBoth("Naa1");
    checkNotExistsBoth("N4a1");

    // bishop
    checkNotExistsBoth("Baa1");
    checkNotExistsBoth("B2a1");
    checkNotExistsBoth("Bb3a1");

    // queen
    checkNotExistsBoth("Qb4a1");

    // king
    // all valid

    // white pawn
    checkNotExistsWhite("a1=Q");

    // black pawn
    checkNotExistsBlack("a8=Q");
  }

  private static void checkExistsBoth(String san) {
    assertTrue(SanValidateStaticallyStrict.exists(san, WHITE));
    assertTrue(SanValidateStaticallyStrict.exists(san, BLACK));
  }

  private static void checkExistsWhite(String san) {
    assertTrue(SanValidateStaticallyStrict.exists(san, WHITE));
  }

  private static void checkExistsBlack(String san) {
    assertTrue(SanValidateStaticallyStrict.exists(san, BLACK));
  }

  private static void checkNotExistsBoth(String san) {
    assertFalse(SanValidateStaticallyStrict.exists(san, WHITE));
    assertFalse(SanValidateStaticallyStrict.exists(san, BLACK));
  }

  private static void checkNotExistsWhite(String san) {
    assertFalse(SanValidateStaticallyStrict.exists(san, WHITE));
  }

  private static void checkNotExistsBlack(String san) {
    assertFalse(SanValidateStaticallyStrict.exists(san, BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void testStaticallyAgainstRuntime() {
    for (final Entry<String, SanParse> entry : SanValidateStaticallyStrict.getSanValidationWhiteMap().entrySet()) {
      checkStaticallyAgainstRuntime(entry.getKey(), entry.getValue(), WHITE);
    }
    for (final Entry<String, SanParse> entry : SanValidateStaticallyStrict.getSanValidationBlackMap().entrySet()) {
      checkStaticallyAgainstRuntime(entry.getKey(), entry.getValue(), BLACK);
    }
  }

  @SuppressWarnings("static-method")
  // TODO today think disabled test @Test
  void testAgainstUsingGenerated() {
    // pieces - none
    for (final PieceType pieceType : PieceType.values()) {
      if (pieceType == PieceType.NONE) {
        continue;
      }
      for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
        final var sanBaseConstruct = new StringBuilder();
        sanBaseConstruct.append(pieceType.getLetter());
        sanBaseConstruct.append(toSquare.getName());
        final var sanBase = NonNullWrapperCommon.toString(sanBaseConstruct);
        checkAgainstUsingGenerated(sanBase, 1);
      }
    }

    // pieces - file
    for (final PieceType pieceType : PieceType.values()) {
      if (pieceType == PieceType.NONE) {
        continue;
      }
      for (final File fromFile : File.values()) {
        if (fromFile == File.NONE) {
          continue;
        }
        for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
          final var sanBaseConstruct = new StringBuilder();
          sanBaseConstruct.append(pieceType.getLetter());
          sanBaseConstruct.append(fromFile.getLetter());
          sanBaseConstruct.append(toSquare.getName());
          final var sanBase = NonNullWrapperCommon.toString(sanBaseConstruct);
          checkAgainstUsingGenerated(sanBase, 2);
        }
      }
    }

    // pieces - rank
    for (final PieceType pieceType : PieceType.values()) {
      if (pieceType == PieceType.NONE) {
        continue;
      }
      for (final Rank fromRank : Rank.values()) {
        if (fromRank == Rank.NONE) {
          continue;
        }
        for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
          final var sanBaseConstruct = new StringBuilder();
          sanBaseConstruct.append(pieceType.getLetter());
          sanBaseConstruct.append(fromRank.getNumber());
          sanBaseConstruct.append(toSquare.getName());
          final var sanBase = NonNullWrapperCommon.toString(sanBaseConstruct);
          checkAgainstUsingGenerated(sanBase, 2);
        }
      }
    }

    // pieces - square
    for (final PieceType pieceType : PieceType.values()) {
      if (pieceType == PieceType.NONE) {
        continue;
      }
      for (final Square fromSquare : Square.BOARD_SQUARE_LIST) {
        for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
          final var sanBaseConstruct = new StringBuilder();
          sanBaseConstruct.append(pieceType.getLetter());
          sanBaseConstruct.append(fromSquare.getName());
          sanBaseConstruct.append(toSquare.getName());
          final var sanBase = NonNullWrapperCommon.toString(sanBaseConstruct);
          checkAgainstUsingGenerated(sanBase, 3);
        }
      }
    }

    // pawns
    // non capturing - non promotion
    for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
      final var sanBase = toSquare.getName();
      checkAgainstUsingGenerated(sanBase);
    }

    // capturing - non promotion
    for (final File fromFile : File.values()) {
      if (fromFile == File.NONE) {
        continue;
      }
      for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
        final var sanBaseConstruct = new StringBuilder();
        sanBaseConstruct.append(fromFile.getLetter());
        sanBaseConstruct.append(SanLetter.CAPTURE.getLetter());
        sanBaseConstruct.append(toSquare.getName());
        final var sanBase = NonNullWrapperCommon.toString(sanBaseConstruct);
        checkAgainstUsingGenerated(sanBase);
      }
    }

    // non capturing - promotion
    for (final PromotionPieceType promotionPieceType : PromotionPieceType.values()) {
      if (promotionPieceType == PromotionPieceType.NONE) {
        continue;
      }
      for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
        final var sanBaseConstruct = new StringBuilder();
        sanBaseConstruct.append(toSquare.getName());
        sanBaseConstruct.append(SanLetter.PROMOTION.getLetter());
        sanBaseConstruct.append(promotionPieceType.getPieceType().getLetter());
        final var sanBase = NonNullWrapperCommon.toString(sanBaseConstruct);
        checkAgainstUsingGenerated(sanBase);
      }
    }

    // capturing - promotion
    for (final PromotionPieceType promotionPieceType : PromotionPieceType.values()) {
      if (promotionPieceType == PromotionPieceType.NONE) {
        continue;
      }
      for (final File fromFile : File.values()) {
        if (fromFile == File.NONE) {
          continue;
        }
        for (final Square toSquare : Square.BOARD_SQUARE_LIST) {
          final var sanBaseConstruct = new StringBuilder();
          sanBaseConstruct.append(fromFile.getLetter());
          sanBaseConstruct.append(SanLetter.CAPTURE.getLetter());
          sanBaseConstruct.append(toSquare.getName());
          sanBaseConstruct.append(SanLetter.PROMOTION.getLetter());
          sanBaseConstruct.append(promotionPieceType.getPieceType().getLetter());
          final var sanBase = NonNullWrapperCommon.toString(sanBaseConstruct);
          checkAgainstUsingGenerated(sanBase);
        }
      }
    }

    // castling
    checkAgainstUsingGenerated(CastlingConstants.SAN_CASTLING_KING_SIDE);
    checkAgainstUsingGenerated(CastlingConstants.SAN_CASTLING_QUEEN_SIDE);
  }

  private static void checkAgainstUsingGenerated(String sanBase, int indexToInsertCaptureSymbol) {
    System.out.println(sanBase);
    for (final String sanVariation : calculateSanVariationList(sanBase, indexToInsertCaptureSymbol)) {
      checkAgainstUsingGenerated(sanVariation, WHITE);
      checkAgainstUsingGenerated(sanVariation, BLACK);
    }
  }

  private static void checkAgainstUsingGenerated(String sanBase) {
    System.out.println(sanBase);
    for (final String sanVariation : calculateSanVariationList(sanBase)) {
      checkAgainstUsingGenerated(sanVariation, WHITE);
      checkAgainstUsingGenerated(sanVariation, BLACK);
    }
  }

  private static void checkAgainstUsingGenerated(String sanVariation, Side side) {

    final var isPassStatically = SanValidateStaticallyStrict.exists(sanVariation, side);

    try {
      final SanParse runTimeResult = SanValidation.validateNonPositionRelatedExtended(sanVariation, side);
      assertTrue(isPassStatically);
      final SanParse staticResult = SanValidateStaticallyStrict.getChecked(sanVariation, side);
      assertEquals(runTimeResult, staticResult);

    } catch (@SuppressWarnings("unused") final SanValidationException e) {
      assertFalse(isPassStatically);
    }

  }

  private static List<String> calculateSanVariationList(String sanBase, int indexToInsertCaptureSymbol) {
    final List<String> result = new ArrayList<>();

    // e.g. Nc3
    result.add(sanBase);
    result.add(sanBase + SanLetter.CHECK.getLetter());
    result.add(sanBase + SanLetter.CHECKMATE.getLetter());

    final StringBuilder sanBaseCaptureConstruct = new StringBuilder(sanBase);
    sanBaseCaptureConstruct.insert(indexToInsertCaptureSymbol, SanLetter.CAPTURE.getLetter());
    final String sanBaseCapture = NonNullWrapperCommon.toString(sanBaseCaptureConstruct);

    result.add(sanBaseCapture);
    result.add(sanBaseCapture + SanLetter.CHECK.getLetter());
    result.add(sanBaseCapture + SanLetter.CHECKMATE.getLetter());

    return result;
  }

  private static List<String> calculateSanVariationList(String sanBase) {
    final List<String> result = new ArrayList<>();

    // e.g. Nc3
    result.add(sanBase);
    result.add(sanBase + SanLetter.CHECK.getLetter());
    result.add(sanBase + SanLetter.CHECKMATE.getLetter());

    return result;
  }

  private static void checkStaticallyAgainstRuntime(String san, SanParse staticResult, Side side) {

    boolean isRuntimeException;
    try {
      final SanParse runTimeResult = SanValidation.validateNonPositionRelatedExtended(san, side);
      assertEquals(runTimeResult, staticResult);
      isRuntimeException = false;
    } catch (@SuppressWarnings("unused") final SanValidationException e) {
      isRuntimeException = true;
    }
    assertFalse(isRuntimeException);

  }

}
