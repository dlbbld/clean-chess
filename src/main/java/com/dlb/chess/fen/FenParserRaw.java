package com.dlb.chess.fen;

import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.common.enums.FenValidationProblem;
import com.dlb.chess.common.exceptions.FenValidationException;
import com.dlb.chess.fen.model.FenRaw;

public class FenParserRaw {

  private FenParserRaw() {
  }

  public static String parsePiecePlacement(String piecePlacement) {
    return parseFenRaw(piecePlacement).piecePlacement();
  }

  public static String parseHavingMove(String fen) {
    return parseFenRaw(fen).havingMove();
  }

  public static String parseCastlingRight(String fen) {
    return parseFenRaw(fen).castlingRightBothStr();
  }

  public static String parseEnPassantCaptureTargetSquare(String fen) {
    return parseFenRaw(fen).enPassantCaptureTargetSquare();
  }

  public static String parseHalfMoveClock(String fen) {
    return parseFenRaw(fen).halfMoveClock();
  }

  public static String parseFullMoveNumber(String fen) {
    return parseFenRaw(fen).fullMoveNumber();
  }

  public static FenRaw parseFenRaw(String fen) throws FenValidationException {
    final var regExp = "^([^ ]+) ([^ ]+) ([^ ]+) ([^ ]+) ([^ ]+) ([^ ]+)$";
    final var pattern = Pattern.compile(regExp);
    final var matcher = pattern.matcher(fen);
    if (!matcher.find()) {
      throw new FenValidationException(FenValidationProblem.INVALID_FORMAT,
          "the format could not be identifed as valid FEN format");
    }
    // the regular expressions assures that these matches are not empty
    @SuppressWarnings("null") @NonNull final String piecePlacement = matcher.group(1);
    @SuppressWarnings("null") @NonNull final String havingMove = matcher.group(2);
    @SuppressWarnings("null") @NonNull final String castlingRight = matcher.group(3);
    @SuppressWarnings("null") @NonNull final String enPassantCaptureTargetSquare = matcher.group(4);
    @SuppressWarnings("null") @NonNull final String halfMoveClock = matcher.group(5);
    @SuppressWarnings("null") @NonNull final String fullMoveNumber = matcher.group(6);

    return new FenRaw(piecePlacement, havingMove, castlingRight, enPassantCaptureTargetSquare, halfMoveClock,
        fullMoveNumber);
  }

}
