package ru.tbank.converter.services

import ru.tbank.converter.models.Money
import ru.tbank.converter.myExceptions.Errors.{
  SameCurrencyExchangeException,
  UnsupportedCurrencyException
}

class CurrencyConverter(ratesDictionary: Map[String, Map[String, BigDecimal]]) {
  def exchange(money: Money, toCurrency: String): Money = {
    if (money.currency == toCurrency || money.currency.equals(toCurrency)) {
      throw new SameCurrencyExchangeException
    }

    val rate = getRate(money, toCurrency)

    val newAmount = money.amount * rate
    Money(newAmount, toCurrency)
  }

  private def getRate(money: Money, toCurrency: String): BigDecimal = {
    val ratesForCurrency = ratesDictionary.getOrElse(
      money.currency,
      throw new UnsupportedCurrencyException
    )

    val rate = ratesForCurrency.getOrElse(
      toCurrency,
      throw new UnsupportedCurrencyException
    )

    rate
  }
}

object CurrencyConverter {

  import ru.tbank.converter.models.Currencies.SupportedCurrencies

  def apply(ratesDictionary: Map[String, Map[String, BigDecimal]]): CurrencyConverter = {
    val fromCurrencies = ratesDictionary.keys
    val toCurrencies = ratesDictionary.values
    if (
      fromCurrencies.toSet
        .subsetOf(SupportedCurrencies) && toCurrencies.forall(_.keys.toSet.subsetOf(SupportedCurrencies))
    ) new CurrencyConverter(ratesDictionary)
    else throw new UnsupportedCurrencyException
  }
}
