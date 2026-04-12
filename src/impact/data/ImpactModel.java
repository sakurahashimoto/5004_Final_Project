package impact.data;

import impact.core.AbstractSupport;
import impact.core.Supportable;
import impact.logic.Donation;
import impact.logic.Microfinance;
import java.util.*;
import java.io.*;

/**
 * 【役割：Model（記憶担当）】
 * アプリの全データを保持し、計算やファイルへの保存・読み込みを行います。
 */
public class ImpactModel {
  // 支援データを管理するリスト
  private final List<Supportable> supportList = new ArrayList<>();

  // --- データの追加と取得 ---

  public void addSupport(Supportable s) {
    supportList.add(s);
  }

  public List<Supportable> getAllSupports() {
    return Collections.unmodifiableList(supportList);
  }

  // --- 計算ロジック ---

  /**
   * 🌍 全支援の合計金額を計算します。
   * Controllerで「レッド（エラー）」が出ていた場合は、このメソッド名が一致しているか確認してください。
   */
  public double getTotalAmount() {
    double total = 0;
    for (Supportable s : supportList) {
      total += s.getAmount();
    }
    return total;
  }

  // 👤 名前で検索して合計金額を出す（Sakuraさんこだわり機能）
  public double getTotalAmountByName(String name) {
    if (name == null || name.trim().isEmpty()) return 0.0;
    double total = 0;
    String searchTarget = name.trim().toLowerCase();

    for (Supportable s : supportList) {
      if (s.getImpactMessage().toLowerCase().contains(searchTarget)) {
        total += s.getAmount();
      }
    }
    return total;
  }

  // --- ファイルの保存と読み込み ---

  /**
   * 💾 データをCSVファイルに書き出します。
   * インターフェースの強化により、保存する項目もスッキリしました。
   */
  public void saveToCSV(String filename) throws IOException {
    try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
      for (Supportable s : supportList) {
        // 保存する時に、DonationかMicrofinanceかを判別するためのラベルを作ります
        String type = (s instanceof Donation) ? "DONATION" : "MICROFINANCE";

        // 金額, 種類, メッセージ の順で保存
        pw.println(s.getAmount() + "," + type + "," + s.getImpactMessage());
      }
    }
  }

  /**
   * 📂 保存されたCSVファイルからデータを復元します。
   */
  public void loadFromCSV(String filename) throws IOException {
    File f = new File(filename);
    if (!f.exists()) return;

    supportList.clear();

    try (BufferedReader br = new BufferedReader(new FileReader(f))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] d = line.split(",");
        if (d.length < 3) continue;

        double amt = Double.parseDouble(d[0]);
        String type = d[1];
        String msg = d[2];

        // メッセージから名前を切り出すロジック（[Donor: Sakura] の形を想定）
        String name = "User";
        if (msg.contains(": ") && msg.contains("]")) {
          name = msg.substring(msg.indexOf(": ") + 2, msg.indexOf("]"));
        }

        // 正しい型で再生成してリストに追加
        if (type.equals("DONATION")) {
          addSupport(new Donation(amt, name));
        } else {
          addSupport(new Microfinance(amt, name));
        }
      }
    }
  }
}