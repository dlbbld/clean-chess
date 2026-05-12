# Tasks

Order within each section is the source of truth. Completed tasks move to **Done** at the bottom.

---

## Current release — current backlog

### FEN parser tier audit and rename
Three FEN parsers exist: `FenParserRaw`, `FenParserAdvanced`, `FenParserAdvancedFurther`. The third is largely unused. Boundaries between the three are unclear and the docs overclaim what each tier proves (see "FEN-validation documentation overclaims" below).
- [ ] Audit each tier — what each parser actually validates vs what its docs claim
- [ ] Document each tier's contract precisely in `specification.md`
- [ ] Decide: keep three tiers, collapse `AdvancedFurther` into `Advanced`, or drop it

### Move FEN-letter parsing off `Side` and `BasicChessUtility` onto the FEN parser
Layering violation surfaced by the API-surface audit. `Side.calculate(String)` parses the FEN single-letter side indicator (`"w"` / `"b"`) into a `Side` enum value — but FEN-syntax knowledge does not belong on the chess-rules `Side` enum. The same parsing also appears, redundantly, on `BasicChessUtility.calculateSideHavingMoveForSide(String)`. Both should be deleted; their logic belongs in `FenParserRaw` / `FenParserAdvanced` (wherever the rest of FEN field parsing lives).

While in `FenParserAdvanced`, `validateHavingMove` currently checks for `w` or `b` via a regular expression — overkill for a two-character alphabet. A direct equality check that throws the advanced FEN validation exception on mismatch is the natural shape.

- [ ] Move FEN-letter → `Side` parsing into the FEN parser layer; pick the right home (likely `FenParserRaw` since this is purely lexical)
- [ ] Delete `Side.calculate(String)` from the `Side` enum
- [ ] Delete `BasicChessUtility.calculateSideHavingMoveForSide(String)` (duplicate of the above)
- [ ] Replace the regex in `FenParserAdvanced.validateHavingMove` with a direct equality check + advanced-FEN-validation throw
- [ ] Update any test callers of the removed methods to go through the FEN parser instead

### Replace `UciValidateHelper` enum with computed lookup
Auto-generated 1984-line enum, ~111 KB class file (~50% of the JAR's bytecode mass). Used as a list-of-strings rather than as an enum. A generation loop in the static init of its only caller would replace 1984 lines of source with ~12.
- [ ] Replace the enum with a `Set<String>` (or similar) computed via a constructor loop
- [ ] Verify JAR shrinks by ~50%

### FEN-validation documentation overclaims
`fen/package-info.java` and `Board.java` (class-level + constructor docs) say advanced FEN rejects positions "no real game could reach." The code does strong structural and rule-consistency checks but does not prove full game reachability. Also: package text says halfmove clock "at or above 150" while code accepts exactly 150.
- [ ] Soften prose to "advanced structural and rule-consistency validation"
- [ ] List exactly what is enforced; drop the unsubstantiated reachability claim
- [ ] Fix the "at or above" off-by-one

### Broken Javadoc link in `fen/package-info.java`
Links to `com.dlb.chess.fen.FenParser` which does not exist. `mvn javadoc:jar` succeeds only because `<doclint>none</doclint>` in pom.xml; the warning is still emitted. Real target is `FenParserRaw` + `FenParserAdvanced`.
- [ ] Fix the link

### CHA / unwinnability wording teaches the wrong mental model
README and `unwinnability/package-info.java` use "worst play / worst-case play by the opponent." In game-theory English, "worst-case opponent" reads like *best defensive play*, but CHA / winnability is the opposite: whether any legal continuation can lead to mate (cooperative / helpmate-style existence). Worth fixing because it is the flagship concept.
- [ ] Rewrite around "no legal sequence exists, even with the opponent's cooperation" or "no theoretical mating sequence exists under any legal continuation"

### README inconsistency — CHA full "100% accurate" vs `UNDETERMINED`
README says CHA full is "slower but 100% accurate," then a few lines down documents the `UNDETERMINED` outcome (and again later in the doc).
- [ ] Reword to "complete when it returns WINNABLE / UNWINNABLE; bounded search may return UNDETERMINED"

### Remove the `EnPassantCaptureRuleThreefold` dual-path
The `EnPassantCaptureRuleThreefold` enum (`DO_IGNORE` / `DO_NOT_IGNORE`) drives a second, parallel repetition-tracking code path that ignores en passant availability when comparing dynamic positions. It was added as a research tool: in FIDE rules, two positions with the same piece arrangement but different en-passant availability are *not* the same position for threefold-repetition purposes — but chess.com (and other platforms) implemented the lazy "visual repetition" rule and don't check en-passant availability. The dual path made it easy to find PGN games where the two interpretations diverge, producing examples to demonstrate the platform-side bug.

That research goal is no longer load-bearing for the library. The dual code path costs ongoing complexity (two flavours of `equals`-like comparison, two parallel data structures on `Report`, two flavours of every repetition test fixture) for a feature whose audience was one researcher. As clean-chess matures, the library should implement the FIDE rule cleanly and only.

- [ ] Drop the `EnPassantCaptureRuleThreefold` enum
- [ ] Remove `Report.repetitionListListInitialEnPassantCapture()` and any other dual-path fields/methods on `Report`
- [ ] Collapse `RepetitionUtility.getCountRepetition` and the surrounding repetition-tracking machinery to the single FIDE-correct path
- [ ] Drop the dual-path test fixtures, reports, and representation code in `com.dlb.chess.test.report.representation.*`
- [ ] Strip the explanatory paragraph in `RepetitionUtility`'s class-level Javadoc about "two different ways" of counting repetition
- [ ] Verify `git grep -i "ignoring en passant"` (or similar phrasing) returns zero hits afterwards

### Replace `EnumConstants` constant interface
`com.dlb.chess.common.constants.EnumConstants` is a `public interface` whose only purpose is to expose ~90 `public static final` aliases for `Square.*`, `Side.*`, `Piece.*`, `PieceType.*`, `Rank.*`, `File.*` so implementing classes inherit them unqualified. This is the classic "constant interface" anti-pattern (Effective Java item 22): interfaces should describe a contract/behavior, not be a convenience-inheritance vehicle for constants. The mechanism reads as beginner Java and leaks an internal vocabulary choice into the public type surface — `ChessBoard extends EnumConstants` is the clearest symptom (the chess contract has nothing to do with how implementers prefer to spell `Square.E4`). Used by 43 files under `src/main` plus tests.

Replacement strategy options, depending on intended audience:
- public-API constants: `public final class EnumConstants` with `public static final` fields and a private constructor (callers `import static`)
- internal-only: make package-private and split closer to where they belong (domain-grouped, e.g. `BoardSquares`, `PieceLetters`)
- derived enum collections: prefer local `EnumSet` / `ImmutableSet` factories in the utility that needs them, or dedicated package-private constants classes by domain

- [ ] Pick a replacement strategy (default lean: package-private utility class with `import static`, since the constants are internal vocabulary and the audit reduces public surface anyway)
- [ ] Drop `extends EnumConstants` from `ChessBoard` regardless of strategy — the interface should not carry constants
- [ ] Convert the 43 src/main call sites + tests to static imports
- [ ] Folds naturally into the API-surface reduction release; treat as a cleanup target there

### Profound-level square geometry — promote single-step calculations to lookup tables
The codebase already uses lookup tables for the geometry that matters — `OrthogonalRange`, `DiagonalRange`, `KnightEmptyBoardSquares`, `BishopEmptyBoardSquares`, `RookEmptyBoardSquares`, `DiagonalLineUtility`. Single-step instance-style methods on `Square` (`calculateLeftSquare`, `calculateLeftDiagonalSquare`, `calculateAheadSquare`, etc.) and `File` / `Rank` are the calculate-on-demand holdouts in an otherwise table-based codebase. The "calculate" form has a deeper testing problem: any independent test implementation faces a definitional regress ("left of E4 from White is D4 — but what does *left* mean if not what `calculateLeft` returns?"), which is how `Square.calculateIsLeftDiagonalSquare` ended up as a tautological method that tested itself against itself.

The fix is to promote these single-step relationships to data:
- `Map<Square, Map<Side, Square>>` (or `EnumMap<Square, EnumMap<Side, Square>>`) constants for left, right, ahead, behind, left-diagonal, right-diagonal
- The "has" predicates collapse to `map.containsKey(...)` or `value != NONE`
- The map is built once at class load; tests verify the table by inspection or via python-chess cross-reference (folds into the existing python-chess backlog)
- The bug surface shrinks to one place: the table-builder

- [ ] Inventory single-step `calculate*` methods on `Square` / `File` / `Rank` that are pure square→square (or square+side→square) lookups
- [ ] Replace each with a precomputed `EnumMap` constant + a thin accessor
- [ ] Generate the expected tables either by hand-curation or by python-chess cross-reference (latter is preferred once the python-chess infrastructure lands)
- [ ] Drop the algorithm-vs-algorithm test patterns; tests become "look up in production table, compare to reference table"
- [ ] Folds naturally into the DeepSquare rename moment — this kind of foundational rigor is exactly what the rename signals
- [ ] **Companion concern — bloated lookup-table implementations.** `PawnDiagonalSquares` is 826 lines of generated code (per-square `addWhiteA1`, `addWhiteA2`, … methods) to express what is conceptually "for each pawn from-square, the 0–2 diagonal capture squares." The same shape recurs across the `com.dlb.chess.squares.emptyboard.*` family (`Knight`, `Bishop`, `Rook`, `Queen`, `King`, `PawnOneAdvance`, `PawnTwoAdvance`, `PawnAnyAdvance`). These tables are correctly precomputed, but their implementation should be a single `static {}` initializer that loops over `Square.REAL` and computes each entry via simple file/rank arithmetic — not hundreds of method-per-square stubs. Replacing them collapses ~thousand-line files to dozens of lines while preserving the precomputed-table API. Same theme as the main bullet: keep the lookup, sane the implementation.

### Introduce `LegalMoveKind` on `LegalMove`; absorb `EnPassantRole`
A legal move has a category — normal, castling, en passant capture, pawn two-square advance, promotion — that the move pipeline already knows when it constructs the `LegalMove`. Today consumers (and tests) recover that category by re-reading `MoveSpecification` fields one at a time (e.g. `move.moveSpecification().promotionPieceType() != PromotionPieceType.NONE` to ask "was this a promotion?"). That is a smell: the move pipeline knew the answer, then threw it away.

The proposed shape is one mutually-exclusive enum on `LegalMove`:

```java
public enum LegalMoveKind {
  NORMAL,
  CASTLING,
  EN_PASSANT_CAPTURE,
  PAWN_TWO_SQUARE_ADVANCE,
  PROMOTION
}
```

```java
public record LegalMove(
    MoveSpecification moveSpecification,
    Piece movingPiece,
    Piece pieceCaptured,
    LegalMoveKind kind) {
}
```

This **replaces / absorbs** `EnPassantRole` rather than sitting beside it as a narrow `promotionRole`. Adding `promotionRole` next to `enPassantRole` would multiply the number of redundant booleans / role-enums; one mutually-exclusive `kind` is cleaner.

Why this is the right shape even if tests are the first consumer: the value isn't test scaffolding. It is true metadata of a legal move, computed at the moment the engine knows the move is legal. Tests using it first is fine — what matters is that the metadata belongs on the type, not in scattered "calculate was last move X?" helpers.

Name caution: prefer `LegalMoveKind` over `MoveType`. `MoveType` is broad enough that future readers will expect it to describe arbitrary move concepts; `LegalMoveKind` is precise — it categorises an already-legal move per this engine's move pipeline.

API trade-off: expands `LegalMove`'s public surface by one field/enum, but lets the project remove the more awkward public `EnPassantRole` and a family of "calculate-was-last-move-X" utility methods. Net effect is **API clarification**, not API bloat.

Surfaced during the API-surface audit by `PromotionUtility.calculateIsPromotionLastMove(ChessBoard)`, which had only test callers and was moved to a private test helper as a stop-gap; the helper still re-reads `MoveSpecification` to answer the question. Once `LegalMoveKind` lands, the test helper collapses to `board.getLastMove().kind() == LegalMoveKind.PROMOTION`, the `EnPassantRole` enum disappears, and the family of "is this kind of move?" helpers on production utilities all collapse to direct `kind()` reads.

- [ ] Define `LegalMoveKind` enum; decide final value set (the five above as a starting list)
- [ ] Add `kind()` component to `LegalMove`; populate at construction in the rule pipeline
- [ ] Remove `EnPassantRole` enum and `LegalMove.enPassantRole()`; switch callers to `kind()`
- [ ] Sweep production "calculate-was-last-move-X" helpers (`PromotionUtility.calculateIsPromotion(...)`, `CastlingUtility.calculateIsCastlingMove(...)`, the various enPassant-checks) and collapse to direct `kind()` reads; delete the helpers if no caller remains
- [ ] Update the private `calculateIsPromotionLastMove` helpers in `TestPerformMoveSeveralStates` and similar test classes to use `kind()` directly

### Records carry data, not behavior — sweep for violations
The project rule (documented in `coding-conventions.md`): records carry data; domain logic that operates on them lives in dedicated utility / service classes. Permitted on a record: compact-constructor validation, `Comparable` when ordering is intrinsic, and language-provided `equals` / `hashCode` / `toString`. Domain-operation methods are not.

Surfaced by the unused-code-detector pass on `StaticPosition`: the record carries multiple non-data methods — `createChangedPosition` (three overloads), `isPawn`, `isOwnPawn`, `isOpponentPawn`, `isOwnKing`, `isOpponentKing`, almost certainly more. Some have only test callers (suggesting test scaffolding), some have production callers, one (`isOwnKing`) has zero callers anywhere.

- [ ] Catalog every non-permitted member on `StaticPosition` and assign a disposition per member: delete (no callers anywhere), move to a test-side helper that **takes** a `StaticPosition` rather than duplicating it (test-only callers), or move to a `StaticPositionUtility` (production callers).
- [ ] Sweep every record under `src/main/java` for the same pattern. Records to check include at least `Fen`, `Tag`, `PgnFile`, `LegalMove`, `MoveSpecification`, `StaticPosition`, plus any other top-level `record` declarations under `src/main`.
- [ ] Apply the dispositions; verify only the permitted member shapes remain on each record.
- [ ] Naturally folds into the API-surface reduction release, since most "move to utility" relocations open the door to making the utility itself package-private.

### Rename `NonNullWrapperCommon` to `Nulls`
The class is used pervasively (every JDT-null-safe wrapper for JDK calls goes through it), and `NonNullWrapperCommon` is too long for something so frequent. `Nulls` is short, pronounceable, says the domain (this utility exists because of nullness handling), and discoverable in the IDE. Rejected alternatives: `NNVC` (cryptic codeword), `Safe` / `Checked` (vague), `NonNulls` (awkward plural).
- [ ] Rename class `NonNullWrapperCommon` → `Nulls`; update all call sites (uses appear in most files in the project — bulk rename)
- [ ] Verify the methods are still all about nullness handling; if any aren't, reconsider the name

## Future release — PGN headers treatment and lenient FEN validation

### Separate PGN parse, semantics, and export — stop normalising input on the lenient path

#### The problem
Today's lenient PGN pipeline conflates **four distinct jobs** into one path:
1. **Parsing** — should preserve what the user gave (tag presence/absence, FEN without SetUp, missing Result, unknown tags, tag order).
2. **Validation** — should *report* forgiven issues to the consumer (already done via `LenientPgnParserValidationResult.sanForgivenItems()` for SAN deviations; needs the same for tag-level issues).
3. **Canonical export** — produces a strict-spec-compliant PGN (completed Seven Tag Roster, `SetUp "1"` when `FEN` is present, `Result` synthesised from termination marker, canonical SAN, normalised formatting).
4. **Round-trip export** — re-emits the input as parsed: same tags, same values, same Result presence/absence — without inventing what the user didn't give.

In the current code, "parse leniently and then write" silently does jobs 1+3 together: the lenient parser accepts a deficient PGN, then `TagPlaceHolderUtility` fabricates STR placeholders, a `*` Result is invented from nothing, `SetUp "1"` is added when `FEN` lacked it, and the output is presented as if it had been the input. The friction is real and load-bearing: **a lenient `parse → write` does not return the user's PGN.** And the discomfort is not bikeshedding — it has been "what was keeping me so long" from finishing the lenient feature.

The principle: **parse preserves, validation reports, canonical export normalises, round-trip export echoes.** Four jobs, separable concerns; each gets its own seam.

#### The three model layers to introduce
1. **Parse model** — what was actually in the PGN. Tag list preserves real presence/absence and order; `FEN`-without-`SetUp` is preserved; missing `Result` is preserved as missing (not collapsed to `*`); unknown tags pass through.
2. **Game semantic model** — the engine's internal view for rules/reporting. Introduces `declaredResult: Optional<ResultTagValue>` (what the input said, if anything) and `effectiveResult` (derived/defaulted, used for chess-rule logic). **Missing must remain distinguishable from `ONGOING`.** Today the codebase maps "Result not present" → `*` ongoing; that is a semantic bug, or at least a design debt, because "the user did not say" and "the game is in progress" are different facts.
3. **Export modes** — `PgnWriter` gains an explicit mode (recommended: `WriteMode` enum parameter, or separate methods):
   - **Canonical export** — strict-spec-compliant output. Completes STR, adds `SetUp "1"` when `FEN` is present, fabricates `Result` from termination marker (or `*` if neither), canonical SAN, standard formatting.
   - **Semantic-preserving export** *(the right default for `PgnWriter`)* — preserves which tags existed, their values, their order, and missing-ness. **Normalises** harmless formatting (whitespace inside tag brackets, line wrapping) and move spelling (canonical SAN — `e2-e4` → `e4`, `Nb1c3` → `Nc3`, bogus `a6+` → `a6`). Forgiven items remain on the validation result, *not echoed in the export*.
   - **Text-preserving export** — re-emits the original source bytes byte-for-byte, including weird whitespace, blank lines, original SAN spelling. **Out of scope for this work** — that is a source-preserving syntax tree, a much heavier feature. Captured here only so the export-mode taxonomy is complete.

#### Concrete cases under the new model
1. **Missing STR tags** — parse keeps tags missing. Semantic export emits only the tags the user gave (no placeholder fill). Canonical export adds placeholders. **`TagPlaceHolderUtility` either goes away or moves to canonical-only path.**
2. **`Result` tag absent** — parse stores `declaredResult: Optional.empty()`. Semantic export omits `Result` from output. Canonical export synthesises `Result "*"` if neither tag nor termination marker exists, otherwise from the termination marker. The engine's chess-rule code uses `effectiveResult` derived separately.
3. **`FEN` without `SetUp`** — parse keeps `FEN` alone. Semantic export emits `FEN` without `SetUp`. Canonical export adds `SetUp "1"`.
4. **Tag-bracket whitespace** (`[White      "John Travolta"     ]`) — semantic export emits `[White "John Travolta"]`. Whitespace is formatting trivia, not chess data; preserving it would require text-preserving mode.
5. **Move spelling** (`1. e2-e4 d5 2. Nb1c3 a6+` where `a6+` is not actually check) — semantic export emits canonical `1. e4 d5 2. Nc3 a6`. The lenient parser already reports each deviation via `ForgivenItem` — that is the reporting channel, not the export.

#### What `PgnWriter` should do by default
Make `PgnWriter` **semantic-preserving** by default. It's the honest balance: respects what the consumer gave (no fabricated tags, no invented Result), but produces clean PGN (canonical SAN, normalised whitespace). Canonical export becomes the opt-in path for consumers who explicitly need strict-spec output.

#### Action items
- [ ] Define `declaredResult: Optional<ResultTagValue>` on the parse-model side; introduce `effectiveResult` as a derivation used by engine code; audit every "result is `*`?" check and split into "declared missing?" vs "is ongoing?"
- [ ] `PgnFile` (or its tag list) preserves missing-tag information explicitly — verify whether today's structure already supports this or needs widening
- [ ] Drop `TagPlaceHolderUtility` STR auto-fill on the lenient parse path; if a canonical-export path needs the same logic, move it there
- [ ] Lenient parser stops synthesising a `Result` tag from the termination marker into the parsed model; the termination marker is part of the movetext, the Result tag is part of the header, they are separate signals
- [ ] Lenient parser stops fabricating `SetUp` when `FEN` is present
- [ ] Introduce explicit `WriteMode` (or two methods) on `PgnWriter`: `SEMANTIC` (the new default) and `CANONICAL` (the spec-strict alternative)
- [ ] Default `PgnWriter.writePgnFile(...)` to `SEMANTIC`; `PgnWriter.writePgnFileCanonical(...)` (or `writePgnFile(..., WriteMode.CANONICAL)`) for spec-compliant output
- [ ] Document the contract in `specification.md`: explicit table of the four jobs (parse / validate / canonical export / semantic export); explicit statement that lenient `parse → semantic-write` round-trips the meaning of the input (tag presence, Result presence, FEN-without-SetUp) while normalising formatting and move spelling
- [ ] Test fixtures: a "deficient" PGN (missing STR, no Result tag, `FEN` without `SetUp`, weird whitespace, `e2-e4`-style moves, bogus check suffixes). Semantic export → equals the same input with normalised whitespace and canonical SAN, *no fabricated tags*. Canonical export → equals a fully strict-compliant form with STR filled, `SetUp "1"` added, `Result "*"` synthesised, canonical SAN.
- [ ] Text-preserving export remains **out of scope** — note in `specification.md` that source-text-preserving export would be a separate library mode if ever needed.

#### Notes for whoever picks this up
- The `LenientPgnParserValidationResult.sanForgivenItems()` channel is already the right pattern for SAN-level forgiven items. The same pattern should be extended (or a parallel `tagForgivenItems()` added) so consumers can see which tags the lenient parser accepted leniently.
- `TagUtility` (kept as consumer-facing in the API audit) is not the source of the problem; the issue is what the parser *fabricates* before `TagUtility`'s consumers see the tag list. Once the parser stops fabricating, `TagUtility` consumers see what the user actually wrote.
- This work also subsumes the earlier short backlog entry "PGN round-trip fidelity — stop auto-completing tags on import" — that was an early sketch of the same idea; this entry is the full framing.

### Lenient FEN parser/validator
The strict FEN parser `FenParserRaw` is one regex (`^([^ ]+) ([^ ]+) ([^ ]+) ([^ ]+) ([^ ]+) ([^ ]+)$`): six fields, single space between, no leading/trailing whitespace, no tabs, no newlines, no missing counters. Any deviation gets the same bland exception. For a class-A library that consumes FEN from outside producers (engine output, lichess/chess.com exports, Stockfish, ChessBase), that's too narrow. Mirrors the strict/lenient split already established for PGN and planned for SAN.

The lenient layer is purely a **syntactic-tolerance pass**, not a semantic one. A FEN with a king missing must still fail `FenParserAdvanced`; the lenient layer just doesn't reject for whitespace, casing, or missing counters. Strict-vs-lenient (syntactic) and raw-vs-advanced (semantic) are orthogonal axes:

|                | Raw (lexical only)        | Advanced (rules-consistent)        |
|----------------|---------------------------|------------------------------------|
| Strict input   | `FenParserRaw` (today)    | `FenParserAdvanced` (today)        |
| Lenient input  | `LenientFenParser` → Raw  | `LenientFenParser` → Advanced      |

#### Implementation strategy — normalise → delegate
The lenient parser walks the input, produces a canonical FEN string plus a list of forgiven items, then hands the canonical string to the *existing* `FenParserRaw` (and `FenParserAdvanced` if requested). No duplication of the strict parsing logic; the strict regex stays one line.

#### Diagnostic taxonomy (starting set; will sharpen with the arbiter lens)
- `EXTRA_WHITESPACE_BETWEEN_FIELDS` — 2+ spaces collapse to 1
- `LEADING_WHITESPACE`
- `TRAILING_WHITESPACE`
- `TAB_OR_NEWLINE_AS_SEPARATOR` — common in engine output
- `MISSING_HALFMOVE_CLOCK` — 5-field FEN → default `0`
- `MISSING_FULLMOVE_NUMBER` — 5- or 4-field FEN → default `1`
- `MISSING_HALFMOVE_AND_FULLMOVE` — 4-field FEN; engines like Stockfish produce this constantly
- `UPPERCASE_SIDE_TO_MOVE` — `W`/`B` → `w`/`b`
- `CASTLING_NON_CANONICAL_ORDER` — `QKqk` → `KQkq`; intra-field whitespace; etc.
- `EN_PASSANT_NON_STANDARD_DASH` — `—`, `–`, `X` → `-`
- `EN_PASSANT_UPPERCASE` — `E3` → `e3`
- `RANK_DIGIT_ZERO` — `0` used for empty squares (typo)
- `TRAILING_GARBAGE_TOKEN` — extra junk after fullmove

#### Public API sketch
```java
public final class LenientFenParser {
  public static LenientFenParserValidationResult parseText(String fen);
  public static LenientFenParserValidationResult validateText(String fen);  // diagnostics only
}

public record LenientFenParserValidationResult(
    Fen fen,                                      // null only if validateText
    ImmutableList<ForgivenFenItem> forgivenItems);

public record ForgivenFenItem(
    ForgivenFenItemCode code,
    String originalToken,
    String canonicalValue);

public class LenientFenParserValidationException extends UsageException { ... }
```

Plus `Board.fromFenLenient(String)` (or `new Board(String, FenStrictness)`) for the consumer-facing opt-in. `new Board(String)` stays strict — same call-site discipline as `performMove` vs `performMoveLenient`.

#### Action items
- [ ] Settle the diagnostic taxonomy with the arbiter lens — add/remove codes based on real-world FEN deviations
- [ ] Define `ForgivenFenItemCode` enum + `ForgivenFenItem` record + `LenientFenParserValidationResult` record
- [ ] Implement `LenientFenParser` as a normaliser that produces a canonical FEN string + forgiven items list, then delegates to `FenParserRaw` (and optionally `FenParserAdvanced`)
- [ ] `LenientFenParser.parseText` vs `validateText`: same pipeline, payload vs diagnostics-only
- [ ] `LenientFenParserValidationException extends UsageException` for unrecoverable input
- [ ] `Board` opt-in entry point for lenient FEN; `new Board(String)` stays strict
- [ ] Decide: does `LenientPgnParser` use lenient FEN when reading the `FEN`/`SetUp` PGN tag, or stay strict on the tag? Lean: lenient, for consistency with movetext leniency
- [ ] One test fixture per forgiven code; one end-to-end "deficient FEN" fixture; round-trip test (lenient parse → canonical FEN → strict parse) for each
- [ ] Document the contract in `specification.md` — explicit table of strict-vs-lenient × raw-vs-advanced
- [ ] Update README "Lenient PGN parser" framing — the project now has lenient parsers for all three input languages

#### Open design questions
- Castling normalisation scope — KQkq-ordering deviations only, or also X-FEN / Shredder-FEN (Chess960) castling notation? Lean: KQkq-ordering only; X-FEN is a separate feature.
- Order of forgiven items in the result — left-to-right (token position) or grouped by code? PGN does left-to-right; mirror.

## Future release — Auto-CHA (DeepSquare moment)

### GPL v3 source-file headers

Add a short GPL preamble at the top of every source file, in the style CHA (D3-Chess) uses on its own files. The unwinnability release is the natural moment because the file headers are the strongest place to assert the CHA derivation that this release leans into.

Reference — CHA's header style:

```
/*
  Chess Unwinnability Analyzer, an implementation of a decision procedure for
  checking whether a certain player can deliver checkmate (i.e. win) in a given
  chess position.

  This software leverages Stockfish as a backend for chess-related functions.
  Stockfish is free software provided under the GNU General Public License
  (see <http://www.gnu.org/licenses/>) and so is this tool.
  The full source code of Stockfish can be found here:
  <https://github.com/official-stockfish/Stockfish>.

  Chess Unwinnability Analyzer is distributed in the hope that it will be
  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU GPL for more
  details.
*/
```

For clean-chess, adapt: short project description, copyright line, GPL v3 reference, credit to CHA as the derivation source for the unwinnability code, standard no-warranty boilerplate.

- [ ] Design the clean-chess header text (one paragraph + boilerplate)
- [ ] Apply to every `.java` file under `src/main/` and `src/test/` via a script
- [ ] One isolated commit (noisy diff, do not bundle with semantic changes)

### Auto-CHA after every move
- [ ] Per-move pipeline: invoke `isUnwinnableQuick` for both sides after every legal move; both unwinnable ⇒ `DEAD_POSITION` automatic termination
- [ ] Update `isAutomaticallyTerminated()` to include this case
- [ ] `BoardConfig` (record) with `autoChaEnabled` boolean; default `true` in production, `false` for tests and bulk PGN parsers
- [ ] Test factory/base class that constructs disabled boards
- [ ] `StrictPgnParser` and `LenientPgnParser` construct boards with auto-CHA disabled by default
- [ ] README: remove the "CHA is not run automatically after every move" note
- [ ] `specification.md` §3.1 termination table: add "Dead position (CHA quick) — automatic"
- [ ] `specification.md` §3.2: invert the framing — quick is now per-move automatic; full remains opt-in
- [ ] Performance check: with disable flag on, suite within ~10% of current
- [ ] Targeted regression tests on positions that should auto-terminate

---

## Future release — python-chess as primary cross-validation reference

This is **discussion + design first**, implementation later. The project currently uses Carlos's `chesslib` (`LibraryCarlosBoard`) as a cross-validation reference, with limitations: cannot import PGN from a non-initial position, smaller test surface, less actively maintained. python-chess is the de-facto reference in chess software, actively maintained, and handles arbitrary positions.

### Pattern recommendation — generation-based, not live invocation

- A Python script using python-chess generates expected outputs (legal moves, FEN, SAN, LAN, repetition counts, halfmove clock, dead-position verdicts) for a battery of fixtures, writes to a fixed file path.
- Java tests read the file and compare to clean-chess output.
- The Python script runs only when fixtures are added or regenerated, **not** during `mvn test`.
- Chess outputs are deterministic per input; cached reference data doesn't go stale.

`GeneratePythonTestCases.java` already exists — that's the foundation. Audit it and extend.

### Discussion items to settle before coding

- [ ] Inventory exactly what python-chess will be used as reference for: legal-move generation, SAN/LAN, FEN, repetition counts, fifty-move clock, threefold/fivefold, dead-position (does python-chess support this directly or via heuristic?), CHA-style unwinnability (it doesn't — that stays unique to clean-chess).
- [ ] Decide: gradual migration (both chesslib and python-chess as references during transition) or hard cutover (drop chesslib at the same time).
- [ ] Decide: drop `LibraryCarlosBoard` entirely once python-chess covers its usage, or keep it for rule-engine-style cross-checks.
- [ ] Document the toolchain requirement: contributors need Python 3 + `pip install chess` (the package). Goes in `setup.md`.
- [ ] Plan the regeneration workflow: how is "I added a fixture; now regenerate the python-chess-expected outputs" triggered cleanly? Maven goal? Script? Make target?

### Implementation tasks (after the discussion)

- [ ] Decide and document the file format for stored expected outputs (JSON? line-based?)
- [ ] Refactor `GeneratePythonTestCases` (or replace) to produce the agreed format
- [ ] Migrate at least one cross-validation test from chesslib to python-chess as a proof-of-concept
- [ ] Phase out chesslib usage if the discussion lands there

---

## Future release — publish to Maven Central

The capstone release. Publish to Central only when the library has stabilised — every prior release done, identity questions settled, and any tasks that surface during the prerequisite work itself addressed first. Maven Central artifacts are immutable: once published, an artifactId+version pair lives forever in the public record. The bar for moving from JitPack to Central is therefore "we are confident this artifact represents the project well, indefinitely."

### Prerequisites — must be true before any Central work begins
- [ ] All earlier releases completed (cleanup follow-through, lenient SAN, API-surface audit, auto-CHA, python-chess cross-validation)
- [ ] Rename decision resolved — clean-chess → DeepSquare or final name. Once published, the artifactId is permanent
- [ ] Every task that surfaces during the prerequisite releases has been addressed (re-evaluate this list at the moment of starting; the bar is "library is mature")

### Sonatype Central Portal setup
- [ ] Create Sonatype Central account at https://central.sonatype.com, sign in via GitHub
- [ ] Verify the `io.github.dlbbld` namespace (auto-verified for GitHub-signed-in users — no domain needed)
- [ ] Generate a GPG key, publish it to a public keyserver (e.g. `keyserver.ubuntu.com`), record the keyID
- [ ] Configure `~/.m2/settings.xml` with Sonatype Portal credentials and GPG passphrase

### `pom.xml` — Central-required metadata
- [ ] `<groupId>` → `io.github.dlbbld` (currently `com.github.dlbbld`, the JitPack convention)
- [ ] `<version>` → strict semver (`4.x` → `4.x.0`)
- [ ] Add `<name>`, `<description>`, `<url>` (link to GitHub repo)
- [ ] Add `<licenses>` block (GPL v3, with full URL)
- [ ] Add `<developers>` block
- [ ] Add `<scm>` block (`connection`, `developerConnection`, `url`)

### `pom.xml` — required plugins
- [ ] `central-publishing-maven-plugin` (the new Sonatype Portal plugin — *not* the deprecated `nexus-staging-maven-plugin` / OSSRH that older tutorials still document)
- [ ] `maven-gpg-plugin` for artifact signing
- [ ] `maven-javadoc-plugin` to produce a javadoc jar (`maven-source-plugin` is already present)

### JAR-content audit at publish time
Whatever ships in the first Central artifact is in the public record forever. Re-audit at publish time.

- [ ] Re-audit `src/main/resources` end-to-end: anything developer-facing, test-only, or environment-specific should not ship
- [ ] Re-audit `src/main/java` for any utility classes that should have been package-private rather than public (folds the residual API-surface work in if not already done by the API-surface release)
- [ ] (The test-fixture message keys are handled in the cleanup follow-through release; this audit is the safety net for anything similar that surfaces between now and publish)

### First publish + workflow
- [ ] Update README: drop the JitPack `<repositories>` block, leave only the plain Maven dependency snippet (no extra repository declarations needed for Central)
- [ ] Drop the JitPack URL and any related framing from README and other docs
- [ ] First publish via the Central Portal — staged release, manual approval the first time
- [ ] Verify the artifact appears at https://central.sonatype.com/artifact/io.github.dlbbld/...
- [ ] Document the per-release workflow (version bump → tag → `mvn deploy` → Portal release) in `setup.md` under a new "Releasing" section, or in a dedicated `release.md`

### Post-publish
- [ ] Decide whether JitPack stays available in parallel (free, harmless) or should be deprecated by removing the JitPack publish hook
- [ ] (Optional) Add a Maven Central status badge to the README

---

## Backlog — captured but unscheduled

Items here are not assigned to any release. Captured so they don't get lost; revisit if/when scope or motivation aligns.


---

