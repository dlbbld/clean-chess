package com.dlb.chess.test.apicarlos.chessboard;

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
    switch (moveTwoSquareAdvanceTo) {
      // white is capturing
      case A5:
        return Square.A6;
      case B5:
        return Square.B6;
      case C5:
        return Square.C6;
      case D5:
        return Square.D6;
      case E5:
        return Square.E6;
      case F5:
        return Square.F6;
      case G5:
        return Square.G6;
      case H5:
        return Square.H6;
      // black is capturing
      case A4:
        return Square.A3;
      case B4:
        return Square.B3;
      case C4:
        return Square.C3;
      case D4:
        return Square.D3;
      case E4:
        return Square.E3;
      case F4:
        return Square.F3;
      case G4:
        return Square.G3;
      case H4:
        return Square.H3;
      case A1:
      case A2:
      case A3:
      case A6:
      case A7:
      case A8:
      case B1:
      case B2:
      case B3:
      case B6:
      case B7:
      case B8:
      case C1:
      case C2:
      case C3:
      case C6:
      case C7:
      case C8:
      case D1:
      case D2:
      case D3:
      case D6:
      case D7:
      case D8:
      case E1:
      case E2:
      case E3:
      case E6:
      case E7:
      case E8:
      case F1:
      case F2:
      case F3:
      case F6:
      case F7:
      case F8:
      case G1:
      case G2:
      case G3:
      case G6:
      case G7:
      case G8:
      case H1:
      case H2:
      case H3:
      case H6:
      case H7:
      case H8:
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
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
    switch (piece) {
      case WHITE_PAWN:
      case BLACK_PAWN:
        return true;
      case BLACK_BISHOP:
      case BLACK_KING:
      case BLACK_KNIGHT:
      case BLACK_QUEEN:
      case BLACK_ROOK:
      case WHITE_BISHOP:
      case WHITE_KING:
      case WHITE_KNIGHT:
      case WHITE_QUEEN:
      case WHITE_ROOK:
        return false;
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
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
        switch (pieceType) {
          case BISHOP:
            return board.getBitboard(com.github.bhlangonijr.chesslib.Piece.BLACK_BISHOP) != 0L;
          case KING:
            return board.getBitboard(com.github.bhlangonijr.chesslib.Piece.BLACK_KING) != 0L;
          case KNIGHT:
            return board.getBitboard(com.github.bhlangonijr.chesslib.Piece.BLACK_KNIGHT) != 0L;
          case PAWN:
            return board.getBitboard(com.github.bhlangonijr.chesslib.Piece.BLACK_PAWN) != 0L;
          case QUEEN:
            return board.getBitboard(com.github.bhlangonijr.chesslib.Piece.BLACK_QUEEN) != 0L;
          case ROOK:
            return board.getBitboard(com.github.bhlangonijr.chesslib.Piece.BLACK_ROOK) != 0L;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      case WHITE:
        switch (pieceType) {
          case BISHOP:
            return board.getBitboard(com.github.bhlangonijr.chesslib.Piece.WHITE_BISHOP) != 0L;
          case KING:
            return board.getBitboard(com.github.bhlangonijr.chesslib.Piece.WHITE_KING) != 0L;
          case KNIGHT:
            return board.getBitboard(com.github.bhlangonijr.chesslib.Piece.WHITE_KNIGHT) != 0L;
          case PAWN:
            return board.getBitboard(com.github.bhlangonijr.chesslib.Piece.WHITE_PAWN) != 0L;
          case QUEEN:
            return board.getBitboard(com.github.bhlangonijr.chesslib.Piece.WHITE_QUEEN) != 0L;
          case ROOK:
            return board.getBitboard(com.github.bhlangonijr.chesslib.Piece.WHITE_ROOK) != 0L;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
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
        switch (squareType) {
          case DARK_SQUARE:
            return (Bitboard.darkSquares & board.getBitboard(com.github.bhlangonijr.chesslib.Piece.BLACK_BISHOP)) != 0L;
          case LIGHT_SQUARE:
            return (Bitboard.lightSquares
                & board.getBitboard(com.github.bhlangonijr.chesslib.Piece.BLACK_BISHOP)) != 0L;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      case WHITE:
        switch (squareType) {
          case DARK_SQUARE:
            return (Bitboard.darkSquares & board.getBitboard(com.github.bhlangonijr.chesslib.Piece.WHITE_BISHOP)) != 0L;
          case LIGHT_SQUARE:
            return (Bitboard.lightSquares
                & board.getBitboard(com.github.bhlangonijr.chesslib.Piece.WHITE_BISHOP)) != 0L;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

}
