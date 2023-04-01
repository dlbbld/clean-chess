package com.dlb.chess.pgn.reader.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.pgn.reader.enums.PgnReaderStrictValidationProblem;
import com.dlb.chess.pgn.reader.enums.StandardTag;
import com.dlb.chess.pgn.reader.exceptions.PgnReaderStrictValidationException;
import com.dlb.chess.pgn.reader.model.FirstDuplicateTag;
import com.dlb.chess.pgn.reader.model.Tag;
import com.dlb.chess.pgn.reader.model.TagCandidate;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.utility.TagUtility;

public abstract class ParseTagStrictUtility extends AbstractParseTagUtility {

  private static final String TAG_PATTERN = "\\[(" + TAG_NAME_PATTERN + ") \"([^\"]*)\"\\]";
  @SuppressWarnings("null")
  private static final Pattern TAG_PATTERN_COMPILE = Pattern.compile(TAG_PATTERN);

  private static final String TAG_PATTERN_FIRST_CHARACTER = "\\[[A-Za-z0-9]{1}.*";
  @SuppressWarnings("null")
  private static final Pattern TAG_PATTERN_FIRST_CHARACTER_COMPILE = Pattern.compile(TAG_PATTERN_FIRST_CHARACTER);

  private static final int MAX_TAG_NAME_LENGTH = 255;

  // we have that validation already in the pattern for the format,
  // but we validate that here before so we can give a more specific
  // validation exception in case one of these brackets is missing
  private static void validateTagNameSquareBrackets(String tagLine) {
    if (!tagLine.startsWith(LEFT_SQUARE_BRACKET)) {
      throw new PgnReaderStrictValidationException(
          PgnReaderStrictValidationProblem.TAG_FORMAT_NOT_STARTING_WITH_LEFT_SQUARE_BRACKET, SanValidationProblem.NONE,
          "The tag line must start with the left square bracket " + LEFT_SQUARE_BRACKET);
    }
    if (!tagLine.endsWith(RIGHT_SQUARE_BRACKET)) {
      throw new PgnReaderStrictValidationException(
          PgnReaderStrictValidationProblem.TAG_FORMAT_NOT_ENDING_WITH_RIGHT_SQUARE_BRACKET, SanValidationProblem.NONE,
          "The tag line must end with the right square bracket " + RIGHT_SQUARE_BRACKET);
    }
    if (tagLine.startsWith(LEFT_SQUARE_BRACKET + " ")) {
      throw new PgnReaderStrictValidationException(
          PgnReaderStrictValidationProblem.TAG_FORMAT_LEFT_SQUARE_BRACKET_FOLLOWED_BY_SPACE, SanValidationProblem.NONE,
          "The left square bracked " + LEFT_SQUARE_BRACKET
              + "must be followed by the tag name, but a space was found.");
    }
  }

  private static void validateTagNameFirstCharacter(String tagLine) {
    final var matcher = TAG_PATTERN_FIRST_CHARACTER_COMPILE.matcher(tagLine);
    // check all occurance
    if (!matcher.matches()) {
      throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.TAG_NAME_FIRST_CHARACTER,
          SanValidationProblem.NONE, "The first character in the tag name must be one of A-Z, a-z or 0-9.");
    }
  }

  public static Tag validateTag(String tagLine) {
    validateTagNameSquareBrackets(tagLine);
    validateTagNameFirstCharacter(tagLine);
    validateTagFormat(tagLine);
    final TagCandidate tagCandidate = calculateTagCandidate(tagLine, TAG_PATTERN_COMPILE);
    validateTagNameLength(tagCandidate.name());
    return new Tag(tagCandidate.name(), tagCandidate.value());
  }

  private static void validateTagNameLength(String tagName) throws PgnReaderStrictValidationException {
    if (tagName.length() > MAX_TAG_NAME_LENGTH) {
      throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.TAG_NAME_EXCEEDS_MAXIMUM_LENGTH,
          SanValidationProblem.NONE,
          "With " + tagName.length() + " characters, the tag name exceeds the allowed maximum length of "
              + MAX_TAG_NAME_LENGTH + " characters per the PGN specification.");
    }
  }

  private static boolean validateTagFormat(String tagLine) throws PgnReaderStrictValidationException {
    final var pattern = Pattern.compile(TAG_PATTERN);
    final var matcher = pattern.matcher(tagLine);
    // check all occurance
    if (matcher.matches()) {
      return true;
    }
    throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.TAG_FORMAT_INVALID,
        SanValidationProblem.NONE,
        "A tag line with an invalid format was found. The expected format is exactly like: [White \"Alpha Zero\"]. The tag line is \""
            + tagLine + "\"");
  }

  private static void validateTagUniqueNames(List<Tag> tagList) throws PgnReaderStrictValidationException {
    final FirstDuplicateTag check = calculateFirstDuplicateTag(tagList);
    if (check.hasDuplicateTag()) {
      throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.TAG_NAME_NOT_UNIQUE,
          SanValidationProblem.NONE,
          "The tag name must be unique. The tag name \"" + check.duplicateTagName() + "\" was used more than once.");
    }
  }

  private static void validateSevenTagRoster(List<Tag> tagList) throws PgnReaderStrictValidationException {
    for (final StandardTag expectedTag : TagUtility.SEVEN_TAG_ROSTER_TAG_LIST) {
      if (!TagUtility.existsTag(tagList, expectedTag)) {
        throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.TAG_NOT_ALL_REQUIRED_TAGS_SET,
            SanValidationProblem.NONE,
            "Not all tags from the seven tag roster (" + TagUtility.calculateSevenTagRosterDescription()
                + ") are set. The first not found tag is \"" + expectedTag.getName() + "\".");
      }
    }
  }

  public static List<Tag> validateTagList(List<String> fileLines) throws PgnReaderStrictValidationException {
    final List<Tag> result = new ArrayList<>();

    final List<String> tagList = calculateTagList(fileLines);
    for (final String tagStr : tagList) {
      final Tag tag = validateTag(tagStr);
      result.add(tag);
    }

    validateTagUniqueNames(result);
    validateSevenTagRoster(result);

    return result;
  }

  private static List<String> calculateTagList(List<String> fileLines) {
    final List<String> tagList = new ArrayList<>();
    for (final String line : fileLines) {
      if ("".equals(line)) {
        break;
      }
      tagList.add(line);
    }
    if (tagList.isEmpty()) {
      throw new ProgrammingMistakeException("At this point, the tag list cannot be empty");
    }
    return tagList;
  }
}
