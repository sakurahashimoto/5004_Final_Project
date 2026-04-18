package impact.ui;

import impact.data.ImpactModel;
import impact.ui.ImpactView;
import impact.ui.ImpactController;
import javax.swing.SwingUtilities;

/**
 * Main クラス
 * [MVC: Launcher]
 * プロジェクトのすべての部品を繋ぎ合わせる、SmileLog の起動クラスです。
 * 注意: このファイルには public class Main のみが含まれる必要があります。
 */
public class Main {
  public static void main(String[] args) {
    // SwingのGUIスレッドを安全に起動するための仕組み
    SwingUtilities.invokeLater(() -> {
      try {
        // 1. データの管理役 (Model) を作成
        ImpactModel model = new ImpactModel();

        // 過去のデータがあれば読み込む
        try {
          model.loadFromCSV("impact_data.csv");
        } catch (Exception e) {
          System.out.println("New session started: impact_data.csv will be created upon submission.");
        }

        // 2. 画面の表示役 (View) を作成
        ImpactView view = new ImpactView();

        // 3. 橋渡し役 (Controller) を作成し、ModelとViewを繋ぐ
        new ImpactController(model, view);

        // 4. 画面を可視化
        view.setVisible(true);

        System.out.println("SmileLog is now active... 🌸");
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
}