package impact.ui.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import impact.data.ImpactModel;
import impact.logic.Donation;
import impact.logic.Microfinance;
import impact.logic.AgricultureStrategy;
import impact.logic.ApparelStrategy;
import impact.logic.HealthStrategy;

/**
 * SmileLogLogicTest
 * JUnit suite for validating the core application logic, badge generation,
 * and the accuracy of various strategy implementations.
 * Tests are structured using the Arrange-Act-Assert (AAA) pattern to ensure
 * clarity and reliability.
 */
class SmileLogLogicTest {

  private ImpactModel model;

  /**
   * Setup method executed before each test case.
   * Instantiates a fresh ImpactModel to ensure test isolation and prevent
   * data contamination between test methods.
   */
  @BeforeEach
  void setUp() {
    model = new ImpactModel();
  }

  /**
   * Verifies the threshold logic for donor badge promotion based on
   * cumulative contribution totals.
   */
  @Test
  @DisplayName("Donation: Validate badge tier promotion based on cumulative totals")
  void testDonationBadgeTiers() {
    // Arrange & Act
    Donation d = new Donation(0.0, "Sakura", "Hashimoto", "2026-04-18");

    // Assert: Under $50
    d.setCumulativeTotal(49.0);
    assertTrue(d.getImpactBadge().contains("Smile"), "Amounts under $50 should result in 'Smile Partner'.");

    // Assert: $100
    d.setCumulativeTotal(100.0);
    assertTrue(d.getImpactBadge().contains("Silver"), "An amount of $100 should result in 'Silver Supporter'.");

    // Assert: $500
    d.setCumulativeTotal(500.0);
    assertTrue(d.getImpactBadge().contains("Platinum"), "An amount of $500 should result in 'Platinum Elite'.");

    // Assert: $1000
    d.setCumulativeTotal(1000.0);
    assertTrue(d.getImpactBadge().contains("Hall of Fame"), "Amounts of $1000 and above should result in 'Hall of Fame'.");
  }

  /**
   * Validates the calculation logic of the AgricultureStrategy, ensuring
   * the correct number of entrepreneurs helped for specific dollar amounts.
   */
  @Test
  @DisplayName("Agriculture Strategy: Validate entrepreneur count calculation logic")
  void testAgricultureStrategyCalculation() {
    AgricultureStrategy agri = new AgricultureStrategy();

    // Verify support ranks
    assertEquals(15, agri.calculateEntrepreneursHelped(100.0), "A $100 contribution should help 15 entrepreneurs.");
    assertEquals(5, agri.calculateEntrepreneursHelped(50.0), "A $50 contribution should help 5 entrepreneurs.");
    assertEquals(1, agri.calculateEntrepreneursHelped(1.0), "A small amount should still help at least 1 entrepreneur.");
  }

  /**
   * Verifies the ApparelStrategy's image path logic, specifically checking
   * that it returns a fixed asset path regardless of the amount.
   */
  @Test
  @DisplayName("Apparel Strategy: Validate fixed image path specification")
  void testApparelStrategyImagePath() {
    ApparelStrategy apparel = new ApparelStrategy();

    // Verify current spec: fixed path for all amounts
    String expectedPath = "/images/Microfinance_100.jpg";
    assertEquals(expectedPath, apparel.getImagePath(10.0), "Should return fixed path for $10.");
    assertEquals(expectedPath, apparel.getImagePath(500.0), "Should return fixed path for $500.");
  }

  /**
   * Tests global calculations in the ImpactModel, specifically the total
   * amount raised and the progress toward the financial goal.
   */
  @Test
  @DisplayName("Model: Validate global total and completion percentage calculations")
  void testModelGlobalCalculations() {
    // Arrange
    model.addSupport(new Donation(100.0, "UserA", "Test", "2026-04-18"));
    model.addSupport(new Microfinance(200.0, "UserB", "Test", "2026-04-18", new HealthStrategy()));

    // Act & Assert: Total should be 300.0
    assertEquals(300.0, model.getTotalAmount(), "Global total amount calculation is incorrect.");

    // Act & Assert: 300 out of 10,000 should be 3.0% (with 0.01 tolerance for floating point)
    assertEquals(3.0, model.getCompletionPercentage(), 0.01, "Goal completion percentage calculation is incorrect.");
  }

  /**
   * Boundary value test to ensure the goal completion percentage is
   * capped at 100.0% even when contributions exceed the target goal.
   */
  @Test
  @DisplayName("Boundary: Ensure completion percentage does not exceed 100%")
  void testBoundaryGoalCompletion() {
    // Add contribution significantly over the $10,000 goal
    model.addSupport(new Donation(20000.0, "VHN", "Donor", "2026-04-18"));

    // Verify it is capped at 100.0% instead of 200%
    assertEquals(100.0, model.getCompletionPercentage(), "Completion percentage should be capped at 100.0%.");
  }

  /**
   * Verifies that the first and last names are concatenated correctly
   * within the support objects.
   */
  @Test
  @DisplayName("Naming: Validate full name concatenation")
  void testFullNameConcatenation() {
    Donation d = new Donation(10.0, "Sakura", "Hashimoto", "2026-04-18");
    assertEquals("Sakura Hashimoto", d.getFullName(), "Full name concatenation does not match the expected format.");
  }
}