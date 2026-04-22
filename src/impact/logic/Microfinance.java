package impact.logic;

import impact.core.AbstractSupport;

/**
 * Microfinance Class
 * This class represents a micro-loan support type. It utilizes the Strategy Pattern
 * to delegate specific impact calculations and storytelling to a "strategy" object,
 * allowing for high flexibility across different sectors (Agriculture, Apparel, Health, etc.).
 */
public class Microfinance extends AbstractSupport {

  /**
   * The specific empowerment strategy (e.g., Agriculture, Apparel, Health)
   * injected at runtime to handle sector-specific logic.
   */
  private EmpowermentStrategy strategy;

  /**
   * Constructs a new Microfinance instance.
   * @param amount The loan amount.
   * @param firstName The supporter's first name.
   * @param lastName The supporter's last name.
   * @param date The date the support was recorded.
   * @param strategy The specific sector strategy to be used for impact calculation.
   */
  public Microfinance(double amount, String firstName, String lastName, String date, EmpowermentStrategy strategy) {
    // Pass common data to the parent class (AbstractSupport)
    super(amount, firstName, lastName, "MICROFINANCE", date);
    this.strategy = strategy;
  }

  /**
   * Returns an investor-style badge title based on the number of entrepreneurs helped.
   * @return A string representing the supporter's impact tier.
   */
  @Override
  public String getImpactBadge() {
    int helped = getPeopleHelped();
    if (helped >= 15) return "🚀 Visionary Level";
    if (helped >= 5)  return "🤝 Core Partner";
    return "🌱 Seed Member";
  }

  /**
   * Returns the role name specific to the current investment sector.
   * @return The sector designation string provided by the strategy.
   */
  @Override
  public String getRoleName() {
    return strategy.getSectorName();
  }

  /**
   * Provides the specific narrative of the investment impact.
   * This logic is delegated to the specific strategy object.
   * @return A string describing the tangible investment story.
   */
  @Override
  protected String getSpecificImpact() {
    return strategy.getInvestmentStory(amount);
  }

  /**
   * [NEW/Override] Retrieves a global fact or statistic related to the specific sector.
   * Uses Delegation to pull data from the injected strategy "cassette."
   * @return A relevant global fact string.
   */
  @Override
  public String getGlobalFact() {
    return strategy.getGlobalFact(amount);
  }

  /**
   * Calculates the number of entrepreneurs assisted by this loan.
   * @return The count of people helped as determined by the strategy.
   */
  @Override
  public int getPeopleHelped() {
    return strategy.calculateEntrepreneursHelped(amount);
  }

  /**
   * Returns the file path for the representative impact image.
   * @return The string path to the image resource provided by the strategy.
   */
  @Override
  public String getImagePath() {
    return strategy.getImagePath(this.amount);
  }

}