package impact.logic;

/**
 * AgricultureStrategy
 * Responsible for defining support logic, investment narratives, and image paths
 * specifically for the agricultural sector.
 */
public class AgricultureStrategy implements EmpowermentStrategy {

  /**
   * Returns the official name of this sector.
   * @return The sector designation string.
   */
  @Override
  public String getSectorName() {
    return "Agricultural Partner";
  }

  /**
   * Calculates the number of entrepreneurs supported based on the contribution amount.
   * @param amount The financial contribution.
   * @return The number of individuals positively impacted.
   */
  @Override
  public int calculateEntrepreneursHelped(double amount) {
    if (amount >= 100) return 15;
    if (amount >= 50)  return 5;
    if (amount >= 15)  return 2;
    return 1;
  }

  /**
   * Generates a specific investment narrative based on the contribution amount.
   * Focuses on the "Story" aspect of the impact.
   * @param amount The financial contribution.
   * @return A string describing the tangible items or services funded.
   */
  @Override
  public String getInvestmentStory(double amount) {
    if (amount >= 100) {
      return "A large-scale irrigation system for a local cooperative! 🚜";
    }
    if (amount >= 50) {
      return "Professional farming tools for multiple families! 🧑‍🌾";
    }
    if (amount >= 15) {
      return "High-quality, climate-resilient seeds for a small farm! 🌱";
    }
    return "Essential training materials on sustainable farming! 📖";
  }

  /**
   * Retrieves a global statistic or fact related to agriculture.
   * @param amount The financial contribution (used to scale the fact's relevance).
   * @return A relevant global fact string.
   */
  @Override
  public String getGlobalFact(double amount) {
    if (amount >= 100) {
      return "Irrigation can increase crop yields by up to 300% in dry regions.";
    }
    if (amount >= 50) {
      return "Access to basic tools is often the biggest barrier for small-scale farmers.";
    }
    return "Smallholder farmers produce about 80% of the food consumed in developing regions.";
  }

  /**
   * Returns the file path for the image associated with the impact level.
   * Standardized to match system screenshot naming conventions.
   * @param amount The financial contribution.
   * @return The string path to the image resource.
   */
  @Override
  public String getImagePath(double amount) {
    // Standardized to a specific asset for the presentation demo
    return "/images/Microfinance_50.jpg";
  }
}