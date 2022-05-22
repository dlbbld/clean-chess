package com.dlb.chess.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.pgn.reader.enums.ResultTagValue;
import com.dlb.chess.pgn.reader.enums.StandardTag;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.pgn.reader.model.Tag;
import com.google.common.collect.ImmutableList;

public class TagUtility {

  private static final String TAG_PATTERN = "\\[([A-Za-z0-9_]+) \"([^\"]*)\"\\]";

  @SuppressWarnings("null")
  public static final ImmutableList<StandardTag> SEVEN_TAG_ROSTER_TAG_LIST = NonNullWrapperCommon
      .copyOfList(Arrays.asList(StandardTag.EVENT, StandardTag.SITE, StandardTag.DATE, StandardTag.ROUND,
          StandardTag.WHITE, StandardTag.BLACK, StandardTag.RESULT));

  public static boolean hasEvent(List<Tag> tagList) {
    return existsTagName(tagList, StandardTag.EVENT);
  }

  public static String readEvent(List<Tag> tagList) {
    return readTagValue(tagList, StandardTag.EVENT);
  }

  public static boolean hasSite(List<Tag> tagList) {
    return existsTagName(tagList, StandardTag.SITE);
  }

  public static String readSite(List<Tag> tagList) {
    return readTagValue(tagList, StandardTag.SITE);
  }

  public static boolean hasDate(List<Tag> tagList) {
    return existsTagName(tagList, StandardTag.DATE);
  }

  public static String readDate(List<Tag> tagList) {
    return readTagValue(tagList, StandardTag.DATE);
  }

  public static boolean hasRound(List<Tag> tagList) {
    return existsTagName(tagList, StandardTag.ROUND);
  }

  public static String readRound(List<Tag> tagList) {
    return readTagValue(tagList, StandardTag.ROUND);
  }

  public static boolean hasWhite(List<Tag> tagList) {
    return existsTagName(tagList, StandardTag.WHITE);
  }

  public static String readWhite(List<Tag> tagList) {
    return readTagValue(tagList, StandardTag.WHITE);
  }

  public static boolean hasBlack(List<Tag> tagList) {
    return existsTagName(tagList, StandardTag.BLACK);
  }

  public static String readBlack(List<Tag> tagList) {
    return readTagValue(tagList, StandardTag.BLACK);
  }

  public static boolean hasResult(List<Tag> tagList) {
    return existsTagName(tagList, StandardTag.RESULT);
  }

  public static String readResult(List<Tag> tagList) {
    return readTagValue(tagList, StandardTag.RESULT);
  }

  public static boolean hasSetUp(List<Tag> tagList) {
    return existsTagName(tagList, StandardTag.SET_UP);
  }

  public static String readSetUp(List<Tag> tagList) {
    return readTagValue(tagList, StandardTag.SET_UP);
  }

  public static boolean hasFen(List<Tag> tagList) {
    return existsTagName(tagList, StandardTag.FEN);
  }

  public static String readFen(List<Tag> tagList) {
    return readTagValue(tagList, StandardTag.FEN);
  }

  public static ResultTagValue readResultTagValue(List<Tag> tagList) {
    return ResultTagValue.calculate(readResult(tagList));
  }

  public static ResultTagValue readResultTagValue(PgnFile pgnFile) {
    return readResultTagValue(pgnFile.tagList());
  }

  public static Tag calculateTag(String tagLine) {
    final var pattern = Pattern.compile(TAG_PATTERN);
    final var matcher = pattern.matcher(tagLine);
    // check all occurance
    if (matcher.matches()) {
      @SuppressWarnings("null") @NonNull final String tagName = matcher.group(1);
      @SuppressWarnings("null") @NonNull final String tagValue = matcher.group(2);
      return new Tag(tagName, tagValue);
    }
    throw new ProgrammingMistakeException("Must be validated to be a correct tag at this point");
  }

  private static String readTagValue(List<Tag> tagList, StandardTag tag) {
    return readTagValue(tagList, tag.getName());
  }

  public static String readTagValue(List<Tag> tagList, String tagName) {
    if (!existsTagName(tagList, tagName)) {
      throw new IllegalArgumentException(
          "The method can only be used if a tag with the tagName exists, check first with the provided method.");
    }
    for (final Tag tag : tagList) {
      if (tag.name().equals(tagName)) {
        return tag.value();
      }
    }
    throw new ProgrammingMistakeException();
  }

  public static boolean existsTag(List<Tag> tagList, StandardTag tag) {
    return existsTagName(tagList, tag.getName());
  }

  private static boolean existsTagName(List<Tag> tagList, StandardTag standardTag) {
    return existsTagName(tagList, standardTag.getName());
  }

  private static boolean existsTagName(List<Tag> tagList, String tagName) {
    for (final Tag tag : tagList) {
      if (tag.name().equals(tagName)) {
        return true;
      }
    }
    return false;
  }

  public static boolean calculateIsContainsAllSevenTagRosterTags(List<Tag> tagList) {
    for (final StandardTag standardTag : SEVEN_TAG_ROSTER_TAG_LIST) {
      if (!TagUtility.existsTag(tagList, standardTag)) {
        return false;
      }
    }
    return true;
  }

  public static String calculateSevenTagRosterDescription() {
    final List<String> list = new ArrayList<>();
    for (final StandardTag tag : SEVEN_TAG_ROSTER_TAG_LIST) {
      list.add(tag.getName());
    }
    return BasicUtility.calculateCommaSeparatedList(list);
  }

  public static void removeTag(List<Tag> tagList, StandardTag tag) {

    var indexFound = -1;
    var index = -1;
    for (final Tag tagCandidate : tagList) {
      index++;
      if (tagCandidate.name().equals(tag.getName())) {
        indexFound = index;
        break;
      }
    }
    if (indexFound != -1) {
      tagList.remove(indexFound);
    }
  }

  public static void removeSetUpTag(List<Tag> tagList) {
    removeTag(tagList, StandardTag.SET_UP);
  }

  public static void removeFenTag(List<Tag> tagList) {
    removeTag(tagList, StandardTag.FEN);
  }

  public static String calculateTagValue(PgnFile pgnFile, String tagName) {
    if (!existsTagName(pgnFile.tagList(), tagName)) {
      return "NA";
    }
    return readTagValue(pgnFile.tagList(), tagName);
  }

  public static String calculateTagValue(PgnFile pgnFile, StandardTag sevenTagRoster) {
    return calculateTagValue(pgnFile, sevenTagRoster.getName());
  }

}
