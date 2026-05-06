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
 * contract for callers constructing commentary outside the parsers (manually-built {@code PgnFile} or
 * {@code PgnHalfMove}).
 *
 * <p>
 * Contract: a constructed {@code PgnCommentary} must contain none of tab, newline (LF), carriage return (CR), or other
 * ASCII control characters. Spaces are permitted, including consecutive spaces.
 */
class TestPgnCommentary {

  // -------------------------------------------------------------------------------------------------
  // Constructor — accepted values
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
  // Constructor — rejected values
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void tabIsRejected() {
    final var thrown = assertThrows(PgnCommentaryValidationException.class,
        () -> new PgnCommentary("a\tb"));
    org.junit.jupiter.api.Assertions.assertTrue(thrown.getMessage().contains("tab"),
        "Message should name the offending character: " + thrown.getMessage());
  }

  @SuppressWarnings("static-method")
  @Test
  void newlineIsRejected() {
    final var thrown = assertThrows(PgnCommentaryValidationException.class,
        () -> new PgnCommentary("a\nb"));
    org.junit.jupiter.api.Assertions.assertTrue(thrown.getMessage().contains("newline"),
        "Message should name the offending character: " + thrown.getMessage());
  }

  @SuppressWarnings("static-method")
  @Test
  void carriageReturnIsRejected() {
    assertThrows(PgnCommentaryValidationException.class, () -> new PgnCommentary("a\rb"));
  }

  @SuppressWarnings("static-method")
  @Test
  void crlfSequenceIsRejected() {
    // CRLF is two forbidden characters in a row; the constructor reports the first (CR).
    assertThrows(PgnCommentaryValidationException.class, () -> new PgnCommentary("a\r\nb"));
  }

  @SuppressWarnings("static-method")
  @Test
  void bellControlCharacterIsRejected() {
    assertThrows(PgnCommentaryValidationException.class, () -> new PgnCommentary("ab"));
  }

  @SuppressWarnings("static-method")
  @Test
  void escapeControlCharacterIsRejected() {
    assertThrows(PgnCommentaryValidationException.class, () -> new PgnCommentary("ab"));
  }

  @SuppressWarnings("static-method")
  @Test
  void delCharacterIsRejected() {
    assertThrows(PgnCommentaryValidationException.class, () -> new PgnCommentary("ab"));
  }

  // -------------------------------------------------------------------------------------------------
  // fromLenientImport — substitution + validation factory
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void fromLenientImportSubstitutesTabWithSpace() {
    assertEquals("a b", PgnCommentary.fromLenientImport("a\tb").value());
  }

  @SuppressWarnings("static-method")
  @Test
  void fromLenientImportSubstitutesNewlineWithSpace() {
    assertEquals("a b", PgnCommentary.fromLenientImport("a\nb").value());
  }

  @SuppressWarnings("static-method")
  @Test
  void fromLenientImportSubstitutesCarriageReturnWithSpace() {
    assertEquals("a b", PgnCommentary.fromLenientImport("a\rb").value());
  }

  @SuppressWarnings("static-method")
  @Test
  void fromLenientImportTreatsCrlfAsSingleSpace() {
    assertEquals("a b", PgnCommentary.fromLenientImport("a\r\nb").value());
  }

  @SuppressWarnings("static-method")
  @Test
  void fromLenientImportPreservesConsecutiveSpaces() {
    // Two tabs → two spaces — the substitution is character-level, not run-collapsing.
    assertEquals("a  b", PgnCommentary.fromLenientImport("a\t\tb").value());
  }

  @SuppressWarnings("static-method")
  @Test
  void fromLenientImportStillRejectsBell() {
    assertThrows(PgnCommentaryValidationException.class, () -> PgnCommentary.fromLenientImport("ab"));
  }

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
