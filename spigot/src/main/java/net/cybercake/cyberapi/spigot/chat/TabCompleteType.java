package net.cybercake.cyberapi.spigot.chat;

/**
 * Represents the type of tab completion that will be used in some of {@link UTabComp}'s methods
 */
public enum TabCompleteType {
    /**
     * Represents the contains method for tab completing
     */
    CONTAINS,

    /**
     * Represents the search method for tab completing
     */
    SEARCH,

    /**
     * Represents using the default method for tab completing
     */
    NONE;
}