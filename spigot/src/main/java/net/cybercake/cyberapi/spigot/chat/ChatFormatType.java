package net.cybercake.cyberapi.spigot.chat;

import net.cybercake.cyberapi.common.chat.LegacyToMiniMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents the certain chat format type used for {@link UChat#format(ChatFormatType, Object) UChat}.
 * Listed below in the "See Also" section are all the available format options.
 * @param <T> the input type of the method, usually a {@link String}
 * @param <R> the output type that will be returned by the method, usually a {@link String} or {@link Component}
 * @see ChatFormatType#LEGACY
 * @see ChatFormatType#BUNGEE_COMPONENT
 * @see ChatFormatType#COMPONENT
 * @see ChatFormatType#MINI_MESSAGE
 * @see ChatFormatType#COMBINED
 * @since 139
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public interface ChatFormatType<T, R> {

    /**
     * A legacy chat format type.
     * Converts "{@code &cSome text &asome text}" into initially {@link LegacyToMiniMessage#RED red text} and then {@link LegacyToMiniMessage#GREEN green text}.
     * @since 139
     */
    ChatFormatType<String, String> LEGACY = new LegacyFormatType<>(String.class, (input, character) ->
            ChatColor.translateAlternateColorCodes(character, input)
    );

    /**
     * A legacy chat format type in the form of {@link BaseComponent BungeeCord's (old) component system}.
     * Converts "{@code &cSome text &asome text}" into a serialized {@link TextComponent Bungee component} from initially {@link LegacyToMiniMessage#RED red text} and then {@link LegacyToMiniMessage#GREEN green text}.
     * @since 139
     */
    ChatFormatType<String, BaseComponent> BUNGEE_COMPONENT = new LegacyFormatType<>(BaseComponent.class, (input, character) ->
            new TextComponent(((LegacyFormatType<String, String>)LEGACY).execute(input, character))
    );

    /**
     * A chat format type in the form of {@link Component Adventure API's component system}.
     * Converts "{@code &cSome text &asome text}" into a serialized {@link Component Adventure API component} from initially {@link LegacyToMiniMessage#RED red text} and then {@link LegacyToMiniMessage#GREEN green text}.
     * @since 139
     */
    ChatFormatType<String, Component> COMPONENT = new LegacyFormatType<>(Component.class, (input, character) ->
        LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().character(character).build().deserialize(input).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    );

    /**
     * A chat format type in the form of a {@link Component Adventure API's component system}.
     * Converts "{@code <red>Some text <green>some text}" into a serialized {@link Component Adventure API component} from initially {@link LegacyToMiniMessage#RED red text} and then {@link LegacyToMiniMessage#GREEN green text}.
     * @since 139
     */
    ChatFormatType<String, Component> MINI_MESSAGE = new MiniMessageFormatType((input, miniMessage) -> miniMessage.deserialize(input));

    /**
     * A chat format type in the form of a {@link Component Adventure API's component system}.
     * Converts "{@code <red>Some text &asome text}" into a serialized {@link Component Adventure API component} from initially {@link LegacyToMiniMessage#RED red text} and then {@link LegacyToMiniMessage#GREEN green text}.
     * @since 139
     */
    ChatFormatType<String, Component> COMBINED = new MiniMessageFormatType((input, miniMessage) -> {
        String output = input;
        for(LegacyToMiniMessage legacy : LegacyToMiniMessage.values())
            output = legacy.cleanse(output);
        return ((MiniMessageFormatType) MINI_MESSAGE).execute(output, miniMessage);
    });


    /**
     * Executes the specified chat format type and returns its result.
     * @param input the input, likely in {@link String} form
     * @return the output, likely in a {@link String} form or a {@link Component} form
     * @since 139
     */
    R execute(T input);

    /**
     * @return gets the {@link Class} type of the returned output
     * @since 139
     */
    Class<R> getReturnType();

    class SpecificGenericFormat<T, R> implements ChatFormatType<T, R> {

        private final Class<R> returnType;
        private final Function<T, R> execution;

        SpecificGenericFormat(@NotNull Class<R> returnType, Function<T, R> execution) {
            this.returnType = returnType;
            this.execution = execution;
        }

        @Override public Class<R> getReturnType() { return this.returnType; }

        @Override
        public R execute(T input) {
            return this.execution.apply(input);
        }
    }

    // legacy is a separate class as there is an input character
    class LegacyFormatType<T, R> implements ChatFormatType<T, R> {

        private final Class<R> returnType;
        private final BiFunction<T, Character, R> execution;

        LegacyFormatType(Class<R> returnType, BiFunction<T, Character, R> execution) { this.returnType = returnType; this.execution = execution; }

        @Override public Class<R> getReturnType() { return this.returnType; }

        /**
         * Executes the specified chat format type and returns its result using a character for the alternate character
         * @param input the input, likely in {@link String} form
         * @param alternateCharacter the alternate character used in the char part of {@link ChatColor#translateAlternateColorCodes(char, String)}
         * @return the output, likely in a {@link String} form or a {@link Component} form
         * @since 139
         */
        public R execute(T input, Character alternateCharacter) { return this.execution.apply(input, alternateCharacter); }

        @Override public R execute(T input) { return this.execute(input, '&'); }
    }

    // mini message is a separate class as there is an input constructor
    class MiniMessageFormatType implements ChatFormatType<String, Component> {

        private final BiFunction<String, MiniMessage, Component> execution;

        MiniMessageFormatType(BiFunction<String, MiniMessage, Component> execution) { this.execution = execution; }

        @Override public Class<Component> getReturnType() { return Component.class; }

        /**
         * Executes the specified chat format type and returns its result using a specific {@link MiniMessage} builder.
         * @param input the input in {@link String} form
         * @param miniMessage the {@link MiniMessage} builder to apply to the {@link String} input
         * @return the output in a {@link Component} form
         * @since 139
         */
        public Component execute(String input, MiniMessage miniMessage) { return this.execution.apply(input, miniMessage); }

        @Override public Component execute(String input) {
            return this.execute(input, MiniMessage.builder().build());
        }
    }

}
