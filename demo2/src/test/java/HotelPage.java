import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HotelPage {
    private WebDriver driver;

    public HotelPage(WebDriver driver) {
        this.driver = driver;
    }

    /** ホームページを開く */
    public void openHomePage() {
        driver.get("https://hotel-example-site.takeyaqa.dev/ja/");
    }

    /** ログインボタンをクリック */
    public void clickLoginButton() {
        WebElement btn = driver.findElement(By.className("btn-outline-secondary"));
        System.out.println("Login button: " + btn);
        driver.findElement(By.xpath("//*[@id=\"login-holder\"]/a")).click();
    }

    /** メールアドレスとパスワードを入力 */
    public void enterLoginCredentials(String email, String password) {
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(password);
    }

    /** ログインを実行 */
    public void submitLogin() {
        WebElement loginButton = driver.findElement(By.id("login-button"));
        System.out.println("ログインボタンのテキスト: " + loginButton.getText());
        loginButton.click();
    }

    /** 宿泊プランページのチェック */
    public void checkPlanPage() {
        String title = driver.getTitle();
        assert title.equals("宿泊プラン一覧 | HOTEL PLANISPHERE - テスト自動化練習サイト");

        String cardHeader = driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[1]")).getText();
        assert cardHeader.equals("⭐おすすめプラン⭐");

        String cardTitle = driver.findElement(By.cssSelector("body > div > div:nth-child(2) > div > div > div.card-body > h5")).getText();
        assert cardTitle.equals("お得な特典付きプラン");

        String list1 = driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[2]/ul/li[1]")).getText();
        String list2 = driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[2]/ul/li[2]")).getText();
        String list3 = driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[2]/ul/li[3]")).getText();
        String button = driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[2]/a")).getText();

        String cardFooter = driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[3]")).getText();
        assert cardFooter.equals("本日限り");

        assertAll(
            () -> assertEquals("大人1名7,000円", list1),
            () -> assertEquals("1名様から", list2),
            () -> assertEquals("スタンダードツイン", list3),
            () -> assertEquals("このプランで予約",button)
        );

        System.out.println("OK");
    }
}
