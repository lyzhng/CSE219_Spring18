package actions;

import javafx.application.Platform;
import javafx.stage.FileChooser;
import settings.AppPropertyTypes;
import ui.AppUI;
import vilij.components.ActionComponent;
import vilij.components.ConfirmationDialog;
import vilij.components.Dialog;
import vilij.settings.PropertyTypes;
import vilij.templates.ApplicationTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static settings.AppPropertyTypes.*;

/**
 * This is the concrete implementation of the action handlers required by the application.
 *
 * @author Ritwik Banerjee
 */
public final class AppActions implements ActionComponent {

    /** The application to which this class of actions belongs. */
    private ApplicationTemplate applicationTemplate;

    /** Path to the data file currently active. */
    Path dataFilePath;

    private static final String NEW_LINE = "\n";

    public AppActions(ApplicationTemplate applicationTemplate) {
        this.applicationTemplate = applicationTemplate;
    }

    @Override
    public void handleNewRequest() {
        // TODO for homework 1
        // Confirmation dialog appears, asking Y/N/Cancel.

        try {
            applicationTemplate.getDialog(Dialog.DialogType.CONFIRMATION)
                    .show( applicationTemplate.manager.getPropertyValue(SAVE_UNSAVED_WORK_TITLE.name()), applicationTemplate.manager.getPropertyValue(SAVE_UNSAVED_WORK.name()));

            ConfirmationDialog.Option option =
                    ((ConfirmationDialog) (applicationTemplate.getDialog(Dialog.DialogType.CONFIRMATION)))
                            .getSelectedOption();

            if (option == ConfirmationDialog.Option.YES) {
                promptToSave();
            }

            if (option == ConfirmationDialog.Option.NO) {
                applicationTemplate.getUIComponent().clear();
            }

            if (option == ConfirmationDialog.Option.CANCEL) {
                // By default, it works that way.
            }
        }
        catch (IOException e) {
            applicationTemplate.
                    getDialog(Dialog.DialogType.ERROR)
                    .show( applicationTemplate.manager.getPropertyValue(PropertyTypes.SAVE_ERROR_TITLE.name()) ,
                            applicationTemplate.manager.getPropertyValue(AppPropertyTypes.RESOURCE_SUBDIR_NOT_FOUND.name()));

        }

    }

    @Override
    public void handleSaveRequest() {
        // TODO: NOT A PART OF HW 1
    }

    @Override
    public void handleLoadRequest() {
        // TODO: NOT A PART OF HW 1
    }

    @Override
    public void handleExitRequest() {
        // TODO for homework 1
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void handlePrintRequest() {
        // TODO: NOT A PART OF HW 1
    }

    public void handleScreenshotRequest() throws IOException {
        // TODO: NOT A PART OF HW 1
    }

    /**
     * This helper method verifies that the user really wants to save their unsaved work, which they might not want to
     * do. The user will be presented with three options:
     * <ol>
     * <li><code>yes</code>, indicating that the user wants to save the work and continue with the action,</li>
     * <li><code>no</code>, indicating that the user wants to continue with the action without saving the work, and</li>
     * <li><code>cancel</code>, to indicate that the user does not want to continue with the action, but also does not
     * want to save the work at this point.</li>
     * </ol>
     *
     * @return <code>false</code> if the user presses the <i>cancel</i>, and <code>true</code> otherwise.
     */

    private boolean promptToSave() throws IOException {
        // TODO for homework 1
        // TODO remove the placeholder line below after you have implemented this method

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter( applicationTemplate.manager.getPropertyValue(DATA_FILE_EXT_DESC.name()) +
                        applicationTemplate.manager.getPropertyValue(TSD_EXT_TITLE.name()), DATA_FILE_EXT.name())
        );

        // System.getProperty("user.dir") + "/hw1/data-vilij/resources/data");
        // /Users/lilyzhong/IdeaProjects/cse219homework/hw1/data-vilij/src/actions/AppActions.java
        String hw1_dir = applicationTemplate.manager.getPropertyValue(AppPropertyTypes.HW1_DIR.name());
        String datav_dir = applicationTemplate.manager.getPropertyValue(AppPropertyTypes.DATAV_DIR.name());
        String rsc_dir = applicationTemplate.manager.getPropertyValue(AppPropertyTypes.RSC_DIR.name());
        String data_dir = applicationTemplate.manager.getPropertyValue(AppPropertyTypes.DATA_RESOURCE_PATH.name());
        File location = new File(hw1_dir.substring(1) + datav_dir + rsc_dir + File.separator + data_dir);

        // fileChooser.setInitialDirectory(new File("hw1/data-vilij/resources/data"));

        if (!location.exists()) {
            throw new IOException();
        }

        fileChooser.setInitialDirectory(location);

        // choose somewhere to save the file
        File selectedFile = fileChooser.showSaveDialog( applicationTemplate.getUIComponent().getPrimaryWindow() );

        if (selectedFile != null)
            dataFilePath = selectedFile.toPath();

        else
            return false;

        String dir = dataFilePath.getParent().toString();
        String fileName = dataFilePath.getFileName().toString();
        String ext = applicationTemplate.manager.
                getPropertyValue(AppPropertyTypes.DATA_FILE_EXT.name());
        if (!fileName.contains(ext)) {
            fileName.concat(ext);
        }

        File file = new File(dir, fileName);

        FileWriter fileWriter = new FileWriter(file);

        String text = ((AppUI) (applicationTemplate.getUIComponent())).getText();
        for (String s : text.split(NEW_LINE))
            fileWriter.write(s + NEW_LINE);

        fileWriter.flush();

        return true;
    }
}
