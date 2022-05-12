package com.dlb.chess.analysis.print;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.model.ClaimAhead;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.common.utility.HalfMoveUtility;
import com.dlb.chess.model.LegalMove;

public class ThreefoldClaimAheadPrint {

  public static List<String> calculateClaimAheadList(List<List<ClaimAhead>> claimAheadListList) {

    final List<String> result = new ArrayList<>();

    for (final List<ClaimAhead> claimAheadList : claimAheadListList) {
      final StringBuilder line = new StringBuilder();

      // we designed the list to be not empty
      final ClaimAhead claimAheadFirst = NonNullWrapperCommon.getFirst(claimAheadList);
      final LegalMove legalMoveFirst = claimAheadFirst.legalMove();
      final String fullMoveNumber = HalfMoveUtility.calculateFullMoveNumberInitialWithSpace(
          claimAheadFirst.fullMoveNumber(), legalMoveFirst.moveSpecification().havingMove());
      line.append(fullMoveNumber);

      final List<String> claimAheadSanList = new ArrayList<>();
      for (final ClaimAhead claimAhead : claimAheadList) {
        claimAheadSanList.add(claimAhead.san());
      }

      final String sanList = BasicUtility.calculateCommaSeparatedList(claimAheadSanList);
      line.append(sanList);

      result.add(NonNullWrapperCommon.toString(line));
    }

    return result;
  }
}
