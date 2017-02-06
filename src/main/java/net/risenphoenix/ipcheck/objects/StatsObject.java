package net.risenphoenix.ipcheck.objects;

import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.database.DatabaseManager;

public class StatsObject {

    private IPCheck ipc;

    // Stat Storage
    private int bannedPlayerSession;
    private int logPlayerSession;
    private int warningIssuedSession;
    private int kickIssuedSession;
    private int unbannedPlayerSession;

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

    public int getPlayersExempt() {
        return ipc.getDatabaseController().getPlayerExemptList().size();
    }

    public int getIPsExempt() {
        return ipc.getDatabaseController().getIPExemptList().size();
    }

    public int getPlayersRejoinExempt() {
        return ipc.getDatabaseController().fetchRejoinExemptPlayers().size();
    }

    public int getIPsRejoinExempt() {
        return ipc.getDatabaseController().fetchRejoinExemptIPs().size();
    }

    public int getPlayersBanned() {
        return ipc.getDatabaseController().fetchBannedPlayers().size();
    }

    public int getIPsBanned() {
        return ipc.getDatabaseController().fetchBannedIPs().size();
    }

    public DatabaseManager.DatabaseType getDatabaseType() {
        return ipc.getDatabaseController().getDatabaseType();
    }

    public void logPlayerBan(int count) {
        this.bannedPlayerSession += count;
    }

    public void logPlayerJoin(int count) {
        this.logPlayerSession += count;
    }

    public void logWarningIssue(int count) {
        this.warningIssuedSession += count;
    }

    public void logKickIssue(int count) {
        this.kickIssuedSession += count;
    }

    public void logPlayerUnban(int count) {
        this.unbannedPlayerSession += count;
    }

    public int getBannedPlayerSession() {
        return bannedPlayerSession;
    }

    public int getLogPlayerSession() {
        return logPlayerSession;
    }

    public int getWarningIssuedSession() {
        return warningIssuedSession;
    }

    public int getKickIssuedSession() {
        return kickIssuedSession;
    }

    public int getUnbannedPlayerSession() {
        return unbannedPlayerSession;
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
