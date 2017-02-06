package net.risenphoenix.ipcheck.commands;

import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.database.DatabaseController;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdPurge extends Command {

    private static DatabaseController db;

    public CmdPurge(final IPCheck plugin, String[] callArgs, CommandType type) {
        super(plugin, callArgs, type);

        CmdPurge.db = IPCheck.getInstance().getDatabaseController();

        setName(getLocalString("CMD_PURGE"));
        setHelp(getLocalString("HELP_PURGE"));
        setSyntax("ipc purge <PLAYER | IP>");
        setPermissions(new Permission[]{
                new Permission("ipcheck.use"),
                new Permission("ipcheck.purge")
        });
    }

    public static void cmd(CommandSender sender, String[] args, IPCheck plugin) {
        // Regex for differentiating between IP and username
        String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";

        // If the argument is an IP-Address, validate it
        if (args[1].matches(ip_filter)) {
            if (db.isValidIP(args[1])) {
                // Purge the IP and send success message.
                db.purgeIP(args[1]);
                sender.sendMessage(String.format(
                        plugin.getLocalizationManager().getLocalString("PURGE_SUC"), args[1]));
            } else {
                // Report if the IP was not found in the database.
            	sender.sendMessage(plugin.getLocalizationManager().getLocalString("NO_FIND"));
            }

        // If the argument is a Player, validate it
        } else {
            if (db.isValidPlayer(args[1])) {
                // Purge the player and send success message.
                db.purgePlayer(args[1]);
                sender.sendMessage(String.format(
                        plugin.getLocalizationManager().getLocalString("PURGE_SUC"), args[1]));
            } else {
                // Report if the Player was not found in the database.
            	sender.sendMessage(plugin.getLocalizationManager().getLocalString("NO_FIND"));
            }
        }
    }
}
