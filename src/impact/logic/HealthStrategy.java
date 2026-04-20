package impact.logic;

/**
 * HealthStrategy クラス
 * [Strategy Pattern: Concrete Strategy]
 * 医療・保健分野における支援ロジックと画像パスの生成を担当します。
 * [UPDATE] getGlobalFact() を実装し、医療支援に関する豆知識を分離しました。
 */
public class HealthStrategy implements EmpowermentStrategy {

  /**
   * 分野名を返します。
   */
  @Override
  public String getSectorName() {
    return "Health Champion";
  }

  /**
   * 金額に応じた支援人数（保健ワーカーや恩恵を受ける人数）を計算します。
   */
  @Override
  public int calculateEntrepreneursHelped(double amount) {
    if (amount >= 100) return 20; // 地域の小規模クリニック全体を支援
    if (amount >= 50)  return 8;  // 複数の保健ワーカーを支援
    if (amount >= 15)  return 3;  // 特定の家族やワーカーを支援
    return 1;
  }

  /**
   * 金額に応じた具体的な投資ストーリーを返します。
   */
  @Override
  public String getInvestmentStory(double amount) {
    if (amount >= 100) {
      return "Advanced diagnostic tools and cold-chain storage for vaccines! 💉";
    }
    if (amount >= 50) {
      return "Comprehensive medical kits and pharmacy stock for 8 workers! 💊";
    }
    if (amount >= 15) {
      return "Essential first-aid supplies and sanitation tools for 3 households! 🧼";
    }
    return "Professional health and nutrition training for a local caregiver! 📖";
  }

  /**
   * [NEW] 医療・保健に関する豆知識（Global Fact）を取得します。
   */
  @Override
  public String getGlobalFact(double amount) {
    if (amount >= 100) {
      return "Strengthening local primary health care is the most cost-effective way to save lives.";
    }
    if (amount >= 50) {
      return "Community health workers are the first line of defense against preventable diseases.";
    }
    return "Simple interventions like clean water and basic hygiene can prevent 60% of child deaths.";
  }

  /**
   * [IMAGE LOGIC] 金額に応じた画像パスを生成します。
   * 用意されたファイル名 (Microfinance_100.jpg 等) に合わせました。
   */
  @Override
  public String getImagePath(double amount) {
    return "/images/Microfinance_15.jpg";
  }
}