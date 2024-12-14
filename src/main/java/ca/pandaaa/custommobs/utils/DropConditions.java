package ca.pandaaa.custommobs.utils;

import java.util.HashMap;
import java.util.Map;

public enum DropConditions {
    NEARBY,

    KILLER,
    DROP,
    MOST_DAMAGE; // Might be hard to do... Might remove

    public static DropConditions getNextCondition(DropConditions currentCondition) {
        Map<DropConditions, DropConditions> next = new HashMap<>();
        next.put(null, NEARBY);
        next.put(NEARBY, KILLER);
        next.put(KILLER, DROP);
        next.put(DROP, MOST_DAMAGE);
        next.put(MOST_DAMAGE, NEARBY);

        return next.get(currentCondition);
    }
}

