package com.dlb.chess.test.pgn.parser.commentary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.exceptions.PgnCommentaryValidationException;
import com.dlb.chess.pgn.parser.model.PgnCommentary;

/**
 * Direct tests for the {@link PgnCommentary} value object. The parser-side behaviour is exercised in
 * {@link TestCommentaryStrict} and {@link TestCommentaryLenient}; this test class covers the programmatic-API
 * contract for callers constructing commentary outside the parsers.
 *
 * <p>
 * Contract: a constructed {@code PgnCommentary} must contain neither {@code {} nor {@code }}. Everything else is
 * permitted, including tabs, newlines, carriage returns, extended-ASCII characters, and other ASCII control
 * characters. The contract mirrors python-chess's commentary handling — preserve source bytes verbatim, only the
 * brace characters are forbidden because they would corrupt the {@code {...}} grammar on export.
 */
class TestPgnCommentary {

  // -------------------------------------------------------------------------------------------------
  // Constructor — accepted values (printing characters and ordinary whitespace)
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void emptyStringIsAccepted() {
    assertEquals("", new PgnCommentary("").value());
  }

  @SuppressWarnings("static-method")
  @Test
  void singleSpaceIsAccepted() {
    assertEquals(" ", new PgnCommentary(" ").value());
  }

  @SuppressWarnings("static-method")
  @Test
  void doubleSpacesArePreserved() {
    assertEquals("a  b", new PgnCommentary("a  b").value());
  }

  @SuppressWarnings("static-method")
  @Test
  void leadingAndTrailingSpacesArePreserved() {
    assertEquals("  hello world  ", new PgnCommentary("  hello world  ").value());
  }

  @SuppressWarnings("static-method")
  @Test
  void typicalPlainTextIsAccepted() {
    assertEquals("This is a comment.", new PgnCommentary("This is a comment.").value());
  }

  @SuppressWarnings("static-method")
  @Test
  void specialCharactersAreAccepted() {
    assertEquals("! ? + # - / .", new PgnCommentary("! ? + # - / .").value());
  }

  // -------------------------------------------------------------------------------------------------
  // Constructor — accepts whitespace previously rejected (tab / newline / CR / CRLF)
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void tabIsAcceptedAndPreservedVerbatim() {
    assertEquals("a\tb", new PgnCommentary("a\tb").value());
  }

  @SuppressWarnings("static-method")
  @Test
  void newlineIsAcceptedAndPreservedVerbatim() {
    assertEquals("line one\nline two", new PgnCommentary("line one\nline two").value());
  }

  @SuppressWarnings("static-method")
  @Test
  void carriageReturnIsAcceptedAndPreservedVerbatim() {
    assertEquals("a\rb", new PgnCommentary("a\rb").value());
  }

  @SuppressWarnings("static-method")
  @Test
  void crlfSequenceIsAcceptedAndPreservedVerbatim() {
    assertEquals("a\r\nb", new PgnCommentary("a\r\nb").value());
  }

  @SuppressWarnings("static-method")
  @Test
  void mixedWhitespaceIsAcceptedAndPreservedVerbatim() {
    assertEquals("a \t b\n c\r d", new PgnCommentary("a \t b\n c\r d").value());
  }

  // -------------------------------------------------------------------------------------------------
  // Constructor — accepts other control characters (per python-chess interop)
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void bellControlCharacterIsAccepted() {
    assertEquals("ab", new PgnCommentary("ab").value());
  }

  @SuppressWarnings("static-method")
  @Test
  void escapeControlCharacterIsAccepted() {
    assertEquals("ab", new PgnCommentary("ab").value());
  }

  @SuppressWarnings("static-method")
  @Test
  void delCharacterIsAccepted() {
    assertEquals("ab", new PgnCommentary("ab").value());
  }

  // -------------------------------------------------------------------------------------------------
  // Constructor — rejects { and } (the only forbidden characters in commentary content)
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void openingBraceIsRejected() {
    final var thrown = assertThrows(PgnCommentaryValidationException.class,
        () -> new PgnCommentary("a { b"));
    org.junit.jupiter.api.Assertions.assertTrue(thrown.getMessage().contains("opening brace"),
        "Message should name the offending character: " + thrown.getMessage());
  }

  @SuppressWarnings("static-method")
  @Test
  void closingBraceIsRejected() {
    final var thrown = assertThrows(PgnCommentaryValidationException.class,
        () -> new PgnCommentary("a } b"));
    org.junit.jupiter.api.Assertions.assertTrue(thrown.getMessage().contains("closing brace"),
        "Message should name the offending character: " + thrown.getMessage());
  }

  @SuppressWarnings("static-method")
  @Test
  void openingBraceAtStartIsRejected() {
    assertThrows(PgnCommentaryValidationException.class, () -> new PgnCommentary("{commentary"));
  }

  @SuppressWarnings("static-method")
  @Test
  void closingBraceAtEndIsRejected() {
    assertThrows(PgnCommentaryValidationException.class, () -> new PgnCommentary("commentary}"));
  }

  // -------------------------------------------------------------------------------------------------
  // EMPTY constant
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void emptyConstantIsTheEmptyString() {
    assertEquals("", PgnCommentary.EMPTY.value());
  }

  @SuppressWarnings("static-method")
  @Test
  void emptyConstantIsACanonicalSingleton() {
    // EMPTY is referenced from many sites — confirm it really is the same instance every time, not allocated lazily.
    assertSame(PgnCommentary.EMPTY, PgnCommentary.EMPTY);
  }
}
