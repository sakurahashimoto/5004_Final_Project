package impact.data;

/**
 * Class that has list of warm messages and getRandomMessage() picks on only one line of
 * message and return formatted message
 */

public class ImpactMessages {

  private static final String[] WARM_MESSAGES = {
      ", your kindness is making the world a brighter place! ✨",
      ", thank you! A child has a reason to smile today because of you. 🌸",
      ", every dollar you give creates a ripple of hope. ❤️",
      ", you're not just giving money; you're giving a future. 🌟",
      ", small acts of kindness like yours change lives. Thank you! 🌊",
      ", thank you for being a hero for someone today! 🦸‍♀️",
      ", your support has been safely received. You're amazing! 🕊️"
  };

  /**
   * Created random warm messages
   */
  public static String getRandomMessage(String name, double amount) {
    //Mat.random return 0 to 0.99
    //int 4.5 -> 4
    int index = (int) (Math.random() * WARM_MESSAGES.length);
    String heartMsg = WARM_MESSAGES[index];

    // [SUCCESS] の後に、名前、ランダムメッセージ、そして最後にしっかり金額の記録を表示
    return String.format(" [SUCCESS] %s%s (Recorded: $%,.2f) ✨",
        name, heartMsg, amount);
  }
}