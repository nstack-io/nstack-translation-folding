package com.nodes.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.ToggleActionButton;
import com.intellij.util.Icons;
import com.nodes.folding.NstackTranslationConfig;

import javax.swing.Icon;

/**
 * Created by Vladimir Ostaci on 28/11/2017.
 */

public class ToggleLineMarkingAction extends ToggleActionButton {

    private final NstackTranslationConfig config = NstackTranslationConfig.getInstance();

    public ToggleLineMarkingAction() {
        super("", Icons.CHECK_ICON);
    }

    public ToggleLineMarkingAction(String text, Icon icon) {
        super(text, icon);
    }

    @Override
    public boolean isSelected(AnActionEvent e) {
        return false;
    }

    @Override
    public void setSelected(AnActionEvent e, boolean state) {
        getTemplatePresentation().setSelectedIcon(Icons.CHECK_ICON_SELECTED);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

}
