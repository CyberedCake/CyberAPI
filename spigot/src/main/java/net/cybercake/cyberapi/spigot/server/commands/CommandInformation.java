package net.cybercake.cyberapi.spigot.server.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.cybercake.cyberapi.spigot.chat.TabCompleteType;
import net.cybercake.cyberapi.spigot.chat.UTabComp;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

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
        private LiteralCommandNode<?> node = null;
        private boolean useFolderCommodore = false;

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
         * @since 12
         */
        public Builder setPermission(String permission) {
            this.permission = permission; return this;
        }

        /**
         * Sets the permission required to run the command and the message that sends if that permission is not met
         * @param permission the required permission
         * @param permissionMessage what to send if {@code permission} is not met
         * @since 12
         */
        public Builder setPermission(String permission, String permissionMessage) {
            this.permission = permission; this.permissionMessage = permissionMessage; return this;
        }

        /**
         * Sets the description of the command
         * @param description the command's description
         * @since 12
         */
        public Builder setDescription(String description) {
            this.description = description; return this;
        }

        /**
         * Sets the correct usage of the command
         * @param usage the command's correct usage
         * @since 12
         */
        public Builder setUsage(String usage) {
            this.usage = usage; return this;
        }

        /**
         * Sets the aliases of the command, a.k.a. new commands that do the same thing as this command
         * @param aliases the aliases of the command
         * @since 12
         */
        public Builder setAliases(String... aliases) {
            this.aliases = aliases; return this;
        }

        /**
         * Sets the aliases of the command, a.k.a. new commands that do the same thing as this command
         * @param aliases the aliases of the command, in {@link List} form
         * @since 12
         */
        public Builder setAliases(List<String> aliases) {
            this.aliases = aliases.toArray(new String[0]); return this;
        }

        /**
         * Sets the types of tab completions for {@link Command#tab(CommandSender, String, CommandInformation, String[])}
         * @param tabCompleteType the type of tab completions
         * @since 12
         * @see UTabComp#tabCompletions(TabCompleteType, String, List)
         */
        public Builder setTabCompleteType(TabCompleteType tabCompleteType) {
            this.tabCompleteType = tabCompleteType; return this;
        }

        /**
         * Sets the commodore {@link com.mojang.brigadier.builder.LiteralArgumentBuilder LiteralArgumentBuilder} for this command
         * @param node the node for the command
         * @since 46
         * @see Builder#setCommodore(boolean)
         * @deprecated please use {@link Builder#setCommodore(LiteralCommandNode)} and build your {@link com.mojang.brigadier.builder.LiteralArgumentBuilder LiteralArgumentBuilder} instead
         */
        @Deprecated public Builder setCommodore(com.mojang.brigadier.builder.LiteralArgumentBuilder<?> node) {
            this.node = node.build(); return this;
        }

        /**
         * Sets the commodore {@link LiteralCommandNode} for this command
         * @param node the node for the command
         * @since 62
         * @see Builder#setCommodore(boolean) setCommodore(useFolder)
         */
        public Builder setCommodore(LiteralCommandNode<?> node) {
            this.node = node; return this;
        }

        /**
         * Sets whether commodore should use the folder instead of {@link Builder#setCommodore(LiteralCommandNode)}
         * @param useFolder whether to use folder, default is 'false', the "folder" in question will be your 'resources/commodore/' folder of your plugin
         * @since 46
         * @see Builder#setCommodore(LiteralCommandNode)
         */
        public Builder setCommodore(boolean useFolder) {
            this.useFolderCommodore = useFolder; return this;
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
     * @since 12
     */
    public String getName() { return builder.name; }

    /**
     * @return the permission required to execute the command
     * @since 12
     */
    public String getPermission() { return builder.permission; }

    /**
     * @return the permission message that is sent when the sender doesn't have the correct permission
     * @since 12
     */
    public String getPermissionMessage() { return builder.permissionMessage; }

    /**
     * @return the description of the command
     * @since 12
     */
    public String getDescription() { return builder.description; }

    /**
     * @return the correct usage of the command
     * @since 12
     */
    public String getUsage() { return builder.usage; }

    /**
     * @return the aliases (other ways to execute the main command)
     * @since 12
     */
    public String[] getAliases() { return builder.aliases; }

    /**
     * @return the tab complete type
     * @since 12
     * @see UTabComp#tabCompletions(TabCompleteType, String, List)
     */
    public TabCompleteType getTabCompleteType() { return builder.tabCompleteType; }

    /**
     * @return the commodore {@link LiteralCommandNode}
     * @since 46
     */
    public @Nullable LiteralCommandNode<?> getCommodoreNode() { return builder.node; }

    /**
     * @return should use 'resources/commodore/' folder and commodore file format
     * @since 46
     */
    public boolean shouldUseFolderCommodore() { return builder.useFolderCommodore; }

    protected Builder getCommandInformationBuilder() { return this.builder; }

}
