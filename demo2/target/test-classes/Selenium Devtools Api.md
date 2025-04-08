# Selenium DevTools を使用した API レスポンステスト

## 1. ChromeDriver の起動
- `new ChromeDriver()` でブラウザを開く。
- Selenium WebDriver を使って Chrome を制御できるようにする。

```java
WebDriver driver = new ChromeDriver();
```

## 2. DevTools のセッション作成
- `devTools.createSession()` を実行し、DevTools との通信セッションを作成。
- これにより、ブラウザのネットワークイベントを監視できるようになる。

```java
DevTools devTools = ((ChromeDriver) driver).getDevTools();
devTools.createSession();
```

## 3. ネットワークモニタリングの有効化（詳しく解説）
- `Network.enable()` を使って、**ネットワークリクエストやレスポンスを監視可能にする**。
- `Optional.empty()` を渡すことで、デフォルトの設定で有効化する。
- これを実行しないと、API のリクエストやレスポンスを取得できない。

```java
devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
```

### **なぜ必要なのか？**
通常、Selenium は **Web ページの UI 要素の操作** に特化しており、**バックグラウンドで発生する API 通信の情報を取得する機能はない**。
`Network.enable()` を使うことで、ブラウザのネットワーク通信を DevTools 経由でキャプチャし、API のリクエストやレスポンスを取得できるようになる。

---

## 4. レスポンスの監視（リスナー追加）（詳しく解説）
- `devTools.addListener(Network.responseReceived(), callback)` を使って **ネットワーク通信のレスポンスを取得するリスナーを設定**。
- `ResponseReceived` オブジェクトから、URL とステータスコードを取得。
- **特定の API（例: `jsonplaceholder.typicode.com/posts/1`）のレスポンスのみを検証**。

```java
devTools.addListener(Network.responseReceived(), response -> {
    ResponseReceived res = response;
    String url = res.getResponse().getUrl();
    int statusCode = res.getResponse().getStatus();
    System.out.println("URL: " + url + ", ステータスコード: " + statusCode);

    if (url.contains("jsonplaceholder.typicode.com/posts/1")) {
        assertEquals(200, statusCode, "APIのレスポンスコードが200であることを確認");
    }
});
```

### **なぜリスナーが必要なのか？**
DevTools を利用することで **非同期的にブラウザが受け取るレスポンスをリアルタイムで検出** できる。
通常の Selenium の `driver.get()` では、ページの HTML 要素のみ取得できるが、
リスナーを設定することで **API の通信をキャッチしてレスポンスを取得できる**。

---

## 5. API のリクエストを発生させる（詳しく解説）
- `driver.get("https://jsonplaceholder.typicode.com/posts/1")` を実行。
- これにより、ブラウザが `jsonplaceholder.typicode.com/posts/1` にアクセスし、API リクエストを発生させる。

```java
driver.get("https://jsonplaceholder.typicode.com/posts/1");
```

### **この処理の役割**
- 実際のブラウザを操作して API を呼び出すことで、**ネットワークリクエストが発生** し、それを DevTools で監視する。
- Selenium では通常、API リクエストを直接送信できないが、**ブラウザのページ遷移を利用することで API をテストできる**。

---

## 6. ステータスコードの検証（詳しく解説）
- 取得したレスポンスの **HTTP ステータスコード** が `200` であることを `assertEquals` でチェック。
- `assertEquals(200, statusCode, "APIのレスポンスコードが200であることを確認");`

### **HTTP ステータスコードとは？**
| ステータスコード | 意味 |
|-----------------|------|
| 200 | 成功（OK） |
| 400 | クライアントエラー（Bad Request） |
| 404 | ページが見つからない（Not Found） |
| 500 | サーバーエラー（Internal Server Error） |

このテストでは、API が **正常に動作しているか（200 が返ってくるか）を確認** している。

---

## 7. ブラウザを閉じる
- `driver.quit()` を実行し、ブラウザを閉じる。
- リソースの解放を行い、メモリを無駄にしないようにする。

```java
driver.quit();
```

---

## **まとめ**
| **ステップ** | **処理内容** |
|-------------|-------------|
| 1. ChromeDriver の起動 | Selenium でブラウザを開く |
| 2. DevTools のセッション作成 | ネットワークイベントを取得できるようにする |
| 3. ネットワークモニタリングの有効化 | `Network.enable()` で API の通信を監視可能にする |
| 4. レスポンスの監視（リスナー追加） | `Network.responseReceived()` を使ってレスポンスを取得 |
| 5. API のリクエストを発生 | `driver.get()` で API を呼び出す |
| 6. ステータスコードの検証 | `assertEquals(200, statusCode)` で 200 ステータスを確認 |
| 7. ブラウザを閉じる | `driver.quit()` で終了処理 |

この手順を応用すれば、**UI の自動テストと API のレスポンスチェックを組み合わせたテストが可能になります！** 🚀

