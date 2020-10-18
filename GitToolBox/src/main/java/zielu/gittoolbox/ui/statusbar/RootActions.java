package zielu.gittoolbox.ui.statusbar;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.CalledInAwt;
import zielu.gittoolbox.ui.statusbar.actions.RefreshStatusAction;

/**
 * Created by Lukasz_Zielinski on 19.09.2016.
 */
public class RootActions extends DefaultActionGroup {

  public RootActions(Project project) {
    super("", true);
  }

  @CalledInAwt
  public boolean update() {
    removeAll();
    add(new RefreshStatusAction());

    return true;
  }

  @Override
  public boolean canBePerformed(DataContext context) {
    return true;
  }
}
