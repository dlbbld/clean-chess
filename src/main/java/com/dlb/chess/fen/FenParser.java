package com.dlb.chess.fen;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.board.enums.SquareType;
import com.dlb.chess.board.model.UpdateSquare;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.BasicConstants;
import com.dlb.chess.common.constants.CastlingConstants;
import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.enums.FenValidationProblem;
import com.dlb.chess.common.exceptions.FenValidationException;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.common.utility.MaterialUtility;
import com.dlb.chess.common.utility.StaticPositionUtility;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.fen.model.FenRaw;
import com.dlb.chess.model.CastlingRightBoth;
import com.dlb.chess.moves.utility.CastlingUtility;

public class FenParser implements EnumConstants {

  private static final String REG_EXP_EMPTY_RANK = "//";
  @SuppressWarnings("null")
  private static final Pattern PATTERN_EMPTY_RANK = Pattern.compile(REG_EXP_EMPTY_RANK);

  private static final String REG_EXP_RANK = "^[RNBQKPrnbqkp12345678]+$";
  @SuppressWarnings("null")
  private static final Pattern PATTERN_RANK = Pattern.compile(REG_EXP_RANK);

  private static final String REG_EXP_SIDE = "^[wb]{1}$";
  @SuppressWarnings("null")
  private static final Pattern PATTERN_SIDE = Pattern.compile(REG_EXP_SIDE);

  private FenParser() {
  }

  public static Fen parseAdvancedFen(String fen) throws FenValidationException {
    final FenRaw fenRaw = FenParserRaw.parseFenRaw(fen);

    final var piecePlacement = fenRaw.piecePlacement();
    final StaticPosition staticPosition = validatePiecePlacement(piecePlacement);
    validateNumberOfPieces(staticPosition);

    validatePawnRankNotPromotionRank(staticPosition);

    validatePawnRankNotGroundRank(staticPosition);

    final var havingMoveCheck = fenRaw.havingMove();
    final Side havingMove = validateHavingMove(havingMoveCheck);

    if (StaticPositionUtility.calculateIsEvaluateAttackingKing(staticPosition, havingMove)) {
      throw new FenValidationException(FenValidationProblem.INVALID_POSITION_CHECK,
          "the king of the opposing player is in check");
    }

    final var castlingRightBothStr = fenRaw.castlingRightBothStr();
    final CastlingRightBoth castlingRightBoth = validateCastlingRightBoth(staticPosition, castlingRightBothStr);

    final var enPassantCaptureTargetSquareStr = fenRaw.enPassantCaptureTargetSquare();
    final Square enPassantCaptureTargetSquare = validateEnPassantCaptureTargetSquare(staticPosition,
        enPassantCaptureTargetSquareStr, havingMove);

    final var halfMoveClockStr = fenRaw.halfMoveClock();
    final var halfMoveClock = validateHalfMoveClock(halfMoveClockStr, enPassantCaptureTargetSquare);

    final var fullMoveNumberStr = fenRaw.fullMoveNumber();
    final var fullMoveNumber = validateFullMoveNumber(fen, havingMove, fullMoveNumberStr);

    final var maximumPossibleHalfMoveClock = calculateMaximumPossibleHalfMoveClock(fullMoveNumber, havingMove);

    if (halfMoveClock > maximumPossibleHalfMoveClock) {
      throw new FenValidationException(
          FenValidationProblem.INVALID_HALF_MOVE_CLOCK_TOO_BIG_RELATIVE_TO_FULL_MOVE_NUMBER,
          "the half-move clock \"" + halfMoveClockStr + "\" is greater than the maximum possible half-move clock \""
              + maximumPossibleHalfMoveClock + "\" for the specified full-move counter of " + fullMoveNumber);
    }

    return new Fen(fen, staticPosition, havingMove, castlingRightBoth, enPassantCaptureTargetSquare, halfMoveClock,
        fullMoveNumber);
  }

  private static int calculateMaximumPossibleHalfMoveClock(int fullMoveNumber, Side havingMove) {

    final var baseValue = 2 * (fullMoveNumber - 1);
    switch (havingMove) {
      case BLACK:
        return baseValue + 1;
      case WHITE:
        return baseValue;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }

  }

  // important semantically
  // we only check if the string is correct regarding format
  // we do not validate if the position actually makes sense, like two kings of
  // same color or no king or both kings in
  // check, too much pawns etc.
  public static StaticPosition validatePiecePlacement(String piecePlacement) throws FenValidationException {

    if (piecePlacement.endsWith("/")) {
      throw new FenValidationException(FenValidationProblem.INVALID_POSITION_ENDS_WITH_FORWARD_SLASH,
          "it ends with a slash");
    }

    // we check for empty ranks before the rank count, so we can avoid counting
    // empty ranks
    final var matcherEmptyRank = PATTERN_EMPTY_RANK.matcher(piecePlacement);
    if (matcherEmptyRank.find()) {
      throw new FenValidationException(FenValidationProblem.INVALID_POSITION_EMPTY_RANK, "it contains empty ranks");
    }

    final var rankDescriptionList = piecePlacement.split("/");

    if (rankDescriptionList.length != 8) {
      throw new FenValidationException(FenValidationProblem.INVALID_POSITION_NUMBER_OF_RANKS,
          "it does not specify eight ranks");
    }

    var rankNumber = 0;
    for (final String rankDescription : rankDescriptionList) {
      rankNumber++;
      final var matcherRank = PATTERN_RANK.matcher(rankDescription);
      if (!matcherRank.find()) {
        throw new FenValidationException(FenValidationProblem.INVALID_POSITION_UNKNOWN_CHAR,
            "the rank " + rankNumber + " contains invalid chars");
      }
    }

    final List<List<String>> evaluatedRankList = new ArrayList<>();
    for (final String rankDescription : rankDescriptionList) {
      @SuppressWarnings("null") @NonNull final String rankDescriptionNonNull = rankDescription;
      final List<String> evaluatedRank = validateEvaluatedLength(rankDescriptionNonNull);
      evaluatedRankList.add(evaluatedRank);
    }

    final List<Piece> pieceList = new ArrayList<>();
    for (final List<String> evaluatedRank : evaluatedRankList) {
      pieceList.addAll(convertRankDescriptionEvaluatedToRank(evaluatedRank));
    }

    if (pieceList.size() != 64) {
      throw new ProgrammingMistakeException("The piece list construction is incorrect");
    }

    return new StaticPosition(pieceList.get(0), pieceList.get(1), pieceList.get(2), pieceList.get(3), pieceList.get(4),
        pieceList.get(5), pieceList.get(6), pieceList.get(7), pieceList.get(8), pieceList.get(9), pieceList.get(10),
        pieceList.get(11), pieceList.get(12), pieceList.get(13), pieceList.get(14), pieceList.get(15),
        pieceList.get(16), pieceList.get(17), pieceList.get(18), pieceList.get(19), pieceList.get(20),
        pieceList.get(21), pieceList.get(22), pieceList.get(23), pieceList.get(24), pieceList.get(25),
        pieceList.get(26), pieceList.get(27), pieceList.get(28), pieceList.get(29), pieceList.get(30),
        pieceList.get(31), pieceList.get(32), pieceList.get(33), pieceList.get(34), pieceList.get(35),
        pieceList.get(36), pieceList.get(37), pieceList.get(38), pieceList.get(39), pieceList.get(40),
        pieceList.get(41), pieceList.get(42), pieceList.get(43), pieceList.get(44), pieceList.get(45),
        pieceList.get(46), pieceList.get(47), pieceList.get(48), pieceList.get(49), pieceList.get(50),
        pieceList.get(51), pieceList.get(52), pieceList.get(53), pieceList.get(54), pieceList.get(55),
        pieceList.get(56), pieceList.get(57), pieceList.get(58), pieceList.get(59), pieceList.get(60),
        pieceList.get(61), pieceList.get(62), pieceList.get(63));
  }

  private static List<Piece> convertRankDescriptionEvaluatedToRank(List<String> rankDescriptionEvaluated) {
    if (rankDescriptionEvaluated.size() != 8) {
      throw new ProgrammingMistakeException("The rank description evaluated must consist of exactly eight characters");
    }
    final List<Piece> rankPieceList = new ArrayList<>();
    for (final String letter : rankDescriptionEvaluated) {
      if (BasicConstants.BLANK.equals(letter)) {
        rankPieceList.add(Piece.NONE);
      } else {
        if (!Piece.exists(letter)) {
          throw new ProgrammingMistakeException(
              "An unknown piece was found which was not filtered before by regular expression");
        }
        final Piece piece = Piece.calculate(letter);
        rankPieceList.add(piece);
      }
    }

    // post condition
    if (rankPieceList.size() != 8) {
      throw new ProgrammingMistakeException("Post condition of eight elements for rank evaluation not met");
    }

    return rankPieceList;

  }

  private static List<String> validateEvaluatedLength(String rankDescription) throws FenValidationException {
    final List<String> squareDescriptionList = new ArrayList<>();

    var countEvaluatedLength = 0;

    for (var i = 0; i < rankDescription.length(); i++) {
      final String currentChar = NonNullWrapperCommon.substring(rankDescription, i, i + 1);
      try {
        final var numberOfEmptyFields = Integer.parseInt(currentChar);
        countEvaluatedLength += numberOfEmptyFields;
        for (var j = 1; j <= numberOfEmptyFields; j++) {
          squareDescriptionList.add(BasicConstants.BLANK);
        }
      } catch (@SuppressWarnings("unused") final NumberFormatException e) {
        countEvaluatedLength++;
        squareDescriptionList.add(currentChar);
      }

    }
    if (countEvaluatedLength != 8) {
      throw new FenValidationException(FenValidationProblem.INVALID_POSITION_LINE_EVALUATION_LENGTH,
          "the rank description \"" + rankDescription + "\" for the position does not evaluate to eight squares");
    }

    // post condition
    if (squareDescriptionList.size() != 8) {
      throw new ProgrammingMistakeException("Post condition of eight elements for square description not met");
    }
    return squareDescriptionList;
  }

  private static Side validateHavingMove(String havingMove) throws FenValidationException {
    final var matcher = PATTERN_SIDE.matcher(havingMove);
    if (!matcher.find()) {
      throw new FenValidationException(FenValidationProblem.INVALID_HAVING_MOVE_RANGE,
          "the having move part of \"" + havingMove + "\" is not valid");
    }
    if ("w".equals(havingMove)) {
      return WHITE;
    }
    if ("b".equals(havingMove)) {
      return BLACK;
    }
    throw new ProgrammingMistakeException("Check the regular expression");
  }

  private static CastlingRightBoth validateCastlingRightBoth(StaticPosition staticPosition, String castlingRightBothStr)
      throws FenValidationException {
    final CastlingRightBoth castlingRightBoth = validateCastlingRightBoth(castlingRightBothStr);
    validateCastlingRightAgainstStaticPosition(staticPosition, castlingRightBoth);
    return castlingRightBoth;
  }

  private static CastlingRightBoth validateCastlingRightBoth(String castlingRightBothStr)
      throws FenValidationException {

    switch (castlingRightBothStr) {
      case "-":
        return CastlingConstants.CASTLING_NONE_NONE;
      case "K":
        return CastlingConstants.CASTLING_K_NONE;
      case "Q":
        return CastlingConstants.CASTLING_Q_NONE;
      case "k":
        return CastlingConstants.CASTLING_NONE_K;
      case "q":
        return CastlingConstants.CASTLING_NONE_Q;
      case "KQ":
        return CastlingConstants.CASTLING_KQ_NONE;
      case "Kk":
        return CastlingConstants.CASTLING_K_K;
      case "Kq":
        return CastlingConstants.CASTLING_K_Q;
      case "Qk":
        return CastlingConstants.CASTLING_Q_K;
      case "Qq":
        return CastlingConstants.CASTLING_Q_Q;
      case "kq":
        return CastlingConstants.CASTLING_NONE_KQ;
      case "KQk":
        return CastlingConstants.CASTLING_KQ_K;
      case "KQq":
        return CastlingConstants.CASTLING_KQ_Q;
      case "Kkq":
        return CastlingConstants.CASTLING_K_KQ;
      case "Qkq":
        return CastlingConstants.CASTLING_Q_KQ;
      case "KQkq":
        return CastlingConstants.CASTLING_KQ_KQ;
      default:
        break;
    }
    throw new FenValidationException(FenValidationProblem.INVALID_CASTLING_RIGHT_RANGE,
        "the castling right part of \"" + castlingRightBothStr + "\" is not valid");
  }

  private static Square validateEnPassantCaptureTargetSquare(StaticPosition staticPosition,
      String enPassantCaptureTargetSquareStr, Side havingMove) throws FenValidationException {
    final Square enPassantCaptureTargetSquare = validateEnPassantCaptureTargetSquare(enPassantCaptureTargetSquareStr,
        havingMove);
    validateEnPassantCaptureTargetSquareAgainstStaticPosition(staticPosition, enPassantCaptureTargetSquare, havingMove);
    return enPassantCaptureTargetSquare;
  }

  private static Square validateEnPassantCaptureTargetSquare(String enPassantCaptureTargetSquare, Side havingMove)
      throws FenValidationException {
    if (enPassantCaptureTargetSquare.length() == 1 && "-".equals(enPassantCaptureTargetSquare)) {
      return Square.NONE;
    }
    if (enPassantCaptureTargetSquare.length() == 2) {
      final var fileLetter = NonNullWrapperCommon.toString(enPassantCaptureTargetSquare.charAt(0));
      if (File.exists(fileLetter)) {
        final File file = File.calculateFile(fileLetter);
        final var rankLetter = NonNullWrapperCommon.toString(enPassantCaptureTargetSquare.charAt(1));
        if (BasicUtility.isInt(rankLetter)) {
          final var checkRankNumber = BasicUtility.parseInt(rankLetter);

          if (Rank.exists(checkRankNumber)) {
            final Rank rank = Rank.calculateRank(checkRankNumber);
            final Square square = Square.calculate(file, rank);
            if (Square.calculateEnPassantCaptureTargetSquareList(havingMove).contains(square)) {
              return square;
            }
            final Side oppositeSide = havingMove.getOppositeSide();
            if (Square.calculateEnPassantCaptureTargetSquareList(oppositeSide).contains(square)) {
              throw new FenValidationException(
                  FenValidationProblem.INVALID_EN_PASSANT_CAPTURE_TARGET_SQUARE_WRONG_COLOR,
                  "the en passant target square \"" + enPassantCaptureTargetSquare
                      + "\" belongs to the player having the move, not the opponent");
            }
          }
        }
      }
    }
    throw new FenValidationException(FenValidationProblem.INVALID_EN_PASSANT_CAPTURE_TARGET_SQUARE_RANGE,
        "the en passant target square part of \"" + enPassantCaptureTargetSquare + "\" is not valid");
  }

  private static void validateEnPassantCaptureTargetSquareAgainstStaticPosition(StaticPosition staticPosition,
      Square enPassantCaptureTargetSquare, Side havingMove) throws FenValidationException {
    if (enPassantCaptureTargetSquare == Square.NONE) {
      // if not set there is nothing to validate
      return;
    }

    final Side oppositeSide = havingMove.getOppositeSide();
    final Square pawnTwoAdvanceSquare = Square.calculateAheadSquare(oppositeSide, enPassantCaptureTargetSquare);
    // if not two square advance then specifying the en passant target square is
    // invalid
    final Piece pieceOnTwoAdvanceSquare = staticPosition.get(pawnTwoAdvanceSquare);
    if (pieceOnTwoAdvanceSquare == Piece.NONE
        || pieceOnTwoAdvanceSquare.getSide() != oppositeSide && pieceOnTwoAdvanceSquare.getPieceType() != PAWN) {
      throw new FenValidationException(FenValidationProblem.INVALID_EN_PASSANT_CAPTURE_TARGET_SQUARE_NO_PAWN_AFTER,
          "the en passant target square is specified as \"" + enPassantCaptureTargetSquare
              + "\" but there is no opponent pawn on \"" + pawnTwoAdvanceSquare + "\"");
    }

    // that must come after checking if pawn has potentially advanced two squares
    final Square startingSquare = Square.calculateBehindSquare(oppositeSide, enPassantCaptureTargetSquare);
    final Piece pieceOnStartingSquare = staticPosition.get(startingSquare);
    final var isStartingSquareEmpty = pieceOnStartingSquare != Piece.NONE;

    final Piece pieceOnEnPassantCaptureTargetSquare = staticPosition.get(enPassantCaptureTargetSquare);
    final var isEnPassantCaptureTargetSquareEmpty = pieceOnEnPassantCaptureTargetSquare != Piece.NONE;

    if (isStartingSquareEmpty && isEnPassantCaptureTargetSquareEmpty) {
      throw new FenValidationException(FenValidationProblem.INVALID_EN_PASSANT_CAPTURE_BOTH_NOT_EMPTY,
          "the en passant target square \"" + enPassantCaptureTargetSquare + "\" and the pawn starting square  \""
              + startingSquare + "\" are not empty");
    }

    if (isStartingSquareEmpty) {
      throw new FenValidationException(FenValidationProblem.INVALID_EN_PASSANT_CAPTURE_STARTING_SQUARE_NOT_EMPTY,
          "the from square \"" + startingSquare + "\" of the pawn making the two square advance is not empty");
    }

    if (isEnPassantCaptureTargetSquareEmpty) {
      throw new FenValidationException(FenValidationProblem.INVALID_EN_PASSANT_CAPTURE_TARGET_SQUARE_NOT_EMPTY,
          "the en passant target square \"" + enPassantCaptureTargetSquare + "\" is not empty");
    }

    // previous check are necessary for this check
    // check if previous position is legal
    final List<UpdateSquare> updateSquareList = new ArrayList<>();
    updateSquareList.add(new UpdateSquare(pawnTwoAdvanceSquare));
    updateSquareList.add(new UpdateSquare(startingSquare, Piece.calculatePawnPiece(oppositeSide)));

    final StaticPosition staticPositionBeforeTwoSquareAdvance = staticPosition.createChangedPosition(updateSquareList);

    if (StaticPositionUtility.calculateIsEvaluateAttackingKing(staticPositionBeforeTwoSquareAdvance, oppositeSide)) {
      throw new FenValidationException(FenValidationProblem.INVALID_EN_PASSANT_CAPTURE_PREVIOUS_POSITION_ILLEGAL,
          "the opponent king was in check before before performing the pawn two square advance");
    }

  }

  private static int validateHalfMoveClock(String halfMoveClockStr, Square enPassantCaptureTargetSquare)
      throws FenValidationException {
    final var halfMoveClock = validateHalfMoveClock(halfMoveClockStr);
    validateHalfMoveClock(halfMoveClock, enPassantCaptureTargetSquare);
    return halfMoveClock;
  }

  private static int validateHalfMoveClock(String halfMoveClockStr) throws FenValidationException {
    int halfMoveClock;
    try {
      halfMoveClock = Integer.parseInt(halfMoveClockStr);
    } catch (@SuppressWarnings("unused") final NumberFormatException e) {
      throw new FenValidationException(FenValidationProblem.INVALID_HALF_MOVE_CLOCK_RANGE,
          "the half-move clock part of \"" + halfMoveClockStr + "\" is not an integer value");
    }

    return halfMoveClock;
  }

  private static void validateHalfMoveClock(int halfMoveClock, Square enPassantCaptureTargetSquare)
      throws FenValidationException {
    if (enPassantCaptureTargetSquare != Square.NONE && halfMoveClock != 0) {
      throw new FenValidationException(
          FenValidationProblem.INVALID_HALF_MOVE_CLOCK_NOT_ZERO_BUT_EN_PASSANT_CAPTURE_TARGET_SQUARE_SET,
          "the half-move clock is \"" + halfMoveClock + "\" must be zero if en passant target square is set");
    }
  }

  private static int validateFullMoveNumber(String fen, Side havingMove, String fullMoveNumberStr)
      throws FenValidationException {
    int fullMoveNumber;
    try {
      fullMoveNumber = Integer.parseInt(fullMoveNumberStr);
    } catch (@SuppressWarnings("unused") final NumberFormatException e) {
      throw new FenValidationException(FenValidationProblem.INVALID_FULL_MOVE_NUMBER_RANGE,
          "the full move counter of \"" + fullMoveNumberStr + "\" is not an integer value");
    }

    if (fullMoveNumber < 0) {
      throw new FenValidationException(FenValidationProblem.INVALID_FULL_MOVE_NUMBER_NEGATIVE,
          "the full move counter of \"" + fullMoveNumberStr + "\" cannot be negative");
    }

    if (fullMoveNumber == 0) {
      throw new FenValidationException(FenValidationProblem.INVALID_FULL_MOVE_NUMBER_ZERO,
          "the full move counter cannot be zero");
    }

    if (fullMoveNumber == 1) {
      switch (havingMove) {
        case BLACK:
          if (!FenConstants.POSSIBLE_FEN_AFTER_FIRST_HALF_MOVE.contains(fen)) {
            throw new FenValidationException(
                FenValidationProblem.INVALID_FULL_MOVE_NUMBER_ONE_BLACK_IN_NON_POSSIBLE_POSITION,
                "Black can only make the first move, "
                    + "(full move counter of one), if it's any of the 20 possible positions after White's first move");
          }
          break;
        case WHITE:
          if (!FenConstants.FEN_INITIAL_STR.equals(fen)) {
            throw new FenValidationException(
                FenValidationProblem.INVALID_FULL_MOVE_NUMBER_ONE_WHITE_IN_NON_STARTING_POSITION,
                "White can only make the first move, " + "(full move counter of one), in the starting position");
          }
          break;
        case NONE:
        default:
          throw new IllegalArgumentException();
      }
    }

    if (fullMoveNumber > FenConstants.MAX_FULL_MOVE_NUMBER) {
      throw new FenValidationException(FenValidationProblem.INVALID_FULL_MOVE_NUMBER_TOO_BIG_ABSOLUT,
          "the full move counter of " + fullMoveNumber + " is above the maximum supported value of "
              + FenConstants.MAX_FULL_MOVE_NUMBER + "");
    }

    return fullMoveNumber;
  }

  private static void validateCastlingRightAgainstStaticPosition(StaticPosition staticPosition,
      CastlingRightBoth castlingRightBoth) throws FenValidationException {

    final List<Side> sideToCheckList = new ArrayList<>();
    sideToCheckList.add(WHITE);
    sideToCheckList.add(BLACK);

    for (final Side sideToCheck : sideToCheckList) {
      final CastlingRight sideCastlingRight = CastlingUtility.getCastlingRight(castlingRightBoth, sideToCheck);

      final var isKingSideCastlingOriginalPosition = CastlingUtility
          .calculateKingSideCastlingIsOriginalPosition(staticPosition, sideToCheck);
      final var isQueenSideCastlingOriginalPosition = CastlingUtility
          .calculateQueenSideCastlingIsOriginalPosition(staticPosition, sideToCheck);

      final FenValidationProblem parseFenCheck = calculateParseFenCheck(sideToCheck, sideCastlingRight,
          isKingSideCastlingOriginalPosition, isQueenSideCastlingOriginalPosition);

      if (parseFenCheck != FenValidationProblem.SUCCESS) {
        throw calculateParseFenException(sideToCheck, sideCastlingRight, parseFenCheck);
      }
    }
  }

  private static FenValidationException calculateParseFenException(Side sideToCheck, CastlingRight castlingRight,
      FenValidationProblem parseFenCheck) {

    final StringBuilder message = new StringBuilder();
    message.append("Castling rights for ");
    message.append(sideToCheck.getName());
    message.append(" have been specified as ");
    message.append(castlingRight.getDescription());
    message.append(
        ", but castling as such is not possible, as the king and/or rock are not in their original positions anymore.");

    return new FenValidationException(parseFenCheck, NonNullWrapperCommon.toString(message));
  }

  private static FenValidationProblem calculateParseFenCheck(Side sideToCheck, CastlingRight castlingRight,
      boolean isKingSideCastlingOriginalPosition, boolean isQueenSideCastlingOriginalPosition) {
    switch (castlingRight) {
      case KING_AND_QUEEN_SIDE:
        if (!isKingSideCastlingOriginalPosition && !isQueenSideCastlingOriginalPosition) {
          switch (sideToCheck) {
            case BLACK:
              return FenValidationProblem.INVALID_CASTLING_RIGHT_BLACK_BOTH;
            case WHITE:
              return FenValidationProblem.INVALID_CASTLING_RIGHT_WHITE_BOTH;
            case NONE:
            default:
              throw new IllegalArgumentException();
          }
        }
        if (!isKingSideCastlingOriginalPosition) {
          switch (sideToCheck) {
            case BLACK:
              return FenValidationProblem.INVALID_CASTLING_RIGHT_BLACK_KINGSIDE;
            case WHITE:
              return FenValidationProblem.INVALID_CASTLING_RIGHT_WHITE_KINGSIDE;
            case NONE:
            default:
              throw new IllegalArgumentException();
          }
        }
        if (!isQueenSideCastlingOriginalPosition) {
          switch (sideToCheck) {
            case BLACK:
              return FenValidationProblem.INVALID_CASTLING_RIGHT_BLACK_QUEENSIDE;
            case WHITE:
              return FenValidationProblem.INVALID_CASTLING_RIGHT_WHITE_QUEENSIDE;
            case NONE:
            default:
              throw new IllegalArgumentException();
          }
        }
        break;
      case KING_SIDE:
        if (!isKingSideCastlingOriginalPosition) {
          switch (sideToCheck) {
            case BLACK:
              return FenValidationProblem.INVALID_CASTLING_RIGHT_BLACK_KINGSIDE;
            case WHITE:
              return FenValidationProblem.INVALID_CASTLING_RIGHT_WHITE_KINGSIDE;
            case NONE:
            default:
              throw new IllegalArgumentException();
          }
        }
        break;
      case NONE:
        break;
      case QUEEN_SIDE:
        if (!isQueenSideCastlingOriginalPosition) {
          switch (sideToCheck) {
            case BLACK:
              return FenValidationProblem.INVALID_CASTLING_RIGHT_BLACK_QUEENSIDE;
            case WHITE:
              return FenValidationProblem.INVALID_CASTLING_RIGHT_WHITE_QUEENSIDE;
            case NONE:
            default:
              throw new IllegalArgumentException();
          }
        }
        break;
      default:
        throw new IllegalArgumentException();
    }
    return FenValidationProblem.SUCCESS;

  }

  private static void validateNumberOfPieces(StaticPosition staticPosition) throws FenValidationException {

    // white
    {
      // kings
      final var numberOfKings = MaterialUtility.calculateNumberOfPieces(Side.WHITE, staticPosition, PieceType.KING);
      if (numberOfKings > ChessConstants.NUMBER_OF_KINGS) {
        throw new FenValidationException(FenValidationProblem.INVALID_WHITE_TOO_MANY_KINGS,
            "there is more than one white king");
      }
      if (numberOfKings == 0) {
        throw new FenValidationException(FenValidationProblem.INVALID_WHITE_NO_KING, "there is no white king");
      }

      // pawns
      final var numberOfPawns = MaterialUtility.calculateNumberOfPieces(Side.WHITE, staticPosition, PieceType.PAWN);
      if (numberOfPawns > ChessConstants.INITIAL_NUMBER_OF_PAWNS) {
        throw new FenValidationException(FenValidationProblem.INVALID_WHITE_TOO_MANY_PAWNS,
            "there are too many white pawns");
      }

      final var numberOfPossiblePromotions = ChessConstants.INITIAL_NUMBER_OF_PAWNS - numberOfPawns;

      // rooks
      final var numberOfRooks = MaterialUtility.calculateNumberOfPieces(Side.WHITE, staticPosition, PieceType.ROOK);
      final var numberOfRooksPromoted = numberOfRooks - ChessConstants.INITIAL_NUMBER_OF_ROOKS;
      if (numberOfRooksPromoted > numberOfPossiblePromotions) {
        throw new FenValidationException(FenValidationProblem.INVALID_WHITE_TOO_MANY_ROOKS,
            "there are too many white rooks");
      }

      // knights
      final var numberOfKnights = MaterialUtility.calculateNumberOfPieces(Side.WHITE, staticPosition, PieceType.KNIGHT);
      final var numberOfKnightsPromoted = numberOfKnights - ChessConstants.INITIAL_NUMBER_OF_KNIGHTS;
      if (numberOfKnightsPromoted > numberOfPossiblePromotions) {
        throw new FenValidationException(FenValidationProblem.INVALID_WHITE_TOO_MANY_KNIGHTS,
            "there are too many white knights");
      }

      // light square bishops
      final var numberOfLightSquareBishops = MaterialUtility.calculateNumberOfBishops(Side.WHITE, staticPosition,
          SquareType.LIGHT_SQUARE);
      final var numberOfLightSquareBishopsPromoted = numberOfLightSquareBishops
          - ChessConstants.INITIAL_NUMBER_OF_LIGHT_SQUARE_BISHOPS;
      if (numberOfLightSquareBishopsPromoted > numberOfPossiblePromotions) {
        throw new FenValidationException(FenValidationProblem.INVALID_WHITE_TOO_MANY_LIGHT_SQUARE_BISHOPS,
            "there are too many white light squared bishops");
      }

      // dark square bishops
      final var numberOfDarkSquareBishops = MaterialUtility.calculateNumberOfBishops(Side.WHITE, staticPosition,
          SquareType.DARK_SQUARE);
      final var numberOfDarkSquareBishopsPromoted = numberOfDarkSquareBishops
          - ChessConstants.INITIAL_NUMBER_OF_DARK_SQUARE_BISHOPS;
      if (numberOfDarkSquareBishopsPromoted > numberOfPossiblePromotions) {
        throw new FenValidationException(FenValidationProblem.INVALID_WHITE_TOO_MANY_DARK_SQUARE_BISHOPS,
            "there are too many white dark squared bishops");
      }

      // queens
      final var numberOfQueens = MaterialUtility.calculateNumberOfPieces(Side.WHITE, staticPosition, PieceType.QUEEN);
      final var numberOfQueensPromoted = numberOfQueens - ChessConstants.INITIAL_NUMBER_OF_QUEENS;
      if (numberOfQueensPromoted > numberOfPossiblePromotions) {
        throw new FenValidationException(FenValidationProblem.INVALID_WHITE_TOO_MANY_QUEENS,
            "there are too many white queens");
      }
    }

    // black
    // copy/replace code of white
    {
      // kings
      final var numberOfKings = MaterialUtility.calculateNumberOfPieces(Side.BLACK, staticPosition, PieceType.KING);
      if (numberOfKings > ChessConstants.NUMBER_OF_KINGS) {
        throw new FenValidationException(FenValidationProblem.INVALID_BLACK_TOO_MANY_KINGS,
            "there is more than one black king");
      }
      if (numberOfKings == 0) {
        throw new FenValidationException(FenValidationProblem.INVALID_BLACK_NO_KING, "there is no black king");
      }

      // pawns
      final var numberOfPawns = MaterialUtility.calculateNumberOfPieces(Side.BLACK, staticPosition, PieceType.PAWN);
      if (numberOfPawns > ChessConstants.INITIAL_NUMBER_OF_PAWNS) {
        throw new FenValidationException(FenValidationProblem.INVALID_BLACK_TOO_MANY_PAWNS,
            "there are too many black pawns");
      }

      final var numberOfPossiblePromotions = ChessConstants.INITIAL_NUMBER_OF_PAWNS - numberOfPawns;

      // rooks
      final var numberOfRooks = MaterialUtility.calculateNumberOfPieces(Side.BLACK, staticPosition, PieceType.ROOK);
      final var numberOfRooksPromoted = numberOfRooks - ChessConstants.INITIAL_NUMBER_OF_ROOKS;
      if (numberOfRooksPromoted > numberOfPossiblePromotions) {
        throw new FenValidationException(FenValidationProblem.INVALID_BLACK_TOO_MANY_ROOKS,
            "there are too many black rooks");
      }

      // knights
      final var numberOfKnights = MaterialUtility.calculateNumberOfPieces(Side.BLACK, staticPosition, PieceType.KNIGHT);
      final var numberOfKnightsPromoted = numberOfKnights - ChessConstants.INITIAL_NUMBER_OF_KNIGHTS;
      if (numberOfKnightsPromoted > numberOfPossiblePromotions) {
        throw new FenValidationException(FenValidationProblem.INVALID_BLACK_TOO_MANY_KNIGHTS,
            "there are too many black knights");
      }

      // light square bishops
      final var numberOfLightSquareBishops = MaterialUtility.calculateNumberOfBishops(Side.BLACK, staticPosition,
          SquareType.LIGHT_SQUARE);
      final var numberOfLightSquareBishopsPromoted = numberOfLightSquareBishops
          - ChessConstants.INITIAL_NUMBER_OF_LIGHT_SQUARE_BISHOPS;
      if (numberOfLightSquareBishopsPromoted > numberOfPossiblePromotions) {
        throw new FenValidationException(FenValidationProblem.INVALID_BLACK_TOO_MANY_LIGHT_SQUARE_BISHOPS,
            "there are too many black light squared bishops");
      }

      // dark square bishops
      final var numberOfDarkSquareBishops = MaterialUtility.calculateNumberOfBishops(Side.BLACK, staticPosition,
          SquareType.DARK_SQUARE);
      final var numberOfDarkSquareBishopsPromoted = numberOfDarkSquareBishops
          - ChessConstants.INITIAL_NUMBER_OF_DARK_SQUARE_BISHOPS;
      if (numberOfDarkSquareBishopsPromoted > numberOfPossiblePromotions) {
        throw new FenValidationException(FenValidationProblem.INVALID_BLACK_TOO_MANY_DARK_SQUARE_BISHOPS,
            "there are too many black dark squared bishops");
      }

      // queens
      final var numberOfQueens = MaterialUtility.calculateNumberOfPieces(Side.BLACK, staticPosition, PieceType.QUEEN);
      final var numberOfQueensPromoted = numberOfQueens - ChessConstants.INITIAL_NUMBER_OF_QUEENS;
      if (numberOfQueensPromoted > numberOfPossiblePromotions) {
        throw new FenValidationException(FenValidationProblem.INVALID_BLACK_TOO_MANY_QUEENS,
            "there are too many black queens");
      }
    }
  }

  private static void validatePawnRankNotPromotionRank(StaticPosition staticPosition) throws FenValidationException {
    for (final Square square : Square.getPromotionRank(WHITE)) {
      if (staticPosition.get(square) == WHITE_PAWN) {
        throw new FenValidationException(FenValidationProblem.INVALID_WHITE_PAWN_INVALID_RANK_PROMOTION_RANK,
            "There is a non promoted white pawn on rank " + square.getRank().getNumber());
      }
    }

    for (final Square square : Square.getPromotionRank(BLACK)) {
      if (staticPosition.get(square) == BLACK_PAWN) {
        throw new FenValidationException(FenValidationProblem.INVALID_BLACK_PAWN_INVALID_RANK_PROMOTION_RANK,
            "There is a non promoted black pawn on rank " + square.getRank().getNumber());
      }
    }
  }

  private static void validatePawnRankNotGroundRank(StaticPosition staticPosition) throws FenValidationException {
    for (final Square square : Square.getPromotionRank(BLACK)) {
      if (staticPosition.get(square) == WHITE_PAWN) {
        throw new FenValidationException(FenValidationProblem.INVALID_WHITE_PAWN_INVALID_RANK_GROUND_RANK,
            "There is a white pawn on rank " + square.getRank().getNumber());
      }
    }

    for (final Square square : Square.getPromotionRank(WHITE)) {
      if (staticPosition.get(square) == BLACK_PAWN) {
        throw new FenValidationException(FenValidationProblem.INVALID_BLACK_PAWN_INVALID_RANK_GROUND_RANK,
            "There is a black pawn on rank " + square.getRank().getNumber());
      }
    }

  }
}
