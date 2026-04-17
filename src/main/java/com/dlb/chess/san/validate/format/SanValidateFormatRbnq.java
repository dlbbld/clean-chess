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
abstract class SanValidateFormatRbnq extends AbstractSan {

  static SanParse parseRbnqMove(final String core, final SanTerminalMarker sanTerminalMarker) {
    // Valid core lengths: piece(1) + middle(0–3) + destination(2) = 3 to 6
    if (core.length() < 3 || core.length() > 6) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PIECE_LENGTH,
          Message.getString("validation.san.format.piece.length"));
    }

    // The destination square is always the last two characters
    final var toFileChar = core.charAt(core.length() - 2);
    final var toRankChar = core.charAt(core.length() - 1);
    if (!SanValidateFormat.isFileLetter(toFileChar) || !SanValidateFormat.isRankDigit(toRankChar)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_PIECE_DESTINATION,
          Message.getString("validation.san.format.piece.destination"));
    }
    final var toSquare = Square.calculate(SanValidateFormat.parseFile(toFileChar),
        SanValidateFormat.parseRank(toRankChar));
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
        if (SanValidateFormat.isFileLetter(m)) {
          // "Qae5" – file disambiguation, no capture
          yield new SanParse(pieceMoveSanType(piece, true, false, false), new SanConversion(
              SanValidateFormat.parseFile(m), Rank.NONE, toSquare, PromotionPieceType.NONE, sanTerminalMarker));
        }
        if (SanValidateFormat.isRankDigit(m)) {
          // "Q2e5" – rank disambiguation, no capture
          yield new SanParse(pieceMoveSanType(piece, false, true, false), new SanConversion(File.NONE,
              SanValidateFormat.parseRank(m), toSquare, PromotionPieceType.NONE, sanTerminalMarker));
        }
        throw new SanValidationException(SanValidationProblem.FORMAT_PIECE_MIDDLE,
            Message.getString("validation.san.format.piece.middle", NonNullWrapperCommon.toString(m)));
      }

      case 2 -> {
        final var m0 = core.charAt(1);
        final var m1 = core.charAt(2);
        if (SanValidateFormat.isFileLetter(m0) && m1 == 'x') {
          // "Qaxe5" – file disambiguation, capture
          yield new SanParse(pieceMoveSanType(piece, true, false, true), new SanConversion(
              SanValidateFormat.parseFile(m0), Rank.NONE, toSquare, PromotionPieceType.NONE, sanTerminalMarker));
        }
        if (SanValidateFormat.isRankDigit(m0) && m1 == 'x') {
          // "Q2xe5" – rank disambiguation, capture
          yield new SanParse(pieceMoveSanType(piece, false, true, true), new SanConversion(File.NONE,
              SanValidateFormat.parseRank(m0), toSquare, PromotionPieceType.NONE, sanTerminalMarker));
        }
        if (SanValidateFormat.isFileLetter(m0) && SanValidateFormat.isRankDigit(m1)) {
          // "Qc3e5" – square disambiguation (file + rank), no capture
          yield new SanParse(pieceMoveSanType(piece, true, true, false),
              new SanConversion(SanValidateFormat.parseFile(m0), SanValidateFormat.parseRank(m1), toSquare,
                  PromotionPieceType.NONE, sanTerminalMarker));
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
        if (!SanValidateFormat.isFileLetter(m0) || !SanValidateFormat.isRankDigit(m1) || m2 != 'x') {
          throw new SanValidationException(SanValidationProblem.FORMAT_PIECE_MIDDLE,
              Message.getString("validation.san.format.piece.middle3", NonNullWrapperCommon.toString(m0),
                  NonNullWrapperCommon.toString(m1), NonNullWrapperCommon.toString(m2)));
        }
        yield new SanParse(pieceMoveSanType(piece, true, true, true), new SanConversion(SanValidateFormat.parseFile(m0),
            SanValidateFormat.parseRank(m1), toSquare, PromotionPieceType.NONE, sanTerminalMarker));
      }

      default -> throw new ProgrammingMistakeException("Unreachable: mid is exactly 0-3 given the length check above");
    };
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
