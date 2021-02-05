package pl.portfolio.currencyconverter.repository

import pl.portfolio.currencyconverter.api.RetrofitInstance

class CurrencyRepository {

    suspend fun getLatestCurrencyExchange() =
        RetrofitInstance.api.getLatestCurrencyExchange()
}