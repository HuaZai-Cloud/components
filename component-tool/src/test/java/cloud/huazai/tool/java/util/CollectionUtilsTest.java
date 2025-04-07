package cloud.huazai.tool.java.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * CollectionUtilsTest
 *
 * @author devon
 * @since 2025/1/15
 */

class CollectionUtilsTest {

    @Test
    void emptyCollection() {

        Collection<String> collection = getEmpty();
        collection.add("aa");
        System.out.println("collection = " + collection);


        List<String> list = getList();
        list.add("bb");
        System.out.println("list = " + list);

      Set<String> set = getSet();
        set.add("aaa");
        set.add("bbb");
        set.add("aaa");

        System.out.println("set = " + set);
    }

    private Set<String> getSet() {

        return CollectionUtils.emptySet();
    }

    private List<String> getList() {

        return CollectionUtils.emptyList();
    }

    @Test
    void requireNonEmpty() {
    }

    private Collection<String> getEmpty() {
        return CollectionUtils.emptyCollection();

    }


    @Test
    void intersection() {

        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        List<Integer> list2 = new ArrayList<>();
        list2.add(3);
        list2.add(4);
        list2.add(5);
        list2.add(6);

        Collection<Integer> intersection = CollectionUtils.intersection(list1, list2);

        intersection.forEach(System.out::println); // 3
    }

    @Test
    void union() {

        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        List<Integer> list2 = new ArrayList<>();
        list2.add(3);
        list2.add(4);
        list2.add(5);
        list2.add(6);

        Collection<Integer> union = CollectionUtils.union(list1, list2);
        union.forEach(System.out::println); // 1 2 3 4 5 6
    }

    @Test
    void difference() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        List<Integer> list2 = new ArrayList<>();
        list2.add(3);
        list2.add(4);
        list2.add(5);
        list2.add(6);

        Collection<Integer> difference = CollectionUtils.subtract(list1, list2);
        difference.forEach(System.out::println); // 1 2
    }


    @Test
    void symmetricDifference() {

        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        List<Integer> list2 = new ArrayList<>();
        list2.add(3);
        list2.add(4);
        list2.add(5);
        list2.add(6);

        Collection<Integer> difference = CollectionUtils.symmetricDifference(list1, list2);
        difference.forEach(System.out::println); // 1 2 4 5 6
    }


}