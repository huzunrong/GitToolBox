package zielu.intellij.ui

import com.intellij.openapi.project.Project

internal interface GtPrjFormUi<T> {
  fun setProject(project: Project)
  fun fillFromProjectState(state: T)
  fun applyToProjectState(state: T)
}
