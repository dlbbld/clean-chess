package com.dlb.chess.pgn.create;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.model.Movetext;
import com.dlb.chess.common.utility.BasicChessUtility;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.common.utility.HalfMoveUtility;
import com.dlb.chess.enums.MoveSuffixAnnotation;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.reader.enums.ResultTagValue;
import com.dlb.chess.pgn.reader.enums.SetUpTagValue;
import com.dlb.chess.pgn.reader.enums.StandardTag;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.pgn.reader.model.Tag;
import com.dlb.chess.utility.TagPlaceHolderUtility;
import com.dlb.chess.utility.TagUtility;

public class PgnCreate {

  // per PGN standard, the maximum line length is 79 characters
  private static final int MAX_LINE_LENGTH = 79;

  public static String createPgnFileString(ApiBoard board) {
    return createPgnFileString(createPgnFile(board));
  }

  public static String createPgnFileString(PgnFile pgnFile) {
    return BasicUtility
        .convertToString(calculatePgnFileFileLines(pgnFile.tagList(), pgnFile.startFen(), pgnFile.halfMoveList()));
  }

  public static List<String> createPgnFileLines(PgnFile pgnFile) {
    return calculatePgnFileFileLines(pgnFile.tagList(), pgnFile.startFen(), pgnFile.halfMoveList());
  }

  private static List<String> calculatePgnFileFileLines(List<Tag> tagList, Fen startFen,
      List<PgnHalfMove> halfMoveList) {

    final ResultTagValue resultTagValue = TagUtility.readResultTagValue(tagList);

    final List<String> fileLines = new ArrayList<>();
    // first add the existing tags
    for (final Tag tag : tagList) {
      fileLines.add(calculateTagEntry(tag));
    }

    // add the empty line between tags and move text
    fileLines.add("");

    // add the moves and game termination marker
    final List<String> movetextWithLineBreaks = calculateMovetextWithLineBreaks(startFen.fullMoveNumber(),
        startFen.havingMove(), halfMoveList, resultTagValue);

    fileLines.addAll(movetextWithLineBreaks);

    // finally add an empty line
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
      if (boardHalfMove.moveSpecification().havingMove() != Side.WHITE
          && boardHalfMove.moveSpecification().havingMove() != Side.BLACK) {
        throw new ProgrammingMistakeException("The program created an inconsistent alternating halfmove list");
      }
      halfMove = new PgnHalfMove(boardHalfMove.san(), MoveSuffixAnnotation.NONE, "");
      halfMoveList.add(halfMove);
    }

    return halfMoveList;
  }

  private static ResultTagValue calculateResultTagValue(ApiBoard board) {
    final GameStatus gameStatus = BasicChessUtility.calculateGameStatus(board);

    return switch (gameStatus) {
      case CHECKMATE -> switch (board.getHavingMove()) {
        case WHITE -> ResultTagValue.BLACK_WON;
        case BLACK -> ResultTagValue.WHITE_WON;
        case NONE -> throw new IllegalArgumentException();
        default -> throw new IllegalArgumentException();
      };
      case FIVE_FOLD_REPETITION_RULE, INSUFFICIENT_MATERIAL_BOTH -> ResultTagValue.DRAW;
      case INSUFFICIENT_MATERIAL_MADE_THE_MOVE_ONLY, INSUFFICIENT_MATERIAL_NOT_MADE_THE_MOVE_ONLY, OTHER -> ResultTagValue.ONGOING;
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
    for (final PgnHalfMove halfMove : halfMoveList) {

      // write first move number (before first move which can be White or Black)
      if (isFirstMove) {
        isFirstMove = false;
        final var fullMoveNumberPart = HalfMoveUtility.calculateFullMoveNumberInitialWithoutSpace(fullMoveNumber,
            currentHavingMove);
        result.append(fullMoveNumberPart);
      } else if (currentHavingMove == Side.WHITE) {
        // write following move numbers (before White move)
        final var fullMoveNumberPart = currentFullMoveNumber + ".";
        result.append(" " + fullMoveNumberPart);
      }

      final String san = halfMove.san();
      result.append(" " + san);

      if (halfMove.moveSuffixAnnotation() != MoveSuffixAnnotation.NONE) {
        result.append(halfMove.moveSuffixAnnotation().getSuffix());
      }

      if (!halfMove.commentary().isBlank()) {
        result.append(" {");
        result.append(halfMove.commentary());
        result.append("}");
      }

      // fullMoveNumber is incremented after Black's move
      if (currentHavingMove == Side.BLACK) {
        currentFullMoveNumber++;
      }
      currentHavingMove = currentHavingMove.getOppositeSide();
    }
    return NonNullWrapperCommon.toString(result);

  }

  private static Movetext calculateMovetext(int fullMoveNumber, Side havingMove, List<PgnHalfMove> halfMoveList,
      ResultTagValue resultTagValue) {

    final String movetextWithoutGameTerminationMarker = calculateMovetextWithoutGameTerminationMarker(fullMoveNumber,
        havingMove, halfMoveList);
    final var movetext = movetextWithoutGameTerminationMarker + " " + resultTagValue.getValue();
    return new Movetext(movetext);
  }

  private static List<String> calculateMovetextWithLineBreaks(int fullMoveNumber, Side havingMove,
      List<PgnHalfMove> halfMoveList, ResultTagValue resultTagValue) {

    final Movetext movetext = calculateMovetext(fullMoveNumber, havingMove, halfMoveList, resultTagValue);

    return BasicUtility.calculateWrappedLines(movetext.movetext(), MAX_LINE_LENGTH);
  }

  public static PgnFile createPgnFile(ApiBoard board, List<Tag> tagList) {

    final List<PgnHalfMove> halfMoveList = calculatePgnHalfMoveList(board.getHalfMoveList());

    return new PgnFile(tagList, board.getInitialFen(), "", halfMoveList);
  }

  public static PgnFile createPgnFile(ApiBoard board) {

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
