package net.risenphoenix.ipcheck.stores;

import net.risenphoenix.commons.configuration.ConfigurationOption;
import net.risenphoenix.ipcheck.IPCheck;

import java.util.ArrayList;

public class ConfigurationStore extends Store {

    private ArrayList<ConfigurationOption> options;

    public ConfigurationStore(final IPCheck plugin) {
        super(plugin);
        this.options = new ArrayList<ConfigurationOption>();
        initializeStore();
    }

    public final void add(ConfigurationOption option) {
        this.options.add(option);
    }

    public final ArrayList<ConfigurationOption> getOptions() {
        return this.options;
    }
}
