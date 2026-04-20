package impact.ui.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import impact.data.ImpactModel;
import impact.logic.Donation;
import impact.logic.Microfinance;
import impact.logic.AgricultureStrategy;
import impact.logic.ApparelStrategy;
import impact.logic.HealthStrategy;

/**
 * SmileLogLogicTest
 * JUnit: アプリケーションの計算ロジック、バッジ生成、および戦略パターンの正確性を検証します。
 * * ユニットテストは「Arrange (準備)」「Act (実行)」「Assert (検証)」の3ステップで構成されます。
 */
class SmileLogLogicTest {

  private ImpactModel model;

  /**
   * 各テストメソッドが実行される前に、毎回呼ばれる準備メソッドです。
   * テストごとにデータが混ざらないよう、常に新しいモデルをインスタンス化します。
   */
  @BeforeEach
  void setUp() {
    model = new ImpactModel();
  }

  @Test
  @DisplayName("寄付：累計額に応じたバッジ昇格ロジックの検証")
  void testDonationBadgeTiers() {
    // Arrange & Act
    Donation d = new Donation(0.0, "Sakura", "Hashimoto", "2026-04-18");

    // Assert: 50ドル未満
    d.setCumulativeTotal(49.0);
    assertTrue(d.getImpactBadge().contains("Smile"), "50ドル未満はSmile Partnerであるべきです");

    // Assert: 100ドル
    d.setCumulativeTotal(100.0);
    assertTrue(d.getImpactBadge().contains("Silver"), "100ドルはSilver Supporterであるべきです");

    // Assert: 500ドル
    d.setCumulativeTotal(500.0);
    assertTrue(d.getImpactBadge().contains("Platinum"), "500ドルはPlatinum Eliteであるべきです");

    // Assert: 1000ドル
    d.setCumulativeTotal(1000.0);
    assertTrue(d.getImpactBadge().contains("Hall of Fame"), "1000ドルは殿堂入り（Hall of Fame）であるべきです");
  }

  @Test
  @DisplayName("農業戦略：金額に対する支援人数の計算ロジック検証")
  void testAgricultureStrategyCalculation() {
    AgricultureStrategy agri = new AgricultureStrategy();

    // 農業支援の各ランクの計算が正しいか検証
    assertEquals(15, agri.calculateEntrepreneursHelped(100.0), "100ドルで15人の起業家を支援できるはずです");
    assertEquals(5, agri.calculateEntrepreneursHelped(50.0), "50ドルで5人の起業家を支援できるはずです");
    assertEquals(1, agri.calculateEntrepreneursHelped(1.0), "少額でも最低1人は支援されるはずです");
  }

  @Test
  @DisplayName("裁縫戦略：最新の仕様（金額に関わらず固定の画像パス）の検証")
  void testApparelStrategyImagePath() {
    ApparelStrategy apparel = new ApparelStrategy();

    // 最新の修正：どの金額でも同じパス（固定画像）を返すことを確認
    String expectedPath = "/images/Microfinance_100.jpg";
    assertEquals(expectedPath, apparel.getImagePath(10.0), "10ドルでも固定パスを返すべきです");
    assertEquals(expectedPath, apparel.getImagePath(500.0), "500ドルでも固定パスを返すべきです");
  }

  @Test
  @DisplayName("モデル：全体合計金額と目標達成率（%）の計算検証")
  void testModelGlobalCalculations() {
    // Arrange
    model.addSupport(new Donation(100.0, "UserA", "Test", "2026-04-18"));
    model.addSupport(new Microfinance(200.0, "UserB", "Test", "2026-04-18", new HealthStrategy()));

    // Act & Assert
    // 合計が 300.0 になっているか
    assertEquals(300.0, model.getTotalAmount(), "合計金額の計算に誤りがあります");

    // 目標10,000ドルに対して300ドルは 3.0% か（浮動小数点の比較には誤差 0.01 を許容）
    assertEquals(3.0, model.getCompletionPercentage(), 0.01, "目標達成率（%）の計算に誤りがあります");
  }

  @Test
  @DisplayName("境界値：目標達成率が100%を超えないことの検証")
  void testBoundaryGoalCompletion() {
    // 目標10,000ドルを大幅に超える寄付を追加
    model.addSupport(new Donation(20000.0, "VHN", "Donor", "2026-04-18"));

    // 200%ではなく、100.0%でキャップがかかっているか
    assertEquals(100.0, model.getCompletionPercentage(), "進捗率は最大100%で制限されるべきです");
  }

  @Test
  @DisplayName("名前の正規化：姓名の結合が期待通りに行われるかの検証")
  void testFullNameConcatenation() {
    Donation d = new Donation(10.0, "Sakura", "Hashimoto", "2026-04-18");
    assertEquals("Sakura Hashimoto", d.getFullName(), "姓名が正しいフォーマットで結合されていません");
  }
}