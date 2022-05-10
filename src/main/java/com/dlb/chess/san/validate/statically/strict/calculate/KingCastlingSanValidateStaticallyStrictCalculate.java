package com.dlb.chess.san.validate.statically.strict.calculate;

import java.util.Map;
import java.util.TreeMap;

import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.CastlingConstants;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.enums.CheckmateOrCheck;
import com.dlb.chess.san.enums.SanLetter;
import com.dlb.chess.san.enums.SanType;
import com.dlb.chess.san.model.SanParse;
import com.google.common.collect.ImmutableMap;

public class KingCastlingSanValidateStaticallyStrictCalculate extends AbstractSanValidateStaticallyStrictCalculate {

  public static ImmutableMap<String, SanParse> calculateSanMap() {

    final Map<String, SanParse> sanCastlingMap = new TreeMap<>();
    // king-side castling
    {
      final String san = CastlingConstants.SAN_CASTLING_KING_SIDE;

      final var model = new SanParse(SanType.KING_CASTLING_KING_SIDE_MOVE,
          new SanConversion(FILE_NONE, RANK_NONE, Square.NONE, PromotionPieceType.NONE, CheckmateOrCheck.NONE));
      sanCastlingMap.put(san, model);
    }
    {
      final var san = CastlingConstants.SAN_CASTLING_KING_SIDE + SanLetter.CHECKMATE.getLetter();
      final var model = new SanParse(SanType.KING_CASTLING_KING_SIDE_MOVE,
          new SanConversion(FILE_NONE, RANK_NONE, Square.NONE, PromotionPieceType.NONE, CheckmateOrCheck.CHECKMATE));
      sanCastlingMap.put(san, model);
    }
    {
      final var san = CastlingConstants.SAN_CASTLING_KING_SIDE + SanLetter.CHECK.getLetter();
      final var model = new SanParse(SanType.KING_CASTLING_KING_SIDE_MOVE,
          new SanConversion(FILE_NONE, RANK_NONE, Square.NONE, PromotionPieceType.NONE, CheckmateOrCheck.CHECK));
      sanCastlingMap.put(san, model);
    }

    // queen-side castling
    {
      final String san = CastlingConstants.SAN_CASTLING_QUEEN_SIDE;
      final var model = new SanParse(SanType.KING_CASTLING_QUEEN_SIDE_MOVE,
          new SanConversion(FILE_NONE, RANK_NONE, Square.NONE, PromotionPieceType.NONE, CheckmateOrCheck.NONE));
      sanCastlingMap.put(san, model);
    }
    {
      final var san = CastlingConstants.SAN_CASTLING_QUEEN_SIDE + SanLetter.CHECKMATE.getLetter();
      final var model = new SanParse(SanType.KING_CASTLING_QUEEN_SIDE_MOVE,
          new SanConversion(FILE_NONE, RANK_NONE, Square.NONE, PromotionPieceType.NONE, CheckmateOrCheck.CHECKMATE));
      sanCastlingMap.put(san, model);
    }
    {
      final var san = CastlingConstants.SAN_CASTLING_QUEEN_SIDE + SanLetter.CHECK.getLetter();
      final var model = new SanParse(SanType.KING_CASTLING_QUEEN_SIDE_MOVE,
          new SanConversion(FILE_NONE, RANK_NONE, Square.NONE, PromotionPieceType.NONE, CheckmateOrCheck.CHECK));
      sanCastlingMap.put(san, model);
    }

    return NonNullWrapperCommon.copyOfMap(sanCastlingMap);
  }

}
