package com.dlb.chess.test.common.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.enums.MoveSuffixAnnotation;
import com.dlb.chess.model.MovetextParse;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.reader.enums.PgnReaderStrictValidationProblem;
import com.dlb.chess.pgn.reader.exceptions.PgnReaderStrictValidationException;
import com.dlb.chess.utility.MovetextUtility;

class TestMovetextUtilityUsingText extends AbstractTestMovetextUtility {

  // TODO we should also test the leading commentary using the movetextPart not only using PGN

  @SuppressWarnings({ "static-method" })
  @Test
  void testInitialException() {

    checkInitialException(" 1. e4 e5",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_CORRECT);
    checkInitialException(". e4 e5", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_CORRECT);
    checkInitialException(" e4 e5", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_CORRECT);
    checkInitialException("e4 e5", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_CORRECT);
    checkInitialException("2. e4 e5",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_CORRECT);

    checkInitialException("0. e4 e5",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_CORRECT);
    checkInitialException("x. e4 e5",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_CORRECT);
    checkInitialException("78902342. e4 e5",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_CORRECT);

    checkInitialException("1. e4 e5 3. d4 d5",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_CONTINUE_AS_EXPECTED);
    checkInitialException("1. e4 e5 2. d4 d5 2. Nc3 Nc6",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_CONTINUE_AS_EXPECTED);
    checkInitialException("1. e4 e5 2. d4 d5 1. Nc3 Nc6",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_CONTINUE_AS_EXPECTED);
    checkInitialException("1. e4 e5 2. d4 d5 3. Nc3 Nc6 99. Nf3 Nf6",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_CONTINUE_AS_EXPECTED);
    checkInitialException("1. e4 e5 2. d4 d5 3. Nc3 Nc6 1. Nf3 Nf6",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_CONTINUE_AS_EXPECTED);

    checkInitialException("1. ", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_ENDS_AFTER);
    checkInitialException("1. e", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID);
    checkInitialException("1.  e4", PgnReaderStrictValidationProblem.MOVETEXT_SAN_CHARACTER_INVALID);
    checkInitialException("1. e4 ", PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER);
    checkInitialException("1. e4 e5 ", PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER);
    checkInitialException("1. e4 e5  2.",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_CONTINUE_AS_EXPECTED);
    checkInitialException("1. e4 e5 2. ", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_ENDS_AFTER);
    checkInitialException("1. e4 e5 2. d", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID);
    checkInitialException("1. e4 e5 2.  d4", PgnReaderStrictValidationProblem.MOVETEXT_SAN_CHARACTER_INVALID);
    checkInitialException("1. e4 e5 2. d4 ",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER);
    checkInitialException("1. e4 e5 2. d4 d", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID);
    checkInitialException("1. e4 e5 2. d4  d5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_CHARACTER_INVALID);
    checkInitialException("1. e4 e5 2. d4 d5 ",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER);

    checkInitialException("1. ", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_ENDS_AFTER);
    checkInitialException("1. e4 e5 2. ", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_ENDS_AFTER);
    checkInitialException("1. e4 e5 2. d4 d5 3. ", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_ENDS_AFTER);

    checkInitialException("1. ! e5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_EMPTY);
    checkInitialException("1. !! e5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_EMPTY);
    checkInitialException("1. e5 ?", PgnReaderStrictValidationProblem.MOVETEXT_SAN_EMPTY);
    checkInitialException("1. e4 ??", PgnReaderStrictValidationProblem.MOVETEXT_SAN_EMPTY);

    checkInitialException("1. e e5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID);
    checkInitialException("1. d e5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID);
    checkInitialException("1. 1 e5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID);

    checkInitialException("1. e2345678 e5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID);
    checkInitialException("1. aaaaaaaa e5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID);
    checkInitialException("1. -------- e5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID);
    checkInitialException("1. +-=Oe822 e5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID);
    checkInitialException("1. eeeeeeeeeeeeeeeeeeeeeeeee e5",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID);

    checkInitialException("1. e4!!! e5", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID);
    checkInitialException("1. e4!-? e5", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID);
    checkInitialException("1. e4?a e5", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID);
    checkInitialException("1. e4!a e5", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID);

    checkInitialException("1. e4 e5!!!", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID);
    checkInitialException("1. e4!-? e5", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID);
    checkInitialException("1. e4?a e5", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID);
    checkInitialException("1. e4!a e5", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID);

    checkInitialException("1. e4! ", PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER);
    checkInitialException("1. e4?? ", PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER);
    checkInitialException("1. e4 e5? ", PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER);
    checkInitialException("1. e4 e5?? ",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER);

  }

  private static void checkInitialException(String movetextPart, PgnReaderStrictValidationProblem expected) {
    var isException = false;
    try {
      parseMoveList(movetextPart);
    } catch (final PgnReaderStrictValidationException e) {
      final PgnReaderStrictValidationProblem actual = e.getPgnReaderStrictValidationProblem();
      assertEquals(expected, actual);
      isException = true;
    }
    assertTrue(isException);
  }

  @SuppressWarnings({ "static-method" })
  @Test
  void testNonInitialException() {
    checkNonInitialException("2. e4", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_CORRECT,
        1, WHITE);
    checkNonInitialException("1. e4", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_CORRECT,
        2, WHITE);
    checkNonInitialException("25. e4",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_CORRECT, 50, WHITE);
    checkNonInitialException("50. e4",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_CORRECT, 25, WHITE);

    checkNonInitialException("2... e4",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_CORRECT, 1, BLACK);
    checkNonInitialException("1... e4",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_CORRECT, 2, BLACK);
    checkNonInitialException("25... e4",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_CORRECT, 50, BLACK);
    checkNonInitialException("50... e4",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_CORRECT, 25, BLACK);

    checkNonInitialException("1... e4",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_WHITE_MOVE, 1, WHITE);
    checkNonInitialException("2... e4",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_WHITE_MOVE, 2, WHITE);
    checkNonInitialException("50... e4",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_WHITE_MOVE, 50, WHITE);
    checkNonInitialException("25... e4",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_WHITE_MOVE, 25, WHITE);

    checkNonInitialException("1. e4",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_BLACK_MOVE, 1, BLACK);
    checkNonInitialException("2. e4",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_BLACK_MOVE, 2, BLACK);
    checkNonInitialException("50. e4",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_BLACK_MOVE, 50, BLACK);
    checkNonInitialException("25. e4",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_BEGIN_WITH_BLACK_MOVE, 25, BLACK);

    checkNonInitialException("1... e4 ", PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER,
        1, BLACK);
    checkNonInitialException("1... e4 2. ", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_ENDS_AFTER, 1, BLACK);

    checkNonInitialException("1... e4 3",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_CONTINUE_AS_EXPECTED, 1, BLACK);
    checkNonInitialException("1... e4 3.",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_CONTINUE_AS_EXPECTED, 1, BLACK);
    checkNonInitialException("1... e4 3. d4",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_CONTINUE_AS_EXPECTED, 1, BLACK);

    // WHITE
    checkNonInitialException("10. ", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_ENDS_AFTER, 10, WHITE);
    checkNonInitialException("10. e", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10, WHITE);
    checkNonInitialException("10.  e4", PgnReaderStrictValidationProblem.MOVETEXT_SAN_CHARACTER_INVALID, 10, WHITE);
    checkNonInitialException("10. e4 ", PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER,
        10, WHITE);
    checkNonInitialException("10. e4 e5 ",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER, 10, WHITE);
    checkNonInitialException("10. e4 e5  11.",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_CONTINUE_AS_EXPECTED, 10, WHITE);
    checkNonInitialException("10. e4 e5 11. ", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_ENDS_AFTER, 10,
        WHITE);
    checkNonInitialException("10. e4 e5 11. d", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10,
        WHITE);
    checkNonInitialException("10. e4 e5 11.  d4", PgnReaderStrictValidationProblem.MOVETEXT_SAN_CHARACTER_INVALID, 10,
        WHITE);
    checkNonInitialException("10. e4 e5 11. d4 ",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER, 10, WHITE);
    checkNonInitialException("10. e4 e5 11. d4 d", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10,
        WHITE);
    checkNonInitialException("10. e4 e5 11. d4  d5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_CHARACTER_INVALID,
        10, WHITE);
    checkNonInitialException("10. e4 e5 11. d4 d5 ",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER, 10, WHITE);

    checkNonInitialException("10. ", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_ENDS_AFTER, 10, WHITE);
    checkNonInitialException("10. e4 e5 11. ", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_ENDS_AFTER, 10,
        WHITE);
    checkNonInitialException("10. e4 e5 11. d4 d5 12. ",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_ENDS_AFTER, 10, WHITE);

    checkNonInitialException("10. ! e5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_EMPTY, 10, WHITE);
    checkNonInitialException("10. !! e5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_EMPTY, 10, WHITE);
    checkNonInitialException("10. e5 ?", PgnReaderStrictValidationProblem.MOVETEXT_SAN_EMPTY, 10, WHITE);
    checkNonInitialException("10. e4 ??", PgnReaderStrictValidationProblem.MOVETEXT_SAN_EMPTY, 10, WHITE);

    checkNonInitialException("10. e e5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10, WHITE);
    checkNonInitialException("10. d e5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10, WHITE);
    checkNonInitialException("10. 1 e5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10, WHITE);

    checkNonInitialException("10. e2345678 e5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10,
        WHITE);
    checkNonInitialException("10. aaaaaaaa e5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10,
        WHITE);
    checkNonInitialException("10. -------- e5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10,
        WHITE);
    checkNonInitialException("10. +-=Oe822 e5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10,
        WHITE);
    checkNonInitialException("10. eeeeeeeeeeeeeeeeeeeeeeeee e5",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10, WHITE);

    checkNonInitialException("10. e4!!! e5", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID,
        10, WHITE);
    checkNonInitialException("10. e4!-? e5", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID,
        10, WHITE);
    checkNonInitialException("10. e4?a e5", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID,
        10, WHITE);
    checkNonInitialException("10. e4!a e5", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID,
        10, WHITE);

    checkNonInitialException("10. e4 e5!!!", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID,
        10, WHITE);
    checkNonInitialException("10. e4!-? e5", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID,
        10, WHITE);
    checkNonInitialException("10. e4?a e5", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID,
        10, WHITE);
    checkNonInitialException("10. e4!a e5", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID,
        10, WHITE);

    checkNonInitialException("10. e4! ", PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER,
        10, WHITE);
    checkNonInitialException("10. e4?? ",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER, 10, WHITE);
    checkNonInitialException("10. e4 e5? ",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER, 10, WHITE);
    checkNonInitialException("10. e4 e5?? ",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER, 10, WHITE);

    // BLACK
    checkNonInitialException("10... ", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_ENDS_AFTER, 10, BLACK);
    checkNonInitialException("10... e", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10, BLACK);
    checkNonInitialException("10...  e4", PgnReaderStrictValidationProblem.MOVETEXT_SAN_CHARACTER_INVALID, 10, BLACK);
    checkNonInitialException("10... e4 ",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER, 10, BLACK);
    checkNonInitialException("10... e4 11. e5 ",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER, 10, BLACK);
    checkNonInitialException("10... e4 e5  11.",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_CONTINUE_AS_EXPECTED, 10, BLACK);
    checkNonInitialException("10... e4 11. ", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_ENDS_AFTER, 10,
        BLACK);
    checkNonInitialException("10... e4 11. d", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10, BLACK);
    checkNonInitialException("10... e4 11.  d4", PgnReaderStrictValidationProblem.MOVETEXT_SAN_CHARACTER_INVALID, 10,
        BLACK);
    checkNonInitialException("10... e4 11. d4 ",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER, 10, BLACK);
    checkNonInitialException("10... e4 11. d4 d", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10,
        BLACK);
    checkNonInitialException("10... e4 11. d4  d5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_CHARACTER_INVALID, 10,
        BLACK);
    checkNonInitialException("10... e4 11. d4 d5 ",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER, 10, BLACK);

    checkNonInitialException("10... ", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_ENDS_AFTER, 10, BLACK);
    checkNonInitialException("10... e4 11. ", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_ENDS_AFTER, 10,
        BLACK);
    checkNonInitialException("10... e4 11. d4 d5 12. ",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_ENDS_AFTER, 10, BLACK);

    checkNonInitialException("10... !", PgnReaderStrictValidationProblem.MOVETEXT_SAN_EMPTY, 10, BLACK);
    checkNonInitialException("10... !!", PgnReaderStrictValidationProblem.MOVETEXT_SAN_EMPTY, 10, BLACK);
    checkNonInitialException("10... e5 11. ?", PgnReaderStrictValidationProblem.MOVETEXT_SAN_EMPTY, 10, BLACK);
    checkNonInitialException("10... e4 11. ??", PgnReaderStrictValidationProblem.MOVETEXT_SAN_EMPTY, 10, BLACK);

    checkNonInitialException("10... e", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10, BLACK);
    checkNonInitialException("10... d", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10, BLACK);
    checkNonInitialException("10... 1", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10, BLACK);

    checkNonInitialException("10... e2345678", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10, BLACK);
    checkNonInitialException("10... aaaaaaaa", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10, BLACK);
    checkNonInitialException("10... --------", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10, BLACK);
    checkNonInitialException("10... +-=Oe822", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10, BLACK);
    checkNonInitialException("10... eeeeeeeeeeeeeeeeeeeeeeeee",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10, BLACK);

    checkNonInitialException("10... e4!!!", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID,
        10, BLACK);
    checkNonInitialException("10... e4!-?", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID,
        10, BLACK);
    checkNonInitialException("10... e4?a", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID, 10,
        BLACK);
    checkNonInitialException("10... e4!a", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID, 10,
        BLACK);

    checkNonInitialException("10... e4 11. e5!!!",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID, 10, BLACK);
    checkNonInitialException("10... e4 11. e5!-?",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID, 10, BLACK);

    // special case
    checkNonInitialException("10... e4 11. a?a5", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10,
        BLACK);
    checkNonInitialException("10... e4 11. a!a", PgnReaderStrictValidationProblem.MOVETEXT_SAN_LENGTH_INVALID, 10,
        BLACK);

    checkNonInitialException("10... e4! ",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER, 10, BLACK);
    checkNonInitialException("10... e4?? ",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER, 10, BLACK);
    checkNonInitialException("10... e4 ",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER, 10, BLACK);
    checkNonInitialException("10... e4 11. e5 ",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_FOLLOWED_BY_SPACE_BUT_ENDS_AFTER, 10, BLACK);
  }

  private static void checkNonInitialException(String movetextPart, PgnReaderStrictValidationProblem expected,
      int startFullMoveNumber, Side havingMove) {
    var isException = false;
    try {
      MovetextUtility.parseMovetextAfterInitialComment(movetextPart, startFullMoveNumber, havingMove, true);
    } catch (final PgnReaderStrictValidationException e) {
      final PgnReaderStrictValidationProblem actual = e.getPgnReaderStrictValidationProblem();
      assertEquals(expected, actual);
      isException = true;
    }
    assertTrue(isException);

  }

  @SuppressWarnings({ "static-method", "null" })
  @Test
  void testInitialWithoutCommentary() {

    checkInitialWithoutCommentary("", Arrays.asList(), Arrays.asList());
    checkInitialWithoutCommentary("1. e4", Arrays.asList("e4"), Arrays.asList());
    checkInitialWithoutCommentary("1. e4 e5", Arrays.asList("e4"), Arrays.asList("e5"));
    checkInitialWithoutCommentary("1. e4 e5 2. d4", Arrays.asList("e4", "d4"), Arrays.asList("e5"));
    checkInitialWithoutCommentary("1. e4 e5 2. d4 d5", Arrays.asList("e4", "d4"), Arrays.asList("e5", "d5"));
    checkInitialWithoutCommentary("1. e4 e5 2. d4 d5 3. Nc3", Arrays.asList("e4", "d4", "Nc3"),
        Arrays.asList("e5", "d5"));
    checkInitialWithoutCommentary("1. e4 e5 2. d4 d5 3. Nc3 Nc6", Arrays.asList("e4", "d4", "Nc3"),
        Arrays.asList("e5", "d5", "Nc6"));
    checkInitialWithoutCommentary("1. e4 e5 2. d4 d5 3. Nc3 Nc6 4. a4", Arrays.asList("e4", "d4", "Nc3", "a4"),
        Arrays.asList("e5", "d5", "Nc6"));
    checkInitialWithoutCommentary("1. e4 e5 2. d4 d5 3. Nc3 Nc6 4. a4 h5", Arrays.asList("e4", "d4", "Nc3", "a4"),
        Arrays.asList("e5", "d5", "Nc6", "h5"));
    checkInitialWithoutCommentary("1. e4 e5 2. d4 d5 3. Nc3 Nc6 4. a4 h5 5. Ra2",
        Arrays.asList("e4", "d4", "Nc3", "a4", "Ra2"), Arrays.asList("e5", "d5", "Nc6", "h5"));
    checkInitialWithoutCommentary("1. e4 e5 2. d4 d5 3. Nc3 Nc6 4. a4 h5 5. Ra2 Rh7",
        Arrays.asList("e4", "d4", "Nc3", "a4", "Ra2"), Arrays.asList("e5", "d5", "Nc6", "h5", "Rh7"));

    checkInitialWithoutCommentary(
        "1. e4 e5 2. d4 d5 3. Nc3 Nc6 4. a4 h5 5. Ra2 Rh7 6. a5 h4 7. Ra3 Rh6 8. a6 h3 9. Ra4 Rh5 10. Ra5 Rh4 11. Ra1 Rh8 12. exd5 exd4",
        Arrays.asList("e4", "d4", "Nc3", "a4", "Ra2", "a5", "Ra3", "a6", "Ra4", "Ra5", "Ra1", "exd5"),
        Arrays.asList("e5", "d5", "Nc6", "h5", "Rh7", "h4", "Rh6", "h3", "Rh5", "Rh4", "Rh8", "exd4"));

  }

  private static void checkInitialWithoutCommentary(String movetextPart, List<String> whiteMoveSanListExpected,
      List<String> blackMoveSanListExpected) {
    final MovetextParse model = parseMoveList(movetextPart);

    assertEquals(whiteMoveSanListExpected, calculateSanList(model.whiteHalfMoveList()));
    assertEquals(blackMoveSanListExpected, calculateSanList(model.blackHalfMoveList()));

    assertTrue(calculateIsEmptyCommentaryList(model.whiteHalfMoveList()));
    assertTrue(calculateIsEmptyCommentaryList(model.blackHalfMoveList()));
  }

  @SuppressWarnings({ "static-method", "null" })
  @Test
  void testInitialWithCommentary() {

    checkInitialWithCommentary("", Arrays.asList(), Arrays.asList(), Arrays.asList(), Arrays.asList());

    checkInitialWithCommentary("1. e4 {commentWhite}", Arrays.asList("e4"), Arrays.asList("commentWhite"),
        Arrays.asList(), Arrays.asList());
    checkInitialWithCommentary("1. e4 {commentWhite} e5 {commentBlack}", Arrays.asList("e4"),
        Arrays.asList("commentWhite"), Arrays.asList("e5"), Arrays.asList("commentBlack"));
    checkInitialWithCommentary("1. e4 {commentWhite} e5 {commentBlack} 2. d4 {commentWhite2}",
        Arrays.asList("e4", "d4"), Arrays.asList("commentWhite", "commentWhite2"), Arrays.asList("e5"),
        Arrays.asList("commentBlack"));
    checkInitialWithCommentary("1. e4 {commentWhite} e5 {commentBlack} 2. d4 {commentWhite2} d5 {commentBlack2}",
        Arrays.asList("e4", "d4"), Arrays.asList("commentWhite", "commentWhite2"), Arrays.asList("e5", "d5"),
        Arrays.asList("commentBlack", "commentBlack2"));

    // only white
    checkInitialWithCommentary("1. e4 {commentWhite} e5 2. d4 {commentWhite2} d5", Arrays.asList("e4", "d4"),
        Arrays.asList("commentWhite", "commentWhite2"), Arrays.asList("e5", "d5"), Arrays.asList("", ""));

    // only black
    checkInitialWithCommentary("1. e4 e5 {commentBlack} 2. d4 d5 {commentBlack2}", Arrays.asList("e4", "d4"),
        Arrays.asList("", ""), Arrays.asList("e5", "d5"), Arrays.asList("commentBlack", "commentBlack2"));

  }

  private static void checkInitialWithCommentary(String movetextPart, List<String> whiteMoveSanListExpected,
      List<String> whiteMoveCommentaryListExpected, List<String> blackMoveSanListExpected,
      List<String> blackMoveCommentaryListExpected) {
    final MovetextParse model = parseMoveList(movetextPart);

    assertEquals(whiteMoveSanListExpected, calculateSanList(model.whiteHalfMoveList()));
    assertEquals(blackMoveSanListExpected, calculateSanList(model.blackHalfMoveList()));

    assertEquals(whiteMoveCommentaryListExpected, calculateCommentaryList(model.whiteHalfMoveList()));
    assertEquals(blackMoveCommentaryListExpected, calculateCommentaryList(model.blackHalfMoveList()));
  }

  @SuppressWarnings({ "static-method", "null" })
  @Test
  void testNonInitialWithoutCommentary() {

    checkNonInitialWithoutCommentary("1. e4 e5", Arrays.asList("e4"), Arrays.asList("e5"), 1, WHITE);
    checkNonInitialWithoutCommentary("2. e4 e5", Arrays.asList("e4"), Arrays.asList("e5"), 2, WHITE);
    checkNonInitialWithoutCommentary("2. e4 e5 3. d4", Arrays.asList("e4", "d4"), Arrays.asList("e5"), 2, WHITE);
    checkNonInitialWithoutCommentary("2. e4 e5 3. d4 d5", Arrays.asList("e4", "d4"), Arrays.asList("e5", "d5"), 2,
        WHITE);
    checkNonInitialWithoutCommentary("30. e4 e5", Arrays.asList("e4"), Arrays.asList("e5"), 30, WHITE);

    checkNonInitialWithoutCommentary("1... e5", Arrays.asList(), Arrays.asList("e5"), 1, BLACK);
    checkNonInitialWithoutCommentary("1... e5 2. e4", Arrays.asList("e4"), Arrays.asList("e5"), 1, BLACK);
    checkNonInitialWithoutCommentary("1... e5 2. e4 d5", Arrays.asList("e4"), Arrays.asList("e5", "d5"), 1, BLACK);

    checkNonInitialWithoutCommentary("23... e5", Arrays.asList(), Arrays.asList("e5"), 23, BLACK);
    checkNonInitialWithoutCommentary("23... e5 24. e4", Arrays.asList("e4"), Arrays.asList("e5"), 23, BLACK);
    checkNonInitialWithoutCommentary("23... e5 24. e4 d5", Arrays.asList("e4"), Arrays.asList("e5", "d5"), 23, BLACK);
  }

  private static void checkNonInitialWithoutCommentary(String movetextPart, List<String> whiteMoveSanListExpected,
      List<String> blackMoveSanListExpected, int startFullMoveNumber, Side havingMove) {
    final MovetextParse model = MovetextUtility.parseMovetextAfterInitialComment(movetextPart, startFullMoveNumber,
        havingMove, true);

    assertEquals(whiteMoveSanListExpected, calculateSanList(model.whiteHalfMoveList()));
    assertEquals(blackMoveSanListExpected, calculateSanList(model.blackHalfMoveList()));

    assertTrue(calculateIsEmptyCommentaryList(model.whiteHalfMoveList()));
    assertTrue(calculateIsEmptyCommentaryList(model.blackHalfMoveList()));
  }

  @SuppressWarnings({ "static-method", "null" })
  @Test
  void testNonInitialWithCommentary() {

    checkNonInitialWithCommentary("1. e4 {commentWhite} e5", Arrays.asList("e4"), Arrays.asList("commentWhite"),
        Arrays.asList("e5"), Arrays.asList(""), 1, WHITE);
    checkNonInitialWithCommentary("1. e4 e5 {commentBlack}", Arrays.asList("e4"), Arrays.asList(""),
        Arrays.asList("e5"), Arrays.asList("commentBlack"), 1, WHITE);
    checkNonInitialWithCommentary("1. e4 {commentWhite} e5 {commentBlack}", Arrays.asList("e4"),
        Arrays.asList("commentWhite"), Arrays.asList("e5"), Arrays.asList("commentBlack"), 1, WHITE);

    checkNonInitialWithCommentary("2. e4 {commentWhite} e5", Arrays.asList("e4"), Arrays.asList("commentWhite"),
        Arrays.asList("e5"), Arrays.asList(""), 2, WHITE);
    checkNonInitialWithCommentary("2. e4 e5 {commentBlack}", Arrays.asList("e4"), Arrays.asList(""),
        Arrays.asList("e5"), Arrays.asList("commentBlack"), 2, WHITE);
    checkNonInitialWithCommentary("2. e4 {commentWhite} e5 {commentBlack}", Arrays.asList("e4"),
        Arrays.asList("commentWhite"), Arrays.asList("e5"), Arrays.asList("commentBlack"), 2, WHITE);

    checkNonInitialWithCommentary("2. e4 {commentWhite1} e5 3. d4", Arrays.asList("e4", "d4"),
        Arrays.asList("commentWhite1", ""), Arrays.asList("e5"), Arrays.asList(""), 2, WHITE);
    checkNonInitialWithCommentary("2. e4 e5 {commentBlack} 3. d4", Arrays.asList("e4", "d4"), Arrays.asList("", ""),
        Arrays.asList("e5"), Arrays.asList("commentBlack"), 2, WHITE);
    checkNonInitialWithCommentary("2. e4 e5 3. d4 {commentWhite2}", Arrays.asList("e4", "d4"),
        Arrays.asList("", "commentWhite2"), Arrays.asList("e5"), Arrays.asList(""), 2, WHITE);
    checkNonInitialWithCommentary("2. e4 {commentWhite1} e5 {commentBlack} 3. d4 {commentWhite2}",
        Arrays.asList("e4", "d4"), Arrays.asList("commentWhite1", "commentWhite2"), Arrays.asList("e5"),
        Arrays.asList("commentBlack"), 2, WHITE);

    checkNonInitialWithCommentary("2. e4 {commentWhite1} e5 3. d4 d5", Arrays.asList("e4", "d4"),
        Arrays.asList("commentWhite1", ""), Arrays.asList("e5", "d5"), Arrays.asList("", ""), 2, WHITE);
    checkNonInitialWithCommentary("2. e4 e5 {commentBlack1} 3. d4 d5", Arrays.asList("e4", "d4"), Arrays.asList("", ""),
        Arrays.asList("e5", "d5"), Arrays.asList("commentBlack1", ""), 2, WHITE);
    checkNonInitialWithCommentary("2. e4 e5 3. d4 {commentWhite2} d5", Arrays.asList("e4", "d4"),
        Arrays.asList("", "commentWhite2"), Arrays.asList("e5", "d5"), Arrays.asList("", ""), 2, WHITE);
    checkNonInitialWithCommentary("2. e4 e5 3. d4 d5 {commentBlack2}", Arrays.asList("e4", "d4"), Arrays.asList("", ""),
        Arrays.asList("e5", "d5"), Arrays.asList("", "commentBlack2"), 2, WHITE);
    checkNonInitialWithCommentary("2. e4 {commentWhite1} e5 {commentBlack1} 3. d4 {commentWhite2} d5 {commentBlack2}",
        Arrays.asList("e4", "d4"), Arrays.asList("commentWhite1", "commentWhite2"), Arrays.asList("e5", "d5"),
        Arrays.asList("commentBlack1", "commentBlack2"), 2, WHITE);

    checkNonInitialWithCommentary("30. e4 {commentWhite} e5", Arrays.asList("e4"), Arrays.asList("commentWhite"),
        Arrays.asList("e5"), Arrays.asList(""), 30, WHITE);
    checkNonInitialWithCommentary("30. e4 e5 {commentBlack}", Arrays.asList("e4"), Arrays.asList(""),
        Arrays.asList("e5"), Arrays.asList("commentBlack"), 30, WHITE);
    checkNonInitialWithCommentary("30. e4 {commentWhite} e5 {commentBlack}", Arrays.asList("e4"),
        Arrays.asList("commentWhite"), Arrays.asList("e5"), Arrays.asList("commentBlack"), 30, WHITE);

    checkNonInitialWithCommentary("1... e5 {commentBlack}", Arrays.asList(), Arrays.asList(), Arrays.asList("e5"),
        Arrays.asList("commentBlack"), 1, BLACK);

    checkNonInitialWithCommentary("1... e5 {commentBlack} 2. e4", Arrays.asList("e4"), Arrays.asList(""),
        Arrays.asList("e5"), Arrays.asList("commentBlack"), 1, BLACK);
    checkNonInitialWithCommentary("1... e5 2. e4 {commentWhite}", Arrays.asList("e4"), Arrays.asList("commentWhite"),
        Arrays.asList("e5"), Arrays.asList(""), 1, BLACK);
    checkNonInitialWithCommentary("1... e5 {commentBlack} 2. e4 {commentWhite}", Arrays.asList("e4"),
        Arrays.asList("commentWhite"), Arrays.asList("e5"), Arrays.asList("commentBlack"), 1, BLACK);

    checkNonInitialWithCommentary("1... e5 {commentBlack1} 2. e4 d5", Arrays.asList("e4"), Arrays.asList(""),
        Arrays.asList("e5", "d5"), Arrays.asList("commentBlack1", ""), 1, BLACK);
    checkNonInitialWithCommentary("1... e5 2. e4 {commentWhite} d5", Arrays.asList("e4"), Arrays.asList("commentWhite"),
        Arrays.asList("e5", "d5"), Arrays.asList("", ""), 1, BLACK);
    checkNonInitialWithCommentary("1... e5 2. e4 d5 {commentBlack2}", Arrays.asList("e4"), Arrays.asList(""),
        Arrays.asList("e5", "d5"), Arrays.asList("", "commentBlack2"), 1, BLACK);
    checkNonInitialWithCommentary("1... e5 {commentBlack1} 2. e4 {commentWhite} d5 {commentBlack2}",
        Arrays.asList("e4"), Arrays.asList("commentWhite"), Arrays.asList("e5", "d5"),
        Arrays.asList("commentBlack1", "commentBlack2"), 1, BLACK);

    checkNonInitialWithCommentary("23... e5 {commentBlack}", Arrays.asList(), Arrays.asList(), Arrays.asList("e5"),
        Arrays.asList("commentBlack"), 23, BLACK);

    checkNonInitialWithCommentary("23... e5 {commentBlack} 24. e4", Arrays.asList("e4"), Arrays.asList(""),
        Arrays.asList("e5"), Arrays.asList("commentBlack"), 23, BLACK);
    checkNonInitialWithCommentary("23... e5 24. e4 {commentWhite}", Arrays.asList("e4"), Arrays.asList("commentWhite"),
        Arrays.asList("e5"), Arrays.asList(""), 23, BLACK);
    checkNonInitialWithCommentary("23... e5 {commentBlack} 24. e4 {commentWhite}", Arrays.asList("e4"),
        Arrays.asList("commentWhite"), Arrays.asList("e5"), Arrays.asList("commentBlack"), 23, BLACK);

    checkNonInitialWithCommentary("23... e5 {commentBlack1} 24. e4 d5", Arrays.asList("e4"), Arrays.asList(""),
        Arrays.asList("e5", "d5"), Arrays.asList("commentBlack1", ""), 23, BLACK);
    checkNonInitialWithCommentary("23... e5 24. e4 {commentWhite} d5", Arrays.asList("e4"),
        Arrays.asList("commentWhite"), Arrays.asList("e5", "d5"), Arrays.asList("", ""), 23, BLACK);
    checkNonInitialWithCommentary("23... e5 24. e4 d5 {commentBlack2}", Arrays.asList("e4"), Arrays.asList(""),
        Arrays.asList("e5", "d5"), Arrays.asList("", "commentBlack2"), 23, BLACK);
    checkNonInitialWithCommentary("23... e5 {commentBlack1} 24. e4 {commentWhite} d5 {commentBlack2}",
        Arrays.asList("e4"), Arrays.asList("commentWhite"), Arrays.asList("e5", "d5"),
        Arrays.asList("commentBlack1", "commentBlack2"), 23, BLACK);
  }

  private static void checkNonInitialWithCommentary(String movetextPart, List<String> whiteMoveSanListExpected,
      List<String> whiteMoveCommentaryListExpected, List<String> blackMoveSanListExpected,
      List<String> blackMoveCommentaryListExpected, int startFullMoveNumber, Side havingMove) {
    final MovetextParse model = MovetextUtility.parseMovetextAfterInitialComment(movetextPart, startFullMoveNumber,
        havingMove, true);

    assertEquals(whiteMoveSanListExpected, calculateSanList(model.whiteHalfMoveList()));
    assertEquals(blackMoveSanListExpected, calculateSanList(model.blackHalfMoveList()));

    assertEquals(whiteMoveCommentaryListExpected, calculateCommentaryList(model.whiteHalfMoveList()));
    assertEquals(blackMoveCommentaryListExpected, calculateCommentaryList(model.blackHalfMoveList()));
  }

  @SuppressWarnings({ "static-method", "null" })
  @Test
  void testInitialAll() {
    checkInitialAll("1. e4? {commentWhite}", Arrays.asList("e4"), Arrays.asList(MoveSuffixAnnotation.MISTAKE),
        Arrays.asList("commentWhite"), Arrays.asList(), Arrays.asList(), Arrays.asList());
    checkInitialAll("1. e4! {commentWhite}", Arrays.asList("e4"), Arrays.asList(MoveSuffixAnnotation.GOOD_MOVE),
        Arrays.asList("commentWhite"), Arrays.asList(), Arrays.asList(), Arrays.asList());
    checkInitialAll("1. e4?? {commentWhite}", Arrays.asList("e4"), Arrays.asList(MoveSuffixAnnotation.BLUNDER),
        Arrays.asList("commentWhite"), Arrays.asList(), Arrays.asList(), Arrays.asList());
    checkInitialAll("1. e4?! {commentWhite}", Arrays.asList("e4"), Arrays.asList(MoveSuffixAnnotation.DUBIOUS_MOVE),
        Arrays.asList("commentWhite"), Arrays.asList(), Arrays.asList(), Arrays.asList());
    checkInitialAll("1. e4!? {commentWhite}", Arrays.asList("e4"), Arrays.asList(MoveSuffixAnnotation.INTERESTING_MOVE),
        Arrays.asList("commentWhite"), Arrays.asList(), Arrays.asList(), Arrays.asList());
    checkInitialAll("1. e4!! {commentWhite}", Arrays.asList("e4"), Arrays.asList(MoveSuffixAnnotation.BRILLIANT_MOVE),
        Arrays.asList("commentWhite"), Arrays.asList(), Arrays.asList(), Arrays.asList());

    checkInitialAll("1. e4! {commentWhite} e5? {commentBlack}", Arrays.asList("e4"),
        Arrays.asList(MoveSuffixAnnotation.GOOD_MOVE), Arrays.asList("commentWhite"), Arrays.asList("e5"),
        Arrays.asList(MoveSuffixAnnotation.MISTAKE), Arrays.asList("commentBlack"));

    checkInitialAll("1. e4! {commentWhite1} e5? {commentBlack} 2. d4!! {commentWhite2}", Arrays.asList("e4", "d4"),
        Arrays.asList(MoveSuffixAnnotation.GOOD_MOVE, MoveSuffixAnnotation.BRILLIANT_MOVE),
        Arrays.asList("commentWhite1", "commentWhite2"), Arrays.asList("e5"),
        Arrays.asList(MoveSuffixAnnotation.MISTAKE), Arrays.asList("commentBlack"));

    checkInitialAll("1. e4! {commentWhite1} e5? {commentBlack1} 2. d4!! {commentWhite2} d5?? {commentBlack2}",
        Arrays.asList("e4", "d4"), Arrays.asList(MoveSuffixAnnotation.GOOD_MOVE, MoveSuffixAnnotation.BRILLIANT_MOVE),
        Arrays.asList("commentWhite1", "commentWhite2"), Arrays.asList("e5", "d5"),
        Arrays.asList(MoveSuffixAnnotation.MISTAKE, MoveSuffixAnnotation.BLUNDER),
        Arrays.asList("commentBlack1", "commentBlack2"));
  }

  private static void checkInitialAll(String movetextPart, List<String> whiteMoveSanListExpected,
      List<MoveSuffixAnnotation> whiteMoveSuffixAnnotationList, List<String> whiteMoveCommentaryListExpected,
      List<String> blackMoveSanListExpected, List<MoveSuffixAnnotation> blackMoveSuffixAnnotationList,
      List<String> blackMoveCommentaryListExpected) {
    final MovetextParse model = parseMoveList(movetextPart);

    assertEquals(whiteMoveSanListExpected, calculateSanList(model.whiteHalfMoveList()));
    assertEquals(blackMoveSanListExpected, calculateSanList(model.blackHalfMoveList()));

    assertEquals(whiteMoveSuffixAnnotationList, calculateMoveSuffixAnnotationList(model.whiteHalfMoveList()));
    assertEquals(blackMoveSuffixAnnotationList, calculateMoveSuffixAnnotationList(model.blackHalfMoveList()));

    assertEquals(whiteMoveCommentaryListExpected, calculateCommentaryList(model.whiteHalfMoveList()));
    assertEquals(blackMoveCommentaryListExpected, calculateCommentaryList(model.blackHalfMoveList()));
  }

  @SuppressWarnings({ "static-method", "null" })
  @Test
  void testNonInitialAll() {

    // WHITE
    checkNonInitialAll("10. e4? {commentWhite}", Arrays.asList("e4"), Arrays.asList(MoveSuffixAnnotation.MISTAKE),
        Arrays.asList("commentWhite"), Arrays.asList(), Arrays.asList(), Arrays.asList(), 10, WHITE);
    checkNonInitialAll("10. e4! {commentWhite}", Arrays.asList("e4"), Arrays.asList(MoveSuffixAnnotation.GOOD_MOVE),
        Arrays.asList("commentWhite"), Arrays.asList(), Arrays.asList(), Arrays.asList(), 10, WHITE);
    checkNonInitialAll("10. e4+?? {commentWhite}", Arrays.asList("e4+"), Arrays.asList(MoveSuffixAnnotation.BLUNDER),
        Arrays.asList("commentWhite"), Arrays.asList(), Arrays.asList(), Arrays.asList(), 10, WHITE);
    checkNonInitialAll("10. e4#?! {commentWhite}", Arrays.asList("e4#"),
        Arrays.asList(MoveSuffixAnnotation.DUBIOUS_MOVE), Arrays.asList("commentWhite"), Arrays.asList(),
        Arrays.asList(), Arrays.asList(), 10, WHITE);
    checkNonInitialAll("10. e4!? {commentWhite}", Arrays.asList("e4"),
        Arrays.asList(MoveSuffixAnnotation.INTERESTING_MOVE), Arrays.asList("commentWhite"), Arrays.asList(),
        Arrays.asList(), Arrays.asList(), 10, WHITE);
    checkNonInitialAll("10. e4!! {commentWhite}", Arrays.asList("e4"),
        Arrays.asList(MoveSuffixAnnotation.BRILLIANT_MOVE), Arrays.asList("commentWhite"), Arrays.asList(),
        Arrays.asList(), Arrays.asList(), 10, WHITE);

    checkNonInitialAll("10. e4! {commentWhite} e5? {commentBlack}", Arrays.asList("e4"),
        Arrays.asList(MoveSuffixAnnotation.GOOD_MOVE), Arrays.asList("commentWhite"), Arrays.asList("e5"),
        Arrays.asList(MoveSuffixAnnotation.MISTAKE), Arrays.asList("commentBlack"), 10, WHITE);

    checkNonInitialAll("10. e4! {commentWhite1} e5? {commentBlack} 11. d4!! {commentWhite2}", Arrays.asList("e4", "d4"),
        Arrays.asList(MoveSuffixAnnotation.GOOD_MOVE, MoveSuffixAnnotation.BRILLIANT_MOVE),
        Arrays.asList("commentWhite1", "commentWhite2"), Arrays.asList("e5"),
        Arrays.asList(MoveSuffixAnnotation.MISTAKE), Arrays.asList("commentBlack"), 10, WHITE);

    checkNonInitialAll("10. e4! {commentWhite1} e5? {commentBlack1} 11. d4!! {commentWhite2} d5?? {commentBlack2}",
        Arrays.asList("e4", "d4"), Arrays.asList(MoveSuffixAnnotation.GOOD_MOVE, MoveSuffixAnnotation.BRILLIANT_MOVE),
        Arrays.asList("commentWhite1", "commentWhite2"), Arrays.asList("e5", "d5"),
        Arrays.asList(MoveSuffixAnnotation.MISTAKE, MoveSuffixAnnotation.BLUNDER),
        Arrays.asList("commentBlack1", "commentBlack2"), 10, WHITE);

    // BLACK
    checkNonInitialAll("10... e4? {commentBlack}", Arrays.asList(), Arrays.asList(), Arrays.asList(),
        Arrays.asList("e4"), Arrays.asList(MoveSuffixAnnotation.MISTAKE), Arrays.asList("commentBlack"), 10, BLACK);
    checkNonInitialAll("10... e4! {commentBlack}", Arrays.asList(), Arrays.asList(), Arrays.asList(),
        Arrays.asList("e4"), Arrays.asList(MoveSuffixAnnotation.GOOD_MOVE), Arrays.asList("commentBlack"), 10, BLACK);
    checkNonInitialAll("10... e4+?? {commentBlack}", Arrays.asList(), Arrays.asList(), Arrays.asList(),
        Arrays.asList("e4+"), Arrays.asList(MoveSuffixAnnotation.BLUNDER), Arrays.asList("commentBlack"), 10, BLACK);
    checkNonInitialAll("10... e4#?! {commentBlack}", Arrays.asList(), Arrays.asList(), Arrays.asList(),
        Arrays.asList("e4#"), Arrays.asList(MoveSuffixAnnotation.DUBIOUS_MOVE), Arrays.asList("commentBlack"), 10,
        BLACK);
    checkNonInitialAll("10... e4!? {commentBlack}", Arrays.asList(), Arrays.asList(), Arrays.asList(),
        Arrays.asList("e4"), Arrays.asList(MoveSuffixAnnotation.INTERESTING_MOVE), Arrays.asList("commentBlack"), 10,
        BLACK);
    checkNonInitialAll("10... e4!! {commentBlack}", Arrays.asList(), Arrays.asList(), Arrays.asList(),
        Arrays.asList("e4"), Arrays.asList(MoveSuffixAnnotation.BRILLIANT_MOVE), Arrays.asList("commentBlack"), 10,
        BLACK);

    checkNonInitialAll("10... e5! {commentBlack} 11. e4? {commentWhite}", Arrays.asList("e4"),
        Arrays.asList(MoveSuffixAnnotation.MISTAKE), Arrays.asList("commentWhite"), Arrays.asList("e5"),
        Arrays.asList(MoveSuffixAnnotation.GOOD_MOVE), Arrays.asList("commentBlack"), 10, BLACK);

    checkNonInitialAll("10... e5! {commentBlack1} 11. e4? {commentWhite} a6?? {commentBlack2}", Arrays.asList("e4"),
        Arrays.asList(MoveSuffixAnnotation.MISTAKE), Arrays.asList("commentWhite"), Arrays.asList("e5", "a6"),
        Arrays.asList(MoveSuffixAnnotation.GOOD_MOVE, MoveSuffixAnnotation.BLUNDER),
        Arrays.asList("commentBlack1", "commentBlack2"), 10, BLACK);

    checkNonInitialAll(
        "10... e5! {commentBlack1} 11. e4? {commentWhite1} a6?? {commentBlack2} 12. Qd4+!! {commentWhite2}",
        Arrays.asList("e4", "Qd4+"), Arrays.asList(MoveSuffixAnnotation.MISTAKE, MoveSuffixAnnotation.BRILLIANT_MOVE),
        Arrays.asList("commentWhite1", "commentWhite2"), Arrays.asList("e5", "a6"),
        Arrays.asList(MoveSuffixAnnotation.GOOD_MOVE, MoveSuffixAnnotation.BLUNDER),
        Arrays.asList("commentBlack1", "commentBlack2"), 10, BLACK);
  }

  private static void checkNonInitialAll(String movetextPart, List<String> whiteMoveSanListExpected,
      List<MoveSuffixAnnotation> whiteMoveSuffixAnnotationList, List<String> whiteMoveCommentaryListExpected,
      List<String> blackMoveSanListExpected, List<MoveSuffixAnnotation> blackMoveSuffixAnnotationList,
      List<String> blackMoveCommentaryListExpected, int startFullMoveNumber, Side havingMove) {
    final MovetextParse model = MovetextUtility.parseMovetextAfterInitialComment(movetextPart, startFullMoveNumber,
        havingMove, true);

    assertEquals(whiteMoveSanListExpected, calculateSanList(model.whiteHalfMoveList()));
    assertEquals(blackMoveSanListExpected, calculateSanList(model.blackHalfMoveList()));

    assertEquals(whiteMoveSuffixAnnotationList, calculateMoveSuffixAnnotationList(model.whiteHalfMoveList()));
    assertEquals(blackMoveSuffixAnnotationList, calculateMoveSuffixAnnotationList(model.blackHalfMoveList()));

    assertEquals(whiteMoveCommentaryListExpected, calculateCommentaryList(model.whiteHalfMoveList()));
    assertEquals(blackMoveCommentaryListExpected, calculateCommentaryList(model.blackHalfMoveList()));
  }

  private static MovetextParse parseMoveList(String movetextPart) {
    return MovetextUtility.parseMovetextAfterInitialComment(movetextPart, 1, WHITE, true);
  }

  private static boolean calculateIsEmptyCommentaryList(List<PgnHalfMove> halfMoveList) {
    for (final PgnHalfMove halfMove : halfMoveList) {
      if (!halfMove.commentary().isEmpty()) {
        return false;
      }
    }
    return true;
  }

}
