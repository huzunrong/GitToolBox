package zielu.gittoolbox.ui.blame;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBusConnection;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zielu.gittoolbox.blame.BlameListener;
import zielu.gittoolbox.blame.BlameService;
import zielu.gittoolbox.config.ConfigNotifier;
import zielu.gittoolbox.config.GitToolBoxConfig2;

class BlameUiSubscriber {
  private final Logger log = Logger.getInstance(getClass());
  private final Project project;

  BlameUiSubscriber(@NotNull Project project) {
    this.project = project;
    MessageBusConnection connection = project.getMessageBus().connect(project);
    connection.subscribe(ConfigNotifier.CONFIG_TOPIC, new ConfigNotifier() {
      @Override
      public void configChanged(GitToolBoxConfig2 previous, GitToolBoxConfig2 current) {
        if (onConfigChanged(previous, current)) {
          VirtualFile file = getFileForSelectedEditor();
          if (file != null) {
            log.debug("Refresh editor on config change for ", file);
            refreshEditorFile(file);
          }
        }
      }
    });
    connection.subscribe(BlameService.BLAME_UPDATE, new BlameListener() {
      @Override
      public void blameUpdated(@NotNull VirtualFile file) {
        onBlameUpdate(file);
      }

      @Override
      public void blameInvalidated(@NotNull VirtualFile file) {
        onBlameUpdate(file);
      }
    });
    connection.subscribe(EditorColorsManager.TOPIC, this::onColorSchemeChanged);
  }

  private void onBlameUpdate(@NotNull VirtualFile file) {
    GitToolBoxConfig2 config = GitToolBoxConfig2.getInstance();
    if (config.showEditorInlineBlame) {
      BlameEditorService.getExistingInstance(project).ifPresent(service -> service.blameUpdated(file));
      VirtualFile fileInEditor = getFileForSelectedEditor();
      if (Objects.equals(fileInEditor, file)) {
        log.debug("Refresh editor on blame update for ", file);
        refreshEditorFile(file);
      }
    }
  }

  @Nullable
  private VirtualFile getFileForSelectedEditor() {
    FileEditor editor = FileEditorManager.getInstance(project).getSelectedEditor();
    if (editor != null) {
      return editor.getFile();
    }
    return null;
  }

  private void refreshEditorFile(@NotNull VirtualFile file) {
    FileEditorManagerEx.getInstanceEx(project).updateFilePresentation(file);
  }

  private void onColorSchemeChanged(@Nullable EditorColorsScheme scheme) {
    if (scheme != null) {
      BlameEditorService.getExistingInstance(project).ifPresent(service -> service.colorsSchemeChanged(scheme));
    }
  }

  private boolean onConfigChanged(GitToolBoxConfig2 previous, GitToolBoxConfig2 current) {
    boolean blamePresentationChanged = current.isBlameInlinePresentationChanged(previous);
    BlameEditorService.getExistingInstance(project).ifPresent(service -> service.configChanged(previous, current));
    return current.showBlame != previous.showBlame
        || current.showEditorInlineBlame != previous.showEditorInlineBlame
        || blamePresentationChanged;
  }
}
