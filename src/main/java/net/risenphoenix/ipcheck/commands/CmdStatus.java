package net.risenphoenix.ipcheck.commands;

import net.risenphoenix.commons.commands.CommandType;
import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.objects.StatsObject;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdStatus extends Command {

	public CmdStatus(final IPCheck plugin, String[] callArgs, CommandType type) {
		super(plugin, callArgs, type);

		setName(getLocalString("CMD_STATUS"));
		setHelp(getLocalString("HELP_STATUS"));
		setSyntax("ipc status");
		setPermissions(new Permission[]{new Permission("ipcheck.use")});
	}

	public static void cmd(CommandSender sender, String[] args, IPCheck plugin) {
		// Stats Object
		StatsObject stats = IPCheck.getInstance().getStatisticsObject();

		// Border
		sender.sendMessage(ChatColor.DARK_GRAY +
				"------------------------------------------------");

		sender.sendMessage(plugin.getLocalizationManager().getLocalString("STATS_HEADER"));

		// Border
		sender.sendMessage(ChatColor.DARK_GRAY +
				"------------------------------------------------");

		// IPC Ver.
		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_PVER") + ChatColor.YELLOW +
				stats.getPluginVersion());

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_DB_TYPE") + ChatColor.YELLOW +
				stats.getDatabaseType());

		// Border
		sender.sendMessage(ChatColor.DARK_GRAY +
				"------------------------------------------------");

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_PLOG") + ChatColor.YELLOW +
				stats.getPlayersLogged());

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_ILOG") + ChatColor.YELLOW +
				stats.getIPsLogged());

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_PLOGS") + ChatColor.YELLOW +
				stats.getLogPlayerSession());

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_WARNS") + ChatColor.YELLOW +
				stats.getWarningIssuedSession());

		String isTrue = ChatColor.GREEN + "True",
				isFalse = ChatColor.RED + "False";

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_SECURE") + ChatColor.YELLOW +
				((stats.getSecureStatus()) ? isTrue : isFalse));
	
		// Border
		sender.sendMessage(ChatColor.DARK_GRAY +
				"------------------------------------------------");
	}
}
