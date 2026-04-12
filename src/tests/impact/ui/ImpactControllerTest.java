package impact.ui;

import impact.core.Supportable;
import impact.data.ImpactModel;
import javax.swing.JTextField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImpactControllerTest {
  private StubImpactModel stubModel;
  private StubImpactView stubView;
  private ImpactController controller;

  // --- Stub Classes ---
  private static class StubImpactModel extends ImpactModel {
    boolean addSupportCalled = false;
    boolean saveToCSVCalled = false;
    boolean loadFromCSVCalled = false;
    @Override
    public void addSupport(Supportable s) { addSupportCalled = true;
      super.addSupport(s);
    }
    @Override
    public void saveToCSV(String path) { saveToCSVCalled = true; }

    @Override
    public void loadFromCSV(String path) { loadFromCSVCalled = true; }
  }

  private static class StubImpactView extends ImpactView {
    String name = "Kent", amount = "250", type = "DONATION";
    @Override
    public String getNameText() { return name; }
    @Override
    public String getAmountText() { return amount; }
    @Override
    public String getSelectedType() { return type; }
    @Override
    public void showRandomImage(String message) {}
    @Override
    public JTextField getSearchField() { return new JTextField(); }
    @Override
    public void setHistoryText(String t) {}
    @Override
    public void setGlobalTotalText(String t) {}
    @Override
    public void clearInputs() {}
  }

  @BeforeEach
  public void setup() {
    stubModel = new StubImpactModel();
    stubView = new StubImpactView();
    controller = new ImpactController(stubModel, stubView);
  }

  @Test
  public void testAddDonationCallsModel() {
    // Act
    controller.addSupportFromView();

    // Assert
    Assertions.assertTrue(stubModel.addSupportCalled, "addSupport should have been called");
    Assertions.assertTrue(stubModel.saveToCSVCalled, "saveToCSV should have been called");
    Assertions.assertEquals(1, stubModel.getAllSupports().size());
    Assertions.assertEquals( 250.0, stubModel.getTotalAmountByName("Kent"));
  }
}