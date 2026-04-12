package impact.ui;

import impact.data.ImpactModel;
import impact.core.Supportable;
import impact.logic.Donation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import impact.logic.Microfinance;

public class ImpactController {
  private ImpactModel model;
  private ImpactView view;

  public ImpactController(ImpactModel model, ImpactView view) {
    this.model = model;
    this.view = view;
    try {
      model.loadFromCSV("impact_data.csv");
    } catch (Exception e) {
      System.out.println("Ready to start! 🌸");
    }
    setupListeners();
    refreshUI();
  }

  private void setupListeners() {
    view.getStartButton().addActionListener(e -> view.switchToInput());
    view.getCreateButton().addActionListener(e -> addSupportFromView());
    view.getSearchField().addActionListener(e -> refreshUI());
  }

  public void addSupportFromView() {
    try {
      String name = view.getNameText().trim();
      String amtStr = view.getAmountText().trim();
      if (name.isEmpty() || amtStr.isEmpty()) return;

      double amount = Double.parseDouble(amtStr);
      String type = view.getSelectedType();

      // 支援オブジェクトの作成
      impact.core.AbstractSupport s = type.equals("DONATION")
          ? new Donation(amount, name)
          : new Microfinance(amount, name);

      model.addSupport(s);
      model.saveToCSV("impact_data.csv");

      view.showRandomImage(s.getImpactMessage()); // 写真とメッセージを表示
      view.getSearchField().setText(name); // 検索窓に名前を入れて自動更新
      refreshUI();
      view.clearInputs();
    } catch (Exception ex) {
      System.out.println("Error adding support: " + ex.getMessage());
    }
  }

  private void refreshUI() {
    double totalAmt = model.getTotalAmount();
    int totalPeople = 0;
    for (Supportable s : model.getAllSupports()) {
      totalPeople += s.getPeopleHelped();
    }
    view.setGlobalTotalText(String.format("🌍 Total: $%.2f | 🧒 %d Lives Impacted", totalAmt, totalPeople));

    String searchName = view.getSearchText().trim();
    if (searchName.isEmpty()) return;

    // 🌸 履歴を全部溜めるための「魔法の箱（StringBuilder）」
    // 以前の課題で顧客リストを全部つなげたのと同じやり方です！
    StringBuilder allHistory = new StringBuilder();

    for (Supportable s : model.getAllSupports()) {
      if (s.getImpactMessage().toLowerCase().contains(searchName.toLowerCase())) {

        double a = s.getAmount();
        String photoPath = "resources/default.jpg";
        String displayMessage = s.getImpactMessage();

        // 250ドル以上の寄付レシート処理
        if (s instanceof Donation && a >= 250) {
          photoPath = "images/Donation_250.jpg";
          try {
            String home = System.getProperty("user.home");
            Path path = Paths.get(home, "Desktop", "donation_receipt.txt");
            if (Files.exists(path)) {
              String template = Files.readString(path);

              LocalDate now = LocalDate.now();
              String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

              displayMessage = template
                  .replace("[NAME]", searchName)
                  .replace("[AMOUNT]", String.format("%.2f", a))
                  .replace("[DATE]", formattedDate);
            } else {
              displayMessage = "🌸 Thank you for your generous donation of $" + String.format("%.2f", a) + "!\n" + s.getImpactMessage();
            }
          } catch (IOException e) {
            displayMessage = "⚠️ [Error] Could not read receipt file.\n" + s.getImpactMessage();
          }
        }
        // 写真の条件分岐（ここはそのまま）
        else if (s instanceof Donation) {
          if (a >= 250) photoPath = "images/Donation_250.jpg";
          else if (a >= 50) photoPath = "images/Donaiton_50.jpg";
          else if (a >= 10) photoPath = "images/Donation_10.jpg";
          else photoPath = "images/Donation_lessthanfive.jpg";
        }
        else if (s instanceof Microfinance) {
          if (a >= 100) photoPath = "images/Microfinance_100.jpg";
          else if (a >= 50) photoPath = "images/MIcrofinance_50.jpg";
          else if (a >= 15) photoPath = "images/Microfinance_15.jpg"; // アイコン変えたところ！
          else photoPath = "images/Microfinance_5.jpg";
        }

        // ① メイン画面（写真とメッセージ）を更新
        view.setStoryContent("", photoPath);

        // ② 見つかったメッセージを履歴の箱にどんどん追加していく
        allHistory.append(displayMessage).append("\n------------------\n");
      }
    }

    // 🌸 ループが終わった後に、溜まった履歴を全部表示する！
    view.setHistoryText(allHistory.toString());
  }}