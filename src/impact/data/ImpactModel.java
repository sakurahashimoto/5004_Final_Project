package impact.data;

import impact.core.Supportable;
import impact.logic.*;
import java.util.*;
import java.io.*;

/**
 * ImpactModel
 * [MVC: Model]
 * Manages the collection of support records and provides analytical data like totals and history.
 */
public class ImpactModel {
  private final List<Supportable> supportList = new ArrayList<>();
  private final double GOAL_AMOUNT = 10000.0;

  public void addSupport(Supportable s) { supportList.add(s); }

  /** Sums all contributions from a specific donor by name. */
  public double calculateCumulativeTotal(String fullName) {
    return supportList.stream()
        .filter(s -> s.getFullName().equalsIgnoreCase(fullName.trim()))
        .mapToDouble(Supportable::getAmount)
        .sum();
  }

  /** @return Total amount of support collected across all entries. */
  public double getTotalAmount() {
    return supportList.stream().mapToDouble(Supportable::getAmount).sum();
  }

  /** @return Progress toward the goal as a percentage (0.0 to 100.0). */
  public double getCompletionPercentage() {
    return Math.min((getTotalAmount() / GOAL_AMOUNT) * 100, 100.0);
  }

  /**
   * Generates a formatted table string for a specific donor's history.
   */
  public String getConciseHistory(String searchName) {
    if (searchName == null || searchName.trim().isEmpty()) return "Search a donor legacy.";

    StringBuilder sb = new StringBuilder();
    sb.append(String.format("%-12s | %-12s | %-20s | %s\n", "Date", "Amount", "Role", "Impact Status"));
    sb.append("--------------------------------------------------------------------------\n");

    boolean found = false;
    for (Supportable s : supportList) {
      if (s.getFullName().toLowerCase().contains(searchName.toLowerCase().trim())) {
        found = true;
        sb.append(String.format("%-12s | $%-11.2f | %-20s | %d lives helped\n",
            s.getDate(),
            s.getAmount(),
            s.getRoleName(),
            s.getPeopleHelped()));
      }
    }
    return found ? sb.toString() : "No history found.";
  }

  /** Appends the latest support entry to a CSV file for persistent storage. */
  public void saveToCSV(String filename) {
    File file = new File(filename);
    try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
      if (!file.exists() || file.length() == 0) out.println("Type,FirstName,LastName,Amount,Date");

      if (!supportList.isEmpty()) {
        Supportable s = supportList.get(supportList.size() - 1);
        String[] names = s.getFullName().split(" ");
        out.println(String.format("%s,%s,%s,%.2f,%s",
            s.getType(),
            s.getFirstName(),
            (names.length > 1 ? names[1] : ""),
            s.getAmount(),
            s.getDate()));
      }
    } catch (IOException e) { e.printStackTrace(); }
  }

  /** Loads and reconstructs support objects from a CSV file. */
  public void loadFromCSV(String filename) throws IOException {
    File f = new File(filename);
    if (!f.exists()) return;
    supportList.clear();

    try (BufferedReader br = new BufferedReader(new FileReader(f))) {
      String line;
      boolean isHeader = true;
      while ((line = br.readLine()) != null) {
        if (isHeader) { isHeader = false; continue; }
        String[] d = line.split(",");
        if (d.length < 5) continue;

        double amt = Double.parseDouble(d[3]);
        if (d[0].equals("DONATION")) {
          addSupport(new Donation(amt, d[1], d[2], d[4]));
        } else {
          addSupport(new Microfinance(amt, d[1], d[2], d[4], new AgricultureStrategy()));
        }
      }
    }
  }
}