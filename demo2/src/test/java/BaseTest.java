import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach; // JUnitのAfterEachアノテーション：各テストの後にメソッドを実行するためのもの。
import org.junit.jupiter.api.BeforeEach; // JUnitのBeforeEachアノテーション：各テストの前にメソッドを実行するためのもの。
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver; // Selenium WebDriverのインターフェイス：ブラウザを操作するために使用。
import org.openqa.selenium.chrome.ChromeDriver; // Chromeブラウザ用のWebDriver実装。
import io.github.bonigarcia.wdm.WebDriverManager; // WebDriverManager：ドライバのセットアップを簡略化。

public class BaseTest { // テストの基盤となるクラス。
    protected WebDriver driver; // WebDriverをインスタンス化するための変数（子クラスからもアクセス可能）。

    @BeforeEach // 各テストメソッドが実行される前に実行されるメソッド。
    public void setUp() {
        WebDriverManager.chromedriver().setup(); // ChromeDriverを自動的にセットアップしてくれるメソッド。
        driver = new ChromeDriver(); // Chromeブラウザのインスタンスを作成。
    }

    @AfterEach // 各テストメソッドが終了した後に実行されるメソッド。
    public void tearDown(TestInfo testInfo) {
        if (driver != null) { // driverがnullでない場合のみ終了処理を実行。
            driver.quit(); // ブラウザを閉じて、WebDriverのリソースを解放する
        }
    }

    public void takeScreenshot(String name) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            File destFile = new File("screenshots/" + name + "_" + System.currentTimeMillis() + ".png");
            FileUtils.copyFile(screenshot, destFile);
            System.out.println("try");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("catch");
        }
    }

    public WebDriver getDriver() {
        return this.driver;
    }

}
