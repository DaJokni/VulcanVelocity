package com.jokni.vulcan.velocity.config;

import com.jokni.vulcan.velocity.VulcanVelocity;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public final class Config {
    private static ConfigurationNode configuration;

    public static String ALERTS_FORMAT;
    public static String PUNISHMENT_FORMAT;
    public static String ALERTS_CLICK_COMMAND;
    public static String NO_PERMISSION;
    public static String PREFIX;
    public static String ALERTS_ENABLED;
    public static String ALERTS_DISABLED;
    public static List<String> ALERTS_HOVER_FORMAT;

    public static boolean DONT_SEND_ALERTS_TO_SAME_SERVER;
    public static boolean ENABLE_ALERTS_ON_JOIN;

    public static void initializeConfig() throws SerializationException {
        VulcanVelocity instance = VulcanVelocity.getInstance();
        try {
            File dataFolder = instance.getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }

            File configFile = new File(dataFolder, "config.yml");
            if (!configFile.exists()) {
                try (InputStream in = instance.getClass().getClassLoader().getResourceAsStream("config.yml")) {
                    if (in != null) {
                        Files.copy(in, configFile.toPath());
                    }
                } catch (IOException e) {
                    instance.getLogger().error("Error while loading VulcanVelocity's configuration file!");
                    e.printStackTrace();
                }
            }

            YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                    .path(configFile.toPath())
                    .build();
            configuration = loader.load();

            boolean isLegacy = configuration.node("config-version-do-not-touch").isNull();

            if (isLegacy) {
                //rename the old config file to old-config.yml
                File oldConfigFile = new File(dataFolder, "old-config.yml");
                if (configFile.renameTo(oldConfigFile)) {
                    instance.getLogger().warn("Legacy configuration renamed to old-config.yml.");

                    initializeConfig();
                } else {
                    instance.getLogger().error("Failed to rename legacy configuration file.");
                }
                return;
            }

            PREFIX = configuration.node("prefix").getString();
            ALERTS_FORMAT = configuration.node("alerts", "format").getString();
            ALERTS_ENABLED = configuration.node("messages", "alerts-enabled").getString();
            ALERTS_DISABLED = configuration.node("messages", "alerts-disabled").getString();
            NO_PERMISSION = configuration.node("messages", "no-permission").getString();
            PUNISHMENT_FORMAT = configuration.node("punishments", "format").getString();
            ALERTS_CLICK_COMMAND = configuration.node("alerts", "click-command").getString();
            ALERTS_HOVER_FORMAT = configuration.node("alerts", "hover-format").getList(TypeToken.get(String.class));

            ENABLE_ALERTS_ON_JOIN = configuration.node("settings", "enable-alerts-on-join").getBoolean();
            DONT_SEND_ALERTS_TO_SAME_SERVER = configuration.node("settings", "dont-send-alerts-to-same-server").getBoolean();

        } catch (IOException e) {
            instance.getLogger().error("Error while loading VulcanVelocity's configuration file!");
            e.printStackTrace();
        }
    }

    private Config() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}