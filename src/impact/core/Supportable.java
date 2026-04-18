package impact.core;

/**
 * Supportable Interface
 * すべての支援オブジェクトが持つべき「能力」を定義します。
 */
public interface Supportable {
  double getAmount();
  String getImpactMessage();
  int getPeopleHelped();
  String getFirstName();
  String getFullName();
  String getRoleName();
  String getImagePath();
  String getType();
  String getGlobalFact();

  /**
   * [NEW] 支援が行われた日付を取得する能力を追加
   */
  String getDate();
}