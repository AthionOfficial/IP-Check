package net.risenphoenix.ipcheck;

import net.risenphoenix.ipcheck.commands.Cmd;
import net.risenphoenix.ipcheck.commands.CommandManager;
import net.risenphoenix.ipcheck.commands.block.BlockManager;
import net.risenphoenix.ipcheck.database.DatabaseController;
import net.risenphoenix.ipcheck.events.PlayerLoginListener;
import net.risenphoenix.ipcheck.objects.GeoIPObject;
import net.risenphoenix.ipcheck.objects.StatsObject;
import net.risenphoenix.ipcheck.stores.CmdStore;
import net.risenphoenix.ipcheck.stores.LocaleStore;
import net.risenphoenix.ipcheck.util.DateStamp;
import net.risenphoenix.ipcheck.util.Messages;
import net.risenphoenix.ipcheck.util.Metrics;
import net.risenphoenix.ipcheck.util.Updater;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Collection;

public class IPCheck extends JavaPlugin implements Listener {

	private CommandManager CM;
	private LocalizationManager LM;

	// Instance and Main System Objects
	private static IPCheck instance;
	private DatabaseController dbController;

	// Metrics Objects
	private Metrics metrics;

	// Statistics Object
	private StatsObject statsObject;

	// GeoIP Service Objects
	private GeoIPObject geoIPOBject = null;
	private BlockManager blockManager = null;


	// Control used mainly in the event of an in-plugin Reload.
	private boolean hasRegistered = false;

	private String vConfig = "3";

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent e) {
		new PlayerLoginListener(this, e);
	}

	@Override
	public void onEnable() {

		// Initialize Configuration
		this.saveDefaultConfig();
		if(!this.getConfig().getString("config-version").equalsIgnoreCase(vConfig)){
			this.getLogger().severe("Config version mismatch. Make your config again!");
		}

		this.CM = new CommandManager(this);

		instance = this;

		// Register the Player Login Listener
		if (!hasRegistered) {
			getServer().getPluginManager().registerEvents(this, this);
			this.hasRegistered = true; // Prevents Re-registration with Bukkit
		}

		this.getCommand("ipc").setExecutor(new Cmd(this));

		this.LM = new LocalizationManager(this, this.getConfig().getString("language"));

		LocaleStore locStore = new LocaleStore(this);
		getLocalizationManager().appendLocalizationStore(locStore);

		// Initialize GeoIP Services
		if (this.getConfig().getBoolean("use-geoip-services")) {
			this.geoIPOBject = new GeoIPObject(this);
		}

		this.blockManager = new BlockManager(this);

		// Initialize Database Controller
		if(this.getConfig().getBoolean("use-mysql")) {
			// MySQL Database Initialization
			this.getLogger().info("Initialising MySQL Database");
			dbController = new DatabaseController(this,
					this.getConfig().getString("dbHostname"),
					this.getConfig().getInt("dbPort"),
					this.getConfig().getString("dbName"),
					this.getConfig().getString("dbUsername"),
					this.getConfig().getString("dbPassword")
					);
		} else {
			// SQLite Database Initialization
			this.getLogger().info("Initialising SQLite Database");
			dbController = new DatabaseController(this);
		}

		// Initialize Commands
		CmdStore cmdStore = new CmdStore(this);
		this.getCommandManager().registerStore(cmdStore);

		// Initialize Statistics
		this.statsObject = new StatsObject(this);

		// Auto-Update Checker
		if (!getConfig()
				.getBoolean("disable-update-detection")) {
			new Updater(this, 55121, this.getFile(), Updater.UpdateType.DEFAULT, false);
		}

		// Metrics Monitoring
		if (!getConfig()
				.getBoolean("disable-metrics-monitoring")) {
			try {
				metrics = new Metrics(this);
				metrics.start();
			} catch (IOException e) {
				this.getLogger().severe(getLocalizationManager()
						.getLocalString("METRICS_ERR"));
			}
		}

		// Display Random Message
		showRandomMessage();
	}

	@Override
	public void onDisable() {
		dbController.getDatabaseConnection().closeConnection();
	}

	public static IPCheck getInstance() {
		return instance;
	}

	public DatabaseController getDatabaseController() {
		return this.dbController;
	}

	public StatsObject getStatisticsObject() {
		return this.statsObject;
	}

	public GeoIPObject getGeoIPObject() {
		return this.geoIPOBject;
	}

	public BlockManager getBlockManager() {
		return this.blockManager;
	}

	public String getVersion() {
		return "3.0";
	}

	public int getBuildNumber() {
		return 2068;
	}

	private void showRandomMessage() {
		DateStamp ds = new DateStamp();
		String ran = Messages.getSeasonalMessage(ds.getCustomStamp("MM-dd"));

		if (ran != null) {
			this.getLogger().info(ran);
		} else {
			this.getLogger().info(Messages.getRandomMessage());
		}
	}

	public final Player[] getOnlinePlayers() {
		try {
			Collection<? extends Player> result = Bukkit.getOnlinePlayers();
			return result.toArray(new Player[result.size()]);
		} catch (NoSuchMethodError err) {
			this.getLogger().info(getLocalizationManager()
					.getLocalString("VER_COMP_ERR"));
		}

		return new Player[0];
	}

	public final LocalizationManager getLocalizationManager() {
		return this.LM;
	}

	public final void sendPlayerMessage(CommandSender sender, String message) {
		sendPlayerMessage(sender, message, true);
	}

	public final void sendPlayerMessage(CommandSender sender, String message,
			boolean useName) {
		sender.sendMessage(message);
	}

	public final CommandManager getCommandManager() {
		return this.CM;
	}

}
