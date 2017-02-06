package net.risenphoenix.ipcheck.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.risenphoenix.ipcheck.IPCheck;

public class DatabaseManager {

	private final IPCheck plugin;
	private DatabaseConnection connection;
	private DatabaseType type;
	private boolean debug = false;

	/* Connection Properties */

	// MySQL
	private String hostname = null;
	private int port = -1;
	private String database = null;
	private String username = null;
	private String password = null;
	private int poolSize = 1;

	// SQLite
	private String dbName = null;

	// SQLite Constructor
	public DatabaseManager(final IPCheck plugin) {
		this.plugin = plugin;
		this.connection = new DatabaseConnection(plugin);
		this.type = DatabaseType.SQLITE;
	}

	// SQLite Constructor w/ DB Name
	public DatabaseManager(final IPCheck ipCheck, final String dbName) {
		this.plugin = ipCheck;

		// Value Assignment
		this.dbName = dbName;

		// Connection Creation
		this.connection = new DatabaseConnection(ipCheck, dbName);
		this.type = DatabaseType.SQLITE;
	}

	// MySQL Constructor
	public DatabaseManager(final IPCheck plugin, String hostname, int port,
			String database, String username, String pwd,
			int poolSize) {
		this.plugin = plugin;

		// Value Assignment
		this.hostname = hostname;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = pwd;
		this.poolSize = poolSize;

		// Connection Creation (Pooled)
		if (poolSize > 1) {
			this.connection = new DatabaseConnection(plugin, hostname, port,
					database, username, pwd, poolSize);

			// Connection Creation (Non-pooled)
		} else {
			this.connection = new DatabaseConnection(plugin, hostname, port,
					database, username, pwd);
		}
		this.type = DatabaseType.MYSQL;
	}

	public void enableDebug(boolean shouldDebug) {
		this.debug = shouldDebug;
		this.plugin.getLogger().info( this.plugin
				.getLocalizationManager().getLocalString("DB_DEBUG_ACTIVE"));
	}

	// Statement Execute Master Method
	public final boolean executeStatement(StatementObject stmt) {
		try {
			confirmConnection(); // Confirm connection has not timed out.
			PreparedStatement statement =
					stmt.getStatement(this.connection.getConnection());

			if (this.debug)
				this.plugin.getLogger().info( statement.toString());

			stmt.getStatement(this.connection.getConnection()).executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// Query Execute Master Method
	public final Object executeQuery(StatementObject stmt, QueryFilter filter) {
		ResultSet res = null;
		Object obj = null;

		try {
			confirmConnection(); // Confirm connection has not timed out.
			res = stmt.getStatement(this.connection.getConnection())
					.executeQuery();
			obj = filter.onExecute(res);
			res.close();
		} catch (SQLException e) {
			this.plugin.getLogger().severe(
					e.getMessage());
		} finally {
			try {
				if (res != null) res.close();
			} catch (Exception e) {
				this.plugin.getLogger().severe(
						e.getMessage());
				res = null;
			}
		}

		return obj;
	}

	private void confirmConnection() {
		if (connection == null) {
			if (getDatabaseType().equals(DatabaseType.MYSQL)) {
				connection = new DatabaseConnection(getPlugin(), hostname, port,
						database, username, password);
			} else {
				if (dbName == null) {
					connection = new DatabaseConnection(getPlugin());
				} else {
					connection = new DatabaseConnection(getPlugin(), dbName);
				}
			}
		}
	}

	public final IPCheck getPlugin() {
		return this.plugin;
	}

	public final DatabaseConnection getDatabaseConnection() {
		return this.connection;
	}

	public final DatabaseType getDatabaseType() {
		return this.type;
	}

	public enum DatabaseType {
		MYSQL,
		SQLITE
	}

}
