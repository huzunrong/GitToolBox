package zielu.gittoolbox.ui.config.app.v2

import com.intellij.openapi.observable.properties.AtomicBooleanProperty
import com.intellij.openapi.observable.properties.AtomicLazyProperty
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.layout.panel
import zielu.gittoolbox.ResBundle
import zielu.gittoolbox.config.AuthorNameType
import zielu.gittoolbox.config.DateType
import zielu.gittoolbox.config.GitToolBoxConfig2
import zielu.intellij.ui.GtFormUiEx
import javax.swing.JComponent

internal class BlamePage : GtFormUiEx<GitToolBoxConfig2> {
  private val inlineAuthorNameType = AtomicLazyProperty {
    AuthorNameType.LASTNAME
  }
  private val inlineDateType = AtomicLazyProperty {
    DateType.AUTO
  }
  private val inlineShowSubject = AtomicBooleanProperty(true)
  private val inlineAlwaysShowBlameWhileDebugging = AtomicBooleanProperty(false)
  private val statusAuthorNameType = AtomicLazyProperty {
    AuthorNameType.LASTNAME
  }
  private lateinit var panel: DialogPanel

  override fun init() {
    panel = panel {
      titledRow(ResBundle.message("configurable.app.editorInlineBlame.label")) {
        row(ResBundle.message("configurable.app.blameAuthorName")) {
          comboBox(
            CollectionComboBoxModel(AuthorNameType.allValues),
            inlineAuthorNameType::get,
            { inlineAuthorNameType.set(it!!) }
          )
        }
        row(ResBundle.message("configurable.app.blameDateType")) {
          comboBox(
            CollectionComboBoxModel(DateType.allValues),
            inlineDateType::get,
            { inlineDateType.set(it!!) }
          )
        }
        row {
          checkBox(
            ResBundle.message("configurable.app.blameSubject"),
            inlineShowSubject::get,
            { inlineShowSubject.set(it) }
          )
        }
        row {
          checkBox(
            ResBundle.message("configurable.app.alwaysShowInlineBlameWhileDebugging.label"),
            inlineAlwaysShowBlameWhileDebugging::get,
            { inlineAlwaysShowBlameWhileDebugging.set(it) }
          )
        }
      }
      titledRow(ResBundle.message("configurable.app.statusBlame.label")) {
        row(ResBundle.message("configurable.app.blameAuthorName")) {
          comboBox(
            CollectionComboBoxModel(AuthorNameType.allValues),
            statusAuthorNameType::get,
            { statusAuthorNameType.set(it!!) }
          )
        }
      }
    }
  }

  override fun fillFromState(state: GitToolBoxConfig2) {
    inlineAuthorNameType.set(state.blameInlineAuthorNameType)
    inlineDateType.set(state.blameInlineDateType)
    inlineShowSubject.set(state.blameInlineShowSubject)
    inlineAlwaysShowBlameWhileDebugging.set(state.alwaysShowInlineBlameWhileDebugging)
    statusAuthorNameType.set(state.blameStatusAuthorNameType)
    // TODO: fill from state
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
