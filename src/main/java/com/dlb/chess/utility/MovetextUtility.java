package com.dlb.chess.utility;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.BasicConstants;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.model.Movetext;
import com.dlb.chess.common.utility.HalfMoveUtility;
import com.dlb.chess.enums.MoveSuffixAnnotation;
import com.dlb.chess.enums.MoveSuffixAnnotationLetter;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.MovetextParse;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.model.SanAndMoveSuffix;
import com.dlb.chess.movetext.model.MovetextParseResult;
import com.dlb.chess.movetext.model.ReadComment;
import com.dlb.chess.movetext.model.SanAnnotatedProcess;
import com.dlb.chess.pgn.reader.enums.PgnReaderStrictValidationProblem;
import com.dlb.chess.pgn.reader.enums.PgnReaderValidationProblem;
import com.dlb.chess.pgn.reader.enums.ResultTagValue;
import com.dlb.chess.pgn.reader.exceptions.PgnReaderStrictValidationException;
import com.dlb.chess.pgn.reader.exceptions.PgnReaderValidationException;
import com.dlb.chess.pgn.reader.utility.ParseTagUtility;
import com.dlb.chess.san.enums.SanLetter;
import com.dlb.chess.san.enums.SanValidationProblem;

public class MovetextUtility {

  static final String VALUE_SEPARATION_LETTER = " ";
  private static final int SAN_MIN_LENGTH = 2;
  private static final int SAN_MAX_LENGTH = 7;

  private static void validateTermination(Movetext movetext, ResultTagValue resultTagValue)
      throws PgnReaderStrictValidationException {
    final String resultValueTag = resultTagValue.getValue();
    final var gameTerminationMarker = calculateGameTerminationMarker(resultTagValue);
    if (!movetext.movetext().endsWith(gameTerminationMarker)) {
      throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.MOVETEXT_TERMINATION_INVALID,
          SanValidationProblem.NONE,
          "The file must end with the result provided in the result tag. The provided result is \"" + resultValueTag
              + "\".");
    }
  }

  private static Movetext calculateMovetextStrict(List<String> fileLines, int indexFirstEmptyLine) {
    final List<String> movetextLines = calculateMovetextLinesStrict(fileLines, indexFirstEmptyLine);
    return calculateMovetextForMovetextLines(movetextLines);
  }

  public static Movetext calculateTrimmedMovetext(List<String> fileLines) {
    final List<String> movetextLines = calculateMovetextLines(fileLines);
    // by trimming we prevent parse difficulties
    final List<String> movetextLinesTrimmed = trimMovetextLines(movetextLines);
    return calculateMovetextForMovetextLines(movetextLinesTrimmed);
  }

  private static List<String> calculateMovetextLinesStrict(List<String> fileLines, int indexFirstEmptyLine) {
    final List<String> fileLinesCopy = new ArrayList<>(fileLines);

    final var indexLastEmptyLine = fileLines.size() - 1;

    final var indexStartInclusive = indexFirstEmptyLine + 1;
    final var indexEndExclusive = indexLastEmptyLine;

    // must operate on the copy as the sublist backs up the original list
    return NonNullWrapperCommon.subList(fileLinesCopy, indexStartInclusive, indexEndExclusive);
  }

  /**
   * Calculate the movetext.
   *
   * @param movetextLines The lines of the PGN file containing the movetext.
   * @return The movetext per the PGN specification with line breaks replaced by spaces.
   */
  private static Movetext calculateMovetextForMovetextLines(List<String> movetextLines) {

    final StringBuilder result = new StringBuilder();
    for (var i = 0; i < movetextLines.size(); i++) {
      final var isFirstLine = i == 0;
      if (!isFirstLine) {
        final var isLastLineEndedWithCommentaryStartBrace = NonNullWrapperCommon.get(movetextLines, i - 1)
            .endsWith(AbstractCommentaryUtility.COMMENTARY_START_BRACE);
        // we add no space if previous line ends with commentary start brace (would add initial space in the comment)
        if (!isLastLineEndedWithCommentaryStartBrace) {
          final var isThisLineStartsWithCommentaryEndBrace = NonNullWrapperCommon.get(movetextLines, i)
              .startsWith(AbstractCommentaryUtility.COMMENTARY_END_BRACE);
          // we add no space if this line begins with commentary end brace (would add space at the end in the comment)
          if (!isThisLineStartsWithCommentaryEndBrace) {
            result.append(" ");
          }
        }
      }
      result.append(movetextLines.get(i));
    }

    return new Movetext(NonNullWrapperCommon.toString(result));
  }

  private static List<String> calculateMovetextLines(List<String> fileLines) {
    final List<String> result = new ArrayList<>();

    for (final String line : fileLines) {
      if (line.isBlank()) {
        continue;
      }
      if (!ParseTagUtility.isConsideredTagLine(line)) {
        result.add(line);
      }
    }
    return result;
  }

  private static List<String> trimMovetextLines(List<String> movetextLines) {
    // we left trim the first line and right trim the last line
    final List<String> result = new ArrayList<>(movetextLines);

    final String firstLine = NonNullWrapperCommon.getFirst(result);
    result.set(0, NonNullWrapperCommon.stripLeading(firstLine));

    final String lastLine = NonNullWrapperCommon.getLast(result);
    result.set(result.size() - 1, NonNullWrapperCommon.stripTrailing(lastLine));

    return result;
  }

  public static MovetextParseResult validateMovetextStrict(List<String> fileLines, Fen startFen,
      ResultTagValue resultTagValue, int indexFirstEmptyLine) throws PgnReaderStrictValidationException {

    final Movetext movetext = MovetextUtility.calculateMovetextStrict(fileLines, indexFirstEmptyLine);
    return validateMovetextStrict(movetext, startFen, resultTagValue);
  }

  private static MovetextParseResult validateMovetextStrict(Movetext movetext, Fen startFen,
      ResultTagValue resultTagValue) throws PgnReaderStrictValidationException {

    // now we can validate that the movetext terminates with the result tag value
    validateTermination(movetext, resultTagValue);

    final String movetextWithoutTerminationMarker = removeGameTerminationMarkerStrict(movetext, resultTagValue);

    final ReadComment initialComment = CommentaryUtility.parseComment(movetextWithoutTerminationMarker, true);

    final List<PgnHalfMove> halfMoveParseList = parseMovetextAfterInitialComment(initialComment.remainingValue(),
        startFen, true);
    return new MovetextParseResult(movetext, halfMoveParseList, initialComment.comment());
  }

  public static MovetextParseResult validateMovetext(Fen startFen, Movetext movetext)
      throws PgnReaderValidationException {
    try {
      return validateMovetextInternal(startFen, movetext);
    } catch (final PgnReaderStrictValidationException e) {
      throw new PgnReaderValidationException(PgnReaderValidationProblem.EXCEPTION_CATCHED_FROM_STRICT_VALIDATION,
          SanValidationProblem.NONE, "The validation failed with the following message: " + e.getMessage());
    }
  }

  private static MovetextParseResult validateMovetextInternal(Fen startFen, Movetext movetext)
      throws PgnReaderStrictValidationException {

    // here we just ignore the result tag in the move text

    final String movetextWithoutTerminationMarker = removeGameTerminationMarker(movetext);

    final ReadComment initialComment = CommentaryUtility.parseComment(movetextWithoutTerminationMarker, false);

    final List<PgnHalfMove> halfMoveParseList = parseMovetextAfterInitialComment(initialComment.remainingValue(),
        startFen, false);
    return new MovetextParseResult(movetext, halfMoveParseList, initialComment.comment());
  }

  private static String removeGameTerminationMarkerStrict(Movetext movetext, ResultTagValue resultTagValue) {

    final var gameTerminationMarker = calculateGameTerminationMarker(resultTagValue);

    final String movetextStr = movetext.movetext();

    if (!movetextStr.endsWith(gameTerminationMarker)) {
      throw new ProgrammingMistakeException(
          "At this point, the movetext must be checked to properly end with game termination marker");
    }
    return NonNullWrapperCommon.removeEnd(movetextStr, gameTerminationMarker);
  }

  private static String removeGameTerminationMarker(Movetext movetext) {

    final String movetextStr = movetext.movetext();
    for (final ResultTagValue checkResultTagValue : ResultTagValue.values()) {
      // movetext contains only result with no leading space
      if (movetextStr.endsWith(checkResultTagValue.getValue())) {
        // we remove the game termination marker
        final String movetextStrWithoutGameTerminationMarker = NonNullWrapperCommon.removeEnd(movetextStr,
            checkResultTagValue.getValue());
        // we remove trailing space if any
        return NonNullWrapperCommon.stripTrailing(movetextStrWithoutGameTerminationMarker);
      }
    }

    return movetextStr;
  }

  private static List<PgnHalfMove> parseMovetextAfterInitialComment(String movetextPart, Fen startFen, boolean isStrict)
      throws PgnReaderStrictValidationException {

    final var startFullMoveNumber = startFen.fullMoveNumber();
    final Side havingMove = startFen.havingMove();

    final MovetextParse model = parseMovetextAfterInitialComment(movetextPart, startFullMoveNumber, havingMove,
        isStrict);

    return calculateHalfMoveList(model, havingMove);

  }

  public static MovetextParse parseMovetextAfterInitialComment(String movetextPart, int startFullMoveNumber,
      Side havingMoveInitital, boolean isStrict) throws PgnReaderStrictValidationException {

    String movetextProcess;
    if (isStrict) {
      if (BasicConstants.BLANK.equals(movetextPart)) {
        return new MovetextParse(false, new ArrayList<>(), new ArrayList<>());
      }

      final var startFullMoveNumberStr = String.valueOf(startFullMoveNumber);
      if (!movetextPart.startsWith(startFullMoveNumberStr)) {
        throw new PgnReaderStrictValidationException(
            PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_CORRECT,
            SanValidationProblem.NONE,
            "The move text does not begin with full move number \"" + startFullMoveNumberStr + " \" as expected.");
      }

      final String expectedMoveNumberSegmentProcess = HalfMoveUtility
          .calculateFullMoveNumberInitialWithSpace(startFullMoveNumber, havingMoveInitital);
      if (!movetextPart.startsWith(expectedMoveNumberSegmentProcess)) {
        switch (havingMoveInitital) {
          case BLACK:
            throw new PgnReaderStrictValidationException(
                PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_BLACK_MOVE,
                SanValidationProblem.NONE, "The move text does not begin indicating a Black move \""
                    + expectedMoveNumberSegmentProcess + " \" as expected.");
          case WHITE:
            throw new PgnReaderStrictValidationException(
                PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_WHITE_MOVE,
                SanValidationProblem.NONE, "The move text does not begin indicating a White move \""
                    + expectedMoveNumberSegmentProcess + " \" as expected.");
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      }

      if (movetextPart.length() == expectedMoveNumberSegmentProcess.length()) {
        throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_ENDS_AFTER,
            SanValidationProblem.NONE, "The movetext ends after a move number which is invalid.");
      }

      // we have something remaining
      movetextProcess = NonNullWrapperCommon.substring(movetextPart, expectedMoveNumberSegmentProcess.length());
    } else {
      // here we first adapt and then check, for we delete white space in adaptation
      movetextProcess = adaptMoveTextUntilBeforeNextCommentary(movetextPart);
      if (movetextProcess.isBlank()) {
        return new MovetextParse(false, new ArrayList<>(), new ArrayList<>());
      }
    }

    final List<PgnHalfMove> whiteHalfMoveList = new ArrayList<>();
    final List<PgnHalfMove> blackHalfMoveList = new ArrayList<>();

    Side havingMoveProcess = havingMoveInitital;
    var moveCounterProcess = startFullMoveNumber;
    // validated successful to start with move number

    while (true) {

      // after the move number we expect a san
      final SanAnnotatedProcess sanAnnotatedProcess = calculateSanAnnotated(movetextProcess);
      final SanAndMoveSuffix sanAndMoveSuffix = calculateSanAndMoveSuffix(sanAnnotatedProcess.sanAnnotated());

      // then we read the comment, which returns an empty comment for convenience if the movetext ends after the san
      final ReadComment commentProcess = CommentaryUtility.parseComment(sanAnnotatedProcess.remainingValue(), isStrict);

      // we add the found information to the move lists
      switch (havingMoveProcess) {
        case WHITE:
          whiteHalfMoveList.add(new PgnHalfMove(sanAndMoveSuffix.san(), sanAndMoveSuffix.moveSuffixAnnotation(),
              commentProcess.comment()));
          break;
        case BLACK:
          blackHalfMoveList.add(new PgnHalfMove(sanAndMoveSuffix.san(), sanAndMoveSuffix.moveSuffixAnnotation(),
              commentProcess.comment()));
          break;
        case NONE:
        default:
          throw new IllegalArgumentException();
      }

      if (sanAnnotatedProcess.isExhausted() || commentProcess.isExhausted()) {
        final var hasMovesAndIsLastMoveBlack = !blackHalfMoveList.isEmpty() && havingMoveProcess == Side.BLACK;

        checkHalfMoveCount(havingMoveProcess, havingMoveInitital, whiteHalfMoveList.size(), blackHalfMoveList.size());

        return new MovetextParse(hasMovesAndIsLastMoveBlack, whiteHalfMoveList, blackHalfMoveList);
      }

      // what remains after previous parsing
      movetextProcess = commentProcess.remainingValue();

      // now we check for the next move number so switch the color
      havingMoveProcess = havingMoveProcess.getOppositeSide();

      // if white has the move the move counter is incremented by one
      if (havingMoveProcess == Side.WHITE) {
        moveCounterProcess++;
      }

      if (isStrict) {
        final String expectedMoveNumberSegmentProcess = HalfMoveUtility
            .calculateFullMoveNumberInitialWithSpace(moveCounterProcess, havingMoveProcess);
        switch (havingMoveProcess) {
          case WHITE:
            // move number is mandatory
            if (!movetextProcess.startsWith(expectedMoveNumberSegmentProcess)) {
              throw new PgnReaderStrictValidationException(
                  PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_CONTINUE_AS_EXPECTED,
                  SanValidationProblem.NONE, "The movetext does not continue with move number \""
                      + expectedMoveNumberSegmentProcess + " \" as expected");
            }
            if (movetextProcess.length() == expectedMoveNumberSegmentProcess.length()) {
              throw new PgnReaderStrictValidationException(
                  PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_ENDS_AFTER, SanValidationProblem.NONE,
                  "The movetext ends after a move number which is invalid.");
            }
            movetextProcess = NonNullWrapperCommon.substring(movetextProcess,
                expectedMoveNumberSegmentProcess.length());
            break;
          case BLACK:
            // move number for the black move is only specified if it's the first move of the game
            if (movetextProcess.startsWith(expectedMoveNumberSegmentProcess)) {
              throw new PgnReaderStrictValidationException(
                  PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_FOR_BLACK_NON_INITIAL_MOVE,
                  SanValidationProblem.NONE, "Move number specified for Black non initial move.");
            }
            break;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      } else {
        movetextProcess = adaptMoveTextUntilBeforeNextCommentary(movetextProcess);
      }
    }
  }

  private static List<PgnHalfMove> calculateHalfMoveList(MovetextParse model, Side havingMove)
      throws PgnReaderStrictValidationException {
    final List<PgnHalfMove> halfMoveParseList = new ArrayList<>();

    if (model.whiteHalfMoveList().isEmpty() && model.blackHalfMoveList().isEmpty()) {
      return new ArrayList<>();
    }

    final var hasMovesAndIsLastMoveBlack = model.hasMovesAndIsLastMoveBlack();

    final List<PgnHalfMove> whiteHalfMoveList = model.whiteHalfMoveList();
    final List<PgnHalfMove> blackHalfMoveList = model.blackHalfMoveList();

    // four cases
    switch (havingMove) {
      case BLACK:
        halfMoveParseList.add(blackHalfMoveList.get(0));
        if (hasMovesAndIsLastMoveBlack) {
          // 1... e5 2. d4 d5 3. c4 c5
          for (var i = 0; i <= whiteHalfMoveList.size() - 1; i++) {
            halfMoveParseList.add(whiteHalfMoveList.get(i));
            halfMoveParseList.add(blackHalfMoveList.get(i + 1));
          }
        } else {
          // 1... e5 2. d4 d5 3. c4
          for (var i = 0; i <= whiteHalfMoveList.size() - 2; i++) {
            halfMoveParseList.add(whiteHalfMoveList.get(i));
            halfMoveParseList.add(blackHalfMoveList.get(i + 1));
          }
          halfMoveParseList.add(NonNullWrapperCommon.getLast(whiteHalfMoveList));
        }
        break;
      case WHITE:
        if (hasMovesAndIsLastMoveBlack) {
          // 1. e5 a6 2. d4 d5 3. c4 c5
          for (var i = 0; i <= whiteHalfMoveList.size() - 1; i++) {
            halfMoveParseList.add(whiteHalfMoveList.get(i));
            halfMoveParseList.add(blackHalfMoveList.get(i));
          }
        } else {
          // 1. e5 a6 2. d4 d5 3. c4
          for (var i = 0; i <= whiteHalfMoveList.size() - 2; i++) {
            halfMoveParseList.add(whiteHalfMoveList.get(i));
            halfMoveParseList.add(blackHalfMoveList.get(i));
          }
          halfMoveParseList.add(NonNullWrapperCommon.getLast(whiteHalfMoveList));
        }
        break;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }

    return halfMoveParseList;
  }

  private static void checkHalfMoveCount(Side havingMove, Side havingMoveInitital, int numberOfWhiteMovesFound,
      int numberOfBlackMovesFound) {
    switch (havingMove) {
      case BLACK:
        switch (havingMoveInitital) {
          case BLACK:
            if (numberOfBlackMovesFound != numberOfWhiteMovesFound + 1) {
              throw new ProgrammingMistakeException("There is a bug in the parse move text code");
            }
            break;
          case WHITE:
            if (numberOfBlackMovesFound != numberOfWhiteMovesFound) {
              throw new ProgrammingMistakeException("There is a bug in the parse move text code");
            }
            break;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
        break;
      case WHITE:
        switch (havingMoveInitital) {
          case BLACK:
            if (numberOfWhiteMovesFound != numberOfBlackMovesFound) {
              throw new ProgrammingMistakeException("There is a bug in the parse move text code");
            }
            break;
          case WHITE:
            if (numberOfWhiteMovesFound != numberOfBlackMovesFound + 1) {
              throw new ProgrammingMistakeException("There is a bug in the parse move text code");
            }
            break;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
        break;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  private static SanAndMoveSuffix calculateSanAndMoveSuffix(String sanAnnotated)
      throws PgnReaderStrictValidationException {

    final var indexOfExclamationMark = sanAnnotated.indexOf(MoveSuffixAnnotationLetter.EXCLAMATION_MARK.getLetter());
    final var indexOfQuestionMark = sanAnnotated.indexOf(MoveSuffixAnnotationLetter.QUESTION_MARK.getLetter());

    if (indexOfExclamationMark == -1 && indexOfQuestionMark == -1) {
      // we have no move annotation
      final String san = sanAnnotated;
      validateSan(san);
      return new SanAndMoveSuffix(san, MoveSuffixAnnotation.NONE);
    }

    // we have found move annotation letter
    final var firstIndexOfMoveSuffixAnnotationLetter = calculateMinimumIndexOf(indexOfExclamationMark,
        indexOfQuestionMark);
    if (firstIndexOfMoveSuffixAnnotationLetter == 0) {
      // no SAN at all
      // 1. ??
      throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.MOVETEXT_SAN_EMPTY,
          SanValidationProblem.NONE,
          "The movetext is invalid because a SAN possibly annotated was expected but no SAN was found.");
    }

    // now we have a SAN and an annotation part
    // we look first at the SAN, then the annotation part
    // 1. Na3?
    final String san = NonNullWrapperCommon.substring(sanAnnotated, 0, firstIndexOfMoveSuffixAnnotationLetter);
    validateSan(san);

    final String moveSuffixAnnotationStr = NonNullWrapperCommon.substring(sanAnnotated,
        firstIndexOfMoveSuffixAnnotationLetter);
    final MoveSuffixAnnotation moveSuffixAnnotation = validateMoveSuffixAnnotation(moveSuffixAnnotationStr);
    return new SanAndMoveSuffix(san, moveSuffixAnnotation);
  }

  private static int calculateMinimumIndexOf(int indexOfExclamationMark, int indexOfQuestionMark) {
    if (indexOfExclamationMark != -1 && indexOfQuestionMark != -1) {
      return Math.min(indexOfExclamationMark, indexOfQuestionMark);
    }
    if (indexOfExclamationMark != -1) {
      return indexOfExclamationMark;
    }
    if (indexOfQuestionMark != -1) {
      return indexOfQuestionMark;
    }
    throw new ProgrammingMistakeException(
        "Unexpected fall through, we checked before that we have an occurrence of the move annotation letter");
  }

  private static void validateSan(String san) {
    validateSanCharacters(san);
    validateSanLength(san);
  }

  private static void validateSanCharacters(String san) {
    for (var i = 0; i < san.length(); i++) {
      final var currentLetter = NonNullWrapperCommon.toString(san.charAt(i));
      if (!SanLetter.exists(currentLetter)) {
        throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.MOVETEXT_SAN_CHARACTER_INVALID,
            SanValidationProblem.NONE,
            "The movetext is invalid because a SAN contains an invalid character of \"" + currentLetter + "\".");
      }
    }
  }

  private static MoveSuffixAnnotation validateMoveSuffixAnnotation(String moveSuffixAnnotation) {
    if (!MoveSuffixAnnotation.exists(moveSuffixAnnotation)) {
      throw new PgnReaderStrictValidationException(
          PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID, SanValidationProblem.NONE,
          "An invalid move annotation suffix of \"" + moveSuffixAnnotation + "\" was found. Valid values are \""
              + MoveSuffixAnnotation.calculateValueList() + "\".");
    }
    return MoveSuffixAnnotation.calculate(moveSuffixAnnotation);
  }

  private static void validateSanLength(String san) throws PgnReaderStrictValidationException {
    if (san.length() < SAN_MIN_LENGTH || san.length() > SAN_MAX_LENGTH) {
      throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID,
          SanValidationProblem.NONE, "The movetext ends unexpectedly afer a SAN followed by a space.");
    }
  }

  private static SanAnnotatedProcess calculateSanAnnotated(String movetextPart)
      throws PgnReaderStrictValidationException {
    // mistake - we only parse if we have something remainig
    // <empty> -> segment value is "", exhausted=true, remaining value is ""
    if (movetextPart.isEmpty()) {
      throw new ProgrammingMistakeException("The san cannot be parsed if there is nothing to parse");
    }

    final var indexOfFirstSpace = movetextPart.indexOf(VALUE_SEPARATION_LETTER);

    if (indexOfFirstSpace != -1) {
      // validation exception
      // <space><anything>
      if (indexOfFirstSpace == 0) {
        throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.MOVETEXT_SAN_CHARACTER_INVALID,
            SanValidationProblem.NONE, "The movetext is invalid because it contains a SAN beginning with a space.");
      }
      // validation exception
      // abc<space> -> segment value is "abc", exhausted=false, remaining value is ""
      if (indexOfFirstSpace == movetextPart.length() - 1) {
        throw new PgnReaderStrictValidationException(
            PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER, SanValidationProblem.NONE,
            "The movetext is invalid because it ends after a SAN and an optional annotation with a space.");
      }

      // valid
      // abc<space>def -> segment value is "abc", exhausted=false, remaining value is "def"
      final String parsedSan = NonNullWrapperCommon.substring(movetextPart, 0, indexOfFirstSpace);
      final String remainingValue = NonNullWrapperCommon.substring(movetextPart, indexOfFirstSpace + 1);
      return new SanAnnotatedProcess(parsedSan, false, remainingValue);
    }

    // valid
    // abc -> segment value is "abc", exhausted=true, remaining value is "" (unused)
    return new SanAnnotatedProcess(movetextPart, true, "");
  }

  private static String adaptMoveTextUntilBeforeNextCommentary(String movetextPart) {
    String partToAdapt;
    String remainingPart;
    if (movetextPart.contains(AbstractCommentaryUtility.COMMENTARY_START_BRACE)) {
      partToAdapt = NonNullWrapperCommon.substringBefore(movetextPart,
          AbstractCommentaryUtility.COMMENTARY_START_BRACE);
      remainingPart = AbstractCommentaryUtility.COMMENTARY_START_BRACE
          + NonNullWrapperCommon.substringAfter(movetextPart, AbstractCommentaryUtility.COMMENTARY_START_BRACE);
    } else {
      partToAdapt = movetextPart;
      remainingPart = "";
    }
    // we expect move numbers, SAN, move suffix annotation

    // first we adapt left white space
    partToAdapt = NonNullWrapperCommon.stripLeading(partToAdapt);

    // now we replace like double spaces by single space
    partToAdapt = NonNullWrapperCommon.normalizeSpace(partToAdapt);

    // now if we have move suffixes preceded by a space, we remove the space
    partToAdapt = normalizeMoveSuffixAnnotations(partToAdapt);

    partToAdapt = normalizeCheckAndCheckmate(partToAdapt);

    // now we remove the move numbers
    // first we change the format to no spaces before dots
    partToAdapt = NonNullWrapperCommon.replace(partToAdapt, " .", ".");

    // now we delete move numbers
    partToAdapt = NonNullWrapperCommon.replaceAll(partToAdapt, "[0-9]+\\.", "");

    // now if any dots remaining, like for black moves or superfluous, we delete them
    partToAdapt = NonNullWrapperCommon.replace(partToAdapt, ".", "");

    // now there will be two spaces where we removed the move numbers, so normalize the space again
    partToAdapt = NonNullWrapperCommon.normalizeSpace(partToAdapt);

    // because the before command does strip trailing white space, we need to add a space if commentary followed
    if (movetextPart.contains(AbstractCommentaryUtility.COMMENTARY_START_BRACE)) {
      partToAdapt = partToAdapt + " ";
    }

    return partToAdapt + remainingPart;
  }

  private static String normalizeMoveSuffixAnnotations(String partToAdapt) {
    String result = partToAdapt;
    for (final MoveSuffixAnnotation moveSuffix : MoveSuffixAnnotation.values()) {
      if (moveSuffix != MoveSuffixAnnotation.NONE) {
        result = NonNullWrapperCommon.replace(result, " " + moveSuffix.getSuffix(), moveSuffix.getSuffix());
      }
    }
    return result;
  }

  private static String normalizeCheckAndCheckmate(String partToAdapt) {
    String result = partToAdapt;
    result = NonNullWrapperCommon.replace(result, " " + SanLetter.CHECK.getLetter(), SanLetter.CHECK.getLetter());
    result = NonNullWrapperCommon.replace(result, " " + SanLetter.CHECKMATE.getLetter(),
        SanLetter.CHECKMATE.getLetter());
    return result;
  }

  public static boolean calculateIsEndWithGameTerminationMarker(Movetext movetext) {
    for (final ResultTagValue resultTagValue : ResultTagValue.values()) {
      if (movetext.movetext().endsWith(resultTagValue.getValue())) {
        return true;
      }
    }
    return false;
  }

  public static ResultTagValue calculateResultValueMoveText(Movetext movetext) {
    for (final ResultTagValue resultTagValue : ResultTagValue.values()) {
      if (movetext.movetext().endsWith(resultTagValue.getValue())) {
        return resultTagValue;
      }
    }
    throw new ProgrammingMistakeException("The movetext must contain a game termination marker at this point");
  }

  private static String calculateGameTerminationMarker(ResultTagValue resultTagValue) {
    return " " + resultTagValue.getValue();
  }
}
