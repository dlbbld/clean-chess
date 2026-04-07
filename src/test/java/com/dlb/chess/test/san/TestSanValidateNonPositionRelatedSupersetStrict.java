package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.san.SanValidateFormat;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.test.san.validate.statically.strict.calculate.SanValidateStaticallyStrict;

class TestSanValidateNonPositionRelatedSupersetStrict extends AbstractTestSanValidate {

  @SuppressWarnings("static-method")
  @Test
  void testStaticallySubsetRuntime() {
    for (final Entry<String, SanParse> entry : NonNullWrapperCommon
        .entrySet(SanValidateStaticallyStrict.getSanValidationWhiteMap())) {
      checkStaticallySubsetRuntime(NonNullWrapperCommon.getKey(entry), NonNullWrapperCommon.getValue(entry));
    }
    for (final Entry<String, SanParse> entry : NonNullWrapperCommon
        .entrySet(SanValidateStaticallyStrict.getSanValidationBlackMap())) {
      checkStaticallySubsetRuntime(NonNullWrapperCommon.getKey(entry), NonNullWrapperCommon.getValue(entry));
    }
  }

  private static void checkStaticallySubsetRuntime(String san, SanParse staticResult) {

    boolean isRuntimeException;
    try {
      final var sanParse = SanValidateFormat.validateFormat(san);
      assertEquals(sanParse, staticResult);
      isRuntimeException = false;
    } catch (@SuppressWarnings("unused") final SanValidationException e) {
      isRuntimeException = true;
    }
    assertFalse(isRuntimeException);

  }

}