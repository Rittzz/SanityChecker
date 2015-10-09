package rittzz.sanitychecker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Checks classes for sanity.  A class is said to be sane if all fields that have been marked with {@link MustExist} under classes marked
 * withed {@link SanityCheck} are not null.  This is useful for testing model objects back from unreliable sources to save yourself some
 * null checks.
 */
public class SanityChecker {

    private static SanityChecker DEFAULT = new SanityChecker();

    private final Map<Class<?>, List<Field>> cachedFields = new HashMap<>();
    private final Map<Class<?>, Boolean> cachedClassSanityCheck = new HashMap<>();
    private final Map<Field, Boolean> cachedFieldMustExist = new HashMap<>();

    public static SanityChecker get() {
        return DEFAULT;
    }

    public SanityChecker() {
        // Empty
    }

    private boolean isClassAnnotated(Class<?> clazz) {
        if (!cachedClassSanityCheck.containsKey(clazz)) {
            cachedClassSanityCheck.put(clazz, clazz.isAnnotationPresent(SanityCheck.class));
        }
        return cachedClassSanityCheck.get(clazz);
    }

    private boolean isFieldAnnotated(Field field) {
        if (!cachedFieldMustExist.containsKey(field)) {
            cachedFieldMustExist.put(field, field.isAnnotationPresent(MustExist.class));
        }
        return cachedFieldMustExist.get(field);
    }

    private List<Field> getFields(Class<?> root) {
        if (cachedFields.containsKey(root)) {
            return cachedFields.get(root);
        }

        List<Field> fields = new ArrayList<>();

        Class<?> clazz = root;
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);

                if (!field.getDeclaringClass().isPrimitive()) {
                    fields.add(field);
                }
            }
            clazz = clazz.getSuperclass();
        }

        cachedFields.put(root, fields);

        return fields;
    }

    private String getFieldName(final Field field) {
        return field.getName();
    }

    private String trailToString(final List<String> trail) {
        final StringBuilder str = new StringBuilder();
        for (String s : trail) {
            str.append(s);
        }
        return str.toString();
    }

    /**
     * Returns true if the object is sane.  See the description of this class for what sane means.
     */
    public boolean isSane(final Object obj) {
        try {
            check(obj);
            return true;
        }
        catch (SanityException ex) {
            return false;
        }
    }

    /**
     * Checks the object for sanity.  If found sane, this method will do nothing.  If insanity is found, then this method will throw
     * an exception detailing the insanity (i.e. where the null check failed).
     * @throws SanityException
     */
    public void check(final Object obj) throws SanityException {
        final LinkedList<String> trail = new LinkedList<>();
        trail.add(obj.getClass().getSimpleName());

        check(obj, trail);
    }

    private void check(final Object obj, LinkedList<String> trailSoFar) throws SanityException {
        if (obj == null) {
            throw new SanityException("You passed a null object to check!");
        }

        Class<?> clazz = obj.getClass();
        if (!isClassAnnotated(clazz)) {
            return;
        }

        for (Field field : getFields(clazz)) {
            try {
                trailSoFar.add(".");
                trailSoFar.add(getFieldName(field));

                final Object member = field.get(obj);

                // The real null check
                if (isFieldAnnotated(field) && member == null) {
                    throw new SanityException("Encounter null value at: " + trailToString(trailSoFar));
                }

                if (member != null) {
                    // Be sure to check this guy for sanity checking
                    check(member, trailSoFar);

                    // Go through any collection
                    if (member instanceof Collection<?>) {
                        final Collection<?> collection = (Collection<?>) member;
                        trailSoFar.add("[");

                        int index = 0;
                        for (Object item : collection) {
                            trailSoFar.add(Integer.toString(index));
                            trailSoFar.add("]");

                            if (item != null) {
                                check(item, trailSoFar);
                            }

                            trailSoFar.removeLast();
                            trailSoFar.removeLast();
                        }

                        trailSoFar.removeLast();
                    }

                    // And any arrays
                    if (member instanceof Object[]) {
                        final Object[] array = (Object[]) member;
                        trailSoFar.add("[");

                        int index = 0;
                        for (Object item : array) {
                            trailSoFar.add(Integer.toString(index));
                            trailSoFar.add("]");

                            if (item != null) {
                                check(item, trailSoFar);
                            }

                            trailSoFar.removeLast();
                            trailSoFar.removeLast();
                        }

                        trailSoFar.removeLast();
                    }
                }

                trailSoFar.removeLast();
                trailSoFar.removeLast();
            }
            catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }

        // Call through to our manual sanity checker
        if (obj instanceof SanityCheckable) {
            final SanityCheckable checkable = (SanityCheckable) obj;
            checkable.sanityCheck();
        }
    }
}
