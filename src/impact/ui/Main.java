package impact.ui;

import impact.data.ImpactModel;
import impact.ui.ImpactView;
import impact.ui.ImpactController;
import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args) {
    // [文法] SwingUtilities.invokeLater
    // [意味] 「画面（GUI）の準備が整ってから安全に起動してね」というJavaのおまじない。
    SwingUtilities.invokeLater(() -> {
      // 1. 【脳】を作る
      ImpactModel model = new ImpactModel();

      // 2. 【顔】を作る
      ImpactView view = new ImpactView();

      // 3. 【司令塔】に「脳」と「顔」を預けて繋いでもらう
      // なぜ？ ➔ Controllerがこの2つを持っていないと、橋渡しができないから。
      new ImpactController(model, view);

      // 4. 【表示】窓を見えるようにする
      view.setVisible(true);
    });
  }
}