package net.risenphoenix.ipcheck.commands.parsers;

import net.risenphoenix.ipcheck.commands.Command;
import net.risenphoenix.ipcheck.commands.CommandManager;
import net.risenphoenix.ipcheck.commands.ComparisonResult;

public class Parser {

    public CommandManager cmdManager;
    public Command cmd;
    public String[] input;

    public Parser(CommandManager mngr, Command cmd, String[] input) {
        this.cmdManager =  mngr;
        this.cmd = cmd;
        this.input = input;
    }

    public ComparisonResult parseCommand(){
        throw new UnsupportedOperationException(this.cmdManager.getPlugin()
                .getLocalizationManager().getLocalString("BAD_PARSE_SET"));
    }

}
