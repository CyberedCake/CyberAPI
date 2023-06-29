package net.cybercake.cyberapi.spigot.chat;

import net.cybercake.cyberapi.common.chat.LegacyToMiniMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
    ChatFormatType<String, String> LEGACY = new FormatType.LegacyInput<>("LEGACY", String.class, (input, character) ->
            ChatColor.translateAlternateColorCodes(character, input)
    );

    /**
     * A legacy chat format type in the form of {@link BaseComponent BungeeCord's (old) component system}.
     * Converts "{@code &cSome text &asome text}" into a serialized {@link TextComponent Bungee component} from initially {@link LegacyToMiniMessage#RED red text} and then {@link LegacyToMiniMessage#GREEN green text}.
     * @since 139
     */
    ChatFormatType<String, BaseComponent> BUNGEE_COMPONENT = new FormatType.LegacyInput<>("BUNGEE_COMPONENT", BaseComponent.class, (input, character) ->
            new TextComponent(((FormatType.LegacyInput<String, String>)LEGACY).execute(input, character))
    );

    /**
     * A chat format type in the form of {@link Component Adventure API's component system}.
     * Converts "{@code &cSome text &asome text}" into a serialized {@link Component Adventure API component} from initially {@link LegacyToMiniMessage#RED red text} and then {@link LegacyToMiniMessage#GREEN green text}.
     * @since 139
     */
    ChatFormatType<String, Component> COMPONENT = new FormatType.LegacyInput<>("COMPONENT", Component.class, (input, character) ->
        LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().character(character).build().deserialize(input).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    );

    /**
     * A chat format type in the form of a {@link Component Adventure API's component system}.
     * Converts "{@code <red>Some text <green>some text}" into a serialized {@link Component Adventure API component} from initially {@link LegacyToMiniMessage#RED red text} and then {@link LegacyToMiniMessage#GREEN green text}.
     * @since 139
     */
    ChatFormatType<String, Component> MINI_MESSAGE = new FormatType.MiniMessageInput("MINI_MESSAGE", (input, miniMessage) -> miniMessage.deserialize(input));

    /**
     * A chat format type in the form of a {@link Component Adventure API's component system}.
     * Converts "{@code <red>Some text &asome text}" into a serialized {@link Component Adventure API component} from initially {@link LegacyToMiniMessage#RED red text} and then {@link LegacyToMiniMessage#GREEN green text}.
     * @since 139
     */
    ChatFormatType<String, Component> COMBINED = new FormatType.MiniMessageInput("COMBINED", (input, miniMessage) -> {
        String output = input;
        for(LegacyToMiniMessage legacy : LegacyToMiniMessage.values())
            output = legacy.cleanse(output);
        return ((FormatType.MiniMessageInput) MINI_MESSAGE).execute(output, miniMessage);
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

    /**
     * @return gets the specific name as if the value was an enum
     * @since 140
     */
    String getName();

    /**
     * @return gets the {@link List} of potential {@link ChatFormatType ChatFormatTypes}
     * @since 142
     */
    static List<ChatFormatType<?, ?>> values() {
        try {
            Field[] fields = ChatFormatType.class.getDeclaredFields();
            List<ChatFormatType<?, ?>> values = new ArrayList<>();
            for(Field field : fields)
                values.add((ChatFormatType<?, ?>) field.get(null));
            return values;
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to get " + ChatFormatType.class.getCanonicalName() + " values (CyberAPI)", exception);
        }
    }

    /**
     * @param name the name of the chat format type using its {@link String} literal
     * @return gets the {@link ChatFormatType}, this is essentially like doing {@code ChatFormatType.<name>} (i.e., {@link ChatFormatType#LEGACY ChatFormatType.LEGACY})
     * @since 142
     */
    @SuppressWarnings("unchecked")
    static <T, R> @Nullable ChatFormatType<T, R> valueOf(String name) {
        return (ChatFormatType<T, R>) values().stream().filter(type -> type.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
