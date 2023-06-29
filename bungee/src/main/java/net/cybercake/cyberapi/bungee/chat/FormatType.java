package net.cybercake.cyberapi.bungee.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;

public class FormatType {

    static class SpecificInput<T, R> implements ChatFormatType<T, R> {
        
        private final String name;
        private final Class<R> returnType;
        private final Function<T, R> execution;

        SpecificInput(String name, @NotNull Class<R> returnType, Function<T, R> execution) {
            this.name = name;
            this.returnType = returnType;
            this.execution = execution;
        }

        @Override public Class<R> getReturnType() { return this.returnType; }
        @Override public String getName() { return this.name; }

        @Override
        public R execute(T input) {
            return this.execution.apply(input);
        }
    }

    // legacy is a separate class as there is an input character
    static class LegacyInput<T, R> implements ChatFormatType<T, R> {

        private final String name;
        private final Class<R> returnType;
        private final BiFunction<T, Character, R> execution;

        LegacyInput(String name, Class<R> returnType, BiFunction<T, Character, R> execution) {
            this.name = name;
            this.returnType = returnType;
            this.execution = execution;
        }

        @Override public Class<R> getReturnType() { return this.returnType; }
        @Override public String getName() { return this.name; }

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
    static class MiniMessageInput implements ChatFormatType<String, Component> {

        private final String name;
        private final BiFunction<String, MiniMessage, Component> execution;

        MiniMessageInput(String name, BiFunction<String, MiniMessage, Component> execution) {
            this.name = name;
            this.execution = execution;
        }

        @Override public Class<Component> getReturnType() { return Component.class; }
        @Override public String getName() { return this.name; }

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
