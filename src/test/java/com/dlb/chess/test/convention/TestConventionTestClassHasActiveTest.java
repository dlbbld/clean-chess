package com.dlb.chess.test.convention;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ConfigurationConstants;

/**
 * Convention test: every Java class under {@code src/test/java} whose simple name starts with
 * {@code Test} must contain at least one {@code @Test} annotation.
 *
 * <p>Rationale: the {@code Test} prefix at every import site is a promise — readers expect a
 * runnable JUnit class. A {@code Test}-named class with no {@code @Test} is either a misplaced
 * utility (which should be renamed to {@code Generate*}/{@code Run*}/etc.) or a class whose
 * test methods got their annotations stripped during a refactor and silently stopped running.
 * Either is drift worth catching at setup time.
 *
 * <p>Pairs with the implicit convention that {@code Generate*} classes must NOT carry
 * {@code @Test} (they are {@code main}-driven utilities). Together the two enforce a clean
 * shape: a class is runnable as a JUnit test iff its simple name starts with {@code Test}.
 *
 * <p>Detection is a literal regex {@code @Test\b} against the source file's contents — strict,
 * so {@code @TestFactory}/{@code @TestTemplate}/{@code @ParameterizedTest}/{@code @RepeatedTest}
 * do not satisfy the invariant. The codebase uses only plain {@code @Test} today; if a future
 * test introduces one of those forms, broaden the pattern at that point so the convention
 * keeps matching reality.
 *
 * <p>The test class itself satisfies the invariant — its name starts with {@code Test} and it
 * has the {@code @Test} above {@link #everyTestPrefixedClassDeclaresAtLeastOneTestMethod()}.
 */
class TestConventionTestClassHasActiveTest {

  private static final Path TEST_JAVA_ROOT = NonNullWrapperCommon
      .resolve(ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH, "src/test/java");

  private static final String REQUIRED_NAME_PREFIX = "Test";

  private static final Pattern TEST_ANNOTATION = Pattern.compile("@Test\\b");

  @SuppressWarnings("static-method")
  @Test
  void everyTestPrefixedClassDeclaresAtLeastOneTestMethod() throws IOException {
    final List<String> violations = new ArrayList<>();

    try (Stream<Path> paths = Files.walk(TEST_JAVA_ROOT)) {
      paths.filter(Files::isRegularFile).filter(p -> p.toString().endsWith(".java"))
          .filter(p -> p.getFileName().toString().startsWith(REQUIRED_NAME_PREFIX)).forEach(p -> {
            final String contents = readSource(p);
            if (!TEST_ANNOTATION.matcher(contents).find()) {
              violations.add(TEST_JAVA_ROOT.relativize(p).toString().replace('\\', '/'));
            }
          });
    }

    if (!violations.isEmpty()) {
      final StringBuilder report = new StringBuilder().append(violations.size())
          .append(" Test-prefixed class(es) under src/test/java contain no @Test annotation:\n");
      for (final String violation : violations) {
        report.append("  ").append(violation).append('\n');
      }
      fail(report.toString());
    }
  }

  private static String readSource(Path javaFile) {
    try {
      return Files.readString(javaFile);
    } catch (final IOException e) {
      throw new IllegalStateException("Failed to read " + javaFile, e);
    }
  }
}
