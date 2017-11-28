package com.nodes.folding;

/**
 * Created by Vladimir Ostaci on 28/11/2017.
 */

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;

import org.jetbrains.annotations.Nullable;

/**
 * PersistentStateComponent keeps project config values.
 */
@State(
        name = "NstackTranslationConfig",
        storages = {@Storage("StoragePathMacros.WORKSPACE_FILE")}
)
public class NstackTranslationConfig implements PersistentStateComponent<NstackTranslationConfig> {

    boolean lineMarkingEnabled;

    public boolean isLineMarkingEnabled() {
        return lineMarkingEnabled;
    }

    public void setLineMarkingEnabled(boolean lineMarkingEnabled) {
        this.lineMarkingEnabled = lineMarkingEnabled;
    }

    @Nullable
    @Override
    public NstackTranslationConfig getState() {
        return this;
    }

    @Override
    public void loadState(NstackTranslationConfig nstackTranslationConfig) {
        XmlSerializerUtil.copyBean(nstackTranslationConfig, this);
    }

    @Nullable
    public static NstackTranslationConfig getInstance() {
        return ServiceManager.getService(NstackTranslationConfig.class);
    }
}