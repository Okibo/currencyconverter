package pl.portfolio.currencyconverter.api


import pl.portfolio.currencyconverter.models.CurrencyModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyAPI {

    @GET("/{day}")
    suspend fun getHistoricalCurrencyExchange(
        @Path("day")
        day: String,
        @Query("base")
        base: String
    ): Response<CurrencyModel>

    @GET("/latest")
    suspend fun getLatestCurrencyExchangeForBase(
        @Query("base")
        base: String
    ): Response<CurrencyModel>

    @GET("/latest")
    suspend fun getLatestCurrencyExchange(): Response<CurrencyModel>

}