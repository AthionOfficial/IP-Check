package net.risenphoenix.ipcheck.commands;

import net.risenphoenix.commons.commands.CommandType;
import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.objects.ReportObject;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdCheck extends Command {

    public CmdCheck(IPCheck plugin, String[] callArgs, CommandType type) {
        super(plugin, callArgs, type);

        setName("Check");
        setHelp(this.getLocalString("HELP_CHECK"));
        this.setSyntax("ipc <PLAYER | IP>");
        this.setPermissions(new Permission[]{new Permission("ipcheck.use")});
    }

    public static void cmd(CommandSender sender, String[] args) {
        new ReportObject(IPCheck.getInstance()).onExecute(sender, args[0]);
    }

}
