package zielu.gittoolbox.ui.config.app.v2

import zielu.gittoolbox.config.AppConfig
import zielu.gittoolbox.config.GitToolBoxConfig2
import zielu.intellij.ui.GtConfigurableBase

internal class AppConfigurable : GtConfigurableBase<AppConfigForm, GitToolBoxConfig2>() {
  override fun createForm(): AppConfigForm {
    return AppConfigForm()
  }

  override fun getDisplayName(): String {
    return "Git Toolbox Global V2"
  }

  override fun setFormState(form: AppConfigForm, config: GitToolBoxConfig2) {
    form.fillFromState(config)
  }

  override fun getConfig(): GitToolBoxConfig2 {
    return AppConfig.get()
  }

  override fun checkModified(form: AppConfigForm, config: GitToolBoxConfig2): Boolean {
    return form.isModified()
  }

  override fun doApply(form: AppConfigForm, config: GitToolBoxConfig2) {
    form.applyToState(config)
  }
}
