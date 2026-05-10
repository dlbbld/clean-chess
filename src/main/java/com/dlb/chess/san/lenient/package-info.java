/**
 * Lenient SAN parser. Accepts inputs the strict {@link com.dlb.chess.san.validate.StrictSanParser} pipeline rejects,
 * when those inputs uniquely identify a legal move and the deviation matches one of the supported tolerance categories
 * enumerated in {@link com.dlb.chess.san.enums.LenientSanValidationProblem}.
 *
 * <p>
 * Strategy is two-phase, canonical-first:
 * <ol>
 * <li>Try the strict pipeline as-is. If it accepts, the input was canonical SAN and the result has no forgiven items.
 * <li>If the strict pipeline rejects, apply pure-string shape normalization (case fixups, castling-zero, UCI / LAN
 * translation, explicit pawn letter, missing promotion equals, etc.), accumulating one forgiven item per detected
 * deviation.
 * <li>Re-run the strict pipeline on the normalized candidate. If it rejects with a recoverable diagnostic
 * (over-specification, capture-marker mismatch, terminal-marker mismatch), mutate the candidate, accumulate another
 * forgiven item, and retry. If the final rejection is non-recoverable, throw
 * {@link com.dlb.chess.san.exceptions.LenientSanParserValidationException} carrying the deepest underlying strict
 * reason and the partial forgiven-items list.
 * </ol>
 *
 * <p>
 * The strict pipeline is reused unchanged for all chess-validation logic; this package contains only the input-shape
 * transformations and the recovery loop.
 */
@NonNullByDefault
package com.dlb.chess.san.lenient;

import org.eclipse.jdt.annotation.NonNullByDefault;
