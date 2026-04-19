package com.dlb.chess.san.validate;

import java.util.Set;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.enums.SanFormat;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.format.SanValidateFormat;
import com.dlb.chess.san.validate.movement.SanValidateMovement;

public class SanValidation extends AbstractSan {

  public static MoveSpecification validateSan(String san, ApiBoard board) throws SanValidationException {
    final var sanParse = SanValidateFormat.validateFormat(san);

    SanValidateNonMovement.validateNonMovement(sanParse);

    final Side havingMove = board.getHavingMove();
    SanValidateMovement.validateMovement(sanParse, havingMove);

    final var sanFormat = sanParse.sanFormat();
    final var sanConversion = sanParse.sanConversion();

    SanValidatePieceExists.validatePieceExists(havingMove, sanFormat, sanConversion, sanConversion.movingPieceType(),
        board.getStaticPosition());

    SanValidateDestination.validateDestinationSquareSemantics(board, havingMove, sanFormat, sanConversion);

    final Set<LegalMove> legalMovesCandidates = SanValidateLegalMoves.calculateLegalMovesCandidates(board, havingMove,
        sanParse);
    SanValidateLegalMoves.validateAgainstLegalMoves(board, havingMove, legalMovesCandidates, sanFormat, sanConversion);

    final LegalMove legalMoveOnlyCandidate = SanValidateLegalMoves.calculateOnlyPossibleLegalMove(sanFormat,
        sanConversion, legalMovesCandidates);
    final MoveSpecification moveSpecification = SanValidateLegalMoves.calculateMoveSpecificationForSan(board,
        havingMove, sanFormat, sanConversion, legalMoveOnlyCandidate.moveSpecification());
    if (!moveSpecification.equals(legalMoveOnlyCandidate.moveSpecification())) {
      throw new ProgrammingMistakeException("A mistake happened in the move construction");
    }

    SanValidateCheck.validateSanTerminalMarker(board, sanConversion.sanTerminalMarker(), moveSpecification);

    return moveSpecification;
  }
}
