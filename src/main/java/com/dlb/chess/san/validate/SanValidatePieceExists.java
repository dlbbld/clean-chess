package com.dlb.chess.san.validate;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.MaterialUtility;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.enums.SanFormat;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;

public abstract class SanValidatePieceExists extends AbstractSan {

  public static void validatePieceExists(Side havingMove, SanFormat sanFormat, SanConversion sanConversion,
      PieceType movingPieceType, StaticPosition staticPosition) {
    switch (sanFormat) {
      case KING_CASTLING_KING_SIDE:
      case KING_CASTLING_QUEEN_SIDE:
      case KING_NON_CASTLING_CAPTURING:
      case KING_NON_CASTLING_NON_CAPTURING:
        return;
      case PAWN_NON_CAPTURING_NON_PROMOTION:
      case PAWN_NON_CAPTURING_PROMOTION: {
        // for non-capturing pawn moves, the pawn must be on the to-square's file
        final File pawnFile = sanConversion.toSquare().getFile();
        if (!MaterialUtility.calculateHasPieceType(havingMove, PieceType.PAWN, staticPosition, pawnFile)) {
          throw new SanValidationException(SanValidationProblem.PAWN_NO_PIECE_EXISTS,
              Message.getString("validation.san.pawn.noPieceExists", havingMove.getName(), pawnFile.getLetterString()));
        }
        break;
      }
      case PAWN_CAPTURING_NON_PROMOTION:
      case PAWN_CAPTURING_PROMOTION: {
        // for capturing pawn moves, the SAN specifies the from-file explicitly
        final File pawnFile = sanConversion.fromFile();
        if (!MaterialUtility.calculateHasPieceType(havingMove, PieceType.PAWN, staticPosition, pawnFile)) {
          throw new SanValidationException(SanValidationProblem.PAWN_NO_PIECE_EXISTS,
              Message.getString("validation.san.pawn.noPieceExists", havingMove.getName(), pawnFile.getLetterString()));
        }
        break;
      }
      case PIECE_CAPTURING_NEITHER:
      case PIECE_NON_CAPTURING_NEITHER:
        if (!MaterialUtility.calculateHasPieceType(havingMove, movingPieceType, staticPosition)) {
          throw new SanValidationException(SanValidationProblem.PIECE_NEITHER_NO_PIECE_EXISTS,
              Message.getString("validation.san.notPawn.specification.none.otherThanKing.noPieceExists",
                  havingMove.getName(), movingPieceType.getName()));
        }
        break;
      case PIECE_CAPTURING_FILE:
      case PIECE_NON_CAPTURING_FILE:
        if (!MaterialUtility.calculateHasPieceType(havingMove, movingPieceType, staticPosition,
            sanConversion.fromFile())) {
          throw new SanValidationException(SanValidationProblem.PIECE_FILE_NO_PIECE_EXISTS,
              Message.getString("validation.san.notPawn.specification.file.noPieceExists", havingMove.getName(),
                  movingPieceType.getName(), sanConversion.fromFile().getLetterString()));
        }
        break;
      case PIECE_CAPTURING_RANK:
      case PIECE_NON_CAPTURING_RANK:
        if (!MaterialUtility.calculateHasPieceType(havingMove, movingPieceType, staticPosition,
            sanConversion.fromRank())) {
          throw new SanValidationException(SanValidationProblem.PIECE_RANK_NO_PIECE_EXISTS,
              Message.getString("validation.san.notPawn.specification.rank.noPieceExists", havingMove.getName(),
                  movingPieceType.getName(), NonNullWrapperCommon.valueOf(sanConversion.fromRank().getNumber())));
        }
        break;
      case PIECE_CAPTURING_SQUARE:
      case PIECE_NON_CAPTURING_SQUARE:
        final Square fromSquare = Square.calculate(sanConversion.fromFile(), sanConversion.fromRank());
        final Piece pieceOnFromSquare = staticPosition.get(fromSquare);
        if (pieceOnFromSquare == Piece.NONE || pieceOnFromSquare.getSide() != havingMove
            || pieceOnFromSquare.getPieceType() != movingPieceType) {
          throw new SanValidationException(SanValidationProblem.PIECE_SQUARE_NO_PIECE_EXISTS,
              Message.getString("validation.san.notPawn.specification.square.noPieceExists", havingMove.getName(),
                  movingPieceType.getName(), fromSquare.getName()));
        }
        break;
      default:
        throw new IllegalArgumentException();

    }
  }

}
