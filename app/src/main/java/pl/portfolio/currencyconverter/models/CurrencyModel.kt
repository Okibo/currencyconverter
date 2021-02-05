package pl.portfolio.currencyconverter.models

data class CurrencyModel(
    val base: String,
    val date: String,
    val rates: Map<String,Double>
)