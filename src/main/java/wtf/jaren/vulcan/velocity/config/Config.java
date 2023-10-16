package wtf.jaren.vulcan.velocity.config;

import com.google.common.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
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

    public static void initializeConfig() throws ObjectMappingException {
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
                YAMLConfigurationLoader loader = ((YAMLConfigurationLoader.Builder)YAMLConfigurationLoader.builder().setPath((new File(VulcanVelocity.INSTANCE.getDataFolder(), "config.yml")).toPath())).build();
                configuration = loader.load();
            } catch (IOException e) {
                System.err.println("[VulcanVelocity] Error while loading VulcanVelocity's configuration file!");
                e.printStackTrace();
            }
            PREFIX = configuration.getNode(new Object[] { "prefix" }).getString();
            ALERTS_FORMAT = configuration.getNode(new Object[] { "alerts", "format" }).getString();
            ALERTS_ENABLED = configuration.getNode(new Object[] { "messages", "alerts-enabled" }).getString();
            ALERTS_DISABLED = configuration.getNode(new Object[] { "messages", "alerts-disabled" }).getString();
            NO_PERMISSION = configuration.getNode(new Object[] { "messages", "no-permission" }).getString();
            PUNISHMENT_FORMAT = configuration.getNode(new Object[] { "punishments", "format" }).getString();
            ALERTS_CLICK_COMMAND = configuration.getNode(new Object[] { "alerts", "click-command" }).getString();
            ALERTS_HOVER_FORMAT = configuration.getNode(new Object[] { "alerts", "hover-format" }).getList(new TypeToken<String>() {

            });
            ENABLE_ALERTS_ON_JOIN = configuration.getNode(new Object[] { "settings", "enable-alerts-on-join" }).getBoolean();
            DONT_SEND_ALERTS_TO_SAME_SERVER = configuration.getNode(new Object[] { "settings", "dont-send-alerts-to-same-server" }).getBoolean();
        } catch (Throwable $ex) {
            throw $ex;
        }
    }

    private Config() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
