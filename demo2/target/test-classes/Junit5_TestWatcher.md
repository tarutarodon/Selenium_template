# JUnit 5 で TestWatcher を使用してテスト失敗時にスクリーンショットを撮る

## ✅ JUnit 5 の `TestWatcher` とは？
JUnit 5 には **テストのライフサイクルを監視するための拡張機能** として `TestWatcher` が用意されています。
これを使うことで、「テストが失敗した時にスクリーンショットを撮る」という処理を共通化できます。

`TestWatcher` は `TestExecutionExceptionHandler` を含むクラスで、**テストの成功・失敗をフックできる** のが特徴です。

## ✅ 実装の流れ
1. **TestWatcher を継承したクラスを作成**
   - `TestWatcher` を拡張し、失敗時の処理を `failed()` メソッドで実装
   - Selenium でスクリーンショットを撮影し、ファイル保存

2. **JUnit 5 のテストクラスで `@ExtendWith` を使って適用**
   - `@ExtendWith(ScreenshotOnFailure.class)` を付与するだけで、すべてのテストケースに適用可能

---

## ✅ 実装コード

### ① `TestWatcher` を継承したクラスを作成

```java
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

public class ScreenshotOnFailure implements TestWatcher {

    private final WebDriver driver;

    public ScreenshotOnFailure(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String testName = context.getDisplayName();
            Path destination = Path.of("screenshots", testName + ".png");

            Files.createDirectories(destination.getParent());
            Files.copy(screenshot.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Screenshot saved: " + destination);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testSuccessful(ExtensionContext context) {}
}
```

### ② JUnit 5 のテストクラスで使用

```java
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

@ExtendWith(ScreenshotOnFailure.class)
public class SampleTest {

    private WebDriver driver;
    private static ScreenshotOnFailure watcher;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        watcher = new ScreenshotOnFailure(driver);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testFailureExample() {
        driver.get("https://example.com/");
        Assertions.fail("意図的に失敗させるテスト");
    }

    @Test
    void testSuccessExample() {
        driver.get("https://example.com/");
        Assertions.assertTrue(true);
    }
}
```

---

## ✅ コードの詳しい解説

### 1️⃣ `TestWatcher` の役割
JUnit 5 の `TestWatcher` を継承することで、
- `testFailed(ExtensionContext context, Throwable cause)` → **テストが失敗した時に実行される**
- `testSuccessful(ExtensionContext context)` → **テストが成功した時に実行される**
といったテストのライフサイクルを監視できる。

### 2️⃣ `@ExtendWith(ScreenshotOnFailure.class)` の役割
JUnit 5 では、`@ExtendWith(クラス名.class)` を使うことで **特定のルール（拡張機能）をテストクラスに適用** できる。
これにより `ScreenshotOnFailure` を適用することで、「テストが失敗したらスクリーンショットを撮る」という動作を **全テストケースに適用** できる。

### 3️⃣ `TakesScreenshot` を使用したスクリーンショットの撮影
```java
File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
Path destination = Path.of("screenshots", testName + ".png");
Files.copy(screenshot.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
```
- `TakesScreenshot` インターフェースを使ってスクリーンショットを撮影
- `Path.of("screenshots", testName + ".png")` で **テスト名をファイル名として保存**
- `Files.copy()` で **screenshots フォルダに画像を保存**

---

## ✅ 実装のポイントまとめ
1. **`TestWatcher` を使えばテスト失敗時の処理を共通化できる**
2. **`@ExtendWith(ScreenshotOnFailure.class)` を付けるだけで適用できる**
3. **スクリーンショットの保存処理を `Files.copy()` で簡潔に記述**
4. **テストクラスの `setUp()` 内で `ScreenshotOnFailure` に WebDriver を渡す**

---

## ✅ さらにシンプルにしたい場合
もし **`@ExtendWith` を使わず、テストクラス内でのみ適用** したい場合は、以下のように変更できる。

```java
@AfterEach
void tearDown(TestInfo testInfo) {
    if (driver != null) {
        if (testInfo.getTags().contains("fail_screenshot")) {
            new ScreenshotOnFailure(driver).testFailed(null, new Throwable("スクリーンショット撮影"));
        }
        driver.quit();
    }
}
```

これにより、**特定のテストにだけ適用することも可能** となる。

---

## ✅ まとめ
JUnit 5 の `TestWatcher` を使えば、**短いコードで全テストケースの失敗時にスクリーンショットを撮る** ことが可能。
拡張クラスを作って `@ExtendWith` を付けるだけで適用できるので、**メンテナンス性も抜群**。

この実装を試してみて、わからない点があれば気軽に質問してください！ 💡

