package net.cybercake.cyberapi.common.basic;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * Represents a pair of objects
 * @param <A> object one
 * @param <B> object two
 * @since 89
 * @author CyberedCake
 */
public class Pair<A, B> implements Serializable {

    //<editor-fold desc="static methods">
    /**
     * Creates a pair of two objects through this static method
     * @param a object one
     * @param b object two
     * @return the pair of object one and object two
     * @param <A> the type of object one
     * @param <B> the type of object two
     * @since 144
     */
    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<>(a, b);
    }
    //</editor-fold>

    //<editor-fold desc="constructor(s) and variables">
    private A item1;
    private B item2;

    /**
     * Creates a pair object of two objects combined
     * @param item1 the first item
     * @param item2 the second item
     * @since 89
     */
    public Pair(A item1, B item2) {
        this.item1 = item1;
        this.item2 = item2;
    }
    //</editor-fold>

    //<editor-fold desc="set items">
    /**
     * Sets the pair to certain values
     * @param item1 the first item
     * @param item2 the second item
     * @since 89
     * @see Pair#setFirstItem(Object)
     * @see Pair#setSecondItem(Object)
     */
    public void setPair(A item1, B item2) { setFirstItem(item1); setSecondItem(item2); }

    /**
     * Sets only the first item in the pair to a certain value
     * @param item1 the item to set
     * @since 89
     * @see Pair#setPair(Object, Object)
     * @see Pair#setSecondItem(Object)
     */
    public void setFirstItem(A item1) { this.item1 = item1; }

    /**
     * Sets only the second item in the pair to a certain value
     * @param item2 the item to set
     * @since 89
     * @see Pair#setPair(Object, Object)
     * @see Pair#setFirstItem(Object)
     */
    public void setSecondItem(B item2) { this.item2 = item2; }
    //</editor-fold>

    //<editor-fold desc="get items">
    /**
     * Retrieve only the value of the first item
     * @return the first item
     * @since 89
     * @see Pair#getSecondItem()
     */
    public A getFirstItem() { return this.item1; }

    /**
     * Retrieve only the value of the second item
     * @return the second item
     * @since 89
     * @see Pair#getFirstItem()
     */
    public B getSecondItem() { return this.item2; }
    //</editor-fold>

    //<editor-fold desc="check items are set">
    /**
     * Check if the first item is set by checking if it is not null
     * @return whether the item is set (true if yes, false if no)
     * @since 89
     * @see Pair#isSecondItemSet()
     */
    public boolean isFirstItemSet() { return this.item1 != null; }

    /**
     * Check if the second item is set by checking if it is not null
     * @return whether the item is set (true if yes, false if no)
     * @since 89
     * @see Pair#isFirstItemSet()
     */
    public boolean isSecondItemSet() { return this.item2 != null; }
    //</editor-fold>

    //<editor-fold desc="misc public methods">
    /**
     * Check if the two items are of the same type, this is done by comparing the classes of the two.
     * @return whether the items are of the same type (same class)
     * @since 89
     */
    public boolean areSameType() {
        Preconditions.checkArgument(this.item1 != null, "item1 cannot be null");
        Preconditions.checkArgument(this.item2 != null, "item2 cannot be null");
        return this.item1.getClass() == this.item2.getClass();
    }

    /**
     * The stringified version of the objects
     * @return the string version of the objects
     * @since 89
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("item1=" + (item1 == null ? "null" : item1))
                .add("item2=" + (item2 == null ? "null" : item2))
                .toString();
    }
    //</editor-fold>

}