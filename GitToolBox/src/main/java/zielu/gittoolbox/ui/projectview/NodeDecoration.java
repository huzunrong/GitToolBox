package zielu.gittoolbox.ui.projectview;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;

public interface NodeDecoration {

  boolean apply(ProjectViewNode node, PresentationData data);
}
