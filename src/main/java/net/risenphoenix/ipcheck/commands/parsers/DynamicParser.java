package net.risenphoenix.ipcheck.commands.parsers;

import net.risenphoenix.ipcheck.commands.Command;
import net.risenphoenix.ipcheck.commands.CommandManager;
import net.risenphoenix.ipcheck.commands.ComparisonResult;

public class DynamicParser extends Parser {

    public DynamicParser(CommandManager mngr, Command cmd, String[] args) {
        super(mngr, cmd, args);
    }

    @Override
    public ComparisonResult parseCommand() {
        String[] COMMAND_ARGS = new String[this.cmd.getCallArgs().length];
        String[] INPUT_ARGS = new String[this.cmd.getCallArgs().length];

        for (int i = 0; i < this.cmd.getCallArgs().length; i++) {
            COMMAND_ARGS[i] = this.cmd.getCallArgs()[i];
        }

        for (int i = 0; i < this.cmd.getCallArgs().length; i++) {
            if (i >= this.input.length) {
                INPUT_ARGS[i] = "null";
            } else {
                INPUT_ARGS[i] = this.input[i];
            }
        }

        // Check for Matching Arguments
        for (int i = 0; i < COMMAND_ARGS.length; i++) {
            // Debug Output
            if (this.cmdManager.debugMode()) {
                System.out.println("Command Expected: " + COMMAND_ARGS[i]);
                System.out.println("Received: " + INPUT_ARGS[i]);
            }

            if (COMMAND_ARGS[i].equals("VAR_ARG_OPT")) continue;

            if (COMMAND_ARGS[i].equals("VAR_ARG") &&
                    INPUT_ARGS[i].equals("null")) {
                return ComparisonResult.ARG_ERR;
            } else if (COMMAND_ARGS[i].equals("VAR_ARG")) {
                continue;
            }

            if (!INPUT_ARGS[i].equalsIgnoreCase(COMMAND_ARGS[i])) {
                return ComparisonResult.BAD;
            }
        }

        return ComparisonResult.GOOD;
    }
}
