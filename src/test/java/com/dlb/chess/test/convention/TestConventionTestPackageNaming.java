package com.dlb.chess.test.convention;

import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ConfigurationConstants;
import com.dlb.chess.common.utility.FileUtility;

/**
 * Convention test: every Java file under {@code src/test/java} must declare a package starting with
 * {@code com.dlb.chess.test}.
 *
 * <p>
 * Rationale: a class in {@code src/test/java} is test-scope code by Maven contract. Forcing the package name to start
 * with {@code com.dlb.chess.test} makes that scope visible at every import site, so a future reader cannot mistake test
 * infrastructure for production code by name alone. The single-prefix invariant also keeps the source tree easy to
 * navigate.
 *
 * <p>
 * The test class itself satisfies the invariant — it lives in {@code com.dlb.chess.test.convention}.
 *
 * <p>
 * The package name is derived from the file's path under {@code src/test/java}; Java requires the {@code package}
 * declaration to match, so reading the path is equivalent to reading the declaration and avoids parsing the source.
 */
class TestConventionTestPackageNaming {

  private static final Path TEST_JAVA_ROOT = NonNullWrapperCommon
      .pathResolve(ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH, "src/test/java");

  private static final String REQUIRED_PACKAGE_PREFIX = "com.dlb.chess.test";

  @SuppressWarnings("static-method")
  @Test
  void everyTestSourceFileIsUnderRequiredPackagePrefix() {
    final List<String> violations = new ArrayList<>();

    for (final Path p : FileUtility.listAllFilesRecursively(TEST_JAVA_ROOT)) {
      if (!NonNullWrapperCommon.toString(p).endsWith(".java")) {
        continue;
      }
      final String packageName = derivePackageName(p);
      if (!REQUIRED_PACKAGE_PREFIX.equals(packageName) && !packageName.startsWith(REQUIRED_PACKAGE_PREFIX + ".")) {
        violations.add(packageName + "  in  " + NonNullWrapperCommon
            .replace(NonNullWrapperCommon.toString(NonNullWrapperCommon.pathRelativize(TEST_JAVA_ROOT, p)), '\\', '/'));
      }
    }

    if (!violations.isEmpty()) {
      final var report = new StringBuilder().append(violations.size())
          .append(" file(s) under src/test/java declare a package outside ").append(REQUIRED_PACKAGE_PREFIX)
          .append(":\n");
      for (final String v : violations) {
        report.append("  ").append(v).append('\n');
      }
      fail(report.toString());
    }
  }

  private static String derivePackageName(Path javaFile) {
    final var parent = javaFile.getParent();
    if (parent == null) {
      return "";
    }
    final Path relative = NonNullWrapperCommon.pathRelativize(TEST_JAVA_ROOT, parent);
    return NonNullWrapperCommon
        .replace(NonNullWrapperCommon.replace(NonNullWrapperCommon.toString(relative), '\\', '.'), '/', '.');
  }
}
