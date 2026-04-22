package impact.logic;

/**
 * HealthStrategy
 * [Strategy Pattern: Concrete Strategy]
 * Responsible for the logic, impact narratives, and data regarding
 * health and medical microfinance support.
 */
public class HealthStrategy implements EmpowermentStrategy {

  /**
   * Returns the official name of this sector.
   * @return The sector designation string.
   */
  @Override
  public String getSectorName() {
    return "Health Champion";
  }

  /**
   * Calculates the number of healthcare workers supported or individuals impacted
   * based on the contribution amount.
   * @param amount The financial contribution.
   * @return The count of people helped.
   */
  @Override
  public int calculateEntrepreneursHelped(double amount) {
    if (amount >= 100) return 20; // Supports a whole small local clinic
    if (amount >= 50)  return 8;  // Supports multiple health workers
    if (amount >= 15)  return 3;  // Supports specific families or workers
    return 1;
  }

  /**
   * Generates a descriptive story about the medical equipment or services provided.
   * @param amount The financial contribution.
   * @return A string describing the tangible medical impact.
   */
  @Override
  public String getInvestmentStory(double amount) {
    if (amount >= 100) {
      return "Advanced diagnostic tools and cold-chain storage for vaccines! 💉";
    }
    if (amount >= 50) {
      return "Comprehensive medical kits and pharmacy stock for 8 workers! 💊";
    }
    if (amount >= 15) {
      return "Essential first-aid supplies and sanitation tools for 3 households! 🧼";
    }
    return "Professional health and nutrition training for a local caregiver! 📖";
  }

  /**
   * Provides global statistics and insights regarding public health and primary care.
   * This implementation separates educational facts from the specific investment story.
   * @param amount The financial contribution.
   * @return A relevant global health fact.
   */
  @Override
  public String getGlobalFact(double amount) {
    if (amount >= 100) {
      return "Strengthening local primary health care is the most cost-effective way to save lives.";
    }
    if (amount >= 50) {
      return "Community health workers are the first line of defense against preventable diseases.";
    }
    return "Simple interventions like clean water and basic hygiene can prevent 60% of child deaths.";
  }

  /**
   * Returns the file path for the representative image.
   * Standardized to match system asset naming conventions.
   * @param amount The financial contribution.
   * @return The string path to the image resource.
   */
  @Override
  public String getImagePath(double amount) {
    return "/images/Microfinance_15.jpg";
  }
}