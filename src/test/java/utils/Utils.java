package utils;

import java.util.*;

public class Utils {
    public static boolean compareCircuitsLists(List<List<String>> expected, List<List<String>> circuits) {
        Map<List<String>, Boolean> circuitsFounded = new HashMap<>();
        expected.forEach(l -> circuitsFounded.put(l, false));
        for (List<String> l : circuits) {
            for (List<String> expectedList : expected) {
                int j = 0;
                boolean found = false;
                List<String> expectedListClone = new ArrayList<>(expectedList);
                while (!found && j < expectedList.size()) {
                    if (expectedListClone.equals(l)) {
                        found = true;
                        circuitsFounded.put(expectedList, true);
                    } else {
                        Collections.rotate(expectedListClone, 1);
                    }
                    j++;
                }
            }
        }
        return circuitsFounded.values().stream().reduce(true, (b1, b2) -> b1 && b2);
    }
}
