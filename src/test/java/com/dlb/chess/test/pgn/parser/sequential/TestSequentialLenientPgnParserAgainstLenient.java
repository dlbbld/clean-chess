package com.dlb.chess.test.pgn.parser.sequential;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.dlb.chess.pgn.parser.LenientPgnParser;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.pgn.parser.sequential.SequentialLenientPgnParser;
import com.dlb.chess.test.pgntest.constants.PgnTestConstants;

/**
 * Parity test — the sequential lenient parser must produce the exact same {@link PgnFile} as the existing
 * {@link LenientPgnParser} over the happy-path lenient corpus.
 */
class TestSequentialLenientPgnParserAgainstLenient {

  @SuppressWarnings("static-method")
  @Test
  void parityOverHappyPathCorpus() throws IOException {
    final List<Path> subFolders = List.of(
        PgnTestConstants.LENIENT_PGN_PARSER_TEST_ROOT_FOLDER_PATH.resolve("fromInitialPosition"),
        PgnTestConstants.LENIENT_PGN_PARSER_TEST_ROOT_FOLDER_PATH.resolve("fromCustomPosition"),
        PgnTestConstants.LENIENT_PGN_PARSER_TEST_ROOT_FOLDER_PATH.resolve("fromInitialPositionUsingFen"),
        PgnTestConstants.LENIENT_PGN_PARSER_TEST_ROOT_FOLDER_PATH.resolve("tag"),
        PgnTestConstants.LENIENT_PGN_PARSER_TEST_ROOT_FOLDER_PATH.resolve("result"),
        PgnTestConstants.LENIENT_PGN_PARSER_TEST_ROOT_FOLDER_PATH.resolve("lineBreaks"),
        PgnTestConstants.LENIENT_PGN_PARSER_TEST_ROOT_FOLDER_PATH.resolve("utf8"));

    final List<Path> pgnFiles = new ArrayList<>();
    for (final Path folder : subFolders) {
      if (!Files.isDirectory(folder)) {
        continue;
      }
      try (Stream<Path> stream = Files.list(folder)) {
        stream.filter(p -> p.toString().endsWith(".pgn")).forEach(pgnFiles::add);
      }
    }

    if (pgnFiles.isEmpty()) {
      throw new IllegalStateException("No PGN files found under lenient test corpus.");
    }

    final List<String> mismatches = new ArrayList<>();
    int bothAccepted = 0;
    int bothRejected = 0;
    for (final Path pgnFile : pgnFiles) {
      PgnFile expected = null;
      RuntimeException expectedError = null;
      try {
        expected = LenientPgnParser.parse(pgnFile);
      } catch (final RuntimeException e) {
        expectedError = e;
      }

      PgnFile actual = null;
      RuntimeException actualError = null;
      try {
        actual = SequentialLenientPgnParser.parse(pgnFile);
      } catch (final RuntimeException e) {
        actualError = e;
      }

      if (expectedError != null && actualError != null) {
        bothRejected++;
        continue;
      }
      if (expectedError != null) {
        mismatches.add(pgnFile.getFileName() + ": existing lenient rejected but sequential accepted. Existing: "
            + expectedError.getClass().getSimpleName() + ": " + expectedError.getMessage());
        continue;
      }
      if (actualError != null) {
        mismatches.add(pgnFile.getFileName() + ": sequential rejected but existing accepted. Sequential: "
            + actualError.getClass().getSimpleName() + ": " + actualError.getMessage());
        continue;
      }
      if (!expected.equals(actual)) {
        mismatches.add(pgnFile.getFileName() + ":\n  expected tagList=" + expected.tagList() + "\n  actual tagList="
            + actual.tagList() + "\n  expected halfMoveCount=" + expected.halfMoveList().size()
            + " actual halfMoveCount=" + actual.halfMoveList().size() + "\n  expected leading='"
            + expected.leadingCommentary() + "' actual leading='" + actual.leadingCommentary() + "'");
        continue;
      }
      bothAccepted++;
    }

    assertEquals(List.of(), mismatches,
        "Sequential lenient parser diverges on " + mismatches.size() + " of " + pgnFiles.size() + " files"
            + " (both accepted: " + bothAccepted + ", both rejected: " + bothRejected + ")");
  }
}
