package com.dlb.chess.test.pgn.report;

import org.junit.jupiter.api.Test;

import com.dlb.chess.report.Reporter;

/**
 * Smoke test for {@link Reporter#printReport(String)}. Ensures the message-bundle keys consumed by the print path
 * exist in {@code messages.properties}. Without this coverage a key drift would only surface at runtime when a user
 * actually called {@code printReport} (e.g. the README example).
 */
class TestReporterPrintReport {

  @SuppressWarnings("static-method")
  @Test
  void printReportDoesNotThrowOnSimpleGame() {
    final var pgn = """
        1. e4 e5 2. Nf3 Nf6 3. Bc4 Bc5
        """;
    Reporter.printReport(pgn);
  }
}
