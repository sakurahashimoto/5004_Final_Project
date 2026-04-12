package impact.logic;

import impact.core.AbstractSupport;

/**
 * 【役割：自立支援】
 * 寄付とは違い、起業や農業の道具を提供して「自立」を助けるクラスです。
 */
public class Microfinance extends AbstractSupport {

  public Microfinance(double amount, String donorName) {
    // 親クラス（AbstractSupport）に金額と名前を渡します
    super(amount, donorName);
  }

  /**
   * 📈 未来の笑顔（Smiles）を育てる人数を計算
   */
  @Override
  public int getPeopleHelped() {
    if (amount >= 100) return 5; // ミシンで家族5人の生活を支える
    if (amount >= 50)  return 8; // 共有農具で2家族（約8人）を支援
    if (amount >= 15)  return 3; // 種や肥料で学生グループを支援
    return 1;                    // 小さな起業家1人を支援
  }

  @Override
  public String getImpactMessage() {
    String project;
    if (amount >= 100) project = "A sewing machine for a family of 5! 🧵";
    else if (amount >= 50) project = "Farming tools for 2 families! 🧑‍🌾";
    else if (amount >= 15) project = "Seeds for a small farm! 🌱";
    else if (amount >= 5)  project = "Spice-growing startup materials! 🌶️";
    else project = "A business literacy workshop! 📈";

    // 支援者ではなく「Investor（投資家）」と呼ぶのがSakuraさんのこだわり！
    return "[Investor: " + donorName + "] Your $" + amount + " impact: " + project;
  }
}