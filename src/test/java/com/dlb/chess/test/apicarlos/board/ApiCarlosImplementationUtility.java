package com.dlb.chess.test.apicarlos.board;

import java.util.List;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.SquareType;
import com.dlb.chess.test.apicarlos.NonNullWrapperApiCarlos;
import com.dlb.chess.test.apicomparison.utility.EnumConversionUtility;
import com.github.bhlangonijr.chesslib.Bitboard;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.MoveBackup;
import com.github.bhlangonijr.chesslib.Rank;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;

public class ApiCarlosImplementationUtility {

  private static Square calculateEnPassantCaptureDestination(Square moveTwoSquareAdvanceTo) {
    return switch (moveTwoSquareAdvanceTo) {
      case A5 -> Square.A6;
      case B5 -> Square.B6;
      case C5 -> Square.C6;
      case D5 -> Square.D6;
      case E5 -> Square.E6;
      case F5 -> Square.F6;
      case G5 -> Square.G6;
      case H5 -> Square.H6;
      case A4 -> Square.A3;
      case B4 -> Square.B3;
      case C4 -> Square.C3;
      case D4 -> Square.D3;
      case E4 -> Square.E3;
      case F4 -> Square.F3;
      case G4 -> Square.G3;
      case H4 -> Square.H3;
      case A1, A2, A3, A6, A7, A8, B1, B2, B3, B6, B7, B8, C1, C2, C3, C6, C7, C8, D1, D2, D3, D6, D7, D8, E1, E2, E3, E6, E7, E8, F1, F2, F3, F6, F7, F8, G1, G2, G3, G6, G7, G8, H1, H2, H3, H6, H7, H8, NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  private static boolean calculateIsLegalMoveEnPassantCapture(Board board, Square enPassantCaptureDestination) {

    final List<Move> legalMoveList = generateLegalMoves(board);
    for (final Move legalMove : legalMoveList) {
      if (legalMove.getTo() == enPassantCaptureDestination) {
        final com.github.bhlangonijr.chesslib.Piece piece = calculatePieceForLegalMove(board, legalMove);
        if (calculateIsPawn(piece)) {
          return true;
        }
      }
    }
    return false;
  }

  private static com.github.bhlangonijr.chesslib.Piece calculatePieceForLegalMove(Board board, Move legalMove) {
    return NonNullWrapperApiCarlos.getPiece(board, NonNullWrapperApiCarlos.getFrom(legalMove));
  }

  private static boolean calculateIsPawn(com.github.bhlangonijr.chesslib.Piece piece) {
    return switch (piece) {
      case WHITE_PAWN, BLACK_PAWN -> true;
      case BLACK_BISHOP, BLACK_KING, BLACK_KNIGHT, BLACK_QUEEN, BLACK_ROOK, WHITE_BISHOP, WHITE_KING, WHITE_KNIGHT, WHITE_QUEEN, WHITE_ROOK -> false;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  private static boolean calculateIsPawnInitialTwoSquaresAdvance(MoveBackup moveBackup) {
    final var movingPiece = moveBackup.getMovingPiece();
    final var move = moveBackup.getMove();
    if (movingPiece == com.github.bhlangonijr.chesslib.Piece.WHITE_PAWN) {
      return move.getFrom().getRank() == Rank.RANK_2 && move.getTo().getRank() == Rank.RANK_4;
    }
    if (movingPiece == com.github.bhlangonijr.chesslib.Piece.BLACK_PAWN) {
      return move.getFrom().getRank() == Rank.RANK_7 && move.getTo().getRank() == Rank.RANK_5;
    }
    return false;
  }

  @SuppressWarnings("null")
  static List<Move> generateLegalMoves(Board board) {
    try {
      return MoveGenerator.generateLegalMoves(board);
    } catch (final MoveGeneratorException e) {
      throw new RuntimeException("A move generator exception occurred", e);
    }
  }

  static boolean calculateIsEnPassantCapturePossible(Board board) {
    final List<MoveBackup> backup = NonNullWrapperApiCarlos.getBackup(board);
    if (backup.isEmpty()) {
      return false;
    }

    final MoveBackup moveBackupTwoSquareAdvance = NonNullWrapperApiCarlos.getLast(board);
    final Move moveTwoSquareAdvance = NonNullWrapperApiCarlos.getMove(moveBackupTwoSquareAdvance);
    if (ApiCarlosImplementationUtility.calculateIsPawnInitialTwoSquaresAdvance(moveBackupTwoSquareAdvance)) {
      final Square moveTwoSquareAdvanceTo = NonNullWrapperApiCarlos.getTo(moveTwoSquareAdvance);

      final Square enPassantCaptureDestination = ApiCarlosImplementationUtility
          .calculateEnPassantCaptureDestination(moveTwoSquareAdvanceTo);

      return ApiCarlosImplementationUtility.calculateIsLegalMoveEnPassantCapture(board, enPassantCaptureDestination);
    }
    return false;
  }

  public static boolean calculateIsInsufficientMaterial(Board board) {
    return calculateIsInsufficientMaterial(Side.WHITE, board) && calculateIsInsufficientMaterial(Side.BLACK, board);
  }

  public static boolean calculateIsInsufficientMaterial(Side side, Board board) {
    // using current python chess 1.5.0 implementation
    if (calculateHasPiece(side, com.github.bhlangonijr.chesslib.PieceType.PAWN, board)
        || calculateHasPiece(side, com.github.bhlangonijr.chesslib.PieceType.ROOK, board)
        || calculateHasPiece(side, com.github.bhlangonijr.chesslib.PieceType.QUEEN, board)) {
      return false;
    }

    // king and knight
    final Side oppositeSide = side.getOppositeSide();
    if (calculateHasPiece(side, com.github.bhlangonijr.chesslib.PieceType.KNIGHT, board)) {
      final var numberOfPieces = calculateNumberOfPieces(side, board);
      final var hasAtMostTwoPieces = numberOfPieces <= 2;
      if (hasAtMostTwoPieces) {
        return !calculateHasPiece(oppositeSide, com.github.bhlangonijr.chesslib.PieceType.PAWN, board)
            && !calculateHasPiece(oppositeSide, com.github.bhlangonijr.chesslib.PieceType.KNIGHT, board)
            && !calculateHasPiece(oppositeSide, com.github.bhlangonijr.chesslib.PieceType.BISHOP, board)
            && !calculateHasPiece(oppositeSide, com.github.bhlangonijr.chesslib.PieceType.ROOK, board);
      }
      return false;
    }

    // now we have at most king and zeor or more bishops left
    if (calculateHasPiece(side, com.github.bhlangonijr.chesslib.PieceType.BISHOP, board)) {
      final var hasLightSquareBishop = calculateHasBishopForColorSquare(side, SquareType.LIGHT_SQUARE, board);
      final var hasDarkSquareBishop = calculateHasBishopForColorSquare(side, SquareType.DARK_SQUARE, board);
      if (hasLightSquareBishop && hasDarkSquareBishop) {
        return false;
      }
      final var hasLightSquareBishopOpponent = calculateHasBishopForColorSquare(oppositeSide, SquareType.LIGHT_SQUARE,
          board);
      final var hasDarkSquareBishopOpponent = calculateHasBishopForColorSquare(oppositeSide, SquareType.DARK_SQUARE,
          board);

      if (hasLightSquareBishop && hasDarkSquareBishopOpponent || hasDarkSquareBishop && hasLightSquareBishopOpponent) {
        return false;
      }
      return !calculateHasPiece(oppositeSide, com.github.bhlangonijr.chesslib.PieceType.PAWN, board)
          && !calculateHasPiece(oppositeSide, com.github.bhlangonijr.chesslib.PieceType.KNIGHT, board);
    }

    // bare king
    return true;
  }

  private static boolean calculateHasPiece(Side side, com.github.bhlangonijr.chesslib.PieceType pieceType,
      Board board) {
    switch (side) {
      case BLACK:
        return switch (pieceType) {
          case BISHOP -> board.getBitboard(com.github.bhlangonijr.chesslib.Piece.BLACK_BISHOP) != 0L;
          case KING -> board.getBitboard(com.github.bhlangonijr.chesslib.Piece.BLACK_KING) != 0L;
          case KNIGHT -> board.getBitboard(com.github.bhlangonijr.chesslib.Piece.BLACK_KNIGHT) != 0L;
          case PAWN -> board.getBitboard(com.github.bhlangonijr.chesslib.Piece.BLACK_PAWN) != 0L;
          case QUEEN -> board.getBitboard(com.github.bhlangonijr.chesslib.Piece.BLACK_QUEEN) != 0L;
          case ROOK -> board.getBitboard(com.github.bhlangonijr.chesslib.Piece.BLACK_ROOK) != 0L;
          case NONE -> throw new IllegalArgumentException();
          default -> throw new IllegalArgumentException();
        };
      case WHITE:
        return switch (pieceType) {
          case BISHOP -> board.getBitboard(com.github.bhlangonijr.chesslib.Piece.WHITE_BISHOP) != 0L;
          case KING -> board.getBitboard(com.github.bhlangonijr.chesslib.Piece.WHITE_KING) != 0L;
          case KNIGHT -> board.getBitboard(com.github.bhlangonijr.chesslib.Piece.WHITE_KNIGHT) != 0L;
          case PAWN -> board.getBitboard(com.github.bhlangonijr.chesslib.Piece.WHITE_PAWN) != 0L;
          case QUEEN -> board.getBitboard(com.github.bhlangonijr.chesslib.Piece.WHITE_QUEEN) != 0L;
          case ROOK -> board.getBitboard(com.github.bhlangonijr.chesslib.Piece.WHITE_ROOK) != 0L;
          case NONE -> throw new IllegalArgumentException();
          default -> throw new IllegalArgumentException();
        };
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  private static int calculateNumberOfPieces(Side side, Board board) {
    final com.github.bhlangonijr.chesslib.Side sideApiCarlos = EnumConversionUtility.convertToSide(side);
    return Long.bitCount(board.getBitboard(sideApiCarlos));
  }

  private static boolean calculateHasBishopForColorSquare(Side side, SquareType squareType, Board board) {
    switch (side) {
      case BLACK:
        return switch (squareType) {
          case DARK_SQUARE -> (Bitboard.darkSquares & board.getBitboard(com.github.bhlangonijr.chesslib.Piece.BLACK_BISHOP)) != 0L;
          case LIGHT_SQUARE -> (Bitboard.lightSquares
                          & board.getBitboard(com.github.bhlangonijr.chesslib.Piece.BLACK_BISHOP)) != 0L;
          case NONE -> throw new IllegalArgumentException();
          default -> throw new IllegalArgumentException();
        };
      case WHITE:
        return switch (squareType) {
          case DARK_SQUARE -> (Bitboard.darkSquares & board.getBitboard(com.github.bhlangonijr.chesslib.Piece.WHITE_BISHOP)) != 0L;
          case LIGHT_SQUARE -> (Bitboard.lightSquares
                          & board.getBitboard(com.github.bhlangonijr.chesslib.Piece.WHITE_BISHOP)) != 0L;
          case NONE -> throw new IllegalArgumentException();
          default -> throw new IllegalArgumentException();
        };
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

}
