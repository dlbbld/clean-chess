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

  /**
   * Parses a non-king piece move SAN sequentially, left to right. The SAN shape is
   * {@code [piece][fromFile?][fromRank?][x?][toFile][toRank]}: a piece letter at position 0, followed by an optional
   * disambiguation (file, rank, or both), an optional capture symbol, and always a two-character destination square at
   * the end.
   *
   * <p>
   * Middle-section components are matched greedily in grammar order (file before rank before {@code x}), bounded by the
   * position where the destination must begin. If the greedy pass doesn't consume the middle completely, the first
   * unconsumed character violates the grammar and an error is reported there.
   */
  static SanParse parseRbnqMove(final String core, final SanTerminalMarker sanTerminalMarker) {
    final var length = core.length();

    // Length: piece(1) + middle(0–3) + destination(2) = 3 to 6
    if (length < 3 || length > 6) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_LENGTH,
          Message.getString("validation.san.format.rnbq.length"));
    }

    final var piece = parsePieceLetter(core.charAt(0));
    final var destStart = length - 2;
    var pos = 1;

    // Greedy-match optional [fromFile]?[fromRank]?[x]?. The pos < destStart guard ensures we never consume into the
    // destination square's two reserved positions.
    var fromFile = File.NONE;
    if (pos < destStart && SanValidateFormat.isFileLetter(core.charAt(pos))) {
      fromFile = SanValidateFormat.parseFile(core.charAt(pos));
      pos++;
    }

    var fromRank = Rank.NONE;
    if (pos < destStart && SanValidateFormat.isRankDigit(core.charAt(pos))) {
      fromRank = SanValidateFormat.parseRank(core.charAt(pos));
      pos++;
    }

    var isCapture = false;
    if (pos < destStart && core.charAt(pos) == 'x') {
      isCapture = true;
      pos++;
    }

    // After greedy consumption, pos must be at destStart. If not, the remaining middle characters don't form a valid
    // disambiguation — report using the existing width-specific middle messages.
    if (pos != destStart) {
      throw buildMiddleException(core, pos, destStart);
    }

    // Destination square (always the last two characters)
    final var toFileChar = core.charAt(destStart);
    final var toRankChar = core.charAt(destStart + 1);
    if (!SanValidateFormat.isFileLetter(toFileChar) || !SanValidateFormat.isRankDigit(toRankChar)) {
      throw new SanValidationException(SanValidationProblem.FORMAT_RNBQ_DESTINATION,
          Message.getString("validation.san.format.rnbq.destination"));
    }
    final var toSquare = Square.calculate(SanValidateFormat.parseFile(toFileChar),
        SanValidateFormat.parseRank(toRankChar));

    final var hasFromFile = fromFile != File.NONE;
    final var hasFromRank = fromRank != Rank.NONE;

    return new SanParse(pieceMoveSanType(piece, hasFromFile, hasFromRank, isCapture),
        new SanConversion(fromFile, fromRank, toSquare, PromotionPieceType.NONE, sanTerminalMarker));
  }

  /**
   * Builds a {@link FORMAT_PIECE_MIDDLE} exception describing the unconsumed middle characters. Picks the
   * width-specific message key ({@code middle}, {@code middle2}, {@code middle3}) based on how many characters remain.
   */
  private static SanValidationException buildMiddleException(final String core, final int pos, final int destStart) {
    final var remainingLength = destStart - pos;
    return switch (remainingLength) {
      case 1 -> new SanValidationException(SanValidationProblem.FORMAT_RNBQ_MIDDLE,
          Message.getString("validation.san.format.rnbq.middle", NonNullWrapperCommon.toString(core.charAt(pos))));
      case 2 -> new SanValidationException(SanValidationProblem.FORMAT_RNBQ_MIDDLE,
          Message.getString("validation.san.format.rnbq.middle2", NonNullWrapperCommon.toString(core.charAt(pos)),
              NonNullWrapperCommon.toString(core.charAt(pos + 1))));
      case 3 -> new SanValidationException(SanValidationProblem.FORMAT_RNBQ_MIDDLE,
          Message.getString("validation.san.format.rnbq.middle3", NonNullWrapperCommon.toString(core.charAt(pos)),
              NonNullWrapperCommon.toString(core.charAt(pos + 1)),
              NonNullWrapperCommon.toString(core.charAt(pos + 2))));
      default -> throw new ProgrammingMistakeException(
          "Unreachable: remainingLength is in [1, 3] given the length check at entry");
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
