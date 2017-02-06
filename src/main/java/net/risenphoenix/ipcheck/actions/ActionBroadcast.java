package net.risenphoenix.ipcheck.actions;

import net.risenphoenix.ipcheck.IPCheck;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class ActionBroadcast {

    private IPCheck ipc;

    private String message;
    private String[] values = null;
    private Permission[] requiredPerms = null;
    private boolean useName;

    public ActionBroadcast(String message, boolean useName) {
        this.message = message;
        this.ipc = IPCheck.getInstance();
        this.useName = useName;
    }

    public ActionBroadcast(String message, Permission[] perms, boolean useName) {
        this.message = message;
        this.requiredPerms = perms;
        this.ipc = IPCheck.getInstance();
        this.useName = useName;
    }

    public ActionBroadcast(String message, String[] values, boolean useName) {
        this.message = message;
        this.values = values;
        this.ipc = IPCheck.getInstance();
        this.useName = useName;
    }

    public ActionBroadcast(String message, String[] values, Permission[] perms,
                           boolean useName) {
        this.message = message;
        this.values = values;
        this.requiredPerms = perms;
        this.ipc = IPCheck.getInstance();
        this.useName = useName;
    }

    public void execute() {
        // Fetch Online Players
        Player[] players = ipc.getOnlinePlayers();

        // Get Final Message
        String finalMsg = this.formatMessage();

        // Broadcast
        for (Player p : players) {
            // Check if Permissions are required, and if so check for them
            if (requiredPerms != null) {
                if (!hasPermission(p)) continue;
            }

            // Display Message
            this.ipc.sendPlayerMessage(p, finalMsg, this.useName);
        }

        // Send Message to Console
        Bukkit.getConsoleSender().sendMessage(finalMsg);
    }

    private String formatMessage() {
        return String.format(this.message, this.values);
    }

    private boolean hasPermission(Player player) {
        // Check for OP Status
        if (player.isOp()) return true;

        // If not OP, check for permissions
        for (Permission p : this.requiredPerms) {
            if (!player.hasPermission(p)) return false;
        }

        // If the check passed, return true
        return true;
    }
}
