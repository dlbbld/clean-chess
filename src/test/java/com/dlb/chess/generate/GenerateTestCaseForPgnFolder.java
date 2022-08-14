package com.dlb.chess.generate;

import java.io.File;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.exceptions.FileSystemAccessException;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.utility.PgnExtensionUtility;
import com.dlb.chess.test.pgntest.enums.PgnTest;

public class GenerateTestCaseForPgnFolder extends AbstractGenerateTestCaseForPgn {

  // the folder can only contain PGN files
  private static final String PGN_FOLDER_PATH = PgnTest.REPETITION_QUIZ_TWO.getFolderPath();

  public static void main(String[] args) throws Exception {
    generateTestCaseForFolder(PGN_FOLDER_PATH);
  }

  private static void generateTestCaseForFolder(String pgnFolderPath) throws Exception {
    final File folder = new File(pgnFolderPath);
    if (!folder.isDirectory()) {
      throw new IllegalArgumentException("\"" + pgnFolderPath + "\" is not a directory");
    }

    final var filesList = folder.listFiles();
    if (filesList == null) {
      throw new FileSystemAccessException("File list retrieval for \"" + pgnFolderPath + "\" failed");
    }

    for (final File file : filesList) {
      if (file == null) {
        throw new ProgrammingMistakeException("Wrong assumption about API behaviour");
      }
      final String pgnFileName = NonNullWrapperCommon.getName(file);
      if (!PgnExtensionUtility.hasPgnFileExtension(pgnFileName)) {
        throw new IllegalArgumentException(
            "All files in the folder must be valid PGN files and have the extension \"" + ChessConstants.PGN_EXTENSION
                + "\". The file \"" + pgnFileName + " does not meet the extension expectation");
      }
      final String testCaseValues = generate(pgnFolderPath, pgnFileName);
      System.out.println(testCaseValues);
    }
  }

}
