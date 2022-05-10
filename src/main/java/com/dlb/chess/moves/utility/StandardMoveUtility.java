package com.dlb.chess.moves.utility;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.model.UpdateSquare;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.model.LegalMove;

public class StandardMoveUtility implements EnumConstants {

  public static List<UpdateSquare> performStandardMovements(StaticPosition oldStaticPosition,
      MoveSpecification moveSpecification) {

    final List<UpdateSquare> result = new ArrayList<>();

    final Piece movingPiece = oldStaticPosition.get(moveSpecification.fromSquare());

    result.add(new UpdateSquare(moveSpecification.fromSquare()));
    result.add(new UpdateSquare(moveSpecification.toSquare(), movingPiece));

    return result;
  }

  public static List<UpdateSquare> performStandardUndoMovements(LegalMove lastMove) {

    final List<UpdateSquare> result = new ArrayList<>();

    if (lastMove.pieceCaptured() != Piece.NONE) {
      result.add(new UpdateSquare(lastMove.moveSpecification().toSquare(), lastMove.pieceCaptured()));
    } else {
      result.add(new UpdateSquare(lastMove.moveSpecification().toSquare()));
    }
    result.add(new UpdateSquare(lastMove.moveSpecification().fromSquare(), lastMove.movingPiece()));

    return result;
  }
}
