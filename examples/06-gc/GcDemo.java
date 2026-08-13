import java.util.ArrayList;
import java.util.List;

/// GC 觀察示範:持續分配物件並保留一部分(模擬快取),
/// 搭配不同 GC 與 GC log 參數執行,觀察各 GC 的行為差異。
///
/// 執行方式(比較不同 GC):
/// ```
/// java -Xmx512m -Xlog:gc GcDemo.java                       # 預設 G1
/// java -XX:+UseZGC       -Xmx512m -Xlog:gc GcDemo.java     # 分代 ZGC(Java 23 預設分代)
/// java -XX:+UseParallelGC -Xmx512m -Xlog:gc GcDemo.java
/// java -XX:+UseSerialGC   -Xmx512m -Xlog:gc GcDemo.java
/// ```
public class GcDemo {

    static final int ITERATIONS = 2_000;
    static final int RETAIN_EVERY = 100;   // 每 100 筆保留一筆,模擬長生命週期物件

    public static void main(String[] args) {
        System.out.println("使用中的 GC:" + currentGcNames());

        List<byte[]> retained = new ArrayList<>();
        long start = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            byte[] chunk = new byte[1024 * 1024];       // 每次分配 1MB(多數立即變成垃圾)
            if (i % RETAIN_EVERY == 0) {
                retained.add(chunk);                    // 少數晉升為長生命週期物件
            }
        }

        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        long usedMb = (Runtime.getRuntime().totalMemory()
                - Runtime.getRuntime().freeMemory()) / (1024 * 1024);

        System.out.println("共分配 " + ITERATIONS + " MB,保留 " + retained.size()
                + " MB,耗時 " + elapsedMs + " ms,目前堆使用約 " + usedMb + " MB");
    }

    static String currentGcNames() {
        var names = new ArrayList<String>();
        for (var gc : java.lang.management.ManagementFactory.getGarbageCollectorMXBeans()) {
            names.add(gc.getName());
        }
        return String.join(", ", names);
    }
}
