import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class MinHeapTest {

    @Test
    public void test1() {
        MinHeap<Integer> mh1 = new MinHeap<>();
        assertThat(mh1.size()).isEqualTo(0);

        mh1.insert(10);
        assertThat(mh1.size()).isEqualTo(1);

        mh1.insert(1);
        System.out.println(mh1.toString());

        mh1.insert(5);
        mh1.insert(50);
        mh1.insert(15);
        System.out.println(mh1.toString());
        assertThat(mh1.findMin()).isEqualTo(1);
        assertThat(mh1.size()).isEqualTo(5);

        mh1.removeMin();
        System.out.println(mh1.toString());

        mh1.removeMin();
        System.out.println(mh1.toString());
    }

    @Test
    public void test2() {
        MinHeapPQ<String> mh2 = new MinHeapPQ<>();
        assertThat(mh2.size()).isEqualTo(0);

        mh2.insert("a", 0);
        assertThat(mh2.size()).isEqualTo(1);
        assertThat(mh2.contains("a")).isTrue();

        mh2.insert("b", 1);
        mh2.insert("c", 2);
        mh2.insert("d", 3);
        mh2.insert("e", 4);
        assertThat(mh2.size()).isEqualTo(5);
        assertThat(mh2.contains("b")).isTrue();
        assertThat(mh2.contains("f")).isFalse();
        System.out.println(mh2.toString());

        assertThat(mh2.peek()).isEqualTo("a");

        assertThat(mh2.poll()).isEqualTo("a");
        System.out.println(mh2.toString());

        mh2.changePriority("b", 5);
        System.out.println(mh2.toString());

        mh2.changePriority("c", 0);
        System.out.println(mh2.toString());

        mh2.changePriority("c", 4);
        System.out.println(mh2.toString());
    }
}
