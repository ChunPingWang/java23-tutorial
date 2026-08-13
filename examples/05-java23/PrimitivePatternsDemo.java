/// 模式比對支援基本型別(JEP 455,Java 23 預覽)。
///
/// 執行方式:
/// ```
/// java --enable-preview PrimitivePatternsDemo.java
/// ```
public class PrimitivePatternsDemo {

    static String describeStatus(int status) {
        return switch (status) {
            case 200 -> "OK";
            case int s when s >= 500 -> "伺服器錯誤(" + s + ")";
            case int s -> "其他狀態:" + s;
        };
    }

    public static void main(String[] args) {
        System.out.println(describeStatus(200));
        System.out.println(describeStatus(503));
        System.out.println(describeStatus(404));

        // instanceof 檢查 long 是否能無損轉為 int
        long fits = 1_000_000L;
        long tooBig = 10_000_000_000L;
        if (fits instanceof int small) {
            System.out.println(fits + " 可無損轉為 int:" + small);
        }
        if (!(tooBig instanceof int)) {
            System.out.println(tooBig + " 無法無損轉為 int");
        }
    }
}
