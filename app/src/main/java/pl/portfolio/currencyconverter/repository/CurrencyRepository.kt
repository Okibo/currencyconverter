package pl.portfolio.currencyconverter.repository

import pl.portfolio.currencyconverter.api.RetrofitInstance

class CurrencyRepository {

    suspend fun getLatestCurrencyExchange() =
        RetrofitInstance.api.getLatestCurrencyExchange()

    suspend fun getLatestCurrencyExchangeForBase(base: String) =
        RetrofitInstance.api.getLatestCurrencyExchangeForBase(base)

    suspend fun  getHistoricalCurrencyExchange(day: String, base: String) =
        RetrofitInstance.api.getHistoricalCurrencyExchange(day, base)
}