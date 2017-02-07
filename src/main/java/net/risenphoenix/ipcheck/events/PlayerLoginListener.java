package net.risenphoenix.ipcheck.events;

import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.actions.ActionBroadcast;
import net.risenphoenix.ipcheck.database.DatabaseController;
import net.risenphoenix.ipcheck.objects.DateObject;
import net.risenphoenix.ipcheck.objects.IPObject;
import net.risenphoenix.ipcheck.objects.UserObject;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;

public class PlayerLoginListener {

	private IPCheck ipc;
	private DatabaseController db;
	private PlayerLoginEvent e;

	public PlayerLoginListener(IPCheck ipc, PlayerLoginEvent e) {
		this.ipc = ipc;
		this.db = ipc.getDatabaseController();
		this.e = e;

		this.execute();
	}

	public void execute() {
		// Fetch IP Address and Player
		Player player = e.getPlayer();
		String address = e.getAddress().getHostAddress();

		boolean debugAddress = false;

		if (debugAddress)
			ipc.getLogger().info("Address Output: " + address);

		// Log Player and IP
		db.log(player.getName(), address);

		// Stats Link
		ipc.getStatisticsObject().logPlayerJoin(1);

		// If the player was not kicked for having a banned status or a blocked
		// country, check for alt accounts with the database. (Secure Mode Hook)
		boolean shouldCheck = true;

		// Attempt a Secure-Kick if Secure-Mode is enabled
		if (ipc.getConfig().getBoolean("secure-mode")) {
			shouldCheck = this.secureKick(e, address);
		}

		// Perform a Login Notification
		if (ipc.getConfig().getBoolean("notify-on-login") && shouldCheck) {
			IPObject ipo = db.getIPObject(address);
			ArrayList<String> names = new ArrayList<String>();

			for (String s : ipo.getUsers()) {
				if (!names.contains(s.toLowerCase())) {
					names.add(s.toLowerCase());
				}
			}

			// Execute Login Notification
			new LoginNotification(ipc, player, address, names);
		}

	}

	// Attempt to Kick Player in the event of having too many alt accounts
	private boolean secureKick(PlayerLoginEvent e, String ip) {
		String player = e.getPlayer().getName();
		ArrayList<String> names = this.getUniqueAccounts(player);

		int threshold = ipc.getConfig().getInt("secure-kick-threshold");

		// If the number of accounts is greater than the threshold, and the
		// player-name and IP are both non-exempt, then check if the account
		// may log in.
		if (names.size() > threshold) {

			ArrayList<DateObject> dates = new ArrayList<DateObject>();

			for (String name : names) {
				dates.add(new DateObject(name, db.getLogTime(name)));
			}

			DateObject[] exempt = new DateObject[threshold];

			for (int i = 0; i < threshold; i++) {
				// By default set the value to the first entry in the ArrayList
				exempt[i] = dates.get(0);

				// Check for any dates occurring before the date of index 0
				for (DateObject d : dates) {
					if (d.getDate().before(exempt[i].getDate())) exempt[i] = d;
				}

				// Remove the latest entry from the Dates ArrayList so as to
				// prevent accidental duplication of entries
				dates.remove(exempt[i]);
			}

			boolean shouldKick = true;

			// See if the player who is logging in is allowed
			for (DateObject d : exempt) {
				if (d.getPlayer().equalsIgnoreCase(player)) shouldKick = false;
			}

			if (shouldKick) {
				// If IPC should ban when performing a Secure-Mode Kick
				e.setKickMessage(ipc.getConfig().getString("secure-kick-message"));

				// Kick Player
				e.setResult(Result.KICK_OTHER);

				return false;
			}
		}

		return true;
	}

	private ArrayList<String> getUniqueAccounts(String player) {
		ArrayList<String> unique_names = new ArrayList<String>();
		UserObject user = db.getUserObject(player);

		if (user.getNumberOfIPs() == 0) return null;

		// Fetch IPObjects from UserObject
		ArrayList<IPObject> ipos = new ArrayList<IPObject>();

		for (String s : user.getIPs()) {
			ipos.add(db.getIPObject(s));
		}

		for (IPObject ipo : ipos) {
			if (ipo.getNumberOfUsers() == 1) {
				if (ipo.getUsers().contains(player.toLowerCase())) continue;
			}

			// Append each account, plus punctuation to the string builder
			for (String s : ipo.getUsers()) {
				if (!unique_names.contains(s.toLowerCase())) {
					unique_names.add(s.toLowerCase());
				}
			}
		}

		return unique_names;
	}

}
