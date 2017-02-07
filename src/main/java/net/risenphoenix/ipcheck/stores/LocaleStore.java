/*
 * Copyright © 2014 Jacob Keep (Jnk1296). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 *
 *  * Neither the name of JuNK Software nor the names of its contributors may 
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package net.risenphoenix.ipcheck.stores;

import net.risenphoenix.ipcheck.IPCheck;

public class LocaleStore extends LocalizationStore {

    public LocaleStore(final IPCheck plugin) {
        super(plugin);
    }
    
    @Override
    public void initializeStore() {
        this.add("NO_FIND", "The player or IP specified could not be found.");
        this.add("TOGGLE_SECURE", "Secure-Mode");
        this.add("TOGGLE_NOTIFY", "Notify-On-Login");
        this.add("TOGGLE_DETAIL", "Descriptive-Notify");
        this.add("TOGGLE_GEOIP", "GeoIP-Services");
        this.add("TOGGLE_REJOIN", "Rejoin-Warning");
        this.add("TOGGLE_INVALID", "You did not specify a valid option.");
        this.add("PURGE_SUC", "Successfully purged %s.");
        this.add("PURGE_ERR", "Failed to purge %s.");
        this.add("DISABLE_ERR", "This command has been disabled via configuration.");
        this.add("SCAN_CLEAN", "No players with multiple accounts are logged " +
                "in right now.");
        this.add("TIME_RANGE_EMPTY","No accounts were returned within the " +
                "date-range given.");
        this.add("RELOAD","Reload complete!");
        this.add("NO_MODIFY","No accounts were modified.");
        this.add("CMD_FETCH_ERR", "An error occurred while attempting to " +
                "fetch this Command from the Command Manager.");
        this.add("METRICS_ERR", "An error occurred while initializing the " +
                "Metrics system.");

        // Command Names
        this.add("CMD_CHECK","Check");
        this.add("CMD_RELOAD","Reload");
        this.add("CMD_ABOUT","About");
        this.add("CMD_TOGGLE","Toggle");
        this.add("CMD_HELP","Help");
        this.add("CMD_PURGE","Purge");
        this.add("CMD_SCAN","Scan");
        this.add("CMD_STATUS", "Status");

        // Command Help Documentation
        this.add("HELP_CHECK","Displays information about " +
                "the player or IP Specified.");
        this.add("HELP_RELOAD","Reloads the IP-Check plugin.");
        this.add("HELP_ABOUT","Displays Information about " +
                "IP-Check.");
        this.add("HELP_TOGGLE","Toggles the specified " +
                "option. For a list of options, type ''/ipc toggle help''");
        this.add("HELP_HELP","Provides information about " +
                "all of the associated IP-Check Commands.");
        this.add("HELP_PURGE","Deletes records of the IP or " +
                "Player name specified.");
        this.add("HELP_SCAN","Scans all players currently " +
                "online to check for any who may possess multiple accounts.");
        this.add("HELP_STATUS", "Displays IP-Check usage statistics.");

        // Other Messages
        this.add("SCAN_TITLE","Player Scan Results");
        this.add("SCAN_EXPLAIN","The following players were " +
                "found to have multiple accounts: ");
        this.add("LOGIN_WARN","Warning!");
        this.add("LOGIN_EXPLAIN"," may have multiple " +
                "accounts!");
        this.add("REJOIN_WARN", "Notice!");
        this.add("REJOIN_EXPLAIN", " was kicked from the server due to a " +
                "previous IP-Ban.");
        this.add("TIME_STAMP_ERR","An error occurred while " +
                "attempting to parse a time stamp. This should never happen. " +
                "If you see this message, please contact the developers at " +
                "dev-bukkit and inform them of the circumstances that caused " +
                "this error.");
        this.add("TOGGLE_HEAD","List of Toggle Options:");
        this.add("ABOUT_TEXT","Version %s build %s by Jacob Keep (Jnk1296). " +
                "All rights reserved. Built against %s %s, build %s by %s. " +
                "This product includes GeoLite data created by MaxMind, " +
                "available from: http://www.maxmind.com/");

        // Report Messages
        this.add("REPORT_HEAD_ONE","Alternate Accounts found "+
                "for: ");
        this.add("REPORT_HEAD_TWO","Alternate Accounts " +
                "found: ");

        this.add("REPORT_BODY_ONE","The following players " +
                "connect with the above IP address: ");
        this.add("REPORT_BODY_TWO","The following players " +
                "connect using the same IP address: ");
        this.add("REPORT_BODY_THREE","Players and IPs " +
                "associated with the search term: ");
        this.add("REPORT_BODY_FOUR","No alternate accounts " +
                "were found for this user.");

        this.add("REPORT_FOOT_LAST_IP","Last Known IP: ");
        this.add("REPORT_FOOT_LOCATION","Last Location: ");
        this.add("LOCATION_UNAVAILABLE","GeoIP Services unavailable.");

        this.add("REPORT_FOOT_PTIME", "Last Login: ");

        this.add("REPORT_FOOT_PREXM", "Will warn on Rejoin Attempt: ");

        this.add("REPORT_FOOT_ERROR","Player object returned "+
                "was NULL");

        // GeoIP / Block Messages
        this.add("GEOIP_DB_MISSING", "The GeoIP.dat database could not be found. " +
                "This file must be available in order for GeoIP Services to " +
                "function properly. It can be downloaded at: " +
                "http://geolite.maxmind.com/download/geoip/database/" +
                "GeoLiteCountry/GeoIP.dat.gz");
        this.add("GEOIP_DB_READ_ERR", "An error occurred while attempting to " +
                "read the GeoIP database.");
        this.add("GEOIP_DOWNLOAD", "Attempting automatic download of GeoIP " +
                "database from: http://geolite.maxmind.com/download/geoip/" +
                "database/GeoLiteCountry/GeoIP.dat.gz...");
        this.add("GEOIP_DISABLED", "GeoIP Services have been disabled via configuration.");

        // Stats Messages
        this.add("STATS_HEADER", "Plugin Usage Statistics: ");
        this.add("STATS_PVER", "IP-Check Version: ");
        this.add("STATS_DB_TYPE", "Database Type: ");
        this.add("STATS_PLOG", "Total Players Logged: ");
        this.add("STATS_ILOG", "Total IPs Logged: ");
        this.add("STATS_PLOGS", "Player Logins this Session: ");;
        this.add("STATS_WARNS", "Login Warnings this Session: ");
        this.add("STATS_SECURE", "Secure Mode Status: ");

        this.add("VER_COMP_ERR", "This version of IP-Check is not fully " +
                "compatible with the version of Bukkit you are running. " +
                "Please upgrade your Bukkit installation or downgrade " +
                "IP-Check to v2.0.2.");

        this.add("DEV_BUILD_WARN", "NOTICE: This is a development build of " +
                "IP-Check! Automatic Updater and Metrics have been disabled! " +
                "If you are seeing this message, please alert the plugin " +
                "developer, as this should not appear.");
    }

}
