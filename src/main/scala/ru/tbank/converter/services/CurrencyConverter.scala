package ru.tbank.converter.services

import ru.tbank.converter.models.Money
import ru.tbank.converter.myExceptions.Errors.UnsupportedCurrencyException

class CurrencyConverter(ratesDictionary: Map[String, Map[String, BigDecimal]]) {
  def exchange(money: Money, toCurrency: String): Money = ???
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
