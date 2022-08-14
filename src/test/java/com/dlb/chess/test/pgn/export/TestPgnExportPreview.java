package com.dlb.chess.test.pgn.export;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.create.PgnCreate;
import com.dlb.chess.pgn.reader.PgnReader;
import com.dlb.chess.pgn.reader.model.PgnFile;

class TestPgnExportPreview {

  @SuppressWarnings("static-method")
  @Test
  void testInitial() {

    {
      final var expected = """
          [Event "?"]
          [Site "?"]
          [Date "?"]
          [Round "?"]
          [White "?"]
          [Black "?"]
          [Result "*"]

          1. e4 e5 2. Nf3 Nc6 *

          """;

      final List<String> actualLineList = new ArrayList<>();
      actualLineList.add("e4 e5 Nf3 Nc6");
      final PgnFile pgnFile = PgnReader.readPgn(actualLineList);

      final List<String> expectedLineList = PgnCreate.createPgnFileLines(pgnFile);
      final String actual = convertToTextBlock(expectedLineList);

      assertEquals(expected, actual);
    }

    {
      final var expected = """
          [Event "?"]
          [Site "?"]
          [Date "?"]
          [Round "?"]
          [White "?"]
          [Black "?"]
          [Result "*"]

          1. e4 e5 2. Nf3 Nc6 *

          """;

      final var pgnFileImport = """
          e4 e5 Nf3 Nc6""";

      final List<String> actualLineList = convertFromTextBlock(pgnFileImport);
      final PgnFile pgnFile = PgnReader.readPgn(actualLineList);

      final List<String> expectedLineList = PgnCreate.createPgnFileLines(pgnFile);
      final String actual = convertToTextBlock(expectedLineList);

      assertEquals(expected, actual);
    }

    {
      final var expected = """
          [Event "Zuerich Open"]
          [Site "?"]
          [Date "?"]
          [Round "?"]
          [White "?"]
          [Black "?"]
          [Result "1-0"]

          1. e4 e5 2. Nf3 Nc6 1-0

          """;

      final var pgnFileImport = """
          [Event "Zuerich Open"]
          e4 e5 Nf3 Nc6 1-0""";

      final List<String> actualLineList = convertFromTextBlock(pgnFileImport);
      final PgnFile pgnFile = PgnReader.readPgn(actualLineList);

      final List<String> expectedLineList = PgnCreate.createPgnFileLines(pgnFile);
      final String actual = convertToTextBlock(expectedLineList);

      assertEquals(expected, actual);
    }

    {
      final var expected = """
          [Event "Zuerich Open"]
          [Site "?"]
          [Date "2000.01.01"]
          [Round "?"]
          [White "?"]
          [Black "?"]
          [Result "1/2-1/2"]

          1. e4 e5 2. Nf3 Nc6 1/2-1/2

          """;

      final var pgnFileImport = """
          [Result "1/2-1/2"]
          [Date "2000.01.01"]
          [Event "Zuerich Open"]
          e4 e5 Nf3 Nc6 """;

      final List<String> actualLineList = convertFromTextBlock(pgnFileImport);
      final PgnFile pgnFile = PgnReader.readPgn(actualLineList);

      final List<String> expectedLineList = PgnCreate.createPgnFileLines(pgnFile);
      final String actual = convertToTextBlock(expectedLineList);

      assertEquals(expected, actual);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testFromPositionWhiteStart() {

    {
      final var expected = """
          [Event "?"]
          [Site "?"]
          [Date "?"]
          [Round "?"]
          [White "?"]
          [Black "?"]
          [Result "*"]
          [SetUp "1"]
          [FEN "r1b2r2/pp1pk1pp/8/7q/3pP1n1/5N1P/PPQ2PP1/3R1RK1 w - - 0 17"]

          17. Qa4 Rb8 18. Qc4 Ra8 *

          """;

      final var pgnFileImport = """
          [FEN "r1b2r2/pp1pk1pp/8/7q/3pP1n1/5N1P/PPQ2PP1/3R1RK1 w - - 0 17"]
          Qa4 Rb8 Qc4 Ra8 """;

      final List<String> actualLineList = convertFromTextBlock(pgnFileImport);
      final PgnFile pgnFile = PgnReader.readPgn(actualLineList);

      final List<String> expectedLineList = PgnCreate.createPgnFileLines(pgnFile);
      final String actual = convertToTextBlock(expectedLineList);

      assertEquals(expected, actual);
    }

    {
      final var expected = """
          [Event "?"]
          [Site "?"]
          [Date "?"]
          [Round "?"]
          [White "Donald Duck"]
          [Black "Mickey Mouse"]
          [Result "*"]
          [SetUp "1"]
          [FEN "r1b2r2/pp1pk1pp/8/7q/3pP1n1/5N1P/PPQ2PP1/3R1RK1 w - - 0 17"]

          17. Qa4 Rb8 18. Qc4 Ra8 *

          """;

      final var pgnFileImport = """
          [Black "Mickey Mouse"]
          [White "Donald Duck"]
          [FEN "r1b2r2/pp1pk1pp/8/7q/3pP1n1/5N1P/PPQ2PP1/3R1RK1 w - - 0 17"]
          1. Qa4 Rb8 2. Qc4 Ra8 """;

      final List<String> actualLineList = convertFromTextBlock(pgnFileImport);
      final PgnFile pgnFile = PgnReader.readPgn(actualLineList);

      final List<String> expectedLineList = PgnCreate.createPgnFileLines(pgnFile);
      final String actual = convertToTextBlock(expectedLineList);

      assertEquals(expected, actual);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testFromPositionBlackStart() {

    {
      final var expected = """
          [Event "?"]
          [Site "?"]
          [Date "?"]
          [Round "?"]
          [White "?"]
          [Black "?"]
          [Result "1-0"]
          [SetUp "1"]
          [FEN "1n2kb1r/p4ppp/4q3/4p1B1/4P3/8/PPP2PPP/2KR4 b k - 0 17"]

          17... Qd7 18. Re1 Na6 19. Rd1 Nb8 20. Kb1 Be7 21. Kc1 1-0

          """;

      final var pgnFileImport = """
          [Result "1-0"]
          [FEN "1n2kb1r/p4ppp/4q3/4p1B1/4P3/8/PPP2PPP/2KR4 b k - 0 17"]
          Qd7 Re1 Na6 Rd1 Nb8 Kb1 Be7 Kc1 """;

      final List<String> actualLineList = convertFromTextBlock(pgnFileImport);
      final PgnFile pgnFile = PgnReader.readPgn(actualLineList);

      final List<String> expectedLineList = PgnCreate.createPgnFileLines(pgnFile);
      final String actual = convertToTextBlock(expectedLineList);

      assertEquals(expected, actual);
    }

    {
      final var expected = """
          [Event "?"]
          [Site "?"]
          [Date "2011.12.31"]
          [Round "13"]
          [White "?"]
          [Black "?"]
          [Result "*"]
          [SetUp "1"]
          [FEN "1n2kb1r/p4ppp/4q3/4p1B1/4P3/8/PPP2PPP/2KR4 b k - 0 17"]

          17... Qd7 18. Re1 Na6 19. Rd1 Nb8 20. Kb1 Be7 21. Kc1 *

          """;

      final var pgnFileImport = """
          [Date "2011.12.31"]
          [Round "13"]
          [FEN "1n2kb1r/p4ppp/4q3/4p1B1/4P3/8/PPP2PPP/2KR4 b k - 0 17"]
          1... Qd7 2. Re1 Na6 3. Rd1 Nb8 4. Kb1 Be7 5. Kc1""";

      final List<String> actualLineList = convertFromTextBlock(pgnFileImport);
      final PgnFile pgnFile = PgnReader.readPgn(actualLineList);

      final List<String> expectedLineList = PgnCreate.createPgnFileLines(pgnFile);
      final String actual = convertToTextBlock(expectedLineList);

      assertEquals(expected, actual);
    }
  }

  private static String convertToTextBlock(List<String> lineList) {
    final StringBuilder result = new StringBuilder();
    for (final String line : lineList) {
      result.append(line);
      result.append("\n");
    }
    return NonNullWrapperCommon.toString(result);
  }

  private static List<String> convertFromTextBlock(String textBlock) {
    return NonNullWrapperCommon.asList(NonNullWrapperCommon.split(textBlock, "\n"));
  }
}
