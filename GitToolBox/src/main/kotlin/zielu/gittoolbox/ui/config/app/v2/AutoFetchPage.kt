package zielu.gittoolbox.ui.config.app.v2

import com.intellij.openapi.observable.properties.AtomicBooleanProperty
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.layout.panel
import com.intellij.ui.layout.selected
import zielu.gittoolbox.ResBundle
import zielu.gittoolbox.config.GitToolBoxConfig2
import zielu.gittoolbox.fetch.AutoFetchParams
import zielu.intellij.ui.GtFormUiEx
import zielu.intellij.util.createIntProperty
import javax.swing.JComponent

internal class AutoFetchPage : GtFormUiEx<GitToolBoxConfig2> {
  private val autoFetchEnabled = AtomicBooleanProperty(true)
  private val autoFetchInterval = createIntProperty(AutoFetchParams.DEFAULT_INTERVAL_MINUTES)
  private val autoFetchOnBranchSwitch = AtomicBooleanProperty(true)
  private lateinit var panel: DialogPanel

  override fun init() {
    panel = panel {
      row {
        val autoFetchCheckBox = checkBox(
          ResBundle.message("configurable.app.autoFetchEnabled.label"),
          autoFetchEnabled::get,
          autoFetchEnabled::set
        )
        cell {
          spinner(autoFetchInterval::value, AutoFetchParams.INTERVAL_MIN_MINUTES, AutoFetchParams.INTERVAL_MAX_MINUTES)
            .enableIf(autoFetchCheckBox.selected)
          label(ResBundle.message("configurable.app.autoFetchUnits.label"))
        }
      }
      row {
        checkBox(
          ResBundle.message("configurable.app.autoFetchOnBranchSwitchEnabled.label"),
          autoFetchOnBranchSwitch::get,
          autoFetchOnBranchSwitch::set
        )
      }
    }
  }

  override fun fillFromState(state: GitToolBoxConfig2) {
    autoFetchEnabled.set(state.autoFetchEnabled)
    autoFetchInterval.value = state.autoFetchIntervalMinutes
    autoFetchOnBranchSwitch.set(state.autoFetchOnBranchSwitch)
  }

  override fun isModified(): Boolean {
    return panel.isModified()
  }

  override fun getContent(): JComponent {
    return panel
  }

  override fun afterStateSet() {
    panel.reset()
  }

  override fun applyToState(state: GitToolBoxConfig2) {
    panel.apply()
    // TODO: apply to state
  }
}
