package com.dlb.chess.common.utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;

public abstract class BasicUtility {

  private static final String COMMA_SEPARATOR_LIST = ", ";

  private static final String SEMICOLON_SEPARATOR_LIST = "; ";

  private static final String SPACE_SEPARATOR_LIST = " ";

  private static final String DATE_PATTERN = "yyyy.MM.dd";

  private BasicUtility() {
  }

  public static String readProjectFolderPath() {
    final String projectRootFolderPath = System.getProperty("user.dir");
    if (projectRootFolderPath == null) {
      throw new RuntimeException("Project root folder path is not set as property");
    }
    return projectRootFolderPath;
  }

  public static void removeLastChar(StringBuilder stringBuilder) {
    final var length = stringBuilder.length();
    if (length > 0) {
      final var lastIndex = length - 1;
      stringBuilder.deleteCharAt(lastIndex);
    }
  }

  public static String calculateCommaSeparatedList(Set<String> set) {
    return calculateCommaSeparatedList(new ArrayList<>(set));
  }

  public static String calculateCommaSeparatedList(List<String> list) {
    return NonNullWrapperCommon.join(COMMA_SEPARATOR_LIST, list);
  }

  public static String calculateSemicolonSeparatedList(List<String> list) {
    return NonNullWrapperCommon.join(SEMICOLON_SEPARATOR_LIST, list);
  }

  public static String calculateSpaceSeparatedList(List<String> list) {
    return NonNullWrapperCommon.join(SPACE_SEPARATOR_LIST, list);
  }

  public static int calculateLineCountContainingString(List<String> lineList, String searchString) {
    var count = 0;
    for (final String line : lineList) {
      if (line.contains(searchString)) {
        count++;
      }
    }
    return count;
  }

  public static String formatMilliseconds(long totalMillisecond) {
    final var hours = TimeUnit.MILLISECONDS.toHours(totalMillisecond);
    final var totalMillisecondRemainingMinusHours = totalMillisecond - TimeUnit.HOURS.toMillis(hours);

    final var minutes = TimeUnit.MILLISECONDS.toMinutes(totalMillisecondRemainingMinusHours);
    final var totalMillisecondRemainingMinusHoursMinutes = totalMillisecondRemainingMinusHours
        - TimeUnit.MINUTES.toMillis(minutes);

    final var seconds = TimeUnit.MILLISECONDS.toSeconds(totalMillisecondRemainingMinusHoursMinutes);

    return NonNullWrapperCommon.format("%02d:%02d:%02d", hours, minutes, seconds);
  }

  // for "1. a4", "1. a4 a5", "1. a4 a5 2. e3", "1. a4 a5 2. e3 e6 3. b4 Nfg" etc.
  public static List<String> splitStringRetainingDelimiter(String string, String regExp) {
    final List<String> result = new ArrayList<>();

    final var pattern = Pattern.compile(regExp);
    final var matcher = pattern.matcher(string);
    final List<Integer> startIndexList = new ArrayList<>();
    while (matcher.find()) {
      startIndexList.add(matcher.start());
    }

    // no matches
    if (startIndexList.isEmpty()) {
      result.add(string);
      return result;
    }

    int startIndexCurrentMatch = NonNullWrapperCommon.getFirst(startIndexList);
    if (startIndexCurrentMatch > 0) {
      result.add(NonNullWrapperCommon.substring(string, 0, startIndexCurrentMatch));
    }
    int endIndexCurrentMatch;
    for (var i = 0; i < startIndexList.size() - 1; i++) {
      startIndexCurrentMatch = NonNullWrapperCommon.get(startIndexList, i);
      final int regionStartIndexNextMatch = NonNullWrapperCommon.get(startIndexList, i + 1);
      endIndexCurrentMatch = regionStartIndexNextMatch - 1;
      result.add(NonNullWrapperCommon.substring(string, startIndexCurrentMatch, endIndexCurrentMatch + 1));
    }
    startIndexCurrentMatch = NonNullWrapperCommon.getLast(startIndexList);
    endIndexCurrentMatch = string.length() - 1;
    result.add(NonNullWrapperCommon.substring(string, startIndexCurrentMatch, endIndexCurrentMatch + 1));

    // the split string sequence must composed by exactly the original string
    if (string.length() != calculateTotalLength(result)) {
      throw new ProgrammingMistakeException("The split string method contains a programming mistake");
    }

    return result;
  }

  private static int calculateTotalLength(List<String> stringList) {
    var counter = 0;
    for (final String string : stringList) {
      counter += string.length();
    }
    return counter;
  }

  public static List<String> calculateWrappedLines(String line, int lineLength) {
    final List<String> result = new ArrayList<>();
    final var blockArray = line.split(" ");

    if (blockArray.length == 0) {
      throw new ProgrammingMistakeException("As the passed text cannot be null the array cannot be empty");
    }
    final var firstBlock = blockArray[0];
    StringBuilder wrappedLine = new StringBuilder();
    // we add the first block unconditionally without leading space
    // if checking and then first adding line as for other elements that would produce an empty line
    wrappedLine.append(firstBlock);

    // we add the remaining blocks with a leading space
    if (blockArray.length >= 2) {
      for (var i = 1; i < blockArray.length; i++) {
        // +1 for the space we also need to append
        final var block = blockArray[i];
        if (wrappedLine.length() + 1 + block.length() <= lineLength) {
          wrappedLine.append(" " + block);
        } else {
          result.add(NonNullWrapperCommon.toString(wrappedLine));
          wrappedLine = new StringBuilder();
          wrappedLine.append(block);
        }
      }
    }
    result.add(NonNullWrapperCommon.toString(wrappedLine));
    return result;
  }

  @SuppressWarnings("null")
  public static String calculateTodayDate() {
    // SimpleDateFormat should not be static according to SonarLint reason gives is threading
    return new SimpleDateFormat(DATE_PATTERN).format(new Date());
  }

  public static <E> E getFirstElement(Set<E> set) {
    if (set.isEmpty()) {
      throw new IllegalArgumentException();
    }
    return NonNullWrapperCommon.getFirst(new ArrayList<>(set));
  }

  public static boolean isInt(String string) {
    try {
      Integer.parseInt(string);
      return true;
    } catch (@SuppressWarnings("unused") final NumberFormatException e) {
      return false;
    }
  }

  public static int parseInt(String string) {
    if (!isInt(string)) {
      throw new IllegalArgumentException();
    }
    return Integer.parseInt(string);
  }

  public static <E> boolean calculateIsDisjoint(Set<E> firstSet, Set<E> secondSet) {
    for (final E elementFirstSet : firstSet) {
      if (secondSet.contains(elementFirstSet)) {
        return false;
      }
    }
    for (final E elementSecondSet : secondSet) {
      if (firstSet.contains(elementSecondSet)) {
        return false;
      }
    }
    return true;
  }

  public static String convertToString(List<String> list) {
    final StringBuilder result = new StringBuilder();
    for (final String line : list) {
      result.append(line).append("\n");
    }
    return NonNullWrapperCommon.toString(result);

  }
}
