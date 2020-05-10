package zielu.gittoolbox.ui.config.app;

import zielu.gittoolbox.status.BehindStatus;
import zielu.gittoolbox.ui.StatusPresenter;

public class PresenterPreview {
  private static final String DELIMITER = " | ";

  private PresenterPreview() {
    throw new IllegalStateException();
  }

  public static String getStatusBarPreview(StatusPresenter presenter) {
    return String.join(DELIMITER, presenter.aheadBehindStatus(3, 2),
        presenter.aheadBehindStatus(3, 0),
        presenter.aheadBehindStatus(0, 2));
  }

  public static String getProjectViewPreview(StatusPresenter presenter) {
    return String.join(DELIMITER,presenter.nonZeroAheadBehindStatus(3, 2),
        presenter.nonZeroAheadBehindStatus(3, 0),
        presenter.nonZeroAheadBehindStatus(0, 2),
        presenter.branchAndParent("feature", "master"));
  }

  public static String getBehindTrackerPreview(StatusPresenter presenter) {
    return String.join(DELIMITER, presenter.behindStatus(BehindStatus.create(3, 1)),
        presenter.behindStatus(BehindStatus.create(3, -1)),
        presenter.behindStatus(BehindStatus.create(3)));
  }
}
