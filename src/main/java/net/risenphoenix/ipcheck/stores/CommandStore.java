package net.risenphoenix.ipcheck.stores;

import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.commands.Command;

import java.util.ArrayList;

public class CommandStore extends Store {

    private ArrayList<Command> commands;

    public CommandStore(final IPCheck ipCheck) {
        super(ipCheck);
        this.commands = new ArrayList<Command>();
        initializeStore();
    }

    public void initializeStore() {
        throw new UnsupportedOperationException(this.plugin
                .getLocalizationManager().getLocalString("NO_IMPLEMENT"));
    }

    public final void add(Command cmd) {
        this.commands.add(cmd);
    }

    public final ArrayList<Command> getCommands() {
        return this.commands;
    }

}
