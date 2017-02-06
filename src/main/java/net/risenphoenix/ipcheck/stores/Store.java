package net.risenphoenix.ipcheck.stores;

import net.risenphoenix.ipcheck.IPCheck;

public class Store {

    public final IPCheck plugin;

    public Store(final IPCheck ipCheck) {
        this.plugin = ipCheck;
    }

    public void initializeStore() {
        throw new UnsupportedOperationException(this.plugin
                .getLocalizationManager().getLocalString("NO_IMPLEMENT"));
    }

}
