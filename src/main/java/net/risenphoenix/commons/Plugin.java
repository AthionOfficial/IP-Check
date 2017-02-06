package net.risenphoenix.commons;

import net.risenphoenix.commons.commands.*;
import net.risenphoenix.commons.configuration.ConfigurationManager;
import net.risenphoenix.ipcheck.LocalizationManager;
import net.risenphoenix.ipcheck.commands.Command;
import net.risenphoenix.ipcheck.commands.CommandManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.logging.Level;

public class Plugin extends JavaPlugin {

    private LocalizationManager LM;
    private CommandManager CM;

    private String pluginName;
    private ChatColor pluginColor = ChatColor.GOLD;
    private ChatColor messageColor = ChatColor.WHITE;

	/* Required Methods for Bukkit JavaPlugin. */

    @Override
    public final void onEnable() {
        this.pluginName = "[" + this.getDescription().getName() + "] ";

        // Must make direct call to Plugin configuration as Config-Manager is
        // not initialized at this point in the execution.
        this.LM = new LocalizationManager(this, this.getConfig().getString("language"));
        this.CM = new CommandManager(this);
        this.onStartup();
    }

    @Override
    public final void onDisable() {
        this.onShutdown();
    }

    @Override
    public final boolean onCommand(CommandSender sender,
                                   org.bukkit.command.Command root,
                                   String commandLabel, String[] args) {

        /* THIS CODE IS REQUIRED FOR THE COMMAND PARSER TO WORK CORRECTLY */
        // Append Command Root to the list of arguments
        List<String> argsPre = new LinkedList<String>(Arrays.asList(args));
        argsPre.add(0, root.getName());

        // Convert back to Array
        String[] argsFinal = new String[argsPre.size()];
        argsFinal = argsPre.toArray(argsFinal);
        /* THIS CODE IS REQUIRED FOR THE COMMAND PARSER TO WORK CORRECTLY */

        // Parse
        ParseResult pResult = this.CM.parseCommand(argsFinal);

        // If the Parser returned a Command
        if (pResult.getResult() == ResultType.SUCCESS) {
            /*
             * A new command instance must be created when a command is called,
             * so as to prevent static commands from being served to players.
             * This prevents any form of data swappage between two different
             * players executing the same command at the same time.
             */

            // New Command Instance.
            Command cmd = null;

            // The dirty dynamic hack :D
            try {
                // Fetch Class Type
                Class<?> clazz = Class.forName(
                        pResult.getCommand().getClass().getName());

                // Create the Constructor for the Command class. This works
                // only because all commands extend Command, which requires
                // these three arguments to be constructed.
                Constructor<?> ctor = clazz.getConstructor(
                        Plugin.class, String[].class, CommandType.class);

                // Create the specified Command Instance
                cmd = (Command) ctor.newInstance(this,
                        pResult.getCommand().getCallArgs(),
                        pResult.getCommand().getType());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // If Player is calling this command, check Permissions and execute
            if (cmd != null) {
                if (sender instanceof Player) {
                    if (!cmd.onCall(sender, args)) {
                        this.sendPlayerMessage(sender,
                                this.LM.getLocalString("PERM_ERR"));
                    }

                    // If Console is calling Command, check if Command is executable
                } else {
                    if (!cmd.canConsoleExecute()) {
                        this.sendConsoleMessage(Level.INFO,
                                this.LM.getLocalString("NO_CONSOLE"));
                    } else {
                        cmd.onCall(sender, args);
                    }
                }
            } else {
                sendPlayerMessage(sender, getLocalizationManager()
                                .getLocalString("CMD_NULL_ERR"));
            }

            // If the Parser returned a Command, but the Argument count was bad
        } else if (pResult.getResult() == ResultType.BAD_NUM_ARGS) {
            this.sendPlayerMessage(sender,
                    this.LM.getLocalString("NUM_ARGS_ERR"));

            // If the Parser did not return a Command
        } else if (pResult.getResult() == ResultType.FAIL) {
            this.sendPlayerMessage(sender, this.LM.getLocalString("NO_CMD"));
        }

        return true;
    }

	/* Library Specific Methods */

    public void onStartup() {
        throw new UnsupportedOperationException(
                this.LM.getLocalString("NO_IMPLEMENT"));
    }

    public void onShutdown() {
        throw new UnsupportedOperationException(
                this.LM.getLocalString("NO_IMPLEMENT"));
    }

    public final Map<String, String> getVersionInfo() {
        Map<String, String> info = new HashMap<String, String>();

        info.put("NAME", "RP-Commons");
        info.put("VERSION", "v1.05");
        info.put("AUTHOR", "Jacob Keep");
        info.put("BUILD", "93");
        info.put("JVM_COMPAT", "1.7");

        return info;
    }

    public final LocalizationManager getLocalizationManager() {
        return this.LM;
    }

    // Can be over-ridden in order to avoid Bukkit contaminating YML files

    public final CommandManager getCommandManager() {
        return this.CM;
    }

    public final void setPluginName(ChatColor color, String name) {
        this.pluginName = "[" + name + "] ";
        this.pluginColor = color;
    }

    public final void setMessageColor(ChatColor color) {
        this.messageColor = color;
    }

    public final void sendPlayerMessage(CommandSender sender, String message) {
        sendPlayerMessage(sender, message, true);
    }

    public final void sendPlayerMessage(CommandSender sender, String message,
                                        boolean useName) {
        sender.sendMessage(((useName) ? this.pluginColor + pluginName : "") +
                this.messageColor + message);
    }

    public final void sendConsoleMessage(Level level, String message) {
        Bukkit.getLogger().log(level, pluginName + message);
    }

    public final String formatPlayerMessage(String msg) {
        return (this.pluginColor + pluginName + this.messageColor + msg);
    }
}