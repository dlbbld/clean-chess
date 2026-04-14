package com.dlb.chess.san.validate;

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
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.enums.SanTerminalMarker;
import com.dlb.chess.san.enums.SanType;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanParse;

public abstract class SanValidateFormat extends AbstractSan {

  public static void validateFormatBasic(String san) {
    // fast pre-checks to reject obviously invalid strings in a single pass
    if (san.isBlank()) {
      throw new SanValidationException(SanValidationProblem.BLANK, Message.getString("validation.san.format.blank"));
    }
    if (san.length() > 7) {
      throw new SanValidationException(SanValidationProblem.FORMAT,
          Message.getString("validation.san.format.nonSpecific"));
    }
    var countX = 0;
    var countEquals = 0;
    var equalsIndex = -1;
    var countCheckOrCheckmate = 0;
    var countK = 0;
    var countRbnq = 0;
    var countDigits = 0;
    var countFiles = 0;
    for (var i = 0; i < san.length(); i++) {
      switch (san.charAt(i)) {
        case 'x' -> countX++;
        case '=' -> {
          countEquals++;
          equalsIndex = i;
        }
        case '+', '#' -> countCheckOrCheckmate++;
        case 'K' -> countK++;
        case 'R', 'N', 'B', 'Q' -> countRbnq++;
        case '1', '2', '3', '4', '5', '6', '7', '8' -> countDigits++;
        case 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' -> countFiles++;
        case 'O', '-' -> {
          // castling characters, counted implicitly by parseCastling
        }
        default -> throw new SanValidationException(SanValidationProblem.FORMAT_INVALID_CHARACTER,
            Message.getString("validation.san.format.invalidCharacter", NonNullWrapperCommon.toString(san.charAt(i))));
      }
    }
    if (countX > 1 || countEquals > 1 || countCheckOrCheckmate > 1 || countK > 1 || countRbnq > 1 || countDigits > 2
        || countFiles > 2) {
      throw new SanValidationException(SanValidationProblem.FORMAT,
          Message.getString("validation.san.format.nonSpecific"));
    }
    if (countEquals == 1 && equalsIndex != san.length() - 2 && equalsIndex != san.length() - 3) {
      throw new SanValidationException(SanValidationProblem.FORMAT,
          Message.getString("validation.san.format.nonSpecific"));
    }
  }

  public static SanParse validateFormat(String san) {

    validateFormatBasic(san);

    // Strip the optional trailing check (+) or checkmate (#) symbol to get the core SAN string
    final var checkmateOrCheck = parseSanTerminalMarker(san);
    final var core = checkmateOrCheck == SanTerminalMarker.NONE ? san : san.substring(0, san.length() - 1);

    if (core.isEmpty()) {
      throw invalidFormat();
    }

    final var first = core.charAt(0);

    // Dispatch on the first character to the appropriate parser
    if (first == 'O') {
      return parseCastling(core, checkmateOrCheck);
    }
    if (isFileLetter(first)) {
      return parsePawnMove(core, checkmateOrCheck);
    }
    if (first == 'K') {
      return parseKingMove(core, checkmateOrCheck);
    }
    if (isPieceLetterRbnq(first)) {
      return parseRbnqMove(core, checkmateOrCheck);
    }
    throw new SanValidationException(SanValidationProblem.FORMAT_FIRST_CHARACTER,
        Message.getString("validation.san.format.firstCharacter", NonNullWrapperCommon.toString(first)));
  }

  private static SanTerminalMarker parseSanTerminalMarker(final String san) {
    final var last = san.charAt(san.length() - 1);
    if (last == '#') {
      return SanTerminalMarker.CHECKMATE;
    }
    if (last == '+') {
      return SanTerminalMarker.CHECK;
    }
    return SanTerminalMarker.NONE;
  }

  private static boolean isFileLetter(final char c) {
    return c >= 'a' && c <= 'h';
  }

  private static boolean isRankDigit(final char c) {
    return c >= '1' && c <= '8';
  }

  private static boolean isAnyPromotionRank(final char c) {
    return c == '1' || c == '8';
  }

  private static boolean isPieceLetterRbnq(final char c) {
    return c == 'R' || c == 'N' || c == 'B' || c == 'Q';
  }

  private static boolean isCaptureSymbol(final char c) {
    return c == 'x';
  }

  private static boolean isPromotionSymbol(final char c) {
    return c == '=';
  }

  private static File parseFile(final char c) {
    return File.calculateFile(NonNullWrapperCommon.toString(c));
  }

  private static Rank parseRank(final char c) {
    return Rank.calculateRank(BasicUtility.parseInt(NonNullWrapperCommon.toString(c)));
  }

  /**
   * Converts a promotion-piece character to its {@link PromotionPieceType}. Valid promotion pieces are R, N, B, Q; K
   * and P are not valid and cause a {@link SanValidationException} to be thrown.
   */
  private static PromotionPieceType parsePromotionPiece(final char c) {
    final var letter = NonNullWrapperCommon.toString(c);
    if (!NotationPromotionPiece.exists(letter)) {
      throw invalidFormat();
    }
    return NotationPromotionPiece.calculate(letter).getPromotionPieceType();
  }

  private static PieceType parsePieceLetter(final char c) {
    return NotationMovingPiece.calculate(NonNullWrapperCommon.toString(c)).getPieceType();
  }

  /** Creates the format-validation exception used for any malformed SAN string. */
  private static SanValidationException invalidFormat() {
    return new SanValidationException(SanValidationProblem.FORMAT,
        Message.getString("validation.san.format.nonSpecific"));
  }

  /**
   * Parses a castling SAN string: {@code "O-O"} (king-side) or {@code "O-O-O"} (queen-side). The optional trailing
   * check/checkmate symbol has already been stripped into {@code checkmateOrCheck}.
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
    throw new SanValidationException(SanValidationProblem.FORMAT_CASTLING,
        Message.getString("validation.san.format.castling"));
  }

  private static SanParse parsePawnMove(final String core, final SanTerminalMarker sanTerminalMarker) {
    // too short
    if (core.length() == 1) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PAWN_LENGTH_GENERAL,
          Message.getString("validation.san.format.pawn.length.general"));
    }

    final var secondChar = core.charAt(1);

    if (isRankDigit(secondChar)) {
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
      return new SanParse(SanType.PAWN_NON_CAPTURING_NON_PROMOTION_MOVE, new SanConversion(File.NONE, Rank.NONE,
          Square.calculate(parseFile(firstChar), parseRank(secondChar)), PromotionPieceType.NONE, sanTerminalMarker));
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
        new SanConversion(File.NONE, Rank.NONE, Square.calculate(parseFile(firstChar), parseRank(secondChar)),
            parsePromotionPiece(fourthChar), sanTerminalMarker));
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
    if (!isFileLetter(thirdChar)) {
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
    if (!isRankDigit(fourthChar)) {
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
      return new SanParse(SanType.PAWN_CAPTURING_NON_PROMOTION_MOVE, new SanConversion(parseFile(firstChar), Rank.NONE,
          Square.calculate(parseFile(thirdChar), parseRank(fourthChar)), PromotionPieceType.NONE, sanTerminalMarker));
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
        new SanConversion(parseFile(firstChar), Rank.NONE,
            Square.calculate(parseFile(thirdChar), parseRank(fourthChar)), parsePromotionPiece(sixthChar),
            sanTerminalMarker));
  }

  /**
   * Parses a king-move SAN (core starts with 'K').
   *
   * <p>
   * Recognised core patterns (trailing +/# already stripped):
   *
   * <pre>
   *   length 3: K[file][rank]   non-castling, non-capturing   e.g. Ke5
   *   length 4: Kx[file][rank]  non-castling, capturing       e.g. Kxe5
   * </pre>
   */
  private static SanParse parseKingMove(final String core, final SanTerminalMarker sanTerminalMarker) {
    return switch (core.length()) {

      case 3 -> {
        // K[toFile][toRank] – non-capturing e.g. "Ke5"
        final var toFileChar = core.charAt(1);
        final var toRankChar = core.charAt(2);
        if (!isFileLetter(toFileChar) || !isRankDigit(toRankChar)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_DESTINATION,
              Message.getString("validation.san.format.king.destination"));
        }
        yield new SanParse(SanType.KING_NON_CASTLING_NON_CAPTURING_MOVE, new SanConversion(File.NONE, Rank.NONE,
            Square.calculate(parseFile(toFileChar), parseRank(toRankChar)), PromotionPieceType.NONE, sanTerminalMarker));
      }

      case 4 -> {
        // Kx[toFile][toRank] – capturing e.g. "Kxe5"
        // But first check for disambiguation patterns (never valid for king)
        final var secondChar4 = core.charAt(1);
        final var thirdChar4 = core.charAt(2);
        final var fourthChar4 = core.charAt(3);
        // K[file][toFile][toRank] — file disambiguation
        if (isFileLetter(secondChar4) && isFileLetter(thirdChar4) && isRankDigit(fourthChar4)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_FILE_SPECIFIED,
              Message.getString("validation.san.format.king.fileSpecified"));
        }
        // K[rank][toFile][toRank] — rank disambiguation
        if (isRankDigit(secondChar4) && isFileLetter(thirdChar4) && isRankDigit(fourthChar4)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_RANK_SPECIFIED,
              Message.getString("validation.san.format.king.rankSpecified"));
        }
        if (secondChar4 != 'x') {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_SECOND_CHARACTER, Message
              .getString("validation.san.format.king.secondCharacter", NonNullWrapperCommon.toString(secondChar4)));
        }
        if (!isFileLetter(thirdChar4) || !isRankDigit(fourthChar4)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_CAPTURE_DESTINATION,
              Message.getString("validation.san.format.king.captureDestination"));
        }
        yield new SanParse(SanType.KING_NON_CASTLING_CAPTURING_MOVE,
            new SanConversion(File.NONE, Rank.NONE, Square.calculate(parseFile(thirdChar4), parseRank(fourthChar4)),
                PromotionPieceType.NONE, sanTerminalMarker));
      }

      case 5 -> {
        // K[file]x[toFile][toRank] or K[rank]x[toFile][toRank] or K[file][rank][toFile][toRank]
        final var secondChar5 = core.charAt(1);
        final var thirdChar5 = core.charAt(2);
        final var fourthChar5 = core.charAt(3);
        final var fifthChar5 = core.charAt(4);
        // K[file]x[toFile][toRank] — file disambiguation + capture
        if (isFileLetter(secondChar5) && thirdChar5 == 'x' && isFileLetter(fourthChar5) && isRankDigit(fifthChar5)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_FILE_SPECIFIED,
              Message.getString("validation.san.format.king.fileSpecified"));
        }
        // K[rank]x[toFile][toRank] — rank disambiguation + capture
        if (isRankDigit(secondChar5) && thirdChar5 == 'x' && isFileLetter(fourthChar5) && isRankDigit(fifthChar5)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_RANK_SPECIFIED,
              Message.getString("validation.san.format.king.rankSpecified"));
        }
        // K[file][rank][toFile][toRank] — square disambiguation
        if (isFileLetter(secondChar5) && isRankDigit(thirdChar5) && isFileLetter(fourthChar5)
            && isRankDigit(fifthChar5)) {
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
        if (isFileLetter(c1) && c2 == 'x' && isFileLetter(c3) && isRankDigit(c4)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_FILE_SPECIFIED,
              Message.getString("validation.san.format.king.fileSpecified"));
        }
        // K2xf3 — rank disambiguation + capture
        if (isRankDigit(c1) && c2 == 'x' && isFileLetter(c3) && isRankDigit(c4)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_RANK_SPECIFIED,
              Message.getString("validation.san.format.king.rankSpecified"));
        }
        // Ka2b3 — square disambiguation, non-capturing
        if (isFileLetter(c1) && isRankDigit(c2) && isFileLetter(c3) && isRankDigit(c4)) {
          throw new SanValidationException(SanValidationProblem.FORMAT_KING_SQUARE_SPECIFIED,
              Message.getString("validation.san.format.king.squareSpecified"));
        }
        // Ka2xb3 — square disambiguation + capture
        if (isFileLetter(c1) && isRankDigit(c2) && c3 == 'x' && isFileLetter(c4) && isRankDigit(c5)) {
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

  /**
   * Parses a non-king piece move SAN (core starts with R, N, B, or Q).
   *
   * <p>
   * Structure: {@code [piece][middle][toFile][toRank]}, where the destination square is always the last two characters
   * and {@code middle} is a 0–3 character sequence encoding optional from-square disambiguation and an optional capture
   * symbol 'x'.
   *
   * <p>
   * Recognised middle patterns by length:
   *
   * <pre>
   *   0: ""              no disambiguation, no capture           e.g. Qe5
   *   1: x               no disambiguation, capture              e.g. Qxe5
   *      [file]          file disambiguation,  no capture        e.g. Qae5
   *      [rank]          rank disambiguation,  no capture        e.g. Q2e5
   *   2: [file]x         file disambiguation,  capture           e.g. Qaxe5
   *      [rank]x         rank disambiguation,  capture           e.g. Q2xe5
   *      [file][rank]    square disambiguation, no capture       e.g. Qc3e5
   *   3: [file][rank]x   square disambiguation, capture          e.g. Qc3xe5
   * </pre>
   *
   * <p>
   * All four piece types (Q, R, N, B) accept every combination above.
   */
  private static SanParse parseRbnqMove(final String core, final SanTerminalMarker sanTerminalMarker) {
    // Valid core lengths: piece(1) + middle(0–3) + destination(2) = 3 to 6
    if (core.length() < 3 || core.length() > 6) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PIECE_LENGTH,
          Message.getString("validation.san.format.piece.length"));
    }

    // The destination square is always the last two characters
    final var toFileChar = core.charAt(core.length() - 2);
    final var toRankChar = core.charAt(core.length() - 1);
    if (!isFileLetter(toFileChar) || !isRankDigit(toRankChar)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PIECE_DESTINATION,
          Message.getString("validation.san.format.piece.destination"));
    }
    final var toSquare = Square.calculate(parseFile(toFileChar), parseRank(toRankChar));
    final var piece = parsePieceLetter(core.charAt(0));
    final var mid = core.length() - 3; // middle length: 0, 1, 2, or 3

    return switch (mid) {

      case 0 ->
          // "Qe5" – no disambiguation, no capture
          new SanParse(pieceMoveSanType(piece, false, false, false),
              new SanConversion(File.NONE, Rank.NONE, toSquare, PromotionPieceType.NONE, sanTerminalMarker));

      case 1 -> {
        final var m = core.charAt(1);
        if (m == 'x') {
          // "Qxe5" – no disambiguation, capture
          yield new SanParse(pieceMoveSanType(piece, false, false, true),
              new SanConversion(File.NONE, Rank.NONE, toSquare, PromotionPieceType.NONE, sanTerminalMarker));
        }
        if (isFileLetter(m)) {
          // "Qae5" – file disambiguation, no capture
          yield new SanParse(pieceMoveSanType(piece, true, false, false),
              new SanConversion(parseFile(m), Rank.NONE, toSquare, PromotionPieceType.NONE, sanTerminalMarker));
        }
        if (isRankDigit(m)) {
          // "Q2e5" – rank disambiguation, no capture
          yield new SanParse(pieceMoveSanType(piece, false, true, false),
              new SanConversion(File.NONE, parseRank(m), toSquare, PromotionPieceType.NONE, sanTerminalMarker));
        }
        throw new SanValidationException(SanValidationProblem.FORMAT_PIECE_MIDDLE,
            Message.getString("validation.san.format.piece.middle", NonNullWrapperCommon.toString(m)));
      }

      case 2 -> {
        final var m0 = core.charAt(1);
        final var m1 = core.charAt(2);
        if (isFileLetter(m0) && m1 == 'x') {
          // "Qaxe5" – file disambiguation, capture
          yield new SanParse(pieceMoveSanType(piece, true, false, true),
              new SanConversion(parseFile(m0), Rank.NONE, toSquare, PromotionPieceType.NONE, sanTerminalMarker));
        }
        if (isRankDigit(m0) && m1 == 'x') {
          // "Q2xe5" – rank disambiguation, capture
          yield new SanParse(pieceMoveSanType(piece, false, true, true),
              new SanConversion(File.NONE, parseRank(m0), toSquare, PromotionPieceType.NONE, sanTerminalMarker));
        }
        if (isFileLetter(m0) && isRankDigit(m1)) {
          // "Qc3e5" – square disambiguation (file + rank), no capture
          yield new SanParse(pieceMoveSanType(piece, true, true, false),
              new SanConversion(parseFile(m0), parseRank(m1), toSquare, PromotionPieceType.NONE, sanTerminalMarker));
        }
        throw new SanValidationException(SanValidationProblem.FORMAT_PIECE_MIDDLE,
            Message.getString("validation.san.format.piece.middle2", NonNullWrapperCommon.toString(m0),
                NonNullWrapperCommon.toString(m1)));
      }

      case 3 -> {
        // "Qc3xe5" – square disambiguation (file + rank), capture
        final var m0 = core.charAt(1);
        final var m1 = core.charAt(2);
        final var m2 = core.charAt(3);
        if (!isFileLetter(m0) || !isRankDigit(m1) || m2 != 'x') {
          throw new SanValidationException(SanValidationProblem.FORMAT_PIECE_MIDDLE,
              Message.getString("validation.san.format.piece.middle3", NonNullWrapperCommon.toString(m0),
                  NonNullWrapperCommon.toString(m1), NonNullWrapperCommon.toString(m2)));
        }
        yield new SanParse(pieceMoveSanType(piece, true, true, true),
            new SanConversion(parseFile(m0), parseRank(m1), toSquare, PromotionPieceType.NONE, sanTerminalMarker));
      }

      default -> throw new ProgrammingMistakeException("Unreachable: mid is exactly 0-3 given the length check above");
    };
  }

  /**
   * Resolves the {@link SanType} for a non-king piece move given the piece type and three boolean flags describing the
   * move's structure.
   *
   * <p>
   * Every non-king piece (Q, R, N, B) supports all eight disambiguation/capture combinations, so this method covers 4 ×
   * 8 = 32 distinct values. The conditions are mutually exclusive and exhaustive; the final block handles
   * {@code hasFromFile && hasFromRank && isCapture}.
   */
  private static SanType pieceMoveSanType(final PieceType piece, final boolean hasFromFile, final boolean hasFromRank,
      final boolean isCapture) {
    if (!hasFromFile && !hasFromRank && !isCapture) {
      return switch (piece) {
        case QUEEN -> SanType.QUEEN_NON_CAPTURING_NEITHER_MOVE;
        case ROOK -> SanType.ROOK_NON_CAPTURING_NEITHER_MOVE;
        case KNIGHT -> SanType.KNIGHT_NON_CAPTURING_NEITHER_MOVE;
        case BISHOP -> SanType.BISHOP_NON_CAPTURING_NEITHER_MOVE;
        default -> throw new ProgrammingMistakeException("Unexpected piece type: " + piece);
      };
    }
    if (hasFromFile && !hasFromRank && !isCapture) {
      return switch (piece) {
        case QUEEN -> SanType.QUEEN_NON_CAPTURING_FILE_MOVE;
        case ROOK -> SanType.ROOK_NON_CAPTURING_FILE_MOVE;
        case KNIGHT -> SanType.KNIGHT_NON_CAPTURING_FILE_MOVE;
        case BISHOP -> SanType.BISHOP_NON_CAPTURING_FILE_MOVE;
        default -> throw new ProgrammingMistakeException("Unexpected piece type: " + piece);
      };
    }
    if (!hasFromFile && hasFromRank && !isCapture) {
      return switch (piece) {
        case QUEEN -> SanType.QUEEN_NON_CAPTURING_RANK_MOVE;
        case ROOK -> SanType.ROOK_NON_CAPTURING_RANK_MOVE;
        case KNIGHT -> SanType.KNIGHT_NON_CAPTURING_RANK_MOVE;
        case BISHOP -> SanType.BISHOP_NON_CAPTURING_RANK_MOVE;
        default -> throw new ProgrammingMistakeException("Unexpected piece type: " + piece);
      };
    }
    if (hasFromFile && hasFromRank && !isCapture) {
      return switch (piece) {
        case QUEEN -> SanType.QUEEN_NON_CAPTURING_SQUARE_MOVE;
        case ROOK -> SanType.ROOK_NON_CAPTURING_SQUARE_MOVE;
        case KNIGHT -> SanType.KNIGHT_NON_CAPTURING_SQUARE_MOVE;
        case BISHOP -> SanType.BISHOP_NON_CAPTURING_SQUARE_MOVE;
        default -> throw new ProgrammingMistakeException("Unexpected piece type: " + piece);
      };
    }
    if (!hasFromFile && !hasFromRank && isCapture) {
      return switch (piece) {
        case QUEEN -> SanType.QUEEN_CAPTURING_NEITHER_MOVE;
        case ROOK -> SanType.ROOK_CAPTURING_NEITHER_MOVE;
        case KNIGHT -> SanType.KNIGHT_CAPTURING_NEITHER_MOVE;
        case BISHOP -> SanType.BISHOP_CAPTURING_NEITHER_MOVE;
        default -> throw new ProgrammingMistakeException("Unexpected piece type: " + piece);
      };
    }
    if (hasFromFile && !hasFromRank && isCapture) {
      return switch (piece) {
        case QUEEN -> SanType.QUEEN_CAPTURING_FILE_MOVE;
        case ROOK -> SanType.ROOK_CAPTURING_FILE_MOVE;
        case KNIGHT -> SanType.KNIGHT_CAPTURING_FILE_MOVE;
        case BISHOP -> SanType.BISHOP_CAPTURING_FILE_MOVE;
        default -> throw new ProgrammingMistakeException("Unexpected piece type: " + piece);
      };
    }
    if (!hasFromFile && hasFromRank && isCapture) {
      return switch (piece) {
        case QUEEN -> SanType.QUEEN_CAPTURING_RANK_MOVE;
        case ROOK -> SanType.ROOK_CAPTURING_RANK_MOVE;
        case KNIGHT -> SanType.KNIGHT_CAPTURING_RANK_MOVE;
        case BISHOP -> SanType.BISHOP_CAPTURING_RANK_MOVE;
        default -> throw new ProgrammingMistakeException("Unexpected piece type: " + piece);
      };
    }
    // hasFromFile && hasFromRank && isCapture
    return switch (piece) {
      case QUEEN -> SanType.QUEEN_CAPTURING_SQUARE_MOVE;
      case ROOK -> SanType.ROOK_CAPTURING_SQUARE_MOVE;
      case KNIGHT -> SanType.KNIGHT_CAPTURING_SQUARE_MOVE;
      case BISHOP -> SanType.BISHOP_CAPTURING_SQUARE_MOVE;
      default -> throw new ProgrammingMistakeException("Unexpected piece type: " + piece);
    };
  }

}
