package net.risenphoenix.ipcheck.events;

import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.LocalizationManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RejoinNotification {

    private IPCheck ipc;
    private LocalizationManager local;
    private Player player;

    public RejoinNotification(IPCheck ipc, Player player) {
        this.ipc = ipc;
        this.local = ipc.getLocalizationManager();
        this.player = player;

        this.execute();
    }

    private void execute() {
        Player[] online = ipc.getOnlinePlayers();

        for (int i = 0; i < online.length; i++) {
            displayWarning(online[i]);
        }
    }

    private void displayWarning(Player p) {
        if (p.hasPermission("ipcheck.getnotify") || p.isOp()) {
            ipc.sendPlayerMessage(p, ChatColor.DARK_GRAY +
                    "---------------------------------------", false);

            ipc.sendPlayerMessage(p, ChatColor.RED +
                    local.getLocalString("REJOIN_WARN") + " " +
                    ChatColor.LIGHT_PURPLE + player.getDisplayName() +
                    ChatColor.RED + local.getLocalString("REJOIN_EXPLAIN"));

            ipc.sendPlayerMessage(p, ChatColor.DARK_GRAY +
                    "---------------------------------------", false);
        }
    }
}
