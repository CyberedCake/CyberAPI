package net.cybercake.cyberapi.common.basic;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * General utilities regarding the usage of {@link List Lists}
 */
public class ListUtils {

    /**
     * Finds the difference between two lists. <b>Note:</b> If you get returned an empty list, try swapping the items around!
     * @param originalList the original list with its original items
     * @param differentList the secondary (or different) list with slightly differing items
     * @return a list of all the items that are different in each list. This would mean that if one list has {@code ['apple', 'banana']} and another has {@code ['apple', 'banana', 'orange']}, this method would return {@code ['orange']}
     * @since 102
     * @apiNote <a href="https://www.baeldung.com/java-lists-difference">source</a>
     */
    public static <T> List<T> findDifference(List<T> originalList, List<T> differentList) {
        List<T> differences = new ArrayList<>(originalList);
        differences.removeAll(differentList);
        return differences;
    }

    /**
     * Removes duplicates from an {@link List}
     * @param list the list to remove duplicates from
     * @return the list without duplicates
     * @since 102
     */
    public static List<String> removeDuplicates(ArrayList<String> list) {
        List<String> alreadyOver = new ArrayList<>();
        for(String str : list) {
            if(!alreadyOver.contains(str)) {
                alreadyOver.add(str);
            }
        }
        return alreadyOver;
    }

    /**
     * Add an item to a list in one line of code
     * @param originalList the original list that needs to have an item added to it
     * @param addition the item that needs to be added to the {@code originalList}
     * @return the new list with the {@code addition} appended onto the end
     * @param <T> the type of list
     * @since 127
     * @see ListUtils#addToList(List, Object, List) add item to a list with a default list
     */
    public static <T> List<T> addToList(List<T> originalList, T addition) {
        return addToList(originalList, addition, null);
    }

    /**
     * Add an item to a list (if it exists) in one line of code
     * @param originalList the original list that needs to have an item added to it
     * @param addition the item that needs to be added to the {@code originalList} or the {@code defaultList} if the {@code originalList} is not set (or null)
     * @param def the default list which the item will be added to if the originalList is null -- if this item is null, the added item will create it's own list
     * @return the new list with the {@code addition} appended onto the end
     * @param <T> the type of list
     * @since 127
     * @see ListUtils#addToList(List, Object) same method with the default list automatically set to null
     */
    public static <T> List<T> addToList(List<T> originalList, T addition, @Nullable List<T> def) {
        if(originalList == null || originalList.size() < 1) {
            Preconditions.checkNotNull(def, "The default list is set to null, cannot add to a non-existent list!");
            originalList = def;
        }
        originalList.add(addition);
        return originalList;
    }

}
