package impact.core;

/**
 * AbstractSupport
 * Defines the common structure for all support types.
 * This class uses the Template Method pattern to standardize how impact messages are generated.
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
   * Template Method
   * Generates a standardized mission report.
   * Specific details are provided by subclasses via abstract methods.
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

  /** @return A visual or text-based badge representing the support type. */
  public abstract String getImpactBadge();

  /** @return A specific description of the impact achieved (e.g., items purchased). */
  protected abstract String getSpecificImpact();

  /** * Returns a global fact related to the cause.
   * Defined as abstract to force concrete subclasses to provide relevant data.
   */
  @Override
  public abstract String getGlobalFact();
}