package net.risenphoenix.ipcheck.objects;

import java.util.ArrayList;

public class IPObject {

    private String IP;
    private ArrayList<String> Users;

    public IPObject(String IP, ArrayList<String> Users) {
        this.IP = IP;
        this.Users = Users;
    }

    public IPObject(String IP) {
        this.IP = IP;
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
}
