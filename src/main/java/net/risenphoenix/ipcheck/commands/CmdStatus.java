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

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        // Stats Object
        StatsObject stats = IPCheck.getInstance().getStatisticsObject();

        // Border
        getPlugin().sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);

        sendPlayerMessage(sender, getLocalString("STATS_HEADER"));

        // Border
        getPlugin().sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);

        // IPC Ver.
        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_PVER") + ChatColor.YELLOW +
                stats.getPluginVersion(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_LVER") + ChatColor.YELLOW +
                stats.getPluginVersion(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_DB_TYPE") + ChatColor.YELLOW +
                stats.getDatabaseType(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_JVER") + ChatColor.YELLOW +
                stats.getJavaVersion(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_OS") + ChatColor.YELLOW +
                stats.getOperatingSystem(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_OS_ARCH") + ChatColor.YELLOW +
                stats.getOperatingSystemArch(), false);

        // Border
        getPlugin().sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_PLOG") + ChatColor.YELLOW +
                stats.getPlayersLogged(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_ILOG") + ChatColor.YELLOW +
                stats.getIPsLogged(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_PEXM") + ChatColor.YELLOW +
                stats.getPlayersExempt(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_IEXM") + ChatColor.YELLOW +
                stats.getIPsExempt(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_RPEXM") + ChatColor.YELLOW +
                stats.getPlayersRejoinExempt(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_RIEXM") + ChatColor.YELLOW +
                stats.getIPsRejoinExempt(), false);

        // Border
        getPlugin().sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_PBAN") + ChatColor.YELLOW +
                stats.getPlayersBanned(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_IBAN") + ChatColor.YELLOW +
                stats.getIPsBanned(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_PLOGS") + ChatColor.YELLOW +
                stats.getLogPlayerSession(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_PBANS") + ChatColor.YELLOW +
                stats.getBannedPlayerSession(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_PUNBANS") + ChatColor.YELLOW +
                stats.getUnbannedPlayerSession(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_WARNS") + ChatColor.YELLOW +
                stats.getWarningIssuedSession(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_KICKS") + ChatColor.YELLOW +
                stats.getKickIssuedSession(), false);

        // Border
        getPlugin().sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);

        String isTrue = ChatColor.GREEN + "True",
               isFalse = ChatColor.RED + "False";

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_SECURE") + ChatColor.YELLOW +
                ((stats.getSecureStatus()) ? isTrue : isFalse), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_ACTIVE") + ChatColor.YELLOW +
                ((stats.getActiveStatus()) ? isTrue : isFalse), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_BLACKLIST") + ChatColor.YELLOW +
                ((stats.getBlackListStatus()) ? isTrue : isFalse), false);

        // Border
        getPlugin().sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);
    }
}
