package com.dlb.chess.model;

/**
 * Captures a {@link LegalMove}'s role in the en-passant mechanic.
 *
 * <p>
 * The three values are mutually exclusive by design: a single move cannot simultaneously be an en-passant capture and a
 * two-square pawn advance. Storing this as one enum field (rather than two booleans) prevents the impossible combined
 * state.
 *
 * <ul>
 * <li>{@link #NONE} — the move has no en-passant significance.</li>
 * <li>{@link #EN_PASSANT_CAPTURE} — the move is itself an en-passant capture (a pawn captures an opponent pawn that
 * just made a two-square advance; the captured pawn is not on the destination square).</li>
 * <li>{@link #TWO_SQUARE_ADVANCE} — the move is a pawn's two-square initial advance, which creates an en-passant
 * capture target on the square the pawn skipped over. The opponent may capture en passant on its next move only.</li>
 * </ul>
 */
public enum EnPassantRole {
  NONE,
  EN_PASSANT_CAPTURE,
  TWO_SQUARE_ADVANCE;

  public boolean isEnPassantCapture() {
    return this == EN_PASSANT_CAPTURE;
  }

  public boolean createsEnPassantTarget() {
    return this == TWO_SQUARE_ADVANCE;
  }
}
