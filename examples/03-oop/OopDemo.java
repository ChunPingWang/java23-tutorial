/// 物件導向基礎:類別、Record、介面、Sealed 類別、模式比對。
///
/// 執行方式:
/// ```
/// java OopDemo.java
/// ```
public class OopDemo {

    // === 4.4 Sealed 介面:限制只有 Circle 與 Square 可以實作 ===
    sealed interface Shape permits Circle, Square {
        double area();

        default String describe() {         // default 方法提供預設實作
            return "面積為 " + area();
        }
    }

    // === 4.2 Record:不可變資料類別 ===
    record Circle(double radius) implements Shape {
        public double area() { return Math.PI * radius * radius; }
    }

    record Square(double side) implements Shape {
        public double area() { return side * side; }
    }

    // === 4.1 傳統類別 ===
    static class Person {
        private final String name;
        private final int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person[name=%s, age=%d]".formatted(name, age);
        }
    }

    // === 4.5 switch 模式比對 + Record 解構(Java 21+)===
    static double area(Shape shape) {
        return switch (shape) {
            case Circle(double r) -> Math.PI * r * r;
            case Square(double s) -> s * s;
            // sealed 介面已涵蓋所有情況,不需要 default
        };
    }

    public static void main(String[] args) {
        var person = new Person("Alice", 30);
        System.out.println("傳統類別:" + person);

        var personRecord = new PersonRecord("Bob", 25);
        System.out.println("Record:" + personRecord + ",name() = " + personRecord.name());

        Shape circle = new Circle(2.0);
        Shape square = new Square(3.0);
        System.out.println("circle.describe():" + circle.describe());
        System.out.println("square.describe():" + square.describe());
        System.out.println("area(circle) = " + area(circle));
        System.out.println("area(square) = " + area(square));

        // instanceof 模式(Java 16+)
        Object obj = "hello";
        if (obj instanceof String s) {
            System.out.println("instanceof 模式:字串長度 = " + s.length());
        }
    }

    record PersonRecord(String name, int age) {}
}
