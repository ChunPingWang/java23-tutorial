import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/// 常用 API:集合、Stream、Optional。
///
/// 執行方式:
/// ```
/// java ApiDemo.java
/// ```
public class ApiDemo {

    public static void main(String[] args) {
        collections();
        streams();
        optionals();
    }

    // === 5.1 集合 ===
    static void collections() {
        System.out.println("=== 集合 ===");

        List<String> fruits = List.of("apple", "banana", "cherry");
        Set<Integer> nums = Set.of(1, 2, 3);
        Map<String, Integer> scores = Map.of("Alice", 90, "Bob", 85);
        System.out.println("不可變 List:" + fruits);
        System.out.println("不可變 Set 含 2?" + nums.contains(2));
        System.out.println("Alice 的分數:" + scores.get("Alice"));

        var list = new ArrayList<String>();
        list.add("hello");
        var map = new HashMap<String, Integer>();
        map.put("key", 42);
        System.out.println("可變 List:" + list + ",map.getOrDefault(\"missing\", 0) = "
                + map.getOrDefault("missing", 0));
    }

    // === 5.2 Stream API ===
    static void streams() {
        System.out.println("=== Stream API ===");

        List<String> fruits = List.of("apple", "banana", "cherry", "fig");

        List<String> result = fruits.stream()
                .filter(f -> f.length() > 5)
                .map(String::toUpperCase)
                .sorted()
                .toList();
        System.out.println("長度 > 5 轉大寫排序:" + result);

        int total = IntStream.rangeClosed(1, 100).sum();
        System.out.println("1 加到 100 = " + total);

        Map<Integer, List<String>> byLength = fruits.stream()
                .collect(Collectors.groupingBy(String::length));
        System.out.println("依長度分組:" + byLength);
    }

    // === 5.3 Optional ===
    static void optionals() {
        System.out.println("=== Optional ===");

        List<String> fruits = List.of("apple", "banana", "cherry");

        Optional<String> found = fruits.stream()
                .filter(f -> f.startsWith("b"))
                .findFirst();
        System.out.println("orElse:" + found.orElse("預設值"));
        found.ifPresent(f -> System.out.println("ifPresent:" + f));

        Optional<String> missing = fruits.stream()
                .filter(f -> f.startsWith("z"))
                .findFirst();
        System.out.println("找不到時 orElse:" + missing.orElse("預設值"));
    }
}
