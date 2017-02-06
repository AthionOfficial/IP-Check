package net.risenphoenix.ipcheck.commands;

import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.commands.parsers.DynamicParser;
import net.risenphoenix.ipcheck.commands.parsers.Parser;
import net.risenphoenix.ipcheck.commands.parsers.StaticParser;
import net.risenphoenix.ipcheck.commands.parsers.VariableParser;
import net.risenphoenix.ipcheck.stores.CommandStore;

import java.util.ArrayList;

public class CommandManager {

	private final IPCheck plugin;
	private ArrayList<Command> commands = new ArrayList<Command>();

	private boolean debugMode = false;

	public CommandManager(final IPCheck ipCheck) {
		this.plugin = ipCheck;
	}

	public final void registerStore(CommandStore cmdStore) {
		this.commands = cmdStore.getCommands();
	}

	public final boolean registerCommand(Command cmd) {
		if (this.commands.add(cmd)) {
			return true;
		} else {
			plugin.getLogger().warning(
					this.plugin.getLocalizationManager().
					getLocalString("CMD_REG_ERR") +
					cmd.getName());

			return false;
		}
	}

	public final Command getCommand(String identifier) {
		for (Command cmd : this.commands) {
			if (cmd.getName().equalsIgnoreCase(identifier)) return cmd;
		}

		return null;
	}

	public final ArrayList<Command> getAllCommands() {
		return this.commands;
	}

	public final ParseResult parseCommand(String[] args) {
		for (Command cmd : this.commands) {
			Parser parser;

			if (cmd.getType() == CommandType.STATIC) {
				parser = new StaticParser(this, cmd, args);
			} else if (cmd.getType() == CommandType.VARIABLE) {
				parser = new VariableParser(this, cmd, args);
			} else if (cmd.getType() == CommandType.DYNAMIC) {
				parser = new DynamicParser(this, cmd, args);
			} else {
				parser = new StaticParser(this, cmd, args);
			}

			ComparisonResult result = parser.parseCommand();

			if (result.equals(ComparisonResult.GOOD)) {
				return new ParseResult(ResultType.SUCCESS, cmd);
			} else if (result.equals(ComparisonResult.ARG_ERR)) {
				return new ParseResult(ResultType.BAD_NUM_ARGS, cmd);
			}
		}

		return new ParseResult(ResultType.FAIL, null);
	}

	public IPCheck getPlugin() {
		return this.plugin;
	}

	public void setDebugMode(boolean flag) {
		this.debugMode = flag;
	}

	public boolean debugMode() {
		return this.debugMode;
	}
}
