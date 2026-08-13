import java.util.ArrayList;
import java.util.List;

/// 語言基礎:變數與型別、流程控制、文字區塊、方法、例外處理。
///
/// 執行方式:
/// ```
/// java Basics.java
/// ```
public class Basics {

    public static void main(String[] args) {
        variables();
        controlFlow();
        textBlocks();
        methods();
        exceptions();
    }

    // === 3.1 變數與型別 ===
    static void variables() {
        System.out.println("=== 變數與型別 ===");

        int age = 30;
        long population = 8_000_000_000L;   // 底線可提升可讀性
        double pi = 3.14159;
        boolean active = true;
        char grade = 'A';
        String name = "Alice";

        var list = new ArrayList<String>(); // 型別推斷為 ArrayList<String>
        list.add("hello");

        System.out.println("age=" + age + ", population=" + population
                + ", pi=" + pi + ", active=" + active
                + ", grade=" + grade + ", name=" + name + ", list=" + list);
    }

    // === 3.2 流程控制 ===
    static void controlFlow() {
        System.out.println("=== 流程控制 ===");

        int age = 30;
        if (age >= 18) {
            System.out.println("成年");
        } else {
            System.out.println("未成年");
        }

        char grade = 'B';
        String label = switch (grade) {     // switch 運算式(Java 14+)
            case 'A', 'B' -> "優良";
            case 'C'      -> "及格";
            default       -> "不及格";
        };
        System.out.println("等級 " + grade + " → " + label);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(i).append(' ');
        }
        System.out.println("for 迴圈:" + sb);

        for (String s : List.of("a", "b", "c")) {
            System.out.print(s + " ");
        }
        System.out.println("(for-each)");

        int n = 0;
        while (n < 10) { n++; }
        System.out.println("while 結束時 n=" + n);
    }

    // === 3.3 文字區塊 ===
    static void textBlocks() {
        System.out.println("=== 文字區塊 ===");
        String json = """
                {
                  "name": "Alice",
                  "age": 30
                }
                """;
        System.out.print(json);
    }

    // === 3.4 方法 ===
    static void methods() {
        System.out.println("=== 方法 ===");
        System.out.println("add(1, 2) = " + add(1, 2));
        System.out.println("sum(1, 2, 3, 4, 5) = " + sum(1, 2, 3, 4, 5));
    }

    static int add(int a, int b) {
        return a + b;
    }

    static int sum(int... nums) {           // 可變參數
        int total = 0;
        for (int x : nums) total += x;
        return total;
    }

    // === 3.5 例外處理 ===
    static void exceptions() {
        System.out.println("=== 例外處理 ===");
        try {
            Integer.parseInt("abc");
        } catch (NumberFormatException e) {
            System.out.println("捕捉到格式錯誤:" + e.getMessage());
        } finally {
            System.out.println("finally 一定會執行");
        }
    }
}
