import java.util.List;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

/// Stream Gatherers(JEP 473,Java 23 第二次預覽):自訂 Stream 中間操作。
///
/// 執行方式:
/// ```
/// java --enable-preview GatherersDemo.java
/// ```
public class GatherersDemo {

    public static void main(String[] args) {
        // 滑動視窗
        List<List<Integer>> sliding = Stream.of(1, 2, 3, 4, 5)
                .gather(Gatherers.windowSliding(3))
                .toList();
        System.out.println("windowSliding(3):" + sliding);

        // 固定視窗
        List<List<Integer>> fixed = Stream.of(1, 2, 3, 4, 5, 6, 7)
                .gather(Gatherers.windowFixed(3))
                .toList();
        System.out.println("windowFixed(3):" + fixed);

        // 累計摺疊(scan):輸出每一步的累加結果
        List<Integer> runningSum = Stream.of(1, 2, 3, 4, 5)
                .gather(Gatherers.scan(() -> 0, Integer::sum))
                .toList();
        System.out.println("scan 累加:" + runningSum);
    }
}
