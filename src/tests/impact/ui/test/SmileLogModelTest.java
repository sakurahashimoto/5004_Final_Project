package impact.ui.test;

/**
 * 【超重要：インポート文】
 * これらは「テスト専用の道具箱」から必要な道具を取り出す作業です。
 * static import を使うことで、Assertions.assertEquals() と書かずに
 * 直接 assertEquals() と書けるようになります。
 */
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import impact.data.ImpactModel;
import impact.logic.Donation;
import java.io.File;
import java.io.IOException;

/**
 * SmileLogModelTest クラス
 * * テストの基本構成は「準備 (Arrange)」「実行 (Act)」「検証 (Assert)」の3段階です。
 */
class SmileLogModelTest {

  // 1. 【フィールド（メンバー変数）】
  // テスト対象となる「モデル」と、テスト用の「仮のCSVファイル名」を準備します。
  private ImpactModel model;
  private final String TEST_CSV = "junit_test_data.csv";

  /**
   * 2. 【@BeforeEach：テスト前の儀式】
   * 「各テストの直前」に必ず実行されます。
   * テストごとに新しい model を作り直すのは、前のテストのデータが
   * 残っていると、計算が狂って正しく判定できないからです。
   */
  @BeforeEach
  void setUp() {
    model = new ImpactModel();
    // もし古いテスト用ファイルが残っていたら、一旦消して真っさらにします。
    File file = new File(TEST_CSV);
    if (file.exists()) {
      file.delete();
    }
  }

  /**
   * 3. 【@AfterEach：テスト後の後片付け】
   * テストが終わるたびに呼ばれます。
   * Sakuraさんのパソコンに「テスト用のゴミ（CSV）」を残さないためのマナーです。
   */
  @AfterEach
  void tearDown() {
    File file = new File(TEST_CSV);
    if (file.exists()) {
      file.delete();
    }
  }

  /**
   * 4. 【@Test：実際のテストケース】
   */
  @Test
  @DisplayName("Model：データを入れたら合計金額が正しく増えるか？")
  void testDataAddition() {
    // --- A: 準備 (Arrange) ---
    // 100ドルの寄付データを作ります。
    Donation d = new Donation(100.0, "Sakura", "Hashimoto", "2026-04-18");

    // --- B: 実行 (Act) ---
    // モデルにデータを登録します。
    model.addSupport(d);

    // --- C: 検証 (Assert) ---
    // 「合計金額は100.0のはずだ！」と宣言します。
    // 第1引数：期待する値 (100.0)
    // 第2引数：実際の計算結果 (model.getTotalAmount())
    // 第3引数：もし失敗した時に人間が見るエラーメッセージ
    assertEquals(100.0, model.getTotalAmount(), "合計金額が 100.0 になっている必要があります");
  }

  @Test
  @DisplayName("検索：名前で検索して履歴が見つかるか？（大文字小文字の違いも無視できるか）")
  void testSearchHistory() {
    // 準備：Sakuraさんの名前で登録
    model.addSupport(new Donation(50.0, "Sakura", "Hashimoto", "2026-04-18"));

    // 実行：あえて小文字の "sakura" で検索してみる
    String history = model.getConciseHistory("sakura");

    // 検証：history（文字列）の中に "Sakura Hashimoto" という文字が含まれている（contains）か？
    // assertTrue は「カッコの中が true（正解）であること」をチェックします。
    assertTrue(history.contains("Sakura Hashimoto"), "履歴に正しい名前が含まれている必要があります");
  }

  @Test
  @DisplayName("CSV：保存したデータを読み直しても、金額が壊れていないか？")
  void testCSVLogic() throws IOException {
    // 準備：250ドルのデータを1件入れる
    model.addSupport(new Donation(250.0, "CSV_User", "Test", "2026-04-18"));

    // 実行1：ファイルに保存する
    model.saveToCSV(TEST_CSV);

    // 実行2：新しい「空のモデル」を作り、そこへファイルを読み込む（復元作業）
    ImpactModel newModel = new ImpactModel();
    newModel.loadFromCSV(TEST_CSV);

    // 検証：復元されたモデルの合計金額が、元の 250.0 と同じか？
    assertEquals(250.0, newModel.getTotalAmount(), "CSVから読み込んだ後も金額が維持されるべきです");
  }

  @Test
  @DisplayName("境界値：目標金額を超えても、進捗率は100%で止まるか？")
  void testGoalCap() {
    // 準備：目標1万ドルに対して、1万5千ドルという巨額の寄付を入れる
    model.addSupport(new Donation(15000.0, "Rich", "Donor", "2026-04-18"));

    // 検証：150% ではなく、上限の 100.0% になっているか？
    assertEquals(100.0, model.getCompletionPercentage(), "進捗率は最大100%でなければなりません");
  }

  @Test
  @DisplayName("堅牢性：存在しないファイルを読み込もうとしてもアプリが落ちないか？")
  void testErrorHandling() {
    // assertDoesNotThrow は、「この中の処理を実行してもエラー（例外）が出ないこと」をテストします。
    // () -> { ... } という書き方は「この処理を今すぐやってみて」という合言葉です。
    assertDoesNotThrow(() -> {
      model.loadFromCSV("存在しないファイル.csv");
    }, "存在しないファイルを読み込んでもクラッシュ（エラー停止）してはいけません");
  }
}