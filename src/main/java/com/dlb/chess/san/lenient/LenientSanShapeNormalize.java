package com.dlb.chess.san.lenient;

import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ChessBoard;
import com.dlb.chess.messages.Message;
import com.dlb.chess.san.enums.LenientSanValidationProblem;
import com.dlb.chess.san.exceptions.LenientSanParserValidationException;
import com.dlb.chess.san.model.ForgivenItem;

/**
 * Phase 1 of the lenient pipeline: pure-string shape normalization plus board-aware UCI translation. Takes the raw user
 * input and produces a candidate SAN string in (or close to) canonical form, accumulating one
 * {@link LenientSanValidationProblem} per detected deviation. The candidate is then handed to Phase 2
 * ({@link LenientSanRecover}) for board-aware semantic recovery.
 *
 * <p>
 * Order of normalization is significant — earlier steps assume later steps have not yet run. The order is:
 * <ol>
 * <li>Castling shape (zero-vs-O, mixed-rejection, UCI castling)
 * <li>Strip terminal marker for body-only processing, re-attach at the end
 * <li>UCI piece-move translation (board-aware lookup of the piece on the from-square)
 * <li>Body transforms: explicit-P strip, LAN hyphen strip, missing promotion-equals insert
 * <li>Case fixups (lowercase piece letter, uppercase file letter, uppercase capture marker, lowercase promotion piece)
 * </ol>
 */
final class LenientSanShapeNormalize {

  private LenientSanShapeNormalize() {
  }

  static String normalize(String text, ChessBoard board, List<LenientSanValidationProblem> codes) {
    if (text.isBlank()) {
      // Defer to strict for the canonical "blank" diagnostic.
      return text;
    }

    final @Nullable String castlingNormalized = tryNormalizeCastling(text, board, codes);
    if (castlingNormalized != null) {
      return castlingNormalized;
    }

    final char lastChar = text.charAt(text.length() - 1);
    final boolean hasTerminalMarker = lastChar == '+' || lastChar == '#';
    final String terminalMarker = hasTerminalMarker ? NonNullWrapperCommon.valueOf(lastChar) : "";
    String body = hasTerminalMarker ? NonNullWrapperCommon.substring(text, 0, text.length() - 1) : text;

    body = stripExplicitPawnLetter(body, codes);
    body = handleLanOrUci(body, board, codes);
    body = insertMissingPromotionEquals(body, codes);
    body = applyCaseFixups(body, codes);

    return body + terminalMarker;
  }

  // --- Castling normalization (returns null if the input doesn't match a castling shape) ---

  private static @Nullable String tryNormalizeCastling(String text, ChessBoard board,
      List<LenientSanValidationProblem> codes) {
    final char lastChar = text.charAt(text.length() - 1);
    final boolean hasTerminalMarker = lastChar == '+' || lastChar == '#';
    final String marker = hasTerminalMarker ? NonNullWrapperCommon.valueOf(lastChar) : "";
    final String body = hasTerminalMarker ? NonNullWrapperCommon.substring(text, 0, text.length() - 1) : text;

    // Classic O-O / 0-0 / mixed forms. Three or five characters with hyphens and zero-or-O at every other position.
    if (matchesCastlingZeroOPattern(body)) {
      final boolean hasO = body.indexOf('O') >= 0;
      final boolean hasZero = body.indexOf('0') >= 0;
      if (hasO && hasZero) {
        throw new LenientSanParserValidationException(
            Message.getString("validation.san.lenient.mixedCastlingZeroAndO", text), text, null,
            NonNullWrapperCommon.copyOfList(List.<ForgivenItem>of()));
      }
      if (hasZero) {
        codes.add(LenientSanValidationProblem.ZERO_INSTEAD_OF_O_CASTLING);
        return NonNullWrapperCommon.replace(body, '0', 'O') + marker;
      }
      // All-O: already canonical (or canonical with marker) — let strict handle it.
      return text;
    }

    // UCI king-castling: e1g1 / e1c1 / e8g8 / e8c8 with the king actually on the e-file home square.
    final @Nullable String uciCastling = tryTranslateUciCastling(body, board);
    if (uciCastling != null) {
      codes.add(LenientSanValidationProblem.UCI_NOTATION);
      return uciCastling + marker;
    }

    return null;
  }

  private static boolean matchesCastlingZeroOPattern(String body) {
    if (body.length() == 3) {
      return isZeroOrO(body.charAt(0)) && body.charAt(1) == '-' && isZeroOrO(body.charAt(2));
    }
    if (body.length() == 5) {
      return isZeroOrO(body.charAt(0)) && body.charAt(1) == '-' && isZeroOrO(body.charAt(2)) && body.charAt(3) == '-'
          && isZeroOrO(body.charAt(4));
    }
    return false;
  }

  private static boolean isZeroOrO(char c) {
    return c == '0' || c == 'O';
  }

  private static @Nullable String tryTranslateUciCastling(String body, ChessBoard board) {
    if (body.length() != 4) {
      return null;
    }
    if (!isFileLetter(body.charAt(0)) || !isRankDigit(body.charAt(1)) || !isFileLetter(body.charAt(2))
        || !isRankDigit(body.charAt(3))) {
      return null;
    }
    final Square from = parseSquare(body.charAt(0), body.charAt(1));
    final Square to = parseSquare(body.charAt(2), body.charAt(3));
    if (from == Square.NONE || to == Square.NONE) {
      return null;
    }
    final StaticPosition position = board.getStaticPosition();
    final Piece piece = position.get(from);
    if (piece == Piece.NONE || piece.getPieceType() != PieceType.KING) {
      return null;
    }
    if (from == Square.E1 && to == Square.G1) {
      return "O-O";
    }
    if (from == Square.E1 && to == Square.C1) {
      return "O-O-O";
    }
    if (from == Square.E8 && to == Square.G8) {
      return "O-O";
    }
    if (from == Square.E8 && to == Square.C8) {
      return "O-O-O";
    }
    return null;
  }

  // --- LAN / UCI handling (mutually exclusive at the input level) ---
  //
  // LAN-with-hyphen ("e2-e4", "Nb1-d7") and UCI ("e2e4", "b1d7") use overlapping pure-string shapes; without
  // the hyphen, pawn LAN is identical to pawn UCI. We disambiguate by the presence of a hyphen in the input:
  // a hyphen means LAN (only LONG_ALGEBRAIC_NOTATION code); no hyphen means UCI shape (only UCI_NOTATION code).
  // Either branch may then translate to canonical SAN; the translation itself does not emit a second code.

  private static String handleLanOrUci(String body, ChessBoard board, List<LenientSanValidationProblem> codes) {
    if (body.indexOf('-') >= 0) {
      final String dehyphenated = NonNullWrapperCommon.replace(body, "-", "");
      codes.add(LenientSanValidationProblem.LONG_ALGEBRAIC_NOTATION);
      final @Nullable String translated = tryTranslateUciShape(dehyphenated, board);
      return translated != null ? translated : dehyphenated;
    }
    final @Nullable String translated = tryTranslateUciShape(body, board);
    if (translated != null) {
      codes.add(LenientSanValidationProblem.UCI_NOTATION);
      return translated;
    }
    return body;
  }

  /**
   * Translates a UCI-shape string (4 or 5 chars, no piece letter, file/rank/file/rank/optional-promotion) to canonical
   * SAN using the piece on the from-square. Returns {@code null} when the shape doesn't match — the caller decides
   * whether that means "leave the body unchanged" or "this branch doesn't apply."
   */
  private static @Nullable String tryTranslateUciShape(String body, ChessBoard board) {
    if (body.length() != 4 && body.length() != 5) {
      return null;
    }
    if (!isFileLetter(body.charAt(0)) || !isRankDigit(body.charAt(1)) || !isFileLetter(body.charAt(2))
        || !isRankDigit(body.charAt(3))) {
      return null;
    }
    if (body.length() == 5 && !isPromotionPieceLetterAnyCase(body.charAt(4))) {
      return null;
    }
    final Square from = parseSquare(body.charAt(0), body.charAt(1));
    final Square to = parseSquare(body.charAt(2), body.charAt(3));
    if (from == Square.NONE || to == Square.NONE || from == to) {
      return null;
    }
    final StaticPosition position = board.getStaticPosition();
    final Piece piece = position.get(from);
    if (piece == Piece.NONE) {
      return null;
    }
    final PieceType pieceType = piece.getPieceType();

    final StringBuilder out = new StringBuilder();
    if (pieceType == PieceType.PAWN) {
      // Pawn SAN: forward "<file><rank>", capture "<fromFile>x<toFile><toRank>", optional "=<P>"
      final boolean isCapture = body.charAt(0) != body.charAt(2);
      if (isCapture) {
        out.append(body.charAt(0)).append('x').append(body.charAt(2)).append(body.charAt(3));
      } else {
        out.append(body.charAt(2)).append(body.charAt(3));
      }
      if (body.length() == 5) {
        out.append('=').append(Character.toUpperCase(body.charAt(4)));
      }
    } else {
      // Non-pawn SAN with full square disambiguation. The destination capture-marker is unknown without
      // walking destination semantics; let Phase 2 add it via MISSING_CAPTURE_MARKER if needed.
      out.append(pieceType.getLetter());
      out.append(body.charAt(0)).append(body.charAt(1));
      out.append(body.charAt(2)).append(body.charAt(3));
    }
    return NonNullWrapperCommon.toString(out);
  }

  // --- Body transforms ---

  private static String stripExplicitPawnLetter(String body, List<LenientSanValidationProblem> codes) {
    if (body.length() < 2 || body.charAt(0) != 'P') {
      return body;
    }
    if (!isFileLetter(body.charAt(1))) {
      return body;
    }
    codes.add(LenientSanValidationProblem.EXPLICIT_PAWN_LETTER);
    return NonNullWrapperCommon.substring(body, 1);
  }

  private static String insertMissingPromotionEquals(String body, List<LenientSanValidationProblem> codes) {
    final int len = body.length();
    if (len < 3) {
      return body;
    }
    final char last = body.charAt(len - 1);
    if (!isPromotionPieceLetterAnyCase(last)) {
      return body;
    }
    final char prev = body.charAt(len - 2);
    if (prev != '1' && prev != '8') {
      return body;
    }
    if (body.charAt(len - 3) == '=') {
      return body;
    }
    codes.add(LenientSanValidationProblem.MISSING_PROMOTION_EQUALS);
    return NonNullWrapperCommon.substring(body, 0, len - 1) + "=" + last;
  }

  private static String applyCaseFixups(String body, List<LenientSanValidationProblem> codes) {
    String result = body;
    result = caseFixUppercaseCaptureMarker(result, codes);
    result = caseFixLowercasePromotionPiece(result, codes);
    result = caseFixLowercasePieceLetter(result, codes);
    result = caseFixUppercaseFileLetters(result, codes);
    return result;
  }

  private static String caseFixUppercaseCaptureMarker(String body, List<LenientSanValidationProblem> codes) {
    if (body.indexOf('X') < 0) {
      return body;
    }
    codes.add(LenientSanValidationProblem.UPPERCASE_CAPTURE_MARKER);
    return NonNullWrapperCommon.replace(body, 'X', 'x');
  }

  private static String caseFixLowercasePromotionPiece(String body, List<LenientSanValidationProblem> codes) {
    final int eq = body.lastIndexOf('=');
    if (eq < 0 || eq != body.length() - 2) {
      return body;
    }
    final char piece = body.charAt(eq + 1);
    if (piece == 'r' || piece == 'n' || piece == 'b' || piece == 'q') {
      codes.add(LenientSanValidationProblem.LOWERCASE_PROMOTION_PIECE);
      return NonNullWrapperCommon.substring(body, 0, eq + 1) + Character.toUpperCase(piece);
    }
    return body;
  }

  private static String caseFixLowercasePieceLetter(String body, List<LenientSanValidationProblem> codes) {
    if (body.isEmpty()) {
      return body;
    }
    final char first = body.charAt(0);
    if (first == 'n' || first == 'r' || first == 'q' || first == 'k') {
      codes.add(LenientSanValidationProblem.LOWERCASE_PIECE_LETTER);
      return Character.toUpperCase(first) + NonNullWrapperCommon.substring(body, 1);
    }
    if (first == 'b') {
      // Ambiguous: 'b' is both a file letter (canonical pawn move) and a lowercase bishop letter.
      // We get here only because Phase 0 (raw strict) already failed, so the input is not a valid pawn move.
      // Treat as bishop and case-fold.
      codes.add(LenientSanValidationProblem.LOWERCASE_PIECE_LETTER);
      return "B" + NonNullWrapperCommon.substring(body, 1);
    }
    return body;
  }

  private static String caseFixUppercaseFileLetters(String body, List<LenientSanValidationProblem> codes) {
    final StringBuilder out = new StringBuilder(body.length());
    boolean changed = false;
    for (var i = 0; i < body.length(); i++) {
      final char c = body.charAt(i);
      // First character: piece letter is canonically uppercase, never a file letter; skip case fix here.
      if (i == 0) {
        out.append(c);
        continue;
      }
      if (isUppercaseFileLetter(c)) {
        out.append(Character.toLowerCase(c));
        changed = true;
      } else {
        out.append(c);
      }
    }
    if (changed) {
      codes.add(LenientSanValidationProblem.UPPERCASE_FILE_LETTER);
      return NonNullWrapperCommon.toString(out);
    }
    return body;
  }

  // --- Character helpers ---

  private static boolean isFileLetter(char c) {
    return c >= 'a' && c <= 'h';
  }

  private static boolean isRankDigit(char c) {
    return c >= '1' && c <= '8';
  }

  private static boolean isUppercaseFileLetter(char c) {
    return c >= 'A' && c <= 'H';
  }

  private static boolean isPromotionPieceLetterAnyCase(char c) {
    return c == 'R' || c == 'N' || c == 'B' || c == 'Q' || c == 'r' || c == 'n' || c == 'b' || c == 'q';
  }

  private static Square parseSquare(char fileLetter, char rankDigit) {
    try {
      return Square.valueOf(NonNullWrapperCommon.toUpperCase("" + fileLetter + rankDigit));
    } catch (IllegalArgumentException e) {
      throw new ProgrammingMistakeException("Square.valueOf failed for validated file/rank chars", e);
    }
  }
}
