package net.risenphoenix.ipcheck.objects;

import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.LocalizationManager;
import net.risenphoenix.ipcheck.database.DatabaseController;
import net.risenphoenix.ipcheck.util.FormatFilter;
import net.risenphoenix.ipcheck.util.ListFormatter;
import net.risenphoenix.ipcheck.util.TimeCalculator;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;

public class ReportObject {

    private IPCheck plugin;
    private DatabaseController db;
    private LocalizationManager local;

    private ArrayList<StringBuilder> SBs;
    private ArrayList<String> singleAlts;
    private ArrayList<String> uniqueAlts;

    private OfflinePlayer player;
    private boolean forPlayer;

    public ReportObject(IPCheck ipCheck) {
        this.plugin = ipCheck;
        this.db = ipCheck.getDatabaseController();
        this.local = ipCheck.getLocalizationManager();

        this.SBs = new ArrayList<StringBuilder>();
        this.singleAlts = new ArrayList<String>();
        this.uniqueAlts = new ArrayList<String>();
    }

    public void onExecute(CommandSender sender, String arg) {
        // IP Filter for differentiating player-names from IPs
        String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";

        // Determine if the input is an IP or a Player name
        forPlayer = (!arg.toLowerCase().matches(ip_filter));

        // Fetch Offline Player for use with the Database
        if (forPlayer) {
            this.player = plugin.getServer().getOfflinePlayer(arg);

            FetchResult fResult = this.fetchPlayerData(arg);

            // If the Fetch Result returned a NO_FIND status, return.
            if (fResult == FetchResult.NOT_FOUND) {
                this.plugin.sendPlayerMessage(sender,
                        this.local.getLocalString("NO_FIND"));
                return;
            }
        } else {
            IPObject ipo = this.db.getIPObject(arg);
            this.singleAlts = ipo.getUsers();

            if (ipo.getNumberOfUsers() == 0) {
                this.plugin.sendPlayerMessage(sender,
                        this.local.getLocalString("NO_FIND"));
                return;
            }
        }

        // Output Report
        this.outputHead(sender, arg);
        this.outputBody(sender, arg);
        this.outputFoot(sender, arg);
    }

    // Report Header
    private void outputHead(CommandSender sender, String arg) {
        this.plugin.sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);

        if (!forPlayer) {
            if (sender.hasPermission("ipcheck.showip") || sender.isOp()) {
                this.plugin.sendPlayerMessage(sender, ChatColor.GOLD +
                        this.local.getLocalString("REPORT_HEAD_ONE") + " " +
                        ChatColor.GREEN + arg + ChatColor.GOLD +
                        " ... " + ChatColor.RED +
                        this.singleAlts.size(), false);
            } else {
                this.plugin.sendPlayerMessage(sender, ChatColor.GOLD +
                        this.local.getLocalString("REPORT_HEAD_TWO") + " " +
                        ChatColor.RED + this.singleAlts.size(), false);
            }
        } else {
            this.plugin.sendPlayerMessage(sender, ChatColor.GOLD +
                    this.local.getLocalString("REPORT_HEAD_ONE") + " " +
                    ChatColor.GREEN + arg + ChatColor.GOLD + " ... " +
                    ChatColor.RED + this.uniqueAlts.size(), false);
        }
    }

    // Report Body
    private void outputBody(CommandSender sender, String arg) {
        if ((forPlayer && uniqueAlts.size() > 0) ||
                (!forPlayer && singleAlts.size() > 0)) {

            this.plugin.sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                    "------------------------------------------------", false);

            if (!forPlayer) {
                String output = new ListFormatter(this.singleAlts)
                        .getFormattedList().toString();

                if (sender.hasPermission("ipcheck.showip")) {
                    this.plugin.sendPlayerMessage(sender,
                            ChatColor.LIGHT_PURPLE +
                                    this.local.getLocalString("REPORT_BODY_ONE"),
                            false);

                    this.plugin.sendPlayerMessage(sender, ChatColor.YELLOW +
                            output, false);
                } else {
                    this.plugin.sendPlayerMessage(sender,
                            ChatColor.LIGHT_PURPLE +
                                    this.local.getLocalString("REPORT_BODY_TWO")
                                    + " " + ChatColor.YELLOW + output, false);
                }

                return;
            } else {
                this.plugin.sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                        this.local.getLocalString("REPORT_BODY_TWO") + " ",
                        false);

                // For each StringBuilder, fetch the IP address from the
                // beginning and store it in a separate variable, then remove
                // it from the list along with the divisor character '|'.
                for (int i = 0; i < this.SBs.size(); i++) {
                    StringBuilder sb = this.SBs.get(i);
                    StringBuilder ipAddress = new StringBuilder();
                    String full = sb.toString();

                    for (int j = 0; j < full.length(); j++) {
                        if (full.charAt(j) != '|') {
                            ipAddress.append(full.charAt(j));
                        } else {
                            break;
                        }
                    }

                    // Create an output omitting the IP-Address and divisor char
                    String out = full.replace(ipAddress.toString() + "|", "");

                    // If Sender can see IPs, show the IP, else show the place-
                    // holder text "####:"
                    if (!sender.hasPermission("ipcheck.showip") &&
                            !sender.isOp()) {
                        this.plugin.sendPlayerMessage(sender,
                                ChatColor.RED + "####:", false);
                    } else {
                        String ipAdd = ipAddress.toString();

                        this.plugin.sendPlayerMessage(sender, ChatColor.RED +
                                ipAdd + ":", false);
                    }

                    // Display the associated accounts
                    this.plugin.sendPlayerMessage(sender, out, false);

                    // Place a Spacer in-between the different IP listings
                    if (i < (SBs.size() - 1)) sender.sendMessage("");
                }
            }
        } else {
            this.plugin.sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                    "------------------------------------------------", false);
            this.plugin.sendPlayerMessage(sender,
                    this.local.getLocalString("REPORT_BODY_FOUR"), false);
        }
    }

    // Report Footer
    private void outputFoot(CommandSender sender, String arg) {
        this.plugin.sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);

        if (forPlayer) {
            if (this.player != null) {
                // Display Last Known IP
                String ipOutput = this.db.getLastKnownIP(arg);

                if (sender.hasPermission("ipcheck.showip") || sender.isOp()) {
                // Output Last Known IP
                    this.plugin.sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE
                            + this.local.getLocalString("REPORT_FOOT_LAST_IP") +
                            " " + ChatColor.YELLOW + ipOutput, false);
                }

                // Display Player Country
                String out = IPCheck.getInstance().getBlockManager()
                        .getCountry(ipOutput);

                String country = ChatColor.YELLOW + " " + ((out != null) ? out :
                        local.getLocalString("LOCATION_UNAVAILABLE"));

                this.plugin.sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                        this.local.getLocalString("REPORT_FOOT_LOCATION") +
                        country, false);

                // Display Time since Last Login
                String lastLog = ChatColor.YELLOW + " " +
                        new TimeCalculator(arg).getLastTime();

                this.plugin.sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                        this.local.getLocalString("REPORT_FOOT_PTIME") +
                        lastLog, false);

            } else {
                this.plugin.sendPlayerMessage(sender, ChatColor.RED +
                        "ERROR: " + ChatColor.GOLD +
                        this.local.getLocalString("REPORT_FOOT_ERR"), false);
            }
        } else {
            // Display IP Country
            String out = IPCheck.getInstance().getBlockManager()
                    .getCountry(arg);

            String country = ChatColor.YELLOW + " " + ((out != null) ? out :
                    local.getLocalString("LOCATION_UNAVAILABLE"));

            this.plugin.sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                    this.local.getLocalString("REPORT_FOOT_LOCATION") +
                    country, false);
        }

        // End Report Output
        this.plugin.sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);
    }

    // Player Data Fetch
    private FetchResult fetchPlayerData(final String arg) {
        // Fetch UserObject from the Database
        UserObject user = this.db.getUserObject(arg);

        // If there were zero IPs returned, return with NOT_FOUND status.
        if (user.getNumberOfIPs() == 0) return FetchResult.NOT_FOUND;

        // Fetch IPObjects from UserObject
        ArrayList<IPObject> ipos = new ArrayList<IPObject>();

        // Get IP-Addresses
        for (String s : user.getIPs()) ipos.add(this.db.getIPObject(s));

        // Parse Information Strings
        for (IPObject ipo : ipos) {

            /* If the current IPObject being parsed has one user linked and the
             * linked user is the same user that we're checking, skip it.*/
            if (ipo.getNumberOfUsers() == 1) {
                if (ipo.getUsers().contains(arg.toLowerCase())) continue;
            }

            // Create New String Builder
            StringBuilder sb = new StringBuilder();

            // Append the leading IP-Address, plus a splitter character '|'
            sb.append(ipo.getIP() + "|");

            // Create a FormatFilter for the ListFormatter
            FormatFilter fFilter = new FormatFilter() {
                private ArrayList<String> inputs = new ArrayList<String>();

                @Override
                public String execute(String input) {
                    // Filter out duplicate entries
                    if (inputs.contains(input.toLowerCase())) {
                        return null;
                    } else {
                        inputs.add(input.toLowerCase());
                    }

                    // Filter out entries of the search term
                    return (!(input.equalsIgnoreCase(arg))) ? input : null;
                }
            };

            // Create a Formatted List with the Account Names and append it to
            // the current StringBuilder.
            ListFormatter format = new ListFormatter(ipo.getUsers(), fFilter);
            sb.append(format.getFormattedList());

            // Create Unique_Accounts List, ignoring case.
            for (String s : ipo.getUsers()) {
                String val = s.toLowerCase();
                if (!val.equalsIgnoreCase(arg)) {
                    if (!uniqueAlts.contains(val)) uniqueAlts.add(val);
                }
            }

            /* Add the StringBuilder to the holder ArrayList if there are
             * accounts linked to the IP that are not filtered out. */
            if (!sb.toString().equals(ipo.getIP() + "|")) SBs.add(sb);
        }

        return FetchResult.GOOD;
    }

    private enum FetchResult {
        GOOD,
        NOT_FOUND
    }
}
