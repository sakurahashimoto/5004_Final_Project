package impact.ui;

import javax.swing.*;
import java.awt.*;

public class ImpactView extends JFrame {
  private final Color TEXT_BLUE = new Color(70, 130, 180);

  private CardLayout cl = new CardLayout();
  private JPanel cardPanel = new JPanel(cl);
  private JTabbedPane tabs = new JTabbedPane();
  private JTextField nameField = new JTextField(), amountField = new JTextField(), searchField = new JTextField();
  private JComboBox<String> typeBox = new JComboBox<>(new String[]{"DONATION", "MICROFINANCE"});
  private JButton startButton = new JButton("Start Journey →"), createButton = new JButton("Confirm Support ✨");

  private JTextArea logArea = new JTextArea();     // 入力直後のお礼用
  private JTextArea historyArea = new JTextArea(); // 履歴リスト用
  private JLabel storyPhotoLabel = new JLabel();   // ★写真とストーリーを表示する主役

  private JLabel globalLabel = new JLabel("🌍 Total Impact: $0.00");

  public ImpactView() {
    setTitle("SmileLog 🌸");
    setSize(550, 900);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setupWelcomeScreen();
    setupInputScreen();
    add(cardPanel);
  }

  private void setupWelcomeScreen() {
    JPanel p = new JPanel(new BorderLayout(20, 20));
    p.setBackground(Color.WHITE);
    JLabel title = new JLabel("SmileLog 🌸", SwingConstants.CENTER);
    title.setFont(new Font("Serif", Font.BOLD, 36));
    p.add(title, BorderLayout.CENTER);
    p.add(startButton, BorderLayout.SOUTH);
    cardPanel.add(p);
  }

  private void setupInputScreen() {
    JPanel mainPanel = new JPanel(new BorderLayout());

    // 上部：合計金額表示
    globalLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    globalLabel.setForeground(TEXT_BLUE);
    globalLabel.setHorizontalAlignment(SwingConstants.CENTER);
    globalLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
    mainPanel.add(globalLabel, BorderLayout.NORTH);

    // Tab 1: Give Support
    JPanel supportTab = new JPanel(new BorderLayout(10, 10));
    JPanel form = new JPanel(new GridLayout(3, 2, 5, 10));
    form.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    form.add(new JLabel("Name:")); form.add(nameField);
    form.add(new JLabel("Amount ($):")); form.add(amountField);
    form.add(new JLabel("Type:")); form.add(typeBox);

    JPanel supportSouth = new JPanel(new BorderLayout());
    supportSouth.add(createButton, BorderLayout.NORTH);
    
    // 写真表示エリアをGive Supportタブへ移動
    storyPhotoLabel.setHorizontalAlignment(SwingConstants.CENTER);
    storyPhotoLabel.setVerticalAlignment(SwingConstants.CENTER);
    storyPhotoLabel.setHorizontalTextPosition(JLabel.CENTER);
    storyPhotoLabel.setVerticalTextPosition(JLabel.BOTTOM);
    storyPhotoLabel.setFont(new Font("Serif", Font.ITALIC, 16));
    JScrollPane scrollStory = new JScrollPane(storyPhotoLabel);
    scrollStory.setPreferredSize(new Dimension(450, 200));
    scrollStory.setBorder(BorderFactory.createTitledBorder(" ✨ Your Smile Journey 🌸 "));
    supportSouth.add(scrollStory, BorderLayout.CENTER);

    JScrollPane scrollLog = new JScrollPane(logArea);
    scrollLog.setPreferredSize(new Dimension(450, 120));
    scrollLog.setBorder(BorderFactory.createTitledBorder("Smile Message"));
    supportSouth.add(scrollLog, BorderLayout.SOUTH);

    supportTab.add(form, BorderLayout.NORTH);
    supportTab.add(supportSouth, BorderLayout.CENTER);

    // Tab 2: My Page (UNICEFスタイル)
    JPanel myPageTab = new JPanel(new BorderLayout(10, 10));
    JPanel searchPanel = new JPanel(new GridLayout(2, 1));
    searchPanel.add(new JLabel("👤 Search Your Name:"));
    searchPanel.add(searchField);
    myPageTab.add(searchPanel, BorderLayout.NORTH);

    // 履歴エリアのみ残す
    JScrollPane scrollHistory = new JScrollPane(historyArea);
    scrollHistory.setBorder(BorderFactory.createTitledBorder(" 📜 History "));
    myPageTab.add(scrollHistory, BorderLayout.CENTER);

    tabs.addTab("✨ Give Support", supportTab);
    tabs.addTab("👤 My Page", myPageTab);
    mainPanel.add(tabs, BorderLayout.CENTER);
    cardPanel.add(mainPanel, "Input");
  }

  /**
   * 🌸 【新しい窓口】
   * ImageIconを使って写真と文字をセットします。
   */
  public void setStoryContent(String message, String photoPath) {
    try {
      java.net.URL imgUrl = getClass().getClassLoader().getResource(photoPath);
      if (imgUrl == null) {
        throw new Exception("Resource not found: " + photoPath);
      }
      ImageIcon icon = new ImageIcon(imgUrl);
      // 写真を横400ピクセルにリサイズ
      Image img = icon.getImage().getScaledInstance(400, 220, Image.SCALE_SMOOTH);
      storyPhotoLabel.setIcon(new ImageIcon(img));
      storyPhotoLabel.setText("<html><body style='width: 300px; text-align: center;'>" + message + "</body></html>");
    } catch (Exception e) {
      storyPhotoLabel.setIcon(null);
      storyPhotoLabel.setText(message + "\n(Image not found: " + photoPath + ")");
    }
  }

  public void showRandomImage(String message) {
    String[] images = {
        "images/alexia-luyt-5fJcSYUS5lQ-unsplash.jpg",
        "images/ali-mkumbwa-s8Kzx7C6yqo-unsplash.jpg",
        "images/ben-mullins-5QTQz-oYk1A-unsplash.jpg",
        "images/cdc-CCofbL9nLd8-unsplash.jpg",
        "images/cdc-oCvk4XaAEV0-unsplash.jpg",
        "images/dana-sarsenbekova-s4dOKRp86Ko-unsplash.jpg",
        "images/daniel-quiceno-m-4MQtWCxUrYc-unsplash.jpg",
        "images/emmanuel-ikwuegbu-OP3DdAHRbCs-unsplash.jpg",
        "images/iwaria-inc-M7ALc3UuX_g-unsplash.jpg",
        "images/md-duran-4k6kHq3k_rM-unsplash.jpg",
        "images/nathan-dumlao-kDxqbAvEBwI-unsplash.jpg",
        "images/noah-BQ5skcjX24o-unsplash.jpg",
        "images/rashid-sadykov-zJilmUpCMDw-unsplash.jpg"
    };
    String randomImg = images[new java.util.Random().nextInt(images.length)];
    
    try {
        java.net.URL imgUrl = getClass().getClassLoader().getResource(randomImg);
        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(imgUrl);
            Image img = icon.getImage().getScaledInstance(400, 220, Image.SCALE_SMOOTH);
            storyPhotoLabel.setIcon(new ImageIcon(img));
            storyPhotoLabel.setText(""); // 写真エリアには文字を出さない
        }
    } catch (Exception e) {
        storyPhotoLabel.setIcon(null);
    }
    
    logArea.setText(message);
  }

  // --- Controllerとの窓口 ---
  public JButton getStartButton() { return startButton; }
  public JButton getCreateButton() { return createButton; }
  public JTextField getSearchField() { return searchField; }
  public String getNameText() { return nameField.getText(); }
  public String getAmountText() { return amountField.getText(); }
  public String getSearchText() { return searchField.getText(); }
  public String getSelectedType() { return (String)typeBox.getSelectedItem(); }
  public JProgressBar getGoalBar() { return new JProgressBar(); } // エラー防止用ダミー
  public void setDisplayText(String t) { logArea.setText(t); }
  public void setHistoryText(String t) { historyArea.setText(t); }
  public void setGlobalTotalText(String t) { globalLabel.setText(t); }
  public void switchToInput() { cl.show(cardPanel, "Input"); }
  public void clearInputs() { nameField.setText(""); amountField.setText(""); }
}