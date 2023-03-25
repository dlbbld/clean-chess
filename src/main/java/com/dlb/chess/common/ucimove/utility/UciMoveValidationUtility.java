package com.dlb.chess.common.ucimove.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.enums.NotationPromotionPiece;
import com.dlb.chess.common.enums.UciValidateHelper;
import com.dlb.chess.model.UciMove;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public abstract class UciMoveValidationUtility implements EnumConstants {

  private static final ImmutableList<UciMove> UCI_MOVE_LIST;
  private static final ImmutableMap<String, UciMove> UCI_MOVE_TEXT_LOOKUP;
  private static final ImmutableMap<UciValidateHelper, UciMove> UCI_MOVE_ENUM_LOOKUP;

  static {

    final List<UciMove> uciMoveList = new ArrayList<>();
    final Map<String, UciMove> uciMoveTextLookup = new TreeMap<>();
    final Map<UciValidateHelper, UciMove> uciMoveEnumLookup = new TreeMap<>();

    for (final UciValidateHelper uciValidate : UciValidateHelper.values()) {
      final UciMove uciMove = calculateUciMove(uciValidate);

      uciMoveList.add(uciMove);
      uciMoveTextLookup.put(uciMove.text(), uciMove);
      uciMoveEnumLookup.put(uciValidate, uciMove);
    }

    UCI_MOVE_LIST = NonNullWrapperCommon.copyOfList(uciMoveList);
    UCI_MOVE_TEXT_LOOKUP = NonNullWrapperCommon.copyOfMap(uciMoveTextLookup);
    UCI_MOVE_ENUM_LOOKUP = NonNullWrapperCommon.copyOfMap(uciMoveEnumLookup);
  }

  public static boolean exists(String text) {
    return UCI_MOVE_TEXT_LOOKUP.containsKey(text);
  }

  public static UciMove lookup(String uciMoveStr) {
    if (!exists(uciMoveStr)) {
      throw new IllegalArgumentException("No such UCI move exists");
    }
    return NonNullWrapperCommon.get(UCI_MOVE_TEXT_LOOKUP, uciMoveStr);
  }

  public static UciMove read(UciValidateHelper uciValidate) {
    return NonNullWrapperCommon.get(UCI_MOVE_ENUM_LOOKUP, uciValidate);
  }

  public static List<UciMove> getUciMoveList() {
    return UCI_MOVE_LIST;
  }

  private static UciMove calculateUciMove(UciValidateHelper uciMove) {
    final var firstSquareNameUpperCase = uciMove.name().substring(0, 2);
    @SuppressWarnings("null") final Square firstSquare = Square.calculate(firstSquareNameUpperCase.toLowerCase());

    final var secondSquareNameUpperCase = uciMove.name().substring(2, 4);
    @SuppressWarnings("null") final Square secondSquare = Square.calculate(secondSquareNameUpperCase.toLowerCase());

    PromotionPieceType promotionPieceType;
    boolean isPromotion;
    if (uciMove.name().length() == 4) {
      isPromotion = false;
      promotionPieceType = PromotionPieceType.NONE;
    } else if (uciMove.name().length() == 5) {
      isPromotion = true;
      final var promotionPieceLetter = NonNullWrapperCommon.toString(uciMove.name().charAt(4));
      promotionPieceType = NotationPromotionPiece.calculateIgnoreCase(promotionPieceLetter).getPromotionPieceType();
    } else {
      throw new IllegalArgumentException();
    }
    final String uciText = calculateUciMoveStr(firstSquare, secondSquare, promotionPieceType);

    return new UciMove(uciMove, firstSquare, secondSquare, uciText, isPromotion, promotionPieceType);
  }

  public static String calculateUciMoveStr(Square fromSquare, Square toSquare, PromotionPieceType promotionPieceType) {
    final StringBuilder uciMove = new StringBuilder();
    uciMove.append(fromSquare.getName());
    uciMove.append(toSquare.getName());
    if (promotionPieceType != PromotionPieceType.NONE) {
      final var promotionPieceTypeLetterLowerCase = NonNullWrapperCommon
          .toLowerCase(promotionPieceType.getPieceType().getLetter());
      uciMove.append(promotionPieceTypeLetterLowerCase);
    }
    return NonNullWrapperCommon.toString(uciMove);
  }
}
