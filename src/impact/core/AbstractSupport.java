package impact.core;

/**
 * AbstractSupport
 * [UPDATE] getGlobalFact() を抽象メソッドとして定義し、子クラスに実装を任せます。
 */
public abstract class AbstractSupport implements Supportable {
  protected double amount;
  protected String firstName;
  protected String lastName;
  protected String type;
  protected String date;

  public AbstractSupport(double amount, String firstName, String lastName, String type, String date) {
    this.amount = amount;
    this.firstName = firstName;
    this.lastName = lastName;
    this.type = type;
    this.date = date;
  }

  @Override public double getAmount() { return this.amount; }
  @Override public String getFirstName() { return this.firstName; }
  @Override public String getFullName() { return firstName + " " + lastName; }
  @Override public String getType() { return this.type; }
  @Override public String getDate() { return this.date; }

  /**
   * [Template Method]
   * [UPDATE] ここからは「豆知識」を除去し、純粋なストーリーのみを表示するようにしました。
   */
  @Override
  public String getImpactMessage() {
    return String.format("%s   |   %s: %s\n\nMISSION REPORT:\n$%.2f impact: %s",
        getImpactBadge(),
        getRoleName(),
        this.firstName,
        this.amount,
        getSpecificImpact());
  }

  public abstract String getImpactBadge();
  protected abstract String getSpecificImpact();

  /** [NEW] インターフェースの要件を満たすため、抽象メソッドとして定義 */
  @Override
  public abstract String getGlobalFact();
}