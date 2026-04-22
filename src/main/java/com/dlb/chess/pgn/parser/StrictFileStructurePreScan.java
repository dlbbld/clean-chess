package com.dlb.chess.pgn.parser;

import com.dlb.chess.pgn.parser.enums.StrictPgnParserValidationProblem;
import com.dlb.chess.pgn.parser.exceptions.StrictPgnParserValidationException;
import com.dlb.chess.san.enums.SanValidationProblem;

/**
 * One-pass structural validation of a PGN source string. Enforces the whole-file invariants that the strict format
 * demands: file not empty, does not start with a blank line, ends with a blank line, exactly two blank lines in
 * total, and those two blank lines are not adjacent.
 *
 * <p>Kept as a dedicated pre-scan so that the main sequential parser does not have to weave these global counts into
 * its per-token control flow. The pass is itself forward-only and allocation-free beyond a small integer counter.
 */
final class StrictFileStructurePreScan {

  private static final String BASIC_FORMAT_DESCRIPTION =
      "The PGN must have exactly two empty lines, one after the last tag and one at the end.";

  private StrictFileStructurePreScan() {
  }

  static void validate(String source) {
    if (source.isEmpty()) {
      throw error(StrictPgnParserValidationProblem.FILE_EMPTY, "The PGN is empty.");
    }

    final char first = source.charAt(0);
    if (first == '\n' || first == '\r') {
      throw error(StrictPgnParserValidationProblem.FILE_EMPTY_LINE_CANNOT_START_WITH,
          "The PGN cannot start with an empty line.");
    }

    final int length = source.length();
    final boolean endsOnNewline = endsOnNewline(source);
    if (!endsOnNewline) {
      throw error(StrictPgnParserValidationProblem.FILE_EMPTY_LINE_MUST_END_WITH,
          "The PGN must end with an empty line.");
    }

    // Count blank lines and track whether any two are adjacent. A "blank line" is an empty logical line — either
    // two line terminators back to back (interior), or a terminator immediately preceded by another terminator at
    // the start (cannot happen given the first-char check), or a terminator followed only by EOF in the trailing
    // position. We walk once.
    int blankCount = 0;
    int previousBlankLineNumber = -1;
    int currentLineNumber = 1;
    int currentLineContentChars = 0;
    boolean adjacentDetected = false;

    for (int i = 0; i < length; i++) {
      final char c = source.charAt(i);
      if (c == '\r') {
        if (i + 1 < length && source.charAt(i + 1) == '\n') {
          i++;
        }
        if (currentLineContentChars == 0) {
          blankCount++;
          if (previousBlankLineNumber != -1 && currentLineNumber == previousBlankLineNumber + 1) {
            adjacentDetected = true;
          }
          previousBlankLineNumber = currentLineNumber;
        }
        currentLineNumber++;
        currentLineContentChars = 0;
      } else if (c == '\n') {
        if (currentLineContentChars == 0) {
          blankCount++;
          if (previousBlankLineNumber != -1 && currentLineNumber == previousBlankLineNumber + 1) {
            adjacentDetected = true;
          }
          previousBlankLineNumber = currentLineNumber;
        }
        currentLineNumber++;
        currentLineContentChars = 0;
      } else {
        currentLineContentChars++;
      }
    }

    if (blankCount != 2) {
      throw error(StrictPgnParserValidationProblem.FILE_EMPTY_LINE_EXACTLY_TWO,
          "The PGN must have exactly two empty lines.");
    }
    if (adjacentDetected) {
      throw error(StrictPgnParserValidationProblem.FILE_EMPTY_LINE_NOT_CONSECUTIVE,
          "The PGN cannot have two consecutive empty lines.");
    }
  }

  private static boolean endsOnNewline(String source) {
    final char last = source.charAt(source.length() - 1);
    return last == '\n' || last == '\r';
  }

  private static StrictPgnParserValidationException error(StrictPgnParserValidationProblem problem, String message) {
    return new StrictPgnParserValidationException(problem, SanValidationProblem.NONE,
        message + " " + BASIC_FORMAT_DESCRIPTION);
  }
}
