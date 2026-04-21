package com.dlb.chess.san.reference;

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
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.enums.SanFormat;
import com.dlb.chess.san.enums.SanSymbol;
import com.dlb.chess.san.enums.SanTerminalMarker;
import com.dlb.chess.san.model.SanConversionCheck;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.san.validate.format.SanValidateFormat;

/**
 * Reference implementation of {@link SanValidateFormat#validateFormat}. Uses the original type-enumeration approach
 * (iterating over all {@link SanFormat} values) as an oracle against which the direct-parse implementation can be
 * verified in tests.
 */
public abstract class SanValidateFormatReference {

  public static SanParse validateFormat(String san) {
    for (final SanFormat sanFormat : SanFormat.values()) {
      final SanConversionCheck sanSanConversion = parseForSanFormat(san, sanFormat);
      if (sanSanConversion.isMatch()) {
        return new SanParse(sanFormat, sanSanConversion.sanConversion());
      }
    }

    throw new IllegalArgumentException("No SanFormat matches the SAN string: \"" + san + "\"");
  }

  private static SanConversionCheck parseForSanFormat(final String san, final SanFormat sanFormat) {

    final SanFormatProperties properties = NonNullWrapperCommon.get(SanFormatPropertiesMap.MAP, sanFormat);

    // length
    final var formatLength = properties.length();
    // additional check or checkmate symbol allowed
    if (san.length() != formatLength && san.length() != formatLength + 1
        || san.length() == formatLength + 1 && !calculateIsAllowedLastChar(san)) {
      return SanConversionCheck.IS_NO_MATCH;
    }

    final SanTerminalMarker sanTerminalMarker = calculateSanTerminalMarker(san, formatLength);

    // castling needs a special treatment
    if (sanFormat == SanFormat.KING_CASTLING_QUEEN_SIDE) {
      // startsWith for the optional final check or checkmate symbol, length is checked
      if (!san.startsWith(CastlingConstants.SAN_CASTLING_QUEEN_SIDE)) {
        return SanConversionCheck.IS_NO_MATCH;
      }
      final var sanConversion = new SanConversion(PieceType.NONE, File.NONE, Rank.NONE, Square.NONE,
          PromotionPieceType.NONE, sanTerminalMarker);
      return new SanConversionCheck(true, sanConversion);
    }
    if (sanFormat == SanFormat.KING_CASTLING_KING_SIDE) {
      // startsWith for the optional final check or checkmate symbol, length is checked
      if (!san.startsWith(CastlingConstants.SAN_CASTLING_KING_SIDE)) {
        return SanConversionCheck.IS_NO_MATCH;
      }
      final var sanConversion = new SanConversion(PieceType.NONE, File.NONE, Rank.NONE, Square.NONE,
          PromotionPieceType.NONE, sanTerminalMarker);
      return new SanConversionCheck(true, sanConversion);
    }

    // movingPieceType: for pawn it is fixed; for RNBQ/king it comes from the first character.
    final PieceType movingPieceType;
    final var movingPieceTypeIndex = properties.movingPieceTypeIndex();
    if (properties.isPawn()) {
      movingPieceType = PieceType.PAWN;
    } else {
      final var checkMovingPieceTypeLetter = san.charAt(movingPieceTypeIndex);
      if (!NotationMovingPiece.exists(checkMovingPieceTypeLetter)) {
        return SanConversionCheck.IS_NO_MATCH;
      }
      final PieceType pieceType = NotationMovingPiece.calculate(checkMovingPieceTypeLetter).getPieceType();
      if (!isPieceTypeForSanFormat(pieceType, sanFormat)) {
        return SanConversionCheck.IS_NO_MATCH;
      }
      movingPieceType = pieceType;
    }

    // fromFileIndex
    final File fromFile;
    final var fromFileIndex = properties.fromFileIndex();
    if (fromFileIndex != -1) {
      final var checkLetter = san.charAt(fromFileIndex);
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
      final var checkLetter = san.charAt(fromRankIndex);
      if (!Rank.exists(checkLetter)) {
        return SanConversionCheck.IS_NO_MATCH;
      }
      fromRank = Rank.calculateRank(checkLetter);
    } else {
      fromRank = Rank.NONE;
    }

    // captureSymbolIndex
    final var captureSymbolIndex = properties.captureSymbolIndex();
    if (captureSymbolIndex != -1) {
      final var checkLetter = san.charAt(captureSymbolIndex);
      if (SanSymbol.CAPTURE.getSymbol() != checkLetter) {
        return SanConversionCheck.IS_NO_MATCH;
      }
    }

    // toFileIndex
    final File toFile;
    final var toFileIndex = properties.toFileIndex();
    if (toFileIndex != -1) {
      final var checkLetter = san.charAt(toFileIndex);
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
      final var checkLetter = san.charAt(toRankIndex);
      if (!Rank.exists(checkLetter)) {
        return SanConversionCheck.IS_NO_MATCH;
      }
      toRank = Rank.calculateRank(checkLetter);
    } else {
      toRank = Rank.NONE;
    }

    // pawn promotion rank enforcement
    if ((sanFormat == SanFormat.PAWN_NON_CAPTURING_NON_PROMOTION
        || sanFormat == SanFormat.PAWN_CAPTURING_NON_PROMOTION) && Rank.calculateIsAnyPromotionRank(toRank)) {
      return SanConversionCheck.IS_NO_MATCH;
    }
    if ((sanFormat == SanFormat.PAWN_NON_CAPTURING_PROMOTION || sanFormat == SanFormat.PAWN_CAPTURING_PROMOTION)
        && !Rank.calculateIsAnyPromotionRank(toRank)) {
      return SanConversionCheck.IS_NO_MATCH;
    }

    // promotionSymbolIndex
    final var promotionSymbolIndex = properties.promotionSymbolIndex();
    if (promotionSymbolIndex != -1) {
      final var checkLetter = san.charAt(promotionSymbolIndex);
      if (SanSymbol.PROMOTION.getSymbol() != checkLetter) {
        return SanConversionCheck.IS_NO_MATCH;
      }
    }

    // promotionPieceTypeIndex
    final PromotionPieceType promotionPieceType;
    final var promotionPieceTypeIndex = properties.promotionPieceTypeIndex();
    if (promotionPieceTypeIndex != -1) {
      final var checkPromotionPieceTypeLetter = san.charAt(promotionPieceTypeIndex);
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
    final var sanConversion = new SanConversion(movingPieceType, fromFile, fromRank, toSquare, promotionPieceType,
        sanTerminalMarker);
    return new SanConversionCheck(true, sanConversion);
  }

  /**
   * Whether the given piece type can produce the given non-pawn, non-castling SAN format.
   * <ul>
   *   <li>King formats accept only KING.</li>
   *   <li>RNBQ formats accept ROOK, KNIGHT, BISHOP, QUEEN.</li>
   * </ul>
   */
  private static boolean isPieceTypeForSanFormat(PieceType pieceType, SanFormat sanFormat) {
    return switch (sanFormat) {
      case KING_NON_CASTLING_NON_CAPTURING, KING_NON_CASTLING_CAPTURING -> pieceType == PieceType.KING;
      case RNBQ_NON_CAPTURING_NEITHER, RNBQ_NON_CAPTURING_FILE, RNBQ_NON_CAPTURING_RANK, RNBQ_NON_CAPTURING_SQUARE,
          RNBQ_CAPTURING_NEITHER, RNBQ_CAPTURING_FILE, RNBQ_CAPTURING_RANK, RNBQ_CAPTURING_SQUARE -> pieceType == PieceType.ROOK
              || pieceType == PieceType.KNIGHT || pieceType == PieceType.BISHOP || pieceType == PieceType.QUEEN;
      default -> false;
    };
  }

  private static boolean calculateIsAllowedLastChar(String san) {
    final var lastLetter = san.charAt(san.length() - 1);
    return lastLetter == SanSymbol.CHECK.getSymbol() || lastLetter == SanSymbol.CHECKMATE.getSymbol();
  }

  private static SanTerminalMarker calculateSanTerminalMarker(String san, int formatLength) {
    if (san.length() != formatLength + 1) {
      return SanTerminalMarker.NONE;
    }
    final var lastLetter = san.charAt(san.length() - 1);
    if (SanSymbol.CHECK.getSymbol() == lastLetter) {
      return SanTerminalMarker.CHECK;
    }
    if (SanSymbol.CHECKMATE.getSymbol() == lastLetter) {
      return SanTerminalMarker.CHECKMATE;
    }

    throw new ProgrammingMistakeException(
        "The expected precondition of last letter checkmate or check symbol does not hold");
  }

}
