# clean-chess — Specification

This document captures decisions, contracts, and deviations from the FIDE Laws of Chess and the PGN standard that are not obvious from the code itself. It is written for readers who already know the project's purpose (a Java chess library covering rules, board state, FEN, SAN, PGN, repetition, draw claims, unwinnability, etc.) and want to understand the *whys* and *boundaries*.

---

## Companion documents

This file is the project's specification root. As individual concerns grow large enough to warrant their own document, they may be moved out of this file into `docs/` and linked from here.

---

## PGN

### Commentary contract

Applies to:

- `PgnFile.leadingCommentary()` — commentary before the first move (also called "pre-game" / "root-node" commentary; see python-chess's `Game.comment` and Lichess's leading-commentary support).
- `PgnHalfMove.commentary()` — commentary attached to a half-move.

Both fields are typed as `PgnCommentary` (a value-object record). Construction validates the contract; once a `PgnCommentary` instance exists, its content is guaranteed to satisfy the contract. Downstream code — including the exporter — does not validate again.

#### Forbidden — grammar

The wrapped string must contain neither `{` nor `}`. These are the brace-comment delimiters; including either in the content would corrupt the grammar on export (and would never appear in content emitted by the parsers, which handle braces at tokenisation time).

#### Forbidden — Unicode categories

| Category | What it covers | Why forbidden |
|---|---|---|
| `Cc` (`Character.CONTROL`) | ASCII / C0 / C1 control characters | They serve no purpose in commentary content. **Exception:** `\t` (U+0009), `\n` (U+000A), and `\r` (U+000D) are explicitly permitted. |
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

The three explicitly permitted control characters (`\t`, `\n`, `\r`) and CRLF (`\r\n`) round-trip verbatim through both parsers and the exporter — no normalisation, no substitution.

#### Why this contract

The PGN standard restricts non-printing characters from *string tokens* (tag values), but is silent on non-printing characters inside `{...}` commentary. Multiple chess tools — python-chess, Lichess — preserve tabs and line breaks in commentary verbatim. We follow that convention, with sanity checks against malformed Unicode (lone surrogates, unassigned code points) and obviously inappropriate content (other control characters, private-use code points). The result is permissive enough for legitimate text and strict enough to surface data corruption at the model boundary.

#### Sources of `PgnCommentary` values

- **`StrictPgnParser`** — parsed brace content goes directly through the constructor. The parser preserves source bytes verbatim; the value object validates per the contract above.
- **`LenientPgnParser`** — same. Lenient is identical to strict with respect to commentary *content*; lenient differs only on the structural rules already covered (R1..R4 — unclosed brace, nested brace, stray close, brace at SAN-expected position).
- **Programmatic API** — any caller invoking the constructor directly sees a `PgnCommentaryValidationException` (extends `UsageException`) on contract violation, with the offending character's index and Unicode category named in the message.

#### Round-trip semantics

For any input that satisfies the contract, `parse → export → parse` produces an equal model on both sides for short comments (any length up to one-physical-line in the export). For long comments that get wrapped at `MAX_LINE_LENGTH`, the round-trip is currently *not* byte-stable because `PgnUtility.calculateWrappedLines` may insert a newline at a space boundary inside `{...}`. This is tracked as a follow-up (brace-aware wrap) — when the wrap stops breaking inside braces, byte-stability is restored for arbitrary lengths.

### Open follow-ups

- **Brace-aware wrap.** `PgnUtility.calculateWrappedLines` currently splits on space without awareness of `{...}` regions. For long single-line commentary, this transforms internal spaces into newlines, producing valid (per the new contract) but content-different output on round-trip. Fix: don't break inside `{...}`. Mirrors python-chess's "long comment produces a long line, accepting the 80-char file-export-format guideline as a soft target." Tests `testFromImportStrictLong` and `testFromImportLenientLong` are `@Disabled` until this lands.
- **Move-number-after-commentary (T-002).** PGN spec §8.2.2 case 1 requires emitting `N...` before a Black move when commentary intervenes between the previous White move and this Black move. Strict parser must require the indicator on input; lenient must accept both forms (with and without). Export must always emit it (mirroring python-chess's `force_movenumber` flag). Not yet implemented.
- **Spec-compliant brace-comment parsing (T-003).** The PGN spec says *"a left brace character appearing in a brace comment loses its special meaning and is ignored"* — i.e., `{` inside a comment is content, not a nested-comment error. Both parsers currently reject this case. Open question on the value object's policy for `{` in content if/when this lands.
- **Rename `leadingCommentary` → `preGameCommentary` (T-004).** Mostly mechanical. The current name is fine but `preGameCommentary` reads more clearly and matches the conceptual position (before the game starts).
