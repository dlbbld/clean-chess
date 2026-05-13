package com.dlb.chess.pgn;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.HalfMoveUtility;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.utility.BasicChessUtility;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.enums.MoveSuffixAnnotation;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.PgnHalfMove;

/**
 * PGN serialisation entry points. The library defaults to {@link WriteMode#SEMANTIC} — emits the parse model
 * as-given without inventing content. {@link WriteMode#ARCHIVAL} runs the model through
 * {@link PgnArchivalNormalization} first to produce a PGN spec section 8.1.1-conformant artifact.
 */
public class PgnCreate {

  /** PGN export-format guideline: lines should not exceed 79 characters. */
  public static final int MAX_LINE_LENGTH = 79;

  public static String createPgnFileString(Board board) {
    return createPgnFileString(createPgnFile(board));
  }

  public static String createPgnFileString(PgnFile pgnFile) {
    return createPgnFileString(pgnFile, WriteMode.SEMANTIC);
  }

  public static String createPgnFileString(PgnFile pgnFile, WriteMode writeMode) {
    return appendEmptyLine(BasicUtility.convertToString(createPgnFileLines(pgnFile, writeMode)));
  }

  public static List<String> createPgnFileLines(PgnFile pgnFile) {
    return createPgnFileLines(pgnFile, WriteMode.SEMANTIC);
  }

  public static List<String> createPgnFileLines(PgnFile pgnFile, WriteMode writeMode) {
    final PgnFile effective = writeMode == WriteMode.ARCHIVAL ? PgnArchivalNormalization.apply(pgnFile) : pgnFile;
    return calculatePgnFileFileLines(effective.tagList(), effective.pregameCommentary(), effective.startFen(),
        effective.halfMoveList(), effective.terminationMarker());
  }

  private static String appendEmptyLine(String text) {
    return text + "\n";
  }

  private static List<String> calculatePgnFileFileLines(List<Tag> tagList, PgnCommentary pregameCommentary,
      Fen startFen, List<PgnHalfMove> halfMoveList, @Nullable ResultTagValue terminationMarker) {

    final List<String> fileLines = new ArrayList<>();
    for (final Tag tag : tagList) {
      fileLines.add(calculateTagEntry(tag));
    }
    // PGN spec section 8.2.2: a tag section is followed by a single empty line. If there is no tag section
    // (semantic-mode output of a Board with no tags), no separator is emitted; the movetext starts immediately.
    if (!tagList.isEmpty()) {
      fileLines.add("");
    }

    final String moves = calculateMovetextWithoutGameTerminationMarker(startFen.fullMoveNumber(), startFen.havingMove(),
        halfMoveList);

    // PgnCommentary is contract-validated (no `}`, no `\r`), so the value writes verbatim into {...}.
    final String pregameCommentaryValue = pregameCommentary.value();
    final String terminationSuffix = terminationMarker != null ? " " + terminationMarker.getValue() : "";
    final String movetextIncludingPreGameCommentary;
    if (pregameCommentaryValue.isEmpty()) {
      movetextIncludingPreGameCommentary = moves + terminationSuffix;
    } else if (moves.isEmpty()) {
      movetextIncludingPreGameCommentary = "{" + pregameCommentaryValue + "}" + terminationSuffix;
    } else {
      movetextIncludingPreGameCommentary = "{" + pregameCommentaryValue + "}" + " " + moves + terminationSuffix;
    }

    fileLines
        .addAll(PgnLineWrapper.calculateWrappedLines(movetextIncludingPreGameCommentary, PgnCreate.MAX_LINE_LENGTH));
    // Trailing blank line per the strict format.
    fileLines.add("");

    return fileLines;
  }

  private static List<PgnHalfMove> calculatePgnHalfMoveList(List<HalfMove> boardHalfMoveList) {
    final List<PgnHalfMove> halfMoveList = new ArrayList<>();

    for (final HalfMove boardHalfMove : boardHalfMoveList) {
      PgnHalfMove halfMove;
      if (boardHalfMove.havingMove() != Side.WHITE && boardHalfMove.havingMove() != Side.BLACK) {
        throw new ProgrammingMistakeException("The program created an inconsistent alternating halfmove list");
      }
      halfMove = new PgnHalfMove(boardHalfMove.san(), MoveSuffixAnnotation.NONE, PgnCommentary.EMPTY);
      halfMoveList.add(halfMove);
    }

    return halfMoveList;
  }

  private static ResultTagValue calculateResultTagValue(Board board) {
    final GameStatus gameStatus = BasicChessUtility.calculateGameStatus(board);

    return switch (gameStatus) {
      case CHECKMATE -> switch (board.getHavingMove()) {
        case WHITE -> ResultTagValue.BLACK_WON;
        case BLACK -> ResultTagValue.WHITE_WON;
        case NONE -> throw new IllegalArgumentException();
        default -> throw new IllegalArgumentException();
      };
      case FIVE_FOLD_REPETITION_RULE, INSUFFICIENT_MATERIAL_BOTH -> ResultTagValue.DRAW;
      case INSUFFICIENT_MATERIAL_MADE_THE_MOVE_ONLY, INSUFFICIENT_MATERIAL_NOT_MADE_THE_MOVE_ONLY, ONGOING -> ResultTagValue.ONGOING;
      case SEVENTY_FIVE_MOVE_RULE, STALEMATE -> ResultTagValue.DRAW;
      default -> throw new IllegalArgumentException();
    };
  }

  private static String calculateTagEntry(Tag tag) {
    final StringBuilder result = new StringBuilder();
    result.append("[").append(tag.name()).append(" ");
    result.append("\"").append(tag.value()).append("\"");
    result.append("]");
    return Nulls.toString(result);
  }

  private static String calculateMovetextWithoutGameTerminationMarker(int fullMoveNumber, Side havingMove,
      List<PgnHalfMove> halfMoveList) {

    final StringBuilder result = new StringBuilder();

    var currentFullMoveNumber = fullMoveNumber;
    Side currentHavingMove = havingMove;
    var isFirstMove = true;
    // T-002 / PGN spec section 8.2.2 case 1: commentary on White's move forces "N..." before the next Black move.
    var priorCommentaryAttached = false;
    for (final PgnHalfMove halfMove : halfMoveList) {

      // Emit the move-number indicator in the three required cases: first half-move, before any White move, or
      // before a Black move that follows commentary on White's move (T-002).
      if (isFirstMove) {
        isFirstMove = false;
        final var fullMoveNumberPart = HalfMoveUtility.calculateFullMoveNumberInitialWithoutSpace(fullMoveNumber,
            currentHavingMove);
        result.append(fullMoveNumberPart);
      } else if (currentHavingMove == Side.WHITE) {
        result.append(" ").append(currentFullMoveNumber).append('.');
      } else if (priorCommentaryAttached) {
        result.append(" ").append(currentFullMoveNumber).append("...");
      }

      final String san = halfMove.san();
      result.append(" ").append(san);
      if (halfMove.moveSuffixAnnotation() != MoveSuffixAnnotation.NONE) {
        result.append(halfMove.moveSuffixAnnotation().getSuffix());
      }

      final String commentaryValue = halfMove.commentary().value();
      if (!commentaryValue.isEmpty()) {
        result.append(" {").append(commentaryValue).append('}');
        priorCommentaryAttached = true;
      } else {
        priorCommentaryAttached = false;
      }

      if (currentHavingMove == Side.BLACK) {
        currentFullMoveNumber++;
      }
      currentHavingMove = currentHavingMove.getOppositeSide();
    }
    return Nulls.toString(result);
  }

  /**
   * Creates a PgnFile from a Board with a caller-supplied tag list. The tag list is preserved verbatim (no
   * fabrication, no sort). The termination marker is derived from the board's game-status — semantic-mode export
   * will emit it as the movetext trailer; archival-mode export will also synthesise a Result tag from it.
   */
  public static PgnFile createPgnFile(Board board, List<Tag> tagList) {

    final List<PgnHalfMove> halfMoveList = calculatePgnHalfMoveList(board.getHalfMoveList());

    return new PgnFile(Nulls.copyOfList(tagList), board.getInitialFen(), PgnCommentary.EMPTY,
        Nulls.copyOfList(halfMoveList), calculateResultTagValue(board));
  }

  /**
   * Creates a PgnFile from a Board with no caller-supplied tags. The tag list is the minimal honest shape: empty
   * when the board started from the initial position, or just SetUp+FEN when from a non-initial position. STR
   * fabrication does not happen here — callers who want a spec section 8.1.1-conformant artifact pass
   * {@link WriteMode#ARCHIVAL} to {@link PgnWriter} or {@link #createPgnFileString(PgnFile, WriteMode)}.
   */
  public static PgnFile createPgnFile(Board board) {

    final List<Tag> tagList = new ArrayList<>();

    if (board.getInitialFen() != FenConstants.FEN_INITIAL) {
      tagList.add(new Tag(StandardTag.SET_UP.getName(), SetUpTagValue.START_FROM_SETUP_POSITION.getValue()));
      tagList.add(new Tag(StandardTag.FEN.getName(), board.getInitialFen().fen()));
    }

    return createPgnFile(board, tagList);
  }

}
