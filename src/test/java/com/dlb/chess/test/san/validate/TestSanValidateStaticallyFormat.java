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
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.CastlingConstants;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.san.SanValidateFormat;
import com.dlb.chess.san.enums.SanLetter;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.san.validate.statically.format.calculate.SanValidateStaticallyFormat;

//at this stage we allow a lot of invalid SAN's which are then checked later
class TestSanValidateStaticallyFormat implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void testInvalidFormat() {
    checkNotExists("");
    checkNotExists("x");
    checkNotExists("x+");
    checkNotExists("R");
    checkNotExists("Ra");
    checkNotExists("Rxa");
    checkNotExists("axb");
    checkNotExists("Q1");
    checkNotExists("Q22");
    checkNotExists("Qx2");
  }

  @SuppressWarnings("static-method")
  @Test
  void testCastling() {
    checkExists("O-O");
    checkExists("O-O+");
    checkExists("O-O#");

    checkExists("O-O-O");
    checkExists("O-O-O+");
    checkExists("O-O-O#");

    checkNotExists("O-Ox");
    checkNotExists("O-O-Ox");
  }

  @SuppressWarnings("static-method")
  @Test
  void testStandard() {
    // rook
    checkExists("Rb2");
    checkExists("Rab2");
    checkExists("R5b2");
    checkExists("Rxb2");
    checkExists("Raxb2");
    checkExists("R5xb2");
    checkExists("Rb2+");
    checkExists("Rab2+");
    checkExists("R5b2+");
    checkExists("Rb2#");
    checkExists("Rab2#");
    checkExists("R5b2#");
    checkExists("Rxb2+");
    checkExists("Raxb2+");
    checkExists("R5xb2+");
    checkExists("Rxb2#");
    checkExists("Raxb2#");
    checkExists("R5xb2#");

    // knight
    checkExists("Nc3");
    checkExists("Nac3");
    checkExists("N4c3");
    checkExists("Na2c3");
    checkExists("Nxc3");
    checkExists("Naxc3");
    checkExists("N4xc3");
    checkExists("Na2xc3");
    checkExists("Nc3+");
    checkExists("Nac3+");
    checkExists("N4c3+");
    checkExists("Na2c3+");
    checkExists("Nc3#");
    checkExists("Nac3#");
    checkExists("N4c3#");
    checkExists("Na2c3#");
    checkExists("Nxc3+");
    checkExists("Naxc3+");
    checkExists("N4xc3+");
    checkExists("Na2xc3+");
    checkExists("Nxc3#");
    checkExists("Naxc3#");
    checkExists("N4xc3#");
    checkExists("Na2xc3#");

    // bishop
    checkExists("Bd4");
    checkExists("Bcd4");
    checkExists("B5d4");
    checkExists("Be5d4");
    checkExists("Bxd4");
    checkExists("Bcxd4");
    checkExists("B5xd4");
    checkExists("Be5xd4");
    checkExists("Bd4+");
    checkExists("Bcd4+");
    checkExists("B5d4+");
    checkExists("Be5d4+");
    checkExists("Bd4#");
    checkExists("Bcd4#");
    checkExists("B5d4#");
    checkExists("Be5d4#");
    checkExists("Bxd4+");
    checkExists("Bcxd4+");
    checkExists("B5xd4+");
    checkExists("Be5xd4+");
    checkExists("Bxd4#");
    checkExists("Bcxd4#");
    checkExists("B5xd4#");
    checkExists("Be5xd4#");

    // queen
    checkExists("Qf7");
    checkExists("Qaf7");
    checkExists("Q8f7");
    checkExists("Qg8f7");
    checkExists("Qxf7");
    checkExists("Qaxf7");
    checkExists("Q8xf7");
    checkExists("Qg8xf7");
    checkExists("Qf7+");
    checkExists("Qaf7+");
    checkExists("Q8f7+");
    checkExists("Qg8f7+");
    checkExists("Qf7#");
    checkExists("Qaf7#");
    checkExists("Q8f7#");
    checkExists("Qg8f7#");
    checkExists("Qxf7+");
    checkExists("Qaxf7+");
    checkExists("Q8xf7+");
    checkExists("Qg8xf7+");
    checkExists("Qxf7#");
    checkExists("Qaxf7#");
    checkExists("Q8xf7#");
    checkExists("Qg8xf7#");

    // king
    checkExists("Kd7");
    checkExists("Kxd7");
    checkExists("Kd7+");
    checkExists("Kd7#");
    checkExists("Kxd7+");
    checkExists("Kxd7#");

    // white pawn
    checkExists("e4");
    checkExists("dxe4");
    checkExists("fxe4");
    checkExists("e8=Q");
    checkExists("e8=N");
    checkExists("dxe8=Q");
    checkExists("fxe8=N");

    checkExists("e4+");
    checkExists("dxe4+");
    checkExists("fxe4+");
    checkExists("e8=Q+");
    checkExists("e8=N+");
    checkExists("dxe8=Q+");
    checkExists("fxe8=N+");

    checkExists("e4#");
    checkExists("dxe4#");
    checkExists("fxe4#");
    checkExists("e8=Q#");
    checkExists("e8=N#");
    checkExists("dxe8=Q#");
    checkExists("fxe8=N#");

    // black pawn
    checkExists("f5");
    checkExists("exf5");
    checkExists("gxf5");
    checkExists("f1=Q");
    checkExists("f1=B");
    checkExists("exf1=Q");
    checkExists("gxf1=B");

    checkExists("f5+");
    checkExists("exf5+");
    checkExists("gxf5+");
    checkExists("f1=Q+");
    checkExists("f1=B+");
    checkExists("exf1=Q+");
    checkExists("gxf1=B+");

    checkExists("f5#");
    checkExists("exf5#");
    checkExists("gxf5#");
    checkExists("f1=Q#");
    checkExists("f1=B#");
    checkExists("exf1=Q#");
    checkExists("gxf1=B#");

  }

  @SuppressWarnings("static-method")
  @Test
  void testInvalidMovement() {
    // rook
    checkExists("R2a1");
    checkExists("Ra2a1");
    checkExists("Rc2b2");

    // knight
    checkExists("Nb3a1");
    checkExists("Naa1");
    checkExists("N4a1");

    // bishop
    checkExists("Baa1");
    checkExists("B2a1");
    checkExists("Bb3a1");

    // queen
    checkExists("Qb4a1");

    // king
    // all valid

  }

  private static void checkExists(String san) {
    assertTrue(SanValidateStaticallyFormat.exists(san));
  }

  private static void checkNotExists(String san) {
    assertFalse(SanValidateStaticallyFormat.exists(san));
  }

  @SuppressWarnings("static-method")
  @Test
  void testStaticallyAgainstRuntime() {
    for (final Entry<String, SanParse> entry : SanValidateStaticallyFormat.getSanValidationMap().entrySet()) {
      checkStaticallyAgainstRuntime(entry.getKey(), entry.getValue());
    }
  }

  @SuppressWarnings("static-method")
  @Test
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
      checkAgainstUsingGeneratedPawn(sanBase);
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
        checkAgainstUsingGeneratedPawn(sanBase);
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
        checkAgainstUsingGeneratedPawn(sanBase);
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
          checkAgainstUsingGeneratedPawn(sanBase);
        }
      }
    }

    // castling
    checkAgainstUsingGenerated(CastlingConstants.SAN_CASTLING_KING_SIDE);
    checkAgainstUsingGenerated(CastlingConstants.SAN_CASTLING_QUEEN_SIDE);
  }

  private static void checkAgainstUsingGenerated(String sanBase, int indexToInsertCaptureSymbol) {
    for (final String sanVariation : calculateSanVariationList(sanBase, indexToInsertCaptureSymbol)) {
      checkAgainstUsingGenerated(sanVariation);
    }
  }

  private static void checkAgainstUsingGenerated(String sanBase) {

    final var isPassStaticallySanValidationWhite = SanValidateStaticallyFormat.exists(sanBase);
    try {
      final SanParse runTimeResult = SanValidateFormat.validateFormat(sanBase);
      assertTrue(isPassStaticallySanValidationWhite);
      final SanParse staticResult = SanValidateStaticallyFormat.calculate(sanBase);
      assertEquals(runTimeResult, staticResult);

    } catch (@SuppressWarnings("unused") final SanValidationException e) {
      assertFalse(isPassStaticallySanValidationWhite);
    }
  }

  private static void checkAgainstUsingGeneratedPawn(String sanBase) {
    for (final String sanVariation : calculateSanVariationList(sanBase)) {
      checkAgainstUsingGenerated(sanVariation);
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

  private static void checkStaticallyAgainstRuntime(String san, SanParse staticResult) {
    boolean isException;
    try {
      final SanParse runTimeResult = SanValidateFormat.validateFormat(san);
      assertEquals(runTimeResult, staticResult);
      isException = false;
    } catch (@SuppressWarnings("unused") final SanValidationException e) {
      isException = true;
    }
    assertFalse(isException);
  }

}
