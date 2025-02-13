package com.dlb.chess.pgn.reader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.exceptions.ChessApiRuntimeException;
import com.dlb.chess.common.exceptions.FileSystemAccessException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.fen.FenParserAdvanced;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.reader.enums.PgnReaderStrictValidationProblem;
import com.dlb.chess.pgn.reader.exceptions.PgnReaderStrictValidationException;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.pgn.reader.model.Tag;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.utility.TagUtility;

public abstract class AbstractPgnReader {

  public static List<MoveSpecification> calculateMoveSpecificationList(PgnFile pgnFile) {
    final Fen startFen = pgnFile.startFen();

    final ApiBoard board = new Board(startFen);

    for (final PgnHalfMove pgnHalfMove : pgnFile.halfMoveList()) {
      board.performMove(pgnHalfMove.san());
    }
    return board.getPerformedMoveSpecificationList();
  }

  static List<String> readPgnFileLineList(Path pgnFolderPath, String pgnFileName)
      throws PgnReaderStrictValidationException {
    final Path filePath = FileUtility.calculateFilePath(pgnFolderPath, pgnFileName);
    return readPgnFileLineList(filePath);
  }

  static List<String> readPgnFileLineList(Path filePath) throws PgnReaderStrictValidationException {
    final List<String> fileLines;
    try {
      if (!FileUtility.exists(filePath)) {
        throw new PgnReaderStrictValidationException(PgnReaderStrictValidationProblem.FILE_NOT_FOUND,
            SanValidationProblem.NONE, "The file \"" + filePath + "\" was not found.");
      }
      fileLines = FileUtility.readFileLines(filePath);
    } catch (final FileNotFoundException ioe) {
      throw new FileSystemAccessException("Reading file \"" + filePath + "\" failed", ioe);

    } catch (final IOException ioe) {
      @SuppressWarnings("null") @NonNull final String message = ioe.getMessage();
      throw new ChessApiRuntimeException(message);
    }
    return fileLines;
  }

  private static String calculateStartFenStr(List<Tag> tagList, boolean isStartFromPosition) {
    if (!isStartFromPosition) {
      return FenConstants.FEN_INITIAL_STR;
    }
    return TagUtility.readFen(tagList);
  }

  static Fen calculateStartFen(List<Tag> tagList, boolean isStartFromPosition) {
    final String startFenStr = calculateStartFenStr(tagList, isStartFromPosition);
    return FenParserAdvanced.parseFenAdvanced(startFenStr);
  }

  static void removeFenIfInitial(List<Tag> tagList, Fen startFen) {
    if (startFen.equals(FenConstants.FEN_INITIAL)) {
      if (TagUtility.hasFen(tagList)) {
        TagUtility.removeFenTag(tagList);
      }
      if (TagUtility.hasSetUp(tagList)) {
        TagUtility.removeSetUpTag(tagList);
      }
    }
  }

}
