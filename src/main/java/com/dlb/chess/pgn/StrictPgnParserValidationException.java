package com.dlb.chess.pgn;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.common.exceptions.UsageException;
import com.dlb.chess.pgn.StrictPgnParserValidationProblem;
import com.dlb.chess.san.SanValidationProblem;

public class StrictPgnParserValidationException extends UsageException {

  private final StrictPgnParserValidationProblem strictPgnParserValidationProblem;

  private final SanValidationProblem sanValidationProblem;

  /**
   * The {@link GameStatus} that ended the game when {@link #sanValidationProblem} is
   * {@link SanValidationProblem#GAME_ALREADY_ENDED}; {@code null} otherwise.
   */
  private final @Nullable GameStatus gameStatus;

  public StrictPgnParserValidationException(StrictPgnParserValidationProblem strictPgnParserValidationProblem,
      SanValidationProblem sanValidationProblem, String message) {
    this(strictPgnParserValidationProblem, sanValidationProblem, message, null);
  }

  public StrictPgnParserValidationException(StrictPgnParserValidationProblem strictPgnParserValidationProblem,
      SanValidationProblem sanValidationProblem, String message, @Nullable GameStatus gameStatus) {
    super(message);
    this.strictPgnParserValidationProblem = strictPgnParserValidationProblem;
    this.sanValidationProblem = sanValidationProblem;
    this.gameStatus = gameStatus;
  }

  public StrictPgnParserValidationProblem getStrictPgnParserValidationProblem() {
    return strictPgnParserValidationProblem;
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
