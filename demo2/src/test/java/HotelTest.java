import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ScreenshotOnFailureExtension.class)
public class HotelTest extends BaseTest {

    @Test
    public void testTitle() {
        HotelPage hotelPage = new HotelPage(driver);
        hotelPage.openHomePage();

        System.out.println("テストケース１");
        // タイトルの検証
        String title = driver.getTitle();
        assertEquals("HOTEL PLANISPHERE - テスト自動化練習サイト", title);
    }

    @Test
    public void testLogin() {
        HotelPage hotelPage = new HotelPage(driver);
        hotelPage.openHomePage();
        hotelPage.clickLoginButton();
        hotelPage.enterLoginCredentials("ichiro@example.com", "passwor");
        hotelPage.submitLogin();

        // ログイン後のURL検証
        assertEquals("https://hotel-example-site.takeyaqa.dev/ja/mypage.html", driver.getCurrentUrl());

        // 「宿泊予約」をクリック
        driver.findElement(By.xpath("//*[@id=\"navbarNav\"]/ul/li[2]/a")).click();
        hotelPage.checkPlanPage();
    }

    @Test
    public void apiTest() {
        ApiTestWithRestAssured api = new ApiTestWithRestAssured();
        api.testApiResponse();
    }

    @Test
    public void afterFailTest() {
        HotelPage hotelPage = new HotelPage(driver);
        hotelPage.openHomePage();
    }

    // @Test
    // @Tag("fail_screenshot")
    // public void testFailureExample() {
    // driver.get("https://example.com/");
    // Assertions.fail("意図的に失敗させるテスト"); // 失敗時にスクリーンショットが撮れるか確認
    // }
}
