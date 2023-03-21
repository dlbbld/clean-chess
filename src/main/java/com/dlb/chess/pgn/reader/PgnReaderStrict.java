package com.dlb.chess.pgn.reader;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.FenValidationException;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.utility.HalfMoveUtility;
import com.dlb.chess.fen.FenParser;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.movetext.model.MovetextParseResult;
import com.dlb.chess.pgn.reader.enums.PgnReaderStrictValidationProblem;
import com.dlb.chess.pgn.reader.enums.ResultTagValue;
import com.dlb.chess.pgn.reader.enums.SetUpTagValue;
import com.dlb.chess.pgn.reader.enums.StandardTag;
import com.dlb.chess.pgn.reader.exceptions.PgnReaderStrictValidationException;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.pgn.reader.model.Tag;
import com.dlb.chess.pgn.reader.utility.ParseTagStrictUtility;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.utility.MovetextUtility;
import com.dlb.chess.utility.TagUtility;

// TODO validateExportFormat
public class PgnReaderStrict extends AbstractPgnReader {

  public static final String EMPTY_LINE = "";

  public static PgnFile readPgn(String pgnFolderPath, String pgnFileName) {
    final List<String> fileLineList = readPgnFileLineList(pgnFolderPath, pgnFileName);
    return readPgn(fileLineList);
  }

  public static PgnFile readPgn(List<String> fileLines) {
    validateEmptyLineRequirement(fileLines);

    final List<Tag> tagList = ParseTagStrictUtility.validateTagList(fileLines);

    final ResultTagValue resultTagValue = validateResultTagValue(tagList);

    final var setUpTagValue = validateTagSetUpValue(tagList);
    final var isStartFromPosition = setUpTagValue == SetUpTagValue.START_FROM_SETUP_POSITION;

    final Fen startFen = calculateStartFen(tagList, isStartFromPosition);

    final var indexFirstEmptyLine = calculateIndexFirstEmptyLine(fileLines);
    final MovetextParseResult movetextParse = MovetextUtility.validateMovetextStrict(fileLines, startFen,
        resultTagValue, indexFirstEmptyLine);

    validateBoardPerLastMove(startFen, movetextParse.halfMoveParseList());

    removeFenIfInitial(tagList, startFen);
    Collections.sort(tagList);

    return new PgnFile(tagList, startFen, movetextParse.leadingCommentary(), movetextParse.halfMoveParseList());
  }

  private static void validateBoardPerLastMove(Fen startFen, List<PgnHalfMove> halfMoveList) {

    final Board board = new Board(startFen);

    for (final PgnHalfMove halfMove : halfMoveList) {
      final Side side = board.getHavingMove();
      final var fullMoveNumber = board.getFullMoveNumberForNextHalfMove();
      try {
        board.performMove(halfMove.san());
      } catch (final SanValidationException e) {
        final String moveNumberAndSan = HalfMoveUtility.calculateMoveNumberAndSanWithSpace(fullMoveNumber, side,
            halfMove.san());
        @SuppressWarnings("null") @NonNull final String messageSanValidationFailure = e.getMessage();
        final var message = "The validation for " + moveNumberAndSan + " failed. Reason: "
            + messageSanValidationFailure;
        throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.SAN, e.getSanValidationProblem(),
            message);
      }
    }
  }

  private static void validateEmptyLineRequirement(List<String> fileLines) {
    final var basicFormatDescription = "The PGN must have exactly two empty lines, one after the last tag and one at the end.";

    if (fileLines.isEmpty()) {
      throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.FILE_EMPTY,
          SanValidationProblem.NONE, "The PGN is empty. " + basicFormatDescription);
    }

    if (EMPTY_LINE.equals(NonNullWrapperCommon.getFirst(fileLines))) {
      throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.FILE_EMPTY_LINE_CANNOT_START_WITH,
          SanValidationProblem.NONE, "The PGN cannot start with an empty line. " + basicFormatDescription);
    }

    if (!EMPTY_LINE.equals(NonNullWrapperCommon.getLast(fileLines))) {
      throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.FILE_EMPTY_LINE_MUST_END_WITH,
          SanValidationProblem.NONE, "The PGN must end with an empty line. " + basicFormatDescription);
    }
    if (calculateNumberOfEmptyLines(fileLines) != 2) {
      throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.FILE_EMPTY_LINE_EXACTLY_TWO,
          SanValidationProblem.NONE, "The PGN must have exactly two empty lines. " + basicFormatDescription);
    }

    final var indexFirstEmptyLine = calculateIndexFirstEmptyLine(fileLines);
    final var indexSecondEmptyLine = fileLines.size() - 1; // last line

    if (indexFirstEmptyLine == indexSecondEmptyLine - 1) {
      throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.FILE_EMPTY_LINE_NOT_CONSECUTIVE,
          SanValidationProblem.NONE, "The PGN cannot have two consecutive empty lines. " + basicFormatDescription);
    }

  }

  private static int calculateNumberOfEmptyLines(List<String> fileLines) {
    var count = 0;
    for (final String line : fileLines) {
      if (EMPTY_LINE.equals(line)) {
        count++;
      }
    }
    return count;
  }

  private static SetUpTagValue validateTagSetUpValue(List<Tag> tagList) {
    SetUpTagValue setUpTagValue;
    if (!TagUtility.hasSetUp(tagList)) {
      setUpTagValue = SetUpTagValue.NONE;
    } else {
      final String setUpTagValueStr = TagUtility.readSetUp(tagList);
      if (!SetUpTagValue.exists(setUpTagValueStr)) {
        throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.TAG_SET_UP_VALUE_INVALID,
            SanValidationProblem.NONE, "The " + StandardTag.SET_UP.getName() + " tag value must exactly match one \""
                + SetUpTagValue.calculateList() + "\".");
      }

      setUpTagValue = SetUpTagValue.calculate(setUpTagValueStr);
    }

    validateTagFenValue(tagList, setUpTagValue);

    return setUpTagValue;
  }

  private static void validateTagFenValue(List<Tag> tagList, SetUpTagValue setUpTagValue) {

    final var hasFenTag = TagUtility.hasFen(tagList);

    switch (setUpTagValue) {
      case NONE:
      case START_FROM_INITIAL_POSITION:
        if (hasFenTag) {
          throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.TAG_FEN_NOT_REQUIRED_BUT_SET,
              SanValidationProblem.NONE, "The FEN can only be set when " + StandardTag.FEN.getName() + " is set to "
                  + SetUpTagValue.START_FROM_SETUP_POSITION.getValue());
        }
        break;
      case START_FROM_SETUP_POSITION:
        if (!hasFenTag) {
          throw new PgnReaderStrictValidationException(
              PgnReaderStrictValidationProblem.TAG_SET_UP_REQUIRES_FEN_TAG_BUT_MISSING, SanValidationProblem.NONE,
              "If the " + StandardTag.SET_UP.getName() + " is set to " + SetUpTagValue.START_FROM_SETUP_POSITION
                  + ", the " + StandardTag.FEN.getName() + " tag must be set.");
        }

        final String fen = TagUtility.readFen(tagList);
        try {
          FenParser.parseAdvancedFen(fen);
        } catch (final FenValidationException e) {
          throw new PgnReaderStrictValidationException(
              PgnReaderStrictValidationProblem.TAG_SET_UP_REQUIRES_FEN_TAG_BUT_FEN_INVALID, SanValidationProblem.NONE,
              "The required FEN tag was provided but is invalid. The error message when parsing was \"" + e.getMessage()
                  + "\".");
        }
        break;
      default:
        throw new IllegalArgumentException();
    }

  }

  private static int calculateIndexFirstEmptyLine(List<String> fileLines) {
    for (var i = 0; i < fileLines.size(); i++) {
      if (EMPTY_LINE.equals(fileLines.get(i))) {
        return i;
      }
    }
    throw new IllegalArgumentException("The method is only designed for lists with an empty line");
  }

  private static ResultTagValue validateResultTagValue(List<Tag> tagList) {
    // at this point we have checked the SevenTagRoster, so the result tag must exist

    if (!TagUtility.hasResult(tagList)) {
      throw new ProgrammingMistakeException(
          "At this point the result value was expected to be validated, which turned out to be a wrong assumption");
    }

    final String resultTagValue = TagUtility.readResult(tagList);
    if (!ResultTagValue.exists(resultTagValue)) {
      throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.TAG_RESULT_VALUE_INVALID,
          SanValidationProblem.NONE, "The " + StandardTag.RESULT.getName() + " tag value must exactly match one \""
              + ResultTagValue.calculateList() + "\".");
    }

    return ResultTagValue.calculate(resultTagValue);
  }

}
