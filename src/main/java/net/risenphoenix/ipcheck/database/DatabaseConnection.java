package net.risenphoenix.ipcheck.database;

import java.beans.PropertyVetoException;
import java.io.File;
import java.sql.*;
import com.mchange.v2.c3p0.*;

import net.risenphoenix.ipcheck.IPCheck;

public class DatabaseConnection {

    private final IPCheck plugin;
    public Connection c = null;

    private String driver;
    private String connectionString;

    private ComboPooledDataSource pooledDataSource = null;

    // SQLite Connection Initializer
    public DatabaseConnection(final IPCheck plugin2) {
        this.plugin = plugin2;

        driver = "org.sqlite.JDBC";
        connectionString = "jdbc:sqlite:" +
                new File(this.plugin.getDataFolder() + File.separator +
                        "store.db").getAbsolutePath();
        this.openConnection();
    }

    public DatabaseConnection(IPCheck plugin, String dbName) {
        this.plugin = plugin;

        driver = "org.sqlite.JDBC";
        connectionString = "jdbc:sqlite:" +
                new File(this.plugin.getDataFolder() + File.separator +
                        dbName + ".db").getAbsolutePath();
        this.openConnection();
    }

    // MySQL Connection Initializer
    public DatabaseConnection(IPCheck plugin, String hostname, int port,
                              String database, String username, String pwd) {
        this.plugin = plugin;

        driver = "com.mysql.jdbc.Driver";
        connectionString = "jdbc:mysql://" + hostname + ":" + port + "/" +
                database + "?user=" + username + "&password=" + pwd;
        this.openConnection();
    }

    // MySQL (Pooled) Connection Initializer
    public DatabaseConnection(IPCheck plugin2, String hostname, int port,
                              String database, String username, String pwd, int
                              poolSize) {
        this.plugin = plugin2;

        driver = "com.mysql.jdbc.Driver";
        connectionString = "jdbc:mysql://" + hostname + ":" + port + "/" +
                database;

        pooledDataSource = new ComboPooledDataSource();

        try {
            pooledDataSource.setDriverClass(driver);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        pooledDataSource.setJdbcUrl(connectionString);
        pooledDataSource.setUser(username);
        pooledDataSource.setPassword(pwd);
        pooledDataSource.setMaxPoolSize(poolSize);
    }



    public Connection openConnection() {
        try {
            Class.forName(this.driver);
            this.c = DriverManager.getConnection(this.connectionString);

            // Output Message
            this.plugin.getLogger().info(
                    this.plugin.getLocalizationManager().
                            getLocalString("DB_OPEN_SUC"));

            return c;
        } catch (SQLException e) {
        	this.plugin.getLogger().severe(
                    this.plugin.getLocalizationManager().
                            getLocalString("DB_CNCT_ERR") + e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
        	this.plugin.getLogger().severe(
                    this.plugin.getLocalizationManager().
                            getLocalString("BAD_DB_DRVR") + this.driver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.c;
    }

    public void closeConnection() {
        try {
            if (this.c != null) c.close();
            this.plugin.getLogger().info(
                    this.plugin.getLocalizationManager().
                            getLocalString("DB_CLOSE_SUC"));
        } catch (SQLException e) {
        	this.plugin.getLogger().severe(
                    this.plugin.getLocalizationManager().
                            getLocalString("DB_CLOSE_ERR") + e.getLocalizedMessage());
        }

        this.c = null;
    }

    public Connection getConnection() {
        if (pooledDataSource != null) {
            try {
                return pooledDataSource.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }

        return this.c;
    }

    public boolean isConnected() {
        try {
            return (c == null || c.isClosed()) ? false : true;
        } catch (SQLException e) {
        	this.plugin.getLogger().severe(
                    e.getLocalizedMessage());
            return false;
        }
    }

    public Result query(final PreparedStatement stmt) {
        return query(stmt, true);
    }

    public Result query(final PreparedStatement stmt, boolean retry) {
        try {
            if (!isConnected()) openConnection();

            if (stmt.execute())
                return new Result(stmt, stmt.getResultSet());
        } catch (final SQLException e) {
        	this.plugin.getLogger().severe(
                    this.plugin.getLocalizationManager().
                            getLocalString("DB_QUERY_ERR") + e.getMessage());

            if (retry && e.getMessage().contains("_BUSY")) {
            	this.plugin.getLogger().severe(
                        this.plugin.getLocalizationManager().
                                getLocalString("DB_QUERY_RETRY") + e.getMessage());

                this.plugin.getServer().getScheduler()
                        .scheduleSyncDelayedTask(this.plugin,
                                new Runnable() {
                                    public void run() {
                                        query(stmt, false);
                                    }
                                }, 20);
            }
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.getStackTrace();
            }
        }

        return null;
    }

    // Result Object
    public class Result {
        private ResultSet resultSet;
        private Statement statement;

        public Result(Statement stmt, ResultSet rset) {
            this.statement = stmt;
            this.resultSet = rset;
        }

        public ResultSet getResultSet() {
            return this.resultSet;
        }

        public String getStatement() {
            return this.statement.toString();
        }

        public void close() {
            try {
                this.statement.close();
                this.resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
