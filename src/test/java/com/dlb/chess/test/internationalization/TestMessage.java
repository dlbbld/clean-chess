package com.dlb.chess.test.internationalization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.internationalization.Message;

class TestMessage implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void testBasic() {

    final var expected = "Test message 1";

    assertEquals(expected, Message.getString("test123"));
    assertEquals(expected, Message.getString("test123.test432"));
    assertEquals(expected, Message.getString("test123.432"));
    assertEquals(expected, Message.getString("test123.test432.test30"));

  }

  @SuppressWarnings("static-method")
  @Test
  void testWhitespace() {

    final var expected = "The knight is good in the attack.";

    assertEquals(expected, Message.getString("test.message.testWithLeftWhitespace"));
    assertEquals(expected, Message.getString("test.message.testWithRightWhitespace"));
    assertEquals(expected, Message.getString("test.message.testWithLeftRightWhitespace"));
    assertEquals(expected, Message.getString("test.message.testWithInbetweenWhitespace"));
    assertEquals(expected, Message.getString("test.message.testWithAllKindOfWhitespace"));

  }

  @SuppressWarnings("static-method")
  @Test
  void testPlaceholder() {

    assertEquals("The knight cannot move in this way.",
        Message.getString("test.message.testWithOnePlaceholder", "knight"));

    assertEquals("The queen is stronger than the bishop.",
        Message.getString("test.message.testWithTwoPlaceholders", "queen", "bishop"));
  }

}