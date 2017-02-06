package net.risenphoenix.ipcheck.commands;

import net.risenphoenix.ipcheck.IPCheck;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdReload extends Command {

    public CmdReload(final IPCheck plugin, String[] callArgs, CommandType type) {
        super(plugin, callArgs, type);

        setName(getLocalString("CMD_RELOAD"));
        setHelp(getLocalString("HELP_RELOAD"));
        setSyntax("ipc reload");
        setPermissions(new Permission[]{new Permission("ipcheck.use"),
                new Permission("ipcheck.reload")});
    }

    public static void cmd(CommandSender sender, String[] args, IPCheck plugin) {
        plugin.onDisable();
        plugin.onEnable();
        sender.sendMessage(plugin.getLocalizationManager().getLocalString("RELOAD"));
    }

}
