package com.dlb.chess.pgn.parser;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.PgnCommentaryValidationException;
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
import com.dlb.chess.pgn.parser.model.LenientPgnParserValidationResult;
import com.dlb.chess.pgn.parser.model.PgnCommentary;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.pgn.parser.model.Tag;
import com.dlb.chess.pgn.parser.sequential.PgnCharStream;
import com.dlb.chess.pgn.parser.sequential.PgnToken;
import com.dlb.chess.pgn.parser.sequential.PgnTokenType;
import com.dlb.chess.pgn.parser.sequential.PgnTokenizer;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.utility.TagPlaceHolderUtility;
import com.dlb.chess.utility.TagUtility;

/**
 * Lenient PGN parser. Permissive inter-token rules: any whitespace separates tokens, move-number indicators are
 * consumed and ignored, termination marker is optional, space before suffixes is tolerated, missing seven-tag-roster
 * entries are patched with placeholders. Independent validation pipeline from {@link StrictPgnParser}.
 */
public final class LenientPgnParser {

  private static final int SAN_MIN_LENGTH = 2;
  private static final int SAN_MAX_LENGTH = 7;

  private final String source;
  private final PgnTokenizer tokenizer;

  private LenientPgnParser(String source) {
    this.source = NewlineNormalization.toLf(stripUtf8Bom(source));
    this.tokenizer = new PgnTokenizer(new PgnCharStream(this.source));
  }

  // -------------------------------------------------------------------------------------------------
  // Public entry points
  // -------------------------------------------------------------------------------------------------

  public static PgnFile parseText(String pgn) {
    return new LenientPgnParser(pgn).parseInternal();
  }

  public static PgnFile parse(Path pgnFilePath) {
    return parseText(FileUtility.readFileAsString(pgnFilePath));
  }

  public static PgnFile parse(Path pgnFolderPath, String pgnFileName) {
    return parse(FileUtility.calculateFilePath(pgnFolderPath, pgnFileName));
  }

  public static PgnFile parse(String pgnFilePath) {
    return parse(NonNullWrapperCommon.get(pgnFilePath));
  }

  /** Parses lines produced by a line-based reader (each entry is one line without its terminator). */
  public static PgnFile parse(List<String> fileLines) {
    return parseText(joinLines(fileLines));
  }

  private static String joinLines(List<String> lines) {
    final StringBuilder builder = new StringBuilder();
    for (final String line : lines) {
      builder.append(line).append('\n');
    }
    return NonNullWrapperCommon.toString(builder);
  }

  public static LenientPgnParserValidationResult validate(Path pgnFolderPath, String pgnFileName) {
    return validate(FileUtility.calculateFilePath(pgnFolderPath, pgnFileName));
  }

  public static LenientPgnParserValidationResult validate(String pgnFilePath) {
    return validate(FileUtility.calculateFilePath(pgnFilePath));
  }

  public static LenientPgnParserValidationResult validate(Path pgnFilePath) {
    try {
      parse(pgnFilePath);
      return new LenientPgnParserValidationResult(LenientPgnParserValidationProblem.OK, SanValidationProblem.NONE,
          "OK");
    } catch (final LenientPgnParserValidationException e) {
      @SuppressWarnings("null") @NonNull final String message = e.getMessage();
      return new LenientPgnParserValidationResult(e.getLenientPgnParserValidationProblem(), e.getSanValidationProblem(),
          message);
    } catch (final RuntimeException e) {
      final var message = "An unexpected error occurred during validation. Reason: " + e.getMessage();
      return new LenientPgnParserValidationResult(LenientPgnParserValidationProblem.UNKNOWN_ERROR,
          SanValidationProblem.NONE, message);
    }
  }

  /** Like {@link #parseText(String)} but returns a structured result instead of throwing. */
  public static LenientPgnParserValidationResult validateText(String pgn) {
    try {
      parseText(pgn);
      return new LenientPgnParserValidationResult(LenientPgnParserValidationProblem.OK, SanValidationProblem.NONE,
          "OK");
    } catch (final LenientPgnParserValidationException e) {
      @SuppressWarnings("null") @NonNull final String message = e.getMessage();
      return new LenientPgnParserValidationResult(e.getLenientPgnParserValidationProblem(), e.getSanValidationProblem(),
          message);
    } catch (final RuntimeException e) {
      final var message = "An unexpected error occurred during validation. Reason: " + e.getMessage();
      return new LenientPgnParserValidationResult(LenientPgnParserValidationProblem.UNKNOWN_ERROR,
          SanValidationProblem.NONE, message);
    }
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
    expectOnlyTrailingContentUntilEof();

    final ResultTagValue resultTagValue = reconcileResult(tagList, movetext.terminationResult());
    fixTagListForResultIfRequired(tagList, resultTagValue);

    validateTagSetUpValue(tagList);
    // Lenient policy: presence of the FEN tag drives the setup-from-position decision; a stale SetUp without FEN
    // is dropped by the fix-up below.
    final var isStartFromPosition = TagUtility.hasFen(tagList);
    fixTagListForSetUpIfRequired(tagList, isStartFromPosition);

    final Fen startFen = calculateStartFen(tagList, isStartFromPosition);

    validateBoardPerLastMove(startFen, movetext.halfMoveList());

    fixTagListForMissingSevenTagRosterTags(tagList);

    removeFenIfInitial(tagList, startFen);
    Collections.sort(tagList);

    return new PgnFile(tagList, startFen, movetext.pregameCommentary(), movetext.halfMoveList());
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
      // Any line still containing [ or ] is a tag-line candidate; report it rather than slip into movetext parsing.
      if (currentLineContainsTagBracket(peek.line())) {
        throw tagFormatError("A tag line with an invalid format was found on line " + peek.line() + ".");
      }
      return tags;
    }
  }

  /** Returns true if line {@code lineNumber} (1-based) contains {@code [} or {@code ]}. */
  private boolean currentLineContainsTagBracket(int lineNumber) {
    var index = 0;
    var currentLine = 1;
    while (currentLine < lineNumber && index < source.length()) {
      final var c = source.charAt(index);
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
      final var c = source.charAt(index);
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
      throw tagFormatError("Tag opening bracket [ expected but found \"" + open.text() + "\".");
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
    for (var i = 0; i < name.length(); i++) {
      final var c = name.charAt(i);
      if (!isAsciiLetterOrDigit(c) && c != '_' && c != '+' && c != '#' && c != '=' && c != ':' && c != '-') {
        throw tagFormatError("The tag name contains an invalid character \"" + c + "\".");
      }
    }
  }

  private static void validateUniqueTagNames(List<Tag> tagList) {
    for (var i = 0; i < tagList.size(); i++) {
      for (var j = i + 1; j < tagList.size(); j++) {
        if (NonNullWrapperCommon.get(tagList, i).name().equals(NonNullWrapperCommon.get(tagList, j).name())) {
          throw new LenientPgnParserValidationException(LenientPgnParserValidationProblem.TAG_NAME_NOT_UNIQUE,
              SanValidationProblem.NONE, "The tag name must be unique. The tag name \""
                  + NonNullWrapperCommon.get(tagList, i).name() + "\" was used more than once.");
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

  private record MovetextOutcome(List<PgnHalfMove> halfMoveList, PgnCommentary pregameCommentary,
      @Nullable ResultTagValue terminationResult) {
  }

  private MovetextOutcome parseMovetext() {
    var pregameCommentary = PgnCommentary.EMPTY;
    final List<PgnHalfMove> halfMoves = new ArrayList<>();
    @Nullable ResultTagValue terminationResult = null;

    skipInsignificantWhitespace();
    // Pregame-commentary slot: exactly one commentary allowed before the first move. Additional braces before any
    // half-move fall through to the main loop and are reported as R4 (brace at SAN-expected position).
    if (isBraceToken(tokenizer.peek().type())) {
      pregameCommentary = consumeCommentaryOrThrow();
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
      if (isBraceToken(type)) {
        throwIfBrokenBrace(peek);
        throw movetextError(LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_NOT_ALLOWED_IN_SAN,
            "A commentary brace cannot occur where a SAN half-move is expected.");
      }
      if (type == PgnTokenType.MOVE_SUFFIX_ANNOTATION) {
        if (halfMoves.isEmpty()) {
          tokenizer.next();
          continue;
        }
        final PgnToken suffixToken = tokenizer.next();
        final MoveSuffixAnnotation suffix = parseMoveSuffix(suffixToken.text());
        final var last = halfMoves.size() - 1;
        final PgnHalfMove previous = NonNullWrapperCommon.get(halfMoves, last);
        halfMoves.set(last, new PgnHalfMove(previous.san(), suffix, previous.commentary()));
        continue;
      }
      if (type == PgnTokenType.SYMBOL) {
        // Tolerate spaced move-number indicators like `1 . e4` and `1 ... e5` — see consumedSpacedMoveNumber.
        if (consumedSpacedMoveNumber(peek)) {
          continue;
        }
        final PgnHalfMove halfMove = parseHalfMoveLenient();
        halfMoves.add(halfMove);
        skipInsignificantWhitespace();
        if (isBraceToken(tokenizer.peek().type())) {
          final var commentary = consumeCommentaryOrThrow();
          final var last = halfMoves.size() - 1;
          final PgnHalfMove previous = NonNullWrapperCommon.get(halfMoves, last);
          halfMoves.set(last, new PgnHalfMove(previous.san(), previous.moveSuffixAnnotation(), commentary));
        }
        continue;
      }
      throw new LenientPgnParserValidationException(
          LenientPgnParserValidationProblem.EXCEPTION_CAUGHT_FROM_STRICT_VALIDATION, SanValidationProblem.NONE,
          "Unexpected token \"" + peek.text() + "\" at line " + peek.line() + ".");
    }

    return new MovetextOutcome(halfMoves, pregameCommentary, terminationResult);
  }

  /**
   * Detects and consumes a spaced move-number indicator (`N . ` or `N ... `) where digits and dots arrive as separate
   * symbols because whitespace split them. Returns true on consumption.
   */
  private boolean consumedSpacedMoveNumber(PgnToken digitsPeek) {
    if (!isPurelyDigits(digitsPeek.text())) {
      return false;
    }
    // Pattern 1 — digits + SPACES + dots-only symbol. Committing to consume the digits is safe because a lone
    // digits symbol is not a valid SAN; the length-error path below catches the no-dots case.
    final PgnTokenType next = tokenizer.peekNext().type();
    if (next == PgnTokenType.SPACES) {
      tokenizer.next(); // digits
      skipInlineWhitespace();
      final PgnToken afterSpace = tokenizer.peek();
      if (afterSpace.type() == PgnTokenType.SYMBOL && isAllDots(afterSpace.text())) {
        tokenizer.next();
        return true;
      }
      throw movetextError(LenientPgnParserValidationProblem.EXCEPTION_CAUGHT_FROM_STRICT_VALIDATION,
          "The movetext contains the SAN '" + digitsPeek.text() + "' with an invalid SAN length.");
    }
    // Pattern 2 — digits + dots-only SYMBOL with no separating SPACES. Rare; tokenizer normally coalesces these.
    if (next == PgnTokenType.SYMBOL && isAllDots(tokenizer.peekNext().text())) {
      tokenizer.next(); // digits
      tokenizer.next(); // dots
      return true;
    }
    return false;
  }

  private static boolean isPurelyDigits(String text) {
    if (text.isEmpty()) {
      return false;
    }
    for (var i = 0; i < text.length(); i++) {
      final var c = text.charAt(i);
      if (c < '0' || c > '9') {
        return false;
      }
    }
    return true;
  }

  private static boolean isAllDots(String text) {
    if (text.isEmpty()) {
      return false;
    }
    for (var i = 0; i < text.length(); i++) {
      if (text.charAt(i) != '.') {
        return false;
      }
    }
    return true;
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

    var suffix = MoveSuffixAnnotation.NONE;
    // Allow whitespace between SAN and suffix annotation (`e4 !!`).
    skipInlineWhitespace();
    if (tokenizer.peek().type() == PgnTokenType.MOVE_SUFFIX_ANNOTATION) {
      suffix = parseMoveSuffix(tokenizer.next().text());
    }

    return new PgnHalfMove(san, suffix, PgnCommentary.EMPTY);
  }

  private static boolean isBareCheckOrMate(String text) {
    return "+".equals(text) || "#".equals(text);
  }

  /** Returns the {@link PgnCommentary} for a well-formed brace token, or throws the matching error category. */
  private PgnCommentary consumeCommentaryOrThrow() {
    final PgnToken token = tokenizer.next();
    switch (token.type()) {
      case BRACE_COMMENT:
        try {
          return new PgnCommentary(token.text());
        } catch (final PgnCommentaryValidationException pcve) {
          // Defensive — the tokenizer cannot produce `}` here (handled as separate types), so unreachable in practice.
          throw movetextError(LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_CONTAINS_FORBIDDEN_CHARACTER,
              pcve.getMessage());
        }
      case BRACE_COMMENT_UNCLOSED:
        throw movetextError(LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE,
            "A commentary opened with { was not closed with } before end of input.");
      case BRACE_STRAY_CLOSE:
        throw movetextError(LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE,
            "A closing brace } was found with no matching opening brace.");
      default:
        throw new com.dlb.chess.common.exceptions.ProgrammingMistakeException(
            "consumeCommentaryOrThrow called for non-brace token: " + token.type());
    }
  }

  private static boolean isBraceToken(PgnTokenType type) {
    return type == PgnTokenType.BRACE_COMMENT || type == PgnTokenType.BRACE_COMMENT_UNCLOSED
        || type == PgnTokenType.BRACE_STRAY_CLOSE;
  }

  /** After the termination marker (or EOF) only whitespace may appear before EOF. */
  private void expectOnlyTrailingContentUntilEof() {
    while (true) {
      final PgnToken token = tokenizer.peek();
      if (token.type() == PgnTokenType.EOF) {
        return;
      }
      if (token.type() == PgnTokenType.SPACES || token.type() == PgnTokenType.NEWLINE) {
        tokenizer.next();
        continue;
      }
      if (token.type() == PgnTokenType.TAG_BRACKET_OPEN || currentLineContainsTagBracket(token.line())) {
        throw tagReappearError();
      }
      throwIfBrokenBrace(token);
      throw movetextError(LenientPgnParserValidationProblem.MOVETEXT_CONTENT_AFTER_TERMINATION,
          "Unexpected content after the game termination marker: \"" + token.text() + "\".");
    }
  }

  /** Throws the broken-brace-specific error if {@code token} is one; returns normally otherwise. */
  private static void throwIfBrokenBrace(PgnToken token) {
    switch (token.type()) {
      case BRACE_COMMENT_UNCLOSED:
        throw movetextError(LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE,
            "A commentary opened with { was not closed with } before end of input.");
      case BRACE_STRAY_CLOSE:
        throw movetextError(LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE,
            "A closing brace } was found with no matching opening brace.");
      default:
        // Not a broken brace — caller handles.
    }
  }

  private static LenientPgnParserValidationException movetextError(LenientPgnParserValidationProblem problem,
      String message) {
    return new LenientPgnParserValidationException(problem, SanValidationProblem.NONE, message);
  }

  private static MoveSuffixAnnotation parseMoveSuffix(String text) {
    // Lenient: unknown suffixes downgrade to NONE rather than fail.
    if (!MoveSuffixAnnotation.exists(text)) {
      return MoveSuffixAnnotation.NONE;
    }
    return MoveSuffixAnnotation.calculate(text);
  }

  private static void validateSanCharacters(String san) {
    for (var i = 0; i < san.length(); i++) {
      final var c = san.charAt(i);
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

  private static ResultTagValue reconcileResult(List<Tag> tagList, @Nullable ResultTagValue terminationResult) {
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

  /**
   * Consumes any run of {@link PgnTokenType#SPACES} and {@link PgnTokenType#NEWLINE} tokens at the current position.
   */
  private void skipInsignificantWhitespace() {
    while (true) {
      final PgnTokenType type = tokenizer.peek().type();
      if (type != PgnTokenType.SPACES && type != PgnTokenType.NEWLINE) {
        break;
      }
      tokenizer.next();
    }
  }

  /**
   * Consumes only {@link PgnTokenType#SPACES} at the current position. Used inside a single logical line.
   */
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
      final var fullMoveNumber = board.getFullMoveNumberForNextHalfMove();
      try {
        board.performMove(halfMove.san());
      } catch (final SanValidationException e) {
        final String moveNumberAndSan = HalfMoveUtility.calculateMoveNumberAndSanWithSpace(fullMoveNumber, side,
            halfMove.san());
        @SuppressWarnings("null") @NonNull final String messageSanValidationFailure = e.getMessage();
        final var message = "The validation for " + moveNumberAndSan + " failed. Reason: "
            + messageSanValidationFailure;
        // Propagate the GameStatus from the SanValidationException for the GAME_ALREADY_ENDED
        // case so callers can react to the specific termination cause without parsing the
        // human-readable message. For any other SAN problem the GameStatus is null.
        throw new LenientPgnParserValidationException(LenientPgnParserValidationProblem.SAN,
            e.getSanValidationProblem(), message, e.getGameStatus());
      }
    }
  }

  private static Fen calculateStartFen(List<Tag> tagList, boolean isStartFromPosition) {
    final var startFenStr = isStartFromPosition ? TagUtility.readFen(tagList) : FenConstants.FEN_INITIAL_STR;
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
    return c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z';
  }
}
