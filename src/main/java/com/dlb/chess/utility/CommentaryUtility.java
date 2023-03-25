package com.dlb.chess.utility;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.movetext.model.ReadComment;
import com.dlb.chess.pgn.reader.enums.PgnReaderStrictValidationProblem;
import com.dlb.chess.pgn.reader.exceptions.PgnReaderStrictValidationException;
import com.dlb.chess.san.enums.SanValidationProblem;

public abstract class CommentaryUtility {

  public static ReadComment parseComment(String movetextPart, boolean isStrict)
      throws PgnReaderStrictValidationException {
    if (movetextPart.isEmpty()) {
      return new ReadComment("", true, "");
    }

    final var firstLetter = NonNullWrapperCommon.toString(movetextPart.charAt(0));
    if (CommentaryUtility.COMMENTARY_END_BRACE.equals(firstLetter)) {
      throw new PgnReaderStrictValidationException(
          PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE, SanValidationProblem.NONE,
          "Movetext starts with commentary end brace");
    }

    if (!CommentaryUtility.COMMENTARY_START_BRACE.equals(firstLetter)) {
      return new ReadComment("", false, movetextPart);
    }
    // we found a commentary start brace
    // now we check that we have no following commentary start brace but a following commentary end brace
    // otherwise we throw an exception

    // first we check if there are characters at all following the commentary start brace
    // if not throw an exception
    if (movetextPart.length() == 1) {
      throw new PgnReaderStrictValidationException(
          PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_ENDS_AFTER_START_BRACE, SanValidationProblem.NONE,
          "Movetext ends after commentary start brace");
    }

    // now we have at least one character after the commentary start brace
    // we check them all for the requirement and return the comment if requirements met, otherwise throw an exception
    for (var i = 1; i < movetextPart.length(); i++) {
      final var currentLetter = NonNullWrapperCommon.toString(movetextPart.charAt(i));

      if (CommentaryUtility.COMMENTARY_END_BRACE.equals(currentLetter)) {
        // we found the end brace
        if (i == movetextPart.length() - 1) {
          // we reached the end of the string
          final String commentary = NonNullWrapperCommon.substring(movetextPart, 1, i);
          return new ReadComment(commentary, true, "");
        }
        if (isStrict) {
          // we have more characters in the string
          final var nextLetter = NonNullWrapperCommon.toString(movetextPart.charAt(i + 1));
          if (!MovetextUtility.VALUE_SEPARATION_LETTER.equals(nextLetter)) {
            throw new PgnReaderStrictValidationException(
                PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_NOT_FOLLOWED_BY_SPACE, SanValidationProblem.NONE,
                "The movetext doesnt continue with a space after a comment");
          }
          // now we have a space as continuation but we need to check further
          if (i == movetextPart.length() - 2) {
            // the movetext ends after the space, this is not valid
            throw new PgnReaderStrictValidationException(
                PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_FOLLOWED_BY_SPACE_BUT_ENDING,
                SanValidationProblem.NONE, "The movetext ends with a space after a comment which is not allowed");
          }
          final var overNextLetter = NonNullWrapperCommon.toString(movetextPart.charAt(i + 2));
          if (MovetextUtility.VALUE_SEPARATION_LETTER.equals(overNextLetter)) {
            throw new PgnReaderStrictValidationException(
                PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_FOLLOWED_BY_TWO_SPACES, SanValidationProblem.NONE,
                "A commentary is followed by two spaces which is not allowed");
          }
          // now we have a valid continuation
          final String commentary = NonNullWrapperCommon.substring(movetextPart, 1, i);
          final String continuation = NonNullWrapperCommon.substring(movetextPart, i + 2);
          return new ReadComment(commentary, false, continuation);
        }

        // non-strict case
        // here we do something easier, we just left trim the remaining movetext for continuation
        final String commentary = NonNullWrapperCommon.substring(movetextPart, 1, i);
        final String continuation = NonNullWrapperCommon.substring(movetextPart, i + 2);
        final String continuationLeftTrim = NonNullWrapperCommon.stripLeading(continuation);
        return new ReadComment(commentary, false, continuationLeftTrim);
      }
    }
    throw new PgnReaderStrictValidationException(
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE,
        SanValidationProblem.NONE, "Found commentary start brace without commentary end brace");
  }

  protected static final String COMMENTARY_START_BRACE = "{";
  protected static final String COMMENTARY_END_BRACE = "}";

}
