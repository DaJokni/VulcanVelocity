package wtf.jaren.vulcan.velocity.command;

import com.velocitypowered.api.command.CommandInvocation;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import wtf.jaren.vulcan.velocity.VulcanVelocity;

public class AlertsCommand implements RawCommand {
    public void execute(RawCommand.Invocation invocation) {
        if (invocation.source() instanceof Player) {
            Player player = (Player)invocation.source();
            VulcanVelocity.INSTANCE.getAlertManager().toggleAlerts(player);
        } else {
            invocation.source().sendMessage((Component)Component.text("This command cannot be executed from console!"));
        }
    }

    public boolean hasPermission(RawCommand.Invocation invocation) {
        return invocation.source().hasPermission("vulcanbungee.alerts");
    }
}
