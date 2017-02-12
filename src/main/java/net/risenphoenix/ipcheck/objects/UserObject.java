package net.risenphoenix.ipcheck.objects;

import java.util.ArrayList;

public class UserObject {

    private String User;
    private ArrayList<String> IPs;

    public UserObject(String User, ArrayList<String> IPs) {
        this.User = User;
        this.IPs = IPs;
    }

    public UserObject(String user) {
        this.User = user;
    }

    public final String getUser() {
        return this.User;
    }

    public final ArrayList<String> getIPs() {
        return this.IPs;
    }

    public final int getNumberOfIPs() {
        return this.IPs.size();
    }
}
