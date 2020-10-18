package zielu.gittoolbox.cache

import com.intellij.dvcs.repo.VcsRepositoryMappingListener
import com.intellij.openapi.project.Project
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryChangeListener
import zielu.gittoolbox.util.MessageBusListener
import zielu.gittoolbox.util.ProjectMessageBusListener

internal class CacheSourcesSubscriberGitRepositoryListener : MessageBusListener(), GitRepositoryChangeListener {
  override fun repositoryChanged(repository: GitRepository) {
    handleEvent(repository.project) { project ->
      CacheSourcesSubscriber.getInstance(project).onRepoChanged(repository)
    }
  }
}

internal class CacheSourcesSubscriberMappingListener(
  project: Project
) : ProjectMessageBusListener(project), VcsRepositoryMappingListener {
  override fun mappingChanged() {
    handleEvent { project ->
      CacheSourcesSubscriber.getInstance(project).onDirMappingChanged()
    }
  }
}
