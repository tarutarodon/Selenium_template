import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v133.network.Network;
import org.openqa.selenium.devtools.v133.network.model.ResponseReceived;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ApiTestWithDevTools {
    
    @Test
    public void testApiResponse() {
        // ChromeDriverのインスタンスを作成（ブラウザを開く）
        WebDriver driver = new ChromeDriver();
        
        // Chrome DevToolsを利用するための DevTools インスタンスを取得
        DevTools devTools = ((ChromeDriver) driver).getDevTools();
        
        // DevToolsセッションを作成（これによりブラウザの内部情報を取得可能になる）
        devTools.createSession();
        
        // ネットワークのモニタリングを有効化
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        // APIレスポンスを監視するリスナーを追加
        devTools.addListener(Network.responseReceived(), response -> {
            ResponseReceived res = response;  // レスポンスデータを取得
            String url = res.getResponse().getUrl();  // リクエストされたURLを取得
            int statusCode = res.getResponse().getStatus();  // ステータスコードを取得
            System.out.println("URL: " + url + ", ステータスコード: " + statusCode);
            
            // もし特定のAPI（jsonplaceholderの /posts/1）に対するリクエストなら、ステータスコードを検証
            if (url.contains("jsonplaceholder.typicode.com/posts/1")) {
                assertEquals(200, statusCode, "APIのレスポンスコードが200であることを確認");
            }
        });

        // テスト対象のAPIエンドポイントをブラウザで開く（リクエストを発生させる）
        driver.get("https://jsonplaceholder.typicode.com/posts/1");

        // テスト終了後にブラウザを閉じる
        driver.quit();
    }
}
