package pl.portfolio.currencyconverter.api


import pl.portfolio.currencyconverter.models.CurrencyModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyAPI {

    //http://data.fixer.io/api/latest?access_key=9d7c955c24a61fe71019ce42c251756e

//    @GET("api/{day}")
//    suspend fun getHistoricalCurrencyExchange(
//        @Path("day")
//        day: String,
//        @Query("access_key")
//        access_key: String = API_KEY
//    )
//
//    @GET("api/latest")
//    suspend fun getLatestCurrencyExchange(
//        @Query("access_key")
//        access_key: String = API_KEY
//    ): Response<CurrencyResponse>
//

    @GET("/latest")
    suspend fun getLatestCurrencyExchangeForBase(
        @Query("base")
        base: String
    ): Response<CurrencyModel>

    @GET("/latest")
    suspend fun getLatestCurrencyExchange(): Response<CurrencyModel>

}