package zielu.gittoolbox.ui.projectview;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBusConnection;
import git4idea.repo.GitRepository;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jetbrains.annotations.NotNull;
import zielu.gittoolbox.cache.PerRepoInfoCache;
import zielu.gittoolbox.cache.PerRepoStatusCacheListener;
import zielu.gittoolbox.cache.RepoInfo;
import zielu.gittoolbox.cache.VirtualFileRepoCache;
import zielu.gittoolbox.changes.ChangesTrackerService;
import zielu.gittoolbox.config.ConfigNotifier;
import zielu.gittoolbox.config.GitToolBoxConfig2;
import zielu.gittoolbox.ui.util.AppUiUtil;

class ProjectViewSubscriber implements ProjectComponent {
  private final AtomicBoolean active = new AtomicBoolean(true);
  private final Project project;
  private final MessageBusConnection connection;

  ProjectViewSubscriber(@NotNull Project project) {
    this.project = project;
    connection = this.project.getMessageBus().connect(project);
    connection.subscribe(ConfigNotifier.CONFIG_TOPIC, new ConfigNotifier() {
      @Override
      public void configChanged(GitToolBoxConfig2 previous, GitToolBoxConfig2 current) {
        refreshProjectView();
      }
    });
    connection.subscribe(PerRepoInfoCache.CACHE_CHANGE, new PerRepoStatusCacheListener() {
      @Override
      public void stateChanged(@NotNull final RepoInfo info, @NotNull final GitRepository repository) {
        refreshProjectView();
      }

      @Override
      public void evicted(@NotNull Collection<GitRepository> repositories) {
        refreshProjectView();
      }
    });
    connection.subscribe(VirtualFileRepoCache.CACHE_CHANGE, this::refreshProjectView);
    connection.subscribe(ChangesTrackerService.CHANGES_TRACKER_TOPIC, changesCount -> refreshProjectView());
  }

  private void refreshProjectView() {
    if (active.get()) {
      AppUiUtil.invokeLater(project, () -> {
        if (active.get()) {
          ProjectView.getInstance(project).refresh();
        }
      });
    }
  }

  @Override
  public void projectClosed() {
    active.compareAndSet(true, false);
  }
}
