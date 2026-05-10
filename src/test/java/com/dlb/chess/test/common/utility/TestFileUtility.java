package com.dlb.chess.test.common.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ConfigurationConstants;
import com.dlb.chess.common.exceptions.FileSystemAccessException;
import com.dlb.chess.test.ConfigurationTestConstants;
import com.dlb.chess.test.FileComparison;

public class TestFileUtility {

  private static final Path TEST_FOLDER_PATH = NonNullWrapperCommon
      .pathResolve(ConfigurationTestConstants.PROJECT_ROOT_FOLDER_PATH, "src/test/resources/fileUtility/utf8");

  private static final String TEST_FILE_NAME = "utf8.txt";

  private static final Path TEST_SOURCE_FILE_PATH = NonNullWrapperCommon.pathResolve(TEST_FOLDER_PATH, TEST_FILE_NAME);

  private static final Path TEST_DESTINATION_FILE_PATH = NonNullWrapperCommon
      .pathResolve(ConfigurationConstants.TEMP_FOLDER_PATH, TEST_FILE_NAME);

  // UTF-8 test string with diverse characters
  private static final String TEST_CONTENT = """
      ASCII: Hello, World!
      German: ä, ö, ü, ß
      Emojis: 😀, 🐍, 🚀
      Chinese: 你好
      Hindi: नमस्ते
      Russian: Привет
      Special: €, ©, ™, ✓, ∞""";

  @SuppressWarnings("static-method")
  @Test
  void testReadUtf8File() {
    final List<String> expectedLines = NonNullWrapperCommon.asList(NonNullWrapperCommon.split(TEST_CONTENT, "\\n"));
    final List<String> actualLines = FileUtility.readFileLines(TEST_SOURCE_FILE_PATH);

    assertEquals(expectedLines, actualLines);
  }

  @SuppressWarnings("static-method")
  @Test
  void testWriteUtf8File() {
    final List<String> lines = NonNullWrapperCommon.asList(NonNullWrapperCommon.split(TEST_CONTENT, "\\n"));
    FileUtility.writeFile(TEST_DESTINATION_FILE_PATH, lines);
    assertTrue(FileComparison.checkWithLineEndingsConversion(TEST_SOURCE_FILE_PATH, TEST_DESTINATION_FILE_PATH));
  }

  @SuppressWarnings("static-method")
  @Test
  void testWriteFileThrowsWhenParentFolderDoesNotExist(@TempDir Path tempFolder) {
    final Path filePath = NonNullWrapperCommon.pathResolve(tempFolder, "missing/output.txt");

    @SuppressWarnings("null") final var exception = assertThrows(FileSystemAccessException.class,
        () -> FileUtility.writeFile(filePath, "text"));

    assertTrue(exception.getCause() instanceof IOException);
  }
}
