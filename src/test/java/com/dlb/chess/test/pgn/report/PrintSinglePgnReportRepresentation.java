package com.dlb.chess.test.pgn.report;

import java.util.List;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.report.Reporter;
import com.dlb.chess.test.pgnall.AbstractPgnTest;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.report.representation.BasicRepresentation;

public class PrintSinglePgnReportRepresentation extends AbstractPgnTest {

  private static final String PGN_FILE_NAME = "various_gundavaa_tari_2022.pgn";

  private static final Logger logger = NonNullWrapperCommon.getLogger(PrintSinglePgnReportRepresentation.class);

  public static void main(String[] args) throws Exception {

    final PgnTest pgnTest = PgnExpectedValue.findPgnTestPgnNotListed(PGN_FILE_NAME);
    final var report = Reporter.calculateReport(pgnTest.getFolderPath(), PGN_FILE_NAME);
    final List<String> representation = BasicRepresentation.calculateRepresentation(report, PGN_FILE_NAME);

    GeneralUtility.logLines(logger, representation);

  }

}
