# Coding conventions

## Java style

We follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html), enforced with Checkstyle, with a small number of project-specific tightenings.

The active Checkstyle configuration is named *Google checks adapted* (referenced from [`.checkstyle`](.checkstyle)). It's the *Google Checks* preset that ships with the Eclipse Checkstyle plugin, with project-local edits.

### Project-specific tightenings

**Method names.** Pure camelCase is the default. The single exception is test methods with a tag prefix — a short alphanumeric label of letters followed by digits (`v01`, `r3`, `t003`), separated from the description by an underscore: `v01_pregameCommentaryOnly`. The underscore disambiguates the digit-letter boundary and marks the boundary between test metadata and test description.

Underscore between two camelCase phrases is **not** allowed: `postTermination_x` and `parseAgreesAcrossParsers_x` are rejected.

Enforced by:

```
^(?:[a-z]+[0-9]+_)?[a-z][a-z0-9][a-zA-Z0-9]*$
```

**JUnit method references.** Avoid hardcoded method names as string arguments in JUnit annotations (e.g. `@MethodSource("supplierName")`). Prefer the no-argument `@MethodSource` (which picks up a static method with the same name as the test) or `@ArgumentsSource(Provider.class)` — both are compiler-checked. The string form silently drifts from the method name under refactor and is only caught at test runtime. Allow only where the alternatives are materially worse.

## Eclipse compiler warnings

All code must compile under the project's Eclipse JDT compiler settings with **zero warnings**. Many diagnostics are configured as errors rather than warnings — null-annotation violations, unused imports, missing `@Override`, raw types, etc. Treat the warning ceiling as a hard rule, not a guideline: fix the cause, do not silence with `@SuppressWarnings` unless the reason is genuinely localized and documented inline.

## Setup for contributors — known gap

The two files that pin the rule sets locally are **not yet checked in to the repository**:

- The Checkstyle XML config (referenced from `.checkstyle` as `internal_config_1646660467605.xml`) lives inside the Eclipse Checkstyle plugin's local storage.
- The Eclipse JDT compiler preferences (`.settings/org.eclipse.jdt.core.prefs`) and formatter preferences (`.settings/org.eclipse.jdt.ui.prefs`) are not in `.settings/`.

Until these are exported and committed, an outside contributor cannot reproduce the same Checkstyle rules or the same compiler warning levels locally. This is an open task to address before broader collaboration.
