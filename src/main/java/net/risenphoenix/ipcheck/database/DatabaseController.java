package net.risenphoenix.ipcheck.database;

import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.objects.IPObject;
import net.risenphoenix.ipcheck.objects.UserObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseController extends DatabaseManager {

	// SQ-Lite Initializer
	public DatabaseController(IPCheck ipCheck) {
		super(ipCheck, "ip-check");

		// Enable Debugging to allow us to view the dynamic SQL queries
		//this.enableDebug(true);
		this.dropTables(ipCheck); // Attempt Table Drop
		this.initializeSQLiteTables();
	}

	// MySQL Initializer
	public DatabaseController(IPCheck plugin, String hostname, int port,
			String database, String username, String pwd) {
		super(plugin, hostname, port, database, username, pwd);


		// Enable Debugging to allow us to view the dynamic SQL queries
		//this.enableDebug(true);
		this.dropTables(plugin); // Attempt Table Drop
		this.initializeMySQLTables(); // Initialize Tables
	}

	private void dropTables(IPCheck plugin) {
		if (!plugin.getConfig().getBoolean("dbGenerated")) {
			// SQL Strings
			String SQL_0 = "DROP TABLE IF EXISTS ipcheck_log;";
			String SQL_1 = "DROP TABLE IF EXISTS ipcheck_user;";
			String SQL_2 = "DROP TABLE IF EXISTS ipcheck_ip;";

			// Execute SQL
			executeStatement(new StatementObject(getPlugin(), SQL_0));
			executeStatement(new StatementObject(getPlugin(), SQL_1));
			executeStatement(new StatementObject(getPlugin(), SQL_2));

			// Save Configuration Option
			plugin.getConfig().set("dbGenerated", true);
		}
	}

	// Initialize Tables for SQ-Lite
	public void initializeSQLiteTables() {
		String TABLE_IPC_LOG = "CREATE TABLE IF NOT EXISTS ipcheck_log ( " +
				"ip TEXT," +
				"username TEXT," +
				"timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
				"PRIMARY KEY(username,ip));";

		String TABLE_IPC_USER = "CREATE TABLE IF NOT EXISTS ipcheck_user ( " +
				"username TEXT," +
				"timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
				"PRIMARY KEY(username));";

		String TABLE_IPC_IP = "CREATE TABLE IF NOT EXISTS ipcheck_ip ( " +
				"ip TEXT," +
				"timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
				"PRIMARY KEY(ip));";

		this.executeStatement(new StatementObject(this.getPlugin(),
				TABLE_IPC_IP));
		this.executeStatement(new StatementObject(this.getPlugin(),
				TABLE_IPC_LOG));
		this.executeStatement(new StatementObject(this.getPlugin(),
				TABLE_IPC_USER));

		executeColumnUpdate();
	}

	// Initialize Tables for MySQL
	public void initializeMySQLTables() {
		String TABLE_IPC_LOG = "CREATE TABLE IF NOT EXISTS ipcheck_log ( " +
				"ip varchar(15) NOT NULL," +
				"username varchar(255) NOT NULL," +
				"timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
				"PRIMARY KEY (ip,username)" +
				");";

		String TABLE_IPC_USER = "CREATE TABLE IF NOT EXISTS ipcheck_user ( " +
				"username varchar(255) NOT NULL," +
				"timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
				"PRIMARY KEY (username)" +
				");";

		String TABLE_IPC_IP = "CREATE TABLE IF NOT EXISTS ipcheck_ip ( " +
				"ip varchar(15) NOT NULL," +
				"timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
				"PRIMARY KEY (ip)" +
				");";

		this.executeStatement(new StatementObject(this.getPlugin(),
				TABLE_IPC_IP));
		this.executeStatement(new StatementObject(this.getPlugin(),
				TABLE_IPC_LOG));
		this.executeStatement(new StatementObject(this.getPlugin(),
				TABLE_IPC_USER));

		executeColumnUpdate();
	}

	public final void log(String player, String ip) {
		this.addIP(ip);
		this.addPlayer(player);

		String SQL = "replace into ipcheck_log (ip,username) VALUES (?, ?)";
		this.executeStatement(new StatementObject(this.getPlugin(),
				SQL, new Object[]{ip, player.toLowerCase()}));
	}

	public final void addIP(String ip) {
		String SQL = "insert " + ((this.getPlugin().getConfig()
				.getBoolean("use-mysql")) ? "" : "or ") + "ignore into " +
				"ipcheck_ip (ip) values (?)";

		this.executeStatement(new StatementObject(this.getPlugin(),
				SQL, new Object[]{ip}));
	}

	public final void addPlayer(String player) {
		String SQL = "insert " + ((this.getPlugin().getConfig()
				.getBoolean("use-mysql")) ? "" : "or ") + "ignore into " +
				"ipcheck_user (username) values (?)";

		this.executeStatement(new StatementObject(this.getPlugin(),
				SQL, new Object[]{player.toLowerCase()}));
	}

	/* Player Methods */

	public final void purgePlayer(String player) {
		String STMT_1 = "delete from ipcheck_user where lower(username) = ?";
		String STMT_2 = "delete from ipcheck_log where lower(username) = ?";

		this.executeStatement(new StatementObject(this.getPlugin(),
				STMT_1, new Object[]{player.toLowerCase()}));

		this.executeStatement(new StatementObject(this.getPlugin(),
				STMT_2, new Object[]{player.toLowerCase()}));
	}

	public final boolean isValidPlayer(String player) {
		String SQL = "select username from ipcheck_user where " +
				"lower(username) = ?";

		QueryFilter filter = new QueryFilter() {
			@Override
			public Object onExecute(ResultSet res) {
				try {
					return (res.next());
				} catch (SQLException e) {
					e.printStackTrace();
				}

				return false;
			}
		};

		return (Boolean) this.executeQuery(new StatementObject(this.getPlugin(),
				SQL, new Object[]{player.toLowerCase()}), filter);
	}


	/* IP Methods */

	public final void purgeIP(String ip) {
		String STMT_1 = "delete from ipcheck_ip where ip = ?";
		String STMT_2 = "delete from ipcheck_log where ip = ?";

		this.executeStatement(new StatementObject(this.getPlugin(),
				STMT_1, new Object[]{ip}));

		this.executeStatement(new StatementObject(this.getPlugin(),
				STMT_2, new Object[]{ip}));
	}

	/* Other Methods */

	public final IPObject getIPObject(String ip) {
		String SQL = "select username from ipcheck_log where ip = ?";

		QueryFilter filter = new QueryFilter(new Object[]{ip, this}) {
			@Override
			public Object onExecute(ResultSet res) {
				ArrayList<String> users = new ArrayList<String>();
				String ip = (String) this.getData()[0];

				try {
					while (res.next()) {
						users.add(res.getString(1));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				return new IPObject(ip, users);
			}
		};

		return (IPObject) this.executeQuery(new StatementObject(
				this.getPlugin(), SQL, new Object[]{ip}), filter);
	}

	public final UserObject getUserObject(String player) {
		String SQL = "select ip from ipcheck_log where lower(username) = ?";

		QueryFilter filter = new QueryFilter() {
			@Override
			public Object onExecute(ResultSet res) {
				ArrayList<String> ips = new ArrayList<String>();

				try {
					while (res.next()) {
						if (!ips.contains(res.getString(1))) {
							ips.add(res.getString(1));
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				return ips;
			}
		};

		ArrayList<String> ips = (ArrayList<String>) this.executeQuery(
				new StatementObject(this.getPlugin(), SQL, new Object[]{
						player.toLowerCase()}), filter);

		return new UserObject(player.toLowerCase(), ips);
	}

	public final String getLastKnownIP(String player) {
		String SQL = "select ip from ipcheck_log where lower(username) = ? " +
				"order by timestamp desc limit 1;";

		QueryFilter filter = new QueryFilter() {
			@Override
			public Object onExecute(ResultSet res) {
				String returning = "NO_FIND";

				try {
					if (res.next()) returning = res.getString(1);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				return returning;
			}
		};

		return (String) this.executeQuery(new StatementObject(this.getPlugin(),
				SQL, new Object[]{player.toLowerCase()}), filter);
	}

	public final boolean isValidIP(String ip) {
		String SQL = "select ip from ipcheck_ip where ip = ?";

		QueryFilter filter = new QueryFilter() {
			@Override
			public Object onExecute(ResultSet res) {
				try {
					return (res.next());
				} catch (SQLException e) {
					e.printStackTrace();
				}

				return false;
			}
		};

		if (ip.equals("NO_FIND")) return false;

		return (Boolean) this.executeQuery(new StatementObject(this.getPlugin(),
				SQL, new Object[]{ip}), filter);
	}

	public final String getLogTime(String player) {
		String SQL = "select timestamp from ipcheck_user where " +
				"lower(username) = ?";

		QueryFilter filter = new QueryFilter() {
			@Override
			public Object onExecute(ResultSet res) {
				String returning = null;

				try {
					if (res.next()) returning = res.getString(1);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				return returning;
			}
		};

		return (String) this.executeQuery(new StatementObject(this.getPlugin(),
				SQL, new Object[]{player.toLowerCase()}), filter);
	}

	public final String getLastTime(String player) {
		String SQL = "select timestamp from ipcheck_log where " +
				"lower(username) = ? order by timestamp desc limit 1;";

		QueryFilter filter = new QueryFilter() {
			@Override
			public Object onExecute(ResultSet res) {
				String returning = null;

				try {
					if (res.next()) returning = res.getString(1);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				return returning;
			}
		};

		return (String) this.executeQuery(new StatementObject(this.getPlugin(),
				SQL, new Object[]{player.toLowerCase()}), filter);
	}

	public final String getCurrentTimeStamp() {
		String SQL = "select CURRENT_TIMESTAMP";

		QueryFilter filter = new QueryFilter() {
			@Override
			public Object onExecute(ResultSet res) {
				String date = null;

				try {
					res.next();
					date = res.getString(1);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				return date;
			}
		};

		return (String) this.executeQuery(new StatementObject(this.getPlugin(),
				SQL), filter);
	}

	public final ArrayList<UserObject> getPlayersByDate(String dateOne,
			String dateTwo) {
		String SQL = "select username from ipcheck_user where timestamp >= ? " +
				"and timestamp <= ?";

		QueryFilter filter = new QueryFilter(new Object[]{this}) {
			@Override
			public Object onExecute(ResultSet res) {
				DatabaseController dbC = (DatabaseController) this.getData()[0];
				ArrayList<UserObject> users = new ArrayList<UserObject>();

				try {
					while (res.next()) {
						String user = res.getString(1);
						users.add(new UserObject(user));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				return users;
			}
		};

		return (ArrayList<UserObject>) this.executeQuery(new StatementObject(
				this.getPlugin(), SQL, new Object[]{dateOne, dateTwo}), filter);
	}

	public final ArrayList<UserObject> fetchAllPlayers() {
		String SQL = "select * from ipcheck_user";

		QueryFilter filter = new QueryFilter(new Object[]{this}) {
			@Override
			public Object onExecute(ResultSet res) {
				ArrayList<UserObject> users = new ArrayList<UserObject>();

				try {
					while (res.next()) {
						String user = res.getString(1);
						users.add(new UserObject(user));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				return users;
			}
		};

		return (ArrayList<UserObject>) this.executeQuery(new StatementObject(
				this.getPlugin(), SQL), filter);
	}

	public final ArrayList<IPObject> fetchAllIPs() {
		String SQL = "select * from ipcheck_ip";

		QueryFilter filter = new QueryFilter(new Object[]{this}) {
			@Override
			public Object onExecute(ResultSet res) {
				ArrayList<IPObject> ips = new ArrayList<IPObject>();

				try {
					while (res.next()) {
						String ip = res.getString(1);
						ips.add(new IPObject(ip));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				return ips;
			}
		};

		return (ArrayList<IPObject>) this.executeQuery(new StatementObject(
				this.getPlugin(), SQL), filter);
	}

	public final void setRejoinExemptPlayer(String player, boolean exempt) {
		String SQL = "update ipcheck_user set rejoinexempt = ? " +
				"where username = ?";
		int value = (exempt) ? 1 : 0;

		this.executeStatement(new StatementObject(this.getPlugin(),
				SQL, new Object[]{value, player.toLowerCase()}));
	}

	private void executeColumnUpdate() {
		// Check for Missing Columns (Required for those upgrading from pre-2.0)
		boolean hasRejoinPlayer = true;
		boolean hasRejoinIP = true;
		boolean hasProtectedPlayer = true;

		// SQLite
		if (getDatabaseType() == DatabaseType.SQLITE) {
			String SQL_P = "PRAGMA table_info(ipcheck_user);";

			QueryFilter filter = new QueryFilter() {
				@Override
				public Object onExecute(ResultSet res) {
					boolean[] flags = new boolean[2];
					// 0 = Rejoin, 1 = Protection
					try {
						while (res.next()) {
							if (res.getString(2).equals("rejoinexempt"))
								flags[0] = true;
							if (res.getString(2).equals("protected"))
								flags[1] = true;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}

					return flags;
				}
			};

			// Fetch Boolean Array from Database
			boolean[] res = (boolean[]) this.executeQuery(
					new StatementObject(this.getPlugin(), SQL_P), filter);

			hasRejoinPlayer = res[0];
			hasProtectedPlayer = res[1];

			String SQL_I = "PRAGMA table_info(ipcheck_ip);";

			QueryFilter filter_two = new QueryFilter() {
				@Override
				public Object onExecute(ResultSet res) {
					boolean flag = false;
					// 0 = Rejoin, 1 = Protection
					try {
						while (res.next()) {
							if (res.getString(2).equals("rejoinexempt"))
								flag = true;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}

					return flag;
				}
			};

			// Fetch Boolean from Database
			hasRejoinIP = Boolean.parseBoolean(this.executeQuery(
					new StatementObject(this.getPlugin(), SQL_I), filter_two).toString());

			// Execute Results
			if (!hasRejoinPlayer) {
				String SQL_ZERO = "ALTER TABLE ipcheck_user ADD COLUMN " +
						"rejoinexempt INTEGER DEFAULT 0";

				this.executeStatement(new StatementObject(this.getPlugin(),
						SQL_ZERO));
			}

			if (!hasProtectedPlayer) {
				String SQL_ZERO = "ALTER TABLE ipcheck_user ADD COLUMN " +
						"protected INTEGER DEFAULT 0";

				this.executeStatement(new StatementObject(this.getPlugin(),
						SQL_ZERO));
			}

			if (!hasRejoinIP) {
				String SQL_ZERO = "ALTER TABLE ipcheck_ip ADD COLUMN " +
						"rejoinexempt INTEGER DEFAULT 0";

				this.executeStatement(new StatementObject(this.getPlugin(),
						SQL_ZERO));
			}


			// MySQL
		} else {
			String SQL_P = "SHOW COLUMNS FROM " +
					getPlugin().getConfig().getString("dbName") +
					".ipcheck_user";

			QueryFilter filter = new QueryFilter() {
				@Override
				public Object onExecute(ResultSet res) {
					boolean[] flags = new boolean[2];
					// 0 = Rejoin, 1 = Protection
					try {
						while (res.next()) {
							if (res.getString(1).equals("rejoinexempt"))
								flags[0] = true;
							if (res.getString(1).equals("protected"))
								flags[1] = true;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}

					return flags;
				}
			};

			// Fetch Boolean Array from Database
			boolean[] res = (boolean[]) this.executeQuery(
					new StatementObject(this.getPlugin(), SQL_P), filter);

			hasRejoinPlayer = res[0];
			hasProtectedPlayer = res[1];

			String SQL_I = "SHOW COLUMNS FROM " +
					getPlugin().getConfig().getString("dbName") +
					".ipcheck_ip";

			QueryFilter filter_two = new QueryFilter() {
				@Override
				public Object onExecute(ResultSet res) {
					boolean flag = false;
					// 0 = Rejoin, 1 = Protection
					try {
						while (res.next()) {
							if (res.getString(1).equals("rejoinexempt"))
								flag = true;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}

					return flag;
				}
			};

			// Fetch Boolean from Database
			hasRejoinIP = Boolean.parseBoolean(this.executeQuery(
					new StatementObject(this.getPlugin(), SQL_I), filter_two).toString());

			// Execute Results
			if (!hasRejoinPlayer) {
				String SQL_ZERO = "ALTER TABLE ipcheck_user ADD COLUMN " +
						"rejoinexempt bit(1) NOT NULL DEFAULT b'0'";

				this.executeStatement(new StatementObject(this.getPlugin(),
						SQL_ZERO));
			}

			if (!hasProtectedPlayer) {
				String SQL_ZERO = "ALTER TABLE ipcheck_user ADD COLUMN " +
						"protected bit(1) NOT NULL DEFAULT b'0'";

				this.executeStatement(new StatementObject(this.getPlugin(),
						SQL_ZERO));
			}

			if (!hasRejoinIP) {
				String SQL_ZERO = "ALTER TABLE ipcheck_ip ADD COLUMN " +
						"rejoinexempt bit(1) NOT NULL DEFAULT b'0'";

				this.executeStatement(new StatementObject(this.getPlugin(),
						SQL_ZERO));
			}
		}
	}

}
