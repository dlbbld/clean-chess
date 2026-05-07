package com.dlb.chess.pgn.create;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ChessBoard;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.model.Movetext;
import com.dlb.chess.common.utility.BasicChessUtility;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.common.utility.HalfMoveUtility;
import com.dlb.chess.enums.MoveSuffixAnnotation;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.parser.enums.ResultTagValue;
import com.dlb.chess.pgn.parser.enums.SetUpTagValue;
import com.dlb.chess.pgn.parser.enums.StandardTag;
import com.dlb.chess.pgn.parser.model.PgnCommentary;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.pgn.parser.model.Tag;
import com.dlb.chess.utility.PgnUtility;
import com.dlb.chess.utility.TagPlaceHolderUtility;
import com.dlb.chess.utility.TagUtility;

public class PgnCreate {

  // per PGN standard, the maximum line length is 79 characters
  public static final int MAX_LINE_LENGTH = 79;

  public static String createPgnFileString(ChessBoard board) {
    return createPgnFileString(createPgnFile(board));
  }

  public static String createPgnFileString(PgnFile pgnFile) {
    return appendEmptyLine(BasicUtility.convertToString(calculatePgnFileFileLines(pgnFile.tagList(),
        pgnFile.leadingCommentary(), pgnFile.startFen(), pgnFile.halfMoveList())));
  }

  private static String appendEmptyLine(String text) {
    return text + "\n";
  }

  public static List<String> createPgnFileLines(PgnFile pgnFile) {
    return calculatePgnFileFileLines(pgnFile.tagList(), pgnFile.leadingCommentary(), pgnFile.startFen(),
        pgnFile.halfMoveList());
  }

  private static List<String> calculatePgnFileFileLines(List<Tag> tagList, PgnCommentary leadingCommentary,
      Fen startFen, List<PgnHalfMove> halfMoveList) {

    final List<String> fileLines = new ArrayList<>();
    // first add the existing tags
    for (final Tag tag : tagList) {
      fileLines.add(calculateTagEntry(tag));
    }

    // add the empty line between tags and move text
    fileLines.add("");

    // add the moves and game termination marker
    final ResultTagValue resultTagValue = TagUtility.readResultTagValue(tagList);
    final Movetext movetext = PgnCreate.calculateMovetext(startFen.fullMoveNumber(), startFen.havingMove(),
        halfMoveList, resultTagValue);

    final String moves = calculateMovetextWithoutGameTerminationMarker(startFen.fullMoveNumber(), startFen.havingMove(),
        halfMoveList);

    // add the leading commentary if any as a comment before the movetext.
    // PgnCommentary's value is contract-validated (no tab/newline/CR/control), so we can write it as-is.
    final String leadingCommentaryValue = leadingCommentary.value();
    final String movetextIncludingLeadingCommentary;
    if (leadingCommentaryValue.isEmpty()) {
      movetextIncludingLeadingCommentary = moves + " " + resultTagValue.getValue();
    } else if (moves.isEmpty()) {
      movetextIncludingLeadingCommentary = "{" + leadingCommentaryValue + "}" + " " + resultTagValue.getValue();
    } else {
      movetextIncludingLeadingCommentary = "{" + leadingCommentaryValue + "}" + " " + moves + " "
          + resultTagValue.getValue();
    }

    fileLines.addAll(PgnUtility.calculateWrappedLines(movetextIncludingLeadingCommentary, PgnCreate.MAX_LINE_LENGTH));

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
      if (boardHalfMove.havingMove() != Side.WHITE && boardHalfMove.havingMove() != Side.BLACK) {
        throw new ProgrammingMistakeException("The program created an inconsistent alternating halfmove list");
      }
      halfMove = new PgnHalfMove(boardHalfMove.san(), MoveSuffixAnnotation.NONE, PgnCommentary.EMPTY);
      halfMoveList.add(halfMove);
    }

    return halfMoveList;
  }

  private static ResultTagValue calculateResultTagValue(ChessBoard board) {
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
    // Tracks whether the previous half-move had attached commentary. Per PGN spec §8.2.2 case 1, an intervening
    // commentary between White's move and Black's move requires emitting "N..." before Black's move.
    var priorCommentaryAttached = false;
    for (final PgnHalfMove halfMove : halfMoveList) {

      // Emit the move-number indicator in the three cases the PGN export-format requires it:
      //   (i)  before the very first half-move (always — White or Black depending on starting FEN);
      //   (ii) before every White move (continuation);
      //   (iii) before a Black move when commentary intervened on the previous White move (PGN spec §8.2.2).
      if (isFirstMove) {
        isFirstMove = false;
        final var fullMoveNumberPart = HalfMoveUtility.calculateFullMoveNumberInitialWithoutSpace(fullMoveNumber,
            currentHavingMove);
        result.append(fullMoveNumberPart);
      } else if (currentHavingMove == Side.WHITE) {
        final var fullMoveNumberPart = currentFullMoveNumber + ".";
        result.append(" ").append(fullMoveNumberPart);
      } else if (priorCommentaryAttached) {
        // Black move after intervening commentary on White's move — emit "N..." indicator.
        final var fullMoveNumberPart = currentFullMoveNumber + "...";
        result.append(" ").append(fullMoveNumberPart);
      }

      final String san = halfMove.san();
      result.append(" ").append(san);
      if (halfMove.moveSuffixAnnotation() != MoveSuffixAnnotation.NONE) {
        result.append(halfMove.moveSuffixAnnotation().getSuffix());
      }

      // PgnCommentary's value is contract-validated; emit as-is.
      final String commentaryValue = halfMove.commentary().value();
      if (!commentaryValue.isEmpty()) {
        result.append(" {");
        result.append(commentaryValue);
        result.append("}");
        priorCommentaryAttached = true;
      } else {
        priorCommentaryAttached = false;
      }

      // fullMoveNumber is incremented after Black's move
      if (currentHavingMove == Side.BLACK) {
        currentFullMoveNumber++;
      }
      currentHavingMove = currentHavingMove.getOppositeSide();
    }
    return NonNullWrapperCommon.toString(result);

  }

  // includes the game termination marker by the PGN spec
  private static Movetext calculateMovetext(int fullMoveNumber, Side havingMove, List<PgnHalfMove> halfMoveList,
      ResultTagValue resultTagValue) {

    final String movetextWithoutGameTerminationMarker = calculateMovetextWithoutGameTerminationMarker(fullMoveNumber,
        havingMove, halfMoveList);
    final var movetext = movetextWithoutGameTerminationMarker + " " + resultTagValue.getValue();
    return new Movetext(movetext);
  }

  public static PgnFile createPgnFile(ChessBoard board, List<Tag> tagList) {

    final List<PgnHalfMove> halfMoveList = calculatePgnHalfMoveList(board.getHalfMoveList());

    return new PgnFile(tagList, board.getInitialFen(), PgnCommentary.EMPTY, halfMoveList);
  }

  public static PgnFile createPgnFile(ChessBoard board) {

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
