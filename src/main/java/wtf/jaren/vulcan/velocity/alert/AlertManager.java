package wtf.jaren.vulcan.velocity.alert;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import wtf.jaren.vulcan.velocity.VulcanVelocity;
import wtf.jaren.vulcan.velocity.config.Config;

public class AlertManager {
    private final Set<Player> alertsEnabled = new HashSet<>();

    public void toggleAlerts(Player player) {
        if (this.alertsEnabled.contains(player)) {
            player.sendMessage((Component)LegacyComponentSerializer.legacyAmpersand().deserialize(Config.ALERTS_DISABLED.replace("%prefix%", Config.PREFIX)));
            this.alertsEnabled.remove(player);
        } else {
            player.sendMessage((Component)LegacyComponentSerializer.legacyAmpersand().deserialize(Config.ALERTS_ENABLED.replace("%prefix%", Config.PREFIX)));
            this.alertsEnabled.add(player);
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
        TextComponent alertMessage = (TextComponent)LegacyComponentSerializer.legacyAmpersand().deserialize(Config.ALERTS_FORMAT.replace("%check%", checkName).replace("%type%", checkType).replace("%vl%", vl).replace("%player%", player).replace("%max-vl%", maxVl).replace("%client-version%", clientVersion).replace("%tps%", tps).replace("%ping%", ping).replace("%info%", info).replace("%description%", description).replace("%server%", serverName).replace("%prefix", Config.PREFIX)).clickEvent(ClickEvent.runCommand("/" + Config.ALERTS_CLICK_COMMAND.replace("%player%", player).replace("%server%", serverName)));
        TextComponent.Builder hoverMessage = Component.text();
        int size = Config.ALERTS_HOVER_FORMAT.size();
        int i = 1;
        for (String line : Config.ALERTS_HOVER_FORMAT) {
            line = line.replace("%check%", checkName).replace("%type%", checkType).replace("%vl%", vl).replace("%player%", player).replace("%max-vl%", maxVl).replace("%client-version%", clientVersion).replace("%tps%", tps).replace("%ping%", ping).replace("%info%", info).replace("%description%", description).replace("%server%", serverName).replace("%dev%", dev).replace("%severity%", severity).replace("%prefix", Config.PREFIX);
            hoverMessage.append((Component)LegacyComponentSerializer.legacyAmpersand().deserialize(line));
            if (i != size)
                hoverMessage.append((Component)Component.newline());
            i++;
        }
        alertMessage = (TextComponent)alertMessage.hoverEvent((HoverEventSource)((TextComponent)hoverMessage.build()).asHoverEvent());
        for (Player staff : this.alertsEnabled) {
            if (Config.DONT_SEND_ALERTS_TO_SAME_SERVER && staff.getCurrentServer().isPresent() && ((ServerConnection)staff.getCurrentServer().get()).getServerInfo().getName().equals(serverName))
                continue;
            staff.sendMessage((Component)alertMessage);
        }
    }

    public void handlePunishment(ServerConnection connection, ByteArrayDataInput input) {
        String[] components = input.readUTF().split("#VULCAN#");
        String command = components[0];
        VulcanVelocity.INSTANCE.getServer().getCommandManager().executeAsync((CommandSource)VulcanVelocity.INSTANCE.getServer().getConsoleCommandSource(), command);
        String checkName = components[1];
        String checkType = components[2];
        String vl = components[3];
        String player = components[4];
        String maxVl = components[5];
        String serverName = connection.getServerInfo().getName();
        TextComponent punishmentMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(Config.PUNISHMENT_FORMAT.replace("%check%", checkName).replace("%type%", checkType).replace("%vl%", vl).replace("%player%", player).replace("%max-vl%", maxVl).replace("%server%", serverName).replace("%prefix%", Config.PREFIX));
        for (Player staff : this.alertsEnabled) {
            if (Config.DONT_SEND_ALERTS_TO_SAME_SERVER && staff.getCurrentServer().isPresent() && ((ServerConnection)staff.getCurrentServer().get()).getServerInfo().getName().equals(serverName))
                continue;
            staff.sendMessage((Component)punishmentMessage);
        }
    }

    public Set<Player> getAlertsEnabled() {
        return this.alertsEnabled;
    }
}
