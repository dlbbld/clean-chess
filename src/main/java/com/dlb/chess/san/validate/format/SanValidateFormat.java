package com.dlb.chess.san.validate.format;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.CastlingConstants;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.enums.SanTerminalMarker;
import com.dlb.chess.san.enums.SanType;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanParse;

/**
 * Public entry point for SAN format validation. Dispatches on the leading character of the core (trailing check /
 * checkmate marker already stripped) to the appropriate piece-type parser:
 *
 * <pre>
 *   O            → {@link #parseCastling}
 *   a–h          → {@link SanValidateFormatPawn#parsePawnMove}
 *   K            → {@link SanValidateFormatKing#parseKingMove}
 *   R, N, B, Q   → {@link SanValidateFormatRnbq#parseRnbqMove}
 * </pre>
 */
public abstract class SanValidateFormat extends AbstractSan {

  public static SanParse validateFormat(String san) {

    // Minimal guard to prevent charAt crash below. Any other malformed input is rejected downstream by the
    // per-piece parsers with a specific problem code.
    if (san.isBlank()) {
      throw new SanValidationException(SanValidationProblem.FORMAT_BLANK,
          Message.getString("validation.san.format.blank"));
    }

    // Strip the optional trailing check (+) or checkmate (#) symbol to get the core SAN string
    final var last = san.charAt(san.length() - 1);
    final var sanTerminalMarker = parseSanTerminalMarker(last);
    final var core = sanTerminalMarker == SanTerminalMarker.NONE ? san : san.substring(0, san.length() - 1);

    if (core.isEmpty()) {
      throw new SanValidationException(SanValidationProblem.FORMAT_FIRST_CHARACTER,
          Message.getString("validation.san.format.firstCharacter", NonNullWrapperCommon.toString(last)));
    }

    final var first = core.charAt(0);

    // Dispatch on the first character to the appropriate parser
    if (first == 'O') {
      return parseCastling(core, sanTerminalMarker);
    }
    if (isFileLetter(first)) {
      return SanValidateFormatPawn.parsePawnMove(core, sanTerminalMarker);
    }
    if (first == 'K') {
      return SanValidateFormatKing.parseKingMove(core, sanTerminalMarker);
    }
    if (isPieceLetterRnbq(first)) {
      return SanValidateFormatRnbq.parseRnbqMove(core, sanTerminalMarker);
    }
    throw new SanValidationException(SanValidationProblem.FORMAT_FIRST_CHARACTER,
        Message.getString("validation.san.format.firstCharacter", NonNullWrapperCommon.toString(first)));
  }

  private static SanTerminalMarker parseSanTerminalMarker(final char last) {
    if (last == '#') {
      return SanTerminalMarker.CHECKMATE;
    }
    if (last == '+') {
      return SanTerminalMarker.CHECK;
    }
    return SanTerminalMarker.NONE;
  }

  /**
   * Parses a castling SAN string: {@code "O-O"} (king-side) or {@code "O-O-O"} (queen-side). The optional trailing
   * check/checkmate symbol has already been stripped into {@code sanTerminalMarker}.
   */
  private static SanParse parseCastling(final String core, final SanTerminalMarker sanTerminalMarker) {
    final var sanConversion = new SanConversion(File.NONE, Rank.NONE, Square.NONE, PromotionPieceType.NONE,
        sanTerminalMarker);
    if (CastlingConstants.SAN_CASTLING_QUEEN_SIDE.equals(core)) {
      return new SanParse(SanType.KING_CASTLING_QUEEN_SIDE_MOVE, sanConversion);
    }
    if (CastlingConstants.SAN_CASTLING_KING_SIDE.equals(core)) {
      return new SanParse(SanType.KING_CASTLING_KING_SIDE_MOVE, sanConversion);
    }
    throw new SanValidationException(SanValidationProblem.FORMAT_KING_CASTLING,
        Message.getString("validation.san.format.king.castling"));
  }

  // --- Shared character-class and parsing helpers (package-private) ---

  static boolean isFileLetter(final char c) {
    return c >= 'a' && c <= 'h';
  }

  static boolean isRankDigit(final char c) {
    return c >= '1' && c <= '8';
  }

  private static boolean isPieceLetterRnbq(final char c) {
    return c == 'R' || c == 'N' || c == 'B' || c == 'Q';
  }

  static File parseFile(final char c) {
    return File.calculateFile(c);
  }

  static Rank parseRank(final char c) {
    return Rank.calculateRank(c);
  }

}
