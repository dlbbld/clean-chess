package com.dlb.chess.test.winnable;

/**
 * Tri-state verdict for the pawn-wall classifier.
 *
 * <p>
 * {@link #YES} is sound: a position classified {@code YES} is guaranteed to be a permanent pawn-wall barrier (neither
 * king can reach any opposing pawn through legal play, in the helpmate-cooperation sense). {@link #NO} is a definite
 * negative on a clear violation. {@link #UNKNOWN} is the safe fallback for cases the classifier cannot decide — the
 * caller should consult a stronger analysis (e.g. CHA-quick) for these positions.
 *
 * <p>
 * Callers asserting unwinnability based on the pawn-wall heuristic must use {@code verdict == YES}; {@code NO} and
 * {@code UNKNOWN} both mean "fall through to the next detection path."
 *
 * <p>
 * See {@code pawn-wall-soundness.md} for the full design — production geometric check, BFS as test oracle, and the
 * asymmetric agreement contract.
 */
public enum PawnWallVerdict {

  YES,
  NO,
  UNKNOWN;

}
