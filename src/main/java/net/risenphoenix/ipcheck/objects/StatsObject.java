package net.risenphoenix.ipcheck.objects;

import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.database.DatabaseManager;

public class StatsObject {

    private IPCheck ipc;

    // Stat Storage
    private int logPlayerSession;

    public StatsObject(final IPCheck ipc) {
        this.ipc = ipc;
    }

    public String getPluginVersion() {
        return ipc.getVersion();
    }

    public String getJavaVersion() {
        return System.getProperty("java.version");
    }

    public String getOperatingSystem() {
        return System.getProperty("os.name");
    }

    public String getOperatingSystemArch() {
        return System.getProperty("os.arch");
    }

    public int getPlayersLogged() {
        return ipc.getDatabaseController().fetchAllPlayers().size();
    }

    public int getIPsLogged() {
        return ipc.getDatabaseController().fetchAllIPs().size();
    }

    public DatabaseManager.DatabaseType getDatabaseType() {
        return ipc.getDatabaseController().getDatabaseType();
    }

    public void logPlayerJoin(int count) {
        this.logPlayerSession += count;
    }

    public int getLogPlayerSession() {
        return logPlayerSession;
    }

    public boolean getSecureStatus() {
        return ipc.getConfig().getBoolean("secure-mode");
    }

    public boolean getActiveStatus() {
        return ipc.getConfig().getBoolean("active-mode");
    }

    public boolean getBlackListStatus() {
        return ipc.getConfig().getBoolean("use-country-blacklist");
    }
}
