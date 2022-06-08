package com.dlb.chess.common.interfaces;

import java.util.List;
import java.util.Set;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.common.model.DynamicPosition;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.model.MoveRepresentation;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.CastlingRightBoth;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.unwinnability.full.enums.DeadPositionFull;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.quick.enums.DeadPositionQuick;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public interface ApiBoard {

  boolean performMove(MoveSpecification moveSpecification);

  boolean performMove(String san);

  boolean performMoves(String... sanArray);

  void unperformMove();

  boolean isCheck();

  boolean isCheckmate();

  boolean isStalemate();

  boolean isInsufficientMaterial();

  boolean isInsufficientMaterial(Side side);

  int getHalfMoveClock();

  /**
   * The full move number for the last half move played. Please note that this is different from the full move number
   * field in the FEN for the current position. Here the full move number for the next half move if any is set. We align
   * here with the other methods relating to moves, like for example {@link #getMovingPiece()}, which relate to the last
   * move.
   *
   * @return The full move number for the last half move played.
   */
  int getFullMoveNumber();

  int getInitialFenFullMoveNumber();

  int getFullMoveNumberForNextHalfMove();

  int getRepetitionCount();

  boolean isFiftyMove();

  boolean canClaimFiftyMoveRuleWithOwnMove();

  boolean canClaimFiftyMoveRule();

  boolean isSeventyFiftyMove();

  boolean isThreefoldRepetition();

  boolean canClaimThreefoldRepetitionRuleWithOwnMove();

  boolean canClaimThreefoldRepetitionRule();

  boolean isFivefoldRepetition();

  InsufficientMaterial calculateInsufficientMaterial();

  DeadPositionQuick isDeadPositionQuick();

  DeadPositionFull isDeadPositionFull();

  UnwinnableQuick isUnwinnableQuick(Side side);

  UnwinnableFull isUnwinnableFull(Side side);

  boolean isGameEnd();

  // name collision with API Carlos's method which must be adapted for warnings
  String getFen();

  Fen getInitialFen();

  String getSan();

  String getLan();

  /**
   * The moved piece except for castling.
   *
   * @return For all moves except castling moves, the moved piece is returned. For the castling move the none piece is
   *         returned.
   */
  Piece getMovingPiece();

  boolean isCapture();

  Side getHavingMove();

  Side getNotHavingMove();

  /**
   * Checks if en passant capture possible and legal.
   *
   * @return true if a pawn of first player just advanced two squares next to a pawn of the second player, and capturing
   *         the pawn of the first player en passant by the second player is a legal move, false otherwise.
   */
  boolean isEnPassantCapturePossible();

  CastlingRightBoth getCastlingRightBoth();

  CastlingRight getCastlingRight(Side havingMove);

  int getPerformedHalfMoveCount();

  List<DynamicPosition> getDynamicPositionList();

  DynamicPosition getDynamicPosition();

  List<HalfMove> getHalfMoveList();

  Set<MoveSpecification> getPossibleMoveSpecificationSet();

  Set<LegalMove> getLegalMoveSet();

  List<MoveSpecification> getPerformedMoveSpecificationList();

  List<LegalMove> getPerformedLegalMoveList();

  List<MoveRepresentation> getLegalMovesRepresentation();

  Set<String> getLegalMovesSan();

  Set<String> getLegalMovesUci();

  LegalMove getLastMove();

  StaticPosition getStaticPosition();

  StaticPosition getStaticPositionBeforeLastMove();

  boolean isFirstMove();

  Square getEnPassantCaptureTargetSquare();

}
