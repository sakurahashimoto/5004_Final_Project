package impact.logic;

/**
 * ApparelStrategy クラス
 * [UPDATE] getGlobalFact() を実装し、裁縫支援に関する豆知識を分離しました。
 */
public class ApparelStrategy implements EmpowermentStrategy {

  @Override
  public String getSectorName() {
    return "Apparel Partner";
  }

  @Override
  public int calculateEntrepreneursHelped(double amount) {
    if (amount >= 100) return 9;
    if (amount >= 50)  return 4;
    if (amount >= 15)  return 2;
    return 1;
  }

  @Override
  public String getInvestmentStory(double amount) {
    if (amount >= 100) {
      return "Industrial sewing machines for a multi-family business! 🧵";
    }
    if (amount >= 50) {
      return "Professional patterns and materials for 4 tailors! 👗";
    }
    return "High-quality fabric and tailoring materials for a startup! ✂️";
  }

  /**
   * [NEW] 裁縫・雇用に関する豆知識
   */
  @Override
  public String getGlobalFact(double amount) {
    if (amount >= 100) {
      return "When women work, they invest 90% of their income back into their families.";
    }
    return "Micro-loans for raw materials allow artisans to double their profit margin.";
  }

  @Override
  public String getImagePath(double amount) {
    return "/images/Microfinance_100.jpg";
  }
}