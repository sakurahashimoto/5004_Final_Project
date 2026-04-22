package impact.ui;

import impact.data.ImpactModel;
import impact.core.Supportable;
import impact.logic.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

/**
 * ImpactController Class
 * Acts as the intermediary between the View and the Model. It processes user inputs,
 * performs data validation, updates the model, and synchronizes the UI components.
 */
public class ImpactController {
  private final ImpactModel model;
  private final ImpactView view;
  private Supportable lastSupport;

  /**
   * Constructs the controller and initializes the UI state.
   * @param model The data management layer.
   * @param view The graphical user interface layer.
   */
  public ImpactController(ImpactModel model, ImpactView view) {
    this.model = model;
    this.view = view;
    setupListeners();
    refreshUI();
  }

  /**
   * Registers action listeners for UI components to handle user events.
   */
  private void setupListeners() {
    view.getStartButton().addActionListener(e -> view.switchToInput());
    view.getNavMyPageButton().addActionListener(e -> view.switchToMyPage());
    view.getBackToDonateButton().addActionListener(e -> view.switchToInput());

    // Listener for submitting new support data
    view.getCreateButton().addActionListener(e -> handleSubmission());

    // Listener for the search bar in My Page (triggered on Enter key)
    view.getSearchField().addActionListener(e -> refreshUI());

    // Listener for displaying documents (Receipts or Certificates)
    view.getViewDocumentButton().addActionListener(e -> {
      if (lastSupport == null) return;
      if (lastSupport instanceof Donation) {
        view.showDocumentModal("Official Tax Receipt", generateReceipt(lastSupport));
      } else {
        view.showDocumentModal("Partnership Certificate", generateCertificate(lastSupport));
      }
    });
  }

  /**
   * Handles the workflow for submitting new support data.
   * Includes validation, data sanitization, object creation, and model updates.
   */
  private void handleSubmission() {
    try {
      String fNameRaw = view.getFirstNameText().trim();
      String lNameRaw = view.getLastNameText().trim();
      String amountStr = view.getAmountText().trim();

      // Validation: Ensure names are not empty
      if (fNameRaw.isEmpty() || lNameRaw.isEmpty()) {
        notifyError("Please enter both First Name and Last Name.");
        return;
      }

      // Validation: Ensure amount is not empty
      if (amountStr.isEmpty()) {
        notifyError("Please enter a contribution amount.");
        return;
      }

      double amount;
      try {
        amount = Double.parseDouble(amountStr);
        if (amount <= 0) {
          notifyError("Amount must be a positive number.");
          return;
        }
      } catch (NumberFormatException ex) {
        notifyError("Invalid amount format. Please use numbers only (e.g., 250.00).");
        return;
      }

      String date = LocalDate.now().toString();

      // Name Sanitization: Capitalize first letter of names
      String fName = fNameRaw.substring(0, 1).toUpperCase() + fNameRaw.substring(1).toLowerCase();
      String lName = lNameRaw.substring(0, 1).toUpperCase() + lNameRaw.substring(1).toLowerCase();
      String fullName = fName + " " + lName;

      Supportable s;
      // Object Creation Logic: Polymorphically instantiate Donation or Microfinance
      if (view.getSelectedType().equals("DONATION")) {
        s = new Donation(amount, fName, lName, date);
        double total = model.calculateCumulativeTotal(fullName) + amount;
        ((Donation) s).setCumulativeTotal(total);
      } else {
        String mission = view.getSelectedMission();
        // Strategy injection based on user selection
        EmpowermentStrategy strategy = mission.contains("Agriculture") ? new AgricultureStrategy() :
            mission.contains("Apparel") ? new ApparelStrategy() : new HealthStrategy();
        s = new Microfinance(amount, fName, lName, date, strategy);
      }

      // Persist data
      this.lastSupport = s;
      model.addSupport(s);
      model.saveToCSV("impact_data.csv");

      // Set search field to the current user's name automatically
      view.setSearchText(fullName);

      // Update View Content
      boolean isDonation = (s instanceof Donation);
      boolean showBtn = (isDonation && ((Donation) s).isTaxDeductible()) || !isDonation;
      String btnText = isDonation ? "View Receipt ✨" : "View Certificate 🏆";

      view.setStoryContent(s.getImpactMessage(), s.getImagePath(), s.getGlobalFact(), showBtn, btnText);
      view.updateRecentActivity(String.format("Latest: %s supported %s with $%.2f", fName, s.getRoleName(), amount));

      refreshUI();
      view.clearInputs();

    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(view, "Please enter a valid numeric amount.");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Notifies the user of validation errors via a dialog box.
   * @param message The error message to display.
   */
  private void notifyError(String message) {
    JOptionPane.showMessageDialog(view, message, "Action Required", JOptionPane.WARNING_MESSAGE);
  }

  /**
   * Generates a tax receipt by populating a template file with support data.
   * @param s The support object containing donor data.
   * @return A formatted receipt string.
   */
  private String generateReceipt(Supportable s) {
    try (InputStream is = getClass().getClassLoader().getResourceAsStream("templates/donation_receipt.txt")) {
      if (is == null) {
        throw new RuntimeException("Resource not found: donation_receipt.txt");
      }
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
        String content = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        return content.replace("[NAME]", s.getFullName())
            .replace("[AMOUNT]", String.format("%.2f", s.getAmount()))
            .replace("[DATE]", s.getDate());
      }
    } catch (Exception e) {
      // Fallback format if template reading fails
      return String.format("--- SMILELOG OFFICIAL RECEIPT ---\n\nDonor: %s\nAmount: $%.2f\nDate: %s\n\nKindness transforms lives.",
          s.getFullName(), s.getAmount(), s.getDate());
    }
  }

  /**
   * Generates a partnership certificate for microfinance lenders.
   * @param s The support object containing lender data.
   * @return A formatted certificate string.
   */
  private String generateCertificate(Supportable s) {
    return String.format("====================================\n   PARTNERSHIP CERTIFICATE\n====================================\n\nRecipient: %s\nSector: %s\nAmount: $%.2f\nDate: %s\n\nBuilding the future together.\n====================================",
        s.getFullName(), s.getRoleName(), s.getAmount(), s.getDate());
  }

  /**
   * Synchronizes the UI with the latest data from the Model.
   * Updates totals, progress bars, and historical records.
   */
  private void refreshUI() {
    view.setGlobalTotalText(String.format("Raised: $%,.2f", model.getTotalAmount()));
    view.updateProgress((int)model.getCompletionPercentage());

    // Update history list based on current search field text
    String searchWord = view.getSearchText();
    view.setHistoryText(model.getConciseHistory(searchWord));
  }
}