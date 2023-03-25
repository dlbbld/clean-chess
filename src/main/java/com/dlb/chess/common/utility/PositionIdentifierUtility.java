package com.dlb.chess.common.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dlb.chess.common.NonNullWrapperCommon;

public abstract class PositionIdentifierUtility {

  private static final int BASE = 26;
  private static final int ASCII_TABLE_BEFORE_UPPER_CASE_A_NUMBER = 64;

  public static String calculateIdentifier(int positionNumber) {

    final List<Integer> representationList = calculateRepresentation(positionNumber - 1, BASE);

    final StringBuilder result = new StringBuilder();
    for (var i = 0; i < representationList.size(); i++) {
      final int representation = NonNullWrapperCommon.get(representationList, i);
      final int representationAdaptedForLastDigit;
      if (i == representationList.size() - 1) {
        representationAdaptedForLastDigit = representation + 1;
      } else {
        representationAdaptedForLastDigit = representation;
      }
      final var letter = (char) (ASCII_TABLE_BEFORE_UPPER_CASE_A_NUMBER + representationAdaptedForLastDigit);
      result.append(letter);
    }
    return NonNullWrapperCommon.toString(result);
  }

  public static List<Integer> calculateRepresentation(int number, int base) {
    final List<Integer> result = new ArrayList<>();

    var workingNumber = number;
    var multiplies = (int) Math.floor(workingNumber / base);
    var remainder = workingNumber % base;
    result.add(remainder);
    while (multiplies > 0) {
      workingNumber = (workingNumber - remainder) / base;
      multiplies = (int) Math.floor(workingNumber / base);
      remainder = workingNumber % base;
      result.add(remainder);
    }

    Collections.reverse(result);

    return result;
  }
}
