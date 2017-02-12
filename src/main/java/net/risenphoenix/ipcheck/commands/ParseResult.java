package net.risenphoenix.ipcheck.commands;

public class ParseResult {

    private final Command cmd;
    private final ResultType type;

    public ParseResult(final ResultType type, final Command cmd) {
        this.type = type;
        this.cmd = cmd;
    }

    public final ResultType getResult() {
        return this.type;
    }

    public final Command getCommand() {
        return this.cmd;
    }
}
