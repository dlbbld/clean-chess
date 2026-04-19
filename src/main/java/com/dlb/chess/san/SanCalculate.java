package com.dlb.chess.san;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.san.enums.SanFormat;
import com.dlb.chess.san.enums.SanSymbol;
import com.dlb.chess.san.enums.SanTerminalMarker;

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

    AbstractSan.appendSanTerminalMarker(san, sanTerminalMarker);

    return NonNullWrapperCommon.toString(san);
  }

  public static SanFormat calculateSanFormat(boolean isCapture, File fromFile, Rank fromRank, PieceType movingPieceType,
      PromotionPieceType promotionPieceType) {

    if (movingPieceType == PAWN) {
      if (promotionPieceType == PromotionPieceType.NONE) {
        return isCapture ? SanFormat.PAWN_CAPTURING_NON_PROMOTION : SanFormat.PAWN_NON_CAPTURING_NON_PROMOTION;
      }
      return isCapture ? SanFormat.PAWN_CAPTURING_PROMOTION : SanFormat.PAWN_NON_CAPTURING_PROMOTION;
    }

    if (movingPieceType == KING) {
      return isCapture ? SanFormat.KING_NON_CASTLING_CAPTURING : SanFormat.KING_NON_CASTLING_NON_CAPTURING;
    }

    // RNBQ
    if (fromRank == Rank.NONE) {
      if (fromFile == File.NONE) {
        return isCapture ? SanFormat.RNBQ_CAPTURING_NEITHER : SanFormat.RNBQ_NON_CAPTURING_NEITHER;
      }
      return isCapture ? SanFormat.RNBQ_CAPTURING_FILE : SanFormat.RNBQ_NON_CAPTURING_FILE;
    }
    if (fromFile == File.NONE) {
      return isCapture ? SanFormat.RNBQ_CAPTURING_RANK : SanFormat.RNBQ_NON_CAPTURING_RANK;
    }
    return isCapture ? SanFormat.RNBQ_CAPTURING_SQUARE : SanFormat.RNBQ_NON_CAPTURING_SQUARE;
  }

}
