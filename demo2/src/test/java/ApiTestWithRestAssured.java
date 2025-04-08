import io.restassured.RestAssured; // Rest Assuredライブラリをインポート：APIのテストを簡単に行うためのツール。
import io.restassured.response.Response; // APIリクエストに対するレスポンスを管理するためのクラス。
import org.junit.jupiter.api.Test; // JUnitのTestアノテーション：テストメソッドを定義するためのもの。
import static org.junit.jupiter.api.Assertions.*; // JUnitのアサーションメソッドを静的インポート：テスト結果を検証するために使用。

public class ApiTestWithRestAssured { // APIテスト用のクラスを定義。
    
    @Test // このアノテーションで「testApiResponse」メソッドをテストメソッドとしてマーク。
    public void testApiResponse() {
        // "https://jsonplaceholder.typicode.com/posts/1" にGETリクエストを送信。
        Response response = RestAssured.get("https://jsonplaceholder.typicode.com/posts/1");
        int statusCode = response.getStatusCode(); // レスポンスのステータスコードを取得。
        
        // レスポンスのボディ内容を文字列として表示。
        System.out.println("APIレスポンス: " + response.getBody().asString());
        // ステータスコードが200であることを検証（成功していることを確認）。
        assertEquals(200, statusCode, "APIのレスポンスコードが200であることを確認");
    }
}


