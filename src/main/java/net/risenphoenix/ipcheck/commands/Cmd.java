package net.risenphoenix.ipcheck.commands;

import org.bukkit.ChatColor;
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
				if(args.length != 1){
					sender.sendMessage(ChatColor.GREEN + "Check" + ":");
					sender.sendMessage(ChatColor.YELLOW + " Displays information about the player or IP Specified.");
					sender.sendMessage(ChatColor.RED + "    " + "Syntax:" + ChatColor.LIGHT_PURPLE + " /ipc check <PLAYER | IP>");
				}else{
					CmdCheck.cmd(sender, args);
				}
				break;
			case "purge":
				if(args.length != 1){
					sender.sendMessage(ChatColor.GREEN + "Purge" + ":");
					sender.sendMessage(ChatColor.YELLOW + " Deletes records of the IP or Player name specified.");
					sender.sendMessage(ChatColor.RED + "    " + "Syntax:" + ChatColor.LIGHT_PURPLE + " /ipc purge <PLAYER | IP>");
				}else{
					CmdPurge.cmd(sender, args, plugin);
				}
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
				if(args.length != 1){
					sender.sendMessage(ChatColor.GREEN + "Check" + ":");
					sender.sendMessage(ChatColor.YELLOW + " Toggles the specified option. For a list of options, type ''/ipc toggle help''");
					sender.sendMessage(ChatColor.RED + "    " + "Syntax:" + ChatColor.LIGHT_PURPLE + " /ipc toggle <OPTION_ID | help>");
				}else{
					CmdToggle.cmd(sender, args, plugin);
				}
				break;
			default:
				CmdHelp.cmd(sender, args, plugin);
			}
		}

		return false;
	}

}
