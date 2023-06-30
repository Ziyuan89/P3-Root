package p3.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Field field, Object instance) {
        try {
            return (T) field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T setFieldValue(Field field, Object instance, T value) {
        try {
            T oldValue = getFieldValue(field, instance);
            field.set(instance, value);
            return oldValue;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <K, V> Map<K, V> deserializeMap(List<SerializedEntry<K, V>> serializedEntries) {
        Map<K, V> map = new HashMap<>();
        serializedEntries.forEach(serializedEntry -> map.put(serializedEntry.key(), serializedEntry.value()));
        return map;
    }
}
