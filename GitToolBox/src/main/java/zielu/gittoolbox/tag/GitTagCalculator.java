package zielu.gittoolbox.tag;

import com.google.common.base.Preconditions;
import com.intellij.execution.process.ProcessOutputType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcs.log.Hash;
import git4idea.commands.Git;
import git4idea.commands.GitCommand;
import git4idea.commands.GitCommandResult;
import git4idea.commands.GitLineHandler;
import git4idea.commands.GitLineHandlerListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class GitTagCalculator {
  private final Logger log = Logger.getInstance(getClass());
  private final Project project;

  private GitTagCalculator(@NotNull Project project) {
    this.project = project;
  }

  public static GitTagCalculator create(@NotNull Project project) {
    return new GitTagCalculator(Preconditions.checkNotNull(project));
  }

  public List<String> tagsForHead(@NotNull VirtualFile gitRoot) {
    return tagsForCommitish(gitRoot, "HEAD");
  }

  public List<String> tagsForCommit(@NotNull VirtualFile gitRoot, @NotNull Hash hash) {
    return tagsForCommitish(gitRoot, hash.asString());
  }

  private List<String> tagsForCommitish(@NotNull VirtualFile gitRoot, @NotNull String commitish) {
    //git tag -l --points-at <ref or commit>
    List<String> tags = new ArrayList<>();
    GitLineHandler handler = new GitLineHandler(project, Preconditions.checkNotNull(gitRoot), GitCommand.TAG);
    handler.addParameters("-l", "--points-at", commitish);
    handler.setSilent(true);
    handler.addLineListener(new GitLineHandlerListener() {
      @Override
      public void onLineAvailable(String line, Key outputType) {
        if (ProcessOutputType.isStdout(outputType)) {
          String tag = StringUtils.trimToNull(line);
          if (tag != null) {
            tags.add(tag);
          }
        }
      }

      @Override
      public void processTerminated(int exitCode) {
      }

      @Override
      public void startFailed(Throwable exception) {
      }
    });
    log.debug("Tags for ", commitish, " are ", tags);
    GitCommandResult result = Git.getInstance().runCommandWithoutCollectingOutput(handler);
    return result.success() ? tags : Collections.emptyList();
  }
}
