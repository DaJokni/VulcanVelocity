package wtf.jaren.vulcan.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import java.io.File;
import java.nio.file.Path;

import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import wtf.jaren.vulcan.velocity.alert.AlertManager;
import wtf.jaren.vulcan.velocity.command.AlertsCommand;
import wtf.jaren.vulcan.velocity.config.Config;
import wtf.jaren.vulcan.velocity.listener.PlayerListener;
import wtf.jaren.vulcan.velocity.listener.PluginMessageListener;

@Plugin(id = "vulcan", name = "VulcanVelocity", version = "2.0.6", authors = {"Jaren"})
public class VulcanVelocity {
    private final ProxyServer server;

    private final Logger logger;

    private final File dataFolder;

    public static VulcanVelocity INSTANCE;

    public ProxyServer getServer() {
        return this.server;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public File getDataFolder() {
        return this.dataFolder;
    }

    private final AlertManager alertManager = new AlertManager();

    public AlertManager getAlertManager() {
        return this.alertManager;
    }

    @Inject
    public VulcanVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataFolder = dataDirectory.toFile();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws ObjectMappingException {
        try {
            INSTANCE = this;
            Config.initializeConfig();
            this.server.getChannelRegistrar().register(new ChannelIdentifier[] { (ChannelIdentifier)MinecraftChannelIdentifier.from("vulcan:bungee") });
            this.server.getEventManager().register(this, new PlayerListener());
            this.server.getEventManager().register(this, new PluginMessageListener());
            this.server.getCommandManager().register(this.server.getCommandManager().metaBuilder("vbalerts").build(), (Command)new AlertsCommand());
        } catch (Throwable $ex) {
            throw $ex;
        }
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        INSTANCE = null;
    }
}
