package impact.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;

/**
 * ImpactView クラス (Humanitarian Final Edition - Perfect Screen Fit)
 * [MVC: View Layer]
 * 社会貢献の重みを感じさせるプレミアムデザインと、直感的な操作性を両立。
 * スクロール不要で、進捗から履歴まで一画面で確認できる完成版です。
 */
public class ImpactView extends JFrame {

  // --- 🌸 【フォトギャラリー用のファイルパス設定】 🌸 ---
  // images フォルダに写真を入れ、その名前をここに記載してください。
  private final String GALLERY_PATH_1 = "images/gallery_1.jpg";
  private final String GALLERY_PATH_2 = "images/gallery_2.jpg";
  private final String GALLERY_PATH_3 = "images/gallery_3.jpg";
  // ---------------------------------------------------

  // カラーパレット (Humanitarian Palette)
  private final Color SL_NAVY = new Color(30, 41, 59);
  private final Color SL_ROSE = new Color(244, 114, 114);
  private final Color BG_CREAM = new Color(252, 251, 247);
  private final Color BORDER_SOFT = new Color(226, 232, 240);
  private final Color TEXT_SLATE = new Color(71, 85, 105);
  private final Color FACT_BG = new Color(248, 250, 252);

  private CardLayout cardLayout = new CardLayout();
  private JPanel cardPanel = new JPanel(cardLayout);

  // 入力パーツ
  private JTextField fName = new JTextField(), lName = new JTextField(), amtField = new JTextField();
  private JTextField searchField = new JTextField(); // マイページ検索用

  private JButton donateToggle = new JButton("DONATION 🎁");
  private JButton microToggle = new JButton("MICROFINANCE 🤝");
  private String currentType = "DONATION";

  private JPanel missionGroup;
  private JComboBox<String> missionBox = new JComboBox<>(new String[]{
      "Agriculture 🚜", "Apparel 🧵", "Health 💉"
  });

  private JButton startBtn = new JButton("Empower a Future →");
  private JButton createBtn = new JButton("Share My Kindness ✨");
  private JButton viewDocBtn = new JButton("View Document ✨");
  private JButton navMyPageBtn = new JButton("My Journey 👤");
  private JButton backBtn = new JButton("← Back to Dashboard");

  private JTextArea historyArea = new JTextArea(), storyArea = new JTextArea();
  private JTextArea factArea = new JTextArea();
  private JTextArea recentActivityArea = new JTextArea();

  private JPanel photoArea = new JPanel(new BorderLayout());
  private JLabel imageLabel = new JLabel();

  // ダッシュボード・コンポーネント（表示の競合を防ぐため一箇所で管理）
  private JLabel globalTotalLabel = new JLabel("$0.00");
  private JProgressBar progressBar = new JProgressBar(0, 100);
  private JLabel progressPercentLabel = new JLabel("0%");

  public ImpactView() {
    setTitle("SmileLog | Building a Brighter Future");
    setExtendedState(MAXIMIZED_BOTH);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    setupWelcomeScreen();
    setupInputScreen();
    setupMyPageScreen();

    add(cardPanel);
    setupToggleLogic();
  }

  private void setupToggleLogic() {
    donateToggle.setFocusPainted(false);
    microToggle.setFocusPainted(false);
    Font toggleFont = new Font("SansSerif", Font.BOLD, 11);
    donateToggle.setFont(toggleFont);
    microToggle.setFont(toggleFont);
    updateToggleStyles();

    donateToggle.addActionListener(e -> {
      currentType = "DONATION";
      missionGroup.setVisible(false);
      updateToggleStyles();
      revalidate(); repaint();
    });
    microToggle.addActionListener(e -> {
      currentType = "MICROFINANCE";
      missionGroup.setVisible(true);
      updateToggleStyles();
      revalidate(); repaint();
    });
  }

  private void updateToggleStyles() {
    boolean isDonation = currentType.equals("DONATION");
    donateToggle.setBackground(isDonation ? SL_NAVY : Color.WHITE);
    donateToggle.setForeground(isDonation ? Color.WHITE : SL_NAVY);
    donateToggle.setOpaque(true);
    donateToggle.setBorder(new LineBorder(isDonation ? SL_NAVY : BORDER_SOFT));
    microToggle.setBackground(!isDonation ? SL_NAVY : Color.WHITE);
    microToggle.setForeground(!isDonation ? Color.WHITE : SL_NAVY);
    microToggle.setOpaque(true);
    microToggle.setBorder(new LineBorder(!isDonation ? SL_NAVY : BORDER_SOFT));
  }

  private void setupWelcomeScreen() {
    JPanel p = new JPanel(new GridBagLayout()); p.setBackground(Color.WHITE);
    GridBagConstraints gbc = new GridBagConstraints();
    JLabel title = new JLabel("SmileLog"); title.setFont(new Font("Serif", Font.BOLD, 100)); title.setForeground(SL_NAVY);
    startBtn.setFont(new Font("SansSerif", Font.BOLD, 22)); startBtn.setPreferredSize(new Dimension(400, 70));
    startBtn.setBackground(SL_ROSE); startBtn.setForeground(Color.WHITE); startBtn.setOpaque(true); startBtn.setBorderPainted(false);
    gbc.gridy = 0; p.add(title, gbc);
    gbc.gridy = 1; gbc.insets = new Insets(10, 0, 50, 0); p.add(new JLabel("For Every Smile, A Future."), gbc);
    gbc.gridy = 2; p.add(startBtn, gbc);
    cardPanel.add(p, "Welcome");
  }

  private JPanel createHeader(boolean showNav) {
    JPanel h = new JPanel(new BorderLayout()); h.setBackground(Color.WHITE);
    h.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,BORDER_SOFT), new EmptyBorder(10,50,10,50)));
    JLabel logo = new JLabel("SmileLog 🌸"); logo.setFont(new Font("Serif", Font.BOLD, 26)); logo.setForeground(SL_NAVY);
    JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)); right.setOpaque(false);
    if (showNav) {
      navMyPageBtn.setForeground(SL_ROSE); navMyPageBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
      navMyPageBtn.setContentAreaFilled(false); navMyPageBtn.setBorder(null);
      right.add(navMyPageBtn);
    }
    h.add(logo, BorderLayout.WEST); h.add(right, BorderLayout.EAST);
    return h;
  }

  private JPanel createDashboardProgressCard() {
    JPanel card = new JPanel(new BorderLayout(30, 0)); card.setBackground(Color.WHITE);
    card.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_SOFT, 1, true), new EmptyBorder(15, 35, 15, 35)));

    JPanel left = new JPanel(new BorderLayout(0, 8)); left.setOpaque(false);
    JLabel lbl = new JLabel("GLOBAL MISSION PROGRESS:"); lbl.setFont(new Font("SansSerif", Font.BOLD, 11)); lbl.setForeground(TEXT_SLATE);

    JPanel barPanel = new JPanel(new BorderLayout(15, 0)); barPanel.setOpaque(false);
    progressBar.setPreferredSize(new Dimension(300, 24)); progressBar.setForeground(new Color(59, 130, 246));
    progressBar.setBackground(new Color(241, 245, 249)); progressBar.setBorderPainted(false);
    progressPercentLabel.setFont(new Font("Serif", Font.BOLD, 28));
    barPanel.add(progressBar, BorderLayout.CENTER); barPanel.add(progressPercentLabel, BorderLayout.EAST);
    left.add(lbl, BorderLayout.NORTH); left.add(barPanel, BorderLayout.CENTER);

    globalTotalLabel.setFont(new Font("Serif", Font.BOLD, 44)); globalTotalLabel.setForeground(SL_NAVY);
    JPanel right = new JPanel(new BorderLayout()); right.setOpaque(false);
    JLabel rLbl = new JLabel("Raised Globally:", SwingConstants.RIGHT); rLbl.setFont(new Font("SansSerif", Font.BOLD, 11));
    right.add(rLbl, BorderLayout.NORTH); right.add(globalTotalLabel, BorderLayout.CENTER);

    card.add(left, BorderLayout.CENTER); card.add(right, BorderLayout.EAST);
    return card;
  }

  private JPanel createGalleryItem(String path, String caption) {
    JPanel item = new JPanel(new BorderLayout(0, 5)); item.setOpaque(false);
    JLabel img = new JLabel(); img.setPreferredSize(new Dimension(150, 90));
    img.setBorder(new LineBorder(BORDER_SOFT)); img.setHorizontalAlignment(SwingConstants.CENTER);
    File f = new File(path);
    if (f.exists()) {
      img.setIcon(new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(150, 90, Image.SCALE_SMOOTH)));
    } else {
      img.setText("📷 Photo Slot"); img.setFont(new Font("SansSerif", Font.PLAIN, 9));
      img.setBackground(Color.WHITE); img.setOpaque(true);
    }
    JLabel cap = new JLabel(caption, SwingConstants.CENTER); cap.setFont(new Font("SansSerif", Font.PLAIN, 10));
    cap.setForeground(TEXT_SLATE); item.add(img, BorderLayout.CENTER); item.add(cap, BorderLayout.SOUTH);
    return item;
  }

  private void setupInputScreen() {
    JPanel p = new JPanel(new BorderLayout()); p.setBackground(BG_CREAM);
    p.add(createHeader(true), BorderLayout.NORTH);

    JPanel body = new JPanel(new GridBagLayout()); body.setOpaque(false);
    body.setBorder(new EmptyBorder(15, 60, 15, 60));
    GridBagConstraints mainGbc = new GridBagConstraints();
    mainGbc.fill = GridBagConstraints.BOTH;

    // 1. ダッシュボード最上部
    GridBagConstraints topGbc = new GridBagConstraints();
    topGbc.gridx = 0; topGbc.gridy = 0; topGbc.gridwidth = 2; topGbc.weightx = 1.0;
    topGbc.fill = GridBagConstraints.HORIZONTAL; topGbc.insets = new Insets(0, 0, 20, 0);
    body.add(createDashboardProgressCard(), topGbc);

    // 2. 左側: 視覚的な報告とギャラリー
    JPanel leftSide = new JPanel(new GridBagLayout()); leftSide.setOpaque(false);
    GridBagConstraints lGbc = new GridBagConstraints();
    lGbc.fill = GridBagConstraints.HORIZONTAL; lGbc.weightx = 1.0; lGbc.gridx = 0; lGbc.anchor = GridBagConstraints.NORTH;

    photoArea.setBackground(Color.WHITE); photoArea.setBorder(new LineBorder(BORDER_SOFT));
    photoArea.setPreferredSize(new Dimension(500, 300));
    setInitialState();

    JPanel insightCard = new JPanel(new BorderLayout(0, 15)); insightCard.setBackground(Color.WHITE);
    insightCard.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_SOFT, 1, true), new EmptyBorder(18, 22, 18, 22)));
    storyArea.setEditable(false); storyArea.setLineWrap(true); storyArea.setWrapStyleWord(true);
    storyArea.setFont(new Font("Serif", Font.PLAIN, 20)); storyArea.setForeground(TEXT_SLATE); storyArea.setOpaque(false);
    factArea.setEditable(false); factArea.setLineWrap(true); factArea.setWrapStyleWord(true);
    factArea.setBackground(FACT_BG); factArea.setFont(new Font("SansSerif", Font.ITALIC, 14));
    JPanel factBox = new JPanel(new BorderLayout(8, 8)); factBox.setBackground(FACT_BG);
    factBox.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(230, 235, 245), 1, true), new EmptyBorder(12, 15, 12, 15)));
    JLabel ft = new JLabel("💡 DID YOU KNOW?"); ft.setFont(new Font("SansSerif", Font.BOLD, 10));
    factBox.add(ft, BorderLayout.NORTH); factBox.add(factArea, BorderLayout.CENTER);
    insightCard.add(storyArea, BorderLayout.NORTH); insightCard.add(factBox, BorderLayout.CENTER);

    JPanel galleryPanel = new JPanel(new GridLayout(1, 3, 15, 0)); galleryPanel.setOpaque(false);
    galleryPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
    galleryPanel.add(createGalleryItem(GALLERY_PATH_1, "Classroom Visit"));
    galleryPanel.add(createGalleryItem(GALLERY_PATH_2, "Medical Supplies"));
    galleryPanel.add(createGalleryItem(GALLERY_PATH_3, "Future Tailor"));

    lGbc.gridy = 0; lGbc.insets = new Insets(0, 0, 15, 0); leftSide.add(photoArea, lGbc);
    lGbc.gridy = 1; lGbc.insets = new Insets(0, 0, 0, 0); leftSide.add(insightCard, lGbc);
    lGbc.gridy = 2; leftSide.add(galleryPanel, lGbc);
    lGbc.gridy = 3; lGbc.weighty = 1.0; leftSide.add(new Box.Filler(new Dimension(0,0), new Dimension(0,0), new Dimension(0, 0)), lGbc);

    // 3. 右側: 支援フォームと最新履歴
    JPanel rightSide = new JPanel(new GridBagLayout()); rightSide.setOpaque(false);
    GridBagConstraints rGbc = new GridBagConstraints();
    rGbc.fill = GridBagConstraints.HORIZONTAL; rGbc.weightx = 1.0; rGbc.gridx = 0; rGbc.anchor = GridBagConstraints.NORTH;
    JPanel inputCard = new JPanel(new BorderLayout(0, 15)); inputCard.setBackground(Color.WHITE);
    inputCard.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_SOFT, 1, true), new EmptyBorder(25,35,25,35)));

    JPanel fds = new JPanel(new GridBagLayout()); fds.setOpaque(false);
    GridBagConstraints g = new GridBagConstraints(); g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0; g.gridx = 0; g.gridy = 0;
    JPanel tp = new JPanel(new GridLayout(1, 2, 10, 0)); tp.setOpaque(false);
    donateToggle.setPreferredSize(new Dimension(140, 48)); microToggle.setPreferredSize(new Dimension(140, 48));
    tp.add(donateToggle); tp.add(microToggle); fds.add(tp, g); g.gridy++; g.insets = new Insets(15,0,0,0);
    missionGroup = new JPanel(new BorderLayout(0, 5)); missionGroup.setOpaque(false);
    missionGroup.add(new JLabel("SELECT MISSION SECTOR"), BorderLayout.NORTH);
    missionGroup.add(missionBox, BorderLayout.CENTER); missionGroup.setVisible(false);
    fds.add(missionGroup, g); g.gridy++;
    fds.add(createInputGroup("FIRST NAME", fName), g); g.gridy++;
    fds.add(createInputGroup("LAST NAME", lName), g); g.gridy++;
    fds.add(createInputGroup("GIFT AMOUNT ($)", amtField), g);
    createBtn.setBackground(SL_NAVY); createBtn.setForeground(Color.WHITE); createBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
    createBtn.setPreferredSize(new Dimension(250, 60)); createBtn.setOpaque(true); createBtn.setBorderPainted(false);
    inputCard.add(new JLabel("<html><h3 style='color:#1e293b; font-family:serif; margin-bottom:5px;'>Join the Mission</h3></html>"), BorderLayout.NORTH);
    inputCard.add(fds, BorderLayout.CENTER); inputCard.add(createBtn, BorderLayout.SOUTH);

    viewDocBtn.setVisible(false); viewDocBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
    viewDocBtn.setBackground(Color.WHITE); viewDocBtn.setBorder(new LineBorder(SL_ROSE)); viewDocBtn.setPreferredSize(new Dimension(250, 48));
    JPanel dw = new JPanel(new BorderLayout()); dw.setOpaque(false); dw.setBorder(new EmptyBorder(10, 0, 0, 0)); dw.add(viewDocBtn, BorderLayout.CENTER);

    recentActivityArea.setEditable(false); recentActivityArea.setBackground(new Color(0,0,0,0)); recentActivityArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    JPanel qh = new JPanel(new BorderLayout(0, 10)); qh.setOpaque(false); qh.setBorder(new EmptyBorder(20, 0, 0, 0));
    qh.add(new JLabel("RECENT COMMUNITY ACTIVITY"), BorderLayout.NORTH); qh.add(recentActivityArea, BorderLayout.CENTER);

    rGbc.gridy = 0; rightSide.add(inputCard, rGbc);
    rGbc.gridy = 1; rightSide.add(dw, rGbc);
    rGbc.gridy = 2; rightSide.add(qh, rGbc);
    rGbc.gridy = 3; rGbc.weighty = 1.0; rightSide.add(new Box.Filler(new Dimension(0,0), new Dimension(0,0), new Dimension(0, 0)), rGbc);

    mainGbc.gridy = 1; mainGbc.weighty = 1.0;
    mainGbc.weightx = 0.50; mainGbc.gridx = 0; body.add(leftSide, mainGbc);
    mainGbc.weightx = 0.50; mainGbc.gridx = 1; mainGbc.insets = new Insets(0, 30, 0, 0); body.add(rightSide, mainGbc);

    p.add(body, BorderLayout.CENTER);
    cardPanel.add(p, "Input");
  }

  private void setInitialState() {
    photoArea.removeAll();
    JPanel wc = new JPanel(new GridBagLayout()); wc.setBackground(new Color(255, 245, 247));
    JLabel t = new JLabel("✨ Impact Dashboard"); t.setFont(new Font("Serif", Font.BOLD, 26));
    wc.add(t); photoArea.add(wc, BorderLayout.CENTER);
    storyArea.setText("Every contribution writes a life story. Choose a mission above to begin.");
    factArea.setText("Every $1 invested in childhood education yields $16 in local economic returns.");
  }

  private void setupMyPageScreen() {
    JPanel p = new JPanel(new BorderLayout()); p.setBackground(BG_CREAM);
    p.add(createHeader(false), BorderLayout.NORTH);
    JPanel content = new JPanel(new GridBagLayout()); content.setOpaque(false);
    content.setBorder(new EmptyBorder(20, 80, 40, 80));
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1.0; c.gridx = 0;

    JPanel topNav = new JPanel(new BorderLayout()); topNav.setOpaque(false);
    backBtn.setFont(new Font("SansSerif", Font.BOLD, 14)); backBtn.setForeground(SL_NAVY);
    topNav.add(backBtn, BorderLayout.WEST);
    JLabel title = new JLabel("Your Impact Legacy", SwingConstants.CENTER); title.setFont(new Font("Serif", Font.BOLD, 32));
    topNav.add(title, BorderLayout.CENTER);
    c.gridy = 0; c.insets = new Insets(0, 0, 25, 0); content.add(topNav, c);

    JPanel searchPanel = new JPanel(new BorderLayout(15, 0)); searchPanel.setBackground(Color.WHITE);
    searchPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_SOFT, 1, true), new EmptyBorder(15, 25, 15, 25)));
    JLabel sLbl = new JLabel("Enter Donor Name:"); sLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
    searchField.setPreferredSize(new Dimension(300, 40)); searchField.setFont(new Font("SansSerif", Font.PLAIN, 16));
    searchField.setBorder(new LineBorder(BORDER_SOFT));
    searchPanel.add(sLbl, BorderLayout.WEST); searchPanel.add(searchField, BorderLayout.CENTER);
    JLabel hint = new JLabel("Press Enter to Search 🔍"); hint.setFont(new Font("SansSerif", Font.ITALIC, 11));
    searchPanel.add(hint, BorderLayout.EAST);
    c.gridy = 1; c.insets = new Insets(0, 0, 20, 0); content.add(searchPanel, c);

    JPanel historyCard = new JPanel(new BorderLayout()); historyCard.setBackground(Color.WHITE);
    historyCard.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_SOFT, 1, true), new EmptyBorder(20, 20, 20, 20)));
    historyArea.setEditable(false); historyArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
    historyArea.setForeground(TEXT_SLATE);
    JScrollPane scroll = new JScrollPane(historyArea); scroll.setBorder(null);
    scroll.setPreferredSize(new Dimension(0, 350));
    historyCard.add(scroll, BorderLayout.CENTER);
    c.gridy = 2; c.weighty = 1.0; c.fill = GridBagConstraints.BOTH; content.add(historyCard, c);

    p.add(content, BorderLayout.CENTER);
    cardPanel.add(p, "MyPage");
  }

  private JPanel createInputGroup(String l, JTextField f) {
    JPanel g = new JPanel(new BorderLayout(0, 5)); g.setOpaque(false);
    JLabel lbl = new JLabel(l); lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
    g.add(lbl, BorderLayout.NORTH); f.setPreferredSize(new Dimension(0, 42)); f.setBorder(new LineBorder(BORDER_SOFT));
    f.setFont(new Font("SansSerif", Font.PLAIN, 16));
    g.add(f, BorderLayout.CENTER); g.setBorder(new EmptyBorder(8, 0, 0, 0));
    return g;
  }

  public void showDocumentModal(String title, String text) {
    JTextArea area = new JTextArea(text); area.setFont(new Font("Monospaced", Font.PLAIN, 14)); area.setEditable(false); area.setMargin(new Insets(25, 25, 25, 25));
    JOptionPane.showMessageDialog(this, new JScrollPane(area), title, JOptionPane.PLAIN_MESSAGE);
  }

  public void setStoryContent(String story, String imagePath, String fact, boolean showBtn, String btnText) {
    storyArea.setText(story); factArea.setText(fact);
    viewDocBtn.setText(btnText); viewDocBtn.setVisible(showBtn);
    photoArea.removeAll();
    try {
      File f = new File(imagePath);
      if (f.exists()) {
        ImageIcon icon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(500, 300, Image.SCALE_SMOOTH));
        imageLabel.setIcon(icon); photoArea.add(imageLabel, BorderLayout.CENTER);
      } else {
        JLabel pl = new JLabel("<html><div style='text-align:center;'>Mission Activity Visualized...</div></html>");
        pl.setHorizontalAlignment(SwingConstants.CENTER); photoArea.add(pl, BorderLayout.CENTER);
      }
    } catch (Exception e) {}
    photoArea.revalidate(); photoArea.repaint();
  }

  public void updateRecentActivity(String text) { recentActivityArea.setText(text); }
  public String getSelectedType() { return currentType; }
  public String getSelectedMission() { return missionBox.getSelectedItem().toString(); }
  public String getFirstNameText() { return fName.getText(); }
  public String getLastNameText() { return lName.getText(); }
  public String getAmountText() { return amtField.getText(); }
  public String getSearchText() { return searchField.getText(); }

  /** [NEW] 検索欄に名前をセットするためのメソッド */
  public void setSearchText(String t) { searchField.setText(t); }

  public JButton getStartButton() { return startBtn; }
  public JButton getCreateButton() { return createBtn; }
  public JButton getViewDocumentButton() { return viewDocBtn; }
  public JButton getNavMyPageButton() { return navMyPageBtn; }
  public JButton getBackToDonateButton() { return backBtn; }
  public JTextField getSearchField() { return searchField; }
  public void updateProgress(int v) { progressBar.setValue(v); progressPercentLabel.setText(v + "%"); }
  public void setGlobalTotalText(String t) { globalTotalLabel.setText(t.replace("Raised: ", "")); }
  public void setHistoryText(String t) { historyArea.setText(t); }
  public void switchToInput() { cardLayout.show(cardPanel, "Input"); }
  public void switchToMyPage() { cardLayout.show(cardPanel, "MyPage"); }
  public void clearInputs() { fName.setText(""); lName.setText(""); amtField.setText(""); }
}