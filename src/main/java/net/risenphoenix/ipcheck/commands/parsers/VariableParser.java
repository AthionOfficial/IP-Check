package net.risenphoenix.ipcheck.commands.parsers;

import net.risenphoenix.ipcheck.commands.Command;
import net.risenphoenix.ipcheck.commands.CommandManager;
import net.risenphoenix.ipcheck.commands.ComparisonResult;

public class VariableParser extends Parser {

    public VariableParser(CommandManager mngr, Command cmd, String[] args) {
        super(mngr, cmd, args);
    }

    @Override
    public ComparisonResult parseCommand() {
        int callSize = this.cmd.getCallArgs().length;
        int inputSize = this.input.length;

        String[] COMMAND_ARGS;
        String[] INPUT_ARGS;

        if (callSize > inputSize) {
            COMMAND_ARGS = new String[callSize];
            INPUT_ARGS = new String[callSize];
        } else {
            COMMAND_ARGS = new String[inputSize];
            INPUT_ARGS = new String[inputSize];
        }

        for (int i = 0; i < this.cmd.getCallArgs().length; i++) {
            COMMAND_ARGS[i] = this.cmd.getCallArgs()[i];
        }
        for (int i = 0; i < this.input.length; i++) {
            INPUT_ARGS[i] = this.input[i];
        }

        if (callSize > inputSize) {
            for (int i = inputSize; i < INPUT_ARGS.length; i++) {
                INPUT_ARGS[i] = "null";
            }
        } else {
            for (int i = callSize; i < COMMAND_ARGS.length; i++) {
                COMMAND_ARGS[i] = "null";
            }
        }

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

            if (COMMAND_ARGS[i].equals("null")) {
                return ComparisonResult.ARG_ERR;
            }

            if (!INPUT_ARGS[i].equalsIgnoreCase(COMMAND_ARGS[i])) {
                return ComparisonResult.BAD;
            }
        }

        return ComparisonResult.GOOD;
    }

}
