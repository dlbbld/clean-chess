package com.dlb.chess.san.validate.format;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.NotationMovingPiece;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.enums.SanTerminalMarker;
import com.dlb.chess.san.enums.SanType;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanParse;

/**
 * Parses a non-king piece move SAN (core starts with R, N, B, or Q) sequentially, character by character.
 *
 * <p>
 * Recognised forms:
 *
 * <pre>
 *   Length 3:  R[toFile][toRank]                   — no disambiguation, no capture  e.g. Ra1
 *   Length 4:  R[fromFile][toFile][toRank]         — file disambiguation           e.g. Rba1
 *              R[fromRank][toFile][toRank]         — rank disambiguation           e.g. R2a1
 *              Rx[toFile][toRank]                  — capture, no disambiguation    e.g. Rxa1
 *   Length 5:  R[fromFile][fromRank][toFile][toRank] — square disambiguation       e.g. Rb2a1
 *              R[fromFile]x[toFile][toRank]        — file disambiguation + capture e.g. Rbxa1
 *              R[fromRank]x[toFile][toRank]        — rank disambiguation + capture e.g. R2xa1
 *   Length 6:  R[fromFile][fromRank]x[toFile][toRank] — square + capture           e.g. Rb2xa1
 * </pre>
 *
 * <p>
 * The parser does a character-by-character walk and branches at each point where the grammar is ambiguous. It never
 * commits to a disambiguation form until the next character makes the branch unambiguous, or until the SAN ends and
 * the shorter valid form is complete. Each failure is reported with the most specific
 * {@link SanValidationProblem} describing which character is missing or wrong.
 */
abstract class SanValidateFormatRbnq extends AbstractSan {

  static SanParse parseRbnqMove(final String core, final SanTerminalMarker sanTerminalMarker) {
    // core[0] is the piece letter, already validated to be R/N/B/Q by the dispatcher in SanValidateFormat.
    final var piece = parsePieceLetter(core.charAt(0));

    // pos 1: second character — file letter, rank digit, or the capture symbol 'x'
    if (core.length() == 1) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_NO_SECOND_CHARACTER,
          Message.getString("validation.san.format.rnbq.noSecondCharacter"));
    }
    final var c1 = core.charAt(1);

    if (c1 == 'x') {
      return parseCaptureNoDisambig(core, sanTerminalMarker, piece);
    }
    if (SanValidateFormat.isRankDigit(c1)) {
      return parseRankBranch(core, sanTerminalMarker, piece, SanValidateFormat.parseRank(c1));
    }
    if (SanValidateFormat.isFileLetter(c1)) {
      return parseFileBranch(core, sanTerminalMarker, piece, SanValidateFormat.parseFile(c1));
    }
    throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_WRONG_SECOND_CHARACTER,
        Message.getString("validation.san.format.rnbq.wrongSecondCharacter", NonNullWrapperCommon.toString(c1)));
  }

  // ---------------------------------------------------------------------------
  // Rx[toFile][toRank] — capture, no disambiguation
  // ---------------------------------------------------------------------------

  private static SanParse parseCaptureNoDisambig(final String core, final SanTerminalMarker sanTerminalMarker,
      final PieceType piece) {
    // pos 2: destination file
    if (core.length() == 2) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_NO_DESTINATION_FILE,
          Message.getString("validation.san.format.rnbq.capture.noDestinationFile"));
    }
    final var c2 = core.charAt(2);
    if (!SanValidateFormat.isFileLetter(c2)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_WRONG_DESTINATION_FILE,
          Message.getString("validation.san.format.rnbq.capture.wrongDestinationFile",
              NonNullWrapperCommon.toString(c2)));
    }

    // pos 3: destination rank
    if (core.length() == 3) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_NO_DESTINATION_RANK,
          Message.getString("validation.san.format.rnbq.capture.noDestinationRank"));
    }
    final var c3 = core.charAt(3);
    if (!SanValidateFormat.isRankDigit(c3)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_WRONG_DESTINATION_RANK,
          Message.getString("validation.san.format.rnbq.capture.wrongDestinationRank",
              NonNullWrapperCommon.toString(c3)));
    }

    // Length 4 is the exact valid form; anything longer is overlength.
    if (core.length() > 4) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_OVERLENGTH,
          Message.getString("validation.san.format.rnbq.capture.overlength"));
    }

    final var toSquare = Square.calculate(SanValidateFormat.parseFile(c2), SanValidateFormat.parseRank(c3));
    return new SanParse(pieceMoveSanType(piece, false, false, true),
        new SanConversion(File.NONE, Rank.NONE, toSquare, PromotionPieceType.NONE, sanTerminalMarker));
  }

  // ---------------------------------------------------------------------------
  // R[rank]... — rank branch (at pos 2 we need 'x' for capture or a file letter for non-capture)
  // ---------------------------------------------------------------------------

  private static SanParse parseRankBranch(final String core, final SanTerminalMarker sanTerminalMarker,
      final PieceType piece, final Rank fromRank) {
    if (core.length() == 2) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_RANK_NO_THIRD_CHARACTER,
          Message.getString("validation.san.format.rnbq.rank.noThirdCharacter"));
    }
    final var c2 = core.charAt(2);

    if (c2 == 'x') {
      return parseCaptureRank(core, sanTerminalMarker, piece, fromRank);
    }
    if (SanValidateFormat.isFileLetter(c2)) {
      return parseNonCaptureRank(core, sanTerminalMarker, piece, fromRank, SanValidateFormat.parseFile(c2));
    }
    throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_RANK_WRONG_THIRD_CHARACTER,
        Message.getString("validation.san.format.rnbq.rank.wrongThirdCharacter", NonNullWrapperCommon.toString(c2)));
  }

  // R[rank][toFile][toRank] — non-capture rank disambiguation (e.g. R2a1)
  private static SanParse parseNonCaptureRank(final String core, final SanTerminalMarker sanTerminalMarker,
      final PieceType piece, final Rank fromRank, final File toFile) {
    // pos 3: destination rank
    if (core.length() == 3) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_NON_CAPTURE_RANK_NO_DESTINATION_RANK,
          Message.getString("validation.san.format.rnbq.nonCapture.rank.noDestinationRank"));
    }
    final var c3 = core.charAt(3);
    if (!SanValidateFormat.isRankDigit(c3)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_NON_CAPTURE_RANK_WRONG_DESTINATION_RANK,
          Message.getString("validation.san.format.rnbq.nonCapture.rank.wrongDestinationRank",
              NonNullWrapperCommon.toString(c3)));
    }

    if (core.length() > 4) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_NON_CAPTURE_RANK_OVERLENGTH,
          Message.getString("validation.san.format.rnbq.nonCapture.rank.overlength"));
    }

    final var toSquare = Square.calculate(toFile, SanValidateFormat.parseRank(c3));
    return new SanParse(pieceMoveSanType(piece, false, true, false),
        new SanConversion(File.NONE, fromRank, toSquare, PromotionPieceType.NONE, sanTerminalMarker));
  }

  // R[rank]x[toFile][toRank] — capture rank disambiguation (e.g. R2xa1)
  private static SanParse parseCaptureRank(final String core, final SanTerminalMarker sanTerminalMarker,
      final PieceType piece, final Rank fromRank) {
    // pos 3: destination file
    if (core.length() == 3) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_RANK_NO_DESTINATION_FILE,
          Message.getString("validation.san.format.rnbq.capture.rank.noDestinationFile"));
    }
    final var c3 = core.charAt(3);
    if (!SanValidateFormat.isFileLetter(c3)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_RANK_WRONG_DESTINATION_FILE,
          Message.getString("validation.san.format.rnbq.capture.rank.wrongDestinationFile",
              NonNullWrapperCommon.toString(c3)));
    }

    // pos 4: destination rank
    if (core.length() == 4) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_RANK_NO_DESTINATION_RANK,
          Message.getString("validation.san.format.rnbq.capture.rank.noDestinationRank"));
    }
    final var c4 = core.charAt(4);
    if (!SanValidateFormat.isRankDigit(c4)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_RANK_WRONG_DESTINATION_RANK,
          Message.getString("validation.san.format.rnbq.capture.rank.wrongDestinationRank",
              NonNullWrapperCommon.toString(c4)));
    }

    if (core.length() > 5) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_RANK_OVERLENGTH,
          Message.getString("validation.san.format.rnbq.capture.rank.overlength"));
    }

    final var toSquare = Square.calculate(SanValidateFormat.parseFile(c3), SanValidateFormat.parseRank(c4));
    return new SanParse(pieceMoveSanType(piece, false, true, true),
        new SanConversion(File.NONE, fromRank, toSquare, PromotionPieceType.NONE, sanTerminalMarker));
  }

  // ---------------------------------------------------------------------------
  // R[file]... — file branch (3-way ambiguity at pos 2: rank=toRank, file=fromFile, x=capture)
  // ---------------------------------------------------------------------------

  private static SanParse parseFileBranch(final String core, final SanTerminalMarker sanTerminalMarker,
      final PieceType piece, final File firstFile) {
    if (core.length() == 2) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_FILE_NO_THIRD_CHARACTER,
          Message.getString("validation.san.format.rnbq.file.noThirdCharacter"));
    }
    final var c2 = core.charAt(2);

    if (c2 == 'x') {
      return parseCaptureFile(core, sanTerminalMarker, piece, firstFile);
    }
    if (SanValidateFormat.isFileLetter(c2)) {
      return parseNonCaptureFile(core, sanTerminalMarker, piece, firstFile, SanValidateFormat.parseFile(c2));
    }
    if (SanValidateFormat.isRankDigit(c2)) {
      return parseFileRankBranch(core, sanTerminalMarker, piece, firstFile, SanValidateFormat.parseRank(c2));
    }
    throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_FILE_WRONG_THIRD_CHARACTER,
        Message.getString("validation.san.format.rnbq.file.wrongThirdCharacter", NonNullWrapperCommon.toString(c2)));
  }

  // R[fromFile][toFile][toRank] — non-capture file disambiguation (e.g. Rba1)
  private static SanParse parseNonCaptureFile(final String core, final SanTerminalMarker sanTerminalMarker,
      final PieceType piece, final File fromFile, final File toFile) {
    // pos 3: destination rank
    if (core.length() == 3) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_NON_CAPTURE_FILE_NO_DESTINATION_RANK,
          Message.getString("validation.san.format.rnbq.nonCapture.file.noDestinationRank"));
    }
    final var c3 = core.charAt(3);
    if (!SanValidateFormat.isRankDigit(c3)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_NON_CAPTURE_FILE_WRONG_DESTINATION_RANK,
          Message.getString("validation.san.format.rnbq.nonCapture.file.wrongDestinationRank",
              NonNullWrapperCommon.toString(c3)));
    }

    if (core.length() > 4) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_NON_CAPTURE_FILE_OVERLENGTH,
          Message.getString("validation.san.format.rnbq.nonCapture.file.overlength"));
    }

    final var toSquare = Square.calculate(toFile, SanValidateFormat.parseRank(c3));
    return new SanParse(pieceMoveSanType(piece, true, false, false),
        new SanConversion(fromFile, Rank.NONE, toSquare, PromotionPieceType.NONE, sanTerminalMarker));
  }

  // R[fromFile]x[toFile][toRank] — capture file disambiguation (e.g. Rbxa1)
  private static SanParse parseCaptureFile(final String core, final SanTerminalMarker sanTerminalMarker,
      final PieceType piece, final File fromFile) {
    // pos 3: destination file
    if (core.length() == 3) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_FILE_NO_DESTINATION_FILE,
          Message.getString("validation.san.format.rnbq.capture.file.noDestinationFile"));
    }
    final var c3 = core.charAt(3);
    if (!SanValidateFormat.isFileLetter(c3)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_FILE_WRONG_DESTINATION_FILE,
          Message.getString("validation.san.format.rnbq.capture.file.wrongDestinationFile",
              NonNullWrapperCommon.toString(c3)));
    }

    // pos 4: destination rank
    if (core.length() == 4) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_FILE_NO_DESTINATION_RANK,
          Message.getString("validation.san.format.rnbq.capture.file.noDestinationRank"));
    }
    final var c4 = core.charAt(4);
    if (!SanValidateFormat.isRankDigit(c4)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_FILE_WRONG_DESTINATION_RANK,
          Message.getString("validation.san.format.rnbq.capture.file.wrongDestinationRank",
              NonNullWrapperCommon.toString(c4)));
    }

    if (core.length() > 5) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_FILE_OVERLENGTH,
          Message.getString("validation.san.format.rnbq.capture.file.overlength"));
    }

    final var toSquare = Square.calculate(SanValidateFormat.parseFile(c3), SanValidateFormat.parseRank(c4));
    return new SanParse(pieceMoveSanType(piece, true, false, true),
        new SanConversion(fromFile, Rank.NONE, toSquare, PromotionPieceType.NONE, sanTerminalMarker));
  }

  // ---------------------------------------------------------------------------
  // R[file][rank]... — ambiguous between plain destination (Ra1) and source-square prefix (Rb2a1 / Rb2xa1)
  // ---------------------------------------------------------------------------

  private static SanParse parseFileRankBranch(final String core, final SanTerminalMarker sanTerminalMarker,
      final PieceType piece, final File firstFile, final Rank firstRank) {
    // Length 3: interpret as plain destination Ra1 — firstFile/firstRank are the destination square.
    if (core.length() == 3) {
      final var toSquare = Square.calculate(firstFile, firstRank);
      return new SanParse(pieceMoveSanType(piece, false, false, false),
          new SanConversion(File.NONE, Rank.NONE, toSquare, PromotionPieceType.NONE, sanTerminalMarker));
    }

    // Length > 3: commit to source-square interpretation — firstFile/firstRank become the source square,
    // and pos 3 must be either the destination file (non-capture) or 'x' (capture).
    final var c3 = core.charAt(3);

    if (c3 == 'x') {
      return parseCaptureSquare(core, sanTerminalMarker, piece, firstFile, firstRank);
    }
    if (SanValidateFormat.isFileLetter(c3)) {
      return parseNonCaptureSquare(core, sanTerminalMarker, piece, firstFile, firstRank, SanValidateFormat.parseFile(c3));
    }
    throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_SQUARE_WRONG_THIRD_CHARACTER,
        Message.getString("validation.san.format.rnbq.square.wrongThirdCharacter", NonNullWrapperCommon.toString(c3)));
  }

  // R[fromFile][fromRank][toFile][toRank] — non-capture square disambiguation (e.g. Rb2a1)
  private static SanParse parseNonCaptureSquare(final String core, final SanTerminalMarker sanTerminalMarker,
      final PieceType piece, final File fromFile, final Rank fromRank, final File toFile) {
    // pos 4: destination rank
    if (core.length() == 4) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_NON_CAPTURE_SQUARE_NO_DESTINATION_RANK,
          Message.getString("validation.san.format.rnbq.nonCapture.square.noDestinationRank"));
    }
    final var c4 = core.charAt(4);
    if (!SanValidateFormat.isRankDigit(c4)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_NON_CAPTURE_SQUARE_WRONG_DESTINATION_RANK,
          Message.getString("validation.san.format.rnbq.nonCapture.square.wrongDestinationRank",
              NonNullWrapperCommon.toString(c4)));
    }

    if (core.length() > 5) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_NON_CAPTURE_SQUARE_OVERLENGTH,
          Message.getString("validation.san.format.rnbq.nonCapture.square.overlength"));
    }

    final var toSquare = Square.calculate(toFile, SanValidateFormat.parseRank(c4));
    return new SanParse(pieceMoveSanType(piece, true, true, false),
        new SanConversion(fromFile, fromRank, toSquare, PromotionPieceType.NONE, sanTerminalMarker));
  }

  // R[fromFile][fromRank]x[toFile][toRank] — capture square disambiguation (e.g. Rb2xa1)
  private static SanParse parseCaptureSquare(final String core, final SanTerminalMarker sanTerminalMarker,
      final PieceType piece, final File fromFile, final Rank fromRank) {
    // pos 4: destination file
    if (core.length() == 4) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_SQUARE_NO_DESTINATION_FILE,
          Message.getString("validation.san.format.rnbq.capture.square.noDestinationFile"));
    }
    final var c4 = core.charAt(4);
    if (!SanValidateFormat.isFileLetter(c4)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_SQUARE_WRONG_DESTINATION_FILE,
          Message.getString("validation.san.format.rnbq.capture.square.wrongDestinationFile",
              NonNullWrapperCommon.toString(c4)));
    }

    // pos 5: destination rank
    if (core.length() == 5) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_SQUARE_NO_DESTINATION_RANK,
          Message.getString("validation.san.format.rnbq.capture.square.noDestinationRank"));
    }
    final var c5 = core.charAt(5);
    if (!SanValidateFormat.isRankDigit(c5)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_SQUARE_WRONG_DESTINATION_RANK,
          Message.getString("validation.san.format.rnbq.capture.square.wrongDestinationRank",
              NonNullWrapperCommon.toString(c5)));
    }

    if (core.length() > 6) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_CAPTURE_SQUARE_OVERLENGTH,
          Message.getString("validation.san.format.rnbq.capture.square.overlength"));
    }

    final var toSquare = Square.calculate(SanValidateFormat.parseFile(c4), SanValidateFormat.parseRank(c5));
    return new SanParse(pieceMoveSanType(piece, true, true, true),
        new SanConversion(fromFile, fromRank, toSquare, PromotionPieceType.NONE, sanTerminalMarker));
  }

  private static PieceType parsePieceLetter(final char c) {
    return NotationMovingPiece.calculate(c).getPieceType();
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
