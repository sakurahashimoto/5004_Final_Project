package impact.logic;

/**
 * EmpowermentStrategy インターフェース
 * [Strategy Pattern - Interface]
 * マイクロファイナンスの具体的な支援ロジックを「カセット」として定義するための契約書です。
 */
public interface EmpowermentStrategy {

  /**
   * 支援金額に応じた具体的な「物語（ストーリー）」を返します。
   * [UPDATE] ここからは豆知識を除去し、純粋なストーリーのみを返すようにします。
   */
  String getInvestmentStory(double amount);

  /**
   * [NEW] 支援分野に関する「豆知識（Fact）」を返します。
   * これにより、UI上でストーリーとは別の場所に「💡 DID YOU KNOW?」として表示可能になります。
   */
  String getGlobalFact(double amount);

  /**
   * 何人の起業家を支援できたかを計算します。
   */
  int calculateEntrepreneursHelped(double amount);

  /**
   * 支援分野の名称（役割名）を返します（例: Agricultural Partner）。
   */
  String getSectorName();

  /**
   * 支援内容に合致した画像のパスを返します。
   */
  String getImagePath(double amount);
}