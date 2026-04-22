package impact.logic;

import impact.core.AbstractSupport;

/**
 * Donation Class
 * Represents a direct donation support type.
 * This class implements specific logic for donation impact,
 * including tiered badges based on cumulative contributions and specific impact narratives.
 */
public class Donation extends AbstractSupport {

  private double cumulativeTotal;

  /**
   * Constructs a new Donation instance.
   * @param amount The current donation amount.
   * @param firstName The donor's first name.
   * @param lastName The donor's last name.
   * @param date The date of the donation.
   */
  public Donation(double amount, String firstName, String lastName, String date) {
    super(amount, firstName, lastName, "DONATION", date);
    this.cumulativeTotal = amount;
  }

  /**
   * Sets the cumulative total of donations for this donor to determine their badge tier.
   * @param total The total amount donated over time.
   */
  public void setCumulativeTotal(double total) {
    this.cumulativeTotal = total;
  }

  /**
   * Returns a visual badge title based on the donor's cumulative contribution level.
   * @return A string representing the donor's status tier.
   */
  @Override
  public String getImpactBadge() {
    if (cumulativeTotal >= 1000) return "💎 Hall of Fame";
    if (cumulativeTotal >= 500)  return "🥇 Platinum Elite";
    if (cumulativeTotal >= 250)  return "🥈 Gold Tier";
    if (cumulativeTotal >= 100)  return "🥉 Silver Supporter";
    return "🌸 Smile Partner";
  }

  /**
   * Returns the role name for this support type.
   * @return The string "Education Advocate".
   */
  @Override public String getRoleName() { return "Education Advocate"; }

  /**
   * Provides a global statistic related to education and child welfare.
   * This fulfills the requirement defined in the Supportable interface.
   * @return A relevant global fact based on the donation amount.
   */
  @Override
  public String getGlobalFact() {
    if (amount >= 250) return "Over 250 million children and youth are currently out of school globally.";
    if (amount >= 50)  return "Proper nutrition is proven to increase school attendance rates by up to 20%.";
    if (amount >= 10)  return "Basic healthcare and vaccines prevent 4 million child deaths annually.";
    return "Early childhood education provides a $16 return for every $1 invested.";
  }

  /**
   * Helper method to map the donation amount to a specific threshold string.
   * Used for matching file names and determining impact scales.
   * @return A string representing the impact level ("250", "50", etc.).
   */
  private String getImpactLevel() {
    if (amount >= 250) return "250";
    if (amount >= 50)  return "50";
    if (amount >= 10)  return "10";
    if (amount >= 5)   return "5";
    return "lessthanfive";
  }

  /**
   * Provides a specific story about how the donation amount is utilized in education.
   * @return A descriptive string of the tangible impact.
   */
  @Override
  protected String getSpecificImpact() {
    String level = getImpactLevel();
    switch (level) {
      case "250": return "Supplies for a whole classroom of 40 students! 🏫";
      case "50":  return "Nutritious school meals for 10 children for a week! 🍱";
      case "10":  return "Essential vaccines for 3 children! 💉";
      case "5":   return "Two hygiene kits for students! 🧼";
      default:    return "High-quality learning tools for a child! ✏️";
    }
  }

  /**
   * Calculates the number of children or students assisted by this donation.
   * @return The count of people helped.
   */
  @Override
  public int getPeopleHelped() {
    String level = getImpactLevel();
    switch (level) {
      case "250": return 40;
      case "50":  return 10;
      case "10":  return 3;
      case "5":   return 2;
      default:    return 1;
    }
  }

  /**
   * Returns the file path for the impact image, dynamically determined by the impact level.
   * @return The string path to the image asset.
   */
  @Override
  public String getImagePath() {
    return "/images/Donation_" + getImpactLevel() + ".jpg";
  }

  /**
   * Determines if the donation qualifies for a tax deduction (threshold: $250).
   * @return true if deductible, false otherwise.
   */
  public boolean isTaxDeductible() { return amount >= 250.0; }
}