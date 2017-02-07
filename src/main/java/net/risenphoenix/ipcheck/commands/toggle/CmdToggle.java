package net.risenphoenix.ipcheck.commands.toggle;

import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.commands.Command;
import net.risenphoenix.ipcheck.commands.CommandType;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;

public class CmdToggle extends Command {

    private FileConfiguration config;
    private static ArrayList<ToggleOption> options;

    public CmdToggle(final IPCheck plugin, String[] callArgs, CommandType type) {
        super(plugin, callArgs, type);

        this.config = getPlugin().getConfig();
        CmdToggle.options = new ArrayList<ToggleOption>();

        /* Options Initialization */

        options.add( // Login Notifications
            new ToggleOption(getPlugin(), "notify-on-login", "TOGGLE_NOTIFY",
                new String[]{"login-notify", "notification", "notify"})
        );

        options.add( // Descriptive Notifications
            new ToggleOption(getPlugin(), "descriptive-notice", "TOGGLE_DETAIL",
                new String[]{"detail-notify", "detail", "dn"})
        );

        options.add( // Secure Mode
            new ToggleOption(getPlugin(), "secure-mode", "TOGGLE_SECURE",
                new String[]{"secure-mode", "secure", "sm"})
        );

        options.add( // GeoIP Services
            new ToggleOption(getPlugin(), "use-geoip-services", "TOGGLE_GEOIP",
                new String[]{"geoip-services", "geoip", "gs"})
        );

        // Command Initialization
        setName(getLocalString("CMD_TOGGLE"));
        setHelp(getLocalString("HELP_TOGGLE"));
        setSyntax("ipc toggle <OPTION_ID | help>");
        setPermissions(new Permission[]{new Permission("ipcheck.use"),
                new Permission("ipcheck.toggle")});
    }

    public static void cmd(CommandSender sender, String[] args, IPCheck plugin) {
        ToggleOption option = null;

        // Obtain Applicable ToggleOption
        for (ToggleOption to : CmdToggle.options) {
            String[] callValues = to.getCallValues();

            for (int i = 0; i < callValues.length; i++) {
                if (args[1].equalsIgnoreCase(callValues[i])) {
                    option = to;
                    break;
                }
            }

            if (option != null) break;
        }

        // Help Argument Output
        if (args[1].equalsIgnoreCase("help")) {
        	sender.sendMessage(ChatColor.DARK_GRAY +
                    "---------------------------------------------");

            for (ToggleOption to : CmdToggle.options) {
                StringBuilder sb = new StringBuilder();
                String[] callValues = to.getCallValues();

                sb.append(" " + to.getDisplayID() + ":" + ChatColor.YELLOW +
                        " < | ");

                for (int i = 0; i < callValues.length; i++) {
                    sb.append(ChatColor.LIGHT_PURPLE + callValues[i] +
                            ChatColor.YELLOW + " | ");
                }

                sb.append(ChatColor.YELLOW + ">");
                sender.sendMessage(sb.toString());
            }

            sender.sendMessage(ChatColor.DARK_GRAY +
                    "---------------------------------------------");
            return;
        }

        // If the ToggleOption is null, return
        if (option == null) {
        	sender.sendMessage(plugin.getLocalizationManager().getLocalString("TOGGLE_INVALID"));
            return;
        }

        // If the ToggleOption is not null, execute toggle and fetch return
        Boolean newValue = option.onExecute();
        ChatColor color = (newValue) ? ChatColor.GREEN : ChatColor.RED;

        // Output Result
        sender.sendMessage(option.getDisplayID() + " set to: " + color +
                newValue);
    }
}
