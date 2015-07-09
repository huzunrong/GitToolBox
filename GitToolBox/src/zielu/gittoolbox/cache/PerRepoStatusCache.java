package zielu.gittoolbox.cache;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.AccessToken;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.util.messages.Topic;
import git4idea.repo.GitRepository;
import git4idea.repo.GitRepositoryChangeListener;
import java.util.concurrent.ConcurrentMap;
import org.jetbrains.annotations.NotNull;
import zielu.gittoolbox.status.GitAheadBehindCount;
import zielu.gittoolbox.status.GitStatusCalculator;

public class PerRepoStatusCache implements GitRepositoryChangeListener, Disposable {
    public static Topic<PerRepoStatusCacheListener> CACHE_CHANGE = Topic.create("Status cache change", PerRepoStatusCacheListener.class);

    private final Logger LOG = Logger.getInstance(getClass());

    private final ConcurrentMap<GitRepository, CachedStatus> behindStatuses = Maps.newConcurrentMap();
    private final Project myProject;
    private final GitStatusCalculator myCalculator;
    private final MessageBusConnection myRepoChangeConnection;

    private PerRepoStatusCache(@NotNull Project project) {
        myProject = project;
        myCalculator = GitStatusCalculator.create(project);
        myRepoChangeConnection = myProject.getMessageBus().connect();
        myRepoChangeConnection.subscribe(GitRepository.GIT_REPO_CHANGE, this);
    }

    public static PerRepoStatusCache create(@NotNull Project project) {
        return new PerRepoStatusCache(project);
    }

    public Optional<GitAheadBehindCount> get(GitRepository repo) {
        CachedStatus cachedStatus = behindStatuses.get(repo);
        if (cachedStatus == null) {
            CachedStatus newStatus = CachedStatus.create();
            CachedStatus foundStatus = behindStatuses.putIfAbsent(repo, newStatus);
            cachedStatus = foundStatus != null ? foundStatus : newStatus;
        }
        return cachedStatus.update(repo, myCalculator);
    }

    @Override
    public void dispose() {
        myRepoChangeConnection.disconnect();
        behindStatuses.clear();
    }

    @Override
    public void repositoryChanged(@NotNull GitRepository gitRepository) {
        final GitRepository repo = gitRepository;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Got repo changed event: " + repo);
        }
        final Application application = ApplicationManager.getApplication();
        application.executeOnPooledThread(new Runnable() {
            @Override
            public void run() {
                AccessToken read = application.acquireReadActionLock();
                Optional<GitAheadBehindCount> aheadBehind = get(repo);
                read.finish();
                myProject.getMessageBus().syncPublisher(CACHE_CHANGE).stateChanged(aheadBehind, repo);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Published cache changed event: " + repo);
                }
            }
        });
    }
}
