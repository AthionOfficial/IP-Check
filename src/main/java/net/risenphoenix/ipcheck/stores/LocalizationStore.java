package net.risenphoenix.ipcheck.stores;

import net.risenphoenix.ipcheck.IPCheck;

import java.util.HashMap;
import java.util.Map;

public class LocalizationStore extends Store {

    private Map<String,String> values;

    public LocalizationStore(final IPCheck plugin) {
        super(plugin);
        this.values = new HashMap<String, String>();
        initializeStore();
    }

    public final void add(String key, String value) {
        this.values.put(key, value);
    }

    public final Map<String,String> getValues() {
        return this.values;
    }
}
