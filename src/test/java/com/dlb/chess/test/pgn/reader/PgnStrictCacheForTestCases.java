package com.dlb.chess.test.pgn.reader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.dlb.chess.pgn.reader.PgnReaderStrict;
import com.dlb.chess.pgn.reader.model.PgnFile;

public class PgnStrictCacheForTestCases {
  private static final Map<String, PgnFile> PGN_CACHE = new HashMap<>();

  public static PgnFile getPgn(Path pgnFolderPath, String pgnFileName) {
    // Validate the folder path
    if (!Files.isDirectory(pgnFolderPath)) {
      throw new IllegalArgumentException("Invalid folder path: " + pgnFolderPath);
    }

    // Construct the full path to the PGN file
    final var pgnFilePath = pgnFolderPath.resolve(pgnFileName);

    // Convert to a canonical String for cache lookups
    final var canonicalPath = pgnFilePath.toAbsolutePath().toString();

    // Check the cache
    if (PGN_CACHE.containsKey(canonicalPath)) {
      return PGN_CACHE.get(canonicalPath);
    }

    // Not in cache; read it from disk and store
    // (If PgnReaderStrict is still using strings, convert back)
    final PgnFile pgnFile = PgnReaderStrict.readPgn(pgnFolderPath, pgnFileName // file name
    );
    PGN_CACHE.put(canonicalPath, pgnFile);

    return pgnFile;
  }

}
