package impact.logic;

/**
 * AgricultureStrategy クラス
 * [Strategy Pattern: Concrete Strategy]
 * 農業分野における支援ロジックと画像パスの生成を担当します。
 */
public class AgricultureStrategy implements EmpowermentStrategy {

  /**
   * 分野名を返します。
   */
  @Override
  public String getSectorName() {
    return "Agricultural Partner";
  }

  /**
   * 金額に応じた支援人数（起業家数）を計算します。
   */
  @Override
  public int calculateEntrepreneursHelped(double amount) {
    if (amount >= 100) return 15;
    if (amount >= 50)  return 5;
    if (amount >= 15)  return 2;
    return 1;
  }

  /**
   * 金額に応じた具体的な投資ストーリーを返します。
   * [UPDATE] 豆知識（Fact）は getGlobalFact に分離したため、ここでは純粋な物語のみを返します。
   */
  @Override
  public String getInvestmentStory(double amount) {
    if (amount >= 100) {
      return "A large-scale irrigation system for a local cooperative! 🚜";
    }
    if (amount >= 50) {
      return "Professional farming tools for multiple families! 🧑‍🌾";
    }
    if (amount >= 15) {
      return "High-quality, climate-resilient seeds for a small farm! 🌱";
    }
    return "Essential training materials on sustainable farming! 📖";
  }

  /**
   * [NEW] 農業に関する豆知識（Global Fact）を取得します。
   */
  @Override
  public String getGlobalFact(double amount) {
    if (amount >= 100) {
      return "Irrigation can increase crop yields by up to 300% in dry regions.";
    }
    if (amount >= 50) {
      return "Access to basic tools is often the biggest barrier for small-scale farmers.";
    }
    return "Smallholder farmers produce about 80% of the food consumed in developing regions.";
  }

  /**
   * 金額に応じた画像パスを生成します。
   * スクリーンショットのファイル名（Microfinance_100.jpg 等）に合わせました。
   */
  @Override
  public String getImagePath(double amount) {
    return "/images/Microfinance_50.jpg";
  }
}