import module java.base;

/// 模組匯入宣告(JEP 476,Java 23 預覽):
/// 一行 `import module java.base;` 即可使用 List、Map、Stream、Path 等
/// java.base 模組匯出的所有套件。
///
/// 執行方式:
/// ```
/// java --enable-preview ModuleImportDemo.java
/// ```
public class ModuleImportDemo {

    public static void main(String[] args) {
        List<Integer> nums = List.of(1, 2, 3, 4, 5);          // java.util
        int sum = nums.stream().mapToInt(Integer::intValue).sum();  // java.util.stream
        Path path = Path.of("/tmp/demo.txt");                  // java.nio.file
        Optional<String> opt = Optional.of("模組匯入");         // java.util

        System.out.println("nums = " + nums + ",總和 = " + sum);
        System.out.println("path 檔名 = " + path.getFileName());
        System.out.println("opt = " + opt.orElse("空"));
    }
}
