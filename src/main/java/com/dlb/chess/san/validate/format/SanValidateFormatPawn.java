package com.dlb.chess.san.validate.format;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.NotationPromotionPiece;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.enums.SanTerminalMarker;
import com.dlb.chess.san.enums.SanType;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanParse;

/**
 * Parses pawn SAN moves — both forward (e.g. {@code d3}, {@code d8=Q}) and capturing (e.g. {@code dxe5},
 * {@code dxe8=Q}).
 */
abstract class SanValidateFormatPawn extends AbstractSan {

  static SanParse parsePawnMove(final String core, final SanTerminalMarker sanTerminalMarker) {
    // too short
    if (core.length() == 1) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_LENGTH_GENERAL,
          Message.getString("validation.san.format.pawn.length.general"));
    }

    final var secondChar = core.charAt(1);

    if (SanValidateFormat.isRankDigit(secondChar)) {
      return parsePawnForwardMove(core, sanTerminalMarker);
    }
    if (isCaptureSymbol(secondChar)) {
      return parsePawnCaptureMove(core, sanTerminalMarker);
    }

    throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_SECOND_CHARACTER,
        Message.getString("validation.san.format.pawn.secondCharacter", NonNullWrapperCommon.toString(secondChar)));
  }

  private static SanParse parsePawnForwardMove(final String core, final SanTerminalMarker sanTerminalMarker) {
    final var length = core.length();
    final var firstChar = core.charAt(0);
    final var secondChar = core.charAt(1);

    if (!isAnyPromotionRank(secondChar)) {
      // non promotion e.g. d3

      // too long
      if (length > 2) {
        throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_LENGTH_FORWARD_NON_PROMOTION,
            Message.getString("validation.san.format.pawn.length.forward.nonPromotion"));
      }

      // valid
      return new SanParse(SanType.PAWN_NON_CAPTURING_NON_PROMOTION_MOVE,
          new SanConversion(File.NONE, Rank.NONE,
              Square.calculate(SanValidateFormat.parseFile(firstChar), SanValidateFormat.parseRank(secondChar)),
              PromotionPieceType.NONE, sanTerminalMarker));
    }

    // promotion e.g. d8=Q

    // no promotion symbol
    if (length == 2) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_PROMOTION_NO_PROMOTION_SYMBOL,
          Message.getString("validation.san.format.pawn.promotion.noPromotionSymbol"));
    }

    final var thirdChar = core.charAt(2);

    // wrong promotion symbol
    if (!isPromotionSymbol(thirdChar)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_PROMOTION_WRONG_PROMOTION_SYMBOL,
          Message.getString("validation.san.format.pawn.promotion.wrongPromotionSymbol"));
    }

    // no promotion piece
    if (length == 3) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_PROMOTION_NO_PROMOTION_PIECE,
          Message.getString("validation.san.format.pawn.promotion.noPromotionPiece"));
    }

    final var fourthChar = core.charAt(3);

    // wrong promotion piece
    if (!NotationPromotionPiece.exists(fourthChar)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_PROMOTION_WRONG_PROMOTION_PIECE,
          Message.getString("validation.san.format.pawn.promotion.wrongPromotionPiece",
              NonNullWrapperCommon.toString(fourthChar)));
    }

    // too long
    if (length > 4) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_LENGTH_FORWARD_PROMOTION,
          Message.getString("validation.san.format.pawn.length.forward.promotion"));
    }

    // valid
    return new SanParse(SanType.PAWN_NON_CAPTURING_PROMOTION_MOVE,
        new SanConversion(File.NONE, Rank.NONE,
            Square.calculate(SanValidateFormat.parseFile(firstChar), SanValidateFormat.parseRank(secondChar)),
            NotationPromotionPiece.calculate(fourthChar).getPromotionPieceType(), sanTerminalMarker));
  }

  private static SanParse parsePawnCaptureMove(final String core, final SanTerminalMarker sanTerminalMarker) {
    final var length = core.length();
    final var firstChar = core.charAt(0);

    // too short
    if (length == 2) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_LENGTH_CAPTURE_GENERAL,
          Message.getString("validation.san.format.pawn.length.capture.general"));
    }

    final var thirdChar = core.charAt(2);

    // file check
    if (!SanValidateFormat.isFileLetter(thirdChar)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_CAPTURE_TO_FILE,
          Message.getString("validation.san.format.pawn.captureToFile", NonNullWrapperCommon.toString(thirdChar)));
    }

    // too short
    if (length == 3) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_LENGTH_CAPTURE_GENERAL,
          Message.getString("validation.san.format.pawn.length.capture.general"));
    }

    final var fourthChar = core.charAt(3);

    // rank check
    if (!SanValidateFormat.isRankDigit(fourthChar)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_CAPTURE_TO_RANK,
          Message.getString("validation.san.format.pawn.captureToRank", NonNullWrapperCommon.toString(fourthChar)));
    }

    if (!isAnyPromotionRank(fourthChar)) {
      // non promotion e.g. dxe5

      // too long
      if (length > 4) {
        throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_LENGTH_CAPTURE_NON_PROMOTION,
            Message.getString("validation.san.format.pawn.length.capture.nonPromotion"));
      }

      // valid
      return new SanParse(SanType.PAWN_CAPTURING_NON_PROMOTION_MOVE,
          new SanConversion(SanValidateFormat.parseFile(firstChar), Rank.NONE,
              Square.calculate(SanValidateFormat.parseFile(thirdChar), SanValidateFormat.parseRank(fourthChar)),
              PromotionPieceType.NONE, sanTerminalMarker));
    }

    // promotion e.g. dxe8=Q

    // no promotion symbol
    if (length == 4) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_PROMOTION_NO_PROMOTION_SYMBOL,
          Message.getString("validation.san.format.pawn.promotion.noPromotionSymbol"));
    }

    final var fifthChar = core.charAt(4);

    // wrong promotion symbol
    if (!isPromotionSymbol(fifthChar)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_PROMOTION_WRONG_PROMOTION_SYMBOL,
          Message.getString("validation.san.format.pawn.promotion.wrongPromotionSymbol",
              NonNullWrapperCommon.toString(fifthChar)));
    }

    // no promotion piece
    if (length == 5) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_PROMOTION_NO_PROMOTION_PIECE,
          Message.getString("validation.san.format.pawn.promotion.noPromotionPiece"));
    }

    final var sixthChar = core.charAt(5);

    // wrong promotion piece
    if (!NotationPromotionPiece.exists(sixthChar)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_PROMOTION_WRONG_PROMOTION_PIECE,
          Message.getString("validation.san.format.pawn.promotion.wrongPromotionPiece",
              NonNullWrapperCommon.toString(sixthChar)));
    }

    // too long
    if (length > 6) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_LENGTH_CAPTURE_PROMOTION,
          Message.getString("validation.san.format.pawn.length.capture.promotion"));
    }

    // valid
    return new SanParse(SanType.PAWN_CAPTURING_PROMOTION_MOVE,
        new SanConversion(SanValidateFormat.parseFile(firstChar), Rank.NONE,
            Square.calculate(SanValidateFormat.parseFile(thirdChar), SanValidateFormat.parseRank(fourthChar)),
            NotationPromotionPiece.calculate(sixthChar).getPromotionPieceType(), sanTerminalMarker));
  }

  private static boolean isAnyPromotionRank(final char c) {
    return c == '1' || c == '8';
  }

  private static boolean isCaptureSymbol(final char c) {
    return c == 'x';
  }

  private static boolean isPromotionSymbol(final char c) {
    return c == '=';
  }

}
