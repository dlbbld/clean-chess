package com.dlb.chess.pgn.reader;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.model.Movetext;
import com.dlb.chess.common.utility.HalfMoveUtility;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.movetext.model.MovetextParseResult;
import com.dlb.chess.pgn.reader.enums.PgnReaderValidationProblem;
import com.dlb.chess.pgn.reader.enums.ResultTagValue;
import com.dlb.chess.pgn.reader.enums.SetUpTagValue;
import com.dlb.chess.pgn.reader.enums.StandardTag;
import com.dlb.chess.pgn.reader.exceptions.PgnReaderValidationException;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.pgn.reader.model.Tag;
import com.dlb.chess.pgn.reader.utility.ParseTagUtility;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.utility.MovetextUtility;
import com.dlb.chess.utility.TagPlaceHolderUtility;
import com.dlb.chess.utility.TagUtility;

public class PgnReader extends AbstractPgnReader {

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
        throw new PgnReaderValidationException(PgnReaderValidationProblem.SAN, e.getSanValidationProblem(), message);
      }
    }
  }

  private static SetUpTagValue validateTagSetUpValue(List<Tag> tagList) {
    SetUpTagValue setUpTagValue;
    if (!TagUtility.hasSetUp(tagList)) {
      setUpTagValue = SetUpTagValue.NONE;
    } else {
      final String setUpTagValueStr = TagUtility.readSetUp(tagList);
      if (!SetUpTagValue.exists(setUpTagValueStr)) {
        throw new PgnReaderValidationException(PgnReaderValidationProblem.TAG_SET_UP_VALUE_INVALID,
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
        // nothing to do
        // if FEN set logically inconsistent but we try to go forward with it
        break;
      case START_FROM_INITIAL_POSITION:
        // if FEN set we complain as it is consistent to indicate the starting position and then provide a FEN
        if (hasFenTag) {
          throw new PgnReaderValidationException(PgnReaderValidationProblem.TAG_SET_UP_VALUE_ZERO_BUT_FEN_PROVIDED,
              SanValidationProblem.NONE,
              "When the " + StandardTag.SET_UP.getName() + " tag is set to "
                  + SetUpTagValue.START_FROM_SETUP_POSITION.getValue() + ", then no " + StandardTag.FEN.getName()
                  + " tag  can be provided");

        }
        break;
      case START_FROM_SETUP_POSITION:
        // nothing to do
        // if FEN not set logically inconsistent, but we try to go forward with the starting position instead
        break;
      default:
        throw new IllegalArgumentException();
    }
  }

  private static void validateResultTagValue(List<Tag> tagList) {

    if (TagUtility.hasResult(tagList)) {
      final String resultTagValue = TagUtility.readResult(tagList);
      if (!ResultTagValue.exists(resultTagValue)) {
        throw new PgnReaderValidationException(PgnReaderValidationProblem.TAG_RESULT_VALUE_INVALID,
            SanValidationProblem.NONE, "The " + StandardTag.RESULT.getName() + " tag value must exactly match one \""
                + ResultTagValue.calculateList() + "\".");
      }
    }
  }

  private static ResultTagValue validateResult(List<Tag> tagList, Movetext movetext) {

    if (TagUtility.hasResult(tagList)) {
      final String result = TagUtility.readResult(tagList);
      final ResultTagValue resultValueTag = ResultTagValue.calculate(result);
      if (MovetextUtility.calculateIsEndWithGameTerminationMarker(movetext)) {
        final ResultTagValue resultValueMovetext = MovetextUtility.calculateResultValueMoveText(movetext);
        if (resultValueTag != resultValueMovetext) {
          throw new PgnReaderValidationException(PgnReaderValidationProblem.TAG_RESULT_BOTH_SET_BUT_DIFFERENT,
              SanValidationProblem.NONE,
              "The result in the result tag and in the movetext must be the same. The result \""
                  + resultValueTag.getValue() + "\" was specified in the result tag, the result \""
                  + resultValueMovetext.getValue() + "\" was specified in the movetext");
        }
      }
      return resultValueTag;
    }

    if (MovetextUtility.calculateIsEndWithGameTerminationMarker(movetext)) {
      final ResultTagValue resultValueMovetext = MovetextUtility.calculateResultValueMoveText(movetext);
      // result tag not set, game termination marker set
      // we use the game termination marker value for the result
      return resultValueMovetext;
    }
    // result not set
    // we use ongoing as an arbitrary decision
    return ResultTagValue.ONGOING;
  }

  private static void fixTagListForResultIfRequired(List<Tag> tagList, ResultTagValue resultTagValue) {
    if (!TagUtility.hasResult(tagList)) {
      final Tag resultTag = new Tag(StandardTag.RESULT.getName(), resultTagValue.getValue());
      tagList.add(resultTag);
    }
  }

  private static void fixTagListForSetUpIfRequired(List<Tag> tagList, boolean isStartFromPosition) {
    if (isStartFromPosition) {
      if (!TagUtility.hasSetUp(tagList)) {
        final Tag setUpTag = new Tag(StandardTag.SET_UP.getName(), SetUpTagValue.START_FROM_SETUP_POSITION.getValue());
        tagList.add(setUpTag);
      }
    } else if (TagUtility.hasSetUp(tagList)) {
      TagUtility.removeTag(tagList, StandardTag.SET_UP);
    }
  }

  private static void fixTagListForMissingSevenTagRosterTags(List<Tag> tagList) {
    for (final StandardTag standardTag : StandardTag.values()) {
      if (standardTag.getIsSevenTagRosterTag() && !TagUtility.existsTag(tagList, standardTag)) {
        final Tag placeHolderTag = TagPlaceHolderUtility.getPlaceHolderTag(standardTag);
        tagList.add(placeHolderTag);
      }
    }
  }

  public static PgnFile readPgn(String pgn) {
    final List<String> lines = NonNullWrapperCommon.asList(NonNullWrapperCommon.split(pgn, "\\n"));
    return readPgn(lines);
  }

  public static PgnFile readPgn(String pgnFolderPath, String pgnFileName) {
    final List<String> fileLines = readPgnFileLineList(pgnFolderPath, pgnFileName);
    return readPgn(fileLines);
  }

  public static PgnFile readPgn(List<String> fileLines) {
    final List<Tag> tagList = ParseTagUtility.validateTagList(fileLines);

    // we check the result if set
    validateResultTagValue(tagList);

    // we check the result in the tag against the movetext
    final Movetext movetext = MovetextUtility.calculateTrimmedMovetext(fileLines);
    final ResultTagValue resultTagValue = validateResult(tagList, movetext);
    // if the result tag was not set, we add the result tag to the tag list to make it consistent
    fixTagListForResultIfRequired(tagList, resultTagValue);

    validateTagSetUpValue(tagList);
    final var isStartFromPosition = TagUtility.hasFen(tagList);

    // if the setup tag is not set but a FEN is given, we add the setup tag with correct value to the tag list to make
    // it consistent
    fixTagListForSetUpIfRequired(tagList, isStartFromPosition);
    final Fen startFen = calculateStartFen(tagList, isStartFromPosition);

    final MovetextParseResult movetextParse = MovetextUtility.validateMovetext(startFen, movetext);

    // now we add missing tags
    fixTagListForMissingSevenTagRosterTags(tagList);

    validateBoardPerLastMove(startFen, movetextParse.halfMoveParseList());

    removeFenIfInitial(tagList, startFen);
    Collections.sort(tagList);

    return new PgnFile(tagList, startFen, movetextParse.leadingCommentary(), movetextParse.halfMoveParseList());
  }
}
