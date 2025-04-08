import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotOnFailureExtension
        implements TestWatcher, BeforeTestExecutionCallback, TestExecutionExceptionHandler {

    private WebDriver driver;
    private boolean screenshotTaken = false; // スクリーンショットを1回だけ撮るためのフラグ

    // スクリーンショット撮影の有効/無効をシステムプロパティから取得（デフォルトはtrue）
    private static final boolean SCREENSHOT_ENABLED = 
        Boolean.parseBoolean(System.getProperty("screenshot.enabled", "true"));
    // 動作	                             コマンド例
    // スクリーンショットあり（デフォルト）	mvn test または mvn test -Dscreenshot.enabled=true
    // スクリーンショットなし	               mvn test -Dscreenshot.enabled=false

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        // 現在のテストメソッドが属しているテストクラスのインスタンスを取得
        Object testInstance = context.getRequiredTestInstance();
        // インスタンスが BaseTest（WebDriverを保持しているクラス）であるかチェック
        if (testInstance instanceof BaseTest) {
            // BaseTestからWebDriverを取得し、この拡張クラスのフィールドにセット
            // オブジェクト（testInstance）が、WebDriverを保持していることを保証するサブクラス（BaseTest）を
            // 継承しているかどうかを確認し、安全に getDriver() を呼び出せるようにしている
            this.driver = ((BaseTest) testInstance).getDriver();
        }
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        // JUnit 5 の TestExecutionExceptionHandler インターフェースのメソッドです。
        // 「テスト実行中に例外がスローされたときに呼び出される処理」
        // → つまり、「テストが失敗した時のフックポイント」
        // これを使えば、「テストが失敗した時にスクリーンショットを撮る」などのカスタム処理が可能
        takeScreenshot(context);
        throw throwable; // 例外を再スローしてテスト失敗として処理させる
        // 例外を再スローしない場合Junit側でテストが成功と見なされる危険性あり
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        // fallback用。JUnitが失敗を伝えてくれた場合はこちらも動作
        // テストの実行が失敗（failed）した時に呼ばれるコールバックメソッド です。
        // つまり、テストが失敗したことをJUnitが検知したときに自動で呼び出される仕組みです。
        // handleTestExecutionException と似ていますが、テスト失敗の後に通知ベースで呼ばれるという違いがあります。
        // handleTestExecutionException が呼ばれなかった場合の 保険（fallback）処理 として実装されることが多いです。
        takeScreenshot(context);
    }

    private void takeScreenshot(ExtensionContext context) {

        if (screenshotTaken || !SCREENSHOT_ENABLED || driver == null) {
            return;
        }

        if (driver != null) {
            String methodName = context.getDisplayName().replace("()", "");
            // JUnitのテスト名を取得"testLogin()" などの表記を "testLogin" に変換
            // フォーマットを指定して現在日時を取得（例：20250406_231012）
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            // 日付ディレクトリ（例：20250406）を作成
            String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String dirPath = "screenshots/" + dateFolder;
            // 保存先ディレクトリのパスを指定
            File dir = new File(dirPath);
            // TakesScreenshot インターフェースを使いキャプチャー操作,一時的なファイルとして作成
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(dirPath + "/" + methodName + "_" + timestamp + ".png");
            
            // ディレクトリが存在しない場合は作成する
            if (!dir.exists()) {
                boolean created = dir.mkdirs(); // 中間ディレクトリも含めて作成
                if (created) {
                    System.out.println("📁 Screenshot directory created: " + dir.getAbsolutePath());
                } else {
                    System.err.println("⚠️ Failed to create screenshot directory: " + dir.getAbsolutePath());
                }
            }
            try {
                FileUtils.copyFile(screenshot, destFile);
                // Apache Commons IO の機能,一時ファイルを指定した位置にコピー
                System.out.println("📸 Screenshot taken: " + destFile.getAbsolutePath());
                screenshotTaken = true; // 撮影済みとしてマーク
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
