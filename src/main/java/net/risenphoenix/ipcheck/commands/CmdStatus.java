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
				plugin.getLocalizationManager().getLocalString("STATS_LVER") + ChatColor.YELLOW +
				stats.getPluginVersion());

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_DB_TYPE") + ChatColor.YELLOW +
				stats.getDatabaseType());

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_JVER") + ChatColor.YELLOW +
				stats.getJavaVersion());

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_OS") + ChatColor.YELLOW +
				stats.getOperatingSystem());

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_OS_ARCH") + ChatColor.YELLOW +
				stats.getOperatingSystemArch());

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
				plugin.getLocalizationManager().getLocalString("STATS_PEXM") + ChatColor.YELLOW +
				stats.getPlayersExempt());

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_IEXM") + ChatColor.YELLOW +
				stats.getIPsExempt());

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_RPEXM") + ChatColor.YELLOW +
				stats.getPlayersRejoinExempt());

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_RIEXM") + ChatColor.YELLOW +
				stats.getIPsRejoinExempt());

		// Border
		sender.sendMessage(ChatColor.DARK_GRAY +
				"------------------------------------------------");

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_PBAN") + ChatColor.YELLOW +
				stats.getPlayersBanned());

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_IBAN") + ChatColor.YELLOW +
				stats.getIPsBanned());

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_PLOGS") + ChatColor.YELLOW +
				stats.getLogPlayerSession());

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_PBANS") + ChatColor.YELLOW +
				stats.getBannedPlayerSession());

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_PUNBANS") + ChatColor.YELLOW +
				stats.getUnbannedPlayerSession());

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_WARNS") + ChatColor.YELLOW +
				stats.getWarningIssuedSession());

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_KICKS") + ChatColor.YELLOW +
				stats.getKickIssuedSession());

		// Border
		sender.sendMessage(ChatColor.DARK_GRAY +
				"------------------------------------------------");

		String isTrue = ChatColor.GREEN + "True",
				isFalse = ChatColor.RED + "False";

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_SECURE") + ChatColor.YELLOW +
				((stats.getSecureStatus()) ? isTrue : isFalse));

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_ACTIVE") + ChatColor.YELLOW +
				((stats.getActiveStatus()) ? isTrue : isFalse));

		sender.sendMessage(ChatColor.LIGHT_PURPLE +
				plugin.getLocalizationManager().getLocalString("STATS_BLACKLIST") + ChatColor.YELLOW +
				((stats.getBlackListStatus()) ? isTrue : isFalse));

		// Border
		sender.sendMessage(ChatColor.DARK_GRAY +
				"------------------------------------------------");
	}
}
