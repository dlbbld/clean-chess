package com.dlb.chess.pgn.reader.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.reader.enums.PgnReaderValidationProblem;
import com.dlb.chess.pgn.reader.exceptions.PgnReaderValidationException;
import com.dlb.chess.pgn.reader.model.FirstDuplicateTag;
import com.dlb.chess.pgn.reader.model.Tag;
import com.dlb.chess.pgn.reader.model.TagCandidate;
import com.dlb.chess.san.enums.SanValidationProblem;

public class ParseTagUtility extends AbstractParseTagUtility {

  // we normalize the tag line before parsing here
  // so we can use a much easier regular expression
  private static final String TAG_PATTERN = "\\[[ \t]*(" + TAG_NAME_PATTERN + ")[ \t]*\"([^\"]*)\"[ \t]*\\]";
  @SuppressWarnings("null")
  private static final Pattern TAG_PATTERN_COMPILE = Pattern.compile(TAG_PATTERN);

  // we need this for consistency of parsing
  // reading the lines containing the tags and the movetext depend on this
  private static void validateTagLineReappear(List<String> fileLines) throws PgnReaderValidationException {
    var hasFoundNonTagLine = false;
    for (final String line : fileLines) {
      if (line.isBlank()) {
        continue;
      }
      if (!isConsideredTagLine(line)) {
        // we found the first line without square bracket, we assume that's the movetext
        if (!hasFoundNonTagLine) {
          hasFoundNonTagLine = true;
        }
      } else if (hasFoundNonTagLine) {
        throw new PgnReaderValidationException(PgnReaderValidationProblem.TAG_REAPPEAR, SanValidationProblem.NONE,
            "After the movetext started, tags can no longer appear.");
      }
    }
  }

  private static Tag calculateTag(String tagLine) {
    final TagCandidate tagCandidate = calculateTagCandidate(tagLine, TAG_PATTERN_COMPILE);
    return new Tag(tagCandidate.name(), tagCandidate.value());
  }

  private static boolean validateTagFormat(String tagLine) throws PgnReaderValidationException {
    final var pattern = Pattern.compile(TAG_PATTERN);
    final var matcher = pattern.matcher(tagLine);
    // check all occurance
    if (matcher.matches()) {
      return true;
    }
    throw new PgnReaderValidationException(PgnReaderValidationProblem.TAG_FORMAT_INVALID, SanValidationProblem.NONE,
        "A tag line with an invalid format was found. The expected format is exactly like: [White \"Alpha Zero\"]. The tag line is \""
            + tagLine + "\"");
  }

  private static void validateTagUniqueNames(List<Tag> tagList) throws PgnReaderValidationException {
    final FirstDuplicateTag check = calculateFirstDuplicateTag(tagList);
    if (check.hasDuplicateTag()) {
      throw new PgnReaderValidationException(PgnReaderValidationProblem.TAG_NAME_NOT_UNIQUE, SanValidationProblem.NONE,
          "The tag name must be unique. The tag name \"" + check.duplicateTagName() + "\" was used more than once.");
    }
  }

  public static List<Tag> validateTagList(List<String> fileLines) throws PgnReaderValidationException {
    validateTagLineReappear(fileLines);

    final List<String> tagList = calculateTagList(fileLines);

    final List<Tag> result = new ArrayList<>();
    for (final String tagLine : tagList) {
      validateTagFormat(tagLine);
      final Tag tag = calculateTag(tagLine);
      result.add(tag);
    }

    validateTagUniqueNames(result);

    return result;
  }

  // here we just keep on adding found tag lines, empty lines are allowed
  private static List<String> calculateTagList(List<String> fileLines) {
    final List<String> tagList = new ArrayList<>();
    for (final String line : fileLines) {
      if (!line.isBlank()) {
        final String lineTrimmed = NonNullWrapperCommon.trim(line);
        if (!isConsideredTagLine(lineTrimmed)) {
          // we found the first line without square bracket, we assume that's the movetext
          return tagList;
        }
        tagList.add(lineTrimmed);
      }

    }
    return tagList;
  }

  public static boolean isConsideredTagLine(String line) {
    return line.contains(LEFT_SQUARE_BRACKET) || line.contains(RIGHT_SQUARE_BRACKET);
  }
}
