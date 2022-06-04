package com.dlb.chess.test.fen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.board.enums.SquareType;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.FenValidationException;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.utility.StaticPositionUtility;
import com.dlb.chess.fen.FenParser;

class TestFenInsufficientMaterial {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestFenInsufficientMaterial.class);

  @SuppressWarnings("static-method")
  @Test
  void testKingVersusKing() {

    assertEquals(3612, calculateKingVersusKing(Side.WHITE));
    assertEquals(3612, calculateKingVersusKing(Side.BLACK));
  }

  private static int calculateKingVersusKing(Side side) {
    var counter = 0;

    StaticPosition staticPositionWork = StaticPosition.EMPTY_POSITION;
    for (final Square squareFirstKing : Square.BOARD_SQUARE_LIST) {
      staticPositionWork = staticPositionWork.createChangedPosition(squareFirstKing, Piece.WHITE_KING);
      for (final Square squareSecondKing : Square.BOARD_SQUARE_LIST) {
        if (squareSecondKing == squareFirstKing) {
          continue;
        }
        staticPositionWork = staticPositionWork.createChangedPosition(squareSecondKing, Piece.BLACK_KING);
        final String piecePlacement = StaticPositionUtility.calculatePiecePlacement(staticPositionWork);
        final String fen = createFenForPiecePlacement(piecePlacement, side);
        try {
          FenParser.parseAdvancedFen(fen);
          // counting if valid
          counter++;
          // we check by the side if we have illegal material
          final Board board = new Board(fen);
          if (board.isCheckmate()) {
            throw new ProgrammingMistakeException("We don't have insufficient material. The FEN is \"" + fen + "\".");
          }
        } catch (@SuppressWarnings("unused") final FenValidationException fve) {
          // not counting if invalid
        }
        staticPositionWork = staticPositionWork.createChangedPosition(squareSecondKing);
      }
      staticPositionWork = staticPositionWork.createChangedPosition(squareFirstKing);
      if (!staticPositionWork.equals(StaticPosition.EMPTY_POSITION)) {
        throw new ProgrammingMistakeException();
      }
    }
    return counter;
  }

  @SuppressWarnings("static-method")
  // @Test
  // takes about two minutes - so commented out
  void testKingKnightVersusKing() {

    System.out.println(calculateKingKnightVersusKing(Side.WHITE));
    System.out.println(calculateKingKnightVersusKing(Side.BLACK));

    assertEquals(205496, calculateKingKnightVersusKing(Side.WHITE));
    assertEquals(223944, calculateKingKnightVersusKing(Side.BLACK));
  }

  private static int calculateKingKnightVersusKing(Side side) {
    return calculateKingPieceVersusKing(side, Piece.WHITE_KNIGHT, SquareType.NONE);
  }

  @SuppressWarnings("static-method")
  // @Test
  // takes about four minutes - so commented out
  void testKingBishopVersusKing() {

    final var actualCountLightSquareBishopWhiteHavingMove = calculateKingPieceVersusKing(Side.WHITE, Piece.WHITE_BISHOP,
        SquareType.LIGHT_SQUARE);
    assertEquals(96642, actualCountLightSquareBishopWhiteHavingMove);

    final var actualCountLightSquareBishopBlackHavingMove = calculateKingPieceVersusKing(Side.BLACK, Piece.WHITE_BISHOP,
        SquareType.LIGHT_SQUARE);
    assertEquals(111972, actualCountLightSquareBishopBlackHavingMove);

    final var actualCountDarkSquareBishopWhiteHavingMove = calculateKingPieceVersusKing(Side.WHITE, Piece.WHITE_BISHOP,
        SquareType.DARK_SQUARE);
    assertEquals(96642, actualCountDarkSquareBishopWhiteHavingMove);

    final var actualCountDarkSquareBishopBlackHavingMove = calculateKingPieceVersusKing(Side.BLACK, Piece.WHITE_BISHOP,
        SquareType.DARK_SQUARE);
    assertEquals(111972, actualCountDarkSquareBishopBlackHavingMove);

    assertEquals(193284, 2 * 96642);
    assertEquals(223944, 2 * 111972);

    assertEquals(193284, calculateKingBishopVersusKing(Side.WHITE));
    assertEquals(223944, calculateKingBishopVersusKing(Side.BLACK));

  }

  private static int calculateKingBishopVersusKing(Side side) {
    return calculateKingPieceVersusKing(side, Piece.WHITE_BISHOP, SquareType.NONE);
  }

  @SuppressWarnings("static-method")
  // @Test
  // not completed run yet, takes maybe hours
  void testKingBishopVersusBishopSameColourSquare() {
    assertEquals(193284, calculateKingBishopVersusBishopSameColourSquare(Side.WHITE, SquareType.LIGHT_SQUARE));
    assertEquals(193284, calculateKingBishopVersusBishopSameColourSquare(Side.BLACK, SquareType.LIGHT_SQUARE));
  }

  private static int calculateKingBishopVersusBishopSameColourSquare(Side side, SquareType squareType) {
    var counter = 0;

    StaticPosition staticPositionWork = StaticPosition.EMPTY_POSITION;
    for (final Square squareFirstKing : Square.BOARD_SQUARE_LIST) {
      staticPositionWork = staticPositionWork.createChangedPosition(squareFirstKing, Piece.WHITE_KING);
      for (final Square squareFirstBishop : Square.BOARD_SQUARE_LIST) {
        if (squareFirstBishop == squareFirstKing || squareFirstBishop.getSquareType() != squareType) {
          continue;
        }
        staticPositionWork = staticPositionWork.createChangedPosition(squareFirstBishop, Piece.WHITE_BISHOP);
        for (final Square squareSecondKing : Square.BOARD_SQUARE_LIST) {
          if (squareSecondKing == squareFirstBishop || squareSecondKing == squareFirstKing) {
            continue;
          }
          staticPositionWork = staticPositionWork.createChangedPosition(squareSecondKing, Piece.BLACK_KING);

          for (final Square squareSecondBishop : Square.BOARD_SQUARE_LIST) {
            if (squareSecondBishop == squareFirstKing || squareSecondBishop == squareFirstBishop
                || squareSecondBishop == squareSecondKing || squareSecondBishop.getSquareType() != squareType) {
              continue;
            }
            staticPositionWork = staticPositionWork.createChangedPosition(squareSecondBishop, Piece.BLACK_BISHOP);

            final String piecePlacement = StaticPositionUtility.calculatePiecePlacement(staticPositionWork);
            final String fen = createFenForPiecePlacement(piecePlacement, side);
            try {
              FenParser.parseAdvancedFen(fen);
              // counting if valid
              counter++;
              // we check by the side if we have illegal material
              final Board board = new Board(fen);
              if (board.isCheckmate()) {
                throw new ProgrammingMistakeException(
                    "We don't have insufficient material. The FEN is \"" + fen + "\".");
              }
            } catch (@SuppressWarnings("unused") final FenValidationException fve) {
              // not counting if invalid
            }
            staticPositionWork = staticPositionWork.createChangedPosition(squareSecondBishop);
          }
          staticPositionWork = staticPositionWork.createChangedPosition(squareSecondKing);
        }
        logger.info(counter);
        staticPositionWork = staticPositionWork.createChangedPosition(squareFirstBishop);
      }
      staticPositionWork = staticPositionWork.createChangedPosition(squareFirstKing);
      if (!staticPositionWork.equals(StaticPosition.EMPTY_POSITION)) {
        throw new ProgrammingMistakeException();
      }
    }
    return counter;
  }

  private static int calculateKingPieceVersusKing(Side side, Piece piece, SquareType squareType) {
    var counter = 0;

    StaticPosition staticPositionWork = StaticPosition.EMPTY_POSITION;
    for (final Square squareFirstKing : Square.BOARD_SQUARE_LIST) {
      staticPositionWork = staticPositionWork.createChangedPosition(squareFirstKing, Piece.WHITE_KING);
      for (final Square squareKnight : Square.BOARD_SQUARE_LIST) {
        if (squareKnight == squareFirstKing) {
          continue;
        }
        switch (squareType) {
          case LIGHT_SQUARE:
          case DARK_SQUARE:
            if (squareKnight.getSquareType() != squareType) {
              continue;
            }
            break;
          case NONE:
            break;
          default:
            throw new IllegalArgumentException();
        }
        staticPositionWork = staticPositionWork.createChangedPosition(squareKnight, piece);
        for (final Square squareSecondKing : Square.BOARD_SQUARE_LIST) {
          if (squareSecondKing == squareKnight || squareSecondKing == squareFirstKing) {
            continue;
          }
          staticPositionWork = staticPositionWork.createChangedPosition(squareSecondKing, Piece.BLACK_KING);
          final String piecePlacement = StaticPositionUtility.calculatePiecePlacement(staticPositionWork);
          final String fen = createFenForPiecePlacement(piecePlacement, side);
          try {
            FenParser.parseAdvancedFen(fen);
            // counting if valid
            counter++;
            // we check by the side if we have illegal material
            final Board board = new Board(fen);
            if (board.isCheckmate()) {
              throw new ProgrammingMistakeException("We don't have insufficient material. The FEN is \"" + fen + "\".");
            }
          } catch (@SuppressWarnings("unused") final FenValidationException fve) {
            // not counting if invalid
          }
          staticPositionWork = staticPositionWork.createChangedPosition(squareSecondKing);
        }
        staticPositionWork = staticPositionWork.createChangedPosition(squareKnight);
      }
      staticPositionWork = staticPositionWork.createChangedPosition(squareFirstKing);
      if (!staticPositionWork.equals(StaticPosition.EMPTY_POSITION)) {
        throw new ProgrammingMistakeException();
      }
    }
    return counter;
  }

  private static String createFenForPiecePlacement(String piecePlacement, Side side) {
    final StringBuilder fen = new StringBuilder();

    fen.append(piecePlacement);
    fen.append(" ");
    fen.append(side.getFenLetter());
    fen.append(" - - 0 100");

    return NonNullWrapperCommon.toString(fen);

  }

}