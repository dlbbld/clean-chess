package com.dlb.chess.san.validate;

import java.util.Set;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.enums.SanFormat;
import com.dlb.chess.san.enums.SanType;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanParse;

public class SanValidation extends AbstractSan {

  public static MoveSpecification validateSan(String san, ApiBoard board) throws SanValidationException {
    final var sanParse = SanValidateFormat.validateFormat(san);
    return validateAgainstPosition(board, sanParse);
  }

  private static MoveSpecification validateAgainstPosition(ApiBoard board, SanParse sanParse) throws SanValidationException {

    final Side havingMove = board.getHavingMove();
    final var sanType = sanParse.sanType();
    final var sanConversion = sanParse.sanConversion();
    final SanFormat sanFormat = sanType.getSanFormat();

    SanValidatePieceExists.validatePieceExists(havingMove, sanFormat, sanConversion, sanType.getMovingPieceType(),
        board.getStaticPosition());

    SanValidateDestination.validateDestinationSquareSemantics(board, havingMove, sanType, sanConversion);

    final Set<LegalMove> legalMovesCandidates = SanValidateLegalMoves.calculateLegalMovesCandidates(board, havingMove,
        sanParse);
    SanValidateLegalMoves.validateAgainstLegalMoves(board.getStaticPosition(), havingMove, legalMovesCandidates,
        sanType, sanConversion);

    final LegalMove legalMoveOnlyCandidate = SanValidateLegalMoves.calculateOnlyPossibleLegalMove(sanFormat,
        sanConversion, legalMovesCandidates);
    final MoveSpecification moveSpecification = SanValidateLegalMoves.calculateMoveSpecificationForSan(board,
        havingMove, sanFormat, sanConversion, legalMoveOnlyCandidate.moveSpecification());
    if (!moveSpecification.equals(legalMoveOnlyCandidate.moveSpecification())) {
      throw new ProgrammingMistakeException("A mistake happened in the move construction");
    }

    SanValidateCheck.validateCheckmateOrCheck(board, sanConversion.checkmateOrCheck(), moveSpecification);

    return moveSpecification;
  }
}
