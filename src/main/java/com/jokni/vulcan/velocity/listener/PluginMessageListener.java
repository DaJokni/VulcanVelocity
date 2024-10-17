package com.jokni.vulcan.velocity.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import com.jokni.vulcan.velocity.VulcanVelocity;

public class PluginMessageListener {

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!(event.getSource() instanceof ServerConnection) ||
                !event.getIdentifier().getId().equals("vulcan:bungee")) {
            return;
        }

        VulcanVelocity instance = VulcanVelocity.getInstance();
        if (instance == null) {
            return;
        }

        event.setResult(PluginMessageEvent.ForwardResult.handled());

        ByteArrayDataInput input = ByteStreams.newDataInput(event.getData());
        String subCommand = input.readUTF();

        switch (subCommand) {
            case "alert" -> instance.getAlertManager().handleAlert((ServerConnection) event.getSource(), input);
            case "punishment" -> instance.getAlertManager().handlePunishment((ServerConnection) event.getSource(), input);
        }
    }
}