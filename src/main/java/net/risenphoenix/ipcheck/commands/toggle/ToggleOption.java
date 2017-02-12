package net.risenphoenix.ipcheck.commands.toggle;

import org.bukkit.configuration.file.FileConfiguration;

import net.risenphoenix.ipcheck.IPCheck;

public class ToggleOption {

    private IPCheck plugin;
    private String optionID;
    private String displayID;
    private String[] callValues;

    public ToggleOption(IPCheck ipCheck, String optionID, String displayID,
                        String[] callValues) {
        this.plugin = ipCheck;
        this.optionID = optionID;
        this.displayID = displayID;
        this.callValues = callValues;
    }

    public final boolean onExecute() {
        /* Fetch Configuration Manager and current option value (inverted)
         * for use with change operation. */
        FileConfiguration config = this.plugin.getConfig();
        boolean newValue = !config.getBoolean(this.optionID);

        // Set Configuration Option to new value
        config.set(this.optionID, newValue);

        // Return new value
        return newValue;
    }

    public final String[] getCallValues() {
        return this.callValues;
    }

    public final String getOptionID() {
        return this.optionID;
    }

    public final String getDisplayID() {
        return plugin.getLocalizationManager().getLocalString(displayID);
    }

}
