package impact.core;//憲法: 全員が知っておくべき共通ルールだから。
//支援の種類を文字列で打つと、Donation を打ち間違えてバグる。
//だから選択肢を固定しよう！
import impact.logic.*;

/**
 * Enum
 * Define support type that we use across the system
 * We can prevent typo by setting SupportType as enum instead of String
 */
public enum SupportType {
  DONATION,
  MICROFINANCE
}
