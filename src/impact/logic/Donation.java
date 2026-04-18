package impact.logic;

import impact.core.AbstractSupport;

/**
 * Donation クラス
 * [FIX] getGlobalFact() メソッドを実装し、抽象メソッド未実装のエラーを解消しました。
 */
public class Donation extends AbstractSupport {

  private double cumulativeTotal;

  public Donation(double amount, String firstName, String lastName, String date) {
    super(amount, firstName, lastName, "DONATION", date);
    this.cumulativeTotal = amount;
  }

  public void setCumulativeTotal(double total) {
    this.cumulativeTotal = total;
  }

  @Override
  public String getImpactBadge() {
    if (cumulativeTotal >= 1000) return "💎 Hall of Fame";
    if (cumulativeTotal >= 500)  return "🥇 Platinum Elite";
    if (cumulativeTotal >= 250)  return "🥈 Gold Tier";
    if (cumulativeTotal >= 100)  return "🥉 Silver Supporter";
    return "🌸 Smile Partner";
  }

  @Override public String getRoleName() { return "Education Advocate"; }

  /**
   * [NEW] 寄付に関する豆知識（Global Fact）を返します。
   * インターフェースの要件を満たすために追加しました。
   */
  @Override
  public String getGlobalFact() {
    if (amount >= 250) return "Over 250 million children and youth are currently out of school globally.";
    if (amount >= 50)  return "Proper nutrition is proven to increase school attendance rates by up to 20%.";
    if (amount >= 10)  return "Basic healthcare and vaccines prevent 4 million child deaths annually.";
    return "Early childhood education provides a $16 return for every $1 invested.";
  }

  /**
   * 金額としきい値を、用意した画像ファイル名に合わせるための補助メソッド。
   */
  private String getImpactLevel() {
    if (amount >= 250) return "250";
    if (amount >= 50)  return "50";
    if (amount >= 10)  return "10";
    if (amount >= 5)   return "5";
    return "lessthanfive";
  }

  @Override
  protected String getSpecificImpact() {
    String level = getImpactLevel();
    switch (level) {
      case "250": return "Supplies for a whole classroom of 40 students! 🏫";
      case "50":  return "Nutritious school meals for 10 children for a week! 🍱";
      case "10":  return "Essential vaccines for 3 children! 💉";
      case "5":   return "Two hygiene kits for students! 🧼";
      default:    return "High-quality learning tools for a child! ✏️";
    }
  }

  @Override
  public int getPeopleHelped() {
    String level = getImpactLevel();
    switch (level) {
      case "250": return 40;
      case "50":  return 10;
      case "10":  return 3;
      case "5":   return 2;
      default:    return 1;
    }
  }

  @Override
  public String getImagePath() {
    return "images/Donation_" + getImpactLevel() + ".jpg";
  }

  public boolean isTaxDeductible() { return amount >= 250.0; }
}