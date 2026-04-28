package com.dlb.chess.test.pgntest;

/**
 * Marks PGN files in the test corpus whose recorded halfmove sequence continues past one of the
 * five FIDE-automatic terminations (checkmate, stalemate, mutual insufficient material, fivefold
 * repetition, 75-move rule).
 *
 * <p>Under the strict-game invariant such PGNs cannot be fully replayed by the standard parsers
 * — {@code Board.performMove} rejects the first move past the terminating halfmove with a
 * {@code GAME_ALREADY_ENDED} diagnostic. The PGN files themselves are kept on disk because they
 * are deliberately crafted edge-case test fixtures (carefully constructed positions exercising
 * intervening repetitions, sixfold positions, FENs at the 75-move threshold, dead-position
 * captures, and so on). Their test role flips from "fully replay and verify analysis" to
 * "verify that strict-pipeline import correctly rejects continuation."
 *
 * <p>Iterating tests that call {@code Analyzer.calculateAnalysis(...)} or otherwise replay the
 * full halfmove sequence skip files identified by this class. A dedicated rejection test
 * ({@code TestPgnPlaysBeyondTerminationRejection}) iterates them separately and asserts
 * {@code GAME_ALREADY_ENDED} is thrown during import.
 */
public final class PgnPlaysBeyondTermination {

  private PgnPlaysBeyondTermination() {
  }

  /**
   * Returns {@code true} iff the given PGN filename is known to record halfmoves past a FIDE
   * automatic termination, i.e. cannot be fully replayed under the strict-game invariant.
   */
  public static boolean playsBeyondAutomaticTermination(String pgnFileName) {
    // Filename patterns covering the corpus's standard "plays beyond" naming conventions.
    // Note: "_beyond" alone is not sufficient — files like
    // "18_threefold_two_threefolds_beyond.pgn" play past a 3-fold repetition (claimable, NOT
    // an automatic termination), so import still succeeds. We only skip files that play past
    // an automatic termination (5-fold, 75-move).
    if (pgnFileName.contains("fivefold_beyond") || pgnFileName.contains("seventy_five_beyond")
        || pgnFileName.contains("_fivefold_beyond_")) {
      return true;
    }
    // pawn_wall_norgaard_beyond_* are deadlock positions (no capture possible) but NOT
    // FIDE-automatic terminations — the analyzer imports them successfully.
    if (pgnFileName.contains("_sixfold")) {
      return true;
    }
    // basic/fivefold/05_fivefold_beyond.pgn matches via fivefold_beyond above.
    // basic/seventyFive/04-10 *_seventy_five_beyond_* matches via seventy_five_beyond above.
    // Intervening repetition patterns: outer encapsulating fivefold (the inner fivefold ends the
    // game, then the outer claim period continues past).
    if (pgnFileName.contains("encapsulating_fivefold")) {
      return true;
    }
    if (pgnFileName.contains("encapsulating_threefold") && !pgnFileName.startsWith("01_")) {
      return true;
    }
    if (pgnFileName.contains("_interlocked_fivefold")) {
      return true;
    }
    if (pgnFileName.contains("_interlocked_threefold") && pgnFileName.startsWith("07_")) {
      return true;
    }

    // Specific files that exercise dead-position continuation. Note that we only mark files
    // whose recorded sequence continues past the terminating move; PGNs whose last move IS the
    // terminating move (e.g. 01_blog_instant_K_K.pgn ending exactly when K-vs-K is reached)
    // import normally.
    if ("insufficient_material_K_K.pgn".equals(pgnFileName)) {
      return true;
    }

    // Files that start from a FEN with halfmove clock at or above the 75-move threshold AND
    // attempt at least one move from there:
    // - 151+ FENs are rejected at parse time by FenParserAdvanced (position unreachable by
    //   legal play).
    // - 150 FENs are at the terminal moment — the FEN is valid, but any move attempted from it
    //   is rejected as GAME_ALREADY_ENDED.
    // The "no_move" variants of either category have no halfmoves to replay, so the analyzer
    // simply imports the FEN and produces an Analysis of the terminal position without
    // throwing — they are NOT plays-beyond cases.
    if (pgnFileName.startsWith("from_fen_") && !pgnFileName.contains("_no_move_")
        && (pgnFileName.contains("_half_move_clock_150_") || pgnFileName.contains("_half_move_clock_151_")
            || pgnFileName.contains("_half_move_clock_154_"))) {
      return true;
    }
    // For the 151-clock FENs, even the "no_move" variants are rejected — the FEN itself
    // does not parse.
    if (pgnFileName.startsWith("from_fen_") && pgnFileName.contains("_no_move_")
        && pgnFileName.contains("_half_move_clock_151_")) {
      return true;
    }

    // Files starting at halfmove clock 149 that record more than one non-resetting move: the
    // first non-capture/non-pawn move advances the clock to 150 (terminal); subsequent moves
    // are rejected. Capture-first / pawn-first / one-move / no-move variants stay at or below
    // the threshold and replay normally.
    if (pgnFileName.startsWith("from_fen_") && pgnFileName.contains("_half_move_clock_149_")
        && (pgnFileName.contains("_capture_second_move_") || pgnFileName.contains("_two_moves_")
            || pgnFileName.contains("_three_moves_"))) {
      return true;
    }

    return false;
  }

}
