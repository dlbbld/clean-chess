package com.dlb.chess.test.pgn.parser.sequential;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.dlb.chess.pgn.parser.StrictPgnParser;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.pgn.parser.sequential.SequentialStrictPgnParser;
import com.dlb.chess.test.pgntest.constants.PgnTestConstants;

/**
 * Parity test — the sequential strict parser must produce the exact same {@link PgnFile} as the existing strict
 * parser over the happy-path strict corpus. Any divergence is a bug in the new pipeline.
 */
class TestSequentialStrictPgnParserAgainstStrict {

  @SuppressWarnings("static-method")
  @Test
  void parityOverHappyPathCorpus() throws IOException {
    final List<Path> subFolders = List.of(
        PgnTestConstants.STRICT_PGN_PARSER_TEST_ROOT_FOLDER_PATH.resolve("fromInitialPosition"),
        PgnTestConstants.STRICT_PGN_PARSER_TEST_ROOT_FOLDER_PATH.resolve("fromCustomPosition"),
        PgnTestConstants.STRICT_PGN_PARSER_TEST_ROOT_FOLDER_PATH.resolve("fromInitialPositionUsingFen"),
        PgnTestConstants.STRICT_PGN_PARSER_TEST_ROOT_FOLDER_PATH.resolve("tag"));

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
      throw new IllegalStateException("No PGN files found under strict test corpus.");
    }

    final List<String> mismatches = new ArrayList<>();
    for (final Path pgnFile : pgnFiles) {
      PgnFile expected = null;
      RuntimeException expectedError = null;
      try {
        expected = StrictPgnParser.parse(pgnFile);
      } catch (final RuntimeException e) {
        expectedError = e;
      }

      PgnFile actual = null;
      RuntimeException actualError = null;
      try {
        actual = SequentialStrictPgnParser.parse(pgnFile);
      } catch (final RuntimeException e) {
        actualError = e;
      }

      if (expectedError != null && actualError != null) {
        // Both fail — acceptable for the happy-path corpus parity check; detailed exception parity is covered by the
        // dedicated exception tests.
        continue;
      }
      if (expectedError != null) {
        mismatches.add(pgnFile.getFileName() + ": existing strict parser rejected but sequential accepted. Existing: "
            + expectedError.getClass().getSimpleName() + ": " + expectedError.getMessage());
        continue;
      }
      if (actualError != null) {
        mismatches.add(pgnFile.getFileName() + ": sequential parser rejected but existing accepted. Sequential: "
            + actualError.getClass().getSimpleName() + ": " + actualError.getMessage());
        continue;
      }
      if (!expected.equals(actual)) {
        mismatches.add(pgnFile.getFileName() + ":\n  expected tagList=" + expected.tagList() + "\n  actual tagList="
            + actual.tagList() + "\n  expected halfMoveCount=" + expected.halfMoveList().size()
            + " actual halfMoveCount=" + actual.halfMoveList().size() + "\n  expected leading='"
            + expected.leadingCommentary() + "' actual leading='" + actual.leadingCommentary() + "'");
      }
    }

    assertEquals(List.of(), mismatches,
        "Sequential strict parser diverges from the existing strict parser on " + mismatches.size() + " file(s)");
  }
}
