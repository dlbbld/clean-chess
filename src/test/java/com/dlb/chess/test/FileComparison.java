package com.dlb.chess.test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.test.pgn.export.linebreaks.TestPgnExportLineBreaks;

public class FileComparison {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestPgnExportLineBreaks.class);

  // problem PGN files in Eclipse suddenly have windows line breaks instead of unix line breaks
  @SuppressWarnings("null")
  public static boolean checkWithLineEndingsConversion(Path pgnFileExpectedPath, Path pgnFileActualPath) {
    logger.info("Testing " + pgnFileExpectedPath + " against " + pgnFileActualPath);

    try {
      final var expectedAsIs = Files.readString(pgnFileExpectedPath, Charset.forName("UTF-8"));
      final String expectedConverted = convertWindowsToUnixLineEndings(expectedAsIs);
      final var actualAsIs = Files.readString(pgnFileActualPath, Charset.forName("UTF-8"));
      return expectedConverted.equals(actualAsIs);
    } catch (final IOException e) {
      logger.error("Error while comparing files: " + e.getMessage());
      return false;
    }
  }

  private static String convertWindowsToUnixLineEndings(String text) {
    return NonNullWrapperCommon.replace(text, "\r\n", "\n");
  }

  public static boolean check(Path pgnFileExpectedPath, Path pgnFileActualPath) {

    logger.info("Testing " + pgnFileExpectedPath + " against " + pgnFileActualPath);

    try {
      return determineIsFilesAreEqual(pgnFileExpectedPath, pgnFileActualPath);
    } catch (final IOException e) {
      logger.error("Error while comparing files: " + e.getMessage());
      return false;
    }
  }

  private static boolean determineIsFilesAreEqual(Path path1, Path path2) throws IOException {
    // Quick check: if sizes differ, no need to compare contents
    if (Files.size(path1) != Files.size(path2)) {
      return false;
    }
    // mismatch() returns -1 if there is no mismatch
    return Files.mismatch(path1, path2) == -1;
  }

}
