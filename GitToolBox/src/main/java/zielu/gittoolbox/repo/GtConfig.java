package zielu.gittoolbox.repo;

import com.intellij.dvcs.repo.RepoStateException;
import com.intellij.openapi.diagnostic.Logger;
import java.io.File;
import java.io.IOException;
import org.ini4j.Ini;
import org.jetbrains.annotations.NotNull;

public class GtConfig {
  private static final Logger LOG = Logger.getInstance(GtConfig.class);
  private static final GtConfig EMPTY = new GtConfig();

  private GtConfig() {
  }

  @NotNull
  public static GtConfig load(@NotNull File configFile) {
    if (!configFile.exists()) {
      LOG.info("No .git/config file at " + configFile.getPath());
      return EMPTY;
    } else {
      Ini ini = new Ini();
      ini.getConfig().setMultiOption(true);
      ini.getConfig().setTree(false);

      try {
        ini.load(configFile);
      } catch (IOException exception) {
        LOG.warn(new RepoStateException("Couldn\'t load .git/config file at " + configFile.getPath(), exception));
        return EMPTY;
      }
      return new GtConfig();
    }
  }
}
