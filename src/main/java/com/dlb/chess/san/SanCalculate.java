package com.dlb.chess.san;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.san.enums.SanTerminalMarker;
import com.dlb.chess.san.enums.SanSymbol;
import com.dlb.chess.san.enums.SanType;

public class SanCalculate implements EnumConstants {

  public static String calculateSan(File fromFile, Rank fromRank, Square toSquare,
      PromotionPieceType promotionPieceType, boolean isCapture, SanTerminalMarker sanTerminalMarker,
      PieceType movingPieceType) {
    final StringBuilder san = new StringBuilder();

    if (movingPieceType != PAWN) {
      san.append(movingPieceType.getLetter());
    }
    if (fromFile != File.NONE) {
      san.append(fromFile.getLetter());
    }
    if (fromRank != Rank.NONE) {
      san.append(fromRank.getNumber());
    }
    if (isCapture) {
      san.append(SanSymbol.CAPTURE.getSymbol());
    }

    san.append(toSquare.getName());

    if (promotionPieceType != PromotionPieceType.NONE) {
      san.append(SanSymbol.PROMOTION.getSymbol());
      san.append(promotionPieceType.getPieceType().getLetter());
    }
    switch (sanTerminalMarker) {
      case CHECKMATE:
        san.append(SanSymbol.CHECKMATE.getSymbol());
        break;
      case CHECK:
        san.append(SanSymbol.CHECK.getSymbol());
        break;
      case NONE:
        break;
      default:
        throw new IllegalArgumentException();

    }
    return NonNullWrapperCommon.toString(san);
  }

  public static SanType calculateSanType(boolean isCapture, File fromFile, Rank fromRank, PieceType movingPieceType,
      PromotionPieceType promotionPieceType) {

    if (!isCapture) {
      switch (movingPieceType) {
        case BISHOP:
          if (fromRank == Rank.NONE) {
            if (fromFile == File.NONE) {
              return SanType.BISHOP_NON_CAPTURING_NEITHER_MOVE;
            }
            return SanType.BISHOP_NON_CAPTURING_FILE_MOVE;
          }
          if (fromFile == File.NONE) {
            return SanType.BISHOP_NON_CAPTURING_RANK_MOVE;
          }
          return SanType.BISHOP_NON_CAPTURING_SQUARE_MOVE;
        case KING:
          return SanType.KING_NON_CASTLING_NON_CAPTURING_MOVE;
        case KNIGHT:
          if (fromRank == Rank.NONE) {
            if (fromFile == File.NONE) {
              return SanType.KNIGHT_NON_CAPTURING_NEITHER_MOVE;
            }
            return SanType.KNIGHT_NON_CAPTURING_FILE_MOVE;
          }
          if (fromFile == File.NONE) {
            return SanType.KNIGHT_NON_CAPTURING_RANK_MOVE;
          }
          return SanType.KNIGHT_NON_CAPTURING_SQUARE_MOVE;
        case PAWN:
          if (promotionPieceType == PromotionPieceType.NONE) {
            return SanType.PAWN_NON_CAPTURING_NON_PROMOTION_MOVE;
          }
          return SanType.PAWN_NON_CAPTURING_PROMOTION_MOVE;
        case QUEEN:
          if (fromRank == Rank.NONE) {
            if (fromFile == File.NONE) {
              return SanType.QUEEN_NON_CAPTURING_NEITHER_MOVE;
            }

            return SanType.QUEEN_NON_CAPTURING_FILE_MOVE;
          }
          if (fromFile == File.NONE) {
            return SanType.QUEEN_NON_CAPTURING_RANK_MOVE;
          }
          return SanType.QUEEN_NON_CAPTURING_SQUARE_MOVE;
        case ROOK:
          if (fromRank == Rank.NONE) {
            if (fromFile == File.NONE) {
              return SanType.ROOK_NON_CAPTURING_NEITHER_MOVE;
            }
            return SanType.ROOK_NON_CAPTURING_FILE_MOVE;
          }
          if (fromFile == File.NONE) {
            return SanType.ROOK_NON_CAPTURING_RANK_MOVE;
          }
          return SanType.ROOK_NON_CAPTURING_SQUARE_MOVE;
        default:
        case NONE:
          throw new IllegalArgumentException();
      }
    }

    // now capture
    switch (movingPieceType) {
      case BISHOP:
        if (fromRank == Rank.NONE) {
          if (fromFile == File.NONE) {
            return SanType.BISHOP_CAPTURING_NEITHER_MOVE;
          }
          return SanType.BISHOP_CAPTURING_FILE_MOVE;
        }
        if (fromFile == File.NONE) {
          return SanType.BISHOP_CAPTURING_RANK_MOVE;
        }
        return SanType.BISHOP_CAPTURING_SQUARE_MOVE;
      case KING:
        return SanType.KING_NON_CASTLING_CAPTURING_MOVE;
      case KNIGHT:
        if (fromRank == Rank.NONE) {
          if (fromFile == File.NONE) {
            return SanType.KNIGHT_CAPTURING_NEITHER_MOVE;
          }
          return SanType.KNIGHT_CAPTURING_FILE_MOVE;
        }
        if (fromFile == File.NONE) {
          return SanType.KNIGHT_CAPTURING_RANK_MOVE;
        }
        return SanType.KNIGHT_CAPTURING_SQUARE_MOVE;
      case PAWN:
        if (promotionPieceType == PromotionPieceType.NONE) {
          return SanType.PAWN_CAPTURING_NON_PROMOTION_MOVE;
        }
        return SanType.PAWN_CAPTURING_PROMOTION_MOVE;
      case QUEEN:
        if (fromRank == Rank.NONE) {
          if (fromFile == File.NONE) {
            return SanType.QUEEN_CAPTURING_NEITHER_MOVE;
          }
          return SanType.QUEEN_CAPTURING_FILE_MOVE;
        }
        if (fromFile == File.NONE) {
          return SanType.QUEEN_CAPTURING_RANK_MOVE;
        }
        return SanType.QUEEN_CAPTURING_SQUARE_MOVE;
      case ROOK:
        if (fromRank == Rank.NONE) {
          if (fromFile == File.NONE) {
            return SanType.ROOK_CAPTURING_NEITHER_MOVE;
          }
          return SanType.ROOK_CAPTURING_FILE_MOVE;
        }
        if (fromFile == File.NONE) {
          return SanType.ROOK_CAPTURING_RANK_MOVE;
        }
        return SanType.ROOK_CAPTURING_SQUARE_MOVE;
      default:
      case NONE:
        throw new IllegalArgumentException();
    }
  }

}
