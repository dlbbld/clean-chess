package com.dlb.chess.pgn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.HalfMoveUtility;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.utility.BasicChessUtility;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.enums.MoveSuffixAnnotation;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.PgnHalfMove;

public class PgnCreate {

  /** PGN export-format guideline: lines should not exceed 79 characters. */
  public static final int MAX_LINE_LENGTH = 79;

  public static String createPgnFileString(Board board) {
    return createPgnFileString(createPgnFile(board));
  }

  public static String createPgnFileString(PgnFile pgnFile) {
    return appendEmptyLine(BasicUtility.convertToString(calculatePgnFileFileLines(pgnFile.tagList(),
        pgnFile.pregameCommentary(), pgnFile.startFen(), pgnFile.halfMoveList())));
  }

  private static String appendEmptyLine(String text) {
    return text + "\n";
  }

  public static List<String> createPgnFileLines(PgnFile pgnFile) {
    return calculatePgnFileFileLines(pgnFile.tagList(), pgnFile.pregameCommentary(), pgnFile.startFen(),
        pgnFile.halfMoveList());
  }

  private static List<String> calculatePgnFileFileLines(List<Tag> tagList, PgnCommentary pregameCommentary,
      Fen startFen, List<PgnHalfMove> halfMoveList) {

    final List<String> fileLines = new ArrayList<>();
    for (final Tag tag : tagList) {
      fileLines.add(calculateTagEntry(tag));
    }
    // Empty separator line between tags and movetext.
    fileLines.add("");

    final ResultTagValue resultTagValue = TagUtility.readResultTagValue(tagList);

    final String moves = calculateMovetextWithoutGameTerminationMarker(startFen.fullMoveNumber(), startFen.havingMove(),
        halfMoveList);

    // PgnCommentary is contract-validated (no `}`, no `\r`), so the value writes verbatim into {...}.
    final String pregameCommentaryValue = pregameCommentary.value();
    final String movetextIncludingPreGameCommentary;
    if (pregameCommentaryValue.isEmpty()) {
      movetextIncludingPreGameCommentary = moves + " " + resultTagValue.getValue();
    } else if (moves.isEmpty()) {
      movetextIncludingPreGameCommentary = "{" + pregameCommentaryValue + "}" + " " + resultTagValue.getValue();
    } else {
      movetextIncludingPreGameCommentary = "{" + pregameCommentaryValue + "}" + " " + moves + " "
          + resultTagValue.getValue();
    }

    fileLines.addAll(PgnUtility.calculateWrappedLines(movetextIncludingPreGameCommentary, PgnCreate.MAX_LINE_LENGTH));
    // Trailing blank line per the strict format.
    fileLines.add("");

    return fileLines;
  }

  private static List<Tag> createBoardPlaceHolderTagList(ResultTagValue resultTagValue) {
    final List<Tag> tagList = new ArrayList<>();

    tagList.add(TagPlaceHolderUtility.getPlaceHolderTag(StandardTag.EVENT));
    tagList.add(TagPlaceHolderUtility.getPlaceHolderTag(StandardTag.SITE));

    tagList.add(new Tag(StandardTag.DATE.getName(), BasicUtility.calculateTodayDate()));

    tagList.add(TagPlaceHolderUtility.getPlaceHolderTag(StandardTag.ROUND));
    tagList.add(TagPlaceHolderUtility.getPlaceHolderTag(StandardTag.WHITE));
    tagList.add(TagPlaceHolderUtility.getPlaceHolderTag(StandardTag.BLACK));

    tagList.add(new Tag(StandardTag.RESULT.getName(), resultTagValue.getValue()));

    return tagList;
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
    return NonNullWrapperCommon.toString(result);
  }

  private static String calculateMovetextWithoutGameTerminationMarker(int fullMoveNumber, Side havingMove,
      List<PgnHalfMove> halfMoveList) {

    final StringBuilder result = new StringBuilder();

    var currentFullMoveNumber = fullMoveNumber;
    Side currentHavingMove = havingMove;
    var isFirstMove = true;
    // T-002 / PGN spec Â§8.2.2 case 1: commentary on White's move forces "N..." before the next Black move.
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
    return NonNullWrapperCommon.toString(result);
  }

  public static PgnFile createPgnFile(Board board, List<Tag> tagList) {

    final List<PgnHalfMove> halfMoveList = calculatePgnHalfMoveList(board.getHalfMoveList());

    return new PgnFile(NonNullWrapperCommon.copyOfList(tagList), board.getInitialFen(), PgnCommentary.EMPTY,
        NonNullWrapperCommon.copyOfList(halfMoveList));
  }

  public static PgnFile createPgnFile(Board board) {

    final ResultTagValue resultTagValue = calculateResultTagValue(board);
    final List<Tag> tagList = createBoardPlaceHolderTagList(resultTagValue);

    if (board.getInitialFen() != FenConstants.FEN_INITIAL) {
      tagList.add(new Tag(StandardTag.SET_UP.getName(), SetUpTagValue.START_FROM_SETUP_POSITION.getValue()));
      tagList.add(new Tag(StandardTag.FEN.getName(), board.getInitialFen().fen()));
    }
    Collections.sort(tagList);

    return createPgnFile(board, tagList);
  }

}
