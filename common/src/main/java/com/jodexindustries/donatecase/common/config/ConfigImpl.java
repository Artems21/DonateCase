package com.jodexindustries.donatecase.common.config;

import com.jodexindustries.donatecase.api.config.CaseStorage;
import com.jodexindustries.donatecase.api.config.Config;
import com.jodexindustries.donatecase.api.config.ConfigCases;
import com.jodexindustries.donatecase.api.config.Messages;
import com.jodexindustries.donatecase.api.data.casedata.CaseDataMaterial;
import com.jodexindustries.donatecase.api.data.casedata.gui.CaseGui;
import com.jodexindustries.donatecase.api.data.storage.CaseLocation;
import com.jodexindustries.donatecase.api.event.plugin.DonateCaseReloadEvent;
import com.jodexindustries.donatecase.common.database.CaseDatabaseImpl;
import com.jodexindustries.donatecase.common.managers.CaseKeyManagerImpl;
import com.jodexindustries.donatecase.common.managers.CaseOpenManagerImpl;
import com.jodexindustries.donatecase.common.platform.BackendPlatform;
import com.jodexindustries.donatecase.common.serializer.CaseDataMaterialSerializer;
import com.jodexindustries.donatecase.common.serializer.CaseGuiSerializer;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ConfigImpl implements Config {

    public final TypeSerializerCollection serializerCollection = TypeSerializerCollection.builder()
            .register(CaseGui.class, new CaseGuiSerializer())
            .register(CaseGui.Item.class, new CaseGuiSerializer.Item())
            .register(CaseDataMaterial.class, new CaseDataMaterialSerializer())
            .register(CaseLocation.class, new CaseLocation())
            .build();

    private final Messages messages;
    private final ConfigCases configCases;
    private final CaseStorage caseStorage;

    private final Map<File, ConfigurationNode> configurations = new HashMap<>();

    private static final String[] defaultFiles = {
            "Config.yml",
            "Cases.yml",
            "Animations.yml",
            "lang/en_US.yml",
    };

    @Getter
    private final BackendPlatform platform;

    public ConfigImpl(BackendPlatform platform) {
        this.platform = platform;
        this.caseStorage = new CaseStorageImpl(this);
        this.messages = new MessagesImpl(platform);
        this.configCases = new ConfigCasesImpl(this);
    }

    @Override
    @Nullable
    public ConfigurationNode get(@NotNull File file) {
        return configurations.get(file);
    }

    @Override
    @Nullable
    public ConfigurationNode get(@NotNull String name) {
        return configurations.get(getFile(name));
    }

    @Override
    public void load() {
        createFiles();
        loadConfigurations();

        try {
            messages.load(getConfig().node("DonateCase", "Languages").getString("en_US"));
            caseStorage.load();
            configCases.load();
        } catch (ConfigurateException e) {
            platform.getLogger().log(Level.WARNING, "Error with loading configuration: ", e);
        }

        long caching = getConfig().node("DonateCase", "Caching").getLong();
        if (caching >= 0) {
            CaseOpenManagerImpl.cache.setMaxAge(caching);
            CaseKeyManagerImpl.cache.setMaxAge(caching);
            CaseDatabaseImpl.cache.setMaxAge(caching);
        }

        platform.getAPI().getEventBus().post(new DonateCaseReloadEvent(DonateCaseReloadEvent.Type.CONFIG));
    }

    @Override
    public void delete(@NotNull File file) {
        if(file.delete()) configurations.remove(file);
    }

    @Override
    public void delete(@NotNull String name) {
        File file = new File(platform.getDataFolder(), name);
        delete(file);
    }

    @Override
    public boolean save(String name) {
        return save(new File(platform.getDataFolder(), name));
    }

    @Override
    public boolean save(File file) {
        String name = file.getName();
        ConfigurationNode node = configurations.get(file);
        if (node == null) return false;

        try {
            YamlConfigurationLoader loader = YamlConfigurationLoader.builder().file(file).build();
            loader.save(node);
            return true;
        } catch (ConfigurateException e) {
            platform.getLogger().log(Level.WARNING, "Couldn't save " + name);
        }
        return false;
    }

    private void loadConfigurations() {
        loadConfigurations(platform.getDataFolder().listFiles());
    }

    private void loadConfigurations(File[] files) {
        if(files == null) return;
        for (File file : files) {
            if(!file.isFile()) continue;

            loadConfiguration(file);
        }
    }

    private void loadConfiguration(File file) {
        if(file.getName().endsWith(".yml") || file.getName().endsWith(".yaml")) {
            try {
                YamlConfigurationLoader loader = YamlConfigurationLoader
                        .builder()
                        .defaultOptions(opts -> opts.serializers(build -> build.registerAll(serializerCollection)))
                        .file(file)
                        .build();
                ConfigurationNode node = loader.load();

                configurations.put(file, node);
            } catch (ConfigurateException e) {
                platform.getLogger().log(Level.WARNING, "Error with loading configuration: ", e);
            }
        }
    }

    private void createFiles() {
        for (String fileName : defaultFiles) {
            File file = new File(platform.getDataFolder(), fileName);
            if (!file.exists()) platform.saveResource(fileName, false);
        }
    }

    @Override
    public File getFile(@NotNull String name) {
        return new File(platform.getDataFolder(), name);
    }

    @Override
    public @NotNull Messages getMessages() {
        return messages;
    }

    @Override
    public @NotNull ConfigCases getConfigCases() {
        return configCases;
    }

    @Override
    public @NotNull CaseStorage getCaseStorage() {
        return caseStorage;
    }


}
