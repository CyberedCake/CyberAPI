package net.cybercake.cyberapi.bungee.server.commands;

import net.cybercake.cyberapi.bungee.chat.TabCompleteType;
import net.cybercake.cyberapi.bungee.chat.UTabComp;
import net.md_5.bungee.api.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandInformation {

    /**
     * Gets a new instance of the {@link Builder} for {@link CommandInformation}
     * @param name the name of the command, with the slash ommitted
     * @return the {@link Builder} instance
     * @since 41
     */
    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder {
        private final String name;
        private String permission = "";
        private String permissionMessage = "";
        private String description = "";
        private String usage = "";
        private String[] aliases = new String[]{};
        private TabCompleteType tabCompleteType = TabCompleteType.NONE;
        private List<Builder> additionalCommands = new ArrayList<>();

        /**
         * Creates an instance of {@link Builder}, allowing you to customize the stored information on the command.
         * @param name the name of the command, without the slash
         * @since 12
         * @deprecated there is no reason to make a {@link Builder} this way, please use {@link Command#newCommand(String)}
         */
        @Deprecated
        @SuppressWarnings({"all"})
        public Builder(String name) {
            this.name = name;
        }

        /**
         * Sets the permission required to run the command
         * @param permission the required permission
         * @since 15
         */
        public Builder setPermission(String permission) {
            this.permission = permission; return this;
        }

        /**
         * Sets the permission required to run the command
         * @param permission the required permission
         * @since 15
         */
        public Builder setPermission(String permission, String permissionMessage) {
            this.permission = permission; this.permissionMessage = permissionMessage; return this;
        }


        /**
         * Sets the description of the command
         * @param description the command's description
         * @since 15
         */
        public Builder setDescription(String description) {
            this.description = description; return this;
        }

        /**
         * Sets the correct usage of the command
         * @param usage the command's correct usage
         * @since 15
         */
        public Builder setUsage(String usage) {
            this.usage = usage; return this;
        }

        /**
         * Sets the aliases of the command, a.k.a. new commands that do the same thing as this command
         * @param aliases the aliases of the command
         * @since 15
         */
        public Builder setAliases(String... aliases) {
            this.aliases = aliases; return this;
        }

        /**
         * Sets the aliases of the command, a.k.a. new commands that do the same thing as this command
         * @param aliases the aliases of the command, in {@link List} form
         * @since 15
         */
        public Builder setAliases(List<String> aliases) {
            this.aliases = aliases.toArray(new String[0]); return this;
        }

        /**
         * Sets the types of tab completions for {@link Command#tab(CommandSender, CommandInformation, String[])}
         * @param tabCompleteType the type of tab completions
         * @since 15
         * @see UTabComp#tabCompletions(TabCompleteType, String, List)
         */
        public Builder setTabCompleteType(TabCompleteType tabCompleteType) {
            this.tabCompleteType = tabCompleteType; return this;
        }

        /**
         * Adds additional commands to this command.
         * <br> <br>
         * Note: you will have to do an additional method to get the {@link Builder} for multiple commands: {@code getAdditionalCommand(index)} or {@code getAdditionalCommands(name)}
         * @param information the additional command to ad
         * @since 41
         */
        public Builder addAdditionalCommand(Builder information) {
            this.additionalCommands.add(information); return this;
        }

        /**
         * Builds the command information
         * @return the built command information
         */
        public CommandInformation build() {
            return new CommandInformation(this);
        }
    }

    private final Builder builder;

    public CommandInformation(Builder builder) {
        this.builder = builder;
    }

    /**
     * @return the name of the command with the slash omitted
     * @since 15
     */
    public String getName() { return builder.name; }

    /**
     * @return the permission required to execute the command
     * @since 15
     */
    public String getPermission() { return builder.permission; }

    /**
     * @return the permission message that is sent when the sender doesn't have the correct permission
     * @since 15
     */
    public String getPermissionMessage() { return builder.permissionMessage; }

    /**
     * @return the description of the command
     * @since 15
     */
    public String getDescription() { return builder.description; }

    /**
     * @return the correct usage of the command
     * @since 15
     */
    public String getUsage() { return builder.usage; }

    /**
     * @return the aliases (other ways to execute the main command)
     * @since 15
     */
    public String[] getAliases() { return builder.aliases; }

    /**
     * @return the tab complete type
     * @since 15
     * @see UTabComp#tabCompletions(TabCompleteType, String, List)
     */
    public TabCompleteType getTabCompleteType() { return builder.tabCompleteType; }

}
