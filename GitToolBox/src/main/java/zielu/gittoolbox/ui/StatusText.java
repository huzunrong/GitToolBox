package zielu.gittoolbox.ui;

import org.jetbrains.annotations.NotNull;
import zielu.gittoolbox.ResBundle;
import zielu.gittoolbox.status.GitAheadBehindCount;
import zielu.gittoolbox.status.Status;

public final class StatusText {
  private StatusText() {
    throw new IllegalStateException();
  }

  public static String format(@NotNull GitAheadBehindCount aheadBehind) {
    Status status = aheadBehind.status();
    if (status.isValid()) {
      if (status == Status.NO_REMOTE) {
        return ResBundle.message("git.no.remote");
      } else {
        return StatusMessagesService.getInstance().aheadBehindStatus(aheadBehind);
      }
    } else {
      return ResBundle.na();
    }
  }

  public static String formatToolTip(@NotNull GitAheadBehindCount aheadBehind) {
    if (aheadBehind.status() == Status.SUCCESS) {
      return "";
    } else {
      return StatusMessagesService.getInstance().aheadBehindStatus(aheadBehind);
    }
  }
}
