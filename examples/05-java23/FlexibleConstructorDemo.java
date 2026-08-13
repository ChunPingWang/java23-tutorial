/// 彈性建構子本體(JEP 482,Java 23 第二次預覽):
/// 允許在呼叫 super(...) 之前先執行驗證邏輯。
///
/// 執行方式:
/// ```
/// java --enable-preview FlexibleConstructorDemo.java
/// ```
public class FlexibleConstructorDemo {

    static class PositiveNumber extends Number {
        private final int value;

        PositiveNumber(int value) {
            if (value <= 0) {                  // Java 23 之前不能寫在 super() 之前
                throw new IllegalArgumentException("必須為正數,收到:" + value);
            }
            super();
            this.value = value;
        }

        @Override public int intValue()       { return value; }
        @Override public long longValue()     { return value; }
        @Override public float floatValue()   { return value; }
        @Override public double doubleValue() { return value; }
    }

    public static void main(String[] args) {
        var ok = new PositiveNumber(42);
        System.out.println("建立成功:" + ok.intValue());

        try {
            new PositiveNumber(-1);
        } catch (IllegalArgumentException e) {
            System.out.println("在 super() 之前就攔下非法引數:" + e.getMessage());
        }
    }
}
