package net.risenphoenix.jnk.ipcheck.commands.exempt.list;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.risenphoenix.jnk.ipcheck.Configuration;
import net.risenphoenix.jnk.ipcheck.commands.IpcCommand;

public class CmdExmtListPlayer implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		ArrayList<String> list = Configuration.getPlayerExemptList();
		StringBuilder sb = new StringBuilder();

		sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
		
		for (String s:list) {
			sb.append(s + ", ");
		}

		sender.sendMessage(sb.toString());
		
		sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
		sender.sendMessage(ChatColor.YELLOW + "Total players in exemption list: " + ChatColor.LIGHT_PURPLE + list.size());
		sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
	}

	@Override
	public int getID() {
		return 9;
	}

}
