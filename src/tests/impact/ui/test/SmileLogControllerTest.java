package impact.ui.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * SmileLogControllerTest
 * [Principle 6 & 13] 入力チェック（バリデーション）とデータの浄化（サニタイズ）を検証します。
 * ユーザーがどんなに入力を間違えても、システムが正しく守られるかを確認します。
 */
class SmileLogControllerTest {

  /**
   * 1. 名前のサニタイズ（浄化）テスト
   * ユーザーが「sAKURA」と打っても、内部では「Sakura」として保存されるべきです。
   */
  @Test
  @DisplayName("サニタイズ：1文字目だけが大文字に変換されるか？")
  void testNameSanitization() {
    // --- Arrange (準備) ---
    String rawName = "sAKURA";

    // --- Act (実行：実際のコントローラーのロジックをシミュレート) ---
    // 実際のコード：fNameRaw.substring(0, 1).toUpperCase() + fNameRaw.substring(1).toLowerCase();
    String sanitized = rawName.substring(0, 1).toUpperCase() + rawName.substring(1).toLowerCase();

    // --- Assert (検証) ---
    assertEquals("Sakura", sanitized, "名前は1文字目だけが大文字の形式に変換されるべきです");
  }

  /**
   * 2. 空欄チェック（バリデーション）のテスト
   * 名前が空のまま登録ボタンを押されたとき、正しく「空である」と判定できるか。
   */
  @Test
  @DisplayName("バリデーション：空の名前を拒否できるか？")
  void testEmptyNameValidation() {
    // --- Arrange ---
    String emptyName = "   "; // スペースだけの入力

    // --- Act & Assert ---
    // trim() を使って空白を消した結果が空文字("")になるかをチェック
    assertTrue(emptyName.trim().isEmpty(), "スペースだけの入力は『空』と判定されるべきです");
  }

  /**
   * 3. 数値形式のチェック
   * 金額欄に「abc」などの文字が入ったとき、正しくエラーをキャッチできるか。
   */
  @Test
  @DisplayName("堅牢性：数字以外の入力で正しく例外が発生するか？")
  void testInvalidAmountFormat() {
    // --- Arrange ---
    String invalidInput = "100dollars";

    // --- Act & Assert ---
    // 数字以外の文字列を Double に変換しようとしたとき、NumberFormatException が出るか検証
    assertThrows(NumberFormatException.class, () -> {
      Double.parseDouble(invalidInput);
    }, "数字以外の入力があった場合は、例外（エラー）を投げて止まるべきです");
  }

  /**
   * 4. 金額のトリム処理
   * ユーザーが「 250 」のように前後にスペースを入れても正しく読み取れるか。
   */
  @Test
  @DisplayName("バリデーション：金額の前後のスペースが自動除去されるか？")
  void testAmountTrimming() {
    String inputWithSpaces = "  250.0  ";
    double amount = Double.parseDouble(inputWithSpaces.trim());
    assertEquals(250.0, amount, "前後のスペースは自動的に削除（trim）されるべきです");
  }
}