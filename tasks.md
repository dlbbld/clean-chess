# Tasks

Order within each section is the source of truth. Completed tasks move to **Done** at the bottom.

---

## Current release — PGN headers treatment and lenient FEN validation

### Separate PGN parse, semantics, and export — honour the spec's archival vs non-archival split

#### The problem
Today's PGN pipeline conflates **four distinct jobs** into one path:
1. **Parsing** — should preserve what the user gave (tag presence/absence, FEN without SetUp, missing Result, unknown tags, tag order).
2. **Validation** — should *report* forgiven / deficient items to the consumer (already done via `LenientPgnParserValidationResult.sanForgivenItems()` for SAN deviations; needs the same for tag-level issues).
3. **Archival export** — produces a PGN-spec §8.1.1-conformant artifact: completed Seven Tag Roster, `SetUp "1"` when `FEN` is present, `Result` synthesised from termination marker (or `*` if neither), canonical SAN, canonical tag order, redundant initial-position `FEN`/`SetUp` dropped.
4. **Semantic export** — re-emits the parse model as given: same tags, same values, same Result presence/absence, same termination-marker presence/absence — without inventing what the user didn't give. Movetext is in canonical SAN (canonicalised at parse time) and formatting is normalised (single-space tag brackets, standard line wrapping). The default.

Three over-reaches in the current code, all stemming from treating archival storage as a *requirement* rather than an *export mode*:

**Over-reach 1 — both parsers normalise into the model.** `Collections.sort(tagList)` reorders user-provided tags; `removeFenIfInitial` strips `FEN`/`SetUp` if they describe the initial position. Neither belongs in parsing; both are archival concerns.

**Over-reach 2 — the lenient parser fabricates into the model.** `TagPlaceHolderUtility` writes `?` STR placeholders, `fixTagListForResultIfRequired` synthesises a `Result` tag from the termination marker, `fixTagListForSetUpIfRequired` adds `SetUp "1"` when `FEN` lacked it. A `parse → write` round-trip then does not return the user's PGN.

**Over-reach 3 — the strict parser requires the full Seven Tag Roster.** PGN spec §8.1.1 introduces STR as required *"for archival storage of PGN data"* — not for general spec-compliant PGN. Strict parsing should still require what is genuinely mandatory at the format/semantic level (Result tag presence and value, SetUp/FEN coupling) but should not enforce archival-only mandates (Event/Site/Date/Round/White/Black). After this change, a strict-but-non-archival PGN — e.g. four tags only (Result + a few extras) — parses through `StrictPgnParser` cleanly. Archival output remains opt-in via `WriteMode.ARCHIVAL`.

The principle: **parse preserves, validation reports, archival export normalises and fills, semantic export echoes.** The library's default posture is honest preservation; archival storage is a mode the caller asks for, not a tax the parser levies.

#### Concrete cases under the new model
1. **Missing STR tags** — both parsers accept the input; the seven-tag-roster mandate moves to ARCHIVAL export only. The lenient parser additionally reports each missing STR tag via `tagForgivenItems`. ARCHIVAL export adds `?` placeholders per PGN spec §8.1.1.
2. **`Result` tag absent** — strict parser rejects (Result remains a semantic essential — the termination marker must match it). Lenient parser accepts; tag absence is preserved and the termination marker (if present) is captured on a new `PgnFile.terminationMarker` field. Semantic export emits no Result tag and emits the marker only if present; archival export synthesises both.
3. **`FEN` without `SetUp`** (or vice-versa) — strict parser rejects (the coupling is a semantic essential). Lenient parser accepts; tag list preserved as given; `tagForgivenItems` records the deviation. Semantic export emits as-given; archival export adds `SetUp "1"`.
4. **Redundant `FEN` / `SetUp` describing the initial position** — both parsers preserve them (the user wrote them). Semantic export emits them as-given; archival export drops them (per the current `removeFenIfInitial` logic).
5. **Tag order** — both parsers preserve input order. Semantic export emits in input order; archival export sorts into canonical (STR-first) order.
6. **Tag-bracket whitespace** (`[White      "John Travolta"     ]`) — both export modes emit `[White "John Travolta"]`. Whitespace is formatting trivia; preserving it would require text-preserving mode.
7. **Move spelling** (`1. e2-e4 d5 2. Nb1c3 a6+` where `a6+` is not actually check) — both export modes emit canonical SAN. Canonicalisation happens at parse time via `replayBoardCanonicalizing`; the lenient parser reports each deviation via `ForgivenItem`.
8. **Text-preserving export** — re-emits the original source bytes byte-for-byte. **Out of scope for this work** — that is a source-preserving syntax tree, a heavier feature. Captured here only so the export-mode taxonomy is complete.

#### `WriteMode` — taxonomy
- `WriteMode.SEMANTIC` (default for both `writePgnFile(PgnFile, …)` and `writePgnFile(Board, …)`) — honest preservation. The library never silently invents content.
- `WriteMode.ARCHIVAL` — PGN spec §8.1.1 archival storage. Opt-in. STR filled, redundant tags dropped, tags sorted, termination marker always emitted, Result tag always present.

#### Strict parser — what it still requires (semantic essentials, not archival mandates)
- Single-space-separated tokens, no leading/trailing whitespace per line, etc. — the spec's import-format syntax. Unchanged.
- `Result` tag presence and valid value. The termination marker must match.
- `SetUp` / `FEN` semantic coupling: `SetUp "1"` ⇒ `FEN` present; `FEN` present ⇒ `SetUp "1"`. (Already enforced today via `validateTagFenValue`.)
- **Dropped:** the seven-tag-roster mandate. `Event`, `Site`, `Date`, `Round`, `White`, `Black` are archival-storage concerns only.

#### Action items
- [x] Add `terminationMarker: Optional<ResultTagValue>` (or null-allowed equivalent) field to `PgnFile` so the movetext signal stays separate from the header signal
- [x] Both parsers stop normalising the tag list: drop `Collections.sort(tagList)` and `removeFenIfInitial` from `LenientPgnParser.parseInternal` and `StrictPgnParser.parseInternal`
- [x] Lenient parser stops fabricating: drop `fixTagListForMissingSevenTagRosterTags`, `fixTagListForResultIfRequired`, `fixTagListForSetUpIfRequired`
- [x] `reconcileResult` keeps its consistency check (Result tag value vs termination marker value must match if both present) but stops mutating the tag list; the marker is captured on `PgnFile.terminationMarker`
- [x] Strict parser: drop `validateSevenTagRoster` (the STR mandate). Keep `validateResultTagValue` (Result still required at strict level) and `validateTagSetUpValue` (SetUp/FEN coupling)
- [x] Strict parser: refine the `TAG_NOT_ALL_REQUIRED_TAGS_SET` error code — either rename to `TAG_RESULT_MISSING` (since Result is now the only required STR tag at strict level) or repurpose with a narrower message
- [x] Introduce `ForgivenTagItem` record + `ForgivenTagItemCode` enum + `tagForgivenItems()` channel on `LenientPgnParserValidationResult`, mirroring the existing `sanForgivenItems()` shape
- [x] Lenient parser emits tag-level forgiven items: `STR_TAG_MISSING` (one per missing STR tag), `RESULT_TAG_MISSING_BUT_TERMINATION_MARKER_PRESENT`, `RESULT_TAG_AND_TERMINATION_MARKER_BOTH_MISSING`, `SETUP_TAG_MISSING_BUT_FEN_PRESENT`, `SETUP_TAG_PRESENT_BUT_FEN_MISSING`, `FEN_AND_SETUP_DESCRIBE_INITIAL_POSITION` — sharpen the list during implementation
- [x] Introduce `WriteMode { SEMANTIC, ARCHIVAL }` and `PgnWriter` overloads: `writePgnFile(PgnFile, Path)` defaults to SEMANTIC; `writePgnFile(PgnFile, Path, WriteMode)` explicit; same shape for `(Board, …)` overloads
- [x] Archival export helper: STR fill, `SetUp "1"` fill, Result tag fill from termination marker, tag sort, drop `FEN`/`SetUp` if they describe initial position; reference PGN spec §8.1.1 in the helper's class javadoc
- [x] Semantic export path: emit tags in input order, omit termination marker if `terminationMarker.isEmpty()`, normalise tag-bracket whitespace, canonical SAN (already canonicalised at parse time)
- [x] `PgnCreate.createPgnFile(Board)` produces a `PgnFile` with no STR fabrication (empty tag list aside from `FEN` if non-initial); `terminationMarker` set from the board's game-status-derived result; STR fabrication moves to the archival export helper
- [x] Audit production callers for "result is `*`?" checks (`ResultTagValue.ONGOING` has 2 src/main hits today — small surface): distinguish "Result tag absent" from "ongoing"
- [x] Delete `TagPlaceHolderUtility` or move it into the archival-export helper, whichever leaves cleaner code
- [x] Document the contract in `specification.md`: four-jobs table (parse / validate / archival export / semantic export), cite PGN spec §8.1.1 when defining ARCHIVAL, note that text-preserving export is out of scope, document the strict parser's revised mandate (Result + SetUp/FEN coupling required; STR not required)
- [x] Test fixtures: a "deficient" PGN (missing STR, no Result tag, `FEN` without `SetUp`, weird whitespace, `e2-e4`-style moves, bogus check suffixes). Semantic export → equals the same input with normalised whitespace and canonical SAN, **no fabricated tags**. Archival export → equals a fully spec-compliant form with STR filled, `SetUp "1"` added, `Result "*"` synthesised, canonical SAN
- [x] Test fixtures: a strict-but-non-archival PGN (Result + a couple of extras, no STR) — must parse cleanly through `StrictPgnParser`
- [x] `TestPgnExportBoard.checkTags` rewritten: drop STR-presence assertions (they were testing the fabrication, not anything chess-meaningful); add a parallel `TestPgnExportBoardArchival` if a dedicated archival-fill test is wanted
- [x] `CHANGELOG.md` entry under the next release

#### Notes for whoever picks this up
- The `LenientPgnParserValidationResult.sanForgivenItems()` channel is the right pattern for SAN-level forgiven items. The same pattern is extended with `tagForgivenItems()` so consumers can see which tag-level deviations the lenient parser accepted.
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
- [x] Settle the diagnostic taxonomy with the arbiter lens — add/remove codes based on real-world FEN deviations
- [x] Define `ForgivenFenItemCode` enum + `ForgivenFenItem` record + `LenientFenParserValidationResult` record
- [x] Implement `LenientFenParser` as a normaliser that produces a canonical FEN string + forgiven items list, then delegates to `FenParserRaw` (and optionally `FenParserAdvanced`)
- [x] `LenientFenParser.parseText` vs `validateText`: same pipeline, payload vs diagnostics-only
- [x] `LenientFenParserValidationException extends UsageException` for unrecoverable input
- [x] `Board` opt-in entry point for lenient FEN; `new Board(String)` stays strict
- [x] Decide: does `LenientPgnParser` use lenient FEN when reading the `FEN`/`SetUp` PGN tag, or stay strict on the tag? Lean: lenient, for consistency with movetext leniency
- [x] One test fixture per forgiven code; one end-to-end "deficient FEN" fixture; round-trip test (lenient parse → canonical FEN → strict parse) for each
- [x] Document the contract in `specification.md` — explicit table of strict-vs-lenient × raw-vs-advanced
- [ ] Update README "Lenient PGN parser" framing — the project now has lenient parsers for all three input languages

#### Open design questions
- Castling normalisation scope — KQkq-ordering deviations only, or also X-FEN / Shredder-FEN (Chess960) castling notation? Lean: KQkq-ordering only; X-FEN is a separate feature.
- Order of forgiven items in the result — left-to-right (token position) or grouped by code? PGN does left-to-right; mirror.

### FEN parser tier audit
Two FEN parsers exist in main: `FenParserRaw` (lexical only, one regex) and `FenParserAdvanced` (structural + rule-consistency). Boundaries were unclear and the docs overclaimed what each tier proves (the "no real game could reach" wording was softened in 6.0.0, but the tier contracts are still not written down precisely). A third class, `FenParserAdvancedFurther`, lives under `src/test` (not main) and runs only via its own test class — its disposition is captured as the closing task below.
- [x] Audit `FenParserRaw` and `FenParserAdvanced` — list what each actually validates vs what its package-level docs claim
- [x] Document each tier's contract precisely in `specification.md` (the strict-vs-lenient × raw-vs-advanced table from the lenient-FEN section is the right home — extend it with the per-tier contract column)

### Promote `FenParserAdvancedFurther` consistency check into strict, drop the class
The test-only `FenParserAdvancedFurther` enforces two semantic invariants on a `Fen`:
1. **Half-move clock vs full-move number consistency** — `halfMoveClock <= 2 * (fullMoveNumber - 1) + (havingMove == BLACK ? 1 : 0)`. A FEN like `... 15 1` (15 half-moves played, claiming move 1) is genuinely impossible in a single chess game.
2. **Full-move number = 1 ⇒ initial position (White) or one of the 20 after-first-half-move positions (Black)** — narrower, more debatable: many engines emit non-initial positions with `fullMoveNumber = 1` as a placeholder, which makes this branch unfriendly to real-world FEN.

The class was kept out of main because of the second branch's practical incompatibility with real-world exporters. With the lenient FEN parser landing alongside (preceding task), the resolution becomes clean: **strict enforces, lenient auto-corrects**.

- [x] Fold the half-move-clock-vs-full-move-number consistency check into `FenParserAdvanced` — it belongs with the other structural invariants (no impossible checks, no pawn on rank 1/8, castling rights consistent with piece placement, etc.)
- [x] Decide on branch 2 (`fullMoveNumber == 1` constraint): **dropped** — too many real-world FEN exporters emit `fullMoveNumber = 1` for non-initial positions for this to be a productive strict rejection. *(Not yet documented in `specification.md` — see follow-up below.)*
- [x] Lenient FEN parser forgives the consistency-check failure: auto-correct `fullMoveNumber` up to `ceil(halfMoveClock / 2) + 1` (the minimum consistent value), surface as `HALF_MOVE_CLOCK_INCONSISTENT_WITH_FULL_MOVE_NUMBER` (or similarly named) forgiven item *(actually implemented with a reserve formula — `halfMoveClock` rounded up to the next multiple of ten — rather than the strict minimum, signalling a reconstructed placeholder)*
- [x] Delete `src/test/java/com/dlb/chess/test/fen/FenParserAdvancedFurther.java`, `TestFenParserAdvancedFurther.java`, `FenAdvancedFurtherValidationProblem.java`, `FenAdvancedFurtherValidationException.java`. The covered test cases migrate to `TestFenParserAdvanced` (for the consistency check) and to the lenient FEN test suite (for the forgiveness)
- [x] CHANGELOG: note the strict-parser invariant gain and the now-impossible "fullMoveNumber=1 + non-initial" position no longer being rejected at any level

##### Pre-tag follow-ups (docs-only)
- [ ] `specification.md` — note explicitly that the `FenParserAdvancedFurther` branch 2 (`fullMoveNumber == 1 ⇒ initial-or-after-first-move position`) was dropped entirely; only the half-move-clock consistency check moved into `FenParserAdvanced`.
- [ ] `README.md` — update the "Lenient PGN parser" framing to reflect that the project now has lenient parsers for all three input languages (SAN, PGN, FEN). Add a short pointer to `Board.fromFenLenient(String)`.

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

### Pawn-wall classifier — sound tri-state verdict

See [pawn-wall-soundness.md](pawn-wall-soundness.md) for the full design: tri-state `YES / NO / UNKNOWN` return, permanent-barrier principle (own pawns + pawn-attacked squares only — own pieces don't count), king-walk BFS, fixtures, implementation checklist, and the option of dropping the local heuristic once Auto-CHA is in place.

### Speed up `findHelpMate` — transposition key instead of FEN string for visited-position storage

The unwinnability `findHelpMate` search keys its visited-position set by `Board.fen()` — the full FEN string. On every node the FEN is re-serialised; on every lookup the string is re-hashed character by character; and the position is implicitly re-parsed when the next FEN is built for comparison. For a search that visits many positions, FEN serialisation is the hot path.

Replace the FEN string with a lightweight transposition key — a single `long` (or a small wrapper of two `long`s if collision-resistance matters) that fingerprints the position via Zobrist-style hashing or equivalent. Equality becomes a long compare, not a string compare, and there is no FEN round-trip in the loop.

- [ ] Decide on the key shape (`long` Zobrist hash, or wider key with collision guard)
- [ ] Implement on `Board` (or the dynamic-position carrier) — incrementally updated on each move rather than computed from scratch
- [ ] Swap `findHelpMate`'s visited-position store to use the new key
- [ ] Verify search speed improvement on representative unwinnability fixtures
- [ ] Confirm CHA-quick and CHA-full correctness unchanged

### Dynamic position should store the en passant capture target square, not just a boolean

The dynamic position today carries a `boolean` for en passant availability — "possible / not possible." Functionally this is correct: the flag is reset after every pawn capture or pawn move, so the rule (en passant is legal only on the very next half-move after a double-step pawn advance) is enforced. But semantically it is wrong: en passant rules are square-specific, not abstract. The actual chess rule, and what FEN encodes (`e3`, `d6`, …), is the *square* the capturing pawn would land on. The implementation works because the target square is reconstructed elsewhere from the last half-move; storing it on the dynamic position would be the honest shape.

- [ ] Replace the `boolean` field with an `enPassantCaptureTargetSquare` field (using the existing square enum, with `NONE` for "no en passant available")
- [ ] Drop wherever the target square is currently reconstructed from the last half-move; the dynamic position becomes the source of truth
- [ ] Verify that `equals` / `hashCode` for dynamic-position comparison (used in threefold repetition) treat the square correctly — same piece arrangement with different en passant targets must remain non-equal per FIDE rules (already enforced in 6.0.0 via the `EnPassantCaptureRuleThreefold` removal)
- [ ] Folds naturally with the transposition-key task above: a square-valued field hashes more cleanly than a boolean tied to an out-of-band reconstruction

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

### Records carry data, not behavior — sweep for violations
The project rule (documented in `coding-conventions.md`): records carry data; domain logic that operates on them lives in dedicated utility / service classes. Permitted on a record: compact-constructor validation, `Comparable` when ordering is intrinsic, and language-provided `equals` / `hashCode` / `toString`. Domain-operation methods are not.

Surfaced by the unused-code-detector pass on `StaticPosition`: the record carries multiple non-data methods — `createChangedPosition` (three overloads), `isPawn`, `isOwnPawn`, `isOpponentPawn`, `isOwnKing`, `isOpponentKing`, almost certainly more. Some have only test callers (suggesting test scaffolding), some have production callers, one (`isOwnKing`) has zero callers anywhere.

- [ ] Catalog every non-permitted member on `StaticPosition` and assign a disposition per member: delete (no callers anywhere), move to a test-side helper that **takes** a `StaticPosition` rather than duplicating it (test-only callers), or move to a `StaticPositionUtility` (production callers).
- [ ] Sweep every record under `src/main/java` for the same pattern. Records to check include at least `Fen`, `Tag`, `PgnFile`, `LegalMove`, `MoveSpecification`, `StaticPosition`, plus any other top-level `record` declarations under `src/main`.
- [ ] Apply the dispositions; verify only the permitted member shapes remain on each record.
- [ ] Naturally folds into the API-surface reduction release, since most "move to utility" relocations open the door to making the utility itself package-private.

---

## Obsolete

Items deemed no longer worth pursuing. Captured so the decision is visible.

### Replace `EnumConstants` constant interface
`com.dlb.chess.common.constants.EnumConstants` is a `public interface` whose only purpose is to expose ~90 `public static final` aliases for `Square.*`, `Side.*`, `Piece.*`, `PieceType.*`, `Rank.*`, `File.*` so implementing classes inherit them unqualified. This is the classic "constant interface" anti-pattern (Effective Java item 22): interfaces should describe a contract/behavior, not be a convenience-inheritance vehicle for constants. The mechanism reads as beginner Java and leaks an internal vocabulary choice into the public type surface — `ChessBoard extends EnumConstants` is the clearest symptom (the chess contract has nothing to do with how implementers prefer to spell `Square.E4`). Used by 43 files under `src/main` plus tests.

Replacement strategy options, depending on intended audience:
- public-API constants: `public final class EnumConstants` with `public static final` fields and a private constructor (callers `import static`)
- internal-only: make package-private and split closer to where they belong (domain-grouped, e.g. `BoardSquares`, `PieceLetters`)
- derived enum collections: prefer local `EnumSet` / `ImmutableSet` factories in the utility that needs them, or dedicated package-private constants classes by domain

- [ ] Pick a replacement strategy (default lean: package-private utility class with `import static`, since the constants are internal vocabulary and the audit reduces public surface anyway)
- [ ] Drop `extends EnumConstants` from `ChessBoard` regardless of strategy — the interface should not carry constants
- [ ] Convert the 43 src/main call sites + tests to static imports
- [ ] Folds naturally into the API-surface reduction release, since most "move to utility" relocations open the door to making the utility itself package-private.

---
