package com.dlb.chess.test.unwinnability.againstcha;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.dlb.chess.unwinnability.CompareAmbronaMobilityOracle;

@Disabled("Suspended for the bitboard backend release; re-enabled in Phase 9.")
class TestAmbronaMobilityOracleComparison {

  @SuppressWarnings("static-method")
  @Test
  void currentMobilityComparisonMatchesKnownBaseline() throws Exception {
    final var comparison = CompareAmbronaMobilityOracle.compare();

    assertEquals(1249, comparison.comparedFenCount());
    assertEquals(0, comparison.fenDifferenceCount());
    assertEquals(0, comparison.rowDifferenceCount());
    assertTrue(comparison.differentFenList().isEmpty());
  }
}
