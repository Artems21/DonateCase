package com.jodexindustries.donatecase;

import com.jodexindustries.donatecase.api.DCAPI;
import com.jodexindustries.donatecase.api.addon.PowerReason;
import com.jodexindustries.donatecase.api.config.Config;
import com.jodexindustries.donatecase.api.config.Loadable;
import com.jodexindustries.donatecase.api.database.CaseDatabase;
import com.jodexindustries.donatecase.api.manager.*;
import com.jodexindustries.donatecase.config.CaseLoader;
import com.jodexindustries.donatecase.config.ConfigImpl;
import com.jodexindustries.donatecase.database.CaseDatabaseImpl;
import com.jodexindustries.donatecase.managers.*;
import com.jodexindustries.donatecase.platform.BackendPlatform;
import com.jodexindustries.donatecase.tools.updater.UpdateChecker;
import org.jetbrains.annotations.NotNull;

public class DonateCase extends DCAPI {

    private final ActionManager actionManager;
    private final AddonManager addonManager;
    private final AnimationManager animationmanager;
    private final GUIManager guiManager;
    private final GUITypedItemManager guiTypedItemManager;
    private final MaterialManager materialManager;
    private final SubCommandManager subCommandManager;
    private final CaseKeyManager caseKeyManager;
    private final CaseOpenManager caseOpenManager;
    private final CaseManager caseManager;

    private final CaseDatabase database;
    private final Config config;
    private final CaseLoader caseLoader;
    private final UpdateChecker updateChecker;

    private final BackendPlatform platform;

    public DonateCase(BackendPlatform platform) {
        this.platform = platform;

        this.actionManager = new ActionManagerImpl(this);
        this.addonManager = new AddonManagerImpl(this);
        this.animationmanager = new AnimationManagerImpl(this);
        this.guiManager = new GUIManagerImpl(this);
        this.guiTypedItemManager = new GUITypedItemManagerImpl(this);
        this.materialManager = new MaterialManagerImpl(this);
        this.subCommandManager = new SubCommandManagerImpl(this);
        this.caseKeyManager = new CaseKeyManagerImpl(this);
        this.caseOpenManager = new CaseOpenManagerImpl(this);
        this.caseManager = new CaseManagerImpl();

        this.database = new CaseDatabaseImpl(this);
        this.config = new ConfigImpl(platform);
        this.caseLoader = new CaseLoader(this);
        this.updateChecker = new UpdateChecker(this);
    }

    public void load() {
        long time = System.currentTimeMillis();
        addonManager.loadAddons();

        config.load();
        caseLoader.load();
        updateChecker.check();
        database.connect();
        addonManager.enableAddons(PowerReason.DONATE_CASE);

        platform.getLogger().info("Enabled in " + (System.currentTimeMillis() - time) + "ms");
    }

    public void unload() {
        addonManager.unloadAddons(PowerReason.DONATE_CASE);
        animationmanager.unregisterAnimations();
        subCommandManager.unregisterSubCommands();
        actionManager.unregisterActions();
        materialManager.unregisterMaterials();
        guiTypedItemManager.unregisterItems();

        database.close();

        guiManager.getPlayersGUI().values().parallelStream().forEach(gui -> gui.getPlayer().closeInventory());

        caseManager.getMap().clear();
        animationmanager.getActiveCases().clear();
        animationmanager.getActiveCasesByBlock().clear();
        caseOpenManager.getCache().clear();
        caseKeyManager.getCache().clear();
        database.getCache().clear();
    }

    @Override
    public @NotNull ActionManager getActionManager() {
        return actionManager;
    }

    @Override
    public @NotNull AddonManager getAddonManager() {
        return addonManager;
    }

    @Override
    public @NotNull AnimationManager getAnimationManager() {
        return animationmanager;
    }

    @Override
    public @NotNull CaseKeyManager getCaseKeyManager() {
        return caseKeyManager;
    }

    @Override
    public @NotNull CaseManager getCaseManager() {
        return caseManager;
    }

    @Override
    public @NotNull CaseOpenManager getCaseOpenManager() {
        return caseOpenManager;
    }

    @Override
    public @NotNull GUIManager getGUIManager() {
        return guiManager;
    }

    @Override
    public @NotNull GUITypedItemManager getGuiTypedItemManager() {
        return guiTypedItemManager;
    }

    @Override
    public @NotNull MaterialManager getMaterialManager() {
        return materialManager;
    }

    @Override
    public @NotNull SubCommandManager getSubCommandManager() {
        return subCommandManager;
    }

    @Override
    public HologramManager getHologramManager() {
        return null;
    }

    @Override
    public @NotNull CaseDatabase getDatabase() {
        return database;
    }

    @Override
    public @NotNull Config getConfig() {
        return config;
    }

    @Override
    public @NotNull Loadable getCaseLoader() {
        return caseLoader;
    }

    @Override
    public @NotNull BackendPlatform getPlatform() {
        return platform;
    }
}
