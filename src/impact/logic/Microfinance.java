package impact.logic;

import impact.core.AbstractSupport;

/**
 * Microfinance クラス
 * [MVC: Model Layer]
 * 具体的な支援ロジックを「Strategy（戦略）」に委譲する、柔軟性の高い設計を採用しています。
 * [UPDATE] getGlobalFact() を実装し、戦略カセットから豆知識を取得するようにしました。
 */
public class Microfinance extends AbstractSupport {

  /**
   * 注入された具体的な支援戦略（農業、裁縫、医療など）を保持します。
   */
  private EmpowermentStrategy strategy;

  /**
   * コンストラクタ
   * [FIX] 引数を5つ（金額、名前、苗字、日付、戦略）に設定し、他クラスとの整合性を取っています。
   */
  public Microfinance(double amount, String firstName, String lastName, String date, EmpowermentStrategy strategy) {
    // 親クラス (AbstractSupport) へ共通データを渡します
    super(amount, firstName, lastName, "MICROFINANCE", date);
    this.strategy = strategy;
  }

  /**
   * [Override] 投資家としての称号（バッジ）を生成します。
   */
  @Override
  public String getImpactBadge() {
    int helped = getPeopleHelped();
    if (helped >= 15) return "🚀 Visionary Level";
    if (helped >= 5)  return "🤝 Core Partner";
    return "🌱 Seed Member";
  }

  /**
   * [Override] 投資分野に応じた役割名を返します（例: Agricultural Partner）。
   */
  @Override
  public String getRoleName() {
    return strategy.getSectorName();
  }

  /**
   * [Override] 具体的な支援内容の物語。
   * 詳細はセットされた戦略オブジェクト（strategy）に問い合わせます。
   */
  @Override
  protected String getSpecificImpact() {
    return strategy.getInvestmentStory(amount);
  }

  /**
   * [NEW/Override] 分野に関する豆知識（Fact）を返します。
   * ストラテジー（カセット）に処理を委譲（Delegation）します。
   */
  @Override
  public String getGlobalFact() {
    return strategy.getGlobalFact(amount);
  }

  /**
   * [Override] 支援人数を計算します。
   */
  @Override
  public int getPeopleHelped() {
    return strategy.calculateEntrepreneursHelped(amount);
  }

  /**
   * [Override] 画像パスを取得します。
   */
  @Override
  public String getImagePath() {
    return strategy.getImagePath(this.amount);
  }
}