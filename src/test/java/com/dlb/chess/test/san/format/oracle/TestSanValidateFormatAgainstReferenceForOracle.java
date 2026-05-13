package com.dlb.chess.test.san.format.oracle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.Nulls;
import com.dlb.chess.san.SanParse;
import com.dlb.chess.san.SanValidateFormat;
import com.dlb.chess.test.san.reference.SanValidateFormatReference;
import com.dlb.chess.test.san.validate.statically.format.calculate.SanValidateStaticallyFormat;

class TestSanValidateFormatAgainstReferenceForOracle {

  @SuppressWarnings("static-method")
  @Test
  void testAgainstReferenceForOracle() {
    for (final String san : Nulls.keySet(SanValidateStaticallyFormat.getSanValidationMap())) {
      final SanParse actual = SanValidateFormat.validateFormat(san);
      final SanParse expected = SanValidateFormatReference.validateFormat(san);
      assertEquals(expected, actual, "validateFormat result differs from reference for SAN: \"" + san + "\"");
    }
  }
}
