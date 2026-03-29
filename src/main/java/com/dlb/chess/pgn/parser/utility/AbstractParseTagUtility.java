package com.dlb.chess.pgn.parser.utility;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.BasicConstants;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.pgn.parser.exceptions.StrictPgnParserValidationException;
import com.dlb.chess.pgn.parser.model.FirstDuplicateTag;
import com.dlb.chess.pgn.parser.model.Tag;
import com.dlb.chess.pgn.parser.model.TagCandidate;

public abstract class AbstractParseTagUtility {

  static final String LEFT_SQUARE_BRACKET = "[";

  static final String RIGHT_SQUARE_BRACKET = "]";

  static final String TAG_NAME_PATTERN = "[A-Za-z0-9]{1}[A-Za-z0-9_+#=:-]*";

  // returning intermediary type so calling functions can validate if needed
  // we do not want to create the tag before it's considered valid by the validation
  static TagCandidate calculateTagCandidate(String tagLine, Pattern pattern) {
    final var matcher = pattern.matcher(tagLine);
    // check all occurance
    if (matcher.matches()) {
      @SuppressWarnings("null") @NonNull final String tagName = matcher.group(1);
      @SuppressWarnings("null") @NonNull final String tagValue = matcher.group(2);
      return new TagCandidate(tagName, tagValue);
    }
    throw new ProgrammingMistakeException("Must be validated to be a correct tag at this point");
  }

  static FirstDuplicateTag calculateFirstDuplicateTag(List<Tag> tagList) throws StrictPgnParserValidationException {
    final Map<String, Integer> countTagNames = new TreeMap<>();
    for (final Tag tag : tagList) {
      final String tagName = tag.name();
      if (!countTagNames.containsKey(tagName)) {
        countTagNames.put(tagName, 1);
      } else {
        @SuppressWarnings("null") final var count = countTagNames.get(tagName);
        final int existingCount = count;
        final var newCount = existingCount + 1;
        countTagNames.put(tagName, newCount);
      }
    }
    for (final Entry<String, Integer> entry : NonNullWrapperCommon.entrySet(countTagNames)) {
      @SuppressWarnings("null") final var value = entry.getValue();
      if (value > 1) {
        return new FirstDuplicateTag(true, NonNullWrapperCommon.getKey(entry));
      }
    }
    return new FirstDuplicateTag(false, BasicConstants.BLANK);
  }

}
