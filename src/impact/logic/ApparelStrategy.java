package impact.logic;

/**
 * ApparelStrategy
 * [Strategy Pattern: Concrete Strategy]
 * Defines the business logic, impact calculations, and storytelling for
 * apparel and tailoring-related microfinance support.
 */
public class ApparelStrategy implements EmpowermentStrategy {

  /**
   * Returns the official name of this sector.
   * @return The sector designation string.
   */
  @Override
  public String getSectorName() {
    return "Apparel Partner";
  }

  /**
   * Calculates the number of entrepreneurs supported based on the contribution amount.
   * @param amount The financial contribution.
   * @return The count of tailors or artisans assisted.
   */
  @Override
  public int calculateEntrepreneursHelped(double amount) {
    if (amount >= 100) return 9;
    if (amount >= 50)  return 4;
    if (amount >= 15)  return 2;
    return 1;
  }

  /**
   * Generates a descriptive story about the specific equipment or materials purchased.
   * @param amount The financial contribution.
   * @return A string representing the tangible impact in the apparel sector.
   */
  @Override
  public String getInvestmentStory(double amount) {
    if (amount >= 100) {
      return "Industrial sewing machines for a multi-family business! 🧵";
    }
    if (amount >= 50) {
      return "Professional patterns and materials for 4 tailors! 👗";
    }
    return "High-quality fabric and tailoring materials for a startup! ✂️";
  }

  /**
   * [NEW] Provides global statistics related to gender-focused economic empowerment.
   * This method separates general facts from specific investment stories.
   * @param amount The financial contribution.
   * @return A relevant global fact string.
   */
  @Override
  public String getGlobalFact(double amount) {
    if (amount >= 100) {
      return "When women work, they invest 90% of their income back into their families.";
    }
    return "Micro-loans for raw materials allow artisans to double their profit margin.";
  }

  /**
   * Returns the file path for the representative image.
   * @param amount The financial contribution.
   * @return The string path to the image resource.
   */
  @Override
  public String getImagePath(double amount) {
    return "/images/Microfinance_100.jpg";
  }
}