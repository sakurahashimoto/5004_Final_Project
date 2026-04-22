package impact.ui;

import impact.data.ImpactModel;
import impact.ui.ImpactView;
import impact.ui.ImpactController;
import javax.swing.SwingUtilities;

/**
 * Main Class
 * The entry point of the SmileLog application. This class is responsible for
 * assembling the MVC components: instantiating the Model, View, and Controller,
 * and launching the GUI on the Event Dispatch Thread (EDT).
 */
public class Main {
  /**
   * Main entry point for the application.
   * @param args Command line arguments (not used).
   */
  public static void main(String[] args) {
    // Standard practice to launch Swing GUIs on the Event Dispatch Thread for thread safety.
    SwingUtilities.invokeLater(() -> {
      try {
        // 1. Initialize the Model (Data Layer)
        ImpactModel model = new ImpactModel();

        // Load existing historical data if available
        try {
          model.loadFromCSV("impact_data.csv");
        } catch (Exception e) {
          System.out.println("New session started: impact_data.csv will be created upon submission.");
        }

        // 2. Initialize the View (Presentation Layer)
        ImpactView view = new ImpactView();

        // 3. Initialize the Controller and wire the Model and View together
        new ImpactController(model, view);

        // 4. Make the application window visible to the user
        view.setVisible(true);

        System.out.println("SmileLog is now active... 🌸");
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
}