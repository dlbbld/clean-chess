package com.dlb.chess.test.common.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.pgn.reader.enums.PgnReaderStrictValidationProblem;
import com.dlb.chess.pgn.reader.exceptions.PgnReaderStrictValidationException;
import com.dlb.chess.pgn.reader.model.Tag;
import com.dlb.chess.pgn.reader.utility.ParseTagStrictUtility;

class TestParseTagUtility {

  @SuppressWarnings("static-method")
  @Test
  void testTagFormat() {

    checkTagFormat("[White \"Alpha\"]", true);
    checkTagFormat("[White \"Alpha Zero\"]", true);
    checkTagFormat("[CustomTagName \"Alpha Zero\"]", true);
    checkTagFormat("[White \"A player name which is a bit longer\"]", true);
    checkTagFormat("[White \" Alpha\"]", true);
    checkTagFormat("[White \"Alpha \"]", true);
    checkTagFormat("[White \" Alpha \"]", true);
    checkTagFormat("[White \"\"]", true);
    checkTagFormat("[WhiteTeam \"VÃ­kingaklubburinn\"]", true);

    checkTagFormat("x[White \"Alpha Zero\"]", false);
    checkTagFormat("White \"Alpha Zero\"]", false);
    checkTagFormat("[ \"Alpha Zero\"]", false);
    checkTagFormat("[White White  \"Alpha Zero\"]", false);
    checkTagFormat("[White  \"Alpha Zero\"]", false);
    checkTagFormat("[White\"Alpha Zero\"]", false);
    checkTagFormat("[White \"\"Alpha Zero\"]", false);
    checkTagFormat("[White Alpha Zero\"]", false);
    checkTagFormat("[White \"Alpha Zero\"\"]", false);
    checkTagFormat("[White \"Alpha Zero]", false);
    checkTagFormat("[White \"Alpha Zero\"]]", false);
    checkTagFormat("[White \"Alpha Zero\"", false);
    checkTagFormat("[White \"Alpha Zero\"]x", false);

    checkTagFormat("[Event \"Live Chess\"]", true);

  }

  private static void checkTagFormat(String tag, boolean isValid) {
    var isException = false;
    try {
      ParseTagStrictUtility.validateTag(tag);
    } catch (@SuppressWarnings("unused") final PgnReaderStrictValidationException e) {
      isException = true;
    }
    if (isValid) {
      assertFalse(isException);
    } else {
      assertTrue(isException);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testReadValidTag() {

    checkTagValues("[Event \"Groningen\"]", "Event", "Groningen");
    checkTagValues("[Site \"Groningen NED\"]", "Site", "Groningen NED");
    checkTagValues("[Date \"1997.??.??\"]", "Date", "1997.??.??");
    checkTagValues("[Round \"9\"]", "Round", "9");
    checkTagValues("[White \"Pavel Blatny\"]", "White", "Pavel Blatny");
    checkTagValues("[Black \"Frank Holzke\"]", "Black", "Frank Holzke");
    checkTagValues("[Result \"1/2-1/2\"]", "Result", "1/2-1/2");
    checkTagValues("[ECO \"A15\"]", "ECO", "A15");
    checkTagValues("[PlyCount \"127\"]", "PlyCount", "127");
    checkTagValues("[EventDate \"1997.??.??\"]", "EventDate", "1997.??.??");
    checkTagValues("[EventDate \"1997.??.??\"]", "EventDate", "1997.??.??");

    checkTagValues("[Event \"Live Chess\"]", "Event", "Live Chess");

    checkTagValues("[Setup \"0\"]", "Setup", "0");
    checkTagValues("[Setup \"1\"]", "Setup", "1");
    checkTagValues("[Setup \"abc\"]", "Setup", "abc");

    checkTagValues("[FEN \"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1\"]", "FEN",
        FenConstants.FEN_INITIAL_STR);
    checkTagValues("[FEN \"rnbqk1nr/pppp1ppp/8/3Np3/1b6/2NP4/PPP1PPPP/R1BQKB1R b KQkq - 0 11\"]", "FEN",
        "rnbqk1nr/pppp1ppp/8/3Np3/1b6/2NP4/PPP1PPPP/R1BQKB1R b KQkq - 0 11");
    checkTagValues("[FEN \"3k4/8/8/8/3K4/3R4/8/8 w - - 0 100\"]", "FEN", "3k4/8/8/8/3K4/3R4/8/8 w - - 0 100");
    checkTagValues("[FEN \"abc\"]", "FEN", "abc");

    // white space allowed in tag value
    checkTagValues("[tagName \"abc  abc\"]", "tagName", "abc  abc");
    checkTagValues("[tagName \"abc abc \"]", "tagName", "abc abc ");
    checkTagValues("[tagName \"abc  abc  \"]", "tagName", "abc  abc  ");
    checkTagValues("[tagName \" abc  abc  \"]", "tagName", " abc  abc  ");
    checkTagValues("[tagName \"  abc  abc  \"]", "tagName", "  abc  abc  ");

  }

  private static void checkTagValues(String tagStr, String expectedTagName, String expectedTagValue) {
    var isException = false;
    try {
      ParseTagStrictUtility.validateTag(tagStr);
    } catch (@SuppressWarnings("unused") final PgnReaderStrictValidationException e) {
      isException = true;
    }
    assertFalse(isException);

    final Tag tag = ParseTagStrictUtility.validateTag(tagStr);
    assertEquals(expectedTagName, tag.name());
    assertEquals(expectedTagValue, tag.value());
  }

  @SuppressWarnings("static-method")
  @Test
  void testTagNameLength() {
    // 254
    checkTagFormat(
        "[tagName0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456 \"tagNameValue\"]",
        true);
    // 255
    checkTagFormat(
        "[tagName01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567 \"tagNameValue\"]",
        true);

    // 256
    checkTagFormat(
        "[tagName012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678 \"tagNameValue\"]",
        false);
    checkTagNameLength(
        "[tagName012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678 \"tagNameValue\"]");

    // 257
    checkTagFormat(
        "[tagName0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789 \"tagNameValue\"]",
        false);
    checkTagNameLength(
        "[tagName0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789 \"tagNameValue\"]");

    // 357
    checkTagFormat(
        "[tagName01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789 \"tagNameValue\"]",
        false);
    checkTagNameLength(
        "[tagName01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789 \"tagNameValue\"]");

  }

  @SuppressWarnings("static-method")
  @Test
  void testTagNameFirstCharacter() {
    checkException("[_vent \"Live Chess\"]", PgnReaderStrictValidationProblem.TAG_NAME_FIRST_CHARACTER);
    checkException("[+vent \"Live Chess\"]", PgnReaderStrictValidationProblem.TAG_NAME_FIRST_CHARACTER);
    checkException("[#vent \"Live Chess\"]", PgnReaderStrictValidationProblem.TAG_NAME_FIRST_CHARACTER);
    checkException("[=vent \"Live Chess\"]", PgnReaderStrictValidationProblem.TAG_NAME_FIRST_CHARACTER);
    checkException("[:vent \"Live Chess\"]", PgnReaderStrictValidationProblem.TAG_NAME_FIRST_CHARACTER);
    checkException("[-vent \"Live Chess\"]", PgnReaderStrictValidationProblem.TAG_NAME_FIRST_CHARACTER);
  }

  @SuppressWarnings("static-method")
  @Test
  void testTagNameSecondCharacter() {
    checkTagFormat("[E_vent \"Live Chess\"]", true);
    checkTagFormat("[E+vent \"Live Chess\"]", true);
    checkTagFormat("[E#vent \"Live Chess\"]", true);
    checkTagFormat("[E=vent \"Live Chess\"]", true);
    checkTagFormat("[E:vent \"Live Chess\"]", true);
    checkTagFormat("[E-vent \"Live Chess\"]", true);
  }

  @SuppressWarnings("static-method")
  @Test
  void testSquareBrackes() {
    checkException("Event \"Live Chess\"]",
        PgnReaderStrictValidationProblem.TAG_FORMAT_NOT_STARTING_WITH_LEFT_SQUARE_BRACKET);
    checkException("]Event \"Live Chess\"]",
        PgnReaderStrictValidationProblem.TAG_FORMAT_NOT_STARTING_WITH_LEFT_SQUARE_BRACKET);

    checkException("[Event \"Live Chess\"",
        PgnReaderStrictValidationProblem.TAG_FORMAT_NOT_ENDING_WITH_RIGHT_SQUARE_BRACKET);
    checkException("[Event \"Live Chess\"[",
        PgnReaderStrictValidationProblem.TAG_FORMAT_NOT_ENDING_WITH_RIGHT_SQUARE_BRACKET);
  }

  private static void checkTagNameLength(String tagLine) {
    checkException(tagLine, PgnReaderStrictValidationProblem.TAG_NAME_EXCEEDS_MAXIMUM_LENGTH);
  }

  private static void checkException(String tagLine, PgnReaderStrictValidationProblem validationProblem) {
    var isException = false;
    try {
      ParseTagStrictUtility.validateTag(tagLine);
    } catch (final PgnReaderStrictValidationException e) {
      assertEquals(e.getPgnReaderStrictValidationProblem(), validationProblem);
      isException = true;
    }
    assertTrue(isException);
  }
}
