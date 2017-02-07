package net.risenphoenix.ipcheck;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.risenphoenix.ipcheck.stores.LocalizationStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class LocalizationManager {

	private Map<String, String> translation;
	private Map<String, String> defaultTranslation;
	private String selectedLanguage;
	private FileConfiguration loadedLanguage;

	public LocalizationManager(final IPCheck plugin, String langID) {
		File f = new File(plugin.getDataFolder() + File.separator + langID +".yml");

		if (f.exists()) {
			this.selectedLanguage = langID;
		} else {
			this.selectedLanguage = "en";
			if(!langID.equalsIgnoreCase("en")){ 
				loadDefaultTranslation();
				plugin.getLogger().warning("Translation Index " + langID + ".yml " + "could not be found. Falling back to Default Translation " + "(English).");
			} else {
				f.getParentFile().mkdirs();
				copy(plugin.getResource("en.yml"), f);
			}
		}

		loadTranslation(f);
	}

	private void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while((len=in.read(buf))>0){
				out.write(buf,0,len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final void loadTranslation(File path) {
		if (!this.selectedLanguage.equals("en") || !selectedLanguage.isEmpty()) {
			loadedLanguage = YamlConfiguration.loadConfiguration(path);
			initializeTranslationIndex();
		}

		// Initialize Default Translation both as primary and as fallback.
		loadDefaultTranslation();
	}

	private final void initializeTranslationIndex() {
		this.translation = new HashMap<String, String>();

		for (Map.Entry<String, Object> entry : loadedLanguage.getValues(true).
				entrySet()) {
			this.translation.put(entry.getKey(), entry.getValue().toString());
		}
	}

	private final void loadDefaultTranslation() {
		this.defaultTranslation = new HashMap<String, String>();

		/* Default Translations Required for Framework to Operate Correctly */

		// Command Instance/Manager Messages
		this.defaultTranslation.put("NO_IMPLEMENT", "Base class does not " +
				"implement.");
		this.defaultTranslation.put("CMD_REG_ERR", "Failed to register " +
				"command. Perhaps it is already registered? Command-ID: ");
		this.defaultTranslation.put("NUM_ARGS_ERR", "An incorrect number of " +
				"arguments was specified.");
		this.defaultTranslation.put("ILL_ARGS_ERR", "An illegal argument was " +
				"passed into the command.");
		this.defaultTranslation.put("PERMS_ERR", "You do not have permission " +
				"to execute this command.");
		this.defaultTranslation.put("NO_CONSOLE", "This command cannot be " +
				"executed from Console.");
		this.defaultTranslation.put("NO_CMD", "An invalid command was " +
				"specified.");
		this.defaultTranslation.put("CMD_NULL_ERR", "An error occurred while " +
				"generating a Command Instance. The command has been aborted.");
		this.defaultTranslation.put("BAD_PARSE_SET", "The parse instructions " +
				"for this Parser have not been determined. Please override " +
				"method Parser.parseCommand() in your parsing class.");

		// configuration Manager Messages
		this.defaultTranslation.put("BAD_CFG_SET", "Failed to register " +
				"Configuration Option. Perhaps it is already specified? " +
				"Cfg-ID: ");
		this.defaultTranslation.put("CFG_INIT_ERR", "The Configuration could " +
				"not be refreshed because it has not yet been initialized.");
		this.defaultTranslation.put("FILE_CFG_NULL", "Failed to save " +
				"configuration; FileConfiguration was null.");
		this.defaultTranslation.put("CFG_SRC_NULL", "Failed to save or reload" +
				"configuration; Configuration Source File was null.");
		this.defaultTranslation.put("CFG_SAVE_ERR", "An IOException " +
				"occurred while attempting to save the Configuration " +
				"(check your input values).");
		this.defaultTranslation.put("CFG_STREAM_NULL", "Failed to reload " +
				"configuration; InputStream was null (is your Source " +
				"File null?).");

		// Database Messages
		this.defaultTranslation.put("DB_CNCT_ERR", "An error occurred while " +
				"attempting to connect to the database.");
		this.defaultTranslation.put("BAD_DB_DRVR", "The database driver " +
				"requested could not be found. Requested driver: ");
		this.defaultTranslation.put("DB_OPEN_SUC", "Established connection " +
				"to database.");
		this.defaultTranslation.put("DB_CLOSE_SUC", "The connection to the " +
				"database was closed successfully.");
		this.defaultTranslation.put("DB_CLOSE_ERR", "An error occurred while " +
				"attempting to close the connection to the database. Error: ");
		this.defaultTranslation.put("DB_QUERY_ERR", "An error occurred while " +
				"attempting to query the database. Error: ");
		this.defaultTranslation.put("DB_QUERY_RETRY", "Retrying Query...");
		this.defaultTranslation.put("DB_PREP_STMT_ERR", "An error occurred " +
				"while attempting to generate a prepared statement for the " +
				"database.");
		this.defaultTranslation.put("DB_DEBUG_ACTIVE", "Database Debugging " +
				"is active. All SQL queries will be logged as they are " +
				"received.");
		this.defaultTranslation.put("BAD_SQL_INPUT", "A parameter passed to " +
				"the StatementObject is invalid! Valid parameters are those " +
				"of type String and type Integer.");
	}

	public final String getLocalString(String key) {
		String value;

		if (translation != null) {
			value = translation.get(key);

			// Attempt to fall back to library/plugin specified default store
			if (value == null || value.equals("null")) {
				value = defaultTranslation.get(key);
			}
		} else {
			value = defaultTranslation.get(key);
		}

		if (value == null || value.equals("null") || value.isEmpty()) {
			return "Invalid Translation-Key: " + key;
		} else {
			return value;
		}
	}

	public final void addDefaultValue(String key, String value) {
		this.defaultTranslation.put(key, value);
	}

	public final void appendLocalizationStore(LocalizationStore values) {
		Map<String,String> finalMap = new HashMap<String, String>();
		finalMap.putAll(defaultTranslation);
		finalMap.putAll(values.getValues());

		this.defaultTranslation = finalMap;
	}

}
