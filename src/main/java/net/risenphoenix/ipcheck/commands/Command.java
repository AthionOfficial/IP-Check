package net.risenphoenix.ipcheck.commands;

import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.LocalizationManager;

import org.bukkit.command.CommandSender;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.permissions.Permission;

import java.util.logging.Level;

public class Command extends ValidatingPrompt {
    private final IPCheck plugin;
    private final CommandType type;

    private String name;
    private String syntax;
    private String help;

    private boolean isConsoleExecutable = true;

    private String[] callArgs;
    private int requiredArgs = 0;

    private Permission[] commandPerms = null;

    private LocalizationManager LM;
    private ConversationFactory conFactory = null;

    public Command(final IPCheck plugin2, String[] callArgs, CommandType type) {
        this.plugin = plugin2;
        this.callArgs = callArgs;
        this.type = type;
        this.LM = this.plugin.getLocalizationManager();
    }

    public final boolean onCall(CommandSender sender, String[] args) {
        if (!canExecute(sender)) return false;
        onExecute(sender, args);
        return true;
    }

    public final void setConsoleExecutable(boolean consoleCanExecute) {
        this.isConsoleExecutable = consoleCanExecute;
    }

    public void onExecute(CommandSender sender, String[] args) {
        throw new UnsupportedOperationException(
                this.getLocalString("NO_IMPLEMENT"));
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(String cmdName) {
        this.name = cmdName;
    }

    public final String getSyntax() {
        return this.syntax;
    }

    public final void setSyntax(String cmdSyntax) {
        this.syntax = cmdSyntax;
    }

    public final String getHelp() {
        return this.help;
    }

    public final void setHelp(String helpDesc) {
        this.help = helpDesc;
    }

    public final Permission[] getPermissions() {
        return this.commandPerms;
    }

    public final void setPermissions(Permission[] perms) {
        this.commandPerms = perms;
    }

    public final boolean canConsoleExecute() {
        return this.isConsoleExecutable;
    }

    public final String[] getCallArgs() {
        return this.callArgs;
    }

    @Deprecated
    public final int getArgumentsRequired() {
        return this.requiredArgs;
    }

    public final boolean canExecute(CommandSender sender) {
        if (this.commandPerms == null || sender.isOp()) return true;

        for (int i = 0; i < this.commandPerms.length; i++) {
            if (!sender.hasPermission(this.commandPerms[i]))return false;
        }

        return true;
    }

    public final String getLocalString(String key) {
        return this.LM.getLocalString(key);
    }


    public final IPCheck getPlugin() {
        return this.plugin;
    }

    public final CommandType getType() {
        return this.type;
    }

    public final void sendPlayerMessage(CommandSender sender, String message) {
        this.sendPlayerMessage(sender, message, true);
    }

    public final void sendPlayerMessage(CommandSender sender, String message,
                                        boolean useName) {
        this.plugin.sendPlayerMessage(sender, message, useName);
    }

    public final void sendConsoleMessage(Level level, String message) {
        this.plugin.getLogger().info(message);
    }

    public final void setConversationFactory(ConversationFactory factory) {
        this.conFactory = factory;
    }

    public final ConversationFactory getConversationFactory() {
        return this.conFactory;
    }

    public String getPromptText(ConversationContext context) {
        throw new UnsupportedOperationException(
                this.getLocalString("NO_IMPLEMENT"));
    }

    @Override
    public Prompt acceptValidatedInput(ConversationContext context, String in) {
        throw new UnsupportedOperationException(
                this.getLocalString("NO_IMPLEMENT"));
    }

    @Override
    public boolean isInputValid (ConversationContext context, String in) {
        throw new UnsupportedOperationException(
                this.getLocalString("NO_IMPLEMENT"));
    }
}
