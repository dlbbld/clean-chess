package com.dlb.chess.test.pgntest;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
 *
 * <h2>Why an explicit registry, not a substring matcher</h2>
 *
 * <p>The classification is an <strong>explicit, hard-coded set of basenames</strong>
 * ({@link #PGN_FILE_NAMES}), not a substring or pattern match. Substring matching is fragile:
 * if a PGN is renamed and no longer matches the historical pattern, the old logic silently
 * stops classifying it, and tests that should skip "plays beyond" files start replaying them
 * (or vice versa, the rejection test stops exercising it). With an explicit registry, such a
 * drift is detected immediately by {@link com.dlb.chess.test.pgntest.TestPgnPlaysBeyondTerminationRegistry}
 * which walks the corpus on every test run and fails if any registered name no longer exists
 * on disk.
 */
public final class PgnPlaysBeyondTermination {

  /**
   * The complete, explicit list of PGN basenames in the test corpus whose recorded sequence
   * continues past a FIDE-automatic termination.
   *
   * <p>Categories represented (kept in line-grouped form for review):
   * <ul>
   *   <li>basic/fivefold — fivefold repetition continuation</li>
   *   <li>basic/seventyFive — 75-move rule continuation</li>
   *   <li>basic/intervening — outer-claim / inner-automatic interactions
   *       (encapsulating-fivefold, interlocked-fivefold, and the asymmetric 07-interlocked-threefold case)</li>
   *   <li>basic/sixfold — explicit sixfold repetition fixtures (continuation past the implicit fivefold)</li>
   *   <li>basic/insufficientMaterial — K-vs-K continuation</li>
   *   <li>basic/fromFen — FEN start positions with halfmove clock at or above the threshold</li>
   *   <li>regression — historical games whose recorded notation runs past the automatic termination</li>
   * </ul>
   *
   * <p>Adding or renaming such a fixture in the corpus must be paired with a corresponding edit
   * here. The registry validator catches missing entries; coverage of newly added "plays
   * beyond" files surfaces naturally because the rejection test will fail to assert rejection
   * on them.
   */
  public static final Set<String> PGN_FILE_NAMES = Set.of(
      // basic/intervening — outer claim / inner automatic termination interactions.
      // 01_intervening_threefold_encapsulating_threefold.pgn does NOT cross an automatic
      // termination (both are claimable threefolds) — intentionally excluded.
      "02_intervening_threefold_encapsulating_fivefold.pgn",
      "03_intervening_fivefold_encapsulating_threefold.pgn",
      "04_intervening_fivefold_encapsulating_fivefold.pgn",
      // 05_intervening_threefold_interlocked_threefold.pgn and 06_*_interlocked_fivefold are
      // partly excluded — only 06_*_interlocked_fivefold and 07_*_interlocked_threefold cross
      // an automatic termination per the historical classification. 08_*_interlocked_fivefold
      // also crosses fivefold.
      "06_intervening_threefold_interlocked_fivefold.pgn",
      "07_intervening_fivefold_interlocked_threefold.pgn",
      "08_intervening_fivefold_interlocked_fivefold.pgn",

      // basic/fivefold — fixture that explicitly continues one halfmove past the fivefold position.
      "05_fivefold_beyond.pgn",

      // basic/sixfold — six occurrences continuation (past the implicit fivefold termination).
      "06_fivefold_end_with_first_sixfold.pgn",

      // basic/seventyFive — fixtures crossing the 75-move threshold.
      "04_seventy_five_beyond_half_move_clock_154_end_with_half_move_clock_99.pgn",
      "05_seventy_five_beyond_half_move_clock_154_end_with_half_move_clock_100.pgn",
      "06_seventy_five_beyond_half_move_clock_154_end_with_half_move_clock_117.pgn",
      "07_seventy_five_beyond_half_move_clock_154_end_with_half_move_clock_149.pgn",
      "08_seventy_five_beyond_half_move_clock_154_end_with_half_move_clock_150.pgn",
      "09_seventy_five_beyond_half_move_clock_154_end_with_half_move_clock_178.pgn",
      "10_seventy_five_beyond_half_move_clock_154_beyond_half_move_clock_178_end_with_half_move_clock_10.pgn",

      // basic/insufficientMaterial — K-vs-K with continuation past the dead-position termination.
      "insufficient_material_K_K.pgn",

      // basic/fromFen — sixfold-from-FEN fixtures.
      "from_fen_repetition_from_zero_moves_white_to_move_sixfold.pgn",
      "from_fen_repetition_from_zero_moves_black_to_move_sixfold.pgn",
      "from_fen_repetition_from_one_move_white_to_move_sixfold.pgn",
      "from_fen_repetition_from_one_move_black_to_move_sixfold.pgn",
      "from_fen_repetition_from_two_moves_white_to_move_sixfold.pgn",
      "from_fen_repetition_from_two_moves_black_to_move_sixfold.pgn",
      "from_fen_repetition_from_three_moves_white_to_move_sixfold.pgn",
      "from_fen_repetition_from_three_moves_black_to_move_sixfold.pgn",

      // basic/fromFen — halfmove-clock-149 fixtures: capture-second / two-moves / three-moves variants
      // each cross the 75-move threshold. Capture-first / pawn-first / one-move / no-move variants stay
      // under the threshold and are intentionally NOT in the registry.
      "from_fen_capture_second_move_half_move_clock_149_white_to_move.pgn",
      "from_fen_capture_second_move_half_move_clock_149_black_to_move.pgn",
      "from_fen_two_moves_half_move_clock_149_white_to_move.pgn",
      "from_fen_two_moves_half_move_clock_149_black_to_move.pgn",
      "from_fen_three_moves_half_move_clock_149_white_to_move.pgn",
      "from_fen_three_moves_half_move_clock_149_black_to_move.pgn",

      // basic/fromFen — halfmove-clock-150 fixtures: any non-no-move variant attempts at least one
      // move from the terminal position and is rejected as GAME_ALREADY_ENDED.
      "from_fen_capture_first_move_half_move_clock_150_white_to_move.pgn",
      "from_fen_capture_first_move_half_move_clock_150_black_to_move.pgn",
      "from_fen_capture_second_move_half_move_clock_150_white_to_move.pgn",
      "from_fen_capture_second_move_half_move_clock_150_black_to_move.pgn",
      "from_fen_one_move_half_move_clock_150_white_to_move.pgn",
      "from_fen_one_move_half_move_clock_150_black_to_move.pgn",
      "from_fen_pawn_first_move_half_move_clock_150_white_to_move.pgn",
      "from_fen_pawn_first_move_half_move_clock_150_black_to_move.pgn",
      "from_fen_two_moves_half_move_clock_150_white_to_move.pgn",
      "from_fen_two_moves_half_move_clock_150_black_to_move.pgn",
      "from_fen_three_moves_half_move_clock_150_white_to_move.pgn",
      "from_fen_three_moves_half_move_clock_150_black_to_move.pgn",

      // basic/fromFen — halfmove-clock-151 fixtures: the FEN itself does not parse (clock above
      // the limit), so every variant — including the no-move ones — is rejected at parse time.
      "from_fen_capture_first_move_half_move_clock_151_white_to_move.pgn",
      "from_fen_capture_first_move_half_move_clock_151_black_to_move.pgn",
      "from_fen_capture_second_move_half_move_clock_151_white_to_move.pgn",
      "from_fen_capture_second_move_half_move_clock_151_black_to_move.pgn",
      "from_fen_no_move_half_move_clock_151_white_to_move.pgn",
      "from_fen_no_move_half_move_clock_151_black_to_move.pgn",
      "from_fen_one_move_half_move_clock_151_white_to_move.pgn",
      "from_fen_one_move_half_move_clock_151_black_to_move.pgn",
      "from_fen_pawn_first_move_half_move_clock_151_white_to_move.pgn",
      "from_fen_pawn_first_move_half_move_clock_151_black_to_move.pgn",
      "from_fen_two_moves_half_move_clock_151_white_to_move.pgn",
      "from_fen_two_moves_half_move_clock_151_black_to_move.pgn",
      "from_fen_three_moves_half_move_clock_151_white_to_move.pgn",
      "from_fen_three_moves_half_move_clock_151_black_to_move.pgn",

      // regression — recorded historical games whose final score-line continues past the
      // automatic termination (typically players continued to play after the fivefold or
      // 75-move position arose).
      "fivefold_beyond_savchenko_yu_y2017.pgn",
      "fivefold_beyond_wang_yu_2017.pgn",
      "fivefold_beyond_yu_alekseenko_2018.pgn",
      "seventy_five_beyond_anton_guijarro_antipov_2015.pgn",
      "seventy_five_beyond_aronian_navara_2017.pgn",
      "seventy_five_beyond_cheparinov_jones_2019.pgn",
      "seventy_five_beyond_moiseenko_radjabov_2016.pgn",
      "seventy_five_beyond_onischuk_guseinov_2014.pgn");

  private PgnPlaysBeyondTermination() {
  }

  /**
   * Returns {@code true} iff the given PGN basename is registered as recording halfmoves past a
   * FIDE automatic termination, i.e. cannot be fully replayed under the strict-game invariant.
   *
   * <p>This is an exact basename match against {@link #PGN_FILE_NAMES}. There is no substring
   * or pattern matching — see the class-level Javadoc for the rationale.
   */
  public static boolean playsBeyondAutomaticTermination(String pgnFileName) {
    return PGN_FILE_NAMES.contains(pgnFileName);
  }

  /**
   * Walks the PGN test corpus rooted at {@code corpusRoot} and returns the registered basenames
   * that are NOT present on disk. An empty result means the registry is in sync with the
   * corpus; a non-empty result means one or more registered files have been renamed or removed
   * and the registry must be updated.
   *
   * <p>The companion test {@link com.dlb.chess.test.pgntest.TestPgnPlaysBeyondTerminationRegistry}
   * calls this method against the live corpus and fails the build when the result is non-empty.
   */
  public static SortedSet<String> findMissingRegisteredFiles(Path corpusRoot) throws IOException {
    final Set<String> existingPgnBasenames = new HashSet<>();
    Files.walkFileTree(corpusRoot, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        final String name = file.getFileName().toString();
        if (name.endsWith(".pgn")) {
          existingPgnBasenames.add(name);
        }
        return FileVisitResult.CONTINUE;
      }
    });
    final SortedSet<String> missing = new TreeSet<>();
    for (final String registered : PGN_FILE_NAMES) {
      if (!existingPgnBasenames.contains(registered)) {
        missing.add(registered);
      }
    }
    return missing;
  }
}
