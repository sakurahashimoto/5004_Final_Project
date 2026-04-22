package impact.core;

/**
 * Supportable Interface
 * Defines the capabilities and data requirements that all support-related objects must implement.
 */
public interface Supportable {
  /** @return The monetary amount of the support. */
  double getAmount();

  /** @return A formatted message describing the social impact. */
  String getImpactMessage();

  /** @return The number of individuals positively impacted by this support. */
  int getPeopleHelped();

  /** @return The first name of the supporter. */
  String getFirstName();

  /** @return The full name of the supporter. */
  String getFullName();

  /** @return The role or designation of the supporter (e.g., "HERO", "LENDER"). */
  String getRoleName();

  /** @return The file path or URL for the representative impact image. */
  String getImagePath();

  /** @return The category or type of support (e.g., "Donation", "Microfinance"). */
  String getType();

  /** @return A relevant global statistic or fact related to the cause. */
  String getGlobalFact();

  /** * Returns the date when the support was provided.
   */
  String getDate();
}