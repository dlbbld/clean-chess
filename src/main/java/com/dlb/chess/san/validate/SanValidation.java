package com.dlb.chess.san.validate;

import java.util.Set;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ChessBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.utility.BasicChessUtility;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.san.AbstractSan;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.format.SanValidateFormat;
import com.dlb.chess.san.validate.movement.SanValidateMovement;

public class SanValidation extends AbstractSan {

  public static MoveSpecification validateSan(String san, ChessBoard board) throws SanValidationException {
    validateGameNotEnded(board);

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

  /**
   * Top-of-pipeline check: a board with history represents a game, and once any FIDE-automatic termination has been
   * reached the game has ended permanently — no further moves are accepted.
   *
   * <p>
   * The five terminal statuses are: checkmate, stalemate, mutual insufficient material (FIDE 5.2.2 dead position),
   * fivefold repetition, and the 75-move rule. Single-side insufficient-material diagnostics, the claimable draws
   * (3-fold repetition, 50-move rule), and ongoing positions are deliberately NOT rejected here.
   */
  private static void validateGameNotEnded(ChessBoard board) throws SanValidationException {
    final GameStatus gameStatus = BasicChessUtility.calculateGameStatus(board);
    if (!gameStatus.isAutomaticTermination()) {
      return;
    }
    throw new SanValidationException(SanValidationProblem.GAME_ALREADY_ENDED,
        Message.getString("validation.san.gameAlreadyEnded", NonNullWrapperCommon.name(gameStatus)), gameStatus);
  }
}
