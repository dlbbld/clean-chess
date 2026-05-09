package com.dlb.chess.san.exceptions;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.common.exceptions.UsageException;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.model.ForgivenItem;
import com.google.common.collect.ImmutableList;

/**
 * Thrown when the lenient SAN parser cannot resolve the input to a legal move even after applying every supported
 * tolerance. Carries the original text, the deepest underlying strict-pipeline reason (when the failure originated
 * there), and any forgiven items the lenient layer had already accumulated before the failure point — so the consumer
 * can see how far the parse got.
 */
@SuppressWarnings("null")
public class LenientSanParserValidationException extends UsageException {

  private final String originalText;
  private final @Nullable SanValidationProblem underlyingSanValidationProblem;
  private final @Nullable GameStatus gameStatus;
  private final @NonNull ImmutableList<@NonNull ForgivenItem> forgivenItemsAccumulated;

  public LenientSanParserValidationException(String message, String originalText,
      @Nullable SanValidationProblem underlyingSanValidationProblem,
      @NonNull ImmutableList<@NonNull ForgivenItem> forgivenItemsAccumulated) {
    this(message, originalText, underlyingSanValidationProblem, null, forgivenItemsAccumulated);
  }

  /**
   * Constructor used when the underlying strict failure carries a {@link GameStatus} (the {@code GAME_ALREADY_ENDED}
   * case): propagate the status so callers — particularly the lenient PGN parser — can identify the specific
   * FIDE-automatic termination without parsing the message.
   */
  public LenientSanParserValidationException(String message, String originalText,
      @Nullable SanValidationProblem underlyingSanValidationProblem, @Nullable GameStatus gameStatus,
      @NonNull ImmutableList<@NonNull ForgivenItem> forgivenItemsAccumulated) {
    super(message);
    this.originalText = originalText;
    this.underlyingSanValidationProblem = underlyingSanValidationProblem;
    this.gameStatus = gameStatus;
    this.forgivenItemsAccumulated = NonNullWrapperCommon.copyOfList(forgivenItemsAccumulated);
  }

  public String getOriginalText() {
    return originalText;
  }

  /**
   * The deepest underlying strict-pipeline rejection reason, when the lenient parser exhausted its recovery options and
   * the final failure came from the strict layer; {@code null} if the lenient layer rejected the input on shape grounds
   * before the strict layer was consulted (e.g. mixed {@code 0-O} castling).
   */
  public @Nullable SanValidationProblem getUnderlyingSanValidationProblem() {
    return underlyingSanValidationProblem;
  }

  /**
   * The forgiven items the lenient parser had already accumulated before failing. Useful for diagnostics — shows which
   * tolerances applied before the input became unrecoverable.
   */
  public @NonNull ImmutableList<@NonNull ForgivenItem> getForgivenItemsAccumulated() {
    return forgivenItemsAccumulated;
  }

  /**
   * The {@link GameStatus} that ended the game when the underlying strict failure was {@code GAME_ALREADY_ENDED};
   * {@code null} otherwise.
   */
  public @Nullable GameStatus getGameStatus() {
    return gameStatus;
  }

}
