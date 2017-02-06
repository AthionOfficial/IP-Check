package net.risenphoenix.ipcheck.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.commands.toggle.CmdToggle;

public class Cmd implements CommandExecutor {

	public IPCheck plugin;

	public Cmd(IPCheck plugin){
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(args.length == 0){
			CmdHelp.cmd(sender, args, plugin);
		}else{
			switch(args[0].toLowerCase()){
			case "help":
				CmdHelp.cmd(sender, args, plugin);
				break;
			case "check":
				CmdCheck.cmd(sender, args);
				break;
			case "purge":
				CmdPurge.cmd(sender, args, plugin);
				break;
			case "reload":
				CmdReload.cmd(sender, args, plugin);
				break;
			case "scan":
				CmdScan.cmd(sender, args, plugin);
				break;
			case "status":
				CmdStatus.cmd(sender, args, plugin);
				break;
			case "toggle":
				CmdToggle.cmd(sender, args, plugin);
				break;
			default:
				CmdHelp.cmd(sender, args, plugin);
			}
		}

		return false;
	}

}
