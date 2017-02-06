package net.risenphoenix.ipcheck.stores;

import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.commands.*;
import net.risenphoenix.ipcheck.commands.toggle.CmdToggle;

public class CmdStore extends CommandStore {

    public CmdStore(final IPCheck ipCheck) {
        super(ipCheck);
    }

    @Override
    public void initializeStore() {

        // Help Command
        this.add(
                new CmdHelp(plugin, new String[]{"ipc", "help", "VAR_ARG_OPT"},
                        CommandType.VARIABLE));

        // Scan Command
        this.add(
                new CmdScan(plugin, new String[]{"ipc", "scan"},
                        CommandType.STATIC));

        // Status Command
        this.add(
                new CmdStatus(plugin, new String[]{"ipc", "status"},
                        CommandType.STATIC));

        // Purge Command
        this.add(
                new CmdPurge(plugin, new String[]{"ipc", "purge", "VAR_ARG"},
                        CommandType.VARIABLE));

        // Toggle Command
        this.add(
                new CmdToggle(plugin, new String[]{"ipc", "toggle", "VAR_ARG"},
                        CommandType.VARIABLE));

        // Reload Command
        this.add(
                new CmdReload(plugin, new String[]{"ipc", "reload"},
                        CommandType.STATIC));

        // ROOT COMMAND
        this.add(
                new CmdCheck(plugin, new String[]{"ipc", "VAR_ARG"},
                        CommandType.VARIABLE));
    }
}
