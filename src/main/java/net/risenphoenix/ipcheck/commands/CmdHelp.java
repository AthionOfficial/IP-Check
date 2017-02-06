package net.risenphoenix.ipcheck.commands;

import net.risenphoenix.commons.commands.CommandType;
import net.risenphoenix.ipcheck.IPCheck;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CmdHelp extends Command {

    public CmdHelp(final IPCheck plugin, String[] callArgs, CommandType type) {
        super(plugin, callArgs, type);

        setName(getLocalString("CMD_HELP"));
        setHelp(getLocalString("HELP_HELP"));
        setSyntax("ipc help [PAGE]");
        setPermissions(new Permission[]{new Permission("ipcheck.use")});
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        // Create a temporal storage area for the commands
        ArrayList<Command> list = new ArrayList<Command>();

        /* Fetch the global command list from the Command Manager. Filter
         * commands by canExecute(). */
        for (Command cmd:this.getPlugin().getCommandManager().getAllCommands()){
            if (cmd.canExecute(sender)) list.add(cmd);
        }

        // Sort List Alphabetically
        List<Command> cmdSort = new ArrayList<Command>(list);
        Collections.sort(cmdSort, new Comparator<Command>() {
            public int compare(Command o1, Command o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        // Return Sorted List to the Global List.
        list = new ArrayList<Command>(cmdSort);

        // Split List into "Pages", which can be viewed by argument.
        int pages = (list.size() / 4);
        if (list.size() % 4 != 0) pages++;

        // Set default Page Number
        int pageNumber = 1;

        // Output Help Display
        sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);

        // Attempt Page Number Parse
        if (args.length == 2) {
            try {
                pageNumber = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sendPlayerMessage(sender, getLocalString("ILL_ARGS_ERR"));
            }
        }

        // Confirm page-number is within bounds.
        if (pageNumber > pages) {
            pageNumber = pages;
        } else if (pageNumber < 1) {
            pageNumber = 1;
        }

        // Create Control Variable for Command Display
        int commandNumber = 0;

        // Output Command Information
        for (Command cmd:list) {
            if (commandNumber >= ((pageNumber - 1) * 4) &&
                    commandNumber < ((pageNumber - 1) * 4) + 4) {
                sendPlayerMessage(sender, ChatColor.GREEN + cmd.getName() + ":",
                        false);
                sendPlayerMessage(sender, ChatColor.YELLOW + " " +
                        cmd.getHelp(), false);
                sendPlayerMessage(sender, ChatColor.RED + "    " + "Syntax:" +
                        ChatColor.LIGHT_PURPLE + " /" + cmd.getSyntax(), false);

                // Blank Space between Commands
                if (commandNumber < (((pageNumber - 1) * 4) + 4) - 1) {
                    sendPlayerMessage(sender, "", false);
                }
            }

            // Step Control Variable
            commandNumber++;
        }

        // Display Information at Bottom of Page w/ Command
        if (pageNumber < pages) {
            sendPlayerMessage(sender, " ", false);
            sendPlayerMessage(sender, ChatColor.RED + "Type " +
                    ChatColor.YELLOW + "/ipc help " + (pageNumber + 1) +
                    ChatColor.RED + " for more information.", false);
        }

        sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);

        // To prevent possible memory leaks
        list.clear();
    }
}
