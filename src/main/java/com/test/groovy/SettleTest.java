package com.test.groovy;

public class SettleTest {

  private Long account;

  private Integer type;

  private Double amount;

  public SettleTest(Long account, Integer type, Double amount) {
    this.account = account;
    this.type = type;
    this.amount = amount;
  }

  public Long getAccount() {
    return account;
  }

  public void setAccount(Long account) {
    this.account = account;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public String toString() {
    return account + " ==> type:" + type + " ==> amount:" + amount;
  }
}
