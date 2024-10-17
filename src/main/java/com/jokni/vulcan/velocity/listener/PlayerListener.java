package com.jokni.vulcan.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import com.jokni.vulcan.velocity.VulcanVelocity;
import com.jokni.vulcan.velocity.config.Config;

public class PlayerListener {

    @Subscribe
    public void onLogin(PostLoginEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("vulcanbungee.alerts") && Config.ENABLE_ALERTS_ON_JOIN) {
            VulcanVelocity.getInstance().getAlertManager().toggleAlerts(player);
        }
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        VulcanVelocity.getInstance().getAlertManager().getAlertsEnabled().remove(event.getPlayer());
    }
}