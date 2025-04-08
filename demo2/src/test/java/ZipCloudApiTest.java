import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * 郵便番号検索API（https://zipcloud.ibsnet.co.jp/doc/api）をテストするクラス。
 * RestAssured を用いて API にアクセスし、JUnit 5 + Hamcrest でレスポンスを検証。
 */
public class ZipCloudApiTest {

    // テスト対象のAPIのベースURL
    private final String BASE_URL = "https://zipcloud.ibsnet.co.jp/api/search";

    /**
     * 正常系テスト：存在する郵便番号を指定した場合、正しい住所が返ることを検証。
     * 1000001 → 東京都千代田区
     */
    @Test
    void testValidZipCode() {
        // RestAssuredを使ってAPIリクエストを送信
        Response response = RestAssured.given()
                .param("zipcode", "1000001") // クエリパラメータに郵便番号を設定
                .when() // when() は 「これからリクエストを送信する」ことを示すトリガー です。
                        // RestAssuredでは、given() → when() → then() の順で使うのが基本です。
                .get(BASE_URL) // GETリクエスト送信
                .then() // レスポンスに対する検証・抽出を行うステップ を開始する合図
                .statusCode(200) // ステータスコードが200であることを確認
                .extract().response(); // レスポンスを取得
        // .extract() は レスポンスを抽出可能な状態にするメソッドです。
        // .response() を組み合わせることで、 Response 型のオブジェクトとして取得 できます。

        // レスポンスの status が 200（正常）であることを確認
        assertThat(response.jsonPath().getInt("status"), is(200));
        // message フィールドが null（エラーメッセージが存在しない＝正常）であることを確認
        assertThat(response.jsonPath().getString("message"), nullValue());

        // 住所情報の検証（都道府県・市区町村）
        assertThat(response.jsonPath().getString("results[0].address1"), is("東京都"));
        assertThat(response.jsonPath().getString("results[0].address2"), is("千代田区"));
    }

    /**
     * 異常系テスト①：存在しない郵便番号（0000000）を指定した場合、
     * レスポンスにはデータが含まれず、messageにメッセージが含まれることを確認。
     */
    @Test
    void testInvalidZipCode_NotExist() {
        Response response = RestAssured.given()
                .param("zipcode", "0000000") // 存在しない郵便番号
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(200) // ステータスコードは200（リクエスト自体は成功）
                .extract().response();

        // ステータスは 200（形式的には正常レスポンス）
        assertThat(response.jsonPath().getInt("status"), is(200));
        // results が null（データが見つからなかったことを示す）
        assertThat(response.jsonPath().getString("results"), is(nullValue()));
    }

    /**
     * 異常系テスト②：空の郵便番号（""）を送信した場合、
     * パラメータエラーとして 400（バリデーションエラー）ステータスが返ることを確認。
     */
    @Test
    void testInvalidZipCode_FormatError() {
        Response response = RestAssured.given()
                .param("zipcode", "") // 空文字を指定（形式不備）
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(200) // HTTPステータスは200（API設計的に常に200）
                .extract().response();

        // JSON上の status は 400（バリデーションエラー）
        assertThat(response.jsonPath().getInt("status"), is(400));
        // message フィールドにエラーメッセージが含まれていることを確認
        assertThat(response.jsonPath().getString("message"), containsString("必須パラメータが指定されていません。"));
    }
}
