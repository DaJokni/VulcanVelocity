package com.jokni.vulcan.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;
import org.spongepowered.configurate.serialize.SerializationException;
import com.jokni.vulcan.velocity.alert.AlertManager;
import com.jokni.vulcan.velocity.command.AlertsCommand;
import com.jokni.vulcan.velocity.config.Config;
import com.jokni.vulcan.velocity.listener.PlayerListener;
import com.jokni.vulcan.velocity.listener.PluginMessageListener;

import java.io.File;
import java.nio.file.Path;

@Plugin(id = "vulcan", name = "VulcanVelocity", version = "2.1.0", authors = {"Jokni"})
public class VulcanVelocity {

    private static VulcanVelocity INSTANCE;

    private final ProxyServer server;
    private final Logger logger;
    private final File dataFolder;
    private final AlertManager alertManager;

    @Inject
    public VulcanVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataFolder = dataDirectory.toFile();
        this.alertManager = new AlertManager();
        INSTANCE = this;
    }

    public static VulcanVelocity getInstance() {
        return INSTANCE;
    }

    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public AlertManager getAlertManager() {
        return alertManager;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws SerializationException {
        Config.initializeConfig();
        server.getChannelRegistrar().register(MinecraftChannelIdentifier.from("vulcan:bungee"));
        server.getEventManager().register(this, new PlayerListener());
        server.getEventManager().register(this, new PluginMessageListener());
        server.getCommandManager().register(
                server.getCommandManager().metaBuilder("vvalerts").aliases("vbalerts").build(),
                new AlertsCommand()
        );
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        INSTANCE = null;
    }
}