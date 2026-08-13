/// # Markdown 文件註解示範(JEP 467,Java 23 正式功能)
///
/// 這個類別的 JavaDoc 全部用 **Markdown** 撰寫,以 `///` 開頭。
/// 產生文件:`javadoc -d doc MarkdownDocDemo.java`
public class MarkdownDocDemo {

    /// 計算兩數之和。
    ///
    /// 使用範例:
    /// ```java
    /// int result = add(1, 2);   // 3
    /// ```
    ///
    /// @param a 第一個數
    /// @param b 第二個數
    /// @return 兩數之和
    public static int add(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {
        System.out.println("add(1, 2) = " + add(1, 2));
        System.out.println("(這個檔案的重點是 /// Markdown 文件註解,請用 javadoc 產生文件觀察)");
    }
}
