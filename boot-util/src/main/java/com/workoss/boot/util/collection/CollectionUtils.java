package com.workoss.boot.util.collection;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;



/**
 * @Description: 集合工具类
 * @author: luanfeng
 * @Date: 2017/8/11 8:10
 * @version: 1.0.0
 */
public class CollectionUtils {

    public static final List EMPTY_ARRAY_LIST = new ArrayList();
    /**
     * 判断是否为空.
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null) || collection.isEmpty();
    }

    public static boolean isEmpty(Map map){
        return (map==null) || (map.isEmpty());
    }

    /**
     * 判断是否不为空.
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return (collection != null) && !(collection.isEmpty());
    }

    public static boolean isNotEmpty(Map map) {
        return (map != null) && !(map.isEmpty());
    }

    /**
     * 取得Collection的第一个元素，如果collection为空返回null.
     */
    public static <T> T getFirst(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        if (collection instanceof List) {
            return ((List<T>) collection).get(0);
        }
        return collection.iterator().next();
    }

    /**
     * 获取Collection的最后一个元素，如果collection为空返回null.
     */




    public static Set<String> toSetString(Collection collection){
        if (isEmpty(collection)){
            return null;
        }
        Set<String> set = new LinkedHashSet<>();
        for (Object element : collection) {
            set.add(String.valueOf(element));
        }
        return set;
    }

    public static Set<Integer> toSetInteger(Collection collection){
        if (isEmpty(collection)){
            return null;
        }
        Set<Integer> set = new LinkedHashSet<>();
        for (Object element : collection) {
            set.add(Integer.parseInt(String.valueOf(element)));
        }
        return set;
    }

    /**
     * 返回a+b的新List.
     */
    public static <T> List<T> union(final Collection<T> a, final Collection<T> b) {
        List<T> result = new ArrayList<T>(a);
        result.addAll(b);
        return result;
    }

    /**
     * 返回a-b的新List.
     */
    public static <T> List<T> subtract(final Collection<T> a, final Collection<T> b) {
        List<T> list = new ArrayList<T>(a);
        if (isEmpty(b)){
            return list;
        }
        for (T element : b) {
            list.remove(element);
        }

        return list;
    }

    /**
     * 返回a-b的新List.
     */
    public static <T> Set<T> subtractToSet(final Collection<T> a, final Collection<T> b) {
        Set<T> set = new LinkedHashSet<>(a);
        for (T element : b) {
            set.remove(element);
        }

        return set;
    }

    /**
     * 返回a与b的交集的新List.
     */
    public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
        List<T> list = new ArrayList<T>();

        for (T element : a) {
            if (b.contains(element)) {
                list.add(element);
            }
        }
        return list;
    }

    ///////////// 求最大最小值，及Top N, Low N//////////
    /**
     * 返回无序集合中的最小值，使用元素默认排序
     */
    public static <T extends Object & Comparable<? super T>> T min(Collection<? extends T> coll) {
        return Collections.min(coll);
    }

    /**
     * 返回无序集合中的最小值
     */
    public static <T> T min(Collection<? extends T> coll, Comparator<? super T> comp) {
        return Collections.min(coll, comp);
    }

    /**
     * 返回无序集合中的最大值，使用元素默认排序
     */
    public static <T extends Object & Comparable<? super T>> T max(Collection<? extends T> coll) {
        return Collections.max(coll);
    }

    /**
     * 返回无序集合中的最大值
     */
    public static <T> T max(Collection<? extends T> coll, Comparator<? super T> comp) {
        return Collections.max(coll, comp);
    }

    /**
     * 返回无序集合中的最小值和最大值，使用元素默认排序
     */
    public static <T extends Object & Comparable<? super T>> Pair<T, T> minAndMax(Collection<? extends T> coll) {
        Iterator<? extends T> i = coll.iterator();
        T minCandidate = i.next();
        T maxCandidate = minCandidate;

        while (i.hasNext()) {
            T next = i.next();
            if (next.compareTo(minCandidate) < 0) {
                minCandidate = next;
            } else if (next.compareTo(maxCandidate) > 0) {
                maxCandidate = next;
            }
        }
        return Pair.of(minCandidate, maxCandidate);
    }

    /**
     * 返回无序集合中的最小值和最大值
     */
    public static <T> Pair<T, T> minAndMax(Collection<? extends T> coll, Comparator<? super T> comp) {

        Iterator<? extends T> i = coll.iterator();
        T minCandidate = i.next();
        T maxCandidate = minCandidate;

        while (i.hasNext()) {
            T next = i.next();
            if (comp.compare(next, minCandidate) < 0) {
                minCandidate = next;
            } else if (comp.compare(next, maxCandidate) > 0) {
                maxCandidate = next;
            }
        }

        return Pair.of(minCandidate, maxCandidate);
    }









    public static String join(Collection collection, String separator, String prefix, String suffix){
        StringJoiner stringJoiner=new StringJoiner(separator,prefix,suffix);
        if (CollectionUtils.isEmpty(collection)){
            return stringJoiner.toString();
        }
        for (Object o : collection) {
            stringJoiner.add(o.toString());
        }
        return stringJoiner.toString();
    }
}