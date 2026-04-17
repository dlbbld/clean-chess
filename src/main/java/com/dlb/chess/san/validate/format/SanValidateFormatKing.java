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
 *
 * <p>
 * Parses the SAN character-by-character (sequential), analogous to {@link SanValidateFormatPawn}. Each failure is
 * reported with the most specific {@link SanValidationProblem} describing which character is missing or wrong.
 */
abstract class SanValidateFormatKing extends AbstractSan {

  static SanParse parseKingMove(final String core, final SanTerminalMarker sanTerminalMarker) {
    // core[0] == 'K' ensured by the dispatcher in SanValidateFormat

    // Second character
    if (core.length() == 1) {
      throw new SanValidationException(SanValidationProblem.FORMAT_KING_NON_CASTLING_NO_SECOND_CHARACTER,
          Message.getString("validation.san.format.king.nonCastling.noSecondCharacter"));
    }

    final var secondChar = core.charAt(1);

    // Kx... = capture move
    if (secondChar == 'x') {
      return parseKingCaptureMove(core, sanTerminalMarker);
    }

    // Anything other than a file letter at position 1 is invalid. Rank digits (e.g. "K2f3") and other characters
    // all fall into the same generic "wrong second character" bucket; detecting rank-disambiguation specifically
    // adds no user value beyond what this message already says.
    if (!SanValidateFormat.isFileLetter(secondChar)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_KING_NON_CASTLING_WRONG_SECOND_CHARACTER, Message
          .getString("validation.san.format.king.nonCastling.wrongSecondCharacter",
              NonNullWrapperCommon.toString(secondChar)));
    }

    // K[file]... — non-capturing path (destination file, or more after that)
    return parseKingNonCaptureMove(core, sanTerminalMarker);
  }

  private static SanParse parseKingNonCaptureMove(final String core, final SanTerminalMarker sanTerminalMarker) {
    // core[0]='K', core[1]=file letter. Expect rank digit next.
    final var secondChar = core.charAt(1);

    if (core.length() == 2) {
      throw new SanValidationException(SanValidationProblem.FORMAT_KING_NON_CASTLING_NO_DESTINATION_RANK,
          Message.getString("validation.san.format.king.nonCastling.noDestinationRank"));
    }

    final var thirdChar = core.charAt(2);

    // Third char must be a rank digit. Anything else (another file letter, 'x', etc.) falls into the same generic
    // "wrong destination rank" bucket; detecting file-disambiguation specifically adds no user value beyond what
    // this message already says.
    if (!SanValidateFormat.isRankDigit(thirdChar)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_KING_NON_CASTLING_WRONG_DESTINATION_RANK,
          Message.getString("validation.san.format.king.nonCastling.wrongDestinationRank",
              NonNullWrapperCommon.toString(thirdChar)));
    }

    // K[file][rank] — valid destination, length 3 expected
    if (core.length() == 3) {
      return new SanParse(SanType.KING_NON_CASTLING_NON_CAPTURING_MOVE,
          new SanConversion(File.NONE, Rank.NONE,
              Square.calculate(SanValidateFormat.parseFile(secondChar), SanValidateFormat.parseRank(thirdChar)),
              PromotionPieceType.NONE, sanTerminalMarker));
    }

    // Length > 3 after valid K[file][rank] — overlength. Square-disambiguation attempts like "Ka2b3" also land here;
    // the generic overlength message is sufficient since the user can see the move already has 4+ chars.
    throw new SanValidationException(SanValidationProblem.FORMAT_KING_NON_CASTLING_OVERLENGTH_NON_CAPTURE,
        Message.getString("validation.san.format.king.nonCastling.overlengthNonCapture"));
  }

  private static SanParse parseKingCaptureMove(final String core, final SanTerminalMarker sanTerminalMarker) {
    // core[0]='K', core[1]='x'. Expect [file][rank] next.

    // Third character (destination file)
    if (core.length() == 2) {
      throw new SanValidationException(SanValidationProblem.FORMAT_KING_NON_CASTLING_NO_CAPTURE_FILE,
          Message.getString("validation.san.format.king.nonCastling.noCaptureFile"));
    }

    final var thirdChar = core.charAt(2);

    if (!SanValidateFormat.isFileLetter(thirdChar)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_KING_NON_CASTLING_WRONG_CAPTURE_FILE,
          Message.getString("validation.san.format.king.nonCastling.wrongCaptureFile",
              NonNullWrapperCommon.toString(thirdChar)));
    }

    // Fourth character (destination rank)
    if (core.length() == 3) {
      throw new SanValidationException(SanValidationProblem.FORMAT_KING_NON_CASTLING_NO_CAPTURE_RANK,
          Message.getString("validation.san.format.king.nonCastling.noCaptureRank"));
    }

    final var fourthChar = core.charAt(3);

    if (!SanValidateFormat.isRankDigit(fourthChar)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_KING_NON_CASTLING_WRONG_CAPTURE_RANK,
          Message.getString("validation.san.format.king.nonCastling.wrongCaptureRank",
              NonNullWrapperCommon.toString(fourthChar)));
    }

    // Valid Kx[file][rank] — length 4 expected
    if (core.length() == 4) {
      return new SanParse(SanType.KING_NON_CASTLING_CAPTURING_MOVE,
          new SanConversion(File.NONE, Rank.NONE,
              Square.calculate(SanValidateFormat.parseFile(thirdChar), SanValidateFormat.parseRank(fourthChar)),
              PromotionPieceType.NONE, sanTerminalMarker));
    }

    // Length > 4 after valid Kx[file][rank] — always overlength
    throw new SanValidationException(SanValidationProblem.FORMAT_KING_NON_CASTLING_OVERLENGTH_CAPTURE,
        Message.getString("validation.san.format.king.nonCastling.overlengthCapture"));
  }

}
