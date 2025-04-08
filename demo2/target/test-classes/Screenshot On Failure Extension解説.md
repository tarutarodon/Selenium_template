# ScreenshotOnFailureExtension.java の詳細解説

このドキュメントでは、JUnit 5とSeleniumを組み合わせてテスト失敗時にスクリーンショットを自動保存する拡張クラス `ScreenshotOnFailureExtension` について、コードベースで詳細に解説します。

## ✅ クラス定義

```java
public class ScreenshotOnFailureExtension implements TestWatcher, BeforeTestExecutionCallback, TestExecutionExceptionHandler
```

### 役割:
JUnit 5の拡張機能を3つ実装しています：

- `TestWatcher`: テストの成功/失敗/中止にフックできる
- `BeforeTestExecutionCallback`: テストの直前に処理を挟む
- `TestExecutionExceptionHandler`: テスト中に発生した例外を補足・処理できる

これにより、失敗時のスクリーンショット撮影、実行前の準備、例外の捕捉と再スローができます。

---

## 🔧 フィールド

```java
private WebDriver driver;
```

テストクラス側（例: BaseTest）から受け取った WebDriver を保持します。

---

## 🔍 テスト実行前処理：`BeforeTestExecutionCallback`

```java
@Override
public void beforeTestExecution(ExtensionContext context) throws Exception {
    Object testInstance = context.getRequiredTestInstance();
    if (testInstance instanceof BaseTest) {
        this.driver = ((BaseTest) testInstance).getDriver();
    }
}
```

### 処理内容：
- 現在実行中のテストインスタンスを取得
- それが `BaseTest` クラス（WebDriver 保持）であれば、`getDriver()` で WebDriver を取得して保持

---

## 📸 テスト失敗時処理：`TestWatcher`

```java
@Override
public void testFailed(ExtensionContext context, Throwable cause) {
    takeScreenshot(context);
}
```

### 処理内容：
- `testFailed()` メソッドは、テストが失敗したとき自動的に呼び出されます。
- 内部で `takeScreenshot()` メソッドを呼び出します。

---

## ❗ 例外処理：`TestExecutionExceptionHandler`

```java
@Override
public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
    takeScreenshot(context);
    throw throwable;
}
```

### 処理内容：
- テスト中に例外が発生すると呼び出されます。
- スクリーンショットを撮った後、例外を再スロー（テスト結果に反映）

---

## 🖼️ スクリーンショット処理

```java
private void takeScreenshot(ExtensionContext context) {
    if (driver == null) return;

    try {
        String methodName = context.getDisplayName();
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        String fileName = "screenshots/" + methodName + "_" + System.currentTimeMillis() + ".png";
        File destFile = new File(fileName);

        FileUtils.copyFile(screenshot, destFile);
        System.out.println("📸 スクリーンショット保存: " + fileName);
    } catch (IOException e) {
        System.err.println("スクリーンショットの保存に失敗: " + e.getMessage());
    }
}
```

### 処理内容：
1. `driver` が `null` でなければ続行
2. メソッド名＋タイムスタンプでファイル名を作成
3. `TakesScreenshot` にキャストして `getScreenshotAs()` を使用
4. `FileUtils.copyFile()` で保存先へコピー（Apache Commons IO）
5. エラー時にはエラーメッセージを出力

---

## 📁 補足：保存先ディレクトリ
- `screenshots/` ディレクトリが存在しない場合は事前に作成しておく必要があります。

```bash
mkdir screenshots
```

---

## 🧪 使用例（BaseTest での適用）

```java
@ExtendWith(ScreenshotOnFailureExtension.class)
public class HotelTest extends BaseTest {
    // テストメソッド
}
```

---

## ✅ まとめ
| 特徴 | 内容 |
|------|------|
| 利点 | WebDriver の再利用、例外対応、スクリーンショット自動撮影 |
| 適用先 | テストクラスに `@ExtendWith(...)` で簡単に導入可能 |
| 拡張性 | テスト結果に応じた通知やログ記録への応用も可能 |


今後、Allureレポート連携や複数ブラウザ対応なども視野に入れることができます。

---

ご不明点や、さらに自動化を進めたい部分があればお気軽にどうぞ！

