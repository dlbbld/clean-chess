package com.dlb.chess.test.unwinnability.againstcha;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.dlb.chess.unwinnability.CompareAmbronaSemiStaticOracle;

@Disabled("Suspended for the bitboard backend release; re-enabled in Phase 9.")
class TestAmbronaSemiStaticOracleComparison {

  @SuppressWarnings("static-method")
  @Test
  void currentSemiStaticComparisonMatchesKnownBaseline() throws Exception {
    final var comparison = CompareAmbronaSemiStaticOracle.compare();

    assertEquals(1249, comparison.comparedFenCount());
    assertEquals(0, comparison.fenDifferenceCount());
    assertEquals(0, comparison.rowDifferenceCount());
    assertTrue(comparison.differenceCountByKind().isEmpty());
    assertTrue(comparison.differentFenList().isEmpty());
  }
}
