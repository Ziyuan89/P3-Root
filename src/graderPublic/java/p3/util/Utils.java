package p3.util;

import java.lang.reflect.Field;

public class Utils {

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Field field, Object instance) {
        try {
            return (T) field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
