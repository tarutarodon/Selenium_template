import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import static org.junit.jupiter.api.Assertions.*;


public class sample {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // WebDriverのセットアップ(自動操作用)
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        // テスト終了後にブラウザを閉じる
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testTitle() {
        openHomePage();

        // タイトルの検証
        String title = driver.getTitle();
        assertEquals("HOTEL PLANISPHERE - テスト自動化練習サイト", title);
    }

    @Test
    public void testLogin() {
        openHomePage();
        clickLoginButton();
        enterLoginCredentials("ichiro@example.com", "password");
        submitLogin();

        //①ログイン後のURL検証
        String url = driver.getCurrentUrl();
        assertEquals("https://hotel-example-site.takeyaqa.dev/ja/mypage.html", url);
        //②会員画面のアイコン設定をクリックする（失敗するケース）
        //driver.findElement(By.xpath("//*[@id=\"icon-link\"]")).click();
        //③「宿泊予約」をクリック　②を実行した場合失敗するので実行されない
        driver.findElement(By.xpath("//*[@id=\"navbarNav\"]/ul/li[2]/a")).click();
        checkPlanPage();
    }
    
    @Test
    public void afterFailTest(){
        //上記のテストケースが失敗した際に続けて実行されるか確認
        openHomePage();
    }
    
    /** ホームページを開く */
    private void openHomePage() {
        driver.get("https://hotel-example-site.takeyaqa.dev/ja/");
    }
    
    /** ログインボタンをクリック */
    private void clickLoginButton() {
        WebElement btn = driver.findElement(By.className("btn-outline-secondary"));
        System.out.println("Login button: " + btn);
        driver.findElement(By.xpath("//*[@id=\"login-holder\"]/a")).click();
    }
    
    /** メールアドレスとパスワードを入力 */
    private void enterLoginCredentials(String email, String password) {
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(password);
    }
    
    /** ログインを実行 */
    private void submitLogin() {
        WebElement loginButton = driver.findElement(By.id("login-button"));
        assertEquals("ログイン", loginButton.getText());
        System.out.println("ログインボタンのテキスト: " + loginButton.getText());
        loginButton.click();
    }

    public void checkPlanPage(){
        String title = driver.getTitle();
        assertEquals("宿泊プラン一覧 | HOTEL PLANISPHERE - テスト自動化練習サイト", title);

        String card_header = driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[1]")).getText();
        assertEquals("⭐おすすめプラン⭐", card_header);

        String card_title = driver.findElement(By.cssSelector("body > div > div:nth-child(2) > div > div > div.card-body > h5")).getText();
        assertEquals("お得な特典付きプラン", card_title);
    }
}


