package net.risenphoenix.ipcheck.objects;

import java.util.ArrayList;

public class IPObject {

    private String IP;
    private ArrayList<String> Users;
    private boolean isBanned;
    private boolean isExempt;
    private boolean isRejoinExempt;

    public IPObject(String IP, ArrayList<String> Users, boolean isBanned,
                    boolean isExempt, boolean isRejoinExempt) {
        this.IP = IP;
        this.Users = Users;
        this.isBanned = isBanned;
        this.isExempt = isExempt;
        this.isRejoinExempt = isRejoinExempt;
    }

    public IPObject(String IP, boolean isBanned) {
        this.IP = IP;
        this.isBanned = isBanned;
    }

    public final int getNumberOfUsers() {
        return this.Users.size();
    }

    public final String getIP() {
        return this.IP;
    }

    public final ArrayList<String> getUsers() {
        return this.Users;
    }

    public final boolean getBannedStatus() {
        return this.isBanned;
    }

    public final boolean getExemptStatus() {
        return this.isExempt;
    }

    public boolean getRejoinExemptStatus() {
        return isRejoinExempt;
    }
}
