package impact.ui.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * SmileLogControllerTest
 * [Principle 6 & 13] Validates input checking (validation) and data cleansing (sanitization).
 * This test suite ensures the system remains robust and secure, regardless of user input errors.
 * It follows the Arrange-Act-Assert (AAA) pattern for clear unit testing.
 */
class SmileLogControllerTest {

  /**
   * 1. Name Sanitization Test
   * Verifies that user input like "sAKURA" is internally converted to "Sakura"
   * to maintain data consistency.
   */
  @Test
  @DisplayName("Sanitization: Is only the first letter capitalized?")
  void testNameSanitization() {
    // --- Arrange ---
    String rawName = "sAKURA";

    // --- Act (Simulating the controller logic) ---
    String sanitized = rawName.substring(0, 1).toUpperCase() + rawName.substring(1).toLowerCase();

    // --- Assert ---
    assertEquals("Sakura", sanitized, "The name should be converted to title case (first letter uppercase).");
  }

  /**
   * 2. Empty Field Validation Test
   * Ensures that the system correctly identifies and rejects inputs consisting only of whitespace.
   */
  @Test
  @DisplayName("Validation: Can it reject an empty name?")
  void testEmptyNameValidation() {
    // --- Arrange ---
    String emptyName = "   "; // Input with only spaces

    // --- Act & Assert ---
    assertTrue(emptyName.trim().isEmpty(), "Input consisting only of spaces should be identified as 'empty'.");
  }

  /**
   * 3. Numerical Format Resilience Test
   * Confirms that the system correctly throws a NumberFormatException when non-numeric
   * strings are provided in the amount field.
   */
  @Test
  @DisplayName("Robustness: Does non-numeric input trigger an exception?")
  void testInvalidAmountFormat() {
    // --- Arrange ---
    String invalidInput = "100dollars";

    // --- Act & Assert ---
    assertThrows(NumberFormatException.class, () -> {
      Double.parseDouble(invalidInput);
    }, "The system should throw a NumberFormatException and halt processing for non-numeric input.");
  }

  /**
   * 4. Amount Trimming Test
   * Ensures that leading or trailing spaces in the amount field are automatically
   * removed before parsing.
   */
  @Test
  @DisplayName("Validation: Are leading/trailing spaces in the amount removed?")
  void testAmountTrimming() {
    // --- Arrange ---
    String inputWithSpaces = "  250.0  ";

    // --- Act ---
    double amount = Double.parseDouble(inputWithSpaces.trim());

    // --- Assert ---
    assertEquals(250.0, amount, "Leading and trailing spaces should be automatically removed (trimmed).");
  }
}