package zielu.gittoolbox.ui.config.app.v2

import com.intellij.ui.components.JBTabbedPane
import zielu.gittoolbox.config.GitToolBoxConfig2
import zielu.intellij.ui.CompositeGtFormUiEx
import zielu.intellij.ui.GtFormUiEx
import javax.swing.JComponent

internal class AppConfigForm : GtFormUiEx<GitToolBoxConfig2> {
  private val pages = CompositeGtFormUiEx<GitToolBoxConfig2>()
  private val tabs = JBTabbedPane()

  override fun getContent(): JComponent {
    return tabs
  }

  override fun afterStateSet() {
    pages.afterStateSet()
  }

  override fun init() {
    val appPages = AppPages()
    val generalPage = GeneralPage(appPages)
    val projectViewPage = ProjectViewPage(appPages)
    pages.add(generalPage)
    pages.add(projectViewPage)
    pages.init()
    tabs.addTab("General", generalPage.content)
    tabs.addTab("Project View", projectViewPage.content)
  }

  override fun dispose() {
    pages.dispose()
  }

  override fun fillFromState(state: GitToolBoxConfig2) {
    pages.fillFromState(state)
  }

  override fun isModified(): Boolean {
    return pages.isModified()
  }

  override fun applyToState(state: GitToolBoxConfig2) {
    pages.applyToState(state)
  }
}
