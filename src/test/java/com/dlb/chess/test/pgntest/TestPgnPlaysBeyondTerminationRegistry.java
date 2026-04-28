package com.dlb.chess.test.pgntest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.SortedSet;

import org.junit.jupiter.api.Test;

import com.dlb.chess.test.pgntest.constants.PgnTestConstants;

/**
 * Validates that every basename registered in {@link PgnPlaysBeyondTermination#PGN_FILE_NAMES}
 * is still present somewhere in the PGN test corpus.
 *
 * <p>The registry is the single source of truth for "this PGN records halfmoves past a FIDE
 * automatic termination". The previous implementation matched filenames against a chain of
 * substring patterns, which silently stopped working when a file was renamed: tests that should
 * have skipped the file would replay it (or, conversely,
 * {@link TestPgnPlaysBeyondTerminationRejection} would stop exercising it). Hard-coding the
 * basenames removes that drift, and this validator surfaces any rename or deletion immediately
 * with a clear message naming the missing files.
 *
 * <p>If this test fails, the action is to update {@link PgnPlaysBeyondTermination#PGN_FILE_NAMES}
 * to reflect the corpus — either add the new name (rename), or remove the entry (deletion).
 * Do not silently work around the failure; the rejection test relies on the registry to know
 * which files to exercise.
 */
class TestPgnPlaysBeyondTerminationRegistry {

  @SuppressWarnings("static-method")
  @Test
  void registeredFilesAllExistOnDisk() throws IOException {
    final SortedSet<String> missing = PgnPlaysBeyondTermination
        .findMissingRegisteredFiles(PgnTestConstants.PGN_TEST_ROOT_FOLDER_PATH);

    assertTrue(missing.isEmpty(),
        "PgnPlaysBeyondTermination.PGN_FILE_NAMES contains "
            + missing.size()
            + " entry/entries that no longer exist in the PGN corpus at "
            + PgnTestConstants.PGN_TEST_ROOT_FOLDER_PATH
            + ". Update the registry (file was renamed or removed):\n  - "
            + String.join("\n  - ", missing));
  }
}
