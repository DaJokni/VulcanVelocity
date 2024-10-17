package com.jokni.vulcan.velocity.command;

import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import com.jokni.vulcan.velocity.VulcanVelocity;
import com.jokni.vulcan.velocity.config.Config;
import com.jokni.vulcan.velocity.alert.AlertManager;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class AlertsCommand implements RawCommand {

    @Override
    public void execute(RawCommand.Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            invocation.source().sendMessage(Component.text("This command cannot be executed from console!"));
            return;
        }

        AlertManager alertManager = VulcanVelocity.getInstance().getAlertManager();

        boolean alertsEnabled = alertManager.toggleAlerts(player);

        if (alertsEnabled) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(Config.ALERTS_ENABLED.replace("%prefix%", Config.PREFIX)));
        } else {
            player.sendMessage(MiniMessage.miniMessage().deserialize(Config.ALERTS_DISABLED.replace("%prefix%", Config.PREFIX)));
        }
    }

    @Override
    public boolean hasPermission(RawCommand.Invocation invocation) {
        return invocation.source().hasPermission("vulcanvelocity.alerts");
    }
}