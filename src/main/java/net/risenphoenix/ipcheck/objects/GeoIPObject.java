package net.risenphoenix.ipcheck.objects;

import com.maxmind.geoip.LookupService;
import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.LocalizationManager;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

import org.bukkit.configuration.file.FileConfiguration;

public class GeoIPObject {

	private LookupService ls = null;

	private IPCheck ipc;
	private LocalizationManager LM;
	private FileConfiguration CM;

	public GeoIPObject(final IPCheck ipc) {
		this.ipc = ipc;
		this.LM = ipc.getLocalizationManager();
		this.CM = ipc.getConfig();

		initializeDatabase();
	}

	private void initializeDatabase() {
		File database;

		// If the user has not opt-out of the GeoIP services, initialize the
		// GeoIP Database.
		if (CM.getBoolean("use-geoip-services")) {
			database = new File(ipc.getDataFolder(), "GeoIP.dat");
		} else {
			return;
		}

		// If the database is not found, instruct the user on how to acquire it.
		if (!database.exists()) {
			// Attempt to download database
			downloadDatabase();

			if (!database.exists()) {
				ipc.getLogger().severe(
						LM.getLocalString("GEOIP_DB_MISSING"));
				return;
			}
		}

		// Attempt to initialize the Lookup Services
		try {
			ls = new LookupService(database);
		} catch (IOException e) {
			ipc.getLogger().severe(LM.getLocalString("GEOIP_DB_READ_ERR"));
		}
	}

	private void downloadDatabase() {
		String URL = "http://geolite.maxmind.com/download/geoip/database/" +
				"GeoLiteCountry/GeoIP.dat.gz";

		if (CM.getBoolean("allow-geoip-download")) {
			ipc.getLogger().info(
					LM.getLocalString("GEOIP_DOWNLOAD"));

			try {
				URL dURL = new URL(URL);
				URLConnection conn = dURL.openConnection();

				conn.setConnectTimeout(8000);
				conn.connect();

				InputStream input = new GZIPInputStream(conn.getInputStream());

				File databaseLoc = new File(ipc.getDataFolder() + "/GeoIP.dat");

				OutputStream output = new FileOutputStream(databaseLoc);

				byte[] dlBuffer = new byte[2048];
				int length = input.read(dlBuffer);
				while (length >= 0) {
					output.write(dlBuffer, 0, length);
					length = input.read(dlBuffer);
				}

				output.close();
				input.close();
			} catch (IOException e) {
				ipc.getLogger().severe( e.getMessage());
			}
		}
	}

	public LookupService getLookupService() {
		return this.ls;
	}

}
