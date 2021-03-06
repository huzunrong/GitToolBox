package zielu.gittoolbox.startup

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import zielu.gittoolbox.config.AppConfig
import zielu.gittoolbox.config.ProjectConfig

internal class GitToolBoxStartup : StartupActivity, DumbAware {
  override fun runActivity(project: Project) {
    if (!project.isDefault) {
      // possibly run config migration
      AppConfig.getConfig()
      ProjectConfig.get(project)

      // project is ready after migrations are done
      GitToolBoxStartupGateway(project).fireProjectReady()
    }
  }
}
