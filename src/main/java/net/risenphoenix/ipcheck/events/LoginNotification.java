package net.risenphoenix.ipcheck.events;

import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.LocalizationManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class LoginNotification {

	private IPCheck ipc;
	private FileConfiguration config;
	private LocalizationManager local;

	private Player player;
	private String ip;
	private ArrayList<String> accounts;

	public LoginNotification(IPCheck ipc, Player player, String ip,
			ArrayList<String> accounts) {
		this.ipc = ipc;
		this.config = ipc.getConfig();
		this.local = ipc.getLocalizationManager();

		this.player = player;
		this.ip = ip;
		this.accounts = accounts;

		this.execute();
	}

	private void execute() {
		Player[] online = ipc.getOnlinePlayers();
		int threshold = config.getInt("min-account-notify-threshold");
		int acctNum = accounts.size();

		// If the player has more accounts than the set threshold
		if (acctNum > threshold) {

			// If the player and their IP are both non-exempt

			// Stats Link
			ipc.getStatisticsObject().logWarningIssue(1);

			for (Player anOnline : online) {
				displayReport(anOnline);
			}
		}
	}

	private void displayReport(Player p) {
		if (p.hasPermission("ipcheck.getnotify") || p.isOp()) {
			if (config.getBoolean("descriptive-notice")) {

				// Notification Head
				ipc.sendPlayerMessage(p, ChatColor.DARK_GRAY +
						"------------------------------------------------",
						false);
				ipc.sendPlayerMessage(p, "Report for: " +
						ChatColor.LIGHT_PURPLE + player.getName(), false);
				ipc.sendPlayerMessage(p, ChatColor.DARK_GRAY +
						"------------------------------------------------",
						false);

				// Notification Body
				if (p.hasPermission("ipcheck.showip") || p.isOp()) {
					ipc.sendPlayerMessage(p, "IP Address: " +
							ChatColor.LIGHT_PURPLE + ip, false);
				}

				ipc.sendPlayerMessage(p, ChatColor.LIGHT_PURPLE +
						player.getName() + ChatColor.RED +
						" was found to have " + ChatColor.YELLOW + accounts +
						ChatColor.RED + " possible alternative accounts. " +
						"Perform command " + ChatColor.LIGHT_PURPLE + "'/ipc " +
						player.getDisplayName() + "'" + ChatColor.RED +
						" for " + "more information.", false);

				ipc.sendPlayerMessage(p, ChatColor.DARK_GRAY +
						"---------------------------------------", false);
			} else {

				ipc.sendPlayerMessage(p, ChatColor.DARK_GRAY +
						"---------------------------------------", false);

				ipc.sendPlayerMessage(p, ChatColor.RED +
						local.getLocalString("LOGIN_WARN") + " " +
						ChatColor.LIGHT_PURPLE + player.getDisplayName() +
						ChatColor.RED + local.getLocalString("LOGIN_EXPLAIN"));

				ipc.sendPlayerMessage(p, ChatColor.DARK_GRAY +
						"---------------------------------------", false);
			}
		}
	}

}
