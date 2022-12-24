package net.cybercake.cyberapi.common.basic;

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
    public static ArrayList<String> removeDuplicates(ArrayList<String> list) {
        ArrayList<String> alreadyOver = new ArrayList<>();
        for(String str : list) {
            if(!alreadyOver.contains(str)) {
                alreadyOver.add(str);
            }
        }
        return alreadyOver;
    }

}
