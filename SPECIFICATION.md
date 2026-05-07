# clean-chess — Specification

This document captures decisions, contracts, and deviations from the FIDE Laws of Chess and the PGN standard that are not obvious from the code itself. It is written for readers who already know the project's purpose (a Java chess library covering rules, board state, FEN, SAN, PGN, repetition, draw claims, unwinnability, etc.) and want to understand the *whys* and *boundaries*.

---

## Companion documents

This file is the project's specification root. As individual concerns grow large enough to warrant their own document, they may be moved out of this file into `docs/` and linked from here.

---

## PGN

### Commentary contract

Applies to:

- `PgnFile.preGameCommentary()` — commentary before the first move (also called "leading" / "root-node" commentary; see python-chess's `Game.comment` and Lichess's leading-commentary support).
- `PgnHalfMove.commentary()` — commentary attached to a half-move.

Both fields are typed as `PgnCommentary` (a value-object record). Construction validates the contract; once a `PgnCommentary` instance exists, its content is guaranteed to satisfy the contract. Downstream code — including the exporter — does not validate again.

#### Forbidden — grammar

The wrapped string must not contain `}`. The closing brace is the brace-comment terminator; including it in content would corrupt the grammar on export.

The opening brace `{` is **allowed** in commentary content. Per PGN spec §8.2.5, "a left brace character appearing in a brace comment loses its special meaning"; both parsers consume an inner `{` as a literal content character (only `}` can close a brace comment), and the exporter writes it verbatim. The inner `{` round-trips unchanged.

#### Forbidden — Unicode categories

| Category | What it covers | Why forbidden |
|---|---|---|
| `Cc` (`Character.CONTROL`) | ASCII / C0 / C1 control characters | They serve no purpose in commentary content. **Exception:** `\t` (U+0009) and `\n` (U+000A) are explicitly permitted. `\r` (U+000D) is **forbidden** — see [Newline handling](#newline-handling) below. |
| `Cs` (`Character.SURROGATE`) | Lone high or low surrogate code points | UTF-16 encoding artefacts, not real characters. A `String` containing a lone surrogate is malformed. |
| `Cn` (`Character.UNASSIGNED`) | Code points not assigned by the Unicode Standard | Including the noncharacter ranges (e.g. U+FDD0–U+FDEF, U+FFFE, U+FFFF) and any code point Unicode has not yet allocated. |
| `Co` (`Character.PRIVATE_USE`) | U+E000–U+F8FF and the Plane 15/16 private-use areas | Reserved for private agreement (custom fonts, in-house symbols). Chess commentary should not depend on these. |

#### Allowed

All other Unicode categories, including:

- Letters (Lu, Ll, Lt, Lm, Lo) — Latin, Cyrillic, CJK, Arabic, etc.
- Marks (Mn, Mc, Me) — combining diacritics.
- Numbers (Nd, Nl, No) — digits, Roman numerals, fractions.
- Punctuation (Pc, Pd, Ps, Pe, Pi, Pf, Po) — period, comma, dashes, quotes, etc.
- Symbols (Sm, Sc, Sk, So) — math, currency, modifier, other (including the Unicode chess pieces U+2654–U+265F).
- Space separators (Zs) — regular space, NBSP.
- Line / paragraph separators (Zl, Zp) — U+2028, U+2029. Treated as legitimate line-break-class content.
- Format characters (Cf) — zero-width joiner, BOM, bidi marks, soft hyphen. Invisible but legitimate in non-Latin scripts and emoji sequences.
- Supplementary characters (above U+FFFF) — emoji, rare scripts. Validator iterates by code point, not by `char`, so surrogate pairs forming a valid supplementary code point are accepted.

The two explicitly permitted control characters (`\t`, `\n`) round-trip verbatim through both parsers and the exporter — no substitution. `\r` is normalised to `\n` at the parser input boundary; see [Newline handling](#newline-handling).

#### Why this contract

The PGN standard restricts non-printing characters from *string tokens* (tag values), but is silent on non-printing characters inside `{...}` commentary. Multiple chess tools — python-chess, Lichess — preserve tabs and line breaks in commentary verbatim. We follow that convention, with sanity checks against malformed Unicode (lone surrogates, unassigned code points) and obviously inappropriate content (other control characters, private-use code points). The result is permissive enough for legitimate text and strict enough to surface data corruption at the model boundary.

#### Sources of `PgnCommentary` values

- **`StrictPgnParser`** — parsed brace content goes directly through the constructor. The parser preserves source bytes verbatim; the value object validates per the contract above.
- **`LenientPgnParser`** — same. Lenient is identical to strict with respect to commentary *content*; lenient differs only on the structural rules already covered (R1..R4 — unclosed brace, nested brace, stray close, brace at SAN-expected position).
- **Programmatic API** — any caller invoking the constructor directly sees a `PgnCommentaryValidationException` (extends `UsageException`) on contract violation, with the offending character's index and Unicode category named in the message.

#### Round-trip semantics

For any input that satisfies the contract, `parse → export → parse` produces an equal model on both sides for short comments (any length up to one-physical-line in the export). For long comments that get wrapped at `MAX_LINE_LENGTH`, the round-trip is currently *not* byte-stable because `PgnUtility.calculateWrappedLines` may insert a newline at a space boundary inside `{...}`. This is tracked as a follow-up (brace-aware wrap) — when the wrap stops breaking inside braces, byte-stability is restored for arbitrary lengths.

### Move-number indicator after intervening commentary

PGN spec §8.2.2 case 1 requires an explicit `N...` move-number indicator before a Black move when commentary intervenes between the previous White move and that Black move. Without the indicator, a reader scanning the move-text cannot disambiguate "trailing commentary on White's move, then Black's response" from "leading commentary on a new full-move pair, then White's move."

Applies to:

- The strict parser (`StrictPgnParser`).
- The lenient parser (`LenientPgnParser`).
- The exporter (`PgnCreate`).

#### Parser rules

| Parser | Indicator present | Indicator missing |
|---|---|---|
| Strict | Accepted. The full-move number must equal the current full-move number. A wrong number (e.g. `2...` where `1...` was expected) surfaces with the same dedicated category as a missing indicator. | Rejected with `MOVETEXT_MOVE_NUMBER_REQUIRED_AFTER_COMMENTARY`. |
| Lenient | Accepted. The indicator is consumed and discarded — same code path as any other move-number token. | Accepted. Real-world PGN sources frequently omit the indicator and the lenient parser must tolerate that. |

The rule fires only when commentary actually intervened on the previous (White) move. A plain `1. e4 e5` continues to parse without any indicator. Commentary on a Black move does not trigger the rule — the next move is White's, which already requires its own move-number for an unrelated reason.

#### Exporter behavior

`PgnCreate.calculateMovetextWithoutGameTerminationMarker` always emits the `N...` indicator before a Black move when the previous half-move had attached commentary. The exporter never produces output that the strict parser would reject for this reason.

This mirrors python-chess's `force_movenumber` flag: tools that import lenient input then re-export get back a strict-valid PGN.

#### Round-trip semantics

- **Strict input → export → strict re-parse.** Byte-stable.
- **Lenient input without indicator → export → strict re-parse.** Not byte-stable on the move-text bytes (the export adds the indicator), but the parsed model is equal on both sides.
- **Lenient input with indicator → export → strict re-parse.** Byte-stable.

### Newline handling

All PGN text handled by the library uses **LF (`\n`) as the canonical newline**, internally and on export. This applies uniformly to tag values, movetext, and `{...}` commentary content.

#### Input normalisation

Both parsers (`StrictPgnParser`, `LenientPgnParser`) normalise the input string at the constructor before tokenisation:

- `\r\n` (CRLF) → `\n`
- standalone `\r` (lone CR) → `\n`
- `\n` → `\n` (no change)

Normalisation is implemented in `NewlineNormalization.toLf(String)`. Inputs that already use only LF are returned unchanged.

#### Model invariant

`PgnCommentary` forbids `\r` in its content — direct construction with `\r` throws `PgnCommentaryValidationException`. The invariant is symmetric with the parser-input rule: the model never sees `\r`, regardless of whether the value came from a parser or from a direct API call.

#### Exporter

`PgnCreate` writes only `\n`. CRLF input round-trips to LF in the exported PGN. There is no platform-dependent line-ending behaviour.

#### Round-trip semantics

For an input containing CRLF or lone CR in commentary content:

- `parse(input)` produces a model with `\n` only (input CR/CRLF normalised).
- `export(parse(input))` writes `\n` only.
- `parse(export(parse(input))) ≡ parse(input)` — equality on both sides because both go through the same normalisation.

The round-trip is **not byte-stable** for inputs that used CR or CRLF; it is byte-stable for inputs that already used LF only.

### Brace-aware wrap

`PgnUtility.calculateWrappedLines` treats each `{...}` region as a single atom — spaces inside commentary are content, never wrap candidates. A brace region that exceeds `MAX_LINE_LENGTH` is emitted on its own line rather than broken. This matches python-chess's "long comment produces a long line, accepting the 80-char export-format guideline as a soft target."

With this in place, `parse → export → parse` is byte-stable for arbitrarily long commentary content (subject to the CR/CRLF normalisation noted under "Newline handling").

### Comment style (T-006)

A first pass over the PGN parser/exporter and their tests trimmed Javadoc and inline comments to the minimum that survives ordinary refactors. The rule going forward:

- **Keep:** decisions, trade-offs, spec references (`PGN spec §8.2.5`), subtle invariants, counter-intuitive behaviour.
- **Drop:** restating the code, narration of implementation steps, double-bookkeeping of test intent, and especially **filesystem paths or other physically-mirrored facts in prose** — those duplicate information the code already carries and silently rot when the code is reorganised.

No fixed line-count rule; a longer comment is fine when the content is genuinely irreplaceable. Rule of thumb: if an AI could regenerate the comment from the code, the comment is a maintenance liability.
