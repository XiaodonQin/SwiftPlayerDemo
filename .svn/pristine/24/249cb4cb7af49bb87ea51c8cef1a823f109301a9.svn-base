package sg.com.conversant.swiftplayer.feedback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/8/24.
 */

public class CollectionUtils {
    static final long[] EMPTY_LONG_ARRAY = new long[0];

    private CollectionUtils() {
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    public static <T> List<T> ensureEmpty(List<T> list) {
        return (List)(isEmpty((Collection)list)?new ArrayList():list);
    }

    public static <T> List<T> unmodifiableList(List<T> list) {
        return Collections.unmodifiableList(ensureEmpty(list));
    }

    @SafeVarargs
    public static <T> List<T> combineLists(List... lists) {
        if(lists != null && lists.length != 0) {
            ArrayList combinedLists = new ArrayList();
            CopyOnWriteArrayList toBeCombinedList = new CopyOnWriteArrayList(lists);
            Iterator i$ = toBeCombinedList.iterator();

            while(true) {
                List currentList;
                do {
                    if(!i$.hasNext()) {
                        return combinedLists;
                    }

                    currentList = (List)i$.next();
                } while(!isNotEmpty((Collection)currentList));

                CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList(currentList);
                Iterator i$1 = copyOnWriteArrayList.iterator();

                while(i$1.hasNext()) {
                    Object item = i$1.next();
                    combinedLists.add(item);
                }
            }
        } else {
            return new ArrayList();
        }
    }

    public static <T> List<T> copyOf(List<T> list) {
        if(list == null) {
            return new ArrayList();
        } else {
            CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList(list);
            ArrayList copiedList = new ArrayList(copyOnWriteArrayList.size());
            Iterator i$ = copyOnWriteArrayList.iterator();

            while(i$.hasNext()) {
                Object item = i$.next();
                copiedList.add(item);
            }

            return copiedList;
        }
    }

    public static <K, V> Map<K, V> copyOf(Map<K, V> map) {
        if(map == null) {
            return new HashMap();
        } else {
            Map sourceMap = Collections.synchronizedMap(map);
            HashMap destinationMap = new HashMap();
            destinationMap.putAll(sourceMap);
            return destinationMap;
        }
    }

    public static long[] toPrimitiveLong(Long[] array) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_LONG_ARRAY;
        } else {
            long[] result = new long[array.length];

            for(int i = 0; i < array.length; ++i) {
                result[i] = array[i].longValue();
            }

            return result;
        }
    }

    public static long[] toPrimitiveLong(Long[] array, long valueForNull) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_LONG_ARRAY;
        } else {
            long[] result = new long[array.length];

            for(int i = 0; i < array.length; ++i) {
                Long b = array[i];
                result[i] = b == null?valueForNull:b.longValue();
            }

            return result;
        }
    }

    public static <T> boolean equalsByContents(Collection<T> collection1, Collection<T> collection2) {
        return isEmpty(collection1)?isEmpty(collection2):(isEmpty(collection2)?false:collection1.containsAll(collection2) && collection2.containsAll(collection1));
    }
}
