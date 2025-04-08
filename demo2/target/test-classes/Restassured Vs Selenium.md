# RestAssured を使用した API レスポンステストの解説

## **📝 コードの解説**
```java
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ApiTestWithRestAssured {
    
    @Test
    public void testApiResponse() {
        // RestAssured を使用して GET リクエストを送信し、レスポンスを取得
        Response response = RestAssured.get("https://jsonplaceholder.typicode.com/posts/1");
        
        // レスポンスの HTTP ステータスコードを取得
        int statusCode = response.getStatusCode();
        
        // レスポンスボディをコンソールに出力（デバッグ用）
        System.out.println("APIレスポンス: " + response.getBody().asString());

        // ステータスコードが 200 であることを検証
        assertEquals(200, statusCode, "APIのレスポンスコードが200であることを確認");
    }
}
```

## **🔍 コードの流れ**
| **ステップ** | **処理内容** |
|-------------|-------------|
| 1. `RestAssured.get()` を使用して API にリクエストを送信 |
| 2. API のレスポンス (`Response` オブジェクト) を取得 |
| 3. `getStatusCode()` で HTTP ステータスコードを取得 |
| 4. `getBody().asString()` でレスポンスボディを取得し、コンソールに出力 |
| 5. `assertEquals(200, statusCode)` でレスポンスが `200 OK` であることを確認 |

---

# **🆚 Selenium を使った API テストとの違い**

| **比較項目** | **RestAssured を使用** | **Selenium（DevTools 使用）** |
|-------------|------------------------|--------------------------------|
| **用途** | API のレスポンステスト専用 | UI テストの一環として API のレスポンスを確認 |
| **API 呼び出し方法** | `RestAssured.get(url)` を直接実行 | `driver.get(url)` を使用してブラウザ経由でアクセス |
| **ステータスコード取得** | `response.getStatusCode()` で直接取得 | `DevTools` の `Network.responseReceived()` で取得 |
| **レスポンス取得の正確性** | API のみを直接呼び出すため高速かつ正確 | UI の影響を受けるため遅延が発生することがある |
| **ブラウザの使用** | **不要**（直接 API を呼ぶ） | **必要**（Selenium でブラウザを操作） |
| **テストの速度** | 速い（API 直接通信） | 遅い（ブラウザを介するため） |
| **応用範囲** | API 単体テスト、バックエンドテスト | UI テスト + API の組み合わせ（統合テスト） |

### **🛠 どちらを使うべきか？**

| **目的** | **推奨ツール** |
|---------|--------------|
| API のテストのみを行う | ✅ RestAssured |
| API のレスポンスと UI の連携を確認したい | ✅ Selenium（DevTools） |
| API と UI の両方の動作をチェックしたい | ✅ Selenium + RestAssured の組み合わせ |

---

# **💡 まとめ**
- **RestAssured は API テスト専用のライブラリ** であり、**Selenium より高速かつ直接的に API を検証** できる。
- **Selenium は UI テスト向け** だが、`DevTools` を使えば API のステータスコードを取得可能。
- **テストの目的によって使い分ける** のがベスト！
  - **API 単体の動作確認 → RestAssured**
  - **フロントエンドとの統合テスト → Selenium（DevTools）**
  - **両方のテストが必要な場合 → Selenium + RestAssured を組み合わせる**

実際のプロジェクトでは、
**RestAssured で API のユニットテストを行い、Selenium で E2E テストを実施する** ことが多いです！ 🚀

