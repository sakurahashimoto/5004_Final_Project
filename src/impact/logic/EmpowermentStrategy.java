package impact.logic;

/**
 * EmpowermentStrategy Interface
 * Defines the contract for specific microfinance support logics.
 * Acts as a "swappable" component to determine how impact is calculated
 * and narrated for different business sectors.
 */
public interface EmpowermentStrategy {

  /**
   * Returns a specific "impact story" based on the contribution amount.
   * This focuses on the tangible narrative of the investment.
   * @param amount The financial contribution.
   * @return A string describing the investment story.
   */
  String getInvestmentStory(double amount);

  /**
   * Returns a global statistic or "Did You Know?" fact related to the sector.
   * Allows the UI to display educational insights separately from the impact story.
   * @param amount The financial contribution.
   * @return A relevant global fact string.
   */
  String getGlobalFact(double amount);

  /**
   * Calculates the number of entrepreneurs supported by the given amount.
   * @param amount The financial contribution.
   * @return The count of entrepreneurs helped.
   */
  int calculateEntrepreneursHelped(double amount);

  /**
   * Returns the formal name of the sector (e.g., "Agricultural Partner").
   * @return The sector designation string.
   */
  String getSectorName();

  /**
   * Provides the file path for an image that matches the support context.
   * @param amount The financial contribution.
   * @return The string path to the image asset.
   */
  String getImagePath(double amount);
}