package zielu.gittoolbox.ui.config.app.v2

import com.intellij.openapi.observable.properties.AtomicBooleanProperty
import com.intellij.openapi.observable.properties.AtomicLazyProperty
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.layout.panel
import zielu.gittoolbox.ResBundle
import zielu.gittoolbox.config.CommitCompletionMode
import zielu.gittoolbox.config.GitToolBoxConfig2
import zielu.intellij.ui.GtFormUiEx
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.ListCellRenderer

internal class CommitPage : GtFormUiEx<GitToolBoxConfig2> {
  private val commitDialogCompletionMode = AtomicLazyProperty {
    CommitCompletionMode.AUTOMATIC
  }
  private val commitDialogBranchCompletion = AtomicBooleanProperty(true)
  private val commitDialogGitmojiCompletion = AtomicBooleanProperty(false)

  private lateinit var panel: DialogPanel

  override fun init() {
    panel = panel {
      row(ResBundle.message("commit.dialog.completion.mode.label")) {
        val renderer: ListCellRenderer<CommitCompletionMode?> = SimpleListCellRenderer.create("") { it?.displayLabel }
        comboBox(
          DefaultComboBoxModel(CommitCompletionMode.values()),
          commitDialogCompletionMode::get,
          { commitDialogCompletionMode.set(it!!) },
          renderer
        )
      }
      row {
        checkBox(
          ResBundle.message("commit.dialog.completion.branch.enabled.label"),
          commitDialogBranchCompletion::get,
          { commitDialogBranchCompletion.set(it) }
        )
      }
      row {
        checkBox(
          ResBundle.message("commit.dialog.completion.gitmoji.enabled.label"),
          commitDialogGitmojiCompletion::get,
          { commitDialogGitmojiCompletion.set(it) }
        )
      }
    }
  }

  override fun fillFromState(state: GitToolBoxConfig2) {
    commitDialogCompletionMode.set(state.commitDialogCompletionMode)
    commitDialogBranchCompletion.set(state.commitDialogCompletion)
    commitDialogGitmojiCompletion.set(state.commitDialogGitmojiCompletion)
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
  }
}
