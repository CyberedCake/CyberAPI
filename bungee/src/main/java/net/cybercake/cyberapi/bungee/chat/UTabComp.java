package net.cybercake.cyberapi.bungee.chat;

import net.cybercake.cyberapi.common.basic.NumberUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UTabComp {

    /**
     * The main tab completion method that you would return on with {@link net.md_5.bungee.api.plugin.TabExecutor#onTabComplete(CommandSender, String[])}
     * <br> <br> <br>
     * {@link TabCompleteType#SEARCH} would work like "/customcommand Cyb" and would tab complete "<b>Cyb</b>eredCake" but not "BestCyber" and "BlueStar"
     * <br> <br>
     * {@link TabCompleteType#CONTAINS} would work like "/customcommand Cyb" and would tab complete "<b>Cyb</b>eredCake" and "Best<b>Cyb</b>er" but not "BlueStar"
     * <br> <br>
     * {@link TabCompleteType#NONE} would work like "/customcommand Cyb" and would tab complete "<b>Cyb</b>eredCake", "Best<b>Cyb</b>er", and "BlueStar"
     * @param type the tab completion type, a {@link TabCompleteType}, describes how the {@code currentArg} would tab complete the {@code completions}
     * @param currentArg the current argument, what the player is currently typing
     * @param completions what to tab complete
     * @return the purified list for the tab completions, return this to the {@code onTabComplete()} method
     * @since 1
     * @see net.md_5.bungee.api.plugin.TabExecutor#onTabComplete(CommandSender, String[])
     * @see TabCompleteType
     * @implNote It is recommended that instead of returning {@code null} for {@code onTabComplete()}, you return {@link UTabComp#emptyList}
     */
    public static List<String> tabCompletions(TabCompleteType type, String currentArg, List<String> completions) {
        if (currentArg.length() <= 0) { return completions; }
        currentArg = currentArg.toLowerCase(Locale.ROOT);
        List<String> returnedCompletions = new ArrayList<>();
        switch(type) {
            case CONTAINS -> {
                for (String str : completions) {
                    if (str.toLowerCase(Locale.ROOT).contains(currentArg)) {
                        returnedCompletions.add(str);
                    }
                }
            }
            case SEARCH -> {
                for (String str : completions) {
                    if (str.toLowerCase(Locale.ROOT).startsWith(currentArg)) {
                        returnedCompletions.add(str);
                    }
                }
            }
            case NONE -> returnedCompletions.addAll(completions);
        }
        return returnedCompletions;
    }

    /**
     * The tab complete method that you would return on with {@link net.md_5.bungee.api.plugin.TabExecutor#onTabComplete(CommandSender, String[])}.
     * This method assumes the tab complete type is {@link TabCompleteType#CONTAINS}
     * <br> <br> <br>
     * You can use {@link UTabComp#tabCompletions(TabCompleteType, String, List)} for a different {@link TabCompleteType}
     * <br> <br> <br>
     * {@link TabCompleteType#SEARCH} would work like "/customcommand Cyb" and would tab complete "<b>Cyb</b>eredCake" but not "BestCyber"
     * <br> <br>
     * {@link TabCompleteType#CONTAINS} would work like "/customcommand Cyb" and would tab complete "<b>Cyb</b>eredCake" and "Best<b>Cyb</b>er"
     * <br> <br>
     * {@link TabCompleteType#NONE} would work like "/customcommand Cyb" and would tab complete "<b>Cyb</b>eredCake", "Best<b>Cyb</b>er", and "BlueStar"
     * @param currentArg the current argument, what the player is currently typing
     * @param completions what to tab complete
     * @return the purified list for the tab completions, return this to the {@code onTabComplete()} method
     * @since 1
     * @see net.md_5.bungee.api.plugin.TabExecutor#onTabComplete(CommandSender, String[])
     * @see TabCompleteType
     * @implNote It is recommended that instead of returning {@code null} for {@code onTabComplete()}, you return {@link UTabComp#emptyList}
     */
    public static List<String> tabCompletionsContains(String currentArg, List<String> completions) { return tabCompletions(TabCompleteType.CONTAINS, currentArg, completions); }

    /**
     * The tab complete method that you would return on with {@link net.md_5.bungee.api.plugin.TabExecutor#onTabComplete(CommandSender, String[])}.
     * This method assumes the tab complete type is {@link TabCompleteType#SEARCH}
     * <br> <br> <br>
     * You can use {@link UTabComp#tabCompletions(TabCompleteType, String, List)} for a different {@link TabCompleteType}
     * <br> <br> <br>
     * {@link TabCompleteType#SEARCH} would work like "/customcommand Cyb" and would tab complete "<b>Cyb</b>eredCake" but not "BestCyber"
     * <br> <br>
     * {@link TabCompleteType#CONTAINS} would work like "/customcommand Cyb" and would tab complete "<b>Cyb</b>eredCake" and "Best<b>Cyb</b>er"
     * <br> <br>
     * {@link TabCompleteType#NONE} would work like "/customcommand Cyb" and would tab complete "<b>Cyb</b>eredCake", "Best<b>Cyb</b>er", and "BlueStar"
     * @param currentArg the current argument, what the player is currently typing
     * @param completions what to tab complete
     * @return the purified list for the tab completions, return this to the {@code onTabComplete()} method
     * @since 1
     * @see net.md_5.bungee.api.plugin.TabExecutor#onTabComplete(CommandSender, String[])
     * @see TabCompleteType
     * @implNote It is recommended that instead of returning {@code null} for {@code onTabComplete()}, you return {@link UTabComp#emptyList}
     */
    public static List<String> tabCompletionsSearch(String currentArg, List<String> completions) { return tabCompletions(TabCompleteType.SEARCH, currentArg, completions); }

    /**
     * This tab completion method returns a bunch of integers. The first param <b>must</b> be an integer, and if it's not, it'll just return all the integers
     * @param integerArgument the {@link String} argument in {@link net.md_5.bungee.api.plugin.TabExecutor#onTabComplete(CommandSender, String[])}
     * @param lowest the lowest number of the integer list
     * @param highest the biggest number of the integer list
     * @return the list of integers from the {@code lowest} param to {@code highest} param
     * @since 1
     * @see net.md_5.bungee.api.plugin.TabExecutor#onTabComplete(CommandSender, String[])
     * @see TabCompleteType
     * @see NumberUtils
     * @implNote It is recommended that instead of returning {@code null} for {@code onTabComplete()}, you return {@link UTabComp#emptyList}
     */
    public static List<String> getIntegers(String integerArgument, int lowest, int highest) {
        List<String> integers = new ArrayList<>();
        if(!NumberUtils.isInteger(integerArgument)) { return integers; }
        if(!NumberUtils.isBetweenEquals(Integer.parseInt(integerArgument), lowest, highest)) { return integers; }

        for(int i=1; i<10; i++) {
            if(!NumberUtils.isBetweenEquals(Integer.parseInt(integerArgument + i), lowest, highest)) continue;

            integers.add(integerArgument + i + "");
        }
        return integers;
    }

    /**
     * Represents an empty list with {@link BaseComponent}s
     */
    public static ArrayList<BaseComponent> emptyComponentList = new ArrayList<>();
    /**
     * Represents an empty list with {@link String}s, used instead of {@code null} for returning {@link net.md_5.bungee.api.plugin.TabExecutor#onTabComplete(CommandSender, String[])}
     */
    public static ArrayList<String> emptyList = new ArrayList<>();

}
