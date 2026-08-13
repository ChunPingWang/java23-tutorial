import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/// 虛擬執行緒(Java 21+):以 10,000 條各睡 1 秒的任務示範輕量級併發。
/// 若用平台執行緒逐一執行需要 10,000 秒;虛擬執行緒全部併發,約 1 秒完成。
///
/// 執行方式:
/// ```
/// java VirtualThreadsDemo.java
/// ```
public class VirtualThreadsDemo {

    public static void main(String[] args) {
        var counter = new AtomicInteger();
        var start = Instant.now();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 10_000; i++) {
                executor.submit(() -> {
                    Thread.sleep(Duration.ofSeconds(1));  // 不會佔住 OS 執行緒
                    counter.incrementAndGet();
                    return null;
                });
            }
        }  // executor 關閉時等待所有任務完成

        var elapsed = Duration.between(start, Instant.now());
        System.out.println("完成 " + counter.get() + " 個任務(每個睡 1 秒),共花 "
                + elapsed.toMillis() + " ms");
    }
}
