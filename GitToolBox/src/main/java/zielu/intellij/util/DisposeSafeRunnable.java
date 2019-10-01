package zielu.intellij.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jetbrains.annotations.NotNull;

public class DisposeSafeRunnable implements Runnable {
  private final Logger log = Logger.getInstance(getClass());
  private final Project project;
  @NotNull
  private final Runnable operation;

  public DisposeSafeRunnable(@NotNull Project project, @NotNull Runnable operation) {
    this.project = project;
    this.operation = operation;
  }

  @Override
  public void run() {
    try {
      if (!project.isDisposed()) {
        operation.run();
      }
    } catch (AssertionError error) {
      if (project.isDisposed()) {
        log.debug("Project already disposed", error);
      } else {
        log.error(error);
      }
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("operation", operation)
        .toString();
  }
}
