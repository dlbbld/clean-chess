package com.dlb.chess.san;

import java.util.List;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.utility.BasicChessUtility;
import com.dlb.chess.messages.Message;
import com.dlb.chess.model.LegalMove;

/**
 * Public entry point for the strict SAN pipeline. Accepts canonical SAN only; the result is symmetric in shape with
 * {@link com.dlb.chess.san.LenientSanParser} so callers can switch between strict and lenient by changing one method
 * call. Use {@link com.dlb.chess.san.LenientSanParser} when parsing real-world PGN that may contain forgivable
 * deviations from canonical SAN.
 */
public class StrictSanParser extends AbstractSan {

  /**
   * Parses {@code san} as canonical SAN against {@code board} and returns the resolved {@link MoveSpecification}.
   *
   * @throws SanValidationException if the input is not canonical SAN, or is canonical but does not represent a legal
   *                                move on the current position
   */
  public static StrictSanParserValidationResult parseText(String san, Board board) throws SanValidationException {
    final MoveSpecification moveSpecification = parseTextInternal(san, board);
    return new StrictSanParserValidationResult(moveSpecification);
  }

  private static MoveSpecification parseTextInternal(String san, Board board) throws SanValidationException {
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

    final List<LegalMove> legalMovesCandidates = SanValidateLegalMoves.calculateLegalMovesCandidates(board, havingMove,
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
   * reached the game has ended permanently â€” no further moves are accepted.
   *
   * <p>
   * The five terminal statuses are: checkmate, stalemate, mutual insufficient material (FIDE 5.2.2 dead position),
   * fivefold repetition, and the 75-move rule. Single-side insufficient-material diagnostics, the claimable draws
   * (3-fold repetition, 50-move rule), and ongoing positions are deliberately NOT rejected here.
   */
  private static void validateGameNotEnded(Board board) throws SanValidationException {
    final GameStatus gameStatus = BasicChessUtility.calculateGameStatus(board);
    if (!gameStatus.isAutomaticTermination()) {
      return;
    }
    throw new SanValidationException(SanValidationProblem.GAME_ALREADY_ENDED,
        Message.getString("validation.san.gameAlreadyEnded", Nulls.name(gameStatus)), gameStatus);
  }
}
