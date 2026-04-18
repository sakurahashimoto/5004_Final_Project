package impact.data;

import impact.core.Supportable;
import impact.logic.*;
import java.util.*;
import java.io.*;

/**
 * ImpactModel クラス
 * [MVC: Model]
 * 履歴生成ロジックに「Amount（金額）」列を追加しました。
 */
public class ImpactModel {
  private final List<Supportable> supportList = new ArrayList<>();
  private final double GOAL_AMOUNT = 10000.0;

  public void addSupport(Supportable s) { supportList.add(s); }

  public double calculateCumulativeTotal(String fullName) {
    return supportList.stream().filter(s -> s.getFullName().equalsIgnoreCase(fullName.trim())).mapToDouble(Supportable::getAmount).sum();
  }

  public double getTotalAmount() { return supportList.stream().mapToDouble(Supportable::getAmount).sum(); }

  public double getCompletionPercentage() { return Math.min((getTotalAmount() / GOAL_AMOUNT) * 100, 100.0); }

  /**
   * [UPDATE] 履歴に「Amount」列を追加しました。
   * String.format を使用して、等幅フォントで綺麗に並ぶように整形します。
   */
  public String getConciseHistory(String searchName) {
    if (searchName == null || searchName.trim().isEmpty()) return "Search a donor legacy.";

    StringBuilder sb = new StringBuilder();
    // ヘッダーに Amount を追加
    sb.append(String.format("%-12s | %-12s | %-20s | %s\n", "Date", "Amount", "Role", "Impact Status"));
    sb.append("--------------------------------------------------------------------------\n");

    boolean found = false;
    for (Supportable s : supportList) {
      if (s.getFullName().toLowerCase().contains(searchName.toLowerCase().trim())) {
        found = true;
        // データ行に金額 ($) を追加
        sb.append(String.format("%-12s | $%-11.2f | %-20s | %d lives helped\n",
            s.getDate(),
            s.getAmount(),
            s.getRoleName(),
            s.getPeopleHelped()));
      }
    }
    return found ? sb.toString() : "No history found.";
  }

  public void saveToCSV(String filename) {
    File file = new File(filename);
    try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
      if (!file.exists() || file.length() == 0) out.println("Type,FirstName,LastName,Amount,Date");
      if (!supportList.isEmpty()) {
        Supportable s = supportList.get(supportList.size() - 1);
        String[] names = s.getFullName().split(" ");
        out.println(String.format("%s,%s,%s,%.2f,%s", s.getType(), s.getFirstName(), (names.length > 1 ? names[1] : ""), s.getAmount(), s.getDate()));
      }
    } catch (IOException e) { e.printStackTrace(); }
  }

  public void loadFromCSV(String filename) throws IOException {
    File f = new File(filename);
    if (!f.exists()) return;
    supportList.clear();
    try (BufferedReader br = new BufferedReader(new FileReader(f))) {
      String line; boolean header = true;
      while ((line = br.readLine()) != null) {
        if (header) { header = false; continue; }
        String[] d = line.split(",");
        if (d.length < 5) continue;
        double amt = Double.parseDouble(d[3]);
        if (d[0].equals("DONATION")) addSupport(new Donation(amt, d[1], d[2], d[4]));
        else addSupport(new Microfinance(amt, d[1], d[2], d[4], new AgricultureStrategy()));
      }
    }
  }
}