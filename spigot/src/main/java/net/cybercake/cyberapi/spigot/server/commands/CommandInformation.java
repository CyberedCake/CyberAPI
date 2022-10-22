package net.cybercake.cyberapi.spigot.server.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.cybercake.cyberapi.spigot.chat.TabCompleteType;
import net.cybercake.cyberapi.spigot.chat.UTabComp;
import net.cybercake.cyberapi.spigot.server.commands.cooldown.CommandCooldown;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class CommandInformation implements Serializable {

    /**
     * Gets a new instance of the {@link Builder} for {@link CommandInformation}
     * @param name the name of the command, with the slash omitted
     * @return the {@link Builder} instance
     * @since 41
     */
    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder implements Serializable {
        private final String name;
        private String permission = "";
        private String permissionMessage = "";
        private CommandCooldown cooldown = null;
        private String description = "";
        private String usage = "";
        private String[] aliases = new String[]{};
        private TabCompleteType tabCompleteType = TabCompleteType.NONE;
        private LiteralCommandNode<?> node = null;
        private boolean useFolderCommodore = false;
        private boolean autoRegister = true;

        /**
         * Creates an instance of {@link Builder}, allowing you to customize the stored information on the command.
         * @param name the name of the command, without the slash
         * @since 12
         * @deprecated there is no reason to make a {@link Builder} this way, please use {@link SpigotCommand#newCommand(String)}
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
         * Sets the types of tab completions for {@link SpigotCommand#tab(CommandSender, String, CommandInformation, String[])}
         * @param tabCompleteType the type of tab completions
         * @since 12
         * @see UTabComp#tabCompletions(TabCompleteType, String, List)
         */
        public Builder setTabCompleteType(TabCompleteType tabCompleteType) {
            this.tabCompleteType = tabCompleteType; return this;
        }

        /**
         * Sets the cooldown for the command, a {@link CommandCooldown} object
         * <br>
         *  (<b>note, not persistent over server restarts</b>)
         * @param cooldown the cooldown in between command usages
         * @since 79
         * @see CommandCooldown
         */
        public Builder setCooldown(CommandCooldown cooldown) {
            this.cooldown = cooldown; return this;
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
         * Sets whether this command should auto-register with the server or require you to specify it in your main class. Default is {@link Boolean#TRUE true}.
         * @param shouldAutoRegister whether to auto-register with the {@link org.bukkit.command.SimpleCommandMap SimpleCommandMap}
         * @since 94
         */
        public Builder setAutoRegister(boolean shouldAutoRegister) {
            this.autoRegister = shouldAutoRegister; return this;
        }

        /**
         * Builds the command information
         * @return the built command information
         */
        public CommandInformation build() {
            return new CommandInformation(this);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "{" +
                    "name='" + name + '\'' +
                    ", permission='" + permission + '\'' +
                    ", permissionMessage='" + permissionMessage + '\'' +
                    ", cooldown=" + cooldown +
                    ", description='" + description + '\'' +
                    ", usage='" + usage + '\'' +
                    ", aliases=" + Arrays.toString(aliases) +
                    ", tabCompleteType=" + tabCompleteType +
                    ", node=" + node +
                    ", useFolderCommodore=" + useFolderCommodore +
                    ", autoRegister=" + autoRegister +
                    '}';
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
     * @return the command cooldown (<b>note, not persistent over server restarts</b>)
     * @since 79
     * @see CommandCooldown
     */
    public CommandCooldown getCooldown() { return builder.cooldown; }

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

    /**
     * @return should CyberAPI attempt to auto-register your command with the {@link org.bukkit.command.SimpleCommandMap SimpleCommandMap}
     * @since 94
     */
    public boolean shouldAutoRegister() { return builder.autoRegister; }

    protected Builder getCommandInformationBuilder() { return this.builder; }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "builder=" + builder +
                '}';
    }
}
