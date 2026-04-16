package com.dlb.chess.san.validate.format;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.enums.SanTerminalMarker;
import com.dlb.chess.san.enums.SanType;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanParse;

/**
 * Parses king-move SAN (core starts with 'K'). Castling (O-O / O-O-O) is handled separately in
 * {@link SanValidateFormat} and does not come through here.
 *
 * <p>
 * Recognised core patterns (trailing +/# already stripped):
 *
 * <pre>
 *   length 3: K[file][rank]   non-castling, non-capturing   e.g. Ke5
 *   length 4: Kx[file][rank]  non-castling, capturing       e.g. Kxe5
 * </pre>
 */
// TODO align with piece parsing -> sequential
abstract class SanValidateFormatKing extends AbstractSan {

  static SanParse parseKingMove(final String core, final SanTerminalMarker sanTerminalMarker) {
    return switch (core.length()) {

      case 3 -> {
        // K[toFile][toRank] – non-capturing e.g. "Ke5"
        final var toFileChar = core.charAt(1);
        final var toRankChar = core.charAt(2);
        if (!SanValidateFormat.isFileLetter(toFileChar) || !SanValidateFormat.isRankDigit(toRankChar)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_DESTINATION,
              Message.getString("validation.san.format.king.destination"));
        }
        yield new SanParse(SanType.KING_NON_CASTLING_NON_CAPTURING_MOVE,
            new SanConversion(File.NONE, Rank.NONE,
                Square.calculate(SanValidateFormat.parseFile(toFileChar), SanValidateFormat.parseRank(toRankChar)),
                PromotionPieceType.NONE, sanTerminalMarker));
      }

      case 4 -> {
        // Kx[toFile][toRank] – capturing e.g. "Kxe5"
        // But first check for disambiguation patterns (never valid for king)
        final var secondChar4 = core.charAt(1);
        final var thirdChar4 = core.charAt(2);
        final var fourthChar4 = core.charAt(3);
        // K[file][toFile][toRank] — file disambiguation
        if (SanValidateFormat.isFileLetter(secondChar4) && SanValidateFormat.isFileLetter(thirdChar4)
            && SanValidateFormat.isRankDigit(fourthChar4)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_FILE_SPECIFIED,
              Message.getString("validation.san.format.king.fileSpecified"));
        }
        // K[rank][toFile][toRank] — rank disambiguation
        if (SanValidateFormat.isRankDigit(secondChar4) && SanValidateFormat.isFileLetter(thirdChar4)
            && SanValidateFormat.isRankDigit(fourthChar4)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_RANK_SPECIFIED,
              Message.getString("validation.san.format.king.rankSpecified"));
        }
        if (secondChar4 != 'x') {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_SECOND_CHARACTER, Message
              .getString("validation.san.format.king.secondCharacter", NonNullWrapperCommon.toString(secondChar4)));
        }
        if (!SanValidateFormat.isFileLetter(thirdChar4) || !SanValidateFormat.isRankDigit(fourthChar4)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_CAPTURE_DESTINATION,
              Message.getString("validation.san.format.king.captureDestination"));
        }
        yield new SanParse(SanType.KING_NON_CASTLING_CAPTURING_MOVE,
            new SanConversion(File.NONE, Rank.NONE,
                Square.calculate(SanValidateFormat.parseFile(thirdChar4), SanValidateFormat.parseRank(fourthChar4)),
                PromotionPieceType.NONE, sanTerminalMarker));
      }

      case 5 -> {
        // K[file]x[toFile][toRank] or K[rank]x[toFile][toRank] or K[file][rank][toFile][toRank]
        final var secondChar5 = core.charAt(1);
        final var thirdChar5 = core.charAt(2);
        final var fourthChar5 = core.charAt(3);
        final var fifthChar5 = core.charAt(4);
        // K[file]x[toFile][toRank] — file disambiguation + capture
        if (SanValidateFormat.isFileLetter(secondChar5) && thirdChar5 == 'x'
            && SanValidateFormat.isFileLetter(fourthChar5) && SanValidateFormat.isRankDigit(fifthChar5)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_FILE_SPECIFIED,
              Message.getString("validation.san.format.king.fileSpecified"));
        }
        // K[rank]x[toFile][toRank] — rank disambiguation + capture
        if (SanValidateFormat.isRankDigit(secondChar5) && thirdChar5 == 'x'
            && SanValidateFormat.isFileLetter(fourthChar5) && SanValidateFormat.isRankDigit(fifthChar5)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_RANK_SPECIFIED,
              Message.getString("validation.san.format.king.rankSpecified"));
        }
        // K[file][rank][toFile][toRank] — square disambiguation
        if (SanValidateFormat.isFileLetter(secondChar5) && SanValidateFormat.isRankDigit(thirdChar5)
            && SanValidateFormat.isFileLetter(fourthChar5) && SanValidateFormat.isRankDigit(fifthChar5)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_SQUARE_SPECIFIED,
              Message.getString("validation.san.format.king.squareSpecified"));
        }
        throw new SanValidationException(SanValidationProblem.FORMAT_KING_LENGTH,
            Message.getString("validation.san.format.king.length"));
      }

      case 6 -> {
        // K[file]x[toFile][toRank] or K[rank]x[toFile][toRank] or K[file][rank][toFile][toRank]
        // or K[file][rank]x[toFile][toRank] (square disambiguation + capture)
        final var c1 = core.charAt(1);
        final var c2 = core.charAt(2);
        final var c3 = core.charAt(3);
        final var c4 = core.charAt(4);
        final var c5 = core.charAt(5);
        // Kfxa1 — file disambiguation + capture
        if (SanValidateFormat.isFileLetter(c1) && c2 == 'x' && SanValidateFormat.isFileLetter(c3)
            && SanValidateFormat.isRankDigit(c4)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_FILE_SPECIFIED,
              Message.getString("validation.san.format.king.fileSpecified"));
        }
        // K2xf3 — rank disambiguation + capture
        if (SanValidateFormat.isRankDigit(c1) && c2 == 'x' && SanValidateFormat.isFileLetter(c3)
            && SanValidateFormat.isRankDigit(c4)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_RANK_SPECIFIED,
              Message.getString("validation.san.format.king.rankSpecified"));
        }
        // Ka2b3 — square disambiguation, non-capturing
        if (SanValidateFormat.isFileLetter(c1) && SanValidateFormat.isRankDigit(c2)
            && SanValidateFormat.isFileLetter(c3) && SanValidateFormat.isRankDigit(c4)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_SQUARE_SPECIFIED,
              Message.getString("validation.san.format.king.squareSpecified"));
        }
        // Ka2xb3 — square disambiguation + capture
        if (SanValidateFormat.isFileLetter(c1) && SanValidateFormat.isRankDigit(c2) && c3 == 'x'
            && SanValidateFormat.isFileLetter(c4) && SanValidateFormat.isRankDigit(c5)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_SQUARE_SPECIFIED,
              Message.getString("validation.san.format.king.squareSpecified"));
        }
        throw new SanValidationException(SanValidationProblem.FORMAT_KING_LENGTH,
            Message.getString("validation.san.format.king.length"));
      }

      default -> throw new SanValidationException(SanValidationProblem.FORMAT_KING_LENGTH,
          Message.getString("validation.san.format.king.length"));
    };
  }

}
