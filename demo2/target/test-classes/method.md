操作（ブラウザ内でのアクション）を行う際に使われるSeleniumの代表的なメソッド

クリック操作

element.click(); 要素をクリックします。たとえば、ボタンやリンクなどの操作に使われます。

テキスト入力

element.sendKeys("テキスト"); 入力フィールドに文字列を入力します。

クリア操作

element.clear(); 入力フィールドの内容をクリアします。

値の取得

element.getText(); 要素内のテキストを取得します。

属性の取得

element.getAttribute("属性名"); 要素の特定の属性値を取得します。

要素の確認

element.isDisplayed(); 要素が表示されているか確認します。

element.isEnabled(); 要素が有効かどうかをチェックします。

ウィンドウやタブの切り替え

driver.switchTo().window("windowName"); 別のウィンドウやタブに切り替えます。

アラートの処理

driver.switchTo().alert().accept(); アラートを承認（OKボタンをクリック）。

driver.switchTo().alert().dismiss(); アラートをキャンセル。


