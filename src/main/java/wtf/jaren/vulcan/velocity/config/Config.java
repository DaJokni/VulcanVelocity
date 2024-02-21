package wtf.jaren.vulcan.velocity.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import io.leangen.geantyref.TypeToken;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import wtf.jaren.vulcan.velocity.VulcanVelocity;

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
        try {
            if (!VulcanVelocity.INSTANCE.getDataFolder().exists())
                VulcanVelocity.INSTANCE.getDataFolder().mkdir();
            File file;
            if (!(file = new File(VulcanVelocity.INSTANCE.getDataFolder(), "config.yml")).exists())
                try {
                    InputStream in = VulcanVelocity.INSTANCE.getClass().getClassLoader().getResourceAsStream("config.yml");
                    try {
                        Files.copy(in, file.toPath(), new java.nio.file.CopyOption[0]);
                        if (in != null)
                            in.close();
                    } catch (Throwable throwable) {
                        if (in != null)
                            try {
                                in.close();
                            } catch (Throwable throwable1) {
                                throwable.addSuppressed(throwable1);
                            }
                        throw throwable;
                    }
                } catch (IOException e) {
                    System.err.println("[VulcanVelocity] Error while loading VulcanVelocity's configuration file!");
                    e.printStackTrace();
                }
            try {
                YamlConfigurationLoader loader = (YamlConfigurationLoader.builder().path((new File(VulcanVelocity.INSTANCE.getDataFolder(), "config.yml")).toPath())).build();
                configuration = loader.load();
            } catch (IOException e) {
                System.err.println("[VulcanVelocity] Error while loading VulcanVelocity's configuration file!");
                e.printStackTrace();
            }
            PREFIX = configuration.node("prefix").getString();
            ALERTS_FORMAT = configuration.node("alerts", "format").getString();
            ALERTS_ENABLED = configuration.node("messages", "alerts-enabled").getString();
            ALERTS_DISABLED = configuration.node("messages", "alerts-disabled").getString();
            NO_PERMISSION = configuration.node("messages", "no-permission").getString();
            PUNISHMENT_FORMAT = configuration.node("punishments", "format").getString();
            ALERTS_CLICK_COMMAND = configuration.node("alerts", "click-command").getString();
            ALERTS_HOVER_FORMAT = configuration.node("alerts", "hover-format").getList(new TypeToken<String>() {});

            ENABLE_ALERTS_ON_JOIN = configuration.node("settings", "enable-alerts-on-join").getBoolean();
            DONT_SEND_ALERTS_TO_SAME_SERVER = configuration.node("settings", "dont-send-alerts-to-same-server").getBoolean();
        } catch (Throwable $ex) {
            throw $ex;
        }
    }

    private Config() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
