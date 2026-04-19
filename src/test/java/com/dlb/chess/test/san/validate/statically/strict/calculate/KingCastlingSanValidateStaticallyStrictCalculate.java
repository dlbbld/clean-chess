package com.dlb.chess.test.san.validate.statically.strict.calculate;

import java.util.Map;
import java.util.TreeMap;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.CastlingConstants;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.enums.SanFormat;
import com.dlb.chess.san.enums.SanSymbol;
import com.dlb.chess.san.enums.SanTerminalMarker;
import com.dlb.chess.san.model.SanParse;
import com.google.common.collect.ImmutableMap;

public class KingCastlingSanValidateStaticallyStrictCalculate extends AbstractSanValidateStaticallyStrictCalculate {

  public static ImmutableMap<String, SanParse> calculateSanMap() {

    final Map<String, SanParse> sanCastlingMap = new TreeMap<>();

    initializeKingSide(sanCastlingMap);
    initializeQueenSide(sanCastlingMap);

    return NonNullWrapperCommon.copyOfMap(sanCastlingMap);
  }

  private static void initializeKingSide(Map<String, SanParse> sanCastlingMap) {
    initializeKingSideNoCheck(sanCastlingMap);
    initializeKingSideCheckmate(sanCastlingMap);
    initializeKingSideCheck(sanCastlingMap);
  }

  private static void initializeKingSideNoCheck(Map<String, SanParse> sanCastlingMap) {
    final String san = CastlingConstants.SAN_CASTLING_KING_SIDE;
    final var model = new SanParse(SanFormat.KING_CASTLING_KING_SIDE,
        new SanConversion(PieceType.NONE, FILE_NONE, RANK_NONE, Square.NONE, PromotionPieceType.NONE,
            SanTerminalMarker.NONE));
    sanCastlingMap.put(san, model);

  }

  private static void initializeKingSideCheckmate(Map<String, SanParse> sanCastlingMap) {
    final var san = CastlingConstants.SAN_CASTLING_KING_SIDE + SanSymbol.CHECKMATE.getSymbol();
    final var model = new SanParse(SanFormat.KING_CASTLING_KING_SIDE,
        new SanConversion(PieceType.NONE, FILE_NONE, RANK_NONE, Square.NONE, PromotionPieceType.NONE,
            SanTerminalMarker.CHECKMATE));
    sanCastlingMap.put(san, model);

  }

  private static void initializeKingSideCheck(Map<String, SanParse> sanCastlingMap) {
    final var san = CastlingConstants.SAN_CASTLING_KING_SIDE + SanSymbol.CHECK.getSymbol();
    final var model = new SanParse(SanFormat.KING_CASTLING_KING_SIDE,
        new SanConversion(PieceType.NONE, FILE_NONE, RANK_NONE, Square.NONE, PromotionPieceType.NONE,
            SanTerminalMarker.CHECK));
    sanCastlingMap.put(san, model);

  }

  private static void initializeQueenSide(Map<String, SanParse> sanCastlingMap) {
    initializeQueenSideNoCheck(sanCastlingMap);
    initializeQueenSideCheckmate(sanCastlingMap);
    initializeQueenSideCheck(sanCastlingMap);
  }

  private static void initializeQueenSideNoCheck(Map<String, SanParse> sanCastlingMap) {
    final String san = CastlingConstants.SAN_CASTLING_QUEEN_SIDE;
    final var model = new SanParse(SanFormat.KING_CASTLING_QUEEN_SIDE,
        new SanConversion(PieceType.NONE, FILE_NONE, RANK_NONE, Square.NONE, PromotionPieceType.NONE,
            SanTerminalMarker.NONE));
    sanCastlingMap.put(san, model);

  }

  private static void initializeQueenSideCheckmate(Map<String, SanParse> sanCastlingMap) {
    final var san = CastlingConstants.SAN_CASTLING_QUEEN_SIDE + SanSymbol.CHECKMATE.getSymbol();
    final var model = new SanParse(SanFormat.KING_CASTLING_QUEEN_SIDE,
        new SanConversion(PieceType.NONE, FILE_NONE, RANK_NONE, Square.NONE, PromotionPieceType.NONE,
            SanTerminalMarker.CHECKMATE));
    sanCastlingMap.put(san, model);
  }

  private static void initializeQueenSideCheck(Map<String, SanParse> sanCastlingMap) {
    final var san = CastlingConstants.SAN_CASTLING_QUEEN_SIDE + SanSymbol.CHECK.getSymbol();
    final var model = new SanParse(SanFormat.KING_CASTLING_QUEEN_SIDE,
        new SanConversion(PieceType.NONE, FILE_NONE, RANK_NONE, Square.NONE, PromotionPieceType.NONE,
            SanTerminalMarker.CHECK));
    sanCastlingMap.put(san, model);
  }
}
