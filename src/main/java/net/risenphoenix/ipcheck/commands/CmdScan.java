package net.risenphoenix.ipcheck.commands;

import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.objects.IPObject;
import net.risenphoenix.ipcheck.objects.UserObject;
import net.risenphoenix.ipcheck.util.ListFormatter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;

public class CmdScan extends Command {

    public CmdScan(final IPCheck plugin, String[] callArgs, CommandType type) {
        super(plugin, callArgs, type);

        // Initialize IPC variable

        setName(this.getLocalString("CMD_SCAN"));
        setHelp(this.getLocalString("HELP_SCAN"));
        setSyntax("ipc scan");
        setPermissions(new Permission[]{new Permission("ipcheck.use"),
                new Permission("ipcheck.scan")});
    }

    public static void cmd(CommandSender sender, String[] args, IPCheck plugin) {
        // Method Variables
        Player[] online = plugin.getOnlinePlayers();
        ArrayList<Player> detected = new ArrayList<Player>();

        // Loop through online players
        for (Player p : online) {
            // Create Store for Unique Accounts linked to this player
            ArrayList<String> unique_names = new ArrayList<String>();

            // Fetch User Object for this player
            UserObject user = plugin.getDatabaseController().getUserObject(p.getName());

            // If no IPs were found for this user, skip them and continue
            if (user.getNumberOfIPs() == 0) continue;

            // Fetch IP Objects from User Object
            ArrayList<IPObject> ipos = new ArrayList<IPObject>();

            for (String ip : user.getIPs()) {
                ipos.add(plugin.getDatabaseController().getIPObject(ip));
            }

            // Get Unique Accounts from IP Objects
            for (IPObject ipo : ipos) {

                /* If IP only has one linked user and that user is the name of
                 * the player we're currently checking, skip it. */
                if (ipo.getNumberOfUsers() == 1) {
                    if (ipo.getUsers().contains(p.getName()
                            .toLowerCase())) continue;
                }

                // Log Unique Account Names to the Storage ArrayList
                for (String s : ipo.getUsers()) {
                    if (!s.equalsIgnoreCase(p.getName())) {
                        if (!unique_names.contains(s.toLowerCase())) {
                            unique_names.add(s.toLowerCase());
                        }
                    }
                }
            }

            /* If multiple accounts were found for the user, add them to the
             * detection queue */
            if (unique_names.size() > 0) detected.add(p);
        }

        // Output Results to Sender
        if (detected.size() > 0) {
            // Convert ArrayList to type String for use with ListFormatter
            ArrayList<String> convert = new ArrayList<String>();
            for (Player p : detected) convert.add(p.getName());

            // Output Header
            sender.sendMessage(ChatColor.DARK_GRAY +
                    "------------------------------------------------");
            sender.sendMessage(ChatColor.RED +
                    plugin.getLocalizationManager().getLocalString("SCAN_TITLE"));
            sender.sendMessage(ChatColor.DARK_GRAY +
                    "------------------------------------------------");

            // Fetch Formatted List
            StringBuilder list = new ListFormatter(convert).getFormattedList();

            // Display Results
            sender.sendMessage(list.toString());
            sender.sendMessage(ChatColor.DARK_GRAY +
                    "------------------------------------------------");
        } else {
        	sender.sendMessage(plugin.getLocalizationManager().getLocalString("SCAN_CLEAN"));
        }
    }
}
