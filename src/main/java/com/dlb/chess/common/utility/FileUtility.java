package com.dlb.chess.common.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.FileSystemAccessException;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;

public class FileUtility {

  private FileUtility() {
  }

  /**
   * Reading a file linewise, without including linebreaks or adding spaces after a line break.
   */
  public static List<String> readFileLines(String folderPath, String fileName) throws IOException {
    return readFileLines(calculateFilePath(folderPath, fileName));
  }

  public static List<String> readFileLines(String filePath) throws IOException {
    final List<String> fileLines = new ArrayList<>();
    final File file = new File(filePath);
    if (!file.isFile()) {
      throw new IllegalArgumentException("\"" + filePath + "\" is not a file");
    }
    try (final Scanner myReader = new Scanner(file, StandardCharsets.ISO_8859_1);) {
      while (myReader.hasNextLine()) {
        final String currentLine = NonNullWrapperCommon.nextLine(myReader);
        fileLines.add(currentLine);
      }
    }

    return fileLines;
  }

  public static String calculateFilePath(String folderPath, String fileName) {
    if (StringUtils.endsWith(folderPath, "\\")) {
      throw new IllegalArgumentException(
          "A folder path cannot end with \"\\\", folder path \"" + folderPath + "\" was provided");
    }
    if (StringUtils.contains(folderPath, "/")) {
      throw new IllegalArgumentException(
          "A folder path cannot contain \"/\", folder path  \"" + folderPath + "\" was provided");
    }
    if (StringUtils.contains(fileName, "\\")) {
      throw new IllegalArgumentException(
          "A file name cannot contain \"\\\", file name \"" + fileName + "\" was provided");
    }
    if (StringUtils.contains(fileName, "/")) {
      throw new IllegalArgumentException(
          "A file name cannot contain \"/\", file name \"" + fileName + "\" was provided");
    }
    return folderPath + "\\" + fileName;
  }

  public static void writeFile(String folderPath, String fileName, List<String> lineList) {
    writeFile(calculateFilePath(folderPath, fileName), lineList);
  }

  public static void writeFile(String folderPath, String fileName, String line) {
    final List<String> lineList = new ArrayList<>();
    lineList.add(line);

    writeFile(calculateFilePath(folderPath, fileName), lineList);
  }

  public static void writeFile(String filePath, String line) {
    final List<String> lineList = new ArrayList<>();
    lineList.add(line);

    writeFile(filePath, lineList);
  }

  public static void writeFile(String filePath, List<String> lineList) {
    deleteFile(filePath);
    final var path = Paths.get(filePath);
    try (var writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
      for (final String line : lineList) {
        writer.write(line);
        writer.write(System.lineSeparator());
      }

    } catch (final IOException ex) {
      ex.printStackTrace();
    }
  }

  public static void appendFile(String filePath, String line) {

    final List<String> lineList = new ArrayList<>();
    lineList.add(line);

    appendFile(filePath, lineList);
  }

  public static void appendFile(String filePath, List<String> lineList) {

    if (!exists(filePath)) {
      writeFile(filePath, lineList);
    } else {

      final File file = new File(filePath);
      if (!file.isFile()) {
        throw new IllegalArgumentException("\"" + filePath + "\" is not a file");
      }
      try (Writer w = new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8.name());
          PrintWriter pw = new PrintWriter(w)) {
        for (final String line : lineList) {
          pw.println(line);
        }
      } catch (final IOException ioe) {
        throw new FileSystemAccessException("File appending to \"" + filePath + "\" failed", ioe);
      }
    }
  }

  public static void deleteIfExists(String folderPath, String fileName) {
    if (exists(folderPath, fileName)) {
      deleteFile(folderPath, fileName);
    }
  }

  public static void deleteIfExists(String path) {
    if (exists(path)) {
      deleteFile(path);
    }
  }

  public static boolean exists(String folderPath, String fileName) {
    return exists(calculateFilePath(folderPath, fileName));
  }

  public static boolean exists(String path) {
    return Files.exists(Paths.get(path));
  }

  public static void deleteFile(String folderPath, String fileName) {
    deleteFile(calculateFilePath(folderPath, fileName));
  }

  public static void deleteFile(String path) {
    try {
      Files.deleteIfExists(Paths.get(path));
    } catch (final IOException ioe) {
      throw new FileSystemAccessException("Deletion of file \"" + path + "\" failed", ioe);
    }
  }

  public static void deleteFilesInDirectory(String folderPath) {
    final File folder = new File(folderPath);
    if (!folder.isDirectory()) {
      throw new IllegalArgumentException("\"" + folderPath + "\" is not a directory");
    }

    final var filesList = folder.listFiles();
    if (filesList == null) {
      throw new FileSystemAccessException("File list retrieval for \"" + folderPath + "\" failed");
    }

    for (final File file : filesList) {
      if (file == null) {
        throw new ProgrammingMistakeException("Wrong assumption about API behaviour");
      }
      final var path = file.toPath();
      try {
        Files.delete(path);
      } catch (final NoSuchFileException nsfe) {
        throw new FileSystemAccessException(
            "The file " + file.getAbsolutePath() + " could not be deleted - no such file", nsfe);
      } catch (final DirectoryNotEmptyException dnee) {
        throw new FileSystemAccessException(
            "The file " + file.getAbsolutePath() + " could not be deleted - directory not empty", dnee);
      } catch (final IOException ioe) {
        throw new FileSystemAccessException(
            "The file " + file.getAbsolutePath() + " could not be deleted - permission issue", ioe);
      }
    }
  }

  public static List<String> readFileNameList(String folderPath) {
    final List<String> result = new ArrayList<>();
    final File folder = new File(folderPath);
    if (!folder.isDirectory()) {
      throw new IllegalArgumentException("\"" + folderPath + "\" is not a directory");
    }
    final var filesList = folder.listFiles();
    if (filesList == null) {
      throw new FileSystemAccessException("File list retrieval for \"" + folderPath + "\" failed");
    }

    for (final File file : filesList) {
      if (file == null) {
        throw new ProgrammingMistakeException("Wrong assumption about API behaviour");
      }
      result.add(NonNullWrapperCommon.getName(file));
    }
    return result;
  }

}
