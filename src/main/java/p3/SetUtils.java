package p3;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SetUtils {

    public static <N> Set<N> immutableCopyOf(Set<N> set) {
        final Set<N> result = new HashSet<>(set.size(), 0.9f);
        result.addAll(set);
        return Collections.unmodifiableSet(result);
    }

    public static <N> Set<N> mutableCopyOf(Set<N> set) {
        final Set<N> result = new HashSet<>((int) (set.size() / 0.75f), 0.75f);
        result.addAll(set);
        return result;
    }
}
