package ru.tbank.converter.models

import ru.tbank.converter.myExceptions.Errors.{CurrencyMismatchException, MoneyAmountShouldBeNonNegativeException, UnsupportedCurrencyException}

import scala.annotation.targetName

case class Money private (amount: BigDecimal, currency: String) {

  @targetName("addMoney")
  def +(other: Money): Money = {
    checkIsCurrenciesAreSame(other)

    new Money(this.amount.+(other.amount), this.currency)
  }

  @targetName("subtractMoney")
  def -(other: Money): Money = {
    if (!isValidToDivideCurrency(other.amount)) {
      throw new MoneyAmountShouldBeNonNegativeException
    }
    checkIsCurrenciesAreSame(other)

    new Money(this.amount.-(other.amount), this.currency)
  }

  private def isValidToDivideCurrency(other: BigDecimal): Boolean = {
    amount.-(other) > 0
  }

  private def checkIsCurrenciesAreSame(other: Money): Unit = {
    if (!isSameCurrency(other)) {
      throw new CurrencyMismatchException
    }
  }

  def isSameCurrency(other: Money): Boolean = {
    this.currency.equals(other.currency)
  }
}

object Money {
  def apply(amount: BigDecimal, currency: String): Money = {
    if (amount < 0) {
      throw new MoneyAmountShouldBeNonNegativeException
    } else if (!Currencies.SupportedCurrencies.contains(currency)) {
      throw new UnsupportedCurrencyException
    }

    new Money(amount, currency)
  }
}
