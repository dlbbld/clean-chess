package com.dlb.chess.test.readme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.pgn.create.PgnCreate;
import com.dlb.chess.pgn.parser.LenientPgnParser;
import com.dlb.chess.pgn.parser.StrictPgnParser;
import com.dlb.chess.pgn.parser.enums.LenientPgnParserValidationProblem;
import com.dlb.chess.pgn.parser.enums.StrictPgnParserValidationProblem;
import com.dlb.chess.pgn.parser.exceptions.LenientPgnParserValidationException;
import com.dlb.chess.pgn.parser.exceptions.StrictPgnParserValidationException;
import com.dlb.chess.pgn.parser.model.LenientPgnParserValidationResult;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.pgn.parser.model.StrictPgnParserValidationResult;
import com.dlb.chess.pgn.parser.model.Tag;
import com.dlb.chess.pgn.writer.PgnWriter;
import com.dlb.chess.report.Reporter;
import com.dlb.chess.report.model.Report;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.unwinnability.full.enums.DeadPositionFull;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.quick.enums.DeadPositionQuick;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;
import com.dlb.chess.utility.PgnUtility;

class TestReadMe {

  @Test
  @SuppressWarnings("static-method")
  void threefoldClaimAheadExampleHasNoRepetitionOnTheBoardYet() {
    final var pgn = """
        1. Nf3 c5 2. c4 Nf6 3. Nc3 Nc6 4. d4 cxd4 5. Nxd4 e6 6. g3 Qb6 7. Nb3 Ne5 8. e4
        Bb4 9. Qe2 O-O 10. f4 Nc6 11. e5 Ne8 12. Bd2 f6 13. c5 Qd8 14. a3 Bxc3 15. Bxc3
        fxe5 16. Bxe5 b6 17. Bg2 Nxe5 18. Bxa8 Nf7 19. Bg2 bxc5 20. Nxc5 Qb6 21. Qf2
        Qb5 22. Bf1 Qc6 23. Bg2 Qb5 24. Bf1 Qc6 25. Bg2""";

    final Report report = Reporter.calculateAnalysis(calculateBoard(pgn));

    assertFalse(report.hasThreefoldRepetition());
    assertFalse(report.hasFivefoldRepetition());
  }

  @Test
  @SuppressWarnings("static-method")
  void threefoldOnTheBoardExampleFindsThreefoldRepetition() {
    final var pgn = """
        1. d4 d5 2. Nf3 Nf6 3. c4 e6 4. Bg5 Nbd7 5. e3 Be7 6. Nc3 O-O 7. Rc1 b6 8. cxd5
        exd5 9. Qa4 c5 10. Qc6 Rb8 11. Nxd5 Bb7 12. Nxe7+ Qxe7 13. Qa4 Rbc8 14. Qa3 Qe6
        15. Bxf6 Qxf6 16. Ba6 Bxf3 17. Bxc8 Rxc8 18. gxf3 Qxf3 19. Rg1 Re8 20. Qd3 g6
        21. Kf1 Re4 22. Qd1 Qh3+ 23. Rg2 Nf6 24. Kg1 cxd4 25. Rc4 dxe3 26. Rxe4 Nxe4 27.
        Qd8+ Kg7 28. Qd4+ Nf6 29. fxe3 Qe6 30. Rf2 g5 31. h4 gxh4 32. Qxh4 Ng4 33. Qg5+
        Kf8 34. Rf5 h5 35. Qd8+ Kg7 36. Qg5+ Kf8 37. Qd8+ Kg7 38. Qg5+ Kf8 39. b3 Qd6
        40. Qf4 Qd1+ 41. Qf1 Qd7 42. Rxh5 Nxe3 43. Qf3 Qd4 44. Qa8+ Ke7 45. Qb7+ Kf8 46.
        Qb8+""";

    final Report report = Reporter.calculateAnalysis(calculateBoard(pgn));

    assertTrue(report.hasThreefoldRepetition());
    assertFalse(report.hasFivefoldRepetition());
  }

  @Test
  @SuppressWarnings("static-method")
  void fiftyMoveExampleFindsFiftyMoveRule() {
    final var pgn = """
        1. d4 Nf6 2. c4 g6 3. Nc3 Bg7 4. e4 d6 5. Nf3 O-O 6. Be2 e5 7. O-O Nc6 8. d5
        Ne7 9. Nd2 a5 10. Rb1 Nd7 11. a3 f5 12. b4 Kh8 13. f3 Ng8 14. Qc2 Ngf6 15. Nb5
        axb4 16. axb4 Nh5 17. g3 Ndf6 18. c5 Bd7 19. Rb3 Nxg3 20. hxg3 Nh5 21. f4 exf4
        22. c6 bxc6 23. dxc6 Nxg3 24. Rxg3 fxg3 25. cxd7 g2 26. Rf3 Qxd7 27. Bb2 fxe4
        28. Rxf8+ Rxf8 29. Bxg7+ Qxg7 30. Qxe4 Qf6 31. Nf3 Qf4 32. Qe7 Rf7 33. Qe6 Rf6
        34. Qe8+ Rf8 35. Qe7 Rf7 36. Qe6 Rf6 37. Qb3 g5 38. Nxc7 g4 39. Nd5 Qc1+ 40.
        Qd1 Qxd1+ 41. Bxd1 Rf5 42. Ne3 Rf4 43. Ne1 Rxb4 44. Bxg4 h5 45. Bf3 d5 46.
        N3xg2 h4 47. Nd3 Ra4 48. Ngf4 Kg7 49. Kg2 Kf6 50. Bxd5 Ra5 51. Bc6 Ra6 52. Bb7
        Ra3 53. Be4 Ra4 54. Bd5 Ra5 55. Bc6 Ra6 56. Bf3 Kg5 57. Bb7 Ra1 58. Bc8 Ra4 59.
        Kf3 Rc4 60. Bd7 Kf6 61. Kg4 Rd4 62. Bc6 Rd8 63. Kxh4 Rg8 64. Be4 Rg1 65. Nh5+
        Ke6 66. Ng3 Kf6 67. Kg4 Ra1 68. Bd5 Ra5 69. Bf3 Ra1 70. Kf4 Ke6 71. Nc5+ Kd6
        72. Nge4+ Ke7 73. Ke5 Rf1 74. Bg4 Rg1 75. Be6 Re1 76. Bc8 Rc1 77. Kd4 Rd1+ 78.
        Nd3 Kf7 79. Ke3 Ra1 80. Kf4 Ke7 81. Nb4 Rc1 82. Nd5+ Kf7 83. Bd7 Rf1+ 84. Ke5
        Ra1 85. Ng5+ Kg6 86. Nf3 Kg7 87. Bg4 Kg6 88. Nf4+ Kg7 89. Nd4 Re1+ 90. Kf5 Rc1
        91. Be2 Re1 92. Bh5 Ra1 93. Nfe6+ Kh6 94. Be8 Ra8 95. Bc6 Ra1 96. Kf6 Kh7 97.
        Ng5+ Kh8 98. Nde6 Ra6 99. Be8 Ra8 100. Bh5 Ra1 101. Bg6 Rf1+ 102. Ke7 Ra1 103.
        Nf7+ Kg8 104. Nh6+ Kh8 105. Nf5 Ra7+ 106. Kf6 Ra1 107. Ne3 Re1 108. Nd5 Rg1
        109. Bf5 Rf1 110. Ndf4 Ra1 111. Ng6+ Kg8 112. Ne7+ Kh8 113. Ng5 Ra6+ 114. Kf7
        Rf6+""";

    final Report report = Reporter.calculateAnalysis(calculateBoard(pgn));

    assertTrue(report.hasFiftyMoveRule());
    assertFalse(report.hasSeventyFiveMoveRule());
  }

  @Test
  @SuppressWarnings("static-method")
  void unwinnabilityExamplesReturnExpectedResults() {
    assertUnwinnability("8/8/4k3/3R4/2K5/8/8/8 w - - 0 50", Side.BLACK, UnwinnableQuick.UNWINNABLE,
        UnwinnableFull.UNWINNABLE);
    assertUnwinnability("8/8/3k4/1p2p1p1/pP1pP1P1/P2P4/1K6/8 b - - 32 62", Side.BLACK, UnwinnableQuick.UNWINNABLE,
        UnwinnableFull.UNWINNABLE);
    assertUnwinnability("5r1k/6P1/7K/5q2/8/8/8/8 b - - 0 51", Side.WHITE, UnwinnableQuick.UNWINNABLE,
        UnwinnableFull.UNWINNABLE);
    assertUnwinnability("q4r2/pR3pkp/1p2p1p1/4P3/6P1/1P3Q2/1Pr2PK1/3R4 b - - 3 29", Side.WHITE,
        UnwinnableQuick.POSSIBLY_WINNABLE, UnwinnableFull.WINNABLE);
    assertUnwinnability("1k6/1P5p/BP3p2/1P6/8/8/5PKP/8 b - - 0 41", Side.WHITE, UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableFull.UNWINNABLE);
  }

  @Test
  @SuppressWarnings("static-method")
  void deadPositionExamplesReturnExpectedResults() {
    assertDeadPosition("8/8/3kn3/8/2K5/8/8/8 w - - 0 50", DeadPositionQuick.DEAD_POSITION,
        DeadPositionFull.DEAD_POSITION);
    assertDeadPosition("8/6b1/1p3k2/1Pp1p1p1/2P1PpP1/5P2/8/5K2 b - - 11 61", DeadPositionQuick.DEAD_POSITION,
        DeadPositionFull.DEAD_POSITION);
    assertDeadPosition("k7/P1K5/8/8/8/8/8/8 b - - 2 58", DeadPositionQuick.DEAD_POSITION,
        DeadPositionFull.DEAD_POSITION);
  }

  @Test
  @SuppressWarnings("static-method")
  void boardExampleEndsInCheckmate() {
    final Board board = new Board();

    board.performMove("e4");
    board.performMoves("e5", "Bc4");

    final var newMove = new MoveSpecification(Square.F8, Square.C5);
    board.performMove(newMove);

    board.unperformMove();

    board.performMoves("Bc5", "Qf3", "h6", "Qxf7#");

    assertTrue(board.isCheckmate());
  }

  @Test
  @SuppressWarnings("static-method")
  void pgnFileCanBeWrittenAndParsed(@TempDir Path tempDir) {
    final Board sourceBoard = createOpeningExampleBoard();
    final PgnFile pgnFile = PgnCreate.createPgnFile(sourceBoard);
    final Path filePath = NonNullWrapperCommon.pathResolve(tempDir, "myFile.pgn");

    PgnWriter.writePgnFile(pgnFile, filePath);

    final Board lenientBoard = PgnUtility.calculateBoardPerLastMove(LenientPgnParser.parse(filePath));
    final Board strictBoard = PgnUtility.calculateBoardPerLastMove(StrictPgnParser.parse(filePath));

    assertFalse(lenientBoard.isCheckmate());
    assertFalse(strictBoard.isThreefoldRepetition());
    assertTrue(LenientPgnParser.validate(filePath).isValid());
    assertTrue(StrictPgnParser.validate(filePath).isValid());
  }

  @Test
  @SuppressWarnings("static-method")
  void lenientParserAcceptsLooseReadmeFormat() {
    final var pgn = """
        [ Event "Spring Classic"]

        1. e4 e5   2. Nf3
        Nf6
          3. Bc4 Bc5
                """;

    final PgnFile pgnFile = LenientPgnParser.parseText(pgn);
    final Board board = PgnUtility.calculateBoardPerLastMove(pgnFile);
    board.performMove("a3");

    assertEquals("Spring Classic", tagValue(pgnFile, "Event"));
    assertEquals(6, pgnFile.halfMoveList().size());
  }

  @Test
  @SuppressWarnings("static-method")
  void lenientParserCreatesExportFormat() {
    final var pgn = """
                [Black "Jane Doe"]
                [White "John Doe"]
                [ Event "Spring Classic"]

                1. e4 e5   2. Nf3
                Nf6
                3. Bc4 Bc5
        """;

    final PgnFile pgnFile = LenientPgnParser.parseText(pgn);
    final String exported = PgnCreate.createPgnFileString(pgnFile);

    assertTrue(exported.contains("[Event \"Spring Classic\"]"));
    assertTrue(exported.contains("[White \"John Doe\"]"));
    assertTrue(exported.contains("[Black \"Jane Doe\"]"));
    assertTrue(exported.contains("1. e4 e5 2. Nf3 Nf6 3. Bc4 Bc5 *"));
    assertTrue(StrictPgnParser.validateText(exported).isValid());
  }

  @Test
  @SuppressWarnings("static-method")
  void lenientParserReportsInvalidSan() {
    final var pgn = """
        [ Event "Spring Classic"]

        1. e4 e5   2. Nf4
        Nf6
          3. Bc4 Bc5
                """;

    try {
      LenientPgnParser.parseText(pgn);
      fail("Expected invalid SAN to fail lenient PGN parsing");
    } catch (final LenientPgnParserValidationException e) {
      assertEquals(LenientPgnParserValidationProblem.SAN, e.getLenientPgnParserValidationProblem());
      assertEquals(SanValidationProblem.NOT_REACHABLE_RNBQ_NEITHER_MULTIPLE, e.getSanValidationProblem());
    }
  }

  @Test
  @SuppressWarnings("static-method")
  void strictParserAcceptsStrictReadmeFormat() {
    final var pgn = """
        [Event "Spring Classic"]
        [Site "Somewhere"]
        [Date "2024.01.01"]
        [Round "1"]
        [White "Player1"]
        [Black "Player2"]
        [Result "*"]

        1. e4 e5 2. Nf3 Nf6 3. Bc4 Bc5 *

        """;

    final PgnFile pgnFile = StrictPgnParser.parseText(pgn);
    final Board board = PgnUtility.calculateBoardPerLastMove(pgnFile);
    board.performMove("a3");

    assertEquals(6, pgnFile.halfMoveList().size());
  }

  @Test
  @SuppressWarnings("static-method")
  void strictParserRejectsLenientTagSyntax() {
    final var pgn = """
        [ Event "Spring Classic"]

        1. e4 e5 2. Nf3 Nf6 3. Bc4 Bc5

        """;

    try {
      StrictPgnParser.parseText(pgn);
      fail("Expected lenient tag syntax to fail strict PGN parsing");
    } catch (final StrictPgnParserValidationException e) {
      assertEquals(StrictPgnParserValidationProblem.TAG_FORMAT_LEFT_SQUARE_BRACKET_FOLLOWED_BY_SPACE,
          e.getStrictPgnParserValidationProblem());
    }
  }

  @Test
  @SuppressWarnings("static-method")
  void strictParserRejectsMissingSevenTagRoster() {
    final var pgn = """
        [Event "Spring Classic"]

        1. e4 e5 2. Nf3 Nf6 3. Bc4 Bc5

        """;

    try {
      StrictPgnParser.parseText(pgn);
      fail("Expected missing seven-tag roster to fail strict PGN parsing");
    } catch (final StrictPgnParserValidationException e) {
      assertEquals(StrictPgnParserValidationProblem.TAG_NOT_ALL_REQUIRED_TAGS_SET,
          e.getStrictPgnParserValidationProblem());
    }
  }

  @Test
  @SuppressWarnings("static-method")
  void pgnCreationProducesParserValidExport() {
    final PgnFile pgnFile = PgnCreate.createPgnFile(createOpeningExampleBoard());
    final String pgnFileString = PgnCreate.createPgnFileString(pgnFile);

    assertTrue(LenientPgnParser.validateText(pgnFileString).isValid());
    assertTrue(StrictPgnParser.validateText(pgnFileString).isValid());
  }

  @Test
  @SuppressWarnings("static-method")
  void lenientValidationExamplesReturnExpectedResults() {
    final var validPgn = """
        [ Event "Spring Classic"]

        1. e4 e5   2. Nf3
        Nf6
          3. Bc4 Bc5
                """;
    final LenientPgnParserValidationResult validResult = LenientPgnParser.validateText(validPgn);
    assertTrue(validResult.isValid());

    final var invalidPgn = """
        [ Event "Spring Classic"]

        1. e4 e5   2. Nf3
        Nf6
          3. Bc4 Bc5 4. X1
                """;
    final LenientPgnParserValidationResult invalidResult = LenientPgnParser.validateText(invalidPgn);
    assertFalse(invalidResult.isValid());
    assertEquals(LenientPgnParserValidationProblem.EXCEPTION_CAUGHT_FROM_STRICT_VALIDATION,
        invalidResult.problemParser());
    assertEquals(SanValidationProblem.NONE, invalidResult.problemSan());
  }

  @Test
  @SuppressWarnings("static-method")
  void strictValidationExamplesReturnExpectedResults() {
    final var validPgn = """
        [Event "Spring Classic"]
        [Site "Somewhere"]
        [Date "2024.01.01"]
        [Round "1"]
        [White "Player1"]
        [Black "Player2"]
        [Result "*"]

        1. e4 e5 2. Nf3 Nf6 3. Bc4 Bc5 *

        """;
    final StrictPgnParserValidationResult validResult = StrictPgnParser.validateText(validPgn);
    assertTrue(validResult.isValid());

    final var invalidPgn = """
        [Event "Spring Classic"]
        [Site "Somewhere"]
        [Date "2024.01.01"]
        [Round "1"]
        [White "Player1"]
        [Black "Player2"]
        [Result "*"]

        1. e4 e5 2. Nf3 Nf6 2. Bc4 Bc5 *

        """;
    final StrictPgnParserValidationResult invalidResult = StrictPgnParser.validateText(invalidPgn);
    assertFalse(invalidResult.isValid());
    assertEquals(StrictPgnParserValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_CONTINUE_AS_EXPECTED,
        invalidResult.problemParser());
    assertEquals(SanValidationProblem.NONE, invalidResult.problemSan());
  }

  private static Board calculateBoard(String pgn) {
    return PgnUtility.calculateBoardPerLastMove(LenientPgnParser.parseText(pgn));
  }

  private static Board createOpeningExampleBoard() {
    final Board board = new Board();
    board.performMoves("e4", "e5", "Nf3", "Nf6", "Bc4", "Bc5");
    return board;
  }

  private static void assertUnwinnability(String fen, Side side, UnwinnableQuick expectedQuick,
      UnwinnableFull expectedFull) {
    final Board board = new Board(fen);
    assertEquals(expectedQuick, board.isUnwinnableQuick(side));
    assertEquals(expectedFull, board.isUnwinnableFull(side));
  }

  private static void assertDeadPosition(String fen, DeadPositionQuick expectedQuick, DeadPositionFull expectedFull) {
    final Board board = new Board(fen);
    assertEquals(expectedQuick, board.isDeadPositionQuick());
    assertEquals(expectedFull, board.isDeadPositionFull());
  }

  private static String tagValue(PgnFile pgnFile, String name) {
    for (final Tag tag : pgnFile.tagList()) {
      if (tag.name().equals(name)) {
        return tag.value();
      }
    }
    fail("Missing PGN tag: " + name);
    return "";
  }
}
