package zielu.gittoolbox.config

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.annotations.Transient
import zielu.gittoolbox.metrics.ProjectMetrics
import zielu.gittoolbox.util.AppUtil

@State(name = "GitToolBoxProjectSettings", storages = [Storage("git_toolbox_prj.xml")])
internal class ProjectConfig(
  @Transient
  private val project: Project
) : PersistentStateComponent<GitToolBoxConfigPrj> {
  private var state: GitToolBoxConfigPrj = GitToolBoxConfigPrj()

  override fun getState(): GitToolBoxConfigPrj = state

  override fun loadState(state: GitToolBoxConfigPrj) {
    this.state = state
  }

  override fun initializeComponent() {
    val appConfig = AppConfig.getConfig()
    val timer = ProjectMetrics.getInstance(project).timer("project-config.migrate")
    val result = timer.timeSupplier { ConfigMigrator().migrate(project, state, appConfig) }
    if (result) {
      log.info("Migration done")
    } else {
      log.info("Already migrated")
    }
  }

  fun updateState(updated: GitToolBoxConfigPrj) {
    val current = state
    if (updated != current) {
      state = updated
      fireChanged(current, updated)
    }
  }

  private fun fireChanged(previous: GitToolBoxConfigPrj, current: GitToolBoxConfigPrj) {
    project.messageBus.syncPublisher(ProjectConfigNotifier.CONFIG_TOPIC).configChanged(previous, current)
  }

  companion object {
    private val log = Logger.getInstance(ProjectConfig::class.java)

    @JvmStatic
    fun get(project: Project): GitToolBoxConfigPrj {
      return getInstance(project).state
    }

    @JvmStatic
    fun getInstance(project: Project): ProjectConfig {
      return AppUtil.getServiceInstance(project, ProjectConfig::class.java)
    }
  }
}
