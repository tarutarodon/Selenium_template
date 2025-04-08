* ExtensionContext
* Throwable
* TakesScreenshot
* IOException
* File
* FileUtils


📦 なぜ両方実装するの？
実は、handleTestExecutionException が呼ばれないケースや、他の拡張と競合してうまく動作しないケースもまれにあります。
そのため testFailed(...) を併用しておくことで、
スクリーンショットが確実に撮られる
JUnitの動作に応じた失敗検出の二重化
という効果があります。
========================================================================
要素	内容
メソッド	testFailed(ExtensionContext context, Throwable cause)
目的	JUnitが失敗を通知してきたときにスクリーンショットを撮る
なぜ必要？	handleTestExecutionException のバックアップ（フォールバック）として動作
違い	handleTestExecutionException は即時／testFailed は後処理的
メリット	保険的にスクリーンショット撮影が保証される構成になる

☑ handleTestExecutionException との違い
比較項目	    handleTestExecutionException	    testFailed
発火タイミング	例外がスローされた「直後」	           テスト失敗がJUnitにより「確定した後」
主な目的	    例外を捕捉して処理＋再スロー	       成果通知ベースの失敗処理
例外再スロー	必要（しないとテスト失敗にならない）	不要（JUnitがすでに失敗判定済）
安全性	        例外の原因が取れるが早期に呼ばれる	   フォールバック的に安全に呼ばれる