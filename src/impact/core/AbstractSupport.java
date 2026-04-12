package impact.core;

public abstract class AbstractSupport implements Supportable {
  protected double amount;
  protected String donorName;

  public AbstractSupport(double amount, String donorName) {
    this.amount = amount;
    this.donorName = donorName;
  }

  @Override
  public double getAmount() {
    return amount;
  }

  @Override
  public String getDonorName() {
    return donorName;
  }
  // 【抽象メソッド】中身はDonationやMicrofinanceに「自分たちのルール」で書かせます
  @Override
  public abstract String getImpactMessage();

  // 【追加】ここがクラスの中に閉じている必要があります🌸
  @Override
  public abstract int getPeopleHelped();
}
