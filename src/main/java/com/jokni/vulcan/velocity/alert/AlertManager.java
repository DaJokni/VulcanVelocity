package com.jokni.vulcan.velocity.alert;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import com.jokni.vulcan.velocity.VulcanVelocity;
import com.jokni.vulcan.velocity.config.Config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AlertManager {
    private final Set<Player> alertsEnabled = new HashSet<>();

    public boolean toggleAlerts(Player player) {
        if (this.alertsEnabled.contains(player)) {
            this.alertsEnabled.remove(player);
            return false;
        } else {
            this.alertsEnabled.add(player);
            return true;
        }
    }

    public void handleAlert(ServerConnection connection, ByteArrayDataInput input) {
        String[] components = input.readUTF().split("#VULCAN#");
        String checkName = components[0];
        String checkType = components[1];
        String vl = components[2];
        String player = components[3];
        String maxVl = components[4];
        String clientVersion = components[5];
        String tps = components[6];
        String ping = components[7];
        String description = components[8];
        String info = components[9];
        String dev = components[10];
        String severity = components[11];
        String serverName = connection.getServerInfo().getName();

        TextComponent alertMessage = (TextComponent) MiniMessage.miniMessage().deserialize(
                Config.ALERTS_FORMAT
                        .replace("%check%", checkName)
                        .replace("%type%", checkType)
                        .replace("%vl%", vl)
                        .replace("%player%", player)
                        .replace("%max-vl%", maxVl)
                        .replace("%client-version%", clientVersion)
                        .replace("%tps%", tps)
                        .replace("%ping%", ping)
                        .replace("%info%", info)
                        .replace("%description%", description)
                        .replace("%server%", serverName)
                        .replace("%prefix%", Config.PREFIX)
        ).clickEvent(ClickEvent.runCommand("/" + Config.ALERTS_CLICK_COMMAND.replace("%player%", player).replace("%server%", serverName)));

        TextComponent.Builder hoverMessage = Component.text();
        for (String line : Config.ALERTS_HOVER_FORMAT) {
            line = line
                    .replace("%check%", checkName)
                    .replace("%type%", checkType)
                    .replace("%vl%", vl)
                    .replace("%player%", player)
                    .replace("%max-vl%", maxVl)
                    .replace("%client-version%", clientVersion)
                    .replace("%tps%", tps)
                    .replace("%ping%", ping)
                    .replace("%info%", info)
                    .replace("%description%", description)
                    .replace("%server%", serverName)
                    .replace("%dev%", dev)
                    .replace("%severity%", severity)
                    .replace("%prefix%", Config.PREFIX);
            hoverMessage.append(MiniMessage.miniMessage().deserialize(line)).append(Component.newline());
        }

        alertMessage = alertMessage.hoverEvent(hoverMessage.build().asHoverEvent());

        for (Player staff : this.alertsEnabled) {
            if (Config.DONT_SEND_ALERTS_TO_SAME_SERVER && staff.getCurrentServer().isPresent() &&
                    staff.getCurrentServer().get().getServerInfo().getName().equals(serverName)) {
                continue;
            }
            staff.sendMessage(alertMessage);
        }
    }

    public void handlePunishment(ServerConnection connection, ByteArrayDataInput input) {
        String inputString = input.readUTF();
        String[] components = inputString.split("#VULCAN#");

        if (components.length > 0) {
            String command = components[0];
            VulcanVelocity.getInstance().getServer().getCommandManager().executeAsync(VulcanVelocity.getInstance().getServer().getConsoleCommandSource(), command);
        } else {
            return;
        }
        String checkName = components.length > 1 ? components[1] : "Unknown Check";
        String checkType = components.length > 2 ? components[2] : "Unknown Type";
        String vl = components.length > 3 ? components[3] : "0";
        String player = components.length > 4 ? components[4] : "Unknown Player";
        String maxVl = components.length > 5 ? components[5] : "N/A";
        String serverName = connection.getServerInfo().getName();

        // Create punishment message
        TextComponent punishmentMessage = (TextComponent) MiniMessage.miniMessage().deserialize(
                Config.PUNISHMENT_FORMAT
                        .replace("%check%", checkName)
                        .replace("%type%", checkType)
                        .replace("%vl%", vl)
                        .replace("%player%", player)
                        .replace("%max-vl%", maxVl)
                        .replace("%server%", serverName)
                        .replace("%prefix%", Config.PREFIX)
        );

        // Send punishment message to enabled players
        for (Player staff : this.alertsEnabled) {
            if (Config.DONT_SEND_ALERTS_TO_SAME_SERVER && staff.getCurrentServer().isPresent() &&
                    staff.getCurrentServer().get().getServerInfo().getName().equals(serverName)) {
                continue;
            }
            staff.sendMessage(punishmentMessage);
        }
    }

    public Set<Player> getAlertsEnabled() {
        return this.alertsEnabled;
    }
}
