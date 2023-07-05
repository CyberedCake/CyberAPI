package net.cybercake.cyberapi.spigot.inventory;

import net.cybercake.cyberapi.spigot.inventory.exceptions.HaltConsumerListException;
import org.bukkit.event.Event;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a GUI consumer. Specifically used for any {@link Event Bukkit events} used in the {@link CustomGUI}.
 * @param <E> the {@link Event Bukkit event} that the GUI consumer will use as a parameter
 * @since 143
 */
public class GUIConsumer<E extends Event> {

    /**
     * Gets a {@link GUIConsumer} from a normal Java {@link Consumer}
     * @param oldConsumer the java consumer to convert to a GUI one
     * @return the new GUI consumer
     * @param <T> the event type that extends {@link Event org.bukkit.Event}
     * @since 143
     */
    public static <T extends Event> GUIConsumer<T> from(Consumer<T> oldConsumer) {
        return new GUIConsumer<>(oldConsumer);
    }

    /**
     * Gets a List of {@link GUIConsumer GUIConsumers} from a normal list of Java {@link Consumer Consumers}
     * @param oldConsumers the list of java consumers to convert to a GUI one
     * @return the new list of GUI consumers
     * @param <T> the event type that events {@link Event org.bukkit.Event}
     * @since 143
     */
    public static <T extends Event> List<GUIConsumer<T>> from(List<Consumer<T>> oldConsumers) {
        List<GUIConsumer<T>> newConsumers = new ArrayList<>();
        for(Consumer<T> oldConsumer : oldConsumers)
            newConsumers.add(new GUIConsumer<>(oldConsumer));
        return newConsumers;
    }

    private Consumer<E> consumer;
    private boolean active;

    private final @Nullable Integer objectHashCode;
    private final @Nullable Object extraInformation;

    /**
     * The constructor for GUI consumer will all available options.
     * @param consumer the Java {@link Consumer} to store
     * @param active whether the consumer will run when the GUI detects it should
     * @param objectHashCode (set to null if none) the optional object hash code from the {@link CustomGUI} or {@link FollowUpGUIAction FollowUpGUIAction (internal CyberAPI stuff)}
     * @param extraInformation (set to null if none) an optional extra object to add to this object
     * @since 143
     * @see GUIConsumer#GUIConsumer(Consumer, boolean)
     * @see GUIConsumer#GUIConsumer(Consumer)
     */
    public GUIConsumer(Consumer<E> consumer, boolean active, @Nullable Integer objectHashCode, @Nullable Object extraInformation) {
        this.consumer = consumer;
        this.active = active;
        this.objectHashCode = objectHashCode;
        this.extraInformation = extraInformation;
    }

    /**
     * The constructor for GUI consumer will two available options.
     * @param consumer the Java {@link Consumer} to store
     * @param active whether the consumer will run when the GUI detects it should
     * @since 143
     * @see GUIConsumer#GUIConsumer(Consumer, boolean, Integer, Object)
     * @see GUIConsumer#GUIConsumer(Consumer)
     */
    public GUIConsumer(Consumer<E> consumer, boolean active) {
        this(consumer, active, null, null);
    }

    /**
     * The constructor for GUI consumer will only the raw Java consumer.
     * @param consumer the Java {@link Consumer} to store
     * @since 143
     * @see GUIConsumer#GUIConsumer(Consumer, boolean, Integer, Object)
     * @see GUIConsumer#GUIConsumer(Consumer, boolean)
     */
    public GUIConsumer(Consumer<E> consumer) {
        this(consumer, true);
    }

    /**
     * @return the Java {@link Consumer} that is stored by this object
     * @since 143
     */
    public Consumer<E> getConsumer() { return (e) -> {
        try {
            this.consumer.accept(e);
        } catch (HaltConsumerListException ignored) { }
    }; }

    /**
     * @return whether the object is active and will be run by {@link CustomGUI}
     * @since 143
     */
    public boolean isActive() { return this.active; }

    /**
     * @return the (optional) object hash code attached to this object
     * @since 143
     */
    public @Nullable Integer getObjectHashCode() { return this.objectHashCode; }

    /**
     * @return the (optional) extra information appended to this object
     * @since 143
     */
    public @Nullable Object getExtraInformation() { return this.extraInformation; }

    @ApiStatus.Internal void setActive(boolean active) { this.active = active; }
    @ApiStatus.Internal void setConsumer(Consumer<E> consumer) {
        try {
            this.consumer = consumer.andThen(this.consumer);
        } catch (HaltConsumerListException ignored) { }
    }

}
