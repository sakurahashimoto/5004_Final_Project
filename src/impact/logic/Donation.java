package impact.logic;
import impact.core.AbstractSupport;
import impact.core.AbstractSupport;

/**
 * 【文法：継承 (extends)】
 * 土台を引き継ぎ、「寄付」ならではの計算ロジックを追加します。
 */
public class Donation extends AbstractSupport {

  public Donation(double amount, String donorName) {
    // 土台（親クラス）に金額と名前を渡します
    super(amount, donorName);
  }

  /**
   * 【誠実な指標】
   * 概算ではなく、金額に応じて「具体的に何人の子供に届くか」を計算します。
   */
  @Override
  public int getPeopleHelped() {
    if (amount >= 250) return 40; // 教室1つ分
    if (amount >= 50)  return 10; // 1週間分の給食
    if (amount >= 10)  return 3;  // ワクチン
    if (amount >= 5)   return 2;  // 衛生キット
    return 1;                     // 鉛筆
  }

  @Override
  public String getImpactMessage() {
    String gift;
    if (amount >= 250) gift = "Supplies for a classroom of 40! 🏫";
    else if (amount >= 50) gift = "Meals for 10 children for a week! 🍱";
    else if (amount >= 10) gift = "Vaccines for 3 children! 💉";
    else if (amount >= 5)  gift = "Hygiene kits for 2 students! 🧼";
    else gift = "A high-quality pencil for a student! ✏️";

    return "[Donor: " + donorName + "] Your $" + amount + " impact: " + gift;
  }
}