package impact.ui;

import impact.data.ImpactModel;
import impact.core.Supportable;
import impact.logic.*;
import java.nio.file.*;
import java.time.LocalDate;
import javax.swing.JOptionPane;

/**
 * ImpactController クラス
 * [MVC: Controller]
 * UIからの入力を受け取り、モデルの更新とビューの表示制御を同期させます。
 * [UPDATE] デスクトップのテンプレートファイルからレシートを生成する機能を復元しました。
 */
public class ImpactController {
  private final ImpactModel model;
  private final ImpactView view;
  private Supportable lastSupport;

  public ImpactController(ImpactModel model, ImpactView view) {
    this.model = model;
    this.view = view;
    setupListeners();
    refreshUI();
  }

  /**
   * 各コンポーネントにアクションリスナーを登録します。
   */
  private void setupListeners() {
    view.getStartButton().addActionListener(e -> view.switchToInput());
    view.getNavMyPageButton().addActionListener(e -> view.switchToMyPage());
    view.getBackToDonateButton().addActionListener(e -> view.switchToInput());

    // 支援登録ボタン
    view.getCreateButton().addActionListener(e -> handleSubmission());

    // マイページ側の検索バー（エンターキーで検索）
    view.getSearchField().addActionListener(e -> refreshUI());

    // 書類表示ボタン
    view.getViewDocumentButton().addActionListener(e -> {
      if (lastSupport == null) return;
      if (lastSupport instanceof Donation) {
        view.showDocumentModal("Official Tax Receipt", generateReceipt(lastSupport));
      } else {
        view.showDocumentModal("Partnership Certificate", generateCertificate(lastSupport));
      }
    });
  }

  /**
   * 支援データのバリデーション、登録、およびビューの更新を行います。
   */
  private void handleSubmission() {
    try {
      String fNameRaw = view.getFirstNameText().trim();
      String lNameRaw = view.getLastNameText().trim();
      String amountStr = view.getAmountText().trim();

      // --- 🌸 【バリデーション：名前のチェック】 🌸 ---
      // || (OR) を使い、姓名どちらかが空ならエラーを出します。
      if (fNameRaw.isEmpty() || lNameRaw.isEmpty()) {
        notifyError("Please enter both First Name and Last Name.");
        return; // 保存処理に進まず、ここで終了
      }

      // --- 🌸 【バリデーション：金額のチェック】 🌸 ---
      if (amountStr.isEmpty()) {
        notifyError("Please enter a contribution amount.");
        return;
      }

      double amount;
      try {
        amount = Double.parseDouble(amountStr);
        if (amount <= 0) {
          notifyError("Amount must be a positive number.");
          return;
        }
      } catch (NumberFormatException ex) {
        notifyError("Invalid amount format. Please use numbers only (e.g., 250.00).");
        return;
      }

      String date = LocalDate.now().toString();

      // 名前のサニタイズ（1文字目大文字）
      String fName = fNameRaw.substring(0, 1).toUpperCase() + fNameRaw.substring(1).toLowerCase();
      String lName = lNameRaw.substring(0, 1).toUpperCase() + lNameRaw.substring(1).toLowerCase();
      String fullName = fName + " " + lName;

      Supportable s;
      if (view.getSelectedType().equals("DONATION")) {
        s = new Donation(amount, fName, lName, date);
        double total = model.calculateCumulativeTotal(fullName) + amount;
        ((Donation) s).setCumulativeTotal(total);
      } else {
        String mission = view.getSelectedMission();
        EmpowermentStrategy strategy = mission.contains("Agriculture") ? new AgricultureStrategy() :
            mission.contains("Apparel") ? new ApparelStrategy() : new HealthStrategy();
        s = new Microfinance(amount, fName, lName, date, strategy);
      }

      // モデルへの保存
      this.lastSupport = s;
      model.addSupport(s);
      model.saveToCSV("impact_data.csv");

      // マイページの検索欄に名前を自動セット
      view.setSearchText(fullName);

      // ビューのコンテンツ更新
      boolean isDonation = (s instanceof Donation);
      boolean showBtn = (isDonation && ((Donation) s).isTaxDeductible()) || !isDonation;
      String btnText = isDonation ? "View Receipt ✨" : "View Certificate 🏆";

      view.setStoryContent(s.getImpactMessage(), s.getImagePath(), s.getGlobalFact(), showBtn, btnText);
      view.updateRecentActivity(String.format("Latest: %s supported %s with $%.2f", fName, s.getRoleName(), amount));

      refreshUI();
      view.clearInputs();

    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(view, "Please enter a valid numeric amount.");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  /**
   * [NEW] ユーザーにエラーを通知するメソッド
   * コントローラー内に定義することで、ビューをいじらずにエラー表示機能を強化できます。
   */
  private void notifyError(String message) {
    // 1. ポップアップで警告を出す (標準Java機能)
    JOptionPane.showMessageDialog(view, message, "Action Required", JOptionPane.WARNING_MESSAGE);

    // 2. ビューにすでにある updateRecentActivity を使って、画面のログにも警告を表示
    //view.updateRecentActivity("⚠️ ERROR: " + message);
  }

  /**
   * 受領証の生成
   * [FIX] デスクトップの donation_receipt.txt テンプレートを読み込むように修正しました。
   */
  private String generateReceipt(Supportable s) {
    String desktopPath = System.getProperty("user.home") + "/Desktop/donation_receipt.txt";
    try {
      Path path = Paths.get(desktopPath);
      if (Files.exists(path)) {
        String content = Files.readString(path);
        // テンプレート内の変数を実際のデータに置換
        return content.replace("[NAME]", s.getFullName())
            .replace("[AMOUNT]", String.format("%.2f", s.getAmount()))
            .replace("[DATE]", s.getDate());
      }
    } catch (Exception e) {
      // ファイル読み込み失敗時はフォールバックのテキストを表示
    }

    // テンプレートがない場合のデフォルト形式
    return String.format("--- SMILELOG OFFICIAL RECEIPT ---\n\nDonor: %s\nAmount: $%.2f\nDate: %s\n\nKindness transforms lives.",
        s.getFullName(), s.getAmount(), s.getDate());
  }

  private String generateCertificate(Supportable s) {
    return String.format("====================================\n   PARTNERSHIP CERTIFICATE\n====================================\n\nRecipient: %s\nSector: %s\nAmount: $%.2f\nDate: %s\n\nBuilding the future together.\n====================================",
        s.getFullName(), s.getRoleName(), s.getAmount(), s.getDate());
  }

  /**
   * 画面上の統計と履歴リストを最新状態に更新します。
   */
  private void refreshUI() {
    view.setGlobalTotalText(String.format("Raised: $%,.2f", model.getTotalAmount()));
    view.updateProgress((int)model.getCompletionPercentage());

    // 検索バーに入っている名前で履歴を更新
    String searchWord = view.getSearchText();
    view.setHistoryText(model.getConciseHistory(searchWord));
  }
}