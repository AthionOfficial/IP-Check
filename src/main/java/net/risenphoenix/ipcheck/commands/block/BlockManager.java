package net.risenphoenix.ipcheck.commands.block;

import com.maxmind.geoip.LookupService;
import net.risenphoenix.ipcheck.IPCheck;

import java.util.List;

public class BlockManager {

	private boolean isEnabled = false;

	private IPCheck ipc;
	private LookupService ls;

	public BlockManager(IPCheck ipc) {
		this.ipc = ipc;

		if (ipc.getConfig().getBoolean("use-geoip-services")) {
			this.isEnabled = true;
			this.ls = ipc.getGeoIPObject().getLookupService();
		}
	}

	public boolean getStatus() {
		return this.isEnabled;
	}

	public String getCountry(String ip) {
		if (!isEnabled) return null;
		if (ls != null) {
			return ls.getCountry(ip).getName();
		} else { return null; }
	}

	public String getCountryID(String ip) {
		if (!isEnabled) return null;
		if (ls != null) {
			return ls.getCountry(ip).getCode();
		} else { return null; }
	}

	// Method Accepts Two-Character Country ID
	public boolean isBlockedCountry(String country) {
		// Fetch Block List
		List<String> c_block = ipc.getConfig()
				.getStringList("country-blacklist");

		// Scan Block List for Match
		for (String s : c_block) {
			if (s.equalsIgnoreCase(country)) return true;
		}

		return false;
	}

	// Method Accepts Two-Character Country ID
	public boolean blockCountry(String country) {
		// Fetch Block List
		List<String> c_block = ipc.getConfig()
				.getStringList("country-blacklist");

		// Fetch Addition Result
		boolean result = !c_block.contains(country.toLowerCase());
		if (result) c_block.add(country.toLowerCase());

		// Store Configuration Value
		ipc.getConfig().set(
				"country-blacklist", c_block);

		return result;
	}

	// Method Accepts Two-Character Country ID
	public boolean unblockCountry(String country) {
		// Fetch Block List
		List<String> c_block = ipc.getConfig()
				.getStringList("country-blacklist");

		// Fetch Removal Result
		boolean result = c_block.contains(country.toLowerCase());
		if (result) c_block.remove(country.toLowerCase());

		// Store Configuration Value
		ipc.getConfig().set(
				"country-blacklist", c_block);

		return result;
	}

	public LookupService getLookupService() {
		return this.ls;
	}
}
