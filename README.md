# Java 23 基礎教學

本文件是 Java 23 的入門教學,涵蓋環境安裝、語言基礎、Java 23 的重要新特性,以及垃圾回收器(GC)的選擇與最佳化。

> Java 23 於 2024 年 9 月發布,屬於非 LTS 版本(上一個 LTS 為 Java 21,下一個 LTS 為 Java 25)。學習與嘗鮮沒問題,正式環境建議評估 LTS 版本。

## 目錄

1. [環境安裝](#1-環境安裝)
2. [第一個程式](#2-第一個程式)
3. [語言基礎](#3-語言基礎)
4. [物件導向基礎](#4-物件導向基礎)
5. [常用 API](#5-常用-api)
6. [Java 23 新特性總覽](#6-java-23-新特性總覽)
7. [GC 的選擇與最佳化](#7-gc-的選擇與最佳化)
8. [延伸閱讀](#8-延伸閱讀)

---

## 1. 環境安裝

推薦使用 [SDKMAN!](https://sdkman.io/)(Linux / macOS / WSL)管理多版本 JDK:

```bash
# 安裝 SDKMAN!
curl -s "https://get.sdkman.io" | bash

# 安裝 Java 23(以 Temurin 發行版為例)
sdk install java 23-tem

# 確認版本
java -version
# openjdk version "23" 2024-09-17
```

也可以直接從 [Adoptium](https://adoptium.net/) 或 [Oracle](https://www.oracle.com/java/technologies/downloads/) 下載安裝。

---

## 2. 第一個程式

### 傳統寫法

```java
// Hello.java
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello, Java 23!");
    }
}
```

```bash
javac Hello.java   # 編譯
java Hello         # 執行

# 或者單一檔案直接執行(Java 11+)
java Hello.java
```

### 簡化寫法(Java 23 預覽功能:隱式宣告類別)

Java 23 讓初學者可以省略類別宣告與 `main` 的完整簽名(JEP 477,第三次預覽):

```java
// Hello.java
void main() {
    println("Hello, Java 23!");
}
```

```bash
java --enable-preview Hello.java
```

---

## 3. 語言基礎

### 3.1 變數與型別

```java
// 基本型別
int age = 30;
long population = 8_000_000_000L;   // 底線可提升可讀性
double pi = 3.14159;
boolean active = true;
char grade = 'A';

// 參考型別
String name = "Alice";

// 區域變數型別推斷(Java 10+)
var list = new ArrayList<String>();  // 推斷為 ArrayList<String>
```

### 3.2 流程控制

```java
// if / else
if (age >= 18) {
    System.out.println("成年");
} else {
    System.out.println("未成年");
}

// switch 運算式(Java 14+ 標準化),支援箭頭語法與回傳值
String label = switch (grade) {
    case 'A', 'B' -> "優良";
    case 'C'      -> "及格";
    default       -> "不及格";
};

// 迴圈
for (int i = 0; i < 5; i++) { /* ... */ }

for (String s : List.of("a", "b", "c")) { /* for-each */ }

int n = 0;
while (n < 10) { n++; }
```

### 3.3 文字區塊(Text Blocks,Java 15+)

```java
String json = """
        {
          "name": "Alice",
          "age": 30
        }
        """;
```

### 3.4 方法

```java
static int add(int a, int b) {
    return a + b;
}

// 可變參數
static int sum(int... nums) {
    int total = 0;
    for (int x : nums) total += x;
    return total;
}
```

### 3.5 例外處理

```java
try {
    int x = Integer.parseInt("abc");
} catch (NumberFormatException e) {
    System.err.println("格式錯誤:" + e.getMessage());
} finally {
    System.out.println("一定會執行");
}

// try-with-resources:自動關閉資源
try (var reader = Files.newBufferedReader(Path.of("data.txt"))) {
    System.out.println(reader.readLine());
} catch (IOException e) {
    e.printStackTrace();
}
```

---

## 4. 物件導向基礎

### 4.1 類別與物件

```java
public class Person {
    private final String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge()     { return age; }

    @Override
    public String toString() {
        return "Person[name=%s, age=%d]".formatted(name, age);
    }
}
```

### 4.2 Record(Java 16+):不可變資料類別

上面的 `Person` 可以一行搞定,自動產生建構子、`equals`、`hashCode`、`toString`:

```java
public record Person(String name, int age) {}

var p = new Person("Alice", 30);
System.out.println(p.name());  // Alice
```

### 4.3 介面與繼承

```java
interface Shape {
    double area();

    // default 方法可提供預設實作
    default String describe() {
        return "面積為 " + area();
    }
}

record Circle(double radius) implements Shape {
    public double area() { return Math.PI * radius * radius; }
}

record Square(double side) implements Shape {
    public double area() { return side * side; }
}
```

### 4.4 Sealed(密封)類別(Java 17+)

限制哪些類別可以實作/繼承,搭配模式比對非常好用:

```java
sealed interface Shape permits Circle, Square {}
```

### 4.5 模式比對(Pattern Matching)

```java
// instanceof 模式(Java 16+)
Object obj = "hello";
if (obj instanceof String s) {
    System.out.println(s.length());  // 直接使用 s,不需轉型
}

// switch 模式比對 + Record 解構(Java 21+)
static double area(Shape shape) {
    return switch (shape) {
        case Circle(double r)   -> Math.PI * r * r;
        case Square(double s)   -> s * s;
        // sealed interface 已涵蓋所有情況,不需要 default
    };
}
```

---

## 5. 常用 API

### 5.1 集合(Collections)

```java
// 不可變集合工廠方法(Java 9+)
List<String> fruits = List.of("apple", "banana", "cherry");
Set<Integer> nums   = Set.of(1, 2, 3);
Map<String, Integer> scores = Map.of("Alice", 90, "Bob", 85);

// 可變集合
var list = new ArrayList<String>();
list.add("hello");

var map = new HashMap<String, Integer>();
map.put("key", 42);
map.getOrDefault("missing", 0);
```

### 5.2 Stream API

```java
List<String> result = fruits.stream()
        .filter(f -> f.length() > 5)
        .map(String::toUpperCase)
        .sorted()
        .toList();                    // Java 16+ 直接 toList()

int total = IntStream.rangeClosed(1, 100).sum();  // 1 加到 100

// 分組
Map<Integer, List<String>> byLength = fruits.stream()
        .collect(Collectors.groupingBy(String::length));
```

### 5.3 Optional:避免 NullPointerException

```java
Optional<String> found = fruits.stream()
        .filter(f -> f.startsWith("b"))
        .findFirst();

String value = found.orElse("預設值");
found.ifPresent(System.out::println);
```

### 5.4 虛擬執行緒(Virtual Threads,Java 21+)

輕量級執行緒,適合高併發的 I/O 密集工作:

```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 10_000; i++) {
        int id = i;
        executor.submit(() -> {
            Thread.sleep(1000);          // 不會佔住 OS 執行緒
            return "task-" + id;
        });
    }
}  // executor 關閉時等待所有任務完成
```

---

## 6. Java 23 新特性總覽

| JEP | 特性 | 狀態 |
|-----|------|------|
| 455 | 模式比對支援基本型別(Primitive Types in Patterns) | 預覽 |
| 467 | Markdown 文件註解 | 正式 |
| 471 | 廢棄 `sun.misc.Unsafe` 的記憶體存取方法 | 正式 |
| 473 | Stream Gatherers(自訂中間操作) | 第二次預覽 |
| **474** | **ZGC 預設改為分代模式(Generational Mode)** | **正式** |
| 476 | 模組匯入宣告(Module Import Declarations) | 預覽 |
| 477 | 隱式宣告類別與實例 main 方法 | 第三次預覽 |
| 480 | 結構化併發(Structured Concurrency) | 第三次預覽 |
| 481 | Scoped Values | 第三次預覽 |
| 482 | 彈性建構子本體(Flexible Constructor Bodies) | 第二次預覽 |

> 預覽功能需加 `--enable-preview` 編譯與執行,API 可能在後續版本變動。

### 6.1 Markdown 文件註解(JEP 467)

JavaDoc 終於可以用 Markdown 寫,使用 `///` 開頭:

```java
/// 計算兩數之和。
///
/// 使用範例:
/// ```java
/// int result = add(1, 2);
/// ```
///
/// @param a 第一個數
/// @param b 第二個數
/// @return 兩數之和
static int add(int a, int b) { return a + b; }
```

### 6.2 模式比對支援基本型別(JEP 455,預覽)

```java
int status = 200;
String message = switch (status) {
    case 200 -> "OK";
    case int s when s >= 500 -> "伺服器錯誤";
    case int s -> "其他狀態:" + s;
};

// instanceof 也可以檢查基本型別轉換是否安全
long big = 1_000_000L;
if (big instanceof int small) {   // 檢查 long 是否能無損轉為 int
    System.out.println(small);
}
```

### 6.3 Stream Gatherers(JEP 473,預覽)

自訂 Stream 的中間操作,補足 `map`/`filter` 做不到的事:

```java
// 滑動視窗
List<List<Integer>> windows = Stream.of(1, 2, 3, 4, 5)
        .gather(Gatherers.windowSliding(3))
        .toList();
// [[1,2,3], [2,3,4], [3,4,5]]
```

### 6.4 模組匯入宣告(JEP 476,預覽)

一行匯入整個模組所有匯出的套件:

```java
import module java.base;   // List、Map、Stream、Path... 全部可用

void main() {
    var list = List.of(1, 2, 3);
}
```

### 6.5 彈性建構子本體(JEP 482,預覽)

允許在呼叫 `super(...)` 之前先執行驗證邏輯:

```java
public class PositiveNumber extends Number {
    private final int value;

    public PositiveNumber(int value) {
        if (value <= 0) {                          // Java 23 之前不能寫在 super() 前
            throw new IllegalArgumentException("必須為正數");
        }
        super();
        this.value = value;
    }
    // ...
}
```

---

## 7. GC 的選擇與最佳化

垃圾回收(Garbage Collection)自動管理記憶體,但不同 GC 演算法在**吞吐量(Throughput)**、**延遲(Latency)**、**記憶體佔用(Footprint)**之間有不同取捨。選對 GC 往往比調一堆參數更有效。

### 7.1 Java 23 提供的 GC

| GC | 啟用參數 | 設計目標 | 適用場景 |
|----|---------|---------|---------|
| **Serial GC** | `-XX:+UseSerialGC` | 最小記憶體佔用、單執行緒 | 小型工具、容器記憶體 < 512MB、單核心環境 |
| **Parallel GC** | `-XX:+UseParallelGC` | 最大吞吐量 | 批次處理、離線運算,可接受較長停頓 |
| **G1 GC**(預設) | `-XX:+UseG1GC` | 吞吐量與延遲的平衡 | 大多數伺服器應用的預設選擇 |
| **ZGC** | `-XX:+UseZGC` | 超低延遲(停頓 < 1ms) | 延遲敏感服務、超大堆(數百 GB 到 TB) |
| **Shenandoah** | `-XX:+UseShenandoahGC` | 低延遲 | 與 ZGC 類似(部分發行版才內建,如 Temurin) |
| Epsilon | `-XX:+UseEpsilonGC` | 不回收(實驗用) | 效能測試基準、極短命程式 |

> **Java 23 重點(JEP 474)**:ZGC 預設改為**分代模式(Generational ZGC)**。分代 ZGC 依「多數物件朝生夕死」的假設,把年輕物件與老物件分開回收,大幅降低 CPU 開銷與記憶體佔用。非分代模式已被廢棄,使用 `-XX:-ZGenerational` 會出現警告,未來版本將移除。

### 7.2 如何選擇:決策指南

```text
你的應用最在乎什麼?
│
├─ 記憶體極度受限(小容器、嵌入式)
│    └─ Serial GC
│
├─ 總處理量最大化,停頓幾秒也沒關係(批次、ETL)
│    └─ Parallel GC
│
├─ 一般 Web / API 服務,停頓幾十毫秒可接受
│    └─ G1 GC(預設,通常不用改)
│
└─ 延遲極度敏感(交易系統、即時服務)或堆超大(> 32GB)
     └─ ZGC(Java 23 預設即為分代模式)
```

實務建議:**先用預設的 G1 跑,拿到實際的 GC 數據後再決定要不要換**。沒有量測就調校是浪費時間。

### 7.3 基本記憶體參數

```bash
# 設定堆的初始與最大值(生產環境常設為相同,避免動態調整的開銷)
java -Xms4g -Xmx4g -jar app.jar

# 容器環境:改用百分比,JVM 會自動感知容器的記憶體上限
java -XX:InitialRAMPercentage=75 -XX:MaxRAMPercentage=75 -jar app.jar

# 其他常用參數
-Xss512k                      # 每條執行緒的堆疊大小
-XX:MaxMetaspaceSize=256m     # Metaspace(類別中繼資料)上限
-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/dumps/  # OOM 時自動傾印堆
```

> **容器注意事項**:JDK 10+ 已能感知 cgroup 限制。若只給容器 1 個 CPU,JVM 會自動退回 Serial GC;請確保容器至少有 2 個以上的 CPU 才會啟用 G1。

### 7.4 G1 GC 最佳化

G1 的核心理念是「**設定目標停頓時間,其餘交給它自動調整**」:

```bash
java -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=100 \      # 目標最大停頓時間(預設 200ms)
     -Xms4g -Xmx4g \
     -jar app.jar
```

常用調校參數:

| 參數 | 說明 |
|------|------|
| `-XX:MaxGCPauseMillis=200` | 目標停頓時間。**設太低**(如 10ms)反而會讓 G1 把年輕代縮太小,GC 更頻繁、吞吐量下降 |
| `-XX:InitiatingHeapOccupancyPercent=45` | 老年代佔比達此值時啟動並發標記週期。頻繁 Full GC 時可調低讓回收更早開始 |
| `-XX:G1HeapRegionSize=8m` | Region 大小(1–32MB)。有大量大物件(humongous objects)時可調大 |
| `-XX:G1ReservePercent=10` | 保留的空堆比例,防止晉升失敗(to-space exhausted) |

**G1 常見問題與對策**:

- **`to-space exhausted` / Full GC 頻繁** → 堆太小或物件晉升太快:加大 `-Xmx`、調低 `InitiatingHeapOccupancyPercent`。
- **大量 humongous 物件分配**(單一物件 > Region 一半)→ 調大 `-XX:G1HeapRegionSize`,或從程式面減少超大陣列/集合的分配。

### 7.5 ZGC 最佳化

ZGC 的停頓時間與堆大小無關(次毫秒級),Java 23 起預設即為分代模式:

```bash
java -XX:+UseZGC -Xms16g -Xmx16g -jar app.jar
```

| 參數 | 說明 |
|------|------|
| `-XX:SoftMaxHeapSize=12g` | 軟上限:ZGC 盡量將堆維持在此值以下,但壓力大時仍可用到 `-Xmx`。適合想降低常駐記憶體的場景 |
| `-XX:ZAllocationSpikeTolerance=2` | 分配尖峰容忍度,分配速率波動大時可調高讓 GC 更早啟動 |
| `-XX:+ZUncommit` | 將閒置記憶體歸還作業系統(預設開啟) |

**ZGC 注意事項**:

- ZGC 是並發回收器,會與應用程式**搶 CPU**;CPU 已經吃緊的環境,吞吐量可能比 G1 差。
- 若分配速率超過回收速度,會發生 **allocation stall**(應用執行緒被迫等待)。解法:加大堆或增加 CPU。
- 堆要給得比 G1 寬裕一些(ZGC 用空間換延遲)。

### 7.6 Parallel GC 最佳化

追求吞吐量的批次工作:

```bash
java -XX:+UseParallelGC \
     -XX:ParallelGCThreads=8 \       # GC 執行緒數(預設依 CPU 數)
     -XX:MaxGCPauseMillis=... \      # 也可設定目標,但會犧牲吞吐量
     -XX:GCTimeRatio=99 \            # 目標:GC 時間佔比 < 1/(1+99) = 1%
     -Xms8g -Xmx8g \
     -jar app.jar
```

### 7.7 GC 觀測與診斷

**沒有數據就不要調參數。**先開 GC log:

```bash
# 統一日誌框架(Java 9+),輪替保留 5 個 20MB 的檔案
java -Xlog:gc*:file=gc.log:time,uptime,level,tags:filecount=5,filesize=20m -jar app.jar

# 快速觀察某個運行中 JVM 的 GC 狀況
jstat -gcutil <pid> 1000     # 每秒印出各代使用率與 GC 次數/時間

# 產生堆傾印以分析記憶體洩漏
jmap -dump:live,format=b,file=heap.hprof <pid>

# 低開銷的持續剖析(生產環境可用)
java -XX:StartFlightRecording=duration=120s,filename=rec.jfr -jar app.jar
```

分析工具:

- **[GCeasy](https://gceasy.io/)**、**[GCViewer](https://github.com/chewiebug/GCViewer)**:分析 GC log,看停頓分佈與回收效率。
- **JDK Mission Control (JMC)**:分析 JFR 記錄,找出分配熱點。
- **Eclipse MAT**:分析 heap dump,找出記憶體洩漏。

**要看的關鍵指標**:

1. **GC 停頓時間分佈**(平均值與 P99):是否符合你的 SLA?
2. **GC 頻率與 GC 總時間佔比**:健康的應用通常 GC 時間 < 5%。
3. **回收後老年代的水位**:每次 Full GC 後水位持續上升 → 可能有記憶體洩漏。
4. **分配速率(Allocation Rate)**:過高(如 > 1GB/s)時,先從程式面減少物件分配,比調 GC 參數更有效。

### 7.8 調校心法總結

1. **先量測,再調校**:開 GC log,用真實流量或壓測取得基準數據。
2. **一次只改一個參數**,對照前後數據。
3. **選對 GC 比細調參數重要**:多數情況 G1 預設值已經很好。
4. **`-Xms` = `-Xmx`** 是伺服器應用的常見實務,避免堆動態伸縮。
5. **不要照抄網路上的參數組合**:別人的最佳化可能是你的災難,尤其是舊版 JVM 的參數。
6. **從應用程式下手常常更有效**:減少不必要的物件分配(如迴圈內建立大集合、過度的字串串接)、使用合適的資料結構,勝過任何 GC 參數。

---

## 8. 延伸閱讀

- [Java 23 官方發布說明](https://jdk.java.net/23/release-notes)
- [OpenJDK JEP 索引](https://openjdk.org/jeps/0)
- [JEP 474: ZGC — Generational Mode by Default](https://openjdk.org/jeps/474)
- [HotSpot GC Tuning Guide(官方調校指南)](https://docs.oracle.com/en/java/javase/23/gctuning/)
- [Dev.java 官方教學](https://dev.java/learn/)
- [Inside Java(官方部落格與 Podcast)](https://inside.java/)
