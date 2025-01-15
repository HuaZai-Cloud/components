package cloud.huazai.tool.java.util;

import org.junit.jupiter.api.Test;

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
}