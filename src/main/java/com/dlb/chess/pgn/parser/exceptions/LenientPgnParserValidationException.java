package com.dlb.chess.pgn.parser.exceptions;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.common.exceptions.UsageException;
import com.dlb.chess.pgn.parser.enums.LenientPgnParserValidationProblem;
import com.dlb.chess.san.enums.SanValidationProblem;

public class LenientPgnParserValidationException extends UsageException {

  private final LenientPgnParserValidationProblem lenientPgnParserValidationProblem;

  private final SanValidationProblem sanValidationProblem;

  /**
   * The {@link GameStatus} that ended the game when {@link #sanValidationProblem} is
   * {@link SanValidationProblem#GAME_ALREADY_ENDED}; {@code null} otherwise. Carried so the
   * caller can distinguish the five FIDE-automatic terminations (checkmate, stalemate,
   * mutual insufficient material, fivefold repetition, 75-move rule) without having to parse
   * the human-readable message.
   */
  private final @Nullable GameStatus gameStatus;

  public LenientPgnParserValidationException(LenientPgnParserValidationProblem lenientPgnParserValidationProblem,
      SanValidationProblem sanValidationProblem, String message) {
    this(lenientPgnParserValidationProblem, sanValidationProblem, message, null);
  }

  /**
   * Constructor used when a SAN validation failure carries a specific {@link GameStatus} (the
   * GAME_ALREADY_ENDED case): the parser propagates the status so callers can react to the
   * specific termination cause without parsing the message.
   */
  public LenientPgnParserValidationException(LenientPgnParserValidationProblem lenientPgnParserValidationProblem,
      SanValidationProblem sanValidationProblem, String message, @Nullable GameStatus gameStatus) {
    super(message);
    this.lenientPgnParserValidationProblem = lenientPgnParserValidationProblem;
    this.sanValidationProblem = sanValidationProblem;
    this.gameStatus = gameStatus;
  }

  public LenientPgnParserValidationProblem getLenientPgnParserValidationProblem() {
    return lenientPgnParserValidationProblem;
  }

  public SanValidationProblem getSanValidationProblem() {
    return sanValidationProblem;
  }

  /**
   * The {@link GameStatus} that ended the game when {@link #getSanValidationProblem()} is
   * {@link SanValidationProblem#GAME_ALREADY_ENDED}; {@code null} otherwise.
   */
  public @Nullable GameStatus getGameStatus() {
    return gameStatus;
  }

}
