package impact.ui.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import impact.data.ImpactModel;
import impact.logic.Donation;
import java.io.File;
import java.io.IOException;

/**
 * SmileLogModelTest Class
 * [MVC: Model Testing]
 * This test suite validates the data management logic within the ImpactModel class.
 * It focuses on data persistence (CSV I/O), aggregate calculations, and
 * search functionality using the Arrange-Act-Assert (AAA) pattern.
 */
class SmileLogModelTest {

  private ImpactModel model;
  private final String TEST_CSV = "junit_test_data.csv";

  /**
   * Setup method executed before each test.
   * Re-instantiates the model and ensures the test environment is clean
   * by deleting any existing test CSV files.
   */
  @BeforeEach
  void setUp() {
    model = new ImpactModel();
    File file = new File(TEST_CSV);
    if (file.exists()) {
      file.delete();
    }
  }

  /**
   * Cleanup method executed after each test.
   * Deletes the temporary test CSV file to prevent side effects on the
   * local file system.
   */
  @AfterEach
  void tearDown() {
    File file = new File(TEST_CSV);
    if (file.exists()) {
      file.delete();
    }
  }

  /**
   * Verifies that adding a support object correctly updates the total amount.
   */
  @Test
  @DisplayName("Model: Does adding data correctly increase the total amount?")
  void testDataAddition() {
    // --- Arrange ---
    Donation d = new Donation(100.0, "Sakura", "Hashimoto", "2026-04-18");

    // --- Act ---
    model.addSupport(d);

    // --- Assert ---
    assertEquals(100.0, model.getTotalAmount(), "Total amount must be 100.0 after addition.");
  }

  /**
   * Validates that the history search logic is case-insensitive and
   * correctly retrieves records based on a partial name match.
   */
  @Test
  @DisplayName("Search: Can history be found by name (case-insensitive)?")
  void testSearchHistory() {
    // Arrange
    model.addSupport(new Donation(50.0, "Sakura", "Hashimoto", "2026-04-18"));

    // Act: Search using lowercase "sakura"
    String history = model.getConciseHistory("sakura");

    // Assert
    assertTrue(history.contains("Sakura Hashimoto"), "The history report should contain the donor's full name.");
  }

  /**
   * Validates the CSV persistence logic.
   * Ensures that data saved by one model instance can be accurately
   * reconstructed by another.
   * @throws IOException If file handling fails during the test.
   */
  @Test
  @DisplayName("CSV: Is data integrity maintained after saving and reloading?")
  void testCSVLogic() throws IOException {
    // Arrange
    model.addSupport(new Donation(250.0, "CSV_User", "Test", "2026-04-18"));

    // Act 1: Save to CSV
    model.saveToCSV(TEST_CSV);

    // Act 2: Reconstruct into a new model instance
    ImpactModel newModel = new ImpactModel();
    newModel.loadFromCSV(TEST_CSV);

    // Assert
    assertEquals(250.0, newModel.getTotalAmount(), "Amount should remain 250.0 after CSV reconstruction.");
  }

  /**
   * Verifies that the completion percentage is capped at 100.0%,
   * even if the total amount exceeds the goal.
   */
  @Test
  @DisplayName("Boundary: Is progress capped at 100% when exceeding the goal?")
  void testGoalCap() {
    // Arrange: $15,000 donation against a $10,000 goal
    model.addSupport(new Donation(15000.0, "Rich", "Donor", "2026-04-18"));

    // Assert
    assertEquals(100.0, model.getCompletionPercentage(), "Progress percentage must not exceed 100.0%.");
  }

  /**
   * Ensures the system is resilient when attempting to load non-existent files.
   */
  @Test
  @DisplayName("Robustness: Does the app handle missing files without crashing?")
  void testErrorHandling() {
    // Assert
    assertDoesNotThrow(() -> {
      model.loadFromCSV("non_existent_file.csv");
    }, "Loading a missing file should be handled gracefully without throwing exceptions.");
  }
}