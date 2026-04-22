package com.dlb.chess.pgn.parser.sequential;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.FileSystemAccessException;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.common.utility.HalfMoveUtility;
import com.dlb.chess.enums.MoveSuffixAnnotation;
import com.dlb.chess.fen.FenParserAdvanced;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.parser.enums.LenientPgnParserValidationProblem;
import com.dlb.chess.pgn.parser.enums.ResultTagValue;
import com.dlb.chess.pgn.parser.enums.SetUpTagValue;
import com.dlb.chess.pgn.parser.enums.StandardTag;
import com.dlb.chess.pgn.parser.exceptions.LenientPgnParserValidationException;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.pgn.parser.model.Tag;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.utility.TagPlaceHolderUtility;
import com.dlb.chess.utility.TagUtility;

/**
 * Sequential lenient PGN parser. Same pull-based token consumption as the strict sequential parser, but with
 * permissive inter-token rules: any amount of whitespace is treated as a token separator, move number indicators
 * are consumed and ignored, the game termination marker is optional, space before a move suffix annotation or a
 * check/mate glyph is tolerated, and missing seven-tag-roster entries are patched in with placeholders rather than
 * rejected.
 *
 * <p>Independent pipeline — does not share validation logic with {@link SequentialStrictPgnParser}. The tokenizer
 * primitive is shared.
 */
public final class SequentialLenientPgnParser {

  private static final int SAN_MIN_LENGTH = 2;
  private static final int SAN_MAX_LENGTH = 7;

  private final String source;
  private final PgnTokenizer tokenizer;

  private SequentialLenientPgnParser(String source) {
    this.source = stripUtf8Bom(source);
    this.tokenizer = new PgnTokenizer(new PgnCharStream(this.source));
  }

  // -------------------------------------------------------------------------------------------------
  // Public entry points
  // -------------------------------------------------------------------------------------------------

  public static PgnFile parseText(String pgn) {
    return new SequentialLenientPgnParser(pgn).parseInternal();
  }

  public static PgnFile parse(Path pgnFilePath) {
    final String source;
    try {
      source = Files.readString(pgnFilePath, StandardCharsets.UTF_8);
    } catch (final IOException e) {
      throw new FileSystemAccessException("Reading file \"" + pgnFilePath + "\" failed.", e);
    }
    return parseText(source);
  }

  public static PgnFile parse(Path pgnFolderPath, String pgnFileName) {
    return parse(FileUtility.calculateFilePath(pgnFolderPath, pgnFileName));
  }

  public static PgnFile parse(String pgnFilePath) {
    return parse(NonNullWrapperCommon.get(pgnFilePath));
  }

  private static String stripUtf8Bom(String source) {
    if (!source.isEmpty() && source.charAt(0) == '\uFEFF') {
      return NonNullWrapperCommon.substring(source, 1);
    }
    return source;
  }

  // -------------------------------------------------------------------------------------------------
  // Top-level parsing
  // -------------------------------------------------------------------------------------------------

  private PgnFile parseInternal() {
    skipInsignificantWhitespace();

    final List<Tag> tagList = parseTagSection();
    validateUniqueTagNames(tagList);
    validateResultTagValueIfPresent(tagList);

    skipInsignificantWhitespace();
    final MovetextOutcome movetext = parseMovetext();

    // After a valid movetext, no further tag-like content may appear. The existing lenient parser walks the whole
    // file to detect this up front; here we catch it by inspecting whatever is left after the termination marker.
    skipInsignificantWhitespace();
    final PgnToken trailingPeek = tokenizer.peek();
    if (trailingPeek.type() == PgnTokenType.TAG_BRACKET_OPEN
        || currentLineContainsTagBracket(trailingPeek.line())) {
      if (trailingPeek.type() != PgnTokenType.EOF) {
        throw tagReappearError();
      }
    }

    final ResultTagValue resultTagValue = reconcileResult(tagList, movetext.terminationResult());
    fixTagListForResultIfRequired(tagList, resultTagValue);

    validateTagSetUpValue(tagList);
    // Lenient policy — derive setup-from-position strictly from the presence of the FEN tag. A SetUp tag with
    // value "1" but no FEN is logically inconsistent; the existing lenient pipeline resolves that by ignoring
    // the stale SetUp and starting from the initial position, then the fix-up below removes the stale tag.
    final boolean isStartFromPosition = TagUtility.hasFen(tagList);
    fixTagListForSetUpIfRequired(tagList, isStartFromPosition);

    final Fen startFen = calculateStartFen(tagList, isStartFromPosition);

    validateBoardPerLastMove(startFen, movetext.halfMoveList());

    fixTagListForMissingSevenTagRosterTags(tagList);

    removeFenIfInitial(tagList, startFen);
    Collections.sort(tagList);

    return new PgnFile(tagList, startFen, movetext.leadingCommentary(), movetext.halfMoveList());
  }

  // -------------------------------------------------------------------------------------------------
  // Tag section
  // -------------------------------------------------------------------------------------------------

  private List<Tag> parseTagSection() {
    final List<Tag> tags = new ArrayList<>();
    while (true) {
      skipInsignificantWhitespace();
      final PgnToken peek = tokenizer.peek();
      if (peek.type() == PgnTokenType.TAG_BRACKET_OPEN) {
        tags.add(parseTag());
        continue;
      }
      // Lenient rule: any line that still contains [ or ] is treated as a tag-line candidate. If the line could not
      // be parsed as a tag (which is the case here, since the peek is not a bracket open), report TAG_FORMAT_INVALID
      // rather than silently starting movetext parsing with malformed tag content.
      if (currentLineContainsTagBracket(peek.line())) {
        throw tagFormatError("A tag line with an invalid format was found on line " + peek.line() + ".");
      }
      return tags;
    }
  }

  /**
   * Returns true if the source line numbered {@code lineNumber} (1-based) contains either {@code [} or {@code ]}.
   * Mirrors the existing lenient parser's {@code isConsideredTagLine} check at the sequential layer.
   */
  private boolean currentLineContainsTagBracket(int lineNumber) {
    int index = 0;
    int currentLine = 1;
    while (currentLine < lineNumber && index < source.length()) {
      final char c = source.charAt(index);
      if (c == '\r') {
        currentLine++;
        if (index + 1 < source.length() && source.charAt(index + 1) == '\n') {
          index++;
        }
      } else if (c == '\n') {
        currentLine++;
      }
      index++;
    }
    while (index < source.length()) {
      final char c = source.charAt(index);
      if (c == '\n' || c == '\r') {
        break;
      }
      if (c == '[' || c == ']') {
        return true;
      }
      index++;
    }
    return false;
  }

  private Tag parseTag() {
    final PgnToken open = tokenizer.next();
    if (open.type() != PgnTokenType.TAG_BRACKET_OPEN) {
      throw tagFormatError(
          "Tag opening bracket [ expected but found \"" + open.text() + "\".");
    }
    skipInlineWhitespace();

    final PgnToken nameToken = tokenizer.next();
    if (nameToken.type() != PgnTokenType.SYMBOL) {
      throw tagFormatError("The first character in the tag name must be one of A-Z, a-z or 0-9.");
    }
    validateTagNameCharacters(nameToken.text());
    skipInlineWhitespace();

    final PgnToken valueToken = tokenizer.next();
    if (valueToken.type() != PgnTokenType.TAG_VALUE_STRING) {
      throw tagFormatError("A tag value enclosed in double quotes was expected.");
    }
    skipInlineWhitespace();

    final PgnToken close = tokenizer.next();
    if (close.type() != PgnTokenType.TAG_BRACKET_CLOSE) {
      throw tagFormatError("The tag line must end with the right square bracket ].");
    }

    return new Tag(nameToken.text(), valueToken.text());
  }

  private static void validateTagNameCharacters(String name) {
    if (name.isEmpty()) {
      throw tagFormatError("Tag name must not be empty.");
    }
    for (int i = 0; i < name.length(); i++) {
      final char c = name.charAt(i);
      if (!isAsciiLetterOrDigit(c) && c != '_' && c != '+' && c != '#' && c != '=' && c != ':' && c != '-') {
        throw tagFormatError("The tag name contains an invalid character \"" + c + "\".");
      }
    }
  }

  private static void validateUniqueTagNames(List<Tag> tagList) {
    for (int i = 0; i < tagList.size(); i++) {
      for (int j = i + 1; j < tagList.size(); j++) {
        if (tagList.get(i).name().equals(tagList.get(j).name())) {
          throw new LenientPgnParserValidationException(LenientPgnParserValidationProblem.TAG_NAME_NOT_UNIQUE,
              SanValidationProblem.NONE,
              "The tag name must be unique. The tag name \"" + tagList.get(i).name() + "\" was used more than once.");
        }
      }
    }
  }

  private static void validateResultTagValueIfPresent(List<Tag> tagList) {
    if (!TagUtility.hasResult(tagList)) {
      return;
    }
    final String value = TagUtility.readResult(tagList);
    if (!ResultTagValue.exists(value)) {
      throw new LenientPgnParserValidationException(LenientPgnParserValidationProblem.TAG_RESULT_VALUE_INVALID,
          SanValidationProblem.NONE, "The " + StandardTag.RESULT.getName() + " tag value must exactly match one \""
              + ResultTagValue.calculateList() + "\".");
    }
  }

  private static void validateTagSetUpValue(List<Tag> tagList) {
    if (!TagUtility.hasSetUp(tagList)) {
      return;
    }
    final String value = TagUtility.readSetUp(tagList);
    if (!SetUpTagValue.exists(value)) {
      throw new LenientPgnParserValidationException(LenientPgnParserValidationProblem.TAG_SET_UP_VALUE_INVALID,
          SanValidationProblem.NONE, "The " + StandardTag.SET_UP.getName() + " tag value must exactly match one \""
              + SetUpTagValue.calculateList() + "\".");
    }
    final SetUpTagValue setUpTagValue = SetUpTagValue.calculate(value);
    if (setUpTagValue == SetUpTagValue.START_FROM_INITIAL_POSITION && TagUtility.hasFen(tagList)) {
      throw new LenientPgnParserValidationException(
          LenientPgnParserValidationProblem.TAG_SET_UP_VALUE_ZERO_BUT_FEN_PROVIDED, SanValidationProblem.NONE,
          "When the " + StandardTag.SET_UP.getName() + " tag is set to "
              + SetUpTagValue.START_FROM_INITIAL_POSITION.getValue() + ", then no " + StandardTag.FEN.getName()
              + " tag can be provided.");
    }
  }

  // -------------------------------------------------------------------------------------------------
  // Movetext section
  // -------------------------------------------------------------------------------------------------

  private record MovetextOutcome(List<PgnHalfMove> halfMoveList, String leadingCommentary,
      ResultTagValue terminationResult) {
  }

  private MovetextOutcome parseMovetext() {
    String leadingCommentary = "";
    final List<PgnHalfMove> halfMoves = new ArrayList<>();
    ResultTagValue terminationResult = null;

    skipInsignificantWhitespace();
    if (isCommentToken(tokenizer.peek().type())) {
      leadingCommentary = readCommentText();
    }

    while (true) {
      skipInsignificantWhitespace();
      final PgnToken peek = tokenizer.peek();
      final PgnTokenType type = peek.type();

      if (type == PgnTokenType.EOF) {
        break;
      }
      if (type == PgnTokenType.TAG_BRACKET_OPEN) {
        throw tagReappearError();
      }
      if (type == PgnTokenType.TERMINATION_MARKER) {
        tokenizer.next();
        terminationResult = ResultTagValue.calculate(peek.text());
        break;
      }
      if (type == PgnTokenType.MOVE_NUMBER_WHITE || type == PgnTokenType.MOVE_NUMBER_BLACK) {
        tokenizer.next();
        continue;
      }
      if (isCommentToken(type)) {
        final String commentary = readCommentText();
        if (halfMoves.isEmpty()) {
          // Stray comment with no preceding half-move — append to the leading commentary so nothing is lost.
          leadingCommentary = leadingCommentary.isEmpty() ? commentary : leadingCommentary + " " + commentary;
        } else {
          final int last = halfMoves.size() - 1;
          final PgnHalfMove previous = halfMoves.get(last);
          final String combined = previous.commentary().isEmpty() ? commentary
              : previous.commentary() + " " + commentary;
          halfMoves.set(last, new PgnHalfMove(previous.san(), previous.moveSuffixAnnotation(), combined));
        }
        continue;
      }
      if (type == PgnTokenType.MOVE_SUFFIX_ANNOTATION) {
        if (halfMoves.isEmpty()) {
          tokenizer.next();
          continue;
        }
        final PgnToken suffixToken = tokenizer.next();
        final MoveSuffixAnnotation suffix = parseMoveSuffix(suffixToken.text());
        final int last = halfMoves.size() - 1;
        final PgnHalfMove previous = halfMoves.get(last);
        halfMoves.set(last, new PgnHalfMove(previous.san(), suffix, previous.commentary()));
        continue;
      }
      if (type == PgnTokenType.SYMBOL) {
        halfMoves.add(parseHalfMoveLenient());
        continue;
      }
      // Anything else at movetext position is an unexpected format.
      throw new LenientPgnParserValidationException(LenientPgnParserValidationProblem.EXCEPTION_CAUGHT_FROM_STRICT_VALIDATION,
          SanValidationProblem.NONE, "Unexpected token \"" + peek.text() + "\" at line " + peek.line() + ".");
    }

    return new MovetextOutcome(halfMoves, leadingCommentary, terminationResult);
  }

  private PgnHalfMove parseHalfMoveLenient() {
    final PgnToken sanToken = tokenizer.next();
    final StringBuilder sanBuilder = new StringBuilder(sanToken.text());

    // Lenient quirk: a lone + or # appearing after whitespace belongs to the preceding SAN. The tokenizer emits
    // it as its own SYMBOL because the whitespace split it off from the main SAN body.
    skipInlineWhitespace();
    final PgnToken peekAfterSpace = tokenizer.peek();
    if (peekAfterSpace.type() == PgnTokenType.SYMBOL && isBareCheckOrMate(peekAfterSpace.text())) {
      sanBuilder.append(peekAfterSpace.text());
      tokenizer.next();
    }

    final String san = NonNullWrapperCommon.toString(sanBuilder);
    validateSanCharacters(san);
    validateSanLength(san);

    MoveSuffixAnnotation suffix = MoveSuffixAnnotation.NONE;
    // Allow whitespace between SAN and suffix annotation (`e4 !!`).
    skipInlineWhitespace();
    if (tokenizer.peek().type() == PgnTokenType.MOVE_SUFFIX_ANNOTATION) {
      suffix = parseMoveSuffix(tokenizer.next().text());
    }

    return new PgnHalfMove(san, suffix, "");
  }

  private static boolean isBareCheckOrMate(String text) {
    return "+".equals(text) || "#".equals(text);
  }

  private String readCommentText() {
    final PgnToken token = tokenizer.next();
    // In lenient mode an unclosed brace still yields the captured text; we do not reject the file over it.
    return token.text();
  }

  private static boolean isCommentToken(PgnTokenType type) {
    return type == PgnTokenType.BRACE_COMMENT || type == PgnTokenType.BRACE_COMMENT_UNCLOSED;
  }

  private static MoveSuffixAnnotation parseMoveSuffix(String text) {
    if (!MoveSuffixAnnotation.exists(text)) {
      // Fall back to NONE rather than failing — lenient parsing should not reject unusual suffixes.
      return MoveSuffixAnnotation.NONE;
    }
    return MoveSuffixAnnotation.calculate(text);
  }

  private static void validateSanCharacters(String san) {
    for (int i = 0; i < san.length(); i++) {
      final char c = san.charAt(i);
      if (!com.dlb.chess.board.enums.Piece.exists(c) && !com.dlb.chess.board.enums.File.exists(c)
          && !com.dlb.chess.board.enums.Rank.exists(c) && !com.dlb.chess.san.enums.SanSymbol.exists(c)) {
        throw new LenientPgnParserValidationException(
            LenientPgnParserValidationProblem.EXCEPTION_CAUGHT_FROM_STRICT_VALIDATION, SanValidationProblem.NONE,
            "The movetext is invalid because a SAN contains an invalid character of \"" + c + "\".");
      }
    }
  }

  private static void validateSanLength(String san) {
    if (san.length() < SAN_MIN_LENGTH || san.length() > SAN_MAX_LENGTH) {
      throw new LenientPgnParserValidationException(
          LenientPgnParserValidationProblem.EXCEPTION_CAUGHT_FROM_STRICT_VALIDATION, SanValidationProblem.NONE,
          "The movetext contains the SAN '" + san + "' with an invalid SAN length.");
    }
  }

  // -------------------------------------------------------------------------------------------------
  // Tag fix-ups
  // -------------------------------------------------------------------------------------------------

  private static ResultTagValue reconcileResult(List<Tag> tagList, ResultTagValue terminationResult) {
    if (TagUtility.hasResult(tagList)) {
      final ResultTagValue fromTag = ResultTagValue.calculate(TagUtility.readResult(tagList));
      if (terminationResult != null && terminationResult != fromTag) {
        throw new LenientPgnParserValidationException(
            LenientPgnParserValidationProblem.TAG_RESULT_BOTH_SET_BUT_DIFFERENT, SanValidationProblem.NONE,
            "The result in the result tag and in the movetext must be the same. The result \"" + fromTag.getValue()
                + "\" was specified in the result tag, the result \"" + terminationResult.getValue()
                + "\" was specified in the movetext");
      }
      return fromTag;
    }
    return terminationResult != null ? terminationResult : ResultTagValue.ONGOING;
  }

  private static void fixTagListForResultIfRequired(List<Tag> tagList, ResultTagValue resultTagValue) {
    if (!TagUtility.hasResult(tagList)) {
      tagList.add(new Tag(StandardTag.RESULT.getName(), resultTagValue.getValue()));
    }
  }

  private static void fixTagListForSetUpIfRequired(List<Tag> tagList, boolean isStartFromPosition) {
    if (isStartFromPosition) {
      if (!TagUtility.hasSetUp(tagList)) {
        tagList.add(new Tag(StandardTag.SET_UP.getName(), SetUpTagValue.START_FROM_SETUP_POSITION.getValue()));
      }
    } else if (TagUtility.hasSetUp(tagList)) {
      TagUtility.removeTag(tagList, StandardTag.SET_UP);
    }
  }

  private static void fixTagListForMissingSevenTagRosterTags(List<Tag> tagList) {
    for (final StandardTag standardTag : StandardTag.values()) {
      if (standardTag.getIsSevenTagRosterTag() && !TagUtility.existsTag(tagList, standardTag)) {
        final Tag placeHolderTag = TagPlaceHolderUtility.getPlaceHolderTag(standardTag);
        tagList.add(placeHolderTag);
      }
    }
  }

  // -------------------------------------------------------------------------------------------------
  // Whitespace skipping helpers
  // -------------------------------------------------------------------------------------------------

  /** Consumes any run of {@link PgnTokenType#SPACES} and {@link PgnTokenType#NEWLINE} tokens at the current position. */
  private void skipInsignificantWhitespace() {
    while (true) {
      final PgnTokenType type = tokenizer.peek().type();
      if (type == PgnTokenType.SPACES || type == PgnTokenType.NEWLINE) {
        tokenizer.next();
      } else {
        break;
      }
    }
  }

  /** Consumes only {@link PgnTokenType#SPACES} at the current position. Used inside a single logical line. */
  private void skipInlineWhitespace() {
    while (tokenizer.peek().type() == PgnTokenType.SPACES) {
      tokenizer.next();
    }
  }

  // -------------------------------------------------------------------------------------------------
  // Board replay & post-processing
  // -------------------------------------------------------------------------------------------------

  private static void validateBoardPerLastMove(Fen startFen, List<PgnHalfMove> halfMoveList) {
    final Board board = new Board(startFen);
    for (final PgnHalfMove halfMove : halfMoveList) {
      final Side side = board.getHavingMove();
      final int fullMoveNumber = board.getFullMoveNumberForNextHalfMove();
      try {
        board.performMove(halfMove.san());
      } catch (final SanValidationException e) {
        final String moveNumberAndSan = HalfMoveUtility.calculateMoveNumberAndSanWithSpace(fullMoveNumber, side,
            halfMove.san());
        @SuppressWarnings("null") @NonNull final String messageSanValidationFailure = e.getMessage();
        final String message = "The validation for " + moveNumberAndSan + " failed. Reason: "
            + messageSanValidationFailure;
        throw new LenientPgnParserValidationException(LenientPgnParserValidationProblem.SAN,
            e.getSanValidationProblem(), message);
      }
    }
  }

  private static Fen calculateStartFen(List<Tag> tagList, boolean isStartFromPosition) {
    final String startFenStr = isStartFromPosition ? TagUtility.readFen(tagList) : FenConstants.FEN_INITIAL_STR;
    return FenParserAdvanced.parseFenAdvanced(startFenStr);
  }

  private static void removeFenIfInitial(List<Tag> tagList, Fen startFen) {
    if (startFen.equals(FenConstants.FEN_INITIAL)) {
      if (TagUtility.hasFen(tagList)) {
        TagUtility.removeFenTag(tagList);
      }
      if (TagUtility.hasSetUp(tagList)) {
        TagUtility.removeSetUpTag(tagList);
      }
    }
  }

  // -------------------------------------------------------------------------------------------------
  // Exception builder
  // -------------------------------------------------------------------------------------------------

  private static LenientPgnParserValidationException tagFormatError(String message) {
    return new LenientPgnParserValidationException(LenientPgnParserValidationProblem.TAG_FORMAT_INVALID,
        SanValidationProblem.NONE, message);
  }

  private static LenientPgnParserValidationException tagReappearError() {
    return new LenientPgnParserValidationException(LenientPgnParserValidationProblem.TAG_REAPPEAR,
        SanValidationProblem.NONE, "After the movetext started, tags can no longer appear.");
  }

  private static boolean isAsciiLetterOrDigit(char c) {
    return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
  }
}
