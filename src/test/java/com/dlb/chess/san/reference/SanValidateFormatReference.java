package com.dlb.chess.san.reference;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.CastlingConstants;
import com.dlb.chess.common.enums.NotationMovingPiece;
import com.dlb.chess.common.enums.NotationPromotionPiece;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.SanValidateFormat;
import com.dlb.chess.san.enums.CheckmateOrCheck;
import com.dlb.chess.san.enums.SanFormat;
import com.dlb.chess.san.enums.SanLetter;
import com.dlb.chess.san.enums.SanType;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanConversionCheck;
import com.dlb.chess.san.model.SanParse;

/**
 * Reference implementation of {@link SanValidateFormat#validateFormat}. Uses the original
 * type-enumeration approach (iterating over all {@link SanType} values) as an oracle against which
 * the direct-parse implementation can be verified in tests.
 */
public abstract class SanValidateFormatReference {

  private static final List<String> ALLOWED_LAST_LETTER_SYMBOLS;

  static {
    ALLOWED_LAST_LETTER_SYMBOLS = new ArrayList<>();
    ALLOWED_LAST_LETTER_SYMBOLS.add(SanLetter.CHECK.getLetter());
    ALLOWED_LAST_LETTER_SYMBOLS.add(SanLetter.CHECKMATE.getLetter());
  }

  public static SanParse validateFormat(String san) {
    SanValidateFormat.validateFormatBasic(san);

    for (final SanType sanType : SanType.values()) {
      final SanConversionCheck sanSanConversion = parseForSanType(san, sanType);
      if (sanSanConversion.isMatch()) {
        return new SanParse(sanType, sanSanConversion.sanConversion());
      }
    }

    throw new SanValidationException(SanValidationProblem.FORMAT, Message.getString("validation.san.format"));
  }

  private static SanConversionCheck parseForSanType(final String san, final SanType sanType) {

    final SanFormat sanFormat = sanType.getSanFormat();
    final SanFormatProperties properties = SanFormatPropertiesMap.MAP.get(sanFormat);

    // length
    final var formatLength = properties.length();
    // additional check or checkmate symbol allowed
    if (san.length() != formatLength && san.length() != formatLength + 1
        || san.length() == formatLength + 1 && !calculateIsAllowedLastChar(san)) {
      return SanConversionCheck.IS_NO_MATCH;
    }

    final CheckmateOrCheck checkmateOrCheck = calculateCheckmateOrCheck(san, formatLength);

    // castling needs a special treatment
    if (sanFormat == SanFormat.KING_CASTLING_QUEEN_SIDE_FORMAT) {
      // startsWith for the optional final check or checkmate symbol, length is checked
      if (!san.startsWith(CastlingConstants.SAN_CASTLING_QUEEN_SIDE)) {
        return SanConversionCheck.IS_NO_MATCH;
      }
      final var sanConversion = new SanConversion(File.NONE, Rank.NONE, Square.NONE, PromotionPieceType.NONE,
          checkmateOrCheck);
      return new SanConversionCheck(true, sanConversion);
    }
    if (sanFormat == SanFormat.KING_CASTLING_KING_SIDE_FORMAT) {
      // startsWith for the optional final check or checkmate symbol, length is checked
      if (!san.startsWith(CastlingConstants.SAN_CASTLING_KING_SIDE)) {
        return SanConversionCheck.IS_NO_MATCH;
      }
      final var sanConversion = new SanConversion(File.NONE, Rank.NONE, Square.NONE, PromotionPieceType.NONE,
          checkmateOrCheck);
      return new SanConversionCheck(true, sanConversion);
    }

    // movingPieceTypeIndex
    final var movingPieceTypeIndex = properties.movingPieceTypeIndex();
    if (!properties.isPawn()) {
      final var checkMovingPieceTypeLetter = NonNullWrapperCommon.toString(san.charAt(movingPieceTypeIndex));
      if (!NotationMovingPiece.exists(checkMovingPieceTypeLetter)) {
        return SanConversionCheck.IS_NO_MATCH;
      }
      final PieceType pieceType = NotationMovingPiece.calculate(checkMovingPieceTypeLetter).getPieceType();
      if (pieceType != sanType.getMovingPieceType()) {
        return SanConversionCheck.IS_NO_MATCH;
      }
    }

    // fromFileIndex
    final File fromFile;
    final var fromFileIndex = properties.fromFileIndex();
    if (fromFileIndex != -1) {
      final var checkLetter = NonNullWrapperCommon.toString(san.charAt(fromFileIndex));
      if (!File.exists(checkLetter)) {
        return SanConversionCheck.IS_NO_MATCH;
      }
      fromFile = File.calculateFile(checkLetter);
    } else {
      fromFile = File.NONE;
    }

    // fromRankIndex
    final Rank fromRank;
    final var fromRankIndex = properties.fromRankIndex();
    if (fromRankIndex != -1) {
      final var checkLetter = NonNullWrapperCommon.toString(san.charAt(fromRankIndex));
      if (!BasicUtility.isInt(checkLetter)) {
        return SanConversionCheck.IS_NO_MATCH;
      }
      final var checkRankNumber = BasicUtility.parseInt(checkLetter);
      if (!Rank.exists(checkRankNumber)) {
        return SanConversionCheck.IS_NO_MATCH;
      }
      fromRank = Rank.calculateRank(checkRankNumber);
    } else {
      fromRank = Rank.NONE;
    }

    // captureSymbolIndex
    final var captureSymbolIndex = properties.captureSymbolIndex();
    if (captureSymbolIndex != -1) {
      final var checkLetter = NonNullWrapperCommon.toString(san.charAt(captureSymbolIndex));
      if (!SanLetter.CAPTURE.getLetter().equals(checkLetter)) {
        return SanConversionCheck.IS_NO_MATCH;
      }
    }

    // toFileIndex
    final File toFile;
    final var toFileIndex = properties.toFileIndex();
    if (toFileIndex != -1) {
      final var checkLetter = NonNullWrapperCommon.toString(san.charAt(toFileIndex));
      if (!File.exists(checkLetter)) {
        return SanConversionCheck.IS_NO_MATCH;
      }
      toFile = File.calculateFile(checkLetter);
    } else {
      toFile = File.NONE;
    }

    // toRankIndex
    final Rank toRank;
    final var toRankIndex = properties.toRankIndex();
    if (toRankIndex != -1) {
      final var checkLetter = NonNullWrapperCommon.toString(san.charAt(toRankIndex));
      if (!BasicUtility.isInt(checkLetter)) {
        return SanConversionCheck.IS_NO_MATCH;
      }
      final var checkRankNumber = BasicUtility.parseInt(checkLetter);
      if (!Rank.exists(checkRankNumber)) {
        return SanConversionCheck.IS_NO_MATCH;
      }
      toRank = Rank.calculateRank(checkRankNumber);
    } else {
      toRank = Rank.NONE;
    }

    // promotionSymbolIndex
    final var promotionSymbolIndex = properties.promotionSymbolIndex();
    if (promotionSymbolIndex != -1) {
      final var checkLetter = NonNullWrapperCommon.toString(san.charAt(promotionSymbolIndex));
      if (!SanLetter.PROMOTION.getLetter().equals(checkLetter)) {
        return SanConversionCheck.IS_NO_MATCH;
      }
    }

    // promotionPieceTypeIndex
    final PromotionPieceType promotionPieceType;
    final var promotionPieceTypeIndex = properties.promotionPieceTypeIndex();
    if (promotionPieceTypeIndex != -1) {
      final var checkPromotionPieceTypeLetter = NonNullWrapperCommon.toString(san.charAt(promotionPieceTypeIndex));
      if (!NotationPromotionPiece.exists(checkPromotionPieceTypeLetter)) {
        return SanConversionCheck.IS_NO_MATCH;
      }
      promotionPieceType = NotationPromotionPiece.calculate(checkPromotionPieceTypeLetter).getPromotionPieceType();
    } else {
      promotionPieceType = PromotionPieceType.NONE;
    }

    final Square toSquare;
    if (toFile == File.NONE && toRank == Rank.NONE) {
      toSquare = Square.NONE;
    } else if (toFile != File.NONE && toRank != Rank.NONE) {
      toSquare = Square.calculate(toFile, toRank);
    } else {
      throw new ProgrammingMistakeException(
          "Incorrect file/rank calculation - either file and rank are both set for non-castling moves or both not set for castling moves");
    }
    final var sanConversion = new SanConversion(fromFile, fromRank, toSquare, promotionPieceType, checkmateOrCheck);
    return new SanConversionCheck(true, sanConversion);
  }

  private static boolean calculateIsAllowedLastChar(String san) {
    final var lastLetter = NonNullWrapperCommon.toString(san.charAt(san.length() - 1));
    return ALLOWED_LAST_LETTER_SYMBOLS.contains(lastLetter);
  }

  private static CheckmateOrCheck calculateCheckmateOrCheck(String san, int formatLength) {
    if (san.length() != formatLength + 1) {
      return CheckmateOrCheck.NONE;
    }
    final var lastLetter = NonNullWrapperCommon.toString(san.charAt(san.length() - 1));
    if (SanLetter.CHECKMATE.getLetter().equals(lastLetter)) {
      return CheckmateOrCheck.CHECKMATE;
    }
    if (SanLetter.CHECK.getLetter().equals(lastLetter)) {
      return CheckmateOrCheck.CHECK;
    }

    throw new ProgrammingMistakeException(
        "The expected precondition of last letter checkmate or check symbol does not hold");
  }

}
