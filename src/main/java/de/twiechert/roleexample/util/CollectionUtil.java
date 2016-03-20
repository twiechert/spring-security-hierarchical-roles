package de.twiechert.roleexample.util;


import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */
public class CollectionUtil {

    public static Collection<String> anyCollectionToStringCollection(Collection<?> anySet) {

        Collection<String> collection = anySet.stream().map(Object::toString).collect(Collectors.toCollection(HashSet::new));

        return collection;
    }

    public static <ObjectToType, ObjectFromType> Collection<ObjectToType> anyCollectionToGenericCollection(Collection<ObjectFromType> anySet, Adapter<ObjectFromType, ObjectToType> objectTypeIFactory) {

        Collection<ObjectToType> collection = anySet.stream().map(objectTypeIFactory::adapt).collect(Collectors.toCollection(HashSet::new));

        return collection;
    }
}
