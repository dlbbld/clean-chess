package com.dlb.chess.test.pgn.report;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import com.dlb.chess.report.Reporter;

class TestReporterPrintReport {

  @SuppressWarnings("static-method")
  @Test
  void printReportDoesNotThrowOnSimpleGame() {
    final var pgn = """
        1. e4 e5 2. Nf3 Nf6 3. Bc4 Bc5
        """;
    Reporter.printReport(pgn);
  }

  @SuppressWarnings("static-method")
  @Test
  void calculateReportTextReturnsNonEmptyOutput() {
    final var pgn = """
        1. e4 e5 2. Nf3 Nf6 3. Bc4 Bc5
        """;
    final String reportText = Reporter.calculateReportText(pgn);
    assertFalse(reportText.isEmpty(), "report text should not be empty");
    assertFalse(reportText.contains("\r\n"), "report text should use LF line endings, not CRLF");
  }
}
